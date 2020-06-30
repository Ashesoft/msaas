package com.longrise.msaas.global.excutor;

import com.longrise.msaas.global.action.ResultSetAction;
import com.longrise.msaas.global.domain.EntityBean;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


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
        List<EntityBean[]> list = jdbcTemplate.query(sql, resultSetAction::dataToBeans);
        return list.isEmpty() ? null : list.get(0);
    }
    /**
     * 通过条件查询多条数据
     * @param sql 执行的语句
     * @return 返回多条数据
     */
    public EntityBean[] querys(String sql, EntityBean bean){
        List<EntityBean[]> list = jdbcTemplate.query(sql, bean, resultSetAction::dataToBeans);
        return list.isEmpty() ? null : list.get(0);
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

    public String getPriKey(String sql){
        return jdbcTemplate.execute(sql, resultSetAction::dataToString);
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
     * 批量新增/修改/删除
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
