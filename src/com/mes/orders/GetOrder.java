package com.mes.orders;

import com.alibaba.fastjson.JSON;
import com.mes.manage.GetLogin;
import tools.dbConnector;
import tools.rsToJSON;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

@WebServlet(name = "GetOrder", value = "/GetOrder")
public class GetOrder extends HttpServlet {
    private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public GetOrder() {
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
            sql = "SELECT *,IFNULL(curr_step/total_step,0) as percentage FROM `orders` LEFT JOIN `products` on orders.product_id=products.id where orders.id=?";
            ps = connect.prepareStatement(sql);
            ps.setString(1,request.getParameter("id"));
            rs = ps.executeQuery();
            json = rsToJSON.resultSetToJSON(rs);

            HttpSession session = request.getSession();
            session.setAttribute("product", json);
            response.sendRedirect("/manage/update_order.jsp");
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
