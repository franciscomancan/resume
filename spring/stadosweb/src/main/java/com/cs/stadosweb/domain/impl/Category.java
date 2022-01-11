package com.cs.stadosweb.domain.impl;

import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;

import com.cs.stadosweb.domain.EntityBase;

@Entity
public class Category extends EntityBase {

	private static final long serialVersionUID = -294546760169219371L;
	private String title;
	private String description;
	private Set<Product> products;
	private List<Menu> menus;

	// Constructors

	@ManyToMany(cascade = { CascadeType.PERSIST, CascadeType.MERGE }, 
				mappedBy = "categories", 
				targetEntity = com.cs.stadosweb.domain.impl.Menu.class)
	public List<Menu> getMenus() {
		return menus;
	}

	public void setMenus(List<Menu> menus) {
		this.menus = menus;
	}

	public Category() {
	}

	@OneToMany(mappedBy = "category")
	public Set<Product> getProducts() {
		return products;
	}

	public void setProducts(Set<Product> products) {
		this.products = products;
	}

	public Category(long id) {
		this.id = id;
	}

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

}
