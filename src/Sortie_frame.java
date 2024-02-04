import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.RowSorter;
import javax.swing.SortOrder;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

import com.formdev.flatlaf.FlatIntelliJLaf;
import com.toedter.calendar.JDateChooser;

import net.proteanit.sql.DbUtils;

import javax.swing.JButton;
import javax.swing.JComboBox;

import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
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
import java.awt.event.ActionEvent;
import javax.swing.JTable;
import java.awt.Color;
import javax.swing.JScrollPane;
import java.awt.Font;
import java.awt.SystemColor;

import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.geom.RoundRectangle2D;
import java.awt.Toolkit;

public class Sortie_frame {
	
	
	Connection cnx=null;
	PreparedStatement prepared = null;
	ResultSet resultat =null; 

	 JFrame frame;
	private JTextField txtQte;
	private JTable tableSortie;
	private JScrollPane scrollPane;
	private JLabel lblNewLabel;
	private String ID_ART;
	private JButton btnAjtSortie;
	private JButton btnModeSortie;
	private String ID_Sortie;
	private String DateS;
	private JTextField txtrecherche;
	private JList<String> listArticle;
	private JComboBox<String> comboBoxArticles;
	
	private int posX = 0;   //Position X de la souris au clic
    private int posY = 0;   //Position Y de la souris au clic

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
					Sortie_frame window = new Sortie_frame("ReMo1");
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public Sortie_frame(String ID_ADEHRENT) {
		initialize(ID_ADEHRENT);
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize(String ID_ADEHRENT) {
		cnx = ConnexionDB.CnxDB();
		frame = new JFrame();
		frame.setIconImage(Toolkit.getDefaultToolkit().getImage(Sortie_frame.class.getResource("/GestionDuStock_img/livraison sortie.png")));
		frame.setBounds(100, 100, 1200, 550);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		frame.setUndecorated(true);
		
		
	//-----------------------------------------------------------------------------------------------------------------------------------------------------------	
		frame.setUndecorated(true);	
		frame.setResizable(false);
		frame.setTitle("gestion des Sorties");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setShape(new RoundRectangle2D.Double(0d, 0d, 1200d, 550d, 25d, 25d));
		frame.setLocationRelativeTo(null);
		frame.  addMouseListener(new MouseAdapter() {
            @Override
            //on recupere les coordonnées de la souris
            public void mousePressed(MouseEvent e) {
                posX = e.getX();    //Position X de la souris au clic
                posY = e.getY();    //Position Y de la souris au clic
            }
        });
		frame.  addMouseMotionListener(new MouseMotionAdapter() {
            // A chaque deplacement on recalcul le positionnement de la fenetre
            @Override
            public void mouseDragged(MouseEvent e) {
                int depX = e.getX() - posX;
                int depY = e.getY() - posY;
                frame.setLocation(frame.getX()+depX,frame.getY()+depY);
            }
        });

		
		//Combo BOX Articles---------------------------------------------------------------------------------------------------------------------------------------------
				 comboBoxArticles = new JComboBox<String>();
				comboBoxArticles.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent arg0) {
						
						if (comboBoxArticles.getSelectedIndex()!=0 ) {//choisir le patient
							String ARTInfo=comboBoxArticles.getSelectedItem().toString();
							 int finIDindice=ARTInfo.indexOf("-");//indice de fin d l'id
							 if (finIDindice != -1) 
							 {
								 ID_ART= ARTInfo.substring(0 , finIDindice); //recuperer l'id du patient
								   String Nom_ARTICLE=ARTInfo.substring(finIDindice +1, ARTInfo.length());
								   txtrecherche.setText(Nom_ARTICLE);
							 }
							 if (tableSortie.getSelectedRow()==-1) {
								UpedateTableSortie(ID_ART);
							}
							 
							 btnAjtSortie.setEnabled(true);
						}
					}
				});
				comboBoxArticles.addItem("Selectionner Article");
				comboBoxArticles.setBackground(new Color(255, 0, 51));
				comboBoxArticles.setForeground(new Color(255, 255, 255));
				comboBoxArticles.setBounds(357, 172, 268, 44);
				frame.getContentPane().add(comboBoxArticles);
				INComboboxARTICLES(comboBoxArticles);//actualiser
				//list  Article**********************************************************************************************************************************
				JScrollPane scrollPanePersonne = new JScrollPane();
				scrollPanePersonne.setViewportBorder(new TitledBorder(null, "", TitledBorder.LEADING, TitledBorder.TOP, null, Color.GRAY));
				scrollPanePersonne.setBackground(Color.LIGHT_GRAY);
				scrollPanePersonne.setBounds(33, 227, 231, 300);
				scrollPanePersonne.setBorder(null);
				frame.getContentPane().add(scrollPanePersonne);
				
				
				listArticle = new JList<String>();
				scrollPanePersonne.setViewportView(listArticle);
				listArticle.addMouseListener(new MouseAdapter() {
					@Override
					public void mouseClicked(MouseEvent e) {
						
						for (int j = 0; j < comboBoxArticles.getItemCount(); j++) //parcourir la comboBox des patients
						{
							 String Nom_Article=" ";
							 String PatInfo=comboBoxArticles.getItemAt(j).toString();//recuperer le contenu de la  combobox a l'element numero j
								 int finIDindice=PatInfo.indexOf("-");//marque la fin de l'ID
								 int finprenomindice=PatInfo.length();//la fin du prenom
								 if (finIDindice != -1) 
								 {
									 Nom_Article=PatInfo.substring(finIDindice+1 , finprenomindice);//recupperer le NOM
								 }
								if (Nom_Article.equals(listArticle.getSelectedValue())) {
									comboBoxArticles.setSelectedIndex(j);
									//selectionner l'element correspandant 
								}
						}
						txtrecherche.setText(listArticle.getSelectedValue());
					}
				});
				listArticle.setOpaque(false);
				listArticle.setBackground(SystemColor.control);
				listArticle.setForeground(new Color(255, 0, 51));
				listArticle.setFont(new Font("Segoe UI", Font.BOLD, 12));
				UpdateListART(listArticle);	
				
				//le text Field pour la recherche*****************************************************************************************************
						txtrecherche = new JTextField();
						//avec le mechanisme de recherche live
						txtrecherche.addKeyListener(new java.awt.event.KeyAdapter() {
				            public void keyReleased(java.awt.event.KeyEvent evt) {
				            	Recherche(evt);//appler la fonction recherche
				            }
				        });
						txtrecherche.addKeyListener(new KeyAdapter() {//limiter le nombre de caractere du textfield
						    public void keyTyped(KeyEvent e) { 
						        if (		txtrecherche.getText().length() >= 100 ) 
						            e.consume(); 
						    }  
						});
						txtrecherche.setBounds(29, 172, 217, 28);
						txtrecherche.setForeground(new Color(255, 0, 51));
						txtrecherche.setFont(new Font("Segoe UI", Font.BOLD, 14));
						txtrecherche.setBackground(Color.WHITE);
						txtrecherche.setBorder(null);
						frame.getContentPane().add(txtrecherche);
						txtrecherche.setColumns(10);		
		
				
				
				
				
				
				
				
				//Date chooser--------------------------------------------------------------------------------------------------------------------------------------------
				JDateChooser dateChooser = new JDateChooser();
				dateChooser.setToolTipText("Date");
				dateChooser.setDateFormatString("yyyy-MM-dd");
				dateChooser.setBorder(null);
				dateChooser.setForeground(new Color(102, 0, 153));
				dateChooser.setBackground(new Color(255, 0, 51));
				dateChooser.setDate(new Date());
				dateChooser.setBounds(496, 232, 258, 39);
				frame.getContentPane().add(dateChooser);
				//--------------------------------------------------------------------------------------------------------------------------------------------

		txtQte = new JTextField();
		txtQte.setForeground(new Color(255, 255, 255));
		txtQte.setFont(new Font("Segoe UI", Font.BOLD, 14));
		txtQte.setBackground(new Color(0, 153, 0));
		txtQte.setOpaque(false);
		txtQte.addKeyListener(new KeyAdapter() {
		    public void keyTyped(KeyEvent e) { 
		        if (		txtQte.getText().length() >= 20 ) 
		            e.consume(); 
		    }  
		});
		txtQte.setBorder(new LineBorder(new Color(171, 173, 179), 0));
		txtQte.setBounds(496, 282, 258, 32);
		frame.getContentPane().add(txtQte);
		txtQte.setColumns(10);
		
		btnAjtSortie = new JButton("");
		btnAjtSortie.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent arg0) {
				if (btnAjtSortie.isEnabled()) {
					btnAjtSortie.setIcon(new ImageIcon(Sortie_frame.class.getResource("/GestiondesSorties_img/plus selected.png")));
				}
			}
			@Override
			public void mouseExited(MouseEvent e) {
				if (btnAjtSortie.isEnabled()) {
					btnAjtSortie.setIcon(new ImageIcon(Sortie_frame.class.getResource("/GestiondesSorties_img/plus.png")));
				}
			}
		});
		btnAjtSortie.setIcon(new ImageIcon(Sortie_frame.class.getResource("/GestiondesSorties_img/plus.png")));
		btnAjtSortie.setEnabled(false);
		btnAjtSortie.setToolTipText("Ajouter une Sortie");
		btnAjtSortie.setFont(new Font("Comic Sans MS", Font.BOLD, 12));
		btnAjtSortie.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ImageIcon icon = new ImageIcon(GestionPerEnBesoin.class.getResource("/messages_img/Questions secretaire.gif"));//Animation "Question"
				 icon.getImage().flush(); // réinitialiser l'animation
				 //message de confirmation
				int ClickedButton	=JOptionPane.showConfirmDialog(null, "Voulez Vous vraiment ajouter une Sortie a cet Article ?", "Confirmation",JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, icon);
				if(ClickedButton==JOptionPane.YES_OPTION)
					{
					String Date = ((JTextField) dateChooser.getDateEditor().getUiComponent()).getText(); //recuperer la Date
					String aneee = Date.substring(0, 4);//annee
					String Mois=Date.substring(5, 7);//Mois
					String jours=Date.substring(8, 10);//Jours
					String Dateinverse =jours+"-"+Mois+"-"+aneee;
					if (txtQte.getText().equals("") || comboBoxArticles.getSelectedIndex()==0) {
						JOptionPane.showMessageDialog(null, " S'il vous plait veuillez Remplire les champs Marqués par '*'  et selectionner un article dela comboox!","Warning", JOptionPane.QUESTION_MESSAGE, new ImageIcon(GestionAnnonces.class.getResource("/messages_img/error.png")));
					}
					else {
						Sortie S =new Sortie(ID_ART, Dateinverse, txtQte.getText());
						S.ajouter();
						UpedateTableSortie(ID_ART);
						btnModeSortie.setEnabled(false);
					}
					
					}
			}
		});
		btnAjtSortie.setBounds(906, 252, 32, 32);
		ButtonStyle(btnAjtSortie);
		frame.getContentPane().add(btnAjtSortie);
		
		 btnModeSortie = new JButton("");
		btnModeSortie.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent arg0) {
				if (btnModeSortie.isEnabled()) {
					btnModeSortie.setIcon(new ImageIcon(Sortie_frame.class.getResource("/GestiondesSorties_img/crayon selected.png")));
				}
			}
			@Override
			public void mouseExited(MouseEvent e) {
				if (btnModeSortie.isEnabled()) {
					btnModeSortie.setIcon(new ImageIcon(Sortie_frame.class.getResource("/GestiondesSorties_img/crayon.png")));
				}
			}
		});
		btnModeSortie.setIcon(new ImageIcon(Sortie_frame.class.getResource("/GestiondesSorties_img/crayon.png")));
		btnModeSortie.setEnabled(false);
		btnModeSortie.setToolTipText("Modifier une Sortie");
		btnModeSortie.setFont(new Font("Comic Sans MS", Font.BOLD, 12));
		btnModeSortie.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ImageIcon icon = new ImageIcon(GestionPerEnBesoin.class.getResource("/messages_img/Questions secretaire.gif"));//Animation "Question"
				 icon.getImage().flush(); // réinitialiser l'animation
				 //message de confirmation
				int ClickedButton	=JOptionPane.showConfirmDialog(null, "Voulez Vous vraiment Modifier cet Sortie de cet Article ?", "Confirmation",JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, icon);
				if(ClickedButton==JOptionPane.YES_OPTION)
					{
					String Date = ((JTextField) dateChooser.getDateEditor().getUiComponent()).getText(); //recuperer la Date
					String aneee = Date.substring(0, 4);//annee
					String Mois=Date.substring(5, 7);//Mois
					String jours=Date.substring(8, 10);//Jours
					String Dateinverse =jours+"-"+Mois+"-"+aneee;
					Sortie S = new Sortie(ID_ART, Dateinverse,txtQte.getText());
					S.modifier(ID_Sortie);
					UpedateTableSortie(ID_ART);
					txtrecherche.setText("");
					btnModeSortie.setEnabled(false);
					btnAjtSortie.setEnabled(true);
					txtQte.setEnabled(true);
					comboBoxArticles.setEnabled(true);
					}
			}
		});
		btnModeSortie.setBounds(994, 252, 32, 32);
		frame.getContentPane().add(btnModeSortie);
		ButtonStyle(btnModeSortie);
		
		scrollPane = new JScrollPane();
		scrollPane.setBounds(307, 339, 861, 188);
		frame.getContentPane().add(scrollPane);
		
		tableSortie = new JTable();
		tableSortie.setFont(new Font("Segoe UI", Font.BOLD, 11));
		tableSortie.setForeground(new Color(255, 255, 255));
		tableSortie.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				btnAjtSortie.setEnabled(false);
				btnModeSortie.setEnabled(true);
				txtQte.setEnabled(false);
				comboBoxArticles.setEnabled(false);
				int ligne=tableSortie.getSelectedRow();

				ID_Sortie = tableSortie.getValueAt(ligne, 0).toString();
				String sql="Select * from SORTIE where codS='"+ID_Sortie+"'";
				try {
					prepared=cnx.prepareStatement(sql);
					resultat=prepared.executeQuery();
					if(resultat.next())
					{
						txtQte.setText(resultat.getString("QTE_SORT"));
						String Date =resultat.getString("DATEE_SORT").toString();
						if (Date!=null)
						{
				    	DateS=Date.substring(0, 10);
				    	DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
				    	Date date;
						date = format.parse(DateS);
				    	dateChooser.setDate(date);
						}
					}
				} catch (SQLException|ParseException e ) {
					// TODO Auto-generated catch block
					JOptionPane.showMessageDialog(null,e);
				}
			}
		});
		scrollPane.setViewportView(tableSortie);
		tableSortie.setBackground(new Color(255, 0, 51));
