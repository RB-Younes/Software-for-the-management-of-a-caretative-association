import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;
import java.util.Timer;
import java.util.TimerTask;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.border.EmptyBorder;

import com.formdev.flatlaf.FlatIntelliJLaf;


////////////////////////////////////////////////////////////////////////////////-----------Fenetre Mot de passe indication------------///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

public class Indicationpass extends JFrame {

	
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField SaisieCode;
	private JButton btnConfirmationCode;
	private JTextField ADRmailField;
	private JPasswordField passwordField;
	private JPasswordField passwordFieldCofirmation;
	private int Rand;
	private Boolean MailEnvoyé = false;
	private String ID;
	private JButton btnModificationP;
	
	Connection cnx=null;
	PreparedStatement prepared = null;
	ResultSet resultat =null; 
	
	private int posX = 0;   
    private int posY = 0;
   
   

	/**
	 * Launch the application.
	 * @throws UnsupportedLookAndFeelException 
	 */
	public static void main(String[] args) throws UnsupportedLookAndFeelException {
		FlatIntelliJLaf.install();	
		UIManager.setLookAndFeel(new FlatIntelliJLaf() );
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Indicationpass frame = new Indicationpass();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public Indicationpass() {
//FAIRE bouger la fenetre////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////		
		 addMouseListener(new MouseAdapter() {
	            @Override
	            //on recupere les coordonnées de la souris
	            public void mousePressed(MouseEvent e) {
	                posX = e.getX();    //Position X de la souris au clic
	                posY = e.getY();    //Position Y de la souris au clic
	            }
	        });
	         
	        addMouseMotionListener(new MouseMotionAdapter() {
	            // A chaque deplacement on recalcul le positionnement de la fenetre
	            @Override
	            public void mouseDragged(MouseEvent e) {
	                int depX = e.getX() - posX;
	                int depY = e.getY() - posY;
	                setLocation(getX()+depX, getY()+depY);
	            }
	        });
// initialisation///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////	
		setLocationRelativeTo(null);
		setUndecorated(true);
		
		setType(Type.POPUP);
		setResizable(false);
		setIconImage(Toolkit.getDefaultToolkit().getImage(Indicationpass.class.getResource("/Passeindixation_img/question.png")));
		setTitle("Forgot password ?");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 670, 400);
		
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		cnx = ConnexionDB.CnxDB();
// Exit bouton/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		
		JButton Exit_BTN = new JButton("");
		Exit_BTN.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent arg0) {
				Exit_BTN.setIcon(new ImageIcon(Indicationpass.class.getResource("/Log_in_img/Exit ML selected.png")));//changer les couleurs button
			}
			@Override
			public void mouseExited(MouseEvent arg0) {
				Exit_BTN.setIcon(new ImageIcon(Indicationpass.class.getResource("/Log_in_img/Exit ML.png")));//remetre le bouton de base
			}
		});
		Exit_BTN.setToolTipText("Exit");
		ButtonStyle(Exit_BTN);
		Exit_BTN.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
			
		});
		Exit_BTN.setIcon(new ImageIcon(Indicationpass.class.getResource("/Log_in_img/Exit ML.png")));
		Exit_BTN.setBounds(633, 5, 32, 32);
		contentPane.add(Exit_BTN);
