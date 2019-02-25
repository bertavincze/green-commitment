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

    public void handleXml(Measurement measurement) {
        // TODO: Check if measurement id equals an already existing filename and call methods accordingly
        List<Measurement> measurements = new ArrayList<Measurement>();
        if (getFiles(".").contains(String.valueOf(measurement.getId()))) {
            loadXml(String.valueOf(measurement.getId() + ".xml"));
        } else {
            writeToXml(measurement);
        }
        Element rootNode = doc.getDocumentElement();
        List<Element> measurementNodes = getElements(rootNode);

        addMeasurement(measurementNodes);
    }

    private void writeRoot(Measurement measurement) {
        DocumentBuilder docBuilder = null;
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
    }


    public void writeToXml(Measurement measurement) {
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

    public void loadXml(String filename) {
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


    public void addMeasurement(List<Element> measurementNodes) {
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
        ArrayList<Element> elements = new ArrayList<Element>();
        for (int i = 0; i < parentNode.getChildNodes().getLength(); i++) {
            Node childnode = parentNode.getChildNodes().item(i);
            if (childnode instanceof Element) {
                elements.add((Element) childnode);
            }

        }
        return elements;
    }

    public List<String> getFiles(String filePath) {
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
