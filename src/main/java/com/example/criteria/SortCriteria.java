package com.example.criteria;


import javax.validation.constraints.NotNull;

public class SortCriteria {
	
	@NotNull(message = "SortCriteria sortField Cannot be empty")
	private Field sortField = Field.id;
	
	@NotNull(message = "SortCriteria sortOrder cannot be blank")
	private SortOrder sortOrder = SortOrder.asc;
	
	
	public Field getSortField() {
		return sortField;
	}

	public void setSortField(Field sortField) {
		this.sortField = sortField;
	}

	public SortOrder getSortOrder() {
		return sortOrder;
	}

	public void setSortOrder(SortOrder sortOrder) {
		this.sortOrder = sortOrder;
	}


	
}
