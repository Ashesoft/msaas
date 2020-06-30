package com.longrise.msaas.mapping;


import com.longrise.msaas.global.domain.EntityBean;
import com.longrise.msaas.global.excutor.JDBCExcutor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public class StoryMapping {

    @Autowired
    private JDBCExcutor jdbcExcutor;

    /**
     * 获取故事目录
     * @return 列表
     */
    public EntityBean[] getBookList(){
        String sql = "select sid, stitlename from storybook";
        return jdbcExcutor.querys(sql);
    }

    /**
     * 获取对应章回内容
     * @return 列表
     */
    public EntityBean getBookContent(Long sid){
        String sql = "select * from storybook where sid=:sid";
        EntityBean bean = new EntityBean(1);
        bean.put("sid", sid);
        return jdbcExcutor.query(sql, bean);
    }

    /**
     * 批量向数据库新增故事书数据
     * @param beans Map数组
     * @return
     */
    @Transactional(rollbackFor=Exception.class)
    public boolean insetBookInfo(EntityBean[] beans){
        String sql = "insert into storybook (sid, createtime, sbookname, stitlename, scontent) " +
                "values (:sid, :createtime, :sbookname, :stitlename, :scontent)";
        return jdbcExcutor.insertUpdateDeletes(sql, beans);
    }

    /**
     * 常用CURD操作大致使用以下三个方法:
     * 1.execute方法，用于直接执行SQL语句
     * 2.update方法，用户新增修改删除操作 -- 其中有个批量的操作
     * 3.query方法，用于查询方法
     */
}
