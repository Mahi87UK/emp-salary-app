package com.example.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit.jupiter.SpringExtension;

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

@ExtendWith(SpringExtension.class)
public class EmpSalaryServiceTest {

	@TestConfiguration
	static class EmpSalaryServiceTestContextConfiguration {

		@Bean
		public EmpSalaryService empSalaryService() {
			return new EmpSalaryService();
		}
	}

	@Autowired
	private EmpSalaryService empSalaryService;

	@MockBean
	private EmpSalaryRepository empSalaryRepo;

	@MockBean
	private CsvHelper csvHelper;

	MockMultipartFile uploadFile = new MockMultipartFile("uploadFile", new byte[1]);

	private EmpSalary empSalaryObj;

	@Test
	public void whenRecordIsValid_thenCreateRecordShouldbeSuccess() {
		// given
		empSalaryObj = new EmpSalary("id1", "login1", "name1", 10.0, LocalDate.now());
		Mockito.when(empSalaryRepo.existsById(empSalaryObj.getId())).thenReturn(false);
		Mockito.when(empSalaryRepo.existsByLoginNotById(empSalaryObj.getLogin(),empSalaryObj.getId())).thenReturn(false);
		Mockito.when(empSalaryRepo.save(empSalaryObj)).thenReturn(empSalaryObj);

		// when
		EmpSalary saved = empSalaryService.createEmpSalaryInfo(empSalaryObj);

		// then
		assert (saved.equals(empSalaryObj));
	}

	@Test
	public void whenIdIsNotUnique_thenCreateRecordShouldFail() {
		// given
		empSalaryObj = new EmpSalary("id1", "login1", "name1", 10.0, LocalDate.now());

		Mockito.when(empSalaryRepo.existsById(empSalaryObj.getId())).thenReturn(true);

		Mockito.when(empSalaryRepo.existsByLoginNotById(empSalaryObj.getLogin(),empSalaryObj.getId())).thenReturn(false);

		// when
		assertThrows(InvalidRequestException.class, () -> {
			empSalaryService.createEmpSalaryInfo(empSalaryObj);
		});

		// then throw Invalid request exception

	}

	@Test
	public void whenLoginIsNotUnique_thenCreateRecordShouldFail() {
		// given
		empSalaryObj = new EmpSalary("id1", "login1", "name1", 10.0, LocalDate.now());

		Mockito.when(empSalaryRepo.existsById(empSalaryObj.getId())).thenReturn(false);

		Mockito.when(empSalaryRepo.existsByLoginNotById(empSalaryObj.getLogin(),empSalaryObj.getId())).thenReturn(true);

		// when
		// then throw Invalid request exception
		assertThrows(InvalidRequestException.class, () -> {empSalaryService.createEmpSalaryInfo(empSalaryObj);});

		

	}

	@Test
	public void whenRecordIsValid_thenSaveRecordShouldbeSuccess() {
		// given
		empSalaryObj = new EmpSalary("id1", "login1", "name1", 10.0, LocalDate.now());
		Mockito.when(empSalaryRepo.existsById(empSalaryObj.getId())).thenReturn(false);
		Mockito.when(empSalaryRepo.existsByLoginNotById(empSalaryObj.getLogin(),empSalaryObj.getId())).thenReturn(false);
		Mockito.when(empSalaryRepo.save(empSalaryObj)).thenReturn(empSalaryObj);

		// when
		EmpSalary saved = empSalaryService.saveEmpSalaryInfo(empSalaryObj);

		// then
		assertEquals(empSalaryObj, saved);
	}

	@Test
	public void whenRecordIsValid_thenDeleteRecordShouldbeSuccess() {
		// given
		empSalaryObj = new EmpSalary("id1", "login1", "name1", 10.0, LocalDate.now());
		Mockito.when(empSalaryRepo.existsById(empSalaryObj.getId())).thenReturn(false);

		Mockito.when(empSalaryRepo.save(empSalaryObj)).thenReturn(empSalaryObj);

		// when
		empSalaryService.deleteEmpSalaryInfo("id1");

		// then delete should be success

	}

