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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
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

////////////////////////////////////////////////////////////////////////////////-----------Fenetre Gestion Patients------------///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

public class GestionAD extends JFrame {

	
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTable tableAD;
	private JButton btnModADH;
	private JButton btnRemoveADH;
	
	
	Connection cnx=null;
	PreparedStatement prepared = null;
	ResultSet resultat =null; 
	ResultSet resultat1 =null; 
	
	private int posX = 0;   //Position X de la souris au clic
    private int posY = 0;   //Position Y de la souris au clic
    
    
	private JTextField NomField;
	private JTextField PrenomField;
	private JTextField TelField;
	private String id_AD;// le id recupere en cliquant sur le tableau
	private JComboBox<String> comboBoxWilaya;  
	protected JComboBox<String> comboBoxFonction;
	private JTextField LNField;
	private JTextField textFieldADRmail;
	private JTextField TypeField;
	private FonctionFrame frame = new FonctionFrame(null);
	
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
					GestionAD frame = new GestionAD("ReMo1");
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
	
	public GestionAD(String ID_ADHERENT) {
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

// initialisation//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////		
		setUndecorated(true);
		setShape(new RoundRectangle2D.Double(0d, 0d, 1200d, 550d, 25d, 25d));
		setResizable(false);
		setIconImage(Toolkit.getDefaultToolkit().getImage(GestionAD.class.getResource("/GestionAD_img/united.png")));
		setTitle("Gestion des Adherents");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(0, 0, 1200, 550);
		
		
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
// Fields//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		// textField Adresse
		JTextField AdrtextField = new JTextField();
		AdrtextField.addKeyListener(new KeyAdapter() {
		    public void keyTyped(KeyEvent e) { 
		        if (		AdrtextField.getText().length() >= 100 ) //limiter le nombre de cactere a 100
		            e.consume(); 
		    }  
		});
		AdrtextField.setForeground(new Color(255, 255, 255));
		AdrtextField.setFont(new Font("Segoe UI", Font.BOLD, 14));
		AdrtextField.setBorder(null);
		AdrtextField.setBounds(133, 409, 183, 28);
		AdrtextField.setOpaque(false);
		contentPane.add(AdrtextField);
		
		//textField NUMTel
		TelField = new JTextField();
		TelField.addKeyListener(new KeyAdapter() {
		    public void keyTyped(KeyEvent e) { 
		        if (		TelField.getText().length() >= 10 )
		            e.consume(); 
		    }  
		});
		TelField.addKeyListener(new KeyAdapter() {
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
		TelField.setForeground(new Color(255, 255, 255));
		TelField.setFont(new Font("Segoe UI", Font.BOLD, 14));
		TelField.setOpaque(false);
		TelField.setBorder(null);
		TelField.setBounds(133, 458, 183, 28);
		contentPane.add(TelField);
		TelField.setColumns(10);
		
		
		//checkBox Homme & Femme	
		JCheckBox CheckBoxFemme = new JCheckBox();
		CheckBoxFemme.setToolTipText("Femme");
		JCheckBox CheckBoxHomme = new JCheckBox();
		CheckBoxHomme.setToolTipText("Homme");
		CheckBoxFemme.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				
				CheckBoxHomme.setSelected(false);
			
			}
			
		});
		CheckBoxFemme.setBounds(215, 261, 21, 20);
		contentPane.add(CheckBoxFemme);
	
		CheckBoxHomme.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
		
