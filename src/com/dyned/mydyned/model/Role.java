package com.dyned.mydyned.model;

import java.util.Locale;

public class Role {
	
	public static final int ROLE_GUEST = 1;
	public static final int ROLE_STUDENT = 2;
	public static final int ROLE_TEACHER = 3;
	
	public Role() {
	}

	public static int getRoleIdByString(String role) {
		if (role.toLowerCase(Locale.getDefault()).contains("teacher")) {
			return ROLE_TEACHER;
		} else if (role.toLowerCase(Locale.getDefault()).contains("student")) {
			return ROLE_STUDENT;
		} else {
			return ROLE_GUEST;
		}
	}
	
}
