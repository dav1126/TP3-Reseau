package model;

import java.text.SimpleDateFormat;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class FormatCellule {

	/**
	 * Le id du client
	 */
	private IntegerProperty idClient;
	
	/**
	 * Le nom du client
	 */
	private SimpleStringProperty nomClient;
	
	/**
	 * Le prenom du client
	 */
	private SimpleStringProperty prenomClient;
	
	/**
	 * La date du début de la réservation
	 */
	private SimpleStringProperty dateDebut;
	
	/**
	 * La date de fin de la réservation
	 */
	private SimpleStringProperty dateFin;
	
	/**
	 * Le numéro de la chambre
	 */
	private IntegerProperty noChambre;

	public FormatCellule(IntegerProperty idClient, SimpleStringProperty nomClient, 
			SimpleStringProperty prenomClient) {
		
		super();
		this.idClient = idClient;
		this.nomClient = nomClient;
		this.prenomClient = prenomClient;
	}
	
	public FormatCellule(SimpleStringProperty nomClient, 
			SimpleStringProperty prenomClient) {
		
		super();
		this.idClient = idClient;
		this.nomClient = nomClient;
		this.prenomClient = prenomClient;
	}
	
	public FormatCellule(SimpleStringProperty dateDebut, SimpleStringProperty dateFin,
			IntegerProperty noChambre){
		
		super();
		this.dateDebut = dateDebut;
		this.dateFin = dateFin;
		this.noChambre = noChambre;
	}

	public int getIdClient() {
		
		return idClient.get();
	}

	public String getNomClient() {
		
		return nomClient.get();
	}

	public String getPrenomClient() {
		
		return prenomClient.get();
	}

	public String getDateDebut() {
		
		return dateDebut.get();
	}

	public String getDateFin() {
		
		return dateFin.get();
	}

	public int getNoChambre() {
		
		return noChambre.get();
	}
	
	
}
