package edu.stu.controller;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.log4j.Logger;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Date;

public class DownloadFilter implements Filter {

    private static final Logger LOGGER = Logger.getLogger(DownloadFilter.class);
    private String initUser;
    private String initPsw;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        initUser = filterConfig.getServletContext().getInitParameter("username");
        initPsw = filterConfig.getServletContext().getInitParameter("password");
        initPsw = DigestUtils.sha1Hex(initPsw);
    }

    @Override
    public void destroy() {

    }


    @Override
    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) resp;
        if (validate(request)) {
            chain.doFilter(request, response);
        } else {
            response.sendRedirect(request.getServletContext().getContextPath());
        }
    }

    private boolean validate(HttpServletRequest request) {
        HttpSession session = request.getSession();
        String username = (String) session.getAttribute("username");
        String password = (String) session.getAttribute("password");

        LOGGER.info("客户端提交的： username=" + username + " password=" + password);

        if (null == username || null == password) {
            return false;
        }

        boolean result = username.equals(initUser) && password.equals(initPsw);
        if (result) {
            return isNotRepeatedRequest(session);
        }
        return false;
    }

    private boolean isNotRepeatedRequest(HttpSession session) {
        Long again = (Long) session.getAttribute("again");
        if (again == null) {
            Long _again = new Date().getTime() + 10 * 1000; // 第一次认证成功后，必须等待10秒钟才可进行下一次请求
            session.setAttribute("again", _again);
            return true;
        } else {
            boolean notRepeated = again < new Date().getTime();
            if (notRepeated) {
                Long _again = new Date().getTime() + 10 * 1000; // 认证成功若再次请求必须等待10秒钟
                session.setAttribute("again", _again);
            }
            return notRepeated;
        }
    }
}
