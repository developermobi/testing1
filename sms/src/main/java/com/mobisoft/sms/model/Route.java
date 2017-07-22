package com.mobisoft.sms.model;

import java.sql.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonFormat;

@Entity
@Table(name = "route")
public class Route {

	@Id
	@GeneratedValue
	@Column(name = "id")
	private int id;
	
	@Column(name = "route")
	private int route;
	
	@Column(name = "type")
	private String type;
	
	@Column(name = "smpp_name")
	private String smppName;
	
	@Column(name = "account_type")
	private int AccountType;
	
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

	public int getRoute() {
		return route;
	}

	public void setRoute(int route) {
		this.route = route;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getSmppName() {
		return smppName;
	}

	public void setSmppName(String smppName) {
		this.smppName = smppName;
	}

	public int getAccountType() {
		return AccountType;
	}

	public void setAccountType(int accountType) {
		AccountType = accountType;
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
