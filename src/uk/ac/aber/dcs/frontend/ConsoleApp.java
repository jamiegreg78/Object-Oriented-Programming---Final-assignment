package uk.ac.aber.dcs.frontend;

import uk.ac.aber.dcs.backend.CaesarCipher;
import uk.ac.aber.dcs.backend.CipherEncryptor;
import uk.ac.aber.dcs.backend.KeyedCaesarCipher;
import uk.ac.aber.dcs.backend.VigenereCipher;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;

/**
 * The console application
 *
 * @author Jamie Gregory [jag73]
 * @version 1.0
 */

public class ConsoleApp {

    private CipherEncryptor cipherEncryptor;
    private Scanner read;
    private String inputFile;

    // starts the program - runs setup and menu
    public static void main(String[] args) {
        ConsoleApp app = new ConsoleApp();
        app.setUp();
        app.runMenu();
    }

    //Creates the scanner and cipher object. Loads any key or shift the cipher may need
    private void setUp() {
        read = new Scanner(System.in);
        cipherEncryptor = new CaesarCipher();
        changeInput();
        try {
            updateCipher();
        } catch (IOException e) {
            System.out.println("Error: " + e);
        }
    }


    private void printMenu() {
        System.out.println("1: Choose a cipher (Current: " + cipherEncryptor.getCipherName() + ")");
        System.out.println("2: Edit cipher key");
        System.out.println("3: Display key");
        System.out.println("4: Change input file");
        System.out.println("5: Display input text");
        System.out.println("6: Encrypt input file");
        System.out.println("7: Display cipher text");
        System.out.println("8: Save encrypted text to file");
        System.out.println("9: Input cipher text");
        System.out.println("10: Decrypt cipher text");
        System.out.println("E: Exit");

    }

    /**
     * runs the menu system from main. Allows for the choosing of actions.
     */
    private void runMenu() {
        String input;

        do { //Asks the user for input
            printMenu();
            System.out.println("Choose an action: ");
            input = read.nextLine().toUpperCase();

            switch (input) {
                case "1": // Choose a cipher
                    chooseCipher();
                    break;
                case "2": // Edit Cipher key
                    setKey();
                    break;
                case "3": // Display key
                    displayKey();
                    break;
                case "4": // Change input file
                    changeInput();
                    break;
                case "5": // Display input text
                    printPreparedText();
                    break;
                case "6": // Encrypt input text
                    encryptText();
                    break;
                case "7": // Display cipher text
                    printCipherText();
                    break;
                case "8": // Save encrypted text to file
                    saveCipherText();
                    break;
                case "9": // Input cipher text
                    inputCipherText();
                    break;
                case "10": // Decrypt cipher text
                    decryptText();
                    break;
                case "E": // Exit
                    break;
            }

        } while (!input.equals("E"));
    }

    /**
     * Prompts the user to input a selection of cipher, which is then created and prepared for use for encryption/decryption
     */
    private void chooseCipher() {
        String choice;
        System.out.println("Which cipher would you like to use? (1. Caesar, 2. Keyed Caesar or 3. Vigenere)");
        choice = read.nextLine().toUpperCase();

        switch (choice) {
            case "CAESAR", "1" -> {
                cipherEncryptor = new CaesarCipher();
                cipherEncryptor.setRawTextFile(inputFile);
            }
            case "KEYED CAESAR", "2" -> {
                cipherEncryptor = new KeyedCaesarCipher();
                cipherEncryptor.setRawTextFile(inputFile);
            }
            case "VIGENERE", "3" -> {
                cipherEncryptor = new VigenereCipher();
                cipherEncryptor.setRawTextFile(inputFile);
            }
            default -> System.out.println("Not a valid choice");
        }
        try {
            updateCipher();
        } catch (IOException e) {
            System.out.println("Error updating ciphers: " + e);
        }

    }

    /**
     * Allows the user to choose a text file to input.
     */
    private void changeInput() {
        //Takes the user input
        System.out.println("Please enter a filename: ");
        inputFile = read.nextLine();
        cipherEncryptor.setRawTextFile(inputFile);
        //Attempts to load the input files and prepare the text.
        try {
            cipherEncryptor.loadRawText();
            cipherEncryptor.prepareText();
        } catch (FileNotFoundException e) {
            System.err.println("File not found");
        } catch (IOException e) {
            System.err.println("Error occurred when attempting to open file: " + cipherEncryptor.getRawTextFile());
            System.out.println(e.getMessage());
        }

    }

    /**
     * Allows the user to choose an encrypted text file to input.
     */
    private void inputCipherText() {
        //Takes the user input
        System.out.println("Please enter a filename: ");
        cipherEncryptor.setCipherTextFile(read.nextLine());
        //Attempts to load the input text
        try {
            cipherEncryptor.loadCipherText();
        } catch (FileNotFoundException e) {
            System.err.println("File not found");
        } catch (IOException e) {
            System.err.println("Error occurred when attempting to open file: " + cipherEncryptor.getRawTextFile());
            System.out.println(e.getMessage());
        }

    }

    private void saveCipherText() {
        System.out.println("Please enter a filename: ");
        cipherEncryptor.saveCipherText(read.nextLine());
    }

    private void printCipherText() {
        System.out.println("Encrypted Text: " + cipherEncryptor.getCipherText());
    }

    private void setKey() {
        cipherEncryptor.setCipherKey();
        displayKey();
    }

    private void displayKey() {
        try {
            cipherEncryptor.loadKey();
            cipherEncryptor.printKey();
        } catch (IOException e) {
            System.err.println(e);
        }
    }

    private void printPreparedText() {
        System.out.println(cipherEncryptor.getPreparedText());
    }

    /**
     * Updates the ciphers information to ensure it is up to date - This includes raw text, prepared text and keys.
     *
     * @throws IOException An error can arise when updating a cipher - Loading the text is a common culprit
     */
    private void updateCipher() throws IOException {
        try {
            cipherEncryptor.loadRawText();
            cipherEncryptor.prepareText();
            cipherEncryptor.loadKey();
        } catch (IOException e) {
            System.out.println("Error: " + e);
        }
    }

    /**
     * Runs the encryption algorithm of a chosen cipher. Encrypts prepared text and saves to cipher text.
     */
    private void encryptText() {
        if (cipherEncryptor.getPreparedText() == null) {
            System.out.println("Nothing to encrypt");
        } else {
            cipherEncryptor.setCipherText(cipherEncryptor.encrypt(cipherEncryptor.getPreparedText()));
        }
    }

    /**
     * Runs the decryption algorithm for a chosen cipher. Prints the result to console.
     */
    private void decryptText() {
        cipherEncryptor.decrypt();
    }
}
