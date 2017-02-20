package com.reply.spartans.datacenter;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.reply.spartans.datacenter.model.Coordinate;
import com.reply.spartans.datacenter.model.Datafarm;
import com.reply.spartans.datacenter.model.ProblemParameters;
import com.reply.spartans.datacenter.model.Server;
import com.reply.spartans.datacenter.model.ServerAssignment;
import com.reply.spartans.datacenter.model.Slot;
import com.reply.spartans.datacenter.model.Solution;

public class TestAlgorithm {

	private static final Logger log = LoggerFactory.getLogger(TestAlgorithm.class);
	
	public static Solution processSolution(ProblemParameters parameters) {
	
		Set<Server> servers = parameters.getServers();
		
		/*
		HashMap<Integer,Set<Server>> serversBySize = new HashMap<>();
		HashMap<Integer,Set<Server>> serversByCapacity = new HashMap<>();
		
		for (Server server : servers) {
			int size = server.getSize();
			int capacity = server.getCapacity();
			
			Set<Server> sizesSet = serversBySize.get(size);
			if (sizesSet == null) {
				sizesSet = new HashSet<>();
				serversBySize.put(size,sizesSet);
			};
			sizesSet.add(server);
			
			Set<Server> capacitySet = serversByCapacity.get(capacity);
			if (capacitySet == null) {
				capacitySet = new HashSet<>();
				serversByCapacity.put(capacity,capacitySet);
			};			
			capacitySet.add(server);
		}
		*/
		
		ArrayList<Server> serversListByQuality = new ArrayList<>(servers);
		serversListByQuality.sort(new Comparator<Server>() {

			@Override
			public int compare(Server o1, Server o2) {
				double q1 = o1.getQuality();
				double q2 = o2.getQuality();
				
				if (q1 < q2) {
					return -1;
				}
				
				if (q1 > q2) {
					return 1;
				}
				
				return 0;
			}
		});

		Datafarm datafarm = parameters.getDatafarm();
		int rowsNum = datafarm.getRows();
		
		HashMap<Server, ServerAssignment> assignmentMap = new HashMap<>();
		
		for (Iterator<Server> iterator = serversListByQuality.iterator(); iterator.hasNext();) {
			Server server = (Server) iterator.next();
			int neededSize = server.getSize();
			
			//Looking for a perfect size fit
			ServerAssignment assignment = null;
			for (int rowIndex = 0; rowIndex < rowsNum; rowIndex++) {
				List<Slot> rowSlots = datafarm.getServerRow(rowIndex);
				for (Slot slot : rowSlots) {
					if (slot.getDimension() == neededSize) {
						Coordinate position = new Coordinate(slot.getStartingColumn(), rowIndex);
						log.debug("assigning server {} to slot {}", server, position);
						assignment = new ServerAssignment(position, server, null);
						iterator.remove();
						break;
					}
				}
				if (assignment != null) {
					assignmentMap.put(server, assignment);
					break;
				}
			}
		}
		

		
		
		
		int pools = parameters.getTargetPoolsNumber();
		return null;
	}
	
}
