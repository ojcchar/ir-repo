package edu.wayne.cs.severe.ir4se.processor.utils;

import edu.wayne.cs.severe.ir4se.processor.exception.ArgsException;
import edu.wayne.cs.severe.ir4se.processor.exception.ParameterException;

public class GenericUtils {

	public static String parseArguments(String[] args)
			throws ParameterException, ArgsException {

		if (args == null) {
			throw new ArgsException();
		}

		if (args.length != 1) {
			throw new ArgsException();
		}

		return args[0];
	}
}
