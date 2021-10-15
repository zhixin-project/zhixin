package com.mes.products;


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
import java.sql.SQLException;
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
            sql="SELECT count(*) FROM products WHERE name =? AND standard=? AND caizhi=? AND maoban=? AND houchuli=? AND size=? AND " +
                    " jianban =? AND luoliao =? AND chongyoucao =? AND chongkong =? AND dazi =? AND zx_hg =?" +
                    "AND zx_hg_t =? AND zx_hg_z =? AND zx_zk=? AND zx_sg =? AND zx_sg_t =? AND zx_sg_z =? AND " +
                    "zx_bh =? AND dj_wj=? AND dj_nj=? AND dj_gd=? AND dj_gd_t=? AND dj_gd_z=? AND kkf =? AND fanbian=? AND " +
                    "qlb =? AND yanmo =? AND paoguang =? AND shangyou = ?;";
            PreparedStatement ps = connect.prepareStatement(sql);
            ps.setString(1,request.getParameter("name"));
            ps.setString(2, request.getParameter("standard"));
            ps.setString(3,request.getParameter("caizhi"));
            ps.setString(4, request.getParameter("maoban"));
            ps.setString(5, request.getParameter("houchuli"));
            ps.setString(6, request.getParameter("size"));
            ps.setString(7, request.getParameter("jianban"));
            ps.setString(8,request.getParameter("luoliao"));
            ps.setString(9,request.getParameter("chongyoucao"));
            ps.setString(10,request.getParameter("chongkong"));
            ps.setString(11,request.getParameter("dazi"));
            ps.setString(12,request.getParameter("zx_hg"));
            ps.setString(13,request.getParameter("zx_hg_t"));
            ps.setString(14,request.getParameter("zx_hg_z"));
            ps.setString(15,request.getParameter("zx_zk"));
            ps.setString(16,request.getParameter("zx_sg"));
            ps.setString(17,request.getParameter("zx_sg_t"));
            ps.setString(18,request.getParameter("zx_sg_z"));
            ps.setString(19,request.getParameter("zx_bh"));
            ps.setString(20,request.getParameter("dj_wj"));
            ps.setString(21,request.getParameter("dj_nj"));
            ps.setString(22,request.getParameter("dj_gd"));
            ps.setString(23,request.getParameter("dj_gd_t"));
            ps.setString(24,request.getParameter("dj_gd_z"));
            ps.setString(25,request.getParameter("kkf"));
            ps.setString(26,request.getParameter("fanbian"));
            ps.setString(27,request.getParameter("qlb"));
            ps.setString(28,request.getParameter("yanmo"));
            ps.setString(29,request.getParameter("paoguang"));
            ps.setString(30,request.getParameter("shangyou"));
            ResultSet rs = ps.executeQuery();
            rs.next();
            if(rs.getInt(1)!=0){
                out.print("产品重复");
                return;
            }

            int productId=Integer.parseInt(request.getParameter("product_id"));


            sql = "UPDATE products SET  name =?,size=?,standard=?, jianban =? ,luoliao =? ,chongyoucao =? ,chongkong =? , dazi =? , zx_hg =?" +
                    ", zx_hg_t =? , zx_hg_z =? , zx_zk=? , zx_sg =? , zx_sg_t =? , zx_sg_z =? ," +
                    "zx_bh =? , dj_wj=? , dj_nj=? , dj_gd=? , dj_gd_t=? , dj_gd_z=? , kkf =? , fanbian=? ," +
                    "qlb =? , yanmo =? , paoguang =? , shangyou = ?,houchuli=?,caizhi=?,kucun=?,maoban=? WHERE id=?;";
            ps = connect.prepareStatement(sql);
            ps.setString(1,request.getParameter("name"));
            ps.setString(2, request.getParameter("size"));
            ps.setString(3, request.getParameter("standard"));
            ps.setString(4, request.getParameter("jianban"));
            ps.setString(5,request.getParameter("luoliao"));
            ps.setString(6,request.getParameter("chongyoucao"));
            ps.setString(7,request.getParameter("chongkong"));
            ps.setString(8,request.getParameter("dazi"));
            ps.setString(9,request.getParameter("zx_hg"));
            ps.setString(10,request.getParameter("zx_hg_t"));
            ps.setString(11,request.getParameter("zx_hg_z"));
            ps.setString(12,request.getParameter("zx_zk"));
            ps.setString(13,request.getParameter("zx_sg"));
            ps.setString(14,request.getParameter("zx_sg_t"));
            ps.setString(15,request.getParameter("zx_sg_z"));
            ps.setString(16,request.getParameter("zx_bh"));
            ps.setString(17,request.getParameter("dj_wj"));
            ps.setString(18,request.getParameter("dj_nj"));
            ps.setString(19,request.getParameter("dj_gd"));
            ps.setString(20,request.getParameter("dj_gd_t"));
            ps.setString(21,request.getParameter("dj_gd_z"));
            ps.setString(22,request.getParameter("kkf"));
            ps.setString(23,request.getParameter("fanbian"));
            ps.setString(24,request.getParameter("qlb"));
            ps.setString(25,request.getParameter("yanmo"));
            ps.setString(26,request.getParameter("paoguang"));
            ps.setString(27,request.getParameter("shangyou"));
            ps.setString(28, request.getParameter("houchuli"));
            ps.setString(29,request.getParameter("caizhi"));
            ps.setInt(30, Integer.parseInt(request.getParameter("kucun")));
            ps.setString(31, request.getParameter("maoban"));
            ps.setInt(32,productId);
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


