import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.UIManager;

import com.formdev.flatlaf.FlatIntelliJLaf;

////////////////////////////////////////////////////////////////////////////////-----------Fenetre authentification------------///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

public class AuthentificationFrame extends JFrame{

	 
	private static final long serialVersionUID = 1L;
	private JFrame frmLoginMenu;
	private JTextField UserNameField;
	
	private JPasswordField passwordField;
	
	Connection cnx=null;
	PreparedStatement prepared = null;
	ResultSet resultat =null; 

	/*
	  Launch the application.
	 */
	public static void main(String[] args) throws Exception{
		FlatIntelliJLaf.install();	//application du Look&Feel plus Moderne 
		UIManager.setLookAndFeel(new FlatIntelliJLaf() );
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					AuthentificationFrame window = new AuthentificationFrame();
					window.frmLoginMenu.setVisible(true);
				} catch (Exception e) {
					JOptionPane.showMessageDialog(null,e);
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public AuthentificationFrame() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmLoginMenu = new JFrame();
		
		
		frmLoginMenu.setUndecorated(true);//enlever la bare de menu de base(et programmer nos propre button fermer et reduire)		
		frmLoginMenu.setTitle("Login Menu");
		frmLoginMenu.setIconImage(Toolkit.getDefaultToolkit().getImage(AuthentificationFrame.class.getResource("/Log_in_img/hand.png")));
		frmLoginMenu.setSize(1100, 650);
		frmLoginMenu.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		frmLoginMenu.getContentPane().setLayout(null);
		frmLoginMenu.setLocationRelativeTo(null);//localisation au centre de l'ecran
		
		cnx = ConnexionDB.CnxDB();
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////		
		// reduire bouton:	
		JButton Minimise_BTN = new JButton("");
		Minimise_BTN.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {//afficher une autre icone(rouge) a l'entree de la souris
				Minimise_BTN.setIcon(new ImageIcon(AuthentificationFrame.class.getResource("/Log_in_img/Minimize ML selected.png")));
			}
			@Override
			public void mouseExited(MouseEvent e) {// rendre l'icone de base
				Minimise_BTN.setIcon(new ImageIcon(AuthentificationFrame.class.getResource("/Log_in_img/Minimize ML .png")));
				
			}
		});
		Minimise_BTN.setToolTipText("Minimize");
		ButtonStyle(Minimise_BTN);
		Minimise_BTN.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				frmLoginMenu.setState(ICONIFIED);//reduire au clique
			}
		});
		// Exit bouton:		
		JButton Exit_BTN = new JButton("");
		Exit_BTN.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent arg0) {
				Exit_BTN.setIcon(new ImageIcon(AuthentificationFrame.class.getResource("/Log_in_img/Exit ML selected.png")));
			}
			@Override
			public void mouseExited(MouseEvent arg0) {
				Exit_BTN.setIcon(new ImageIcon(AuthentificationFrame.class.getResource("/Log_in_img/Exit ML.png")));
			}
		});
		Exit_BTN.setToolTipText("Exit");
		ButtonStyle(Exit_BTN);
		Exit_BTN.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
					int ClickedButton	=JOptionPane.showConfirmDialog(null, "Do you really want to leave?", "Close", JOptionPane.YES_NO_OPTION);//message de confirmation
					if(ClickedButton==JOptionPane.YES_OPTION)
					{
						UserNameField.setText("");// et vider les fields
						frmLoginMenu.dispose();// fermer la fenetre
					}
			}
			
		});
		Exit_BTN.setIcon(new ImageIcon(AuthentificationFrame.class.getResource("/Log_in_img/Exit ML.png")));
		Exit_BTN.setBounds(1058, 11, 32, 32);
		frmLoginMenu.getContentPane().add(Exit_BTN);
		Minimise_BTN.setIcon(new ImageIcon(AuthentificationFrame.class.getResource("/Log_in_img/Minimize ML .png")));
		Minimise_BTN.setBounds(1016, 11, 32, 32);
		frmLoginMenu.getContentPane().add(Minimise_BTN);
