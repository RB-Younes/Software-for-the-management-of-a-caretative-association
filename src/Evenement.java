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

public class Evenement {
	
	Connection cnx=null;
	PreparedStatement prepared = null;
	ResultSet resultat =null;
	PreparedStatement prepared1 = null;
	ResultSet resultat1 =null;
	
	
	private String ID_ADH;
	private String NomEvent;
	private String ContenuEvet;
	private String HEvent;
	private String DateEvent;
	
	
	
	public Evenement(String iD_ADH, String nomEvent, String contenuEvet, String hEvent, String dateEvent) {
		super();
		ID_ADH = iD_ADH;
		NomEvent = nomEvent;
		ContenuEvet = contenuEvet;
		HEvent = hEvent;
		DateEvent = dateEvent;
	}

	public Evenement() {
		super();
	}

	public void Ajouter ()
	{
		cnx = ConnexionDB.CnxDB();
		String sql = "insert into Evenements values (ID_Event.nextval,'"+this.NomEvent+"','"+this.ContenuEvet+"','"+this.DateEvent+"','"+this.HEvent+"','"+this.ID_ADH+"')";
		try {
			prepared=cnx.prepareStatement(sql);
			prepared.executeQuery();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			JOptionPane.showMessageDialog(null, e);
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
					 int DebContenuindice=this.ContenuEvet.indexOf("##");
					 if (DebContenuindice != -1) 
					 {
						  contenu1= this.ContenuEvet.substring(0,DebContenuindice); 
						  contenu2= this.ContenuEvet.substring(DebContenuindice+2 ,this.ContenuEvet.length() ); 
						  int FinContenuindice=contenu2.indexOf("##");
						  contenu3=contenu2.substring(0,FinContenuindice);
						  contenu4=contenu2.substring(FinContenuindice+2,contenu2.length());
					 }
					String COntenuFinal="	"+contenu3+"\n"+contenu1+contenu4;

			            Message message = new MimeMessage(session);
			            message.setFrom(new InternetAddress("moha.rebai741@gmail.com"));
			            message.setRecipients(Message.RecipientType.TO,InternetAddress.parse(ADRmail));

			            message.setSubject("Evenement: "+this.NomEvent+" .");
			            message.setText("Bonjour,"
			                + "\n"+COntenuFinal+"\nCordialemnt." );
			            Transport.send(message);
			            
			        } catch (MessagingException e) {
						JOptionPane.showMessageDialog(null, "Echec de l'envoie.(Verifier que vous etes bien connecté a internet SVP)","Warning", JOptionPane.QUESTION_MESSAGE, new ImageIcon(GestionAnnonces.class.getResource("/messages_img/error.png")));
			        }
			
			}
			JOptionPane.showMessageDialog(null, "Evenement ajouté avc succés!","Ajout" ,JOptionPane.PLAIN_MESSAGE, new ImageIcon(AuthentificationFrame.class.getResource("/messages_img/stamp.png")));
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			JOptionPane.showMessageDialog(null, e);
		}
		
	}
	
	public void Modifer (String ID_Event,String ID_AD,String NomEvent,String EventIn ,String DateEvent,String DateMod,String HEvent)
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
			String sql2 = "Update Evenements set NomEvent =?,InEvent =?,DateEvent =?,HeureEvent =?,ID_AD=? where ID_Event='"+ID_Event+"'";
			prepared1=cnx.prepareStatement(sql2);
			
			EventIn=EventIn+"Evenement Modifié par :"+Nom+" "+Prenom+",le:"+DateMod+".\n";
			prepared1.setString(1, NomEvent);
			prepared1.setString(2, EventIn);	
			prepared1.setString(3, DateEvent);	
			prepared1.setString(4, HEvent);
			prepared1.setString(5, ID_AD);	
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
						 int DebContenuindice=EventIn.indexOf("##");
						 if (DebContenuindice != -1) 
						 {
							  contenu1= EventIn.substring(0,DebContenuindice); 
							  contenu2= EventIn.substring(DebContenuindice+2 ,EventIn.length() ); 
							  int FinContenuindice=contenu2.indexOf("##");
							  contenu3=contenu2.substring(0,FinContenuindice);
							  contenu4=contenu2.substring(FinContenuindice+2,contenu2.length());
						 }
						String COntenuFinal="	"+contenu3+"\n"+contenu1+"\n"+contenu4;

				            Message message = new MimeMessage(session);
				            message.setFrom(new InternetAddress("moha.rebai741@gmail.com"));
				            message.setRecipients(Message.RecipientType.TO,InternetAddress.parse(ADRmail));

				            message.setSubject("Evenement Modifié :"+NomEvent+" .");
				            message.setText("Bonjour,"
				                + "\n"+COntenuFinal+"Cordialemnt.");
				            Transport.send(message);
				        } catch (MessagingException e) {
							JOptionPane.showMessageDialog(null, "Echec de l'envoie.(Verifier que vous etes bien connecté a internet SVP)","Warning", JOptionPane.QUESTION_MESSAGE, new ImageIcon(GestionAnnonces.class.getResource("/messages_img/error.png")));
				        }
				
				}
				JOptionPane.showMessageDialog(null, "Evenement modifé avec succé!","Modification",JOptionPane.PLAIN_MESSAGE, new ImageIcon(AuthentificationFrame.class.getResource("/messages_img/stamp.png")));
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				JOptionPane.showMessageDialog(null, e);
			}
	}
	
	public void Supprimer (String ID, String iD_ADHERENT,String Date,String heure,String titre )
	{
		cnx = ConnexionDB.CnxDB();
		String Nom=null;
		String Prenom=null;
		String sql = "Select Nom,Prenom from Adherent where ID_AD='"+iD_ADHERENT+"'";
		try {
			prepared=cnx.prepareStatement(sql);
			resultat=prepared.executeQuery();
			if(resultat.next())
			{
				 Nom=resultat.getString("Nom");
				 Prenom=resultat.getString("Prenom");
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			JOptionPane.showMessageDialog(null, e);
		}
		 sql ="delete from Evenements where ID_Event="+ID;
		try {
			prepared=cnx.prepareStatement(sql);
			prepared.execute();
			JOptionPane.showMessageDialog(null, "Evenement supprimé avec succès!","Suppression" ,JOptionPane.PLAIN_MESSAGE, new ImageIcon(AuthentificationFrame.class.getResource("/messages_img/stamp.png")));
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			JOptionPane.showMessageDialog(null, e);
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
					    props.put("mail.smtp.socketFactory.port", "465");  
					    props.put("mail.smtp.socketFactory.class","javax.net.ssl.SSLSocketFactory");  
					    props.put("mail.smtp.socketFactory.fallback", "false");
				        Session session = Session.getInstance(props,
				                new javax.mail.Authenticator() {
				                  protected PasswordAuthentication getPasswordAuthentication() {
				                      return new PasswordAuthentication("moha.rebai741@gmail.com", "moha74123");
				                  }
				                });
				            Message message = new MimeMessage(session);
				            message.setFrom(new InternetAddress("moha.rebai741@gmail.com"));
				            message.setRecipients(Message.RecipientType.TO,InternetAddress.parse(ADRmail));

				            message.setSubject("Evenement :"+titre+" Supprimé.");
				            message.setText("Bonjour,"
				                + "\n L'evenement '"+ titre+ "' du: "+Date+" // "+heure+" ,a été supprimé par: "+Nom+" "+ Prenom+".\n Cordialemnt.");
				            Transport.send(message);
				        } catch (MessagingException e) {
							JOptionPane.showMessageDialog(null, "Echec de l'envoie.(Verifier que vous etes bien connecté a internet SVP)","Warning", JOptionPane.QUESTION_MESSAGE, new ImageIcon(GestionAnnonces.class.getResource("/messages_img/error.png")));
				        }
				
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				JOptionPane.showMessageDialog(null, e);
			}
		
	}

	
	

}


