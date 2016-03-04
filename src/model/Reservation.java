package model;

import java.time.LocalDate;

public class Reservation {

	private int idReservation;
	private int chambreur;
	private int noChambre;
	private LocalDate dateDebut;
	private LocalDate dateFin;
	
	public Reservation(){
		
	}
	public Reservation(int idReservation, int chambreur, int noChambre, LocalDate dateDebut, LocalDate dateFin) {
		
		super();
		this.idReservation = idReservation;
		this.chambreur = chambreur;
		this.noChambre = noChambre;
		this.dateDebut = dateDebut;
		this.dateFin = dateFin;
	}

	public Reservation(int chambreur, int noChambre, LocalDate dateDebut, LocalDate dateFin) {
		
		super();
		this.chambreur = chambreur;
		this.noChambre = noChambre;
		this.dateDebut = dateDebut;
		this.dateFin = dateFin;
	}



	public int getIdReservation() {
		
		return idReservation;
	}

	public void setIdReservation(int idReservation) {
		
		this.idReservation = idReservation;
	}

	public int getIdChambreur() {
		
		return chambreur;
	}

	public void setIdChambreur(int chambreur) {
		
		this.chambreur = chambreur;
	}

	public int getNoChambre() {
		
		return noChambre;
	}

	public void setNoChambre(int noChambre) {
		
		this.noChambre = noChambre;
	}

	public LocalDate getDateDebut() {
		
		return dateDebut;
	}
	
	

	public void setDateDebut(LocalDate dateDebut) {
		
		this.dateDebut = dateDebut;
	}

	public LocalDate getDateFin() {
		
		return dateFin;
	}

	public void setDateFin(LocalDate dateFin) {
		
		this.dateFin = dateFin;
	}
	
	
}
