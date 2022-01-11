package com.cs.stadosweb.domain;

import java.util.Date;
import java.util.UUID;
import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Modeling for Entity types for the webapp, expressing commonplace record
 * attributes.  
 * 
 * DONT USE THIS FOR LIGHT-WEIGHT TABLES, NOT EVERYTHING NEEDS
 * ACCOUNTING (or use @embeddable).
 */

@MappedSuperclass
public abstract class EntityBase implements Entity {
	public static Log log = LogFactory.getLog(EntityBase.class);

	protected static final long serialVersionUID = 1L;

	protected Long id;
	protected String uuid = UUID.randomUUID().toString();
	protected Date createdDate;
	protected Date modifiedDate;
	protected String createdBy;
	protected String modifiedBy;
	protected Date deletedDate;

	@Id
	@GeneratedValue
	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Column(name = "_uuid_")
	public String getUUID() {
		return this.uuid;
	}

	public void setUUID(String uuid) {
		this.uuid = uuid;
	}

	@Temporal(TemporalType.TIMESTAMP)
	public Date getCreatedDate() {
		return this.createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	@Temporal(TemporalType.TIMESTAMP)
	public Date getModifiedDate() {
		return this.modifiedDate;
	}

	public void setModifiedDate(Date modifiedDate) {
		this.modifiedDate = modifiedDate;
	}

	@Column(length = 80)
	public String getCreatedBy() {
		return this.createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	@Column(length = 80)
	public String getModifiedBy() {
		return this.modifiedBy;
	}

	public void setModifiedBy(String modifiedBy) {
		this.modifiedBy = modifiedBy;
	}

	@Temporal(TemporalType.TIMESTAMP)
	public Date getDeletedDate() {
		return deletedDate;
	}

	public void setDeletedDate(Date deleted) {
		this.deletedDate = deleted;
	}
}
