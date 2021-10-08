package com.mes.manage;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * 判断是否是登录状态
 * @author 10626
 */
@WebServlet("/GetLogin")

public class GetLogin extends HttpServlet {
    private static final long serialVersionUID = 1L;



    /**
     * @see HttpServlet#HttpServlet()
     */
    public GetLogin() {
        super();
    }
    @Override
    public void init() throws ServletException {

    }
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/json; charset=utf-8");
        PrintWriter out = response.getWriter();
        HttpSession s = request.getSession(true);
        if (s.getAttribute("IsLogin")==null||!(Boolean) s.getAttribute("IsLogin")) {
            out.println("false");
        } else {
            out.println("true");
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

    /**
     * 通过GetLogin方法获得登录状态
     * @param request  请求
     * @param response 响应
     * @return  返回当前的登录状态
     * @throws ServletException
     * @throws IOException
     */
    public static Boolean getStat(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/json; charset=utf-8");
        HttpSession s = request.getSession(true);
        return s.getAttribute("IsLogin") != null && (Boolean) s.getAttribute("IsLogin");
    }
}

