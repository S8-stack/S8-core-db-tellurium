package com.s8.core.db.tellurium.demos.model;

import com.s8.api.annotations.S8Field;
import com.s8.api.annotations.S8ObjectType;
import com.s8.api.flow.table.objects.RowS8Object;


@S8ObjectType(name = "alpha-user")
public class DemoUser extends RowS8Object {
	
	public @S8Field(name = "elapsed-time") double elapsedTime;

	public DemoUser(String id) {
		super(id);
	}

}
