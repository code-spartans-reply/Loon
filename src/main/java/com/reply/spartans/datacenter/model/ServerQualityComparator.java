package com.reply.spartans.datacenter.model;

import java.util.Comparator;

public final class ServerQualityComparator implements Comparator<Server> {
	@Override
	public int compare(Server first, Server second) {
		int comparison = (int) Math.round(second.getQuality() - first.getQuality()); 
		return (comparison != 0) ? comparison : first.getServerId() - second.getServerId();
	}
}