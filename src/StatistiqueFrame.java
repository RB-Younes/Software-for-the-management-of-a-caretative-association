import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.geom.RoundRectangle2D;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.border.EmptyBorder;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PiePlot3D;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;

import com.formdev.flatlaf.FlatIntelliJLaf;
import com.toedter.calendar.JDateChooser;

////////////////////////////////////////////////////////////////////////////////-----------Fenetre Statistique------------///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

public class StatistiqueFrame extends JFrame {

	
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JPanel panelChart;
	private String ID_ART;
	private String ID_ART2;
	private   String Nom_ARTICLE;
	private   String Nom_ARTICLE2;
	private String SelectD="SPEB";
	private JButton btnSPEB;
	private JButton btnSS;
	private JButton btnST;
	private JButton btnSP;
	
	private int posX = 0;   //Position X de la souris au clic
    private int posY = 0;   //Position Y de la souris au clic
    
	
	Connection cnx=null;
	PreparedStatement prepared = null;
	ResultSet resultat =null; 
	PreparedStatement prepared1 = null;
	ResultSet resultat1 =null; 


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
					StatistiqueFrame frame = new StatistiqueFrame("ReMo1");
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
	public StatistiqueFrame(String ID_ADEHRENT) {
		
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
        JLabel lblNewLabel = new JLabel("New label");
        
		setLocationRelativeTo(null);
		setUndecorated(true);
		setShape(new RoundRectangle2D.Double(0d, 0d, 1200, 650, 25d, 25d));
		setType(Type.POPUP);
		setResizable(false);
		setTitle("Forgot password ?");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(0, 0, 1200, 650);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		//Date chooser1--------------------------------------------------------------------------------------------------------------------------------------------
		JDateChooser dateChooser1 = new JDateChooser();
		dateChooser1.setToolTipText("Date");
		dateChooser1.setDateFormatString("yyyy-MM-dd");
		dateChooser1.setBorder(null);
		dateChooser1.setForeground(new Color(102, 0, 153));
		dateChooser1.setBackground(new Color(102, 153, 255));
		dateChooser1.setDate(new Date());
		dateChooser1.setBounds(155, 203, 258, 39);
		contentPane.add(dateChooser1);
		//Date chooser2--------------------------------------------------------------------------------------------------------------------------------------------
		JDateChooser dateChooser2 = new JDateChooser();
		dateChooser2.setToolTipText("Date");
		dateChooser2.setDateFormatString("yyyy-MM-dd");
		dateChooser2.setBorder(null);
		dateChooser2.setForeground(new Color(102, 0, 153));
		dateChooser2.setBackground(new Color(102, 153, 255));
		dateChooser2.setDate(new Date());
		dateChooser2.setBounds(155, 253, 258, 39);
		contentPane.add(dateChooser2);
		//Combo BOX Wilaya---------------------------------------------------------------------------------------------------------------------------------------------
		JComboBox<String> comboBoxWilaya = new JComboBox<String>();
		comboBoxWilaya.setModel(new DefaultComboBoxModel<String>(new String[] {"S\u00E9lectionner Wilaya", " ADRAR", "CHLEF", "LAGHOUAT", "OUM BOUAGHI5", "BATNA", "BEJAIA", "BISKRA", "BECHAR", "BLIDA", "BOUIRA", "TAMANRASSET", "TEBESSA", "TLEMCEN", "TIARET", "TIZI OUZOU", "ALGER", "DJELFA", "JIJEL", "SETIF", "SAIDA", "SKIKDA", "SIDI BEL ABBES", "ANNABA", "GUELMA", "CONSTANTINE", "MEDEA", "MOSTAGANEM", "M'SILA", "MASCARA", "OUARGLA", "ORAN", "EL BAYDH", "ILLIZI", "BORDJ BOU ARRERIDJ", "BOUMERDES", "EL TAREF", "TINDOUF", "TISSEMSILT", "EL OUED", "KHENCHLA", "SOUK AHRASS", "TIPAZA", "MILA", "A\u00CFN DEFLA", "N\u00C2AMA", "A\u00CFN TEMOUCHENT", "GHARDA\u00CFA", "RELIZANE"}));
		comboBoxWilaya.setForeground(Color.WHITE);
		comboBoxWilaya.setFont(new Font("Segoe UI", Font.BOLD, 12));
		comboBoxWilaya.setBackground(new Color(102, 153, 255));
		comboBoxWilaya.setBounds(155, 310, 258, 39);
		contentPane.add(comboBoxWilaya);
		//Combo BOX Articles---------------------------------------------------------------------------------------------------------------------------------------------
		JComboBox<String> comboBoxArticles = new JComboBox<String>();
		comboBoxArticles.setFont(new Font("Tahoma", Font.BOLD, 13));
		comboBoxArticles.setVisible(false);
		comboBoxArticles.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				if (comboBoxArticles.getSelectedIndex()!=0 ) {//choisir le patient
					String ARTInfo=comboBoxArticles.getSelectedItem().toString();
					 int finIDindice=ARTInfo.indexOf("-");//indice de fin d l'id
					 if (finIDindice != -1) 
					 {
						 ID_ART= ARTInfo.substring(0 , finIDindice); //recuperer l'id du patient
						    Nom_ARTICLE=ARTInfo.substring(finIDindice +1, ARTInfo.length());
					 }
				}
			}
		});
		comboBoxArticles.addItem("Selectionner Article");
		comboBoxArticles.setBackground(new Color(51, 153, 51));
		comboBoxArticles.setForeground(new Color(255, 255, 255));
		comboBoxArticles.setBounds(117, 446, 268, 44);
		contentPane.add(comboBoxArticles);
		INComboboxARTICLES(comboBoxArticles);//actualiser
		
		JComboBox<String> comboBoxArticles2 = new JComboBox<String>();
		comboBoxArticles2.setFont(new Font("Tahoma", Font.BOLD, 13));
		comboBoxArticles2.setVisible(false);
		comboBoxArticles2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				if (comboBoxArticles2.getSelectedIndex()!=0 ) {//choisir le patient
					String ARTInfo=comboBoxArticles2.getSelectedItem().toString();
					 int finIDindice=ARTInfo.indexOf("-");//indice de fin d l'id
					 if (finIDindice != -1) 
					 {
						 ID_ART2= ARTInfo.substring(0 , finIDindice); //recuperer l'id du patient
						    Nom_ARTICLE2=ARTInfo.substring(finIDindice +1, ARTInfo.length());
					 }
				}
			}
		});
		comboBoxArticles2.addItem("Selectionner Article");
		comboBoxArticles2.setBackground(new Color(51, 153, 51));
		comboBoxArticles2.setForeground(new Color(255, 255, 255));
		comboBoxArticles2.setBounds(117, 259, 268, 44);
		contentPane.add(comboBoxArticles2);
		INComboboxARTICLES(comboBoxArticles2);//actualiser
		//annee-----------------------------------------------------------------------------------------------------------------------------------------------------------------		
		JComboBox<Object> CBYear2 = new JComboBox<Object>();
		CBYear2.setFont(new Font("Tahoma", Font.BOLD, 13));
		CBYear2.setVisible(false);
		CBYear2.setModel(new DefaultComboBoxModel<Object>(new String[] {"Anneé"}));		
		for (int i = 2021; i<=2050 ; i++) {
			String Year =""+i+"";
			CBYear2.addItem(Year);		
			}
		CBYear2.setBackground(new Color(255, 102, 102));
		CBYear2.setForeground(new Color(255, 255, 255));
		CBYear2.setBounds(117, 495, 116, 32);
		contentPane.add(CBYear2);
				
		JComboBox<Object> CBYear = new JComboBox<Object>();
		CBYear.setFont(new Font("Tahoma", Font.BOLD, 13));
		CBYear.setVisible(false);
		CBYear.setModel(new DefaultComboBoxModel<Object>(new String[] {"Anneé"}));		
		for (int i = 2021; i<=2050 ; i++) {
		String Year =""+i+"";
			CBYear.addItem(Year);		
				}
		CBYear.setBackground(new Color(255, 102, 102));
		CBYear.setForeground(new Color(255, 255, 255));
		CBYear.setBounds(117, 314, 116, 32);
		contentPane.add(CBYear);
				
