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
import java.util.ArrayList;
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

import net.proteanit.sql.DbUtils;
import java.awt.Toolkit;



public class Article_frame {
	
	Connection cnx=null;
	PreparedStatement prepared = null;
	ResultSet resultat =null; 
	ResultSet resultat1 =null; 

	 JFrame frmGestionDuStock;
	private JTextField textFieldQTE;
	private JTextField textFieldLBL;
	private JTable tableArticle;
	private final JLabel lblNewLabel = new JLabel("");
	private String ID_ARTICLE;
	private JButton btnSupprimer;
	
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
					Article_frame window = new Article_frame("ReMo1");
					window.frmGestionDuStock.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public Article_frame(String ID_ADEHRENT) {
		initialize(ID_ADEHRENT);
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize(String ID_ADEHRENT) {
		// CNX 
				cnx = ConnexionDB.CnxDB();
				
		frmGestionDuStock = new JFrame();
		frmGestionDuStock.setIconImage(Toolkit.getDefaultToolkit().getImage(Article_frame.class.getResource("/GestionDuStock_img/delivery-box.png")));
		frmGestionDuStock.setBounds(100, 100, 1200,550 );
		frmGestionDuStock.getContentPane().setLayout(null);
		
		frmGestionDuStock.setUndecorated(true);	
		frmGestionDuStock.setResizable(false);
		frmGestionDuStock.setTitle("gestion du Stock");
		frmGestionDuStock.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmGestionDuStock.setShape(new RoundRectangle2D.Double(0d, 0d, 1200d, 550d, 25d, 25d));
		frmGestionDuStock.setLocationRelativeTo(null);
		//vu que la frame est Undecorated on a besoin de ces MouseListeners pour la faire bouger(frame)
		frmGestionDuStock.  addMouseListener(new MouseAdapter() {
	            @Override
	            //on recupere les coordonnées de la souris
	            public void mousePressed(MouseEvent e) {
	                posX = e.getX();    //Position X de la souris au clic
	                posY = e.getY();    //Position Y de la souris au clic
	            }
	        });
		frmGestionDuStock.  addMouseMotionListener(new MouseMotionAdapter() {
	            // A chaque deplacement on recalcul le positionnement de la fenetre
	            @Override
	            public void mouseDragged(MouseEvent e) {
	                int depX = e.getX() - posX;
	                int depY = e.getY() - posY;
	                frmGestionDuStock.setLocation(frmGestionDuStock.getX()+depX,frmGestionDuStock.getY()+depY);
	            }
	        });
		textFieldQTE = new JTextField();
		textFieldQTE.addKeyListener(new KeyAdapter() {
		    public void keyTyped(KeyEvent e) { 
		        if (		textFieldQTE.getText().length() >= 20 ) 
		            e.consume(); 
		    }  
		});
		textFieldQTE.setForeground(new Color(255, 255, 255));
		textFieldQTE.setFont(new Font("Segoe UI", Font.BOLD, 14));
		textFieldQTE.setBackground(new Color(0, 153, 0));
		textFieldQTE.setBounds(775, 165, 262, 37);
		frmGestionDuStock.getContentPane().add(textFieldQTE);
		textFieldQTE.setColumns(10);
		
		JButton btnAjouter = new JButton("");
		btnAjouter.setToolTipText("Ajouter Article");
		btnAjouter.setIcon(new ImageIcon(Article_frame.class.getResource("/GestionDuStock_img/plus.png")));
		btnAjouter.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent arg0) {
				btnAjouter.setIcon(new ImageIcon(Article_frame.class.getResource("/GestionDuStock_img/plus selected.png")));
			}
			@Override
			public void mouseExited(MouseEvent e) {
				btnAjouter.setIcon(new ImageIcon(Article_frame.class.getResource("/GestionDuStock_img/plus.png")));
			}
		});
		btnAjouter.setFont(new Font("Comic Sans MS", Font.BOLD, 12));
		btnAjouter.setBackground(new Color(238, 232, 170));
		btnAjouter.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ImageIcon icon = new ImageIcon(GestionPerEnBesoin.class.getResource("/messages_img/Questions secretaire.gif"));//Animation "Question"
				 icon.getImage().flush(); // réinitialiser l'animation
				 //message de confirmation
				int ClickedButton	=JOptionPane.showConfirmDialog(null, "Voulez Vous vraiment ajouter cet Article ?", "Confirmation",JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, icon);
				if(ClickedButton==JOptionPane.YES_OPTION)
					{
					if (textFieldLBL.getText().equals("") || textFieldQTE.getText().equals("")) {
						JOptionPane.showMessageDialog(null, " S'il vous plait veuillez Remplire les champs Marqués par '*' !","Warning", JOptionPane.QUESTION_MESSAGE, new ImageIcon(GestionAnnonces.class.getResource("/messages_img/error.png")));
					}
					else {
						Article a = new Article( textFieldQTE.getText(), textFieldLBL.getText());
						a.ajouter();
						UpedateTableArticle();
					}
					}
			}
		});
		btnAjouter.setBounds(183, 307, 32, 32);
		ButtonStyle(btnAjouter);
		frmGestionDuStock.getContentPane().add(btnAjouter);
		
		JButton btnModifer = new JButton("");
		btnModifer.setEnabled(false);
		btnModifer.setIcon(new ImageIcon(Article_frame.class.getResource("/GestionDuStock_img/pencil.png")));
		btnModifer.setToolTipText("Modifier Article");
		btnModifer.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent arg0) {
				if (btnModifer.isEnabled()) {
					btnModifer.setIcon(new ImageIcon(Article_frame.class.getResource("/GestionDuStock_img/pencil selected.png")));
				}
			}
			@Override
			public void mouseExited(MouseEvent e) {
				btnModifer.setIcon(new ImageIcon(Article_frame.class.getResource("/GestionDuStock_img/pencil.png")));
			}
		});
		btnModifer.setFont(new Font("Comic Sans MS", Font.BOLD, 12));
		ButtonStyle(btnModifer);
		btnModifer.setBackground(new Color(238, 232, 170));
		btnModifer.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				 ImageIcon icon = new ImageIcon(GestionPerEnBesoin.class.getResource("/messages_img/Questions secretaire.gif"));//Animation "Question"
				 icon.getImage().flush(); // réinitialiser l'animation
				//message de confirmation
				int ClickedButton	=JOptionPane.showConfirmDialog(null, "Voulez Vous vraiment Modifier cet Article ? ", "Confirmation",JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, icon);
				if(ClickedButton==JOptionPane.YES_OPTION)
				{
					if (textFieldLBL.getText().equals("") || textFieldQTE.getText().equals("")) {
						JOptionPane.showMessageDialog(null, " S'il vous plait veuillez Remplire les champs Marqués par '*' !","Warning", JOptionPane.QUESTION_MESSAGE, new ImageIcon(GestionAnnonces.class.getResource("/messages_img/error.png")));
					}
					else {
						Article a = new Article( textFieldQTE.getText(), textFieldLBL.getText());
						a.modifier(ID_ARTICLE);
						UpedateTableArticle();
						btnSupprimer.setEnabled(false);
						btnModifer.setEnabled(false);
					}
				}
				
			}
		});
		btnModifer.setBounds(183, 350, 32, 32);
		frmGestionDuStock.getContentPane().add(btnModifer);
		
	    btnSupprimer = new JButton("");
	    btnSupprimer.setEnabled(false);
		btnSupprimer.setIcon(new ImageIcon(Article_frame.class.getResource("/GestionDuStock_img/remove.png")));
		btnSupprimer.setToolTipText("Supprimer Article");
		btnSupprimer.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent arg0) {
				if (btnSupprimer.isEnabled()) {
					btnSupprimer.setIcon(new ImageIcon(Article_frame.class.getResource("/GestionDuStock_img/remove selected.png")));
				}
			}
			@Override
			public void mouseExited(MouseEvent e) {
				btnSupprimer.setIcon(new ImageIcon(Article_frame.class.getResource("/GestionDuStock_img/remove.png")));
			}
		});
		btnSupprimer.setFont(new Font("Comic Sans MS", Font.BOLD, 12));
		ButtonStyle(btnSupprimer);
		btnSupprimer.setBackground(new Color(238, 232, 170));
		btnSupprimer.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				 ImageIcon icon = new ImageIcon(GestionPerEnBesoin.class.getResource("/messages_img/Questions secretaire.gif"));//Animation "Question"
				 icon.getImage().flush(); // réinitialiser l'animation
				 //message de confirmation
				int ClickedButton	=JOptionPane.showConfirmDialog(null, "Voulez Vous vraiment supprimer cet Article?", "Confirmation",JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE,icon);
				if(ClickedButton==JOptionPane.YES_OPTION)
					{	
						Article a = new Article( textFieldQTE.getText(), textFieldLBL.getText());
						a.supprimer(ID_ARTICLE);
						UpedateTableArticle();
						textFieldLBL.setText("");
						textFieldQTE.setText("");
						btnSupprimer.setEnabled(false);
						btnModifer.setEnabled(false);
					}
			}
		});
		btnSupprimer.setBounds(183, 393, 32, 32);
		frmGestionDuStock.getContentPane().add(btnSupprimer);

		textFieldLBL = new JTextField();
		textFieldLBL.addKeyListener(new KeyAdapter() {
		    public void keyTyped(KeyEvent e) { 
		        if (		textFieldLBL.getText().length() >= 20 ) 
		            e.consume(); 
		    }  
		});
		textFieldLBL.setColumns(10);
		textFieldLBL.setForeground(new Color(255, 255, 255));
		textFieldLBL.setFont(new Font("Segoe UI", Font.BOLD, 14));
		textFieldLBL.setBorder(BorderFactory.createEmptyBorder());
		textFieldLBL.setBackground(new Color(0, 153, 0));
		textFieldLBL.setBounds(203, 165, 262, 37);
		frmGestionDuStock.getContentPane().add(textFieldLBL);
		textFieldQTE.setBorder(BorderFactory.createEmptyBorder());
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(280, 282, 889, 246);
		frmGestionDuStock.getContentPane().add(scrollPane);
		
		tableArticle = new JTable();
		tableArticle.setFont(new Font("Segoe UI", Font.BOLD, 11));
		tableArticle.setForeground(new Color(255, 255, 255));
		tableArticle.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				btnModifer.setEnabled(true);
				btnSupprimer.setEnabled(true);
				int ligne=tableArticle.getSelectedRow();

				ID_ARTICLE = tableArticle.getValueAt(ligne, 0).toString();
				String sql="Select * from ARTICLE where CODEA='"+ID_ARTICLE+"'";
				try {
					prepared=cnx.prepareStatement(sql);
					resultat=prepared.executeQuery();
					if(resultat.next())
					{
						textFieldLBL.setText(resultat.getString("LIBELLE"));
						textFieldQTE.setText(resultat.getString("QTE_Stck"));
					}
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					JOptionPane.showMessageDialog(null,e);
				}
				
			}
		});
		scrollPane.setViewportView(tableArticle);
		tableArticle.setBackground(new Color(0, 153, 0));
