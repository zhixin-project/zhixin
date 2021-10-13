package com.mes.workers;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * 可以返回给前端登录账户的worker用户名
 * @author 10626
 */
@WebServlet("/GetWorkerName")

public class GetName extends HttpServlet {
    private static final long serialVersionUID = 1L;



    /**
     * @see HttpServlet#HttpServlet()
     */
    public GetName() {
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
        if (s.getAttribute("WorkerIsLogin")==null||!(Boolean) s.getAttribute("WorkerIsLogin")) {
            out.println("暂未登录");
        } else {
            out.println(s.getAttribute("name"));
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

    public Boolean getStat(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/json; charset=utf-8");
        HttpSession s = request.getSession(true);
        return s.getAttribute("IsLogin") != null && (Boolean) s.getAttribute("IsLogin");
    }
}
