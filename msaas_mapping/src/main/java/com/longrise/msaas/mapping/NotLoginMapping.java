package com.longrise.msaas.mapping;

import com.longrise.msaas.global.domain.EntityBean;
import com.longrise.msaas.global.excutor.JDBCExcutor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class NotLoginMapping {
    @Autowired
    private JDBCExcutor jdbcExcutor;

    public EntityBean[] queryPhoneAreaCode(){
        String sql = "select areaname, areacode from phoneareacode";
        return jdbcExcutor.querys(sql);
    }

    public EntityBean isOwnerUserByPhone(EntityBean bean){
        String sql = "select id, uname, uphone, uemail, usex, ucardnum, uimg from userinfo where uphone=:uphone and upassword=:upassword";
        return jdbcExcutor.query(sql, bean);
    }
}
