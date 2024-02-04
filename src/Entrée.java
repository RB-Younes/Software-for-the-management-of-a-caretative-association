import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

public class Entrée {
private String codeA,date,codE;
private String qte;




public Entrée(String codeA, String date, String qte) {
	super();
	this.codeA = codeA;
	this.date = date;
	this.codE = null;
	this.qte = qte;
}
public void ajouter() {
	//création d'un séquenceur
	int Clickedbutton=	JOptionPane.showConfirmDialog(null, "Etes vous sur des information saisie ? voulez vous enregistrer Cette Entrée?","Confirmation",JOptionPane.YES_NO_OPTION);
	if(Clickedbutton==JOptionPane.YES_OPTION) {
	try {
		String rq1="SELECT  ENTREE_sq.NEXTVAL S FROM dual";
		 Statement p = conn().createStatement();
	 
	   ResultSet rs1= p.executeQuery(rq1);
	  int val = 1;
	   if(rs1.next()) {
	   	val=rs1.getInt(1);}
		codE=String.valueOf(val);
		@SuppressWarnings("unused")
		Statement st = conn().createStatement();
		String rq = "INSERT INTO ENTREE VALUES (?,?,?,?)";
		PreparedStatement pp = conn().prepareStatement(rq);
		pp.setString(1, codE);
		pp.setString(3, qte);
		pp.setString(2,codeA);
		pp.setString(4, date);
		@SuppressWarnings("unused")
		ResultSet rs = pp.executeQuery();
		String rqq1 = "SELECT * FROM ARTICLE WHERE CODEA='"+codeA+"'";
		PreparedStatement p0 = conn().prepareStatement(rqq1);
		ResultSet res = p0.executeQuery();
		float k1=0;
		while(res.next()) {
		  String q = res.getString(3);
		  float k = Float.valueOf(q);
		   k1 = k+Float.parseFloat(qte);
		  }
		  String qte1 = String.valueOf(k1);
		  String rq2 = "UPDATE ARTICLE SET QTE_STCK='"+qte1+"' WHERE CODEA='"+codeA+"'";
			PreparedStatement p1 = conn().prepareStatement(rq2);
			@SuppressWarnings("unused")
			ResultSet rs2= p1.executeQuery();
		
			JOptionPane.showMessageDialog(null, "Entrée ajoutée avc succés!","Ajout" ,JOptionPane.PLAIN_MESSAGE, new ImageIcon(AuthentificationFrame.class.getResource("/messages_img/stamp.png")));
		
	} catch(Exception e) {
		JOptionPane.showMessageDialog(null, e);
	
	}} else {
		JOptionPane.showMessageDialog(null, "Veuillez verifier les informations introduites");
	}
}
public String getCodeA() {
	return codeA;
}
public void setCodeA(String codeA) {
	this.codeA = codeA;
}
public String getDate() {
	return date;
}
public void setDate(String date) {
	this.date = date;
}
public String getCodE() {
	return codE;
}
public void setCodE(String codE) {
	this.codE = codE;
}
public String getQte() {
	return qte;
}
public void setQte(String qte) {
	this.qte = qte;
}
public void modifier(String i) {
	try {
		
		String rq = "UPDATE ENTREE SET DATE_ENTREE='"+date+"' ,CODEA='"+codeA+"'WHERE CODE='"+i+"'";
	    PreparedStatement pp=conn().prepareStatement(rq);
	    pp.execute();
		JOptionPane.showMessageDialog(null, "Entée Modifiée avc succés!","Modification" ,JOptionPane.PLAIN_MESSAGE, new ImageIcon(AuthentificationFrame.class.getResource("/messages_img/stamp.png")));

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
