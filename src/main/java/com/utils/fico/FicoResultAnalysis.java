package com.utils.fico;

import com.utils.HttpsXml;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * @author answer
 *         2017/12/12
 */
public class FicoResultAnalysis {
    public static void main(String[] args) {
        String result = analysis("000");
        System.out.println(result);
    }

    public static String analysis(String code) {
        String result = "";
        try {
            InputStream input = Class.forName(HttpsXml.class.getName()).getResourceAsStream("/fico.txt");
            BufferedReader read = new BufferedReader(new InputStreamReader(input));
            String info = "";
            while ((info = read.readLine()) != null) {//一行一行读
                String ficoCode = info.split(":")[0];
                String string = info.split(":")[1];
                if (ficoCode.equals(code)) {
                    result = string;
                    break;
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return result;
    }
}
