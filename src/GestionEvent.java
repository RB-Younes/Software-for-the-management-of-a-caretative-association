import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.geom.RoundRectangle2D;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.RowSorter;
import javax.swing.SortOrder;
import javax.swing.SpinnerDateModel;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

import com.formdev.flatlaf.FlatIntelliJLaf;
import com.toedter.calendar.JDateChooser;

import net.proteanit.sql.DbUtils;
////////////////////////////////////////////////////////////////////////////////-----------Fenetre Evenement------------///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
public class GestionEvent extends JFrame {

	
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTable tableEvent;
	private String datactuelle="";
	private JButton btnModEvent;
	private JButton btnRemoveEvent;
	private String contenu= null;
	private String DateS;
	private String Heure;
	private String Titre;
	
	
	private JButton btnSamedi;
	private JButton btnDimanche;
	private JButton btnJeudi;
	private JButton btnMardi;
	private JButton btnLundi;
	private JButton btnMercredi;

	
	Connection cnx=null;
	PreparedStatement prepared = null;
	ResultSet resultat =null; 
	
	private int posX = 0;   //Position X de la souris au clic
    private int posY = 0;   //Position Y de la souris au clic
	private String num_event;
	private JTextField textFieldTitre;
	
	/**
	 * Launch the application.
	 * @throws UnsupportedLookAndFeelException 
	 */
	public static void main(String[] args) throws UnsupportedLookAndFeelException {
		FlatIntelliJLaf.install();	
	UIManager.setLookAndFeel(new FlatIntelliJLaf() );
	JFrame.setDefaultLookAndFeelDecorated(true);
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					GestionEvent frame = new GestionEvent("ReMo1");
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
	
	public GestionEvent(String ID_ADHERENT) {
// CNX 
		cnx = ConnexionDB.CnxDB();
		
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
		setUndecorated(true);
		setShape(new RoundRectangle2D.Double(0d, 0d, 1200d, 550d, 25d, 25d));
		setResizable(false);
		setIconImage(Toolkit.getDefaultToolkit().getImage(GestionEvent.class.getResource("/ChercherPersonne_img/chercher ICO.png")));
		setTitle("Staff Management");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(0, 0, 1200, 550);
		
		
		contentPane = new JPanel();
		
		
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
// Fields-//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		
		textFieldTitre = new JTextField();
		textFieldTitre.addKeyListener(new KeyAdapter() {
		    public void keyTyped(KeyEvent e) { 
		        if (		textFieldTitre.getText().length() >= 100 ) 
		            e.consume(); 
		    }  
		});
		textFieldTitre.setBounds(140, 391, 272, 32);
		textFieldTitre.setForeground(new Color(255, 255, 255));
		textFieldTitre.setFont(new Font("Segoe UI", Font.BOLD, 14));
		textFieldTitre.setOpaque(false);
		textFieldTitre.setBorder(null);
		contentPane.add(textFieldTitre);
		textFieldTitre.setColumns(10);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(140, 452, 272, 75);
		contentPane.add(scrollPane);
		
		JTextArea textAreaDesc = new JTextArea();
		textAreaDesc.addKeyListener(new KeyAdapter() {
		    public void keyTyped(KeyEvent e) { 
		        if (		textAreaDesc.getText().length() >= 4000 ) 
		            e.consume(); 
		    }  
		});
		textAreaDesc.setBackground(new Color(123, 104, 238));
		textAreaDesc.setForeground(new Color(255, 255, 255));
		textAreaDesc.setFont(new Font("Segoe UI", Font.BOLD, 14));
		textAreaDesc.setBorder(null);
		scrollPane.setViewportView(textAreaDesc);
		
		//Spinner pour l'Heur
		JSpinner spinnerH = new JSpinner();
		spinnerH.setSize(100, 25);
		spinnerH.setLocation(569, 481);
		spinnerH.setModel(new SpinnerDateModel());
		spinnerH.setEditor(new JSpinner.DateEditor(spinnerH, "HH:mm"));
		contentPane.add(spinnerH);
		
		//Date chooser
		JDateChooser dateChooser = new JDateChooser();
		dateChooser.setToolTipText("Date");
		dateChooser.setDateFormatString("yyyy-MM-dd");
		dateChooser.setForeground(new Color(102, 0, 153));
		dateChooser.setBackground(new Color(147, 112, 219));
		dateChooser.setBorder(null);
		dateChooser.setDate(new Date());
		dateChooser.setBounds(559, 391, 217, 32);
		contentPane.add(dateChooser);
		
		//Date chooser
		JDateChooser dateChooserRechercher = new JDateChooser();
		dateChooserRechercher.setToolTipText("Date");
		dateChooserRechercher.setDateFormatString("yyyy-MM-dd");
		dateChooserRechercher.setBorder(null);
		dateChooserRechercher.setForeground(new Color(102, 0, 153));
		dateChooserRechercher.setBackground(new Color(147, 112, 219));
		dateChooserRechercher.setDate(new Date());
		dateChooserRechercher.setBounds(784, 142, 217, 32);
		contentPane.add(dateChooserRechercher);
//ScrollPane/Table Patient(Grande table)//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	//Table Patient Grande**************************************************************************************************************************************************************************************
		JScrollPane scrollPaneRDV = new JScrollPane();
		scrollPaneRDV.setViewportBorder(new TitledBorder(null, "", TitledBorder.LEADING, TitledBorder.TOP, null, Color.GRAY));
		scrollPaneRDV.setBounds(102, 185, 1002, 182);
		contentPane.add(scrollPaneRDV);
		
		tableEvent = new JTable();
		tableEvent.setFont(new Font("Segoe UI", Font.BOLD, 12));
		tableEvent.setForeground(new Color(255, 255, 255));
		tableEvent.setBorder(null);
		tableEvent.addMouseListener(new MouseAdapter() {
			// s
			@Override
			public void mouseClicked(MouseEvent arg0) {
				btnRemoveEvent.setEnabled(true);
				btnModEvent.setEnabled(true);
				String contenu1="";
				String contenu2="";
				int ligne=tableEvent.getSelectedRow();
				num_event =tableEvent.getValueAt(ligne, 0).toString();
				String sql="Select * from Evenements where ID_Event='"+num_event+"'";
				
				try {
					prepared=cnx.prepareStatement(sql);
					resultat=prepared.executeQuery();
					if(resultat.next())
					{
					
						String DateEvent =resultat.getString("DateEvent").toString();
						
						if (DateEvent!=null)
						{
				    	DateS=DateEvent.substring(0, 10);
				    	DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
				    	Date date = format.parse(DateS);
				    	dateChooser.setDate(date);
						}
						
						Heure=resultat.getString("HeureEvent").toString();					
						SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");//le format
						Date H_eve = sdf.parse(Heure); 
						spinnerH.setValue(H_eve); 
						
						Titre =resultat.getString("NomEvent").toString();
						textFieldTitre.setText(Titre);
						
						contenu=resultat.getString("InEvent");
						 int DebContenuindice=contenu.indexOf("##");//indice de fin d l'id
						 if (DebContenuindice != -1) 
						 {
							  contenu1= contenu.substring(DebContenuindice+2 ,contenu.length() ); 
							  int FinContenuindice=contenu1.indexOf("##");
							  contenu2=contenu1.substring(0 ,FinContenuindice); 
						 }
						 textAreaDesc.setText(contenu2);
						
					}
					
				} catch (SQLException  e) {
					// TODO Auto-generated catch block
					JOptionPane.showMessageDialog(null,e);
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					JOptionPane.showMessageDialog(null,e);
				}
			}
		});
		tableEvent.setBackground(new Color(147, 112, 219));
		scrollPaneRDV.setViewportView(tableEvent);
		
		
//afficher le contenue de la table juste a louverture://////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////	

		UpedateTableEvent();
	
		
// REduire bouton//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		JButton Minimise_BTN = new JButton("");
		Minimise_BTN.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				Minimise_BTN.setIcon(new ImageIcon(GestionEvent.class.getResource("/Log_in_img/Minimize ML selected.png")));//changer les couleurs button
			}
			@Override
			public void mouseExited(MouseEvent e) {
				Minimise_BTN.setIcon(new ImageIcon(GestionEvent.class.getResource("/Log_in_img/Minimize ML .png")));//remetre le bouton de base
			}
		});
		Minimise_BTN.setToolTipText("Minimize");
		Minimise_BTN.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setState(ICONIFIED);
				
			}
		});
		Minimise_BTN.setIcon(new ImageIcon(GestionEvent.class.getResource("/Log_in_img/Minimize ML .png")));
		Minimise_BTN.setBounds(1072, 5, 32, 32);
		contentPane.add(Minimise_BTN);
		
