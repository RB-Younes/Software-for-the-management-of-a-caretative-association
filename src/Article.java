import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

public class Article {
private String codeA;
private String qte;
private String libelle;
public Article( String qte, String libelle) {
	try {
		codeA=null;
		this.qte = qte;
		this.libelle = libelle;
	} catch(Exception e) {}
	
	
	
}
public void ajouter() {
	//création d'un séquenceur
	try {
		String rq1="SELECT  Article_sq.NEXTVAL S FROM dual";
		 Statement p = conn().createStatement();
	 
	   ResultSet rs1= p.executeQuery(rq1);
	  int val = 1;
	   if(rs1.next()) {
	   	val=rs1.getInt(1);}
	   codeA = String.valueOf(val);
		@SuppressWarnings("unused")
		Statement st = conn().createStatement();
		String rq = "INSERT INTO ARTICLE VALUES (?,?,?)";
		PreparedStatement pp = conn().prepareStatement(rq);
		pp.setString(1, codeA);
		pp.setString(2, libelle);
		pp.setString(3, qte);
		
	
		@SuppressWarnings("unused")
		ResultSet rs = pp.executeQuery();
		JOptionPane.showMessageDialog(null, "Article ajouté avec succés!","Ajout" ,JOptionPane.PLAIN_MESSAGE, new ImageIcon(AuthentificationFrame.class.getResource("/messages_img/stamp.png")));
	} catch(Exception e) {
		JOptionPane.showMessageDialog(null, e);
	
	}
}
public String getCodeA() {
	return codeA;
}
public void setCodeA(String codeA) {
	this.codeA = codeA;
}
public String getQte() {
	return qte;
}
public void setQte(String qte) {
	this.qte = qte;
}
public String getLibelle() {
	return libelle;
}
public void setLibelle(String libelle) {
	this.libelle = libelle;
}
public void modifier(String i) {
	try {
		
		String rq = "UPDATE ARTICLE SET LIBELLE='"+libelle+"' ,QTE_Stck='"+qte+"'WHERE CODEA='"+i+"'";
	    PreparedStatement pp=conn().prepareStatement(rq);
	    pp.execute();
	    JOptionPane.showMessageDialog(null, "Article Modifié avec succés!","Modification" ,JOptionPane.PLAIN_MESSAGE, new ImageIcon(AuthentificationFrame.class.getResource("/messages_img/stamp.png")));
	}catch(Exception e) {
		JOptionPane.showMessageDialog(null, e);
	}
}
public void supprimer(String i) {
	int Clickedbutton=	JOptionPane.showConfirmDialog(null, "voulez vous vraiment supprimer Cet Article? en le supprimant vous risquez de perdre tous les entrées, sorties le concernant","Confirmation",JOptionPane.YES_NO_OPTION);
	if(Clickedbutton==JOptionPane.YES_OPTION) {
	String sql2 = "DELETE FROM ARTICLE WHERE CODEA= '"+i+"' ";
	String sql = "DELETE FROM ENTREE WHERE CODEA= '"+i+"' ";
	String sql1 = "DELETE FROM SORTIE WHERE CODEA= '"+i+"' ";
	PreparedStatement p1;
	try {
		p1 = conn().prepareStatement(sql);
		p1.execute();
		p1 = conn().prepareStatement(sql1);
		p1.execute();
		p1 = conn().prepareStatement(sql2);
		p1.execute();
		  JOptionPane.showMessageDialog(null, "Article supprimé avec succés!","Suppression" ,JOptionPane.PLAIN_MESSAGE, new ImageIcon(AuthentificationFrame.class.getResource("/messages_img/stamp.png")));
	} catch (SQLException e) {
		JOptionPane.showConfirmDialog(null, e);
		// TODO Auto-generated catch block
		e.printStackTrace();
	}}
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
