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

/**
 * @author 10626
 */
@WebServlet("/WorkerLogin")

public class Login extends HttpServlet {
    private static final long serialVersionUID = 1L;


    String user;
    String password;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public Login() {
        super();
    }
    @Override
    public void init() throws ServletException {

    }
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        user = request.getParameter("id");
        password = request.getParameter("password");
        String passwd = null,name = null;
        response.setContentType("text/json; charset=utf-8");
        request.setCharacterEncoding("utf-8");
        PrintWriter out = response.getWriter();
        HttpSession s = request.getSession(true);
        try {
            new dbConnector();
            Connection connect = dbConnector.getConnection();
            String sql;
            sql = "SELECT * FROM `workers` WHERE id = ?";
            PreparedStatement ps = connect.prepareStatement(sql);
            ps.setInt(1, Integer.parseInt(request.getParameter("id")));
            ResultSet rs = ps.executeQuery();
            rs.next();
            name=rs.getString(2);
            passwd=rs.getString(3);
            // 完成后关闭
            rs.close();
            ps.close();
            connect.close();
        }catch (Exception e) {
            out.print(rsToJSON.getError());
            e.printStackTrace(); }



        if (password.equals(passwd)) {
            s.setAttribute("WorkerIsLogin", true);
            s.setAttribute("name", name);
            s.setAttribute("id",Integer.parseInt(request.getParameter("id")));
            s.setAttribute("tag",Integer.parseInt(request.getParameter("tag")));
            out.println("login_success");
        } else {
            out.println("用户名或密码错误！");
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