// Vider les champs bouton////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		JButton btnViderFields = new JButton("");
		btnViderFields.setToolTipText("R\u00E9initialiser les champs");
		ButtonStyle(btnViderFields);
		btnViderFields.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				textAreaDesc.setText("");
				textFieldTitre.setText("");
				dateChooser.setDate(new Date());
				spinnerH.setModel(new SpinnerDateModel());
				spinnerH.setEditor(new JSpinner.DateEditor(spinnerH, "HH:mm"));//prendre la valeur du systeme
				
			}
		});
		btnViderFields.setIcon(new ImageIcon(GestionEvent.class.getResource("/GestionAD_img/trash.png")));
		btnViderFields.setBounds(452, 507, 32, 32);
		contentPane.add(btnViderFields);
		
// Exit bouton/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		JButton Exit_BTN = new JButton("");
		Exit_BTN.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent arg0) {
				Exit_BTN.setIcon(new ImageIcon(GestionEvent.class.getResource("/Log_in_img/Exit ML selected.png")));//changer les couleurs button
			}
			@Override
			public void mouseExited(MouseEvent arg0) {
				Exit_BTN.setIcon(new ImageIcon(GestionEvent.class.getResource("/Log_in_img/Exit ML.png")));//remetre le bouton de base
			}
		});
		Exit_BTN.setToolTipText("Exit");
		Exit_BTN.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			
					
					int ClickedButton	=JOptionPane.showConfirmDialog(null, "Do you really want to leave?", "Close", JOptionPane.YES_NO_OPTION);
					if(ClickedButton==JOptionPane.YES_OPTION)
					{
						dispose();
					}
			}
			
		});
		Exit_BTN.setIcon(new ImageIcon(GestionEvent.class.getResource("/Log_in_img/Exit ML.png")));
		Exit_BTN.setBounds(1156, 5, 32, 32);
		contentPane.add(Exit_BTN);
