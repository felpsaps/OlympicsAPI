import java.io.IOException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import com.google.gson.Gson;
import com.olympics.api.OlympicAPI.model.CompetitionModel;
import com.olympics.api.OlympicAPI.model.CountryModel;
import com.olympics.api.OlympicAPI.model.LocalModel;
import com.olympics.api.OlympicAPI.model.SportModel;
import com.olympics.api.OlympicAPI.model.StepModel;

public class OlympicsTestClient {

	private CloseableHttpClient client;
	
	private String BASE_URL = "http://localhost:8282/OlympicsAPI/api/";
	private String CONSULT_URL = "consultcompetitions";
	private String REGISTER_SPORT_URL = "registersport?sport=";
	private String REGISTER_COUNTRY_URL = "registercountry?country=";
	private String REGISTER_LOCAL_URL = "registerlocal?local=";
	private String REGISTER_COMPETITION_URL = "insertcompetition";
	
	private DateFormat df;
	
	public OlympicsTestClient() {
		RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(30000).build();
		client = HttpClients.custom().setDefaultRequestConfig(requestConfig).build();
		df = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
	}
	
	private void startTests() {
		try {
			CountryModel country1 = new CountryModel();
			CountryModel country2 = new CountryModel();
			LocalModel local = new LocalModel();
			SportModel sport = new SportModel();
			StepModel step = new StepModel();
			CompetitionModel cm = new CompetitionModel();
			Gson json = new Gson();
			List<NameValuePair> postParams = new ArrayList<NameValuePair>();
			
			/**
			 * Acao: Realiza a consulta das competiçoes
			 * Resultado esperado: Retorna nenhuma competiçao
			 */
			System.out.println(makeGet(client, BASE_URL+CONSULT_URL));
			
			/* Criaçao da competiçao */
			country1.setCountry("Brasil");
			country2.setCountry("EUA");
			local.setLocal("Estadio 1");
			sport.setSport("Futebol");
			step.setStep("Final");
			cm.setIniDate(new Timestamp(df.parse("01/01/2018 13:00:00").getTime()));
			cm.setEndDate(new Timestamp(df.parse("01/01/2018 13:25:00").getTime()));
			cm.setCountry1(country1);
			cm.setCountry2(country2);
			cm.setLocal(local);
			cm.setSport(sport);
			cm.setStep(step);
			
			/**
			 * Acao: Tenta cadastrar uma competicao menos de 30 minutos
			 * Resultado esperado: Mensagem de erro
			 */
			postParams.add(new BasicNameValuePair("competition", json.toJson(cm)));
			System.out.println(makePost(client, BASE_URL+REGISTER_COMPETITION_URL, postParams));

			/**
			 * Acao: Altera o tempo para mais de 30 minutos, porém nenhum pais, esporte, local estao cadastrados
			 * Resultado esperado: Mensagem de erro
			 */
			postParams = new ArrayList<NameValuePair>();
			cm.setEndDate(new Timestamp(df.parse("01/01/2018 13:35:00").getTime()));
			postParams.add(new BasicNameValuePair("competition", json.toJson(cm)));
			System.out.println(makePost(client, BASE_URL+REGISTER_COMPETITION_URL, postParams));
			
			/**
			 * Acao: Registra alguns paises
			 * Resultado esperado: Mensagem de sucesso e erro para cadastrar o Brasil 2 vezes
			 */
			System.out.println(makeGet(client, BASE_URL+REGISTER_COUNTRY_URL+"Brasil"));
			System.out.println(makeGet(client, BASE_URL+REGISTER_COUNTRY_URL+"EUA"));
			System.out.println(makeGet(client, BASE_URL+REGISTER_COUNTRY_URL+"Canada"));
			System.out.println(makeGet(client, BASE_URL+REGISTER_COUNTRY_URL+"Italia"));
			System.out.println(makeGet(client, BASE_URL+REGISTER_COUNTRY_URL+"Brasil"));
			System.out.println(makeGet(client, BASE_URL+REGISTER_COUNTRY_URL+"Argentina"));
			System.out.println(makeGet(client, BASE_URL+REGISTER_COUNTRY_URL+"Colombia"));
			System.out.println(makeGet(client, BASE_URL+REGISTER_COUNTRY_URL+"Coreia+do+Sul"));
			System.out.println(makeGet(client, BASE_URL+REGISTER_COUNTRY_URL+"Alemanha"));
			
			/**
			 * Acao: Tenta cadastrar competiçao, porém nenhum  esporte, local estao cadastrados
			 * Resultado esperado: Mensagem de erro
			 */
			System.out.println(makePost(client, BASE_URL+REGISTER_COMPETITION_URL, postParams));
			
			/**
			 * Acao: Registra alguns locais
			 * Resultado esperado: Mensagem de sucesso e erro para cadastrar o Estadio 1, 2 vezes
			 */
			System.out.println(makeGet(client, BASE_URL+REGISTER_LOCAL_URL+"Estadio+1"));
			System.out.println(makeGet(client, BASE_URL+REGISTER_LOCAL_URL+"Estadio+2"));
			System.out.println(makeGet(client, BASE_URL+REGISTER_LOCAL_URL+"Estadio+3"));
			System.out.println(makeGet(client, BASE_URL+REGISTER_LOCAL_URL+"Estadio+4"));
			System.out.println(makeGet(client, BASE_URL+REGISTER_LOCAL_URL+"Estadio+1"));
			
			/**
			 * Acao: Tenta cadastrar competiçao, porém nenhum  esporte esta cadastrado
			 * Resultado esperado: Mensagem de erro
			 */
			System.out.println(makePost(client, BASE_URL+REGISTER_COMPETITION_URL, postParams));
			
			/**
			 * Acao: Registra alguns esportes
			 * Resultado esperado: Mensagem de sucesso e erro para cadastrar o Futebol  2 vezes
			 */
			System.out.println(makeGet(client, BASE_URL+REGISTER_SPORT_URL+"Futebol"));
			System.out.println(makeGet(client, BASE_URL+REGISTER_SPORT_URL+"Futebol"));
			System.out.println(makeGet(client, BASE_URL+REGISTER_SPORT_URL+"Basquete"));
			System.out.println(makeGet(client, BASE_URL+REGISTER_SPORT_URL+"Tenis"));
			System.out.println(makeGet(client, BASE_URL+REGISTER_SPORT_URL+"Voley"));
			
			/**
			 * Acao: Tenta cadastrar competiçao, 
			 * Resultado esperado: Mensagem de sucesso
			 */
			System.out.println(makePost(client, BASE_URL+REGISTER_COMPETITION_URL, postParams));
			
			/**
			 * Acao: Tenta cadastrar competiçao, 
			 * Resultado esperado: Nao é possivel ter uma mesma competiçao da mesma modalidade no mesmo local e horario
			 */
			System.out.println(makePost(client, BASE_URL+REGISTER_COMPETITION_URL, postParams));
			
			/**
			 * Acao: Insere outra competiçao
			 * Resultado esperado: Mensagem de sucesso
			 */
			postParams = new ArrayList<NameValuePair>();
			cm.setIniDate(new Timestamp(df.parse("01/01/2018 10:35:00").getTime()));
			cm.setEndDate(new Timestamp(df.parse("01/01/2018 11:35:00").getTime()));
			cm.getCountry1().setCountry("Canada");
			cm.getCountry2().setCountry("Italia");
			cm.getSport().setSport("Tenis");
			postParams.add(new BasicNameValuePair("competition", json.toJson(cm)));
			System.out.println(makePost(client, BASE_URL+REGISTER_COMPETITION_URL, postParams));
			
			/**
			 * Acao: Insere terceira competiçao
			 * Resultado esperado: Mensagem de sucesso
			 */
			postParams = new ArrayList<NameValuePair>();
			cm.setIniDate(new Timestamp(df.parse("01/01/2018 08:35:00").getTime()));
			cm.setEndDate(new Timestamp(df.parse("01/01/2018 09:35:00").getTime()));
			cm.getCountry1().setCountry("Alemanha");
			cm.getCountry2().setCountry("Coreia do sul");
			cm.getSport().setSport("Tenis");
			postParams.add(new BasicNameValuePair("competition", json.toJson(cm)));
			System.out.println(makePost(client, BASE_URL+REGISTER_COMPETITION_URL, postParams));
			
			/**
			 * Acao: Insere quarta competiçao
			 * Resultado esperado: Mensagem de sucesso
			 */
			postParams = new ArrayList<NameValuePair>();
			cm.setIniDate(new Timestamp(df.parse("01/01/2018 14:35:00").getTime()));
			cm.setEndDate(new Timestamp(df.parse("01/01/2018 15:35:00").getTime()));
			cm.getCountry1().setCountry("Brasil");
			cm.getCountry2().setCountry("Italia");
			cm.getSport().setSport("Futebol");
			postParams.add(new BasicNameValuePair("competition", json.toJson(cm)));
			System.out.println(makePost(client, BASE_URL+REGISTER_COMPETITION_URL, postParams));
			
			/**
			 * Acao: Tenta inserir quinta competiçao
			 * Resultado esperado: Mensagem de apenas 4 competiçoes por dia
			 */
			postParams = new ArrayList<NameValuePair>();
			cm.setIniDate(new Timestamp(df.parse("01/01/2018 17:35:00").getTime()));
			cm.setEndDate(new Timestamp(df.parse("01/01/2018 18:35:00").getTime()));
			cm.getCountry1().setCountry("Alemanha");
			cm.getCountry2().setCountry("Argentina");
			cm.getSport().setSport("Futebol");
			postParams.add(new BasicNameValuePair("competition", json.toJson(cm)));
			System.out.println(makePost(client, BASE_URL+REGISTER_COMPETITION_URL, postParams));
			
			/**
			 * Acao: Realiza a consulta das competiçoes
			 * Resultado esperado: Retorna as duas competiçoes inseridas, primeiro o Tenis e depois Futebol
			 */
			System.out.println(makeGet(client, BASE_URL+CONSULT_URL));
			
			/**
			 * Acao: Realiza a consulta das competiçoes filtrando por tenis
			 * Resultado esperado: Retorna a competiçao tenis
			 */
			System.out.println(makeGet(client, BASE_URL+CONSULT_URL+"?sport=tenis"));
			
			/**
			 * Acao: Realiza a consulta das competiçoes filtrando por basquete
			 * Resultado esperado: Retorna nenhuma competicao
			 */
			System.out.println(makeGet(client, BASE_URL+CONSULT_URL+"?sport=basquete"));
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public String makePost(CloseableHttpClient client, String url, List<NameValuePair> postParams) throws Exception {
		CloseableHttpResponse rspRequest = null;
		String rsp = "";
		try {
			String getLink = url;			
			HttpPost postRequest = new HttpPost(getLink);
			postRequest.setHeader("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/61.0.3163.100 Safari/537.36");
			postRequest.setHeader("Content-Type", "application/json");


			if (postParams != null) {
				postRequest.setEntity(new UrlEncodedFormEntity(postParams));
			}
			rspRequest = client.execute(postRequest);
			rsp = EntityUtils.toString(rspRequest.getEntity());
			//System.out.println(rsp);
			EntityUtils.consume(rspRequest.getEntity());; 
			rspRequest.close();
		} finally {
			try {
				if (rspRequest != null) {
					rspRequest.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return rsp;
	}
	
	public static String makeGet(CloseableHttpClient client, String url) throws Exception {
		CloseableHttpResponse rspRequest = null;
		String rsp = "";
		try {
			String getLink = url;			
			HttpGet getRequest = new HttpGet(getLink);			
			getRequest.setHeader("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/61.0.3163.100 Safari/537.36");

			rspRequest = client.execute(getRequest);

			HttpEntity entity = rspRequest.getEntity();

			rsp = EntityUtils.toString(entity);
			//System.out.println(rsp);
			EntityUtils.consume(rspRequest.getEntity());; 
			rspRequest.close();
		} finally {
			try {
				if (rspRequest != null) {
					rspRequest.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return rsp;
	}
	
	public static void main(String[] args) {
		OlympicsTestClient tc = new OlympicsTestClient();
		tc.startTests();
	}
}
