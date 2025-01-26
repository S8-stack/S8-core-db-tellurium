package com.s8.core.db.tellurium;

import java.nio.file.Path;
import java.nio.file.Paths;

import com.s8.core.arch.silicon.SiliconEngine;
import com.s8.core.bohr.beryllium.codebase.BeCodebase;
import com.s8.core.bohr.beryllium.exception.BeBuildException;
import com.s8.core.db.tellurium.store.TeDatabaseHandler;
import com.s8.core.io.json.types.JSON_CompilingException;
import com.s8.core.io.xml.annotations.XML_SetElement;
import com.s8.core.io.xml.annotations.XML_Type;


@XML_Type(root=true, name = "Tellurium-config")
public class TeConfiguration {



	public String rootFolderPathname;

	@XML_SetElement(tag = "path")
	public void setRootFolderPathname(String pathname) {
		this.rootFolderPathname = pathname;
	}


	
	/**
	 * 
	 * @param path
	 * @return
	 * @throws JSON_CompilingException 
	 * @throws BeBuildException 
	 */
	public TeDatabaseHandler create(SiliconEngine ng, Class<?>[] classes) 
			throws JSON_CompilingException, BeBuildException {
		
		BeCodebase codebase = BeCodebase.from(classes); 
		
		Path rootFolderPath = Paths.get(rootFolderPathname);
		
		Path metadataFilePath = TeDatabaseHandler.getMetadataFilePath(rootFolderPath);
		
		boolean isSaved = metadataFilePath.toFile().exists();
		
		return new TeDatabaseHandler(ng, codebase, rootFolderPath, isSaved);	
	}
}
