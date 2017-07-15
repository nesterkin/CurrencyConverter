package com.nesterkin.currencyconverter.view;

import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;

import com.nesterkin.currencyconverter.R;
import com.nesterkin.currencyconverter.controller.CacheLoader;
import com.nesterkin.currencyconverter.controller.Converter;
import com.nesterkin.currencyconverter.controller.InternetLoader;
import com.nesterkin.currencyconverter.controller.CurrencyApp;
import com.nesterkin.currencyconverter.model.Currency;
import com.nesterkin.currencyconverter.controller.ConnectivityReceiver;
import com.nesterkin.currencyconverter.model.CurrencyList;

import java.text.DecimalFormat;
import java.util.List;

public class CurrencyActivity extends AppCompatActivity
        implements ConnectivityReceiver.ConnectivityReceiverListener {

    private Spinner mFromSpinner;
    private Spinner mToSpinner;
    private EditText mValueEditText;
    private Button mConvertButton;
    private TextView mResultTextView;
    private ScrollView mScrollView;
    private TextView mErrorTextView;

    private static final int INTERNET_LOADER_ID = 1;
    private static final int CACHE_LOADER_ID = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_currency);

        mFromSpinner = (Spinner) findViewById(R.id.from_currency_spinner);
        mToSpinner = (Spinner) findViewById(R.id.to_currency_spinner);
        mValueEditText = (EditText) findViewById(R.id.value_edit_text);
        mConvertButton = (Button) findViewById(R.id.convert_button);
        mResultTextView = (TextView) findViewById(R.id.result_text_view);
        mScrollView = (ScrollView) findViewById(R.id.scroll_view);
        mErrorTextView = (TextView) findViewById(R.id.load_error_text_view);

        CurrencyApp.getInstance().setConnectivityListener(this);

        mValueEditText.addTextChangedListener(new EditTextWatcher());
        mConvertButton.setOnClickListener(new ButtonClickListener());

        if (checkConnection()) {
            getSupportLoaderManager()
                    .initLoader(INTERNET_LOADER_ID, null, new InternetLoaderCallbacks());
        } else {
            getSupportLoaderManager()
                    .initLoader(CACHE_LOADER_ID, null, new CacheLoaderCallbacks());
        }
    }

    private class InternetLoaderCallbacks implements LoaderManager.LoaderCallbacks<CurrencyList> {

        @Override
        public Loader<CurrencyList> onCreateLoader(int id, Bundle args) {
            return new InternetLoader(CurrencyActivity.this);
        }

        @Override
        public void onLoadFinished(Loader<CurrencyList> loader, CurrencyList data) {
            if (data != null) {
                setSpinner(mFromSpinner, data.getCurrencyList());
                setSpinner(mToSpinner, data.getCurrencyList());
                showScrollLayout(true);
                showErrorText(false);
            }
        }

        @Override
        public void onLoaderReset(Loader<CurrencyList> loader) {
        }
    }

    private class CacheLoaderCallbacks implements LoaderManager.LoaderCallbacks<CurrencyList> {

        @Override
        public Loader<CurrencyList> onCreateLoader(int id, Bundle args) {
            return new CacheLoader(CurrencyActivity.this);
        }

        @Override
        public void onLoadFinished(Loader<CurrencyList> loader, CurrencyList data) {
            if (data != null) {
                setSpinner(mFromSpinner, data.getCurrencyList());
                setSpinner(mToSpinner, data.getCurrencyList());
                showScrollLayout(true);
                showErrorText(false);
            } else {
                showScrollLayout(false);
                showErrorText(true);
            }
        }

        @Override
        public void onLoaderReset(Loader<CurrencyList> loader) {
        }
    }

    public void setSpinner(Spinner spinner, List<Currency> currencyList) {
        ArrayAdapter<Currency> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, currencyList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
    }

    private void showScrollLayout(boolean show) {
        mScrollView.setVisibility(show ? View.VISIBLE : View.GONE);
    }

    private void showErrorText(boolean show) {
        mErrorTextView.setVisibility(show ? View.VISIBLE : View.GONE);
    }

    private class EditTextWatcher implements TextWatcher {

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }

        @Override
        public void afterTextChanged(Editable s) {
            boolean isFilled = true;
            if (TextUtils.isEmpty(mValueEditText.getText())) {
                isFilled = false;
            }
            mConvertButton.setEnabled(isFilled);
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }
    }

    private class ButtonClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            Currency from, to;

            double amount = Double.parseDouble(mValueEditText.getText().toString().trim());
            from = (Currency) mFromSpinner.getSelectedItem();
            to = (Currency) mToSpinner.getSelectedItem();
            Converter converter = new Converter(amount, from.getNominal(), from.getValue(),
                    to.getNominal(), to.getValue());
            double result = converter.convert();
            String resultString = new DecimalFormat("#0.00").format(result);
            mResultTextView.setText(resultString);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        CurrencyApp.getInstance().setConnectivityListener(this);
    }

    private boolean checkConnection() {
        return ConnectivityReceiver.isConnected();
    }

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        if (isConnected) {
            getSupportLoaderManager()
                    .initLoader(INTERNET_LOADER_ID, null, new InternetLoaderCallbacks());
        } else {
            getSupportLoaderManager()
                    .initLoader(CACHE_LOADER_ID, null, new CacheLoaderCallbacks());
        }
    }
}