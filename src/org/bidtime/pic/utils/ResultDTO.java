/*
 * $Id:$
 * Copyright 2017 ecarpo.com All rights reserved.
 */
package org.bidtime.pic.utils;

import java.io.Serializable;
import java.util.Collection;

/**
 * ResultDTO
 * 
 * @author Jades.He
 * @param <T>
 * @since 2017.06.13
 */
@SuppressWarnings("serial")
public class ResultDTO<T> implements Serializable {

  private boolean success = true;

  private Long total;

  private T data;

  private String msg;

  public ResultDTO() {
    super();
  }

  public ResultDTO(T data) {
    this.setData(data);
  }

  public ResultDTO(T data, Long total) {
    this.data = data;
    this.total = total;
  }

  @SuppressWarnings("rawtypes")
  public static ResultDTO error(String msg) {
    ResultDTO res = new ResultDTO<>();
    res.setSuccess(false);
    res.setMsg(msg);
    return res;
  }

  @SuppressWarnings("rawtypes")
  public static ResultDTO apply(int applies, String msg) {
    ResultDTO res = new ResultDTO<>();
    res.setSuccess(applies > 0 ? true : false);
    if (!res.isSuccess()) {
      res.setMsg(msg);
    }
    return res;
  }

  public boolean isSuccess() {
    return success;
  }

  public void setSuccess(boolean success) {
    this.success = success;
  }

  public Long getTotal() {
    return total;
  }

  public void setTotal(Long total) {
    this.total = total;
  }

  public T getData() {
    return data;
  }

  @SuppressWarnings("rawtypes")
  public void setData(T data) {
    if (data != null) {
      if (data instanceof Collection) {
        this.total = (this.total == null || this.total == 0) ? ((Collection) data).size()
            : this.total;
      }
    }
    this.data = data;
  }

  public String getMsg() {
    return msg;
  }

  public void setMsg(String msg) {
    this.msg = msg;
  }

}
