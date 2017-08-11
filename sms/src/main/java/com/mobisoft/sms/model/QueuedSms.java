package com.mobisoft.sms.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="queued_sms")
public class QueuedSms {

	@Id
	@GeneratedValue
	@Column(name = "sql_id")
	private int sqlId;
	
	@Column(name="momt")
	private String momt;
	
	@Column(name="sender")
	private String sender;
	
	@Column(name="receiver")
	private String receiver;
	
	@Column(name="udhdata")
	private String udhdata;
	
	@Column(name="msgdata")
	private String msgdata;
	
	@Column(name="time")
	private String time;
	
	@Column(name="smsc_id")
	private String smscId;
	
	@Column(name="service")
	private String service;
	
	@Column(name="account")
	private String account;
	
	@Column(name="id")
	private int id;
	
	@Column(name="sms_type")
	private int smsType;
	
	@Column(name="mclass")
	private String mclass;
	
	@Column(name="mwi")
	private String mwi;
	
	@Column(name="coding")
	private String coding;
	
	@Column(name="compress")
	private String compress;
	
	@Column(name="validity")
	private String validity;
	
	@Column(name="deferred")
	private String deferred;
	
	@Column(name="dlr_mask")
	private int dlrMask;
	
	@Column(name="dlr_url")
	private String dlr_url;
	
	@Column(name="pid")
	private int pid;
	
	@Column(name="alt_dcs")
	private int altDcs;
	
	@Column(name="rpi")
	private int rpi;
	
	@Column(name="charset")
	private String charset;
	
	@Column(name="boxc_id")
	private String boxcId;
	
	@Column(name="binfo")
	private String binfo;
	
	@Column(name="meta_data")
	private String metaData;
	
	@Column(name="foreign_id")
	private String foreignId;

	public int getSqlId() {
		return sqlId;
	}

	public void setSqlId(int sqlId) {
		this.sqlId = sqlId;
	}

	public String getMomt() {
		return momt;
	}

	public void setMomt(String momt) {
		this.momt = momt;
	}

	public String getSender() {
		return sender;
	}

	public void setSender(String sender) {
		this.sender = sender;
	}

	public String getReceiver() {
		return receiver;
	}

	public void setReceiver(String receiver) {
		this.receiver = receiver;
	}

	public String getUdhdata() {
		return udhdata;
	}

	public void setUdhdata(String udhdata) {
		this.udhdata = udhdata;
	}

	public String getMsgdata() {
		return msgdata;
	}

	public void setMsgdata(String msgdata) {
		this.msgdata = msgdata;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getSmscId() {
		return smscId;
	}

	public void setSmscId(String smscId) {
		this.smscId = smscId;
	}

	public String getService() {
		return service;
	}

	public void setService(String service) {
		this.service = service;
	}

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getSmsType() {
		return smsType;
	}

	public void setSmsType(int smsType) {
		this.smsType = smsType;
	}

	public String getMclass() {
		return mclass;
	}

	public void setMclass(String mclass) {
		this.mclass = mclass;
	}

	public String getMwi() {
		return mwi;
	}

	public void setMwi(String mwi) {
		this.mwi = mwi;
	}

	public String getCoding() {
		return coding;
	}

	public void setCoding(String coding) {
		this.coding = coding;
	}

	public String getCompress() {
		return compress;
	}

	public void setCompress(String compress) {
		this.compress = compress;
	}

	public String getValidity() {
		return validity;
	}

	public void setValidity(String validity) {
		this.validity = validity;
	}

	public String getDeferred() {
		return deferred;
	}

	public void setDeferred(String deferred) {
		this.deferred = deferred;
	}

	public int getDlrMask() {
		return dlrMask;
	}

	public void setDlrMask(int dlrMask) {
		this.dlrMask = dlrMask;
	}

	public String getDlr_url() {
		return dlr_url;
	}

	public void setDlr_url(String dlr_url) {
		this.dlr_url = dlr_url;
	}

	public int getPid() {
		return pid;
	}

	public void setPid(int pid) {
		this.pid = pid;
	}

	public int getAltDcs() {
		return altDcs;
	}

	public void setAltDcs(int altDcs) {
		this.altDcs = altDcs;
	}

	public int getRpi() {
		return rpi;
	}

	public void setRpi(int rpi) {
		this.rpi = rpi;
	}

	public String getCharset() {
		return charset;
	}

	public void setCharset(String charset) {
		this.charset = charset;
	}

	public String getBoxcId() {
		return boxcId;
	}

	public void setBoxcId(String boxcId) {
		this.boxcId = boxcId;
	}

	public String getBinfo() {
		return binfo;
	}

	public void setBinfo(String binfo) {
		this.binfo = binfo;
	}

	public String getMetaData() {
		return metaData;
	}

	public void setMetaData(String metaData) {
		this.metaData = metaData;
	}

	public String getForeignId() {
		return foreignId;
	}

	public void setForeignId(String foreignId) {
		this.foreignId = foreignId;
	}
	
	
	

}
