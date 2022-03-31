package com.longrise.msaas.global.domain;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.longrise.msaas.global.excutor.JDBCExcutor;

import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class EntityBean extends ConcurrentHashMap<String, Object> implements Serializable {
  private static final long serialVersionUID = 2919051104722035818L;

  /* 注意在使用new创建对象时, 对象里的自动化装配注解就会失效, 需要手动在ioc容器里面获取 */
  private JDBCExcutor jdbcExcutor = JDBCExcutor.getBean(JDBCExcutor.class);

  public EntityBean() {
    super();
  }

  public EntityBean(Object beanName) {
    this(8);
    this.put("beanname", beanName);
  }

  public EntityBean(int initSize) {
    super(initSize);
  }


  // 获取当前值的字符串值
  public String getString(String key) {
    Object value = super.get(key);
    if (Objects.isNull(value)) {
      return null;
    }
    return value.toString();
  }

  // 获取当前值的字节值
  public byte getByte(String key) {
    Object value = super.get(key);
    if (Objects.isNull(value)) {
      return (byte) 0;
    }
    try {
      return Byte.parseByte(value.toString().trim());
    } catch (NumberFormatException e) {
      throw new APIException(5001, e.getMessage());
    }
  }

  // 获取当前值的 short 值
  public short getShort(String key) {
    Object value = super.get(key);
    if (Objects.isNull(value)) {
      return (short) 0;
    }
    try {
      return Short.parseShort(value.toString().trim());
    } catch (NumberFormatException e) {
      throw new APIException(5001, e.getMessage());
    }
  }

  // 获取当前值的 int 值
  public int getInt(String key) {
    Object value = super.get(key);
    if (Objects.isNull(value)) {
      return 0;
    }
    try {
      return Integer.parseInt(value.toString().trim());
    } catch (NumberFormatException e) {
      throw new APIException(5001, e.getMessage());
    }
  }

  // 获取当前值的 long 值
  public long getLong(String key) {
    Object value = super.get(key);
    if (Objects.isNull(value)) {
      return 0L;
    }
    try {
      return Long.parseLong(value.toString().trim());
    } catch (NumberFormatException e) {
      throw new APIException(5001, e.getMessage());
    }
  }

  // 获取当前值的 float 值
  public float getFloat(String key) {
    Object value = super.get(key);
    if (Objects.isNull(value)) {
      return 0f;
    }
    try {
      return Float.parseFloat(value.toString().trim());
    } catch (NumberFormatException e) {
      throw new APIException(5001, e.getMessage());
    }
  }

  // 获取当前值的 double 值
  public double getDouble(String key) {
    Object value = super.get(key);
    if (Objects.isNull(value)) {
      return 0d;
    }
    try {
      return Double.parseDouble(value.toString().trim());
    } catch (NumberFormatException e) {
      throw new APIException(5001, e.getMessage());
    }
  }

  /**
   * 修改(更新)操作
   *
   * @return 操作是否成功
   */
  public boolean update() {
    String beanName = Optional.ofNullable(this.getString("beanname")).orElseThrow(() -> new APIException(5002, "没有绑定表名"));
    String[] pks = jdbcExcutor.getPriKey(beanName);
    Object id = null;
    String pk = null;
    for (String s : pks) {
      pk = s;
      id = this.get(pk);
      if (Objects.nonNull(id)) {
        break;
      }
    }
    id = Optional.ofNullable(id).orElseThrow(() -> new APIException(5002, "没有绑定主键"));
    this.remove("beanname");
    this.remove(pk);

    StringBuffer buffer = new StringBuffer("update ");
    buffer.append(beanName).append(" set ");
    this.forEach((k, v) -> buffer.append(k).append("=:").append(k).append(','));
    buffer.deleteCharAt(buffer.lastIndexOf(","));
    buffer.append(" where ").append(pk).append("=:").append(pk);
    this.put(pk, id);
    System.out.println("update = " + buffer.toString());
    return jdbcExcutor.insertUpdateDelete(buffer.toString(), this);
  }

  /**
   * 新增操作
   *
   * @return 操作是否成功
   */
  public boolean insert() {
    String beanName = Optional.ofNullable(this.getString("beanname")).orElseThrow(() -> new APIException("没有绑定beanname"));
    this.remove("beanname");

    StringBuffer buffer = new StringBuffer("insert into ");
    buffer.append(beanName).append("(");
    StringBuffer values = new StringBuffer();
    this.forEach((k, v) -> {
      buffer.append(k).append(',');
      values.append(":").append(k).append(',');
    });
    buffer.deleteCharAt(buffer.lastIndexOf(","));
    buffer.append(")values(");
    values.deleteCharAt(values.lastIndexOf(","));
    buffer.append(values).append(')');
    System.out.println("insert = " + buffer.toString());
    return jdbcExcutor.insertUpdateDelete(buffer.toString(), this);
  }

  /**
   * 删除操作
   *
   * @return 操作是否成功
   */
  public boolean delete() {
    String beanName = Optional.ofNullable(this.getString("beanname")).orElseThrow(() -> new APIException("没有绑定beanname"));
    this.remove("beanname");
    StringBuffer buffer = new StringBuffer("delete from ");
    buffer.append(beanName).append(" where ");
    this.forEach((k, v) -> buffer.append(k).append("=:").append(k).append(" and "));
    buffer.delete(buffer.lastIndexOf(" and "), buffer.length() - 1);
    System.out.println("delete = " + buffer.toString());
    return jdbcExcutor.insertUpdateDelete(buffer.toString(), this);
  }


  @Override
  public String toString() {
    return toJsonString();
  }

  /**
   * 使用jackjson序列化当前对象
   *
   * @return 序列化后的字符串
   */
  public String toJsonString() {
    try {
      return new ObjectMapper().writeValueAsString(this);
    } catch (JsonProcessingException e) {
      throw new APIException("转成json字符串失败");
    }
  }
}
