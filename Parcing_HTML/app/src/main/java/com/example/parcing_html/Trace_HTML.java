package com.example.parcing_html;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

public class Trace_HTML {

    public static void main(String[] args) throws IOException {
        GetData();
    }

    public static List<Currency> GetData() throws IOException {
        List<Currency> result = new ArrayList<Currency>();

        URL url = new URL("https://kantorconti.pl/ru/");

        URLConnection con = url.openConnection();
        InputStream is = con.getInputStream();

        BufferedReader br = new BufferedReader(new InputStreamReader(is));

        String line = null;
        int c = 0;
        while ((line = br.readLine()) != null) {
            if (line.equals("<tbody>")) {
                while ((line = br.readLine()) != null && c < 2) {
                    if (line.equals("<tr>")) {
                        c++;
                    }
                }
            }
            if (c >= 2) {
                break;
            }
        }

        while ((!line.equals("</table>") && (line = br.readLine()) != null)) {
            result.add(ExtractCorruncy(br, line));
            line = br.readLine();
            line = br.readLine();
            line = br.readLine();
            line = br.readLine();
            line = br.readLine();
            line = br.readLine();
            line = br.readLine();
        }
        return result;
    }

    public static Currency ExtractCorruncy(BufferedReader br, String line) throws IOException {
        String currency_name = Extract(line);
        line = br.readLine();
        String currency_buy = Extract(line);
        line = br.readLine();
        String currency_sell = Extract(line);
        return new Currency(currency_name, Double.parseDouble(currency_buy), Double.parseDouble(currency_sell));
    }

    public static String Extract(String line) {
        return line.substring(4, line.length() - 5);
    }
}

class Currency {
    public String Name;
    public double To_buy;
    public double To_sell;

    public Currency(String name, double to_buy, double to_sell) {
        Name = name;
        To_buy = to_buy;
        To_sell = to_sell;
    }

    public String GetInfo() {
        return (Name + "\n" + To_buy+ "\n" + To_sell);
    }
}
