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

/**  筛选器
 *  按照客户名或者客户id查找客户的订单
 * @author 10626
 */
@WebServlet("/GetOrders")
public class GetOrders extends HttpServlet {
    private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public GetOrders() {
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
        JSON json = null;
        try {
            new dbConnector();
            Connection connect = dbConnector.getConnection();
            String sql;
            PreparedStatement ps;
            ResultSet rs;
            // 如果是客户
            if ("customer".equals(request.getParameter("search"))) {
                // 查找客户 数据  通过客户名进行查找， 按照订单id进行降序排列
                sql = "SELECT *,IFNULL(curr_step/total_step,0) as percentage FROM `orders` LEFT JOIN `products` on orders.product_id=products.id WHERE `customer`LIKE ? ORDER BY orders.id DESC";
                ps = connect.prepareStatement(sql);
                // 根据查找的匹配规则进行查找
                ps.setString(1, "%" + request.getParameter("customer") + "%");
                rs = ps.executeQuery();
                json = rsToJSON.resultSetToJSON(rs);

            } else if ("custom_id".equals(request.getParameter("search"))) {
                sql = "SELECT *,IFNULL(curr_step/total_step,0) as percentage FROM `orders` LEFT JOIN `products` on orders.product_id=products.id WHERE `custom_id`LIKE ? ORDER BY orders.id DESC";
                ps = connect.prepareStatement(sql);
                ps.setString(1, "%" + request.getParameter("custom_id") + "%");
                rs = ps.executeQuery();
                json = rsToJSON.resultSetToJSON(rs);

            } else {
                sql = "SELECT *,IFNULL(curr_step/total_step,0) as percentage FROM `orders` LEFT JOIN `products` on orders.product_id=products.id ORDER BY orders.id DESC";
                ps = connect.prepareStatement(sql);
                rs = ps.executeQuery();
                json = rsToJSON.resultSetToJSON(rs, Integer.parseInt(request.getParameter("page")), Integer.parseInt(request.getParameter("limit")));
            }


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