//Grande table actualisation /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		JButton btnalltheTable = new JButton("");
		btnalltheTable.setToolTipText("Actualiser");
		btnalltheTable.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				btnalltheTable.setIcon(new ImageIcon(GestionEvent.class.getResource("/GestionEvent_img/chercher selected.png")));
			}
			@Override
			public void mouseExited(MouseEvent e) {
				btnalltheTable.setIcon(new ImageIcon(GestionEvent.class.getResource("/GestionEvent_img/chercher.png")));
			}
		});
		btnalltheTable.setIcon(new ImageIcon(GestionEvent.class.getResource("/GestionEvent_img/chercher.png")));
		btnalltheTable.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {//actualiser la table au clique
				String Date = ((JTextField) dateChooserRechercher.getDateEditor().getUiComponent()).getText(); //recuperer la Date
				String aneee = Date.substring(0, 4);//annee
				String Mois=Date.substring(5, 7);//Mois
				String jours=Date.substring(8, 10);//Jours
				String Dateinverse =jours+"-"+Mois+"-"+aneee;
				UpedateTableEventJours( Dateinverse);
			}
		});
		btnalltheTable.setBounds(1026, 142, 32, 32);
		contentPane.add(btnalltheTable);
		//Ajouter un Patient**********************************************************************************************************************************************************************************************************************************
				JButton btnAddEvent = new JButton("");
				btnAddEvent.addMouseListener(new MouseAdapter() {
					@Override
					public void mouseEntered(MouseEvent arg0) {
						btnAddEvent.setIcon(new ImageIcon(GestionAD.class.getResource("/GestionEvent_img/plus selected.png")));//changer les couleurs button
					}
					@Override
					public void mouseExited(MouseEvent e) {
						btnAddEvent.setIcon(new ImageIcon(GestionAD.class.getResource("/GestionEvent_img/plus.png")));//remetre le bouton de base
					}
				});
				btnAddEvent.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent arg0) {
						 ImageIcon icon = new ImageIcon(GestionAD.class.getResource("/messages_img/Questions secretaire.gif"));//Animation "Question"
						 icon.getImage().flush(); // réinitialiser l'animation
						 //message de confirmation
						int ClickedButton	=JOptionPane.showConfirmDialog(null, "Vouler Vous vraiment ajouter cet Evenement ?", "Confirmation",JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, icon);
						if(ClickedButton==JOptionPane.YES_OPTION)
							{
							if (textAreaDesc.getText().equals("") || textFieldTitre.getText().equals("")) {
								JOptionPane.showMessageDialog(null, " S'il vous plait veuillez Remplire les champs marqués par '*' !","!", JOptionPane.QUESTION_MESSAGE, new ImageIcon(GestionAnnonces.class.getResource("/messages_img/error.png")));
							}
							else {
								String Nom=null;
								String Prenom=null;
								String sql1 = "Select Nom,Prenom from Adherent where ID_AD='"+ID_ADHERENT+"'";
							try {
								prepared=cnx.prepareStatement(sql1);
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
								Date actuelle = new Date();
								DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
								String datactuelle = dateFormat.format(actuelle);
							
								String titre =textFieldTitre.getText();
								String Heure=spinnerH.getValue().toString().substring(11,16);
							
								String Date = ((JTextField) dateChooser.getDateEditor().getUiComponent()).getText(); //recuperer la Date
								String aneee = Date.substring(0, 4);//annee
								String Mois=Date.substring(5, 7);//Mois
								String jours=Date.substring(8, 10);//Jours
								String Dateinverse =jours+"-"+Mois+"-"+aneee;
							
								String Evenement_Contenu ="Evenement Ajouté par: "+Nom+" "+Prenom+",le: "+datactuelle+"."+"##"+textAreaDesc.getText().toString()+"##";
								Evenement E  = new Evenement(ID_ADHERENT, titre, Evenement_Contenu, Heure, Dateinverse);
								E.Ajouter();
								UpedateTableEventJours(Dateinverse);
							
							}
										
							}
					}
				});
				btnAddEvent.setIcon(new ImageIcon(GestionEvent.class.getResource("/GestionEvent_img/plus.png")));
				btnAddEvent.setToolTipText("Ajouter Evenement");
				ButtonStyle(btnAddEvent);
				btnAddEvent.setBounds(863, 430, 32, 32);
				contentPane.add(btnAddEvent);
			//Modifier un Patient**********************************************************************************************************************************************************************************************************************************	
				btnModEvent = new JButton("");
				btnModEvent.setEnabled(false);//desactiver le bouton tant que acune ligne de la table n'a ete selectionné
				btnModEvent.setToolTipText("Modifier Evenement");
				btnModEvent.addMouseListener(new MouseAdapter() {
					@Override
					public void mouseEntered(MouseEvent e) {
						if (btnModEvent.isEnabled()) {
							btnModEvent.setIcon(new ImageIcon(GestionAD.class.getResource("/GestionEvent_img/crayon selected.png")));//changer les couleurs button
						}
					}
					@Override
					public void mouseExited(MouseEvent e) {
						if (btnModEvent.isEnabled()) {
							btnModEvent.setIcon(new ImageIcon(GestionAD.class.getResource("/GestionEvent_img/crayon.png")));//remetre le bouton de base
						}
					}
				});
				btnModEvent.addActionListener(new ActionListener() {
					
					@Override
					public void actionPerformed(ActionEvent e) {
						 ImageIcon icon = new ImageIcon(GestionAD.class.getResource("/messages_img/Questions secretaire.gif"));//Animation "Question"
						 icon.getImage().flush(); // réinitialiser l'animation
						//message de confirmation
						int ClickedButton	=JOptionPane.showConfirmDialog(null, "Vouler Vous vraiment Modifier les informations de cet evenment? ", "Confirmation",JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, icon);
						if(ClickedButton==JOptionPane.YES_OPTION)
						{
							if (tableEvent.getSelectedRow()==-1 )  
							{//message
							JOptionPane.showMessageDialog(null, "S'il vous plait veuillez selectioner une ligne de la table Evenements a modifer !","Warning" ,JOptionPane.PLAIN_MESSAGE, new ImageIcon(GestionAnnonces.class.getResource("/messages_img/error.png")));
							}
							else 
							{
								if (textAreaDesc.getText().equals("")|| textFieldTitre.getText().equals("")) {
									JOptionPane.showMessageDialog(null, " S'il vous plait veuillez Remplire les champs marqués par '*' !","!", JOptionPane.QUESTION_MESSAGE, new ImageIcon(GestionAnnonces.class.getResource("/messages_img/error.png")));
								}
								else {
									Date actuelle = new Date();
									DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
									String datactuelle = dateFormat.format(actuelle);
									
									String titre =textFieldTitre.getText();
									String Heure=spinnerH.getValue().toString().substring(11,16);
									
									String Date = ((JTextField) dateChooser.getDateEditor().getUiComponent()).getText(); //recuperer la Date
									String aneee = Date.substring(0, 4);//annee
									String Mois=Date.substring(5, 7);//Mois
									String jours=Date.substring(8, 10);//Jours
									String Dateinverse =jours+"-"+Mois+"-"+aneee;
									
									String contenu1="";
									String contenu2="";
									String contenu3="";
									 int DebContenuindice=contenu.indexOf("##");
									 if (DebContenuindice != -1) 
									 {
										  contenu1= contenu.substring(0,DebContenuindice); 
										  contenu2= contenu.substring(DebContenuindice+2 ,contenu.length() ); 
										  int FinContenuindice=contenu2.indexOf("##");
										  contenu3=contenu2.substring(FinContenuindice,contenu2.length()); 
									 }
									String COntenuFinal=contenu1+"##"+textAreaDesc.getText()+contenu3;
										
										Evenement E =new Evenement();
										E.Modifer(num_event, ID_ADHERENT, titre, COntenuFinal, Dateinverse, datactuelle, Heure);
									
										btnModEvent.setEnabled(false);
										btnRemoveEvent.setEnabled(false);
										UpedateTableEventJours(Dateinverse);
								}
							}
						}
					}
				});
				btnModEvent.setIcon(new ImageIcon(GestionEvent.class.getResource("/GestionEvent_img/crayon.png")));
				btnModEvent.setBounds(1026, 430, 32, 32);
				ButtonStyle(btnModEvent);
				contentPane.add(btnModEvent);
			
			//Supprimer un medicament**********************************************************************************************************************************************************************************************************************************
				btnRemoveEvent = new JButton("");
				btnRemoveEvent.setEnabled(false);//desactiver le bouton tant que acune ligne de la table n'a ete selectionné
				btnRemoveEvent.setToolTipText("Supprimer Evenement");
				btnRemoveEvent.addMouseListener(new MouseAdapter() {
					@Override
					public void mouseEntered(MouseEvent e) {
						if (btnRemoveEvent.isEnabled()) {
								btnRemoveEvent.setIcon(new ImageIcon(GestionAD.class.getResource("/GestionEvent_img/retirer selected.png")));//changer les couleurs button
						}
					
					}
					@Override
					public void mouseExited(MouseEvent e) {
						if (btnRemoveEvent.isEnabled()) {
							btnRemoveEvent.setIcon(new ImageIcon(GestionAD.class.getResource("/GestionEvent_img/retirer.png")));//remetre le bouton de base
						}
					}
				});
				btnRemoveEvent.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						if (tableEvent.getSelectedRow()==-1)
						{
							JOptionPane.showMessageDialog(null, "S'il vous plait veuillez selectioner une ligne a supprimer !","Message", JOptionPane.QUESTION_MESSAGE,new ImageIcon(GestionAnnonces.class.getResource("/messages_img/error.png")));
						}
					else{
						 ImageIcon icon = new ImageIcon(GestionPerEnBesoin.class.getResource("/messages_img/Questions secretaire.gif"));
						 icon.getImage().flush();
						int ClickedButton	=JOptionPane.showConfirmDialog(null, "Voulez Vous vraiment supprimer cet evenement ?", "Confirmation",JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE,icon);
						if(ClickedButton==JOptionPane.YES_OPTION)
							{
							String Date = ((JTextField) dateChooser.getDateEditor().getUiComponent()).getText(); //recuperer la Date
							String aneee = Date.substring(0, 4);//annee
							String Mois=Date.substring(5, 7);//Mois
							String jours=Date.substring(8, 10);//Jours
							String Dateinverse =jours+"-"+Mois+"-"+aneee;
							
							Evenement E = new Evenement();
							E.Supprimer(num_event,ID_ADHERENT ,DateS, Heure, Titre);
							btnModEvent.setEnabled(false);
							btnRemoveEvent.setEnabled(false);
							UpedateTableEventJours(Dateinverse);
							textAreaDesc.setText("");
							textFieldTitre.setText("");
							dateChooser.setDate(new Date());
							spinnerH.setModel(new SpinnerDateModel());
							spinnerH.setEditor(new JSpinner.DateEditor(spinnerH, "HH:mm"));
							}
						}
						
					}
				});
				
				btnRemoveEvent.setIcon(new ImageIcon(GestionEvent.class.getResource("/GestionEvent_img/retirer.png")));
				btnRemoveEvent.setBounds(939, 430, 32, 32);
				ButtonStyle(btnRemoveEvent);
				contentPane.add(btnRemoveEvent);		
		
