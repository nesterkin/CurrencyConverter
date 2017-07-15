package com.nesterkin.currencyconverter.model;

import android.content.Context;
import android.support.annotation.NonNull;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;
import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;
import org.simpleframework.xml.transform.RegistryMatcher;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.nio.charset.Charset;
import java.util.List;

@Root(name = "ValCurs")
public class CurrencyList implements Serializable {

    private final static String CACHE_FILE = "cache.xml";

    @Attribute(name = "Date")
    private String mDate;

    @Attribute(name = "name")
    private String mName;

    @ElementList(entry = "Valute", inline = true)
    private List<Currency> mCurrencyList;

    public List<Currency> getCurrencyList() {
        return mCurrencyList;
    }

    public static CurrencyList readStream(@NonNull InputStream inputStream) throws IOException {

        BufferedReader bufferedReader = new BufferedReader(
                new InputStreamReader(inputStream, Charset.forName("windows-1251")));

        RegistryMatcher registryMatcher = new RegistryMatcher();
        registryMatcher.bind(Double.class, new DoubleTransformer());
        Serializer serializer = new Persister(registryMatcher);

        try {
            return serializer.read(CurrencyList.class, bufferedReader);
        } catch (Exception e) {
            throw new IOException(e);
        }
    }

    public static CurrencyList readFile(Context context) throws IOException, ClassNotFoundException {
        FileInputStream fileInputStream = context.openFileInput(CACHE_FILE);
        ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
        return (CurrencyList) objectInputStream.readObject();
    }

    public static void writeFile(Context context, CurrencyList currencyList) throws IOException {
        FileOutputStream fileOutputStream = null;
        try {
            fileOutputStream = context.openFileOutput(CACHE_FILE, Context.MODE_PRIVATE);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
            objectOutputStream.writeObject(currencyList);
        } finally {
            if (fileOutputStream != null) {
                fileOutputStream.close();
            }
        }
    }

    private void writeObject(java.io.ObjectOutputStream stream)
            throws IOException {
        stream.writeObject(mDate);
        stream.writeObject(mName);
        stream.writeObject(mCurrencyList);
    }

    private void readObject(java.io.ObjectInputStream stream)
            throws IOException, ClassNotFoundException {
        mDate = (String) stream.readObject();
        mName = (String) stream.readObject();
        mCurrencyList = (List<Currency>) stream.readObject();
    }
}