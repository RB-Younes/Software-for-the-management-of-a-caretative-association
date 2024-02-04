import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.geom.RoundRectangle2D;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.util.Date;
import java.util.TimerTask;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.Timer;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import com.formdev.flatlaf.FlatIntelliJLaf;

////////////////////////////////////////////////////////////////////////////////-----------Fenetre Menu Principale------------///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

public class MenuPrincipaleFrame extends JFrame {
	

	private static final long serialVersionUID = 1L;

	//protected static final String ID_Med = null;
	
	Connection cnx=null;
	PreparedStatement prepared = null;
	ResultSet resultat =null; 
	
	private int posX = 0;   //Position X de la souris au clic
    private int posY = 0;   //Position Y de la souris au clic
    

	private JPanel contentPane;
	
	//les bouttons de droite Reglement File Actu Menu Pricipale
	private JButton btnReg ;
	private JButton btnFA;
	private JButton btnMP;
	
	private JButton btnGSTANN;
	private JButton btnGSTADH;
	private JButton btnSTATCOV;
	private JButton btnGDPN; 
	private JButton btnGDE;
	private JButton btnGDF;
	private JButton btnGDS;
	private JButton btnGDC;
	
	private String Fonction;
	
	private String SelectD="MP";//Sauvgarder Quel bouton des boutons de Droite a été cliqué (pour codifier les couleurs des boutons)
	
	//declaration des timers d'animation
	private java.util.Timer chrono1 = new java.util.Timer();//du bouton menu pricipale
	private java.util.Timer chrono2 = new java.util.Timer();//du bouton file'd'actualité
	private java.util.Timer chrono3 = new java.util.Timer();//du bouton reglement
	private JTextArea textAreaAnn_Event;
	private JScrollPane scrollpane; 

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
					MenuPrincipaleFrame frame = new MenuPrincipaleFrame("ReMo1");
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
	public MenuPrincipaleFrame(String ID_ADEHRENT) {
		setIconImage(Toolkit.getDefaultToolkit().getImage(MenuPrincipaleFrame.class.getResource("/Menu_Principale_BTNimg/support.png")));
		//cnx
		cnx = ConnexionDB.CnxDB(); 
		
		String sql="Select code_F from Adherent where ID_AD='"+ID_ADEHRENT+"'";//Selectionner toute les info du Patient aec l'ID selectionné de la bdd
		
		try {
			prepared=cnx.prepareStatement(sql);
			resultat=prepared.executeQuery();
			if(resultat.next())
			{
				 sql="Select NomF from Fonction where Code_F='"+resultat.getString("Code_F")+"'";
				prepared=cnx.prepareStatement(sql);
				resultat=prepared.executeQuery();
				if(resultat.next())
				{
					Fonction=resultat.getString("NomF");
				}
			}
		} catch (SQLException e) {
		// TODO Auto-generated catch block
		JOptionPane.showMessageDialog(null,e);
		}
		
		

		setUndecorated(true);	
		setResizable(false);
		setTitle("Menu Principale");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(0, 0, 1100, 650);
		setShape(new RoundRectangle2D.Double(0d, 0d, 1100d, 650d, 25d, 25d));
		setLocationRelativeTo(null);
		//vu que la frame est Undecorated on a besoin de ces MouseListeners pour la faire bouger(frame)
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
	        
	        
		contentPane = new JPanel();
		setContentPane(contentPane);
		contentPane.setLayout(null);
//declaration du BG//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////		
		JLabel BGMP = new JLabel("");
		BGMP.setIcon(new ImageIcon(MenuPrincipaleFrame.class.getResource("/Menu_Principale_img/1.png")));
		
// le BG et lannimation////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		
		JLabel animation1 = new JLabel("");
		animation1.setIcon(new ImageIcon(MenuPrincipaleFrame.class.getResource("/Menu_Principale_gif/Team spirit.gif")));//animation de base
		animation1.setBounds(687, 220, 370, 337);
		contentPane.add(animation1);
//gestion des utilisateurs///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////		
		JButton GestionUtilisateurs = new JButton("");
		GestionUtilisateurs.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (Fonction.equals("Président")) {
					GestionUtiMenu frame = new GestionUtiMenu(ID_ADEHRENT);
					frame.setVisible(true);
					frame.setLocationRelativeTo(null);
				}
				else {
			    	   JOptionPane.showMessageDialog(null, "Accès restreint !","Warning", JOptionPane.QUESTION_MESSAGE, new ImageIcon(GestionAnnonces.class.getResource("/messages_img/banned.png")));
				}
				
			}
		});
		GestionUtilisateurs.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent arg0) {
				GestionUtilisateurs.setIcon(new ImageIcon(MenuPrincipaleFrame.class.getResource("/Menu_Principale_img/reglage selected.png")));
			}
			@Override
			public void mouseExited(MouseEvent e) {
				GestionUtilisateurs.setIcon(new ImageIcon(MenuPrincipaleFrame.class.getResource("/Menu_Principale_img/reglage.png")));
			}
		});
		GestionUtilisateurs.setIcon(new ImageIcon(MenuPrincipaleFrame.class.getResource("/Menu_Principale_img/reglage.png")));
		GestionUtilisateurs.setToolTipText("Gestion des utilisateurs & Association");
		GestionUtilisateurs.setOpaque(false);
		GestionUtilisateurs.setFocusPainted(false);
		GestionUtilisateurs.setContentAreaFilled(false);
		GestionUtilisateurs.setBorderPainted(false);
		GestionUtilisateurs.setBounds(1015, 11, 32, 32);
		contentPane.add(GestionUtilisateurs);
				
