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
 * @author 10626
 */
@WebServlet("/GetWarnOrders")
public class GetWarnOrders extends HttpServlet {
    private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public GetWarnOrders() {
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
            // 所有三天未操作的订单（包括已经完成所有工序的订单）
//            sql = "SELECT * FROM orders where id in (SELECT id FROM (SELECT id,max(finished_time) t from processes GROUP BY id) a WHERE DATEDIFF(now(),a.t)>=3) order by  required_time; ";
            //  未完成所有工序的订单中三天未操作的订单，按照交付时间增序排列
            sql = "SELECT *,IFNULL(curr_step/total_step,0) as percentage FROM `orders` LEFT JOIN `products` on orders.product_id=products.id  where orders.id in (SELECT id FROM (SELECT id FROM (SELECT id,max(finished_time) t from processes GROUP BY id) a WHERE DATEDIFF(now(),a.t)>=3) c WHERE id IN (SELECT id from processes WHERE finished_time is NULL)) order by  required_time; ";
            ps = connect.prepareStatement(sql);
            rs = ps.executeQuery();
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


