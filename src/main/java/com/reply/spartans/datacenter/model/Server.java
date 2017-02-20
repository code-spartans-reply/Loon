package com.reply.spartans.datacenter.model;

/**
 * Server model, with capacity and size. 
 * Quality is a weight statistic that helps choosing the best servers in the algorithm
 */

public class Server {

	private final int serverId;
	
	private final int capacity;
	
	private final int size;
	
	private final double quality;

	public Server(int serverId, int capacity, int size) {
		this.serverId = serverId;
		this.capacity = capacity;
		this.size = size;
		this.quality = capacity/size;
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
		return this.quality;
	}
	
}
