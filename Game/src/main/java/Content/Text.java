/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Content;

import java.awt.Graphics;

public class Text {

    private String s;
    private int x;
    private int y;
    private int multiplier;
    private int type;
    private int start;
    private int letterCount;
    private boolean show;

    public Text(String s, int x, int y, int multiplier, int type) {
        this.s = s;
        this.x = x;
        this.y = y;
        this.multiplier = multiplier;
        this.type = type;
    }

    private int count;

    public void render(Graphics g) {
        try {
            if (type == -1) {
                start = x;
                for (int i = 0; i < s.length(); i++) {
                    char c = s.charAt(i);
                    System.out.println("Rendering type -1 char '" + c + "' at index " + i + " in string '" + s + "'");
                    if (c == ' ') {
                        count -= multiplier;
                        start += 5 * multiplier + count;
                        g.drawImage(Assets.miniAlphabet[26], start, y, 3 * multiplier, 6 * multiplier, null);
                    } else if (Character.toLowerCase(c) == 'm' || Character.toLowerCase(c) == 'w') {
                        count += multiplier;
                        start += 5 * multiplier;
                        int index = Character.toLowerCase(c) - 'a';
                        if (index >= 0 && index < Assets.miniAlphabet.length) {
                            g.drawImage(Assets.miniAlphabet[index], start, y, 5 * multiplier, 6 * multiplier, null);
                        }
                    } else if (c == '!') {
                        start += 5 * multiplier + count;
                        g.drawImage(Assets.miniAlphabet[27], start, y, 3 * multiplier, 6 * multiplier, null);
                    } else if (Character.isLetter(c)) {
                        start += 5 * multiplier + count;
                        int index = Character.toLowerCase(c) - 'a';
                        if (index >= 0 && index < Assets.miniAlphabet.length) {
                            g.drawImage(Assets.miniAlphabet[index], start, y, 4 * multiplier, 6 * multiplier, null);
                        }
                        count = 0;
                    } else {
                        System.out.println("Skipping unsupported character in type -1: '" + c + "'");
                    }
                }
            } else if (type == 0 || type == 5) {
                String delims = "[ ]+";
                String[] tokens = s.split(delims);
                int shift = 0;
                int xShift = 0;
                letterCount = 0;
                for (int i = 0; i < tokens.length; i++) {
                    if (tokens[i].length() + letterCount > 28) {
                        f = true;
                        letterCount = 0;
                        shift += 60;
                    }
                    xShift = 400 - ((maxLetters - 1) * multiplier * 3);
                    alphabetRender(g, tokens[i], i, xShift, y + shift, type == 0 ? 0 : 1);
                }
                show = true;
                f = true;
            } else if (type == 1) {
                for (int i = 0; i < s.length(); i++) {
                    char c = s.charAt(i);
                    if (c == '/') {
                        g.drawImage(Assets.numbers[10], x + i * 5 * multiplier, y, 4 * multiplier, 6 * multiplier, null);
                    } else if (Character.isDigit(c)) {
                        g.drawImage(Assets.numbers[c - '0'], x + i * 5 * multiplier, y, 4 * multiplier, 6 * multiplier, null);
                    }
                }
            } else if (type == 2) {
                for (int i = 0; i < s.length(); i++) {
                    g.drawImage(Assets.damageNumbers[s.charAt(i) - '0'], x + i * 7 * multiplier, y, 7 * multiplier, 8 * multiplier, null);
                }
            } else if (type == 3) {
                for (int i = 0; i < s.length(); i++) {
                    if (s.charAt(i) == '-') {
                        g.drawImage(Assets.redNumbers[0], x + i * 5 * multiplier, y, 4 * multiplier, 6 * multiplier, null);
                    } else {
                        g.drawImage(Assets.redNumbers[(s.charAt(i) - '0') + 1], x + i * 5 * multiplier, y, 4 * multiplier, 6 * multiplier, null);
                    }
                }
            } else if (type == 4) {
                for (int i = 0; i < s.length(); i++) {
                    g.drawImage(Assets.moneyNumbers[(s.charAt(i) - '0')], x + i * 5 * multiplier, y, 4 * multiplier, 6 * multiplier, null);
                }
            }
        } catch (Exception e) {
            System.err.println("Error rendering text: '" + s + "' with type " + type);
            e.printStackTrace();
        }
    }

    public void setHealth(int health, int baseHealth) {
        s = health + "/" + baseHealth;
    }

    public void setY(int y) {
        this.y = y;
    }

    private int maxLetters;
    private boolean f;

    private void alphabetRender(Graphics g, String s, int j, int x, int y, int type) {
        if (!f) {
            maxLetters++;
        }
        for (int i = 0; i < s.length(); i++) {
            if (!f) {
                maxLetters++;
            }
            if (show) {
                char c = s.charAt(i);
                System.out.println("Rendering char '" + c + "' at index " + i + " in string '" + s + "'");
                if (c == ' ') {
                    g.drawImage(Assets.alphabet[26], x + letterCount * 6 * multiplier, y, 5 * multiplier, 8 * multiplier, null);
                } else if (c == '!') {
                    g.drawImage(Assets.alphabet[27], x + letterCount * 6 * multiplier, y, 5 * multiplier, 8 * multiplier, null);
                } else if (c == '/') {
                    g.drawImage(Assets.numbers[10], x + letterCount * 6 * multiplier, y, 4 * multiplier, 6 * multiplier, null);
                } else if (Character.isDigit(c)) {
                    if (type == 0) {
                        g.drawImage(Assets.moneyNumbers[c - '0'], x + letterCount * 6 * multiplier, y, 5 * multiplier, 8 * multiplier, null);
                    } else if (type == 1) {
                        g.drawImage(Assets.xpNumbers[c - '0'], x + letterCount * 6 * multiplier, y, 5 * multiplier, 8 * multiplier, null);
                    }
                } else if (Character.isLetter(c)) {
                    int index = Character.toLowerCase(c) - 'a';
                    if (index >= 0 && index < Assets.alphabet.length) {
                        g.drawImage(Assets.alphabet[index], x + letterCount * 6 * multiplier, y, 5 * multiplier, 8 * multiplier, null);
                    } else {
                        System.out.println("Invalid alphabet index: " + index + " for char '" + c + "'");
                    }
                } else {
                    System.out.println("Skipping unsupported character: '" + c + "'");
                }
            }
            letterCount++;
        }
        g.drawImage(Assets.alphabet[26], x + letterCount * 6 * multiplier, y, 5 * multiplier, 8 * multiplier, null);
        letterCount++;
    }
}