// les bouttons de la grande table (jours de la semaine)	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	//bouton Dimanche************************************************************************************************************************************************************************************************
		btnDimanche = new JButton("Dimanche");
		btnDimanche.setFont(new Font("Segoe UI", Font.BOLD, 11));
		btnDimanche.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				Date date = new Date();
				DateFormat dateFormat = new SimpleDateFormat("E");
				datactuelle = dateFormat.format(date);
				Date dateret;
				String daterech ;
				Calendar cal = Calendar.getInstance(); 
			
				switch(datactuelle.substring(0,3))
				{
				case "ven"://Vendredi

					
					cal.add(Calendar.DATE, -5); 
					dateret =cal.getTime();
					dateFormat = new SimpleDateFormat("dd-MM-yyyy");
					daterech =dateFormat.format(dateret); 
					UpedateTableEventJours(daterech);
					break;
					
				case "sam":

					cal.add(Calendar.DATE, -6); 
					dateret =cal.getTime();
					dateFormat = new SimpleDateFormat("dd-MM-yyyy");
					daterech =dateFormat.format(dateret);
					UpedateTableEventJours(daterech);
					break;
					
				case "dim":

					UpedateTableEvent();
					break;

				case "lun":

					cal.add(Calendar.DATE, -1);
					 dateret =cal.getTime();
					dateFormat = new SimpleDateFormat("dd-MM-yyyy");
					daterech =dateFormat.format(dateret); 
					UpedateTableEventJours(daterech);
					break;
				case "mar":

					cal.add(Calendar.DATE, -2); 
					 dateret =cal.getTime();
					dateFormat = new SimpleDateFormat("dd-MM-yyyy");
					daterech =dateFormat.format(dateret); 
					UpedateTableEventJours(daterech);
					break;
				case "mer":

					cal.add(Calendar.DATE, -3);
					 dateret =cal.getTime();
					dateFormat = new SimpleDateFormat("dd-MM-yyyy");
					daterech =dateFormat.format(dateret); 
					UpedateTableEventJours(daterech);
					break;
				case "jeu":

					cal.add(Calendar.DATE, -4);
					 dateret =cal.getTime();
					dateFormat = new SimpleDateFormat("dd-MM-yyyy");
					daterech =dateFormat.format(dateret);
					UpedateTableEventJours(daterech);
					break;
					
				default:
					
				}	
			}
		});
		btnDimanche.setForeground(Color.WHITE);
		btnDimanche.setBackground(new Color(147, 112, 219));
		btnDimanche.setBounds(-14, 194, 94, 30);
		contentPane.add(btnDimanche);
		
	//bouton Lundi************************************************************************************************************************************************************************************************
		btnLundi = new JButton("Lundi");
		btnLundi.setFont(new Font("Segoe UI", Font.BOLD, 11));
		btnLundi.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Date date = new Date();
				DateFormat dateFormat = new SimpleDateFormat("E");
				datactuelle = dateFormat.format(date);
				
				
				Date dateret;
				String daterech ;
				Calendar cal = Calendar.getInstance(); 
			
				switch(datactuelle.substring(0,3)) 
				{
				case "ven"://Vendredi
					
					cal.add(Calendar.DATE, -4); 
					 dateret =cal.getTime();
					dateFormat = new SimpleDateFormat("dd-MM-yyyy");
					daterech =dateFormat.format(dateret); 
					UpedateTableEventJours(daterech);

					break;
					
				case "sam":

					cal.add(Calendar.DATE, -5); 
					dateret =cal.getTime();
					dateFormat = new SimpleDateFormat("dd-MM-yyyy");
					daterech =dateFormat.format(dateret); 
					UpedateTableEventJours(daterech);
					break;
					
				case "dim":
				
					cal.add(Calendar.DATE, +1);
					 dateret =cal.getTime();
					dateFormat = new SimpleDateFormat("dd-MM-yyyy");
					daterech =dateFormat.format(dateret);
					UpedateTableEventJours(daterech);
					break;
					
				case "lun":
					UpedateTableEvent();
					break;
					
				case "mar":
				
					cal.add(Calendar.DATE, -1); 
					 dateret =cal.getTime();
					dateFormat = new SimpleDateFormat("dd-MM-yyyy");
					daterech =dateFormat.format(dateret);
					UpedateTableEventJours(daterech);
					break;
				case "mer":
			
					cal.add(Calendar.DATE, -2);
					 dateret =cal.getTime();
					dateFormat = new SimpleDateFormat("dd-MM-yyyy");
					daterech =dateFormat.format(dateret);
					UpedateTableEventJours(daterech);
					break;
				case "jeu":
					
					cal.add(Calendar.DATE, -3);
					 dateret =cal.getTime();
					dateFormat = new SimpleDateFormat("dd-MM-yyyy");
					daterech =dateFormat.format(dateret);
					UpedateTableEventJours(daterech);
					break;
				default:
					
				}	
			}
		});
		btnLundi.setForeground(Color.WHITE);
		btnLundi.setBackground(new Color(147, 112, 219));
		btnLundi.setBounds(-14, 223, 94, 27);
		contentPane.add(btnLundi);
		
		//de meme pour le reste
	//bouton Mardi************************************************************************************************************************************************************************************************
		btnMardi = new JButton("Mardi");
		btnMardi.setFont(new Font("Segoe UI", Font.BOLD, 11));
		btnMardi.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Date date = new Date();
				DateFormat dateFormat = new SimpleDateFormat("E");
				datactuelle = dateFormat.format(date);
				
				Date dateret; 
				String daterech ;
				Calendar cal = Calendar.getInstance(); 
			
				switch(datactuelle.substring(0,3)) 
				{
				case "ven"://Vendredi

					
					cal.add(Calendar.DATE, -3); 
					 dateret =cal.getTime();
					dateFormat = new SimpleDateFormat("dd-MM-yyyy");
					daterech =dateFormat.format(dateret); 
					UpedateTableEventJours(daterech);
					break;
					
				case "sam":

					cal.add(Calendar.DATE, -4); 
					dateret =cal.getTime();
					dateFormat = new SimpleDateFormat("dd-MM-yyyy");
					daterech =dateFormat.format(dateret); 
					UpedateTableEventJours(daterech);
					break;
					
				case "lun":

					cal.add(Calendar.DATE, +1);
					 dateret =cal.getTime();
					dateFormat = new SimpleDateFormat("dd-MM-yyyy");
					daterech =dateFormat.format(dateret); 
					UpedateTableEventJours(daterech);
					break;
				case "dim":

					cal.add(Calendar.DATE, +2); 
					 dateret =cal.getTime();
					dateFormat = new SimpleDateFormat("dd-MM-yyyy");
					daterech =dateFormat.format(dateret); 
					UpedateTableEventJours(daterech);
					break;
					
				case "mar":

					UpedateTableEvent();
					break;
				case "mer":

					cal.add(Calendar.DATE, -1);
					 dateret =cal.getTime();
					dateFormat = new SimpleDateFormat("dd-MM-yyyy");
					daterech =dateFormat.format(dateret); 
					UpedateTableEventJours(daterech);
					break;
				case "jeu":

					cal.add(Calendar.DATE, -2);
					 dateret =cal.getTime();
					dateFormat = new SimpleDateFormat("dd-MM-yyyy");
					daterech =dateFormat.format(dateret); 
					UpedateTableEventJours(daterech);
					break;
				default:
					
				}	
			}
		});
		btnMardi.setForeground(Color.WHITE);
		btnMardi.setBackground(new Color(147, 112, 219));
		btnMardi.setBounds(-14, 249, 94, 27);
		contentPane.add(btnMardi);
		
	//bouton Mercredi************************************************************************************************************************************************************************************************
		btnMercredi = new JButton("Mercredi");
		btnMercredi.setFont(new Font("Segoe UI", Font.BOLD, 11));
		btnMercredi.setForeground(Color.WHITE);
		btnMercredi.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				Date date = new Date();
				DateFormat dateFormat = new SimpleDateFormat("E");
				datactuelle = dateFormat.format(date);
				
				
				Date dateret; 
				String daterech ;
				Calendar cal = Calendar.getInstance();
			
				switch(datactuelle.substring(0,3)) 
				{
				case "ven"://Vendredi

					
					cal.add(Calendar.DATE, -2); 
					 dateret =cal.getTime();
					dateFormat = new SimpleDateFormat("dd-MM-yyyy");
					daterech =dateFormat.format(dateret); 
					UpedateTableEventJours(daterech);
					break;
					
				case "sam":

					cal.add(Calendar.DATE, -3); 
					dateret =cal.getTime();
					dateFormat = new SimpleDateFormat("dd-MM-yyyy");
					daterech =dateFormat.format(dateret); 
					UpedateTableEventJours(daterech);
					break;
					
				case "lun":

					cal.add(Calendar.DATE, +2 );
					 dateret =cal.getTime();
					dateFormat = new SimpleDateFormat("dd-MM-yyyy");
					daterech =dateFormat.format(dateret); 
					UpedateTableEventJours(daterech);
					break;
				case "mar":

					cal.add(Calendar.DATE, +1); 
					 dateret =cal.getTime();
					dateFormat = new SimpleDateFormat("dd-MM-yyyy");
					daterech =dateFormat.format(dateret); 
					UpedateTableEventJours(daterech);
					break;
				case "mer":

					UpedateTableEvent();
					break;
				case "dim":

					cal.add(Calendar.DATE, +3);
					 dateret =cal.getTime();
					dateFormat = new SimpleDateFormat("dd-MM-yyyy");
					daterech =dateFormat.format(dateret); 
					UpedateTableEventJours(daterech);
					break;
				case "jeu":

					cal.add(Calendar.DATE, -1);
					 dateret =cal.getTime();
					dateFormat = new SimpleDateFormat("dd-MM-yyyy");
					daterech =dateFormat.format(dateret); 
					UpedateTableEventJours(daterech);
					break;
				default:
					
				}	
			}
		});
		btnMercredi.setForeground(Color.WHITE);
		btnMercredi.setBackground(new Color(147, 112, 219));
		btnMercredi.setBounds(-14, 275, 94, 27);
		contentPane.add(btnMercredi);
		
	//bouton Jeudi************************************************************************************************************************************************************************************************
		btnJeudi = new JButton("Jeudi");
		btnJeudi.setFont(new Font("Segoe UI", Font.BOLD, 11));
		btnJeudi.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Date date = new Date();
				DateFormat dateFormat = new SimpleDateFormat("E");
				datactuelle = dateFormat.format(date);
				
				
				Date dateret; 
				String daterech ;
				Calendar cal = Calendar.getInstance();
			
				switch(datactuelle.substring(0,3)) 
				{
				case "ven"://Vendredi

					
					cal.add(Calendar.DATE, -1); 
					 dateret =cal.getTime();
					dateFormat = new SimpleDateFormat("dd-MM-yyyy");
					daterech =dateFormat.format(dateret); 
					UpedateTableEventJours(daterech);
					break;
					
				case "sam":

					cal.add(Calendar.DATE, -2); 
					dateret =cal.getTime();
					dateFormat = new SimpleDateFormat("dd-MM-yyyy");
					daterech =dateFormat.format(dateret); 
					UpedateTableEventJours(daterech);
					break;
					
				case "lun":

					cal.add(Calendar.DATE, +3);
					 dateret =cal.getTime();
					dateFormat = new SimpleDateFormat("dd-MM-yyyy");
					daterech =dateFormat.format(dateret); 
					UpedateTableEventJours(daterech);
					break;
					
				case "jeu":

					UpedateTableEvent();
					break;
				case "mar":

					cal.add(Calendar.DATE, +2); 
					 dateret =cal.getTime();
					dateFormat = new SimpleDateFormat("dd-MM-yyyy");
					daterech =dateFormat.format(dateret); 
					UpedateTableEventJours(daterech);
					break;
				case "mer":

					cal.add(Calendar.DATE, +1);
					 dateret =cal.getTime();
					dateFormat = new SimpleDateFormat("dd-MM-yyyy");
					daterech =dateFormat.format(dateret); 
					UpedateTableEventJours(daterech);
					break;
				case "dim":

					cal.add(Calendar.DATE, +4);
					 dateret =cal.getTime();
					dateFormat = new SimpleDateFormat("dd-MM-yyyy");
					daterech =dateFormat.format(dateret); 
					UpedateTableEventJours(daterech);
					break;
				default:
					
				}	
			}
		});
		btnJeudi.setForeground(Color.WHITE);
		btnJeudi.setBackground(new Color(147, 112, 219));
		btnJeudi.setBounds(-14, 301, 94, 27);
		contentPane.add(btnJeudi);
		
	//bouton Samedi************************************************************************************************************************************************************************************************
		btnSamedi = new JButton("Samedi");
		btnSamedi.setFont(new Font("Segoe UI", Font.BOLD, 11));
		btnSamedi.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent arg0) {
						Date date = new Date();
						DateFormat dateFormat = new SimpleDateFormat("E");
						datactuelle = dateFormat.format(date);
						
						
						Date dateret; 
						String daterech ;
						Calendar cal = Calendar.getInstance(); 
					
						switch(datactuelle.substring(0,3)) 
						{
						case "ven":
							cal.add(Calendar.DATE, +1);
							 dateret =cal.getTime();
							dateFormat = new SimpleDateFormat("dd-MM-yyyy");
							daterech =dateFormat.format(dateret); 
							UpedateTableEventJours(daterech);
							break;
						case "sam":

							UpedateTableEvent();
							break;
							
						case "dim":

							cal.add(Calendar.DATE, +6); 
							 dateret =cal.getTime();
							dateFormat = new SimpleDateFormat("dd-MM-yyyy");
							daterech =dateFormat.format(dateret); 
							UpedateTableEventJours(daterech);
							break;
							
						case "lun":

							cal.add(Calendar.DATE, +5);
							 dateret =cal.getTime();
							dateFormat = new SimpleDateFormat("dd-MM-yyyy");
							daterech =dateFormat.format(dateret); 
							UpedateTableEventJours(daterech);
							break;
						case "mar":

							cal.add(Calendar.DATE, +4); 
							 dateret =cal.getTime();
							dateFormat = new SimpleDateFormat("dd-MM-yyyy");
							daterech =dateFormat.format(dateret); 
							UpedateTableEventJours(daterech);
							break;
						case "mer":

							cal.add(Calendar.DATE, +3); 
							 dateret =cal.getTime();
							dateFormat = new SimpleDateFormat("dd-MM-yyyy");
							daterech =dateFormat.format(dateret); 
							UpedateTableEventJours(daterech);
							break;
						case "jeu":

							cal.add(Calendar.DATE, +2); 
							 dateret =cal.getTime();
							dateFormat = new SimpleDateFormat("dd-MM-yyyy");
							daterech =dateFormat.format(dateret); 
							UpedateTableEventJours(daterech);
							break;
						default:
							
						}		
						
					}
				});
		btnSamedi.setForeground(Color.WHITE);
		btnSamedi.setBackground(new Color(147, 112, 219));
		btnSamedi.setBounds(-14, 327, 94, 27);
		contentPane.add(btnSamedi);
		
