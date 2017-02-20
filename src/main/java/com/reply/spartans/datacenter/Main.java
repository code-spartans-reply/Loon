package com.reply.spartans.datacenter;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.util.Scanner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.reply.spartans.datacenter.model.ProblemParameters;
import com.reply.spartans.datacenter.model.Solution;

import eu.reply.hashcode2017.codespartans.Main;

public class Main {

	private static final Logger log = LoggerFactory.getLogger(Main.class);
	
	public static void main(String[] args) {
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
		Preconditions.checkArgument(Preconditions.checkNotNull(filename).trim().isEmpty() == false,
				"You must supply an input file name");

		ProblemParameters problemParameters = null;
		try (final Scanner inputData = new Scanner(FileSystems.getDefault().getPath(filename), "UTF-8")) {
			
			problemParameters = new ProblemParameters();
		}
		return problemParameters;
	}


}
