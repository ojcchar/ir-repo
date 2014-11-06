package edu.wayne.cs.severe.ir4se.processor.boundary;

import edu.wayne.cs.severe.ir4se.processor.controllers.RetrievalProcessor;
import edu.wayne.cs.severe.ir4se.processor.controllers.impl.DefaultRetrievalProcessor;
import edu.wayne.cs.severe.ir4se.processor.utils.GenericUtils;

public class Main {

	public static String paramFile;

	public static void main(String args[]) {

		try {
			paramFile = GenericUtils.parseArguments(args);

			RetrievalProcessor procesor = new DefaultRetrievalProcessor();
			procesor.processSystem(paramFile);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}