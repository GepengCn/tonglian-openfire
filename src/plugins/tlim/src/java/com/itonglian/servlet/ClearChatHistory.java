package com.itonglian.servlet;

import com.alibaba.fastjson.JSONObject;
import com.itonglian.dao.ChatDao;
import com.itonglian.dao.impl.ChatDaoImpl;
import com.itonglian.utils.MessageUtils;
import org.jivesoftware.admin.AuthCheckFilter;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class ClearChatHistory extends HttpServlet {

    ChatDao chatDao = ChatDaoImpl.getInstance();
    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        AuthCheckFilter.addExclude("tlim/clearChatHistory");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        MessageUtils.setResponse(resp);

        PrintWriter printWriter = resp.getWriter();


        String user_id = req.getParameter("user_id");

        String other_id = req.getParameter("other_id");

        chatDao.clearChatHistory(user_id,other_id);
        chatDao.clearChatHistory(other_id,user_id);

        doBack(new BackJson("ok",""),printWriter);

    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        this.doGet(req, resp);
    }

    private void doBack(BackJson backJson, PrintWriter printWriter){
        printWriter.append(JSONObject.toJSONString(backJson));
        printWriter.flush();
        printWriter.close();
    }

    private class BackJson{
        private String result;

        private String result_detail;


        public BackJson(String result, String result_detail) {
            this.result = result;
            this.result_detail = result_detail;
        }

        public String getResult() {
            return result;
        }

        public void setResult(String result) {
            this.result = result;
        }

        public String getResult_detail() {
            return result_detail;
        }

        public void setResult_detail(String result_detail) {
            this.result_detail = result_detail;
        }

    }
}
