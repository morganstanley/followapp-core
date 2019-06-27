package com.followapp.core.model;

import java.sql.Timestamp;

public class BaseEntityObject {

	private Long id;
	private Timestamp createdTs;
	private Timestamp modifiedTs;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Timestamp getCreatedTs() {
		return createdTs;
	}
	public void setCreatedTs(Timestamp createdTs) {
		this.createdTs = createdTs;
	}
	public Timestamp getModifiedTs() {
		return modifiedTs;
	}
	public void setModifiedTs(Timestamp modifiedTs) {
		this.modifiedTs = modifiedTs;
	}
	
}

