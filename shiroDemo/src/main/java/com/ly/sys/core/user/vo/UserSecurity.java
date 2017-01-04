package com.ly.sys.core.user.vo;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.ly.sys.common.entity.BaseEntity;

/**
 * <p>
 * Description: this is UserSecurity bean
 * <p>
 * User: mtwu
 * <p>
 * Date: 2016-6-12 16:33:31
 * <p>
 * Version: 1.0
 */

public class UserSecurity extends BaseEntity {

	// 用户ID
	private Long userId;
	// 安全问题a
	private String aQuestion;
	// 安全答案a
	private String aAnswer;
	// 安全问题b
	private String bQuestion;
	// 安全答案b
	private String bAnswer;
	// 安全问题c
	private String cQuestion;
	// 安全答案c
	private String cAnswer;
	// 安全邮箱
	private String safeEmail;

	public Long getUserId() {
		return this.userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getAQuestion() {
		return this.aQuestion;
	}

	public void setAQuestion(String aQuestion) {
		this.aQuestion = aQuestion;
	}

	public String getAAnswer() {
		return this.aAnswer;
	}

	public void setAAnswer(String aAnswer) {
		this.aAnswer = aAnswer;
	}

	public String getBQuestion() {
		return this.bQuestion;
	}

	public void setBQuestion(String bQuestion) {
		this.bQuestion = bQuestion;
	}

	public String getBAnswer() {
		return this.bAnswer;
	}

	public void setBAnswer(String bAnswer) {
		this.bAnswer = bAnswer;
	}

	public String getCQuestion() {
		return this.cQuestion;
	}

	public void setCQuestion(String cQuestion) {
		this.cQuestion = cQuestion;
	}

	public String getCAnswer() {
		return this.cAnswer;
	}

	public void setCAnswer(String cAnswer) {
		this.cAnswer = cAnswer;
	}

	public String getSafeEmail() {
		return this.safeEmail;
	}

	public void setSafeEmail(String safeEmail) {
		this.safeEmail = safeEmail;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
}
