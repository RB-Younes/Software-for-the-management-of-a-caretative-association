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
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.RowSorter;
import javax.swing.SortOrder;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.border.EmptyBorder;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

import com.formdev.flatlaf.FlatIntelliJLaf;

import net.proteanit.sql.DbUtils;
////////////////////////////////////////////////////////////////////////////////-----------Fenetre Bilan------------///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
public class FonctionFrame extends JFrame {

	
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;

	private int posX = 0;   //Position X de la souris au clic
    private int posY = 0;   //Position Y de la souris au clic
	
	Connection cnx=null;
	PreparedStatement prepared = null;
	ResultSet resultat =null; 
	
	private JTextField txtNomFonction;
	private JTable table_1;
	private String Code_F;
	private JButton btnModFonc;
	private JButton btnSuppFonc;
	private JButton btnAddFonc ;

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
					GestionAD frameG = new GestionAD("ReMo4");
					FonctionFrame frame = new FonctionFrame(frameG);
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
	public FonctionFrame(GestionAD G) {
		setIconImage(Toolkit.getDefaultToolkit().getImage(FonctionFrame.class.getResource("/GestionAD_img/brain.png")));
		
		cnx = ConnexionDB.CnxDB();
		
//FAIRE bouger le menu////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////		
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

		setLocationRelativeTo(null);
		setUndecorated(true);
		setShape(new RoundRectangle2D.Double(0d, 0d, 800, 650, 25d, 25d));
		setType(Type.POPUP);
		setResizable(false);
		setTitle("Gestion des Fonctions");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(0, 0, 800, 373);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
// Bouton Exit ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		JButton Exit_BTN = new JButton("");
		Exit_BTN.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent arg0) {
				Exit_BTN.setIcon(new ImageIcon(FonctionFrame.class.getResource("/Log_in_img/Exit ML selected.png")));//changer les couleurs button
			}
			@Override
			public void mouseExited(MouseEvent arg0) {
				Exit_BTN.setIcon(new ImageIcon(FonctionFrame.class.getResource("/Log_in_img/Exit ML.png")));//remetre le bouton de base
			}
		});
		Exit_BTN.setToolTipText("Exit");
		Exit_BTN.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			
				//message de confirmation
					int ClickedButton	=JOptionPane.showConfirmDialog(null, "Do you really want to leave?", "Close", JOptionPane.YES_NO_OPTION);
					if(ClickedButton==JOptionPane.YES_OPTION)
					{
						dispose();
					}
			}
			
		});
		Exit_BTN.setIcon(new ImageIcon(FonctionFrame.class.getResource("/Log_in_img/Exit ML.png")));
		Exit_BTN.setBounds(746, 11, 32, 32);
		ButtonStyle(Exit_BTN);
		contentPane.add(Exit_BTN);
		
// Fields////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////		
	// textField
		txtNomFonction = new JTextField();
		txtNomFonction.setOpaque(false);
		txtNomFonction.setBorder(null);
		txtNomFonction.setForeground(new Color(255, 255, 255));
		txtNomFonction.setFont(new Font("Segoe UI", Font.BOLD, 14));
		txtNomFonction.setBounds(62, 186, 207, 38);
		txtNomFonction.addKeyListener(new KeyAdapter() {
		    public void keyTyped(KeyEvent e) { //limiter le nombre caractere a 100
		        if (		txtNomFonction.getText().length() >= 100 ) 
		            e.consume(); 
		    }  
		});
		contentPane.add(txtNomFonction);
		
//ScrollPane Table Bilan////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(305, 114, 400, 232);
		contentPane.add(scrollPane);
		table_1 = new JTable();
		table_1.setForeground(Color.WHITE);
		table_1.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				//activer les boutons modifer bilan et supprimer
				btnModFonc.setEnabled(true);
				btnSuppFonc.setEnabled(true);
				int ligne=table_1.getSelectedRow();
				
				Code_F =table_1.getValueAt(ligne, 0).toString();
				String sql="Select * from Fonction where Code_F='"+Code_F+"'";
				
				try {
					prepared=cnx.prepareStatement(sql);
					resultat=prepared.executeQuery();
					if(resultat.next())
					{
						txtNomFonction.setText(resultat.getString("NomF"));
					}
				} catch (SQLException  e) {
					// TODO Auto-generated catch block
					JOptionPane.showMessageDialog(null,e);
				}
			}
		});
		table_1.setFont(new Font("Segoe UI", Font.BOLD, 12));
		table_1.setBackground(new Color(255, 153, 153));
		scrollPane.setViewportView(table_1);
		
//afficher le contenue de la table juste a l'ouverture////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		UpedateTable_1();
		
