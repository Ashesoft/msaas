package com.longrise.msaas.global.excutor;

import com.longrise.msaas.global.action.ResultSetAction;
import com.longrise.msaas.global.domain.APIException;
import com.longrise.msaas.global.domain.EntityBean;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


@Component("jabcExcutor")
public class JDBCExcutor implements ApplicationContextAware {

    @Autowired
    private NamedParameterJdbcTemplate jdbcTemplate;

    @Autowired
    private ResultSetAction resultSetAction;

    /* springboot bean 容器 */
    private static ApplicationContext context;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        JDBCExcutor.context = applicationContext;
    }

    /**
     * 在容器中获取想要的bean对象
     * @param clazz 想要的对象全限定名
     * @param <T> 对象类型
     * @return 返回想要的bean对象
     */
    public static <T> T getBean(Class<T> clazz){
        return JDBCExcutor.context.getBean(clazz);
    }

    /**
     * 查询多条数据
     * @param sql 执行的语句
     * @return 返回多条数据
     */
    public EntityBean[] querys(String sql){
        return jdbcTemplate.query(sql, resultSetAction::dataToBeans);
    }
    /**
     * 通过条件查询多条数据
     * @param sql 执行的语句
     * @return 返回多条数据
     */
    public EntityBean[] querys(String sql, EntityBean bean){
        return jdbcTemplate.query(sql, bean, resultSetAction::dataToBeans);
    }

    /**
     * 查询一条数据
     * @param sql 执行的语句
     * @return 返回一条数据
     */
    public EntityBean query(String sql){
        return jdbcTemplate.query(sql, resultSetAction::dataToBean);
    }

    /**
     * 通过条件查询一条数据
     * @param sql 执行的sql语句
     * @param bean 条件封装的对象
     * @return 用户信息
     */
    public EntityBean query(String sql, EntityBean bean){
        return jdbcTemplate.query(sql, bean, resultSetAction::dataToBean);
    }

    /**
     * 获取给定数据表的主键
     * @param tableName 要获取主键的数据表名
     * @return 主键
     */
    public String[] getPriKey(String tableName){
        try{
            DataSource dataSource = Objects.requireNonNull(jdbcTemplate.getJdbcTemplate().getDataSource());
            DatabaseMetaData metaData = dataSource.getConnection().getMetaData();
            ResultSet priKeyInfo = metaData.getPrimaryKeys(null, "%", tableName);
            priKeyInfo.getMetaData();
            List<String> priKeys = new ArrayList<>(6);
            while (priKeyInfo.next()){
                priKeys.add(priKeyInfo.getString("COLUMN_NAME"));
            }
            if(priKeys.isEmpty()){
                throw new APIException(5002, String.format("[%s]数据表没有主键", tableName));
            }
            return priKeys.toArray(String[]::new);
        }catch (SQLException e){
            throw new APIException(5002, e.getMessage());
        }
    }

    /**
     * 新增/修改/删除 -- 不是批量
     * @param sql 执行语句
     * @param bean 执行时要绑定的数据
     * @return t|f
     */
    @Transactional(rollbackFor=Exception.class)
    public boolean insertUpdateDelete(String sql, EntityBean bean){
        return jdbcTemplate.update(sql, bean) > 0;
    }

    /**
     * 新增/修改/删除 -- 批量
     * @param sql 执行语句
     * @param beans 批量数据
     * @return t|f
     */
    @Transactional(rollbackFor=Exception.class)
    public boolean insertUpdateDeletes(String sql, EntityBean[] beans){
        /* 返回sql执行后影响的行数记录数组 */
       int[] res = jdbcTemplate.batchUpdate(sql, beans);
        return res.length > 0;
    }
}
