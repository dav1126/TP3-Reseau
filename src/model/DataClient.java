package model;

import java.util.ArrayList;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class DataClient {

	private static DataClient instance;
	ObservableList<FormatCellule> listeObsChambreur; 
	ObservableList<FormatCellule> listeObsReservation;
	ArrayList<Chambreur> listeChambreur;
	ArrayList<Reservation> listeReservation;
	
	public DataClient(){
		
		listeObsChambreur = FXCollections.observableArrayList();
		listeObsReservation = FXCollections.observableArrayList();
		listeChambreur = new ArrayList<>();
		listeReservation  = new ArrayList<>();
	}

	public ObservableList<FormatCellule> getListeObsChambreur() {
		return listeObsChambreur;
	}

	public void setListeObsChambreur(ObservableList<FormatCellule> listeChambreur) {
		this.listeObsChambreur = listeChambreur;
	}

	public ObservableList<FormatCellule> getListeObsReservation() {
		return listeObsReservation;
	}

	public void setListeObsReservation(ObservableList<FormatCellule> listeReservation) {
		this.listeObsReservation = listeReservation;
	}
	
	public static DataClient getInstance(){
		
		if(instance == null){
			
			instance = new DataClient();
		}
		
		return instance;
	}

	public ArrayList<Chambreur> getListeChambreur()
	{
		return listeChambreur;
	}

	public ArrayList<Reservation> getListeReservation()
	{
		return listeReservation;
	}
}