//Boutons Ajouter,Supprimer,Modifier///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		
	//Modifer une Fonction**********************************************************************************************************************************************************************************************************************************
		btnModFonc = new JButton();
		btnModFonc.setEnabled(false);//desactiver le bouton tant que acune ligne de la table n'a ete selectionné
		btnModFonc.addMouseListener(new MouseAdapter() {
					@Override
					public void mouseEntered(MouseEvent e) {
						if (btnModFonc.isEnabled()) {
							btnModFonc.setIcon(new ImageIcon(FonctionFrame.class.getResource("/GestionAD_img/pencil selected.png")));//changer les couleurs button
						}
					}
					@Override
					public void mouseExited(MouseEvent e) {
						if (btnModFonc.isEnabled()) {
							btnModFonc.setIcon(new ImageIcon(FonctionFrame.class.getResource("/GestionAD_img/pencil.png")));//remetre le bouton de base
						}
					}
				});
				
				btnModFonc.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						if (table_1.getSelectedRow()==-1) // return -1 quand aucune ligne est selectionnée
						{//message d'erreur
							JOptionPane.showMessageDialog(null, "S'il vous plait veuillez selectioner une ligne (Fonction) a modifer !");
						}
						else 
						{
							ImageIcon icon = new ImageIcon(FonctionFrame.class.getResource("/messages_img/Questions menu medcin.gif"));//Animation "Question"
							 icon.getImage().flush(); // réinitialiser l'animation
							//message de confirmation
							int ClickedButton	=JOptionPane.showConfirmDialog(btnAddFonc, "Vouler Vous vraiment Modifier cette Fonctioon?", "Confirmation",JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, icon);
							if(ClickedButton==JOptionPane.YES_OPTION)
							{
								if (txtNomFonction.getText().toString().equals("")) {
									JOptionPane.showMessageDialog(null, "SVP donnez le Nom de la Fonction!","Warning" ,JOptionPane.PLAIN_MESSAGE, new ImageIcon(FonctionFrame.class.getResource("/messages_img/error.png")));
								}
								else
								{
									Fonction F= new Fonction();
									F.Modifier(Code_F, txtNomFonction.getText().toString());
									UpedateTable_1();//actualiser la table
									G.INComboboxFonc(G.comboBoxFonction);//actualiser la combo box the de la fenetre mere
									//desactiver les boutons modifer bilan et supprimer
									btnModFonc.setEnabled(false);
									btnSuppFonc.setEnabled(false);
								}
								}
							}
						}
						
					}
				);
				btnModFonc.setToolTipText("Modifier analyse");
				btnModFonc.setIcon(new ImageIcon(FonctionFrame.class.getResource("/GestionAD_img/pencil.png")));
				btnModFonc.setBounds(130, 286, 32, 32);
				ButtonStyle(btnModFonc);
				contentPane.add(btnModFonc);
				
		//ajouter une Fonction**********************************************************************************************************************************************************************************************************************************
				btnAddFonc = new JButton();
				btnAddFonc.addMouseListener(new MouseAdapter() {
					@Override
					public void mouseEntered(MouseEvent arg0) {
						btnAddFonc.setIcon(new ImageIcon(FonctionFrame.class.getResource("/GestionAD_img/plus selected.png")));//changer les couleurs button
					}
					@Override
					public void mouseExited(MouseEvent e) {
						btnAddFonc.setIcon(new ImageIcon(FonctionFrame.class.getResource("/GestionAD_img/plus.png")));//remetre le bouton de base
					}
				});
				btnAddFonc.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent arg0) {
						ImageIcon icon = new ImageIcon(FonctionFrame.class.getResource("/messages_img/Questions menu medcin.gif"));//Animation "Question"
						 icon.getImage().flush(); // réinitialiser l'animation
						 //message de confirmation
						int ClickedButton	=JOptionPane.showConfirmDialog(btnAddFonc, "Vouler Vous vraiment Ajouter  cette Fonctioon?", "Confirmation",JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, icon);
						if(ClickedButton==JOptionPane.YES_OPTION)
						{
							if (txtNomFonction.getText().toString().equals("")) {
								JOptionPane.showMessageDialog(null, "SVP donnez le nom de la Fonction!","Warning" ,JOptionPane.PLAIN_MESSAGE, new ImageIcon(FonctionFrame.class.getResource("/messages_img/error.png")));
							}
							else
							{
							//recuperer la date du systeme
							Fonction F= new Fonction(txtNomFonction.getText().toString());
							F.Ajouter();
								UpedateTable_1();//actualiser la table
								G.INComboboxFonc(G.comboBoxFonction);//actualiser la combo box the de la fenetre mere
								//desactiver les boutons modifer bilan et supprimer
								btnModFonc.setEnabled(false);
								btnSuppFonc.setEnabled(false);
							}
						}
					}
				});
				btnAddFonc.setToolTipText("Ajouter Bilan");
				btnAddFonc.setIcon(new ImageIcon(FonctionFrame.class.getResource("/GestionAD_img/plus.png")));
				btnAddFonc.setBounds(50, 286, 32, 32);
				ButtonStyle(btnAddFonc);
				contentPane.add(btnAddFonc);
				
		//Supprimer une Fonction**********************************************************************************************************************************************************************************************************************************
		
				btnSuppFonc = new JButton();
				btnSuppFonc.setEnabled(false);//desactiver le bouton tant que acune ligne de la table n'a ete selectionné
				btnSuppFonc.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseEntered(MouseEvent e) {
					if (btnSuppFonc.isEnabled()) {
						btnSuppFonc.setIcon(new ImageIcon(FonctionFrame.class.getResource("/GestionAD_img/remove selected.png")));//changer les couleurs button
					}
					}
				@Override
				public void mouseExited(MouseEvent e) {
					if (btnSuppFonc.isEnabled()) {
						btnSuppFonc.setIcon(new ImageIcon(FonctionFrame.class.getResource("/GestionAD_img/remove.png")));//remetre le bouton de base
					}
							}
					});
				btnSuppFonc.addActionListener(new ActionListener() {
						@Override
						public void actionPerformed(ActionEvent e) {
								if (table_1.getSelectedRow()==-1) // return -1 quand aucune ligne est selectionnée
								{//afficher un message d'erreur
									JOptionPane.showMessageDialog(null, " S'il vous plait veuillez selectioner une ligne (Fonction) a supprimer !");

								}
								else {
									 ImageIcon icon = new ImageIcon(FonctionFrame.class.getResource("/messages_img/Questions menu medcin.gif"));//Animation "Question"
									 icon.getImage().flush();// réinitialiser l'animation
									//message de confirmation
									int ClickedButton	=JOptionPane.showConfirmDialog(btnSuppFonc, "Vouler Vous vraiment Supprimer cette Fonctioon ?", "Confirmation", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, icon);
								if(ClickedButton==JOptionPane.YES_OPTION)
								{
										Fonction F= new Fonction();
										F.Supprimer(Code_F);
										UpedateTable_1();//Actualiser la table
										G.INComboboxFonc(G.comboBoxFonction);//actualiser la combo box the de la fenetre mere
										//desactiver les boutons modifer bilan et supprimer
										btnModFonc.setEnabled(false);
										btnSuppFonc.setEnabled(false);
								}
								}
							}
						});
						btnSuppFonc.setToolTipText("Supprimer Bilan");
						btnSuppFonc.setIcon(new ImageIcon(FonctionFrame.class.getResource("/GestionAD_img/remove.png")));
						btnSuppFonc.setBounds(206, 286, 32, 32);
						ButtonStyle(btnSuppFonc);
						contentPane.add(btnSuppFonc);
						
