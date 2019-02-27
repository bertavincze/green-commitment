package com.codecool.greencommitment.api.common;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import javax.xml.parsers.DocumentBuilder;
import java.io.Serializable;

public class Measurement implements Serializable {
    private int id;
    private long time;
    private int value;
    private MeasurementType measurementType;

    public Measurement(int id, int value, MeasurementType measurementType) {
        this.id = id;
        this.time = System.currentTimeMillis();
        this.value = value;
        this.measurementType = measurementType;
    }

    public Measurement(int id, long time, int value, MeasurementType measurementType) {
        this.id = id;
        this.time = time;
        this.value = value;
        this.measurementType = measurementType;
    }

    public Document convertToDocument() {
        XMLHandler xmlHandler = new XMLHandler();
        DocumentBuilder db = xmlHandler.createDocumentBuilder();
        Document doc = db.newDocument();
        Element root = doc.createElement("measurement");
        doc.appendChild(root);
        root.setAttribute("id", String.valueOf(id));
        xmlHandler.createElement(doc, "time", String.valueOf(time), root);
        xmlHandler.createElement(doc, "value", String.valueOf(value), root);
        xmlHandler.createElement(doc, "type", String.valueOf(measurementType).toLowerCase(), root);
        return doc;
    }

    public int getId() {
        return id;
    }

    public long getTime() {
        return time;
    }

    public int getValue() {
        return value;
    }

    public MeasurementType getMeasurementType() {
        return measurementType;
    }
}
