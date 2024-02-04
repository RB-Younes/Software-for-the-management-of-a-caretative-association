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
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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
import com.toedter.calendar.JDateChooser;

import net.proteanit.sql.DbUtils;
import java.awt.Toolkit;

public class TSortie_frame {
	
	Connection cnx=null;
	PreparedStatement prepared = null;
	ResultSet resultat =null; 
	ResultSet resultat1 =null; 

	JFrame frame;
	private JTextField textFieldmnt;
	private JTable tableSortie;
	private JScrollPane scrollPane;
	private JLabel lblNewLabel_3;
	private JButton btnAjtSortie;
	private JButton btnModSortie;
	private String ID_SORTIE;
	private String DateS;
	
	private int posX = 0;   //Position X de la souris au clic
    private int posY = 0;   //Position Y de la souris au clic

	/**
	 * Launch the application.
	 */
	public static void main(String[] args)  throws UnsupportedLookAndFeelException {
		FlatIntelliJLaf.install();	
		UIManager.setLookAndFeel(new FlatIntelliJLaf() );
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					TSortie_frame window = new TSortie_frame("ReMo1");
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
	public TSortie_frame(String ID_ADEHRENT) {
		initialize(ID_ADEHRENT);
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize(String ID_ADEHRENT) {
		// CNX 
		cnx = ConnexionDB.CnxDB();
		frame = new JFrame();
		frame.setIconImage(Toolkit.getDefaultToolkit().getImage(TSortie_frame.class.getResource("/Tr\u00E9sorerie_img/trend.png")));
		frame.setBounds(100, 100, 1100, 550);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		frame.setUndecorated(true);
		
		frame.setUndecorated(true);	
		frame.setResizable(false);
		frame.setTitle("gestion du Stock");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setShape(new RoundRectangle2D.Double(0d, 0d, 1100d, 550d, 25d, 25d));
		frame.setLocationRelativeTo(null);
		//vu que la frame est Undecorated on a besoin de ces MouseListeners pour la faire bouger(frame)
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
		
		textFieldmnt = new JTextField();
		textFieldmnt.setOpaque(false);
		textFieldmnt.addKeyListener(new KeyAdapter() {
		    public void keyTyped(KeyEvent e) { 
		        if (		textFieldmnt.getText().length() >= 20 ) 
		            e.consume(); 
		    }  
		});
		textFieldmnt.setColumns(10);
		textFieldmnt.setForeground(new Color(255, 255, 255));
		textFieldmnt.setFont(new Font("Segoe UI", Font.BOLD, 14));
		textFieldmnt.setBorder(BorderFactory.createEmptyBorder());
		textFieldmnt.setBackground(new Color(0, 153, 0));
		textFieldmnt.setBounds(600, 180, 262, 37);
		frame.getContentPane().add(textFieldmnt);
		textFieldmnt.setBorder(BorderFactory.createEmptyBorder());
		//Date chooser--------------------------------------------------------------------------------------------------------------------------------------------
		JDateChooser dateChooser = new JDateChooser();
		dateChooser.setToolTipText("Date");
		dateChooser.setDateFormatString("yyyy-MM-dd");
		dateChooser.setBorder(null);
		dateChooser.setForeground(new Color(102, 0, 153));
		dateChooser.setBackground(new Color(51, 51, 204));
		dateChooser.setDate(new Date());
		dateChooser.setBounds(156, 178, 258, 39);
		frame.getContentPane().add(dateChooser);
		
		
		btnAjtSortie = new JButton("");
		btnAjtSortie.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent arg0) {
				if (btnAjtSortie.isEnabled()) {
					btnAjtSortie.setIcon(new ImageIcon(Sortie_frame.class.getResource("/TSortie_img/plus selected.png")));
				}
			}
			@Override
			public void mouseExited(MouseEvent e) {
				if (btnAjtSortie.isEnabled()) {
					btnAjtSortie.setIcon(new ImageIcon(Sortie_frame.class.getResource("/TSortie_img/plus.png")));
				}
			}
		});
		btnAjtSortie.setIcon(new ImageIcon(TSortie_frame.class.getResource("/TSortie_img/plus.png")));
		btnAjtSortie.setToolTipText("Ajouter une Sortie");
		btnAjtSortie.setFont(new Font("Comic Sans MS", Font.BOLD, 12));
		btnAjtSortie.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ImageIcon icon = new ImageIcon(GestionPerEnBesoin.class.getResource("/messages_img/Questions secretaire.gif"));//Animation "Question"
				 icon.getImage().flush(); // réinitialiser l'animation
				 //message de confirmation
				int ClickedButton	=JOptionPane.showConfirmDialog(null, "Etes vous sur des informations saisies ? voulez vous enregistrer Cette Transaction?", "Confirmation",JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, icon);
				if(ClickedButton==JOptionPane.YES_OPTION)
					{
					String Date = ((JTextField) dateChooser.getDateEditor().getUiComponent()).getText(); //recuperer la Date
					String aneee = Date.substring(0, 4);//annee
					String Mois=Date.substring(5, 7);//Mois
					String jours=Date.substring(8, 10);//Jours
					String Dateinverse =jours+"-"+Mois+"-"+aneee;
					if (textFieldmnt.getText().equals("")) {
						JOptionPane.showMessageDialog(null, " S'il vous plait veuillez Remplire les champs Marqués par '*'  et selectionner un article dela comboox!","Warning", JOptionPane.QUESTION_MESSAGE, new ImageIcon(GestionAnnonces.class.getResource("/messages_img/error.png")));
					}
					else {
						Trésorie_Sortie TS = new Trésorie_Sortie(textFieldmnt.getText(), Dateinverse);
						TS.ajouter();
						UpedateTableTSortie();
						btnAjtSortie.setEnabled(true);
						btnModSortie.setEnabled(false);
					}
					}
			}
		});
		btnAjtSortie.setBounds(56, 331, 32, 32);
		ButtonStyle(btnAjtSortie);
		frame.getContentPane().add(btnAjtSortie);
		
		 btnModSortie = new JButton("");
			btnModSortie.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseEntered(MouseEvent arg0) {
					if (btnModSortie.isEnabled()) {
						btnModSortie.setIcon(new ImageIcon(Sortie_frame.class.getResource("/TSortie_img/crayon selected.png")));
					}
				}
				@Override
				public void mouseExited(MouseEvent e) {
					if (btnModSortie.isEnabled()) {
						btnModSortie.setIcon(new ImageIcon(Sortie_frame.class.getResource("/TSortie_img/crayon.png")));
					}
				}
			});
			btnModSortie.setIcon(new ImageIcon(TSortie_frame.class.getResource("/TSortie_img/crayon.png")));
			btnModSortie.setEnabled(false);
			btnModSortie.setToolTipText("Modifier une Sortie");
			btnModSortie.setFont(new Font("Comic Sans MS", Font.BOLD, 12));
			btnModSortie.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					ImageIcon icon = new ImageIcon(GestionPerEnBesoin.class.getResource("/messages_img/Questions secretaire.gif"));//Animation "Question"
					 icon.getImage().flush(); // réinitialiser l'animation
					 //message de confirmation
					int ClickedButton	=JOptionPane.showConfirmDialog(null, "Voulez Vous vraiment Modifier ?", "Confirmation",JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, icon);
					if(ClickedButton==JOptionPane.YES_OPTION)
						{
						String Date = ((JTextField) dateChooser.getDateEditor().getUiComponent()).getText(); //recuperer la Date
						String aneee = Date.substring(0, 4);//annee
						String Mois=Date.substring(5, 7);//Mois
						String jours=Date.substring(8, 10);//Jours
						String Dateinverse =jours+"-"+Mois+"-"+aneee;
						Trésorie_Sortie TS = new Trésorie_Sortie(textFieldmnt.getText(), Dateinverse);
						TS.modifier(ID_SORTIE);
						UpedateTableTSortie();
						btnModSortie.setEnabled(false);
						textFieldmnt.setEnabled(true);
						btnAjtSortie.setEnabled(true);
						}
				}
			});
			btnModSortie.setBounds(56, 390, 32, 32);
			frame.getContentPane().add(btnModSortie);
			ButtonStyle(btnModSortie);
		
		scrollPane = new JScrollPane();
		scrollPane.setBounds(152, 278, 885, 244);
		frame.getContentPane().add(scrollPane);
		
		tableSortie = new JTable();
		tableSortie.setFont(new Font("Segoe UI", Font.BOLD, 11));
		tableSortie.setForeground(new Color(255, 255, 255));
		tableSortie.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				btnAjtSortie.setEnabled(false);
				btnModSortie.setEnabled(true);
				textFieldmnt.setEnabled(false);
				int ligne=tableSortie.getSelectedRow();

				ID_SORTIE = tableSortie.getValueAt(ligne, 0).toString();
				String sql="Select * from TSORTIE where code='"+ID_SORTIE+"'";
				try {
					prepared=cnx.prepareStatement(sql);
					resultat=prepared.executeQuery();
					if(resultat.next())
					{
						textFieldmnt.setText(resultat.getString("montantdepo"));
						String Date =resultat.getString("Date_depo").toString();
						if (Date!=null)
						{
				    	DateS=Date.substring(0, 10);
				    	DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
				    	Date date;
						date = format.parse(DateS);
				    	dateChooser.setDate(date);
						}
					}
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					JOptionPane.showMessageDialog(null,e);
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					JOptionPane.showMessageDialog(null,e);
				}
			}
		});
		tableSortie.setBackground(new Color(153, 0, 204));
		scrollPane.setViewportView(tableSortie);
		// Vider les champs bouton////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		JButton btnViderFields = new JButton("");
		btnViderFields.setToolTipText("Clear the fields");
		ButtonStyle(btnViderFields);
		btnViderFields.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				btnModSortie.setEnabled(false);
				btnAjtSortie.setEnabled(true);
				textFieldmnt.setText("");
				textFieldmnt.setEnabled(true);
				UpedateTableTSortie();
				dateChooser.setDate(new Date());
			}
		});
		btnViderFields.setIcon(new ImageIcon(GestionPerEnBesoin.class.getResource("/GestionAD_img/trash.png")));
		btnViderFields.setBounds(56, 443, 32, 32);
		frame.getContentPane().add(btnViderFields);
		
		
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
				Minimise_BTN.setBounds(947, 22, 32, 32);
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
				Exit_BTN.setBounds(1024, 22, 32, 32);
				frame.getContentPane().add(Exit_BTN);
		//Boutton home//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
				JButton btnHome = new JButton("");
				btnHome.addMouseListener(new MouseAdapter() {
							@Override
							public void mouseEntered(MouseEvent e) {
								btnHome.setIcon(new ImageIcon(MenuPrincipaleFrame.class.getResource("/TSortie_img/fleche-gauche selected.png")));//changer les couleurs button
							}
							@Override
							public void mouseExited(MouseEvent e) {
								btnHome.setIcon(new ImageIcon(MenuPrincipaleFrame.class.getResource("/TSortie_img/fleche-gauche.png")));//remetre le bouton de base
							}
						});
				btnHome.addActionListener(new ActionListener() {
							public void actionPerformed(ActionEvent e) {
								MenuTrésorier window = new MenuTrésorier(ID_ADEHRENT);// retourner au menu 
										window.frame.setLocationRelativeTo(null);
										window.frame.setVisible(true);
										frame.dispose();
							}
						});
				btnHome.setIcon(new ImageIcon(TSortie_frame.class.getResource("/TSortie_img/fleche-gauche.png")));
				btnHome.setToolTipText("Retourner au Menu");
				btnHome.setBounds(985, 22, 32, 32);
				ButtonStyle(btnHome);
				frame.getContentPane().add(btnHome);		
				
			
		
		lblNewLabel_3 = new JLabel("New label");
		lblNewLabel_3.setIcon(new ImageIcon(TSortie_frame.class.getResource("/TSortie_img/1.png")));
		lblNewLabel_3.setBounds(0, 0, 1201, 550);
		frame.getContentPane().add(lblNewLabel_3);
		UpedateTableTSortie();
		
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

	 public void UpedateTableTSortie()
	 { 	
	 	
	 	try {
	 		String rq = "SELECT * FROM TSORTIE";
			PreparedStatement pp = conn().prepareStatement(rq);
			ResultSet rs = pp.executeQuery();
			tableSortie.setModel(DbUtils.resultSetToTableModel(rs));
	 		TableRowSorter<TableModel> sorter = new TableRowSorter<TableModel>(tableSortie.getModel());
	 		tableSortie.setRowSorter(sorter);
	 		List<RowSorter.SortKey> sortKeys = new ArrayList<>(25);
	 		sortKeys.add(new RowSorter.SortKey(1, SortOrder.ASCENDING));	
	 		sorter.setSortKeys(sortKeys);
	 		//renomer les  colones
	 		tableSortie.getColumnModel().getColumn( 0 ).setHeaderValue("Code Sortie");
	 		tableSortie.getColumnModel().getColumn( 1 ).setHeaderValue("Date Sortie");	
	 		tableSortie.getColumnModel().getColumn( 2 ).setHeaderValue("Montant Retiré");
	 	

	 } catch (SQLException e) {
	 // TODO Auto-generated catch block
	 	JOptionPane.showMessageDialog(null,e);
	 }

}
}
