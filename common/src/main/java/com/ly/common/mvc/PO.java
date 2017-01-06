package com.ly.common.mvc;

import com.ly.common.X;

/**
 * Copyright (C) 2015 GO2.CN. All rights reserved. This computer program source
 * code file is protected by copyright law and international treaties.
 * Unauthorized distribution of source code files, programs, or portion of the
 * package, may result in severe civil and criminal penalties, and will be
 * prosecuted to the maximum extent under the law.
 * 
 * Only the class which inherit the DAO interface will be scanned by mybatis as mapper
 * 
 * @author gaofeng
 * @since 2015-10-30
 * @deprecated
 */
public class PO {
  private String  id;
  private boolean stored = Boolean.FALSE;

  public PO() {
    id = X.uuid();
  }

  public String getId() {
    if (id == null) {
      id = X.uuid();
    }
    return id;
  }

  public void setId(String id) {
    this.id = id;
    setStored(Boolean.TRUE);
  }

  public boolean isStored() {
    return stored;
  }

  public void setStored(boolean stored) {
    this.stored = stored;
  }

}
