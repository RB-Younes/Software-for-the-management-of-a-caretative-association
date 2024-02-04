import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.SystemColor;
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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.RowSorter;
import javax.swing.SortOrder;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

import com.formdev.flatlaf.FlatIntelliJLaf;

import net.proteanit.sql.DbUtils;
import javax.swing.JRadioButton;

////////////////////////////////////////////////////////////////////////////////-----------Fenetre Consultation------------///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

public class PsyConsultationFrame extends JFrame {

	
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTable tableCons;
	private JButton btnAddCons;
	private JButton btnSuppCons;
	private JButton btnModCons;

	
	Connection cnx=null;
	PreparedStatement prepared = null;
	ResultSet resultat =null; 
	PreparedStatement prepared1 = null;
	ResultSet resultat1 =null; 
	
	private int posX = 0;   //Position X de la souris au clic
    private int posY = 0;   //Position Y de la souris au clic
	private String ID_PC;
	private JTable tablePER; //table RDV
	@SuppressWarnings("unused")
	private JTextArea textAreaOBS;
	private String ID_PER;
	private JTextField txtrecherche;
	private JList<String> listPersonne;
	
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
					PsyConsultationFrame frame = new PsyConsultationFrame("ReMo1");
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
	
public PsyConsultationFrame(String ID_ADHERENT) {
		setIconImage(Toolkit.getDefaultToolkit().getImage(PsyConsultationFrame.class.getResource("/PsyConsultation_img/medical-report.png")));//faire passer le id du medecin pour afficher que les rdv concernanat ce medecin
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

// initialisation////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		
        setUndecorated(true);
		setShape(new RoundRectangle2D.Double(0d, 0d, 1200d, 680, 25d, 25d));
		setResizable(false);
		setTitle("Staff Management");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(0, 0, 1200, 620);
		setLocationRelativeTo(null);
		
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
	//Combo BOX Patients---------------------------------------------------------------------------------------------------------------------------------------------
		JComboBox<String> comboBoxPersonne = new JComboBox<String>();
		
		comboBoxPersonne.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (comboBoxPersonne.getSelectedIndex()!=0) {//choisir le patient
					String PatInfo=comboBoxPersonne.getSelectedItem().toString();
					 int finIDindice=PatInfo.indexOf("-");//indice de fin d l'id
					 if (finIDindice != -1) 
					 {
						   ID_PER= PatInfo.substring(0 , finIDindice); //recuperer l'id du patient
						   String Nom_Prenom=PatInfo.substring(finIDindice +1, PatInfo.length());
						   txtrecherche.setText(Nom_Prenom);
					 }
					 UpedateTablecons(ID_PER);
					 btnAddCons.setEnabled(true);
				}
			}
		});
		comboBoxPersonne.addItem("Selectionner Patient");
		comboBoxPersonne.setBackground(new Color(255, 165, 0));
		comboBoxPersonne.setForeground(new Color(255, 255, 255));
		comboBoxPersonne.setBounds(11, 211, 268, 44);
		contentPane.add(comboBoxPersonne);
		INComboboxPersonne(comboBoxPersonne);//actualiser
		//RADIO//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		JRadioButton R1 = new JRadioButton("");
		R1.setBounds(173, 307, 21, 23);
		contentPane.add(R1);
		
		JRadioButton R2 = new JRadioButton("");
		R2.setBounds(354, 307, 21, 23);
		contentPane.add(R2);
		
		JRadioButton R3 = new JRadioButton("");
		R3.setBounds(506, 307, 21, 23);
		contentPane.add(R3);
		
		JRadioButton R4 = new JRadioButton("");
		R4.setBounds(256, 335, 21, 23);
		contentPane.add(R4);
		
		JRadioButton R5 = new JRadioButton("");
		R5.setBounds(474, 335, 21, 23);
		contentPane.add(R5);
		
		JRadioButton R6 = new JRadioButton("");
		R6.setBounds(219, 373, 21, 23);
		contentPane.add(R6);
		
		JRadioButton R7 = new JRadioButton("");
		R7.setBounds(506, 373, 21, 23);
		contentPane.add(R7);
		
		//list Personne**********************************************************************************************************************************
				JScrollPane scrollPanePersonne = new JScrollPane();
				scrollPanePersonne.setViewportBorder(new TitledBorder(null, "", TitledBorder.LEADING, TitledBorder.TOP, null, Color.GRAY));
				scrollPanePersonne.setBackground(Color.LIGHT_GRAY);
				scrollPanePersonne.setBounds(289, 135, 245, 127);
				scrollPanePersonne.setBorder(null);
				contentPane.add(scrollPanePersonne);
				
				
				listPersonne = new JList<String>();
				scrollPanePersonne.setViewportView(listPersonne);
				listPersonne.addMouseListener(new MouseAdapter() {
					@Override
					public void mouseClicked(MouseEvent e) {
						
						for (int j = 0; j < comboBoxPersonne.getItemCount(); j++) //parcourir la comboBox des patients
						{
							 String Name_pat=" ";
							 String FirstName_pat=" ";

							String PatInfo=comboBoxPersonne.getItemAt(j).toString();//recuperer le contenu de la  combobox a l'element numero j
							
						
								 int finIDindice=PatInfo.indexOf("-");//marque la fin de l'ID
								 int finnomindice=PatInfo.indexOf(" ");//la fin du Nom
								 int finprenomindice=PatInfo.length();//la fin du prenom
								 if (finIDindice != -1) 
								 {
									  Name_pat=PatInfo.substring(finIDindice+1 , finnomindice);//recupperer le NOM
									  FirstName_pat=PatInfo.substring(finnomindice+1 , finprenomindice);//recuperer le Prenom 
									
								 }
							String Nom_Prenom=Name_pat+" "+FirstName_pat;//sous format Nom" "Prenom
							
								if (Nom_Prenom.equals(listPersonne.getSelectedValue())) {
									comboBoxPersonne.setSelectedIndex(j);
									//selectionner l'element correspandant 
								}
						}
						txtrecherche.setText(listPersonne.getSelectedValue());
					}
				});
				listPersonne.setOpaque(false);
				listPersonne.setBackground(SystemColor.control);
				listPersonne.setForeground(new Color(255, 165, 0));
				listPersonne.setFont(new Font("Segoe UI", Font.BOLD, 12));
				UpdateListPEB(listPersonne);	
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
		txtrecherche.setBounds(22, 135, 226, 30);
		txtrecherche.setForeground(new Color(255, 165, 0));
		txtrecherche.setFont(new Font("Segoe UI", Font.BOLD, 14));
		txtrecherche.setBackground(Color.WHITE);
		txtrecherche.setBorder(null);
		contentPane.add(txtrecherche);
		txtrecherche.setColumns(10);
	//Diagnostique+scrollpane-----------------------------------------------------------------------------------------------------------------------------------------------------
		JScrollPane scrDiag = new JScrollPane();
		scrDiag.setSize(600, 141);
		scrDiag.setLocation(576, 221);
		contentPane.add(scrDiag);
		textAreaOBS = new JTextArea();
		scrDiag.setViewportView(textAreaOBS);
		textAreaOBS.addKeyListener(new KeyAdapter() {
		    public void keyTyped(KeyEvent e) { 
		        if (		textAreaOBS.getText().length() >= 4000 ) 
		            e.consume(); 
		    }  
		});
		textAreaOBS.setText("1)- Please state your gender?\r\n\n" + 
				"2)- How old are you?\r\n\n" + 
				"3)- Please state your ethnicity?\r\n\n" + 
				"4)- In which country did you spend most of your life?\r\n\n" + 
				"5)- What is the highest level of education you have completed?\r\n\n" +  
				"6)- What is your current employment status?\r\n\n" + 
				"7)- What is your current relationship status?\r\n\n" + 
				"8)- How many children do you have\r\n\n" + 
				"9)- On a scale of 1 to 7, 7 being the highest, how happy do you consider yourself?\r\n\n" + 
				"10)- Compare to your colleagues or friends, do you consider yourself happier than most of them?\r\n\\n" + 
				"11)- How often do you feel lonely?\r\n\n\n" + 
				"12)- How often do you feel depressed?\r\n\n" + 
				"13)- Please state your level of agreement for the following statements: (Completely agree, agree, neutral, disagree, completely disagree)\r\n\n" + 
				"14)- How often do you procrastinate about that status of your personal goals in life?\r\n\n" + 
				"15)- Do you have any other concerns that you would like to mention?\n");
		textAreaOBS.setFont(new Font("Segoe UI", Font.PLAIN, 13));
		textAreaOBS.setForeground(new Color(255, 255, 255));
		textAreaOBS.setBackground(new Color(255, 165, 0));
		
