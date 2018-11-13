package com.itonglian.utils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.itonglian.bean.Protocol;
import com.itonglian.dao.ChatDao;
import com.itonglian.dao.MessageDao;
import com.itonglian.dao.SessionDao;
import com.itonglian.dao.SubscriberDao;
import com.itonglian.dao.impl.ChatDaoImpl;
import com.itonglian.dao.impl.MessageDaoImpl;
import com.itonglian.dao.impl.SessionDaoImpl;
import com.itonglian.dao.impl.SubscriberDaoImpl;
import com.itonglian.entity.OfMessage;
import com.itonglian.entity.OfSubscriber;
import org.jivesoftware.openfire.PacketDeliverer;
import org.jivesoftware.openfire.XMPPServer;
import org.xmpp.packet.JID;
import org.xmpp.packet.Message;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

public class DissolvedUtils {

    private static SessionDao sessionDao = SessionDaoImpl.getInstance();

    private static SubscriberDao subscriberDao = SubscriberDaoImpl.getInstance();

    private static MessageDao messageDao = MessageDaoImpl.getInstance();

    private static PacketDeliverer packetDeliverer = XMPPServer.getInstance().getPacketDeliverer();

    private static ChatDao chatDao = ChatDaoImpl.getInstance();

    public static void handler(String session_id){

        Protocol protocol = new Protocol();

        protocol.setCompress("0");
        protocol.setEncode("1");
        protocol.setEncrypt("0");
        protocol.setVersion("2.0.0");
        protocol.setMsg_id(UUID.randomUUID().toString());
        protocol.setMsg_time(MessageUtils.getTs());
        protocol.setMsg_type("MTS-107");
        List<Body> bodies = new ArrayList<>();
        bodies.add(new Body(session_id));
        protocol.setBody(JSONArray.toJSONString(bodies));

        try {
            batchRoute(session_id,protocol);
        } catch (Exception e) {
            e.printStackTrace();
        }

        sessionDao.delete(session_id);

        subscriberDao.deleteBySession(session_id);

        chatDao.deleteOfflineBySession(session_id);

        messageDao.deleteBySession(session_id);

    }

    private static void batchRoute(String sessionId, Protocol protocol) throws Exception {

        List<OfSubscriber> subscriberList = subscriberDao.findSubscribers(sessionId);

        if(subscriberList == null){
            return;
        }

        Iterator<OfSubscriber> iterator = subscriberList.iterator();

        while(iterator.hasNext()){

            OfSubscriber ofSubscriber = iterator.next();

            String msgTo = ofSubscriber.getUser_id();

            if(StringUtils.isNullOrEmpty(msgTo)){
                continue;
            }


            OfMessage ofMessage = new OfMessage();

            ofMessage.setMsg_id(protocol.getMsg_id());

            ofMessage.setMsg_type(protocol.getMsg_type());

            ofMessage.setMsg_from(msgTo);

            ofMessage.setMsg_to(msgTo);

            ofMessage.setMsg_time(protocol.getMsg_time());

            ofMessage.setBody(protocol.getBody());

            ofMessage.setSession_id(sessionId);

            chatDao.addThenSend(ofMessage);

            Message newMessage = new Message();

            newMessage.setType(Message.Type.chat);

            newMessage.setFrom(new JID(MessageUtils.toJid(msgTo)));

            newMessage.setTo(new JID(MessageUtils.toJid(msgTo)));

            protocol.setMsg_from(msgTo);

            protocol.setMsg_to(msgTo);

            newMessage.setBody(JSONObject.toJSONString(protocol));

            packetDeliverer.deliver(newMessage);



        }
    }

    private static class Body{

        public Body(String session_id) {
            this.session_id = session_id;
        }

        private String session_id;

        public String getSession_id() {
            return session_id;
        }

        public void setSession_id(String session_id) {
            this.session_id = session_id;
        }
    }
}
