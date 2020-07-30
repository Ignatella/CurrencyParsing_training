package com.example.parcing_html;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Exchanger;

public class MainActivity extends AppCompatActivity {
    public static List<Currency> List_Of_Currency;
    public static TextView current_buy_sell;
    public static TextView valute_name;
    public static EditText pln_c;
    public static EditText curr_c;
    public static Calculator calculator;
    public static double prev_c = 0;
    public static double prev_pln = 0;
    public static int currency = 0;
    public static boolean is_first_call = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        current_buy_sell = findViewById(R.id.current_currency);
        valute_name = findViewById(R.id.valute_name);
        pln_c = findViewById(R.id.pln_c);
        curr_c = findViewById(R.id.curr_c);
        Spinner spinner = (Spinner) findViewById(R.id.spinner);
        spinner.setOnItemSelectedListener(new SpinnerActivity());
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.CURR,
                android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        List_Of_Currency = new ArrayList<Currency>();
        Exchanger<List<Currency>> exchanger = new Exchanger<List<Currency>>();

        new Thread(new PutThread(exchanger)).start();
        try {
            List_Of_Currency = exchanger.exchange(List_Of_Currency);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        calculator = new Calculator(List_Of_Currency);

    }

    public static void Update() {
        current_buy_sell.setText("Buy: " + List_Of_Currency.get(currency).To_buy + " PLN\nTo sell: "
                + List_Of_Currency.get(currency).To_sell + " PLN");
        valute_name.setText(List_Of_Currency.get(currency).Name);
    }

    public void Calculate(View view) {
        // pln_c curr_c
        System.out.println(curr_c.getText());
        // Converting to double and then back to string
        String first = curr_c.getText().toString();
        double tmp = Double.parseDouble(first);
        first = String.valueOf(tmp);
        String second = Double.toString(prev_c);
        if (!first.equals(second)) {

            double d = Double.parseDouble(first);
            double to_ret = calculator.fromValute(d, currency);
            prev_c = d;
            pln_c.setText(to_ret + "");
            System.out.println("PLN DIDN'T CHANGED");
        } else {

            double d = Double.parseDouble(pln_c.getText().toString());
            double to_ret = calculator.toValute(d, currency);
            prev_pln = d;
            curr_c.setText(to_ret + "");
            System.out.println("PLN CHANGED");
        }
    }
}

class PutThread implements Runnable {
    Exchanger<List<Currency>> exchanger;
    List<Currency> List_Of_Currency = new ArrayList<Currency>();

    PutThread(Exchanger<List<Currency>> exchanger) {
        this.exchanger = exchanger;

    }

    @Override
    public void run() {
        try {
            List<Currency> result = Trace_HTML.ExtractCurrency();

            exchanger.exchange(result);
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }

    }
}

class SpinnerActivity extends MainActivity implements AdapterView.OnItemSelectedListener {

    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
        MainActivity.currency = pos;
        MainActivity.Update();
    }

    public void onNothingSelected(AdapterView<?> parent) {
        current_buy_sell.setText("Buy: " + List_Of_Currency.get(currency).To_buy + " PLN\nTo sell: "
                + List_Of_Currency.get(currency).To_sell + " PLN");
    }
}
