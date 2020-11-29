package com.example.repository.specs;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.jpa.domain.Specification;

import com.example.criteria.FilterCriteria;

public class EmpSalarySpecification<EmpSalary> implements Specification<EmpSalary> {

	private static final long serialVersionUID = 1L;

	private List<FilterCriteria> list;

	public EmpSalarySpecification() {
		this.list = new ArrayList<>();
	}

	public void add(FilterCriteria criteria) {
		list.add(criteria);
	}

	@Override
	public Predicate toPredicate(Root<EmpSalary> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
		List<Predicate> predicates = new ArrayList<>();
		for (FilterCriteria criteria : list) {
			switch (criteria.getOperation()) {
			case GREATER_THAN:
				predicates.add(builder.greaterThan(root.get(criteria.getKey().name()), criteria.getValue().toString()));
				break;
			case LESS_THAN:
				predicates.add(builder.lessThan(root.get(criteria.getKey().name()), criteria.getValue().toString()));
				break;
			case GREATER_THAN_EQUAL:
				predicates
						.add(builder.greaterThanOrEqualTo(root.get(criteria.getKey().name()), criteria.getValue().toString()));
				break;
			case LESS_THAN_EQUAL:
				predicates.add(builder.lessThanOrEqualTo(root.get(criteria.getKey().name()), criteria.getValue().toString()));
				break;
			case NOT_EQUAL:
				predicates.add(builder.notEqual(root.get(criteria.getKey().name()), criteria.getValue()));
				break;
			case EQUAL:
				predicates.add(builder.equal(root.get(criteria.getKey().name()), criteria.getValue()));
				break;
			case MATCH:
				predicates.add(builder.like(builder.lower(root.get(criteria.getKey().name())),
						"%" + criteria.getValue().toString().toLowerCase() + "%"));
				break;
			case MATCH_END:
				predicates.add(builder.like(builder.lower(root.get(criteria.getKey().name())),
						criteria.getValue().toString().toLowerCase() + "%"));
				break;
			case MATCH_START:
				predicates.add(builder.like(builder.lower(root.get(criteria.getKey().name())),
						"%" + criteria.getValue().toString().toLowerCase()));
				break;
			case IN:
				predicates.add(builder.in(root.get(criteria.getKey().name())).value(criteria.getValue()));
				break;
			case NOT_IN:
				predicates.add(builder.not(root.get(criteria.getKey().name())).in(Arrays.asList(criteria.getValue())));
				break;

			}
		}

		return builder.and(predicates.toArray(new Predicate[0]));
	}
}
