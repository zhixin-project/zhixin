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
 *
 * 筛选出已经完成的订单，当search为true时就可以使用 按照客户名字进行筛选没有完成的订单
 * @author 10626
 */
@WebServlet("/WarnOrders")
public class WarnOrders extends HttpServlet {
    private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public WarnOrders() {
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
        JSON json =null;
        try {
            new dbConnector();
            Connection connect = dbConnector.getConnection();
            String sql;
            PreparedStatement ps;
            ResultSet rs;

            sql = "select * from processes";
            ps = connect.prepareStatement(sql);
//            ps.setString(1,"%"+request.getParameter("customer")+"%");
            rs = ps.executeQuery();
            json = rsToJSON.resultSetToJSON(rs);

            // 输出数据
            out = response.getWriter();

            out.println(json);

            // 完成后关闭
            rs.close();
            ps.close();
            connect.close();
        }catch (Exception e) {
            out.print(rsToJSON.getError());
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


