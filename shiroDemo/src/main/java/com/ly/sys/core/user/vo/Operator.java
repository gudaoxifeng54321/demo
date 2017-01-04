package com.ly.sys.core.user.vo;

import java.util.Date;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.springframework.format.annotation.DateTimeFormat;

import com.google.gson.annotations.Expose;
import com.ly.sys.core.permission.vo.Role;
import com.ly.sys.core.resource.vo.temp.Menu;

/**
 * <p>Description: this is Operator bean
 * <p>User: mtwu
 * <p>Date: 2016-6-12 16:33:31
 * <p>Version: 1.0
 */

public class Operator extends BaseUser {

  // 平台来源标识
  @Expose
  private String             source;
  @Expose
  private String             email;
  // 电话号码
  @Expose
  private String             mobile;
  // 用户状态
  @Expose
  private Integer            status;
  // 是否管理员 0否 1是 管理员拥有权限传递功能
  @Expose
  private Integer            isAdmin;
  //
  private String             deleted                     = "0";
  //
  @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
  @Expose
  private Date               createDate;

  // 操作者登录IP
  @Expose
  private String             host;

  // 多对多关系
  @Expose
  private Set<Role>         roles;
  @Expose
  private Set<String>         roleStrs;
  @Expose
  private List<Menu>         menus;
  @Expose
  private Set<String>        userPermissions;
  @Expose
  private OperatorLastOnline operatorLastOnline;

  public static final String USERNAME_PATTERN            = "^[\\u4E00-\\u9FA5\\uf900-\\ufa2d_a-zA-Z][\\u4E00-\\u9FA5\\uf900-\\ufa2d\\w]{1,19}$";
  public static final String EMAIL_PATTERN               = "^((([a-z]|\\d|[!#\\$%&'\\*\\+\\-\\/=\\?\\^_`{\\|}~]|[\\u00A0-\\uD7FF\\uF900-\\uFDCF\\uFDF0-\\uFFEF])+(\\.([a-z]|\\d|[!#\\$%&'\\*\\+\\-\\/=\\?\\^_`{\\|}~]|[\\u00A0-\\uD7FF\\uF900-\\uFDCF\\uFDF0-\\uFFEF])+)*)|((\\x22)((((\\x20|\\x09)*(\\x0d\\x0a))?(\\x20|\\x09)+)?(([\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x7f]|\\x21|[\\x23-\\x5b]|[\\x5d-\\x7e]|[\\u00A0-\\uD7FF\\uF900-\\uFDCF\\uFDF0-\\uFFEF])|(\\\\([\\x01-\\x09\\x0b\\x0c\\x0d-\\x7f]|[\\u00A0-\\uD7FF\\uF900-\\uFDCF\\uFDF0-\\uFFEF]))))*(((\\x20|\\x09)*(\\x0d\\x0a))?(\\x20|\\x09)+)?(\\x22)))@((([a-z]|\\d|[\\u00A0-\\uD7FF\\uF900-\\uFDCF\\uFDF0-\\uFFEF])|(([a-z]|\\d|[\\u00A0-\\uD7FF\\uF900-\\uFDCF\\uFDF0-\\uFFEF])([a-z]|\\d|-|\\.|_|~|[\\u00A0-\\uD7FF\\uF900-\\uFDCF\\uFDF0-\\uFFEF])*([a-z]|\\d|[\\u00A0-\\uD7FF\\uF900-\\uFDCF\\uFDF0-\\uFFEF])))\\.)+(([a-z]|[\\u00A0-\\uD7FF\\uF900-\\uFDCF\\uFDF0-\\uFFEF])|(([a-z]|[\\u00A0-\\uD7FF\\uF900-\\uFDCF\\uFDF0-\\uFFEF])([a-z]|\\d|-|\\.|_|~|[\\u00A0-\\uD7FF\\uF900-\\uFDCF\\uFDF0-\\uFFEF])*([a-z]|[\\u00A0-\\uD7FF\\uF900-\\uFDCF\\uFDF0-\\uFFEF])))\\.?";
  public static final String MOBILE_PHONE_NUMBER_PATTERN = "^0{0,1}(13[0-9]|15[0-9]|14[0-9]|18[0-9])[0-9]{8}$";

  public Set<String> getUserPermissions() {
    return userPermissions;
  }

  public void setUserPermissions(Set<String> userPermissions) {
    this.userPermissions = userPermissions;
  }

  public List<Menu> getMenus() {
    return menus;
  }

  public void setMenus(List<Menu> menus) {
    this.menus = menus;
  }

  public OperatorLastOnline getOperatorLastOnline() {
    return operatorLastOnline;
  }

  public void setOperatorLastOnline(OperatorLastOnline operatorLastOnline) {
    this.operatorLastOnline = operatorLastOnline;
  }

  public String getSource() {
    return this.source;
  }

  public void setSource(String source) {
    this.source = source;
  }

  public Operator() {

  }

  public Operator(Long id, String source, String username, String email, String mobile, String password, Integer status, Date createDate) {
    this.id = id;
    this.source = source;
    this.username = username;
    this.email = email;
    this.mobile = mobile;
    this.password = password;
    this.status = status;
    this.createDate = createDate;
  }

  public String getEmail() {
    return this.email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getMobile() {
    return this.mobile;
  }

  public void setMobile(String mobile) {
    this.mobile = mobile;
  }

  public Integer getStatus() {
    return this.status;
  }

  public void setStatus(Integer status) {
    this.status = status;
  }

  public Integer getIsAdmin() {
    return this.isAdmin;
  }

  public void setIsAdmin(Integer isAdmin) {
    this.isAdmin = isAdmin;
  }

  public String getDeleted() {
    return this.deleted;
  }

  public void setDeleted(String deleted) {
    this.deleted = deleted;
  }

  public Date getCreateDate() {
    return this.createDate;
  }

  public void setCreateDate(Date createDate) {
    this.createDate = createDate;
  }

  public Set<Role> getRoles() {
    return roles;
  }

  public void setRoles(Set<Role> roles) {
    this.roles = roles;
  }

  public Set<String> getRoleStrs() {
    return roleStrs;
  }

  public void setRoleStrs(Set<String> roleStrs) {
    this.roleStrs = roleStrs;
  }

  public String getHost() {
    return host;
  }

  public void setHost(String host) {
    this.host = host;
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }
}
