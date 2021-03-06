package com.itonglian.dao.impl;

import com.itonglian.dao.OfflineDao;
import com.itonglian.entity.OfCustomOffline;
import com.itonglian.enums.DBType;
import com.itonglian.mapper.mysql.OfflineMapper;
import com.itonglian.utils.DBUtils;
import com.itonglian.utils.MyBatisSessionFactory;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class OfflineDaoImpl implements OfflineDao {

    private static final OfflineDao offlineDao = new OfflineDaoImpl();

    public static OfflineDao getInstance(){
        return offlineDao;
    }

    private static final Logger Log = LoggerFactory.getLogger(OfflineDaoImpl.class);


    @Override
    public void add(OfCustomOffline ofCustomOffline) {
        SqlSessionFactory sqlSessionFactory = MyBatisSessionFactory.getInstance().createSessionFactory();
        SqlSession session = sqlSessionFactory.openSession();
        try {
            if(DBUtils.getDBType()== DBType.SQLServer){
                com.itonglian.mapper.sqlserver.OfflineMapper offlineMapper = session.getMapper(com.itonglian.mapper.sqlserver.OfflineMapper.class);
                ofCustomOffline.setId_(UUID.randomUUID().toString());
                offlineMapper.insertOffline(ofCustomOffline);
            }else{
                OfflineMapper offlineMapper = session.getMapper(OfflineMapper.class);
                ofCustomOffline.setId_(UUID.randomUUID().toString());
                offlineMapper.insertOffline(ofCustomOffline);
            }
            session.commit();
        } catch (Exception e){
            Log.error(ExceptionUtils.getFullStackTrace(e));
            session.rollback();
        }finally {
            session.close();
        }
    }


    @Override
    public void updateStatus(String msg_id, int msg_status) {
        SqlSessionFactory sqlSessionFactory = MyBatisSessionFactory.getInstance().createSessionFactory();
        SqlSession session = sqlSessionFactory.openSession();
        OfflineMapper offlineMapper = session.getMapper(OfflineMapper.class);
        try {
            offlineMapper.updateStatus(msg_status,msg_id);
            session.commit();
        } catch (Exception e){
            Log.error(ExceptionUtils.getFullStackTrace(e));
            session.rollback();
        }finally {
            session.close();
        }
    }

    @Override
    public void delete(String msg_id) {
        SqlSessionFactory sqlSessionFactory = MyBatisSessionFactory.getInstance().createSessionFactory();
        SqlSession session = sqlSessionFactory.openSession();
        OfflineMapper offlineMapper = session.getMapper(OfflineMapper.class);
        try {
            offlineMapper.deleteByMsgId(msg_id);
            session.commit();
        } catch (Exception e){
            Log.error(ExceptionUtils.getFullStackTrace(e));
            session.rollback();
        }finally {
            session.close();
        }
    }

    @Override
    public void deleteByUser(String delete_user) {
        SqlSessionFactory sqlSessionFactory = MyBatisSessionFactory.getInstance().createSessionFactory();
        SqlSession session = sqlSessionFactory.openSession();
        OfflineMapper offlineMapper = session.getMapper(OfflineMapper.class);
        try {
            offlineMapper.deleteByUser(delete_user);
            session.commit();
        } catch (Exception e){
            Log.error(ExceptionUtils.getFullStackTrace(e));
            session.rollback();
        }finally {
            session.close();
        }
    }

    @Override
    public List<OfCustomOffline> findByUser(String user_id) {
        SqlSessionFactory sqlSessionFactory = MyBatisSessionFactory.getInstance().createSessionFactory();
        SqlSession session = sqlSessionFactory.openSession();
        List<OfCustomOffline> ofCustomOfflines = new ArrayList<>();
        OfflineMapper offlineMapper = session.getMapper(OfflineMapper.class);
        try {
            ofCustomOfflines = offlineMapper.findByUser(user_id);
        } catch (Exception e){
            Log.error(ExceptionUtils.getFullStackTrace(e));
        }finally {
            session.close();
        }
        return ofCustomOfflines;
    }

    @Override
    public List<OfCustomOffline> findByUserAfterThatTime(String user_id, String msg_time) {
        SqlSessionFactory sqlSessionFactory = MyBatisSessionFactory.getInstance().createSessionFactory();
        SqlSession session = sqlSessionFactory.openSession();
        List<OfCustomOffline> ofCustomOfflines = new ArrayList<>();
        OfflineMapper offlineMapper = session.getMapper(OfflineMapper.class);
        try {
            ofCustomOfflines = offlineMapper.findByUserAfterThatTime(user_id,msg_time);
        } catch (Exception e){
            Log.error(ExceptionUtils.getFullStackTrace(e));
        }finally {
            session.close();
        }
        return ofCustomOfflines;
    }

    @Override
    public OfCustomOffline findByMsgId(String msg_id) {
        SqlSessionFactory sqlSessionFactory = MyBatisSessionFactory.getInstance().createSessionFactory();
        SqlSession session = sqlSessionFactory.openSession();
        OfflineMapper offlineMapper = session.getMapper(OfflineMapper.class);
        try {
            List<OfCustomOffline> ofCustomOfflines = offlineMapper.findByMsgId(msg_id);
            if(ofCustomOfflines!=null&&ofCustomOfflines.size()>0){
                return ofCustomOfflines.get(0);
            }
        } catch (Exception e){
            Log.error(ExceptionUtils.getFullStackTrace(e));
        }finally {
            session.close();
        }
        return null;
    }

    @Override
    public List<OfCustomOffline> findAll() {
        SqlSessionFactory sqlSessionFactory = MyBatisSessionFactory.getInstance().createSessionFactory();
        SqlSession session = sqlSessionFactory.openSession();
        List<OfCustomOffline> ofCustomOfflines = new ArrayList<>();
        OfflineMapper offlineMapper = session.getMapper(OfflineMapper.class);
        try {
            ofCustomOfflines = offlineMapper.findAll();
        } catch (Exception e){
            Log.error(ExceptionUtils.getFullStackTrace(e));
        }finally {
            session.close();
        }
        return ofCustomOfflines;
    }

    @Override
    public void deleteBySession(String session_id) {
        SqlSessionFactory sqlSessionFactory = MyBatisSessionFactory.getInstance().createSessionFactory();
        SqlSession session = sqlSessionFactory.openSession();
        OfflineMapper offlineMapper = session.getMapper(OfflineMapper.class);
        try {
            offlineMapper.deleteBySession(session_id);
            session.commit();
        } catch (Exception e){
            Log.error(ExceptionUtils.getFullStackTrace(e));
            session.rollback();
        }finally {
            session.close();
        }
    }

    @Override
    public void deleteByUserAndId(String user_id, String msg_id) {
        SqlSessionFactory sqlSessionFactory = MyBatisSessionFactory.getInstance().createSessionFactory();
        SqlSession session = sqlSessionFactory.openSession();
        OfflineMapper offlineMapper = session.getMapper(OfflineMapper.class);
        try {
            offlineMapper.deleteByUserAndId(user_id,msg_id);
            session.commit();
        } catch (Exception e){
            Log.error(ExceptionUtils.getFullStackTrace(e));
            session.rollback();
        }finally {
            session.close();
        }
    }
}