				CheckBoxFemme.setSelected(false);
			}
		});
		CheckBoxHomme.setBounds(147, 261, 21, 20);
		contentPane.add(CheckBoxHomme);
	//textField Prenom	
		PrenomField = new JTextField();
		PrenomField.addKeyListener(new KeyAdapter() {
		    public void keyTyped(KeyEvent e) { 
		        if (		PrenomField.getText().length() >= 40 )
		            e.consume(); 
		    }  
		});
		PrenomField.setBounds(133, 210, 183, 28);
		PrenomField.setForeground(new Color(255, 255, 255));
		PrenomField.setFont(new Font("Segoe UI", Font.BOLD, 14));
		PrenomField.setOpaque(false);
		PrenomField.setBorder(null);
		contentPane.add(PrenomField);
		PrenomField.setColumns(10);
		
	//textField nom	
		NomField = new JTextField();
		NomField.addKeyListener(new KeyAdapter() {
		    public void keyTyped(KeyEvent e) { 
		        if (		NomField.getText().length() >= 40 ) 
		            e.consume(); 
		    }  
		});
		NomField.setBounds(133, 146, 183, 28);
		NomField.setForeground(new Color(255, 255, 255));
		NomField.setFont(new Font("Segoe UI", Font.BOLD, 14));
		NomField.setOpaque(false);
		NomField.setBorder(null);
		contentPane.add(NomField);
		NomField.setColumns(10);
		
	//textField Lieu de naissance	
		LNField = new JTextField();
		LNField.addKeyListener(new KeyAdapter() {
		    public void keyTyped(KeyEvent e) { 
		        if (		LNField.getText().length() >= 75 ) 
		            e.consume(); 
		    }  
		});
		LNField.setForeground(Color.WHITE);
		LNField.setFont(new Font("Segoe UI", Font.BOLD, 14));
		LNField.setColumns(10);
		LNField.setBorder(null);
		LNField.setOpaque(false);
		LNField.setBounds(133, 360, 183, 28);
		contentPane.add(LNField);
		
	//ComboBox Année
		JComboBox<Object> CBYear = new JComboBox<Object>();
		CBYear.setModel(new DefaultComboBoxModel<Object>(new String[] {"Anneé"}));		
		for (int i = 2020; i>=1900 ; i--) {
			String Year =""+i+"";
			CBYear.addItem(Year);		
			}
		CBYear.setBackground(new Color(255, 153, 153));
		CBYear.setForeground(new Color(255, 255, 255));
		CBYear.setBounds(252, 312, 70, 20);
		contentPane.add(CBYear);
		
	//ComboBox Mois	
		JComboBox<Object> CBMonth = new JComboBox<Object>();
		CBMonth.setModel(new DefaultComboBoxModel<Object>(new String[] {"Mois","01","02","03","04","05","06","07","08","09","10","11","12"	}));
		CBMonth.setBackground(new Color(255, 153, 153));
		CBMonth.setForeground(new Color(255, 255, 255));
		CBMonth.setBounds(184, 312, 70, 20);
		contentPane.add(CBMonth);
		
	//ComboBox jours
		JComboBox<Object> CBDay = new JComboBox<Object>();
		CBDay.setModel(new DefaultComboBoxModel<Object>(new String[] {"Jour"}));

		for (int j = 1; j<=31 ; j++) {
			
			if (j<=9) {
				String Day ="0"+j+"";CBDay.addItem(Day);	
			}
			else {
				String Day =""+j+"";CBDay.addItem(Day);	
			}
			
				
			}
		CBDay.setBackground(new Color(255, 153, 153));
		CBDay.setForeground(new Color(255, 255, 255));
		CBDay.setBounds(124, 312, 64, 20);
		contentPane.add(CBDay);
		
	//comboBox Wilaya
		comboBoxWilaya = new JComboBox<String>();
		comboBoxWilaya.setFont(new Font("Segoe UI", Font.BOLD, 12));
		comboBoxWilaya.setModel(new DefaultComboBoxModel<String>(new String[] {"Sélectionner Wilaya",
			" ADRAR" , "CHLEF" , "LAGHOUAT" ,"OUM BOUAGHI5", "BATNA" , "BEJAIA", "BISKRA", "BECHAR"  ,"BLIDA" , "BOUIRA"  ,"TAMANRASSET",
			"TEBESSA" ,"TLEMCEN", "TIARET","TIZI OUZOU", "ALGER" , "DJELFA" , "JIJEL" ,"SETIF" ,"SAIDA" ,"SKIKDA" ,"SIDI BEL ABBES",  "ANNABA",
			"GUELMA" , "CONSTANTINE" , "MEDEA" , "MOSTAGANEM",  "M'SILA",  "MASCARA" , "OUARGLA" , "ORAN" , "EL BAYDH" ,"ILLIZI" , "BORDJ BOU ARRERIDJ" , "BOUMERDES" , "EL TAREF",
			"TINDOUF" , "TISSEMSILT" , "EL OUED"  ,"KHENCHLA" , "SOUK AHRASS" , "TIPAZA"  , "MILA" , "AÏN DEFLA" ,"NÂAMA" , "AÏN TEMOUCHENT" , "GHARDAÏA"  ,"RELIZANE"} ));
		comboBoxWilaya.setBackground(new Color(255, 153, 153));
		comboBoxWilaya.setForeground(new Color(255, 255, 255));
		comboBoxWilaya.setBounds(862, 147, 183, 28);
		contentPane.add(comboBoxWilaya);
		
	//comboBox Fonction
		comboBoxFonction = new JComboBox<String>();
		comboBoxFonction.setBackground(new Color(255, 153, 153));
		comboBoxFonction.setFont(new Font("Segoe UI", Font.BOLD, 12));
		comboBoxFonction.setModel(new DefaultComboBoxModel<String>(new String[] {"Sélectionner Fonction"} ));
		INComboboxFonc(comboBoxFonction);
		comboBoxFonction.setForeground(new Color(255, 255, 255));
		comboBoxFonction.setBounds(465, 147, 183, 28);
		contentPane.add(comboBoxFonction);
		
	//textField Type
		TypeField = new JTextField();
		TypeField.addKeyListener(new KeyAdapter() {
		    public void keyTyped(KeyEvent e) { 
		        if (		TypeField.getText().length() >= 50) 
		            e.consume(); 
		    }  
		});
		TypeField.setForeground(Color.WHITE);
		TypeField.setFont(new Font("Segoe UI", Font.BOLD, 14));
		TypeField.setColumns(10);
		TypeField.setBorder(null);
		TypeField.setOpaque(false);
		TypeField.setBounds(465, 210, 183, 28);
		contentPane.add(TypeField);
		
	//textField Adresse mail	
		textFieldADRmail = new JTextField();
		textFieldADRmail.addKeyListener(new KeyAdapter() {
		    public void keyTyped(KeyEvent e) { 
		        if (		textFieldADRmail.getText().length() >= 100) 
		            e.consume(); 
		    }  
		});
		textFieldADRmail.setForeground(Color.WHITE);
		textFieldADRmail.setFont(new Font("Segoe UI", Font.BOLD, 14));
		textFieldADRmail.setColumns(10);
		textFieldADRmail.setBorder(null);
		textFieldADRmail.setOpaque(false);
		textFieldADRmail.setBounds(133, 504, 183, 28);
		contentPane.add(textFieldADRmail);
		