//Actualiser///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////		
		UpedateTableSortie(ID_ART);
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
				frame.setState(frame.ICONIFIED);
			}
		});
		Minimise_BTN.setIcon(new ImageIcon(MenuPrincipaleFrame.class.getResource("/Menu_Principale_img/Minimize ML .png")));
		Minimise_BTN.setBounds(1047, 11, 32, 32);
		frame.getContentPane().add(Minimise_BTN);
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
					frame.dispose();
				}
			}
			
		});
		Exit_BTN.setIcon(new ImageIcon(MenuPrincipaleFrame.class.getResource("/Menu_Principale_img/Exit ML.png")));
		Exit_BTN.setBounds(1123, 11, 32, 32);
		frame.getContentPane().add(Exit_BTN);
//Boutton home//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		JButton btnHome = new JButton("");
		btnHome.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				btnHome.setIcon(new ImageIcon(MenuPrincipaleFrame.class.getResource("/GestiondesSorties_img/fleche-gauche selected.png")));//changer les couleurs button
			}
			@Override
			public void mouseExited(MouseEvent e) {
				btnHome.setIcon(new ImageIcon(MenuPrincipaleFrame.class.getResource("/GestiondesSorties_img/fleche-gauche.png")));//remetre le bouton de base
			}
		});
		btnHome.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
								Article_frame window = new Article_frame(ID_ADEHRENT);
								window.frmGestionDuStock.setVisible(true);
								window.frmGestionDuStock.setLocationRelativeTo(null);
								window.frmGestionDuStock.setVisible(true);
								frame.dispose();
					}
				});
		btnHome.setIcon(new ImageIcon(Sortie_frame.class.getResource("/GestiondesSorties_img/fleche-gauche.png")));
		btnHome.setToolTipText("Retourner au Menu");
		btnHome.setBounds(1085, 11, 32, 32);
		ButtonStyle(btnHome);
		frame.getContentPane().add(btnHome);		
