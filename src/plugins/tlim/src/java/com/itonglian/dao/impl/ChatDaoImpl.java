package com.itonglian.dao.impl;

import com.itonglian.dao.ChatDao;
import com.itonglian.entity.OfMessage;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.jivesoftware.database.DbConnectionManager;
import org.jivesoftware.database.SequenceManager;
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

    private static final String INSERT_WITH_SESS = "INSERT INTO ofmessage (id_,msg_id,msg_type,msg_from,msg_to,msg_time,body,session_id) VALUES(?,?,?,?,?,?,?,?)";

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
            preparedStatement = connection.prepareStatement(INSERT_WITH_SESS);
            int i=1;
            preparedStatement.setLong(i++,SequenceManager.nextID(OfMessage.ID_Contants.MSG_KEY));
            preparedStatement.setString(i++,ofMessage.getMsg_id());
            preparedStatement.setString(i++,ofMessage.getMsg_type());
            preparedStatement.setString(i++,ofMessage.getMsg_from());
            preparedStatement.setString(i++,ofMessage.getMsg_to());
            preparedStatement.setString(i++,ofMessage.getMsg_time());
            preparedStatement.setString(i++,ofMessage.getBody());
            preparedStatement.setString(i++,ofMessage.getSession_id());
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
                ofMessage.setId_(resultSet.getLong("id_"));
                ofMessage.setMsg_id(resultSet.getString("msg_id"));
                ofMessage.setMsg_type(resultSet.getString("msg_type"));
                ofMessage.setMsg_from(resultSet.getString("msg_from"));
                ofMessage.setMsg_type(resultSet.getString("msg_to"));
                ofMessage.setMsg_time(resultSet.getString("msg_time"));
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