//ScrollPane/Table Patient//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		JScrollPane scrollPanePat = new JScrollPane();
		scrollPanePat.setViewportBorder(new TitledBorder(null, "", TitledBorder.LEADING, TitledBorder.TOP, null, Color.GRAY));
		scrollPanePat.setBackground(Color.WHITE);
		scrollPanePat.setBounds(346, 301, 844, 226);
		contentPane.add(scrollPanePat);
		
		tableAD = new JTable();
		tableAD.setFont(new Font("Segoe UI", Font.BOLD, 11));
		tableAD.setForeground(new Color(255, 255, 255));
		tableAD.setBorder(null);
		tableAD.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				//activer les deux boutons Modifer et supprimer
				btnRemoveADH.setEnabled(true);
				btnModADH.setEnabled(true);
				int ligne=tableAD.getSelectedRow();//recuperer le numero de ligne
				

				id_AD =tableAD.getValueAt(ligne, 0).toString();//recuperer l'ID du Adherent qui est a la premiere colone (0) de la table
				String sql="Select * from Adherent where ID_AD='"+id_AD+"'";//Selectionner toute les info du Patient aec l'ID selectionné de la bdd
				
				try {
					prepared=cnx.prepareStatement(sql);
					resultat=prepared.executeQuery();
					if(resultat.next())
					{
					NomField.setText(resultat.getString("Nom"));// mettre le Nom de  l'Adherent recuperé dans le TextField Nom
					PrenomField.setText(resultat.getString("Prenom"));// mettre le PreNom de l'Adherent dans le TextField PreNom
					
					
					String DN=resultat.getString("DateN");// recuperer la date de naissance du patient
				    if (DN!=null)
				    {
				    	DN=(String) DN.subSequence(0, 10);//pour ne pas prendre la partie HH:mm:ss
				    	//parcourir la combobox Année
				    	for (int i = 0; i < CBYear.getItemCount(); i++) 
						{
								if (CBYear.getItemAt(i).equals(DN.subSequence(0,4 ))) { CBYear.setSelectedIndex(i);}//selectionner cet element
							
						}
				    	//parcourir la combobox Mois
				    	for (int i = 0; i < CBMonth.getItemCount(); i++) 
						{
								if (CBMonth.getItemAt(i).equals(DN.subSequence(5,7 ))) { CBMonth.setSelectedIndex(i);}//selectionner cet element
							
						}
				    	//parcourir la combobox jour
				    	for (int i = 0; i < CBDay.getItemCount(); i++) 
						{
								if (CBDay.getItemAt(i).equals(DN.subSequence(8,10 ))) { CBDay.setSelectedIndex(i);}//selectionner cet element
						}

				    }
				    //selectionner la wilaya
				    for (int i = 0; i < comboBoxWilaya.getItemCount(); i++) 
					{
							
							if (comboBoxWilaya.getItemAt(i).equals(resultat.getString("Wilaya"))) { comboBoxWilaya.setSelectedIndex(i);}//selectionner cet element
					}
				    //recuperer le nom de la fonction
				    String Fonction = null; 
				    String code_F =resultat.getString("Code_F");
				    sql="Select NomF from Fonction where Code_F='"+code_F+"'";
					try {
						prepared=cnx.prepareStatement(sql);
						resultat1=prepared.executeQuery();
						if(resultat1.next())
						{
							 Fonction =resultat1.getString("NomF");
						}

					} catch (SQLException e) {
						// TODO Auto-generated catch block
						JOptionPane.showMessageDialog(null,e);
					}
					
				    for (int i = 0; i < comboBoxFonction.getItemCount(); i++) //selectionner la fonction
					{
							
							if (comboBoxFonction.getItemAt(i).equals(Fonction)) { comboBoxFonction.setSelectedIndex(i);}//selectionner cet element
					}
					String sexe=resultat.getString("sexe");// recuperer la sexe  du ADH
					if(sexe.equals("H")) {CheckBoxHomme.setSelected(true);CheckBoxFemme.setSelected(false);}// le cas Homme
					if(sexe.equals("F")) {CheckBoxFemme.setSelected(true);CheckBoxHomme.setSelected(false);}//le cas femme
					if(sexe.equals("null")) { CheckBoxFemme.setSelected(false);CheckBoxHomme.setSelected(false);}// le cas ou ce n'est pas mentionnée
						
					TypeField.setText(resultat.getString("Type"));// mettre le type recuperé dans le TextField profession
					LNField.setText(resultat.getString("LieuDN"));// mettre la profession du ADH recuperé dans le TextField profession
					AdrtextField.setText(resultat.getString("ADR"));// mettre l'adresse du ADH recuperé dans le TextField adresse
					TelField.setText(resultat.getString("numTel"));// mettre le Numero de tel du ADH recuperé dans le TextField Numero de tlephone
					textFieldADRmail.setText(resultat.getString("ADRmail"));// mettre l'adresse mail du ADH recuperé dans le TextField Nom
					
					}
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					JOptionPane.showMessageDialog(null,e);
				}
			}
		});
		tableAD.setBackground(new Color(255, 153, 153));
		scrollPanePat.setViewportView(tableAD);
