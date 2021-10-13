package com.mes.orders;


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
@WebServlet("/FinishOrders")
public class FinishOrders extends HttpServlet {
    private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public FinishOrders() {
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
        request.setCharacterEncoding("utf-8");

        try {
            new dbConnector();
            Connection connect = dbConnector.getConnection();
            String sql;
            if(Integer.parseInt(request.getParameter("fhsl"))==0){
                sql = "UPDATE `orders` SET `curr_step`='100', `total_step`='100', `finished`='1', `actual_ship`=?  WHERE (`custom_id`=?)";
            }else {
                sql = "UPDATE `orders` SET`finished`='1', `actual_ship`=?  WHERE (`custom_id`=?)";
            }
            PreparedStatement ps = connect.prepareStatement(sql);
            ps.setInt(1, Integer.parseInt(request.getParameter("fhsl")));
            ps.setString(2, request.getParameter("custom_id"));
            ps.executeUpdate();

            sql="SELECT `product_id` FROM orders WHERE id=?";
            ps = connect.prepareStatement(sql);
            ps.setString(1, request.getParameter("id"));
            ResultSet rs=ps.executeQuery();
            rs.next();
            int productId=rs.getInt(1);


            sql="UPDATE `products` SET `kucun`=kucun+? WHERE (`id`=?)";
            ps = connect.prepareStatement(sql);
            ps.setInt(1, Integer.parseInt(request.getParameter("jksl")));
            ps.setInt(2, productId);
            ps.executeUpdate();


            // 输出数据
            out = response.getWriter();

            out.println("success");
            // 完成后关闭
            ps.close();
            connect.close();
        }catch (Exception e) {
            out.print("fail");
            e.printStackTrace(); }
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