//COVID/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////				
		//	Bouton affichage etat Covid ar periode et wilaya/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////	
		JButton btnCOVEtatWilPer = new JButton("           Etats Covid-19");
		btnCOVEtatWilPer.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent arg0) {
				btnCOVEtatWilPer.setIcon(new ImageIcon(StatistiqueFrame.class.getResource("/Statistique_img/cam selected.png")));
			}
			@Override
			public void mouseExited(MouseEvent e) {
				btnCOVEtatWilPer.setIcon(new ImageIcon(StatistiqueFrame.class.getResource("/Statistique_img/cam.png")));
			}
		});
		btnCOVEtatWilPer.setForeground(new Color(102, 153, 255));
		btnCOVEtatWilPer.setIcon(new ImageIcon(StatistiqueFrame.class.getResource("/Statistique_img/cam.png")));
		btnCOVEtatWilPer.setToolTipText("Etats Covid-19");
		btnCOVEtatWilPer.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(comboBoxWilaya.getSelectedIndex()!=0) {
				final DefaultPieDataset Dataset = new DefaultPieDataset();//le dataset
				
				String Date1 = ((JTextField) dateChooser1.getDateEditor().getUiComponent()).getText();
				String aneee1 = Date1.substring(0, 4);//annee
				String Mois1 = Date1.substring(5, 7);//Mois
				String jours1 = Date1.substring(8, 10);//Jours
				String Dateinverse1 = jours1+"-"+Mois1+"-"+aneee1;
				
				String Date2 = ((JTextField) dateChooser2.getDateEditor().getUiComponent()).getText();
				String aneee2 = Date2.substring(0, 4);//annee
				String Mois2 =Date2.substring(5, 7);//Mois
				String jours2 =Date2.substring(8, 10);//Jours
				String Dateinverse2 = jours2+"-"+Mois2+"-"+aneee2;
				
				String sql = "SELECT ETATCOV,COUNT(ETATCOV) FROM PERSOENBESOIN WHERE WILAYA ='"+comboBoxWilaya.getSelectedItem().toString()+"' AND DATEPREMIEREAIDE BETWEEN '"+Dateinverse1+"' AND '"+Dateinverse2+"'GROUP BY ETATCOV";
				try {
					prepared=cnx.prepareStatement(sql);
					resultat=prepared.executeQuery();
					while (resultat.next()) {
						Dataset.setValue(resultat.getString(1)+": "+resultat.getString(2),resultat.getFloat(2));//ajouter comme donnée
					}
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				 JFreeChart chart = ChartFactory.createPieChart3D( 
				         "Ratio Etats Covid-19 wilaya: "+comboBoxWilaya.getSelectedItem().toString() ,  // chart title                   
				         Dataset ,         // data 
				         true ,                          
				         true, 
				         false);
				      final PiePlot3D R = ( PiePlot3D ) chart.getPlot( );             
				      R.setStartAngle( 270 );    
				      R.setForegroundAlpha( 0.60f );             
				      R.setInteriorGap( 0.02 );    
				      R.setLabelGenerator(null);
				      chart.getTitle().setPaint(new Color(51, 204, 153));
				final ChartPanel cPanel = new ChartPanel(chart);
				panelChart.removeAll();//vider le pannel
				panelChart.add(cPanel,BorderLayout.CENTER);//ajouter le graphique
				panelChart.validate();//Valider
				}
				else 						JOptionPane.showMessageDialog(null, " S'il vous plait veuillez Choisir une wilaya!","Warning", JOptionPane.QUESTION_MESSAGE, new ImageIcon(GestionAnnonces.class.getResource("/messages_img/error.png")));

				
				
			}
		});
		btnCOVEtatWilPer.setBounds(155, 371, 258, 41);
		btnCOVEtatWilPer.setFont(new Font("Microsoft PhagsPa", Font.BOLD, 16));
		contentPane.add(btnCOVEtatWilPer);	
		//btn afficher Stock////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		JButton btnPPW = new JButton("Personne Par Wilaya");
		btnPPW.setIcon(new ImageIcon(StatistiqueFrame.class.getResource("/Statistique_img/cam.png")));
		btnPPW.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent arg0) {
				btnPPW.setIcon(new ImageIcon(StatistiqueFrame.class.getResource("/Statistique_img/cam selected.png")));
			}
			@Override
			public void mouseExited(MouseEvent e) {
				btnPPW.setIcon(new ImageIcon(StatistiqueFrame.class.getResource("/Statistique_img/cam.png")));
			}
		});
		btnPPW.setForeground(new Color(102, 153, 255));
		btnPPW.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				final DefaultPieDataset Dataset = new DefaultPieDataset();//le dataset
				String sql = "SELECT WILAYA,COUNT(WILAYA) FROM PERSOENBESOIN GROUP BY WILAYA";
				try {
					prepared=cnx.prepareStatement(sql);
					resultat=prepared.executeQuery();
					while (resultat.next()) {
						Dataset.setValue(resultat.getString(1)+": "+resultat.getString(2),resultat.getFloat(2));//ajouter comme donnée
					}
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				 JFreeChart chart = ChartFactory.createPieChart3D( 
				         "Nombre de Perssones Par Wilaya" ,  // chart title                   
				         Dataset ,         // data 
				         true ,                          
				         true, 
				         false);
				      final PiePlot3D R = ( PiePlot3D ) chart.getPlot( );             
				      R.setStartAngle( 270 );    
				      R.setForegroundAlpha( 0.60f );             
				      R.setInteriorGap( 0.02 );    
				      R.setLabelGenerator(null);
				      chart.getTitle().setPaint(new Color(51, 204, 153));
				final ChartPanel cPanel = new ChartPanel(chart);
				panelChart.removeAll();//vider le pannel
				panelChart.add(cPanel,BorderLayout.CENTER);//ajouter le graphique
				panelChart.validate();//Valider
				
			}
		});
		btnPPW.setFont(new Font("Microsoft PhagsPa", Font.BOLD, 16));
		btnPPW.setBounds(96, 491, 296, 39);
		contentPane.add(btnPPW);
		
