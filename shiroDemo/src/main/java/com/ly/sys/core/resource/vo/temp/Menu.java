package com.ly.sys.core.resource.vo.temp;

import java.beans.Transient;
import java.io.Serializable;
import java.util.List;

import com.google.common.collect.Lists;
import com.google.gson.annotations.Expose;

/**
 * 界面是那个使用的菜单对象
 * <p>User: mtwu
 * <p>Date: 13-4-9 下午4:20
 * <p>Version: 1.0
 */
public class Menu implements Serializable {

    private static final long serialVersionUID = 1L;
    @Expose
    private Long id;
    @Expose
    private String name;
    @Expose
    private String source;
    @Expose
    private String icon;
    @Expose
    private String url;
    @Expose
    private String identity;
    @Expose
    private List<Menu> children;

    public Menu(Long id, String name, String icon, String url) {
        this.id = id;
        this.name = name;
        this.icon = icon;
        this.url = url;
    }
    
    public Menu(Long id, String name, String source, String icon, String url, String identity) {
        this.id = id;
        this.name = name;
        this.source = source;
        this.icon = icon;
        this.url = url;
        this.identity = identity;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    
    public String getSource() {
      return source;
    }

    public void setSource(String source) {
      this.source = source;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public List<Menu> getChildren() {
        if (children == null) {
            children = Lists.newArrayList();
        }
        return children;
    }

    public void setChildren(List<Menu> children) {
        this.children = children;
    }

    /**
     * @return
     */
    public boolean isHasChildren() {
        return !getChildren().isEmpty();
    }
    
    public String getIdentity() {
    return identity;
  }

  public void setIdentity(String identity) {
    this.identity = identity;
  }

  @Override
    public String toString() {
        return "Menu{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", icon='" + icon + '\'' +
                ", url='" + url + '\'' +
                ", identity='" + identity + '\'' +
                ", children=" + children +
                '}';
    }
}