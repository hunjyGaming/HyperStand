package de.hunjy.utils;


import net.md_5.bungee.api.ChatColor;

import java.awt.*;

public class Colorizer {

    public static String gradientToWhite(String text, float hue, float saturation, float brightness) {
        StringBuilder stringBuilder = new StringBuilder();

        int textLength = text.length();
        float steps = saturation / textLength;

        for (int i = 0; i < textLength; i++) {
            Color color = Color.getHSBColor(hue, saturation, brightness);
            String letter = String.valueOf(text.charAt(i));
            stringBuilder.append(ChatColor.of(color) + letter);
            saturation -= steps;
        }

        return stringBuilder.toString();
    }

    public static String gradientNormalise(String text, float hue, float saturation, float brightness) {
        StringBuilder stringBuilder = new StringBuilder();

        int textLength = text.length();
        float steps = (saturation / textLength)  * 1.2f;

        for (int i = 0; i < textLength; i++) {
            Color color = Color.getHSBColor(hue, saturation, brightness);
            String letter = String.valueOf(text.charAt(i));
            stringBuilder.append(ChatColor.of(color) + letter);
            if (i < (textLength / 2)) {
                saturation -= steps;
            } else {
                saturation += steps;
            }
        }

        return stringBuilder.toString();
    }

    public static String hex(String text, String hex) {
        Color color = new Color(
                Integer.valueOf( hex.substring( 0, 2 ), 16 ),
                Integer.valueOf( hex.substring( 2, 4 ), 16 ),
                Integer.valueOf( hex.substring( 4, 6 ), 16 ) );
        return ChatColor.of(color) + text;
    }
}
