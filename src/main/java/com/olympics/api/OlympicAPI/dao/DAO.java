package com.olympics.api.OlympicAPI.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class DAO {
	
	private static final String USUARIO = "postgres", SENHA = "890823Fe";
	
	private static List<Connection> connectionPool = new ArrayList<Connection>(145);

	/**
	 * Cria uma conexão  banco de dados
	 *
	 * @throws SQLException
	 */
	private static Connection estabeleceConexao() throws SQLException {
		return criaConexao(USUARIO, SENHA);
	}

	public static synchronized Connection getCon() {
		try {
			Connection c;
			if (connectionPool.isEmpty()) {
				System.out.println("<< NENHUMA CONEXAO DISPONIVEL. CRIANDO UMA NOVA.  >>");
				c = estabeleceConexao();
				c.setAutoCommit(false);
				return c;
			}
			c = connectionPool.remove(0);
			if (c == null || (c != null && c.isClosed())) {
				c = estabeleceConexao();
				c.setAutoCommit(false);
			}
			return c;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
		
	}

	/**
	 * Fecha uma conexão e o statement cm o banco de dados
	 *
	 * @throws SQLException
	 */
	public static synchronized void fechaConexao(Connection con) {
		try {
			connectionPool.add(con);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	private static Connection criaConexao(String usuario, String senha)
			throws SQLException {
		String driver = "org.postgresql.Driver";
		String banco = "olympics";
		String host = "127.0.0.1:5433/";
		Properties props = new Properties();
		props.setProperty("user", usuario);
		props.setProperty("password", senha);
		props.setProperty("loginTimeout", "5");
		props.setProperty("socketTimeout", "5");
		
		String strConn = "jdbc:postgresql://" + host + banco;
		try {
			Class.forName(driver);
		} catch (ClassNotFoundException ex) {
			ex.printStackTrace();
		}
		return  DriverManager.getConnection(strConn, props);
	}
}

