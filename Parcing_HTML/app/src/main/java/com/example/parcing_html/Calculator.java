package com.example.parcing_html;

import java.util.List;

public class Calculator {
    List<Currency> List_Of_Currency;

    public Calculator(List<Currency> List_Of_Currency) {
        this.List_Of_Currency = List_Of_Currency;
    }

    public double toValute(double number, int current) {
        double to_ret;
        to_ret = number / List_Of_Currency.get(current).To_buy;
        return to_ret;
    }

    public double fromValute(double number, int current) {
        double to_ret;
        to_ret = number * List_Of_Currency.get(current).To_sell;
        return to_ret;
    }

}
