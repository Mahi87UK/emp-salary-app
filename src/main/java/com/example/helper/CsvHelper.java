package com.example.helper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.example.exception.FileProcessingException;
import com.example.pojo.EmpSalaryPojo;
import com.example.util.CommentLineFilter;
import com.opencsv.bean.CsvToBeanBuilder;
import com.opencsv.enums.CSVReaderNullFieldIndicator;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class CsvHelper {

	private CommentLineFilter commentLineFilter = new CommentLineFilter();

	public static String TYPE = "text/csv";
	static String[] HEADERs = { "Id", "Title", "Description", "Published" };

	public static boolean hasCSVFormat(MultipartFile file) {
		String fileType = file.getContentType();
		log.debug("Received File content type {}", fileType);
		if (!TYPE.equals(fileType)) {
			log.error("CsvHelper Invalid file content {}", fileType);
			throw new FileProcessingException(
					"Only text/csv files are supported but received " + file.getContentType());
		}

		return true;
	}

	public List<EmpSalaryPojo> csvToEmpSalay(MultipartFile file) throws FileProcessingException {
		List<EmpSalaryPojo> empList = null;
		if (hasCSVFormat(file)) {

			InputStreamReader inputStreamReader;
			try {
				inputStreamReader = new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8);

				BufferedReader reader = new BufferedReader(inputStreamReader);
				empList = new CsvToBeanBuilder<EmpSalaryPojo>(reader).withType(EmpSalaryPojo.class)
						.withIgnoreLeadingWhiteSpace(true).withIgnoreEmptyLine(true).withFilter(commentLineFilter)
						.withFieldAsNull(CSVReaderNullFieldIndicator.NEITHER).build().parse();
			} catch (IOException e) {
				throw new FileProcessingException(e.getMessage());
			}

		}
		return empList;
	}

}
