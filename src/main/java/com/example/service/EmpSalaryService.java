package com.example.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.criteria.Field;
import com.example.criteria.FilterCriteria;
import com.example.criteria.FilterOperation;
import com.example.criteria.RecordsCriteria;
import com.example.criteria.SortCriteria;
import com.example.exception.FileProcessingException;
import com.example.exception.InvalidRequestException;
import com.example.helper.CsvHelper;
import com.example.model.EmpSalary;
import com.example.pojo.EmpSalaryPojo;
import com.example.repository.EmpSalaryRepository;
import com.example.repository.specs.EmpSalarySpecification;

@Service
@Transactional
public class EmpSalaryService {

	@Value("${record.create.fail.id}")
	private String empIdExistsMsg;

	@Value("${record.create.fail.login}")
	private String loginIdExistsMsg;

	@Autowired
	private EmpSalaryRepository empSalaryRepo;

	@Autowired
	private CsvHelper csvHelper;
	
	/**
	 * Method to process and persist the valid csv data as EmpSalary
	 * @param file
	 * @throws FileProcessingException
	 */
	public void processAndSaveCsvFile(MultipartFile file) throws FileProcessingException {
		List<EmpSalaryPojo> empSalaryPojo = csvHelper.csvToEmpSalay(file);
		checkForDuplicates(empSalaryPojo);
		List<EmpSalary> empSalaryEntityObj = getAsEmpSalaryEntity(empSalaryPojo);
		empSalaryRepo.saveAll(empSalaryEntityObj);

	}
	
	/**
	 * Method to create/save new employee salary information
	 * @param empSalary
	 * @return EmpSalary
	 * @throws InvalidRequestException
	 */
	public EmpSalary createEmpSalaryInfo(EmpSalary empSalary) throws InvalidRequestException {
		if (checkEmpRecordExistsById(empSalary.getId())) {
			throw new InvalidRequestException(empIdExistsMsg);
		} else {
			return saveEmpSalaryInfo(empSalary);
		}
	}
	
	/**
	 * Method to save/update employee salary information
	 * @param empSalary
	 * @return
	 */
	public EmpSalary saveEmpSalaryInfo(EmpSalary empSalary) {
		if (checkEmpRecordExistsByLoginNotById(empSalary.getLogin(),empSalary.getId())) {
			throw new InvalidRequestException(loginIdExistsMsg);
		} else {
			return empSalaryRepo.save(empSalary);
		}
	}
	
	/**
	 * Method to delete employee salary information
	 * @param id
	 */
	public void deleteEmpSalaryInfo(String id) {
		empSalaryRepo.deleteById(id);
	}
	
	/**
	 * Method to retrieve specific employee salary information
	 * @param id
	 * @return
	 */
	public Optional<EmpSalary> getEmpSalaryInfo(String id) {
		return empSalaryRepo.findById(id);
	}
	
	/**
	 * Method to retrieve all employee salary information based on criteria
	 * @param recordsCriteria
	 * @return
	 */
	public List<EmpSalary> getAllEmpSalaryInfo(RecordsCriteria recordsCriteria) {
		List<EmpSalary> empSalaryList = new ArrayList<EmpSalary>();
		if (recordsCriteria.getLimit() > 0) {
			Pageable pageableObj = buildPaginationCriteria(recordsCriteria.getOffset(), recordsCriteria.getLimit(),
					buildSortOrder(recordsCriteria.getSortCriteria()));
			EmpSalarySpecification<EmpSalary> empSalarySpecification = new EmpSalarySpecification<>();
			empSalarySpecification.add(
					new FilterCriteria(Field.salary, recordsCriteria.getMinSalary(), FilterOperation.GREATER_THAN_EQUAL));
			empSalarySpecification
					.add(new FilterCriteria(Field.salary, recordsCriteria.getMaxSalary(), FilterOperation.LESS_THAN));
			if (recordsCriteria.getFilterCriteria() != null) {
				empSalarySpecification.add(recordsCriteria.getFilterCriteria());
			}
			Page<EmpSalary> pageResults = empSalaryRepo.findAll(empSalarySpecification, pageableObj);
			if (pageResults.hasContent()) {
				empSalaryList = pageResults.getContent();
			}
		}
		return empSalaryList;
	}

	private List<EmpSalary> getAsEmpSalaryEntity(List<EmpSalaryPojo> empSalaryPojo) {
		return empSalaryPojo.stream().map(
				emp -> new EmpSalary(emp.getId(), emp.getLogin(), emp.getName(), emp.getSalary(), emp.getStartDate()))
				.collect(Collectors.toList());

	}

	private List<Order> buildSortOrder(SortCriteria sortCriteria) {
		return sortCriteria != null
				? Arrays.asList(
						new Order(Direction.fromString(sortCriteria.getSortOrder().name()), sortCriteria.getSortField().name()))
				: null;
	}

	private Pageable buildPaginationCriteria(int offset, int limit, List<Order> sortOrders) {
		return CollectionUtils.isNotEmpty(sortOrders) ? PageRequest.of(offset, limit, Sort.by(sortOrders))
				: PageRequest.of(offset, limit);
	}

	private void checkForDuplicates(List<EmpSalaryPojo> empSalaryPojos) {
		List<String> duplicateEmpObjsById = empSalaryPojos.stream().collect(Collectors.groupingBy(EmpSalaryPojo::getId))
				.entrySet().stream().filter(e -> e.getValue().size() > 1).flatMap(e -> e.getValue().stream())
				.map(e -> e.getId()).collect(Collectors.toList());

		List<String> duplicateEmpObjsByLogin = empSalaryPojos.stream()
				.collect(Collectors.groupingBy(EmpSalaryPojo::getLogin)).entrySet().stream()
				.filter(e -> e.getValue().size() > 1).flatMap(e -> e.getValue().stream()).map(e -> e.getLogin())
				.collect(Collectors.toList());

		if (CollectionUtils.isNotEmpty(duplicateEmpObjsById)) {
			throw new FileProcessingException("Employee Id not unique " + StringUtils.join(duplicateEmpObjsById, "|"));
		}

		if (CollectionUtils.isNotEmpty(duplicateEmpObjsByLogin)) {
			throw new FileProcessingException(
					"Employee login not unique " + StringUtils.join(duplicateEmpObjsByLogin, "|"));
		}

	}

	private boolean checkEmpRecordExistsById(String id) {
		return empSalaryRepo.existsById(id);
	}

	private boolean checkEmpRecordExistsByLoginNotById(String login,String id) {
		return empSalaryRepo.existsByLoginNotById(login,id);
	}
}