//ScrollPane Table Patient////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////		
	//Grande table *TABLE CONSULTATION****************************************************************************************************************************************************************************************	
		JScrollPane scrollPaneCons = new JScrollPane();
		scrollPaneCons.setViewportBorder(new TitledBorder(null, "", TitledBorder.LEADING, TitledBorder.TOP, null, Color.GRAY));
		scrollPaneCons.setBorder(null);
		scrollPaneCons.setBounds(26, 462, 1156, 136);
		contentPane.add(scrollPaneCons);
		
		tableCons = new JTable();
		tableCons.setFont(new Font("Segoe UI", Font.BOLD, 12));
		tableCons.setForeground(new Color(255, 255, 255));
		tableCons.setBorder(null);
		tableCons.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				btnModCons.setEnabled(true);
				btnSuppCons.setEnabled(true);
				
				int ligne=tableCons.getSelectedRow();

				ID_PC =tableCons.getValueAt(ligne, 0).toString();
				String sql="Select * from PsyConsult where ID_PC='"+ID_PC+"'";
				try {
					prepared=cnx.prepareStatement(sql);
					resultat=prepared.executeQuery();
					if(resultat.next())
					{
						String OBS=resultat.getString("Obs");
						textAreaOBS.setText(OBS);
						
						R1.setSelected(false);
						R2.setSelected(false);
						R3.setSelected(false);
						R4.setSelected(false);
						R5.setSelected(false);
						R6.setSelected(false);
						R7.setSelected(false);
						
						String Etat=resultat.getString("Etat");
						String EtatFinal[] = Etat.split("/"); 
						for(String s:EtatFinal) 
						{
							switch(s) {
							case "Dépression":
								R1.setSelected(true);
								break;
							case "Anxiété - Stress":
								R2.setSelected(true);
								break;
							case "Emotionnelles":
								R3.setSelected(true);
								break;
							case "Troubles cognitifs":
								R4.setSelected(true);
								break;
							case "Eléments délirants":
								R5.setSelected(true);
								break;
							case "Eléments psycho traumatiques identifiés":
								R6.setSelected(true);
								break;
							case "Relations sociales et familiales":
								R7.setSelected(true);
								break;
							}
							
						}
						
						
					}
				
				} catch (SQLException  e) {
					// TODO Auto-generated catch block
					JOptionPane.showMessageDialog(null,e);
				}
			}
		});
		tableCons.setBackground(new Color(255, 165, 0));
		scrollPaneCons.setViewportView(tableCons);
	
	//Mini table Table personne***************************************************************************************************************************************************************************************	
		JScrollPane scrollPanePER = new JScrollPane();
		scrollPanePER.setBounds(576, 52, 437, 120);
		contentPane.add(scrollPanePER);
		tablePER = new JTable();
		tablePER.setFont(new Font("Segoe UI", Font.BOLD, 12));
		tablePER.setForeground(new Color(255, 255, 255));
		tablePER.setBorder(null);
		tablePER.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				btnAddCons.setEnabled(true);
				
				int ligne=tablePER.getSelectedRow();//recuperer le numero de ligne
				ID_PER =tablePER.getValueAt(ligne, 0).toString();///recuperer l'ID du RDV qui est a la premiere colone (0) de la table
				
				for (int i = 1; i <comboBoxPersonne.getItemCount() ; i++) {
					   if (ID_PER.equals(comboBoxPersonne.getItemAt(i).toString().substring(0 , comboBoxPersonne.getItemAt(i).toString().indexOf("-")))) //retirer l'ID de la combobox
					   {// selectionner cet element
						   comboBoxPersonne.setSelectedIndex(i);
					   } 
				}
				UpedateTablecons(ID_PER);
			}
		});
		tablePER.setBackground(new Color(255, 165, 0));
		scrollPanePER.setViewportView(tablePER);
		
