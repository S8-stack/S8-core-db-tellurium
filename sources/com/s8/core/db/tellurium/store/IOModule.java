package com.s8.core.db.tellurium.store;

import java.io.IOException;
import java.nio.channels.FileChannel;
import java.nio.charset.StandardCharsets;
import java.nio.file.OpenOption;
import java.nio.file.StandardOpenOption;

import com.s8.core.arch.magnesium.handlers.h3.H3MgIOModule;
import com.s8.core.io.json.JSON_Lexicon;
import com.s8.core.io.json.parsing.JSON_ParsingException;
import com.s8.core.io.json.types.JSON_CompilingException;
import com.s8.core.io.json.utilities.JOOS_BufferedFileReader;
import com.s8.core.io.json.utilities.JOOS_BufferedFileWriter;



/**
 * 
 */
class IOModule implements H3MgIOModule<TeStore> {

	private static JSON_Lexicon lexicon;
	
	
	public static JSON_Lexicon JOOS_getLexicon() throws JSON_CompilingException {
	
		return lexicon;
	}

	
	public final TeDatabaseHandler handler;
	
	
	public IOModule(TeDatabaseHandler handler) throws JSON_CompilingException {
		super();
		
		this.handler = handler;
		
		if(lexicon == null) { 
			lexicon = JSON_Lexicon.from(TeStoreMetadata.class); 
		}
	}


	@Override
	public TeStore load() throws IOException, JSON_ParsingException {

		FileChannel channel = FileChannel.open(handler.getMetadataFilePath(), new OpenOption[]{ 
				StandardOpenOption.READ
		});

		/**
		 * lexicon
		 */
		
		JOOS_BufferedFileReader reader = new JOOS_BufferedFileReader(channel, StandardCharsets.UTF_8, 64);
		
		TeStoreMetadata metadata = (TeStoreMetadata) lexicon.parse(reader, true);

		reader.close();

		return new TeStore(handler, handler.codebase, metadata);
	}
	
	

	@Override
	public void save(TeStore repo) throws IOException {

		FileChannel channel = FileChannel.open(handler.getMetadataFilePath(), new OpenOption[]{ 
				StandardOpenOption.WRITE
		});
		
		JOOS_BufferedFileWriter writer = new JOOS_BufferedFileWriter(channel, StandardCharsets.UTF_8, 256);

		lexicon.compose(writer, repo.metadata, "   ", false);

		writer.close();
	}
}
