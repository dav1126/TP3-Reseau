package model;

import java.time.LocalDate;

public class Chambreur {
	
	private int idClient;
	private String nom;
	private String prenom;
	private String telephone;
	private String adresse;
	private LocalDate dateNaissance;
	private String orientationSexuelle;
	
	public Chambreur(){
		
	}
	
	public Chambreur(int idClient, String nom, String prenom, String telephone, String adresse, LocalDate dateNaissance,
			String orientationSexuelle) {
		super();
		this.idClient = idClient;
		this.nom = nom;
		this.prenom = prenom;
		this.telephone = telephone;
		this.adresse = adresse;
		this.dateNaissance = dateNaissance;
		this.orientationSexuelle = orientationSexuelle;
	}

	public Chambreur(String nom, String prenom, String telephone, String adresse, LocalDate dateNaissance,
			String orientationSexuelle) {
		super();
		this.nom = nom;
		this.prenom = prenom;
		this.telephone = telephone;
		this.adresse = adresse;
		this.dateNaissance = dateNaissance;
		this.orientationSexuelle = orientationSexuelle;
	}

	public int getIdClient() {
		return idClient;
	}

	public void setIdClient(int idClient) {
		this.idClient = idClient;
	}

	public String getNom() {
		return nom;
	}

	public void setNom(String nom) {
		this.nom = nom;
	}

	public String getPrenom() {
		return prenom;
	}

	public void setPrenom(String prenom) {
		this.prenom = prenom;
	}

	public String getTelephone() {
		return telephone;
	}

	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}

	public String getAdresse() {
		return adresse;
	}

	public void setAdresse(String adresse) {
		this.adresse = adresse;
	}

	public LocalDate getDateNaissance() {
		return dateNaissance;
	}

	public void setDateNaissance(LocalDate dateNaissance) {
		this.dateNaissance = dateNaissance;
	}

	public String getOrientationSexuelle() {
		return orientationSexuelle;
	}

	public void setOrientationSexuelle(String orientationSexuelle) {
		this.orientationSexuelle = orientationSexuelle;
	}
	
	
}
