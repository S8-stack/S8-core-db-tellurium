package com.s8.core.db.tellurium.demos;

import java.io.IOException;

import com.s8.core.bohr.beryllium.exception.BeBuildException;
import com.s8.core.db.tellurium.demos.model.DemoUser;
import com.s8.core.db.tellurium.tools.TeDbUtility;
import com.s8.core.io.json.types.JSON_CompilingException;

/**
 * 
 */
public class Demo02 {
	
	
	/**
	 * 
	 * @param args
	 * @throws JSON_CompilingException 
	 * @throws IOException 
	 * @throws BeBuildException 
	 */
	public static void main(String[] args) throws IOException, JSON_CompilingException, BeBuildException {
		
		TeDbUtility utility = new TeDbUtility("/Users/pc/qx/db/alphaventor.s8app/tables-db");
		
		utility.createDb(new Class<?>[] { DemoUser.class});
		
		DemoUser row = new DemoUser("toto@gmail.com");
		row.elapsedTime = 12.9;
		
		utility.createTable("USERS", row);
		
		
		
	}

}
