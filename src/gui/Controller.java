package gui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

/**
 * Created by tomas on 10/27/16.
 */
public class Controller {
	@FXML TextField inputTextField;
	@FXML TextArea outputTextArea;

	public void handleInputTextField(ActionEvent event){
		outputTextArea.appendText(inputTextField.getText() +"\n");
		inputTextField.setText("");
	}
}
