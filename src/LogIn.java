import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.JOptionPane;

public class LogIn {
	
	// les composants de ma connection connection a la BD:
	Connection cnx=null;
	PreparedStatement prepared = null;
	ResultSet resultat =null; 
	
	private String Password;
	private String Adresse;
	
	
	public LogIn(String adresse,String password) {
		super();
		Password = password;
		Adresse = adresse;
	}
	 public String SeConnecter() {
		 String ID_AD="";
			// etablire la connection:
			cnx = ConnexionDB.CnxDB();
			// la commande sql
			String sql = "select * from Users";	//where AdrMail ="+this.Adresse+""	AdrMail,Password	
			try {
				
				prepared =cnx.prepareStatement(sql);
				resultat = prepared.executeQuery();

				while (resultat.next()) {
					String MDP=resultat.getString("Password");
					String ADR=resultat.getString("AdrMail");
					if (ADR.equals(this.Adresse) && MDP.equals(this.Password)) return resultat.getString("ID_AD"); 
				}
				 return ID_AD; 
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				JOptionPane.showMessageDialog(null, e);
			}
			return ID_AD;
		 
	 }

}
