package edu.stu.controller;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class ConfigDownload extends HttpServlet {

    private static final Logger LOGGER = Logger.getLogger(ConfigDownload.class);

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // shell脚本在当前文件系统中的绝对路径
        String script = getServletConfig().getInitParameter("shellScriptPath");
        String clientConfigFileName = String.valueOf(System.currentTimeMillis());
        try {
            cmd(script, clientConfigFileName);
        } catch (InterruptedException e) {
            LOGGER.error(e.getMessage(), e);
            return;
        }

        download(response, clientConfigFileName + ".ovpn");
    }

    private void download(HttpServletResponse response, String clientConfigFileName) throws IOException {
        String clientConfigDir = getServletConfig().getInitParameter("clientConfigFileDir");
        String configFile = clientConfigDir.endsWith("/") ? clientConfigDir + clientConfigFileName :
                clientConfigDir + "/" + clientConfigFileName;
        File file = new File(configFile);

        response.setContentType("application/octet-stream"); // 二进制数据文件
        response.setHeader("Content-disposition", "attachment; filename=" + file.getName());
        response.setHeader("Content-Length", String.valueOf(file.length()));
        FileInputStream inputStream = new FileInputStream(file);
        ServletOutputStream outputStream = response.getOutputStream();
        IOUtils.copy(inputStream, outputStream);
    }


    // 执行shell命令
    private void cmd(String script, String clientConfigFileName) throws IOException, InterruptedException {
        String cmd = script + " " + clientConfigFileName;
        LOGGER.info("执行： " + cmd);

        Process exec = Runtime.getRuntime().exec(cmd);

        int status = exec.waitFor(); // 阻塞异步
        if (status != 0) {
            LOGGER.error("Failed to execute " + cmd);
        }

        exec.destroy();

    }


}
