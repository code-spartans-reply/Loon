package com.reply.spartans.datacenter;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.FileSystems;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.reply.spartans.datacenter.model.Coordinate;
import com.reply.spartans.datacenter.model.Datafarm;
import com.reply.spartans.datacenter.model.ProblemParameters;
import com.reply.spartans.datacenter.model.Server;
import com.reply.spartans.datacenter.model.Slot;
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

	private static Solution processSolution(ProblemParameters parameters)
			throws ScriptException, NoSuchMethodException {
		ScriptEngineManager scriptEngineManager = new ScriptEngineManager();
		ScriptEngine baseEngine = scriptEngineManager.getEngineByName("nashorn");
		baseEngine.eval(new InputStreamReader(Main.class.getClassLoader().getResourceAsStream("main.js")));
		final Invocable jsEngine = (Invocable) baseEngine;

		final List<Slot> allSlots = new LinkedList<>();
		 parameters.getDatafarm().rows().forEach((item) -> {
		 allSlots.addAll(item);
		 });

		Solution result = (Solution) jsEngine.invokeFunction("allocateServerInFarm", allSlots, parameters.getServers());
		return result;
	}

	private static void renderOutput(Solution result, ProblemParameters parameters) {
		// TODO Auto-generated method stub
	}

	private static ProblemParameters readInputParametersFrom(String filename)
			throws FileNotFoundException, IOException {
		checkArgument(checkNotNull(filename).trim().isEmpty() == false, "You must supply an input file name");

		ProblemParameters problemParameters = null;
		try (final Scanner inputData = new Scanner(FileSystems.getDefault().getPath(filename), "UTF-8")) {
			final int datacenterRows = inputData.nextInt();
			final int rowSlots = inputData.nextInt();
			final int unavailableDescriptorsNumber = inputData.nextInt();
			final int targetPoolsNumber = inputData.nextInt();
			final int serversNumber = inputData.nextInt();

			final List<Coordinate> unavailableSlots = new ArrayList<>(unavailableDescriptorsNumber);
			for (int i = 0; i < unavailableDescriptorsNumber; ++i) {
				unavailableSlots.add(new Coordinate(inputData.nextInt(), inputData.nextInt()));
			}
			final Datafarm datafarm = evaluteDatafarm(datacenterRows, rowSlots, unavailableSlots);

			final List<Server> servers = new LinkedList<>();
			int serverId = 0;
			while (inputData.hasNextLine()) {
				if (inputData.hasNextInt()) {
					int size = inputData.nextInt();
					int capacity = inputData.nextInt();

					servers.add(new Server(serverId++, capacity, size));
				} else {
					inputData.nextLine();
				}
			}

			problemParameters = new ProblemParameters(datafarm, servers, targetPoolsNumber);
		}
		return problemParameters;
	}

	private static Datafarm evaluteDatafarm(int datacenterRows, int rowSlots, List<Coordinate> unavailableSlots) {
		// devide unavailable slots: list of y coords for each x
		// example: unavailable slots: (0, 3), (0, 6), (1, 2) become
		// - 0 --> 3, 6
		// - 1 --> 2

		Map<Integer, List<Integer>> yForxMap = new HashMap<Integer, List<Integer>>();
		for (Coordinate pair : unavailableSlots) {
			Integer x = pair.getX();
			List<Integer> ys = yForxMap.get(x);
			if (ys == null) {
				ys = new ArrayList<Integer>();
			}
			ys.add(pair.getY());
			yForxMap.put(x, ys);
		}

		// new datafarm
		Datafarm result = new Datafarm(datacenterRows);

		// for every row, add spaces
		for (int i = 0; i < datacenterRows; i++) {

			List<Slot> slots = new ArrayList<Slot>();

			// get ys for row
			List<Integer> ys = yForxMap.get(i);

			// no holes in current row --> 1 slot
			if (ys == null) {
				Slot slot = new Slot(0, rowSlots - 1);
				slots.add(slot);
			} else {

				int start = 0;
				int size = 0;

				int lastHole = -1;
				for (int y : ys) {
					start = lastHole + 1;
					size = (y - start);
					if (size != 0) {
						slots.add(createSlot(start, size));
					}
					lastHole = y;
				}
				// last in row
				if (lastHole < rowSlots) {
					slots.add(createSlot(lastHole + 1, rowSlots - (lastHole + 1)));
					;
				}

			}

			// add all
			result.setSlotsSpaceAtRows(i, slots);

		}

		return result;

	}

	private static Slot createSlot(int start, int size) {
		return new Slot(start, size);
	}

}
