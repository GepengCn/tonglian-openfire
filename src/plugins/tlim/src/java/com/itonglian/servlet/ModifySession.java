package com.itonglian.servlet;

import com.alibaba.fastjson.JSONArray;
import com.itonglian.dao.MessageDao;
import com.itonglian.dao.SessionDao;
import com.itonglian.dao.SubscriberDao;
import com.itonglian.dao.impl.MessageDaoImpl;
import com.itonglian.dao.impl.SessionDaoImpl;
import com.itonglian.dao.impl.SubscriberDaoImpl;
import com.itonglian.entity.OfSubscriber;
import com.itonglian.entity.User;
import com.itonglian.utils.MessageUtils;
import com.itonglian.utils.StringUtils;
import com.itonglian.utils.UserCacheManager;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Iterator;
import java.util.List;

public class ModifySession extends BaseServlet {

    SessionDao sessionDao = SessionDaoImpl.getInstance();

    SubscriberDao subscriberDao = SubscriberDaoImpl.getInstance();

    MessageDao messageDao = MessageDaoImpl.getInstance();

    @Override
    protected String mapper() {
        return "tlim/modifySession";
    }

    @Override
    protected void execute(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        PrintWriter printWriter = resp.getWriter();

        String sessionId = req.getParameter("session_id");

        String sessionName = req.getParameter("session_name");

        String subscribers = req.getParameter("subscribers");

        if(StringUtils.isNullOrEmpty(sessionId)){
            doBack(new BackJson("error-007","session_id为空",sessionId),printWriter);
            return;
        }

        String modifyTime = "";
        //更新session
        if(!StringUtils.isNullOrEmpty(sessionName)){
            modifyTime = MessageUtils.getTs();
            sessionDao.updateNameById(sessionId,sessionName,modifyTime);
        }

        //更新订阅者
        handlerSubscribers(subscribers,sessionId);

        doBack(new BackJson("ok","",sessionId,modifyTime),printWriter);
    }



    private void handlerSubscribers(String subscribers,String sessionId){

        List<HandlerSubcriber> subcriberList = JSONArray.parseArray(subscribers,HandlerSubcriber.class);

        if(subcriberList==null){
            return;
        }

        Iterator<HandlerSubcriber> iterator = subcriberList.iterator();

        while(iterator.hasNext()){

            HandlerSubcriber handlerSubcriber = iterator.next();

            String type = handlerSubcriber.getType();

            if(StringUtils.isNullOrEmpty(type)){
                continue;
            }

            switch (type){
                case "add":
                    User user = UserCacheManager.findUserByKey(handlerSubcriber.getUserId());
                    if(user == null){
                        break;
                    }
                    subscriberDao.delete(handlerSubcriber.getUserId(),sessionId);
                    OfSubscriber ofSubscriber = new OfSubscriber();
                    ofSubscriber.setUser_id(user.getUser_id());
                    ofSubscriber.setUser_name(user.getUser_name());
                    ofSubscriber.setAcct_login(user.getAcct_login());
                    ofSubscriber.setPic(user.getPic_url());
                    ofSubscriber.setTs(MessageUtils.getTs());
                    ofSubscriber.setSession_id(sessionId);
                    subscriberDao.add(ofSubscriber);
                    break;
                case "del":
                    subscriberDao.delete(handlerSubcriber.getUserId(),sessionId);
                    messageDao.deleteByUser(sessionId,handlerSubcriber.getUserId());
                    break;
                default:
                    break;


            }

        }


    }

    @Data
    @AllArgsConstructor
    @EqualsAndHashCode(callSuper = false)
    private class BackJson extends BaseServlet.BackJson {

        private String result;

        private String result_detail;

        private String session_id;

        private String session_modify_time;

        public BackJson(String result, String result_detail, String session_id) {
            this.result = result;
            this.result_detail = result_detail;
            this.session_id = session_id;
        }

    }

    @Data
    private static class HandlerSubcriber{

        private String userId;

        private String type;

    }
}
