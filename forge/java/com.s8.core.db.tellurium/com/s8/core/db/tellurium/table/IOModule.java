package com.s8.core.db.tellurium.table;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;

import com.s8.api.bytes.ByteInflow;
import com.s8.core.arch.titanium.handlers.h3.H3MgIOModule;
import com.s8.core.bohr.beryllium.branch.BeBranch;
import com.s8.core.bohr.beryllium.branch.BeInbound;
import com.s8.core.bohr.beryllium.branch.BeOutbound;
import com.s8.core.db.tellurium.store.TeStore;
import com.s8.core.io.bytes.linked.LinkedByteInflow;
import com.s8.core.io.bytes.linked.LinkedByteOutflow;
import com.s8.core.io.bytes.linked.LinkedBytes;
import com.s8.core.io.bytes.linked.LinkedBytesIO;


/**
 * 
 * @author pierreconvert
 *
 */
class IOModule implements H3MgIOModule<BeBranch> {


	public final TableHandler handler;


	/**
	 * 
	 * @param handler
	 */
	public IOModule(TableHandler handler) {
		super();
		this.handler = handler;
	}


	@Override
	public BeBranch load() throws IOException {

		TeStore store = handler.getStore();
		
		boolean isExisting = Files.exists(handler.getDataFilePath(), LinkOption.NOFOLLOW_LINKS);

		if(isExisting) {

			/* read from disk */
			LinkedBytes head = LinkedBytesIO.read(handler.getDataFilePath(), true);

			/* build inflow */
			ByteInflow inflow = new LinkedByteInflow(head);

			/* build inbound session */
			BeInbound inbound = new BeInbound(store.getCodebase());

			/* build branch */
			BeBranch branch = new BeBranch(store.getCodebase(), "t");

			/* load branch */
			inbound.pullFrame(inflow, delta -> branch.pushDelta(delta));

			return branch;

		}
		else {
			BeBranch branch = new BeBranch(store.getCodebase(), "t");

			return branch;
		}
	}
	


	@Override
	public void save(BeBranch branch) throws IOException {

		/* commit changes */
		TeStore store = handler.getStore();
		
		/* build inflow */
		LinkedByteOutflow outflow = new LinkedByteOutflow();

		/* build outbound session */
		BeOutbound outbound = new BeOutbound(store.getCodebase());

		/* push branch */
		outbound.pushFrame(outflow, branch.pullDeltas());
		
		/* create required subdirectories */
		Files.createDirectories(handler.getFolderPath());

		/* read from disk */
		Path path = handler.getFolderPath().resolve(TableHandler.DATA_FILENAME);
		LinkedBytesIO.write(outflow.getHead(), path, true);
	}

}
