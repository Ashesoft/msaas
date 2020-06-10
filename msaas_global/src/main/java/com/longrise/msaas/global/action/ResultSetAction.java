package com.longrise.msaas.global.action;

import com.longrise.msaas.global.domain.APIException;
import com.longrise.msaas.global.domain.EntityBean;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Component;

import java.sql.PreparedStatement;
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
     * @param rowNum 当前行号
     * @return 返回封装数据
     * @throws SQLException 异常
     */
    public EntityBean[] dataToBeans(ResultSet rs, int rowNum) throws SQLException {
        List<EntityBean> beans = new ArrayList<>(8);
        ResultSetMetaData md = rs.getMetaData();
        do{
            EntityBean bean = new EntityBean();
            for (int j = 1, columnCount = md.getColumnCount(); j <= columnCount; j++) {
                bean.put(md.getColumnName(j), rs.getObject(j));
            }
            beans.add(bean);
        }while(rs.next());
        EntityBean[] entityBean = new EntityBean[beans.size()];
        return beans.toArray(entityBean);
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

    /**
     *
     * @param ps prepareStatement
     * @return 数据表的主键
     * @throws SQLException sql语句异常
     * @throws DataAccessException 数据访问异常
     */
    public String dataToString(PreparedStatement ps) throws SQLException, DataAccessException {
         /* catalog-目录名称(数据库名称)；必须与存储在数据库中的目录名称匹配； “”检索那些没有目录的内容； null表示不应使用目录名称来缩小搜索范围; 如springboot */
         /* schema-模式名称；必须与存储在数据库中的架构名称匹配； “”检索那些没有模式的文件； null表示不应使用架构名称来缩小搜索范围 */
         /* table-表格名称；必须与存储在数据库中的表名匹配 */
        ResultSet rs = ps.getConnection().getMetaData().getPrimaryKeys(null, "%", ps.getMetaData().getTableName(1));
        String pk = null;
        /* TABLE_CAT TABLE_SCHEM TABLE_NAME COLUMN_NAME KEY_SEQ PK_NAME */
        /* 数据库名称 模式 数据表名称 列名 主键内的序列号 主键名称(PKIMARY) */
         if(rs.next()){
            pk = rs.getString("COLUMN_NAME");
        }
        return pk;
    }
}