//Envoyer Mail Bouton////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////		
		JButton btnSendMail = new JButton("");
		ButtonStyle(btnSendMail);
		btnSendMail.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent arg0) {
				btnSendMail.setIcon(new ImageIcon(Indicationpass.class.getResource("/Passeindixation_img/send in.png")));
			}
			@Override
			public void mouseExited(MouseEvent e) {
				btnSendMail.setIcon(new ImageIcon(Indicationpass.class.getResource("/Passeindixation_img/send.png")));
			}
		});
		btnSendMail.setIcon(new ImageIcon(Indicationpass.class.getResource("/Passeindixation_img/send.png")));
		btnSendMail.setToolTipText("Envoyer le code a l'adresse mail ");
		btnSendMail.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
			if (ADRmailField.getText().toString().equals("Adresse Mail de Recuperation")) {
				JOptionPane.showMessageDialog(null, "Veuiller Saisir votre adresse mail de recuperation!","Warning", JOptionPane.QUESTION_MESSAGE, new ImageIcon(Indicationpass.class.getResource("/messages_img/error.png")));
			} 
			else {
				 String ADR =ADRmailField.getText().toString();//ADR dans notre table doit etre NOT NULL and UNIQUE
					
					// vRetireer le ID  de l'utilisateur
					String sql = "select ID_Users from Users where ADRMAIL = ?";// le champ vide == ?
					
					try {
						prepared = cnx.prepareStatement(sql);
						prepared.setString(1, ADR); // le 1 fait referance au premier "?"
						resultat= prepared.executeQuery();
						if (resultat.next())//on envoie le mail avec le code de recuperation.
						{
							ID=resultat.getString("ID_Users");
							 try {
								 Properties props = new Properties();
								/*props.put("mail.smtp.auth", "true");
						        props.put("mail.smtp.starttls.enable", "true");
						        props.put("mail.smtp.host", "smtp.gmail.com");
						        props.put("mail.smtp.port", "465");*/
						        
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
						       

						            Message message = new MimeMessage(session);
						            message.setFrom(new InternetAddress("moha.rebai741@gmail.com"));
						            message.setRecipients(Message.RecipientType.TO,
						                InternetAddress.parse(ADRmailField.getText()));
						           Rand= (int) (100000 * Math.random());//random cde
						           System.out.println(Rand);
						            message.setSubject("Code de recuperation Compte Association");
						            message.setText("Bonjour,"
						                + "\n\n Votre code derecuperation est :"+Rand+".\n");

						            Transport.send(message);
						            MailEnvoyé=true;//pour savoir que le mail a bien ete envoye
						          

						        } catch (MessagingException e) {
						        	  MailEnvoyé=false;//le mail n'est pas envoyé
									JOptionPane.showMessageDialog(null, "Echec de l'envoie.\nVeuillez Saisir une Adresse mail Valide !\n(Verifier votre Connexion internet SVP)","Warning", JOptionPane.QUESTION_MESSAGE, new ImageIcon(Indicationpass.class.getResource("/messages_img/error.png")));
						        }
							 if (MailEnvoyé==true) {
								 	SaisieCode.setEnabled(true);
								 	btnConfirmationCode.setEnabled(true);
									JOptionPane.showMessageDialog(null, "Code envoyé avec succé!","Envoie de Code",JOptionPane.PLAIN_MESSAGE, new ImageIcon(Indicationpass.class.getResource("/messages_img/stamp.png")));

							}
						
						}
						else {
							JOptionPane.showMessageDialog(null, "Echec de l'envoie.\n Adresse mail invalide ou inexistante !\n(Verifier votre Connexion internet SVP)","Warning", JOptionPane.QUESTION_MESSAGE, new ImageIcon(Indicationpass.class.getResource("/messages_img/error.png")));
						}
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
			}		 
					
					
			  
			}
		});
		btnSendMail.setBounds(496, 160, 32, 32);
		contentPane.add(btnSendMail);
//btn confirmation du code///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////		
		btnConfirmationCode = new JButton("");
		ButtonStyle(btnConfirmationCode);
		btnConfirmationCode.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				if (btnConfirmationCode.isEnabled()) {
						btnConfirmationCode.setIcon(new ImageIcon(Indicationpass.class.getResource("/Passeindixation_img/check in.png")));
				}
			}
			@Override
			public void mouseExited(MouseEvent e) {
				if (btnConfirmationCode.isEnabled()) {
					btnConfirmationCode.setIcon(new ImageIcon(Indicationpass.class.getResource("/Passeindixation_img/check.png")));
				}
			}
		});
		btnConfirmationCode.setIcon(new ImageIcon(Indicationpass.class.getResource("/Passeindixation_img/check.png")));
		btnConfirmationCode.setToolTipText("Confirmer Code");
		btnConfirmationCode.setEnabled(false);
		btnConfirmationCode.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(SaisieCode.getText().equals("") || SaisieCode.getText().equals("Saisir Code")){
					JOptionPane.showMessageDialog(null, "Saisissez le code svp!","Warning", JOptionPane.QUESTION_MESSAGE, new ImageIcon(Indicationpass.class.getResource("/messages_img/error.png")));
				}
				else {
					if (Integer.parseInt(SaisieCode.getText())==Rand && MailEnvoyé==true)  {
					passwordFieldCofirmation.setEnabled(true);// && !SaisieCode.getText().toString().equals("Saisir Code")
					passwordField.setEnabled(true);
					btnModificationP.setEnabled(true);
					JOptionPane.showMessageDialog(null, "Code Correct \n Vous pouvez Modifier votre mot de passe!","Code Correct",JOptionPane.PLAIN_MESSAGE, new ImageIcon(Indicationpass.class.getResource("/messages_img/stamp.png")));
					
				}
				else {
					JOptionPane.showMessageDialog(null, "Code Erroné!\nVeuillez Resaisir le code correctement!","Warning", JOptionPane.QUESTION_MESSAGE, new ImageIcon(Indicationpass.class.getResource("/messages_img/error.png")));
					}
				}
				
			}
		});
		btnConfirmationCode.setBounds(475, 236, 32, 32);
		contentPane.add(btnConfirmationCode);
