package com.olympics.api.OlympicAPI.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.google.gson.Gson;
import com.olympics.api.OlympicAPI.dao.OlympicsDAO;
import com.olympics.api.OlympicAPI.model.CompetitionModel;
import com.olympics.api.OlympicAPI.model.CountryModel;
import com.olympics.api.OlympicAPI.model.LocalModel;
import com.olympics.api.OlympicAPI.model.ServerMessageModel;
import com.olympics.api.OlympicAPI.model.SportModel;
import com.olympics.api.OlympicAPI.model.StepModel;


@Path("/")
public class OlympicAPIService {


	/**
	 * 
	 * Consulta a base e retorna um JSON com todas as competiçoes ordenadas pela data de inicio
	 * .../OlympicsAPI/api/consultcompetitions
	 * 
	 * Consulta a base e retorna um JSON com todas as competiçoes ordenadas pela data de inicio e que sejam do esporte especificado
	 * .../OlympicsAPI/api/consultcompetitions?sport=Futebol
	 * *** Se o esporte não existir na base, retorna uma mensagem informando todos os esportes inseridos e a URL para cadastrar outro esporte
	 * 
	 * Exemplo de JSON:
	 * [
	 * {
	 * 	"id":1,
	 * 	"iniDate":"Jan 25, 2018 1:02:51 AM",
	 * 	"endDate":"Jan 25, 2018 1:02:51 AM",
	 * 	"country1":{
	 * 				"id":1,
	 * 				"country":"Brasil"
	 * 			   },
	 * 	"country2":{
	 * 				"id":2,
	 * 				"country":"EUA"
	 * 			   },
	 * 	"step":    {
	 * 				"id":1,
	 * 				"step":"Eliminatórias"
	 * 		       },
	 * 	"local":   {
	 * 				"id":1,
	 * 				"local":"Local 1"
	 * 			   },
	 * 	"sport":   {
	 * 				"id":1,
	 * 				"sport":"Futebol"
	 *    		   }
	 * }
	 * ]
	 * 
	 * @param requestContext para pegar o parametro de filtro
	 * @return JSON com os dados
	 */
	@GET
	@Path("/consultcompetitions")
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	public Response consultCompetitions(@Context HttpServletRequest requestContext) {
		String filter = requestContext.getParameter("sport");
		SportModel sp = null;
		Gson json = new Gson();
		if (filter != null && !filter.isEmpty()) {
			sp = OlympicsDAO.getSportByName(filter);
			if (sp == null) {
				ServerMessageModel msg = new ServerMessageModel();
				msg.setType("Erro");
				msg.setMsg("Este esporte não existe! Veja a lista de esportes existentes e utilize /OlympicsAPI/api/registersport?sport={esporte} para cadastrar um novo esporte.<br>");
				msg.setMsg(msg.getMsg()+OlympicsDAO.getAllSports().toString());
				return Response.status(200).entity(json.toJson(msg)).build();
			}
		}
		List<CompetitionModel> list = OlympicsDAO.listCompetitions(sp);
		String res = json.toJson(list.isEmpty() ? ("Nenhuma competição encontrada" + (filter != null ? " para o esporte " + filter : "")) : list);
		return Response.status(200).entity(res).build();
	}
	