// Bouton Reduire ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		JButton Minimise_BTN = new JButton("");
		Minimise_BTN.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				Minimise_BTN.setIcon(new ImageIcon(PsyConsultationFrame.class.getResource("/Log_in_img/Minimize ML selected.png")));//changer les couleurs button
			}
			@Override
			public void mouseExited(MouseEvent e) {
				Minimise_BTN.setIcon(new ImageIcon(PsyConsultationFrame.class.getResource("/Log_in_img/Minimize ML .png")));//remetre le bouton de base
			}
		});
		Minimise_BTN.setToolTipText("Minimize");
		Minimise_BTN.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setState(ICONIFIED);
				
			}
		});
		Minimise_BTN.setIcon(new ImageIcon(PsyConsultationFrame.class.getResource("/Log_in_img/Minimize ML .png")));
		Minimise_BTN.setBounds(1064, 11, 32, 32);
		ButtonStyle(Minimise_BTN);
		contentPane.add(Minimise_BTN);

		
// Bouton Exit ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		JButton Exit_BTN = new JButton("");
		Exit_BTN.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent arg0) {
				Exit_BTN.setIcon(new ImageIcon(PsyConsultationFrame.class.getResource("/Log_in_img/Exit ML selected.png")));//changer les couleurs button
			}
			@Override
			public void mouseExited(MouseEvent arg0) {
				Exit_BTN.setIcon(new ImageIcon(PsyConsultationFrame.class.getResource("/Log_in_img/Exit ML.png")));//remetre le bouton de base
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
		Exit_BTN.setIcon(new ImageIcon(PsyConsultationFrame.class.getResource("/Log_in_img/Exit ML.png")));
		Exit_BTN.setBounds(1148, 11, 32, 32);
		ButtonStyle(Exit_BTN);
		contentPane.add(Exit_BTN);

//Actualiser la table des personnes /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////		
		UpedateTablePersonne();

//Boutons Ajouter,Supprimer,Modifier///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	//Supprimer une consultation**********************************************************************************************************************************************************************************************************************************
		btnSuppCons = new JButton();
		btnSuppCons.setEnabled(false);//desactiver le bouton tant que acune ligne de la table n'a ete selectionné
		btnSuppCons.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				if (btnSuppCons.isEnabled()) {
					btnSuppCons.setIcon(new ImageIcon(PsyConsultationFrame.class.getResource("/PsyConsultation_img/remove selected.png")));//changer les couleurs button
				}
			}
			@Override
			public void mouseExited(MouseEvent e) {
				if (btnSuppCons.isEnabled()) {
					btnSuppCons.setIcon(new ImageIcon(PsyConsultationFrame.class.getResource("/PsyConsultation_img/remove.png")));//remetre le bouton de base
				}
			}
		});
		ButtonStyle(btnSuppCons);
		btnSuppCons.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				if (tableCons.getSelectedRow()==-1) // return  quand aucune ligne esr selectionnée
				{//message d'erreur
					JOptionPane.showMessageDialog(null, " S'il vous plait veuillez selectioner une ligne(Consultation) a supprimer !","WArning" ,JOptionPane.PLAIN_MESSAGE, new ImageIcon(PsyConsultationFrame.class.getResource("/messages_img/error.png")));

				}
				else {
					ImageIcon icon = new ImageIcon(PsyConsultationFrame.class.getResource("/messages_img/Questions menu medcin.gif"));//Animation "Question"
					 icon.getImage().flush(); // réinitialiser l'animation
					//message de confirmation
					int ClickedButton	=JOptionPane.showConfirmDialog(null, "Vouler Vous vraiment Supprimer cette consultations ?", "Confirmation",JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, icon);
				if(ClickedButton==JOptionPane.YES_OPTION)
				{	
					PsyConsultation Cons =new PsyConsultation();
					Cons.Supprimer(ID_PC);
					UpedateTablecons(ID_PER);
					btnAddCons.setEnabled(false);
					btnModCons.setEnabled(false);
					btnSuppCons.setEnabled(false);
				}
				}
				
				
			}
		});
		btnSuppCons.setToolTipText("Supprimer une Consultation");
		btnSuppCons.setIcon(new ImageIcon(PsyConsultationFrame.class.getResource("/PsyConsultation_img/remove.png")));
		btnSuppCons.setBounds(924, 393, 32, 32);
		contentPane.add(btnSuppCons);
		
		
	//Ajouter une consultation**********************************************************************************************************************************************************************************************************************************
		btnAddCons = new JButton();
		btnAddCons.setEnabled(false);//desactiver le bouton tant que acune ligne de la table RDV(mini table) n'a ete selectionné
		btnAddCons.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent arg0) {
				if (btnAddCons.isEnabled()) {
					btnAddCons.setIcon(new ImageIcon(PsyConsultationFrame.class.getResource("/PsyConsultation_img/plus selected.png")));//changer les couleurs button
				}
			}
			@Override
			public void mouseExited(MouseEvent e) {
				if (btnAddCons.isEnabled()) {
					btnAddCons.setIcon(new ImageIcon(PsyConsultationFrame.class.getResource("/PsyConsultation_img/plus.png")));//remetre le bouton de base
				}
			}
		});
		ButtonStyle(btnAddCons);
		btnAddCons.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				 ImageIcon icon = new ImageIcon(PsyConsultationFrame.class.getResource("/messages_img/Questions menu medcin.gif"));//Animation "Question"
				 icon.getImage().flush(); // réinitialiser l'animation
				//message de confirmation
				int ClickedButton	=JOptionPane.showConfirmDialog(null, "Vouler Vous vraiment Ajouter une consultation a cette personne?", "Confirmation",JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, icon);
				if(ClickedButton==JOptionPane.YES_OPTION)
				{
					String Etat="";
					if(R1.isSelected()) {Etat="/Dépression";} 
					if(R2.isSelected()) {Etat=Etat+"/Anxiété - Stress";} 
					if(R3.isSelected()) {Etat=Etat+"/Emotionnelles";} 
					if(R4.isSelected()) {Etat=Etat+"/Troubles cognitifs";} 
					if(R5.isSelected()) {Etat=Etat+"/Eléments délirants";} 
					if(R6.isSelected()) {Etat=Etat+"/Eléments psycho traumatiques identifiés";} 
					if(R7.isSelected()) {Etat=Etat+"/Relations sociales et familiales";} 
					
					String PerInfo=comboBoxPersonne.getSelectedItem().toString();
					if (!PerInfo.equals("Selectionner Patient")) {
						String OBS=textAreaOBS.getText();
						if (!OBS.equals("")&& !Etat.equals("")) {
							Date actuelle = new Date();
							DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
							String datactuelle = dateFormat.format(actuelle);
			 			
							DateFormat HaureFormat = new SimpleDateFormat("HH:mm");
							String heureactuelle = HaureFormat.format(actuelle);
							
							PsyConsultation Cons =new PsyConsultation(ID_PER, datactuelle, heureactuelle, OBS,Etat);
							Cons.Ajouter();
							UpedateTablecons(ID_PER);
							btnAddCons.setEnabled(false);
							btnModCons.setEnabled(false);
							btnSuppCons.setEnabled(false);
							
						}
						else {
							JOptionPane.showMessageDialog(null, " S'il vous plait veuillez Donner une Observation & un etat(s)!","!", JOptionPane.QUESTION_MESSAGE, new ImageIcon(PsyConsultationFrame.class.getResource("/messages_img/error.png")));
						}
					}
					else {
						JOptionPane.showMessageDialog(null, " S'il vous plait veuillez Selectionner une personne de la table personne ou la liste personne !","!", JOptionPane.QUESTION_MESSAGE, new ImageIcon(PsyConsultationFrame.class.getResource("/messages_img/error.png")));
					}
			
				}
			}
		});
		btnAddCons.setToolTipText("Ajouter Consultation");
		btnAddCons.setIcon(new ImageIcon(PsyConsultationFrame.class.getResource("/PsyConsultation_img/plus.png")));
		btnAddCons.setBounds(807, 393, 32, 32);
		contentPane.add(btnAddCons);
		
	//Modifie une consultation**********************************************************************************************************************************************************************************************************************************
		btnModCons = new JButton();
		btnModCons.setEnabled(false);//desactiver le bouton tant que acune ligne de la table consultation n'a ete selectionné
		btnModCons.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				if (btnModCons.isEnabled()) {
					btnModCons.setIcon(new ImageIcon(PsyConsultationFrame.class.getResource("/PsyConsultation_img/pencil selected.png")));//changer les couleurs button
				}
			}
			@Override
			public void mouseExited(MouseEvent e) {
				if (btnModCons.isEnabled()) {
					btnModCons.setIcon(new ImageIcon(PsyConsultationFrame.class.getResource("/PsyConsultation_img/pencil.png")));//remetre le bouton de base
				}
			}
		});
		ButtonStyle(btnModCons);
		btnModCons.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				 ImageIcon icon = new ImageIcon(PsyConsultationFrame.class.getResource("/messages_img/Questions menu medcin.gif"));//Animation "Question"
				 icon.getImage().flush(); // réinitialiser l'animation
				//message de confirmation
				int ClickedButton	=JOptionPane.showConfirmDialog(null, "Vouler Vous vraiment Modifier cette Consultation?", "Confirmation",JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, icon);
				if (ClickedButton==JOptionPane.YES_OPTION) {
					if (tableCons.getSelectedRow()==-1 || comboBoxPersonne.getSelectedIndex()==0 )  
						{//message
						JOptionPane.showMessageDialog(null, "S'il vous plait veuillez selectioner une ligne de la table consultation a modifer !","Warning" ,JOptionPane.PLAIN_MESSAGE, new ImageIcon(PsyConsultationFrame.class.getResource("/messages_img/error.png")));
						}
					else 
					{
						String OBS=textAreaOBS.getText();
						String Etat="";
						if(R1.isSelected()) {Etat="/Dépression";} 
						if(R2.isSelected()) {Etat=Etat+"/Anxiété - Stress";} 
						if(R3.isSelected()) {Etat=Etat+"/Emotionnelles";} 
						if(R4.isSelected()) {Etat=Etat+"/Troubles cognitifs";} 
						if(R5.isSelected()) {Etat=Etat+"/Eléments délirants";} 
						if(R6.isSelected()) {Etat=Etat+"/Eléments psycho traumatiques identifiés";} 
						if(R7.isSelected()) {Etat=Etat+"/Relations sociales et familiales";} 
						
						if (!OBS.equals("")) {
							
						PsyConsultation Cons =new PsyConsultation();
						Cons.Modifier(ID_PC, OBS,Etat);
						UpedateTablecons(ID_PER);
						btnAddCons.setEnabled(false);
						btnModCons.setEnabled(false);
						btnSuppCons.setEnabled(false);
					}
					else {
						JOptionPane.showMessageDialog(null, " S'il vous plait veuillez Donner une Observation & un etat(s) !","!", JOptionPane.QUESTION_MESSAGE, new ImageIcon(PsyConsultationFrame.class.getResource("/messages_img/error.png")));
						}
					}
				}
				
				
			}
		});
		btnModCons.setToolTipText("Modifier info Consutation");
		btnModCons.setIcon(new ImageIcon(PsyConsultationFrame.class.getResource("/PsyConsultation_img/pencil.png")));
		btnModCons.setBounds(861, 393, 32, 32);
		contentPane.add(btnModCons);
		
