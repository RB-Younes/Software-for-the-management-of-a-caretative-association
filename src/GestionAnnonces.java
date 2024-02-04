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

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.RowSorter;
import javax.swing.SortOrder;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.border.EmptyBorder;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

import com.formdev.flatlaf.FlatIntelliJLaf;

import net.proteanit.sql.DbUtils;

////////////////////////////////////////////////////////////////////////////////-----------Fenetre Consultation------------///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

public class GestionAnnonces extends JFrame {

	
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JButton btnAddANN;
	private JButton btnSuppAnn;
	private JButton btnModANN;
	private String ID_Ann;
	private String contenu= null;

	
	Connection cnx=null;
	PreparedStatement prepared = null;
	ResultSet resultat =null; 
	PreparedStatement prepared1 = null;
	ResultSet resultat1 =null; 
	
	private int posX = 0;   //Position X de la souris au clic
    private int posY = 0;   //Position Y de la souris au clic
	private JTable tableANN; //table 
	@SuppressWarnings("unused")
	private JTextArea textAreaAnn;
	
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
					GestionAnnonces frame = new GestionAnnonces("ReMo1");
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
	
public GestionAnnonces(String ID_ADHERENT) {
		setIconImage(Toolkit.getDefaultToolkit().getImage(GestionAnnonces.class.getResource("/PsyConsultation_img/medical-report.png")));
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
		setShape(new RoundRectangle2D.Double(0d, 0d, 1200d, 550, 25d, 25d));
		setResizable(false);
		setTitle("Staff Management");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(0, 0, 1200, 620);
		setLocationRelativeTo(null);
		
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
	//Diagnostique+scrollpane-----------------------------------------------------------------------------------------------------------------------------------------------------
		JScrollPane scrDiag = new JScrollPane();
		scrDiag.setSize(794, 141);
		scrDiag.setLocation(31, 155);
		contentPane.add(scrDiag);
		textAreaAnn = new JTextArea();
		scrDiag.setViewportView(textAreaAnn);
		textAreaAnn.addKeyListener(new KeyAdapter() {
		    public void keyTyped(KeyEvent e) { 
		        if (		textAreaAnn.getText().length() >= 4000 ) 
		            e.consume(); 
		    }  
		});
		textAreaAnn.setFont(new Font("Segoe UI", Font.PLAIN, 13));
		textAreaAnn.setForeground(new Color(255, 255, 255));
		textAreaAnn.setBackground(new Color(255, 165, 0));
	
	//Mini table Table Annonce***************************************************************************************************************************************************************************************	
		JScrollPane scrollPaneANN = new JScrollPane();
		scrollPaneANN.setBounds(31, 329, 1140, 188);
		contentPane.add(scrollPaneANN);
		tableANN = new JTable();
		tableANN.setFont(new Font("Segoe UI", Font.BOLD, 12));
		tableANN.setForeground(new Color(255, 255, 255));
		tableANN.setBorder(null);
		tableANN.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				btnModANN.setEnabled(true);
				btnSuppAnn.setEnabled(true);
				int ligne=tableANN.getSelectedRow();
				String contenu1="";
				String contenu2="";
				ID_Ann =tableANN.getValueAt(ligne, 0).toString();
				String sql="Select AnnIn from Annonce where ID_Ann='"+ID_Ann+"'";
				try {
					prepared=cnx.prepareStatement(sql);
					resultat=prepared.executeQuery();
					if(resultat.next())
					{
						contenu=resultat.getString("AnnIn");
						 int DebContenuindice=contenu.indexOf("##");//indice de fin d l'id
						 if (DebContenuindice != -1) 
						 {
							  contenu1= contenu.substring(DebContenuindice+2 ,contenu.length() ); 
							  int FinContenuindice=contenu1.indexOf("##");
							  contenu2=contenu1.substring(0 ,FinContenuindice); 
						 }
						 textAreaAnn.setText(contenu2);
					}
				} catch (SQLException  e) {
					// TODO Auto-generated catch block
					JOptionPane.showMessageDialog(null,e);
				}
			}
		});
		tableANN.setBackground(new Color(255, 165, 0));
		scrollPaneANN.setViewportView(tableANN);
		
