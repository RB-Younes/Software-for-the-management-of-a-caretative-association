import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
public class Sortie {
	private String codeA,date,codS;
	private String qte;
	public Sortie(String codeA, String date, String qte) {
		super();
		this.codeA = codeA;
		this.date = date;
		this.codS = null;
		this.qte = qte;
	
	}


@SuppressWarnings("unused")
public void ajouter() {
	//création d'un séquenceur
	int Clickedbutton=	JOptionPane.showConfirmDialog(null, "Etes vous sur des information saisie ? voulez vous enregistrer Cette Sortie?","Confirmation",JOptionPane.YES_NO_OPTION);
	if(Clickedbutton==JOptionPane.YES_OPTION) {
	try {
		
		
		String rqq1 = "SELECT * FROM ARTICLE WHERE CODEA='"+codeA+"'";
		PreparedStatement p0 = conn().prepareStatement(rqq1);
		ResultSet res = p0.executeQuery();
		float k=0;
		while(res.next()) {
		  String q = res.getString(3);
		  k = Float.valueOf(q);
		  }
		float l = Float.parseFloat(qte);
       if(l>k) {
    	   JOptionPane.showMessageDialog(null, "Attention!! la quantité introduite dépasse la quantité en stock.","Warning", JOptionPane.QUESTION_MESSAGE, new ImageIcon(GestionAnnonces.class.getResource("/messages_img/error.png")));
    	    }else {
    	    	String rq1="SELECT  Sortie_sq.NEXTVAL S FROM dual";
    			 Statement p = conn().createStatement();
    		 
    		   ResultSet rs1= p.executeQuery(rq1);
    		  int val = 1;
    		   if(rs1.next()) {
    		   	val=rs1.getInt(1);
    		   	}
    		   codS=String.valueOf(val);
    		    
    			Statement st = conn().createStatement();
    			String rq = "INSERT INTO SORTIE VALUES (?,?,?,?)";
    			PreparedStatement pp = conn().prepareStatement(rq);
    			pp.setString(1, codS);
    			pp.setString(3, qte);
    			pp.setString(2, codeA);
    			pp.setString(4, date);
    			
    		
    			ResultSet rs = pp.executeQuery();
       float q = k-l;
       String qte = String.valueOf(q);
       String rq2 = "UPDATE ARTICLE SET QTE_STCK='"+qte+"' WHERE CODEA='"+codeA+"'";
		PreparedStatement p1 = conn().prepareStatement(rq2);
		ResultSet rs2= p1.executeQuery();
		
		JOptionPane.showMessageDialog(null, "Sortie ajoutée avc succés!","Ajout" ,JOptionPane.PLAIN_MESSAGE, new ImageIcon(AuthentificationFrame.class.getResource("/messages_img/stamp.png")));
    	    }} catch(Exception e) {
		JOptionPane.showMessageDialog(null, e);
	
	}
	}
}
public void modifier(String i) {
	try {
		String rq = "UPDATE SORTIE SET DATEE_SORT='"+date+"'WHERE CODS='"+i+"'";
	    PreparedStatement pp=conn().prepareStatement(rq);
	    pp.execute();
	    JOptionPane.showMessageDialog(null, "Sortie Modifiée avec succés!","Modification" ,JOptionPane.PLAIN_MESSAGE, new ImageIcon(AuthentificationFrame.class.getResource("/messages_img/stamp.png")));
	}catch(Exception e) {
		JOptionPane.showMessageDialog(null, e.toString());
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


public String getCodS() {
	return codS;
}


public void setCodS(String codS) {
	this.codS = codS;
}


public String getQte() {
	return qte;
}


public void setQte(String qte) {
	this.qte = qte;
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
