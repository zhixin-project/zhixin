package com.mes.products;


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

/**
 * @author 10626
 */
@WebServlet("/ReduceStock")
public class ReduceStock extends HttpServlet {
    private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public ReduceStock() {
        super();
    }

    @Override
    public void init() throws ServletException {

    }

    public static void reduce(int id, int amount) {
        try {
            new dbConnector();
            Connection connect = dbConnector.getConnection();
            String sql;
            sql = "UPDATE `products` SET `kucun`=kucun-? WHERE (`id`=?)";
            PreparedStatement ps = connect.prepareStatement(sql);
            ps.setInt(1, amount);
            ps.setInt(2, id);
            ps.executeUpdate();

            // 完成后关闭
            ps.close();
            connect.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/json; charset=utf-8");
        PrintWriter out = response.getWriter();
        if (!GetLogin.getStat(request, response)) {
            out.print(rsToJSON.getErrorNoLogin());
            return;
        }
        request.setCharacterEncoding("utf-8");

        try {
            reduce(Integer.parseInt(request.getParameter("id")),Integer.parseInt(request.getParameter("amount")));

            // 输出数据
            out = response.getWriter();

            out.println("success");
            // 完成后关闭
        } catch (Exception e) {
            out.print("fail");
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


