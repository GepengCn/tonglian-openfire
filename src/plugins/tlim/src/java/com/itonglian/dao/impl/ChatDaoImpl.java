package com.itonglian.dao.impl;

import com.itonglian.dao.ChatDao;
import com.itonglian.entity.OfMessage;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.jivesoftware.database.DbConnectionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;
import java.util.Map;

public class ChatDaoImpl implements ChatDao {

    private static final ChatDao chatDao = new ChatDaoImpl();

    private static final String INSERT = "INSERT INTO ofmessage (msg_id,msg_type,msg_from,msg_to,msg_time,body) VALUES(?,?,?,?,?,?)";

    private static final String DELETE = "DELETE FROM ofmessage WHERE msg_id=?";

    private static final String QUERY_BY_ID = "SELECT * FROM ofmessage WHERE msg_id = ?";

    private static final Logger Log = LoggerFactory.getLogger(ChatDaoImpl.class);

    public static ChatDao getInstance(){
        return chatDao;
    }

    @Override
    public void add(OfMessage ofMessage) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            connection = DbConnectionManager.getConnection();
            preparedStatement = connection.prepareStatement(INSERT);
            int i=1;
            preparedStatement.setString(i++,ofMessage.getMsgId());
            preparedStatement.setString(i++,ofMessage.getMsgType());
            preparedStatement.setString(i++,ofMessage.getMsgFrom());
            preparedStatement.setString(i++,ofMessage.getMsgTo());
            preparedStatement.setString(i++,ofMessage.getMsgTime());
            preparedStatement.setString(i++,ofMessage.getBody());
            preparedStatement.execute();
        }catch (Exception e){
            Log.error(ExceptionUtils.getFullStackTrace(e));
        }finally {
            DbConnectionManager.closeConnection(preparedStatement,connection);
        }


    }

    @Override
    public void delete(String msgId) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            connection = DbConnectionManager.getConnection();
            preparedStatement = connection.prepareStatement(DELETE);
            int i=1;
            preparedStatement.setString(i++,msgId);
            preparedStatement.execute();
        }catch (Exception e){
            Log.error(ExceptionUtils.getFullStackTrace(e));
        }finally {
            DbConnectionManager.closeConnection(preparedStatement,connection);
        }
    }

    @Override
    public void update(OfMessage ofMessage) {

    }

    @Override
    public OfMessage findEntityById(String msgId) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            connection = DbConnectionManager.getConnection();
            preparedStatement = connection.prepareStatement(QUERY_BY_ID);
            preparedStatement.setString(1,msgId);
            resultSet = preparedStatement.executeQuery();
            OfMessage ofMessage = new OfMessage();
            if(resultSet.next()){
                ofMessage.setMsgId(resultSet.getString("msg_id"));
                ofMessage.setMsgType(resultSet.getString("msg_type"));
                ofMessage.setMsgFrom(resultSet.getString("msg_from"));
                ofMessage.setMsgType(resultSet.getString("msg_to"));
                ofMessage.setMsgTime(resultSet.getString("msg_time"));
                ofMessage.setBody(resultSet.getString("body"));
                return ofMessage;
            }
        }catch (Exception e){
            Log.error(ExceptionUtils.getFullStackTrace(e));
        }finally {
            DbConnectionManager.closeConnection(resultSet,preparedStatement,connection);
        }
        return null;
    }

    @Override
    public List<OfMessage> findList(Map<String, Object> conditions) {
        return null;
    }
}
