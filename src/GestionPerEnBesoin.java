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
import javax.swing.JTextArea;
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

public class GestionPerEnBesoin extends JFrame {

	
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTable tablePEB;
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
	private String id_PEB;// le id recupere en cliquant sur le tableau
	private JComboBox<String> comboBoxWilaya;  
	private JTextField LNField;
	private JTextField textFieldADRmail;
	private ChercherPersonneFrame frame = new ChercherPersonneFrame();
	private JTextField ProField;
	private JTextArea textAreaCause;
	
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
					GestionPerEnBesoin frame = new GestionPerEnBesoin("ReMo1");
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
	
	public GestionPerEnBesoin(String ID_ADHERENT) {
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
		setIconImage(Toolkit.getDefaultToolkit().getImage(GestionPerEnBesoin.class.getResource("/GestionPEB_img/loan.png")));
		setTitle("Gestion des personnes en besoin");
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
		CBYear.setBackground(new Color(34, 139, 34));
		CBYear.setForeground(new Color(255, 255, 255));
		CBYear.setBounds(247, 312, 70, 20);
		contentPane.add(CBYear);
		
	//ComboBox Mois	
		JComboBox<Object> CBMonth = new JComboBox<Object>();
		CBMonth.setModel(new DefaultComboBoxModel<Object>(new String[] {"Mois","01","02","03","04","05","06","07","08","09","10","11","12"	}));
		CBMonth.setBackground(new Color(34, 139, 34));
		CBMonth.setForeground(new Color(255, 255, 255));
		CBMonth.setBounds(180, 312, 70, 20);
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
		CBDay.setBackground(new Color(34, 139, 34));
		CBDay.setForeground(new Color(255, 255, 255));
		CBDay.setBounds(120, 312, 64, 20);
		contentPane.add(CBDay);
		
	//comboBox Wilaya
		comboBoxWilaya = new JComboBox<String>();
		comboBoxWilaya.setFont(new Font("Segoe UI", Font.BOLD, 12));
		comboBoxWilaya.setModel(new DefaultComboBoxModel<String>(new String[] {"Sélectionner Wilaya",
			" ADRAR" , "CHLEF" , "LAGHOUAT" ,"OUM BOUAGHI5", "BATNA" , "BEJAIA", "BISKRA", "BECHAR"  ,"BLIDA" , "BOUIRA"  ,"TAMANRASSET",
			"TEBESSA" ,"TLEMCEN", "TIARET","TIZI OUZOU", "ALGER" , "DJELFA" , "JIJEL" ,"SETIF" ,"SAIDA" ,"SKIKDA" ,"SIDI BEL ABBES",  "ANNABA",
			"GUELMA" , "CONSTANTINE" , "MEDEA" , "MOSTAGANEM",  "M'SILA",  "MASCARA" , "OUARGLA" , "ORAN" , "EL BAYDH" ,"ILLIZI" , "BORDJ BOU ARRERIDJ" , "BOUMERDES" , "EL TAREF",
			"TINDOUF" , "TISSEMSILT" , "EL OUED"  ,"KHENCHLA" , "SOUK AHRASS" , "TIPAZA"  , "MILA" , "AÏN DEFLA" ,"NÂAMA" , "AÏN TEMOUCHENT" , "GHARDAÏA"  ,"RELIZANE"} ));
		comboBoxWilaya.setBackground(new Color(34, 139, 34));
		comboBoxWilaya.setForeground(new Color(255, 255, 255));
		comboBoxWilaya.setBounds(449, 147, 183, 28);
		contentPane.add(comboBoxWilaya);
		
	//comboBox EtatCOV	
		JComboBox<String> comboBoxEtatCov = new JComboBox<String>();
		comboBoxEtatCov.setModel(new DefaultComboBoxModel<String>(new String[] {"Sélectionner état","jamais atteint du covid","Atteint du Covid","Membre de la famille atteint du covid","Déja atteint du covid mais guerrie"  } ));
		comboBoxEtatCov.setForeground(Color.WHITE);
		comboBoxEtatCov.setFont(new Font("Segoe UI", Font.BOLD, 12));
		comboBoxEtatCov.setBackground(new Color(34, 139, 34));
		comboBoxEtatCov.setBounds(910, 233, 183, 28);
		contentPane.add(comboBoxEtatCov);
		
		
		
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
		
		ProField = new JTextField();
		ProField.addKeyListener(new KeyAdapter() {
		    public void keyTyped(KeyEvent e) { 
		        if (		ProField.getText().length() >= 100 ) 
		            e.consume(); 
		    }  
		});
		ProField.setOpaque(false);
		ProField.setForeground(Color.WHITE);
		ProField.setFont(new Font("Segoe UI", Font.BOLD, 14));
		ProField.setColumns(10);
		ProField.setBorder(null);
		ProField.setBounds(878, 146, 183, 28);
		contentPane.add(ProField);
		
		JComboBox<Object> CBMF = new JComboBox<Object>();
		CBMF.setModel(new DefaultComboBoxModel<Object>(new String[] {"N°","01","02","03","04","05","06","07","08","09","10","11","12","13","14","15"}));
		CBMF.setForeground(Color.WHITE);
		CBMF.setBackground(new Color(34, 139, 34));
		CBMF.setBounds(991, 202, 70, 20);
		CBMF.setVisible(false);
		contentPane.add(CBMF);
		
		JCheckBox CheckBoxMariee = new JCheckBox();
		CheckBoxMariee.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				if (CheckBoxMariee.isSelected()) {
					CBMF.setVisible(true);
					CBMF.setSelectedIndex(0);
				}
			else {
				CBMF.setVisible(false);
				CBMF.setSelectedIndex(0);
				}
			}
		});
		CheckBoxMariee.setToolTipText("Marié(e)");
		CheckBoxMariee.setBounds(910, 202, 21, 20);
		contentPane.add(CheckBoxMariee);
		
		JScrollPane scrollPaneCause = new JScrollPane();
		scrollPaneCause.setBounds(440, 192, 334, 155);
		contentPane.add(scrollPaneCause);
		
		textAreaCause = new JTextArea();
		textAreaCause.setForeground(Color.WHITE);
		textAreaCause.setFont(new Font("Segoe UI", Font.BOLD, 14));
		textAreaCause.setBackground(new Color(34, 139, 34));
		scrollPaneCause.setViewportView(textAreaCause);
		textAreaCause.addKeyListener(new KeyAdapter() {
		    public void keyTyped(KeyEvent e) { 
		        if (		textAreaCause.getText().length() >= 1000 ) 
		            e.consume(); 
		    }  
		});
		
		
		
