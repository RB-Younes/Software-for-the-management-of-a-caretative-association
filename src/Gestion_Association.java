import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.geom.RoundRectangle2D;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.RowSorter;
import javax.swing.SortOrder;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

import com.formdev.flatlaf.FlatIntelliJLaf;
import com.toedter.calendar.JDateChooser;

import net.proteanit.sql.DbUtils;
import java.awt.Toolkit;

public class Gestion_Association {
	
	Connection cnx=null;
	PreparedStatement prepared = null;
	ResultSet resultat =null; 
	ResultSet resultat1 =null; 

	JFrame frmAssociation;
	private JTextField textFieldFond;
	private JTable tableAsso;
	private JScrollPane scrollPane;
	private JLabel lblNewLabel_3;
	private JButton btnAjtAsso;
	private JButton btnModAsso;
	private JButton btnSuppUser;
	private String ID_ASSO;
	private String DateS;
	
	private int posX = 0;   //Position X de la souris au clic
    private int posY = 0;   //Position Y de la souris au clic
    private JTextField textFieldNom;
    private JTextField textFieldAdr;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args)  throws UnsupportedLookAndFeelException {
		FlatIntelliJLaf.install();	
		UIManager.setLookAndFeel(new FlatIntelliJLaf() );
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Gestion_Association window = new Gestion_Association("ReMo1");
					window.frmAssociation.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public Gestion_Association(String ID_ADEHRENT) {
		initialize(ID_ADEHRENT);
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize(String ID_ADEHRENT) {
		// CNX 
		cnx = ConnexionDB.CnxDB();
		frmAssociation = new JFrame();
		frmAssociation.setIconImage(Toolkit.getDefaultToolkit().getImage(Gestion_Association.class.getResource("/GestionUtilisateurs_img/coup-de-main.png")));
		frmAssociation.setBounds(100, 100, 1100, 550);
		frmAssociation.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmAssociation.getContentPane().setLayout(null);
		frmAssociation.setUndecorated(true);
		
		frmAssociation.setUndecorated(true);	
		frmAssociation.setResizable(false);
		frmAssociation.setTitle("Association");
		frmAssociation.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmAssociation.setShape(new RoundRectangle2D.Double(0d, 0d, 1100d, 550d, 25d, 25d));
		frmAssociation.setLocationRelativeTo(null);
		//vu que la frame est Undecorated on a besoin de ces MouseListeners pour la faire bouger(frame)
		frmAssociation.  addMouseListener(new MouseAdapter() {
	            @Override
	            //on recupere les coordonnées de la souris
	            public void mousePressed(MouseEvent e) {
	                posX = e.getX();    //Position X de la souris au clic
	                posY = e.getY();    //Position Y de la souris au clic
	            }
	        });
		frmAssociation.  addMouseMotionListener(new MouseMotionAdapter() {
	            // A chaque deplacement on recalcul le positionnement de la fenetre
	            @Override
	            public void mouseDragged(MouseEvent e) {
	                int depX = e.getX() - posX;
	                int depY = e.getY() - posY;
	                frmAssociation.setLocation(frmAssociation.getX()+depX,frmAssociation.getY()+depY);
	            }
	        });
		
		textFieldFond = new JTextField();
		textFieldFond.setOpaque(false);
		textFieldFond.addKeyListener(new KeyAdapter() {
		    public void keyTyped(KeyEvent e) { 
		        if (		textFieldFond.getText().length() >= 20 ) 
		            e.consume(); 
		    }  
		});
		textFieldFond.setColumns(10);
		textFieldFond.setForeground(new Color(255, 255, 255));
		textFieldFond.setFont(new Font("Segoe UI", Font.BOLD, 14));
		textFieldFond.setBorder(BorderFactory.createEmptyBorder());
		textFieldFond.setBackground(new Color(0, 153, 0));
		textFieldFond.setBounds(586, 147, 262, 37);
		frmAssociation.getContentPane().add(textFieldFond);
		textFieldFond.setBorder(BorderFactory.createEmptyBorder());
		
		textFieldNom = new JTextField();
		textFieldNom.addKeyListener(new KeyAdapter() {
		    public void keyTyped(KeyEvent e) { 
		        if (		textFieldNom.getText().length() >= 40 ) 
		            e.consume(); 
		    }  
		});
		textFieldNom.setOpaque(false);
		textFieldNom.setForeground(Color.WHITE);
		textFieldNom.setFont(new Font("Segoe UI", Font.BOLD, 14));
		textFieldNom.setColumns(10);
		textFieldNom.setBorder(BorderFactory.createEmptyBorder());
		textFieldNom.setBackground(new Color(0, 153, 0));
		textFieldNom.setBounds(145, 147, 262, 37);
		frmAssociation.getContentPane().add(textFieldNom);
		
		textFieldAdr = new JTextField();
		textFieldAdr.addKeyListener(new KeyAdapter() {
		    public void keyTyped(KeyEvent e) { 
		        if (		textFieldAdr.getText().length() >= 50 ) 
		            e.consume(); 
		    }  
		});
		textFieldAdr.setOpaque(false);
		textFieldAdr.setForeground(Color.WHITE);
		textFieldAdr.setFont(new Font("Segoe UI", Font.BOLD, 14));
		textFieldAdr.setColumns(10);
		textFieldAdr.setBorder(BorderFactory.createEmptyBorder());
		textFieldAdr.setBackground(new Color(0, 153, 0));
		textFieldAdr.setBounds(586, 211, 262, 37);
		frmAssociation.getContentPane().add(textFieldAdr);
		//Date chooser--------------------------------------------------------------------------------------------------------------------------------------------
		JDateChooser dateChooser = new JDateChooser();
		dateChooser.setToolTipText("Date");
		dateChooser.setDateFormatString("yyyy-MM-dd");
		dateChooser.setBorder(null);
		dateChooser.setForeground(new Color(102, 0, 153));
		dateChooser.setBackground(new Color(51, 153, 255));
		dateChooser.setDate(new Date());
		dateChooser.setBounds(145, 209, 258, 39);
		frmAssociation.getContentPane().add(dateChooser);
		
		
		btnAjtAsso = new JButton("");
		btnAjtAsso.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent arg0) {
				if (btnAjtAsso.isEnabled()) {
					btnAjtAsso.setIcon(new ImageIcon(Sortie_frame.class.getResource("/Assobtns_img/plus selected.png")));
				}
			}
			@Override
			public void mouseExited(MouseEvent e) {
				if (btnAjtAsso.isEnabled()) {
					btnAjtAsso.setIcon(new ImageIcon(Sortie_frame.class.getResource("/Assobtns_img/plus.png")));
				}
			}
		});
		btnAjtAsso.setIcon(new ImageIcon(Gestion_Association.class.getResource("/Assobtns_img/plus.png")));
		btnAjtAsso.setToolTipText("Ajouter une Association");
		btnAjtAsso.setFont(new Font("Comic Sans MS", Font.BOLD, 12));
		btnAjtAsso.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ImageIcon icon = new ImageIcon(GestionPerEnBesoin.class.getResource("/messages_img/Questions secretaire.gif"));//Animation "Question"
				 icon.getImage().flush(); // réinitialiser l'animation
				 //message de confirmation
				int ClickedButton	=JOptionPane.showConfirmDialog(null, "Etes vous sur des informations saisies ? voulez vous enregistrer Cette Association?", "Confirmation",JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, icon);
				if(ClickedButton==JOptionPane.YES_OPTION)
					{
					String Date = ((JTextField) dateChooser.getDateEditor().getUiComponent()).getText(); //recuperer la Date
					String aneee = Date.substring(0, 4);//annee
					String Mois=Date.substring(5, 7);//Mois
					String jours=Date.substring(8, 10);//Jours
					String Dateinverse =jours+"-"+Mois+"-"+aneee;
					if (textFieldFond.getText().equals("") || textFieldNom.getText().equals("") || textFieldAdr.getText().equals("")) {
						JOptionPane.showMessageDialog(null, " S'il vous plait veuillez Remplire les champs Marqués par '*'  et selectionner un article dela comboox!","Warning", JOptionPane.QUESTION_MESSAGE, new ImageIcon(GestionAnnonces.class.getResource("/messages_img/error.png")));
					}
					else {
						Association as = new Association(Dateinverse, textFieldNom.getText(), textFieldAdr.getText(), textFieldFond.getText());
						as.ajouter();
						UpedateTableAsso();
						JOptionPane.showMessageDialog(null, "Association ajoutée avc succés!","Ajout" ,JOptionPane.PLAIN_MESSAGE, new ImageIcon(AuthentificationFrame.class.getResource("/messages_img/stamp.png")));
						btnAjtAsso.setEnabled(true);
						btnModAsso.setEnabled(false);
					}
					}
			}
		});
		btnAjtAsso.setBounds(56, 321, 32, 32);
		ButtonStyle(btnAjtAsso);
		frmAssociation.getContentPane().add(btnAjtAsso);
		
		 btnModAsso = new JButton("");
			btnModAsso.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseEntered(MouseEvent arg0) {
					if (btnModAsso.isEnabled()) {
						btnModAsso.setIcon(new ImageIcon(Sortie_frame.class.getResource("/Assobtns_img/crayon selected.png")));
					}
				}
				@Override
				public void mouseExited(MouseEvent e) {
					if (btnModAsso.isEnabled()) {
						btnModAsso.setIcon(new ImageIcon(Sortie_frame.class.getResource("/Assobtns_img/crayon.png")));
					}
				}
			});
			btnModAsso.setIcon(new ImageIcon(Gestion_Association.class.getResource("/Assobtns_img/crayon.png")));
			btnModAsso.setEnabled(false);
			btnModAsso.setToolTipText("Modifier une Association");
			btnModAsso.setFont(new Font("Comic Sans MS", Font.BOLD, 12));
			btnModAsso.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					ImageIcon icon = new ImageIcon(GestionPerEnBesoin.class.getResource("/messages_img/Questions secretaire.gif"));//Animation "Question"
					 icon.getImage().flush(); // réinitialiser l'animation
					 //message de confirmation
					int ClickedButton	=JOptionPane.showConfirmDialog(null, "Voulez Vous vraiment Modifier ?", "Confirmation",JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, icon);
					if(ClickedButton==JOptionPane.YES_OPTION)
						{
						if (textFieldFond.getText().equals("") || textFieldNom.getText().equals("") || textFieldAdr.getText().equals("")) {
							JOptionPane.showMessageDialog(null, " S'il vous plait veuillez Remplire les champs Marqués par '*'  et selectionner un article dela comboox!","Warning", JOptionPane.QUESTION_MESSAGE, new ImageIcon(GestionAnnonces.class.getResource("/messages_img/error.png")));
						}
						else {
						String Date = ((JTextField) dateChooser.getDateEditor().getUiComponent()).getText(); //recuperer la Date
						String aneee = Date.substring(0, 4);//annee
						String Mois=Date.substring(5, 7);//Mois
						String jours=Date.substring(8, 10);//Jours
						String Dateinverse =jours+"-"+Mois+"-"+aneee;
						Association as = new Association(Dateinverse, textFieldNom.getText(), textFieldAdr.getText(), textFieldFond.getText());
						as.modifier(ID_ASSO);
						UpedateTableAsso();
						JOptionPane.showMessageDialog(null, "Association modifiée avec succés!","Modification" ,JOptionPane.PLAIN_MESSAGE, new ImageIcon(AuthentificationFrame.class.getResource("/messages_img/stamp.png")));
						btnModAsso.setEnabled(false);
						btnSuppUser.setEnabled(false);
						}
						
						}
				}
			});
			btnModAsso.setBounds(56, 364, 32, 32);
			frmAssociation.getContentPane().add(btnModAsso);
			ButtonStyle(btnModAsso);
			
			btnSuppUser = new JButton();
			btnSuppUser.setEnabled(false);//desactiver le bouton tant que acune ligne de la table Medecin n'a ete selectionné
			btnSuppUser.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseEntered(MouseEvent e) {
					if (btnSuppUser.isEnabled()) {
						btnSuppUser.setIcon(new ImageIcon(GestionUtilisateursExistants.class.getResource("/Assobtns_img/retirer selected.png")));//changer les couleurs button
					}
				}
				@Override
				public void mouseExited(MouseEvent e) {
					btnSuppUser.setIcon(new ImageIcon(GestionUtilisateursExistants.class.getResource("/Assobtns_img/retirer.png")));//remetre le bouton de base
				}
			});
			ButtonStyle(btnSuppUser);
			btnSuppUser.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
					if (tableAsso.getSelectedRow()==-1) // return  quand aucune ligne esr selectionnée
					{
						JOptionPane.showMessageDialog(null, " Veuillez  selectionner une ligne de la table utilisateur a suprimer !","WArning" ,JOptionPane.PLAIN_MESSAGE, new ImageIcon(GestionUtilisateursExistants.class.getResource("/messages_img/error.png")));

					}
					else {
						ImageIcon icon = new ImageIcon(GestionPerEnBesoin.class.getResource("/messages_img/Questions secretaire.gif"));//Animation "Question"
						 icon.getImage().flush(); // réinitialiser l'animation
						//message de confirmation
						int ClickedButton	=JOptionPane.showConfirmDialog(	null, "Voulez vous vraiment supprimer ?", "Confirmation", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, icon);
					if(ClickedButton==JOptionPane.YES_OPTION)
					{	
						String Date = ((JTextField) dateChooser.getDateEditor().getUiComponent()).getText(); //recuperer la Date
						String aneee = Date.substring(0, 4);//annee
						String Mois=Date.substring(5, 7);//Mois
						String jours=Date.substring(8, 10);//Jours
						String Dateinverse =jours+"-"+Mois+"-"+aneee;
						Association as = new Association(Dateinverse, textFieldNom.getText(), textFieldAdr.getText(), textFieldFond.getText());
						as.supprimer(ID_ASSO);
						UpedateTableAsso();
						JOptionPane.showMessageDialog(null, "Association supprimée avec succès!","Suppression" ,JOptionPane.PLAIN_MESSAGE, new ImageIcon(AuthentificationFrame.class.getResource("/messages_img/stamp.png")));
						btnModAsso.setEnabled(false);
						btnSuppUser.setEnabled(false);
					}
					}
					
				}
			});
			btnSuppUser.setToolTipText("Supprimer Medecin");
			btnSuppUser.setIcon(new ImageIcon(Gestion_Association.class.getResource("/Assobtns_img/retirer.png")));
			btnSuppUser.setBounds(56, 407, 32, 32);
			frmAssociation.getContentPane().add(btnSuppUser);
		
		scrollPane = new JScrollPane();
		scrollPane.setBounds(152, 278, 885, 244);
		frmAssociation.getContentPane().add(scrollPane);
		
		tableAsso = new JTable();
		tableAsso.setFont(new Font("Segoe UI", Font.BOLD, 11));
		tableAsso.setForeground(new Color(255, 255, 255));
		tableAsso.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				btnModAsso.setEnabled(true);
				btnSuppUser.setEnabled(true);
				int ligne=tableAsso.getSelectedRow();

				ID_ASSO = tableAsso.getValueAt(ligne, 0).toString();
				String sql="Select * from ASSOCIATION where ass='"+ID_ASSO+"'";
				try {
					prepared=cnx.prepareStatement(sql);
					resultat=prepared.executeQuery();
					if(resultat.next())
					{
						textFieldFond.setText(resultat.getString("FOND"));
						textFieldNom.setText(resultat.getString("NOM"));
						textFieldAdr.setText(resultat.getString("ADR"));
						
						String Date =resultat.getString("date_").toString();
						if (Date!=null)
						{
				    	DateS=Date.substring(0, 10);
				    	DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
				    	Date date;
						date = format.parse(DateS);
				    	dateChooser.setDate(date);
						}
					}
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					JOptionPane.showMessageDialog(null,e);
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					JOptionPane.showMessageDialog(null,e);
				}
			}
		});
		tableAsso.setBackground(new Color(0, 153, 255));
		scrollPane.setViewportView(tableAsso);
		// Vider les champs bouton////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		JButton btnViderFields = new JButton("");
		btnViderFields.setToolTipText("Clear the fields");
		ButtonStyle(btnViderFields);
		btnViderFields.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				btnModAsso.setEnabled(false);
				btnAjtAsso.setEnabled(true);
				textFieldFond.setText("");
				textFieldAdr.setText("");
				textFieldNom.setText("");
				UpedateTableAsso();
				dateChooser.setDate(new Date());
			}
		});
		btnViderFields.setIcon(new ImageIcon(GestionPerEnBesoin.class.getResource("/GestionAD_img/trash.png")));
		btnViderFields.setBounds(56, 443, 32, 32);
		frmAssociation.getContentPane().add(btnViderFields);
		
		
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
					@SuppressWarnings("static-access")
					public void actionPerformed(ActionEvent e) {
						frmAssociation.setState(frmAssociation.ICONIFIED);
					}
				});
				Minimise_BTN.setIcon(new ImageIcon(MenuPrincipaleFrame.class.getResource("/Menu_Principale_img/Minimize ML .png")));
				Minimise_BTN.setBounds(948, 22, 32, 32);
				frmAssociation.getContentPane().add(Minimise_BTN);
		//Exit bouton//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
				
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
							frmAssociation.dispose();
						}
					}
					
				});
				Exit_BTN.setIcon(new ImageIcon(MenuPrincipaleFrame.class.getResource("/Menu_Principale_img/Exit ML.png")));
				Exit_BTN.setBounds(1024, 22, 32, 32);
				frmAssociation.getContentPane().add(Exit_BTN);
				//Boutton home//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
				JButton btnHome = new JButton("");
				ButtonStyle(btnHome);
				btnHome.addMouseListener(new MouseAdapter() {
					@Override
					public void mouseEntered(MouseEvent e) {
						btnHome.setIcon(new ImageIcon(GestionUtilisateursExistants.class.getResource("/Menu_Principale_img/reglage selected.png")));//changer les couleurs button
					}
					@Override
					public void mouseExited(MouseEvent e) {
						btnHome.setIcon(new ImageIcon(GestionUtilisateursExistants.class.getResource("/Menu_Principale_img/reglage.png")));//remetre le bouton de base
					}
				});
				btnHome.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						GestionUtiMenu f1 = new GestionUtiMenu(ID_ADEHRENT);
						f1.setVisible(true);
						f1.setLocationRelativeTo(null);
						frmAssociation.dispose();
					}
				});
				btnHome.setIcon(new ImageIcon(GestionUtilisateursExistants.class.getResource("/Menu_Principale_img/reglage.png")));
				btnHome.setToolTipText("Retourner au Menu");
				btnHome.setBounds(986, 22, 32, 32);
				frmAssociation.getContentPane().add(btnHome);		
				
		lblNewLabel_3 = new JLabel("");
		lblNewLabel_3.setIcon(new ImageIcon(Gestion_Association.class.getResource("/GestionUtilisateurs_img/Gestion Association.png")));
		lblNewLabel_3.setBounds(0, 0, 1201, 550);
		frmAssociation.getContentPane().add(lblNewLabel_3);
		UpedateTableAsso();
		
	}

	 private void ButtonStyle(JButton btn) {
			//enlever les bordures des btn
			 btn.setFocusPainted(false);
			 btn.setBorderPainted(false);
			 btn.setContentAreaFilled(false);
			 btn.setContentAreaFilled(false); // On met à false pour empêcher le composant de peindre l'intérieur du JButton.
			/* btn.setBorderPainted(false); // De même, on ne veut pas afficher les bordures.
			 btn.setFocusPainted(false); // On n'affiche pas l'effet de focus*/
		}
	 public Connection conn() {
			try {
				Class.forName("oracle.jdbc.OracleDriver");
				Connection cn = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:Asso","system","123");
						return cn;}
			catch(Exception e) {
				JOptionPane.showMessageDialog(null, e.toString());
			}return null;}

	 public void UpedateTableAsso()
	 { 	
	 	
	 	try {
	 		String rq = "SELECT * FROM ASSOCIATION";
			PreparedStatement pp = conn().prepareStatement(rq);
			ResultSet rs = pp.executeQuery();
			tableAsso.setModel(DbUtils.resultSetToTableModel(rs));
	 		TableRowSorter<TableModel> sorter = new TableRowSorter<TableModel>(tableAsso.getModel());
	 		tableAsso.setRowSorter(sorter);
	 		List<RowSorter.SortKey> sortKeys = new ArrayList<>(25);
	 		sortKeys.add(new RowSorter.SortKey(1, SortOrder.ASCENDING));	
	 		sorter.setSortKeys(sortKeys);
	 		//renomer les  colones
	 		tableAsso.getColumnModel().getColumn( 0 ).setHeaderValue("Code Association");
	 		tableAsso.getColumnModel().getColumn( 1 ).setHeaderValue("Nom Association");	
	 		tableAsso.getColumnModel().getColumn( 2 ).setHeaderValue("Fond Association");
	 		tableAsso.getColumnModel().getColumn( 3 ).setHeaderValue("Date");
	 		tableAsso.getColumnModel().getColumn( 4 ).setHeaderValue("Adresse Association");

	 } catch (SQLException e) {
	 // TODO Auto-generated catch block
	 	JOptionPane.showMessageDialog(null,e);
	 }

}
}