//Stock/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		//Entree///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		JButton btnEntréeArticle = new JButton("             Entr\u00E9es Article");
		btnEntréeArticle.setToolTipText("             Entr\u00E9es Article");
		btnEntréeArticle.setIcon(new ImageIcon(StatistiqueFrame.class.getResource("/Statistique_img/plot.png")));
		btnEntréeArticle.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				btnEntréeArticle.setIcon(new ImageIcon(StatistiqueFrame.class.getResource("/Statistique_img/plot selected.png")));
			}
			@Override
			public void mouseExited(MouseEvent e) {
				btnEntréeArticle.setIcon(new ImageIcon(StatistiqueFrame.class.getResource("/Statistique_img/plot.png")));
			}
		});
		btnEntréeArticle.setForeground(new Color(51, 153, 51));
		btnEntréeArticle.setVisible(false);
		btnEntréeArticle.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e1) {
				if (CBYear.getSelectedIndex()!=0 && comboBoxArticles2.getSelectedIndex()!=0) {
					DefaultCategoryDataset barChartData=new DefaultCategoryDataset(); // le dataset
				String sql = "SELECT SUM(QTE_ENTR),EXTRACT(MONTH FROM DATE_ENTREE) FROM ENTREE where CODEA='"+ID_ART2+"' AND EXTRACT(YEAR FROM DATE_ENTREE) = '"+CBYear.getSelectedItem().toString()+"' group by EXTRACT(MONTH FROM DATE_ENTREE)ORDER BY EXTRACT(MONTH FROM DATE_ENTREE) ASC";
				try {
					prepared=cnx.prepareStatement(sql);
					resultat=prepared.executeQuery();
					while (resultat.next()) {
						barChartData.addValue(resultat.getFloat(1),"Quantité",DonnerNomMois(resultat.getInt(2)));//ajouter la donnée
					}
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				 JFreeChart barChart = ChartFactory.createBarChart3D(
				         "Quantités Entrées article '"+ Nom_ARTICLE2 +"'par mois Année:"+CBYear2.getSelectedItem().toString(), //titre           
				         "Mois",    //x         
				         "Unitées",   //y        
				         barChartData,            
				         PlotOrientation.VERTICAL,             
				         false, true, false);
				 CategoryPlot plot = barChart.getCategoryPlot();

					/* Change Bar colors */
					BarRenderer renderer = (BarRenderer) plot.getRenderer();
					renderer.setSeriesPaint(0, new Color(51, 204, 153));
					renderer.setDrawBarOutline(false);
					renderer.setItemMargin(0);

				 barChart.getTitle().setPaint(new Color(51, 204, 153));// changer la couleurs du titre
				 final ChartPanel cPanel = new ChartPanel(barChart);       
				panelChart.removeAll();
				panelChart.add(cPanel,BorderLayout.CENTER);
				panelChart.validate();
				}
				else 						JOptionPane.showMessageDialog(null, " S'il vous plait veuillez Choisir une Année / un Article!","Warning", JOptionPane.QUESTION_MESSAGE, new ImageIcon(GestionAnnonces.class.getResource("/messages_img/error.png")));
			}
		});
		btnEntréeArticle.setBounds(117, 349, 268, 39);
		btnEntréeArticle.setFont(new Font("Microsoft PhagsPa", Font.BOLD, 16));
		contentPane.add(btnEntréeArticle);
		//Sortie///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		JButton btnSortieArticle = new JButton("             Sorties Article");
		btnSortieArticle.setToolTipText("Sorties Article");
		btnSortieArticle.setIcon(new ImageIcon(StatistiqueFrame.class.getResource("/Statistique_img/plot.png")));
		btnSortieArticle.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				btnSortieArticle.setIcon(new ImageIcon(StatistiqueFrame.class.getResource("/Statistique_img/plot selected.png")));
			}
			@Override
			public void mouseExited(MouseEvent e) {
				btnSortieArticle.setIcon(new ImageIcon(StatistiqueFrame.class.getResource("/Statistique_img/plot.png")));
			}
		});
		btnSortieArticle.setForeground(new Color(51, 153, 51));
		btnSortieArticle.setVisible(false);
		btnSortieArticle.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e1) {
				if (CBYear2.getSelectedIndex()!=0 && comboBoxArticles.getSelectedIndex()!=0) {
						DefaultCategoryDataset barChartData=new DefaultCategoryDataset(); // le dataset
				String sql = "SELECT SUM(QTE_SORT),EXTRACT(MONTH FROM DATEE_SORT) FROM SORTIE where CODEA='"+ID_ART+"' AND EXTRACT(YEAR FROM DATEE_SORT) = '"+CBYear2.getSelectedItem().toString()+"' group by EXTRACT(MONTH FROM DATEE_SORT)ORDER BY EXTRACT(MONTH FROM DATEE_SORT) ASC";
				try {
					prepared=cnx.prepareStatement(sql);
					resultat=prepared.executeQuery();
					while (resultat.next()) {
						barChartData.addValue(resultat.getFloat(1),"Quantite",DonnerNomMois(resultat.getInt(2)));//ajouter la donnée
					}
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				 JFreeChart barChart = ChartFactory.createBarChart3D(
				         "Quantités Sorties article '"+ Nom_ARTICLE +"' par mois Année:"+CBYear2.getSelectedItem().toString(), //titre           
				         "Mois",    //x         
				         "Unitées",   //y        
				         barChartData,            
				         PlotOrientation.VERTICAL,             
				         false, true, false);
				 CategoryPlot plot = barChart.getCategoryPlot();

					/* Change Bar colors */
					BarRenderer renderer = (BarRenderer) plot.getRenderer();
					renderer.setSeriesPaint(0, new Color(51, 204, 153));
					renderer.setDrawBarOutline(false);
					renderer.setItemMargin(0);

				 barChart.getTitle().setPaint(new Color(51, 204, 153));// changer la couleurs du titre
				 final ChartPanel cPanel = new ChartPanel(barChart);       
				panelChart.removeAll();
				panelChart.add(cPanel,BorderLayout.CENTER);
				panelChart.validate();
				}
				else 						JOptionPane.showMessageDialog(null, " S'il vous plait veuillez Choisir une Année / un Article!","Warning", JOptionPane.QUESTION_MESSAGE, new ImageIcon(GestionAnnonces.class.getResource("/messages_img/error.png")));

			
			}
		});
		btnSortieArticle.setBounds(117, 533, 258, 39);
		btnSortieArticle.setFont(new Font("Microsoft PhagsPa", Font.BOLD, 16));
		contentPane.add(btnSortieArticle);
		//btn afficher Stock////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
				JButton btnArticleRatio = new JButton("    Quantit\u00E9 de chaque Article");
				btnArticleRatio.setIcon(new ImageIcon(StatistiqueFrame.class.getResource("/Statistique_img/cam.png")));
				btnArticleRatio.addMouseListener(new MouseAdapter() {
					@Override
					public void mouseEntered(MouseEvent arg0) {
						btnArticleRatio.setIcon(new ImageIcon(StatistiqueFrame.class.getResource("/Statistique_img/cam selected.png")));
					}
					@Override
					public void mouseExited(MouseEvent e) {
						btnArticleRatio.setIcon(new ImageIcon(StatistiqueFrame.class.getResource("/Statistique_img/cam.png")));
					}
				});
				btnArticleRatio.setForeground(new Color(51, 153, 51));
				btnArticleRatio.setVisible(false);
				btnArticleRatio.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent arg0) {
						final DefaultPieDataset Dataset = new DefaultPieDataset();//le dataset
						String sql = "SELECT LIBELLE,QTE_STCK FROM ARTICLE ";
						try {
							prepared=cnx.prepareStatement(sql);
							resultat=prepared.executeQuery();
							while (resultat.next()) {
								Dataset.setValue(resultat.getString(1)+": "+resultat.getString(2),resultat.getFloat(2));//ajouter comme donnée
							}
						} catch (SQLException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						 JFreeChart chart = ChartFactory.createPieChart3D( 
						         "Ratio Articles" ,  // chart title                   
						         Dataset ,         // data 
						         true ,                          
						         true, 
						         false);
						      final PiePlot3D R = ( PiePlot3D ) chart.getPlot( );             
						      R.setStartAngle( 270 );    
						      R.setForegroundAlpha( 0.60f );             
						      R.setInteriorGap( 0.02 );    
						      R.setLabelGenerator(null);
						      chart.getTitle().setPaint(new Color(51, 204, 153));
						final ChartPanel cPanel = new ChartPanel(chart);
						panelChart.removeAll();//vider le pannel
						panelChart.add(cPanel,BorderLayout.CENTER);//ajouter le graphique
						panelChart.validate();//Valider
						
					}
				});
				btnArticleRatio.setFont(new Font("Microsoft PhagsPa", Font.BOLD, 16));
				btnArticleRatio.setBounds(85, 166, 296, 39);
				contentPane.add(btnArticleRatio);
