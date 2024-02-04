import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.JButton;
import java.awt.Font;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.ImageIcon;
import java.awt.Color;
import javax.swing.border.LineBorder;

import com.formdev.flatlaf.FlatIntelliJLaf;

import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.geom.RoundRectangle2D;
import java.awt.event.ActionEvent;
import java.awt.Toolkit;

public class MenuTrésorier {

	JFrame frame;
	private JTextField textField;
	
	private int posX = 0;   //Position X de la souris au clic
    private int posY = 0;   //Position Y de la souris au clic

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) throws UnsupportedLookAndFeelException {
		FlatIntelliJLaf.install();	
		UIManager.setLookAndFeel(new FlatIntelliJLaf() );
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MenuTrésorier window = new MenuTrésorier("ReMo1");
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
	public MenuTrésorier(String ID_ADEHRENT) {
		initialize(ID_ADEHRENT);
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize(String ID_ADEHRENT) {
		frame = new JFrame();
		frame.setIconImage(Toolkit.getDefaultToolkit().getImage(MenuTrésorier.class.getResource("/Tr\u00E9sorerie_img/target.png")));
		frame.getContentPane().setFont(new Font("Tahoma", Font.PLAIN, 18));
		frame.setBounds(100, 100, 1000, 500);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setUndecorated(true);
		frame.getContentPane().setLayout(null);
		
		frame.setUndecorated(true);	
		frame.setResizable(false);
		frame.setTitle("gestion du Stock");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setShape(new RoundRectangle2D.Double(0d, 0d, 1200d, 550d, 25d, 25d));
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
		JLabel lblNewLabel = new JLabel("New label");
		
		textField = new JTextField();
		textField.setEditable(false);
		textField.setFont(new Font("Segoe UI", Font.BOLD, 22));
		textField.setForeground(Color.WHITE);
		textField.setBorder(new LineBorder(new Color(171, 173, 179), 0));
		textField.setBackground(new Color(153, 0, 204));
		textField.setBounds(362, 218, 170, 63);
		frame.getContentPane().add(textField);
		textField.setColumns(10);
		textField.setText(setFond());
		
		JButton btnTEntrée = new JButton("Tr\u00E9sorie Entr\u00E9e");
		btnTEntrée.setIcon(new ImageIcon(MenuTrésorier.class.getResource("/Tr\u00E9sorerie_img/rising.png")));
		btnTEntrée.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent arg0) {
				btnTEntrée.setForeground(Color.WHITE);
				lblNewLabel.setIcon(new ImageIcon(MenuTrésorier.class.getResource("/Trésorerie_img/3.png")));
			}
			@Override
			public void mouseExited(MouseEvent e) {
				btnTEntrée.setForeground(Color.LIGHT_GRAY);
				lblNewLabel.setIcon(new ImageIcon(MenuTrésorier.class.getResource("/Trésorerie_img/1.png")));
			}
		});
		btnTEntrée.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Trésorie_ENTREE_Frame window = new Trésorie_ENTREE_Frame(ID_ADEHRENT);
				window.frame.setVisible(true);
				window.frame.setLocationRelativeTo(null);
				frame.dispose();
				
			}
		});
		ButtonStyle(btnTEntrée);
		btnTEntrée.setForeground(Color.LIGHT_GRAY);
		btnTEntrée.setFont(new Font("Segoe UI", Font.BOLD, 14));
		btnTEntrée.setBounds(52, 358, 212, 45);
		frame.getContentPane().add(btnTEntrée);
		
		JButton btnTSortie = new JButton("Tr\u00E9sorie Sortie");
		btnTSortie.setIcon(new ImageIcon(MenuTrésorier.class.getResource("/Tr\u00E9sorerie_img/trend.png")));
		btnTSortie.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent arg0) {
				btnTSortie.setForeground(Color.WHITE);
				lblNewLabel.setIcon(new ImageIcon(MenuTrésorier.class.getResource("/Trésorerie_img/2.png")));
			}
			@Override
			public void mouseExited(MouseEvent e) {
				btnTSortie.setForeground(Color.LIGHT_GRAY);
				lblNewLabel.setIcon(new ImageIcon(MenuTrésorier.class.getResource("/Trésorerie_img/1.png")));
			}
		});
		btnTSortie.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				TSortie_frame window = new TSortie_frame(ID_ADEHRENT);
				window.frame.setVisible(true);
				window.frame.setLocationRelativeTo(null);
				frame.dispose();
			}
		});
		ButtonStyle(btnTSortie);
		btnTSortie.setForeground(Color.LIGHT_GRAY);
		btnTSortie.setFont(new Font("Segoe UI", Font.BOLD, 14));
		btnTSortie.setBounds(737, 358, 212, 45);
		frame.getContentPane().add(btnTSortie);
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
		Minimise_BTN.setBounds(847, 22, 32, 32);
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
		Exit_BTN.setBounds(931, 22, 32, 32);
		frame.getContentPane().add(Exit_BTN);
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
								frame.dispose();
					}
				});
		btnHome.setIcon(new ImageIcon(GestionPerEnBesoin.class.getResource("/Log_in_img/home.png")));
		btnHome.setToolTipText("Retourner au Menu");
		btnHome.setBounds(889, 22, 32, 32);
		ButtonStyle(btnHome);
		frame.getContentPane().add(btnHome);		
		
		
		
//BG////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////		
		lblNewLabel.setIcon(new ImageIcon(MenuTrésorier.class.getResource("/Tr\u00E9sorerie_img/1.png")));
		lblNewLabel.setBounds(0, 0, 1000, 500);
		frame.getContentPane().add(lblNewLabel);
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
	 private String setFond() {
		 String q =null;
		 try {
			 String rqq1 = "SELECT * FROM ASSOCIATION A WHERE A.ass=(SELECT MAX(ass) FROM ASSOCIATION)";
				PreparedStatement p0 = conn().prepareStatement(rqq1);
				ResultSet res = p0.executeQuery();
				
				
				
				
				while(res.next()) {
				  q = res.getString(3);
				
				   }
				
				
		 } catch(Exception e){JOptionPane.showConfirmDialog(null, e);}
		 return q;
	 }

public Connection conn() {
	try {
		Class.forName("oracle.jdbc.OracleDriver");
		Connection cn = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:Asso","system","123");
				return cn;}
	catch(Exception e) {
		JOptionPane.showMessageDialog(null, e.toString());
	}return null;}
}
