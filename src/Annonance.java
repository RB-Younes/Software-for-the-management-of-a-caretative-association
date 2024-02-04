import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

public class Annonance {
	
	Connection cnx=null;
	PreparedStatement prepared = null;
	ResultSet resultat =null;
	PreparedStatement prepared1 = null;
	ResultSet resultat1 =null;
	
	private String ID_ADH;
	private String Annonce;
	private String DateAnnonce;
	//il faut ajouter qui a mis l'annonce
	



	public Annonance() {
		super();
	}


	public Annonance(String iD_ADH, String annonce, String dateAnnonce) {
		super();
		ID_ADH = iD_ADH;
		Annonce = annonce;
		DateAnnonce = dateAnnonce;
	}


	public void Ajouter ()
	{
		cnx = ConnexionDB.CnxDB();
		String sql = "insert into Annonce values (ID_Ann.nextval,'"+this.ID_ADH+"','"+this.Annonce+"','"+this.DateAnnonce+"')";
		try {
			prepared=cnx.prepareStatement(sql);
			prepared.executeQuery();//executer la commande
			//Message
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			//JOptionPane.showMessageDialog(null, e);
			e.printStackTrace();
		}
		 sql = "select ADRmail from Adherent";
		
		try {
			prepared = cnx.prepareStatement(sql);
			resultat= prepared.executeQuery();
			while (resultat.next())
			{
				String ADRmail=resultat.getString("ADRmail");
				 try {
					 Properties props = new Properties();
			        
					props.setProperty("mail.transport.protocol", "smtp");     
				    props.setProperty("mail.host", "smtp.gmail.com");  
				    props.put("mail.smtp.auth", "true");  
				    props.put("mail.smtp.port", "465");  
				    //props.put("mail.debug", "true");  
				    props.put("mail.smtp.socketFactory.port", "465");  
				    props.put("mail.smtp.socketFactory.class","javax.net.ssl.SSLSocketFactory");  
				    props.put("mail.smtp.socketFactory.fallback", "false");
			        Session session = Session.getInstance(props,
			                new javax.mail.Authenticator() {
			                  protected PasswordAuthentication getPasswordAuthentication() {
			                      return new PasswordAuthentication("moha.rebai741@gmail.com", "moha74123");
			                  }
			                });
			        String contenu1="";
					String contenu2="";
					String contenu3="";
					String contenu4="";
					 int DebContenuindice=this.Annonce.indexOf("##");
					 if (DebContenuindice != -1) 
					 {
						  contenu1= this.Annonce.substring(0,DebContenuindice); 
						  contenu2= this.Annonce.substring(DebContenuindice+2 ,this.Annonce.length() ); 
						  int FinContenuindice=contenu2.indexOf("##");
						  contenu3=contenu2.substring(0,FinContenuindice);
						  contenu4=contenu2.substring(FinContenuindice+2,contenu2.length());
					 }
					String COntenuFinal="	"+contenu3+"\n"+contenu1+contenu4;

			            Message message = new MimeMessage(session);
			            message.setFrom(new InternetAddress("moha.rebai741@gmail.com"));
			            message.setRecipients(Message.RecipientType.TO,InternetAddress.parse(ADRmail));

			            message.setSubject("Nouvelle Annonce de l'Association Abed&Re");
			            message.setText("Bonjour,"
			                + "\n"+COntenuFinal+"\nCordialemnt." );
			            Transport.send(message);
			            
			        } catch (MessagingException e) {
						JOptionPane.showMessageDialog(null, "Echec de l'envoie.(Verifier que vous etes bien connecté a internet SVP)","Warning", JOptionPane.QUESTION_MESSAGE, new ImageIcon(GestionAnnonces.class.getResource("/messages_img/error.png")));
			        }
			
			}
			JOptionPane.showMessageDialog(null, "Annonce ajouté avc succés!","Ajout" ,JOptionPane.PLAIN_MESSAGE, new ImageIcon(AuthentificationFrame.class.getResource("/messages_img/stamp.png")));
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			JOptionPane.showMessageDialog(null, e);
		}
	}

