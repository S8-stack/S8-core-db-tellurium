package com.s8.core.db.tellurium.store;

import java.io.IOException;

import com.s8.api.flow.table.requests.GetRowS8Request;
import com.s8.api.flow.table.requests.GetRowS8Request.Status;
import com.s8.core.arch.silicon.SiliconChainCallback;
import com.s8.core.arch.silicon.async.MthProfile;
import com.s8.core.arch.titanium.databases.RequestDbMgOperation;
import com.s8.core.arch.titanium.handlers.h3.ConsumeResourceMgAsyncTask;
import com.s8.core.db.tellurium.table.TableHandler;

class GetRowOp extends RequestDbMgOperation<TeStore> {

	public final TeDatabaseHandler handler;

	public final GetRowS8Request request;
	
	
	public GetRowOp(long timeStamp, SiliconChainCallback callback, TeDatabaseHandler dbHandler, GetRowS8Request request) {
		super(timeStamp, null, callback);
		this.handler = dbHandler;
		this.request = request;
	}


	@Override
	public TeDatabaseHandler getHandler() {
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

				TableHandler tableHandler = store.getTableHandler(request.tableId);

				if(tableHandler != null) {
					/* exit point 1 -> continue */
					tableHandler.get(timeStamp, callback, request);
				}
				else {
					/* exit point 2 -> soft fail */
					request.onSucceed(Status.TABLE_DOES_NOT_EXIST, null);
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
