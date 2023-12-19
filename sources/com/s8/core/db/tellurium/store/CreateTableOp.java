package com.s8.core.db.tellurium.store;

import java.io.IOException;

import com.s8.api.flow.table.requests.CreateTableS8Request;
import com.s8.api.flow.table.requests.CreateTableS8Request.Status;
import com.s8.core.arch.magnesium.databases.RequestDbMgOperation;
import com.s8.core.arch.magnesium.handlers.h3.ConsumeResourceMgAsyncTask;
import com.s8.core.arch.magnesium.handlers.h3.H3MgHandler;
import com.s8.core.arch.silicon.SiliconChainCallback;
import com.s8.core.arch.silicon.async.MthProfile;
import com.s8.core.bohr.beryllium.branch.BeBranch;
import com.s8.core.db.tellurium.table.TableHandler;


/**
 * 
 * @author pierreconvert
 *
 * @param <T>
 */
class CreateTableOp extends RequestDbMgOperation<TeStore> {



	/**
	 * handler
	 */
	public final TeDatabaseHandler handler;


	/**
	 * 
	 */
	public final CreateTableS8Request request;




	public CreateTableOp(long timeStamp, SiliconChainCallback callback, TeDatabaseHandler handler, CreateTableS8Request request) {
		super(timeStamp, null, callback);
		this.handler = handler;
		this.request = request;
	}


	@Override
	public H3MgHandler<TeStore> getHandler() {
		return handler;
	}

	@Override
	public ConsumeResourceMgAsyncTask<TeStore> createAsyncTask() {
		return new ConsumeResourceMgAsyncTask<TeStore>(handler) {

			@Override
			public String describe() {
				return "login op";
			}

			@Override
			public MthProfile profile() {
				return MthProfile.FX0;
			}

			@Override
			public boolean consumeResource(TeStore store) throws IOException {
				TableHandler tableHandler = store.createTableHandler(request.tableId);

				if(tableHandler != null) {
					
					BeBranch branch = new BeBranch(store.getCodebase(), request.tableId);
					
					/* initialize branch */
					tableHandler.initializeResource(branch);
					
					
					if(request.isSaveImmediatelyRequired) {
						tableHandler.saveImmediately();
					}
					
					/* exit point 1 -> continue */
					request.onSucceed(Status.OK);
				}
				else {
					/* exit point 2 -> soft fail */
					request.onSucceed(Status.TABLE_ALREADY_EXISTS);
					callback.call();
				}
				
				/* no new space created */
				return false;
			}

			@Override
			public void catchException(Exception exception) {
				request.onFailed(exception);
				callback.call();
			}
		};
	}

}
