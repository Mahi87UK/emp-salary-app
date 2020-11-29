package com.example.criteria;

import javax.validation.Valid;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Min;

public class RecordsCriteria {

	@DecimalMin(value = "0", message = "Minimum salary should be atleast 0")
	private double minSalary = 0;

	@DecimalMin(value = "0", message = "Maximum salary should be atleast 0")
	private double maxSalary = 4000.00;

	@Min(value = 0, message = "Offset should be minimum 0")
	private int offset = 0;

	@Min(value = 0, message = "Limit should be minimum 0")
	private int limit = 0;

	@Valid
	private SortCriteria sortCriteria;

	@Valid
	private FilterCriteria filterCriteria;

	public int getOffset() {
		return offset;
	}

	public void setOffset(int offset) {
		this.offset = offset;
	}

	public int getLimit() {
		return limit;
	}

	public void setLimit(int limit) {
		this.limit = limit;
	}

	public double getMinSalary() {
		return minSalary;
	}

	public void setMinSalary(double minSalary) {
		this.minSalary = minSalary;
	}

	public double getMaxSalary() {
		return maxSalary;
	}

	public void setMaxSalary(double maxSalary) {
		this.maxSalary = maxSalary;
	}

	public SortCriteria getSortCriteria() {
		return sortCriteria;
	}

	public void setSortCriteria(SortCriteria sortCriteria) {
		this.sortCriteria = sortCriteria;
	}

	public FilterCriteria getFilterCriteria() {
		return filterCriteria;
	}

	public void setFilterCriteria(FilterCriteria filterCriteria) {
		this.filterCriteria = filterCriteria;
	}

	@Override
	public String toString() {
		return "RecordsCriteria [minSalary=" + minSalary + ", maxSalary=" + maxSalary + ", offset=" + offset
				+ ", limit=" + limit + ", sortCriteria=" + sortCriteria + ", filterCriteria=" + filterCriteria + "]";
	}

}
