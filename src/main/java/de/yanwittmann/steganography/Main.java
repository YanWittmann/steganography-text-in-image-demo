package de.yanwittmann.steganography;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Arrays;
import java.util.StringJoiner;

public class Main {

    public static void main(String[] args) {
        if (args.length <= 1) {
            printHelp();
            return;
        }

        final String command = args[0];
        if (command.equals("encode")) {
            encode(args);
        } else if (command.equals("decode")) {
            System.out.println(decode(args, "Usage: java -jar steganography.jar decode -f <file>"));
        } else if (command.equals("has")) {
            final boolean result = has(args);
            System.out.println(result);
        } else {
            System.out.println("Unknown command: " + command);
            printHelp();
        }
    }

    public static void encode(String[] args) {
        final String file = findOptionValue(args, "-f");
        final String output = findOptionValue(args, "-o");
        final String text = findOptionValue(args, "-t");
        if (file == null || output == null || text == null) {
            System.out.println("Usage: java -jar steganography.jar encode -f <file> -o <output> -t <text>");
            return;
        }

        final BufferedImage input = new ImageEditor(new File(file)).getImage();
        final BufferedImage result = MessageCoder.encodeText(":ENC:" + text, input);
        new ImageEditor(result).save(new File(output));
        System.out.println("Saved to " + output);
    }

    public static String decode(String[] args, String fileArgumentDoesNotExistMessage) {
        final String file = findOptionValue(args, "-f");
        if (file == null) {
            System.out.println(fileArgumentDoesNotExistMessage);
            return null;
        }

        final boolean force = Arrays.asList(args).contains("--force");

        final BufferedImage input = new ImageEditor(new File(file)).getImage();
        final String decoded = MessageCoder.decodeText(input);
        if (isValidDecodedContent(decoded) || force) {
            return decoded.replaceFirst("^:ENC:", "");
        } else {
            System.out.println("No encoded message found in " + file);
            return null;
        }
    }

    public static boolean has(String[] args) {
        final String decoded = decode(args, "Usage: java -jar steganography.jar has -f <file>");
        return isValidDecodedContent(decoded);
    }

    private static boolean isValidDecodedContent(String decoded) {
        if (decoded == null || decoded.isEmpty()) {
            return false;
        }

        if (decoded.startsWith(":ENC:")) {
            return true;
        }

        int requiredAsciiCharacters = 6;
        int asciiCharacters = 0;
        for (int i = 0; i < decoded.length(); i++) {
            if (isValidHasDecodedCharacter(decoded.charAt(i))) {
                asciiCharacters++;
            } else {
                asciiCharacters = 0;
            }
        }
        return asciiCharacters >= requiredAsciiCharacters;
    }

    private static boolean isValidHasDecodedCharacter(char c) {
        return c < 128 && !Character.isISOControl(c) && c != 'I' && c != '$';
    }

    private static void printHelp() {
        System.out.println("Usage: java -jar steganography.jar <command> <options>");
        System.out.println("Commands:");
        System.out.println("  encode -f <file> -o <output> -t <text>");
        System.out.println("  decode -f <file> [--force]");
        System.out.println("  has -f <file>");
    }

    private static String findOptionValue(String[] args, String option) {
        final StringJoiner joiner = new StringJoiner(" ");
        for (int i = 0; i < args.length; i++) {
            if (args[i].equals(option)) {
                for (int j = i + 1; j < args.length; j++) {
                    if (args[j].equals("-f") || args[j].equals("-o") || args[j].equals("-t") || args[j].equals("--force")) {
                        return joiner.toString();
                    }
                    joiner.add(args[j]);
                }
                return joiner.toString();
            }
        }
        return null;
    }
}
