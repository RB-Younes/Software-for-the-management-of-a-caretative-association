

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

public class Trésorie_Sortie {
	private String MontantDépo;
	private String dateDépo,code;
	public Trésorie_Sortie(String montantDépo, String dateDépo) {
		super();
		MontantDépo = montantDépo;
		this.dateDépo = dateDépo;
		this.code = null;
	}
	@SuppressWarnings("unused")
	public void ajouter() {
		
		try {
				
			String rq1="SELECT  TSORT_sq.NEXTVAL S FROM dual";
			 Statement p = conn().createStatement();

		  ResultSet rs1= p.executeQuery(rq1);
		 int val = 1;
		  if(rs1.next()) {
		  	val=rs1.getInt(1);}
		  code= String.valueOf(val);
			    
				Statement st = conn().createStatement();
				String rq = "INSERT INTO TSORTIE VALUES (?,?,?)";
				PreparedStatement pp = conn().prepareStatement(rq);
				pp.setString(1,code);
				pp.setString(2, dateDépo);
				pp.setString(3,MontantDépo);
				//recuperer la drniere maj
				String rqq1 = "SELECT * FROM ASSOCIATION A WHERE A.ass=(SELECT MAX(ass) FROM ASSOCIATION)";
				PreparedStatement p0 = conn().prepareStatement(rqq1);
				ResultSet res = p0.executeQuery();
				float k1=0;
				float l=0;
				float k=0;
				String nom="Association Abed&Re",adr="Bordj Bahri,Zeralda";
				String q ="0";
				nom=null;
				adr=null;
				while(res.next()) {
				  q = res.getString(3);
				  nom= res.getString(2);if (nom==null) nom="Association Abed&Re";
				  adr=res.getString(5);if (adr==null) adr="Bordj Bahri,Zeralda";
				   }
				//l est le montant déposé
				   l=Float.parseFloat(MontantDépo);
				   //k est la derniere maj du fond
				  k = Float.valueOf(q);
				if(l>k) {
			    	   JOptionPane.showMessageDialog(null, "Attention!! le montant introduit dépasse le montant qu'on possède","Warning", JOptionPane.QUESTION_MESSAGE, new ImageIcon(GestionAnnonces.class.getResource("/messages_img/error.png")));
			    	    }else {   k1 = k-Float.parseFloat(MontantDépo);
							  String qte1 = String.valueOf(k1);
								 Association a = new  Association(dateDépo,nom,adr,qte1);
								 a.ajouter();
								 ResultSet rs = pp.executeQuery();
									JOptionPane.showMessageDialog(null, "Sortie ajoutée avc succés!","Ajout" ,JOptionPane.PLAIN_MESSAGE, new ImageIcon(AuthentificationFrame.class.getResource("/messages_img/stamp.png")));
			    	    }
				
			} catch(Exception e) {
				JOptionPane.showMessageDialog(null, e);
				e.printStackTrace();
			
			}
		}
			
		
		public String getCode() {
		return code;
	}
		public void modifier(String i) {
		try {
				
				String rq = "UPDATE TSORTIE SET Date_depo='"+dateDépo+"'WHERE CODE='"+i+"'";
			    PreparedStatement pp=conn().prepareStatement(rq);
			    pp.execute();
				JOptionPane.showMessageDialog(null, "Sortie Modifiée avc succés!","Ajout" ,JOptionPane.PLAIN_MESSAGE, new ImageIcon(AuthentificationFrame.class.getResource("/messages_img/stamp.png")));
			}catch(Exception e) {
				JOptionPane.showMessageDialog(null, e.toString());
			}
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


