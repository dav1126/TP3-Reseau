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
import java.time.LocalDateTime;
import java.util.logging.FileHandler;
import java.util.logging.Formatter;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLServerSocketFactory;
import javax.net.ssl.SSLSocket;
import model.DataServer;

public class Server {
	
	SSLServerSocket serveur;
	private final String CODE_FIN_TRANSMISSION = "CODEDEFINDETRANSMISSIONALPHAROMEO*$??62863*?/0";
	private static final Logger LOGGER = Logger.getLogger(Server.class.getName());
	Handler fileHandler = null;
	Formatter simpleFormatter = null;
	String homeDirectoryPath = System.getProperty("user.home");
	
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
			
			//Logging
			// Creating FileHandler
			fileHandler = new FileHandler(homeDirectoryPath + "/tp3.log" );

			// Creating SimpleFormatter
			simpleFormatter = new SimpleFormatter();

			// Assigning handler to logger
			LOGGER.addHandler(fileHandler);

			// Logging message of Level info (this should be publish in the default format i.e. XMLFormat)
			LOGGER.info("Finnest message: Logger with DEFAULT FORMATTER");

			// Setting formatter to the handler
			fileHandler.setFormatter(simpleFormatter);

			// Setting Level to ALL
			fileHandler.setLevel(Level.ALL);
			LOGGER.setLevel(Level.ALL);

			// Logging message of Level finest (this should be publish in the simple format)
			LOGGER.finest("Finnest message: Logger with SIMPLE FORMATTER");
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
					LOGGER.log(Level.INFO, "Connection de:" + client.getInetAddress().getHostAddress());
					
					sendListChambreur(client);
					sendListReservation(client);
					waitForDBRequest(client);
					
				} catch (IOException e) 
				{
		
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
						while (true)
						{
						
						BufferedReader reader = new BufferedReader(new InputStreamReader(client.getInputStream()));	
						String msgRecu = reader.readLine();
						String[] msgRecuSplit = msgRecu.split(";");
						Chambreur chambreur = new Chambreur();
						Reservation reserv = new Reservation();
						System.out.println("msgRecuSplit: " +msgRecuSplit[0]);
						if (msgRecuSplit[0].equals("1") || msgRecuSplit[0].equals("2") || msgRecuSplit[0].equals("3") || msgRecuSplit[0].equals("7") || msgRecuSplit[0].equals("91"))
						{
							chambreur.setIdClient(Integer.parseInt(msgRecuSplit[1]));
							chambreur.setNom(msgRecuSplit[2]);
							chambreur.setPrenom(msgRecuSplit[3]);
							chambreur.setAdresse(msgRecuSplit[4]);
							chambreur.setTelephone(msgRecuSplit[5]);
							chambreur.setDateNaissance(LocalDate.parse(msgRecuSplit[6]));
							chambreur.setOrientationSexuelle(msgRecuSplit[7]);
						}
						else if (msgRecuSplit[0].equals("4") || msgRecuSplit[0].equals("5") || msgRecuSplit[0].equals("6") || msgRecuSplit[0].equals("8") || msgRecuSplit[0].equals("92"))
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
							case "7":
								boolean enConsult1 = checkIfChambreurIsInConsult(chambreur.getIdClient());
								if (!enConsult1)
								{
									System.out.println("ajout chambreur: " + chambreur.getIdClient());
									DataServer.getInstance().getListeConsultChambreur().add(chambreur);
								}
								sendConfirmation(enConsult1, client);
								break;
							case "8":
								boolean enConsult2 = checkIfReservIsInConsult(reserv.getIdReservation());
								if (!enConsult2)
								{
									System.out.println("ajout reserv" + reserv.getIdReservation());
									DataServer.getInstance().getListeConsultReservation().add(reserv);
								}
								sendConfirmation(enConsult2, client);
								break;
							case "91":								
									System.out.println("removechambreur: " + chambreur.getIdClient());
									DB.getInstance().removeFromConsultList(chambreur);
									break;
							case "92":
									System.out.println("removereserv" + reserv.getIdReservation());
									DB.getInstance().removeFromConsultList(reserv);
									break;
								
						}
						
						if (!msgRecuSplit[0].equals("7") && !msgRecuSplit[0].equals("8") && !msgRecuSplit[0].equals("91") && !msgRecuSplit[0].equals("92"))
						{
							sendListChambreur(client);
							sendListReservation(client);
						}
				}
			}
			catch(Exception e)
			{
				LOGGER.log(Level.INFO, "Déconnection de:" + client.getInetAddress().getHostAddress());
				System.out.println("*****Client déconnecté*****");
			}
			
		});
		thread.start();
	}

	private void sendConfirmation(Boolean enConsult, SSLSocket client) throws Exception
	{
		BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(client.getOutputStream()));
		
		String msg = "";
		if (enConsult)
			msg = "1\n";
		else
			msg = "0\n";
		System.out.println("msg envoyé: " + msg);
		writer.write(msg);
		writer.flush();
	}
	
	private boolean checkIfReservIsInConsult(int reservID)
	{
		boolean dejaEnConsultation = false;
		for (Reservation reserv : DataServer.getInstance().getListeConsultReservation())
		{
			if (reserv.getIdReservation() == reservID)
				dejaEnConsultation = true;
		}
		return dejaEnConsultation;
	}

	private boolean checkIfChambreurIsInConsult(int chambreurID)
	{
		boolean dejaEnConsultation = false;
		for (Chambreur chambreur : DataServer.getInstance().getListeConsultChambreur())
		{
			if (chambreur.getIdClient() == chambreurID)
				dejaEnConsultation = true;
		}
		return dejaEnConsultation;		
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
	
	public Logger getLogger()
	{
		return LOGGER;
	}
}