//ScrollPane/Table Patient//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		JScrollPane scrollPaneTab = new JScrollPane();
		scrollPaneTab.setViewportBorder(new TitledBorder(null, "", TitledBorder.LEADING, TitledBorder.TOP, null, Color.GRAY));
		scrollPaneTab.setBackground(Color.WHITE);
		scrollPaneTab.setBounds(336, 377, 854, 155);
		contentPane.add(scrollPaneTab);
		
		tablePEB = new JTable();
		tablePEB.setFont(new Font("Segoe UI", Font.BOLD, 11));
		tablePEB.setForeground(new Color(255, 255, 255));
		tablePEB.setBorder(null);
		tablePEB.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
		
				btnRemoveADH.setEnabled(true);
				btnModADH.setEnabled(true);
				int ligne=tablePEB.getSelectedRow();

				id_PEB =tablePEB.getValueAt(ligne, 0).toString();
				String sql="Select * from PersoEnBesoin where ID_PEB='"+id_PEB+"'";
				
				try {
					prepared=cnx.prepareStatement(sql);
					resultat=prepared.executeQuery();
					if(resultat.next())
					{
					NomField.setText(resultat.getString("Nom"));
					PrenomField.setText(resultat.getString("Prenom"));
					String DN=resultat.getString("DateN");
				    if (DN!=null)
				    {
				    	DN=(String) DN.subSequence(0, 10);
				    	//parcourir la combobox Année
				    	for (int i = 0; i < CBYear.getItemCount(); i++) 
						{
								if (CBYear.getItemAt(i).equals(DN.subSequence(0,4 ))) { CBYear.setSelectedIndex(i);}
							
						}
				    	//parcourir la combobox Mois
				    	for (int i = 0; i < CBMonth.getItemCount(); i++) 
						{
								if (CBMonth.getItemAt(i).equals(DN.subSequence(5,7 ))) { CBMonth.setSelectedIndex(i);}
							
						}
				    	//parcourir la combobox jour
				    	for (int i = 0; i < CBDay.getItemCount(); i++) 
						{
								if (CBDay.getItemAt(i).equals(DN.subSequence(8,10 ))) { CBDay.setSelectedIndex(i);}
						}

				    }
				    //selectionner la wilaya
				    for (int i = 0; i < comboBoxWilaya.getItemCount(); i++) 
					{
							
							if (comboBoxWilaya.getItemAt(i).equals(resultat.getString("Wilaya"))) { comboBoxWilaya.setSelectedIndex(i);}
					}
				    
				    //selectionner etat cov
				    for (int i = 0; i < comboBoxEtatCov.getItemCount(); i++) 
					{
							
							if (comboBoxEtatCov.getItemAt(i).equals(resultat.getString("etatCOV"))) { comboBoxEtatCov.setSelectedIndex(i);}
					}
				    
				  
					String sexe=resultat.getString("sexe");
					if(sexe.equals("H")) {CheckBoxHomme.setSelected(true);CheckBoxFemme.setSelected(false);}
					if(sexe.equals("F")) {CheckBoxFemme.setSelected(true);CheckBoxHomme.setSelected(false);}
					if(sexe.equals("null")) { CheckBoxFemme.setSelected(false);CheckBoxHomme.setSelected(false);}
						
					String Marie =resultat.getString("Mariee");
					if(Marie.equals("Oui")) {CheckBoxMariee.setSelected(true); CBMF.setVisible(true);}
					if(Marie.equals("Non")) {CheckBoxMariee.setSelected(false); CBMF.setVisible(false);}
					
					  if (CheckBoxMariee.isSelected()) {
						 
						  for (int i = 0; i < CBMF.getItemCount(); i++) 
						  	{
							
							if (CBMF.getItemAt(i).equals(resultat.getString("Nfamille"))) { CBMF.setSelectedIndex(i);}
						  	}
					}
				  
					LNField.setText(resultat.getString("LieuDN"));
					AdrtextField.setText(resultat.getString("ADR"));
					TelField.setText(resultat.getString("numTel"));
					textFieldADRmail.setText(resultat.getString("ADRmail"));
					textAreaCause.setText(resultat.getString("Cause"));
					ProField.setText(resultat.getString("pro"));
					
					}
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					JOptionPane.showMessageDialog(null,e);
				}
			}
		});
		tablePEB.setBackground(new Color(34, 139, 34));
		scrollPaneTab.setViewportView(tablePEB);