//Actualiser//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////	        
				UpedateTableArticle();  
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////	
		
		JButton btnEntrée = new JButton("Gestion Entr\u00E9es");
		btnEntrée.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent arg0) {
				btnEntrée.setForeground(Color.WHITE);
				lblNewLabel.setIcon(new ImageIcon(Article_frame.class.getResource("/GestionDuStock_img/3.png")));
			}
			@Override
			public void mouseExited(MouseEvent e) {
				btnEntrée.setForeground(Color.LIGHT_GRAY);
				lblNewLabel.setIcon(new ImageIcon(Article_frame.class.getResource("/GestionDuStock_img/1.png")));
			}
		});
		btnEntrée.setIcon(new ImageIcon(Article_frame.class.getResource("/GestionDuStock_img/livraison.png")));
		btnEntrée.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Entree_frame entree = new Entree_frame(ID_ADEHRENT);
				entree.frame.setVisible(true);
				entree.frame.setLocationRelativeTo(null);
				frmGestionDuStock.dispose();
			}
		});
		btnEntrée.setForeground(Color.LIGHT_GRAY);
		btnEntrée.setFont(new Font("Segoe UI", Font.BOLD, 14));
		ButtonStyle(btnEntrée);
		btnEntrée.setBounds(495, 220, 185, 46);
		frmGestionDuStock.getContentPane().add(btnEntrée);
		
		JButton btnSortie = new JButton("Gestion Sorties");
		btnSortie.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent arg0) {
				btnSortie.setForeground(Color.WHITE);
				lblNewLabel.setIcon(new ImageIcon(Article_frame.class.getResource("/GestionDuStock_img/2.png")));
			}
			@Override
			public void mouseExited(MouseEvent e) {
				btnSortie.setForeground(Color.LIGHT_GRAY);
				lblNewLabel.setIcon(new ImageIcon(Article_frame.class.getResource("/GestionDuStock_img/1.png")));
			}
		});
		btnSortie.setIcon(new ImageIcon(Article_frame.class.getResource("/GestionDuStock_img/livraison sortie.png")));
		btnSortie.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Sortie_frame srtie = new Sortie_frame(ID_ADEHRENT);
				srtie.frame.setVisible(true);
				srtie.frame.setLocationRelativeTo(null);
				frmGestionDuStock.dispose();
			}
		});
		ButtonStyle(btnSortie);
		btnSortie.setForeground(Color.LIGHT_GRAY);
		btnSortie.setFont(new Font("Segoe UI", Font.BOLD, 14));
		btnSortie.setBounds(749, 220, 185, 46);
		frmGestionDuStock.getContentPane().add(btnSortie);
		
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
						frmGestionDuStock.setState(frmGestionDuStock.ICONIFIED);
					}
				});
				Minimise_BTN.setIcon(new ImageIcon(MenuPrincipaleFrame.class.getResource("/Menu_Principale_img/Minimize ML .png")));
				Minimise_BTN.setBounds(1043, 11, 32, 32);
				frmGestionDuStock.getContentPane().add(Minimise_BTN);
