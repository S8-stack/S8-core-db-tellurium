package com.s8.core.db.tellurium.store;

import com.s8.api.flow.table.requests.PutRowS8Request;
import com.s8.api.flow.table.requests.PutRowS8Request.Status;
import com.s8.core.arch.silicon.SiliconChainCallback;
import com.s8.core.arch.silicon.async.MthProfile;
import com.s8.core.arch.titanium.databases.RequestDbMgOperation;
import com.s8.core.arch.titanium.handlers.h3.ConsumeResourceMgAsyncTask;
import com.s8.core.arch.titanium.handlers.h3.H3MgHandler;
import com.s8.core.db.tellurium.table.TableHandler;


/**
 * 
 * @author pierreconvert
 *
 */
class PutRowOp extends RequestDbMgOperation<TeStore> {


	public final TeDatabaseHandler handler;


	public final PutRowS8Request request;


	public PutRowOp(long timeStamp, SiliconChainCallback callback, TeDatabaseHandler handler, PutRowS8Request request) {
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
			public boolean consumeResource(TeStore store) throws Exception {
				
				TableHandler tableHandler = store.getTableHandler(request.tableId);

				if(tableHandler != null) {
					/* exit point 1 -> continue */
					tableHandler.put(timeStamp, callback, request);
				}
				else {
					/* exit point 2 -> soft fail */
					request.onSucceed(Status.TABLE_DOES_NOT_EXIST);
					callback.call();
				}
				
				/* no new space created */
				return false;
			}

			@Override
			public void catchException(Exception exception) {
				request.onError(exception);
				callback.call();
			}
		};
	}
}
