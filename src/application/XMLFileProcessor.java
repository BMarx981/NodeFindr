package application;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.NoSuchElementException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

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
	
	private DocumentBuilder getBuilder() {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = null;
		try {
			builder = factory.newDocumentBuilder();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return builder;
	}
	
	public void processXMLNodes(String fileName) {
		try {
			DocumentBuilder builder = getBuilder();
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
		if (node.hasChildNodes()) {
			for (Node child : iterable(node.getChildNodes())) {
				Node n = findNodeWith(searchString, child);
				if (n == null) { continue; }
				else if (n != null){
					list.add(n);
				}
			}
		} else {
			Node n = findNodeWith(searchString, node);
			if (n == null) {}
			else if (n != null){
				System.out.println(n.toString());
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
	
	/************************* xmlToString method ****************************/
	

	public String xmlToString(Node node, boolean omitXmlDeclaration, boolean prettyPrint) {
		if (node == null) {
			throw new IllegalArgumentException("node is null.");
		}

		try {
			// Remove unwanted whitespaces
			node.normalize();
			XPath xpath = XPathFactory.newInstance().newXPath();
			XPathExpression expr = xpath.compile("//text()[normalize-space()='']");
			NodeList nodeList = (NodeList)expr.evaluate(node, XPathConstants.NODESET);

			for (int i = 0; i < nodeList.getLength(); ++i) {
				Node nd = nodeList.item(i);
				nd.getParentNode().removeChild(nd);
			}

			// Create and setup transformer
			Transformer transformer =  TransformerFactory.newInstance().newTransformer();
			transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");

			if (omitXmlDeclaration == true) {
				transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
			}

			if (prettyPrint == true) {
				transformer.setOutputProperty(OutputKeys.INDENT, "yes");
				transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
			}

			// Turn the node into a string
			StringWriter writer = new StringWriter();
			transformer.transform(new DOMSource(node), new StreamResult(writer));
			return writer.toString();
		} catch (TransformerException e) {
			throw new RuntimeException(e);
		} catch (XPathExpressionException e) {
			throw new RuntimeException(e);
		}
	}
}