//Vider les champs bouton////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		JButton btnViderFields = new JButton("");
		btnViderFields.setToolTipText("Clear the fields");
		ButtonStyle(btnViderFields);
		btnViderFields.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				btnModeSortie.setEnabled(false);
				comboBoxArticles.setSelectedIndex(0);
				txtrecherche.setText("");
				txtQte.setText("");
				txtQte.setEnabled(true);
				comboBoxArticles.setEnabled(true);
				UpedateTableSortie("");
				dateChooser.setDate(new Date());
			}
		});
		btnViderFields.setIcon(new ImageIcon(GestionPerEnBesoin.class.getResource("/GestionAD_img/trash.png")));
		btnViderFields.setBounds(1070, 252, 32, 32);
		frame.getContentPane().add(btnViderFields);
		
		
//BG///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////	
		lblNewLabel = new JLabel("New label");
		lblNewLabel.setIcon(new ImageIcon(Sortie_frame.class.getResource("/GestiondesSorties_img/4.png")));
		lblNewLabel.setBounds(0, 0, 1201, 550);
		frame.getContentPane().add(lblNewLabel);
	}

	 private void ButtonStyle(JButton btn) {
			//enlever les bordures des btn
			 btn.setFocusPainted(false);
			 btn.setBorderPainted(false);
			 btn.setContentAreaFilled(false);
			 btn.setContentAreaFilled(false); // On met à false pour empêcher le composant de peindre l'intérieur du JButton.
			
		}
	 
	 
	public Connection conn() {
		try {
			Class.forName("oracle.jdbc.OracleDriver");
			Connection cn = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:Asso","system","123");
					return cn;}
		catch(Exception e) {
			JOptionPane.showMessageDialog(null, e.toString());
		}return null;}
	
