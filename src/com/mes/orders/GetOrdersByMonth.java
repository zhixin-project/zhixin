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
 * 筛选器  ，通过开始结束时间来筛选数据
 * @author 10626
 */
@WebServlet("/GetOrdersByMonth")
public class GetOrdersByMonth extends HttpServlet {
    private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public GetOrdersByMonth() {
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

            sql = "SELECT A.custom_id,A.customer,name,A.required_time,A.amount AS task_num,SUM(B.amount) AS waste_num ,A.actual_ship,A.tqck,A.price,A.price*A.actual_ship AS total_price" +
                    "                    FROM `orders` A LEFT JOIN `orders` B ON( A.custom_id=B.custom_id AND A.buxiadan=0 AND B.buxiadan=1)  " +
                    "                    JOIN products ON A.product_id=products.id WHERE A.required_time BETWEEN ? AND ?  AND A.curr_step!=100 AND A.finished=1 GROUP BY B.custom_id HAVING waste_num IS NOT NULL" +
                    "                    UNION SELECT custom_id,customer,name,required_time, amount as task_num,0,actual_ship,tqck,price,price*actual_ship AS total_price  " +
                    "                    FROM orders JOIN products ON orders.product_id=products.id WHERE buxiadan=0 AND custom_id NOT IN  " +
                    "                    (SELECT custom_id FROM orders WHERE buxiadan=1)  " +
                    "                    AND required_time BETWEEN ? AND ? AND curr_step!=100 AND finished=1";
            ps = connect.prepareStatement(sql);
            ps.setString(1,  request.getParameter("start_time"));
            ps.setString(2,  request.getParameter("end_time"));
            ps.setString(3,  request.getParameter("start_time"));
            ps.setString(4,  request.getParameter("end_time"));
            rs = ps.executeQuery();
            json = rsToJSON.resultSetToJSON(rs);


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


