package com.s8.core.db.tellurium.tools;

import java.nio.file.Path;
import java.nio.file.Paths;

import com.s8.core.arch.silicon.SiliconConfiguration;
import com.s8.core.arch.silicon.SiliconEngine;
import com.s8.core.bohr.beryllium.codebase.BeCodebase;
import com.s8.core.bohr.beryllium.exception.BeBuildException;
import com.s8.core.db.tellurium.store.TeDatabaseHandler;
import com.s8.core.io.json.types.JSON_CompilingException;

public class DbLauncher {
	
	
	
	
	/**
	 * 
	 * @param path
	 * @return
	 * @throws JSON_CompilingException 
	 * @throws BeBuildException 
	 */
	public static TeDatabaseHandler create(Class<?>[] classes, String pathname) 
			throws JSON_CompilingException, BeBuildException {
		
		SiliconConfiguration siConfig = new SiliconConfiguration();
		SiliconEngine ng = new SiliconEngine(siConfig);
		ng.start();
		
		BeCodebase codebase = BeCodebase.from(classes); 
		
		Path path = Paths.get(pathname);
		
		return new TeDatabaseHandler(ng, codebase, path, true);	
	}
}
