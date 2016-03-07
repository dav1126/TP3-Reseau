package control;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.Client;
import model.Reservation;

public class FXMLController_ConsultModifReservation{

    @FXML
    private TextField textFieldIdReserv;

    @FXML
    private TextField textFieldIdClient;

    @FXML
    private TextField textFieldNoChambre;

    @FXML
    private DatePicker dateDebutPicker;

    @FXML
    private DatePicker dateFinPicker;

    @FXML
    private Button buttonModifier;

    @FXML
    private Button buttonSupprimerClient;

    @FXML
    private Button buttonAnnuler;

    @FXML
    private Button buttonEnregistrer;

    @FXML
    private Button quitterButton;

	private Stage mainStage;

	private Stage subStage;

	private Client client;

	private FXMLControllerTP3 mainController;

	private Reservation reservation;

    @FXML
    void annuler() {

    	resetReservationFields();
    	buttonModifier.setDisable(false);
    }

	@FXML
    void enregistrer() {

		Reservation reserv = new Reservation(Integer.parseInt(textFieldIdReserv.getText()),
				Integer.parseInt(textFieldIdClient.getText()), Integer.parseInt(textFieldNoChambre.getText()),
				dateDebutPicker.getValue(), dateFinPicker.getValue());
		client.sendRequestToServer(5, null, reserv);
		mainController.updateLists();
		client.sendRequestToServer(92, null, reservation);
		mainController.closeSubWindow();
    }

    @FXML
    void modifier() {

    	buttonModifier.setDisable(true);
    }

    @FXML
    void quitter() {
    	client.sendRequestToServer(92, null, reservation);
    	mainController.closeSubWindow();
    }

    @FXML
    void supprimerClient() {

    	client.sendRequestToServer(6, null, reservation);
    	mainController.updateLists();
    	client.sendRequestToServer(92, null, reservation);
    	mainController.closeSubWindow();
    }
    
    public void initialize(){
    	
    	buttonAnnuler.setDisable(true);
		buttonEnregistrer.setDisable(true);
		buttonModifier.disableProperty().addListener((observable, oldValue, newValue) ->
		{
			boolean disable = true;
			if (buttonModifier.isDisabled())
			{
				disable = false;
			}
			buttonAnnuler.setDisable(disable);
			buttonEnregistrer.setDisable(disable);
			getTextFieldIdClient().setDisable(disable);
			getTextFieldNoChambre().setDisable(disable);
			getDateDebutPicker().setDisable(disable);
			getDateFinPicker().setDisable(disable);			
		});
		buttonModifier.setDisable(false);
    }
    
    private void resetReservationFields()
   	{
   	
    	getTextFieldIdReserv().setText(String.valueOf(reservation.getIdReservation()));
    	getTextFieldIdClient().setText(String.valueOf(reservation.getIdChambreur()));
    	getTextFieldNoChambre().setText(String.valueOf(reservation.getNoChambre()));
    	getDateDebutPicker().setValue(reservation.getDateDebut());
    	getDateFinPicker().setValue(reservation.getDateFin());
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

	public void setReservation(Reservation reserv)
	{
		this.reservation = reserv;
	}

	public TextField getTextFieldIdReserv()
	{
		return textFieldIdReserv;
	}

	public TextField getTextFieldIdClient()
	{
		return textFieldIdClient;
	}

	public TextField getTextFieldNoChambre()
	{
		return textFieldNoChambre;
	}

	public DatePicker getDateDebutPicker()
	{
		return dateDebutPicker;
	}

	public DatePicker getDateFinPicker()
	{
		return dateFinPicker;
	}
	
	
}