	public void Modifier (String ID_Ann,String ID_AD,String AnnIn ,String DateAnn)
	{
		cnx = ConnexionDB.CnxDB();
		String Nom=null;
		String Prenom=null;
		String sql1 = "Select Nom,Prenom from Adherent where ID_AD='"+ID_AD+"'";
		try {
			prepared=cnx.prepareStatement(sql1);
			resultat=prepared.executeQuery();
			if(resultat.next())
			{
				 Nom=resultat.getString("Nom");
				 Prenom=resultat.getString("Prenom");
			}
			String sql2 = "Update Annonce set AnnIn =? where ID_Ann='"+ID_Ann+"'";
			prepared1=cnx.prepareStatement(sql2);
			
			AnnIn=AnnIn+"Annonce Modifiée par :"+Nom+" "+Prenom+",le:"+DateAnn+".\n";
			prepared1.setString(1, AnnIn);		
			prepared1.execute();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			JOptionPane.showMessageDialog(null, e);
		}
		
		 sql1 = "select ADRmail from Adherent";
			
			try {
				prepared = cnx.prepareStatement(sql1);
				resultat= prepared.executeQuery();
				while (resultat.next())
				{
					String ADRmail=resultat.getString("ADRmail");
					 try {
						 Properties props = new Properties();
				        
						props.setProperty("mail.transport.protocol", "smtp");     
					    props.setProperty("mail.host", "smtp.gmail.com");  
					    props.put("mail.smtp.auth", "true");  
					    props.put("mail.smtp.port", "465");  
					    props.put("mail.smtp.socketFactory.port", "465");  
					    props.put("mail.smtp.socketFactory.class","javax.net.ssl.SSLSocketFactory");  
					    props.put("mail.smtp.socketFactory.fallback", "false");
				        Session session = Session.getInstance(props,
				                new javax.mail.Authenticator() {
				                  protected PasswordAuthentication getPasswordAuthentication() {
				                      return new PasswordAuthentication("moha.rebai741@gmail.com", "moha74123");
				                  }
				                });
				        String contenu1="";
						String contenu2="";
						String contenu3="";
						String contenu4="";
						 int DebContenuindice=AnnIn.indexOf("##");
						 if (DebContenuindice != -1) 
						 {
							  contenu1= AnnIn.substring(0,DebContenuindice); 
							  contenu2= AnnIn.substring(DebContenuindice+2 ,AnnIn.length() ); 
							  int FinContenuindice=contenu2.indexOf("##");
							  contenu3=contenu2.substring(0,FinContenuindice);
							  contenu4=contenu2.substring(FinContenuindice+2,contenu2.length());
						 }
						String COntenuFinal="	"+contenu3+"\n"+contenu1+"\n"+contenu4;

				            Message message = new MimeMessage(session);
				            message.setFrom(new InternetAddress("moha.rebai741@gmail.com"));
				            message.setRecipients(Message.RecipientType.TO,InternetAddress.parse(ADRmail));

				            message.setSubject("Annonce Modifiée de l'Association Abed&Re");
				            message.setText("Bonjour,"
				                + "\n"+COntenuFinal+"Cordialemnt." );
				            Transport.send(message);
				        } catch (MessagingException e) {
							JOptionPane.showMessageDialog(null, "Echec de l'envoie.(Verifier que vous etes bien connecté a internet SVP)","Warning", JOptionPane.QUESTION_MESSAGE, new ImageIcon(GestionAnnonces.class.getResource("/messages_img/error.png")));
				        }
				
				}
				JOptionPane.showMessageDialog(null, "Annonce modifé avec succé!","Modification",JOptionPane.PLAIN_MESSAGE, new ImageIcon(AuthentificationFrame.class.getResource("/messages_img/stamp.png")));
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				JOptionPane.showMessageDialog(null, e);
			}
		
	}
	
	public void Supprimer (String ID)
	{
		cnx = ConnexionDB.CnxDB();
		
		String sql ="delete from Annonce where ID_Ann="+ID;
		try {
			prepared=cnx.prepareStatement(sql);
			prepared.execute();
			JOptionPane.showMessageDialog(null, "Annonce supprimé avec succès!","Suppression" ,JOptionPane.PLAIN_MESSAGE, new ImageIcon(AuthentificationFrame.class.getResource("/messages_img/stamp.png")));
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
	}
	
	
	//Main TEST 
	/*public static void main(String[] args) {
		Annonance A =new Annonance("Anniv", "30/01/2021");
		A.Supprimer("1");
		//A.Ajouter();
		//A.Modifier("1", "AnnivMod", "31/01/2021");
	}*/


}
