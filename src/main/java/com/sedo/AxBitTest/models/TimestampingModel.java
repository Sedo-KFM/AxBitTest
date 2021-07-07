package com.sedo.AxBitTest.models;

import javax.persistence.MappedSuperclass;
import javax.persistence.PreUpdate;
import java.sql.Date;

@MappedSuperclass
abstract public class TimestampingModel {
	protected Date creationDate;
	protected Date modificationDate;

	public TimestampingModel() {
		this.creationDate = new Date(System.currentTimeMillis());
		this.modificationDate = new Date(System.currentTimeMillis());
	}

	public TimestampingModel(Date creationDate, Date modificationDate) {
		this.creationDate = creationDate;
		this.modificationDate = modificationDate;
	}

	public Date getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	public Date getModificationDate() {
		return modificationDate;
	}

	public void setModificationDate(Date modificationDate) {
		this.modificationDate = modificationDate;
	}

	public void updateModificationDate() {
		this.modificationDate.setTime(System.currentTimeMillis());
	}

	@PreUpdate
	private void preUpdate() {
		this.modificationDate.setTime(System.currentTimeMillis());
	}
}
