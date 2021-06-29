package com.jamesngyz.demo.salarymanagement.common;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import com.opencsv.bean.AbstractBeanField;
import com.opencsv.exceptions.CsvConstraintViolationException;
import com.opencsv.exceptions.CsvDataTypeMismatchException;

public class CsvDateConverter<T, I> extends AbstractBeanField<T, I> {
	@Override
	protected Object convert(String s) throws CsvDataTypeMismatchException, CsvConstraintViolationException {
		try {
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern(Constants.DATE_FORMAT_DD_MMM_YY);
			return LocalDate.parse(s, formatter);
			
		} catch (DateTimeParseException e) {
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern(Constants.DATE_FORMAT_YYYY_MM_DD);
			return LocalDate.parse(s, formatter);
		}
	}
	
}
