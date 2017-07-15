package com.nesterkin.currencyconverter.controller;

import android.support.v4.content.AsyncTaskLoader;
import android.content.Context;

import com.nesterkin.currencyconverter.model.CurrencyList;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;

public class InternetLoader extends AsyncTaskLoader<CurrencyList> {

    private static final String URL_ADDRESS = "http://www.cbr.ru/scripts/XML_daily.asp";
    private CurrencyList mCurrencyList;
    private WeakReference<Context> mContext;

    public InternetLoader(Context context) {
        super(context);
        mContext = new WeakReference<>(context);
    }

    @Override
    protected void onStartLoading() {
        super.onStartLoading();
        forceLoad();
    }

    @Override
    public void deliverResult(CurrencyList data) {
        super.deliverResult(data);
        mCurrencyList = data;
    }

    @Override
    public CurrencyList loadInBackground() {
        Context context = mContext.get();
        HttpURLConnection connection = null;
        try {
            URL url = new URL(URL_ADDRESS);
            connection = (HttpURLConnection) url.openConnection();

            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                mCurrencyList = CurrencyList.readStream(connection.getInputStream());
            }

            if (context != null) {
                CurrencyList.writeFile(context, mCurrencyList);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
        return mCurrencyList;
    }
}
