package com.reply.spartans.datacenter.model;

/**
 * Server assignment, used for modeling the Solution. Mutable.
 */
public class ServerAssignment {
	private Coordinate position;
	private Server server;
	private Pool pool;
		
	public ServerAssignment(Coordinate position, Server server, Pool pool) {
		super();
		this.position = position;
		this.server = server;
		this.pool = pool;
	}
	
	public Coordinate getPosition() {
		return position;
	}
	public void setPosition(Coordinate position) {
		this.position = position;
	}
	public Server getServer() {
		return server;
	}
	public void setServer(Server server) {
		this.server = server;
	}
	public Pool getPool() {
		return pool;
	}
	public void setPool(Pool pool) {
		this.pool = pool;
	}
	
	
}
