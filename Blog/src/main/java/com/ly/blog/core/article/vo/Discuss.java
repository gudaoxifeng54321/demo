package com.ly.blog.core.article.vo;


import org.apache.commons.lang.builder.ToStringBuilder;


 /**
 * <p>Description: this is Discuss bean
 * <p>User: mtwu
 * <p>Date: 2016-12-30 15:00:14
 * <p>Version: 1.0
 */

public class Discuss {
 	private Integer id;
					//
    private String content;
    
    private Integer articleId;
    		
	public Integer getId() {
    	return id;
  	}

  	public void setId(Integer id) {
    	this.id = id;
  	}
  
					
	public String getContent() {
		return this.content;
	}
	
	public void setContent(String content) {
		this.content = content;
	}
	
	
	
			public Integer getArticleId() {
		return articleId;
	}

	public void setArticleId(Integer articleId) {
		this.articleId = articleId;
	}

			@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
}
