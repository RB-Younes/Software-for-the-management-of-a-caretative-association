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

public class GestionUtilisateursExistants extends JFrame {

	
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTable tableUsers;
	private JButton btnModuser ;
	private JButton btnSuppUser;
	
	Connection cnx=null;
	PreparedStatement prepared = null;
	ResultSet resultat =null; 
	
	private int posX = 0;   //Position X de la souris au clic
    private int posY = 0;   //Position Y de la souris au clic
	private JTextField AdresseMailField;
	private JTextField MdpField;
	private JTextField MdpCField;
	private String id_User;
	private String id_adh;
	private JTextField textField;
	
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
					GestionUtilisateursExistants frame = new GestionUtilisateursExistants("ReMo1");
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
	
	public GestionUtilisateursExistants(String ID_ADHERENT) {
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
		setIconImage(Toolkit.getDefaultToolkit().getImage(GestionUtilisateursExistants.class.getResource("/Menu_Principale_img/reglage.png")));
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
		MdpField.setBounds(71, 323, 239, 37);
		MdpField.setForeground(new Color(255, 255, 255));
		MdpField.setFont(new Font("Microsoft PhagsPa", Font.BOLD, 16));
		MdpField.setBackground(new Color(153, 204, 255));
		MdpField.setOpaque(false);
		MdpField.setBorder(null);
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
		MdpCField.setBackground(new Color(255, 182, 193));
		MdpCField.setBorder(null);
		MdpCField.setOpaque(false);
		MdpCField.setBounds(71, 427, 239, 37);
		contentPane.add(MdpCField);
		MdpCField.setColumns(10);
		
	//Nom d'utilisateur text Field	
		AdresseMailField = new JTextField();
		AdresseMailField.setEnabled(false);
		AdresseMailField.addKeyListener(new KeyAdapter() {
		    public void keyTyped(KeyEvent e) { 
		        if (		AdresseMailField.getText().length() >= 30 ) 
		            e.consume(); 
		    }  
		});
		AdresseMailField.setForeground(new Color(255, 255, 255));
		AdresseMailField.setFont(new Font("Microsoft PhagsPa", Font.BOLD, 16));
		AdresseMailField.setBackground(new Color(153, 204, 255));
		AdresseMailField.setBorder(null);
		AdresseMailField.setBounds(71, 219, 239, 37);
		AdresseMailField.setOpaque(false);
		contentPane.add(AdresseMailField);
		AdresseMailField.setColumns(10);

//ScrollPane+table *Secretaire*//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		JScrollPane scrollPaneUsers = new JScrollPane();
		scrollPaneUsers.setViewportBorder(new TitledBorder(null, "", TitledBorder.LEADING, TitledBorder.TOP, null, Color.GRAY));
		scrollPaneUsers.setBackground(new Color(0, 51, 102));
		scrollPaneUsers.setBounds(435, 225, 532, 236);
		contentPane.add(scrollPaneUsers);
		
		tableUsers = new JTable();
		tableUsers.setFont(new Font("Segoe UI", Font.BOLD, 11));
		tableUsers.setForeground(new Color(255, 255, 255));
		tableUsers.setBorder(null);
		tableUsers.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				btnSuppUser.setEnabled(true);
				btnModuser.setEnabled(true);
				int ligne=tableUsers.getSelectedRow();
				id_adh =tableUsers.getValueAt(ligne, 3).toString();
				
				id_User =tableUsers.getValueAt(ligne, 0).toString();
				String sql="Select * from Users where ID_Users='"+id_User+"'";
				
				try {
					prepared=cnx.prepareStatement(sql);
					resultat=prepared.executeQuery();
					if(resultat.next())
					{
						String Adr=resultat.getString("AdrMail");
						AdresseMailField.setText(Adr);
						String MDP=resultat.getString("Password");
						MdpField.setText(MDP);
						MdpCField.setText(MDP);
						
						String ID_AD=resultat.getString("ID_AD");
						sql="Select * from Adherent where ID_AD='"+ID_AD+"'";
						prepared=cnx.prepareStatement(sql);
						resultat=prepared.executeQuery();
						if(resultat.next())
						{
							textField.setText(resultat.getString("Nom")+" "+resultat.getString("Prenom"));
						}
					}
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					JOptionPane.showMessageDialog(null,e);
				}
				
			}
		});
		tableUsers.setBackground(new Color(240, 128, 128));
		scrollPaneUsers.setViewportView(tableUsers);
