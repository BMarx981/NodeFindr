package application;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.NoSuchElementException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class XMLFileProcessor {
	
	private NodeList nodes = null;
	private Document doc = null;
	private ArrayList<Node> extractedNodes = new ArrayList<Node>();

	public String processStringXML(String fileName) {
		FileReader fileReader;
		String processed = new String();
		try {
			fileReader = new FileReader(fileName);
			BufferedReader buffer = new BufferedReader(fileReader);
			StringBuilder sb = new StringBuilder();
			String line = buffer.readLine();
			while((line = buffer.readLine()) != null) {
				sb.append(line).append("\n");
				processed = sb.toString();
			}
			
			buffer.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return processed;
	}
	
	public void processXMLNodes(String fileName) {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		
		try {
			DocumentBuilder builder = factory.newDocumentBuilder();
			doc = builder.parse(new File(fileName));
			doc.normalize();
			nodes = doc.getElementsByTagName("dataInput");
			
			for (Node item : iterable(nodes)) {
				extractedNodes.add(item);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public ArrayList<Node> getExtractedNodes() {
		return extractedNodes;
	}
	
	/*************************  FindNodesWith function ************************/
	public Node findNodeWith(String searchString, Node node) {
		Node n = null;
		if (!node.hasChildNodes() || node.getTextContent().equals(searchString)) {
			return n;
		} else if (node.hasChildNodes()) {
			n = findNodeWith(searchString, node);
		} else {
			n = null;
		}
		return n;
	}
	
	public ArrayList<Node> findNodesWith(String searchString, Node node) {
		ArrayList<Node> list = new ArrayList<Node>();
		for (Node child : iterable(node.getChildNodes())) {
			Node n = findNodeWith(searchString, child);
			if (n == null) { continue; }
			else if (n != null){
				list.add(n);
			}
		}
		return list;
	}
	
	/*************************  FindNodesWith function ************************/
	
	public static Iterable<Node> iterable(final NodeList nodeList) {
	    return () -> new Iterator<Node>() {

	        private int index = 0;

	        @Override
	        public boolean hasNext() {
	            return index < nodeList.getLength();
	        }

	        @Override
	        public Node next() {
	            if (!hasNext())
	                throw new NoSuchElementException();
	            return nodeList.item(index++); 
	        }
	    };
	}
}
