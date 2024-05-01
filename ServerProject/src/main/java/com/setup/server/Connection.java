package com.setup.server;

import com.setup.server.utils.AppUtils;

public class Connection {
	public static void main(String[] args) {
		var em = AppUtils.getEntityManager();
		em.close();
	}
}