//afficher le contenue de la table juste a louverture//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

		UpedateTableAD();
		
// Bouton Reduire ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		JButton Minimise_BTN = new JButton("");
		Minimise_BTN.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				Minimise_BTN.setIcon(new ImageIcon(GestionAD.class.getResource("/Log_in_img/Minimize ML selected.png")));//changer les couleurs button
			}
			@Override
			public void mouseExited(MouseEvent e) {
				Minimise_BTN.setIcon(new ImageIcon(GestionAD.class.getResource("/Log_in_img/Minimize ML .png")));	//remetre le bouton de base
			}
		});
		Minimise_BTN.setToolTipText("Minimize");
		Minimise_BTN.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setState(ICONIFIED);
			}
		});
		Minimise_BTN.setIcon(new ImageIcon(GestionAD.class.getResource("/Log_in_img/Minimize ML .png")));
		Minimise_BTN.setBounds(1039, 11, 32, 32);
		ButtonStyle(Minimise_BTN);
		contentPane.add(Minimise_BTN);
// Bouton Exit ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		JButton Exit_BTN = new JButton("");
		Exit_BTN.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent arg0) {
				Exit_BTN.setIcon(new ImageIcon(GestionAD.class.getResource("/Log_in_img/Exit ML selected.png")));//changer les couleurs button
			}
			@Override
			public void mouseExited(MouseEvent arg0) {
				Exit_BTN.setIcon(new ImageIcon(GestionAD.class.getResource("/Log_in_img/Exit ML.png")));//remetre le bouton de base
			}
		});
		Exit_BTN.setToolTipText("Exit");
		Exit_BTN.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			
					int ClickedButton	=JOptionPane.showConfirmDialog(null, "Do you really want to leave?", "Close", JOptionPane.YES_NO_OPTION);
					if(ClickedButton==JOptionPane.YES_OPTION)
					{
						//  vider les fields et remetre les combobox et check box par default(les valeurs)
						comboBoxFonction.setSelectedIndex(0);
						comboBoxWilaya.setSelectedIndex(0);
						CBYear.setSelectedIndex(0);
						CBDay.setSelectedIndex(0);
						CBMonth.setSelectedIndex(0);
						AdrtextField.setText("");
						TelField.setText("");
						NomField.setText("");	
						PrenomField.setText("");
						TypeField.setText("");
						LNField.setText("");
						CheckBoxFemme.setSelected(false);
						CheckBoxHomme.setSelected(false);
						textFieldADRmail.setText("");
						
						if (!frame.isVisible()) {//si la fenetre Prescrire traitement est vesible alors la fermer
							frame.dispose();
						}
						
						dispose();
					}
			}
			
		});
		Exit_BTN.setIcon(new ImageIcon(GestionAD.class.getResource("/Log_in_img/Exit ML.png")));
		ButtonStyle(Exit_BTN);
		Exit_BTN.setBounds(1123, 11, 32, 32);
		contentPane.add(Exit_BTN);
