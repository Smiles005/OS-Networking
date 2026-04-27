package csci1140;

import java.io.*;

/*
 * Name:Isabel Kliethermes
 *
 * Date: 3/25/2024
 * 
 * Assignment Number:3
 * 
 * Instructor: tenBroek
 * 
 *
 */

public class KeyboardReader {
    private static BufferedReader reader;
    static {
        reader = new BufferedReader(new InputStreamReader(System.in));
    }

    public static String readLine() {
        try {
            return reader.readLine();
        } catch (IOException ioe) {
            ioe.printStackTrace(System.err);
            return "";
        }
    }

    public static String readLine(String prompt) {
        try {
            System.out.println(prompt);
            return reader.readLine();
        } catch (IOException ioe) {
            ioe.printStackTrace(System.err);
            return "";
        }
    }

    public static int readInt() {
        while (true) {
            try {
                return Integer.parseInt(KeyboardReader.readLine());
            } catch (NumberFormatException nfe) {
                System.out.println("Sorry, that's not an integer. Please try again.");
            }
        }
    }

    public static int readInt(String prompt) {
        while (true) {
            try {
                return Integer.parseInt(KeyboardReader.readLine(prompt));
            } catch (NumberFormatException nfe) {
                System.out.println("Sorry, that's not an integer. Please try again.");
            }
        }
    }

    public static double readDouble(String prompt) {
        while (true) {
            try {
                return Double.parseDouble(KeyboardReader.readLine(prompt));
            } catch (NumberFormatException nfe) {
                System.out.println("Sorry, that's not an real number(double). Please try again.");
            }
        }
    }

    public static long readLong(String prompt) {
        while (true) {
            try {
                return Long.parseLong(KeyboardReader.readLine(prompt));
            } catch (NumberFormatException nfe) {
                System.out.println("Sorry, that's not an real number(long). Please try again.");
            }
        }
    }

    public static boolean readBoolean(java.lang.String prompt) {
        while (true) {
            try {
                return Boolean.parseBoolean(KeyboardReader.readLine(prompt));
            } catch (NumberFormatException nfe) {
                System.out.println("Sorry, that's not boolean (True or False). Please try again.");
            }
        }
    }

    public static byte readByte(java.lang.String prompt) {
        while (true) {
            try {
                return Byte.parseByte(KeyboardReader.readLine(prompt));
            } catch (NumberFormatException nfe) {
                System.out.println("Sorry, that's not a byte. Please try again.");
            }
        }
    }

    public static char readChar(String prompt) {
        return KeyboardReader.readLine(prompt).charAt(0);
    }

    public static float readFloat(String prompt) {
        while (true) {
            try {
                return Float.parseFloat(KeyboardReader.readLine(prompt));
            } catch (NumberFormatException nfe) {
                System.out.println("Sorry, that's not an real number(Float). Please try again.");
            }
        }
    }

    public static short readShort(String prompt) {
        while (true) {
            try {
                return Short.parseShort(KeyboardReader.readLine(prompt));
            } catch (NumberFormatException nfe) {
                System.out.println("Sorry, that's not an real number(Short). Please try again.");
            }
        }
    }

    public static void main(String[] args) {
        System.out.println(readLine("prompt:"));
        System.out.println(readBoolean("bool:"));
        // Reads the words "true" and "false" from the keyboard.
        System.out.println(readByte("byte:"));
        // Reads a byte value (between -128 and 127) from the keyboard.
        System.out.println(readChar("Char:"));
        System.out.println(readDouble("Double:"));
        System.out.println(readFloat("Float:"));
        System.out.println(readInt("int:"));
        System.out.println(readLong("long:"));
        System.out.println(readShort("short:"));
    }
}