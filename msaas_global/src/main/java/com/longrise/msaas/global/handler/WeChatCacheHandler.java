package com.longrise.msaas.global.handler;

import com.longrise.msaas.global.domain.EntityBean;
import com.longrise.msaas.global.excutor.JDBCExcutor;
import com.longrise.msaas.global.utils.IdWorker;
import org.springframework.stereotype.Repository;

import java.util.Date;

@Repository
public class WeChatCacheHandler {
  private final JDBCExcutor jdbcExcutor;

  private final IdWorker idWorker;

  public WeChatCacheHandler(JDBCExcutor jdbcExcutor) {
    this.jdbcExcutor = jdbcExcutor;
    this.idWorker = new IdWorker(1, 1, 1);
  }

  private EntityBean getTokenBean() {
    EntityBean tokenBean = new EntityBean();
    tokenBean.put("beanname", "wechatcache");
    tokenBean.put("updatetime", new Date());
    return tokenBean;
  }

  /**
   * 获取token
   *
   * @param tokenkey token键名
   * @return 公众号配置信息
   */
  public EntityBean getWeChatToken(String tokenkey) {
    String sql = "select tokenval,expire from wechatcache where tokenkey=:tokenkey";
    EntityBean bean = new EntityBean(1);
    bean.put("tokenkey", tokenkey);
    return jdbcExcutor.query(sql, bean);
  }

  /**
   * 添加token信息
   *
   * @param tokenkey 键
   * @param tokenval 值
   * @return 是否成功
   */
  public boolean addWeChatToken(int expire, String tokenkey, String tokenval) {
    EntityBean tokenBean = getTokenBean();
    tokenBean.put("id", idWorker.nextId());
    tokenBean.put("tokenkey", tokenkey);
    tokenBean.put("tokenval", tokenval);
    tokenBean.put("expire", System.currentTimeMillis() + expire * 1000L);
    return tokenBean.insert();
  }
}
