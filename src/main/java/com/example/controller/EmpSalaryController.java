package com.example.controller;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.criteria.RecordsCriteria;
import com.example.exception.FileProcessingException;
import com.example.exception.InvalidRequestException;
import com.example.model.EmpSalary;
import com.example.pojo.ResponseMessage;
import com.example.service.EmpSalaryService;
import com.example.util.ResponseGenerator;

import lombok.extern.slf4j.Slf4j;

@Configuration
@RestController
@Slf4j
@CrossOrigin("*")
public class EmpSalaryController {

	@Value("${record.create.success}")
	private String createSuccessMsg;

	@Value("${record.update.success}")
	private String updateSuccessMsg;

	@Value("${record.delete.success}")
	private String deleteSuccessMsg;

	@Value("${record.create.fail}")
	private String createFailsMsg;

	@Value("${record.update.fail}")
	private String updateFailMsg;

	@Value("${record.delete.fail}")
	private String deleteFailMsg;

	@Value("${record.notexists}")
	private String recordNotExistsMsg;

	@Value("${file.process.success}")
	private String fileProcessSuccessMsg;

	@Autowired
	private EmpSalaryService empSalaryService;

	@PostMapping(path = "/users/upload")
	public ResponseEntity<?> uploadFile(@RequestParam(name = "file") MultipartFile file)
			throws IOException, FileProcessingException {
		log.debug("EmpSalaryController uploadFile called ");
		empSalaryService.processAndSaveCsvFile(file);
		return ResponseGenerator.buildResponse(fileProcessSuccessMsg, HttpStatus.OK);
	}

	@GetMapping(path = "/users/{id}")
	public ResponseEntity<Object> getEmpSalaryInfo(@PathVariable("id") String id) {
		log.debug("EmpSalaryController getEmpSalaryInfo {} ", id);
		Optional<EmpSalary> empSalary = empSalaryService.getEmpSalaryInfo(id);
		if (!empSalary.isPresent()) {
			throw new InvalidRequestException(recordNotExistsMsg);
		}
		return ResponseEntity.ok(empSalary.get());

	}

	@GetMapping(path = "/users")
	public ResponseEntity<ResponseMessage> getAllEmpSalaryInfo(@Valid RecordsCriteria recordsCriteria) {
		log.debug("EmpSalaryController getAllEmpSalaryInfo {} ", recordsCriteria);
		List<EmpSalary> empSalaryList = empSalaryService.getAllEmpSalaryInfo(recordsCriteria);
		return ResponseGenerator.buildResponse(empSalaryList, null, HttpStatus.OK);

	}

	@PostMapping(path = "/users")
	public ResponseEntity<ResponseMessage> createEmpSalary(@RequestBody @Valid EmpSalary empSalary)
			throws InvalidRequestException {
		log.debug("EmpSalaryController createEmpSalary {} ", empSalary);
		empSalaryService.createEmpSalaryInfo(empSalary);
		return ResponseGenerator.buildResponse(createSuccessMsg, HttpStatus.CREATED);

	}

	@DeleteMapping(path = "/users/{id}")
	public ResponseEntity<ResponseMessage> deleteEmpSalaryInfo(@PathVariable("id") String id) {
		log.debug("EmpSalaryController deleteEmpSalaryInfo {} ", id);
		Optional<EmpSalary> empSalary = empSalaryService.getEmpSalaryInfo(id);
		if (!empSalary.isPresent()) {
			throw new InvalidRequestException(deleteFailMsg);
		}
		empSalaryService.deleteEmpSalaryInfo(id);
		return ResponseGenerator.buildResponse(deleteSuccessMsg, HttpStatus.OK);

	}

	@RequestMapping(path = "/users/{id}", method = { RequestMethod.PUT, RequestMethod.PATCH })
	public ResponseEntity<ResponseMessage> updateEmpSalaryInfo(@PathVariable("id") String id,
			@RequestBody @Valid EmpSalary empSalaryToUpdate) {
		log.debug("EmpSalaryController updateEmpSalaryInfo {} ", id);
		Optional<EmpSalary> empSalary = empSalaryService.getEmpSalaryInfo(id);
		if (!empSalary.isPresent()) {
			throw new InvalidRequestException(updateFailMsg);
		}
		empSalaryService.saveEmpSalaryInfo(empSalaryToUpdate);
		return ResponseGenerator.buildResponse(updateSuccessMsg, HttpStatus.OK);

	}
}
