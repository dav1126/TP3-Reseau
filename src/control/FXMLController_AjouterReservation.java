package control;

import javafx.stage.Stage;
import model.Chambreur;
import model.Client;
import model.Reservation;

import java.time.LocalDate;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;

public class FXMLController_AjouterReservation
{

	private Stage mainStage;
	private Stage subStage;
	private Client client;
	private FXMLControllerTP3 mainController;
	
	@FXML
    private TextField textFieldIdReserv;

    @FXML
    private TextField textFieldIdClient;

    @FXML
    private DatePicker dateDebutPicker;

    @FXML
    private DatePicker dateFinPicker;

    @FXML
    private TextField textFieldNoChambre;

    @FXML
    private Button buttonAnnuler;
    
    @FXML
    private Button buttonAjouter;

    @FXML
    void ajouter(ActionEvent event) {

    	Reservation reserv = new Reservation(Integer.parseInt(textFieldIdClient.getText()),
    			Integer.parseInt(textFieldNoChambre.getText()),
    			dateDebutPicker.getValue(), dateFinPicker.getValue());
    	client.sendRequestToServer(4, null, reserv);
    	mainController.updateLists();
    	mainController.closeSubWindow();
    }

    @FXML
    void annuler(ActionEvent event) {

    	mainController.closeSubWindow();
    }

	public void setMainStage(Stage mainStage)
		{
			this.mainStage = mainStage;
		}
		
		public void setSubStage(Stage subStage)
		{
			this.subStage = subStage;
		}
		
		public void setClient(Client client)
		{
			this.client = client;
		}
		
		public void setMainController(FXMLControllerTP3 mainController)
		{
			this.mainController = mainController;
		}
}
