package com.mes.orders;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.mes.products.ReduceStock;
import tools.dbConnector;
import tools.rsToJSON;
import tools.ReadAsChars;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

import com.mes.manage.GetLogin;

/**
 * @author 10626
 */
@WebServlet("/AddOrders")
public class AddOrders extends HttpServlet {
    private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public AddOrders() {
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
            if(rs.next()){
                out.print("订单号重复！");
                return;
            }
            sql = "INSERT INTO `orders` (`id`,`custom_id`, `required_time`, `customer`, `product_id`, `amount`, `beizhu`,`using_stock`, `price`,`mbsl`,`touliao`,`finish_time`) VALUES (?,?, ?, ?, ?, ?,?,?,?,?,?,?)";
            ps = connect.prepareStatement(sql);
            DateTimeFormatter ofPattern = DateTimeFormatter.ofPattern("yyyyMMddHHmmssSS");
            String localDate = LocalDateTime.now().format(ofPattern);
            ps.setString(1,localDate);
            ps.setString(2, json.getString("custom_id"));
            ps.setString(3, json.getString("required_time"));
            ps.setString(4,json.getString("customer"));
            ps.setInt(5, json.getIntValue("product_id"));
            ps.setInt(6,json.getIntValue("amount"));
            ps.setString(7,json.getString("beizhu"));
            ps.setInt(8,json.getIntValue("reduce"));
            ps.setDouble(9,json.getDoubleValue("price"));
            ps.setInt(10,json.getIntValue("mbsl"));
            ps.setString(11,json.getString("touliao"));
            if(json.getIntValue("amount")==json.getIntValue("reduce")){
                java.util.Date date = new Date();
                DateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String punchTime = simpleDateFormat.format(date);
                ps.setString(12, punchTime);

            }else {
                ps.setNull(12, Types.TIME);
            }
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


