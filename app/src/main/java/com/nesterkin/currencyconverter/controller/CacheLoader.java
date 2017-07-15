package com.nesterkin.currencyconverter.controller;

import android.support.v4.content.AsyncTaskLoader;
import android.content.Context;

import com.nesterkin.currencyconverter.model.CurrencyList;

import java.io.IOException;
import java.lang.ref.WeakReference;

public class CacheLoader extends AsyncTaskLoader<CurrencyList> {

    private CurrencyList mCurrencyList;
    private WeakReference<Context> mContext;

    public CacheLoader(Context context) {
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
        if (context != null) {
            try {
                mCurrencyList = CurrencyList.readFile(context);
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
        return mCurrencyList;
    }
}
