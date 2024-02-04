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
import java.util.ArrayList;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
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

////////////////////////////////////////////////////////////////////////////////-----------Fenetre Gestion Users------------///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

public class GestionUtilisateursInexistants extends JFrame {

	
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTable tableADH;
	private JButton btnAddUser;
	
	Connection cnx=null;
	PreparedStatement prepared = null;
	ResultSet resultat =null; 
	
	private int posX = 0;   //Position X de la souris au clic
    private int posY = 0;   //Position Y de la souris au clic
	private JTextField AdresseMailField;
	private JTextField MdpField;
	private JTextField MdpCField;
	private String id_ADH;
	private JTextField textFieldNom;
	private JTextField textFieldPrenom;
	private JTextField textFieldFonction;
	
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
					GestionUtilisateursInexistants frame = new GestionUtilisateursInexistants("ReMo1");
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
	
	public GestionUtilisateursInexistants(String ID_ADHERENT) {
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

// initialisation/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		setUndecorated(true);
		setShape(new RoundRectangle2D.Double(0d, 0d, 1200d, 650d, 25d, 25d));
		setResizable(false);
		setIconImage(Toolkit.getDefaultToolkit().getImage(GestionUtilisateursInexistants.class.getResource("/Menu_Principale_img/reglage.png")));
		setTitle("Staff Management");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(0, 0, 1000, 500);
		
		
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
// Fields///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	//passwordField pour le mot de passe
		MdpField = new JPasswordField();
		MdpField.addKeyListener(new KeyAdapter() {
		    public void keyTyped(KeyEvent e) { 
		        if (		MdpField.getText().length() >= 30 ) //limiter le nombre de caractere a 30
		            e.consume(); 
		    }  
		});
		MdpField.setBounds(71, 343, 236, 37);
		MdpField.setForeground(new Color(255, 255, 255));
		MdpField.setFont(new Font("Microsoft PhagsPa", Font.BOLD, 16));
		MdpField.setBackground(new Color(255, 165, 0));
		MdpField.setBorder(null);
		MdpField.setOpaque(false);
		contentPane.add(MdpField);
		MdpField.setColumns(10);
		
	//Confirmer Mot de passe 	(PasswordField)
		MdpCField = new JPasswordField();
		MdpCField.addKeyListener(new KeyAdapter() {
		    public void keyTyped(KeyEvent e) { 
		        if (		MdpCField.getText().length() >= 30 ) 
		            e.consume(); 
		    }  
		});
		MdpCField.setForeground(new Color(255, 255, 255));
		MdpCField.setFont(new Font("Microsoft PhagsPa", Font.BOLD, 16));
		MdpCField.setBackground(new Color(255, 165, 0));
		MdpCField.setBorder(null);
		MdpCField.setBounds(71, 426, 236, 37);
		MdpCField.setOpaque(false);
		contentPane.add(MdpCField);
		MdpCField.setColumns(10);
		
	//Nom d'utilisateur text Field	
		AdresseMailField = new JTextField();
		AdresseMailField.addKeyListener(new KeyAdapter() {
		    public void keyTyped(KeyEvent e) { 
		        if (		AdresseMailField.getText().length() >= 30 ) 
		            e.consume(); 
		    }  
		});
		AdresseMailField.setForeground(new Color(255, 255, 255));
		AdresseMailField.setFont(new Font("Microsoft PhagsPa", Font.BOLD, 16));
		AdresseMailField.setBackground(new Color(255, 165, 0));
		AdresseMailField.setBorder(null);
		AdresseMailField.setOpaque(false);
		AdresseMailField.setBounds(71, 253, 236, 37);
		contentPane.add(AdresseMailField);
		AdresseMailField.setColumns(10);

//ScrollPane+table //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		JScrollPane scrollPaneADH = new JScrollPane();
		scrollPaneADH.setViewportBorder(new TitledBorder(null, "", TitledBorder.LEADING, TitledBorder.TOP, null, Color.GRAY));
		scrollPaneADH.setBackground(new Color(0, 51, 102));
		scrollPaneADH.setBounds(430, 221, 531, 242);
		contentPane.add(scrollPaneADH);
		
		tableADH = new JTable();
		tableADH.setFont(new Font("Segoe UI", Font.BOLD, 11));
		tableADH.setForeground(new Color(255, 255, 255));
		tableADH.setBorder(null);
		tableADH.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				int ligne=tableADH.getSelectedRow();
				btnAddUser.setEnabled(true);
				id_ADH =tableADH.getValueAt(ligne, 0).toString();
				String sql="Select * from Users where ID_AD='"+id_ADH+"'";
				
				try {
					prepared=cnx.prepareStatement(sql);
					resultat=prepared.executeQuery();
					if(resultat.next())
					{
						JOptionPane.showMessageDialog(null, "Cet Adherent est Deja un Utilisateur de l'application","Warning", JOptionPane.QUESTION_MESSAGE, new ImageIcon(GestionAnnonces.class.getResource("/messages_img/error.png")));
					}
					else{
						 sql="Select * from Adherent where ID_AD='"+id_ADH+"'";
							try {
								prepared=cnx.prepareStatement(sql);
								resultat=prepared.executeQuery();
								if(resultat.next())
								{
									String Nom=resultat.getString("Nom");
									textFieldNom.setText(Nom);
									String Prenom=resultat.getString("Prenom");
									textFieldPrenom.setText(Prenom);
									AdresseMailField.setText(resultat.getString("ADRmail"));
									
									String CF=resultat.getString("code_F");
									sql="Select * from Fonction where Code_F='"+CF+"'";
									prepared=cnx.prepareStatement(sql);
									resultat=prepared.executeQuery();
									if(resultat.next())
									{
										textFieldFonction.setText(resultat.getString("NomF"));
									}
									
								}
							} catch (SQLException e) {
								// TODO Auto-generated catch block
								JOptionPane.showMessageDialog(null,e);
							}
					}
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					JOptionPane.showMessageDialog(null,e);
				}
			}
		});
		tableADH.setBackground(new Color(255, 165, 0));
		scrollPaneADH.setViewportView(tableADH);