// Exit bouton//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
				
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
							frmGestionDuStock.dispose();
						}
					}
					
				});
				Exit_BTN.setIcon(new ImageIcon(MenuPrincipaleFrame.class.getResource("/Menu_Principale_img/Exit ML.png")));
				Exit_BTN.setBounds(1123, 11, 32, 32);
				frmGestionDuStock.getContentPane().add(Exit_BTN);
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
										MenuPrincipaleFrame FramePrincipale = new MenuPrincipaleFrame(ID_ADEHRENT);// retourner au menu 
										FramePrincipale.setLocationRelativeTo(null);
										FramePrincipale.setVisible(true);
									frmGestionDuStock.dispose();
							}
						});
				btnHome.setIcon(new ImageIcon(GestionPerEnBesoin.class.getResource("/Log_in_img/home.png")));
				btnHome.setToolTipText("Retourner au Menu");
				btnHome.setBounds(1085, 11, 32, 32);
				ButtonStyle(btnHome);
				frmGestionDuStock.getContentPane().add(btnHome);		
// Vider les champs bouton////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
				JButton btnViderFields = new JButton("");
				btnViderFields.setToolTipText("Clear the fields");
				ButtonStyle(btnViderFields);
				btnViderFields.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						btnModifer.setEnabled(false);
						btnSupprimer.setEnabled(false);
						textFieldLBL.setText("");
						textFieldQTE.setText("");
						UpedateTableArticle();
					}
				});
				btnViderFields.setIcon(new ImageIcon(GestionPerEnBesoin.class.getResource("/GestionAD_img/trash.png")));
				btnViderFields.setBounds(183, 471, 32, 32);
				frmGestionDuStock.getContentPane().add(btnViderFields);
				
