package com.ly.sys.core.resource.vo;


import java.util.List;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.google.gson.annotations.Expose;
import com.ly.sys.common.entity.BaseEntity;
import com.ly.sys.common.plugin.entity.Treeable;
import com.ly.sys.core.permission.vo.Permission;


 /**
 * <p>Description: this is Resource bean
 * <p>User: mtwu
 * <p>Date: 2016-6-12 16:33:31
 * <p>Version: 1.0
 */

public class Resource  extends BaseEntity implements Treeable<Long>{

					//资源名称
    @Expose
    private String name;
    			//平台标识
    @Expose
    private String source;
    			//资源唯一标识 如go2_index   平台标识_资源标识
    @Expose
    private String identity="";
    			//请求url
    @Expose
    private String url;
    			//父亲节点
    @Expose
    private Long parentId;
    			//父辈节点组成的字符串
    @Expose
    private String parentIds;
    			//图标class
    @Expose
    private String icon="ztree_file";
    			//排序种子 ，值越大越靠后
    @Expose
    private Integer weight;
    			//是否父亲节点 0否 1是
    @Expose
    private String isParent="0";
    			//是否显示 0否 1是
    @Expose
    private Integer isShow=0;
    			//是否删除 0否 1是
    @Expose
    private Integer deleted=0;
    
  //多对多关系
    @Expose
  private List<Permission> permissions;
					
	public String getName() {
		return this.name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
				
	public String getSource() {
		return this.source;
	}
	
	public void setSource(String source) {
		this.source = source;
	}
	
				
	public String getIdentity() {
		return this.identity;
	}
	
	public void setIdentity(String identity) {
		this.identity = identity;
	}
	
				
	public String getUrl() {
		return this.url;
	}
	
	public void setUrl(String url) {
		this.url = url;
	}
	
				
	public Long getParentId() {
		return this.parentId;
	}
	
	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}
	
				
	public String getParentIds() {
		return this.parentIds;
	}
	
	public void setParentIds(String parentIds) {
		this.parentIds = parentIds;
	}
	
				
	public String getIcon() {
		return this.icon;
	}
	
	public void setIcon(String icon) {
		this.icon = icon;
	}
	
				
	public Integer getWeight() {
		return this.weight;
	}
	
	public void setWeight(Integer weight) {
		this.weight = weight;
	}
	
				
	public String getIsParent() {
		return this.isParent;
	}
	
	public void setIsParent(String isParent) {
		this.isParent = isParent;
	}
	
				
	public Integer getIsShow() {
		return this.isShow;
	}
	
	public void setIsShow(Integer newStatus) {
		this.isShow = newStatus;
	}
	
				
	public Integer getDeleted() {
		return this.deleted;
	}
	
	public void setDeleted(Integer deleted) {
		this.deleted = deleted;
	}
	
		public List<Permission> getPermissions() {
    return permissions;
  }

  public void setPermissions(List<Permission> permissions) {
    this.permissions = permissions;
  }

    @Override
  	public String toString() {
  		return ToStringBuilder.reflectionToString(this);
  	}

    @Override
    public String getSeparator() {
      
      return "/";
    }

    @Override
    public String makeSelfAsNewParentIds() {
      
      return getParentIds() + getId() + getSeparator();
    }

    @Override
    public boolean isRoot() {
      if (getParentId() != null && getParentId() == 0) {
        return true;
      }
      return false;
    }

    @Override
    public boolean isLeaf() {
      if (isRoot()) {
        return false;
      }
      if (isHasChildren()) {
          return false;
      }

      return true;
    }

    @Override
    public boolean isHasChildren() {
      
      if(null==this.isParent||"0".equals(this.isParent)){
        return false;
      }
      
      return true;
    }

    @Override
    public String getRootDefaultIcon() {
      
      return "ztree_setting";
    }

    @Override
    public String getBranchDefaultIcon() {
      
      return "ztree_folder";
    }

    @Override
    public String getLeafDefaultIcon() {
      
      return "ztree_file";
    }
}
