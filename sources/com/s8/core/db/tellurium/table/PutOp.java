package com.s8.core.db.tellurium.table;

import com.s8.api.flow.table.requests.PutRowS8Request;
import com.s8.api.flow.table.requests.PutRowS8Request.Status;
import com.s8.core.arch.magnesium.databases.RequestDbMgOperation;
import com.s8.core.arch.magnesium.handlers.h3.ConsumeResourceMgAsyncTask;
import com.s8.core.arch.magnesium.handlers.h3.H3MgHandler;
import com.s8.core.arch.silicon.SiliconChainCallback;
import com.s8.core.arch.silicon.async.MthProfile;
import com.s8.core.bohr.beryllium.branch.BeBranch;


/**
 * 
 * @author pierreconvert
 *
 */
class PutOp extends RequestDbMgOperation<BeBranch> {


	public final TableHandler handler;

	public final PutRowS8Request request;


	public PutOp(long timeStamp, SiliconChainCallback callback, TableHandler handler, PutRowS8Request request) {
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
			public boolean consumeResource(BeBranch branch) throws Exception {
				
				boolean hasBeenModified = false;
				PutRowS8Request.Status status = Status.OK;

				String key = request.row.S8_key;
				
				
				if(request.isOverridingAllowed) {
					branch.put(request.row);
					status = Status.OK;
					hasBeenModified = true;
				}
				else { // no overriding
					if(!branch.hasEntry(key)) {
						branch.put(request.row);
						status = Status.OK;
						hasBeenModified = true;
					}
					else {
						status = Status.ID_CONFLICT;
						hasBeenModified = false;
					}
				}

				if(hasBeenModified && request.isImmediateHDWriteRequired) {
					handler.save();
				}
				
				/* run function */
				request.onSucceed(status);
				callback.call();
				
				return hasBeenModified;
			}

			@Override
			public void catchException(Exception exception) {
				request.onError(exception);
				callback.call();
			}
		};
	}
}