//BG//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////				
		lblNewLabel.setIcon(new ImageIcon(Article_frame.class.getResource("/GestionDuStock_img/1.png")));
		lblNewLabel.setBounds(0, 0, 1201, 550);
		frmGestionDuStock.getContentPane().add(lblNewLabel);
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////		
		
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
//Actualiser table///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////	 
	 public void UpedateTableArticle()
	 { 	
	 	
	 	try {
	 		String rq = "SELECT * FROM ARTICLE";
			PreparedStatement pp = conn().prepareStatement(rq);
			ResultSet rs = pp.executeQuery();
			tableArticle.setModel(DbUtils.resultSetToTableModel(rs));
	 		TableRowSorter<TableModel> sorter = new TableRowSorter<TableModel>(tableArticle.getModel());
	 		tableArticle.setRowSorter(sorter);
	 		List<RowSorter.SortKey> sortKeys = new ArrayList<>(25);
	 		sortKeys.add(new RowSorter.SortKey(0, SortOrder.ASCENDING));	
	 		sorter.setSortKeys(sortKeys);
	 		//renomer les  colones
	 		tableArticle.getColumnModel().getColumn( 0 ).setHeaderValue("Code Article");
	 		tableArticle.getColumnModel().getColumn( 1 ).setHeaderValue("Libellé Article");	
	 		tableArticle.getColumnModel().getColumn( 2 ).setHeaderValue("Quanttié en stock");

	 } catch (SQLException e) {
	 // TODO Auto-generated catch block
	 	JOptionPane.showMessageDialog(null,e);
	 }

}
}