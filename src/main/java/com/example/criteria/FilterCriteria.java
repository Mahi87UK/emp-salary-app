package com.example.criteria;


import javax.validation.constraints.NotNull;



public class FilterCriteria {
		
		@NotNull(message = "FilterCriteria key cannot be blank")
	    private Field key;
		
		@NotNull(message = "FilterCriteria value cannot be null")
	    private Object value;
		
		@NotNull(message = "FilterCriteria operation cannot be null")
	    private FilterOperation operation;

	    public FilterCriteria() {
	    }

	    public FilterCriteria(Field key, Object value, FilterOperation operation) {
	        this.key = key;
	        this.value = value;
	        this.operation = operation;
	    }

		public Field getKey() {
			return key;
		}

		public void setKey(Field key) {
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

		   

}
