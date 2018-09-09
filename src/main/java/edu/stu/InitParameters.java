package edu.stu;

import org.apache.log4j.Logger;

import javax.servlet.Servlet;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import java.io.IOException;

public class InitParameters implements Servlet {
    @Override
    public void init(ServletConfig config) throws ServletException {
        String logName = config.getServletContext().getInitParameter("servlet-jsp-vpn");
        System.setProperty("servlet-jsp-vpn", logName);
        final Logger logger = Logger.getLogger(InitParameters.class);
        logger.info("Initialize " + logName + "for log4j.");
    }

    @Override
    public ServletConfig getServletConfig() {
        return null;
    }

    @Override
    public void service(ServletRequest req, ServletResponse res) throws ServletException, IOException {

    }

    @Override
    public String getServletInfo() {
        return null;
    }

    @Override
    public void destroy() {

    }
}
