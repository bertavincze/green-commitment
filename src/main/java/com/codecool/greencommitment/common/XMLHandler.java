package com.codecool.greencommitment.common;

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
    private List<Measurement> measurements;

    public XMLHandler() {
        DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder = null;
        try {
            docBuilder = docFactory.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }
        this.doc = docBuilder.newDocument();
        this.measurements = new ArrayList<>();
    }

    public void handleXml(Measurement measurement) {
        if (getFiles(".").contains(String.valueOf(measurement.getId()))) {
            loadXml(measurement.getId() + ".xml");
        }
        measurements.add(measurement);
        Element rootNode = doc.getDocumentElement();
        List<Element> measurementNodes = getElements(rootNode);
        addMeasurement(measurementNodes);
        writeXml(measurement);
    }

    private void writeXml(Measurement measurement) {
        DocumentBuilder docBuilder;
        Element rootElement = null;
        try {
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            docBuilder = docFactory.newDocumentBuilder();
            doc = docBuilder.newDocument();
            rootElement = doc.createElement("measurements");
            doc.appendChild(rootElement);

            doc.createAttribute("id");
            rootElement.setAttribute("id", String.valueOf(measurement.getId()));

        } catch (ParserConfigurationException pce) {
            pce.printStackTrace();
        }
        for (Measurement tempMeasurement : measurements) {
            if (rootElement != null) {
                writeNodes(tempMeasurement, rootElement);
            }
        }
    }

    private void writeNodes(Measurement measurement, Element rootElement) {
        try {
            Element tempMeasurement = doc.createElement("measurement");

            rootElement.appendChild(tempMeasurement);

            Element time = doc.createElement("time");
            time.appendChild(doc.createTextNode(String.valueOf(measurement.getTime())));
            tempMeasurement.appendChild(time);

            Element value = doc.createElement("value");
            value.appendChild(doc.createTextNode(String.valueOf(measurement.getValue())));
            tempMeasurement.appendChild(value);

            Element type = doc.createElement("type");
            type.appendChild(doc.createTextNode(measurement.getMeasurementType().getValue()));
            tempMeasurement.appendChild(type);

            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(new File(measurement.getId() + ".xml"));

            transformer.transform(source, result);

        } catch (TransformerException tfe) {
            tfe.printStackTrace();
        }

    }

    private void loadXml(String filename) {
        try {
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
            InputStream is = new FileInputStream(filename);
            this.doc = docBuilder.parse(is);
            this.doc.getDocumentElement().normalize();
        } catch (ParserConfigurationException | IOException | SAXException e) {
            e.printStackTrace();
        }
    }

    private void addMeasurement(List<Element> measurementNodes) {
        int id = Integer.valueOf(measurementNodes.get(0).getAttribute("id"));
        for (Element measurementNode : measurementNodes) {
            List<Element> fieldNodes = getElements(measurementNode);
            long time = Long.valueOf(getString(fieldNodes, "time"));
            int value = Integer.valueOf(getString(fieldNodes, "value"));
            MeasurementType measurementType = MeasurementType.valueOf((getString(fieldNodes, "type")));

            Measurement measurement = new Measurement(id, time, value, measurementType);
            measurements.add(measurement);
        }
    }

    private String getString(List<Element> elements, String name) {
        for (Element element : elements) {
            if (element.getTagName().equals(name)) {
                return element.getTextContent();
            }
        }
        throw new IllegalStateException();
    }

    private List<Element> getElements(Element parentNode) {
        ArrayList<Element> elements = new ArrayList<>();
        for (int i = 0; i < parentNode.getChildNodes().getLength(); i++) {
            Node childNode = parentNode.getChildNodes().item(i);
            if (childNode instanceof Element) {
                elements.add((Element) childNode);
            }

        }
        return elements;
    }

    private List<String> getFiles(String filePath) {
        List<String> fileNames = new ArrayList<>();
        File folder = new File(filePath);
        File[] listOfFiles = folder.listFiles((dir, name) -> name.endsWith(".xml"));

        for (File file : listOfFiles) {
            if (file.isFile()) {
                System.out.println(file.getName());
                fileNames.add(file.getName());
            }
        }
        return fileNames;
    }
}
