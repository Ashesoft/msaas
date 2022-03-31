package com.longrise.msaas.mapping;


import com.longrise.msaas.global.domain.EntityBean;
import com.longrise.msaas.global.excutor.JDBCExcutor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public class AudioMapping {

  @Autowired
  private JDBCExcutor jdbcExcutor;

  public String[] getPK(String tableName) {
    return jdbcExcutor.getPriKey(tableName);
  }

  /**
   * 获取音频列表
   *
   * @return 列表
   */
  public EntityBean[] getAllAudio() {
    String sql = "select * from audiolist";
    return jdbcExcutor.querys(sql);
  }

  /**
   * 获取音频列表
   *
   * @return 列表
   */
  public EntityBean getAudioInfo() {
    String sql = "select * from audiolist where id>328";
    return jdbcExcutor.query(sql);
  }

  /**
   * 批量向数据库新增数据
   *
   * @param beans Map数组
   * @return t || f
   */
  @Transactional(rollbackFor = Exception.class)
  public boolean insetAudioInfo(EntityBean[] beans) {
    String sql = "insert into audiolist (id, createtime, imgsrc, audiosrc, programtitle, podcaster, channeltitle, timetotal, progress) " +
      "values (:id, :createtime, :imgsrc, :audiosrc, :programtitle, :podcaster, :channeltitle, :timetotal, :progress)";
    return jdbcExcutor.insertUpdateDeletes(sql, beans);
  }

  /**
   * 常用CURD操作大致使用以下三个方法:
   * 1.execute方法，用于直接执行SQL语句
   * 2.update方法，用户新增修改删除操作 -- 其中有个批量的操作
   * 3.query方法，用于查询方法
   */
}