	@POST
	@Path("/insertcompetition")
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response insertcompetition(@FormParam("competition") String js){
		Gson json = new Gson();
		CompetitionModel cm = json.fromJson(js, CompetitionModel.class);
		ServerMessageModel sm = new ServerMessageModel();
		if (cm.getEndDate().getTime() - cm.getIniDate().getTime() < (1000 * 60 * 30)) {
			sm.setType("Erro");
			sm.setMsg("A competição não pode ter menos de 30 minutos");
			return Response.status(200).entity(json.toJson(sm)).build();
		}
		CountryModel country = OlympicsDAO.getCounryByName(cm.getCountry1().getCountry());
		if (country == null) {
			sm.setType("Erro");
			sm.setMsg("Pais " + cm.getCountry1().getCountry() + " nao existente na database. "
					+ "Utilize /OlympicsAPI/api/registercountry?countr={pais} para registrar um novo pais");
			return Response.status(200).entity(json.toJson(sm)).build();
		}
		cm.setCountry1(country);
		country = OlympicsDAO.getCounryByName(cm.getCountry2().getCountry());
		if (country == null) {
			sm.setType("Erro");
			sm.setMsg("Pais " + cm.getCountry2().getCountry() + " nao existente na database. "
					+ "Utilize /OlympicsAPI/api/registercountry?country={pais} para registrar um novo pais");
			return Response.status(200).entity(json.toJson(sm)).build();
		}
		cm.setCountry2(country);
		LocalModel local = OlympicsDAO.getLocalByName(cm.getLocal().getLocal());
		if (local == null) {
			sm.setType("Erro");
			sm.setMsg("Local " + cm.getLocal().getLocal() + " nao existente na database. "
					+ "Utilize /OlympicsAPI/api/registerlocal?local={local} para registrar um novo local");
			return Response.status(200).entity(json.toJson(sm)).build();
		}
		cm.setLocal(local);
		StepModel step = OlympicsDAO.getStepByName(cm.getStep().getStep());
		if (step == null) {
			sm.setType("Erro");
			sm.setMsg("Etapa " + cm.getStep().getStep() + " nao é válida ");
			return Response.status(200).entity(json.toJson(sm)).build();
		}
		cm.setStep(step);
		SportModel sp = OlympicsDAO.getSportByName(cm.getSport().getSport());
		if (sp == null) {
			sm.setType("Erro");
			sm.setMsg("Esporte " + cm.getSport().getSport() + " nao existente na database. "
					+ "Utilize /OlympicsAPI/api/registersport?sport={sport} para registrar um novo sport");
			return Response.status(200).entity(json.toJson(sm)).build();
		}
		cm.setSport(sp);
		if (OlympicsDAO.checkNumberOfCompetitions(cm) >= 4) {
			sm.setType("Erro");
			sm.setMsg("Nao e possivel ter mais de 4 competicoes no mesmo dia e lugar ");
			return Response.status(200).entity(json.toJson(sm)).build();
		}
		
		if (!OlympicsDAO.isCompetitionValid(cm)) {
			sm.setType("Erro");
			sm.setMsg("Ja existe uma competicao desta modalidade neste horario e local ");
			return Response.status(200).entity(json.toJson(sm)).build();
		}
		OlympicsDAO.insertCompetition(cm);
		sm.setType("Sucesso");
		sm.setMsg(" Competiçao inserida com sucesso ");
		
		return Response.status(200).entity(json.toJson(sm)).build();
	}
	

	/**
	 * Registra um novo esporte no banco de dados
	 * @param requestContext necessario para pegar o parametro esporte
	 * @return mensagem com o resultado da inserção
	 */
	@GET
	@Path("/registersport")
	@Produces(MediaType.TEXT_PLAIN + ";charset=utf-8")
	public Response registersport(@Context HttpServletRequest requestContext) {
		String sport = requestContext.getParameter("sport");
		String msg = "Esporte inserido com sucesso!";
		if (sport == null || sport.isEmpty()) {
			msg = "Parâmetro sport é necessário. Exemplo: /OlympicsAPI/api/registersport?sport=Futebol";
			return Response.status(200).entity(msg).build();
		}
		SportModel sp = OlympicsDAO.getSportByName(sport);
		if (sp != null) {
			msg = "Esporte já existente!";
			return Response.status(200).entity(msg).build();
		}
		OlympicsDAO.insertSport(sport);
		return Response.status(200).entity(msg).build();
	}
	
	@GET
	@Path("/registercountry")
	@Produces(MediaType.TEXT_PLAIN + ";charset=utf-8")
	public Response registercountry(@Context HttpServletRequest requestContext) {
		String country = requestContext.getParameter("country");
		String msg = "País inserido com sucesso!";
		if (country == null || country.isEmpty()) {
			msg = "Parâmetro country é necessário. Exemplo: /OlympicsAPI/api/registercountry?country=Brasil";
			return Response.status(200).entity(msg).build();
		}
		CountryModel cm = OlympicsDAO.getCountryByName(country);
		if (cm != null) {
			msg = "País já existente!";
			return Response.status(200).entity(msg).build();
		}
		OlympicsDAO.insertCountry(country);
		return Response.status(200).entity(msg).build();
	}
	
	@GET
	@Path("/registerlocal")
	@Produces(MediaType.TEXT_PLAIN + ";charset=utf-8")
	public Response registerlocal(@Context HttpServletRequest requestContext) {
		String local = requestContext.getParameter("local");
		String msg = "Local inserido com sucesso!";
		if (local == null || local.isEmpty()) {
			msg = "Parâmetro local é necessário. Exemplo: /OlympicsAPI/api/registerlocal?local=Estadio";
			return Response.status(200).entity(msg).build();
		}
		LocalModel localm = OlympicsDAO.getLocalByName(local);
		if (localm != null) {
			msg = "Local já existente!";
			return Response.status(200).entity(msg).build();
		}
		OlympicsDAO.insertLocal(local);
		return Response.status(200).entity(msg).build();
	}
}
