package com.reply.spartans.datacenter.model;

/**
 * Server model, with capacity and size. 
 * Quality is a weight statistic that helps choosing the best servers in the algorithm
 */

public class Server {

	private final int serverId;
	
	private final int capacity;
	
	private final int size;
	

	public Server(int serverId, int capacity, int size) {
		this.serverId = serverId;
		this.capacity = capacity;
		this.size = size;
	}

	public int getServerId() {
		return serverId;
	}
	
	public int getSize() {
		return size;
	}
	
	public int getCapacity() {
		return capacity;
	}
	
	public double getQuality() {
		return  capacity/size;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		
		if (! (obj instanceof Server)) {
			return false;
		}
		
		final Server other = (Server) obj;
		return this.serverId == other.serverId;
	}
	
	@Override
	public int hashCode() {
		return Integer.hashCode(this.serverId);
	}
	
	@Override
	public String toString() {
		return "[Server " + this.serverId + ", cap = " + this.capacity + ", size = " + this.size + "]";
	}
}
