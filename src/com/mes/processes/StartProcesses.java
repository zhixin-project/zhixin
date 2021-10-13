package com.mes.processes;


import com.alibaba.fastjson.JSONObject;
import com.mes.manage.GetLogin;
import tools.ReadAsChars;
import tools.dbConnector;
import tools.rsToJSON;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @author 10626
 */
@WebServlet("/StartProcesses")
public class StartProcesses extends HttpServlet {
    private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public StartProcesses() {
        super();
    }
    @Override
    public void init() throws ServletException {

    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/json; charset=utf-8");
        PrintWriter out = response.getWriter();
        if(!GetLogin.getStat(request,response)){
            out.print(rsToJSON.getErrorNoLogin());
            return;
        }
        request.setCharacterEncoding("utf-8");
        String js = ReadAsChars.read(request);
        JSONObject json = JSONObject.parseObject(js);

        try {
            new dbConnector();
            Connection connect = dbConnector.getConnection();
            String sql;
            sql = "SELECT tag_id,orders.product_id FROM `orders` LEFT JOIN `tag_relates` on orders.product_id=tag_relates.product_id WHERE `id`= ? ORDER BY sequence";
            PreparedStatement ps = connect.prepareStatement(sql);
            ps.setString(1, request.getParameter("id"));
            ResultSet rs =ps.executeQuery();
            int i=1;
            while(rs.next()){
                int tagId =rs.getInt(1);
                sql="INSERT INTO `processes` (`id`,`tag_id`,`sequence`) VALUES (?,?,?)";
                ps = connect.prepareStatement(sql);
                ps.setString(1, request.getParameter("id"));
                ps.setInt(2, tagId);
                ps.setInt(3,i++);
                ps.executeUpdate();
            }
            // 输出数据
            sql="UPDATE `orders` SET `started_production`='1', `total_step`=? WHERE (`id`=?)";
            ps = connect.prepareStatement(sql);
            ps.setInt(1,--i);
            ps.setString(2, request.getParameter("id"));
            ps.executeUpdate();
            rs.first();
            int productId =rs.getInt(2);
            sql="UPDATE `products` SET `used`='1' WHERE (`id`=?)";
            ps = connect.prepareStatement(sql);
            ps.setInt(1,productId);
            ps.executeUpdate();
            out = response.getWriter();

            out.println("success");
            // 完成后关闭
            ps.close();
            connect.close();
        }catch (Exception e) {
            out.print("fail");
            e.printStackTrace(); }
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