//afficher le contenue table  juste a louverture/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

		UpedateTableUsers();
		
//les boutons  Supprimer,Modifier//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	//supprimer un Utilisateur**********************************************************************************************************************************************************************************************************************************
		btnSuppUser = new JButton();
		btnSuppUser.setEnabled(false);//desactiver le bouton tant que acune ligne de la table Medecin n'a ete selectionné
		btnSuppUser.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				if (btnModuser.isEnabled()) {
					btnSuppUser.setIcon(new ImageIcon(GestionUtilisateursExistants.class.getResource("/GestionUtilisateurs_img/retirer selected.png")));//changer les couleurs button
				}
			}
			@Override
			public void mouseExited(MouseEvent e) {
				btnSuppUser.setIcon(new ImageIcon(GestionUtilisateursExistants.class.getResource("/GestionUtilisateurs_img/retirer.png")));//remetre le bouton de base
			}
		});
		ButtonStyle(btnSuppUser);
		btnSuppUser.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				if (tableUsers.getSelectedRow()==-1) // return  quand aucune ligne esr selectionnée
				{
					JOptionPane.showMessageDialog(null, " Veuillez  selectionner une ligne de la table utilisateur a suprimer !","WArning" ,JOptionPane.PLAIN_MESSAGE, new ImageIcon(GestionUtilisateursExistants.class.getResource("/messages_img/error.png")));

				}
				else {
					ImageIcon icon = new ImageIcon(GestionUtilisateursExistants.class.getResource("/messages_img/Questions menu medcin.gif"));//Animation "Question"
					 icon.getImage().flush(); // réinitialiser l'animation
					//message de confirmation
					int ClickedButton	=JOptionPane.showConfirmDialog(	null, "Voulez vous vraiment supprimer cet Utilisateur  ?", "Confirmation", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, icon);
				if(ClickedButton==JOptionPane.YES_OPTION)
				{	
					
					if (!id_adh.equals(ID_ADHERENT))
					{//pour ne pas supprimer sa propre session
						String sql ="delete from Users where ID_Users="+id_User;
							try {
								prepared=cnx.prepareStatement(sql);
								prepared.execute();
								JOptionPane.showMessageDialog(null, "Utilisateur supprimé avec succès!","Suppression" ,JOptionPane.PLAIN_MESSAGE, new ImageIcon(AuthentificationFrame.class.getResource("/messages_img/stamp.png")));
								UpedateTableUsers();
								btnSuppUser.setEnabled(false);
								btnModuser.setEnabled(false);
							} catch (SQLException e1) {
								// TODO Auto-generated catch block
								JOptionPane.showMessageDialog(null, e1);
							}
					}
					else {
						JOptionPane.showMessageDialog(null, " Vous ne pouvez pas supprimer votre propre session (ca peux causer des erreurs) !","WArning" ,JOptionPane.PLAIN_MESSAGE, new ImageIcon(GestionUtilisateursExistants.class.getResource("/messages_img/error.png")));
					}
				}
				}
				
				
			}
		});
		btnSuppUser.setToolTipText("Supprimer Utilisateur");
		btnSuppUser.setIcon(new ImageIcon(GestionUtilisateursExistants.class.getResource("/GestionUtilisateurs_img/retirer.png")));
		btnSuppUser.setBounds(367, 355, 32, 32);
		contentPane.add(btnSuppUser);
		
	//Modifier un Utilisateur**********************************************************************************************************************************************************************************************************************************
		btnModuser = new JButton();
		btnModuser.setEnabled(false);//desactiver le bouton tant que acune ligne de la table Medecin n'a ete selectionné
		btnModuser.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				if (btnModuser.isEnabled()) {
					btnModuser.setIcon(new ImageIcon(GestionUtilisateursExistants.class.getResource("/GestionUtilisateurs_img/crayon selected.png")));//changer les couleurs button
				}
			}
			@Override
			public void mouseExited(MouseEvent e) {
				btnModuser.setIcon(new ImageIcon(GestionUtilisateursExistants.class.getResource("/GestionUtilisateurs_img/crayon.png")));//remetre le bouton de base
			}
		});
		ButtonStyle(btnModuser);
		btnModuser.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				if (tableUsers.getSelectedRow()==-1) // return  quand aucune ligne esr selectionnée
				{//message d'erreur
					JOptionPane.showMessageDialog(null, "Veuillez  selectionner une ligne de la table medecin a modifier !","WArning" ,JOptionPane.PLAIN_MESSAGE, new ImageIcon(GestionUtilisateursExistants.class.getResource("/messages_img/error.png")));
				}
				else 
				{
					ImageIcon icon = new ImageIcon(GestionUtilisateursExistants.class.getResource("/messages_img/Questions menu medcin.gif"));//Animation "Question"
					 icon.getImage().flush(); // réinitialiser l'animation
					//message de confirmation
					int ClickedButton	=JOptionPane.showConfirmDialog(null, "Voulez vous vraiment modifier cette ligne ?", "Confirmation",JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, icon);
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
											if (!resultat.getString("ID_Users").equals(id_User)) {
												JOptionPane.showMessageDialog(null, "Adresse mail déja utilisée !\n Veuillez choisir une non utilisée.","Warning", JOptionPane.QUESTION_MESSAGE, new ImageIcon(GestionAnnonces.class.getResource("/messages_img/error.png")));
											}
											else{
											String sql2 = "Update Users set AdrMail =?,Password =? where ID_Users='"+id_User+"'";
											prepared=cnx.prepareStatement(sql2);
											prepared.setString(1, AdresseMailField.getText());
											prepared.setString(2, MdpField.getText());	
											prepared.execute();
											JOptionPane.showMessageDialog(null, "Utilisateur Modifié avc succés!","Ajout" ,JOptionPane.PLAIN_MESSAGE, new ImageIcon(AuthentificationFrame.class.getResource("/messages_img/stamp.png")));
											UpedateTableUsers();
											btnSuppUser.setEnabled(false);
											btnModuser.setEnabled(false);
										}
										}
										
									} catch (SQLException e1) {
										// TODO Auto-generated catch block
										JOptionPane.showMessageDialog(null, e1);
									}
									
							}
						}
					}
				}
			}
		});
		btnModuser.setToolTipText("Modifier Utilisateur");
		btnModuser.setIcon(new ImageIcon(GestionUtilisateursExistants.class.getResource("/GestionUtilisateurs_img/crayon.png")));
		btnModuser.setBounds(367, 298, 32, 32);
		contentPane.add(btnModuser);
		
