package com.mes.products;


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
import java.sql.SQLException;

/**
 * 添加产品  并将数据加载到数据库
 * @author 10626
 */
@WebServlet("/AddProducts")
public class AddProducts extends HttpServlet {
    private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public AddProducts() {
        super();
    }
    @Override
    public void init() throws ServletException {

    }
     //  写一个添加产品和工序之间关系的函数
    private void addRelates(int productId, int tagId, int sequence) throws SQLException {
        Connection connect = dbConnector.getConnection();
        String sql="INSERT INTO `tag_relates`(`product_id`,`tag_id`,`sequence`) VALUES (?,?,?)";
        PreparedStatement ps = connect.prepareStatement(sql);
        ps.setInt(1,productId);// 产品id
        ps.setInt(2,tagId); // 工序id
        ps.setInt(3,sequence);// 序列
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
            // 查找产品中最大id的产品
            sql="SELECT max(id) FROM products";
            PreparedStatement ps = connect.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            rs.next();
            int productId=rs.getInt(1);
            productId++;


            sql = "INSERT INTO `products` (`name`, `size`, `standard`,  `jianban`, `luoliao`, `chongyoucao`, `chongkong`, `dazi`, `zx_hg`, `zx_hg_t`, `zx_hg_z`, `zx_zk`, `zx_sg`, `zx_sg_t`, `zx_sg_z`, `zx_bh`, `dj_wj`, `dj_nj`, `dj_gd`, `dj_gd_t`, `dj_gd_z`, `kkf`, `fanbian`, `qlb`, `yanmo`, `paoguang`, `shangyou`, `id`, `houchuli`, `caizhi`, `kucun`, `maoban`) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
            ps = connect.prepareStatement(sql);
            ps.setString(1,request.getParameter("name")); // 产品名
            ps.setString(2, request.getParameter("size"));  // 产品规格
            ps.setString(3, request.getParameter("standard"));  // 产品标准
            ps.setString(4, request.getParameter("jianban"));  // 剪板
            ps.setString(5,request.getParameter("luoliao"));  // 落料
            ps.setString(6,request.getParameter("chongyoucao"));  // 冲油槽
            ps.setString(7,request.getParameter("chongkong"));  // 冲孔
            ps.setString(8,request.getParameter("dazi"));  // 打字
            ps.setString(9,request.getParameter("zx_hg"));  //  环规
            ps.setString(10,request.getParameter("zx_hg_t"));  // 环规_通
            ps.setString(11,request.getParameter("zx_hg_z"));  // 环规_止
            ps.setString(12,request.getParameter("zx_zk"));  // 座孔
            ps.setString(13,request.getParameter("zx_sg"));  //  塞规
            ps.setString(14,request.getParameter("zx_sg_t"));  // 塞规_通
            ps.setString(15,request.getParameter("zx_sg_z"));  // 塞规_止
            ps.setString(16,request.getParameter("zx_bh")); //  壁厚
            ps.setString(17,request.getParameter("dj_wj")); // 外角
            ps.setString(18,request.getParameter("dj_nj")); //  内角
            ps.setString(19,request.getParameter("dj_gd")); //   高度
            ps.setString(20,request.getParameter("dj_gd_t"));  // 高度_通
            ps.setString(21,request.getParameter("dj_gd_z")); // 高度_止
            ps.setString(22,request.getParameter("kkf"));  //  开口封
            ps.setString(23,request.getParameter("fanbian"));  //  翻边
            ps.setString(24,request.getParameter("qlb"));  //  切/拉边
            ps.setString(25,request.getParameter("yanmo"));  //  研磨
            ps.setString(26,request.getParameter("paoguang"));  //  抛光
            ps.setString(27,request.getParameter("shangyou"));  // 上油
            ps.setInt(28,productId);  //  产品id
            ps.setString(29, request.getParameter("houchuli"));  // 后处理
            ps.setString(30,request.getParameter("caizhi"));  //  材质
            ps.setInt(31, Integer.parseInt(request.getParameter("kucun")));  //  库存
            ps.setString(32, request.getParameter("maoban"));  //  毛板
            ps.executeUpdate();


            String[] process=request.getParameterValues("process");
            for(int i=0;i<process.length;i++) {
                addRelates(productId,Integer.parseInt(process[i]),i+1);
            }



            // 输出数据
            out = response.getWriter();

            out.println("添加成功！");
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