//btn modification mot de passe///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////		
		btnModificationP = new JButton("");
		ButtonStyle(btnModificationP);
		btnModificationP.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				if (btnModificationP.isEnabled()) {
					btnModificationP.setIcon(new ImageIcon(Indicationpass.class.getResource("/Passeindixation_img/pencil.png")));
				}
			}
			@Override
			public void mouseExited(MouseEvent e) {
				if (btnModificationP.isEnabled()) {
						btnModificationP.setIcon(new ImageIcon(Indicationpass.class.getResource("/Passeindixation_img/pencil.png")));
				}
			}
		});
		btnModificationP.setIcon(new ImageIcon(Indicationpass.class.getResource("/Passeindixation_img/pencil.png")));
		btnModificationP.setToolTipText("Modifier Mote de passe");
		btnModificationP.setEnabled(false);
		btnModificationP.addActionListener(new ActionListener() {
			@SuppressWarnings("deprecation")
			public void actionPerformed(ActionEvent arg0) {
				if ((passwordField.getText().toString().equals("") || passwordFieldCofirmation.getText().toString().equals("")) || (passwordField.getText().toString().equals("XXXXXXXXXXXXXXX") || passwordFieldCofirmation.getText().toString().equals("XXXXXXXXXXXXXXX"))) {
					JOptionPane.showMessageDialog(null, "Veuillez Saisir le mot de passe et sa confirmation!","Warning", JOptionPane.QUESTION_MESSAGE, new ImageIcon(Indicationpass.class.getResource("/messages_img/error.png")));
				}
				else {
					if (passwordField.getText().toString().equals(passwordFieldCofirmation.getText().toString()) ) {
						String sql = "Update Users set password  =? where ID_Users="+ID+"";
						try {
							
							prepared=cnx.prepareStatement(sql);
							prepared.setString(1, passwordField.getText().toString());		
							prepared.execute();
							JOptionPane.showMessageDialog(null, "Mot de passe modifé avec succé!","Modification mot de passe",JOptionPane.PLAIN_MESSAGE, new ImageIcon(Indicationpass.class.getResource("/messages_img/stamp.png")));
							
						} catch (SQLException e) {
							// TODO Auto-generated catch block
							JOptionPane.showMessageDialog(null, e);
						}
					}
					else { 
						JOptionPane.showMessageDialog(null, "La confirmation du mot de passe est incorrect!","Warning", JOptionPane.QUESTION_MESSAGE, new ImageIcon(Indicationpass.class.getResource("/messages_img/error.png")));
						}
				}
				
					
			}
		});
		btnModificationP.setBounds(582, 297, 64, 64);
		contentPane.add(btnModificationP);
		
