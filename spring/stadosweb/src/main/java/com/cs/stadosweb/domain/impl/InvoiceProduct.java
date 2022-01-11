package com.cs.stadosweb.domain.impl;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.cs.stadosweb.enumtype.InvoiceProductOverride;

/**
 *	Custom join entity, used to utilize more flexibility
 *	than allowed via simple manyToMany annotations.
 *	Accomplished by using 2 manyToOne mappings from this
 *	class into its associated tables.
 */

@Entity
public class InvoiceProduct implements Serializable {

	private static final long serialVersionUID = 3240899474992055633L;

	private Long id;
	private Invoice invoice;
	private Product product;
	private String detail;
	private Integer quantity;
	/* override is an enum used to indicate audits */
	private InvoiceProductOverride override;
	
	public InvoiceProduct() {}
	
	public InvoiceProduct(Invoice inv, Product prod) {
		this(inv, prod, null, 1);
	}
	
	public InvoiceProduct(Invoice inv, Product p, String det, Integer qnt) {
		this.invoice = inv;
		this.product = p;
		this.detail = det;
		this.quantity = qnt;
	}
	
	@Id @GeneratedValue
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	@ManyToOne
	@JoinColumn(referencedColumnName="id", name="invoice_id")
	public Invoice getInvoice() {
		return invoice;
	}

	public void setInvoice(Invoice invoice) {
		this.invoice = invoice;
	}
	
	@ManyToOne
	@JoinColumn(referencedColumnName="id", name="product_id")
	public Product getProduct() {
		return product;
	}
	
	public void setProduct(Product product) {
		this.product = product;
	}
	
	public String getDetail() {
		return detail;
	}
	
	public void setDetail(String detail) {
		this.detail = detail;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	@Enumerated(EnumType.STRING)
	@Column(length=10)
	public InvoiceProductOverride getOverride() {
		return override;
	}

	public void setOverride(InvoiceProductOverride override) {
		this.override = override;
	}
}
