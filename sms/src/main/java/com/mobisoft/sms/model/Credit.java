package com.mobisoft.sms.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonFormat;

@Entity
@Table(name = "credit")
public class Credit {
	
	@Id
	@GeneratedValue
	@Column(name = "id")
	private int id;
	
	@OneToOne
	@JoinColumn(name = "u_id")
	private User userId;
	
	@Column(name = "credit")
	private int credit;
	
	@Column(name = "current_amount")
	private int currentAmount;
	
	@Column(name = "previous_amount")
	private int previousAmouunt;
	
	@Column(name = "credit_by")
	private String creditBy;
	
	@Column(name = "created", columnDefinition="DATETIME", nullable=true)
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone="IST")
	private Date created = new Date();
	
	@Column(name = "updated", columnDefinition="TIMESTAMP", nullable=true)
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone="IST")
	private Date updated;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public User getUuserId() {
		return userId;
	}

	public void setUserId(User userId) {
		this.userId = userId;
	}

	public int getCredit() {
		return credit;
	}

	public void setCredit(int credit) {
		this.credit = credit;
	}

	public int getCurrentAmount() {
		return currentAmount;
	}

	public void setCurrentAmount(int currentAmount) {
		this.currentAmount = currentAmount;
	}

	public int getPreviousAmouunt() {
		return previousAmouunt;
	}

	public void setPreviousAmouunt(int previousAmouunt) {
		this.previousAmouunt = previousAmouunt;
	}

	public String getCreditBy() {
		return creditBy;
	}

	public void setCreditBy(String creditBy) {
		this.creditBy = creditBy;
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
