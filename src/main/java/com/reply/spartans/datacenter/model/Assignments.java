package com.reply.spartans.datacenter.model;

import java.util.List;
import java.util.stream.Collectors;

public class Assignments {

	private final Slot slot;

	private final List<Server> servers;

	public Assignments(Slot slot, List<Server> servers) {
		super();
		this.slot = slot;
		this.servers = servers;
	}

	public Slot getSlot() {
		return slot;
	}

	public List<Server> getServers() {
		return servers;
	}

	public int getRemainingSpace() {
		return this.slot.getDimension()
				- this.servers.stream().collect(Collectors.summingInt((server) -> server.getSize())).intValue();
	}
}
