package com.s8.core.db.tellurium.store;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.s8.api.flow.repository.objects.RepoS8Object;
import com.s8.core.arch.magnesium.handlers.h3.H3MgHandler;
import com.s8.core.bohr.beryllium.codebase.BeCodebase;
import com.s8.core.db.tellurium.table.TableHandler;


/**
 * 
 * @author pierreconvert
 *
 */
public class TeStore {


	/**
	 * 
	 */
	public final TeDatabaseHandler handler;

	
	/**
	 * 
	 */
	public final BeCodebase codebase;

	
	/**
	 * 
	 */
	public final TeStoreMetadata metadata;
	
	
	/**
	 * 
	 */
	private Path path;

	

	public final Map<String, TableHandler> tableHandlers = new HashMap<>();


	public TeStore(TeDatabaseHandler handler, BeCodebase codebase, TeStoreMetadata metadata) {
		super();
		this.handler = handler;
		this.codebase = codebase;
		this.metadata = metadata;

		String rootPathname = metadata.rootFolderPathname;
		this.path = Path.of(rootPathname);
	}

	
	
	
	/**
	 * 
	 * @param spaceId
	 * @return
	 * @throws IOException
	 */
	TableHandler getTableHandler(String spaceId) throws IOException {
		
		TableHandler tableHandler = tableHandlers.get(spaceId);
		
		if(tableHandler != null) {
			return tableHandler;
		}
		else {
			Path dataFolderPathname = composeTablePath(spaceId);
			boolean isPresent = dataFolderPathname.toFile().exists();
			if(isPresent) {
				/* Already created -> just need to create the handler and tha will automatically load it */
				TableHandler spaceHandler2 = new TableHandler(
						handler.ng, 
						this, 
						spaceId, 
						dataFolderPathname,
						true);

				tableHandlers.put(spaceId, spaceHandler2);
				
				return spaceHandler2;
			}
			else { 
				/* cannot find an existing one and cannot create one */
				return null;
			}
		}
	}
	
	

	/**
	 * 
	 * @param spaceId
	 * @return
	 * @throws IOException
	 */
	TableHandler createTableHandler(String spaceId) throws IOException {
		
		TableHandler tableHandler = tableHandlers.get(spaceId);
		
		if(tableHandler != null) {
			return null; /* -> conflict with already existing one */
		}
		else {
			Path dataFolderPathname = composeTablePath(spaceId);
			boolean isPresent = dataFolderPathname.toFile().exists();
			if(!isPresent) {
				/* Already created -> just need to create the handler and tha will automatically load it */
				TableHandler spaceHandler2 = new TableHandler(
						handler.ng, 
						this, 
						spaceId, 
						dataFolderPathname,
						false);

				tableHandlers.put(spaceId, spaceHandler2);
				
				return spaceHandler2;
			}
			else { 
				/* Already an existing one -> conflict -> null */
				return null;
			}
		}
	}


	

	/**
	 * 
	 * @param repositoryAddress
	 * @return
	 */
	/*
	public MgSpaceHandler getSpaceHandler(String repositoryAddress) {
		return spaceHandlers.computeIfAbsent(repositoryAddress, 
				address -> new MgSpaceHandler(
						handler.ng, 
						this, 
						repositoryAddress, 
						pathComposer.composePath(repositoryAddress)));
	}
	 */

	/*
	private void JOOS_init() {
		try {
			mapLexicon = JOOS_Lexicon.from(MgRepositoryHandler.class);
		} 
		catch (JOOS_CompilingException e) {
			e.printStackTrace();
		}
	}
	 */


	public Path getRootPath() {
		return path;
	}

	private Path composeTablePath(String address) {
		return path.resolve(address);
	}


	public BeCodebase getCodebase() {
		return codebase;
	}


	/**
	 * 
	 * @param id
	 * @param name
	 */
	public void createRepository(String id, String name) {

	}

	public void commit(String repositoryId, String branchId, RepoS8Object[] objects) {

	}







	public List<H3MgHandler<?>> getSpaceHandlers() {
		List<H3MgHandler<?>> unmountables = new ArrayList<>();
		tableHandlers.forEach((k, repo) -> unmountables.add(repo));
		return unmountables;
	}


}
