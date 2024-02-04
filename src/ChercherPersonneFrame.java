import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.SystemColor;
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

import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;

import com.formdev.flatlaf.FlatIntelliJLaf;
////////////////////////////////////////////////////////////////////////////////-----------Fenetre Bilan------------///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
public class ChercherPersonneFrame extends JFrame {

	
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;

	private int posX = 0;   //Position X de la souris au clic
    private int posY = 0;   //Position Y de la souris au clic
	
	Connection cnx=null;
	PreparedStatement prepared = null;
	ResultSet resultat =null; 
	
	private JTextField txtrecherche;
	private JList<String> listPersonne;
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
					ChercherPersonneFrame frame = new ChercherPersonneFrame();
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
	public ChercherPersonneFrame() {
		setIconImage(Toolkit.getDefaultToolkit().getImage(ChercherPersonneFrame.class.getResource("/ChercherPersonne_img/chercher ICO.png")));
		
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
		setTitle("Chercher Personne");
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
				Exit_BTN.setIcon(new ImageIcon(ChercherPersonneFrame.class.getResource("/Log_in_img/Exit ML selected.png")));//changer les couleurs button
			}
			@Override
			public void mouseExited(MouseEvent arg0) {
				Exit_BTN.setIcon(new ImageIcon(ChercherPersonneFrame.class.getResource("/Log_in_img/Exit ML.png")));//remetre le bouton de base
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
		Exit_BTN.setIcon(new ImageIcon(ChercherPersonneFrame.class.getResource("/Log_in_img/Exit ML.png")));
		Exit_BTN.setBounds(746, 11, 32, 32);
		ButtonStyle(Exit_BTN);
		contentPane.add(Exit_BTN);
		
// Fields////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////		
		
		//le text Field pour la recherche*****************************************************************************************************
				txtrecherche = new JTextField();
				//avec le mechanisme de recherche live
				txtrecherche.addKeyListener(new java.awt.event.KeyAdapter() {
		            public void keyReleased(java.awt.event.KeyEvent evt) {
		            	Recherche(evt);//appler la fonction recherche
		            }
		        });
				txtrecherche.addKeyListener(new KeyAdapter() {//limiter le nombre de caractere du textfield
				    public void keyTyped(KeyEvent e) { 
				        if (		txtrecherche.getText().length() >= 100 ) 
				            e.consume(); 
				    }  
				});
				txtrecherche.setBounds(20, 208, 231, 30);
				txtrecherche.setForeground(new Color(34, 139, 34));
				txtrecherche.setFont(new Font("Segoe UI", Font.BOLD, 14));
				txtrecherche.setBackground(Color.WHITE);
				txtrecherche.setBorder(null);
				contentPane.add(txtrecherche);
				txtrecherche.setColumns(10);
		
//list////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////		

		JScrollPane scrollPanePersonne = new JScrollPane();
		scrollPanePersonne.setViewportBorder(new TitledBorder(null, "", TitledBorder.LEADING, TitledBorder.TOP, null, Color.GRAY));
		scrollPanePersonne.setBackground(Color.LIGHT_GRAY);
		scrollPanePersonne.setBounds(303, 106, 408, 241);
		scrollPanePersonne.setBorder(null);
		contentPane.add(scrollPanePersonne);
		
		
		listPersonne = new JList<String>();
		scrollPanePersonne.setViewportView(listPersonne);
		listPersonne.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				txtrecherche.setText(listPersonne.getSelectedValue());
			}
		});
		listPersonne.setOpaque(false);
		listPersonne.setBackground(SystemColor.control);
		listPersonne.setForeground(new Color(34, 139, 34));
		listPersonne.setFont(new Font("Segoe UI", Font.BOLD, 12));
		UpdateListPEB(listPersonne);
		
						
//le background////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		JLabel lblNewLabel = new JLabel("BG");
		lblNewLabel.setIcon(new ImageIcon(ChercherPersonneFrame.class.getResource("/ChercherPersonne_img/Chercher Personne.png")));

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
//methode du style des buttons/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		public void UpdateListPEB(JList<String> List)
		{ 
	
			String sql ="Select Nom ,Prenom  from PersoEnBesoin";
			try {
				prepared = cnx.prepareStatement(sql);
				resultat =prepared.executeQuery();
				ArrayList<String> L=new ArrayList<String>();//une liste qui va contenir les patients
				while(resultat.next())
				{	
					String item=resultat.getString("Nom").toString()+" "+resultat.getString("Prenom").toString();//sous format Nom Prenom
					L.add(item);//ajouter a la liste
				}
				DefaultListModel<String> defaultListModel=new DefaultListModel<String>();// remplissage de la liste
				L.stream().forEach((star) -> {
	            defaultListModel.addElement(star);//ajouter l'element
				});
				List.setModel(defaultListModel);//changer le modele de la liste
				List.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				JOptionPane.showMessageDialog(null,e);
			}
	
		}
//Recherche///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////				
	private void Recherche(java.awt.event.KeyEvent evt) 
		{                                      
			RechercheFiltre(txtrecherche.getText());
		}        

	/*
	Fltrer les donneés */

	private void RechercheFiltre(String searchTerm)
	{
		String sql ="Select Nom ,Prenom  from PersoEnBesoin";
		try {
			prepared = cnx.prepareStatement(sql);
			resultat =prepared.executeQuery();
			ArrayList<String> L=new ArrayList<String>();//declarer une array list qui va contenir les nom+Prenom Des Patients
			while(resultat.next())
			{
				String item=resultat.getString("Nom").toString()+" "+resultat.getString("Prenom").toString();
				
				L.add(item);//ajouter le patient
				
			}
			DefaultListModel<String> defaultListModel=new DefaultListModel<String>();
			DefaultListModel<String> filteredItems=new DefaultListModel<String>();

		    L.stream().forEach((star) -> {//pour chaque element
		        String starName=star.toString().toLowerCase();
		        if (starName.contains(searchTerm.toLowerCase())) {//si la list contient le term recherché
		        	filteredItems.addElement(star);//alors afficher que les String de la lmist le contenat
		        }
		    });
		    defaultListModel=filteredItems;
		    listPersonne.setModel(defaultListModel);//changer le model de la list
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			JOptionPane.showMessageDialog(null,e);
		}
	}

}