//Boutton home//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		JButton btnHome = new JButton("");
		btnHome.addMouseListener(new MouseAdapter() {
					@Override
					public void mouseEntered(MouseEvent e) {
						btnHome.setIcon(new ImageIcon(MenuPrincipaleFrame.class.getResource("/Log_in_img/home selected.png")));//changer les couleurs button
					}
					@Override
					public void mouseExited(MouseEvent e) {
						btnHome.setIcon(new ImageIcon(MenuPrincipaleFrame.class.getResource("/Log_in_img/home.png")));//remetre le bouton de base
					}
				});
		btnHome.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						
								MenuPrincipaleFrame frame = new MenuPrincipaleFrame(ID_ADHERENT);// retourner au menu medecin
								frame.setLocationRelativeTo(null);
								frame.setVisible(true);
							dispose();
	
					}
				});
		btnHome.setIcon(new ImageIcon(GestionAD.class.getResource("/Log_in_img/home.png")));
		btnHome.setToolTipText("Retourner au Menu");
		btnHome.setBounds(1081, 11, 32, 32);
		ButtonStyle(btnHome);
		contentPane.add(btnHome);	

// Vider les champs bouton////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		JButton btnViderFields = new JButton("");
		btnViderFields.setToolTipText("Clear the fields");
		ButtonStyle(btnViderFields);
		btnViderFields.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				comboBoxFonction.setSelectedIndex(0);
				comboBoxWilaya.setSelectedIndex(0);
				CBYear.setSelectedIndex(0);
				CBDay.setSelectedIndex(0);
				CBMonth.setSelectedIndex(0);
				AdrtextField.setText("");
				TelField.setText("");
				NomField.setText("");	
				PrenomField.setText("");
				LNField.setText("");
				TypeField.setText("");
				CheckBoxFemme.setSelected(false);
				CheckBoxHomme.setSelected(false);
				textFieldADRmail.setText("");
				
			}
		});
		btnViderFields.setIcon(new ImageIcon(GestionAD.class.getResource("/GestionAD_img/trash.png")));
		btnViderFields.setBounds(616, 249, 32, 32);
		contentPane.add(btnViderFields);

