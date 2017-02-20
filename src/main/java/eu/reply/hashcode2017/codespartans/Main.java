package eu.reply.hashcode2017.codespartans;


import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.util.Scanner;

import com.google.common.base.Preconditions;

import eu.reply.hashcode2017.codespartans.model.ProblemParameters;
import eu.reply.hashcode2017.codespartans.model.Solution;

public class Main {

	public static void main(String[] args) throws Exception {
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
		Preconditions.checkArgument(Preconditions.checkNotNull(filename).trim().isEmpty() == false,
				"You must supply an input file name");

		ProblemParameters problemParameters = null;
		try (final Scanner inputData = new Scanner(FileSystems.getDefault().getPath(filename), "UTF-8")) {
			
			problemParameters = new ProblemParameters();
		}
		return problemParameters;
	}

}