//Boutton home//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
				JButton btnHome = new JButton("");
				btnHome.addMouseListener(new MouseAdapter() {
					@Override
					public void mouseEntered(MouseEvent e) {
						btnHome.setIcon(new ImageIcon(GestionEvent.class.getResource("/Log_in_img/home selected.png")));
					}
					@Override
					public void mouseExited(MouseEvent e) {
						btnHome.setIcon(new ImageIcon(GestionEvent.class.getResource("/Log_in_img/home.png")));
					}
				});
				btnHome.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						MenuPrincipaleFrame FramePrincipale = new MenuPrincipaleFrame(ID_ADHERENT);// retourner au menu medecin
						FramePrincipale.setLocationRelativeTo(null);
						FramePrincipale.setVisible(true);
							dispose();//fermer la fenetre consultation
							
					}
				});
				btnHome.setIcon(new ImageIcon(GestionEvent.class.getResource("/Log_in_img/home.png")));
				btnHome.setToolTipText("Retourner au Menu");
				btnHome.setBounds(1114, 5, 32, 32);
				contentPane.add(btnHome);
		
		
//le background////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		JLabel BGStaff = new JLabel("BGStaffM");
		BGStaff.setIcon(new ImageIcon(GestionEvent.class.getResource("/GestionEvent_img/Gestion des evenements (1).png")));
		BGStaff.setBounds(0, 0, 1200, 550);
		contentPane.add(BGStaff);
		
	}
	