// Bouton Reduire ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		JButton Minimise_BTN = new JButton("");
		Minimise_BTN.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				Minimise_BTN.setIcon(new ImageIcon(MenuPrincipaleFrame.class.getResource("/Menu_Principale_img/Minimize ML selected.png")));
			}
			@Override
			public void mouseExited(MouseEvent e) {
				Minimise_BTN.setIcon(new ImageIcon(MenuPrincipaleFrame.class.getResource("/Menu_Principale_img/Minimize ML .png")));
			}
		});
		Minimise_BTN.setToolTipText("Minimize");
		ButtonStyle(Minimise_BTN);
		Minimise_BTN.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setState(ICONIFIED);
				
			}
		});
		Minimise_BTN.setIcon(new ImageIcon(MenuPrincipaleFrame.class.getResource("/Menu_Principale_img/Minimize ML .png")));
		Minimise_BTN.setBounds(973, 11, 32, 32);
		contentPane.add(Minimise_BTN);
// Exit bouton//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		
		JButton Exit_BTN = new JButton("");
		Exit_BTN.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent arg0) {
				Exit_BTN.setIcon(new ImageIcon(MenuPrincipaleFrame.class.getResource("/Menu_Principale_img/Exit ML selected.png")));
			}
			@Override
			public void mouseExited(MouseEvent arg0) {
				Exit_BTN.setIcon(new ImageIcon(MenuPrincipaleFrame.class.getResource("/Menu_Principale_img/Exit ML.png")));
			}
		});
		Exit_BTN.setToolTipText("Exit");
		ButtonStyle(Exit_BTN);
		Exit_BTN.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			
				int ClickedButton	=JOptionPane.showConfirmDialog(null, "Do you really want to leave?", "Close", JOptionPane.YES_NO_OPTION);
				if(ClickedButton==JOptionPane.YES_OPTION)
				{
					dispose();
				}
			}
			
		});
		Exit_BTN.setIcon(new ImageIcon(MenuPrincipaleFrame.class.getResource("/Menu_Principale_img/Exit ML.png")));
		Exit_BTN.setBounds(1057, 11, 32, 32);
		contentPane.add(Exit_BTN);
