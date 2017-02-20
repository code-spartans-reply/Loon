package com.reply.spartans.datacenter.model;

import java.util.Collection;
import java.util.Set;

import com.google.common.collect.ImmutableSet;

public class ProblemParameters {

	private final Datafarm datafarm;
	
	private final Set<Server> servers;
	
	private final int targetPoolsNumber;

	public ProblemParameters(Datafarm datafarm, Collection<Server> servers, int targetPoolNumber) {
		this.datafarm = datafarm;
		targetPoolsNumber = targetPoolNumber;
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
}