//methode du style des buttons/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private void ButtonStyle(JButton btn) {
		btn.setOpaque(false);
		 btn.setFocusPainted(false);
		 btn.setBorderPainted(false);
		 btn.setContentAreaFilled(false);
	}

//Actualisation de table RDV la grande/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	public void UpedateTableEvent()
	{ 
		//recuperer la date du systeme
		Date actuelle = new Date();
		DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
		datactuelle = dateFormat.format(actuelle);
		
		
		String sql ="Select ID_Event,NomEvent,InEvent,DateEvent,HeureEvent from Evenements where DateEvent ='"+datactuelle +"'";
		try {
			prepared = cnx.prepareStatement(sql);
			resultat =prepared.executeQuery();
			tableEvent.setModel(DbUtils.resultSetToTableModel(resultat));
			
		
			TableRowSorter<TableModel> sorter = new TableRowSorter<TableModel>(tableEvent.getModel());
			tableEvent.setRowSorter(sorter);
			List<RowSorter.SortKey> sortKeys = new ArrayList<>(25);
			sortKeys.add(new RowSorter.SortKey(4, SortOrder.ASCENDING));		
			sorter.setSortKeys(sortKeys);
			
			tableEvent.getColumnModel().getColumn( 0 ).setHeaderValue("N °");
			tableEvent.getColumnModel().getColumn( 1 ).setHeaderValue("Titre Evenement");	
			tableEvent.getColumnModel().getColumn( 2 ).setHeaderValue("Description");
			tableEvent.getColumnModel().getColumn( 3 ).setHeaderValue("Date evenement");
			tableEvent.getColumnModel().getColumn( 4 ).setHeaderValue("Heure evenement");
			
		
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			JOptionPane.showMessageDialog(null,e);
		}
		
	}


