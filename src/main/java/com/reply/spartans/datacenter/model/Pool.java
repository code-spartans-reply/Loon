package com.reply.spartans.datacenter.model;

import java.util.HashSet;
import java.util.Set;

import com.google.common.collect.ImmutableSet;

/**
 * Pool of servers
 */

public class Pool {
	private final int poolId;
	private final Set<Server> servers;

	private int capacity = 0;
	
	public Pool(int poolId) {
		super();
		this.poolId = poolId;
		this.servers = new HashSet<Server>();
	}
	
	public int getPoolId() {
		return poolId;
	}
	
	public Set<Server> getServers() {
		return ImmutableSet.copyOf(servers);
	}
	
	public void assignServer(final Server server) {
		this.servers.add(server);
		this.capacity += server.getCapacity();
	}
	
	public int getCapacity() {
		return this.capacity;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		
		if ( ! (obj instanceof Pool)) {
			return false;
		}
		
		final Pool other = (Pool) obj;
		return this.poolId == other.poolId;
	}
	
	@Override
	public int hashCode() {
		return Integer.hashCode(this.poolId);
	}
}
