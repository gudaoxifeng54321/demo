package com.ly.sys.common.entity;

import com.google.gson.annotations.Expose;

public abstract class BaseEntity extends AbstractEntity<Long> {
    @Expose
    protected Long id;
    
    public Long getId() {
      return id;
    }

    public void setId(Long id) {
      this.id = id;
    }
  }
