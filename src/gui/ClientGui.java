package gui;/**
 * Created by tomas on 10/27/16.
 */

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class ClientGui extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("layout.fxml"));
        primaryStage.setTitle("Chat system");
        primaryStage.setScene(new Scene(root, 800, 500));
        primaryStage.show();
    }

	/*public static void main(String[] args) {
		launch(args);
	}
*/
}
