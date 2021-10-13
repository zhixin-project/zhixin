package com.mes.orders;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
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
import java.sql.*;

/**
 * @author 10626
 */
@WebServlet("/PrintOrders")
public class PrintOrders extends HttpServlet {
    private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public PrintOrders() {
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
        request.setCharacterEncoding("UTF-8");

        try {
            new dbConnector();
            Connection connect = dbConnector.getConnection();
            String sql;
            sql = "SELECT * FROM orders LEFT JOIN products ON orders.product_id=products.id WHERE orders.id=?";
            PreparedStatement ps = connect.prepareStatement(sql);
            try {
                ps.setString(1, request.getParameter("id"));
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
            ResultSet rs = ps.executeQuery();
            rs.next();
            int id=rs.getInt("product_id");
            JSONObject result = new JSONObject();
            try {
//          ResultSetMetaData 有关 ResultSet 中列的名称和类型的信息。
                ResultSetMetaData rsmd = rs.getMetaData();
//        遍历数据集

                int columnCount = rsmd.getColumnCount();
//            列从1开始，要等于
                for (int i = 1; i <= columnCount; i++) {
//                获取列名
                    String columnName = rsmd.getColumnName(i);
//                取数据
                    String value = rs.getString(columnName);
//                添加到rowObj中
                    result.put(columnName, value);
                }
                sql="SELECT * FROM tag_relates LEFT JOIN tags ON tag_relates.tag_id=tags.id WHERE product_id="+id;
                ps = connect.prepareStatement(sql);
                rs = ps.executeQuery();
                rs.next();
                StringBuilder r= new StringBuilder(rs.getString("name"));
                while(rs.next()){
                    r.append("->").append(rs.getString("name"));
                }
                result.put("gx",r.toString());

                // 输出数据
                out = response.getWriter();
                out.println(result);
                // 完成后关闭
                rs.close();
                ps.close();
                connect.close();
            } catch (Exception e) {
                out.print(rsToJSON.getError());
                e.printStackTrace();
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
        /**
         * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
         */
        @Override
        protected void doPost (HttpServletRequest request, HttpServletResponse response) throws
        ServletException, IOException {
            // TODO Auto-generated method stub
            doGet(request, response);
        }
    }