// Fields //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		ADRmailField = new JTextField();
		ADRmailField.addFocusListener(new FocusAdapter() {
			@Override
			public void focusGained(FocusEvent arg0) {// vider le textField
				if (ADRmailField.getText().toString().equals("Adresse Mail de Recuperation")) {
					ADRmailField.setText("");
				}
			}
			@Override
			public void focusLost(FocusEvent e) {
				if (ADRmailField.getText().toString().equals("")) {
					ADRmailField.setText("Adresse Mail de Recuperation");
				}
			}
		});
		ADRmailField.setForeground(new Color(128, 128, 128));
		ADRmailField.setFont(new Font("Segoe UI", Font.BOLD, 14));
		ADRmailField.setBackground(new Color(255, 250, 205));
		ADRmailField.setBorder(null);
		ADRmailField.setText("Adresse Mail de Recuperation");
		ADRmailField.setBounds(408, 123, 238, 29);
		contentPane.add(ADRmailField);
		ADRmailField.setColumns(10);
		
		SaisieCode = new JTextField();
		SaisieCode.addFocusListener(new FocusAdapter() {
			@Override
			public void focusGained(FocusEvent e) {
				if (SaisieCode.getText().toString().equals("Saisir Code") && SaisieCode.isEnabled() ) {
					SaisieCode.setText("");
				}
			}
			@Override
			public void focusLost(FocusEvent arg0) {
				if (SaisieCode.getText().toString().equals("") ) {
					SaisieCode.setText("Saisir Code");
				}
			}
		});
		SaisieCode.setEnabled(false);
		SaisieCode.setText("Saisir Code");
		SaisieCode.addKeyListener(new KeyAdapter() {
		    public void keyTyped(KeyEvent e) { 
		        if (		SaisieCode.getText().length() >= 10 ) //limiter le nombre de cactere a 10
		            e.consume(); 
		    }  
		});
		SaisieCode.addKeyListener(new KeyAdapter() {
		    public void keyTyped(KeyEvent e) {
		      char c = e.getKeyChar();
		      if (!((c >= '0') && (c <= '9') ||
		         (c == KeyEvent.VK_BACK_SPACE) ||
		         (c == KeyEvent.VK_DELETE))) {
		        getToolkit().beep();
		        e.consume();
		      }
		    }
		  });
		SaisieCode.setForeground(Color.GRAY);
		SaisieCode.setFont(new Font("Segoe UI", Font.BOLD, 14));
		SaisieCode.setColumns(10);
		SaisieCode.setBorder(null);
		SaisieCode.setBackground(new Color(255, 250, 205));
		SaisieCode.setBounds(385, 203, 238, 29);
		contentPane.add(SaisieCode);
		
		passwordField = new JPasswordField();
		passwordField.addFocusListener(new FocusAdapter() {
			@SuppressWarnings("deprecation")
			@Override
			public void focusGained(FocusEvent e) {
				if (passwordField.getText().toString().equals("XXXXXXXXXXXXXXX")&& passwordField.isEnabled()) {
					passwordField.setText("");
				}
			}
			@SuppressWarnings("deprecation")
			@Override
			public void focusLost(FocusEvent e) {
				if (passwordField.getText().toString().equals("")) {
					passwordField.setText("XXXXXXXXXXXXXXX");
				}
			}
		});
		passwordField.setEnabled(false);
		passwordField.setBorder(BorderFactory.createEmptyBorder());
		passwordField.setBackground(new Color(255, 250, 205));
		passwordField.setForeground(new Color(128, 128, 128));
		passwordField.setFont(new Font("Segoe UI", Font.BOLD, 14));
		passwordField.setText("XXXXXXXXXXXXXXX");
		passwordField.setBounds(279, 295, 270, 29);
		contentPane.add(passwordField);
		
		passwordFieldCofirmation = new JPasswordField();
		passwordFieldCofirmation.addFocusListener(new FocusAdapter() {
			@SuppressWarnings("deprecation")
			@Override
			public void focusGained(FocusEvent e) {
				if (passwordFieldCofirmation.getText().toString().equals("XXXXXXXXXXXXXXX")&& passwordField.isEnabled()) {
					passwordFieldCofirmation.setText("");
				}
			}
			@SuppressWarnings("deprecation")
			@Override
			public void focusLost(FocusEvent e) {
				if (passwordFieldCofirmation.getText().toString().equals("")) {
					passwordFieldCofirmation.setText("XXXXXXXXXXXXXXX");
				}
			}
		});
		passwordFieldCofirmation.setEnabled(false);
		passwordFieldCofirmation.setText("XXXXXXXXXXXXXXX");
		passwordFieldCofirmation.setForeground(Color.GRAY);
		passwordFieldCofirmation.setFont(new Font("Segoe UI", Font.BOLD, 14));
		passwordFieldCofirmation.setBorder(BorderFactory.createEmptyBorder());
		passwordFieldCofirmation.setBackground(new Color(255, 250, 205));
		passwordFieldCofirmation.setBounds(279, 335, 270, 29);
		contentPane.add(passwordFieldCofirmation);
	//label pour contenr une nimation
		JLabel lblanimtion = new JLabel("Animation");
		lblanimtion.setIcon(new ImageIcon(Indicationpass.class.getResource("/Passeindixation_img/Questions.gif")));	
		ImageIcon animationbtn1 =new ImageIcon(Indicationpass.class.getResource("/Passeindixation_img/Questions.gif"));	//animation
		animationbtn1.getImage().flush(); // reinitialiser the  animation
		Timer chrono=new Timer();// on va utiliser un timer our attendre la fin de la premiere animation(d'entree qui dure 1.5 sec)apres lancer le looping
		chrono.schedule(new TimerTask() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				lblanimtion.setIcon(new ImageIcon(AuthentificationFrame.class.getResource("/Passeindixation_img/Questions (1).gif")));
			}
		}, 1500);
		lblanimtion.setBounds(0, 76, 238, 248);
		contentPane.add(lblanimtion);
//le background////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		JLabel lblBG = new JLabel("BG");
		lblBG.setIcon(new ImageIcon(Indicationpass.class.getResource("/Passeindixation_img/mot de passe recup BG.png")));
		lblBG.setBounds(0, 0, 680, 400);
		contentPane.add(lblBG);
		
		
	}
//methode du style des buttons/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	 private void ButtonStyle(JButton btn) {
	//enlever les bordures des btn
	 btn.setOpaque(false);
	 btn.setFocusPainted(false);
	 btn.setBorderPainted(false);
	 btn.setContentAreaFilled(false);
	 
	 
	
}
}