//Mot de passe indication ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////		
		// dans le cas ou un utulisateurs a oublié son mot de passe une fenetre d'aide s'affichera(ndication les 2 premiers caracteres du mdp) 
		JLabel Passindication = new JLabel("Forgot your password ?");
		Passindication.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				Indicationpass frame = new Indicationpass();
				frame.setLocationRelativeTo(null);
				frame.setVisible(true);
			}
			@Override
			public void mouseEntered(MouseEvent e) {
				Passindication.setForeground(Color.ORANGE);
			}
			@Override
			public void mouseExited(MouseEvent e) {
				Passindication.setForeground(new Color(255, 250, 205));
			}
		});
		Passindication.setBounds(219, 375, 162, 14);
		Passindication.setForeground(new Color(255, 250, 205));
		Passindication.setFont(new Font("Segoe UI", Font.BOLD, 13));
		frmLoginMenu.getContentPane().add(Passindication);
		
//Text Fields////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////		
		//Nom d'utilisateurs
		UserNameField = new JTextField();
		UserNameField.addFocusListener(new FocusAdapter() {
			@Override
			public void focusGained(FocusEvent e) {
				if (UserNameField.getText().toString().equals("Adresse Mail")) {
					UserNameField.setText("");
				}
			}
			@Override
			public void focusLost(FocusEvent e) {
				if (UserNameField.getText().toString().equals("")) {
					UserNameField.setText("Adresse Mail");
				}
			}
		});
		UserNameField.setForeground(new Color(128, 128, 128));
		UserNameField.setFont(new Font("Segoe UI", Font.BOLD, 14));
		UserNameField.setBackground(new Color(255, 250, 205));
		UserNameField.setBorder(null);
		UserNameField.setText("Adresse Mail");
		UserNameField.setBounds(96, 290, 270, 29);
		frmLoginMenu.getContentPane().add(UserNameField);
		UserNameField.setColumns(10);
		
		passwordField = new JPasswordField();
		passwordField.addFocusListener(new FocusAdapter() {
			@Override
			@SuppressWarnings("deprecation")
			public void focusGained(FocusEvent e) {
				if (passwordField.getText().toString().equals("XXXXXXXXXXXXXXX")) {
					passwordField.setText("");
				}
			}
			@Override
			@SuppressWarnings("deprecation")
			public void focusLost(FocusEvent e) {
				if (passwordField.getText().toString().equals("")) {
					passwordField.setText("XXXXXXXXXXXXXXX");
				}
			}
		});
		passwordField.setBorder(BorderFactory.createEmptyBorder());// <------------- l Noborders
		passwordField.setBackground(new Color(255, 250, 205));//--------------2 mettre une couleur
		passwordField.setForeground(new Color(128, 128, 128));
		passwordField.setFont(new Font("Segoe UI", Font.BOLD, 14));
		passwordField.setText("XXXXXXXXXXXXXXX");
		passwordField.setBounds(96, 342, 270, 29);
		frmLoginMenu.getContentPane().add(passwordField);
