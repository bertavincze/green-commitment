package com.codecool.greencommitment.api.common;

import org.w3c.dom.*;
import org.xml.sax.SAXException;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

/**
 * Clients send data to the server in XML format.
 * The server stores its collected data for every sensor in an XML file (The XML file's name should match a sensor's id
 * attribute.)
 */

public class XMLHandler {

    private Document doc;
    private List<Document> measurements;

    public XMLHandler() {
        DocumentBuilder docBuilder = createDocumentBuilder();
        this.doc = docBuilder.newDocument();
        this.measurements = new ArrayList<>();
    }

    public void handleXml(Document document) {
        List<String> fileNames = getFiles(".");
        String id = document.getDocumentElement().getAttribute("id") + ".xml";
        if (fileNames.contains(id)) {
            for (String fileName: fileNames) {
                if (fileName.equals(id)) {
                    loadXml(id);
                    Element rootNode = doc.getDocumentElement();
                    List<Element> measurementNodes = getElements(rootNode);
                    addMeasurement(measurementNodes, rootNode);
                }
            }

        }
        measurements.add(document);
        writeXml(document);
    }

    private void writeXml(Document document) {
        DocumentBuilder docBuilder = createDocumentBuilder();
        doc = docBuilder.newDocument();
        Element rootElement = doc.createElement("measurements");
        doc.appendChild(rootElement);
        rootElement.setAttribute("id", document.getDocumentElement().getAttribute("id"));
        for (Document doc : measurements) {
            writeNodes(doc, rootElement);
        }
    }

    private void writeNodes(Document document, Element rootElement) {
        try {
            Element tempMeasurement = doc.createElement("measurement");
            rootElement.appendChild(tempMeasurement);
            createElement(doc, "time", document.getElementsByTagName("time").item(0).getTextContent(), tempMeasurement);
            createElement(doc, "value", document.getElementsByTagName("value").item(0).getTextContent(), tempMeasurement);
            createElement(doc, "type", document.getElementsByTagName("type").item(0).getTextContent(), tempMeasurement);
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(new File(document.getDocumentElement().getAttribute("id") + ".xml"));
            transformer.transform(source, result);

        } catch (TransformerException tfe) {
            tfe.printStackTrace();
        }

    }

    void loadXml(String filename) {
        try {
            DocumentBuilder docBuilder = createDocumentBuilder();
            InputStream is = new FileInputStream(filename);
            this.doc = docBuilder.parse(is);
            this.doc.getDocumentElement().normalize();
        } catch (IOException | SAXException e) {
            e.printStackTrace();
        }
    }

    private void addMeasurement(List<Element> measurementNodes, Element rootNode) {
        int id = Integer.valueOf(rootNode.getAttribute("id"));
        for (Element measurementNode : measurementNodes) {
            List<Element> fieldNodes = getElements(measurementNode);
            long time = Long.valueOf(getString(fieldNodes, "time"));
            int value = Integer.valueOf(getString(fieldNodes, "value"));
            MeasurementType measurementType = MeasurementType.valueOf((getString(fieldNodes, "type")).toUpperCase());

            Measurement measurement = new Measurement(id, time, value, measurementType);
            measurements.add(measurement.convertToDocument());
        }
    }

    String getString(List<Element> elements, String name) {
        for (Element element : elements) {
            if (element.getTagName().equals(name)) {
                return element.getTextContent();
            }
        }
        throw new IllegalStateException();
    }

    List<Element> getElements(Element parentNode) {
        ArrayList<Element> elements = new ArrayList<>();
        for (int i = 0; i < parentNode.getChildNodes().getLength(); i++) {
            Node childNode = parentNode.getChildNodes().item(i);
            if (childNode instanceof Element) {
                elements.add((Element) childNode);
            }

        }
        return elements;
    }

    public List<String> getFiles(String filePath) {
        List<String> fileNames = new ArrayList<>();
        File folder = new File(filePath);
        File[] listOfFiles = folder.listFiles((dir, name) -> name.endsWith(".xml"));

        for (File file : listOfFiles) {
            if (file.isFile() && !file.getName().equals("pom.xml")) {
                fileNames.add(file.getName());
            }
        }
        return fileNames;
    }

    public DocumentBuilder createDocumentBuilder() {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = null;
        try {
            db = dbf.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }
        return db;
    }

    public void createElement(Document doc, String tagName, String textContent, Element root) {
        Element element = doc.createElement(tagName);
        element.setTextContent(textContent);
        root.appendChild(element);
    }

    public Document getDoc() {
        return doc;
    }

    public List<Document> getMeasurements() {
        return measurements;
    }
}
