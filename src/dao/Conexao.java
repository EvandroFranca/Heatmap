package dao;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

import utils.Auxiliar;	

public class Conexao {
	static private Connection conn;

	public Conexao(){
		// super(); 
	}
	public Connection getConnection() {

		Properties prop = new Properties();
		try {
			prop = new Auxiliar().getProp();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		System.out.println("[Neon] Iniciando leitura properties");
		
//		String host = prop.getProperty("prop.db.host").trim();		 
//		String user = prop.getProperty("prop.db.user").trim();
//		String password = prop.getProperty("prop.db.password").trim();

		String host = "localhost";		 
		String user = "metal";
		String password = "metal666.";
		
		try {
			if (conn == null || conn.isClosed()) {
				String driver = "com.mysql.jdbc.Driver";
				try {
					Class.forName(driver);
					conn = (Connection) DriverManager.getConnection("jdbc:mysql://"+ host +":3306/heatmap_neon?autoReconnect=true", user, password);

					System.out.println("Conectado com sucesso - Neon");
					
				} catch (ClassNotFoundException | SQLException sqle) {
					System.out.println("Ocorreu um erro ao conectar com o banco de dados SQL: " + sqle.getMessage());
				}
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return conn;
	}

}