//Tresorerie/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		//Tresorerie Entree//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		JButton btnEntréeTrésorerie = new JButton("             Entr\u00E9es Tr\u00E9sorie");
		btnEntréeTrésorerie.setToolTipText("             Entr\u00E9es Tr\u00E9sorie");
		btnEntréeTrésorerie.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				btnEntréeTrésorerie.setIcon(new ImageIcon(StatistiqueFrame.class.getResource("/Statistique_img/plot selected.png")));
			}
			@Override
			public void mouseExited(MouseEvent e) {
				btnEntréeTrésorerie.setIcon(new ImageIcon(StatistiqueFrame.class.getResource("/Statistique_img/plot.png")));
			}
		});
		btnEntréeTrésorerie.setIcon(new ImageIcon(StatistiqueFrame.class.getResource("/Statistique_img/plot.png")));
		btnEntréeTrésorerie.setForeground(new Color(255, 102, 102));
		btnEntréeTrésorerie.setVisible(false);
		btnEntréeTrésorerie.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e1) {
				if (CBYear.getSelectedIndex()!=0) {
				DefaultCategoryDataset barChartData=new DefaultCategoryDataset(); // le dataset
				String sql = "SELECT SUM(MONTANTDEPO),EXTRACT(MONTH FROM DATE_DEPO) FROM TENTREE where EXTRACT(YEAR FROM DATE_DEPO) = '"+CBYear.getSelectedItem().toString()+"' group by EXTRACT(MONTH FROM DATE_DEPO)ORDER BY EXTRACT(MONTH FROM DATE_DEPO) ASC";
				try {
					prepared=cnx.prepareStatement(sql);
					resultat=prepared.executeQuery();
					while (resultat.next()) {
						barChartData.addValue(resultat.getFloat(1),"Montant",DonnerNomMois(resultat.getInt(2)));//ajouter la donnée
					}
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				 JFreeChart barChart = ChartFactory.createBarChart3D(
				         "Montants Entrée par Mois  "+" Année:"+CBYear2.getSelectedItem().toString(), //titre           
				         "Mois",    //x         
				         "DA",   //y        
				         barChartData,            
				         PlotOrientation.VERTICAL,             
				         false, true, false);
				 CategoryPlot plot = barChart.getCategoryPlot();

					/* Change Bar colors */
					BarRenderer renderer = (BarRenderer) plot.getRenderer();
					renderer.setSeriesPaint(0, new Color(51, 204, 153));
					renderer.setDrawBarOutline(false);
					renderer.setItemMargin(0);

				 barChart.getTitle().setPaint(new Color(51, 204, 153));// changer la couleurs du titre
				 final ChartPanel cPanel = new ChartPanel(barChart);       
				panelChart.removeAll();
				panelChart.add(cPanel,BorderLayout.CENTER);
				panelChart.validate();
				}
				else 						JOptionPane.showMessageDialog(null, " S'il vous plait veuillez Choisir une année!","Warning", JOptionPane.QUESTION_MESSAGE, new ImageIcon(GestionAnnonces.class.getResource("/messages_img/error.png")));
				
			}
		});
		btnEntréeTrésorerie.setBounds(117, 291, 258, 39);
		btnEntréeTrésorerie.setFont(new Font("Microsoft PhagsPa", Font.BOLD, 16));
		contentPane.add(btnEntréeTrésorerie);
		//Tresorerie Sortie//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		JButton btnSortieTresorerie = new JButton("             Sorties Tr\u00E9sorie");
		btnSortieTresorerie.setToolTipText("Sorties Tr\u00E9sorie");
		btnSortieTresorerie.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				btnSortieTresorerie.setIcon(new ImageIcon(StatistiqueFrame.class.getResource("/Statistique_img/plot selected.png")));
			}
			@Override
			public void mouseExited(MouseEvent e) {
				btnSortieTresorerie.setIcon(new ImageIcon(StatistiqueFrame.class.getResource("/Statistique_img/plot.png")));
			}
		});
		btnSortieTresorerie.setIcon(new ImageIcon(StatistiqueFrame.class.getResource("/Statistique_img/plot.png")));
		btnSortieTresorerie.setForeground(new Color(255, 102, 102));
		btnSortieTresorerie.setVisible(false);
		btnSortieTresorerie.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e1) {
				if (CBYear2.getSelectedIndex()!=0) {
				DefaultCategoryDataset barChartData=new DefaultCategoryDataset(); // le dataset
				String sql = "SELECT SUM(MONTANTDEPO),EXTRACT(MONTH FROM DATE_DEPO) FROM TSORTIE where EXTRACT(YEAR FROM DATE_DEPO) = '"+CBYear2.getSelectedItem().toString()+"' group by EXTRACT(MONTH FROM DATE_DEPO)ORDER BY EXTRACT(MONTH FROM DATE_DEPO) ASC";
				try {
					prepared=cnx.prepareStatement(sql);
					resultat=prepared.executeQuery();
					while (resultat.next()) {
						barChartData.addValue(resultat.getFloat(1),"Montant",DonnerNomMois(resultat.getInt(2)));//ajouter la donnée
					}
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				 JFreeChart barChart = ChartFactory.createBarChart3D(
				         "Montants Sortie par Mois "+" Année:"+CBYear2.getSelectedItem().toString(), //titre           
				         "Mois",    //x         
				         "DA",   //y        
				         barChartData,            
				         PlotOrientation.VERTICAL,             
				         false, true, false);
				 CategoryPlot plot = barChart.getCategoryPlot();

					/* Change Bar colors */
					BarRenderer renderer = (BarRenderer) plot.getRenderer();
					renderer.setSeriesPaint(0, new Color(51, 204, 153));
					renderer.setDrawBarOutline(false);
					renderer.setItemMargin(0);

				 barChart.getTitle().setPaint(new Color(51, 204, 153));// changer la couleurs du titre
				 final ChartPanel cPanel = new ChartPanel(barChart);       
				panelChart.removeAll();
				panelChart.add(cPanel,BorderLayout.CENTER);
				panelChart.validate();
				}
				else 						JOptionPane.showMessageDialog(null, " S'il vous plait veuillez Choisir une année!","Warning", JOptionPane.QUESTION_MESSAGE, new ImageIcon(GestionAnnonces.class.getResource("/messages_img/error.png")));

			}
		});
		btnSortieTresorerie.setBounds(117, 489, 258, 44);
		btnSortieTresorerie.setFont(new Font("Microsoft PhagsPa", Font.BOLD, 16));
		contentPane.add(btnSortieTresorerie);
