import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

public class Fonction {
	Connection cnx=null;
	PreparedStatement prepared = null;
	ResultSet resultat =null;
	
	
	
	private String lbl;
	

	public Fonction() {
		super();
	}

	public Fonction(String lbl) {
		super();
		this.lbl = lbl;
	}

	public void Ajouter ()
	{
		cnx = ConnexionDB.CnxDB();
		int nextID_from_seq=0;
		String sql="SELECT Num_SEQ_F.nextval S FROM dual";
		try {
			prepared=cnx.prepareStatement(sql);
			resultat=prepared.executeQuery();
			if(resultat.next())
			    {nextID_from_seq = resultat.getInt(1);}//le numero sequenciel
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			JOptionPane.showMessageDialog(null, e);
		}
		
		 sql = "insert into Fonction values ('"+ this.lbl.substring(0,2 )+nextID_from_seq+"','"+this.lbl+"')";//2premiers car + num seq
		try {
			prepared=cnx.prepareStatement(sql);
			prepared.executeQuery();//executer la commande
			//Message
			JOptionPane.showMessageDialog(null, "Fonction ajouté avc succés!","Ajout" ,JOptionPane.PLAIN_MESSAGE, new ImageIcon(AuthentificationFrame.class.getResource("/messages_img/stamp.png")));
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			JOptionPane.showMessageDialog(null, e);
		}
	}
	
	public void Modifier (String Code_F,String NomF )
	{
		cnx = ConnexionDB.CnxDB();
		String sql = "Update Fonction set NomF =? where Code_F='"+Code_F+"'";
		try { 
			
			prepared=cnx.prepareStatement(sql);
			// remplire les ? avec ces infos
			prepared.setString(1, NomF);		
			//executer
			prepared.execute();
			//message
			JOptionPane.showMessageDialog(null, "Fonction modifé avec succé!","Modification",JOptionPane.PLAIN_MESSAGE, new ImageIcon(AuthentificationFrame.class.getResource("/messages_img/stamp.png")));
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			JOptionPane.showMessageDialog(null, e);
		}
	}
	
	public void Supprimer (String Code_F)
	{
		cnx = ConnexionDB.CnxDB();
		
		String sql ="Select * from Adherent where Code_F='"+Code_F+"'";
		try {
			
			prepared=cnx.prepareStatement(sql);	
			resultat=prepared.executeQuery();
					if (resultat.next()) {
						JOptionPane.showMessageDialog(null, "la suppression de cette Fonction causera la perte de plusieurs Adherents (Suppression impossible!)");
					}
					else {
					sql ="delete from Fonction where Code_F='"+Code_F+"'";
					prepared=cnx.prepareStatement(sql);
					prepared.execute();
					JOptionPane.showMessageDialog(null, "Fonction supprimé avec succès!","Suppression" ,JOptionPane.PLAIN_MESSAGE, new ImageIcon(AuthentificationFrame.class.getResource("/messages_img/stamp.png")));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			JOptionPane.showMessageDialog(null, e);		
			}	
	}
	
	
	//Main TEST 
	/*public static void main(String[] args) {
		Fonction F =new Fonction();
		F.Supprimer("Al3");
		//F.Ajouter();
		//F.Modifier("An2", "AlicevMod");
	}*/


}
