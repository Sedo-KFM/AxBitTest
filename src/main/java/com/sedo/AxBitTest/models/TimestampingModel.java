package com.sedo.AxBitTest.models;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import javax.persistence.MappedSuperclass;
import javax.persistence.PreUpdate;
import java.time.LocalDate;

@MappedSuperclass
abstract public class TimestampingModel {
	@CreatedDate
	protected LocalDate creationDate;
	@LastModifiedDate
	protected LocalDate modificationDate;

	public TimestampingModel() {
	}

	public TimestampingModel(LocalDate creationDate, LocalDate modificationDate) {
		this.creationDate = creationDate;
		this.modificationDate = modificationDate;
	}

	public LocalDate getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(LocalDate creationDate) {
		this.creationDate = creationDate;
	}

	public LocalDate getModificationDate() {
		return modificationDate;
	}

	public void setModificationDate(LocalDate modificationDate) {
		this.modificationDate = modificationDate;
	}

	public void updateModificationDate() {
		this.modificationDate = LocalDate.now();
	}

	@PreUpdate
	private void preUpdate() {
		this.modificationDate = LocalDate.now();
	}
}
