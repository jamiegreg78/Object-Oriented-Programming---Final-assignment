package uk.ac.aber.dcs.frontend;

import uk.ac.aber.dcs.backend.CipherEncryptor;
import uk.ac.aber.dcs.backend.CaesarCipher;
import uk.ac.aber.dcs.backend.KeyedCaesarCipher;
import uk.ac.aber.dcs.backend.VigenereCipher;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.IOException;

/**
 * The GUI controller class. Handles the use of the GUI.
 * @author Jamie Gregory [jag73]
 * @version 1.0
 */
public class Controller {
    private CipherEncryptor cipherEncryptor;
    private File inputLocation;
    private File encryptedInputLocation;

    //MenuBar
    @FXML
    private MenuBar bar;
    @FXML
    private MenuItem load;
    @FXML
    private MenuItem loadEncrypted;

    //FileChooser
    @FXML
    private FileChooser file;
    private File input;

    // Cipher box
    private ObservableList<String> cipherStatus = FXCollections.observableArrayList("Caesar Cipher", "Keyed Caesar Cipher", "Vigenere Cipher");
    @FXML
    private ChoiceBox cipherStatusBox;

    //key and shift
    @FXML
    private Label keyLabel;
    @FXML
    private TextField keyInput;
    @FXML
    private Slider shiftInput;
    @FXML
    private Label shiftLabel;

    //Labels for display
    @FXML
    private TextArea inputField;
    @FXML
    private TextArea preparedField;
    @FXML
    private TextArea outputField;

    /**
     * runs at startup - initializes the choice box, adds a listener to the shift slider and sets the
     * default text file location.
     * @throws IOException
     */
    public void initialize() throws IOException {
        //Initialize the input file - Can be changed by user
        inputLocation = new File(".\\src\\Text\\defaultInput.txt");
        //Initialize the cipher status box
        cipherStatusBox.setItems(cipherStatus);
        cipherStatusBox.setValue("Caesar Cipher");
        cipherEncryptor.loadKey();
        shiftInput.valueProperty().addListener((observable, oldValue, newValue) -> {
            try {
                keyOrShift();
            } catch (IOException e) {
                System.err.println("Error: " + e);
            }
        });
        shiftInput.setValue(cipherEncryptor.getShift());
    }

    /**
     * Gets the current value of the cipherStatus box.
     * @return cipher
     */
    private String cipherStatus(){
        return cipherStatusBox.getValue().toString();
    }

    /**
     * Creates a new cipher object when selected. Updates the ui and any information required by the cipher.
     * @throws IOException thrown when the program fails to load a file.
     */
    //Called When cipher is selected
    public void setCipher() throws IOException {
        switch (cipherStatus()) {
            case "Caesar Cipher":
                cipherEncryptor = new CaesarCipher();
                setShiftVisible(true);
                setKeyVisible(false);
                cipherEncryptor.loadKey();
                shiftInput.setValue(cipherEncryptor.getShift());
                loadInputText(inputLocation);
                break;
            case "Keyed Caesar Cipher":
                cipherEncryptor = new KeyedCaesarCipher();
                setShiftVisible(true);
                setKeyVisible(true);
                cipherEncryptor.loadKey();
                keyInput.setText(cipherEncryptor.getKey());
                shiftInput.setValue(cipherEncryptor.getShift());
                loadInputText(inputLocation);
                break;
            case "Vigenere Cipher":
                cipherEncryptor = new VigenereCipher();
                setShiftVisible(false);
                setKeyVisible(true);
                cipherEncryptor.loadKey();
                keyInput.setText(cipherEncryptor.getKey());
                loadInputText(inputLocation);
                break;
        }
    }

    /**
     * sets the visibilty of the shift elements when not needed by a cipher.
     * @param visibility true or false
     */
    // Hiding the shift settings when not necessary
    public void setShiftVisible(boolean visibility) {
        shiftLabel.setVisible(visibility);
        shiftInput.setVisible(visibility);
    }

