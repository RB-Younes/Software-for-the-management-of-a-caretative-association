import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

public class Adherent {
	
	Connection cnx=null;
	PreparedStatement prepared = null;
	ResultSet resultat =null;
	
	//ID 2 premier car de (2Nom,2Prenom)+Num Seq 
	private String Nom;
	private String Prenom;
	private String Sexe;
	private String Code_F;
	private String Type;
	private String NumTel;
	private String ADRmail;
	private String ADR;
	private String Wilaya;
	private String DateN;
	private String LieuDN;
	private String Dateeng;
	
	public Adherent() {
		super();
	}

	public Adherent(String nom, String prenom, String sexe, String code_F, String type, String numTel, String aDRmail,
			String aDR, String wilaya, String dateN, String lieuDN, String dateeng) {
		super();
		Nom = nom;
		Prenom = prenom;
		Sexe = sexe;
		Code_F = code_F;
		Type = type;
		NumTel = numTel;
		ADRmail = aDRmail;
		ADR = aDR;
		Wilaya = wilaya;
		DateN = dateN;
		LieuDN = lieuDN;
		Dateeng = dateeng;
	}



	public void Ajouter ()
	{
		cnx = ConnexionDB.CnxDB();
		
		int nextID_from_seq=0;
		String sql="SELECT NUM_SEQ_AD.nextval S FROM dual";
		try {
			prepared=cnx.prepareStatement(sql);
			resultat=prepared.executeQuery();
			if(resultat.next())
			    {nextID_from_seq = resultat.getInt(1);}//le numero sequenciel
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			JOptionPane.showMessageDialog(null, e);
		}
		 sql = "insert into Adherent values ('"+this.Nom.substring(0,2)+this.Prenom.substring(0,2)+nextID_from_seq+"','"+this.Nom+"','"+this.Prenom+"','"+this.Sexe+"','"+this.Code_F+"','"+this.Type+"','"+this.NumTel+"','"+this.ADRmail+"','"+this.ADR+"','"+this.Wilaya+"','"+this.DateN+"','"+this.LieuDN+"','"+this.Dateeng+"') ";                                   
		try {
			prepared=cnx.prepareStatement(sql);
			prepared.executeQuery();//executer la commande
			//Message
			JOptionPane.showMessageDialog(null, "Adherent ajouté avc succés!","Ajout" ,JOptionPane.PLAIN_MESSAGE, new ImageIcon(AuthentificationFrame.class.getResource("/messages_img/stamp.png")));
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			JOptionPane.showMessageDialog(null, e);
			e.printStackTrace();
		}
	}
	
	public void Supprimer(String ID_AD,String ID_AD_Util) {
		cnx = ConnexionDB.CnxDB();
		String sql ="Select * from Users where ID_AD='"+ID_AD+"'";
		
		try {	
			prepared=cnx.prepareStatement(sql);	
			resultat=prepared.executeQuery();
					if (resultat.next()) {
						int ClickedButton	=JOptionPane.showConfirmDialog(null, "Vouler Vous vraiment supprimer cet Adherent ?\n la suppression de cette Adherent causera la perte de  ses information et son accees a l'application !", "Confirmation",JOptionPane.YES_NO_OPTION);
						if(ClickedButton==JOptionPane.YES_OPTION )
							{
							
							sql ="Select code_F from Adherent where ID_AD='"+ID_AD_Util+"'";
							prepared=cnx.prepareStatement(sql);	
							resultat=prepared.executeQuery();
							if (resultat.next()) {
								String code=resultat.getString("code_F");
								sql ="Select NomF from Fonction where Code_F='"+code+"'";
								prepared=cnx.prepareStatement(sql);	
								resultat=prepared.executeQuery();
								if (resultat.next()) {
									if (resultat.getString("NomF").equals("Président") ) {
										if (!ID_AD.equals(ID_AD_Util)) {
											sql ="delete from Users where ID_AD='"+ID_AD+"'";
											prepared=cnx.prepareStatement(sql);
											prepared.execute();
										
										 sql ="delete from Adherent where ID_AD='"+ID_AD+"'";
												prepared=cnx.prepareStatement(sql);
												prepared.execute();
												JOptionPane.showMessageDialog(null, "Adherent supprimé avec succès!","Suppression" ,JOptionPane.PLAIN_MESSAGE, new ImageIcon(AuthentificationFrame.class.getResource("/messages_img/stamp.png")));
										}
										else {
											JOptionPane.showMessageDialog(null, "Vous ne pouvez pas supprimer vos informations, un autre president doit le faire","Suppression" ,JOptionPane.PLAIN_MESSAGE,new ImageIcon(GestionPerEnBesoin.class.getResource("/messages_img/error.png")));
										}
										
									}
								}
							}
							}
					}
					else {
						 sql ="delete from Adherent where ID_AD='"+ID_AD+"'";
							try {
								prepared=cnx.prepareStatement(sql);
								prepared.execute();
								JOptionPane.showMessageDialog(null, "Adherent supprimé avec succès!","Suppression" ,JOptionPane.PLAIN_MESSAGE, new ImageIcon(AuthentificationFrame.class.getResource("/messages_img/stamp.png")));
							} catch (SQLException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}	
					}
							
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
		
	}
	
	public void Modifier(String ID_AD,String Nom,String Prenom,String Sexe,String Code_F,String Type,String NumTel,String ADRmail,String ADR,String Wilaya,String DateN,String LieuDN) {
		// etablire la connection:
		cnx = ConnexionDB.CnxDB();

		String sql = "Update Adherent set Nom =?,Prenom  =?,Sexe =?,Code_F =?,Type =?,NumTel =?,ADRmail=?,ADR =?,Wilaya =?,DateN =?,LieuDN =? where ID_AD='"+ID_AD+"'";
		try {
			
			prepared=cnx.prepareStatement(sql);
			
			prepared.setString(1, Nom);	
			prepared.setString(2, Prenom);
			prepared.setString(3, Sexe);
			prepared.setString(4, Code_F);	
			prepared.setString(5, Type);
			prepared.setString(6, NumTel);
			prepared.setString(7, ADRmail);
			prepared.setString(8,ADR);
			prepared.setString(9, Wilaya);
			prepared.setString(10, DateN);
			prepared.setString(11, LieuDN);
			
			prepared.execute();
			//message
			JOptionPane.showMessageDialog(null, "Adherent modifié avec succés!","Modification" ,JOptionPane.PLAIN_MESSAGE, new ImageIcon(AuthentificationFrame.class.getResource("/messages_img/stamp.png")));
			
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			JOptionPane.showMessageDialog(null, e);
		}
	}
	
	

}
