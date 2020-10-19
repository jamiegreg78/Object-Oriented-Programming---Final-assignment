package uk.ac.aber.dcs.backend;

import java.io.*;
import java.util.InputMismatchException;
import java.util.Scanner;

/**
 * The caesar cipher class - encrypts and decrypts using a shift.
 *
 * @author Jamie Gregory [jag73]
 * @version 1.0
 */
public class CaesarCipher extends CipherEncryptor {
    private int shift;

    /**
     * Creates a caeserCipher object. Sets the location of the key, creates a scanner object and empties the raw text.
     */
    public CaesarCipher() {
        setKeyLocation(".\\text\\caesarKey.txt");
        super.read = new Scanner(System.in);
        setRawText("");
    }

    /**
     * Encrypts the character by shifting it by a desired amount.
     *
     * @param text the string to encrypt
     * @return the encrypted text
     */
    @Override
    public String encrypt(String text) {
        StringBuilder output = new StringBuilder();
        int currentPosition;

        for (int i = 0; i < text.length(); i++) {
            // Gets the alphabetical position of the current char
            currentPosition = getALPHABET().indexOf(text.charAt(i));
            // shifts the current char forward/back by the desired shift and appends to the output
            output.append(getALPHABET().charAt(newPosition(currentPosition, shift)));
        }
        return output.toString();
    }

    /**
     * Decrypts the text using the same algorithm but an inverted shift.
     */
    @Override
    public void decrypt() {
        invertShift();
        System.out.println(encrypt(getCipherText()));
        invertShift();
    }

    /**
     * inverts the shift by multiplying by -1.
     */
    private void invertShift() {
        shift *= -1;
    }

    /**
     * Writes the current shift to file.
     */
    public void writeKey(){
        try (FileWriter writer = new FileWriter(getKeyLocation());
             BufferedWriter bw = new BufferedWriter(writer);
             PrintWriter outputFile = new PrintWriter(bw)) {

            //Write the shift to file
            outputFile.println(shift);
        } catch (IOException e) {
            System.err.println("File cannot be found");
        }
    }

    /**
     * Loads the shift from file and saves it to a variable.
     */
    public void loadKey(){
        try (FileReader fr = new FileReader(getKeyLocation());
             BufferedReader br = new BufferedReader(fr);
             Scanner infile = new Scanner(br)) {

            while (infile.hasNext()) {
                shift = infile.nextInt();
            }

        } catch (IOException e) {
            System.err.println("Error: " + e);
        }

    }

    /**
     * Prints the shift.
     */
    @Override
    public void printKey() {
        System.out.println("Shift: " + shift);
    }

    /**
     * Returns the name of the cipher (Caesar Cipher).
     *
     * @return Caesar Cipher
     */
    @Override
    public String getCipherName() {
        return "Caesar Cipher";
    }

    /**
     * Sets the shift of the cipher.
     */
    @Override
    public void setCipherKey() {
        try {
            System.out.println("Please enter the new shift: ");
            setShift(read.nextInt() % 26);
            writeKey();
        } catch (InputMismatchException i) {
            System.err.println("Incorrect input type. Required: int");
        }
    }

    /**
     * returns the current shift.
     *
     * @return current shift
     */
    @Override
    public int getShift() {
        return shift;
    }

    /**
     * Sets the new shift of the cipher
     *
     * @param newShift the desired shift
     */
    @Override
    public void setShift(int newShift) {
        shift = newShift;
    }


}
