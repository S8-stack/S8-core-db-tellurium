package com.s8.core.db.tellurium.store;

import com.s8.core.io.json.JSON_Field;
import com.s8.core.io.json.JSON_Type;

@JSON_Type(name = "TeStoreMetadata")
public class TeStoreMetadata {


	@JSON_Field(name = "rootFolderPathname") 
	public String rootFolderPathname;


}
