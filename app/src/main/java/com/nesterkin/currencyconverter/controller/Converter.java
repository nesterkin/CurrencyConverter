package com.nesterkin.currencyconverter.controller;

public class Converter {

    private double mAmount;
    private double mFromNominal;
    private double mFromValue;
    private double mToNominal;
    private double mToValue;

    public Converter(double mAmount, double mFromNominal, double mFromValue,
                     double mToNominal, double mToValue) {
        this.mAmount = mAmount;
        this.mFromNominal = mFromNominal;
        this.mFromValue = mFromValue;
        this.mToNominal = mToNominal;
        this.mToValue = mToValue;
    }

    public double convert() {
        return (mAmount * mFromValue / mFromNominal) / (mToValue / mToNominal);
    }
}
