package com.mes.processes;


import com.alibaba.fastjson.JSON;
import com.mes.manage.GetLogin;
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

/**
 * @author 10626
 */
@WebServlet("/GetProcesses")
public class GetProcesses extends HttpServlet {
    private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public GetProcesses() {
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
        JSON json =null;
        try {
            new dbConnector();
            Connection connect = dbConnector.getConnection();
            String sql;
            PreparedStatement ps;
            ResultSet rs;
            if("byoperator".equals(request.getParameter("search"))){
                sql = "SELECT * FROM `processes` LEFT JOIN `tags` on processes.tag_id=tags.id WHERE `operator`= ? AND `status`=1 ORDER BY finished_time desc";
                ps = connect.prepareStatement(sql);
                ps.setString(1,request.getParameter("operator"));
                rs = ps.executeQuery();
                json = rsToJSON.resultSetToJSON(rs,Integer.parseInt(request.getParameter("page")),Integer.parseInt(request.getParameter("limit")));

            }
            else if("bytag".equals(request.getParameter("search"))){
                sql = "SELECT * FROM `processes` LEFT JOIN `tags` on processes.tag_id=tags.id WHERE `tag_id`= ? AND `status`=1 ORDER BY finished_time desc ";
                ps = connect.prepareStatement(sql);
                ps.setInt(1, Integer.parseInt(request.getParameter("tag")));
                rs = ps.executeQuery();
                json = rsToJSON.resultSetToJSON(rs,Integer.parseInt(request.getParameter("page")),Integer.parseInt(request.getParameter("limit")));

            }else{
                sql = "SELECT * FROM `processes` LEFT JOIN `tags` on processes.tag_id=tags.id WHERE `status`=1 ORDER BY finished_time desc";
                ps = connect.prepareStatement(sql);
                rs = ps.executeQuery();
                json = rsToJSON.resultSetToJSON(rs,Integer.parseInt(request.getParameter("page")),Integer.parseInt(request.getParameter("limit")));
            }





            // 输出数据
            out = response.getWriter();

            out.println(json);
            // 完成后关闭
            rs.close();
            ps.close();
            connect.close();
        }catch (Exception e) {
            out.print(rsToJSON.getError());
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


