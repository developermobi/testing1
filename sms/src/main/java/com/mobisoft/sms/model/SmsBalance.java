package com.mobisoft.sms.model;

import java.sql.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonFormat;

@Entity
@Table(name = "sms_balance")
public class SmsBalance {

	@Id
	@GeneratedValue
	@Column(name = "id")
	private int id;
	
	@OneToOne
	@JoinColumn(name = "user_id")
	private User userId;
	
	@OneToOne
	@JoinColumn(name = "product_id")
	private Product productId;
	
	@Column(name = "balance")
	private int balance;
	
	@Column(name = "sent_sms")
	private int sentSms;
	
	@Column(name = "expiry_date")
	private Date expiryDate;
	
	@Column(name = "created", columnDefinition="DATETIME", nullable=true)
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone="IST")
	private Date created;
	
	@Column(name = "updated", columnDefinition="TIMESTAMP", nullable=true)
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone="IST")
	private Date updated;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public User getUserId() {
		return userId;
	}

	public void setUserId(User userId) {
		this.userId = userId;
	}

	public Product getProductId() {
		return productId;
	}

	public void setProductId(Product productId) {
		this.productId = productId;
	}

	public int getBalance() {
		return balance;
	}

	public void setBalance(int balance) {
		this.balance = balance;
	}

	public int getSentSms() {
		return sentSms;
	}

	public void setSentSms(int sentSms) {
		this.sentSms = sentSms;
	}

	public Date getExpiryDate() {
		return expiryDate;
	}

	public void setExpiryDate(Date expiryDate) {
		this.expiryDate = expiryDate;
	}

	public Date getCreated() {
		return created;
	}

	public void setCreated(Date created) {
		this.created = created;
	}

	public Date getUpdated() {
		return updated;
	}

	public void setUpdated(Date updated) {
		this.updated = updated;
	}
	
	
}
