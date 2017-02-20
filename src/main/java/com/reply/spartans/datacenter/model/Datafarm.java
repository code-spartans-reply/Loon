package com.reply.spartans.datacenter.model;


import java.util.Arrays;
import java.util.List;

/**
 * Datafarm model: constituted by different rows, each with one one or more space slots
 */

public class Datafarm {
	
	final List<Integer>[] serverRows;
	
	@SuppressWarnings("unchecked")
	public Datafarm(int rows) {
		this.serverRows = new List[rows];
	}

	public List<Integer> getServerRow(int index) {
		return serverRows[index];
	}
	
	public void setSlotsSpaceAtRows(int index, Integer... spaces) {
		this.serverRows[index] = Arrays.asList(spaces);
	}
}
