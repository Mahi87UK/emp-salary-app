package com.example.helper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.util.ResourceUtils;

import com.example.exception.FileProcessingException;



@ExtendWith(SpringExtension.class)
public class CsvHelperTest {

	@InjectMocks
	private CsvHelper underTest;
	
	@Test
	public void givenFileIsNotCsv_whencsvToEmpSalay_thenThrowException() throws IOException {
		MockMultipartFile file = createFileToUpload("invalidformat.TXT", "text/plain");
		assertThrows(FileProcessingException.class, () -> {underTest.csvToEmpSalay(file);});
		
	}
	
	@Test
	public void givenValidFile_whencsvToEmpSalay_thenReturnResults() throws IOException {
		MockMultipartFile file = createFileToUpload("validfile1.csv", "text/csv");
		assertEquals(3, underTest.csvToEmpSalay(file).size());
		
	}
	
	private MockMultipartFile createFileToUpload(String fileName, String contentType) throws IOException {
		File file = ResourceUtils.getFile("classpath:" + fileName);
		return new MockMultipartFile("file", fileName, contentType, Files.readAllBytes(file.toPath()));
	}
}
