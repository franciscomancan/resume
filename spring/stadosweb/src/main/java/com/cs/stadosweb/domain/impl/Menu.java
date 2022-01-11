package com.cs.stadosweb.domain.impl;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;

import com.cs.stadosweb.domain.EntityBase;

@Entity
public class Menu extends EntityBase {

	private static final long serialVersionUID = 8669287260656664447L;
	private String title;
	private String description;
	private String menucustom;
	private Boolean active;
	private Set<Category> categories;

	public Menu() {
	}

	public Menu(long id) {
		this.id = id;
	}
	
	@ManyToMany(targetEntity = com.cs.stadosweb.domain.impl.Category.class, 
				cascade = {	CascadeType.PERSIST, CascadeType.MERGE })
	@JoinTable(name = "menucategory", 
				joinColumns = @JoinColumn(referencedColumnName="id", name="menu_id"), 
				inverseJoinColumns = @JoinColumn(referencedColumnName="id", name = "category_id"))
	public Set<Category> getCategories() {
		return categories;
	}

	public void setCategories(Set<Category> categories) {
		this.categories = categories;
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

	public String getMenucustom() {
		return this.menucustom;
	}

	public void setMenucustom(String menucustom) {
		this.menucustom = menucustom;
	}

	public void setActive(Boolean active) {
		this.active = active;
	}

	public Boolean isActive() {
		return active;
	}

}
