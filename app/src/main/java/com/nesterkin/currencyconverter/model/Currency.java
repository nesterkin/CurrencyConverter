package com.nesterkin.currencyconverter.model;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

import java.io.Serializable;

@Root(name = "Valute")
public class Currency implements Serializable {

    @Attribute(name = "ID")
    private String mId;

    @Element(name = "NumCode")
    private int mNumCode;

    @Element(name = "CharCode")
    private String mCharCode;

    @Element(name = "Nominal")
    private Double mNominal;

    @Element(name = "Name")
    private String mName;

    @Element(name = "Value")
    private Double mValue;

    public String getId() {
        return mId;
    }

    public int getNumCode() {
        return mNumCode;
    }

    public String getCharCode() {
        return mCharCode;
    }

    public Double getNominal() {
        return mNominal;
    }

    public String getName() {
        return mName;
    }

    public Double getValue() {
        return mValue;
    }

    @Override
    public String toString() {
        return getName();
    }
}