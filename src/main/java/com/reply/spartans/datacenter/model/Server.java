package com.reply.spartans.datacenter.model;

public class Server {

	private final int capacity;
	
	private final int size;

	public Server(int capacity, int size) {
		this.capacity = capacity;
		this.size = size;
	}

	public int getSize() {
		return size;
	}
	
	public int getCapacity() {
		return capacity;
	}
	
}
