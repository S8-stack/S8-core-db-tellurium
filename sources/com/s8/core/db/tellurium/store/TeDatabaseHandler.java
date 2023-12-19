package com.s8.core.db.tellurium.store;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import com.s8.api.flow.S8User;
import com.s8.api.flow.table.objects.RowS8Object;
import com.s8.api.flow.table.objects.S8Table;
import com.s8.api.flow.table.requests.CreateTableS8Request;
import com.s8.api.flow.table.requests.GetRowS8Request;
import com.s8.api.flow.table.requests.PutRowS8Request;
import com.s8.api.flow.table.requests.SelectRowsS8Request;
import com.s8.core.arch.magnesium.handlers.h3.H3MgHandler;
import com.s8.core.arch.magnesium.handlers.h3.H3MgIOModule;
import com.s8.core.arch.silicon.SiliconChainCallback;
import com.s8.core.arch.silicon.SiliconEngine;
import com.s8.core.bohr.beryllium.codebase.BeCodebase;
import com.s8.core.io.json.types.JSON_CompilingException;


/**
 * 
 * @author pc
 *
 */
public class TeDatabaseHandler extends H3MgHandler<TeStore> {

	
	public final static String METADATA_FILENAME = "db-meta.js";
	
	
	/**
	 * 
	 */
	public final Pattern pattern = Pattern.compile(S8Table.ID_REGEX);
	
	public final BeCodebase codebase;
	
	public final Path rootFolderPath;
	
	private final IOModule ioModule;
	
	/**
	 * 
	 * @param ng
	 * @param codebase
	 * @param storeInfoPathname
	 * @param initializer
	 * @throws JSON_CompilingException
	 */
	public TeDatabaseHandler(SiliconEngine ng, 
			BeCodebase codebase, 
			Path rootFolderPath, 
			boolean isSaved) throws JSON_CompilingException {
		super(ng, isSaved);
		this.codebase = codebase;
		this.rootFolderPath = rootFolderPath;
		this.ioModule = new IOModule(this);
	}
	

	@Override
	public String getName() {
		return "store";
	}

	@Override
	public H3MgIOModule<TeStore> getIOModule() {
		return ioModule;
	}

	@Override
	public List<H3MgHandler<?>> getSubHandlers() {
		TeStore store = getResource();
		if(store != null) { 
			return store.getSpaceHandlers(); 
		}
		else {
			return new ArrayList<>();
		}
	}

	
	public Path getFolderPath() {
		return rootFolderPath;
	}
	
	
	public Path getMetadataFilePath() {
		return rootFolderPath.resolve(METADATA_FILENAME);
	}

	
	public static Path getMetadataFilePath(Path rootFolderPath) {
		return rootFolderPath.resolve(METADATA_FILENAME);
	}
	

	/**
	 * 
	 * @param t
	 * @param spaceId
	 * @param onProceed
	 * @param onFailed
	 */
	public void getRow(long t, S8User initiator, SiliconChainCallback callback, GetRowS8Request request) {
		pushOpLast(new GetRowOp(t, callback, this, request));
	}
	
	
	
	/**
	 * 
	 * @param t
	 * @param spaceId
	 * @param onProceed
	 * @param onFailed
	 */
	public void putRow(long t, S8User initiator, SiliconChainCallback callback, PutRowS8Request request) {
		pushOpLast(new PutRowOp(t, callback, this, request));
	}

	

	/**
	 * 
	 * @param t
	 * @param spaceId
	 * @param onProceed
	 * @param onFailed
	 */
	public <T extends RowS8Object> void selectRows(long t, 
			S8User initiator, 
			SiliconChainCallback callback, 
			SelectRowsS8Request<T> request) {
		pushOpLast(new SelectRowsOp<T >(t, callback, this, request));
	}
	
	
	

	/**
	 * 
	 * @param t
	 * @param spaceId
	 * @param onProceed
	 * @param onFailed
	 */
	public <T extends RowS8Object> void createTable(long t, 
			S8User initiator, 
			SiliconChainCallback callback, 
			CreateTableS8Request request) {
		pushOpLast(new CreateTableOp(t, callback, this, request));
	}


	
}
