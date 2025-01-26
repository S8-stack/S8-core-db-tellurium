/**
 * 
 */
/**
 * 
 */
module com.s8.core.db.tellurium {
	

	exports com.s8.core.db.tellurium;
	exports com.s8.core.db.tellurium.store;
	exports com.s8.core.db.tellurium.table;

	exports com.s8.core.db.tellurium.tools;
	

	exports com.s8.core.db.tellurium.demos.model;
	
	requires transitive com.s8.core.bohr.beryllium;
	requires transitive com.s8.core.arch.titanium;
	requires transitive com.s8.core.io.bytes;
	requires transitive com.s8.core.io.json;
	requires transitive com.s8.core.io.xml;
	
	
}