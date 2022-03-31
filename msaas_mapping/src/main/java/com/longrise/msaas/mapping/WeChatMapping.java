package com.longrise.msaas.mapping;

import com.longrise.msaas.global.domain.EntityBean;
import com.longrise.msaas.global.excutor.JDBCExcutor;
import com.longrise.msaas.global.utils.IdWorker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class WeChatMapping {
  @Autowired
  private JDBCExcutor jdbcExcutor;

  private IdWorker idWorker = new IdWorker(1, 1, 1);

  /**
   * 根据公众号id获取公众号配置信息
   *
   * @param wxid 公众号id
   * @return 公众号配置信息
   */
  public EntityBean getWeChatCfgById(String wxid) {
    String sql = "select * from wechatconfig where id=:wxid";
    EntityBean bean = new EntityBean(1);
    bean.put("wxid", wxid);
    return jdbcExcutor.query(sql, bean);
  }

  /**
   * 添加公众号配置
   *
   * @param cfg 配置信息
   * @return 是否成功
   */
  public boolean addWeChatCfg(EntityBean cfg) {
    cfg.put("id", idWorker.nextId());
    cfg.put("beanname", "wechatconfig");
    return cfg.insert();
  }
}