//Psy/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		JButton btnRatioEtat = new JButton("             Etats psychologiqe");
		btnRatioEtat.setVisible(false);
		btnRatioEtat.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent arg0) {
				btnRatioEtat.setIcon(new ImageIcon(StatistiqueFrame.class.getResource("/Statistique_img/cam selected.png")));
			}
			@Override
			public void mouseExited(MouseEvent e) {
				btnRatioEtat.setIcon(new ImageIcon(StatistiqueFrame.class.getResource("/Statistique_img/cam.png")));
			}
		});
		btnRatioEtat.setIcon(new ImageIcon(StatistiqueFrame.class.getResource("/Statistique_img/cam.png")));
		btnRatioEtat.setForeground(Color.ORANGE);
		btnRatioEtat.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				boolean Dep=false;
				boolean Anx=false;
				boolean Emo=false;
				boolean Trou=false;
				boolean Eled=false;
				boolean Elep=false;
				boolean Rel=false;
				
				int NDep=0;
				int NAnx=0;
				int NEmo=0;
				int NTrou=0;
				int NEled=0;
				int NElep=0;
				int NRel=0;
				
				String ID=null;
				String Date1 = ((JTextField) dateChooser1.getDateEditor().getUiComponent()).getText();
				String aneee1 = Date1.substring(0, 4);//annee
				String Mois1 = Date1.substring(5, 7);//Mois
				String jours1 = Date1.substring(8, 10);//Jours
				String Dateinverse1 = jours1+"-"+Mois1+"-"+aneee1;
				
				String Date2 = ((JTextField) dateChooser2.getDateEditor().getUiComponent()).getText();
				String aneee2 = Date2.substring(0, 4);//annee
				String Mois2 =Date2.substring(5, 7);//Mois
				String jours2 =Date2.substring(8, 10);//Jours
				String Dateinverse2 = jours2+"-"+Mois2+"-"+aneee2;
				String sql= "SELECT DISTINCT ID_PEB  FROM PsyConsult WHERE DateCons BETWEEN '"+Dateinverse1+"' AND '"+Dateinverse2+"'";
				try {
					prepared=cnx.prepareStatement(sql);
					resultat=prepared.executeQuery();
					while (resultat.next()) {
						
						ID=resultat.getString("ID_PEB").toString();//recuperer l'ID
						String sql2 = "SELECT Etat FROM PsyConsult WHERE ID_PEB='" +ID+"'and DateCons BETWEEN '"+Dateinverse1+"' AND '"+Dateinverse2+"'";
							prepared1=cnx.prepareStatement(sql2);
							resultat1=prepared1.executeQuery();
							while (resultat1.next()) {
								 String Etat =resultat1.getString("Etat");
								 String EtatFinal[] = Etat.split("/"); 
									for(String s:EtatFinal) 
									{
										switch(s) {
										case "Dépression":
											Dep=true;
											break;
										case "Anxiété - Stress":
											Anx=true;
											break;
										case "Emotionnelles":
											Emo=true;
											break;
										case "Troubles cognitifs":
											Trou=true;
											break;
										case "Eléments délirants":
											Eled=true;
											break;
										case "Eléments psycho traumatiques identifiés":
											Elep=true;
											break;
										case "Relations sociales et familiales":
											Rel=true;
											break;
										}
									}
							}
							if (Dep==true) {NDep++;Dep=false;}
							if (Anx==true) {NAnx++;Anx=false;}
							if (Emo==true) {NEmo++;Emo=false;}
							if (Trou==true) {NTrou++;Trou=false;}
							if (Eled==true) {NEled++;Eled=false;}
							if (Elep==true) {NElep++;Elep=false;}
							if (Rel==true) {NRel++;Rel=false;}
							
					}
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
				final DefaultPieDataset Dataset = new DefaultPieDataset();//le dataset

				Dataset.setValue("Dépression ("+NDep+")",NDep); 
				Dataset.setValue("Anxiété - Stress ("+NAnx+")", NAnx);
				Dataset.setValue("Emotionnelles ("+NEmo+")", NEmo);
				Dataset.setValue("Troubles cognitifs ("+NTrou+")", NTrou);
				Dataset.setValue("Eléments délirants ("+NEled+")", NEled);
				Dataset.setValue("Eléments psycho traumatiques identifiés ("+NElep+")", NElep);
				Dataset.setValue("Relations sociales et familiales ("+NRel+")", NRel);
				
				 JFreeChart chart = ChartFactory.createPieChart3D( 
				         "Ratio Etats Psychologique" ,  // chart title                   
				         Dataset ,         // data 
				         true ,                          
				         true, 
				         false);
				      final PiePlot3D R = ( PiePlot3D ) chart.getPlot( );             
				      R.setStartAngle( 270 );    
				      R.setForegroundAlpha( 0.60f );             
				      R.setInteriorGap( 0.02 );    
				      R.setLabelGenerator(null);
				      chart.getTitle().setPaint(new Color(51, 204, 153));
				final ChartPanel cPanel = new ChartPanel(chart);
				panelChart.removeAll();//vider le pannel
				panelChart.add(cPanel,BorderLayout.CENTER);//ajouter le graphique
				panelChart.validate();//Valider
				
				
				
			}
		});
		btnRatioEtat.setBounds(85, 303, 296, 39);
		btnRatioEtat.setFont(new Font("Microsoft PhagsPa", Font.BOLD, 16));
		contentPane.add(btnRatioEtat);
		
		JButton btnNBR = new JButton("       Nombre de Consultations");
		btnNBR.setVisible(false);
		btnNBR.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				btnNBR.setIcon(new ImageIcon(StatistiqueFrame.class.getResource("/Statistique_img/plot selected.png")));
			}
			@Override
			public void mouseExited(MouseEvent e) {
				btnNBR.setIcon(new ImageIcon(StatistiqueFrame.class.getResource("/Statistique_img/plot.png")));
			}
		});
		btnNBR.setIcon(new ImageIcon(StatistiqueFrame.class.getResource("/Statistique_img/plot.png")));
		btnNBR.setForeground(Color.ORANGE);
		btnNBR.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e1) {
				if (CBYear.getSelectedIndex()!=0) {
					DefaultCategoryDataset barChartData=new DefaultCategoryDataset(); // le dataset
					String sql = "SELECT COUNT( ID_PEB),EXTRACT(MONTH FROM DateCons) FROM PsyConsult where EXTRACT(YEAR FROM DateCons) = '"+CBYear.getSelectedItem().toString()+"' group by EXTRACT(MONTH FROM DateCons)ORDER BY EXTRACT(MONTH FROM DateCons) ASC";
					try {
						prepared=cnx.prepareStatement(sql);
						resultat=prepared.executeQuery();
						while (resultat.next()) {
							barChartData.addValue(resultat.getFloat(1),"Nombre de Consultation",DonnerNomMois(resultat.getInt(2)));//ajouter la donnée
						}
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					 JFreeChart barChart = ChartFactory.createBarChart3D(
					         "Nombre de Consultation par Mois  "+" Année:"+CBYear.getSelectedItem().toString(), //titre           
					         "Mois",    //x         
					         "Nbr",   //y        
					         barChartData,            
					         PlotOrientation.VERTICAL,             
					         false, true, false);
					 CategoryPlot plot = barChart.getCategoryPlot();

						/* Change Bar colors */
						BarRenderer renderer = (BarRenderer) plot.getRenderer();
						renderer.setSeriesPaint(0, new Color(51, 204, 153));
						renderer.setDrawBarOutline(false);
						renderer.setItemMargin(0);

					 barChart.getTitle().setPaint(new Color(51, 204, 153));// changer la couleurs du titre
					 final ChartPanel cPanel = new ChartPanel(barChart);       
					panelChart.removeAll();
					panelChart.add(cPanel,BorderLayout.CENTER);
					panelChart.validate();
					}
					else 						JOptionPane.showMessageDialog(null, " S'il vous plait veuillez Choisir une année!","Warning", JOptionPane.QUESTION_MESSAGE, new ImageIcon(GestionAnnonces.class.getResource("/messages_img/error.png")));
					
			}
		});
		btnNBR.setBounds(85, 491, 296, 39);
		btnNBR.setFont(new Font("Microsoft PhagsPa", Font.BOLD, 16));
		contentPane.add(btnNBR);	
