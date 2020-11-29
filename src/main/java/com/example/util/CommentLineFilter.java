package com.example.util;

import java.util.Arrays;

import com.opencsv.bean.CsvToBeanFilter;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CommentLineFilter implements CsvToBeanFilter {
	
	
	
	@Override
 	public boolean allowLine(String[] line) {
 		boolean commentLine = line.length > 0 && line[0].trim().startsWith("#");
 		if(commentLine) {
 			log.warn("Ignoring commentted line {}", Arrays.toString(line));
 		}
 		return !commentLine;
    }

 
}
