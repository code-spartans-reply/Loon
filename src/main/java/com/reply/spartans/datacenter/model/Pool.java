package com.reply.spartans.datacenter.model;

import java.util.HashSet;
import java.util.Set;

/**
 * Pool of servers
 */

public class Pool {
	private final int poolId;
	private final Set<Server> servers;
	
	public Pool(int poolId) {
		super();
		this.poolId = poolId;
		this.servers = new HashSet<Server>();
	}
	
	public int getPoolId() {
		return poolId;
	}
	
	public Set<Server> getServers() {
		return servers;
	}
	
}