//le panepour les graphique //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////// 		
		panelChart = new JPanel();
		panelChart.setBackground(Color.LIGHT_GRAY);
		panelChart.setBounds(478, 140, 691, 432);
		contentPane.add(panelChart);
		
// Bouton Exit  ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		JButton Exit_BTN = new JButton("");
		Exit_BTN.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent arg0) {
				Exit_BTN.setIcon(new ImageIcon(StatistiqueFrame.class.getResource("/Log_in_img/Exit ML selected.png")));//changer les couleurs button
			}
			@Override
			public void mouseExited(MouseEvent arg0) {
				Exit_BTN.setIcon(new ImageIcon(StatistiqueFrame.class.getResource("/Log_in_img/Exit ML.png")));//remetre le bouton de base
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
		Exit_BTN.setIcon(new ImageIcon(StatistiqueFrame.class.getResource("/Log_in_img/Exit ML.png")));
		Exit_BTN.setBounds(1124, 5, 32, 32);
		contentPane.add(Exit_BTN);
		
// Bouton Reduire ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		JButton Minimise_BTN = new JButton("");
		Minimise_BTN.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				Minimise_BTN.setIcon(new ImageIcon(StatistiqueFrame.class.getResource("/Log_in_img/Minimize ML selected.png")));//changer les couleurs button
			}
			@Override
			public void mouseExited(MouseEvent e) {
				Minimise_BTN.setIcon(new ImageIcon(StatistiqueFrame.class.getResource("/Log_in_img/Minimize ML .png")));//remetre le bouton de base
			}
		});
		Minimise_BTN.setToolTipText("Minimize");
		Minimise_BTN.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setState(ICONIFIED);
				
			}
		});
		Minimise_BTN.setIcon(new ImageIcon(StatistiqueFrame.class.getResource("/Log_in_img/Minimize ML .png")));
		Minimise_BTN.setBounds(1040, 5, 32, 32);
		contentPane.add(Minimise_BTN);
		
