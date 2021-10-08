package com.mes.manage;

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
 * 登录界面的业务逻辑判断user和password 然后将IsLogin标签置为true或者返回错误
 * @author 10626
 */
@WebServlet("/Login")

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
        user = request.getParameter("user");
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
            sql = "SELECT * FROM `manage` WHERE user = ?";
            PreparedStatement ps = connect.prepareStatement(sql);
            ps.setString(1, request.getParameter("user"));
            ResultSet rs = ps.executeQuery();
            rs.next();
            passwd=rs.getString(2);
            // 完成后关闭
            rs.close();
            ps.close();
            connect.close();
        }catch (Exception e) {
            out.print(rsToJSON.getError());
            e.printStackTrace(); }



        if (password.equals(passwd)) {
            //  通过IsLogin标签判断是否登录成功
            // 保存user 用户名
            s.setAttribute("IsLogin", true);
            s.setAttribute("user", user);
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

