package com.reply.spartans.datacenter;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.DirectoryStream;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Scanner;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.stream.Collectors;

import javax.script.ScriptException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.reply.spartans.datacenter.model.Coordinate;
import com.reply.spartans.datacenter.model.Datafarm;
import com.reply.spartans.datacenter.model.Pool;
import com.reply.spartans.datacenter.model.ProblemParameters;
import com.reply.spartans.datacenter.model.RemainingSlotSpace;
import com.reply.spartans.datacenter.model.Server;
import com.reply.spartans.datacenter.model.ServerAssignment;
import com.reply.spartans.datacenter.model.ServerQualityComparator;
import com.reply.spartans.datacenter.model.Slot;
import com.reply.spartans.datacenter.model.Solution;

public class Main {

	private static final Logger log = LoggerFactory.getLogger(Main.class);

	public static void main(String[] args) throws Exception {
		if (args.length != 2) {
			log.error("Needs exactly two arguments: input file and output file");
			return;
		}

		final Path path = FileSystems.getDefault().getPath(args[0]);
		final DirectoryStream<Path> inputFilesStream = Files.newDirectoryStream(path,
				file -> file.toString().endsWith(".in"));
		for (final Path inputFile : inputFilesStream) {
			final Path outputFile = FileSystems.getDefault()
					.getPath(args[1], inputFile.getFileName().toString().replaceAll("\\.in$", ".out")).toAbsolutePath();

			final ProblemParameters parameters = Main.readInputParametersFrom(inputFile.toString());

			final Solution result = Main.processSolution(parameters);

			com.google.common.io.Files.createParentDirs(outputFile.toFile());
			Main.renderOutput(result, parameters, outputFile.toString());
		}
	}

	private static Solution processSolution(ProblemParameters parameters)
			throws ScriptException, NoSuchMethodException {
		final Datafarm datafarm = parameters.getDatafarm();
		// ScriptEngineManager scriptEngineManager = new ScriptEngineManager();
		// ScriptEngine baseEngine =
		// scriptEngineManager.getEngineByName("nashorn");
		// baseEngine.eval(new
		// InputStreamReader(Main.class.getClassLoader().getResourceAsStream("main.js")));
		// final Invocable jsEngine = (Invocable) baseEngine;
		//
		// Solution result = (Solution)
		// jsEngine.invokeFunction("allocateServerInFarm",
		// parameters.getDatafarm(),
		// parameters.getServers());

		final Map<Slot, List<Server>> assignments = assignServersToSlots(parameters, datafarm);

		List<List<Server>> serversPerRow = datafarm.rows()
				.map(slotList -> slotList.stream()
						.flatMap((slot) -> assignments.entrySet().stream()
								.filter((assignment) -> assignment.getKey().equals(slot))
								.flatMap(assignment -> assignment.getValue().stream()))
						.collect(Collectors.toList()))
				.collect(Collectors.toList());
		final List<Pool> pools = assignServersToPools(serversPerRow, parameters.getTargetPoolsNumber());

		final Map<Server, Pool> serverToPool = new HashMap<>();
		pools.forEach(pool -> {
			pool.getServers().forEach(server -> {
				serverToPool.put(server, pool);
			});
		});

		// draw server assignment
		final SortedSet<ServerAssignment> solution = new TreeSet<>(
				(first, second) -> first.getServer().getServerId() - second.getServer().getServerId());
		for (int currentRowIndex = 0; currentRowIndex < datafarm.getRowsNumber(); ++currentRowIndex) {
			final StringBuilder row = new StringBuilder();
			int currentSlotNumber = 0;
			for (final Slot slot : datafarm.getServerRow(currentRowIndex)) {
				while (currentSlotNumber < slot.getStartingColumn()) {
					row.append("|    X    |");
					++currentSlotNumber;
				}

				if (assignments.containsKey(slot)) {
					for (final Server server : assignments.get(slot)) {
						final int lastServerSlot = currentSlotNumber + server.getSize();
						solution.add(new ServerAssignment(new Coordinate(currentRowIndex, currentSlotNumber), server,
								serverToPool.get(server)));
						while (currentSlotNumber < lastServerSlot) {
							// row.append("| s" + server.getServerId() + " p " +
							// serverToPool.get(server).getPoolId() + " |");
							row.append(String.format("| s%2d p%2d |", server.getServerId(),
									serverToPool.get(server).getPoolId()));
							++currentSlotNumber;
						}
					}
				}
			}
			while (currentSlotNumber < parameters.getRowSlots()) {
				row.append("|    X    |");
				++currentSlotNumber;
			}
			System.out.println(row);
		}

		final Set<Server> unassignedServers = new HashSet<>(parameters.getServers());
		unassignedServers.removeAll(solution.stream().map(ServerAssignment::getServer).collect(Collectors.toSet()));
		unassignedServers.forEach(server -> solution.add(new ServerAssignment(null, server, null)));

		return new Solution(solution);
	}

