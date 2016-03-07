package control;

import java.util.logging.FileHandler;
import java.util.logging.Formatter;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import model.Server;

public class AppClient extends Application{
	
	private static final Logger LOGGER = Logger.getLogger(Server.class.getName());
	Handler fileHandler = null;
	Formatter simpleFormatter = null;
	String homeDirectoryPath = System.getProperty("user.home");

	public static void main(String[] args) 
	{		
		Application.launch(args);
	}

	@Override
	public void start(Stage pStage) throws Exception 
	{
		//Logging
		// Creating FileHandler
		fileHandler = new FileHandler(homeDirectoryPath + "/tp3_CLIENT.log" );

		// Creating SimpleFormatter
		simpleFormatter = new SimpleFormatter();

		// Assigning handler to logger
		LOGGER.addHandler(fileHandler);

		// Logging message of Level info (this should be publish in the default format i.e. XMLFormat)
		LOGGER.info("Finnest message: Logger with DEFAULT FORMATTER");

		// Setting formatter to the handler
		fileHandler.setFormatter(simpleFormatter);

		// Setting Level to ALL
		fileHandler.setLevel(Level.ALL);
		LOGGER.setLevel(Level.ALL);

		// Logging message of Level finest (this should be publish in the simple format)
		LOGGER.finest("Finnest message: Logger with SIMPLE FORMATTER");
				
		try
		{
			FXMLLoader loader = new FXMLLoader(getClass().getResource(
					"/view/mainInterface.fxml"));
			VBox root = (VBox) loader.load();
			FXMLControllerTP3 mainController = (FXMLControllerTP3)loader.getController();
			mainController.setStage(pStage);
			mainController.setLogger(LOGGER);
			
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
			//int x = 1/0; 
		}
		catch (Exception e)
		{
			e.printStackTrace();
			LOGGER.log(Level.SEVERE, "ERREUR: " + e.getMessage());
		}
	}

}