//Bouton MP/reglement/Actu////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		btnMP= new JButton("Menu Principale");
		btnMP.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				if (!SelectD.equals("MP")) {
					btnMP.setForeground(Color.WHITE);
				}
				
			}
			@Override
			public void mouseExited(MouseEvent e) {
				if (!SelectD.equals("MP")) {
					btnMP.setForeground(Color.GRAY);
				}
				
			}
		});
		btnMP.setToolTipText("menu principale");
		btnMP.setForeground(Color.WHITE);
		btnMP.setFont(new Font("Microsoft PhagsPa", Font.BOLD, 16));
		btnMP.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				scrollpane.setVisible(false);
				textAreaAnn_Event.setVisible(false);
				BGMP.setIcon(new ImageIcon(MenuPrincipaleFrame.class.getResource("/Menu_Principale_img/1.png")));
				ImageIcon animationbtnMP =new ImageIcon(MenuPrincipaleFrame.class.getResource("/Menu_Principale_gif/Team spirit.gif"));//animation Menu principale
				animationbtnMP.getImage().flush(); // réinitialiser l'animation
				animation1.setIcon(animationbtnMP);
				
				//decaler l'animation
				animation1.setBounds(687, 216, 370, 337);
				//Monter la position du bouton cliqueé
				btnMP.setBounds(15, 605, 193, 38);
				//desendre les autres boutons
				btnFA.setBounds(218, 610, 193, 38);
				btnReg.setBounds(421, 610, 193, 38);
				
				SelectD="MP";//selection du Menu principale
				btnFA.setForeground(Color.GRAY);
				btnReg.setForeground(Color.GRAY);
				
				chrono2.cancel();
				chrono3.cancel();
				//rendre les boutons du menu principale invisible
				  btnGSTANN.setVisible(true);
				  btnGSTADH.setVisible(true);
				  btnSTATCOV.setVisible(true);
				  btnGDPN.setVisible(true);
				  btnGDE.setVisible(true);
				  btnGDF.setVisible(true);
				  btnGDS.setVisible(true);
				  btnGDC.setVisible(true);
			}
		});
		btnMP.setBounds(15, 605, 193, 38);
		ButtonStyle(btnMP);
		contentPane.add(btnMP);
		
		btnFA = new JButton("File d'actualité");
		btnFA.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				if (!SelectD.equals("FA")) {
					btnFA.setForeground(Color.WHITE);
				}
			}
			@Override
			public void mouseExited(MouseEvent e) {
				if (!SelectD.equals("FA")) {
					btnFA.setForeground(Color.GRAY);
				}
			}
		});
		btnFA.setToolTipText("file d'actualité");
		btnFA.setForeground(Color.GRAY);
		btnFA.setFont(new Font("Microsoft PhagsPa", Font.BOLD, 16));
		btnFA.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				textAreaAnn_Event.setVisible(true);
				textAreaAnn_Event.setForeground(new Color(255, 51, 51));
				Annonces(textAreaAnn_Event);
				scrollpane.setVisible(true);
				BGMP.setIcon(new ImageIcon(MenuPrincipaleFrame.class.getResource("/Menu_Principale_img/File d%27annonce.png")));
				ImageIcon animationbtnMP =new ImageIcon(MenuPrincipaleFrame.class.getResource("/Menu_Principale_gif/Taking notes.gif"));//animation File actu
				animationbtnMP.getImage().flush(); // réinitialiser l'animation
				animation1.setIcon(animationbtnMP);
				
				//decaler l'animation
				animation1.setBounds(663, 218, 370, 337);
				//Monter la position du bouton cliqueé
				btnFA.setBounds(218, 605, 193, 38);
				//desendre les autres boutons
				btnReg.setBounds(421, 610, 193, 38);
				btnMP.setBounds(15, 610, 193, 38);
				
				SelectD="FA";//selection de la file d'actu
				btnMP.setForeground(Color.GRAY);
				btnReg.setForeground(Color.GRAY);
				
				//arreter les autres timers 
				chrono1.cancel();
				chrono3.cancel();				
				//Loop d'animation avec un timer
				chrono2 =new java.util.Timer();
				chrono2.schedule(new TimerTask() {
					@Override
					public void run() {
							  animation1.setIcon(new ImageIcon(AuthentificationFrame.class.getResource("/Menu_Principale_gif/Taking notes Loop.gif")));
					}
				}, 1500);
				//rendre les boutons du menu principale invisible
				  btnGSTANN.setVisible(false);
				  btnGSTADH.setVisible(false);
				  btnSTATCOV.setVisible(false);
				  btnGDPN.setVisible(false);
				  btnGDE.setVisible(false);
				  btnGDF.setVisible(false);
				  btnGDS.setVisible(false);
				  btnGDC.setVisible(false);
				
			}
		});
		btnFA.setBounds(218, 610, 193, 38);
		ButtonStyle(btnFA);
		contentPane.add(btnFA);
		
		btnReg = new JButton("Règlement");
		btnReg.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				if (!SelectD.equals("Reg")) {
					
				}
				btnReg.setForeground(Color.WHITE);
			}
			@Override
			public void mouseExited(MouseEvent e) {
				if (!SelectD.equals("Reg")) {
					btnReg.setForeground(Color.GRAY);
				}
			}
		});
		btnReg.setToolTipText("règlement");
		btnReg.setForeground(Color.GRAY);
		btnReg.setFont(new Font("Microsoft PhagsPa", Font.BOLD, 16));
		btnReg.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				textAreaAnn_Event.setVisible(true);
				textAreaAnn_Event.setText("Reglement Interne\n ");
				textAreaAnn_Event.setForeground(new Color(0, 153, 51));
				scrollpane.setVisible(true);
				BGMP.setIcon(new ImageIcon(MenuPrincipaleFrame.class.getResource("/Menu_Principale_img/Règlement.png")));
				ImageIcon animationbtnMP =new ImageIcon(MenuPrincipaleFrame.class.getResource("/Menu_Principale_gif/Justice.gif"));//animation reglement
				animationbtnMP.getImage().flush(); // réinitialiser l'animation
				animation1.setIcon(animationbtnMP);
				
				//decaler l'animation
				animation1.setBounds(663, 218, 370, 337);
				
				//Monter la position du bouton cliqueé
				btnReg.setBounds(421, 605, 193, 38);
				//desendre les autres boutons
				btnFA.setBounds(218, 610, 193, 38);
				btnMP.setBounds(15, 610, 193, 38);
				
				SelectD="Reg";//selection du Reglement
				btnMP.setForeground(Color.GRAY);
				btnFA.setForeground(Color.GRAY);
				
				//arreter les autres timers
				chrono1.cancel();
				chrono2.cancel();
				//Loop d'animation avec un timer
				chrono3 =new java.util.Timer();
				chrono3.schedule(new TimerTask() {
					@Override
					public void run() {
							animation1.setIcon(new ImageIcon(AuthentificationFrame.class.getResource("/Menu_Principale_gif/Justice Loop.gif")));
					}
				}, 1500);
				//rendre les boutons du menu principale invisible
				  btnGSTANN.setVisible(false);
				  btnGSTADH.setVisible(false);
				  btnSTATCOV.setVisible(false);
				  btnGDPN.setVisible(false);
				  btnGDE.setVisible(false);
				  btnGDF.setVisible(false);
				  btnGDS.setVisible(false);
				  btnGDC.setVisible(false);
			}
		});
		btnReg.setBounds(421, 610, 194, 38);
		ButtonStyle(btnReg);
		contentPane.add(btnReg);		
