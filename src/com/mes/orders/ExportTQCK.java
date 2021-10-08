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
 *  按照交付时间降序输出提前出库和实际出库原始订单的数据
 * @author 10626
 */
@WebServlet("/ExportTQCK")
public class ExportTQCK extends HttpServlet {
    private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public ExportTQCK() {
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
            //  从orders和products两张表按照产品id进行左外连接之后的附属表中筛选出   buxiadan!=1(表示是原始订单) AND curr_step!=100 AND tqck!=0 的数据，并且按照交付时间按照降序排列
            sql = "SELECT *,actual_ship - tqck as sjck FROM `orders` LEFT JOIN `products` on orders.product_id=products.id WHERE buxiadan!=1 AND curr_step!=100 AND tqck!=0 ORDER BY required_time desc ";
            ps = connect.prepareStatement(sql);
            rs = ps.executeQuery();
            // 将拿到的数据转换成json格式
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


