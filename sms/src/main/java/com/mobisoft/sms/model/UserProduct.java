package com.mobisoft.sms.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "user_product")
public class UserProduct {

	@Id
	@GeneratedValue
	@Column(name = "id")
	private int id;
	
	@OneToOne
	@JoinColumn(name = "user_id")
	private User userId;
	
	@OneToOne
	@JoinColumn(name = "product_id")
	private Product product_id;

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

	public Product getProduct_id() {
		return product_id;
	}

	public void setProduct_id(Product product_id) {
		this.product_id = product_id;
	}
	
	
}