//le background////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		JLabel lblNewLabel = new JLabel("BG");
		lblNewLabel.setIcon(new ImageIcon(FonctionFrame.class.getResource("/GestionAD_img/Gestion des Fonctions.png")));

		lblNewLabel.setBounds(0, 0, 800, 373);
		contentPane.add(lblNewLabel);
		
		
	}

//methode du style des buttons/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		private void ButtonStyle(JButton btn) {
			btn.setOpaque(false);
			 btn.setFocusPainted(false);
			 btn.setBorderPainted(false);
			 btn.setContentAreaFilled(false);
		}

//Actualiser la table//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////				
	public void UpedateTable_1()
	{ 	
		String sql ="Select * from Fonction";//
		try {
			
			prepared = cnx.prepareStatement(sql);
			table_1.setModel(DbUtils.resultSetToTableModel(prepared.executeQuery()));
			TableRowSorter<TableModel> sorter = new TableRowSorter<TableModel>(table_1.getModel());
			table_1.setRowSorter(sorter);
			List<RowSorter.SortKey> sortKeys = new ArrayList<>(25);
			sortKeys.add(new RowSorter.SortKey(0, SortOrder.DESCENDING));		
			sorter.setSortKeys(sortKeys);
			table_1.getColumnModel().getColumn( 0 ).setHeaderValue("N°");
			table_1.getColumnModel().getColumn( 1 ).setHeaderValue("Nom Fonction");	
			

	} catch (SQLException e) {
	// TODO Auto-generated catch block
		JOptionPane.showMessageDialog(null,e);
	}
	}
}
