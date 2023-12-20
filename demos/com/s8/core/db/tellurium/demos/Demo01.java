package com.s8.core.db.tellurium.demos;

import java.io.IOException;

import com.s8.core.db.tellurium.tools.TeDbUtility;
import com.s8.core.io.json.types.JSON_CompilingException;

/**
 * 
 */
public class Demo01 {
	
	
	/**
	 * 
	 * @param args
	 * @throws JSON_CompilingException 
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException, JSON_CompilingException {
		new TeDbUtility("/Users/pc/qx/db/alphaventor.s8app/tables-db").initMetadata();
	}

}
