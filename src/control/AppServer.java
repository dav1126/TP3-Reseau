package control;

import java.time.LocalDate;

import model.Chambreur;
import model.DB;
import model.DataServer;
import model.Reservation;
import model.Server;

public class AppServer {

	public static void main(String[] args) {
		
		DB.getInstance().readAllChambreur();

		for(int i = 0; i < DataServer.getInstance().getListeChambreur().size();i++){
			
			System.out.println(DataServer.getInstance().getListeChambreur().get(i).getIdClient()+"\n"
					+ DataServer.getInstance().getListeChambreur().get(i).getNom() + "\n"
					+ DataServer.getInstance().getListeChambreur().get(i).getPrenom() + "\n"
					+ DataServer.getInstance().getListeChambreur().get(i).getTelephone() + "\n"
					+ DataServer.getInstance().getListeChambreur().get(i).getAdresse() + "\n"
					+ DataServer.getInstance().getListeChambreur().get(i).getDateNaissance() + "\n"
					+ DataServer.getInstance().getListeChambreur().get(i).getOrientationSexuelle() + "\n");	
		}
		
		DB.getInstance().readAllReservation();
		
		for(int i = 0; i < DataServer.getInstance().getListeReservation().size();i++){
			
			System.out.println(DataServer.getInstance().getListeReservation().get(i).getIdReservation()+"\n"
					+ DataServer.getInstance().getListeReservation().get(i).getIdChambreur() + "\n"
					+ DataServer.getInstance().getListeReservation().get(i).getNoChambre() + "\n"
					+ DataServer.getInstance().getListeReservation().get(i).getDateDebut() + "\n"
					+ DataServer.getInstance().getListeReservation().get(i).getDateFin() + "\n");	
		}
		
		Server server = new Server();
		server.openServerSocket();
		server.acceptClientConnection();
	}

}
