package com.reply.spartans.datacenter.model;

import java.util.Collection;
import java.util.Set;

import com.google.common.collect.ImmutableSet;

/**
 * Problems parameters, obtained after parsing the input file
 */

public class ProblemParameters {

	private final Datafarm datafarm;
	
	private final Set<Server> servers;
	
	private final int targetPoolsNumber;

	private int rowSlots;

	public ProblemParameters(Datafarm datafarm, Collection<Server> servers, int rowSlots, int targetPoolsNumber) {
		this.datafarm = datafarm;
		this.rowSlots = rowSlots;
		this.targetPoolsNumber = targetPoolsNumber;
		this.servers = ImmutableSet.copyOf(servers);
	}
	
	public Datafarm getDatafarm() {
		return datafarm;
	}
	
	public Set<Server> getServers() {
		return servers;
	}
	
	public int getTargetPoolsNumber() {
		return targetPoolsNumber;
	}
	
	public int getRowSlots() {
		return rowSlots;
	}
}
