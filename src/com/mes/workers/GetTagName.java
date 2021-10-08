package com.mes.workers;

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

/**
 * 得到工序得名字  传入工序id
 * @author 10626
 */
@WebServlet("/GetTagName")

public class GetTagName extends HttpServlet {
    private static final long serialVersionUID = 1L;


    /**
     * @see HttpServlet#HttpServlet()
     */
    public GetTagName() {
        super();
    }
    @Override
    public void init() throws ServletException {

    }
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/json; charset=utf-8");
        PrintWriter out = response.getWriter();
        HttpSession s = request.getSession(true);
        if (s.getAttribute("WorkerIsLogin")==null||!(Boolean) s.getAttribute("WorkerIsLogin")) {
            out.println("暂未登录");
        } else {
            String name=null;

            try {
                new dbConnector();
                Connection connect = dbConnector.getConnection();
                String sql;
                sql = "SELECT name FROM `tags` WHERE id = ?";
                PreparedStatement ps = connect.prepareStatement(sql);
                ps.setInt(1, Integer.parseInt(s.getAttribute("tag").toString()));
                ResultSet rs = ps.executeQuery();
                rs.next();
                name=rs.getString(1);
                // 完成后关闭
                rs.close();
                ps.close();
                connect.close();
            }catch (Exception e) {
                out.print(rsToJSON.getError());
                e.printStackTrace(); }
            out.println(name);
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

    public Boolean getStat(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/json; charset=utf-8");
        HttpSession s = request.getSession(true);
        return s.getAttribute("IsLogin") != null && (Boolean) s.getAttribute("IsLogin");
    }
}

