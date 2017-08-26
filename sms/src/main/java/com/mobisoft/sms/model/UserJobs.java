package com.mobisoft.sms.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonFormat;

@Entity
@Table(name="user_jobs")
public class UserJobs {
	
	@Id
	@GeneratedValue
	@Column(name = "id")
	private int id;
	
	@Column(name="user_id")
	private int userId;
	
	@Column(name="message")
	private String message;
	
	@Column(name="message_type")
	private int messageType;
	
	@Column(name="message_length")
	private int messageLength;
	
	@Column(name="count")
	private int count;	
	
	@Column(name="sender")
	private String sender;
	
	@Column(name="total_numbers")
	private int totalNumbers;
	
	@Column(name="total_sent")
	private int totalSent;
	
	@Column(name="file_name")
	private String filename;

	
	@Column(name = "scheduled_at", columnDefinition="TIMESTAMP", nullable=true)
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone="IST")
	private Date scheduledAt;
	
	
	@Column(name = "queued_at", columnDefinition="TIMESTAMP", nullable=true)
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone="IST")
	private Date queuedAt;
	
	@Column(name = "completed_at", columnDefinition="TIMESTAMP", nullable=true)
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone="IST")
	private Date completedAt;
	
	@Column(name="job_status")
	private int jobStatus;
	
	@Column(name="job_type")
	private int jobType;
	
	@Column(name="columns")
	private int columns;
	
	@Column(name="send_now")
	private String sendNow;
	
	@Column(name="send_ratio")
	private int sendRatio;
	
	@Column(name="route")
	private String route;

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

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public int getMessageType() {
		return messageType;
	}

	public void setMessageType(int messageType) {
		this.messageType = messageType;
	}

	public int getMessageLength() {
		return messageLength;
	}

	public void setMessageLength(int messageLength) {
		this.messageLength = messageLength;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public String getSender() {
		return sender;
	}

	public void setSender(String sender) {
		this.sender = sender;
	}

	public int getTotalNumbers() {
		return totalNumbers;
	}

	public void setTotalNumbers(int totalNumbers) {
		this.totalNumbers = totalNumbers;
	}

	public int getTotalSent() {
		return totalSent;
	}

	public void setTotalSent(int totalSent) {
		this.totalSent = totalSent;
	}

	public String getFilename() {
		return filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}

	public Date getScheduledAt() {
		return scheduledAt;
	}

	public void setScheduledAt(Date scheduledAt) {
		this.scheduledAt = scheduledAt;
	}

	public Date getQueuedAt() {
		return queuedAt;
	}

	public void setQueuedAt(Date queuedAt) {
		this.queuedAt = queuedAt;
	}

	public Date getCompletedAt() {
		return completedAt;
	}

	public void setCompletedAt(Date completedAt) {
		this.completedAt = completedAt;
	}

	public int getJobStatus() {
		return jobStatus;
	}

	public void setJobStatus(int jobStatus) {
		this.jobStatus = jobStatus;
	}

	public int getJobType() {
		return jobType;
	}

	public void setJobType(int jobType) {
		this.jobType = jobType;
	}

	public int getColumns() {
		return columns;
	}

	public void setColumns(int columns) {
		this.columns = columns;
	}

	public String getSendNow() {
		return sendNow;
	}

	public void setSendNow(String sendNow) {
		this.sendNow = sendNow;
	}

	public int getSendRatio() {
		return sendRatio;
	}

	public void setSendRatio(int sendRatio) {
		this.sendRatio = sendRatio;
	}

	public String getRoute() {
		return route;
	}

	public void setRoute(String route) {
		this.route = route;
	}
	
	
	
	
}
