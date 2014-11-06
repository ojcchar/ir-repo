package edu.wayne.cs.severe.ir4se.processor.boundary;

import java.util.List;

import edu.wayne.cs.severe.ir4se.processor.utils.GenericUtils;

public class MainMassive {

	public static String paramFile;

	public static void main(String[] args) {
		try {
			paramFile = GenericUtils.parseArguments(args);

			MassiveProcessor processor = new MassiveProcessor();
			List<String> configFiles = processor.createConfigFiles(paramFile);
			processor.runConfigurations(configFiles);
			
			processor.summarize(configFiles);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
