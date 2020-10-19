package uk.ac.aber.dcs.backend;

import java.io.*;
import java.util.InputMismatchException;
import java.util.LinkedHashSet;
import java.util.Scanner;

/**
 * The keyed caesar cipher class - encrypts and decrypts using a shift and key.
 *
 * @author Jamie Gregory [jag73]
 * @version 1.0
 */
public class KeyedCaesarCipher extends CipherEncryptor {
    private String key;
    private int shift;

    /**
     * Creates a KeyedCaeserCipher object. Sets the key location, creates a scanner and empties the raw text.
     */
    public KeyedCaesarCipher() {
        setKeyLocation(".\\text\\keyedCaesarKey.txt");
        super.read = new Scanner(System.in);
        setRawText("");

    }

    /**
     * Encrypts the raw text by shifting it forward and using a new alphabet generated with a key.
     *
     * @param text the string to encrypt
     * @return encrypted text
     */
    @Override
    public String encrypt(String text) {
        StringBuilder output = new StringBuilder();
        int currentLocation;

        for (int i = 0; i < text.length(); i++) {
            currentLocation = getCurrentLocation(getALPHABET(), text.charAt(i));
            output.append(key.charAt(newPosition(currentLocation, shift)));
        }

        return output.toString();
    }

    /**
     * Decrypts the cipher text and prints the result.
     */
    @Override
    public void decrypt() {
        invertShift();
        StringBuilder output = new StringBuilder();
        int currentLocation;

        for (int i = 0; i < getCipherText().length(); i++) {
            currentLocation = getCurrentLocation(key, getCipherText().charAt(i));
            output.append(getALPHABET().charAt(newPosition(currentLocation, shift)));
        }

        System.out.println(output.toString());
        invertShift();
    }

    /**
     * Gets the current location of the character within an 'alphabet'.
     *
     * @param searchString the 'alphabet' to search through
     * @param currentChar  the character you are searching for
     * @return the position of currentChar within the searchString
     */
    private int getCurrentLocation(String searchString, char currentChar) {
        return searchString.indexOf(currentChar);
    }

    /**
     * inverts the shift my multiplying by -1.
     */
    private void invertShift() {
        shift *= -1;
    }

    /**
     * Returns the name of the cipher (Keyed Caesar Cipher).
     *
     * @return Keyed Caesar Cipher
     */
    public String getCipherName() {
        return "Keyed Caesar Cipher";
    }

    /**
     * writes the current key and shift to file.
     *
     * @throws IOException throws when the program fails to save the file
     */
    public void writeKey() throws IOException {
        try (FileWriter writer = new FileWriter(getKeyLocation());
             BufferedWriter bw = new BufferedWriter(writer);
             PrintWriter outputFile = new PrintWriter(bw)) {

            //Write the shift and key to file
            outputFile.println(shift);
            outputFile.println(key);
        }
    }

    /**
     * Loads the key and shift from the key file.
     */
    public void loadKey(){
        try (FileReader fr = new FileReader(getKeyLocation());
             BufferedReader br = new BufferedReader(fr);
             Scanner infile = new Scanner(br)) {

            int inputShift = 0;
            String input = "";

            // Read the text in the file
            while (infile.hasNext()) {
                inputShift = Integer.parseInt(infile.nextLine());
                input = infile.nextLine().toUpperCase();
            }

            key = generateKey(input, getALPHABET());
            shift = Math.floorMod(inputShift, 26);
        } catch (IOException e) {
            System.err.println("Error: " + e);
        }

    }

    /**
     * Generates a key alphabet with the chosen key. Removes any duplicates from the string using linked hashsets.
     *
     * @param input    the chosen key
     * @param alphabet an alphabet string
     * @return generated key
     */
    private String generateKey(String input, String alphabet) {
        StringBuilder output = new StringBuilder();
        // Convert the String to an array of chars
        char[] chars = input.toCharArray();

        //add the input to a LinkedHashSet - doesn't allow duplicates
        LinkedHashSet<Character> letters = new LinkedHashSet<>();
        for (char c : chars) {
            letters.add(c);
        }

        //Create the final key
        char[] alphabetChars = alphabet.toCharArray();
        for (char c : alphabetChars) {
            letters.add(c);
        }

        // Build a string from the hashset
        for (Character c : letters) {
            output.append(c);
        }

        return output.toString();
    }

    /**
     * Prints the current key and shift.
     */
    @Override
    public void printKey() {
        System.out.println("Key: " + key);
        System.out.println("Shift: " + shift);
    }

    /**
     * sets the cipher key and shift.
     */
    @Override
    public void setCipherKey() {
        try {
            String scanned;
            System.out.println("Enter the new key: ");
            scanned = read.nextLine();
            if (scanned.length() <= 26) {
                setKey(scanned);
            } else{
                System.err.println("Key is too long");
                return;
            }
            System.out.println("Please enter the new shift: ");
            setShift(Math.floorMod(read.nextInt(), 26));
            writeKey();

        } catch (InputMismatchException i) {
            System.err.println("Incorrect input type.");
        } catch (IOException e) {
            System.err.println("Error:" + e);
        }
    }

    /**
     * returns the current shift.
     *
     * @return shift variable
     */
    @Override
    public int getShift() {
        return shift;
    }

    /**
     * returns the current key.
     *
     * @return key variable
     */
    @Override
    public String getKey() {
        return key;
    }

    /**
     * sets the new shift.
     *
     * @param newShift desired shift.
     */
    @Override
    public void setShift(int newShift) {
        shift = newShift;
    }

    /**
     * sets the new key by generating a key using a keyword and alphabet.
     *
     * @param newKey desired keyword.
     */
    @Override
    public void setKey(String newKey) {
        key = generateKey(newKey.toUpperCase(), super.getALPHABET());
    }
}