	private static List<Pool> assignServersToPools(List<List<Server>> serversPerRow, int targetPoolsNumber) {

		List<SortedSet<Server>> sortedServersPerRow = serversPerRow.stream().map(serverList -> {
			final SortedSet<Server> serversByQuality = new TreeSet<>(new ServerQualityComparator());
			serversByQuality.addAll(serverList);
			return serversByQuality;
		}).collect(Collectors.toList());

		final SortedSet<Pool> pools = new TreeSet<>((first, second) -> {
			int comparison = first.getCapacity() - second.getCapacity();
			return comparison != 0 ? comparison : first.equals(second) ? 0 : -1;
		});
		for (int i = 0; i < targetPoolsNumber; ++i) {
			pools.add(new Pool(i));
		}

		while (!sortedServersPerRow.isEmpty()) {
			for (Iterator<SortedSet<Server>> rowIterator = sortedServersPerRow.iterator(); rowIterator.hasNext();) {
				SortedSet<Server> rowServers = rowIterator.next();
				for (final Pool pool : pools) {
					if (rowServers.isEmpty() == false) {
						final Server mostCapableServer = rowServers.first();
						pool.assignServer(mostCapableServer);
						rowServers.remove(mostCapableServer);
					}
				}
				final Set<Pool> stash = new HashSet<>(pools);
				pools.clear();
				pools.addAll(stash);
				if (rowServers.isEmpty()) {
					rowIterator.remove();
				}
			}
		}

		return new ArrayList<>(pools);
	}

	private static Map<Slot, List<Server>> assignServersToSlots(ProblemParameters parameters, final Datafarm datafarm) {
		final SortedSet<RemainingSlotSpace> slotsByDimension = datafarm.rows().flatMap((item) -> item.stream())
				.map(RemainingSlotSpace::new).collect(Collectors.toCollection(() -> new TreeSet<>((first, second) -> {
					int comparison = first.getRemainingSpace() - second.getRemainingSpace();
					return comparison != 0 ? comparison : first.getBaseSlot().equals(second.getBaseSlot()) ? 0 : -1;
				})));

		final SortedSet<Server> serversByQuality = new TreeSet<>(new ServerQualityComparator());
		serversByQuality.addAll(parameters.getServers());

		final Map<Slot, List<Server>> assignments = new HashMap<>();
		while ((serversByQuality.isEmpty() || serversByQuality.isEmpty()) == false) {
			// assign the largest server to the largest slot
			final Server largestServer = serversByQuality.first();

			final Optional<RemainingSlotSpace> possibleLargestFittingSlot = slotsByDimension.stream()
					.filter((slot) -> slot.getRemainingSpace() >= largestServer.getSize()).findFirst();
			if (possibleLargestFittingSlot.isPresent()) {
				final RemainingSlotSpace largestSlot = possibleLargestFittingSlot.get();

				final List<Server> currentSlotAssignment;
				if (assignments.containsKey(largestSlot.getBaseSlot())) {
					currentSlotAssignment = assignments.get(largestSlot.getBaseSlot());
				} else {
					currentSlotAssignment = new LinkedList<>();
				}
				currentSlotAssignment.add(largestServer);
				assignments.put(largestSlot.getBaseSlot(), currentSlotAssignment);

				slotsByDimension.remove(largestSlot);
				largestSlot.allocate(largestServer.getSize());
				if (!largestSlot.isFull()) {
					slotsByDimension.add(largestSlot);
				}
			}
			serversByQuality.remove(largestServer);
		}
		return assignments;
	}

	private static void renderOutput(Solution result, ProblemParameters parameters, String filename)
			throws IOException {
		try (final PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter(filename)))) {
			writer.print(result.getSolution());
			writer.flush();
		}
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
			/* final int serversNumber = */inputData.nextInt();

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

			problemParameters = new ProblemParameters(datafarm, servers, rowSlots, targetPoolsNumber);
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
				Slot slot = new Slot(0, rowSlots);
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
