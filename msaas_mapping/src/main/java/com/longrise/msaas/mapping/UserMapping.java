package com.longrise.msaas.mapping;

import com.longrise.msaas.global.domain.EntityBean;
import com.longrise.msaas.global.utils.RundomUid;
import org.springframework.stereotype.Repository;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Repository
public class UserMapping {
    public EntityBean[] getAllUserInfo(Integer count){
        List<EntityBean> eList = new ArrayList<>(count);
        for (int i = 0; i < count; i++) {
            EntityBean eBean = new EntityBean(count);
            eBean.put("uuid", RundomUid.get20BItUid());
            eBean.put("name", "张三" + i);
            eBean.put("age", new Random().nextInt(100));
            eBean.put("address", "中国");
            eList.add(eBean);
        }
        /**
         * 创建数组方法一
         */
        EntityBean[] eBeans = (EntityBean[]) Array.newInstance(EntityBean.class, eList.size());
        eList.toArray(eBeans);
        /**
         * 创建数组方法二
         */
        //EntityBean[] eBeans = new EntityBean[eList.size()];
        //System.arraycopy(eList.toArray(), 0, eBeans, 0, eList.size());
        //Global.getInstance().logInfo("实体绑定成功");
        return eBeans;
    }
}
