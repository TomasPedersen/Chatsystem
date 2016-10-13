import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class Controller {
	@FXML TextArea output;
	@FXML TextField input;

	@FXML
	protected void handleNickButton(ActionEvent event) {
		output.appendText("\nNickButton pressed");
	}

	@FXML
	protected void handleQuitButton(ActionEvent event) {
		output.appendText("\nQuitButton pressed");
	}

	@FXML
	protected void handleJoinButton(ActionEvent event) {
		output.appendText("\nJoinButton pressed");
	}
	@FXML public void handleMsgButton(ActionEvent event){
		input.setText("/msg ");

	}
	@FXML protected void handleInput(ActionEvent event){
		if(!input.getText().equals("")) output.appendText("\n"+input.getText());	// Skriv kun noget til output hvis input ikke er en tom linie.
		input.setText("");
	}
}
