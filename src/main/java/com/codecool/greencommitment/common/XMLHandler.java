package com.codecool.greencommitment.common;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.*;
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

    public void handleXml() {
        // TODO: Check if measurement id equals an already existing filename and call methods accordingly
    }

    public void writeToXml(List<Measurement> measurements) {
        DocumentBuilder docBuilder = null;
        Element rootElement = null;
        try {
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            docBuilder = docFactory.newDocumentBuilder();
            doc = docBuilder.newDocument();
            rootElement = doc.createElement("measurements");
            doc.appendChild(rootElement);

            doc.createAttribute("id");
            rootElement.setAttribute("id", String.valueOf(measurements.get(0).getId()));

        } catch (ParserConfigurationException pce) {
            pce.printStackTrace();
        }
        for (Measurement measurement : measurements) {
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
    }

    public void loadXml(String filename) {
        DocumentBuilder docBuilder = null;
        Element rootElement = null;
        try {
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            docBuilder = docFactory.newDocumentBuilder();
            InputStream is = new FileInputStream(filename);
            this.doc = docBuilder.parse(is);
            this.doc.getDocumentElement().normalize();
        } catch (ParserConfigurationException | IOException | SAXException e) {
            e.printStackTrace();
        }
    }
}
