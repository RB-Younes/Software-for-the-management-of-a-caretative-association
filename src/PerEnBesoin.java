import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

public class PerEnBesoin {
	
	
	Connection cnx=null;
	PreparedStatement prepared = null;
	ResultSet resultat =null;
	
	//ID 2 premier car de (2Nom+Num Seq 
	private String Nom;
	private String Prenom;
	private String Sexe;
	private String NumTel;
	private String Cause;
	private String ADRmail;
	private String ADR;
	private String Pro;
	private String etatCOV;
	private String Mariee;
	private String MF;
	private String Wilaya;
	private String DateN;
	private String DatePremierAide;
	private String LieuDN;
	
	
	public PerEnBesoin() {
		super();
	}


	public PerEnBesoin(String nom, String prenom, String sexe, String numTel, String cause, String aDRmail, String aDR,
			String pro, String etatCOV, String mariee, String mF, String wilaya, String dateN, String datePremierAide,
			String lieuDN) {
		super();
		Nom = nom;
		Prenom = prenom;
		Sexe = sexe;
		NumTel = numTel;
		Cause = cause;
		ADRmail = aDRmail;
		ADR = aDR;
		Pro = pro;
		this.etatCOV = etatCOV;
		Mariee = mariee;
		MF = mF;
		Wilaya = wilaya;
		DateN = dateN;
		DatePremierAide = datePremierAide;
		LieuDN = lieuDN;
	}




	public void Ajouter ()
	{
		cnx = ConnexionDB.CnxDB();
		
		cnx = ConnexionDB.CnxDB();
		int nextID_from_seq=0;
		String sql="SELECT NUM_SEQ_PEB.nextval S FROM dual";
		try {
			prepared=cnx.prepareStatement(sql);
			resultat=prepared.executeQuery();
			if(resultat.next())
			    {nextID_from_seq = resultat.getInt(1);}//le numero sequenciel
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			JOptionPane.showMessageDialog(null, e);
		}
		 sql = "insert into PersoEnBesoin values ('"+this.Nom.substring(0,2)+nextID_from_seq+"','"+this.Nom+"','"+this.Prenom+"','"+this.Sexe+"','"+this.NumTel+"','"+this.Cause+"','"+this.etatCOV+"','"+this.Pro+"','"+this.Mariee+"','"+this.MF+"','"+this.ADRmail+"','"+this.ADR+"','"+this.Wilaya+"','"+this.DateN+"','"+this.LieuDN+"','"+this.DatePremierAide+"') ";                                   
		try {
			prepared=cnx.prepareStatement(sql);
			prepared.executeQuery();//executer la commande
			//Message
			JOptionPane.showMessageDialog(null, "Personne En Besoin ajoutée avc succés!","Ajout" ,JOptionPane.PLAIN_MESSAGE, new ImageIcon(AuthentificationFrame.class.getResource("/messages_img/stamp.png")));
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			JOptionPane.showMessageDialog(null, e);
		}
	}
	
	public void Supprimer(String ID_PEB) {
		cnx = ConnexionDB.CnxDB();
		String sql ="Select * from PsyConsult where ID_PEB='"+ID_PEB+"'";
		
		try {	
			prepared=cnx.prepareStatement(sql);	
			resultat=prepared.executeQuery();
					if (resultat.next()) {
						JOptionPane.showMessageDialog(null, "la suppression de cette Personne causera la perte de plusieurs consultation (Suppression impossible!)");
					}
					else {
						sql ="delete from PersoEnBesoin where ID_PEB='"+ID_PEB+"'";
						prepared=cnx.prepareStatement(sql);
						prepared.execute();
						JOptionPane.showMessageDialog(null, "Personne En Besoin supprimée avec succès!","Suppression" ,JOptionPane.PLAIN_MESSAGE, new ImageIcon(AuthentificationFrame.class.getResource("/messages_img/stamp.png")));
					}
							
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
		
	}
	
	public void Modifier(String ID_PEB,String Nom,String Prenom,String Sexe,String NumTel,String cause,String etatCOV,String pro,String Mariee,String Nfamille,String ADRmail,String ADR,String Wilaya,String DateN,String LieuDN) {
		// etablire la connection:
		cnx = ConnexionDB.CnxDB();
		//modifier une cnsultation
		String sql = "Update PersoEnBesoin set Nom =?,Prenom  =?,Sexe =? ,NumTel =?,cause = ?,etatCOV = ?,pro =? ,Mariee=? ,Nfamille=? ,ADRmail =?,ADR=?,Wilaya =?,DateN =?,LieuDN =? where ID_PEB='"+ID_PEB+"'";
		try {
			
			prepared=cnx.prepareStatement(sql);
			
			prepared.setString(1, Nom);	
			prepared.setString(2, Prenom);
			prepared.setString(3, Sexe);
			prepared.setString(4, NumTel);	
			prepared.setString(5, cause);
			prepared.setString(6, etatCOV);
			prepared.setString(7, pro);
			prepared.setString(8, Mariee);
			prepared.setString(9, Nfamille);
			prepared.setString(10, ADRmail);
			prepared.setString(11, ADR);
			prepared.setString(12, Wilaya);
			prepared.setString(13, DateN);
			prepared.setString(14, LieuDN);
			
			prepared.execute();
			//message
			JOptionPane.showMessageDialog(null, " Personne En Besoin modifiée avec succés!","Modification" ,JOptionPane.PLAIN_MESSAGE, new ImageIcon(AuthentificationFrame.class.getResource("/messages_img/stamp.png")));
			
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			JOptionPane.showMessageDialog(null, e);
		}
	}
	
	public static void main(String[] args) {
		//PerEnBesoin A =new PerEnBesoin("Younes", "Rebai","0561331421","cause","qsdqsdqs@gmail.com","zeralda","alger","01/01/2001","01/01/2001","Kpuba");
	//A.Supprimer("Yo4");
	//A.Ajouter();
	//A.Modifier("Yo2","Youn", "Reb","0561331","cau","s@gmail.com","ser","alf","02/01/2001","02/01/2001","Kup");
	}
	
	

}
