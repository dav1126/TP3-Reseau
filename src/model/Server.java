package model;


import java.io.BufferedReader;
import model.DB;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.security.KeyStore;
import java.time.LocalDate;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLServerSocketFactory;
import javax.net.ssl.SSLSocket;
import model.DataServer;

public class Server {
	
	SSLServerSocket serveur;
	private final String CODE_FIN_TRANSMISSION = "CODEDEFINDETRANSMISSIONALPHAROMEO*$??62863*?/0";
	
	public void openServerSocket(){
		
		String ksName = "privateKey.jks";
		char ksPwd[] = "Abc12345".toCharArray();
		char ctPwd[] = "Abc12345".toCharArray();
		
		try{
				
			KeyStore ks = KeyStore.getInstance("JKS");
			ks.load(new FileInputStream(ksName), ksPwd);
			KeyManagerFactory kmf = KeyManagerFactory.getInstance("SunX509");
			kmf.init(ks, ctPwd);
			SSLContext sc = SSLContext.getInstance("TLS");
			sc.init(kmf.getKeyManagers(), null, null);
			SSLServerSocketFactory ssf = sc.getServerSocketFactory();
			serveur = (SSLServerSocket) ssf.createServerSocket(55556);
		}
		
		catch(Exception ex){
			ex.printStackTrace();
			System.out.println("\nOuverture de socket serveur a échouée");
		}
	}
	
	public void acceptClientConnection(){
		
		Thread thread = new Thread(() ->
		{	
			while (true)
			{
				try {
					
					SSLSocket client = (SSLSocket) serveur.accept();
					
					sendListChambreur(client);
					sendListReservation(client);
					waitForDBRequest(client);
					
				} catch (IOException e) {
		
					e.printStackTrace();
				}
				
			}	
		});
		thread.start();
	}
	
	//Patrick le beau pas chic (pôv)
	private void waitForDBRequest(SSLSocket client)
	{
		Thread thread = new Thread(() ->
		{
			try 
			{			
				BufferedReader reader = new BufferedReader(new InputStreamReader(client.getInputStream()));			
				String msgRecu = reader.readLine();
				String[] msgRecuSplit = msgRecu.split(";");
				Chambreur chambreur = new Chambreur();
				Reservation reserv = new Reservation();
				if (msgRecuSplit[0].equals("1") || msgRecuSplit[0].equals("2") || msgRecuSplit[0].equals("3"))
				{
					chambreur.setIdClient(Integer.parseInt(msgRecuSplit[1]));
					chambreur.setNom(msgRecuSplit[2]);
					chambreur.setPrenom(msgRecuSplit[3]);
					chambreur.setAdresse(msgRecuSplit[4]);
					chambreur.setTelephone(msgRecuSplit[5]);
					chambreur.setOrientationSexuelle(msgRecuSplit[6]);
				}
				else
				{
					reserv.setIdReservation(Integer.parseInt(msgRecuSplit[1]));
					reserv.setIdChambreur(Integer.parseInt(msgRecuSplit[2]));
					reserv.setNoChambre(Integer.parseInt(msgRecuSplit[3]));
					reserv.setDateDebut(LocalDate.parse(msgRecuSplit[4]));
					reserv.setDateFin(LocalDate.parse(msgRecuSplit[5]));
				}
				
				switch(msgRecuSplit[0])
				{
					case "1":
						DB.getInstance().insertChambreur(chambreur);
						break;
					case "2":
						DB.getInstance().modifChambreur(chambreur);
						break;
					case "3":
						DB.getInstance().deleteChambreur(chambreur);
						break;
					case "4":
						DB.getInstance().insertReservation(reserv);
						break;
					case "5":
						DB.getInstance().modifReservation(reserv);
						break;
					case "6":
						DB.getInstance().deleteReservation(reserv);
						break;					
				}
			}
			catch(IOException e)
			{
				System.out.println("*****Client déconnecté*****");
			}
		});
		thread.start();
	}

	private void sendListChambreur(SSLSocket client) {
		
		try {
			DataServer.getInstance().getListeChambreur().clear();
			DB.getInstance().readAllChambreur();
			BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(client.getOutputStream()));
			
			String msg = "";
			for(int i = 0; i < DataServer.getInstance().listeChambreur.size(); i++){
				
				msg += DataServer.getInstance().listeChambreur.get(i).getIdClient() + "\n"
					 + DataServer.getInstance().listeChambreur.get(i).getNom() + "\n"
					 + DataServer.getInstance().listeChambreur.get(i).getPrenom() + "\n"
					 + DataServer.getInstance().listeChambreur.get(i).getTelephone() + "\n"
					 + DataServer.getInstance().listeChambreur.get(i).getAdresse() + "\n"
					 + DataServer.getInstance().listeChambreur.get(i).getDateNaissance().toString() + "\n"
					 + DataServer.getInstance().listeChambreur.get(i).getOrientationSexuelle() + "\n";
			}
			msg += CODE_FIN_TRANSMISSION + "\n";
			writer.write(msg);
			writer.flush();
			
		} catch (IOException e) {

			e.printStackTrace();
		}
	}
	
	private void sendListReservation(SSLSocket client){
		
		try {
			DataServer.getInstance().getListeReservation().clear();
			DB.getInstance().readAllReservation();
			BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(client.getOutputStream()));
			String msg = "";
			for(int i = 0; i < DataServer.getInstance().listeReservation.size(); i++){
				
				msg += DataServer.getInstance().listeReservation.get(i).getIdReservation() + "\n"
						 + DataServer.getInstance().listeReservation.get(i).getIdChambreur() + "\n"
						 + DataServer.getInstance().listeReservation.get(i).getNoChambre() + "\n"
						 + DataServer.getInstance().listeReservation.get(i).getDateDebut() + "\n"
						 + DataServer.getInstance().listeReservation.get(i).getDateFin() + "\n";
			}		
			msg += CODE_FIN_TRANSMISSION + "\n";
			writer.write(msg);
			writer.flush();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
