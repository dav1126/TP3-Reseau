package control;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.stage.Window;
import model.Chambreur;
import model.DataClient;
import model.FormatCellule;
import model.Client;

public class FXMLController_ConsultModifClient 
{

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
    private DatePicker datePicker;

    @FXML
    private TextField textFieldOrientSex;

    @FXML
    private Button buttonModifier;

    @FXML
    private Button buttonAnnuler;

    @FXML
    private Button buttonEnregistrer;
    
    @FXML
    private Button quitterButton;
    
    @FXML
    private Button buttonSupprimerClient;
    
    private FXMLControllerTP3 mainController;
    
    private Stage mainStage;
    
    private Stage subStage;

	private Chambreur chambreur;
	
	private Client client;
    
    @FXML
    void quitter() 
    {
    	client.sendRequestToServer(91, chambreur, null);
    	mainController.closeSubWindow();
    }
    
    @FXML
    void annuler() 
    {
    	resetClientFields();
    	buttonModifier.setDisable(false);
    }

    @FXML
    void enregistrer() 
    {
    	Chambreur chambreur = new Chambreur(Integer.parseInt(textFieldId.getText()), textFieldNom.getText(), textFieldPrenom.getText(), 
    			textFieldTelephone.getText(), textFieldAdresse.getText(), 
    			datePicker.getValue(), textFieldOrientSex.getText());
    	client.sendRequestToServer(2, chambreur, null);
    	mainController.updateLists();
    	client.sendRequestToServer(91, chambreur, null);
    	mainController.closeSubWindow(); 
    }

    @FXML
    void modifier() 
    {
    	buttonModifier.setDisable(true);
    }
    
    @FXML
    void supprimerClient() {
//    	FormatCellule fcTemp = null;
//    	for(FormatCellule fc:DataClient.getInstance().getListeObsChambreur())
//    	{
//    		if(fc.getIdClient() ==
//    				chambreur.getIdClient())
//    		{			
//    			fcTemp = fc;
//    		}
//    	} 
    	client.sendRequestToServer(3, chambreur, null);
    	mainController.updateLists();
    	client.sendRequestToServer(91, chambreur, null);
    	mainController.closeSubWindow();  
    }
    
	public void setMainController(FXMLControllerTP3 mainController)
	{
		this.mainController = mainController;
	}
	
	public void initialize()
	{
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
			getTextFieldNom().setDisable(disable);
			getTextFieldPrenom().setDisable(disable);
			getTextFieldAdresse().setDisable(disable);
			getTextFieldTelephone().setDisable(disable);
			getDatePickerDateNaissance().setDisable(disable);
			getTextFieldOrientSex().setDisable(disable);
			
		});
		buttonModifier.setDisable(false);
		
	}
	
	private void resetClientFields()
	{
		getTextFieldNom().setText(chambreur.getNom());
		getTextFieldPrenom().setText(chambreur.getPrenom());
		getTextFieldId().setText(String.valueOf(chambreur.getIdClient()));
		getTextFieldAdresse().setText(chambreur.getAdresse());
		getTextFieldTelephone().setText(chambreur.getTelephone());
		getDatePickerDateNaissance().setValue(chambreur.getDateNaissance());
		getTextFieldOrientSex().setText(chambreur.getOrientationSexuelle());
	}
	
	public void setMainStage(Stage mainStage)
	{
		this.mainStage = mainStage;
	}
	
	public void setSubStage(Stage subStage)
	{
		this.subStage = subStage;
	}
	
	public void setChambreur(Chambreur chambreur)
	{
		this.chambreur = chambreur;
	}
	
	public void setClient(Client client)
	{
		this.client = client;
	}
	
	 public TextField getTextFieldNom()
	{
		return textFieldNom;
	}

	public TextField getTextFieldPrenom()
	{
		return textFieldPrenom;
	}

	public TextField getTextFieldId()
	{
		return textFieldId;
	}

	public TextField getTextFieldTelephone()
	{
		return textFieldTelephone;
	}

	public TextField getTextFieldAdresse()
	{
		return textFieldAdresse;
	}

	public DatePicker getDatePickerDateNaissance()
	{
		return datePicker;
	}

	public TextField getTextFieldOrientSex()
	{
		return textFieldOrientSex;
	}
}
