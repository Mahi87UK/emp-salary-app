package com.example.criteria;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;



public class FilterCriteria {
		
		@NotBlank(message = "FilterCriteria key cannot be blank")
	    private String key;
		@NotNull(message = "FilterCriteria value cannot be null")
	    private Object value;
		@NotNull(message = "FilterCriteria operation cannot be null")
	    private FilterOperation operation;

	    public FilterCriteria() {
	    }

	    public FilterCriteria(String key, Object value, FilterOperation operation) {
	        this.key = key;
	        this.value = value;
	        this.operation = operation;
	    }

		public String getKey() {
			return key;
		}

		public void setKey(String key) {
			this.key = key;
		}

		public Object getValue() {
			return value;
		}

		public void setValue(Object value) {
			this.value = value;
		}

		public FilterOperation getOperation() {
			return operation;
		}

		public void setOperation(FilterOperation operation) {
			this.operation = operation;
		}

		@Override
		public String toString() {
			return "FilterCriteria [key=" + key + ", value=" + value + ", operation=" + operation + "]";
		}

	   

}
