package com.s8.core.db.tellurium.tools;

import java.io.IOException;
import java.nio.channels.FileChannel;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

import com.s8.core.db.tellurium.store.TeDatabaseHandler;
import com.s8.core.db.tellurium.store.TeStoreMetadata;
import com.s8.core.io.json.JSON_Lexicon;
import com.s8.core.io.json.types.JSON_CompilingException;
import com.s8.core.io.json.utilities.JOOS_BufferedFileWriter;

public class DbInitiator {
	

	public static void init(String rootFolderPathname) throws IOException, JSON_CompilingException {
		
		/* create database folder */
		Path rootFolderPath = Path.of(rootFolderPathname);
		Files.createDirectories(rootFolderPath);

		/* create metadata */
		TeStoreMetadata metadata = new TeStoreMetadata();
		metadata.rootFolderPathname = rootFolderPathname;
		
		
		/* delete any previous record */
		Path metadataFilePath = rootFolderPath.resolve(TeDatabaseHandler.METADATA_FILENAME);
		Files.delete(metadataFilePath);
		
		/* write metadata */
		FileChannel channel = FileChannel.open(metadataFilePath, StandardOpenOption.WRITE, StandardOpenOption.CREATE);
		JSON_Lexicon lexicon = JSON_Lexicon.from(TeStoreMetadata.class);
		JOOS_BufferedFileWriter writer = new JOOS_BufferedFileWriter(channel, StandardCharsets.UTF_8, 256);
		lexicon.compose(writer, metadata, "   ", false);
		writer.close();
	}

}
