package model;

import java.io.BufferedReader;
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
	
	SSLSocket client;
	private final String CODE_FIN_TRANSMISSION = "CODEDEFINDETRANSMISSIONALPHAROMEO*$??62863*?/0";

	public void openAndConnectClientSocket()
	{
		// R�cup�ration du fabriquant de sockets client SSLs
		SSLSocketFactory f = (SSLSocketFactory) SSLSocketFactory.getDefault();
		
		try{
			
			// Cr�ation du socket:
			InetAddress localHost = InetAddress.getLocalHost();
			client = (SSLSocket) f.createSocket(localHost, 55556);
		
			client.startHandshake();
			
			receiveListChambreur();
			receiveListReservation();
			
		} catch (IOException e){
			
			e.printStackTrace();
			System.out.println("La connection a �chou�e");
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
	
	public void closeConnection(){
		
		try {
			DataClient.getInstance().listeChambreur.clear();
			DataClient.getInstance().listeObsChambreur.clear();
			DataClient.getInstance().listeReservation.clear();
			DataClient.getInstance().listeObsReservation.clear();
			client.close();
			System.out.println("FERMER CLIENT");
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
}
