package com.mobisoft.sms.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;


@Entity
@Table(name= "user_authrization")
public class UserAuthrization {

	@Id
	@GeneratedValue
	@Column(name = "id")
	private int userAuthrizationId;
	
	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name = "u_id")
	private User userId;
	
	@Column(name = "dnd_check",length=2 )	
	private String dndCheck;
	
	@Column(name = "spam_check",length=2)	
	private String spamCheck;
	
	@Column(name = "percentage",length=10)	
	private String percentage;
	
	
	public int getUserAuthrizationId() {
		return userAuthrizationId;
	}

	public void setUserAuthrizationId(int userAuthrizationId) {
		this.userAuthrizationId = userAuthrizationId;
	}

	public User getUserId() {
		return userId;
	}

	public void setUserId(User userId) {
		this.userId = userId;
	}

	public String getDndCheck() {
		return dndCheck;
	}

	public void setDndCheck(String dndCheck) {
		this.dndCheck = dndCheck;
	}

	
	public String getSpamCheck() {
		return spamCheck;
	}

	public void setSpamCheck(String spamCheck) {
		this.spamCheck = spamCheck;
	}

	public String getPercentage() {
		return percentage;
	}

	public void setPercentage(String percentage) {
		this.percentage = percentage;
	}




}
