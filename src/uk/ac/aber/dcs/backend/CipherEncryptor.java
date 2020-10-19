package uk.ac.aber.dcs.backend;

import java.io.*;
import java.util.Scanner;

/**
 * Contains the common code between cipher classes and stores some information required by the program
 *
 * @author Jamie Gregory [jag73]
 * @version 1.0
 */
public abstract class CipherEncryptor {
    private String cipherText;
    private String rawText;
    private String preparedText;
    private String keyLocation;
    private String rawTextFile;
    private String cipherTextFile;
    Scanner read;

    /**
     * Loads the raw text from a given file and stores it in the rawText variable
     *
     * @throws IOException thrown when the program fails to read the supplied file.
     */
    public void loadRawText() throws IOException {
        try (FileReader fr = new FileReader(rawTextFile);
             BufferedReader br = new BufferedReader(fr);
             Scanner infile = new Scanner(br)) {

            StringBuilder output = new StringBuilder();
            //Add the next read word to the stringbuilder
            while (infile.hasNext()) {
                output.append(infile.next());

                //If there is another word, add a space between them
                if (infile.hasNext()) {
                    output.append(" ");
                }
            }
            rawText = output.toString();
        }

    }

    /**
     * Loads the cipher text from a given file and stores it in the cipherText variable
     *
     * @throws IOException thrown when the program fails to read the supplied file.
     */
    public void loadCipherText() throws IOException {
        try (FileReader fr = new FileReader(cipherTextFile);
             BufferedReader br = new BufferedReader(fr);
             Scanner infile = new Scanner(br)) {
            StringBuilder output = new StringBuilder();

            //Read the string on the next line
            while (infile.hasNext()) {
                output.append(infile.next());

            }

            cipherText = output.toString();
        }

    }

    /**
     * Encrypts the chosen text. Overridden by each cipher.
     *
     * @param text the string to encrypt
     * @return the encrypted text
     */
    public abstract String encrypt(String text);

    /**
     * Decrypts the ciphertext variable. Overridden by each cipher.
     */
    public abstract void decrypt();

    /**
     * Loads the key from file. Overridden by each cipher.
     *
     * @throws IOException thrown when the program fails to load the file
     */
    public abstract void loadKey() throws IOException;

    /**
     * prints the key of the cipher. Overridden by each cipher
     */
    public abstract void printKey();

    /**
     * returns the raw text of a file
     *
     * @return rawText variable
     */
    public String getRawText() {
        return rawText;
    }

    /**
     * Writes the key to file. Overridden by each cipher.
     *
     * @throws IOException thrown when the program fails to write the file
     */
    public abstract void writeKey() throws IOException;

    /**
     * returns the key of the cipher.
     *
     * @return key variable
     */
    public String getKey() {
        return "";
    }

    /**
     * Returns the name of the current cipher in use.
     *
     * @return cipher name
     */
    public abstract String getCipherName();

    /**
     * sets the key for the current cipher. Overridden by each cipher.
     */
    public abstract void setCipherKey();

    /**
     * sets the location for the raw text file.
     *
     * @param file file path
     */
    public void setRawTextFile(String file) {
        rawTextFile = file;
        prepareText();
    }

    /**
     * sets the location for the cipher text file.
     *
     * @param file file path
     */
    public void setCipherTextFile(String file) {
        cipherTextFile = file;
    }

    /**
     * returns the raw text file location.
     *
     * @return file path
     */
    public String getRawTextFile() {
        return rawTextFile;
    }

    /**
     * prepares the text for encryption, removes punctuation.
     */
    public void prepareText() {
        String prepared = rawText.toUpperCase();
        prepared = prepared.replaceAll("([.,!?'’ 123456789])", "");

        preparedText = prepared;
    }

    /**
     * returns the prepared text.
     *
     * @return prepared text
     */
    public String getPreparedText() {
        return preparedText;
    }

    /**
     * returns the cipher text.
     *
     * @return cipher text
     */
    public String getCipherText() {
        return cipherText;
    }

    /**
     * Saves the cipher text string to a text file.
     *
     * @param outPutLocation the desired location for the output text file
     */
    public void saveCipherText(String outPutLocation) {
        try (FileWriter writer = new FileWriter(outPutLocation);
             BufferedWriter bw = new BufferedWriter(writer);
             PrintWriter outputFile = new PrintWriter(bw)) {
            // Writes the cipherText String to file
            outputFile.println(getCipherText());
        } catch (IOException e) {
            System.err.println("File cannot be found");
        }
    }

    /**
     * returns the shift. Overridden by caesar cipher and keyed caesar cipher.
     *
     * @return shift
     */
    public int getShift() {
        return -1;
    }

    /**
     * sets the shift. Overridden by caesar cipher and keyed caesar cipher
     *
     * @param newShift desired shift
     */
    public void setShift(int newShift) {
    }

    public void setRawText(String text) {
        rawText = text;
    }

    /**
     * Sets the file path for the key file
     *
     * @param path file path
     */
    public void setKeyLocation(String path) {
        keyLocation = path;
    }

    /**
     * returns the file path of the key file.
     *
     * @return key path file
     */
    public String getKeyLocation() {
        return keyLocation;
    }

    /**
     * sets the key. Overridden by keyed caesar cipher and vigenere cipher
     *
     * @param newKey desired key
     */
    public void setKey(String newKey) {
    }

    /**
     * calculates the new position of the shifted character.
     *
     * @param currentPosition the current position of the character
     * @param shift           the current shift
     * @return the new character, shifted by the desired amount
     */
    public int newPosition(int currentPosition, int shift) {
        return Math.floorMod((currentPosition + shift), 26);
    }

    /**
     * Returns the alphabet variable
     *
     * @return alphabet
     */
    public String getALPHABET() {
        return "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    }

    public void setCipherText(String text) {
        cipherText = text;
    }

}
