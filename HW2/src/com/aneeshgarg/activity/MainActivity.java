/**
 * Assignment #: 2<br>
 * Filename: MainActivity.java<br>
 * Students in Group: Aneesh Garg
 */
package com.aneeshgarg.activity;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.TextView;

public class MainActivity extends Activity {

    private static final String PRICE_FORMAT = "$ % ,.2f";
    private static final String ZERO_DOLLAR = "$ 0.00";
    private static final String PER_SIGN    = "%";

    private EditText            listPriceEditText;
    private RadioGroup          radioGroup;
    private SeekBar             customDiscountBar;
    private TextView            youSaved, youPay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        radioGroup = (RadioGroup) findViewById(R.id.radioGroup);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                calculateDiscountAndSetValues();

            }
        });

        listPriceEditText = (EditText) findViewById(R.id.listPriceEditText);
        listPriceEditText.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                calculateDiscountAndSetValues();

            }
        });

        ((Button) findViewById(R.id.exitButton)).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                finish();
            }
        });

        customDiscountBar = (SeekBar) findViewById(R.id.customDiscountSeekBar);
        customDiscountBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                calculateDiscountAndSetValues();
                ((TextView) findViewById(R.id.customDiscountTextView)).setText(progress + PER_SIGN);
            }
        });

        youSaved = (TextView) findViewById(R.id.saveValueTextView);
        youPay = (TextView) findViewById(R.id.payValueTextView);

    }

    private void calculateDiscountAndSetValues() {
        String priceString = listPriceEditText.getText().toString();
        double price = 0;
        double discount = 0;
        if (priceString.isEmpty())
            listPriceErrorAction();
        else {
            try {
                price = Double.parseDouble(priceString);
            } catch (NumberFormatException numException) {
                Log.e("AneeshGarg", "Excpetion in parsing Listed Price.");
                listPriceErrorAction();
            }

            switch (radioGroup.getCheckedRadioButtonId()) {
                case R.id.radio10:
                    discount = 0.10 * price;
                    break;

                case R.id.radio25:
                    discount = 0.25 * price;
                    break;

                case R.id.radio50:
                    discount = 0.50 * price;
                    break;

                case R.id.radioCustom:
                    discount = customDiscountBar.getProgress() * price / 100;
                    break;
            }
            
            String discountString = String.format(PRICE_FORMAT,discount);
            String payString = String.format(PRICE_FORMAT, price-discount);
            youSaved.setText(discountString);
            youPay.setText(payString);
        }
    }

    private void listPriceErrorAction() {
        listPriceEditText.setError(getResources().getText(R.string.priceError));
        youSaved.setText(ZERO_DOLLAR);
        youPay.setText(ZERO_DOLLAR);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
}
