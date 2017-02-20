package com.reply.spartans.datacenter;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.google.common.base.Preconditions.*;

import com.google.common.collect.Collections2;
import com.google.common.collect.Multimaps;
import com.reply.spartans.datacenter.model.Coordinate;
import com.reply.spartans.datacenter.model.Datafarm;
import com.reply.spartans.datacenter.model.ProblemParameters;
import com.reply.spartans.datacenter.model.Server;
import com.reply.spartans.datacenter.model.Solution;

public class Main {

	private static final Logger log = LoggerFactory.getLogger(Main.class);

	public static void main(String[] args) throws Exception {
		if (args.length != 2) {
			log.error("Needs exactly two arguments: input file and output file");
			return;
		}

		final ProblemParameters parameters = Main.readInputParametersFrom(args[0]);

		final Solution result = Main.processSolution(parameters);

		Main.renderOutput(result, parameters);
	}

	private static Solution processSolution(ProblemParameters parameters) {
		// TODO Auto-generated method stub
		return null;
	}

	private static void renderOutput(Solution result, ProblemParameters parameters) {
		// TODO Auto-generated method stub
	}

	private static ProblemParameters readInputParametersFrom(String filename)
			throws FileNotFoundException, IOException {
		checkArgument(checkNotNull(filename).trim().isEmpty() == false,
				"You must supply an input file name");

		ProblemParameters problemParameters = null;
		try (final Scanner inputData = new Scanner(FileSystems.getDefault().getPath(filename), "UTF-8")) {
			final int datacenterRows = inputData.nextInt();
			final int rowSlots = inputData.nextInt();
			final int unavailableDescriptorsNumber = inputData.nextInt();
			final int targetPoolsNumber = inputData.nextInt();
			
			final List<Coordinate> unavailableSlots = new ArrayList<>(unavailableDescriptorsNumber);
			for (int i = 0; i < unavailableDescriptorsNumber; ++i) {
				unavailableSlots.add(new Coordinate(inputData.nextInt(), inputData.nextInt()));
			}
			final Datafarm datafarm = evaluteDatafarm(datacenterRows, rowSlots, unavailableSlots);
			
			final List<Server> servers = new LinkedList<>();
			int serverId = 0;
			while (inputData.hasNextLine()) {
				int size = inputData.nextInt();
				int capacity = inputData.nextInt();
				
				servers.add(new Server(serverId++, capacity, size));
			}
			
			problemParameters = new ProblemParameters(datafarm, servers, targetPoolsNumber);
		}
		return problemParameters;
	}

	private static Datafarm evaluteDatafarm(int datacenterRows, int rowSlots, List<Coordinate> unavailableSlots) {
		return null;
		//pippo
	}

}