// Bouton Reduire ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		JButton Minimise_BTN = new JButton("");
		Minimise_BTN.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				Minimise_BTN.setIcon(new ImageIcon(GestionAnnonces.class.getResource("/Log_in_img/Minimize ML selected.png")));//changer les couleurs button
			}
			@Override
			public void mouseExited(MouseEvent e) {
				Minimise_BTN.setIcon(new ImageIcon(GestionAnnonces.class.getResource("/Log_in_img/Minimize ML .png")));//remetre le bouton de base
			}
		});
		Minimise_BTN.setToolTipText("Minimize");
		Minimise_BTN.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setState(ICONIFIED);
				
			}
		});
		Minimise_BTN.setIcon(new ImageIcon(GestionAnnonces.class.getResource("/Log_in_img/Minimize ML .png")));
		Minimise_BTN.setBounds(1064, 11, 32, 32);
		ButtonStyle(Minimise_BTN);
		contentPane.add(Minimise_BTN);

		
// Bouton Exit ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		JButton Exit_BTN = new JButton("");
		Exit_BTN.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent arg0) {
				Exit_BTN.setIcon(new ImageIcon(GestionAnnonces.class.getResource("/Log_in_img/Exit ML selected.png")));//changer les couleurs button
			}
			@Override
			public void mouseExited(MouseEvent arg0) {
				Exit_BTN.setIcon(new ImageIcon(GestionAnnonces.class.getResource("/Log_in_img/Exit ML.png")));//remetre le bouton de base
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
		Exit_BTN.setIcon(new ImageIcon(GestionAnnonces.class.getResource("/Log_in_img/Exit ML.png")));
		Exit_BTN.setBounds(1148, 11, 32, 32);
		ButtonStyle(Exit_BTN);
		contentPane.add(Exit_BTN);

//Actualiser la table des annonces /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////		
		UpedateTableAnnonces();

//Boutons Ajouter,Supprimer,Modifier///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	//Supprimer une Annonce**********************************************************************************************************************************************************************************************************************************
		btnSuppAnn = new JButton();
		btnSuppAnn.setEnabled(false);
		btnSuppAnn.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				if (btnSuppAnn.isEnabled()) {
					btnSuppAnn.setIcon(new ImageIcon(GestionAnnonces.class.getResource("/GestionAnnonces_img/remove selected.png")));//changer les couleurs button
				}
			}
			@Override
			public void mouseExited(MouseEvent e) {
				if (btnSuppAnn.isEnabled()) {
					btnSuppAnn.setIcon(new ImageIcon(GestionAnnonces.class.getResource("/GestionAnnonces_img/remove.png")));//remetre le bouton de base
				}
			}
		});
		ButtonStyle(btnSuppAnn);
		btnSuppAnn.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				
				if (tableANN.getSelectedRow()==-1)
				{
					JOptionPane.showMessageDialog(null, "S'il vous plait veuillez selectioner une ligne a supprimer !","Message", JOptionPane.QUESTION_MESSAGE,new ImageIcon(GestionAnnonces.class.getResource("/messages_img/error.png")));
				}
			else{
				 ImageIcon icon = new ImageIcon(GestionPerEnBesoin.class.getResource("/messages_img/Questions secretaire.gif"));
				 icon.getImage().flush();
				int ClickedButton	=JOptionPane.showConfirmDialog(null, "Voulez Vous vraiment supprimer cette Annonce ?", "Confirmation",JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE,icon);
				if(ClickedButton==JOptionPane.YES_OPTION)
					{	
					Annonance A= new Annonance();
					A.Supprimer(ID_Ann);
					btnSuppAnn.setEnabled(false);
					btnModANN.setEnabled(false);
					UpedateTableAnnonces();
					}
				}
			}
		});
		btnSuppAnn.setToolTipText("Supprimer une Annonce");
		btnSuppAnn.setIcon(new ImageIcon(GestionAnnonces.class.getResource("/GestionAnnonces_img/remove.png")));
		btnSuppAnn.setBounds(1055, 250, 32, 32);
		contentPane.add(btnSuppAnn);
		
		
	//Ajouter une Annonce**********************************************************************************************************************************************************************************************************************************
		btnAddANN = new JButton();
		btnAddANN.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent arg0) {
				if (btnAddANN.isEnabled()) {
					btnAddANN.setIcon(new ImageIcon(GestionAnnonces.class.getResource("/GestionAnnonces_img/plus selected.png")));
				}
			}
			@Override
			public void mouseExited(MouseEvent e) {
				if (btnAddANN.isEnabled()) {
					btnAddANN.setIcon(new ImageIcon(GestionAnnonces.class.getResource("/GestionAnnonces_img/plus.png")));
				}
			}
		});
		ButtonStyle(btnAddANN);
		btnAddANN.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				 ImageIcon icon = new ImageIcon(GestionAnnonces.class.getResource("/messages_img/Questions menu medcin.gif"));
				 icon.getImage().flush(); // réinitialiser l'animation
				//message de confirmation
				int ClickedButton	=JOptionPane.showConfirmDialog(null, "Vouler Vous vraiment Ajouter cette Annonce ?", "Confirmation",JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, icon);
				if(ClickedButton==JOptionPane.YES_OPTION)
				{
					if (textAreaAnn.getText().equals("")) {
						JOptionPane.showMessageDialog(null, " S'il vous plait veuillez Remplire le champ Annonce !","!", JOptionPane.QUESTION_MESSAGE, new ImageIcon(GestionAnnonces.class.getResource("/messages_img/error.png")));
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
						
						String Annonce_Contenu ="Annonce postée par: "+Nom+" "+Prenom+",le: "+datactuelle+"."+"##"+textAreaAnn.getText().toString()+"##";
						Annonance A = new Annonance(ID_ADHERENT,Annonce_Contenu, datactuelle);
						A.Ajouter();
						UpedateTableAnnonces();
					}
				}
			}
		});
		btnAddANN.setToolTipText("Ajouter une Annonce");
		btnAddANN.setIcon(new ImageIcon(GestionAnnonces.class.getResource("/GestionAnnonces_img/plus.png")));
		btnAddANN.setBounds(883, 250, 32, 32);
		contentPane.add(btnAddANN);
		
	//Modifie une consultation**********************************************************************************************************************************************************************************************************************************
		btnModANN = new JButton();
		btnModANN.setEnabled(false);
		btnModANN.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				if (btnModANN.isEnabled()) {
					btnModANN.setIcon(new ImageIcon(GestionAnnonces.class.getResource("/GestionAnnonces_img/pencil  selected.png")));//changer les couleurs button
				}
			}
			@Override
			public void mouseExited(MouseEvent e) {
				if (btnModANN.isEnabled()) {
					btnModANN.setIcon(new ImageIcon(GestionAnnonces.class.getResource("/GestionAnnonces_img/pencil.png")));//remetre le bouton de base
				}
			}
		});
		ButtonStyle(btnModANN);
		btnModANN.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				 ImageIcon icon = new ImageIcon(PsyConsultationFrame.class.getResource("/messages_img/Questions menu medcin.gif"));//Animation "Question"
				 icon.getImage().flush(); // réinitialiser l'animation
				//message de confirmation
				int ClickedButton	=JOptionPane.showConfirmDialog(null, "Vouler Vous vraiment Modifier cette Annonce?", "Confirmation",JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, icon);
				if (ClickedButton==JOptionPane.YES_OPTION) {
					if (tableANN.getSelectedRow()==-1 )  
					{//message
					JOptionPane.showMessageDialog(null, "S'il vous plait veuillez selectioner une ligne de la table Annonce a modifer !","Warning" ,JOptionPane.PLAIN_MESSAGE, new ImageIcon(GestionAnnonces.class.getResource("/messages_img/error.png")));
					}
					else 
					{
						if (textAreaAnn.getText().equals("")) {
							JOptionPane.showMessageDialog(null, " S'il vous plait veuillez Remplire le champ Annonce !","!", JOptionPane.QUESTION_MESSAGE, new ImageIcon(GestionAnnonces.class.getResource("/messages_img/error.png")));
						}
						else {
							Date actuelle = new Date();
							DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
							String datactuelle = dateFormat.format(actuelle);
							
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
							String COntenuFinal=contenu1+"##"+textAreaAnn.getText()+contenu3;
								Annonance A =new Annonance();
								A.Modifier(ID_Ann, ID_ADHERENT, COntenuFinal, datactuelle);
								
								btnSuppAnn.setEnabled(false);
								btnModANN.setEnabled(false);
								UpedateTableAnnonces();
						}
					
					
					}
				}
			}
		});
		btnModANN.setToolTipText("Modifier une Annonce");
		btnModANN.setIcon(new ImageIcon(GestionAnnonces.class.getResource("/GestionAnnonces_img/pencil.png")));
		btnModANN.setBounds(965, 250, 32, 32);
		contentPane.add(btnModANN);
		
