package com.longrise.msaas.global.action;

import com.longrise.msaas.global.domain.APIException;
import com.longrise.msaas.global.domain.EntityBean;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * 对 resultset 结果集进一步封装处理
 */
@Component
public class ResultSetAction{

    /**
     * 封装结果集为对象数组
     * @param rs 结果集
     * @return 返回封装数据
     * @throws SQLException 异常
     */
    public EntityBean[] dataToBeans(ResultSet rs) throws SQLException {
        List<EntityBean> beans = new ArrayList<>(8);
        ResultSetMetaData md = rs.getMetaData();
        while(rs.next()){
            EntityBean bean = new EntityBean();
            for (int j = 1, columnCount = md.getColumnCount(); j <= columnCount; j++) {
                bean.put(md.getColumnName(j), rs.getObject(j));
            }
            beans.add(bean);
        }
        return beans.toArray(EntityBean[]::new);
    }

    /**
     * 封装结果集到对象
     * @param rs 结果集
     * @return 封装后的对象
     * @throws SQLException sql执行异常
     * @throws DataAccessException 数据访问异常
     */
    public EntityBean dataToBean(ResultSet rs) throws SQLException, DataAccessException {
        ResultSetMetaData md = rs.getMetaData();
        EntityBean bean = null;
        if (rs.next()){
            bean = new EntityBean();
            for (int j = 1, columnCount = md.getColumnCount(); j <= columnCount; j++) {
                bean.put(md.getColumnName(j), rs.getObject(j));
            }
        }
        if(rs.next()){
            throw new APIException("查询到的结果不唯一");
        }
        return bean;
    }
}
