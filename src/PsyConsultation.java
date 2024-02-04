import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

public class PsyConsultation {


	Connection cnx=null;
	PreparedStatement prepared = null;
	ResultSet resultat =null;
	
	private String ID_PEB;//PEB==Persoonne en besoin
	private String DateCons;
	private String HeureConsul;
	private String Obs;
	private String Etat;
	
	public PsyConsultation() {
		super();
	}

	public PsyConsultation(String iD_PEB, String dateCons, String heureConsul, String obs, String etat) {
		super();
		ID_PEB = iD_PEB;
		DateCons = dateCons;
		HeureConsul = heureConsul;
		Obs = obs;
		Etat = etat;
	}

	public void Ajouter ()
	{
		cnx = ConnexionDB.CnxDB();
		
		int nextID_from_seq=0;
		String sql="SELECT NUM_SEQ_PC.nextval S FROM dual";
		try {
			prepared=cnx.prepareStatement(sql);
			resultat=prepared.executeQuery();
			if(resultat.next())
			    {nextID_from_seq = resultat.getInt(1);}//le numero sequenciel
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			JOptionPane.showMessageDialog(null, e);
		}
		String jours = this.DateCons.substring(0,2 );//jours
		String mois = this.DateCons.substring(3,5 );//mois
		String annee = this.DateCons.substring(6,10 );//annee
		
		 sql = "insert into PsyConsult values ('"+this.HeureConsul.substring(0,2)+jours+mois+annee+nextID_from_seq+"','"+this.ID_PEB+"','"+this.DateCons+"','"+this.HeureConsul+"','"+this.Obs+"','"+this.Etat+"') ";                                   
		try {
			prepared=cnx.prepareStatement(sql);
			prepared.executeQuery();//executer la commande
			//Message
			JOptionPane.showMessageDialog(null, "Consultation ajoutée avc succés!","Ajout" ,JOptionPane.PLAIN_MESSAGE, new ImageIcon(AuthentificationFrame.class.getResource("/messages_img/stamp.png")));
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			JOptionPane.showMessageDialog(null, e);
		}
	}


	public void Supprimer(String ID_PC) {
		cnx = ConnexionDB.CnxDB();
		
		String sql ="delete from PsyConsult where ID_PC='"+ID_PC+"'";
		try {
			prepared=cnx.prepareStatement(sql);
			prepared.execute();
			JOptionPane.showMessageDialog(null, "Consultation En Besoin supprimée avec succès!","Suppression" ,JOptionPane.PLAIN_MESSAGE, new ImageIcon(AuthentificationFrame.class.getResource("/messages_img/stamp.png")));
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
		
	}
	
	public void Modifier (String ID_PC,String Obs,String Etat)
	{
		cnx = ConnexionDB.CnxDB();
		String sql = "Update PsyConsult set Obs =?,Etat=? where ID_PC="+ID_PC+"";
		try {
			prepared=cnx.prepareStatement(sql);
			prepared.setString(1, Obs);
			prepared.setString(2, Etat);
			//executer
			prepared.execute();
			//message
			JOptionPane.showMessageDialog(null, "Consultation modifé avec succé!","Modification",JOptionPane.PLAIN_MESSAGE, new ImageIcon(AuthentificationFrame.class.getResource("/messages_img/stamp.png")));
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			JOptionPane.showMessageDialog(null, e);
		}
	}
	
	/*public static void main(String[] args) {
		PsyConsultation A =new PsyConsultation("Yo2", "21/01/2021","21:22","RAS");
		//A.Supprimer("21210120211");
		A.Ajouter();
		//A.Modifier("21210120211","Yo2","21/01/2021", "22:17","RAS2");
		}*/





}
