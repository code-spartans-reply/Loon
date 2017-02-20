package com.reply.spartans.datacenter.model;

public class Slot {

	private int startingColumn;
	
	private int dimension;

	public Slot(int startingColumn, int dimension) {
		super();
		this.startingColumn = startingColumn;
		this.dimension = dimension;
	}
	
	public int getStartingColumn() {
		return startingColumn;
	}
	
	public int getDimension() {
		return dimension;
	}
}
