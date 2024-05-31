package com.s8.core.db.tellurium.table;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import com.s8.api.flow.table.objects.RowS8Object;
import com.s8.api.flow.table.requests.GetRowS8Request;
import com.s8.api.flow.table.requests.PutRowS8Request;
import com.s8.api.flow.table.requests.SelectRowsS8Request;
import com.s8.core.arch.silicon.SiliconChainCallback;
import com.s8.core.arch.silicon.SiliconEngine;
import com.s8.core.arch.titanium.handlers.h3.H3MgHandler;
import com.s8.core.arch.titanium.handlers.h3.H3MgIOModule;
import com.s8.core.bohr.beryllium.branch.BeBranch;
import com.s8.core.db.tellurium.store.TeStore;


/**
 * 
 * @author pierreconvert
 *
 */
public class TableHandler extends H3MgHandler<BeBranch> {

	
	/**
	 * 
	 */
	public final static String DATA_FILENAME = "branch.be";
	
	
	/**
	 * 
	 */
	private final TeStore store;
	
	
	/**
	 * 
	 */
	private final IOModule ioModule = new IOModule(this);
	
	
	/**
	 * table id
	 */
	private final String id;
	
	/**
	 * 
	 */
	private final Path folderPath;
	
	
	
	/**
	 * 
	 * @param ng
	 * @param store
	 * @param id
	 * @param folderPath
	 */
	public TableHandler(SiliconEngine ng, TeStore store, String id, Path folderPath, boolean isSaved) {
		super(ng, isSaved);
		this.store = store;
		this.id = id;
		this.folderPath = folderPath;
	}
	
	

	@Override
	public String getName() {
		return "workspace hanlder";
	}

	@Override
	public H3MgIOModule<BeBranch> getIOModule() {
		return ioModule;
	}

	@Override
	public List<H3MgHandler<?>> getSubHandlers() {
		return new ArrayList<>(); // no subhandler
	}

	
	/**
	 * 
	 * @return
	 */
	public Path getFolderPath() {
		return folderPath;
	}
	

	public Path getDataFilePath() {
		return folderPath.resolve(DATA_FILENAME);
	}
	

	public TeStore getStore() {
		return store;
	}

	public String getIdentifier() {
		return id;
	}
	
	
	
	
	
	/**
	 * 
	 * @param t
	 * @param callback
	 * @param request
	 */
	public void get(long t, SiliconChainCallback callback, GetRowS8Request request) {
		pushOpLast(new GetOp(t, callback, this, request));
	}
	
	
	
	/**
	 * 
	 * @param t
	 * @param callback
	 * @param request
	 */
	public void put(long t, SiliconChainCallback callback, PutRowS8Request request) {
		pushOpLast(new PutOp(t, callback, this, request));
	}
	
	
	
	/**
	 * 
	 * @param <T>
	 * @param t
	 * @param filter
	 * @param onSelected
	 * @param onFailed
	 */
	public <T extends RowS8Object> void select(long t, SiliconChainCallback callback, SelectRowsS8Request<T> request) {
		pushOpLast(new SelectOp<T>(t, callback, this, request));
	}





}