// Bouton Reduire ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		JButton Minimise_BTN = new JButton("");
		Minimise_BTN.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				Minimise_BTN.setIcon(new ImageIcon(GestionUtilisateursExistants.class.getResource("/Log_in_img/Minimize ML selected.png")));//changer les couleurs button
			}
			@Override
			public void mouseExited(MouseEvent e) {
				Minimise_BTN.setIcon(new ImageIcon(GestionUtilisateursExistants.class.getResource("/Log_in_img/Minimize ML .png")));//remetre le bouton de base
			}
		});
		Minimise_BTN.setToolTipText("Minimize");
		ButtonStyle(Minimise_BTN);
		Minimise_BTN.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setState(ICONIFIED);
			}
		});
		Minimise_BTN.setIcon(new ImageIcon(GestionUtilisateursExistants.class.getResource("/Log_in_img/Minimize ML .png")));
		Minimise_BTN.setBounds(855, 11, 32, 32);
		contentPane.add(Minimise_BTN);
		
// Vider les champs bouton(reinitialiser)////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		JButton btnViderFields = new JButton("");
		btnViderFields.setToolTipText("Vider les champs");
		ButtonStyle(btnViderFields);
		btnViderFields.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
			}
		});
		btnViderFields.setIcon(new ImageIcon(GestionUtilisateursExistants.class.getResource("/GestionAD_img/trash.png")));
		btnViderFields.setBounds(367, 427, 32, 32);
		contentPane.add(btnViderFields);
		