//boutn Ajouter,Supprimer,Modifier////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	//Ajouter un Patient**********************************************************************************************************************************************************************************************************************************
		JButton btnAddADH = new JButton("");
		btnAddADH.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent arg0) {
				btnAddADH.setIcon(new ImageIcon(GestionAD.class.getResource("/GestionAD_img/plus selected.png")));//changer les couleurs button
			}
			@Override
			public void mouseExited(MouseEvent e) {
				btnAddADH.setIcon(new ImageIcon(GestionAD.class.getResource("/GestionAD_img/plus.png")));//remetre le bouton de base
			}
		});
		btnAddADH.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				 ImageIcon icon = new ImageIcon(GestionAD.class.getResource("/messages_img/Questions secretaire.gif"));//Animation "Question"
				 icon.getImage().flush(); // réinitialiser l'animation
				 //message de confirmation
				int ClickedButton	=JOptionPane.showConfirmDialog(null, "Vouler Vous vraiment ajouter cet adherent ?", "Confirmation",JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, icon);
				if(ClickedButton==JOptionPane.YES_OPTION)
					{
								
						String Nom =NomField.getText().toString();//recuperer le contenu du Textfield Nom
						String Prenom=PrenomField.getText().toString();//recuperer le contenu du Textfield PreNom
						String Wilaya =comboBoxWilaya.getSelectedItem().toString();
						String Fonction = comboBoxFonction.getSelectedItem().toString();
						String id_F = null;
						//recuperer le Sexe
						String Sexe = null;   
						if (CheckBoxHomme.isSelected()) { Sexe="H";}
						if (CheckBoxFemme.isSelected()) { Sexe="F";}
					
						String Adr=AdrtextField.getText().toString();//recuperer le contenu du Textfield Adresse
						String NumT=TelField.getText().toString();//recuperer le contenu du Textfield Numero de tlephone
						String LD =LNField.getText().toString();//recuperer le contenu du Textfield Profession
						String ADRmail = textFieldADRmail.getText().toString();//recuperer le contenu du Textfield Adresse Mail
					
						if ( Nom.equals("")|| Prenom.equals("") || Wilaya.equals("Sélectionner Wilaya") || Fonction.equals("Sélectionner Fonction") ||ADRmail.equals("") || NumT.equals("")) //verifier si le nom et le prenom on ete bien ecrit 
							{//un message d'erreur
								JOptionPane.showMessageDialog(null, " S'il vous plait veuillez remplir tous les champs marqué par '*' !","!", JOptionPane.QUESTION_MESSAGE, new ImageIcon(GestionAD.class.getResource("/messages_img/error.png")));
							}
						else 
						{	
							//recuperer la date de naiisance et la rendre sous forme dd-MM-yyyy
							String DN=CBDay.getSelectedItem().toString()+"-"+CBMonth.getSelectedItem().toString()+"-"+CBYear.getSelectedItem().toString();
							boolean B=verifier(CBYear.getSelectedItem().toString(), CBMonth.getSelectedItem().toString(), CBDay.getSelectedItem().toString());
							//verifier la validite de la date (bissextile...)
							if (!B) //si la date est invalide
								{//un message d'erreur
									JOptionPane.showMessageDialog(null,  " Date de naissance Invalide !","!", JOptionPane.QUESTION_MESSAGE, new ImageIcon(GestionAD.class.getResource("/messages_img/error.png")));
								}
							else 
							{
								//recuperer l'id de la fonction selectionné
								String sql="Select Code_F from Fonction where NomF='"+Fonction+"'";
								
							
									try {
										prepared=cnx.prepareStatement(sql);
										resultat=prepared.executeQuery();
										if(resultat.next())
										{
											id_F=resultat.getString("Code_F");
										}

									} catch (SQLException e) {
										// TODO Auto-generated catch block
										JOptionPane.showMessageDialog(null,e);
									}
								//recuperer la date du systeme
								Date actuelle = new Date();
					 			DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
					 			String datactuelle = dateFormat.format(actuelle);  
					 			
					 			Adherent AD =new Adherent(Nom, Prenom, Sexe,id_F, TypeField.getText(), NumT, ADRmail, Adr,Wilaya, DN, LD, datactuelle);
					 			AD.Ajouter();
						
								//desactiver les boutons supprimer et modifier
								btnRemoveADH.setEnabled(false);
								btnModADH.setEnabled(false);
								UpedateTableAD();//actualiser la table des Patient
							}
						}
					}
			}
		});
		btnAddADH.setIcon(new ImageIcon(GestionAD.class.getResource("/GestionAD_img/plus.png")));
		btnAddADH.setToolTipText("Ajouter Adherent");
		ButtonStyle(btnAddADH);
		btnAddADH.setBounds(856, 225, 32, 32);
		contentPane.add(btnAddADH);
	//Modifier un Patient**********************************************************************************************************************************************************************************************************************************	
		btnModADH = new JButton("");
		btnModADH.setEnabled(false);//desactiver le bouton tant que acune ligne de la table n'a ete selectionné
		btnModADH.setToolTipText("Modifier Adherent");
		btnModADH.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				if (btnModADH.isEnabled()) {
					btnModADH.setIcon(new ImageIcon(GestionAD.class.getResource("/GestionAD_img/pencil selected.png")));//changer les couleurs button
				}
			}
			@Override
			public void mouseExited(MouseEvent e) {
				if (btnModADH.isEnabled()) {
					btnModADH.setIcon(new ImageIcon(GestionAD.class.getResource("/GestionAD_img/pencil.png")));//remetre le bouton de base
				}
			}
		});
		btnModADH.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				 ImageIcon icon = new ImageIcon(GestionAD.class.getResource("/messages_img/Questions secretaire.gif"));//Animation "Question"
				 icon.getImage().flush(); // réinitialiser l'animation
				//message de confirmation
				int ClickedButton	=JOptionPane.showConfirmDialog(null, "Vouler Vous vraiment Modifier les informations de cet Adherent ? ", "Confirmation",JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, icon);
				if(ClickedButton==JOptionPane.YES_OPTION)
				{
					//recuperer le Sexe
					String sexe=null; 
					if(CheckBoxHomme.isSelected()) {  sexe ="H";}
					if(CheckBoxFemme.isSelected()) {  sexe ="F";}
					String ADRmail = textFieldADRmail.getText().toString();
					String Fonction = comboBoxFonction.getSelectedItem().toString();
					String id_F = null;
				
					String id =id_AD;	//recuperer l'id du ADH
				
					if (tableAD.getSelectedRow()==-1) //aucune ligne de la table n'est selectionnée 
					{// message 
						JOptionPane.showMessageDialog(null, "S'il vous plait veuillez selectioner une ligne a modifer !","!", JOptionPane.QUESTION_MESSAGE, new ImageIcon(GestionAD.class.getResource("/messages_img/error.png")));
					}
					else {

						if ( NomField.getText().equals("")|| PrenomField.getText().equals("") || comboBoxWilaya.getSelectedItem().toString().equals("Sélectionner Wilaya") || Fonction.equals("Sélectionner Fonction")|| ADRmail.equals("") || TelField.getText().equals("")) //verifier si le nom et le prenom on ete bien ecrit 
							{//un message d'erreur
								JOptionPane.showMessageDialog(null, " S'il vous plait veuillez remplir tous les champs marqué par '*' !","!", JOptionPane.QUESTION_MESSAGE, new ImageIcon(GestionAD.class.getResource("/messages_img/error.png")));
							}
						else {
							
						
							String DN=CBDay.getSelectedItem().toString()+"-"+CBMonth.getSelectedItem().toString()+"-"+CBYear.getSelectedItem().toString();
							boolean B=verifier(CBYear.getSelectedItem().toString(), CBMonth.getSelectedItem().toString(), CBDay.getSelectedItem().toString());
							//verifier la validite de la date (bissextile...)
							if (!B) //si la date est invalide
								{
								//un message d'erreur
								JOptionPane.showMessageDialog(null,  "Date de naissance Invalide!","!", JOptionPane.QUESTION_MESSAGE, new ImageIcon(GestionAD.class.getResource("/messages_img/error.png")));
								}
							else 
								{
								//recuperer l'id de la fonction selectionné
								String sql="Select Code_F from Fonction where NomF='"+Fonction+"'";
								
							
									try {
										prepared=cnx.prepareStatement(sql);
										resultat=prepared.executeQuery();
										if(resultat.next())
										{
											id_F=resultat.getString("Code_F");
										}

									} catch (SQLException E) {
										// TODO Auto-generated catch block
										JOptionPane.showMessageDialog(null,E);
									}
								Adherent AD=new Adherent();
								AD.Modifier(id, NomField.getText(), PrenomField.getText(), sexe,id_F,TypeField.getText(), TelField.getText(), ADRmail, AdrtextField.getText(),comboBoxWilaya.getSelectedItem().toString(), DN, LNField.getText());
								//desactiver les boutons supprimer et modifier
								btnRemoveADH.setEnabled(false);
								btnModADH.setEnabled(false);
								UpedateTableAD();//actualiser la table
								}
						}
						}
				}
			}
		});
		btnModADH.setIcon(new ImageIcon(GestionAD.class.getResource("/GestionAD_img/pencil.png")));
		btnModADH.setBounds(1024, 225, 32, 32);
		ButtonStyle(btnModADH);
		contentPane.add(btnModADH);
	
	//Supprimer un medicament**********************************************************************************************************************************************************************************************************************************
		btnRemoveADH = new JButton("");
		btnRemoveADH.setEnabled(false);//desactiver le bouton tant que acune ligne de la table n'a ete selectionné
		btnRemoveADH.setToolTipText("Supprimer Adherent");
		btnRemoveADH.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				if (btnRemoveADH.isEnabled()) {
						btnRemoveADH.setIcon(new ImageIcon(GestionAD.class.getResource("/GestionAD_img/remove selected.png")));//changer les couleurs button
				}
			
			}
			@Override
			public void mouseExited(MouseEvent e) {
				if (btnRemoveADH.isEnabled()) {
					btnRemoveADH.setIcon(new ImageIcon(GestionAD.class.getResource("/GestionAD_img/remove.png")));//remetre le bouton de base
				}
			}
		});
		btnRemoveADH.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				
				if (tableAD.getSelectedRow()==-1) // return -1 quand aucune ligne est selectionnée
					{//afficher un message d'erreur
						JOptionPane.showMessageDialog(null, "S'il vous plait veuillez selectioner une ligne a supprimer !","Message", JOptionPane.QUESTION_MESSAGE,new ImageIcon(GestionAD.class.getResource("/messages_img/error.png")));
					}
				else{
					 ImageIcon icon = new ImageIcon(GestionAD.class.getResource("/messages_img/Questions secretaire.gif"));//Animation "Question"
					 icon.getImage().flush(); // réinitialiser l'animation
					 //message de confirmation
					int ClickedButton	=JOptionPane.showConfirmDialog(null, "Vouler Vous vraiment supprimer cet Adherent de la table ?", "Confirmation",JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE,icon);
					if(ClickedButton==JOptionPane.YES_OPTION)
						{	
					Adherent AD =new Adherent();
					AD.Supprimer(id_AD,ID_ADHERENT);
						
						btnRemoveADH.setEnabled(false);
						btnModADH.setEnabled(false);
						UpedateTableAD();//Actualiser la table
						}
					}
			}
		});
		
		btnRemoveADH.setIcon(new ImageIcon(GestionAD.class.getResource("/GestionAD_img/remove.png")));
		btnRemoveADH.setBounds(937, 225, 32, 32);
		ButtonStyle(btnRemoveADH);
		contentPane.add(btnRemoveADH);