//Boutton home//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
				JButton btnHome = new JButton("");
				btnHome.setIcon(new ImageIcon(PsyConsultationFrame.class.getResource("/Log_in_img/home.png")));
				btnHome.addMouseListener(new MouseAdapter() {
					@Override
					public void mouseEntered(MouseEvent e) {
						btnHome.setIcon(new ImageIcon(PsyConsultationFrame.class.getResource("/Log_in_img/home selected.png")));//changer les couleurs button
					}
					@Override
					public void mouseExited(MouseEvent e) {
						btnHome.setIcon(new ImageIcon(PsyConsultationFrame.class.getResource("/Log_in_img/home.png")));//remetre le bouton de base
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
				btnHome.setToolTipText("Retourner au Menu");
				btnHome.setBounds(1106, 11, 32, 32);
				ButtonStyle(btnHome);
				contentPane.add(btnHome);
				
				
				JButton btnViderFields = new JButton("");
				btnViderFields.setToolTipText("Clear the fields");
				ButtonStyle(btnViderFields);
				btnViderFields.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
					
						comboBoxPersonne.setSelectedIndex(0);
						txtrecherche.setText("");
						textAreaOBS.setText("1)- Please state your gender?\r\n\n" + 
								"2)- How old are you?\r\n\n" + 
								"3)- Please state your ethnicity?\r\n\n" + 
								"4)- In which country did you spend most of your life?\r\n\n" + 
								"5)- What is the highest level of education you have completed?\r\n\n" +  
								"6)- What is your current employment status?\r\n\n" + 
								"7)- What is your current relationship status?\r\n\n" + 
								"8)- How many children do you have\r\n\n" + 
								"9)- On a scale of 1 to 7, 7 being the highest, how happy do you consider yourself?\r\n\n" + 
								"10)- Compare to your colleagues or friends, do you consider yourself happier than most of them?\r\n\\n" + 
								"11)- How often do you feel lonely?\r\n\n\n" + 
								"12)- How often do you feel depressed?\r\n\n" + 
								"13)- Please state your level of agreement for the following statements: (Completely agree, agree, neutral, disagree, completely disagree)\r\n\n" + 
								"14)- How often do you procrastinate about that status of your personal goals in life?\r\n\n" + 
								"15)- Do you have any other concerns that you would like to mention?\n");
						
						R1.setSelected(false);
						R2.setSelected(false);
						R3.setSelected(false);
						R4.setSelected(false);
						R5.setSelected(false);
						R6.setSelected(false);
						R7.setSelected(false);
						UpedateTablecons("");
						UpedateTablePersonne();
						btnAddCons.setEnabled(false);
						btnModCons.setEnabled(false);
						btnSuppCons.setEnabled(false);
						
					}
				});
				btnViderFields.setIcon(new ImageIcon(GestionPerEnBesoin.class.getResource("/GestionAD_img/trash.png")));
				btnViderFields.setBounds(981, 393, 32, 32);
				contentPane.add(btnViderFields);
		
	
		