//afficher le contenue de la table juste a louverture//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

		UpedateTableAD();
		
// Bouton Reduire ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		JButton Minimise_BTN = new JButton("");
		Minimise_BTN.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				Minimise_BTN.setIcon(new ImageIcon(GestionPerEnBesoin.class.getResource("/Log_in_img/Minimize ML selected.png")));//changer les couleurs button
			}
			@Override
			public void mouseExited(MouseEvent e) {
				Minimise_BTN.setIcon(new ImageIcon(GestionPerEnBesoin.class.getResource("/Log_in_img/Minimize ML .png")));	//remetre le bouton de base
			}
		});
		Minimise_BTN.setToolTipText("Minimize");
		Minimise_BTN.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setState(ICONIFIED);
			}
		});
		Minimise_BTN.setIcon(new ImageIcon(GestionPerEnBesoin.class.getResource("/Log_in_img/Minimize ML .png")));
		Minimise_BTN.setBounds(1039, 11, 32, 32);
		ButtonStyle(Minimise_BTN);
		contentPane.add(Minimise_BTN);
// Bouton Exit ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		JButton Exit_BTN = new JButton("");
		Exit_BTN.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent arg0) {
				Exit_BTN.setIcon(new ImageIcon(GestionPerEnBesoin.class.getResource("/Log_in_img/Exit ML selected.png")));//changer les couleurs button
			}
			@Override
			public void mouseExited(MouseEvent arg0) {
				Exit_BTN.setIcon(new ImageIcon(GestionPerEnBesoin.class.getResource("/Log_in_img/Exit ML.png")));//remetre le bouton de base
			}
		});
		Exit_BTN.setToolTipText("Exit");
		Exit_BTN.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			
					int ClickedButton	=JOptionPane.showConfirmDialog(null, "Do you really want to leave?", "Close", JOptionPane.YES_NO_OPTION);
					if(ClickedButton==JOptionPane.YES_OPTION)
					{
						//  vider les fields et remetre les combobox et check box par default(les valeurs)
						comboBoxWilaya.setSelectedIndex(0);
						CBYear.setSelectedIndex(0);
						CBDay.setSelectedIndex(0);
						CBMonth.setSelectedIndex(0);
						AdrtextField.setText("");
						TelField.setText("");
						NomField.setText("");	
						PrenomField.setText("");
						LNField.setText("");
						CheckBoxFemme.setSelected(false);
						CheckBoxHomme.setSelected(false);
						textAreaCause.setText("");
						ProField.setText("");
						CBMF.setSelectedIndex(0);
						CBMF.setVisible(false);
						CheckBoxMariee.setSelected(false);
						textFieldADRmail.setText("");
						comboBoxEtatCov.setSelectedIndex(0);
						
						if (frame.isVisible()) {//si la fenetre Prescrire traitement est vesible alors la fermer
							frame.dispose();
						}
						dispose();
					}
			}
			
		});
		Exit_BTN.setIcon(new ImageIcon(GestionPerEnBesoin.class.getResource("/Log_in_img/Exit ML.png")));
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
								if (frame.isVisible()) {
									frame.dispose();
								}
								
								MenuPrincipaleFrame FramePrincipale = new MenuPrincipaleFrame(ID_ADHERENT);// retourner au menu 
								FramePrincipale.setLocationRelativeTo(null);
								FramePrincipale.setVisible(true);
							dispose();
					}
				});
		btnHome.setIcon(new ImageIcon(GestionPerEnBesoin.class.getResource("/Log_in_img/home.png")));
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
				comboBoxWilaya.setSelectedIndex(0);
				CBYear.setSelectedIndex(0);
				CBDay.setSelectedIndex(0);
				CBMonth.setSelectedIndex(0);
				AdrtextField.setText("");
				TelField.setText("");
				NomField.setText("");	
				PrenomField.setText("");
				LNField.setText("");
				CheckBoxFemme.setSelected(false);
				CheckBoxHomme.setSelected(false);
				textAreaCause.setText("");
				ProField.setText("");
				CBMF.setSelectedIndex(0);
				CBMF.setVisible(false);
				CheckBoxMariee.setSelected(false);
				textFieldADRmail.setText("");
				comboBoxEtatCov.setSelectedIndex(0);
				
			}
		});
		btnViderFields.setIcon(new ImageIcon(GestionPerEnBesoin.class.getResource("/GestionAD_img/trash.png")));
		btnViderFields.setBounds(363, 279, 32, 32);
		contentPane.add(btnViderFields);

