package com.itonglian.dao;

import com.itonglian.entity.Message;
import com.itonglian.entity.OfChat;
import com.itonglian.entity.OfMessage;

import java.util.List;

public interface MessageDao {

    public List<OfMessage> findHistory(String session_id,int start, int length);

    public void insert(OfMessage ofMessage);

    public int findMessageTotal(String session_id);

    List<OfMessage> findChatHistory(String msg_from,String msg_to,int start, int length);

    public int findChatMessageTotal(String msg_from,String msg_to);

    public void deleteByUser(String session_id,String msg_from);

    public void deleteBySession(String session_id);

    public String findMessageTime(String msg_id);

    public List<Message> findMessageAfter(String msg_to, String msg_time);

}
