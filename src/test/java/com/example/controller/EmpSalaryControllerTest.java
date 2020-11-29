package com.example.controller;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.example.criteria.RecordsCriteria;
import com.example.exception.FileProcessingException;
import com.example.exception.InvalidRequestException;
import com.example.model.EmpSalary;
import com.example.service.EmpSalaryService;

@ExtendWith(SpringExtension.class)
@WebMvcTest(EmpSalaryController.class)
public class EmpSalaryControllerTest {

	@Autowired
	private MockMvc mvc;

	@MockBean
	private EmpSalaryService empSalaryService;

	private String testData = "{\"id\": \"id2\",\"name\": \"name2\",\"login\": \"login2\",\"salary\": 1234.00,\"startDate\": \"2001-11-16\"}";

	@Test
	public void givenEmpSalary_whenGetEmpSalInfo_thenReturnJsonArray() throws Exception {

		EmpSalary empSalaryObj = new EmpSalary("id1", "login1", "name1", 10.0, LocalDate.now());

		Mockito.when(empSalaryService.getEmpSalaryInfo("id1")).thenReturn(Optional.of(empSalaryObj));

		mvc.perform(MockMvcRequestBuilders.get("/users/id1").contentType(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.jsonPath("$.id", Matchers.is("id1")));
	}

	@Test
	public void givenNoMatchingRecord_whenGetEmpSalInfo_thenReturnErrorResponse() throws Exception {

		mvc.perform(MockMvcRequestBuilders.get("/users/id2").contentType(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().isBadRequest())
				.andExpect(MockMvcResultMatchers.jsonPath("$.message", Matchers.is("No such employee")));

	}

	@Test
	public void givenMatchingRecord_whenDeletEmpSalInfo_thenReturnSuccess() throws Exception {
		EmpSalary empSalaryObj = new EmpSalary("id1", "login1", "name1", 10.0, LocalDate.now());
		Mockito.doNothing().when(empSalaryService).deleteEmpSalaryInfo("id1");
		Mockito.when(empSalaryService.getEmpSalaryInfo("id1")).thenReturn(Optional.of(empSalaryObj));
		mvc.perform(MockMvcRequestBuilders.delete("/users/id1").contentType(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.jsonPath("$.message", Matchers.is("Successfully deleted")));

	}

	@Test
	public void givenNoMatchingRecord_whenDeletEmpSalInfo_thenReturnErrorResponse() throws Exception {

		mvc.perform(MockMvcRequestBuilders.delete("/users/id1").contentType(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().isBadRequest())
				.andExpect(MockMvcResultMatchers.jsonPath("$.message", Matchers.is("No such employee")));

	}

	@Test
	public void givenMatchingRecord_whenUpdateEmpSalInfo_thenReturnSuccessForPutRequest() throws Exception {
		EmpSalary empSalaryObj = new EmpSalary("id1", "login1", "name1", 10.0, LocalDate.now());
		Mockito.when(empSalaryService.saveEmpSalaryInfo(Mockito.any(EmpSalary.class))).thenReturn(empSalaryObj);

		Mockito.when(empSalaryService.getEmpSalaryInfo("id1")).thenReturn(Optional.of(empSalaryObj));
		mvc.perform(MockMvcRequestBuilders.put("/users/id1").content(testData).contentType(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.jsonPath("$.message", Matchers.is("Successfully updated")));

	}

	@Test
	public void givenNoMatchingRecord_whenUpdateEmpSalInfo_thenReturnErrorResponseForPutRequest() throws Exception {

		mvc.perform(MockMvcRequestBuilders.put("/users/id2").content(testData).contentType(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().isBadRequest())
				.andExpect(MockMvcResultMatchers.jsonPath("$.message", Matchers.is("No such employee")));

	}

	@Test
	public void givenMatchingRecord_whenUpdateEmpSalInfo_thenReturnSuccessForPatchRequest() throws Exception {
		EmpSalary empSalaryObj = new EmpSalary("id1", "login1", "name1", 10.0, LocalDate.now());
		Mockito.when(empSalaryService.saveEmpSalaryInfo(Mockito.any(EmpSalary.class))).thenReturn(empSalaryObj);

		Mockito.when(empSalaryService.getEmpSalaryInfo("id1")).thenReturn(Optional.of(empSalaryObj));
		mvc.perform(
				MockMvcRequestBuilders.patch("/users/id1").content(testData).contentType(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.jsonPath("$.message", Matchers.is("Successfully updated")));

	}

	@Test
	public void givenNoMatchingRecord_whenUpdateEmpSalInfo_thenReturnErrorResponseForPatchRequest() throws Exception {

		mvc.perform(
				MockMvcRequestBuilders.patch("/users/id2").content(testData).contentType(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().isBadRequest())
				.andExpect(MockMvcResultMatchers.jsonPath("$.message", Matchers.is("No such employee")));

	}

	@Test
	public void givenValidRecord_whencreateEmpSalInfo_thenReturnCreated() throws Exception {
		EmpSalary empSalaryObj = new EmpSalary("id1", "login1", "name1", 10.0, LocalDate.now());
		Mockito.when(empSalaryService.createEmpSalaryInfo(Mockito.any(EmpSalary.class))).thenReturn(empSalaryObj);

		Mockito.when(empSalaryService.getEmpSalaryInfo("id1")).thenReturn(Optional.of(empSalaryObj));
		mvc.perform(MockMvcRequestBuilders.post("/users").content(testData).contentType(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().isCreated())
				.andExpect(MockMvcResultMatchers.jsonPath("$.message", Matchers.is("Successfully created")));

	}

	@Test
	public void givenNoMatchingRecord_whencreateEmpSalInfo_thenReturnErrorResponse() throws Exception {

		Mockito.doThrow(new InvalidRequestException("Employee ID Alreadys exists")).when(empSalaryService)
				.createEmpSalaryInfo(Mockito.any(EmpSalary.class));
		mvc.perform(MockMvcRequestBuilders.post("/users").content(testData).contentType(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().isBadRequest())
				.andExpect(MockMvcResultMatchers.jsonPath("$.message", Matchers.is("Employee ID Alreadys exists")));

	}
	
	@Test
	public void givenInValidFile_whenupload_thenReturnError() throws Exception {
		
		MockMultipartFile file = new MockMultipartFile("file", "filename.csv", "text/csv", new byte[1]);
		Mockito.doThrow(new FileProcessingException("Invalid file")).when(empSalaryService).processAndSaveCsvFile(file);	
		
		mvc.perform(MockMvcRequestBuilders.multipart("/users/upload").file(file).characterEncoding("UTF-8"))
				.andExpect(MockMvcResultMatchers.status().isBadRequest())
				.andExpect(MockMvcResultMatchers.jsonPath("$.message", Matchers.is("Invalid file")));
		
		

	}

	@Test
	public void givenValidFile_whenupload_thenReturnSuccessResponse() throws Exception {
		
		MockMultipartFile file = new MockMultipartFile("file", "filename.csv", "text/csv", new byte[1]);
		Mockito.doNothing().when(empSalaryService).processAndSaveCsvFile(file);
		
		
		mvc.perform(MockMvcRequestBuilders.multipart("/users/upload").file(file).characterEncoding("UTF-8"))
		.andExpect(MockMvcResultMatchers.status().isOk())
		.andExpect(MockMvcResultMatchers.jsonPath("$.message", Matchers.is("File processed successfully")));


	}
	
	@Test
	public void givenMatchingCriteria_whenGetAllEmpSalInfo_thenReturnJsonArray() throws Exception {

		EmpSalary empSalaryObj1 = new EmpSalary("id1", "login1", "name1", 10.0, LocalDate.now());
		EmpSalary empSalaryObj2 = new EmpSalary("id2", "login2", "name2", 10.0, LocalDate.now());
		List<EmpSalary> empSalList = new ArrayList<>();
		empSalList.add(empSalaryObj1);
		empSalList.add(empSalaryObj2);
		Mockito.when(empSalaryService.getAllEmpSalaryInfo(Mockito.any(RecordsCriteria.class))).thenReturn(empSalList);

		mvc.perform(MockMvcRequestBuilders.get("/users").contentType(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.jsonPath("$.results",Matchers.hasSize(2)));
	}

	@Test
	public void givenNoMatchingCriteria_whenGetEmpSalInfo_thenReturnEmptyResponse() throws Exception {
		
		Mockito.when(empSalaryService.getAllEmpSalaryInfo(Mockito.any(RecordsCriteria.class))).thenReturn(new ArrayList<EmpSalary>());
		mvc.perform(MockMvcRequestBuilders.get("/users").contentType(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.jsonPath("$").isEmpty());

	}
}
