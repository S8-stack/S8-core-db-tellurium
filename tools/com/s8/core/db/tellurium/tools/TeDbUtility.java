package com.s8.core.db.tellurium.tools;

import java.io.IOException;
import java.nio.channels.FileChannel;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

import com.s8.api.flow.table.objects.RowS8Object;
import com.s8.api.flow.table.requests.CreateTableS8Request;
import com.s8.api.flow.table.requests.PutRowS8Request;
import com.s8.core.arch.silicon.SiliconConfiguration;
import com.s8.core.arch.silicon.SiliconEngine;
import com.s8.core.bohr.beryllium.codebase.BeCodebase;
import com.s8.core.bohr.beryllium.exception.BeBuildException;
import com.s8.core.db.tellurium.store.TeDatabaseHandler;
import com.s8.core.db.tellurium.store.TeStoreMetadata;
import com.s8.core.io.json.JSON_Lexicon;
import com.s8.core.io.json.types.JSON_CompilingException;
import com.s8.core.io.json.utilities.JOOS_BufferedFileWriter;

public class TeDbUtility {

	public final String rootFolderPathname;

	public final Path rootFolderPath;

	public TeDatabaseHandler db;


	public TeDbUtility(String rootFolderPathname) {
		super();
		this.rootFolderPathname = rootFolderPathname;
		this.rootFolderPath = Path.of(rootFolderPathname);
	}


	public void initMetadata() throws IOException, JSON_CompilingException {

		/* create database folder */
		Files.createDirectories(rootFolderPath);

		/* create metadata */
		TeStoreMetadata metadata = new TeStoreMetadata();
		metadata.rootFolderPathname = rootFolderPathname;


		/* delete any previous record */
		Path metadataFilePath = rootFolderPath.resolve(TeDatabaseHandler.METADATA_FILENAME);
		Files.deleteIfExists(metadataFilePath);

		/* write metadata */
		FileChannel channel = FileChannel.open(metadataFilePath, StandardOpenOption.WRITE, StandardOpenOption.CREATE);
		JSON_Lexicon lexicon = JSON_Lexicon.from(TeStoreMetadata.class);
		JOOS_BufferedFileWriter writer = new JOOS_BufferedFileWriter(channel, StandardCharsets.UTF_8, 256);
		lexicon.compose(writer, metadata, "   ", false);
		writer.close();
	}


	/**
	 * 
	 * @param path
	 * @return 
	 * @return
	 * @throws JSON_CompilingException 
	 * @throws BeBuildException 
	 */
	public void createDb(Class<?>... classes) 
			throws JSON_CompilingException, BeBuildException {

		SiliconConfiguration siConfig = new SiliconConfiguration();
		SiliconEngine ng = new SiliconEngine(siConfig);
		ng.start();

		BeCodebase codebase = BeCodebase.from(classes); 

		db = new TeDatabaseHandler(ng, codebase, rootFolderPath, true);	
	}


	public void createTable(String id, RowS8Object... objects) {

		db.createTable(0, null, () -> System.out.println("Terminated"), new CreateTableS8Request(id, true) {

			@Override
			public void onSucceed(Status status) {
				System.out.println("TABLE_CREATION Done: status = "+status);

				
				for(RowS8Object row : objects) {
					db.putRow(0, null, ()->{}, new PutRowS8Request("USERS", row, true, true) {

						@Override
						public void onSucceed(PutRowS8Request.Status status) {
							System.out.println("Put row succeed: status = "+status+", for key = "+row.S8_key);
						}

						@Override
						public void onError(Exception exception) {
							System.out.println("Put row failed: exception = "+exception);
						}
					});	
				}
			}

			@Override
			public void onFailed(Exception exception) {
				System.out.println("TABLE_CREATION Failed: exception = "+exception);
			}
		});

	}
}
