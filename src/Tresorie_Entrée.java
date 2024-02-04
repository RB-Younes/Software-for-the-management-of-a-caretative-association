import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
public class Tresorie_Entr�e {
private String code,dateD�po,MontantD�po;

public Tresorie_Entr�e(String montantD�po,String date) {
	super();
	MontantD�po = montantD�po;
	this.code = null;
	this.dateD�po = date;
}
public String getCode() {
	return code;
}
@SuppressWarnings("unused")
public void ajouter() {
	
try {
		
	String rq1="SELECT  TENT_sq.NEXTVAL S FROM dual";
	 Statement p = conn().createStatement();

  ResultSet rs1= p.executeQuery(rq1);
 int val = 1;
  if(rs1.next()) {
  	val=rs1.getInt(1);}
  code= String.valueOf(val);
	    
		Statement st = conn().createStatement();
		String rq = "INSERT INTO TENTREE VALUES (?,?,?)";
		PreparedStatement pp = conn().prepareStatement(rq);
		pp.setString(1,code);
		pp.setString(2, dateD�po);
		pp.setString(3,MontantD�po);
		String rqq1 = "SELECT * FROM ASSOCIATION A WHERE A.ass=(SELECT MAX(ass) FROM ASSOCIATION)";
		PreparedStatement p0 = conn().prepareStatement(rqq1);
		ResultSet res = p0.executeQuery();
		float k1=0;
		String nom="Association Abed&Re",adr="Bordj Bahri,Zeralda";
		
		nom=null;
		adr=null;
		while(res.next()) {
		  String q = res.getString(3);
		  nom= res.getString(2);if (nom==null) nom="Association Abed&Re";
		  adr=res.getString(5);if (adr==null) adr="Bordj Bahri,Zeralda";
		  
		  float k = Float.valueOf(q);
		   k1 = k+Float.parseFloat(MontantD�po);
		  }
		  String qte1 = String.valueOf(k1);
		 Association a = new  Association(dateD�po,nom,adr,qte1);
		 a.ajouter();
			JOptionPane.showMessageDialog(null, "Entr�e ajout�e avc succ�s!","Ajout" ,JOptionPane.PLAIN_MESSAGE, new ImageIcon(AuthentificationFrame.class.getResource("/messages_img/stamp.png")));

		
	
		ResultSet rs = pp.executeQuery();
	} catch(Exception e) {
		JOptionPane.showMessageDialog(null, e);
	
	}
	
}
public void modifier(String i) {
try {
		
	String rq = "UPDATE TENTREE SET Date_depo='"+dateD�po+"'WHERE CODE='"+i+"'";
    PreparedStatement pp=conn().prepareStatement(rq);
	    pp.execute();
		JOptionPane.showMessageDialog(null, "Entr�e Modifi�e avc succ�s!","Modification" ,JOptionPane.PLAIN_MESSAGE, new ImageIcon(AuthentificationFrame.class.getResource("/messages_img/stamp.png")));

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