//Methode pour actualiser la mini table //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////		
		public void UpedateTableEventJours(String Date)
		{ 	
			String sql ="Select ID_Event,NomEvent,InEvent,DateEvent,HeureEvent from Evenements where DateEvent ='"+Date +"'";
		try {
			
			prepared = cnx.prepareStatement(sql);
			resultat =prepared.executeQuery();
			tableEvent.setModel(DbUtils.resultSetToTableModel(resultat));

			TableRowSorter<TableModel> sorter = new TableRowSorter<TableModel>(tableEvent.getModel());
			tableEvent.setRowSorter(sorter);
			List<RowSorter.SortKey> sortKeys = new ArrayList<>(25);
			sortKeys.add(new RowSorter.SortKey(4, SortOrder.ASCENDING));		
			sorter.setSortKeys(sortKeys);
			 
			tableEvent.getColumnModel().getColumn( 0 ).setHeaderValue("N °");
			tableEvent.getColumnModel().getColumn( 1 ).setHeaderValue("Titre Evenement");	
			tableEvent.getColumnModel().getColumn( 2 ).setHeaderValue("Description");
			tableEvent.getColumnModel().getColumn( 3 ).setHeaderValue("Date evenement");
			tableEvent.getColumnModel().getColumn( 4 ).setHeaderValue("Heure evenement");
			 
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			JOptionPane.showMessageDialog(null,e);
		}
		}
}
	
	

