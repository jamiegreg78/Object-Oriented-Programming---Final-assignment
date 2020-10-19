package uk.ac.aber.dcs.backend;

import java.io.*;
import java.util.InputMismatchException;
import java.util.Scanner;

/**
 * The vigenere cipher class - encrypts and decrypts using a key.
 *
 * @author Jamie Gregory [jag73]
 * @version 1.0
 */
public class VigenereCipher extends CipherEncryptor {
    private String key;

    /**
     * Creates a KeyedCaeserCipher object. Sets the key location, creates a scanner and empties the raw text.
     */
    public VigenereCipher() {
        setKeyLocation(".\\text\\vigenereKey.txt");
        super.read = new Scanner(System.in);
        setRawText("");
    }

    /**
     * Encrypts the text by using a key.
     *
     * @param text the string to encrypt
     * @return encrypted text.
     */
    @Override
    public String encrypt(String text) {
        StringBuilder output = new StringBuilder();
        int newPosition;

        for (int i = 0, j = 0; i < text.length(); i++) {

            newPosition = ((text.charAt(i) - 'A') + (key.charAt(j) - 'A')) % 26;
            output.append(getALPHABET().charAt(newPosition));
            j = (j + 1) % key.length();
        }

        return output.toString();
    }

    /**
     * Decrypts the text using a key and prints the result.
     */
    @Override
    public void decrypt() {
        StringBuilder output = new StringBuilder();
        int newPosition;

        for (int i = 0, j = 0; i < getCipherText().length(); i++) {
            newPosition = ((getCipherText().charAt(i) - 'A') - (key.charAt(j) - 'A')) % 26;
            output.append(getALPHABET().charAt(Math.floorMod(newPosition, 26)));
            j = (j + 1) % key.length();
        }
        System.out.println(output.toString());
    }

    /**
     * writes the current key to file.
     */
    public void writeKey(){
        try (FileWriter writer = new FileWriter(getKeyLocation());
             BufferedWriter bw = new BufferedWriter(writer);
             PrintWriter outputFile = new PrintWriter(bw)) {

            outputFile.println(key);
        } catch (IOException e) {
            System.err.println("File cannot be found");
        }
    }

    /**
     * loads the key from the key file.
     *
     */
    public void loadKey(){
        try (FileReader fr = new FileReader(getKeyLocation());
             BufferedReader br = new BufferedReader(fr);
             Scanner infile = new Scanner(br)) {

            String read = "";
            while (infile.hasNext()) {
                read = infile.nextLine();
            }

            key = read.toUpperCase();
        } catch (IOException e) {
            System.err.println("Error: " + e);
        }

    }

    /**
     * sets the key - must be longer than two characters.
     */
    @Override
    public void setCipherKey() {
        try {
            System.out.println("Please enter the new key (Must be at least 2 characters long): ");
            String inputKey = read.nextLine();
            if (inputKey.length() >= 2 && !checkForNumbers(inputKey)) {
                setKey(inputKey);
            } else {
                System.out.println("Key must be at least 2 characters long. Also cannot contain numbers");
            }
            writeKey();
        } catch (InputMismatchException i) {
            System.err.println("Incorrect input type. Required: String");
        }
    }

    /**
     * Checks for numbers and returns true if found
     *
     * @param input search string
     * @return true or false
     */
    private boolean checkForNumbers(String input) {
        char inputLetter;
        for (int i = 0; i < input.length(); i++) {
            inputLetter = input.charAt(i);
            if (Character.isDigit(inputLetter)) {
                return true;
            }
        }
        return false;
    }

    /**
     * returns the cipher name (Vigenere Cipher).
     *
     * @return Vigenere Cipher.
     */
    public String getCipherName() {
        return "Vigenere Cipher";
    }

    /**
     * Prints the current key.
     */
    @Override
    public void printKey() {
        System.out.println("Key: " + key);
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
     * sets the key.
     *
     * @param newKey desired key.
     */
    @Override
    public void setKey(String newKey) {
        key = newKey.toUpperCase();
    }


}
