package com.mes.orders;


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
 * 将已经完成的订单数据进行输出
 * @author 10626
 */
@WebServlet("/ExportOrders")
public class ExportOrders extends HttpServlet {
    private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public ExportOrders() {
        super();
    }

    @Override
    public void init() throws ServletException {

    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/json; charset=utf-8");
        PrintWriter out = response.getWriter();
        if (!GetLogin.getStat(request, response)) {
            // 如果没有登录，就返回编写的json数据
            out.print(rsToJSON.getErrorNoLogin());
            return;
        }
        JSON json = null;
        try {
            new dbConnector();
            Connection connect = dbConnector.getConnection();
            String sql;
            PreparedStatement ps;
            ResultSet rs;
            // 在orders表与products表进行左外连接的附表中查找  满足buxiadan!=1 AND curr_step!=100 AND finished=1 的数据，并且按照完成时间降序将数据进行排列
            sql = "SELECT *,actual_ship - tqck as sjck FROM `orders` LEFT JOIN `products` on orders.product_id=products.id WHERE buxiadan!=1 AND curr_step!=100 AND finished=1 ORDER BY finish_time desc ";
            ps = connect.prepareStatement(sql);
            rs = ps.executeQuery();
            //  将得到的数据以json格式输出
            json = rsToJSON.resultSetToJSON(rs, Integer.parseInt(request.getParameter("page")), Integer.parseInt(request.getParameter("limit")));
            // 输出数据
            out = response.getWriter();

            out.println(json);
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