//Boutton home//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		
		JButton btnHome = new JButton("");
		btnHome.setIcon(new ImageIcon(StatistiqueFrame.class.getResource("/Log_in_img/home.png")));
		btnHome.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				btnHome.setIcon(new ImageIcon(StatistiqueFrame.class.getResource("/Log_in_img/home selected.png")));//changer les couleurs button
			}
			@Override
			public void mouseExited(MouseEvent e) {
				btnHome.setIcon(new ImageIcon(StatistiqueFrame.class.getResource("/Log_in_img/home.png")));//remetre le bouton de base
			}
		});
		btnHome.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				MenuPrincipaleFrame FramePrincipale = new MenuPrincipaleFrame(ID_ADEHRENT);// retourner au menu 
				FramePrincipale.setLocationRelativeTo(null);
				FramePrincipale.setVisible(true);
				dispose();
			}
		});
		btnHome.setToolTipText("Retourner au Menu");
		btnHome.setBounds(1082, 5, 32, 32);
		contentPane.add(btnHome);
//btns/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	btnSPEB= new JButton("Personnes En Besoin");
		btnSPEB.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				if (!SelectD.equals("SPEB")) {
					btnSPEB.setForeground(Color.WHITE);
				}
				
			}
			@Override
			public void mouseExited(MouseEvent e) {
				if (!SelectD.equals("SPEB")) {
					btnSPEB.setForeground(Color.GRAY);
				}
				
			}
		});
		btnSPEB.setToolTipText("Statistiques Personnes En Besoin");
		btnSPEB.setForeground(Color.WHITE);
		btnSPEB.setFont(new Font("Microsoft PhagsPa", Font.BOLD, 16));
		btnSPEB.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				dateChooser1.setVisible(true);
				dateChooser1.setBackground(new Color(102, 153, 255));
				dateChooser2.setVisible(true);
				dateChooser2.setBackground(new Color(102, 153, 255));
				comboBoxWilaya.setVisible(true);
				btnCOVEtatWilPer.setVisible(true);
				btnPPW.setVisible(true);
				//invisible
				comboBoxArticles.setVisible(false);
				CBYear.setVisible(false);
				CBYear2.setVisible(false);
				comboBoxArticles2.setVisible(false);
				btnEntréeArticle.setVisible(false);
				btnSortieArticle.setVisible(false);
				btnArticleRatio.setVisible(false);
				btnSortieTresorerie.setVisible(false);
				btnEntréeTrésorerie.setVisible(false);
				btnNBR.setVisible(false);
				btnRatioEtat.setVisible(false);
				
				btnSPEB.setBounds(15, 602, 193, 38);
				SelectD="SPEB";
				btnST.setForeground(Color.GRAY);
				btnST.setBounds(220, 610, 193, 38);
				btnSS.setForeground(Color.GRAY);
				btnSS.setBounds(423, 610, 193, 38);
				btnSP.setForeground(Color.GRAY);
				btnSP.setBounds(626, 610, 193, 38);
				lblNewLabel.setIcon(new ImageIcon(MenuPrincipaleFrame.class.getResource("/Statistique_img/1.png")));
			}
		});
		btnSPEB.setBounds(15, 602, 200, 38);
		ButtonStyle(btnSPEB);
		contentPane.add(btnSPEB);
		
	btnST = new JButton("Tr\u00E9sorerie");
		btnST.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				if (!SelectD.equals("ST")) {
					btnST.setForeground(Color.WHITE);
				}
			}
			@Override
			public void mouseExited(MouseEvent e) {
				if (!SelectD.equals("ST")) {
					btnST.setForeground(Color.GRAY);
				}
			}
		});
		btnST.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				btnSortieTresorerie.setVisible(true);
				btnEntréeTrésorerie.setVisible(true);
				CBYear.setVisible(true);
				CBYear.setBounds(126, 210, 89, 32);
				CBYear.setBackground(new Color(255, 102, 102));
				CBYear2.setVisible(true);
				CBYear2.setBounds(126, 418, 89, 32);
				CBYear2.setBackground(new Color(255, 102, 102));
				//invisible
				comboBoxArticles.setVisible(false);
				comboBoxArticles2.setVisible(false);
				btnEntréeArticle.setVisible(false);
				btnSortieArticle.setVisible(false);
				btnArticleRatio.setVisible(false);
				dateChooser1.setVisible(false);
				dateChooser2.setVisible(false);
				comboBoxWilaya.setVisible(false);
				btnCOVEtatWilPer.setVisible(false);
				btnPPW.setVisible(false);
				btnNBR.setVisible(false);
				btnRatioEtat.setVisible(false);

				btnST.setBounds(220, 602, 193, 38);
				SelectD="ST";
				btnSP.setForeground(Color.GRAY);
				btnSP.setBounds(626, 610, 193, 38);
				btnSS.setForeground(Color.GRAY);
				btnSS.setBounds(423, 610, 193, 38);
				btnSPEB.setForeground(Color.GRAY);
				btnSPEB.setBounds(15, 610, 193, 38);
				lblNewLabel.setIcon(new ImageIcon(MenuPrincipaleFrame.class.getResource("/Statistique_img/2.png")));
			}
		});
		btnST.setToolTipText("Statistiques Tr\u00E9sorerie");
		btnST.setOpaque(false);
		btnST.setForeground(Color.GRAY);
		btnST.setFont(new Font("Microsoft PhagsPa", Font.BOLD, 16));
		btnST.setFocusPainted(false);
		btnST.setContentAreaFilled(false);
		btnST.setBorderPainted(false);
		btnST.setBounds(220, 610, 193, 38);
		contentPane.add(btnST);
		
	btnSS = new JButton("Stock");
		btnSS.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//invisible
				dateChooser1.setVisible(false);
				dateChooser2.setVisible(false);
				comboBoxWilaya.setVisible(false);
				btnCOVEtatWilPer.setVisible(false);
				btnSortieTresorerie.setVisible(false);
				btnEntréeTrésorerie.setVisible(false);
				btnPPW.setVisible(false);
				btnNBR.setVisible(false);
				btnRatioEtat.setVisible(false);
				//Visible
				comboBoxArticles.setVisible(true);
				CBYear.setVisible(true);
				CBYear.setBounds(117, 314, 89, 32);
				CBYear.setBackground(new Color(51, 153, 51));
				CBYear2.setVisible(true);
				CBYear2.setBounds(117, 495, 89, 32);
				CBYear2.setBackground(new Color(51, 153, 51));
				comboBoxArticles2.setVisible(true);
				btnEntréeArticle.setVisible(true);
				btnSortieArticle.setVisible(true);
				btnArticleRatio.setVisible(true);
				
				
				btnSS.setBounds(423, 602, 193, 38);
				SelectD="SS";
				btnST.setForeground(Color.GRAY);
				btnST.setBounds(220, 610, 193, 38);
				btnSP.setForeground(Color.GRAY);
				btnSP.setBounds(626, 610, 193, 38);
				btnSPEB.setForeground(Color.GRAY);
				btnSPEB.setBounds(15, 610, 193, 38);
				lblNewLabel.setIcon(new ImageIcon(MenuPrincipaleFrame.class.getResource("/Statistique_img/3.png")));
			}
		});
		btnSS.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				if (!SelectD.equals("SS")) {
					btnSS.setForeground(Color.WHITE);
				}
			}
			@Override
			public void mouseExited(MouseEvent e) {
				if (!SelectD.equals("SS")) {
					btnSS.setForeground(Color.GRAY);
				}
			}
		});
		btnSS.setToolTipText("Statistique Stock");
		btnSS.setOpaque(false);
		btnSS.setForeground(Color.GRAY);
		btnSS.setFont(new Font("Microsoft PhagsPa", Font.BOLD, 16));
		btnSS.setFocusPainted(false);
		btnSS.setContentAreaFilled(false);
		btnSS.setBorderPainted(false);
		btnSS.setBounds(423, 610, 193, 38);
		contentPane.add(btnSS);
		
	 btnSP = new JButton("Psychologue");
		btnSP.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				CBYear.setBounds(117, 422, 116, 32);
				CBYear.setVisible(true);
				CBYear.setBackground(Color.ORANGE);
				dateChooser1.setVisible(true);
				dateChooser1.setBackground(Color.ORANGE);
				dateChooser2.setVisible(true);
				dateChooser2.setBackground(Color.ORANGE);
				btnNBR.setVisible(true);
				btnRatioEtat.setVisible(true);
				
				//invisible
				comboBoxArticles.setVisible(false);
				comboBoxArticles2.setVisible(false);
				btnEntréeArticle.setVisible(false);
				btnSortieArticle.setVisible(false);
				btnArticleRatio.setVisible(false);
				comboBoxWilaya.setVisible(false);
				btnCOVEtatWilPer.setVisible(false);
				btnSortieTresorerie.setVisible(false);
				btnEntréeTrésorerie.setVisible(false);
				
				CBYear2.setVisible(false);
				btnPPW.setVisible(false);
				btnSP.setBounds(626, 602, 193, 38);
				SelectD="SP";
				btnST.setForeground(Color.GRAY);
				btnST.setBounds(220, 610, 193, 38);
				btnSS.setForeground(Color.GRAY);
				btnSS.setBounds(423, 610, 193, 38);
				btnSPEB.setForeground(Color.GRAY);
				btnSPEB.setBounds(15, 610, 193, 38);
				lblNewLabel.setIcon(new ImageIcon(MenuPrincipaleFrame.class.getResource("/Statistique_img/4.png")));
			}
		});
		btnSP.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				if (!SelectD.equals("SP")) {
					btnSP.setForeground(Color.WHITE);
				}
			}
			@Override
			public void mouseExited(MouseEvent e) {
				if (!SelectD.equals("SP")) {
					btnSP.setForeground(Color.GRAY);
				}
			}
		});
		btnSP.setToolTipText("Statistiques Psychologue");
		btnSP.setOpaque(false);
		btnSP.setForeground(Color.GRAY);
		btnSP.setFont(new Font("Microsoft PhagsPa", Font.BOLD, 16));
		btnSP.setFocusPainted(false);
		btnSP.setContentAreaFilled(false);
		btnSP.setBorderPainted(false);
		btnSP.setBounds(626, 610, 193, 38);
		contentPane.add(btnSP);
		
		
		