//afficher  ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////	

			//lheure---------------------------------------------------------------------
			 JLabel horloge = new JLabel();
			 horloge.setForeground(Color.GRAY);
			 horloge.setBounds(791, 11, 162, 21);
	         horloge.setHorizontalAlignment(JLabel.CENTER);
	         horloge.setFont(
	           new Font("Segoe UI", Font.BOLD, 13)
	         );
	         horloge.setText(
	           DateFormat.getDateTimeInstance().format(new Date())
	         );
	         getContentPane().add(horloge);
	         Timer t = new Timer(500, new ActionListener() {
	            @Override
	            public void actionPerformed(ActionEvent e) {
	              horloge.setText(
	                 DateFormat.getDateTimeInstance().format(new Date())
	              );
	            }
	         });
	         t.setRepeats(true);
	         t.setCoalesce(true);
	         t.setInitialDelay(0);
	         t.start();
		
//menu principale boutins////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////	         
		
		btnGSTANN = new JButton("");
		btnGSTANN.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (Fonction.equals("Président")) {
					GestionAnnonces frame = new GestionAnnonces(ID_ADEHRENT);
					frame.setLocationRelativeTo(null);
					frame.setVisible(true);
					dispose();
				}
				else {
			    	   JOptionPane.showMessageDialog(null, "Accès restreint !","Warning", JOptionPane.QUESTION_MESSAGE, new ImageIcon(GestionAnnonces.class.getResource("/messages_img/banned.png")));
				}
				
			}
		});
		btnGSTANN.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent arg0) {
				BGMP.setIcon(new ImageIcon(MenuPrincipaleFrame.class.getResource("/Menu_Principale_img/2.png")));
				ImageIcon animationbtn1 =new ImageIcon(MenuPrincipaleFrame.class.getResource("/Menu_Principale_BTNgif/Mobile Marketing.gif")); 
				animationbtn1.getImage().flush(); // réinitialiser l'animation
				animation1.setIcon(animationbtn1);
			}
			@Override
			public void mouseExited(MouseEvent e) {
				BGMP.setIcon(new ImageIcon(MenuPrincipaleFrame.class.getResource("/Menu_Principale_img/1.png")));
				ImageIcon animationbtn1 =new ImageIcon(MenuPrincipaleFrame.class.getResource("/Menu_Principale_gif/Team spirit.gif")); 
				animationbtn1.getImage().flush(); 
				animation1.setIcon(animationbtn1);
			}
		});
		btnGSTANN.setToolTipText("Gestion des annonces");
		btnGSTANN.setBounds(46, 29, 128, 128);
		ButtonStyle(btnGSTANN);
		contentPane.add(btnGSTANN);
		
		btnGSTADH = new JButton("");
		btnGSTADH.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (Fonction.equals("Président")) {
					GestionAD frame = new GestionAD(ID_ADEHRENT);
					frame.setLocationRelativeTo(null);
					frame.setVisible(true);
					dispose();
				}
				else {
			    	   JOptionPane.showMessageDialog(null, "Accès restreint !","Warning", JOptionPane.QUESTION_MESSAGE, new ImageIcon(GestionAnnonces.class.getResource("/messages_img/banned.png")));
				}
			}
		});
		btnGSTADH.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent arg0) {
				BGMP.setIcon(new ImageIcon(MenuPrincipaleFrame.class.getResource("/Menu_Principale_img/3.png")));
				ImageIcon animationbtn2 =new ImageIcon(MenuPrincipaleFrame.class.getResource("/Menu_Principale_BTNgif/Add User.gif")); 
				animationbtn2.getImage().flush(); 
				animation1.setIcon(animationbtn2);
			}
			@Override
			public void mouseExited(MouseEvent e) {
				BGMP.setIcon(new ImageIcon(MenuPrincipaleFrame.class.getResource("/Menu_Principale_img/1.png")));
				ImageIcon animationbtn2 =new ImageIcon(MenuPrincipaleFrame.class.getResource("/Menu_Principale_gif/Team spirit.gif"));
				animationbtn2.getImage().flush();
				animation1.setIcon(animationbtn2);
			}
		});
		btnGSTADH.setToolTipText("Gestion des adherents");
		btnGSTADH.setBounds(32, 220, 128, 128);
		ButtonStyle(btnGSTADH);
		contentPane.add(btnGSTADH);
		
		btnSTATCOV = new JButton("");
		btnSTATCOV.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (Fonction.equals("Président")) {
					StatistiqueFrame frame = new StatistiqueFrame(ID_ADEHRENT);
					frame.setLocationRelativeTo(null);
					frame.setVisible(true);
					dispose();
				}
				else {
			    	   JOptionPane.showMessageDialog(null, "Accès restreint !","Warning", JOptionPane.QUESTION_MESSAGE, new ImageIcon(GestionAnnonces.class.getResource("/messages_img/banned.png")));
				}
				
			}
		});
		btnSTATCOV.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent arg0) {
				BGMP.setIcon(new ImageIcon(MenuPrincipaleFrame.class.getResource("/Menu_Principale_img/4.png")));
				ImageIcon animationbtn2 =new ImageIcon(MenuPrincipaleFrame.class.getResource("/Menu_Principale_BTNgif/Coronavirus second wave.gif")); 
				animationbtn2.getImage().flush(); 
				animation1.setIcon(animationbtn2);
			}
			@Override
			public void mouseExited(MouseEvent e) {
				BGMP.setIcon(new ImageIcon(MenuPrincipaleFrame.class.getResource("/Menu_Principale_img/1.png")));
				ImageIcon animationbtn3 =new ImageIcon(MenuPrincipaleFrame.class.getResource("/Menu_Principale_gif/Team spirit.gif")); //animation Consultation
				animationbtn3.getImage().flush(); // réinitialiser l'animation
				animation1.setIcon(animationbtn3);
			}
		});
		btnSTATCOV.setToolTipText("Statistiques");
		btnSTATCOV.setBounds(32, 411, 142, 128);
		ButtonStyle(btnSTATCOV);
		contentPane.add(btnSTATCOV);
		
		btnGDPN = new JButton("");
		btnGDPN.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (Fonction.equals("Secrétaire")) {
					GestionPerEnBesoin frame = new GestionPerEnBesoin(ID_ADEHRENT);
					frame.setLocationRelativeTo(null);
					frame.setVisible(true);
					dispose();
				}
				else {
			    	   JOptionPane.showMessageDialog(null, "Accès restreint !","Warning", JOptionPane.QUESTION_MESSAGE, new ImageIcon(GestionAnnonces.class.getResource("/messages_img/banned.png")));
				}
				
			}
		});
		btnGDPN.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent arg0) {
				BGMP.setIcon(new ImageIcon(MenuPrincipaleFrame.class.getResource("/Menu_Principale_img/5.png")));
				ImageIcon animationbtn4 =new ImageIcon(MenuPrincipaleFrame.class.getResource("/Menu_Principale_BTNgif/Bankruptcy.gif")); //animation Consultation
				animationbtn4.getImage().flush(); // réinitialiser l'animation
				animation1.setIcon(animationbtn4);
			}
			@Override
			public void mouseExited(MouseEvent e) {
				BGMP.setIcon(new ImageIcon(MenuPrincipaleFrame.class.getResource("/Menu_Principale_img/1.png")));
				ImageIcon animationbtn4 =new ImageIcon(MenuPrincipaleFrame.class.getResource("/Menu_Principale_gif/Team spirit.gif")); //animation Consultation
				animationbtn4.getImage().flush(); // réinitialiser l'animation
				animation1.setIcon(animationbtn4);
			}
		});
		btnGDPN.setToolTipText("Gestion des personnes n\u00E9cessiteux");
		btnGDPN.setBounds(207, 95, 128, 128);
		ButtonStyle(btnGDPN);
		contentPane.add(btnGDPN);
		
		btnGDE = new JButton("");
		btnGDE.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (Fonction.equals("Secrétaire")) {
					GestionEvent frame = new GestionEvent(ID_ADEHRENT);
					frame.setLocationRelativeTo(null);
					frame.setVisible(true);
					dispose();
				}else {
			    	   JOptionPane.showMessageDialog(null, "Accès restreint !","Warning", JOptionPane.QUESTION_MESSAGE, new ImageIcon(GestionAnnonces.class.getResource("/messages_img/banned.png")));
				}
				
			}
		});
		btnGDE.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent arg0) {
				BGMP.setIcon(new ImageIcon(MenuPrincipaleFrame.class.getResource("/Menu_Principale_img/6.png")));
				ImageIcon animationbtn5 =new ImageIcon(MenuPrincipaleFrame.class.getResource("/Menu_Principale_BTNgif/Schedule.gif")); 
				animationbtn5.getImage().flush(); 
				animation1.setIcon(animationbtn5);
			}
			@Override
			public void mouseExited(MouseEvent e) {
				BGMP.setIcon(new ImageIcon(MenuPrincipaleFrame.class.getResource("/Menu_Principale_img/1.png")));
				ImageIcon animationbtn5 =new ImageIcon(MenuPrincipaleFrame.class.getResource("/Menu_Principale_gif/Team spirit.gif")); //animation Consultation
				animationbtn5.getImage().flush(); // réinitialiser l'animation
				animation1.setIcon(animationbtn5);
			}
		});
		btnGDE.setToolTipText("Gestion des \u00E9v\u00E9nements");
		btnGDE.setBounds(207, 278, 128, 128);
		ButtonStyle(btnGDE);
		contentPane.add(btnGDE);
		
		btnGDF = new JButton("");
		btnGDF.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (Fonction.equals("Trésorier")) {
					MenuTrésorier window = new MenuTrésorier(ID_ADEHRENT);
					window.frame.setVisible(true);
					window.frame.setLocationRelativeTo(null);
					dispose();
				}
				else {
			    	   JOptionPane.showMessageDialog(null, "Accès restreint !","Warning", JOptionPane.QUESTION_MESSAGE, new ImageIcon(GestionAnnonces.class.getResource("/messages_img/banned.png")));
				}
				
			}
		});
		btnGDF.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent arg0) {
				BGMP.setIcon(new ImageIcon(MenuPrincipaleFrame.class.getResource("/Menu_Principale_img/7.png")));
				ImageIcon animationbtn6 =new ImageIcon(MenuPrincipaleFrame.class.getResource("/Menu_Principale_BTNgif/Finance.gif")); 
				animationbtn6.getImage().flush(); 
				animation1.setIcon(animationbtn6);
			}
			@Override
			public void mouseExited(MouseEvent e) {
				BGMP.setIcon(new ImageIcon(MenuPrincipaleFrame.class.getResource("/Menu_Principale_img/1.png")));
				ImageIcon animationbtn6 =new ImageIcon(MenuPrincipaleFrame.class.getResource("/Menu_Principale_gif/Team spirit.gif")); //animation Consultation
				animationbtn6.getImage().flush(); // réinitialiser l'animation
				animation1.setIcon(animationbtn6);
			}
		});
		btnGDF.setToolTipText("Gestion des fonds");
		btnGDF.setBounds(218, 458, 128, 128);
		ButtonStyle(btnGDF);
		contentPane.add(btnGDF);
		
		btnGDS = new JButton("");
		btnGDS.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (Fonction.equals("Gestionnaire de Stock")) {
					Article_frame window = new Article_frame(ID_ADEHRENT);
					window.frmGestionDuStock.setVisible(true);
					window.frmGestionDuStock.setLocationRelativeTo(null);
					dispose();
				}
				else {
			    	   JOptionPane.showMessageDialog(null, "Accès restreint !","Warning", JOptionPane.QUESTION_MESSAGE, new ImageIcon(GestionAnnonces.class.getResource("/messages_img/banned.png")));
				}
				
			}
		});
		btnGDS.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent arg0) {
				BGMP.setIcon(new ImageIcon(MenuPrincipaleFrame.class.getResource("/Menu_Principale_img/8.png")));
				ImageIcon animationbtn7 =new ImageIcon(MenuPrincipaleFrame.class.getResource("/Menu_Principale_BTNgif/Humanitarian Help.gif")); //animation Consultation
				animationbtn7.getImage().flush(); // réinitialiser l'animation
				animation1.setIcon(animationbtn7);
			}
			@Override
			public void mouseExited(MouseEvent e) {
				BGMP.setIcon(new ImageIcon(MenuPrincipaleFrame.class.getResource("/Menu_Principale_img/1.png")));
				ImageIcon animationbtn7 =new ImageIcon(MenuPrincipaleFrame.class.getResource("/Menu_Principale_gif/Team spirit.gif")); //animation Consultation
				animationbtn7.getImage().flush(); // réinitialiser l'animation
				animation1.setIcon(animationbtn7);
			}
		});
		btnGDS.setToolTipText("Gestion de stock");
		btnGDS.setBounds(409, 191, 128, 128);
		ButtonStyle(btnGDS);
		contentPane.add(btnGDS);
		
		btnGDC = new JButton("");
		btnGDC.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (Fonction.equals("Psychologue")) {
					PsyConsultationFrame frame = new PsyConsultationFrame(ID_ADEHRENT);
					frame.setLocationRelativeTo(null);
					frame.setVisible(true);
					dispose();
				}
				else {
			    	   JOptionPane.showMessageDialog(null, "Accès restreint !","Warning", JOptionPane.QUESTION_MESSAGE, new ImageIcon(GestionAnnonces.class.getResource("/messages_img/banned.png")));
				}
				
			}
		});
		btnGDC.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent arg0) {
				BGMP.setIcon(new ImageIcon(MenuPrincipaleFrame.class.getResource("/Menu_Principale_img/9.png")));
				ImageIcon animationbtn8 =new ImageIcon(MenuPrincipaleFrame.class.getResource("/Menu_Principale_BTNgif/Psychologist.gif")); //animation Consultation
				animationbtn8.getImage().flush(); // réinitialiser l'animation
				animation1.setIcon(animationbtn8);
			}
			@Override
			public void mouseExited(MouseEvent e) {
				BGMP.setIcon(new ImageIcon(MenuPrincipaleFrame.class.getResource("/Menu_Principale_img/1.png")));
				ImageIcon animationbtn8 =new ImageIcon(MenuPrincipaleFrame.class.getResource("/Menu_Principale_gif/Team spirit.gif")); //animation Consultation
				animationbtn8.getImage().flush(); // réinitialiser l'animation
				animation1.setIcon(animationbtn8);
			}
		});
		btnGDC.setToolTipText("Gestion de consultation");
		btnGDC.setBounds(421, 396, 128, 128);
		ButtonStyle(btnGDC);
		contentPane.add(btnGDC);
		
		
		textAreaAnn_Event = new JTextArea();
		textAreaAnn_Event.setBackground(new Color(255, 51, 51));
		textAreaAnn_Event.setForeground(new Color(255, 51, 51));
		textAreaAnn_Event.setFont(new Font("Microsoft PhagsPa", Font.BOLD, 16));
		textAreaAnn_Event.setRows(10);
		textAreaAnn_Event.setOpaque(false);
		textAreaAnn_Event.setVisible(false);
		textAreaAnn_Event.setColumns(10);

		
		scrollpane = new JScrollPane(textAreaAnn_Event);
		scrollpane.setBounds(56, 159, 493, 435);
		scrollpane.setVisible(false);
		scrollpane.setBorder(BorderFactory.createEmptyBorder());
		scrollpane.getViewport().setOpaque(false);
		contentPane.add(scrollpane);
