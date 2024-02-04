import java.awt.EventQueue;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.geom.RoundRectangle2D;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.border.EmptyBorder;

import com.formdev.flatlaf.FlatIntelliJLaf;


////////////////////////////////////////////////////////////////////////////////-----------Gestion Utilisateurs------------///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

public class GestionUtiMenu extends JFrame {

	
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JButton btnUExistant;
	
	Connection cnx=null;
	PreparedStatement prepared = null;
	ResultSet resultat =null; 
	
	private int posX = 0;   
    private int posY = 0;
   
   

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
					GestionUtiMenu frame = new GestionUtiMenu("ReMo1");
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
	public GestionUtiMenu(String ID_ADHERENT) {
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
// initialisation///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////	
		setLocationRelativeTo(null);
		setUndecorated(true);
		setShape(new RoundRectangle2D.Double(0d, 0d, 1000d, 500d, 25d, 25d));
		setType(Type.POPUP);
		setResizable(false);
		setIconImage(Toolkit.getDefaultToolkit().getImage(GestionUtiMenu.class.getResource("/GestionUtilisateurs_img/parametres.png")));
		setTitle("Gestion Utilisateurs & Association");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 1000, 500);
		
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblBG = new JLabel("BG");
		JLabel lblanimtion = new JLabel("");
		cnx = ConnexionDB.CnxDB();
// Exit bouton/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		
		JButton Exit_BTN = new JButton("");
		Exit_BTN.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent arg0) {
				Exit_BTN.setIcon(new ImageIcon(GestionUtiMenu.class.getResource("/Log_in_img/Exit ML selected.png")));//changer les couleurs button
			}
			@Override
			public void mouseExited(MouseEvent arg0) {
				Exit_BTN.setIcon(new ImageIcon(GestionUtiMenu.class.getResource("/Log_in_img/Exit ML.png")));//remetre le bouton de base
			}
		});
		Exit_BTN.setToolTipText("Exit");
		ButtonStyle(Exit_BTN);
		Exit_BTN.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
			
		});
		Exit_BTN.setIcon(new ImageIcon(GestionUtiMenu.class.getResource("/Log_in_img/Exit ML.png")));
		Exit_BTN.setBounds(937, 11, 32, 32);
		contentPane.add(Exit_BTN);
//btn Utilisateur Inexistant////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////		
		JButton btnUInexistant = new JButton("");
		btnUInexistant.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				GestionUtilisateursInexistants frame = new GestionUtilisateursInexistants(ID_ADHERENT);
				frame.setVisible(true);
				frame.setLocationRelativeTo(null);
				dispose();
			}
		});
		ButtonStyle(btnUInexistant);
		btnUInexistant.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent arg0) {
				ImageIcon animation =new ImageIcon(MenuPrincipaleFrame.class.getResource("/GestionUtilisateurs_gif/Add User.gif")); //animation Consultation
				animation.getImage().flush(); // réinitialiser l'animation
				lblanimtion.setIcon(animation);
				lblBG.setIcon(new ImageIcon(GestionUtiMenu.class.getResource("/GestionUtilisateurs_img/page1 (3).png")));
			}
			@Override
			public void mouseExited(MouseEvent e) {
				lblanimtion.setIcon(null);
				lblBG.setIcon(new ImageIcon(GestionUtiMenu.class.getResource("/GestionUtilisateurs_img/page1.png")));
			}
		});
		btnUInexistant.setToolTipText("Gestion Utilisateurs Inexistant ");
		ButtonStyle(btnUInexistant);
		btnUInexistant.setBounds(814, 168, 137, 118);
		contentPane.add(btnUInexistant);
//btn utilisateur EXistant///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////		
		btnUExistant = new JButton("");
		btnUExistant.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				GestionUtilisateursExistants frame = new GestionUtilisateursExistants(ID_ADHERENT);
				frame.setVisible(true);
				frame.setLocationRelativeTo(null);
				dispose();
			}
		});
		ButtonStyle(btnUExistant);
		btnUExistant.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				ImageIcon animation =new ImageIcon(MenuPrincipaleFrame.class.getResource("/GestionUtilisateurs_gif/Online Review.gif")); //animation Consultation
				animation.getImage().flush(); // réinitialiser l'animation
				lblanimtion.setIcon(animation);
				lblBG.setIcon(new ImageIcon(GestionUtiMenu.class.getResource("/GestionUtilisateurs_img/page1 (2).png")));
			}
			@Override
			public void mouseExited(MouseEvent e) {
				lblanimtion.setIcon(null);
				lblBG.setIcon(new ImageIcon(GestionUtiMenu.class.getResource("/GestionUtilisateurs_img/page1.png")));
			}
		});
		btnUExistant.setToolTipText("Gestion Utilisateurs Existant");
		ButtonStyle(btnUExistant);
		btnUExistant.setBounds(47, 168, 137, 118);
		contentPane.add(btnUExistant);
//label pour contenr une nimation/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		lblanimtion.setBounds(287, 0, 408, 304);
		contentPane.add(lblanimtion);
		
		JButton btnAsso = new JButton("");
		btnAsso.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				Gestion_Association window = new Gestion_Association("ReMo1");
				window.frmAssociation.setVisible(true);
				window.frmAssociation.setLocationRelativeTo(null);
				window.frmAssociation.setVisible(true);
				dispose();
			}
		});
		btnAsso.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				ImageIcon animation =new ImageIcon(MenuPrincipaleFrame.class.getResource("/GestionUtilisateurs_gif/Humanitarian Help.gif")); //animation Consultation
				animation.getImage().flush(); // réinitialiser l'animation
				lblanimtion.setIcon(animation);
				lblBG.setIcon(new ImageIcon(GestionUtiMenu.class.getResource("/GestionUtilisateurs_img/page1 (4).png")));
			}
			@Override
			public void mouseExited(MouseEvent e) {
				lblanimtion.setIcon(null);
				lblBG.setIcon(new ImageIcon(GestionUtiMenu.class.getResource("/GestionUtilisateurs_img/page1.png")));
			}
		});
		ButtonStyle(btnAsso);
		btnAsso.setToolTipText("Association");
		btnAsso.setBounds(413, 343, 137, 146);
		contentPane.add(btnAsso);
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////		
	
		lblBG.setIcon(new ImageIcon(GestionUtiMenu.class.getResource("/GestionUtilisateurs_img/page1.png")));
		lblBG.setBounds(0, 0, 1000, 500);
		contentPane.add(lblBG);
		
	}
//methode du style des buttons/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	 private void ButtonStyle(JButton btn) {
	//enlever les bordures des btn
	 btn.setOpaque(false);
	 btn.setFocusPainted(false);
	 btn.setBorderPainted(false);
	 btn.setContentAreaFilled(false);
	 
}
}