//le background////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		
		lblNewLabel.setIcon(new ImageIcon(StatistiqueFrame.class.getResource("/Statistique_img/1.png")));
		lblNewLabel.setBounds(0, 0, 1200, 650);
		contentPane.add(lblNewLabel);
	}
	
	 private void ButtonStyle(JButton btn) {
		 btn.setOpaque(false);
		 btn.setFocusPainted(false);
		 btn.setBorderPainted(false);
		 btn.setContentAreaFilled(false);
}
//on donne l annee et elle va renvoyer le  derniers jours (numero) de ce mois //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		public String DonnerJour(String Year,String Month)
		{
			String value = null;
			switch(Month) 
			{
			case "01"  :
			case "03":
			case "05":
			case "07":
			case "08":
			case "10":
			case "12":
				value="31";
				break;
				
			case "02":
				if(  (Integer.parseInt(Year) % 400 )==0) 
				{
					 value="28";
				}
				else {
					value="29";
				}
				
				
				break;
				
			
			case "04":
			case "06":
			case "09":
			case "11":
				
				value="30";	
				
				break;
				
			
		case "1"  :
		case "3":
		case "5":
		case "7":
		case "8":
			value="31";
			break;
			
		case "2":
			if(  (Integer.parseInt(Year) % 400 )==0) 
			{
				 value="29";
			}
			else {
				value="28";
			}
			
			
			break;
			
		
		case "4":
		case "6":
		case "9":
			value="30";	
			
			break;
			
		}
				
			return value;
		}
//on donne un  mois et elle va renvoyé le nom  du mois ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		public String DonnerNomMois(int Month)
		{
			String value = null;
			switch(Month) {
		case 1:
			value="Janv";
			break;
		case 3:
			value="Mars";
			break;
		case 5:
			value="Mai";
			break;
		case 7:
			value="Juill";
			break;
		case 8:
			value="Août";
			break;
		case 2:
			value="Févr";
			break;
		case 4:
			value="Avr";
			break;
		case 6:
			value="Juin";
			break;
		case 9:
			value="Sept";
			break;
		case 10:
			value="Octs";
			break;
		case 11:
			value="Nov";
			break;
		case 12:
			value="Déc";
			break;
		}
			return value;
		}
//Dans la comboBox/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		 public void INComboboxARTICLES(JComboBox<String> AR)
		 { 
		 	String sql ="Select CODEA , LIBELLE from ARTICLE "; 
		 	try {
		 		prepared = cnx.prepareStatement(sql);
		 		resultat =prepared.executeQuery();
		 		while(resultat.next())
		 		{
		 				 String item=resultat.getString("CODEA").toString()+"-"+resultat.getString("LIBELLE").toString();
		 				AR.addItem(item);
		 		}
		 	} catch (SQLException e) {
		 		// TODO Auto-generated catch block
		 		JOptionPane.showMessageDialog(null,e);
		 	}
		 	
		 }
}
