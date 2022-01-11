package com.cs.stadosweb.domain.impl;

import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.OneToMany;

import com.cs.stadosweb.domain.EntityBase;

@Entity
public class SystemUser extends EntityBase {

	private static final long serialVersionUID = 6759975321904385499L;
	private String firstname;
	private String lastname;
	private String username;
	private String email;
	private String description;
	private String userCustom;
	private String password;
	private String type;
	private Set<Invoice> invoices;

	/** default constructor */
	public SystemUser() {
	}

	public String getFirstname() {
		return this.firstname;
	}

	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}

	public String getLastname() {
		return this.lastname;
	}

	public void setLastname(String lastname) {
		this.lastname = lastname;
	}

	public String getUsername() {
		return this.username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getUserCustom() {
		return this.userCustom;
	}

	public void setUserCustom(String usercustom) {
		this.userCustom = usercustom;
	}

	public String getPassword() {
		return this.password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getType() {
		return this.type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@OneToMany(mappedBy = "owner")
	public Set<Invoice> getInvoices() {
		return invoices;
	}

	public void setInvoices(Set<Invoice> invoices) {
		this.invoices = invoices;
	}

}
