package com.olympics.api.OlympicAPI.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.olympics.api.OlympicAPI.model.CompetitionModel;
import com.olympics.api.OlympicAPI.model.CountryModel;
import com.olympics.api.OlympicAPI.model.LocalModel;
import com.olympics.api.OlympicAPI.model.SportModel;
import com.olympics.api.OlympicAPI.model.StepModel;



public class OlympicsDAO extends DAO {

	/**
	 * Retorna um objeto CompetitionModel de acordo com o resultset vindo do banco de dados
	 * @param rs resultset da consulta no banco de dados
	 * @return objeto CompetitionModel
	 * @throws SQLException
	 */
	private static CompetitionModel filCompetitionModel(ResultSet rs) throws SQLException {
		CompetitionModel c = new CompetitionModel();
		SportModel sp = new SportModel();
		StepModel st = new StepModel();
		LocalModel loc = new LocalModel();
		
		
		c.setId(rs.getInt("cid"));
		c.setIniDate(rs.getTimestamp("inidate"));
		c.setEndDate(rs.getTimestamp("enddate"));
		
		sp.setId(rs.getInt("sport_id"));
		sp.setSport(rs.getString("sport"));
		c.setSport(sp);
		
		st.setId(rs.getInt("step_id"));
		st.setStep(rs.getString("step"));
		c.setStep(st);
		
		loc.setId(rs.getInt("local_id"));
		loc.setLocal(rs.getString("local"));
		c.setLocal(loc);
		
		c.setCountry1(getCounryById(rs.getInt("country1_id")));
		c.setCountry2(getCounryById(rs.getInt("country2_id")));
		
		return c;
	}
	
	/**
	 * Insere no banco de dados a campetiçao
	 * @param comp objeto CompetitionModel com as informações da competiçao
	 * @return
	 */
	public static Integer insertCompetition(CompetitionModel comp) {
		Connection c = null;
		PreparedStatement ps = null;
		Integer id = 0;
		try {
			c = getCon();
			StringBuilder sb = new StringBuilder();
			
			sb.append(" INSERT INTO competiton ( ")
			  .append(" 	inidate, enddate, country1_id, country2_id, step_id, local_id, sport_id ) ")
			  .append(" VALUES (?,?,?,?,?,?,?) ");
			
			ps = c.prepareStatement(sb.toString());
			ps.setTimestamp(1, comp.getIniDate());
			ps.setTimestamp(2, comp.getEndDate());
			ps.setInt(3, comp.getCountry1().getId());
			ps.setInt(4, comp.getCountry2().getId());
			ps.setInt(5, comp.getStep().getId());
			ps.setInt(6, comp.getLocal().getId());
			ps.setInt(7, comp.getSport().getId());
			
			ps.executeUpdate();
			c.commit();
		} catch (SQLException ex) {
			ex.printStackTrace();
		} finally {
			try {
				if (ps != null) {
					ps.close();
				}
			} catch (SQLException ex) {
				ex.printStackTrace();
			}
			fechaConexao(c);
		}
		return id;
	}
	
	/**
	 * Consulta no banco de o país informado
	 * @param name nome do país a ser procurado
	 * @return objeto country ou null caso nao encontre
	 */
	public static CountryModel getCounryByName(String name) {
		Connection c = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		CountryModel country = null;
		try {
			c = getCon();
			StringBuilder sb = new StringBuilder();
			
			sb.append("SELECT * ")
			  .append(" FROM country ")
			  .append(" WHERE country ilike ? ");
			
			ps = c.prepareStatement(sb.toString());
			ps.setString(1, "%"+name+"%");
			rs = ps.executeQuery();
			
			if (rs.next()) {
				country = new CountryModel();
				country.setId(rs.getInt("id"));
				country.setCountry(rs.getString("country"));
			}
		} catch (SQLException ex) {
			ex.printStackTrace();
		} finally {
			try {
				if (ps != null) {
					ps.close();
				}
				if (rs != null) {
					rs.close();
				}
			} catch (SQLException ex) {
				ex.printStackTrace();
			}
			fechaConexao(c);
		}
		return country;
	}
	
