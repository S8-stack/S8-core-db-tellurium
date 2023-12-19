package com.s8.core.db.tellurium.demos;

import java.io.IOException;

import com.s8.api.flow.table.requests.CreateTableS8Request;
import com.s8.api.flow.table.requests.PutRowS8Request;
import com.s8.core.bohr.beryllium.exception.BeBuildException;
import com.s8.core.db.tellurium.demos.model.DemoUser;
import com.s8.core.db.tellurium.store.TeDatabaseHandler;
import com.s8.core.db.tellurium.tools.DbLauncher;
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
		
		TeDatabaseHandler db = DbLauncher.create(new Class<?>[] { DemoUser.class}, "/Users/pc/qx/db/alphaventor.s8app/tables-db");
		db.createTable(0, null, () -> System.out.println("Terminated"), new CreateTableS8Request("USERS", true) {
			
			@Override
			public void onSucceed(Status status) {
				System.out.println("Done: status = "+status);
				
				DemoUser row = new DemoUser("toto@gmail.com");
				row.elapsedTime = 12.9;
				
				db.putRow(0, null, ()->{}, new PutRowS8Request("USERS", row, false, true) {
					
					@Override
					public void onSucceed(PutRowS8Request.Status status) {
						System.out.println("Put row succeed: status = "+status);
					}
					
					@Override
					public void onError(Exception exception) {
						System.out.println("Put row failed: exception = "+exception);
					}
				});
			}
			
			@Override
			public void onFailed(Exception exception) {
				System.out.println("Failed: exception = "+exception);
			}
		});
		
		
	}

}
