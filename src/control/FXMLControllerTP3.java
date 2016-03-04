package control;

import java.net.URL;
import java.util.Date;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.InputEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import model.Chambreur;
import model.Client;
import model.DB;
import model.DataClient;
import model.FormatCellule;


public class FXMLControllerTP3 implements Initializable{

    @FXML
    private TableView<FormatCellule> tableviewClients;

    @FXML
    private TableColumn<FormatCellule, Integer> columnIdClient;

    @FXML
    private TableColumn<FormatCellule, String> columnNomClient;

    @FXML
    private TableColumn<FormatCellule, String> columnPrenomClient;

    @FXML
    private Button buttonModifClient;

    @FXML
    private Button buttonAjouterClient;

    @FXML
    private TableView<FormatCellule> tableviewReservations;

    @FXML
    private TableColumn<FormatCellule, String> columnDebutReserv;

    @FXML
    private TableColumn<FormatCellule, String> columnFinReserv;

    @FXML
    private TableColumn<FormatCellule, Integer> columnNoChambreReserv;

    @FXML
    private Button buttonModifReserv;

    @FXML
    private Button buttonAjouterReservation;
    
    @FXML
    protected Button connectionButton;
    
    @FXML 
    private VBox root;
    
    boolean gererDisableBouttons;
    Client client;
    boolean connection;
    
    private Stage mainStage;
    
    private Stage subStage;
    
    //EventHandler<InputEvent> inputEventBlockHandler;
    
    @FXML
    void connection() {
    	
    	if(connection == false)
    	{   		
    		client = new Client();
    		client.openAndConnectClientSocket();
    		connection = true;
    		gererDisableBouttons();
    		connectionButton.setText("Se deconnection");
    		tableviewClients.setItems(DataClient.getInstance().getListeObsChambreur());
    		tableviewReservations.setItems(DataClient.getInstance().getListeObsReservation());
    		
    	}
    	else
    	{	
    		connectionButton.setText("Se connection");
    		connection = false;
    		gererDisableBouttons();
    		
    	}
    	
    }
    
    private void gererDisableBouttons()
    {
    	buttonAjouterClient.setDisable(!connection);
    	buttonAjouterReservation.setDisable(!connection);
    }
    
    @FXML
    void ajouterClient() throws Exception 
    {
    	FXMLLoader loader = new FXMLLoader(getClass().getResource(
				"../view/ajouterClient.fxml"));
    	VBox root = (VBox) loader.load();
    	FXMLController_AjouterClient subController = (FXMLController_AjouterClient)loader.getController();
    	subController.setMainController(this);
    	subController.setMainStage(mainStage);
    	subController.setClient(client);
    	
    	subStage = new Stage();
    	Scene scene = new Scene(root);
    	subStage.setScene(scene);
    	subStage.show();
    	subStage.setResizable(false); 
    	
    	setOnCloseEvent(subStage);
    	
    	mainStage.getScene().getRoot().setDisable(true);
    }
    
    @FXML
    void ajouterReservation() {

    }

    @FXML
    void consultModifClient() throws Exception
    {
    	FXMLLoader loader = new FXMLLoader(getClass().getResource(
				"../view/consultationClient.fxml"));
    	VBox root = (VBox) loader.load();
    	FXMLController_ConsultModifClient subController = (FXMLController_ConsultModifClient)loader.getController();
    	subController.setMainController(this);
    	subController.setMainStage(mainStage);
    	subController.setClient(client);
    	Chambreur chambreur = getSelectedChambreur();
    	subController.setChambreur(chambreur);
    	
    	subStage = new Stage();
    	Scene scene = new Scene(root);
    	subStage.setScene(scene);
    	subStage.show();
    	subStage.setResizable(false); 
    	
    	setOnCloseEvent(subStage);
    	setClientFields(subController, chambreur);
    	
    	//mainStage.addEventFilter(InputEvent.ANY, inputEventBlockHandler);
    	mainStage.getScene().getRoot().setDisable(true);
    }
    
   

	private void setClientFields(FXMLController_ConsultModifClient subController, Chambreur chambreur)
	{
		subController.getTextFieldNom().setText(chambreur.getNom());
		subController.getTextFieldPrenom().setText(chambreur.getPrenom());
		subController.getTextFieldId().setText(String.valueOf(chambreur.getIdClient()));
		subController.getTextFieldAdresse().setText(chambreur.getAdresse());
		subController.getTextFieldTelephone().setText(chambreur.getTelephone());
		subController.getTextFieldDateNaissance().setText(chambreur.getDateNaissance().toString());
		subController.getTextFieldOrientSex().setText(chambreur.getOrientationSexuelle());
	}

	private Chambreur getSelectedChambreur()
	{
		int idClientSelected = tableviewClients.getSelectionModel().getSelectedItem().getIdClient();
		System.out.println(idClientSelected);
		for (Chambreur chambreur : DataClient.getInstance().getListeChambreur())
		{	System.out.println("getidclient:" + chambreur.getNom());
			if(chambreur.getIdClient() == idClientSelected)
			{
				System.out.println("allo");
				return chambreur;
			}
		}
		
		return null;
	}

	private void setOnCloseEvent(Stage subStage2)
	{
    	subStage.setOnCloseRequest(new EventHandler<WindowEvent>()
		{
			@Override
			public void handle(WindowEvent event)
			{
				event.consume();
				closeSubWindow();		
			}
	
		});
		
	}

	protected void closeSubWindow()
    {  	
    	mainStage.getScene().getRoot().setDisable(false);
    	subStage.hide();
    	client.receiveListChambreur(client.getSocket());// TROUVER UN MOYEN DE METTRE A JOUR LA LISTE DES TABLEVIEW LORSQU'UNE subWindow est fermée***********************************************************
    	client.receiveListReservation(client.getSocket());
    }

    @FXML
    void consultModifReserv() {
    	
    }

    private void setFormatCell() 
    {		
    	columnIdClient.setCellValueFactory(new PropertyValueFactory<FormatCellule, Integer>("idClient"));
		columnNomClient.setCellValueFactory(new PropertyValueFactory<FormatCellule, String>("nomClient"));
		columnPrenomClient.setCellValueFactory(new PropertyValueFactory<FormatCellule, String>("prenomClient"));
		columnDebutReserv.setCellValueFactory(new PropertyValueFactory<FormatCellule, String>("dateDebut"));
		columnFinReserv.setCellValueFactory(new PropertyValueFactory<FormatCellule, String>("dateFin"));
		columnNoChambreReserv.setCellValueFactory(new PropertyValueFactory<FormatCellule, Integer>("noChambre"));
	}
    
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) 
	{	
		connection = false;
		setFormatCell();
		tableviewClients.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) ->
		{
			boolean selected = tableviewClients.getSelectionModel().getSelectedIndex() != -1;
			buttonModifClient.setDisable(!selected);
		}
		);
		
		tableviewReservations.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) ->
		{
			boolean selected = tableviewReservations.getSelectionModel().getSelectedIndex() != -1;
			buttonModifReserv.setDisable(!selected);
		}
		);
		
//		inputEventBlockHandler = new EventHandler<InputEvent>()
//				{
//					public void handle(InputEvent event)
//					{
//						event.consume();
//					}
//				};
	}
	
	public void setStage(Stage mainStage)
	{
		this.mainStage = mainStage;
	}
	
}
