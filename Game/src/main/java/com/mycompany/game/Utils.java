
package com.mycompany.game;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Utils {

    public static String loadFileAsString(String path) {
        StringBuilder builder = new StringBuilder();

        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(Utils.class.getResourceAsStream(path)))){

            System.out.println("BR: " + br);
            String line;
            while ((line = br.readLine()) != null) {
                builder.append(line + "\n");
            }

            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return builder.toString();
    }

    public static int parseInt(String number) {
        try {
            return Integer.parseInt(number);
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return 0;
        }
    }

}
