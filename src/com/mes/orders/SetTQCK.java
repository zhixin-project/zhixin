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

/**
 *设置提前出库
 * @author 10626
 */
@WebServlet("/SetTQCK")
public class SetTQCK extends HttpServlet {
    private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public SetTQCK() {
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
            // 对几号订单设置提前出库的数量
            sql = "UPDATE `orders` SET `tqck`=? WHERE (`id`=?)";
            PreparedStatement ps = connect.prepareStatement(sql);
            ps.setInt(1, Integer.parseInt(request.getParameter("amount")));
            ps.setString(2, request.getParameter("id"));

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