//Boutton home//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
				JButton btnHome = new JButton("");
				btnHome.setIcon(new ImageIcon(GestionAnnonces.class.getResource("/Log_in_img/home.png")));
				btnHome.addMouseListener(new MouseAdapter() {
					@Override
					public void mouseEntered(MouseEvent e) {
						btnHome.setIcon(new ImageIcon(GestionAnnonces.class.getResource("/Log_in_img/home selected.png")));//changer les couleurs button
					}
					@Override
					public void mouseExited(MouseEvent e) {
						btnHome.setIcon(new ImageIcon(GestionAnnonces.class.getResource("/Log_in_img/home.png")));//remetre le bouton de base
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
		
// Fields////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////		
		//le bale pour l'arrier plan----------------------------------------------------------------------------------------------------------------------------------------------------------
		JLabel BGPsyCo = new JLabel("BG");
		BGPsyCo.setIcon(new ImageIcon(GestionAnnonces.class.getResource("/GestionAnnonces_img/Gestion des Annonces BG.png")));
		BGPsyCo.setBounds(0, 0, 1200, 550);
		contentPane.add(BGPsyCo);
	}
	
//methode du style des buttons/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	private void ButtonStyle(JButton btn) {
		btn.setOpaque(false);
		 btn.setFocusPainted(false);
		 btn.setBorderPainted(false);
		 btn.setContentAreaFilled(false);
	}

		
//actualiser la table ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
public void UpedateTableAnnonces()
{ 	
	String sql ="Select ID_Ann,DateAnn,AnnIn from Annonce";
	try {
		
		prepared = cnx.prepareStatement(sql);
		resultat =prepared.executeQuery();
		tableANN.setModel(DbUtils.resultSetToTableModel(resultat));
		TableRowSorter<TableModel> sorter = new TableRowSorter<TableModel>(tableANN.getModel());
		tableANN.setRowSorter(sorter);
		List<RowSorter.SortKey> sortKeys = new ArrayList<>(25);
		sortKeys.add(new RowSorter.SortKey(1, SortOrder.ASCENDING));	
		sorter.setSortKeys(sortKeys);
		//renomer les  colones
		tableANN.getColumnModel().getColumn( 0 ).setHeaderValue("N°");
		tableANN.getColumnModel().getColumn( 1 ).setHeaderValue("Date");	
		tableANN.getColumnModel().getColumn( 2 ).setHeaderValue("Annonce");

} catch (SQLException e) {
// TODO Auto-generated catch block
	JOptionPane.showMessageDialog(null,e);
}

}

}

	

