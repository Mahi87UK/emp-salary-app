package com.example.integration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.util.ResourceUtils;

import com.example.EmployeeSalaryMgmtServiceApplication;
import com.example.model.EmpSalary;
import com.example.repository.EmpSalaryRepository;
import com.example.service.EmpSalaryService;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK, classes = EmployeeSalaryMgmtServiceApplication.class)
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:application-integrationtest.properties")
public class EmpSalaryControllerITTest {

	@Autowired
	private MockMvc mvc;

	@Autowired
	private EmpSalaryRepository empSalaryRepo;

	@Autowired
	private EmpSalaryService empSalaryService;

	@BeforeEach
	public void init() {
		createDummyRecords(5);
	}

	@AfterEach
	public void clean() {
		cleanUpRecords();
	}

	@Test
	public void givenEmpIdNotExists_whenGetEmpSalInfo_thenStatus400() throws Exception {

		mvc.perform(MockMvcRequestBuilders.get("/users/invalid").contentType(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().isBadRequest());
	}

	@Test
	public void givenEmpIdExists_whenGetEmpSalInfo_thenStatus200() throws Exception {

		mvc.perform(MockMvcRequestBuilders.get("/users/id1").contentType(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.jsonPath("$.id", Matchers.is("id1")));
	}

	@Test
	public void givenEmpIdNotExists_whenDeleteEmpSalInfo_thenStatus400() throws Exception {

		mvc.perform(MockMvcRequestBuilders.delete("/users/invalid1").contentType(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().isBadRequest());
	}

	@Test
	public void givenEmpIdExists_whenDeleteEmpSalInfo_thenStatus200() throws Exception {

		mvc.perform(MockMvcRequestBuilders.delete("/users/id1").contentType(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().isOk());
		assertFalse(empSalaryRepo.existsById("id1"));
	}

	@Test
	public void givenNoCriteria_whenGetAllEmpSalInfo_thenStatus200WithEmptyResults() throws Exception {

		mvc.perform(MockMvcRequestBuilders.get("/users").contentType(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.jsonPath("$").isEmpty());
	}

	@Test
	public void givenWithCriteriaLimitAs5_whenGetAllEmpSalInfo_thenStatus200WithResultsHavingSalaryLessThan4000()
			throws Exception {

		mvc.perform(MockMvcRequestBuilders.get("/users").param("limit", "5").contentType(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.jsonPath("$.results", Matchers.hasSize(2)));
	}

	@Test
	public void givenWithOptionalCriterias_whenGetAllEmpSalInfo_thenStatus200WithResultsMatchingCriteria()
			throws Exception {

		// Filters with default sorting by id in asc
		mvc.perform(MockMvcRequestBuilders.get("/users").param("limit", "5").param("minSalary", "0.0")
				.param("maxSalary", "50000000.00").contentType(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.jsonPath("$.results", Matchers.hasSize(5)))
				.andExpect(MockMvcResultMatchers.jsonPath("$.results[0].id", Matchers.is("id1")));
	}

	@Test
	public void givenWithInvalidSalary_whenGetAllEmpSalInfo_thenStatus400() throws Exception {

		mvc.perform(MockMvcRequestBuilders.get("/users").param("limit", "5").param("minSalary", "0.0")
				.param("maxSalary", "abc").contentType(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().isBadRequest());
	}

	@Test
	public void givenWithSortFilters_whenGetAllEmpSalInfo_thenStatus200WithResultsMatchingCriteria() throws Exception {

		mvc.perform(MockMvcRequestBuilders.get("/users").param("limit", "5").param("minSalary", "0.0")
				.param("maxSalary", "50000000.00").param("sortCriteria.sortField", "id")
				.param("sortCriteria.sortOrder", "desc").contentType(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.jsonPath("$.results", Matchers.hasSize(5)))
				.andExpect(MockMvcResultMatchers.jsonPath("$.results[0].id", Matchers.is("id5")));
	}

	@Test
	public void givenWithAdditionalFilterCrieria_whenGetAllEmpSalInfo_thenStatus200WithResultsMatchingCriteria()
			throws Exception {

		// Name should be equal to name1
		mvc.perform(MockMvcRequestBuilders.get("/users").param("limit", "5").param("minSalary", "0.0")
				.param("maxSalary", "50000000.00").param("sortCriteria.sortField", "id")
				.param("sortCriteria.sortOrder", "desc").param("filterCriteria.key", "name")
				.param("filterCriteria.operation", "EQUAL").param("filterCriteria.value", "name1")
				.contentType(MediaType.APPLICATION_JSON)).andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.jsonPath("$.results", Matchers.hasSize(1)))
				.andExpect(MockMvcResultMatchers.jsonPath("$.results[0].name", Matchers.is("name1")));
		
	}

	@Test
	public void givenWithAdditionalFilterCrieriaMatch_whenGetAllEmpSalInfo_thenStatus200WithResultsMatchingCriteria()
			throws Exception {
		createDummyRecords(20);
		// Filter to get macthed ids (2,12,20) and sorted desc
		mvc.perform(MockMvcRequestBuilders.get("/users").param("limit", "5").param("minSalary", "0.0")
				.param("maxSalary", "50000000.00").param("sortCriteria.sortField", "id")
				.param("sortCriteria.sortOrder", "desc").param("filterCriteria.key", "id")
				.param("filterCriteria.operation", "MATCH").param("filterCriteria.value", "2")
				.contentType(MediaType.APPLICATION_JSON)).andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.jsonPath("$.results", Matchers.hasSize(3)))
				.andExpect(MockMvcResultMatchers.jsonPath("$.results[0].name", Matchers.is("name20")));

	}

	@Test
	public void givenEmpIdExists_whenUpdateEmpSalInfo_thenStatus200() throws Exception {
		String validData = "{\"id\": \"id1\",\"name\": \"name1\",\"login\": \"login100\",\"salary\": 1234.00,\"startDate\": \"2001-11-16\"}";
		mvc.perform(MockMvcRequestBuilders.put("/users/id1").content(validData).contentType(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().isOk());
	}

	@Test
	public void givenEmpIdExistsButLoginNotUnique_whenUpdateEmpSalInfo_thenStatus400() throws Exception {
		String invalidLoginData = "{\"id\": \"invalid\",\"name\": \"name1\",\"login\": \"login2\",\"salary\": 1234.00,\"startDate\": \"2001-11-16\"}";
		mvc.perform(MockMvcRequestBuilders.put("/users/id1").content(invalidLoginData)
				.contentType(MediaType.APPLICATION_JSON)).andExpect(MockMvcResultMatchers.status().isBadRequest())
				.andExpect(MockMvcResultMatchers.jsonPath("$.message", Matchers.is("Employee login not unique")));
	}

	@Test
	public void givenEmpIdNotExists_whenUpdateEmpSalInfo_thenStatus400() throws Exception {
		String invalidData = "{\"id\": \"invalid\",\"name\": \"name1\",\"login\": \"login1\",\"salary\": 1234.00,\"startDate\": \"2001-11-16\"}";
		mvc.perform(MockMvcRequestBuilders.put("/users/invalid").content(invalidData)
				.contentType(MediaType.APPLICATION_JSON)).andExpect(MockMvcResultMatchers.status().isBadRequest())
				.andExpect(MockMvcResultMatchers.jsonPath("$.message", Matchers.is("No such employee")));
	}

	@Test
	public void givenValidEmpData_whencreateEmpSalInfo_thenStatus200() throws Exception {
		String validData = "{\"id\": \"id1111\",\"name\": \"name1111\",\"login\": \"login100\",\"salary\": 1234.00,\"startDate\": \"2001-11-16\"}";
		mvc.perform(MockMvcRequestBuilders.post("/users").content(validData).contentType(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().isCreated());
		assertTrue(empSalaryRepo.existsById("id1111"));
	}

	@Test
	public void givenValidEmpDataWithSupportedDateFormat_whencreateEmpSalInfo_thenStatus200() throws Exception {
		String validData = "{\"id\": \"id1111\",\"name\": \"name1111\",\"login\": \"login100\",\"salary\": 1234.00,\"startDate\": \"01-Jan-16\"}";
		mvc.perform(MockMvcRequestBuilders.post("/users").content(validData).contentType(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().isCreated());
		assertTrue(empSalaryRepo.existsById("id1111"));
	}

	@Test
	public void givenEmpIdExists_whencreateEmpSalInfo_thenStatus400() throws Exception {
		String empIdExistsData = "{\"id\": \"id1\",\"name\": \"name1\",\"login\": \"loginabc\",\"salary\": 1234.00,\"startDate\": \"2001-11-16\"}";
		mvc.perform(
				MockMvcRequestBuilders.post("/users").content(empIdExistsData).contentType(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().isBadRequest())
				.andExpect(MockMvcResultMatchers.jsonPath("$.message", Matchers.is("Employee ID Already exists")));
	}

	@Test
	public void givenLoginExists_whencreateEmpSalInfo_thenStatus400() throws Exception {
		String loginExistsData = "{\"id\": \"id1avc\",\"name\": \"name1\",\"login\": \"login1\",\"salary\": 1234.00,\"startDate\": \"2001-11-16\"}";
		mvc.perform(
				MockMvcRequestBuilders.post("/users").content(loginExistsData).contentType(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().isBadRequest())
				.andExpect(MockMvcResultMatchers.jsonPath("$.message", Matchers.is("Employee login not unique")));
	}

	@Test
	public void givenInValidDate_whencreateEmpSalInfo_thenStatus400() throws Exception {
		String validData = "{\"id\": \"id1111\",\"name\": \"name1111\",\"login\": \"login100\",\"salary\": 1234.00,\"startDate\": \"20011116\"}";
		mvc.perform(MockMvcRequestBuilders.post("/users").content(validData).contentType(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().isBadRequest())
				.andExpect(MockMvcResultMatchers.jsonPath("$.message", Matchers.is("Invalid date")));
	}

	@Test
	public void givenInValidSalary_whencreateEmpSalInfo_thenStatus400() throws Exception {
		String validData = "{\"id\": \"id1111\",\"name\": \"name1111\",\"login\": \"login100\",\"salary\": 1234,\"startDate\": \"2001-11-16\"}";
		mvc.perform(MockMvcRequestBuilders.post("/users").content(validData).contentType(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().isBadRequest())
				.andExpect(MockMvcResultMatchers.jsonPath("$.message", Matchers.is("Invalid salary")));
	}

	@Test
	public void givenInValidSalary_2_whencreateEmpSalInfo_thenStatus400() throws Exception {
		String validData = "{\"id\": \"id1111\",\"name\": \"name1111\",\"login\": \"login100\",\"salary\": \"abc\",\"startDate\": \"2001-11-16\"}";
		mvc.perform(MockMvcRequestBuilders.post("/users").content(validData).contentType(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().isBadRequest())
				.andExpect(MockMvcResultMatchers.jsonPath("$.message", Matchers.is("Invalid salary")));
	}

	@Test
	public void givenNonCsvFile_whenUploadFile_thenStatus400() throws Exception {
		cleanUpRecords();
		MockMultipartFile file = createFileToUpload("invalidformat.txt", "text/plain");

		mvc.perform(MockMvcRequestBuilders.multipart("/users/upload").file(file).characterEncoding("UTF-8"))
				.andExpect(MockMvcResultMatchers.status().isBadRequest()).andExpect(MockMvcResultMatchers.jsonPath(
						"$.message", Matchers.is("Only text/csv files are supported but received text/plain")));

	}

	@Test
	public void givenValidCsvFile_whenUploadFile_thenStatus200() throws Exception {
		cleanUpRecords();
		MockMultipartFile file = createFileToUpload("validfile1.csv", "text/csv");

		mvc.perform(MockMvcRequestBuilders.multipart("/users/upload").file(file).characterEncoding("UTF-8"))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.jsonPath("$.message", Matchers.is("File processed successfully")));

		assertEquals(3, empSalaryRepo.findAll().size());

	}

	@Test
	public void givenValidCsvFileWithComments_whenUploadFile_thenStatus200IgnoringCommentedRows() throws Exception {
		cleanUpRecords();
		// Ignore commentted records and store only non commented rows.
		MockMultipartFile file = createFileToUpload("validfile2.csv", "text/csv");

		mvc.perform(MockMvcRequestBuilders.multipart("/users/upload").file(file).characterEncoding("UTF-8"))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.jsonPath("$.message", Matchers.is("File processed successfully")));

		assertEquals(3, empSalaryRepo.findAll().size());

	}

	@Test
	public void givenValidCsvFileUploadedTwice_whenUploadFile_thenStatus200WithNoDuplicates() throws Exception {
		cleanUpRecords();
		// Ignore commented records and store only non commented rows.
		MockMultipartFile file = createFileToUpload("validfile1.csv", "text/csv");

		mvc.perform(MockMvcRequestBuilders.multipart("/users/upload").file(file).characterEncoding("UTF-8"))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.jsonPath("$.message", Matchers.is("File processed successfully")));

		mvc.perform(MockMvcRequestBuilders.multipart("/users/upload").file(file).characterEncoding("UTF-8"))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.jsonPath("$.message", Matchers.is("File processed successfully")));

		assertEquals(3, empSalaryRepo.findAll().size());

	}

	@Test
	public void givenValidCsvFileWithEmptyLines_whenUploadFile_thenStatus200() throws Exception {
		cleanUpRecords();
		MockMultipartFile file = createFileToUpload("validfile3.csv", "text/csv");

		mvc.perform(MockMvcRequestBuilders.multipart("/users/upload").file(file).characterEncoding("UTF-8"))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.jsonPath("$.message", Matchers.is("File processed successfully")));

		assertEquals(60, empSalaryRepo.findAll().size());

	}

	@Test
	public void givenInValidCsvFileWithDuplicateEmpId_whenUploadFile_thenStatus400() throws Exception {
		cleanUpRecords();
		MockMultipartFile file = createFileToUpload("duplicate_empid_file.csv", "text/csv");

		mvc.perform(MockMvcRequestBuilders.multipart("/users/upload").file(file).characterEncoding("UTF-8"))
				.andExpect(MockMvcResultMatchers.status().isBadRequest()).andExpect(
						MockMvcResultMatchers.jsonPath("$.message", Matchers.containsString("Employee Id not unique")));

	}

	@Test
	public void givenInValidCsvFileWithDuplicateLoginId_whenUploadFile_thenStatus400() throws Exception {
		cleanUpRecords();
		MockMultipartFile file = createFileToUpload("duplicate_login_file.csv", "text/csv");

		mvc.perform(MockMvcRequestBuilders.multipart("/users/upload").file(file).characterEncoding("UTF-8"))
				.andExpect(MockMvcResultMatchers.status().isBadRequest()).andExpect(MockMvcResultMatchers
						.jsonPath("$.message", Matchers.containsString("Employee login not unique")));

	}

	@Test
	public void givenInValidDateInCsvFile_whenUploadFile_thenStatus400() throws Exception {
		cleanUpRecords();
		MockMultipartFile file = createFileToUpload("invalid-dateformat.csv", "text/csv");

		mvc.perform(MockMvcRequestBuilders.multipart("/users/upload").file(file).characterEncoding("UTF-8"))
				.andExpect(MockMvcResultMatchers.status().isBadRequest())
				.andExpect(MockMvcResultMatchers.jsonPath("$.message", Matchers.containsString("Invalid date")));

	}

	@Test
	public void givenInValidDateFormat1InCsvFile_whenUploadFile_thenStatus400() throws Exception {
		cleanUpRecords();
		MockMultipartFile file = createFileToUpload("invalid-dateformat1.csv", "text/csv");

		mvc.perform(MockMvcRequestBuilders.multipart("/users/upload").file(file).characterEncoding("UTF-8"))
				.andExpect(MockMvcResultMatchers.status().isBadRequest())
				.andExpect(MockMvcResultMatchers.jsonPath("$.message", Matchers.containsString("Invalid date")));

	}

	@Test
	public void givenInValidSalaryNonDecimal1InCsvFile_whenUploadFile_thenStatus400() throws Exception {
		cleanUpRecords();
		MockMultipartFile file = createFileToUpload("invalid-salary1.csv", "text/csv");

		mvc.perform(MockMvcRequestBuilders.multipart("/users/upload").file(file).characterEncoding("UTF-8"))
				.andExpect(MockMvcResultMatchers.status().isBadRequest())
				.andExpect(MockMvcResultMatchers.jsonPath("$.message", Matchers.containsString("Invalid salary")));

	}

	@Test
	public void givenInValidSalaryWithAlphabets1InCsvFile_whenUploadFile_thenStatus400() throws Exception {
		cleanUpRecords();
		MockMultipartFile file = createFileToUpload("invalid-salary2.csv", "text/csv");

		mvc.perform(MockMvcRequestBuilders.multipart("/users/upload").file(file).characterEncoding("UTF-8"))
				.andExpect(MockMvcResultMatchers.status().isBadRequest())
				.andExpect(MockMvcResultMatchers.jsonPath("$.message", Matchers.containsString("Invalid salary")));

	}

	@Test
	public void givenValidCsvWithNonUniqueLoginInDB_whenUploadFile_thenStatus400() throws Exception {
		cleanUpRecords();

		EmpSalary empSalary = new EmpSalary("DuplicateLoginUser", "testa", "testa", 10.2,LocalDate.now());
		empSalaryRepo.save(empSalary);
		
		MockMultipartFile file = createFileToUpload("login_testa_notunique_db.csv", "text/csv");

		mvc.perform(MockMvcRequestBuilders.multipart("/users/upload").file(file).characterEncoding("UTF-8"))
				.andExpect(MockMvcResultMatchers.status().isBadRequest())
				.andExpect(MockMvcResultMatchers.jsonPath("$.message", Matchers.containsString("Record is not unique")));

	}

	@Test
	public void givenInValidCsvFileWithMissingHeaders_whenUploadFile_thenStatus400() throws Exception {
		cleanUpRecords();
		MockMultipartFile file = createFileToUpload("invalidfile-missingheader.csv", "text/csv");

		mvc.perform(MockMvcRequestBuilders.multipart("/users/upload").file(file).characterEncoding("UTF-8"))
				.andExpect(MockMvcResultMatchers.status().isBadRequest())
				.andExpect(MockMvcResultMatchers.jsonPath("$.message", Matchers.containsString("Header is missing")));

	}

	@Test
	public void givenInValidCsvFileWithMissingValue_whenUploadFile_thenStatus400() throws Exception {
		cleanUpRecords();
		MockMultipartFile file = createFileToUpload("invalidfile-missingvalue.csv", "text/csv");

		mvc.perform(MockMvcRequestBuilders.multipart("/users/upload").file(file).characterEncoding("UTF-8"))
				.andExpect(MockMvcResultMatchers.status().isBadRequest()).andExpect(
						MockMvcResultMatchers.jsonPath("$.message", Matchers.containsString("no value was provided")));

	}
	
	@Test
	public void givenValidCsvFileWithNonEnglishChars_whenUploadFile_thenStatus200() throws Exception {
		cleanUpRecords();
		MockMultipartFile file = createFileToUpload("validNonEnglishChar.csv", "text/csv");

		mvc.perform(MockMvcRequestBuilders.multipart("/users/upload").file(file).characterEncoding("UTF-8"))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.jsonPath("$.message", Matchers.is("File processed successfully")));

	}
	
	@Test
	public void givenInValidSalaryWithNegativeValueInCsvFile_whenUploadFile_thenStatus400() throws Exception {
		cleanUpRecords();
		MockMultipartFile file = createFileToUpload("invalid-salary3.csv", "text/csv");

		mvc.perform(MockMvcRequestBuilders.multipart("/users/upload").file(file).characterEncoding("UTF-8"))
				.andExpect(MockMvcResultMatchers.status().isBadRequest())
				.andExpect(MockMvcResultMatchers.jsonPath("$.message", Matchers.containsString("Invalid salary")));

	}
	
	@Test
	public void givenWithAdditionalInvalidFilterCriteria_whenGetAllEmpSalInfo_thenStatus400()
			throws Exception {
		
		createDummyRecords(20);
		mvc.perform(MockMvcRequestBuilders.get("/users").param("limit", "5").param("minSalary", "0.0")
				.param("maxSalary", "50000000.00").param("sortCriteria.sortField", "id")
				.param("sortCriteria.sortOrder", "desc").param("filterCriteria.key", "id")
				.param("filterCriteria.operation", "INVALID").param("filterCriteria.value", "2")
				.contentType(MediaType.APPLICATION_JSON)).andExpect(MockMvcResultMatchers.status().isBadRequest());

	}
	
	@Test
	public void givenWithInvalidSortOrder_whenGetAllEmpSalInfo_thenStatus400()
			throws Exception {
		
		createDummyRecords(20);
		mvc.perform(MockMvcRequestBuilders.get("/users").param("limit", "5").param("minSalary", "0.0")
				.param("maxSalary", "50000000.00").param("sortCriteria.sortField", "id")
				.param("sortCriteria.sortOrder", "INVALID")
				.contentType(MediaType.APPLICATION_JSON)).andExpect(MockMvcResultMatchers.status().isBadRequest());

	}
	
	@Test
	public void givenInValidSalaryInNagative_whencreateEmpSalInfo_thenStatus400() throws Exception {
		String validData = "{\"id\": \"id1111\",\"name\": \"name1111\",\"login\": \"login100\",\"salary\": -10.50,\"startDate\": \"2001-11-16\"}";
		mvc.perform(MockMvcRequestBuilders.post("/users").content(validData).contentType(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().isBadRequest())
				.andExpect(MockMvcResultMatchers.jsonPath("$.message", Matchers.is("Invalid salary")));
	}
	
	@Test
	public void givenMissingValues_whencreateEmpSalInfo_thenStatus400() throws Exception {
		String validData = "{\"id\": \"\",\"name\": \"name1111\",\"login\": \"login100\",\"salary\": 10.50,\"startDate\": \"2001-11-16\"}";
		mvc.perform(MockMvcRequestBuilders.post("/users").content(validData).contentType(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().isBadRequest())
				.andExpect(MockMvcResultMatchers.jsonPath("$.message", Matchers.containsString("field is required")));
	}
	
	@Test
	public void givenMissingAttributes_whencreateEmpSalInfo_thenStatus400() throws Exception {
		String validData = "{\"login\": \"login100\",\"salary\": 10.50,\"startDate\": \"2001-11-16\"}";
		mvc.perform(MockMvcRequestBuilders.post("/users").content(validData).contentType(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().isBadRequest())
				.andExpect(MockMvcResultMatchers.jsonPath("$.message", Matchers.containsString("field is required")));
	}
	
	private void createDummyRecords(int count) {
		List<EmpSalary> empSalList = new ArrayList<EmpSalary>();
		for (int i = 1; i <= count; i++) {
			EmpSalary empSal = new EmpSalary("id" + i, "login" + i, "name" + i, 1500.00 * i, LocalDate.now());
			empSalList.add(empSal);
		}
		empSalaryRepo.saveAll(empSalList);
	}

	private void cleanUpRecords() {
		empSalaryRepo.deleteAll();
	}

	private MockMultipartFile createFileToUpload(String fileName, String contentType) throws IOException {
		File file = ResourceUtils.getFile("classpath:" + fileName);
		return new MockMultipartFile("file", fileName, contentType, Files.readAllBytes(file.toPath()));
	}
}
