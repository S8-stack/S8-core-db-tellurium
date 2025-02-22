package com.s8.core.db.tellurium.demos.tools;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.s8.api.flow.table.objects.S8Table;

public class TableIdValidator {
	
	
	
	
	public static void main(String[] args) {
		System.out.println(TableIdValidator.validate("table-Tgzer012973_gXCv"));
	}
	
	
	/**
	 * 
	 * @param id
	 * @return
	 */
	public static boolean validate(String id) {
		Pattern pattern = Pattern.compile(S8Table.ID_REGEX);
		Matcher matcher = pattern.matcher(id);
		return matcher.matches();
	}

}