    /**
     * sets the visibilty of the key elements when not needed by a cipher.
     * @param visibility true or false
     */
    // Hiding the key settings when not necessary
    public void setKeyVisible(boolean visibility) {
        keyInput.setVisible(visibility);
        keyLabel.setVisible(visibility);
    }

    /**
     * allows the user to choose a file for input.
     * @throws IOException thrown when the file cannot be loaded
     */
    // Run when menu item is selected
    public void chooseInputFile() throws IOException {
        file = new FileChooser();
        file.setTitle("Open text file");
        input = file.showOpenDialog(bar.getScene().getWindow());
        inputLocation = input;
        // If user presses cancel - return
        if (input != null) {
            loadInputText(input);
        } else {
            return;
        }

    }

    /**
     * allows the user to choose an already encrypted file for input.
     * @throws IOException
     */
    // Run when menu item is selected
    public void chooseEncryptedFile()throws IOException{
        file = new FileChooser();
        file.setTitle("Open text file");
        input = file.showOpenDialog(bar.getScene().getWindow());
        encryptedInputLocation = input;
        // If user presses cancel - return null and handle it
        if (input != null) {
            loadEncryptedInput(input);
        } else {
            return;
        }
    }

    /**
     * Allows the user to save the encrypted text to a file.
     */
    // Run when menu item is selected
    public void saveEncryptedFile() {
        FileChooser save = new FileChooser();
        save.setTitle("Save text file");
        File outputFile = save.showSaveDialog(bar.getScene().getWindow());
        if (outputFile != null) {
            cipherEncryptor.saveCipherText(outputFile.getPath());
        } else {
            return;
        }
    }

    /**
     * Performs the encryption, but only if the text has been prepared first.
     */
    public void performEncryption(){
        if (cipherEncryptor.getPreparedText() != null){
            cipherEncryptor.setCipherText(cipherEncryptor.encrypt(cipherEncryptor.getPreparedText()));
            outputField.setText(cipherEncryptor.getCipherText());
        } else {
            return;
        }
    }

    /**
     * Performs the decryption, but only if the ciphertext exists.
     */
    public void performDecryption(){
        if (cipherEncryptor.getCipherText() != null){
            cipherEncryptor.decrypt();
            preparedField.setText(cipherEncryptor.getPreparedText());
        } else {
            return;
        }
    }

    /**
     * Clears all text fields.
     */
    public void clearTextFields(){
        inputField.setText("");
        preparedField.setText("");
        outputField.setText("");
    }

    /**
     * performs the loading of the input file.
     * @param inputFile the input file path
     * @throws IOException thrown when the program fails to load the file
     */
    // Loads the input text from file
    public void loadInputText(File inputFile) throws IOException {
        clearTextFields();
        cipherEncryptor.setRawTextFile(inputFile.getPath());
        cipherEncryptor.loadRawText();
        cipherEncryptor.prepareText();
        inputField.setText(cipherEncryptor.getRawText());
        preparedField.setText(cipherEncryptor.getPreparedText());
    }

    /**
     * Performs the loading of the input encrypted file
     * @param encrypted the encrypted file path
     * @throws IOException thrown when the program fails to load the file
     */
    public void loadEncryptedInput(File encrypted) throws IOException {
        clearTextFields();
        cipherEncryptor.setCipherTextFile(encrypted.getPath());
        cipherEncryptor.loadCipherText();
        outputField.setText(cipherEncryptor.getCipherText());
    }

    /**
     * sets the key and shift, depending on which cipher is currently in use.
     * @throws IOException
     */
    public void keyOrShift() throws IOException {
        switch (cipherStatus()){
            case "Caesar Cipher":
                cipherEncryptor.setShift((int)(shiftInput.getValue()));
                cipherEncryptor.writeKey();
                break;
            case "Keyed Caesar Cipher":
                cipherEncryptor.setShift((int)(shiftInput.getValue()));
                cipherEncryptor.setKey(keyInput.getText());
                cipherEncryptor.writeKey();
                break;
            case "Vigenere Cipher":
                cipherEncryptor.setKey(keyInput.getText());
                cipherEncryptor.writeKey();
                break;
        }
    }
}
