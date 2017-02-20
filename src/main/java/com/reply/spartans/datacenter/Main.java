package com.reply.spartans.datacenter;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.FileSystems;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;
import java.util.stream.Collectors;

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
//		parameters.getDatafarm().rows().forEach((item) -> {
//			allSlots.addAll(item);
//		});

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
		return null;
		// pippo
	}

}
