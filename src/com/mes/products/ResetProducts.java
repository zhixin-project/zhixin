package com.mes.products;


import com.alibaba.fastjson.JSONObject;
import com.mes.manage.GetLogin;
import tools.ReadAsChars;
import tools.dbConnector;
import tools.rsToJSON;
import java.util.regex.Pattern;

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
import java.sql.SQLException;
import java.util.Iterator;
import java.util.Map;

/**
 * @author 10626
 */
@WebServlet("/ResetProducts")
public class ResetProducts extends HttpServlet {
    private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public ResetProducts() {
        super();
    }
    @Override
    public void init() throws ServletException {

    }
    private void updateRelates(int productId, int tagId, int sequence) throws SQLException {
        Connection connect = dbConnector.getConnection();
        String sql="UPDATE `tag_relates` SET `sequence`=?  WHERE `product_id`=? AND `tag_id`=?;";
        PreparedStatement ps = connect.prepareStatement(sql);
        ps.setInt(1,sequence);
        ps.setInt(2,productId);
        ps.setInt(3,tagId);
        ps.executeUpdate();
    }
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//        System.out.println("request.getParameter(\"content\")"+request.getParameter("content"));
        response.setContentType("text/json; charset=utf-8");
        String js = ReadAsChars.read(request);

        JSONObject json = JSONObject.parseObject(js);
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
            Iterator itr=json.entrySet().iterator();
            sql="SELECT count(*) FROM products WHERE ";
            while (itr.hasNext()){
                Map.Entry entry = (Map.Entry) itr.next();
                sql+=entry.getKey().toString()+"=\'"+entry.getValue().toString() +"\' AND ";
            }
            String reg=" AND $";
            sql=Pattern.matches(reg,sql)? sql.substring(sql.length()-5)+";":sql;
            PreparedStatement ps = connect.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            rs.next();
            int pNum=rs.getInt(1);
            if(pNum!=0){
                out.print("产品重复");
                return;
            }

            int productId=Integer.parseInt(request.getParameter("product_id"));
            Iterator itr2=json.entrySet().iterator();
            sql="UPDATE products SET ";
            while (itr2.hasNext()){
                Map.Entry entry = (Map.Entry) itr2.next();
                sql+=entry.getKey().toString()+"=\'"+entry.getValue().toString() +"\' , ";
            }
            reg=" , $";
            sql=Pattern.matches(reg,sql)? sql.substring(sql.length()-3):sql;
            sql+=" where id=\'"+productId+"\';";
            ps = connect.prepareStatement(sql);
            ps.executeUpdate();

            String[] process=request.getParameterValues("process");
            for(int i=0;i<process.length;i++) {
                updateRelates(productId,Integer.parseInt(process[i]),i+1);
            }

            // 输出数据

            out.println("修改成功！");
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


