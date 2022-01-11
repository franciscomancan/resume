package com.cs.stadosweb.domain.impl;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.UniqueConstraint;

import org.hibernate.annotations.Index;

import com.cs.stadosweb.domain.EntityBase;

@Entity
public class Product extends EntityBase {

	private static final long serialVersionUID = -804345609811443829L;
	private String title;
	private String description;
	private String productcustom;
	private int pennies = 0;
	private Category category;

	@ManyToOne
	@JoinColumn(name="product_category_fk")
	public Category getCategory() {
		return category;
	}

	public void setCategory(Category category) {
		this.category = category;
	}

	/** default constructor */
	public Product() {
	}
	
	public Product(String name) {
		this.title = name;
	}

	public Product(long id) {
		this.id = id;
	}
 	
	@Column (unique=true, nullable=false, length=127)
	@Index (name="idx_product_title")
	public String getTitle() {
		return this.title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getProductCustom() {
		return this.productcustom;
	}

	public void setProductCustom(String productcustom) {
		this.productcustom = productcustom;
	}

	public int getPennies() {
		return this.pennies;
	}

	public void setPennies(int price) {
		this.pennies = price;
	}

}
