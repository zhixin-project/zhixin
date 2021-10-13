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
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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
            // 当没有登录时就返回json格式报错数据
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
            sql = "INSERT INTO `orders` (`id`,`custom_id`, `required_time`, `customer`, `product_id`, `amount`, `beizhu`,`using_stock`, `price`,`mbsl`,`touliao`) VALUES (?,?, ?, ?, ?, ?,?,?,?,?,?)";
            ps = connect.prepareStatement(sql);
            // id 用时间序列来表示
            DateTimeFormatter ofPattern = DateTimeFormatter.ofPattern("yyyyMMddHHmmssSS");
            String localDate = LocalDateTime.now().format(ofPattern);
            ps.setString(1,localDate);
            ps.setString(2, json.getString("custom_id"));//  订单号
            ps.setString(3, json.getString("required_time"));// 交付时间
            ps.setString(4,json.getString("customer"));// 客户
            ps.setInt(5, json.getIntValue("product_id"));// 产品id
            ps.setInt(6,json.getIntValue("amount"));//  生产产品数量
            ps.setString(7,json.getString("beizhu"));// 备注
            ps.setInt(8,json.getIntValue("reduce"));//  使用库存
            ps.setDouble(9,json.getDoubleValue("price"));// 单价
            ps.setInt(10,json.getIntValue("mbsl")); // 毛板数量
            ps.setString(11,json.getString("touliao"));//  投料
            ps.executeUpdate();
            // 将本次使用的库存从总库存中减去
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

