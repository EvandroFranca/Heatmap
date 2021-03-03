package utils;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.Calendar;
import java.util.Date;
import java.util.Properties;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Auxiliar {
	public String msgRegSucesso(){
		return "Registro inserido com sucesso !";
	}
	
	public String msgRegFalha(String e){
		return "Falha ao inserir registro. FALHA: [" + e + "]";
	}
	
	public String formataData(String data){
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		return df.format(data);
	}
	
	public String formataDataMes(String data){
		String dt_final = "";
		String array[] = data.split("/");
		
		dt_final = array[1] + "-" + array[0] + "-01";
		
		return dt_final;
	}
	
	public String formataDataBanco(String data){
		String dt_final = "";
		String array[] = data.split("/");
		
		dt_final = array[2] + "-" + array[1] + "-" + array[0];
		
		return dt_final;
	}
	
	public String pegaInstrucao(String caminho) throws IOException{
		StringBuilder sb = new StringBuilder();
		String sqlString = "";
		String linha = "";
		
		BufferedReader bReader =  new BufferedReader(
				(new InputStreamReader(new FileInputStream(caminho), "ISO-8859-1")));
		
		while((linha = bReader.readLine()) != null){
			if(!linha.contains("--") ){
				sb.append(" ");
				sb.append(linha.replaceAll(";", ""));
				sb.append(" ");
				sb.append("\n");
			}
			sqlString = sb.toString();
		}
		
		return sqlString;
	}
	
	public int pegaHorasMesAtual(){
		int totalHoras = 0;
		Calendar cal = Calendar.getInstance();
		int mes = cal.get(Calendar.MONTH) + 1;
		int ano = cal.get(Calendar.YEAR);
		int dias = 0;
		YearMonth anoMes = YearMonth.of(ano, mes);
		 
		for(int dia = 1; dia <= anoMes.lengthOfMonth(); dia++){ 
			LocalDate data = anoMes.atDay(dia);  
		  
			if(!data.getDayOfWeek().equals(DayOfWeek.SATURDAY) && !data.getDayOfWeek().equals(DayOfWeek.SUNDAY)){
				dias++;
			}
		}
		
		totalHoras = dias * 8;
		
		return totalHoras;
	}
	
	public String listaMesAnoJson(int qtdMes, String dtInicio) throws JSONException{
		int qtd = qtdMes;
		String dataInicio = dtInicio;
		DateFormat df = new SimpleDateFormat("MM/yyyy");
	    Date dtAux = null;
	    JSONArray jsonArray = new JSONArray();
	    
	    try {
	    	 dtAux = df.parse(dataInicio);
	    } catch (ParseException e) {
	         e.printStackTrace();
	    }
	    
        Calendar c = Calendar.getInstance();
        c.setTime(dtAux);
        
        for (int i = 0; i < qtd; i++) {
        	JSONObject my_obj = new JSONObject();
        	
            c.add(Calendar.MONTH, 1);
            Date dtParcelasGeradas = c.getTime();  
            my_obj.put("mesAno", df.format(dtParcelasGeradas ));
            
            jsonArray.put(my_obj);
        }
        
        return jsonArray.toString();
	}
	
	public String pegarHorarioServidor() {
		SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss");  
	    Date date = new Date();  
	    return formatter.format(date);
	}
	
	public String pegarDataServidor() {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");  
	    Date date = new Date();  
	    return formatter.format(date);
	}
	
	public int retornarIDFuncionalidades(String funcionalidade) {
		String func = funcionalidade.toLowerCase();
		
		switch (func) {
		case "login":
			return 1;
		case "cliente":
			return 2;
		case "saldos/limites":
			return 3;
		case "emprestimos":
			return 4;
		case "vendas em cartao":
			return 5;
		case "pegue safra":
			return 6;
		case "cobsafra":
			return 7;
		case "investimentos":
			return 8;
		default:
			return 0;
		}
	}
	
	public String converterDataPadraoBanco(String data) throws ParseException {
		 Date initDate = new SimpleDateFormat("dd-MM-yyyy").parse(data);
		 SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		 String parsedDate = formatter.format(initDate);

		 return parsedDate;
	}

	 public Properties getProp() throws IOException {
	        Properties props = new Properties();
	        // le o arquivo de configuracoes de c:\\users\\nome_usuário
	        FileInputStream file = new FileInputStream(System.getProperty("user.home") + "\\dados.properties");
	        props.load(file);
	        return props;
	 
	 }		
}
