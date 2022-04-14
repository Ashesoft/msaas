package com.longrise.msaas.mapping;

import com.longrise.msaas.global.domain.EntityBean;
import com.longrise.msaas.global.excutor.JDBCExcutor;
import com.longrise.msaas.global.utils.IdWorker;
import org.springframework.stereotype.Repository;

@Repository
public class WeChatMapping {
  private final JDBCExcutor jdbcExcutor;

  private final IdWorker idWorker;

  public WeChatMapping(JDBCExcutor jdbcExcutor) {
    this.jdbcExcutor = jdbcExcutor;
    this.idWorker = new IdWorker(1, 1, 1);
  }


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
  public String addWeChatCfg(EntityBean cfg) {
    long id = idWorker.nextId();
    cfg.put("id", id);
    cfg.put("beanname", "wechatconfig");
    if (cfg.insert()) {
      return String.valueOf(id);
    }
    return "配置失败";
  }
}