// Bouton Exit ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		JButton Exit_BTN = new JButton("");
		Exit_BTN.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent arg0) {
				Exit_BTN.setIcon(new ImageIcon(GestionUtilisateursExistants.class.getResource("/Log_in_img/Exit ML selected.png")));//changer les couleurs button
			}
			@Override
			public void mouseExited(MouseEvent arg0) {
				Exit_BTN.setIcon(new ImageIcon(GestionUtilisateursExistants.class.getResource("/Log_in_img/Exit ML.png")));//remetre le bouton de base
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
		Exit_BTN.setIcon(new ImageIcon(GestionUtilisateursExistants.class.getResource("/Log_in_img/Exit ML.png")));
		Exit_BTN.setBounds(939, 11, 32, 32);
		contentPane.add(Exit_BTN);
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
				GestionUtiMenu frame = new GestionUtiMenu(ID_ADHERENT);
				frame.setVisible(true);
				frame.setLocationRelativeTo(null);
				dispose();
			}
		});
		btnHome.setIcon(new ImageIcon(GestionUtilisateursExistants.class.getResource("/Menu_Principale_img/reglage.png")));
		btnHome.setToolTipText("Retourner au Menu");
		btnHome.setBounds(897, 11, 32, 32);
		contentPane.add(btnHome);		
		
		textField = new JTextField();
		textField.setEditable(false);
		textField.setEnabled(false);
		textField.setForeground(Color.WHITE);
		textField.setFont(new Font("Microsoft PhagsPa", Font.BOLD, 16));
		textField.setColumns(10);
		textField.setOpaque(false);
		textField.setBorder(null);
		textField.setBackground(new Color(153, 204, 255));
		textField.setBounds(400, 149, 239, 37);
		contentPane.add(textField);
		
		
//le background////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		JLabel BGStaff = new JLabel("BGStaffM");
		BGStaff.setIcon(new ImageIcon(GestionUtilisateursExistants.class.getResource("/GestionUtilisateurs_img/5.png")));
		BGStaff.setBounds(0, 0, 1000, 500);
		contentPane.add(BGStaff);
		
	}
	
//methode du style des buttons/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	private void ButtonStyle(JButton btn) {
		 btn.setOpaque(false);
		 btn.setFocusPainted(false);
		 btn.setBorderPainted(false);
		 btn.setContentAreaFilled(false);
	}

//actualiser la table MEdecin///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	public void UpedateTableUsers()
	{ 
		String sql ="Select * from Users";
		try {
			prepared = cnx.prepareStatement(sql);
			resultat =prepared.executeQuery();
			
			tableUsers.setModel(DbUtils.resultSetToTableModel(resultat));
			TableRowSorter<TableModel> sorter = new TableRowSorter<TableModel>(tableUsers.getModel());
			tableUsers.setRowSorter(sorter);
			
			List<RowSorter.SortKey> sortKeys = new ArrayList<>(25);
			sortKeys.add(new RowSorter.SortKey(0, SortOrder.ASCENDING));
			sorter.setSortKeys(sortKeys);

			tableUsers.getColumnModel().getColumn( 0 ).setHeaderValue("N° Utilisateur");
			tableUsers.getColumnModel().getColumn( 1 ).setHeaderValue("@");
			tableUsers.getColumnModel().getColumn( 2 ).setHeaderValue("Mot de passe");	
			tableUsers.getColumnModel().getColumn( 3 ).setHeaderValue("N° Adherent");
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




