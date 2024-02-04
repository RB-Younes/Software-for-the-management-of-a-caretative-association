import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

import net.proteanit.sql.DbUtils;

import javax.swing.JComboBox;
import java.awt.Color;
import java.awt.Font;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.awt.event.ActionEvent;

public class Statistque_Frame {

	private JFrame frame;
	private JTable table;
	private JTextField txtDate;
	private JTextField txtDate_1;
	private JTextField txtYear;
	private JTextField txtCodea;
	private JButton btnNewButton_2;
	private JButton btnNewButton_3;
	private JButton btnNewButton_4;
	private JButton btnCovid;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Statistque_Frame window = new Statistque_Frame();
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
	public Statistque_Frame() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		JComboBox<String> comboBoxEtatCov = new JComboBox<String>();
		
		frame.setBounds(100, 100, 748, 455);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(322, 42, 378, 226);
		frame.getContentPane().add(scrollPane);
		
		table = new JTable();
		scrollPane.setViewportView(table);
		
		txtDate = new JTextField();
		txtDate.setText("DATE1");
		txtDate.setBounds(35, 41, 96, 19);
		frame.getContentPane().add(txtDate);
		txtDate.setColumns(10);
		
		JComboBox<String> comboBoxWilaya = new JComboBox<String>();
		comboBoxWilaya.setModel(new DefaultComboBoxModel<String>(new String[] {"S\u00E9lectionner Wilaya", " ADRAR", "CHLEF", "LAGHOUAT", "OUM BOUAGHI5", "BATNA", "BEJAIA", "BISKRA", "BECHAR", "BLIDA", "BOUIRA", "TAMANRASSET", "TEBESSA", "TLEMCEN", "TIARET", "TIZI OUZOU", "ALGER", "DJELFA", "JIJEL", "SETIF", "SAIDA", "SKIKDA", "SIDI BEL ABBES", "ANNABA", "GUELMA", "CONSTANTINE", "MEDEA", "MOSTAGANEM", "M'SILA", "MASCARA", "OUARGLA", "ORAN", "EL BAYDH", "ILLIZI", "BORDJ BOU ARRERIDJ", "BOUMERDES", "EL TAREF", "TINDOUF", "TISSEMSILT", "EL OUED", "KHENCHLA", "SOUK AHRASS", "TIPAZA", "MILA", "A\u00CFN DEFLA", "N\u00C2AMA", "A\u00CFN TEMOUCHENT", "GHARDA\u00CFA", "RELIZANE"}));
		comboBoxWilaya.setForeground(Color.WHITE);
		comboBoxWilaya.setFont(new Font("Segoe UI", Font.BOLD, 12));
		comboBoxWilaya.setBackground(new Color(34, 139, 34));
		comboBoxWilaya.setBounds(10, 99, 183, 28);
		frame.getContentPane().add(comboBoxWilaya);
		
