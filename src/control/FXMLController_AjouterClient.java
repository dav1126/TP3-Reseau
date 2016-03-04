package control;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.Chambreur;
import model.Client;
import model.DB;
import model.DataClient;
import model.FormatCellule;

public class FXMLController_AjouterClient {

    @FXML
    private TextField textFieldNom;

    @FXML
    private TextField textFieldPrenom;

    @FXML
    private TextField textFieldId;

    @FXML
    private TextField textFieldTelephone;

    @FXML
    private TextField textFieldAdresse;

    @FXML
    private TextField textFieldDateNaissance;

    @FXML
    private TextField textFieldOrientSex;

    @FXML
    private Button buttonAnnuler;
    
    @FXML
    private DatePicker datePicker;

    @FXML
    private Button buttonAjouter;
    
    private FXMLControllerTP3 mainController;
    
    private Stage mainStage;
    
    private Stage subStage;
	
	private Client client;	

    @FXML
    void ajouter() 
    {
    	Chambreur chambreur = new Chambreur(textFieldNom.getText(), textFieldPrenom.getText(), textFieldTelephone.getText(), textFieldAdresse.getText(), datePicker.getValue(), textFieldOrientSex.getText());
    	DB.getInstance().insertChambreur(chambreur);
    	mainController.closeSubWindow();
    }

    @FXML
    void annuler() 
    {
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