	@Test
	public void whenRecordIsExists_thengetEmpSalaryInfoShouldReturnResult() {
		// given
		empSalaryObj = new EmpSalary("id1", "login1", "name1", 10.0, LocalDate.now());
		Mockito.when(empSalaryRepo.existsById(empSalaryObj.getId())).thenReturn(false);

		Mockito.when(empSalaryRepo.save(empSalaryObj)).thenReturn(empSalaryObj);
		Mockito.when(empSalaryRepo.findById("id1")).thenReturn(Optional.of(empSalaryObj));
		// when
		Optional<EmpSalary> actual = empSalaryService.getEmpSalaryInfo("id1");

		// then
		assertEquals(empSalaryObj, actual.get());
	}

	@Test
	public void whenRecordDoesNotExists_thengetEmpSalaryInfoShouldReturnNull() {
		// given
		empSalaryObj = new EmpSalary("id1", "login1", "name1", 10.0, LocalDate.now());
		Mockito.when(empSalaryRepo.existsById(empSalaryObj.getId())).thenReturn(false);

		Mockito.when(empSalaryRepo.save(empSalaryObj)).thenReturn(empSalaryObj);
		Mockito.when(empSalaryRepo.findById("id2")).thenReturn(null);
		// when
		Optional<EmpSalary> actual = empSalaryService.getEmpSalaryInfo("id2");

		// then
		assertNull(actual);
	}

	@Test
	public void whenRecordAreValids_thenprocessAndSaveFileshouldBeSuccess() {
		// given
		empSalaryObj = new EmpSalary("id1", "login1", "name1", 10.0, LocalDate.now());
		EmpSalaryPojo csvPojo = new EmpSalaryPojo("id1", "login1", "name1", LocalDate.now(), 10.0);
		List<EmpSalaryPojo> pojoList = new ArrayList<EmpSalaryPojo>();
		pojoList.add(csvPojo);
		Mockito.when(csvHelper.csvToEmpSalay(uploadFile)).thenReturn(pojoList);
		Mockito.when(empSalaryRepo.existsById(empSalaryObj.getId())).thenReturn(false);
		
		// when
		empSalaryService.processAndSaveCsvFile(uploadFile);
		// then should be processed without any exception & save record
		verify(empSalaryRepo, times(1)).saveAll(Mockito.anyListOf(EmpSalary.class));
	}

	@Test
	public void whenEmpIdIsNotUnique_thenprocessAndSaveFileshouldFail() {
		// given
		empSalaryObj = new EmpSalary("id1", "login1", "name1", 10.0, LocalDate.now());
		EmpSalaryPojo csvPojo1 = new EmpSalaryPojo("id1", "login1", "name1", LocalDate.now(), 10.0);
		EmpSalaryPojo csvPojo2 = new EmpSalaryPojo("id1", "login2", "name2", LocalDate.now(), 10.0);
		List<EmpSalaryPojo> pojoList = new ArrayList<EmpSalaryPojo>();
		pojoList.add(csvPojo1);
		pojoList.add(csvPojo2);
		Mockito.when(csvHelper.csvToEmpSalay(uploadFile)).thenReturn(pojoList);
		Mockito.when(empSalaryRepo.existsById(empSalaryObj.getId())).thenReturn(false);

		// when

		// then should throw exception and no interaction with repo obj to save data
		assertThrows(FileProcessingException.class, () -> {
			empSalaryService.processAndSaveCsvFile(uploadFile);
		});
	}

