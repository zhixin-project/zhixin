package com.mes.products;


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
@WebServlet("/GetProducts")
public class GetProducts extends HttpServlet {
    private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public GetProducts() {
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

        try {
            new dbConnector();
            Connection connect = dbConnector.getConnection();
            String sql;
            sql = "SELECT * FROM `products` ORDER BY id";
            PreparedStatement ps = connect.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            JSON json = null;
            if("table".equals(request.getParameter("flag"))) {
                json = rsToJSON.resultSetToJSON(rs, Integer.parseInt(request.getParameter("page")), Integer.parseInt(request.getParameter("limit")));
            }else if("search".equals(request.getParameter("flag"))) {
                json = rsToJSON.resultSetToJSON(rs);
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


