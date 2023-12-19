package com.s8.core.db.tellurium.table;

import com.s8.api.flow.table.objects.RowS8Object;
import com.s8.api.flow.table.requests.GetRowS8Request;
import com.s8.api.flow.table.requests.GetRowS8Request.Status;
import com.s8.core.arch.magnesium.databases.RequestDbMgOperation;
import com.s8.core.arch.magnesium.handlers.h3.ConsumeResourceMgAsyncTask;
import com.s8.core.arch.magnesium.handlers.h3.H3MgHandler;
import com.s8.core.arch.silicon.SiliconChainCallback;
import com.s8.core.arch.silicon.async.MthProfile;
import com.s8.core.bohr.beryllium.branch.BeBranch;
import com.s8.core.bohr.beryllium.exception.BeIOException;



/**
 * 
 */
class GetOp extends RequestDbMgOperation<BeBranch> {

	public final TableHandler handler;

	public final GetRowS8Request request;
	
	
	public GetOp(long timeStamp, SiliconChainCallback callback, TableHandler dbHandler, GetRowS8Request request) {
		super(timeStamp, null, callback);
		this.handler = dbHandler;
		this.request = request;
	}


	@Override
	public H3MgHandler<BeBranch> getHandler() {
		return handler;
	}

	@Override
	public ConsumeResourceMgAsyncTask<BeBranch> createAsyncTask() {
		return new ConsumeResourceMgAsyncTask<BeBranch>(handler) {

			@Override
			public String describe() {
				return "login op";
			}

			@Override
			public MthProfile profile() {
				return MthProfile.FX0;
			}

			@Override
			public boolean consumeResource(BeBranch branch) throws BeIOException {
				RowS8Object object =  (RowS8Object) branch.get(request.rowKey);
				GetRowS8Request.Status status = object != null ? Status.OK : Status.NOT_FOUND;
				request.onSucceed(status , object);
				callback.call();
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
