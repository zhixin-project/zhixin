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
 *  定义方法进行删除库存的数量，
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

    /**
     *  ReduceStock 中定义的方法，可以减去使用的库存数量
     * @param id   表示需要减去的产品id
     * @param amount  需要减去的产品数量
     */
    public static void reduce(int id, int amount) {
        try {
            new dbConnector();
            Connection connect = dbConnector.getConnection();
            String sql;
            // 使用sql语句进行减法
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
            // 先判断是否可以登陆
            out.print(rsToJSON.getErrorNoLogin());
            return;
        }
        request.setCharacterEncoding("utf-8");

        try {
            // 调用reduce 方法 进行操作
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


