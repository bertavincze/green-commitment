package com.codecool.greencommitment.common;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
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
 *
 *
 */

public class XMLHandler {

    public class WriteToXml {

        public void writeToTeacherXml(ArrayList<Measurement> measurements) {
            DocumentBuilder docBuilder = null;
            Document doc = null;
            Element rootElement = null;
            try {
                DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
                docBuilder = docFactory.newDocumentBuilder();

                // root elements
                doc = docBuilder.newDocument();
                rootElement = doc.createElement("measurements");

                doc.appendChild(rootElement);

                Attr attr = doc.createAttribute("id");
                attr.setValue(measurements.get(0).getId());
                rootElement.setAttributeNode(attr);
            } catch (ParserConfigurationException pce) {
                pce.printStackTrace();
            }
            for (int i = 0; i < measurements.size(); i++) {
                try {
                    // Teacher elements
                    Element staff = doc.createElement("measurement");

                    rootElement.appendChild(staff);

                    // set attribute to staff element
                    //Attr attr = doc.createAttribute("id");
                    //attr.setValue("1");
                    //staff.setAttributeNode(attr);
                    //No need for attribute id for my XML file

                    //Time elements
                    Element time = doc.createElement("time");
                    time.appendChild(doc.createTextNode(measurements.get(i).getTime()));
                    staff.appendChild(time);

                    // Value elements
                    Element value = doc.createElement("value");
                    value.appendChild(doc.createTextNode(measurements.get(i).getValue()));
                    staff.appendChild(value);

                    // Type elements
                    Element type = doc.createElement("type");
                    type.appendChild(doc.createTextNode(String.valueOf(measurements.get(i).getType())));
                    staff.appendChild(type);



                    // write the content into xml file
                    TransformerFactory transformerFactory = TransformerFactory.newInstance();
                    Transformer transformer = transformerFactory.newTransformer();
                    DOMSource source = new DOMSource(doc);
                    StreamResult result = new StreamResult(new File(measurements.get(i).getId()));


                    // Output to console for testing
                    // StreamResult result = new StreamResult(System.out);

                    transformer.transform(source, result);

                    System.out.println(measurements.get(i).getTime() + " " + measurements.get(i).getValue() + " " + measurements.get(i).getType()+ " was sucessfully saved to file!");
                } catch (TransformerException tfe) {
                    tfe.printStackTrace();
                }
            }
        }
}