//boutn Ajouter,Supprimer,Modifier////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	//Ajouter une personne**********************************************************************************************************************************************************************************************************************************
		JButton btnAddADH = new JButton("");
		btnAddADH.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent arg0) {
				btnAddADH.setIcon(new ImageIcon(GestionPerEnBesoin.class.getResource("/GestionPEB_img/plus selected.png")));//changer les couleurs button
			}
			@Override
			public void mouseExited(MouseEvent e) {
				btnAddADH.setIcon(new ImageIcon(GestionPerEnBesoin.class.getResource("/GestionPEB_img/plus.png")));//remetre le bouton de base
			}
		});
		btnAddADH.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				 ImageIcon icon = new ImageIcon(GestionPerEnBesoin.class.getResource("/messages_img/Questions secretaire.gif"));//Animation "Question"
				 icon.getImage().flush(); // réinitialiser l'animation
				 //message de confirmation
				int ClickedButton	=JOptionPane.showConfirmDialog(null, "Voulez Vous vraiment ajouter cette personne ?", "Confirmation",JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, icon);
				if(ClickedButton==JOptionPane.YES_OPTION)
					{
						String Nom = NomField.getText().toString();
						String Prenom = PrenomField.getText().toString();
						String Wilaya = comboBoxWilaya.getSelectedItem().toString();
						String Cause = textAreaCause.getText().toString();
						String pro = ProField.getText().toString();
						
						String MF = "-";
						
						String Sexe = null;   
						if (CheckBoxHomme.isSelected()) { Sexe="H";}
						if (CheckBoxFemme.isSelected()) { Sexe="F";}
						
						String Marie =null;
						if (CheckBoxMariee.isSelected()) { Marie="Oui";MF=CBMF.getSelectedItem().toString();}
						else {Marie="Non";}
						
						
						String Adr=AdrtextField.getText().toString();
						String NumT=TelField.getText().toString();
						String LD =LNField.getText().toString();
						String ADRmail = textFieldADRmail.getText().toString();
						String etatCOV = comboBoxEtatCov.getSelectedItem().toString();
					
						if (pro.equals("") || Cause.equals("") || Nom.equals("")|| Prenom.equals("") ||etatCOV.equals("Sélectionner état") || Wilaya.equals("Sélectionner Wilaya") ||ADRmail.equals("") || NumT.equals("") || (CBMF.isVisible() && CBMF.getSelectedItem().toString().equals("N°"))) 
							{
								JOptionPane.showMessageDialog(null, " S'il vous plait veuillez remplir tous les champs marqué par '*' !","Warning", JOptionPane.QUESTION_MESSAGE, new ImageIcon(GestionPerEnBesoin.class.getResource("/messages_img/error.png")));
							}
						else 
						{	
							String DN=CBDay.getSelectedItem().toString()+"-"+CBMonth.getSelectedItem().toString()+"-"+CBYear.getSelectedItem().toString();
							boolean B=verifier(CBYear.getSelectedItem().toString(), CBMonth.getSelectedItem().toString(), CBDay.getSelectedItem().toString());
							
							if (!B) 
								{
									JOptionPane.showMessageDialog(null,  " Date de naissance Invalide !","!", JOptionPane.QUESTION_MESSAGE, new ImageIcon(GestionPerEnBesoin.class.getResource("/messages_img/error.png")));
								}
							else 
							{
								Date actuelle = new Date();
					 			DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
					 			String datactuelle = dateFormat.format(actuelle);  
					 			
					 			PerEnBesoin P= new PerEnBesoin(Nom, Prenom, Sexe, NumT, Cause, ADRmail, Adr, pro, etatCOV, Marie, MF, Wilaya, DN, datactuelle, LD);
					 			P.Ajouter();
					 			
								btnRemoveADH.setEnabled(false);
								btnModADH.setEnabled(false);
								UpedateTableAD();
							}
						}
					}
			}
		});
		btnAddADH.setIcon(new ImageIcon(GestionPerEnBesoin.class.getResource("/GestionPEB_img/plus.png")));
		btnAddADH.setToolTipText("Ajouter Personne");
		ButtonStyle(btnAddADH);
		btnAddADH.setBounds(881, 300, 32, 32);
		contentPane.add(btnAddADH);
	//Modifier une personne**********************************************************************************************************************************************************************************************************************************	
		btnModADH = new JButton("");
		btnModADH.setEnabled(false);//desactiver le bouton tant que acune ligne de la table n'a ete selectionné
		btnModADH.setToolTipText("Modifier Personne");
		btnModADH.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				if (btnModADH.isEnabled()) {
					btnModADH.setIcon(new ImageIcon(GestionPerEnBesoin.class.getResource("/GestionPEB_img/pencil selected.png")));//changer les couleurs button
				}
			}
			@Override
			public void mouseExited(MouseEvent e) {
				if (btnModADH.isEnabled()) {
					btnModADH.setIcon(new ImageIcon(GestionPerEnBesoin.class.getResource("/GestionPEB_img/pencil.png")));//remetre le bouton de base
				}
			}
		});
		btnModADH.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				 ImageIcon icon = new ImageIcon(GestionPerEnBesoin.class.getResource("/messages_img/Questions secretaire.gif"));//Animation "Question"
				 icon.getImage().flush(); // réinitialiser l'animation
				//message de confirmation
				int ClickedButton	=JOptionPane.showConfirmDialog(null, "Voulez Vous vraiment Modifier les informations de cette Personne ? ", "Confirmation",JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, icon);
				if(ClickedButton==JOptionPane.YES_OPTION)
				{
					String Nom = NomField.getText().toString();
					String Prenom = PrenomField.getText().toString();
					String Wilaya = comboBoxWilaya.getSelectedItem().toString();
					String Cause = textAreaCause.getText().toString();
					String pro = ProField.getText().toString();
					
					String MF = "-";
					
					String Sexe = null;   
					if (CheckBoxHomme.isSelected()) { Sexe="H";}
					if (CheckBoxFemme.isSelected()) { Sexe="F";}
					
					String Marie =null;
					if (CheckBoxMariee.isSelected()) { Marie="Oui";MF=CBMF.getSelectedItem().toString();}
					else {Marie="Non";}
					
					
					String Adr=AdrtextField.getText().toString();
					String NumT=TelField.getText().toString();
					String LD =LNField.getText().toString();
					String ADRmail = textFieldADRmail.getText().toString();
					String etatCOV = comboBoxEtatCov.getSelectedItem().toString();
				
					String id =id_PEB;	//recuperer l'id du ADH
				
					if (tablePEB.getSelectedRow()==-1) //aucune ligne de la table n'est selectionnée 
					{// message 
						JOptionPane.showMessageDialog(null, "S'il vous plait veuillez selectioner une ligne a modifer !","!", JOptionPane.QUESTION_MESSAGE, new ImageIcon(GestionPerEnBesoin.class.getResource("/messages_img/error.png")));
					}
					else {

						if ( pro.equals("") || Cause.equals("") ||NomField.getText().equals("")|| PrenomField.getText().equals("") || etatCOV.equals("Sélectionner état")|| comboBoxWilaya.getSelectedItem().toString().equals("Sélectionner Wilaya") || ADRmail.equals("") || TelField.getText().equals("")||(CBMF.isVisible() && CBMF.getSelectedItem().toString().equals("N°"))) //verifier si le nom et le prenom on ete bien ecrit 
							{//un message d'erreur
								JOptionPane.showMessageDialog(null, " S'il vous plait veuillez remplir tous les champs marqué par '*' !","Warning", JOptionPane.QUESTION_MESSAGE, new ImageIcon(GestionPerEnBesoin.class.getResource("/messages_img/error.png")));
							}
						else {
							
						
							String DN=CBDay.getSelectedItem().toString()+"-"+CBMonth.getSelectedItem().toString()+"-"+CBYear.getSelectedItem().toString();
							boolean B=verifier(CBYear.getSelectedItem().toString(), CBMonth.getSelectedItem().toString(), CBDay.getSelectedItem().toString());
							//verifier la validite de la date (bissextile...)
							if (!B) //si la date est invalide
								{
								//un message d'erreur
								JOptionPane.showMessageDialog(null,  "Date de naissance Invalide!","!", JOptionPane.QUESTION_MESSAGE, new ImageIcon(GestionPerEnBesoin.class.getResource("/messages_img/error.png")));
								}
							else 
								{
								PerEnBesoin P =new PerEnBesoin();
								P.Modifier(id, Nom, Prenom, Sexe, NumT, Cause,etatCOV,pro, Marie, MF, ADRmail, Adr, Wilaya, DN,LD);
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
		btnModADH.setIcon(new ImageIcon(GestionPerEnBesoin.class.getResource("/GestionPEB_img/pencil.png")));
		btnModADH.setBounds(942, 300, 32, 32);
		ButtonStyle(btnModADH);
		contentPane.add(btnModADH);
	
	//Supprimer un medicament**********************************************************************************************************************************************************************************************************************************
		btnRemoveADH = new JButton("");
		btnRemoveADH.setEnabled(false);//desactiver le bouton tant que acune ligne de la table n'a ete selectionné
		btnRemoveADH.setToolTipText("Supprimer Personne");
		btnRemoveADH.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				if (btnRemoveADH.isEnabled()) {
						btnRemoveADH.setIcon(new ImageIcon(GestionPerEnBesoin.class.getResource("/GestionPEB_img/remove selected.png")));//changer les couleurs button
				}
			}
			@Override
			public void mouseExited(MouseEvent e) {
				if (btnRemoveADH.isEnabled()) {
					btnRemoveADH.setIcon(new ImageIcon(GestionPerEnBesoin.class.getResource("/GestionPEB_img/remove.png")));//remetre le bouton de base
				}
			}
		});
		btnRemoveADH.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				
				if (tablePEB.getSelectedRow()==-1) // return -1 quand aucune ligne est selectionnée
					{//afficher un message d'erreur
						JOptionPane.showMessageDialog(null, "S'il vous plait veuillez selectioner une ligne a supprimer !","Message", JOptionPane.QUESTION_MESSAGE,new ImageIcon(GestionPerEnBesoin.class.getResource("/messages_img/error.png")));
					}
				else{
					 ImageIcon icon = new ImageIcon(GestionPerEnBesoin.class.getResource("/messages_img/Questions secretaire.gif"));//Animation "Question"
					 icon.getImage().flush(); // réinitialiser l'animation
					 //message de confirmation
					int ClickedButton	=JOptionPane.showConfirmDialog(null, "Voulez Vous vraiment supprimer cette Personne de la table ?", "Confirmation",JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE,icon);
					if(ClickedButton==JOptionPane.YES_OPTION)
						{	
						PerEnBesoin P= new PerEnBesoin();
						P.Supprimer(id_PEB);
						
						btnRemoveADH.setEnabled(false);
						btnModADH.setEnabled(false);
						UpedateTableAD();
						}
					}
			}
		});
		btnRemoveADH.setIcon(new ImageIcon(GestionPerEnBesoin.class.getResource("/GestionPEB_img/remove.png")));
		btnRemoveADH.setBounds(999, 300, 32, 32);
		ButtonStyle(btnRemoveADH);
		contentPane.add(btnRemoveADH);
		
		JButton btnRecherche = new JButton("");
		btnRecherche.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent arg0) {
				btnRecherche.setIcon(new ImageIcon(GestionPerEnBesoin.class.getResource("/GestionPEB_img/chercher selected.png")));
			}
			@Override
			public void mouseExited(MouseEvent e) {
				btnRecherche.setIcon(new ImageIcon(GestionPerEnBesoin.class.getResource("/GestionPEB_img/chercher.png")));
			}
		});
		btnRecherche.setIcon(new ImageIcon(GestionPerEnBesoin.class.getResource("/GestionPEB_img/chercher.png")));
		btnRecherche.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				frame.setLocationRelativeTo(null);
				frame.setVisible(true);
			}
		});
		btnRecherche.setToolTipText("Chercher Personne");
		ButtonStyle(btnRecherche);
		btnRecherche.setBounds(1061, 300, 32, 32);
		contentPane.add(btnRecherche);
		