// Fields////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////		
		//le bale pour l'arrier plan----------------------------------------------------------------------------------------------------------------------------------------------------------
		JLabel BGPsyCo = new JLabel("BG");
		BGPsyCo.setBounds(0, 0, 1217, 620);
		contentPane.add(BGPsyCo);
		BGPsyCo.setIcon(new ImageIcon(PsyConsultationFrame.class.getResource("/PsyConsultation_img/Consultation.png")));
	}
	
//methode du style des buttons/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	private void ButtonStyle(JButton btn) {
		btn.setOpaque(false);
		 btn.setFocusPainted(false);
		 btn.setBorderPainted(false);
		 btn.setContentAreaFilled(false);
	}



//actualiser la table consultation///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

public void UpedateTablecons(String ID_PER)
		{ 	
	
				String sql ="Select ID_PC,DateCons,HeureConsul,Obs,Etat from PsyConsult where ID_PEB='"+ ID_PER+"'";
			try {
				prepared = cnx.prepareStatement(sql);
				resultat =prepared.executeQuery();
				tableCons.setModel(DbUtils.resultSetToTableModel(resultat));//changer le model de la table
				//ajouter un sorter pour trier la table
				TableRowSorter<TableModel> sorter = new TableRowSorter<TableModel>(tableCons.getModel());
				tableCons.setRowSorter(sorter);
				List<RowSorter.SortKey> sortKeys = new ArrayList<>(25);
				sortKeys.add(new RowSorter.SortKey(0, SortOrder.ASCENDING));//trier  par apport au Numero de la consultation
				sortKeys.add(new RowSorter.SortKey(1, SortOrder.ASCENDING));//sinon par apport a la date
				sorter.setSortKeys(sortKeys);
				tableCons.getColumnModel().getColumn( 0 ).setHeaderValue("N° Cons");
				tableCons.getColumnModel().getColumn( 1 ).setHeaderValue("Date");
				tableCons.getColumnModel().getColumn( 2 ).setHeaderValue("Heure");	
				tableCons.getColumnModel().getColumn( 3 ).setHeaderValue("Observation");
				tableCons.getColumnModel().getColumn( 3 ).setHeaderValue("Etat");
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				JOptionPane.showMessageDialog(null,e);
			}
		}
		
