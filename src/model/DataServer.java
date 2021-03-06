package model;


import java.util.ArrayList;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class DataServer {

	private static DataServer instance;
	ObservableList<Chambreur> listeChambreur; 
	ObservableList<Reservation> listeReservation;
	ArrayList<Chambreur> listeConsultChambreur;
	ArrayList<Reservation> listeConsultReservation;
	
	private DataServer(){
		
		listeChambreur = FXCollections.observableArrayList();
		listeReservation = FXCollections.observableArrayList();
		listeConsultChambreur = new ArrayList<>();
		listeConsultReservation = new ArrayList<>();
	}

	public ObservableList<Chambreur> getListeChambreur() {
		return listeChambreur;
	}

	public void setListeChambreur(ObservableList<Chambreur> listeChambreur) {
		this.listeChambreur = listeChambreur;
	}

	public ObservableList<Reservation> getListeReservation() {
		return listeReservation;
	}

	public void setListeReservation(ObservableList<Reservation> listeReservation) {
		this.listeReservation = listeReservation;
	}
	
	public ArrayList<Chambreur> getListeConsultChambreur()
	{
		return listeConsultChambreur;
	}

	public ArrayList<Reservation> getListeConsultReservation()
	{
		return listeConsultReservation;
	}

	public static DataServer getInstance(){
		
		if(instance == null){
			
			instance = new DataServer();
		}
		
		return instance;
	}
}