//le background////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		JLabel BGStaff = new JLabel("BGStaffM");
		BGStaff.setIcon(new ImageIcon(GestionPerEnBesoin.class.getResource("/GestionPEB_img/Gestion des personnes en Besooin.png")));
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
	
//actualisation de la table/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	public void UpedateTableAD()
	{ //Nom ,Prenom  ,Ueser_Name ,password , DateN  ,sex ,ADR ,Numtel ,Specilité
		String sql ="Select * from PersoEnBesoin";
		try {
			prepared = cnx.prepareStatement(sql);
			resultat =prepared.executeQuery();
			tablePEB.setModel(DbUtils.resultSetToTableModel(resultat));//changer le model de la table
			//ajouter un sorter pour trier la table
			TableRowSorter<TableModel> sorter = new TableRowSorter<TableModel>(tablePEB.getModel());
			tablePEB.setRowSorter(sorter);
			List<RowSorter.SortKey> sortKeys = new ArrayList<>(25);
			sortKeys.add(new RowSorter.SortKey(13, SortOrder.ASCENDING));//trier  par apport a la date d'ajout(date d'enregistrement)
			sortKeys.add(new RowSorter.SortKey(0, SortOrder.ASCENDING));//trier  par apport a l'ID
			sorter.setSortKeys(sortKeys);
		
			//renomer les noms de colones
			tablePEB.getColumnModel().getColumn( 0 ).setHeaderValue("N°");
			tablePEB.getColumnModel().getColumn( 1 ).setHeaderValue("Nom");
			tablePEB.getColumnModel().getColumn( 2 ).setHeaderValue("Prenom");
			tablePEB.getColumnModel().getColumn( 3 ).setHeaderValue("Sexe");
			tablePEB.getColumnModel().getColumn( 4 ).setHeaderValue("N°phone");
			tablePEB.getColumnModel().getColumn( 5 ).setHeaderValue("Cause");
			tablePEB.getColumnModel().getColumn( 6 ).setHeaderValue("état covid");
			tablePEB.getColumnModel().getColumn( 7 ).setHeaderValue("Profession/ex");
			tablePEB.getColumnModel().getColumn( 8 ).setHeaderValue("Marié ?");
			tablePEB.getColumnModel().getColumn( 9 ).setHeaderValue("N° famille");
			tablePEB.getColumnModel().getColumn( 10 ).setHeaderValue("@");
			tablePEB.getColumnModel().getColumn( 11 ).setHeaderValue("Adresse");
			tablePEB.getColumnModel().getColumn( 12 ).setHeaderValue("Wilaya");
			tablePEB.getColumnModel().getColumn( 13 ).setHeaderValue("Date de naissance");	
			tablePEB.getColumnModel().getColumn( 14 ).setHeaderValue("Lieu de naissance");
			tablePEB.getColumnModel().getColumn( 15 ).setHeaderValue("Date d'enregistrement");
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
	
	

