package control;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class AppClient extends Application{

	public static void main(String[] args) 
	{		
		Application.launch(args);
	}

	@Override
	public void start(Stage pStage) throws Exception 
	{		
		FXMLLoader loader = new FXMLLoader(getClass().getResource(
				"../view/mainInterface.fxml"));
		VBox root = (VBox) loader.load();
		FXMLControllerTP3 mainController = (FXMLControllerTP3)loader.getController();
		mainController.setStage(pStage);
		
		pStage.setOnCloseRequest(new EventHandler<WindowEvent>()
			{
				@Override
				public void handle(WindowEvent event)
				{
					event.consume();
					System.exit(0);				
				}
		
			});
		
		Scene scene = new Scene(root);
		pStage.setScene(scene);
		pStage.show();
		pStage.setResizable(false);
	}

}
