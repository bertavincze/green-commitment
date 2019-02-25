package com.codecool.greencommitment.common;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.io.File;

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

    public void writeToXml(List<Measurement> measurements) {
        DocumentBuilder docBuilder = null;
        Document doc = null;
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
}