	@Test
	public void whenLoginIsNotUnique_thenprocessAndSaveFileshouldFail() {
		// given
		empSalaryObj = new EmpSalary("id1", "login1", "name1", 10.0, LocalDate.now());
		EmpSalaryPojo csvPojo1 = new EmpSalaryPojo("id1", "login1", "name1", LocalDate.now(), 10.0);
		EmpSalaryPojo csvPojo2 = new EmpSalaryPojo("id2", "login1", "name2", LocalDate.now(), 10.0);
		List<EmpSalaryPojo> pojoList = new ArrayList<EmpSalaryPojo>();
		pojoList.add(csvPojo1);
		pojoList.add(csvPojo2);
		Mockito.when(csvHelper.csvToEmpSalay(uploadFile)).thenReturn(pojoList);
		Mockito.when(empSalaryRepo.existsById(empSalaryObj.getId())).thenReturn(false);

		// when

		assertThrows(FileProcessingException.class, () -> {
			empSalaryService.processAndSaveCsvFile(uploadFile);
		});
		// then should throw exception and no interaction with repo obj to save data

	}

	@Test
	public void whenDefaultCriteriaIsMatched_thengetAllEmpSalaryInfoShoudlReturnEmptyResults() {
		// Given
		RecordsCriteria recordsCriteria = new RecordsCriteria();

		empSalaryObj = new EmpSalary("id1", "login1", "name1", 10.0, LocalDate.now());
		Page<EmpSalary> pageResults = Mockito.mock(Page.class);
		List<EmpSalary> empSalaryList = new ArrayList<EmpSalary>();
		empSalaryList.add(empSalaryObj);
		Mockito.when(pageResults.hasContent()).thenReturn(true);
		Mockito.when(pageResults.getContent()).thenReturn(empSalaryList);
		Mockito.when(empSalaryRepo.findAll(Mockito.any(EmpSalarySpecification.class), Mockito.any(Pageable.class)))
				.thenReturn(pageResults);

		// when
		List results = empSalaryService.getAllEmpSalaryInfo(recordsCriteria);

		// then
		assertEquals(0, results.size());
	}

	@Test
	public void whenDefaultCriteriaIsNotMatched_thengetAllEmpSalaryInfoShoudlBeEmpty() {
		// Given
		RecordsCriteria recordsCriteria = new RecordsCriteria();

		Page<EmpSalary> pageResults = Mockito.mock(Page.class);
		List empSalaryList = new ArrayList();
		empSalaryList.add(empSalaryObj);

		Mockito.when(pageResults.hasContent()).thenReturn(false);

		Mockito.when(empSalaryRepo.findAll(Mockito.any(EmpSalarySpecification.class), Mockito.any(Pageable.class)))
				.thenReturn(pageResults);

		// when
		List results = empSalaryService.getAllEmpSalaryInfo(recordsCriteria);

		// then
		assertEquals(0, results.size());
	}

	@Test
	public void whenMultipleCriteriasIsMatched_thengetAllEmpSalaryInfoShouldReturnResults() {
		// Given
		RecordsCriteria recordsCriteria = new RecordsCriteria();
		SortCriteria sortCriteria = new SortCriteria();
		FilterCriteria filterCriteria = new FilterCriteria("id", "id1", FilterOperation.MATCH);
		recordsCriteria.setFilterCriteria(filterCriteria);
		recordsCriteria.setSortCriteria(sortCriteria);
		recordsCriteria.setLimit(1);
		empSalaryObj = new EmpSalary("id1", "login1", "name1", 10.0, LocalDate.now());
		Page<EmpSalary> pageResults = Mockito.mock(Page.class);
		List empSalaryList = new ArrayList();
		empSalaryList.add(empSalaryObj);
		Mockito.when(pageResults.hasContent()).thenReturn(true);
		Mockito.when(pageResults.getContent()).thenReturn(empSalaryList);
		Mockito.when(empSalaryRepo.findAll(Mockito.any(EmpSalarySpecification.class), Mockito.any(Pageable.class)))
				.thenReturn(pageResults);

		// when
		List results = empSalaryService.getAllEmpSalaryInfo(recordsCriteria);

		// then
		assertEquals(empSalaryList, results);
	}
}
