package com.s8.core.db.tellurium.table;

import java.util.List;

import com.s8.api.flow.table.objects.RowS8Object;
import com.s8.api.flow.table.requests.SelectRowsS8Request;
import com.s8.api.flow.table.requests.SelectRowsS8Request.Status;
import com.s8.core.arch.silicon.SiliconChainCallback;
import com.s8.core.arch.silicon.async.MthProfile;
import com.s8.core.arch.titanium.databases.RequestDbMgOperation;
import com.s8.core.arch.titanium.handlers.h3.ConsumeResourceMgAsyncTask;
import com.s8.core.arch.titanium.handlers.h3.H3MgHandler;
import com.s8.core.bohr.beryllium.branch.BeBranch;
import com.s8.core.bohr.beryllium.exception.BeIOException;


/**
 * 
 * @author pierreconvert
 *
 * @param <T>
 */
class SelectOp<T extends RowS8Object> extends RequestDbMgOperation<BeBranch> {



	/**
	 * handler
	 */
	public final TableHandler handler;


	/**
	 * 
	 */
	public final SelectRowsS8Request<T> request;




	public SelectOp(long timeStamp, SiliconChainCallback callback, TableHandler handler, SelectRowsS8Request<T> request) {
		super(timeStamp, null, callback);
		this.handler = handler;
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
				List<T> objects = branch.select(request.filter);
				request.onSucceed(Status.OK, objects);
				callback.call();
				return false; // no resources modified
			}

			@Override
			public void catchException(Exception exception) {
				request.onFailed(exception);
				callback.call();
			}
		};
	}

}