//afficher le contenue des deux tables  juste a louverture/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

		UpedateTableADH();
		
		
//Ajouter un utilisateeur**********************************************************************************************************************************************************************************************************************************
		btnAddUser = new JButton();
		btnAddUser.setEnabled(false);
		btnAddUser.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent arg0) {
				if (btnAddUser.isEnabled()) {
					btnAddUser.setIcon(new ImageIcon(GestionUtilisateursInexistants.class.getResource("/GestionUtilisateurs_img/plus selected.png")));//changer les couleurs button
				}
			}
			@Override
			public void mouseExited(MouseEvent e) {
				btnAddUser.setIcon(new ImageIcon(GestionUtilisateursInexistants.class.getResource("/GestionUtilisateurs_img/plus.png")));//remetre le bouton de base
			}
		});
		ButtonStyle(btnAddUser);
		btnAddUser.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				ImageIcon icon = new ImageIcon(GestionUtilisateursInexistants.class.getResource("/messages_img/Questions menu medcin.gif"));//Animation "Question"
				 icon.getImage().flush(); // réinitialiser l'animation
				//message de confirmation
				int ClickedButton	=JOptionPane.showConfirmDialog(null, "Voulez vous vraiment ajouter cet adherent en tant que utilisateur ?", "Confirmation", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, icon);
				if(ClickedButton==JOptionPane.YES_OPTION)
				{
					if (AdresseMailField.getText().equals("")||MdpField.getText().equals("")||MdpCField.getText().equals("")) {
						JOptionPane.showMessageDialog(null, "Veuillez svp remplire les champs marqués avec '*'","Warning", JOptionPane.QUESTION_MESSAGE, new ImageIcon(GestionAnnonces.class.getResource("/messages_img/error.png")));
					}
					else {
						if (!MdpField.getText().equals(MdpCField.getText())) {
							JOptionPane.showMessageDialog(null, " S'il vous plaît resaisissez le mot de passe!","WArning" ,JOptionPane.PLAIN_MESSAGE, new ImageIcon(GestionAnnonces.class.getResource("/messages_img/error.png")));
						}
						else {
							String sql="Select * from Users where AdrMail='"+AdresseMailField.getText()+"'";
							try {
								prepared=cnx.prepareStatement(sql);
								resultat=prepared.executeQuery();
								if(resultat.next())
								{
									JOptionPane.showMessageDialog(null, "Adresse mail déja utilisée !\n Veuillez choisir une non utilisée.","Warning", JOptionPane.QUESTION_MESSAGE, new ImageIcon(GestionAnnonces.class.getResource("/messages_img/error.png")));
								}
								else{
									 sql="insert into Users values (ID_Users.nextval,'"+AdresseMailField.getText()+"','"+MdpField.getText()+"','"+id_ADH+"')";
										try {
											prepared=cnx.prepareStatement(sql);
											resultat=prepared.executeQuery();
											btnAddUser.setEnabled(false);
											UpedateTableADH();
											JOptionPane.showMessageDialog(null, "Utilisateur ajouté avc succés!","Ajout" ,JOptionPane.PLAIN_MESSAGE, new ImageIcon(AuthentificationFrame.class.getResource("/messages_img/stamp.png")));
										} catch (SQLException e) {
											// TODO Auto-generated catch block
											JOptionPane.showMessageDialog(null,e);
										}
								}
							} catch (SQLException e) {
								// TODO Auto-generated catch block
								JOptionPane.showMessageDialog(null,e);
							}
						}
					}
				}
			}
		});
		btnAddUser.setToolTipText("Ajouter Utilisateur");
		btnAddUser.setIcon(new ImageIcon(GestionUtilisateursInexistants.class.getResource("/GestionUtilisateurs_img/plus.png")));
		btnAddUser.setBounds(364, 291, 32, 32);
		contentPane.add(btnAddUser);
		
