package com.mes.workers;


import com.alibaba.fastjson.JSON;
import tools.dbConnector;
import tools.rsToJSON;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author 10626
 */
@WebServlet("/FinishProcesses")
public class FinishProcesses extends HttpServlet {
    private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public FinishProcesses() {
        super();
    }

    @Override
    public void init() throws ServletException {

    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/json; charset=utf-8");
        PrintWriter out = response.getWriter();


        try {
            new dbConnector();
            Connection connect = dbConnector.getConnection();
            String sql;
            sql = "SELECT * FROM `orders` WHERE orders.id = ?";
            PreparedStatement ps = connect.prepareStatement(sql);
            ps.setString(1, request.getParameter("id"));
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                int i = rs.getInt("curr_step");
                int total=rs.getInt("total_step");
                if (i == total) {
                    if (rs.getInt("started_production") == 0) {
                        out.print("该订单未开始生产！");
                    } else {
                        out.print("该订单已完成！");
                    }
                } else {
                    HttpSession s = request.getSession(true);
                    if (s.getAttribute("WorkerIsLogin") == null || !(Boolean) s.getAttribute("WorkerIsLogin")) {
                        out.println("请登录！");
                    } else {
                        int tag_id = Integer.parseInt(s.getAttribute("tag").toString());
                        i++;
                        sql = "SELECT * FROM `processes` WHERE processes.id = ? AND sequence=?";
                        ps = connect.prepareStatement(sql);
                        ps.setString(1, request.getParameter("id"));
                        ps.setInt(2, i);
                        rs = ps.executeQuery();
                        rs.next();
                        if (tag_id == rs.getInt("tag_id")) {
                            if (rs.getInt("status") == 0) {
                                sql = "UPDATE `processes` SET `status`='1', `operator`=?, `finished_time`=? WHERE (`id`=?) AND (`tag_id`=?)";
                                ps = connect.prepareStatement(sql);
                                ps.setInt(1, Integer.parseInt(s.getAttribute("id").toString()));
                                Date date = new Date();
                                DateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                String punchTime = simpleDateFormat.format(date);
                                ps.setString(2, punchTime);
                                ps.setString(3, request.getParameter("id"));
                                ps.setInt(4, tag_id);
                                ps.executeUpdate();
                                sql = "UPDATE `orders` SET `curr_step`= ? WHERE (`id`=?)";
                                ps = connect.prepareStatement(sql);
                                ps.setInt(1, i);
                                ps.setString(2, request.getParameter("id"));
                                ps.executeUpdate();
                                if(i==total){
                                    sql = "UPDATE `orders` SET `finish_time`=?WHERE (`id`=?)";
                                    ps = connect.prepareStatement(sql);
                                    ps.setString(1, punchTime);
                                    ps.setString(2, request.getParameter("id"));
                                    ps.executeUpdate();
                                }
                                out.print("操作成功！");
                            }
                        } else {
                            out.print("顺序错误、已完成或无此工序！");
                        }
                    }
                }
            } else {
                out.print("无效订单！");
            }

            // 完成后关闭
            rs.close();
            ps.close();
            connect.close();
        } catch (Exception e) {
            out.print(rsToJSON.getError());
            e.printStackTrace();
        }
    }

    /**
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // TODO Auto-generated method stub
        doGet(request, response);
    }
}


