package com.reply.spartans.datacenter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Main {

	private static final Logger log = LoggerFactory.getLogger(Main.class);
	
	public static void main(String[] args) {
		if (args.length != 2) {
			log.error("Needs exactly two arguments: input file and output file");
			return;
		}

	}

}