//UpedateTableSorties/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	 public void UpedateTableSortie(String CODA)
	 { 	
	 	
	 	try {
	 		String rq = "SELECT * FROM 	SORTIE WHERE CODEA='"+CODA+"'";
			PreparedStatement pp = conn().prepareStatement(rq);
			ResultSet rs = pp.executeQuery();
			tableSortie.setModel(DbUtils.resultSetToTableModel(rs));
	 		TableRowSorter<TableModel> sorter = new TableRowSorter<TableModel>(tableSortie.getModel());
	 		tableSortie.setRowSorter(sorter);
	 		List<RowSorter.SortKey> sortKeys = new ArrayList<>(25);
	 		sortKeys.add(new RowSorter.SortKey(0, SortOrder.ASCENDING));	
	 		sorter.setSortKeys(sortKeys);
	 		//renomer les  colones
	 		tableSortie.getColumnModel().getColumn( 0 ).setHeaderValue("Code Sortie");
	 		tableSortie.getColumnModel().getColumn( 1 ).setHeaderValue("Code Article");	
	 		tableSortie.getColumnModel().getColumn( 2 ).setHeaderValue("Quanttié Sortie");
	 		tableSortie.getColumnModel().getColumn( 3 ).setHeaderValue("Date Sortie");

	 } catch (SQLException e) {
	 // TODO Auto-generated catch block
	 	JOptionPane.showMessageDialog(null,e);
	 }

}
//Dans la comboBox/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	 public void INComboboxARTICLES(JComboBox<String> AR)
	 { 
	 	String sql ="Select CODEA , LIBELLE from ARTICLE "; 
	 	try {
	 		prepared = cnx.prepareStatement(sql);
	 		resultat =prepared.executeQuery();
	 		while(resultat.next())
	 		{
	 				 String item=resultat.getString("CODEA").toString()+"-"+resultat.getString("LIBELLE").toString();
	 				AR.addItem(item);
	 		}
	 	} catch (SQLException e) {
	 		// TODO Auto-generated catch block
	 		JOptionPane.showMessageDialog(null,e);
	 	}
	 	
	 }
