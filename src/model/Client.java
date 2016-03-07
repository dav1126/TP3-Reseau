package model;

import java.io.BufferedReader;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import model.DataClient;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class Client {
	
	Logger logger;
	SSLSocket client;
	private final String CODE_FIN_TRANSMISSION = "CODEDEFINDETRANSMISSIONALPHAROMEO*$??62863*?/0";
	InetAddress serveur;

	public void openAndConnectClientSocket()
	{
		// Récupération du fabriquant de sockets client SSLs
		SSLSocketFactory f = (SSLSocketFactory) SSLSocketFactory.getDefault();
		
		try{
			
			// Création du socket:
			serveur = InetAddress.getByName("172.18.10.21");
			client = (SSLSocket) f.createSocket(serveur, 55556);
			logger.log(Level.INFO, "Connection a:" + serveur.getHostAddress());
		
			client.startHandshake();
			
			receiveListChambreur();
			receiveListReservation();
			
		} catch (IOException e){
			
			e.printStackTrace();
			System.out.println("La connection a échouée");
		}
	}
	
	/**
	 * chambreur or reservation must be null
	 * @param requestCode
	 * @param chambreur
	 * @param reserv
	 */
	public void sendRequestToServer(int requestCode, Chambreur chambreur, Reservation reserv)
	{
		try 
		{			
			BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(client.getOutputStream()));
			
			String msg = "";
			msg += requestCode + ";";
			if (chambreur != null)
			{	
				msg += chambreur.getIdClient() + ";";
				msg += chambreur.getNom() + ";";
				msg += chambreur.getPrenom() + ";";
				msg += chambreur.getAdresse() + ";";
				msg += chambreur.getTelephone() + ";";
				msg += chambreur.getDateNaissance() + ";";
				msg += chambreur.getOrientationSexuelle();
				System.out.println(msg);
			}
			else if (reserv != null)
			{
				msg += reserv.getIdReservation() + ";";
				msg += reserv.getIdChambreur() + ";";
				msg += reserv.getNoChambre() + ";";
				msg += reserv.getDateDebut().toString() + ";";
				msg += reserv.getDateFin().toString();
			}
			System.out.println(msg);
			msg += "\n";
			writer.write(msg);
			writer.flush();	
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
	}
	
	public boolean receiveConsultConfirmation()
	{
		Boolean enConsult = false;
		BufferedReader reader;
		try
		{
			reader = new BufferedReader(new InputStreamReader(client.getInputStream()));
			String msgRecu = reader.readLine();
			System.out.println("msgRecu: " +msgRecu);
			if (msgRecu.equals("1"))
				enConsult = true;
				
		} catch (IOException e)
		{
			
			e.printStackTrace();
		}	
		System.out.println("Confirm recu: " + enConsult);
		return enConsult;
	}
	
	public void closeConnection(){
		
		try {
			DataClient.getInstance().listeChambreur.clear();
			DataClient.getInstance().listeObsChambreur.clear();
			DataClient.getInstance().listeReservation.clear();
			DataClient.getInstance().listeObsReservation.clear();
			client.close();
			System.out.println("FERMER CLIENT");
			logger.log(Level.INFO, "Déconnection de:" + serveur.getHostAddress());
		} catch (IOException e) {

			e.printStackTrace();
		}
	}

	public void receiveListChambreur() {
		DataClient.getInstance().getListeObsChambreur().clear();
		DataClient.getInstance().getListeChambreur().clear();
		try {
			
			BufferedReader reader = new BufferedReader(new InputStreamReader(client.getInputStream()));
			
			String line;
			int compteur = 0;
			
			Chambreur chambreurTemp = new Chambreur();
			while(!(line = reader.readLine()).equals(CODE_FIN_TRANSMISSION)){
				compteur++;
				
				
				switch(compteur){
				
				case 1: chambreurTemp.setIdClient(Integer.parseInt(line));break;
				case 2: chambreurTemp.setNom(line);break;
				case 3: chambreurTemp.setPrenom(line);break;
				case 4: chambreurTemp.setTelephone(line);break;
				case 5: chambreurTemp.setAdresse(line);break;
				case 6: chambreurTemp.setDateNaissance(LocalDate.parse(line));break;
				case 7: chambreurTemp.setOrientationSexuelle(line);break;
				}
				
				if(compteur >= 7){
					
//					DataClient.getInstance().listeChambreur.add(chambreurTemp);
					FormatCellule fc = new FormatCellule(new SimpleIntegerProperty(chambreurTemp.getIdClient()),
														 new SimpleStringProperty(chambreurTemp.getNom()),
														 new SimpleStringProperty(chambreurTemp.getPrenom()));
					DataClient.getInstance().getListeObsChambreur().add(0, fc);
					DataClient.getInstance().getListeChambreur().add(chambreurTemp);
					chambreurTemp = new Chambreur();
					compteur = 0;
				}
			}
		} catch (IOException e) {

			e.printStackTrace();
		}
	}
	
	public void receiveListReservation() {
		DataClient.getInstance().getListeObsReservation().clear();
		DataClient.getInstance().getListeReservation().clear();
		try {
			
			BufferedReader reader = new BufferedReader(new InputStreamReader(client.getInputStream()));
			
			String line;
			int compteur = 0;
			Reservation reservationTemp = new Reservation();
				
			while(!(line = reader.readLine()).equals(CODE_FIN_TRANSMISSION)){
				compteur++;
				
				switch(compteur){
				
				case 1: reservationTemp.setIdReservation(Integer.parseInt(line));break;
				case 2: reservationTemp.setIdChambreur(Integer.parseInt(line));break;
				case 3: reservationTemp.setNoChambre(Integer.parseInt(line));break;
				case 4: reservationTemp.setDateDebut(LocalDate.parse(line));break;
				case 5: reservationTemp.setDateFin(LocalDate.parse(line));break;
				}
				
				if(compteur >=5){
					FormatCellule fc = new FormatCellule(new SimpleStringProperty(reservationTemp.getDateDebut().toString()),
							 		   					 new SimpleStringProperty(reservationTemp.getDateFin().toString()),
							 		   					 new SimpleIntegerProperty(reservationTemp.getNoChambre()));				
					DataClient.getInstance().getListeObsReservation().add(0, fc);
					DataClient.getInstance().getListeReservation().add(reservationTemp);
					reservationTemp = new Reservation();
					compteur = 0;
				}
			}
			
		} catch (IOException e) {

			e.printStackTrace();
		}
	}
	
	public void setLogger(Logger logger)
	{
		this.logger = logger;
	}
}
