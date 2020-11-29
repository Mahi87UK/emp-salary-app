package com.example.criteria;

import javax.validation.constraints.NotBlank;

public class SortCriteria {
	
	@NotBlank(message = "SortCriteria sortField Cannot be empty")
	private String sortField = "id";
	
	@NotBlank(message = "SortCriteria sortOrder cannot be blank")
	private String sortOrder = "asc";
	

	public String getSortField() {
		return sortField;
	}

	public void setSortField(String sortField) {
		this.sortField = sortField;
	}

	public String getSortOrder() {
		return sortOrder;
	}

	public void setSortOrder(String sortOrder) {
		this.sortOrder = sortOrder;
	}

	@Override
	public String toString() {
		return "SortCriteria [sortField=" + sortField + ", sortOrder=" + sortOrder + "]";
	}
	
	
}
