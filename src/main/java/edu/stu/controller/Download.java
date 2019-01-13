package edu.stu.controller;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class Download extends HttpServlet {

    private static final Logger LOGGER = Logger.getLogger(Download.class);


    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        service(req, resp, req.getParameter("type"));

    }

    private String getRemoteAddress(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.length() == 0 || ip.equalsIgnoreCase("unknown")) {
            ip = request.getHeader("X-Real-IP");
        }
        if (ip == null || ip.length() == 0 || ip.equalsIgnoreCase("unknown")) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || ip.equalsIgnoreCase("unknown")) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || ip.equalsIgnoreCase("unknown")) {
            ip = request.getRemoteAddr();
        }

        return ip;
    }

    private void service(HttpServletRequest request, HttpServletResponse response, String type) throws ServletException, IOException {
        LOGGER.info(getRemoteAddress(request) + " request for downloading " + type);

        if (null == type) {
            noService(request, response);
            return;
        }
        if ("android".equals(type)) {
            downloadClientFile(response, "apk");
        } else if ("windows".equals(type)) {
            downloadClientFile(response, "exe");
        } else if ("config".equals(type)) {
            HttpSession session = request.getSession();
            session.setAttribute("username", request.getParameter("username"));
            session.setAttribute("password", request.getParameter("password"));
            response.sendRedirect("config.do");
        } else {
            // TODO
            response.sendRedirect(getServletContext().getContextPath()); // 暂时重定向到当前WEB应用程序的根目录中
        }
    }

    private void downloadClientFile(HttpServletResponse response, String initParamName) throws IOException {
        String filepath = getServletConfig().getInitParameter(initParamName);
        File file = new File(getServletContext().getRealPath(filepath));

        response.setContentType("application/octet-stream"); // 二进制数据文件
        response.setHeader("Content-disposition", "attachment; filename=" + file.getName());
        response.setHeader("Content-Length", String.valueOf(file.length()));

        FileInputStream inputStream = new FileInputStream(file);
        ServletOutputStream outputStream = response.getOutputStream();
        IOUtils.copy(inputStream, outputStream);
    }

    private void noService(HttpServletRequest request, HttpServletResponse response) throws ServletException,
            IOException {
        request.getRequestDispatcher("/404.html").forward(request, response);
    }


}
