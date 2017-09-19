package com.mobisoft.sms.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name="otp")
public class OtpValidate {
	
	@Id
	@GeneratedValue
	@Column(name = "id")
	private int otpId;
	
	@OneToOne(fetch=FetchType.LAZY)
	@JoinColumn(name = "user_id")
	private User userId;

	@Column(name="otp_data")
	private String otpData;
	
	public int getOtpId() {
		return otpId;
	}

	public void setOtpId(int otpId) {
		this.otpId = otpId;
	}

	public User getUserId() {
		return userId;
	}

	public void setUserId(User userId) {
		this.userId = userId;
	}

	public String getOtpData() {
		return otpData;
	}

	public void setOtpData(String otpData) {
		this.otpData = otpData;
	}

	

}