//Modifier Fonctions//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////		
		JButton btnGF = new JButton("");
		btnGF.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				FonctionFrame frame = new FonctionFrame(GestionAD.this);
				frame.setLocationRelativeTo(GestionAD.this);
				frame.setVisible(true);
			}
		});
		btnGF.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent arg0) {
				btnGF.setIcon(new ImageIcon(GestionAD.class.getResource("/GestionAD_img/settings selected.png")));//changer les couleurs button
			}
			@Override
			public void mouseExited(MouseEvent e) {
				btnGF.setIcon(new ImageIcon(GestionAD.class.getResource("/GestionAD_img/settings.png")));//remetre le bouton de base
			}
		});
		btnGF.setIcon(new ImageIcon(GestionAD.class.getResource("/GestionAD_img/settings.png")));
		btnGF.setToolTipText("Gestion de Fonction");
		ButtonStyle(btnGF);
		btnGF.setBounds(680, 142, 32, 32);
		contentPane.add(btnGF);
	
		
//le background////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		JLabel BGStaff = new JLabel("BGStaffM");
		BGStaff.setIcon(new ImageIcon(GestionAD.class.getResource("/GestionAD_img/Gestion des Adherents.png")));
		BGStaff.setBounds(0, 0, 1201, 550);
		contentPane.add(BGStaff);
		

		
	}
	
