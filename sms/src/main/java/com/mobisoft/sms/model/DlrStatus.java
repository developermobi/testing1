package com.mobisoft.sms.model;

import java.sql.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonFormat;

@Entity
@Table(name="dlr_status")
public class DlrStatus {

	@Id
	@GeneratedValue
	@Column(name = "id")
	private int id;
	
	@Column(name="user_id")
	private int userId;
	
	@Column(name="mobile")
	private String mobile;
	
	@Column(name="message")
	private String message;
	
	@Column(name="count")
	private int count;
	
	@Column(name="length")
	private int length;
	
	@Column(name="type")
	private int type;
	
	@Column(name="message_id")
	private String  messageId;
	
	@Column(name="mobi_class")
	private int mobiClass;
	
	@Column(name="coding")
	private int coding;
	
	@Column(name="status")
	private int status;
	
	@Column(name="errorCode")
	private int errorCode;	
	
	@Column(name = "logged_at", columnDefinition="TIMESTAMP", nullable=true)
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone="IST")
	private int loggedAt;
	
	@Column(name="dlr_time")
	private Date dlrTime;
	
	@Column(name="job_id")
	private int jobId;
	
	@Column(name="Sender")
	private String Sender;
	
	@Column(name="provider_id")
	private String providerId;


	public int getId() {
		return id;
	}


	public void setId(int id) {
		this.id = id;
	}


	public int getUserId() {
		return userId;
	}


	public void setUserId(int userId) {
		this.userId = userId;
	}


	public String getMobile() {
		return mobile;
	}


	public void setMobile(String mobile) {
		this.mobile = mobile;
	}


	public String getMessage() {
		return message;
	}


	public void setMessage(String message) {
		this.message = message;
	}


	public int getCount() {
		return count;
	}


	public void setCount(int count) {
		this.count = count;
	}


	public int getLength() {
		return length;
	}


	public void setLength(int length) {
		this.length = length;
	}


	public int getType() {
		return type;
	}


	public void setType(int type) {
		this.type = type;
	}


	public String getMessageId() {
		return messageId;
	}


	public void setMessageId(String messageId) {
		this.messageId = messageId;
	}


	public int getMobiClass() {
		return mobiClass;
	}


	public void setMobiClass(int mobiClass) {
		this.mobiClass = mobiClass;
	}


	public int getCoding() {
		return coding;
	}


	public void setCoding(int coding) {
		this.coding = coding;
	}


	public int getStatus() {
		return status;
	}


	public void setStatus(int status) {
		this.status = status;
	}


	public int getErrorCode() {
		return errorCode;
	}


	public void setErrorCode(int errorCode) {
		this.errorCode = errorCode;
	}


	public int getLoggedAt() {
		return loggedAt;
	}


	public void setLoggedAt(int loggedAt) {
		loggedAt = loggedAt;
	}


	public Date getDlrTime() {
		return dlrTime;
	}


	public void setDlrTime(Date dlrTime) {
		this.dlrTime = dlrTime;
	}


	public int getjobId() {
		return jobId;
	}


	public void setjobId(int jobId) {
		jobId = jobId;
	}


	public String getSender() {
		return Sender;
	}


	public void setSender(String sender) {
		Sender = sender;
	}


	public String getProviderId() {
		return providerId;
	}


	public void setProviderId(String providerId) {
		this.providerId = providerId;
	}
	
	
	
	
}