	public static Integer checkNumberOfCompetitions(CompetitionModel cm) {
		Connection c = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		Integer rows = 0;
		try {
			c = getCon();
			StringBuilder sb = new StringBuilder();
			
			sb.append("SELECT * ")
			  .append(" FROM competiton ")
			  .append(" WHERE local_id = ? AND inidate::date = ?::date ");
			
			ps = c.prepareStatement(sb.toString());
			ps.setInt(1, cm.getLocal().getId());
			ps.setTimestamp(2, cm.getIniDate());
			rs = ps.executeQuery();
			
			while (rs.next()) {
				rows++;
			}
		} catch (SQLException ex) {
			ex.printStackTrace();
		} finally {
			try {
				if (ps != null) {
					ps.close();
				}
				if (rs != null) {
					rs.close();
				}
			} catch (SQLException ex) {
				ex.printStackTrace();
			}
			fechaConexao(c);
		}
		return rows;
	}
	
	/**
	 * Verifica se a competição é válida para inserção
	 * @param cm
	 * @return
	 */
	public static Boolean isCompetitionValid(CompetitionModel cm) {
		Connection c = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			c = getCon();
			StringBuilder sb = new StringBuilder();
			
			sb.append("SELECT * ")
			  .append(" FROM competiton ")
			  .append(" WHERE local_id = ? AND sport_id = ? ")
			  .append(" AND ((? between inidate AND enddate) OR (? between inidate AND enddate)) ");
			
			ps = c.prepareStatement(sb.toString());
			ps.setInt(1, cm.getLocal().getId());
			ps.setInt(2, cm.getSport().getId());
			ps.setTimestamp(3, cm.getIniDate());
			ps.setTimestamp(4, cm.getEndDate());
			rs = ps.executeQuery();
			
			if (rs.next()) {
				return false;
			}
		} catch (SQLException ex) {
			ex.printStackTrace();
		} finally {
			try {
				if (ps != null) {
					ps.close();
				}
				if (rs != null) {
					rs.close();
				}
			} catch (SQLException ex) {
				ex.printStackTrace();
			}
			fechaConexao(c);
		}
		return true;
	}
	
	/**
	 * Retorna o local pelo nome
	 * @param name do local
	 * @return local ou null caso nao exista na base
	 */
	public static LocalModel getLocalByName(String name) {
		Connection c = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		LocalModel local = null;
		try {
			c = getCon();
			StringBuilder sb = new StringBuilder();
			
			sb.append("SELECT * ")
			  .append(" FROM local ")
			  .append(" WHERE local ilike ? ");
			
			ps = c.prepareStatement(sb.toString());
			ps.setString(1, "%"+name+"%");
			rs = ps.executeQuery();
			
			if (rs.next()) {
				local = new LocalModel();
				local.setId(rs.getInt("id"));
				local.setLocal(rs.getString("local"));
			}
		} catch (SQLException ex) {
			ex.printStackTrace();
		} finally {
			try {
				if (ps != null) {
					ps.close();
				}
				if (rs != null) {
					rs.close();
				}
			} catch (SQLException ex) {
				ex.printStackTrace();
			}
			fechaConexao(c);
		}
		return local;
	}
	
	public static CountryModel getCountryByName(String scountry) {
		Connection c = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		CountryModel country = null;
		try {
			c = getCon();
			StringBuilder sb = new StringBuilder();
			
			sb.append("SELECT * ")
			  .append(" FROM country ")
			  .append(" WHERE country ilike ? ");
			
			ps = c.prepareStatement(sb.toString());
			ps.setString(1, "%"+scountry+"%");
			rs = ps.executeQuery();
			
			if (rs.next()) {
				country = new CountryModel();
				country.setId(rs.getInt("id"));
				country.setCountry(rs.getString("country"));
			}
		} catch (SQLException ex) {
			ex.printStackTrace();
		} finally {
			try {
				if (ps != null) {
					ps.close();
				}
				if (rs != null) {
					rs.close();
				}
			} catch (SQLException ex) {
				ex.printStackTrace();
			}
			fechaConexao(c);
		}
		return country;
	}
	
	/**
	 * Retorna a etapa pelo nome
	 * @param name da etapa
	 * @return
	 */
	public static StepModel getStepByName(String name) {
		Connection c = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		StepModel step = null;
		try {
			c = getCon();
			StringBuilder sb = new StringBuilder();
			
			sb.append("SELECT * ")
			  .append(" FROM step ")
			  .append(" WHERE step ilike ? ");
			
			ps = c.prepareStatement(sb.toString());
			ps.setString(1, "%"+name+"%");
			rs = ps.executeQuery();
			
			if (rs.next()) {
				step = new StepModel();
				step.setId(rs.getInt("id"));
				step.setStep(rs.getString("step"));
			}
		} catch (SQLException ex) {
			ex.printStackTrace();
		} finally {
			try {
				if (ps != null) {
					ps.close();
				}
				if (rs != null) {
					rs.close();
				}
			} catch (SQLException ex) {
				ex.printStackTrace();
			}
			fechaConexao(c);
		}
		return step;
	}
	
	/**
	 * cadastra no bd um novo esporte
	 * @param sport
	 * @return
	 */
	public static Integer insertSport(String sport) {
		Connection c = null;
		PreparedStatement ps = null;
		Integer id = 0;
		try {
			c = getCon();
			StringBuilder sb = new StringBuilder();
			
			sb.append(" INSERT INTO sport ( ")
			  .append(" 	sport ) ")
			  .append(" VALUES (?) ");
			
			ps = c.prepareStatement(sb.toString());
			ps.setString(1,sport);
			
			ps.executeUpdate();
			c.commit();
		} catch (SQLException ex) {
			ex.printStackTrace();
		} finally {
			try {
				if (ps != null) {
					ps.close();
				}
			} catch (SQLException ex) {
				ex.printStackTrace();
			}
			fechaConexao(c);
		}
		return id;
	}
	
	public static Integer insertLocal(String local) {
		Connection c = null;
		PreparedStatement ps = null;
		Integer id = 0;
		try {
			c = getCon();
			StringBuilder sb = new StringBuilder();
			
			sb.append(" INSERT INTO local ( ")
			  .append(" 	local ) ")
			  .append(" VALUES (?) ");
			
			ps = c.prepareStatement(sb.toString());
			ps.setString(1,local);
			
			ps.executeUpdate();
			c.commit();
		} catch (SQLException ex) {
			ex.printStackTrace();
		} finally {
			try {
				if (ps != null) {
					ps.close();
				}
			} catch (SQLException ex) {
				ex.printStackTrace();
			}
			fechaConexao(c);
		}
		return id;
	}
	
	public static Integer insertCountry(String country) {
		Connection c = null;
		PreparedStatement ps = null;
		Integer id = 0;
		try {
			c = getCon();
			StringBuilder sb = new StringBuilder();
			
			sb.append(" INSERT INTO country ( ")
			  .append(" 	country ) ")
			  .append(" VALUES (?) ");
			
			ps = c.prepareStatement(sb.toString());
			ps.setString(1,country);
			
			ps.executeUpdate();
			c.commit();
		} catch (SQLException ex) {
			ex.printStackTrace();
		} finally {
			try {
				if (ps != null) {
					ps.close();
				}
			} catch (SQLException ex) {
				ex.printStackTrace();
			}
			fechaConexao(c);
		}
		return id;
	}
	
	/**
	 * Consulta no banco de dados o esporte pelo nome
	 * @param name nome do esporte a ser procurado
	 * @return objeto esporte ou null caso nao encontre
	 */
	public static SportModel getSportByName(String name) {
		Connection c = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		SportModel sp = null;
		try {
			c = getCon();
			StringBuilder sb = new StringBuilder();
			
			sb.append("SELECT * ")
			  .append(" FROM sport ")
			  .append(" WHERE sport ilike ? ");
			
			ps = c.prepareStatement(sb.toString());
			ps.setString(1, "%"+name+"%");
			rs = ps.executeQuery();
			
			if (rs.next()) {
				sp = new SportModel();
				sp.setId(rs.getInt("id"));
				sp.setSport(rs.getString("sport"));
			}
		} catch (SQLException ex) {
			ex.printStackTrace();
		} finally {
			try {
				if (ps != null) {
					ps.close();
				}
				if (rs != null) {
					rs.close();
				}
			} catch (SQLException ex) {
				ex.printStackTrace();
			}
			fechaConexao(c);
		}
		return sp;
	}
	
	/**
	 * 
	 * @return todos os esportes cadastrados no bd
	 */
	public static List<SportModel> getAllSports() {
		Connection c = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<SportModel> l = new ArrayList<SportModel>();
		SportModel sp = null;
		try {
			c = getCon();
			StringBuilder sb = new StringBuilder();
			
			sb.append("SELECT * ")
			  .append(" FROM sport ");
			
			ps = c.prepareStatement(sb.toString());
			rs = ps.executeQuery();
			
			while (rs.next()) {
				sp = new SportModel();
				sp.setId(rs.getInt("id"));
				sp.setSport(rs.getString("sport"));
				l.add(sp);
			}
		} catch (SQLException ex) {
			ex.printStackTrace();
		} finally {
			try {
				if (ps != null) {
					ps.close();
				}
				if (rs != null) {
					rs.close();
				}
			} catch (SQLException ex) {
				ex.printStackTrace();
			}
			fechaConexao(c);
		}
		return l;
	}
	
	/**
	 * retorna a lista de todas as competiçoes ou as competiçoes de um
	 * dado esporte caso o filtro nao seja null e seja um esporte valido
	 * @param sportFilter null para retornar todas as competiçoes ou um esporte valido para o filtro
	 * @return lista com as competiçoes encontradas
	 */
	public static List<CompetitionModel> listCompetitions(SportModel sportFilter) {
		Connection c = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<CompetitionModel> l = new ArrayList<CompetitionModel>();
		try {
			c = getCon();
			StringBuilder sb = new StringBuilder();
			
			sb.append("SELECT competiton.id as cid, sport_id, local_id, step_id, country1_id, country2_id, inidate, enddate, ")
			  .append(" step, sport, local ")
			  .append(" FROM competiton as competiton, sport as sport, step as step, local as local ")
			  .append(" WHERE step_id = step.id AND local_id = local.id AND sport_id = sport.id ");
			if (sportFilter != null && sportFilter.getId() != -1) {			
				sb.append(" AND sport_id = sport.id AND sport.id = ? ");
			}
			sb.append(" ORDER BY inidate ASC ");
			
			ps = c.prepareStatement(sb.toString());
			if (sportFilter != null && sportFilter.getId() != -1) {
				ps.setInt(1, sportFilter.getId());
			}
			rs = ps.executeQuery();
			
			while (rs.next()) {
				l.add(filCompetitionModel(rs));
			}
		} catch (SQLException ex) {
			ex.printStackTrace();
		} finally {
			try {
				if (ps != null) {
					ps.close();
				}
				if (rs != null) {
					rs.close();
				}
			} catch (SQLException ex) {
				ex.printStackTrace();
			}
			fechaConexao(c);
		}
		return l;
	}
	
	/**
	 * retorna o pais pelo id do bd
	 * @param id id do pais
	 * @return objeto country ou null caso nao encontre
	 */
	public static CountryModel getCounryById(Integer id) {
		Connection c = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		CountryModel country = null;
		try {
			c = getCon();
			StringBuilder sb = new StringBuilder();
			
			sb.append("SELECT * ")
			  .append(" FROM country ")
			  .append(" WHERE id = ? ");
			
			ps = c.prepareStatement(sb.toString());
			ps.setInt(1, id);
			rs = ps.executeQuery();
			
			if (rs.next()) {
				country = new CountryModel();
				country.setId(rs.getInt("id"));
				country.setCountry(rs.getString("country"));
			}
		} catch (SQLException ex) {
			ex.printStackTrace();
		} finally {
			try {
				if (ps != null) {
					ps.close();
				}
				if (rs != null) {
					rs.close();
				}
			} catch (SQLException ex) {
				ex.printStackTrace();
			}
			fechaConexao(c);
		}
		return country;
	}
}
