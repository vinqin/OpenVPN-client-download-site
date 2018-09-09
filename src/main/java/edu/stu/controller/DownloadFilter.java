package edu.stu.controller;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.log4j.Logger;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

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
        String username = (String) request.getSession().getAttribute("username");
        String password = (String) request.getSession().getAttribute("password");

        LOGGER.info("客户端提交的： username=" + username + " password=" + password);

        if (null == username || null == password) {
            return false;
        }

        return username.equals(initUser) && password.equals(initPsw);
    }
}