//methode du style des buttons/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	private void ButtonStyle(JButton btn) {
		btn.setOpaque(false);
		 btn.setFocusPainted(false);
		 btn.setBorderPainted(false);
		 btn.setContentAreaFilled(false);
	}
	
//Remplire la ComboBox Fonction//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////				
			public void INComboboxFonc(JComboBox<String> Fonctions)
			{ 
									
				DefaultComboBoxModel<String> cmbModel=(DefaultComboBoxModel<String>)Fonctions.getModel();
				// Vider le JComboBox
				cmbModel.removeAllElements();
				//ajouter que le premier 
				Fonctions.setModel(new DefaultComboBoxModel<String>(new String[] {"Sélectionner Fonction"} ));
				
				String sql ="Select NomF  from Fonction";
				try {
					prepared = cnx.prepareStatement(sql);
					resultat =prepared.executeQuery();
					while(resultat.next())
					{
						
							String item=resultat.getString("NomF").toString();
							Fonctions.addItem(item);	
					}
					
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					JOptionPane.showMessageDialog(null,e);
				}
				
			}
		
//actualisation de la table/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	public void UpedateTableAD()
	{ //Nom ,Prenom  ,Ueser_Name ,password , DateN  ,sex ,ADR ,Numtel ,Specilité
		String sql ="Select ID_AD,Nom,Prenom,code_F ,Type ,numTel,ADRmail ,ADR ,Wilaya ,DateN,LieuDN,DateEng from Adherent";
		try {
			prepared = cnx.prepareStatement(sql);
			resultat =prepared.executeQuery();
			tableAD.setModel(DbUtils.resultSetToTableModel(resultat));//changer le model de la table
			//ajouter un sorter pour trier la table
			TableRowSorter<TableModel> sorter = new TableRowSorter<TableModel>(tableAD.getModel());
			tableAD.setRowSorter(sorter);
			List<RowSorter.SortKey> sortKeys = new ArrayList<>(25);
			sortKeys.add(new RowSorter.SortKey(9, SortOrder.ASCENDING));//trier  par apport a la date d'ajout(date d'enregistrement)
			sortKeys.add(new RowSorter.SortKey(0, SortOrder.ASCENDING));//trier  par apport a l'ID
			sorter.setSortKeys(sortKeys);
		
			//renomer les noms de colones
			tableAD.getColumnModel().getColumn( 0 ).setHeaderValue("N°");
			tableAD.getColumnModel().getColumn( 1 ).setHeaderValue("Nom");
			tableAD.getColumnModel().getColumn( 2 ).setHeaderValue("Prenom");
			tableAD.getColumnModel().getColumn( 3 ).setHeaderValue("Code Fonction");
			tableAD.getColumnModel().getColumn( 4 ).setHeaderValue("Type");
			tableAD.getColumnModel().getColumn( 5 ).setHeaderValue("N°phone");
			tableAD.getColumnModel().getColumn( 6 ).setHeaderValue("@");
			tableAD.getColumnModel().getColumn( 7 ).setHeaderValue("Adresse");
			tableAD.getColumnModel().getColumn( 8 ).setHeaderValue("Wilaya");
			tableAD.getColumnModel().getColumn( 9 ).setHeaderValue("Date de naissance");	
			tableAD.getColumnModel().getColumn( 10 ).setHeaderValue("Lieu de naissance");
			tableAD.getColumnModel().getColumn( 11 ).setHeaderValue("Date d'enregistrement");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			JOptionPane.showMessageDialog(null,e);
		}
		
	}
	
//la verfication de la validité de la date ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	public boolean verifier(String Year,String Month,String Day )
	{
		int value=1;
		switch(Month) 
		{
		case "01":
		case "03":
		case "05":
		case "07":
		case "08":
		case "10":
		case "12":
			if (Integer.parseInt(Day)>31) 
			{
			value=0;	
			}
			break;
			
		case "02":
			if( ((Integer.parseInt(Day)>29) && (Integer.parseInt(Year) % 400 )==0)||( ((Integer.parseInt(Day)>28) && (Integer.parseInt(Year) % 400 )!=0))) 
			{
				 value=0;
			}
			
			break;
			
		
		case "04":
		case "06":
		case "09":
		case "11":
			if(Integer.parseInt(Day)>30) 
			{
			value=0;	
			}
			break;
			
		default:
			value=0 ;
		}	
		if(value==0) return false;
			
		else return true;
		
		
	}
}
	
	