//Actualiser la liste des articles/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	 public void UpdateListART(JList<String> List)
	 		{ 
			cnx = ConnexionDB.CnxDB();
	 			String sql ="Select LIBELLE  from ARTICLE";
	 			try {
	 				prepared = cnx.prepareStatement(sql);
	 				resultat =prepared.executeQuery();
	 				ArrayList<String> L=new ArrayList<String>();//une liste qui va contenir les patients
	 				while(resultat.next())
	 				{	
	 					String item=resultat.getString("LIBELLE").toString();
	 					L.add(item);//ajouter a la liste
	 				}
	 				DefaultListModel<String> defaultListModel=new DefaultListModel<String>();// remplissage de la liste
	 				L.stream().forEach((star) -> {
	 	            defaultListModel.addElement(star);//ajouter l'element
	 				});
	 				List.setModel(defaultListModel);//changer le modele de la liste
	 				List.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
	 		
	 			} catch (SQLException e) {
	 				// TODO Auto-generated catch block
	 				JOptionPane.showMessageDialog(null,e);
	 			}
	 	
	 		}
//Recherche///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////				
	 	private void Recherche(java.awt.event.KeyEvent evt) 
	 		{                                      
	 			RechercheFiltre(txtrecherche.getText());
	 		}        

	 	/*
	 	Fltrer les donneés */

	 	private void RechercheFiltre(String searchTerm)
	 	{
	 		String sql ="Select LIBELLE from ARTICLE";
	 		try {
	 			prepared = cnx.prepareStatement(sql);
	 			resultat =prepared.executeQuery();
	 			ArrayList<String> L=new ArrayList<String>();//declarer une array list qui va contenir les nom+Prenom Des Patients
	 			while(resultat.next())
	 			{
	 				String item=resultat.getString("LIBELLE").toString();
	 				
	 				L.add(item);//ajouter le patient
	 				
	 			}
	 			DefaultListModel<String> defaultListModel=new DefaultListModel<String>();
	 			DefaultListModel<String> filteredItems=new DefaultListModel<String>();

	 		    L.stream().forEach((star) -> {//pour chaque element
	 		        String starName=star.toString().toLowerCase();
	 		        if (starName.contains(searchTerm.toLowerCase())) {//si la list contient le term recherché
	 		        	filteredItems.addElement(star);//alors afficher que les String de la lmist le contenat
	 		        }
	 		    });
	 		    defaultListModel=filteredItems;
	 		   listArticle.setModel(defaultListModel);//changer le model de la list
	 		} catch (SQLException e) {
	 			// TODO Auto-generated catch block
	 			JOptionPane.showMessageDialog(null,e);
	 		}
	 	}

}
