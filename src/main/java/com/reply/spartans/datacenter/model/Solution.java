package com.reply.spartans.datacenter.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;

public class Solution {

	private final List<ServerAssignment> assignments;

	public Solution(Collection<ServerAssignment> assignments) {
		super();
		ArrayList<ServerAssignment> solution = new ArrayList<ServerAssignment>(assignments);
		solution.sort(new Comparator<ServerAssignment>() {

			//Sort servers by id to get the ordered list needed for output. Needs well formed assignments (not null)
			@Override
			public int compare(ServerAssignment o1, ServerAssignment o2) {
				int id1 = o1.getServer().getServerId();
				int id2 = o2.getServer().getServerId();
				
				if (id1 < id2) {
					return -1;
				}
				
				if (id1 > id2) {
					return 1;
				}
				
				return 0;
			}
		});
		this.assignments = solution;
	}
	
	public String getSolution() {
		StringBuffer output = new StringBuffer();
		final String unassigned = "x";
		final String lineSeparator = "\n";
		
		for (ServerAssignment assignment : assignments) {
			if (assignment.getPool() == null) {
				output.append(unassigned);
			} else {
				int row = assignment.getPosition().getY();
				int slot = assignment.getPosition().getX();
				int poolId = assignment.getPool().getPoolId();
				output.append(row + " " + slot + " " + poolId);
			}
			output.append(lineSeparator);
		}
		
		return output.toString();
	}
	
}
