package model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;

public class DB {

	private static DB instance;
	private String url = "jdbc:mysql://localhost/bd_tp3_reseau";
	String user = "root";
	String password = "";
	Connection connection = null;
	Statement statement;
	
	/**
	 * Constructs the DB class, set up a connection to the db
	 */
	private DB(){
		
		try {
			
			connection = DriverManager.getConnection(url, user, password);
			statement = connection.createStatement();
			
		} catch (SQLException e) {
			
			e.printStackTrace();
			System.out.println("\nConnection to the db failed");
		}
		
	}
	
	/**
	 * Close up dthe db connection
	 */
	public void closeConnection(){
		
		if(connection != null){
			
			try {
				
				connection.close();
			} catch (SQLException e) {
				
				e.printStackTrace();
				System.out.println("\nClosing the connection failed");
			}
		}
	}
	/*
	 * Reads all Chambreur from chambreur table
	 */
	public void readAllChambreur(){
		try {
			
			ResultSet resultat = statement.executeQuery("SELECT idChambreur, nom, prenom, "
					+ "telephone, adresse, dateNaissance, orientationSexuelle FROM chambreur "
					+ "ORDER BY nom;");
			
			while(resultat.next()){
				
				int idChambreur = resultat.getInt("idChambreur");
				String nom = resultat.getString("nom");
				String prenom = resultat.getString("prenom");
				String telephone = resultat.getString("telephone");
				String adresse = resultat.getString("adresse");
				LocalDate dateNaissance = LocalDate.parse(resultat.getString("dateNaissance"));
				String orientationSexuelle = resultat.getString("orientationSexuelle");
				Chambreur chambreurTemp = new Chambreur(idChambreur, nom, prenom, telephone,
						adresse, dateNaissance, orientationSexuelle);
	
				DataServer.getInstance().listeChambreur.add(chambreurTemp);
			}
			
		} catch (SQLException e) {
		
			e.printStackTrace();
		}
	}
	
	/*
	 * Reads all entries in reservation table in sql db
	 */
	public void readAllReservation(){
		
		try {
			
			ResultSet resultat = statement.executeQuery("SELECT idReservation, idChambreur, "
					+ "noChambre, dateDebut, dateFin FROM reservation "
					+ "ORDER BY dateDebut;");
			
			while(resultat.next()){
				int idReservation = resultat.getInt("idReservation");
				int idChambreur = resultat.getInt("idChambreur");
				int noChambre = resultat.getInt("noChambre");
				LocalDate dateDebut = LocalDate.parse(resultat.getString("dateDebut"));
				LocalDate dateFin = LocalDate.parse(resultat.getString("dateFin"));	
				Reservation reservationTemp = new Reservation(idReservation, idChambreur,
						noChambre, dateDebut, dateFin);
				
				DataServer.getInstance().listeReservation.add(reservationTemp);
			}
		} catch (SQLException e) {
		
			e.printStackTrace();
		}
	}
	
	/*
	 * Inserts a Chambreur entry into chambreur table
	 * @param pChambreur
	 */
	public void insertChambreur(Chambreur pChambreur){
		
		try{
			
			int statut = statement.executeUpdate(
					"INSERT into chambreur values (null, '"
												   + pChambreur.getNom() + "', '"
												   + pChambreur.getPrenom() + "', '"
												   + pChambreur.getTelephone() + "', '"
												   + pChambreur.getAdresse() + "', '"
												   + pChambreur.getDateNaissance() + "', '"
												   + pChambreur.getOrientationSexuelle()+"');");
			System.out.println(statut);
		}
		catch(SQLException e){
			
			System.out.println(e.toString());
		}
	}
	
	public void modifChambreur(Chambreur pChambreur){
		try{
			System.out.println(pChambreur.getIdClient());
			int statut = statement.executeUpdate(
					"UPDATE chambreur SET nom = '" + pChambreur.getNom() + "', prenom = '" + pChambreur.getPrenom() +
					"', telephone = '" + pChambreur.getTelephone() + "', adresse = '" + pChambreur.getAdresse() + "', dateNaissance = '" +
					pChambreur.getDateNaissance() + "', orientationSexuelle = '" + pChambreur.getOrientationSexuelle() + 
					"' WHERE idChambreur = " + pChambreur.getIdClient() +";");
			System.out.println(statut);
		}
		catch(SQLException e){
			
			System.out.println(e.toString());
		}
	}
	
	public void modifReservation(Reservation reserv){
		try{
			
			int statut = statement.executeUpdate(
					"UPDATE reservation SET idChambreur = '" + reserv.getIdChambreur() + "', noChambre = '" + reserv.getNoChambre() +
					"', dateDebut = '" + reserv.getDateDebut().toString() + "', dateFin = '" + reserv.getDateFin().toString() + 
					"' WHERE idReservation = " + reserv.getIdReservation() + ";");
			System.out.println(statut);
		}
		catch(SQLException e){
			
			System.out.println(e.toString());
		}
	}
	
	/**
	 * Delete a Chambreur entry from chambreur table
	 * @param chambreur
	 */
	public void deleteChambreur(Chambreur chambreur){
		
		try{
		
			int statut = statement.executeUpdate(
					"DELETE from chambreur WHERE idChambreur = " + chambreur.getIdClient());
		}
		catch(SQLException e){
			
			System.out.println(e.toString());
		}
	}
	
	
	public void deleteReservation(Reservation reserv){
		
		try
		{		
			int statut = statement.executeUpdate(
					"DELETE from reservation WHERE idReservation = " + reserv.getIdReservation());
		}
		catch(SQLException e)
		{			
			System.out.println(e.toString());
		}
	}
	
	/*
	 * Inserts a Reservation entry into reservation table
	 */
	public void insertReservation(Reservation pReservation){
		
		try{
			
			int statut = statement.executeUpdate(
				  "INSERT into reservation values (null, '"
												   + pReservation.getIdChambreur() + "', '"
												   + pReservation.getNoChambre() + "', '"
												   + pReservation.getDateDebut().toString() + "', '"
												   + pReservation.getDateFin().toString()
												   + "');");
			System.out.println(statut);
		}
		catch(SQLException e){
			
			System.out.println(e.toString());
		}
	}
	
	public static DB getInstance(){
		
		if(instance == null){
			
			instance = new DB();
		}
		
		return instance;
	}	
}