//CE connecter/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////		
		JButton connecter = new JButton("");
		connecter.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				connecter.setIcon(new ImageIcon(AuthentificationFrame.class.getResource("/Log_in_img/login selected 1.png")));
			}
			@Override
			public void mouseExited(MouseEvent e) {
				connecter.setIcon(new ImageIcon(AuthentificationFrame.class.getResource("/Log_in_img/log in.png")));
			}
		});
		ButtonStyle(connecter);
		connecter.setToolTipText("Log in ");
		connecter.setIcon(new ImageIcon(AuthentificationFrame.class.getResource("/Log_in_img/log in.png")));
		connecter.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				String ADR =UserNameField.getText().toString();//l'0 saisie
				@SuppressWarnings("deprecation")
				String password =passwordField.getText().toString();//le Mot de passe saisie
				
				if (ADR.equals("") || password.equals("") || ADR.equals("Adresse Mail") || password.equals("XXXXXXXXXXXXXXX")  )//dans le cas ou les deux champs sont vide(ou non changé) alors afficher un message 
				{
					JOptionPane.showMessageDialog(connecter, "Veuillez vous identifier !","Warning", JOptionPane.QUESTION_MESSAGE, new ImageIcon(AuthentificationFrame.class.getResource("/messages_img/error.png")));
					
				}	
				else	
				{
					LogIn L=new LogIn(ADR, password);
					String ID=L.SeConnecter();
							if (ID!="") //dans le cas ou c'est egale
								{//affichge de message
								JOptionPane.showMessageDialog(connecter, "successful login!","connexion" ,JOptionPane.PLAIN_MESSAGE, new ImageIcon(AuthentificationFrame.class.getResource("/messages_img/stamp.png")));
									//ouverture de la fenetre
								MenuPrincipaleFrame fenetre=new MenuPrincipaleFrame(ID);	
								fenetre.setVisible(true);
									frmLoginMenu.dispose();
								}
							else {//echec de l'identification
								JOptionPane.showMessageDialog(connecter, "Mot de passe ou Adresse mail erroné !","Warning", JOptionPane.QUESTION_MESSAGE, new ImageIcon(AuthentificationFrame.class.getResource("/messages_img/error.png")));

							}
						}
			}
		});
		connecter.setBounds(177, 393, 64, 64);
		frmLoginMenu.getContentPane().add(connecter);
		/*/**/
		/*/*//*ABC*/
		/*ABCGD*qdqsdqs******qdqsds*******************qjdfqfjq f*/
		/*****Z*sdkjfqjdf kqsjdfbd qdjfdsf ******qdsfqd qsdf**qdsfdfqdf/*qsdq/*****/
		/**********ZZE*/
		/***************/
		
		/*wcxxxxxxxxx/*qqsdqsd*/
		/*/*/
		
		
		/*/*//*ABC*/
		/*
		qsdq/******qsdqs*/
		
//animation///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////		
		JLabel lblanim1 = new JLabel("anim1");
		ImageIcon animation =new ImageIcon(AuthentificationFrame.class.getResource("/Log_in_img/Humanitarian Help Entrance.gif"));
		lblanim1.setIcon(animation);
		Timer chrono=new Timer();// on va utiliser un timer our attendre la fin de la premiere animation(d'entree qui dure 1.5 sec)apres lancer le looping
		chrono.schedule(new TimerTask() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				lblanim1.setIcon(new ImageIcon(AuthentificationFrame.class.getResource("/Log_in_img/Humanitarian Help Loop.gif")));
			}
		}, 1500);
		lblanim1.setBounds(599, 72, 475, 455);
		frmLoginMenu.getContentPane().add(lblanim1);
		
		
//BG//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		JLabel BGlogin = new JLabel("Banniere");
		BGlogin.setIcon(new ImageIcon(AuthentificationFrame.class.getResource("/Log_in_img/BG login J.png")));
		/*chrono.schedule(new TimerTask() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				BGlogin.setIcon(new ImageIcon(AuthentificationFrame.class.getResource("/Log_in_img/BG login J.png")));
			}
		}, 1500);*/
		
		BGlogin.setBounds(0, 0, 1100, 650);
		frmLoginMenu.getContentPane().add(BGlogin);
		
	}
//methode du style des buttons/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	 private void ButtonStyle(JButton btn) {
	//enlever les bordures des btn
	 btn.setFocusPainted(false);
	 btn.setBorderPainted(false);
	 btn.setContentAreaFilled(false);
	 btn.setContentAreaFilled(false); // On met à false pour empêcher le composant de peindre l'intérieur du JButton.
	/* btn.setBorderPainted(false); // De même, on ne veut pas afficher les bordures.
	 btn.setFocusPainted(false); // On n'affiche pas l'effet de focus*/
}
}