		JButton btnNewButton = new JButton("COVID1");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try 
				{
					
					String rq = "SELECT ETATCOV,COUNT(ETATCOV) FROM PERSOENBESOIN WHERE WILAYA ='"+comboBoxWilaya.getSelectedItem().toString()+"'AND ETATCOV='"+comboBoxEtatCov.getSelectedItem().toString()+"' AND DATEPREMIEREAIDE BETWEEN '"+txtDate.getText()+"' AND '"+txtDate_1.getText()+"'GROUP BY ETATCOV";
					
					PreparedStatement pp = conn().prepareStatement(rq);
					ResultSet rs = pp.executeQuery();
					table.setModel(DbUtils.resultSetToTableModel(rs));
					
					
					
				} catch(Exception ee) {
					JOptionPane.showMessageDialog(null, ee);
				}
				
			}
		});
		btnNewButton.setBounds(63, 227, 85, 21);
		frame.getContentPane().add(btnNewButton);
		
		JButton btnNewButton_1 = new JButton("ENTREE");
		btnNewButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try 
				{
					
					String rq = "SELECT SUM(QTE_ENTR),EXTRACT(MONTH FROM DATE_ENTREE) FROM ENTREE where CODEA='"+txtCodea.getText()+"' AND EXTRACT(YEAR FROM DATE_ENTREE) = '"+txtYear.getText()+"' group by EXTRACT(MONTH FROM DATE_ENTREE)ORDER BY EXTRACT(MONTH FROM DATE_ENTREE) ASC";
									PreparedStatement pp = conn().prepareStatement(rq);
					ResultSet rs = pp.executeQuery();
					table.setModel(DbUtils.resultSetToTableModel(rs));

				} catch(Exception ee) {
					JOptionPane.showMessageDialog(null, ee);
				}
			}
		});
		btnNewButton_1.setBounds(63, 293, 85, 21);
		frame.getContentPane().add(btnNewButton_1);
		
		txtDate_1 = new JTextField();
		txtDate_1.setText("DATE2");
		txtDate_1.setColumns(10);
		txtDate_1.setBounds(35, 70, 96, 19);
		frame.getContentPane().add(txtDate_1);
		
		comboBoxEtatCov.setModel(new DefaultComboBoxModel<String>(new String[] {"S\u00E9lectionner \u00E9tat", "jamais atteint du covid", "Atteint du Covid", "Membre de la famille atteint du covid", "D\u00E9ja atteint du covid mais guerrie"}));
		comboBoxEtatCov.setForeground(Color.WHITE);
		comboBoxEtatCov.setFont(new Font("Segoe UI", Font.BOLD, 12));
		comboBoxEtatCov.setBackground(new Color(34, 139, 34));
		comboBoxEtatCov.setBounds(10, 144, 183, 28);
		frame.getContentPane().add(comboBoxEtatCov);
		
		txtYear = new JTextField();
		txtYear.setText("YEAR");
		txtYear.setBounds(255, 339, 96, 19);
		frame.getContentPane().add(txtYear);
		txtYear.setColumns(10);
		
		txtCodea = new JTextField();
		txtCodea.setText("CODEA");
		txtCodea.setColumns(10);
		txtCodea.setBounds(114, 339, 96, 19);
		frame.getContentPane().add(txtCodea);
		
		btnNewButton_2 = new JButton("SORTIE");
		btnNewButton_2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try 
				{
					
					
					String rq = "SELECT SUM(QTE_SORT),EXTRACT(MONTH FROM DATEE_SORT) FROM SORTIE where CODEA='"+txtCodea.getText()+"' AND EXTRACT(YEAR FROM DATEE_SORT) = '"+txtYear.getText()+"' group by EXTRACT(MONTH FROM DATEE_SORT)ORDER BY EXTRACT(MONTH FROM DATEE_SORT) ASC";
									PreparedStatement pp = conn().prepareStatement(rq);
					ResultSet rs = pp.executeQuery();
					table.setModel(DbUtils.resultSetToTableModel(rs));
					
					
					
					
				} catch(Exception ee) {
					JOptionPane.showMessageDialog(null, ee);
				}
				
			}
		});
		btnNewButton_2.setBounds(254, 293, 85, 21);
		frame.getContentPane().add(btnNewButton_2);
		
		btnNewButton_3 = new JButton("TENTREE");
		btnNewButton_3.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try 
				{
					
					String rq = "SELECT SUM(MONTANTDEPO),EXTRACT(MONTH FROM DATE_DEPO) FROM TENTREE where EXTRACT(YEAR FROM DATE_DEPO) = '"+txtYear.getText()+"' group by EXTRACT(MONTH FROM DATE_DEPO)ORDER BY EXTRACT(MONTH FROM DATE_DEPO) ASC";
									PreparedStatement pp = conn().prepareStatement(rq);
					ResultSet rs = pp.executeQuery();
					table.setModel(DbUtils.resultSetToTableModel(rs));
					
					
					
					
				} catch(Exception ee) {
					JOptionPane.showMessageDialog(null, ee);
				}
			}
		});
		btnNewButton_3.setBounds(422, 293, 85, 21);
		frame.getContentPane().add(btnNewButton_3);
		
		btnNewButton_4 = new JButton("TSORTIE");
		btnNewButton_4.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try 
				{
					
					String rq = "SELECT SUM(MONTANTDEPO),EXTRACT(MONTH FROM DATE_DEPO) FROM TSORTIE where EXTRACT(YEAR FROM DATE_DEPO) = '"+txtYear.getText()+"' group by EXTRACT(MONTH FROM DATE_DEPO)ORDER BY EXTRACT(MONTH FROM DATE_DEPO) ASC";
									PreparedStatement pp = conn().prepareStatement(rq);
					ResultSet rs = pp.executeQuery();
					table.setModel(DbUtils.resultSetToTableModel(rs));
					
					
					
					
				} catch(Exception ee) {
					JOptionPane.showMessageDialog(null, ee);
				}
			}
		});
		btnNewButton_4.setBounds(597, 293, 85, 21);
		frame.getContentPane().add(btnNewButton_4);
		
		btnCovid = new JButton("COVID2");
		btnCovid.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try 
				{
					
					String rq = "SELECT LIBELLE,QTE_STCK FROM ARTICLE ";
					
					PreparedStatement pp = conn().prepareStatement(rq);
					ResultSet rs = pp.executeQuery();
					table.setModel(DbUtils.resultSetToTableModel(rs));
					
					
				} catch(Exception ee) {
					JOptionPane.showMessageDialog(null, ee);
				}
				
			}
		});
		btnCovid.setBounds(175, 227, 85, 21);
		frame.getContentPane().add(btnCovid);
		
		JButton btnNewButton_3_1 = new JButton("NBRPERSONNE CONSULTATION");
		btnNewButton_3_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try 
				{
					String rq = "SELECT COUNT( ID_PEB),EXTRACT(MONTH FROM DateCons) FROM PsyConsult where EXTRACT(YEAR FROM DateCons) = '"+txtYear.getText()+"' group by EXTRACT(MONTH FROM DateCons)ORDER BY EXTRACT(MONTH FROM DateCons) ASC";
					
					PreparedStatement pp = conn().prepareStatement(rq);
					ResultSet rs = pp.executeQuery();
					table.setModel(DbUtils.resultSetToTableModel(rs));
					
					
				} catch(Exception ee) {
					JOptionPane.showMessageDialog(null, ee);
				}
			}
		});
		btnNewButton_3_1.setBounds(422, 338, 85, 21);
		frame.getContentPane().add(btnNewButton_3_1);
		
		JButton btnNewButton_3_1_1 = new JButton("PSYETAT");
		btnNewButton_3_1_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try 
				{
					String rq = "SELECT COUNT(DISTINCT ID_PEB),ETAT FROM PsyConsult where EXTRACT(YEAR FROM DateCons) = '"+txtYear.getText()+"' group by EXTRACT(MONTH FROM DateCons)ORDER BY EXTRACT(MONTH FROM DateCons) ASC";
					
					PreparedStatement pp = conn().prepareStatement(rq);
					ResultSet rs = pp.executeQuery();
					table.setModel(DbUtils.resultSetToTableModel(rs));
					
					
				} catch(Exception ee) {
					JOptionPane.showMessageDialog(null, ee);
				}
			}
		});
		btnNewButton_3_1_1.setBounds(493, 367, 207, 21);
		frame.getContentPane().add(btnNewButton_3_1_1);
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