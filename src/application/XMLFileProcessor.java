package application;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;

public class XMLFileProcessor {

	
	public String processXML(String fileName) {
		FileReader fileReader;
		String processed = new String();
		try {
			fileReader = new FileReader(fileName);
			BufferedReader buffer = new BufferedReader(fileReader);
			String line = buffer.readLine();
			ArrayList<String> allNodes = new ArrayList<String>();
			


		} catch (Exception e) {
			e.printStackTrace();
		}
		return processed;
	}
}
