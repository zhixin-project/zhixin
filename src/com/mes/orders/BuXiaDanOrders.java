package com.mes.orders;


import com.alibaba.fastjson.JSONObject;
import com.mes.manage.GetLogin;
import tools.ReadAsChars;
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
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @author 10626
 */
@WebServlet("/BxdOrders")
public class BuXiaDanOrders extends HttpServlet {
    private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public BuXiaDanOrders() {
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
            sql="SELECT * FROM `orders` WHERE id=?";
            PreparedStatement ps = connect.prepareStatement(sql);
            ps.setString(1,json.getString("id"));
            ResultSet rs =ps.executeQuery();
            rs.next();



            sql = "INSERT INTO `orders` (`id`,`custom_id`, `required_time`, `customer`, `product_id`, `amount`, `beizhu`, `buxiadan`, `bxdly`) VALUES (?,?, ?, ?, ?, ?,?,1,?)";
            ps = connect.prepareStatement(sql);
            DateTimeFormatter ofPattern = DateTimeFormatter.ofPattern("yyyyMMddHHmmssSS");
            String localDate = LocalDateTime.now().format(ofPattern);
            ps.setString(1,localDate);
            ps.setString(2, rs.getString("custom_id"));
            ps.setString(3, rs.getString("required_time"));
            ps.setString(4,rs.getString("customer"));
            ps.setInt(5, rs.getInt("product_id"));
            ps.setInt(6,json.getIntValue("amount"));
            ps.setString(7,rs.getString("beizhu"));
            ps.setString(8,json.getString("bxdly"));

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


