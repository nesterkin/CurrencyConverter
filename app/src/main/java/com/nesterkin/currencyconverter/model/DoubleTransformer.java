package com.nesterkin.currencyconverter.model;

import org.simpleframework.xml.transform.Transform;

public class DoubleTransformer implements Transform<Double> {

    @Override
    public Double read(String string) throws Exception {
        return Double.parseDouble(string.replace(",", "."));
    }

    @Override
    public String write(Double mDouble) throws Exception {
        return Double.toString(mDouble);
    }
}