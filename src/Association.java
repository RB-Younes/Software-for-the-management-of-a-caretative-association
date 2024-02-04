import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.JOptionPane;

public class Association {
	private String date,nom,adr,fond,asso;

	public Association(String date, String nom, String adr, String fond) {
		super();
		this.date = date;
		this.nom = nom;
		this.adr = adr;
		this.fond = fond;
	    this.asso = null;
	    
	}
	@SuppressWarnings("unused")
	public void ajouter() {
		try {
			String rq1="SELECT  asso_sq.NEXTVAL S FROM dual";
			 Statement p = conn().createStatement();
		 
		   ResultSet rs1= p.executeQuery(rq1);
		  int val = 1;
		   if(rs1.next()) {
		   	val=rs1.getInt(1);}
			asso=String.valueOf(val);
			Statement st = conn().createStatement();
			String rq = "INSERT INTO  ASSOCIATION VALUES (?,?,?,?,?)";
			PreparedStatement pp = conn().prepareStatement(rq);
			pp.setString(1, asso);
			pp.setString(3, fond);
			pp.setString(2, nom);
			pp.setString(4, date);
			pp.setString(5, adr);
			ResultSet rs = pp.executeQuery();
		}catch(Exception e) {
			JOptionPane.showMessageDialog(null, e.toString());
		}
			
	}
	

public void modifier(String i) {
	try {
		
		String rq = "UPDATE  ASSOCIATION SET nom='"+nom+"' ,fond='"+fond+"' ,adr='"+adr+"' ,date_='"+date+"'WHERE ass='"+i+"'";
	    PreparedStatement pp=conn().prepareStatement(rq);
	    pp.execute();
	}catch(Exception e) {
		JOptionPane.showMessageDialog(null, e.toString());
	}
}
public void supprimer(String i) {
	String sql2 = "DELETE FROM ASSOCIATION WHERE ass= '"+i+"' ";
	PreparedStatement p1;
	try {
		p1 = conn().prepareStatement(sql2);
		p1.execute();
	} catch (SQLException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
}
		public String getAsso() {
	return asso;
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
