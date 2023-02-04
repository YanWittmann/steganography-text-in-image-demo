package de.yanwittmann.steganography;

import java.awt.image.BufferedImage;
import java.io.File;

public class MessageCoder {

    /**
     * Transforms teh string into bytes and then makes the rgb values of the image even if byte is 1 and odd if byte is 0
     * by adding 1 to the color value if the color value is odd and the byte is 1
     */
    public static BufferedImage encodeText(String text, BufferedImage image) {
        text += '\0';
        byte[] bytes = text.getBytes();
        ImageEditor img = new ImageEditor(image);
        int currentColor = 0;
        int x = 0, y = 0;
        for (int i = 0; i < bytes.length * 8; i++) {
            boolean currentBit = (bytes[i / 8] & (1 << (7 - (i % 8)))) != 0;
            if (currentBit) {
                if (currentColor == 0) {
                    img.setRed(x, y, makeValueOdd(img.getRed(x, y)));
                } else if (currentColor == 1) {
                    img.setGreen(x, y, makeValueOdd(img.getGreen(x, y)));
                } else if (currentColor == 2) {
                    img.setBlue(x, y, makeValueOdd(img.getBlue(x, y)));
                }
            } else {
                if (currentColor == 0) {
                    img.setRed(x, y, makeValueEven(img.getRed(x, y)));
                } else if (currentColor == 1) {
                    img.setGreen(x, y, makeValueEven(img.getGreen(x, y)));
                } else if (currentColor == 2) {
                    img.setBlue(x, y, makeValueEven(img.getBlue(x, y)));
                }
            }
            currentColor++;
            if (currentColor == 3) {
                currentColor = 0;
                x++;
                if (x >= image.getWidth()) {
                    x = 0;
                    y++;
                }
                if (y >= image.getHeight()) {
                    break;
                }
            }
        }
        return img.getImage();
    }

    private static int makeValueOdd(int value) {
        if (value == 255) return value;
        if (value == 0) return value + 1;
        if (value % 2 == 0) return value + 1;
        return value;
    }

    private static int makeValueEven(int value) {
        if (value == 0) return value;
        if (value == 255) return value - 1;
        if (value % 2 == 1) return value - 1;
        return value;
    }

    public static String decodeText(BufferedImage image) {
        ImageEditor img = new ImageEditor(image);
        StringBuilder sb = new StringBuilder();
        int currentColor = 0;
        int x = 0, y = 0;
        for (int i = 0; i < image.getWidth() * image.getHeight(); i++) {
            if (currentColor == 0) {
                int red = img.getRed(x, y);
                if (red % 2 == 1) sb.append('1');
                else sb.append('0');
            } else if (currentColor == 1) {
                int green = img.getGreen(x, y);
                if (green % 2 == 1) sb.append('1');
                else sb.append('0');
            } else if (currentColor == 2) {
                int blue = img.getBlue(x, y);
                if (blue % 2 == 1) sb.append('1');
                else sb.append('0');
            }
            currentColor++;
            if (currentColor == 3) {
                currentColor = 0;
                x++;
                if (x >= image.getWidth()) {
                    x = 0;
                    y++;
                }
                if (y >= image.getHeight()) {
                    break;
                }
            }
        }
        byte[] bytes = new byte[sb.length() / 8];
        int actualByte = 0;
        for (int i = 0; i < bytes.length; i++) {
            String s = sb.substring(i * 8, (i + 1) * 8);
            byte byt = (byte) Integer.parseInt(s, 2);
            if (byt == 0) {
                actualByte = i;
                break;
            }
            bytes[i] = byt;
        }
        return new String(bytes, 0, actualByte == 0 ? bytes.length : actualByte);
    }

    public static void main(String[] args) {
        String text = "Dieses Blatt wurde von Yan Wittmann für Frau Fimmel für das MA3 Modul zurechtgeschnitten (2023-02-03)";
        File input = new File("C:/Users/yan20/Downloads/Standardnormalverteilung offiziell.png");
        File output = new File("C:/Users/yan20/Downloads/Standardnormalverteilung.png");

        final BufferedImage image = new ImageEditor(input).getImage();

        if (false) {
            final BufferedImage encoded = encodeText(text, image);
            new ImageEditor(encoded).save(output);
        } else {
            System.out.println(decodeText(image));
        }
    }
}