//le background////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		BGMP.setBounds(0, 0, 1100, 650);	
		contentPane.add(BGMP);
		
		
	}
//methode du style des buttons/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	 private void ButtonStyle(JButton btn) {
		 btn.setOpaque(false);
		 btn.setFocusPainted(false);
		 btn.setBorderPainted(false);
		 btn.setContentAreaFilled(false);
}
	 private void Annonces(JTextArea txt) {
		 String all="";
		String sql = "select AnnIn from Annonce";
			int i=0;
			try {
				prepared = cnx.prepareStatement(sql);
				resultat= prepared.executeQuery();
				while (resultat.next())
				{ i++;
					String In=resultat.getString("AnnIn");
					String contenu1="";
					String contenu2="";
					String contenu3="";
					String contenu4="";
					 int DebContenuindice=In.indexOf("##");
					 if (DebContenuindice != -1) 
					 {
						  contenu1= In.substring(0,DebContenuindice); 
						  contenu2= In.substring(DebContenuindice+2 ,In.length() ); 
						  int FinContenuindice=contenu2.indexOf("##");
						  contenu3=contenu2.substring(0,FinContenuindice);
						  contenu4=contenu2.substring(FinContenuindice+2,contenu2.length());
					 }
					 String COntenuFinal="Annonce N° "+i+":\n	"+contenu3+"\n"+contenu1+"\n"+contenu4+"_________________________________________________________________________________________________________________________________________________________________________________\n";
					 
					all=all+COntenuFinal;
					
				}
				txt.setText(all);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				JOptionPane.showMessageDialog(null, e);
			}
	 
	 }
}
