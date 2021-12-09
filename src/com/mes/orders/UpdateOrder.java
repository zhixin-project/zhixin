package com.mes.orders;

import com.alibaba.fastjson.JSONObject;
import com.mes.manage.GetLogin;
import com.mes.products.ReduceStock;
import tools.ReadAsChars;
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
import java.sql.Types;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

@WebServlet(name = "UpdateOrder", value = "/UpdateOrder")
public class UpdateOrder extends HttpServlet {
    private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public UpdateOrder() {
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
        String js = ReadAsChars.read(request);
        JSONObject json = JSONObject.parseObject(js);

        try {
            new dbConnector();
            Connection connect = dbConnector.getConnection();
            String sql;
            sql="SELECT * FROM `orders` WHERE `custom_id`=?";
            PreparedStatement ps = connect.prepareStatement(sql);
            ps.setString(1,json.getString("custom_id"));
            ResultSet rs= ps.executeQuery();
            if(!rs.next()){
                out.print("订单号不存在");
                return;
            }
            sql="SELECT `using_stock` FROM `orders` WHERE `id`=?";
            ps = connect.prepareStatement(sql);
            ps.setString(1,request.getParameter("id"));
            ResultSet rs1= ps.executeQuery();
            rs1.next();
            int using_stock =rs1.getInt(1);
            ReduceStock.reduce(json.getIntValue("product_id"),-using_stock);

            sql = "UPDATE orders SET `custom_id`=?, `required_time`=?, `customer`=?, `product_id`=?, `amount`=?, `beizhu`=?,`using_stock`=?, `price`=?,`mbsl`=?,`touliao`=?,`maoban_size`=? WHERE `id`=?;";
            ps = connect.prepareStatement(sql);
            ps.setString(1, json.getString("custom_id"));
            ps.setString(2, json.getString("required_time"));
            ps.setString(3,json.getString("customer"));
            ps.setInt(4, json.getIntValue("product_id"));
            ps.setInt(5,json.getIntValue("amount"));
            ps.setString(6,json.getString("beizhu"));
            ps.setInt(7,json.getIntValue("reduce"));
            ps.setDouble(8,json.getDoubleValue("price"));
            ps.setInt(9,json.getIntValue("mbsl"));
            ps.setString(10,json.getString("touliao"));
            ps.setString(11, json.getString("maoban_size"));
            ps.setString(12,request.getParameter("id"));
            ps.executeUpdate();
            ReduceStock.reduce(json.getIntValue("product_id"),json.getIntValue("reduce"));
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