// Bouton Reduire ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		JButton Minimise_BTN = new JButton("");
		Minimise_BTN.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				Minimise_BTN.setIcon(new ImageIcon(GestionUtilisateursInexistants.class.getResource("/Log_in_img/Minimize ML selected.png")));//changer les couleurs button
			}
			@Override
			public void mouseExited(MouseEvent e) {
				Minimise_BTN.setIcon(new ImageIcon(GestionUtilisateursInexistants.class.getResource("/Log_in_img/Minimize ML .png")));//remetre le bouton de base
			}
		});
		Minimise_BTN.setToolTipText("Minimize");
		ButtonStyle(Minimise_BTN);
		Minimise_BTN.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setState(ICONIFIED);
			}
		});
		Minimise_BTN.setIcon(new ImageIcon(GestionUtilisateursInexistants.class.getResource("/Log_in_img/Minimize ML .png")));
		Minimise_BTN.setBounds(846, 11, 32, 32);
		contentPane.add(Minimise_BTN);
		
// Vider les champs bouton(reinitialiser)////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		JButton btnViderFields = new JButton("");
		btnViderFields.setToolTipText("Vider les champs");
		ButtonStyle(btnViderFields);
		btnViderFields.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
			}
		});
		btnViderFields.setIcon(new ImageIcon(GestionUtilisateursInexistants.class.getResource("/GestionAD_img/trash.png")));
		btnViderFields.setBounds(364, 359, 32, 32);
		contentPane.add(btnViderFields);
		