//actualiser la table RDV///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
public void UpedateTablePersonne()// elle va actualiser la table avec la date donné et le ID 
{ 	
	String sql ="Select ID_PEB,Nom,Prenom from PersoEnBesoin";
	try {
		prepared = cnx.prepareStatement(sql);
		resultat =prepared.executeQuery();
		tablePER.setModel(DbUtils.resultSetToTableModel(resultat));
		TableRowSorter<TableModel> sorter = new TableRowSorter<TableModel>(tablePER.getModel());
		tablePER.setRowSorter(sorter);
		List<RowSorter.SortKey> sortKeys = new ArrayList<>(25);
		sortKeys.add(new RowSorter.SortKey(0, SortOrder.ASCENDING));		
		sorter.setSortKeys(sortKeys);
		//renomer les  colones
		tablePER.getColumnModel().getColumn( 0 ).setHeaderValue("N°");
		tablePER.getColumnModel().getColumn( 1 ).setHeaderValue("Nom");	
		tablePER.getColumnModel().getColumn( 2 ).setHeaderValue("Prenom");

} catch (SQLException e) {
// TODO Auto-generated catch block
	JOptionPane.showMessageDialog(null,e);
}

	
}

//Dans la comboBox Patients/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
public void INComboboxPersonne(JComboBox<String> Per)
{ 
	String sql ="Select ID_PEB , Nom , Prenom from PersoEnBesoin "; 
	try {
		prepared = cnx.prepareStatement(sql);
		resultat =prepared.executeQuery();
		while(resultat.next())
		{
				 String item=resultat.getString("ID_PEB").toString()+"-"+resultat.getString("Nom").toString()+" "+resultat.getString("Prenom").toString();
				 Per.addItem(item);
		}
	} catch (SQLException e) {
		// TODO Auto-generated catch block
		JOptionPane.showMessageDialog(null,e);
	}
	
}

//Actualiser la liste des personnes/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
public void UpdateListPEB(JList<String> List)
		{ 
	
			String sql ="Select Nom ,Prenom  from PersoEnBesoin";
			try {
				prepared = cnx.prepareStatement(sql);
				resultat =prepared.executeQuery();
				ArrayList<String> L=new ArrayList<String>();//une liste qui va contenir les patients
				while(resultat.next())
				{	
					String item=resultat.getString("Nom").toString()+" "+resultat.getString("Prenom").toString();//sous format Nom Prenom
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
		String sql ="Select Nom ,Prenom  from PersoEnBesoin";
		try {
			prepared = cnx.prepareStatement(sql);
			resultat =prepared.executeQuery();
			ArrayList<String> L=new ArrayList<String>();//declarer une array list qui va contenir les nom+Prenom Des Patients
			while(resultat.next())
			{
				String item=resultat.getString("Nom").toString()+" "+resultat.getString("Prenom").toString();
				
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
		    listPersonne.setModel(defaultListModel);//changer le model de la list
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			JOptionPane.showMessageDialog(null,e);
		}
	}
}

	

