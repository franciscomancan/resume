package com.cs.stadosweb.domain.impl;

import java.util.Date;
import java.util.HashSet;

import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.cs.stadosweb.domain.EntityBase;

/**
 * Primary representation of any composite transaction in the system.
 * This contains all associated debits and credits in the form of
 * pennies.
 */

@Entity
public class Invoice extends EntityBase {

	private static final long serialVersionUID = -2389104964495202229L;
	private String customer;
	private String location;
	/** 
	 * invoiceCustom - Currently used for invoice status, needs to
	 * be overhauled to use an enum type with a short column length,
	 * (as in InvoiceProductOverride enum).
	 */
	private String invoiceCustom;
	private SystemUser owner;
	private Date expeditedDate;
	private Set<InvoiceProduct> invoiceProducts = new HashSet<InvoiceProduct>(0);
	private Set<Coupon> coupons = new HashSet<Coupon>(0);
		/** all monies recorded in pennies **/
	private int subtotal = 0;
	private int tax = 0;
	private int due = 0;
	private int tendered = 0;

	public Invoice() {
	}

	public Invoice(long id) {
		this.id = id;
	}

	public String getInvoiceCustom() {
		return this.invoiceCustom;
	}

	public void setInvoiceCustom(String invoicecustom) {
		this.invoiceCustom = invoicecustom;
	}

	public String getCustomer() {
		return customer;
	}

	public void setCustomer(String customer) {
		this.customer = customer;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	@ManyToOne
	@JoinColumn(name="invoice_user_fk")
	public SystemUser getOwner() {
		return owner;
	}

	public void setOwner(SystemUser owner) {
		this.owner = owner;
	}

	@OneToMany(mappedBy="invoice", cascade=CascadeType.ALL, fetch=FetchType.LAZY)
	//@LazyToOne(LazyToOneOption.PROXY)
	//@Fetch(FetchMode.JOIN)		
	//**note: regarding hibernate; cannot use lazy and join fetch at the same time, it overrides
	public Set<InvoiceProduct> getInvoiceProducts() {
		return invoiceProducts;
	}

	public void setInvoiceProducts(Set<InvoiceProduct> invoiceProducts) {
		this.invoiceProducts = invoiceProducts;
	}

	public int getSubtotal() {
		return subtotal;
	}

	public void setSubtotal(int subtotal) {
		this.subtotal = subtotal;
	}

	public int getTax() {
		return tax;
	}

	public void setTax(int tax) {
		this.tax = tax;
	}

	public int getDue() {
		return due;
	}

	public void setDue(int due) {
		this.due = due;
	}

	public int getTendered() {
		return tendered;
	}

	public void setTendered(int tendered) {
		this.tendered = tendered;
	}

	@Temporal(TemporalType.TIMESTAMP)
	public Date getExpeditedDate() {
		return this.expeditedDate;
	}
	
	public void setExpeditedDate(Date d) {
		this.expeditedDate = d;
	}
	
	@OneToMany(mappedBy="invoice", cascade=CascadeType.ALL, fetch=FetchType.LAZY)
	public Set<Coupon> getCoupons() {
		return coupons;
	}

	public void setCoupons(Set<Coupon> coupons) {
		this.coupons = coupons;
	}
}