// Bouton Exit ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		JButton Exit_BTN = new JButton("");
		Exit_BTN.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent arg0) {
				Exit_BTN.setIcon(new ImageIcon(GestionUtilisateursInexistants.class.getResource("/Log_in_img/Exit ML selected.png")));//changer les couleurs button
			}
			@Override
			public void mouseExited(MouseEvent arg0) {
				Exit_BTN.setIcon(new ImageIcon(GestionUtilisateursInexistants.class.getResource("/Log_in_img/Exit ML.png")));//remetre le bouton de base
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
		Exit_BTN.setIcon(new ImageIcon(GestionUtilisateursInexistants.class.getResource("/Log_in_img/Exit ML.png")));
		Exit_BTN.setBounds(930, 11, 32, 32);
		contentPane.add(Exit_BTN);
//Boutton home//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		JButton btnHome = new JButton("");
		ButtonStyle(btnHome);
		btnHome.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				btnHome.setIcon(new ImageIcon(GestionUtilisateursInexistants.class.getResource("/Menu_Principale_img/reglage selected.png")));//changer les couleurs button
			}
			@Override
			public void mouseExited(MouseEvent e) {
				btnHome.setIcon(new ImageIcon(GestionUtilisateursInexistants.class.getResource("/Menu_Principale_img/reglage.png")));//remetre le bouton de base
			}
		});
		btnHome.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				GestionUtiMenu frame = new GestionUtiMenu(ID_ADHERENT);
				frame.setVisible(true);
				frame.setLocationRelativeTo(null);
				dispose();
			}
		});
		btnHome.setIcon(new ImageIcon(GestionUtilisateursInexistants.class.getResource("/Menu_Principale_img/reglage.png")));
		btnHome.setToolTipText("Retourner au Menu");
		btnHome.setBounds(888, 11, 32, 32);
		contentPane.add(btnHome);		
		
		textFieldNom = new JTextField();
		textFieldNom.setEditable(false);
		textFieldNom.setEnabled(false);
		textFieldNom.setForeground(Color.WHITE);
		textFieldNom.setFont(new Font("Microsoft PhagsPa", Font.BOLD, 16));
		textFieldNom.setColumns(10);
		textFieldNom.setBorder(null);
		textFieldNom.setOpaque(false);
		textFieldNom.setBackground(new Color(255, 165, 0));
		textFieldNom.setBounds(71, 152, 236, 37);
		contentPane.add(textFieldNom);
		
		textFieldPrenom = new JTextField();
		textFieldPrenom.setEditable(false);
		textFieldPrenom.setEnabled(false);
		textFieldPrenom.setForeground(Color.WHITE);
		textFieldPrenom.setFont(new Font("Microsoft PhagsPa", Font.BOLD, 16));
		textFieldPrenom.setColumns(10);
		textFieldPrenom.setBorder(null);
		textFieldPrenom.setOpaque(false);
		textFieldPrenom.setBackground(new Color(255, 165, 0));
		textFieldPrenom.setBounds(393, 152, 236, 37);
		contentPane.add(textFieldPrenom);
		
		textFieldFonction = new JTextField();
		textFieldFonction.setEditable(false);
		textFieldFonction.setEnabled(false);
		textFieldFonction.setForeground(Color.WHITE);
		textFieldFonction.setFont(new Font("Microsoft PhagsPa", Font.BOLD, 16));
		textFieldFonction.setColumns(10);
		textFieldFonction.setBorder(null);
		textFieldFonction.setOpaque(false);
		textFieldFonction.setBackground(new Color(255, 165, 0));
		textFieldFonction.setBounds(730, 152, 232, 37);
		contentPane.add(textFieldFonction);
		
		
//le background////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		JLabel BGStaff = new JLabel("BGStaffM");
		BGStaff.setIcon(new ImageIcon(GestionUtilisateursInexistants.class.getResource("/GestionUtilisateurs_img/4.png")));
		BGStaff.setBounds(0, 0, 1000, 500);
		contentPane.add(BGStaff);
		
	}
	
//methode du style des buttons/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	private void ButtonStyle(JButton btn) {
				//enlever les bordures des btn
				 btn.setOpaque(false);
				 btn.setFocusPainted(false);
				 btn.setBorderPainted(false);
				 btn.setContentAreaFilled(false);
				 
	}

//actualiser la table MEdecin///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	public void UpedateTableADH()
	{ //Nom ,Prenom  ,Ueser_Name ,password , DateN  ,sex ,ADR ,Numtel ,Specilité
		String sql ="Select ID_AD,Nom ,Prenom ,ADRmail,code_F  from Adherent";
		try {
			prepared = cnx.prepareStatement(sql);
			resultat =prepared.executeQuery();
			tableADH.setModel(DbUtils.resultSetToTableModel(resultat));//changer le model de la table
			//ajouter un sorter pour trier la table
			TableRowSorter<TableModel> sorter = new TableRowSorter<TableModel>(tableADH.getModel());
			tableADH.setRowSorter(sorter);
			List<RowSorter.SortKey> sortKeys = new ArrayList<>(25);
			sortKeys.add(new RowSorter.SortKey(0, SortOrder.ASCENDING));//trier  par apport a l'id
			sortKeys.add(new RowSorter.SortKey(1, SortOrder.ASCENDING));//trier  par apport au nom
			sorter.setSortKeys(sortKeys);
			//renomer les  colones
			tableADH.getColumnModel().getColumn( 0 ).setHeaderValue("N°");
			tableADH.getColumnModel().getColumn( 1 ).setHeaderValue("Nom");
			tableADH.getColumnModel().getColumn( 2 ).setHeaderValue("Prenom");	
			tableADH.getColumnModel().getColumn( 3 ).setHeaderValue("@");	
			tableADH.getColumnModel().getColumn( 4 ).setHeaderValue("Code Fonction");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			JOptionPane.showMessageDialog(null,e);
		}
		
	}
//la verfication de la validité de la date///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////	
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




