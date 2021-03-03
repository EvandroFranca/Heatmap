package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import bean.BeanHeatmap;
import utils.Auxiliar;

public class CrudHeatmap {

	Conexao con = new Conexao();
	Connection conn = con.getConnection();
	BeanHeatmap bean = new BeanHeatmap();
	Auxiliar aux = new Auxiliar();

	private String sql;
	private String retorno;

	public String recuperarTabela(String data, String app) throws ParseException {
		//System.out.println("Versão App[API]: " + app);	
		JSONArray jsonArray = new JSONArray();

		String dataConvertida = aux.converterDataPadraoBanco(data);
		String descricaoApp = app.equals("ALL") ? "" : " and (descricao=\"" + app + "\" or descricao=\"TESTE\")"; 
		
		

		sql = "SELECT t.idfuncionalidade, log.funcionalidade, DATE_FORMAT(t.DataHoraLog, \"%H\") as hora,"
				+ " count(*) as total, coalesce(t1.passou, count(*)), CONCAT(format(coalesce((100 - ((t1.passou) * 100 / count(*))), 100), '#'), ' %')  as percent,"
				+ " coalesce((100 - (t1.passou * 100 / count(*))), 100) as percent"
				+ " from tbloghetmap t"
				+ " left join (SELECT idfuncionalidade, DATE_FORMAT(DataHoraLog, \"%H\") as hora, count(*) as passou "
				
				//+ " from tbloghetmap where StatusExecucao = 'falhou' and DATE_FORMAT(datahoralog, \"%Y-%m-%d\") = \""+dataConvertida+"\" and (descricao=\"" + app + "\" or descricao=\"TESTE\")"
				+ " from tbloghetmap where StatusExecucao = 'falhou' and DATE_FORMAT(datahoralog, \"%Y-%m-%d\") = \"" + dataConvertida + "\"" + descricaoApp
				
				+ " group by idfuncionalidade, DATE_FORMAT(DataHoraLog, \"%H\")) t1"
				+ " on DATE_FORMAT(t.DataHoraLog, \"%H\") = t1.hora"
				+ " and t.idfuncionalidade = t1.idFuncionalidade"
				+ " INNER JOIN bdheatmap.tbfuncionalidades log"
				+ " ON t.idFuncionalidade = log.idFuncionalidade"
				
				//+ " and DATE_FORMAT(datahoralog, \"%Y-%m-%d\") = \""+dataConvertida+"\" and (descricao=\"" + app + "\" or descricao=\"TESTE\")"
				+ " and DATE_FORMAT(datahoralog, \"%Y-%m-%d\") = \""+dataConvertida+ "\"" + descricaoApp
				
				+ " group by t.idfuncionalidade, DATE_FORMAT(t.DataHoraLog, \"%H\")";

		
		
		//System.out.println(sql);
		
		try {
			Statement st = conn.createStatement();
			ResultSet rs = st.executeQuery(sql);

			while(rs.next()){
				JSONObject my_obj = new JSONObject();

				my_obj.put("idFuncionalidade",rs.getString("idfuncionalidade"));
				my_obj.put("hora",rs.getString("hora"));
				my_obj.put("totalExecutados",rs.getString("total"));
				my_obj.put("percent",rs.getString("percent"));

				jsonArray.put(my_obj);
			}

			retorno = jsonArray.toString();
		} catch (SQLException | JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return retorno;
	}


	public String salvarTabela(String funcionalidade, String status, String descricao) throws JSONException {
		int id = Integer.parseInt(funcionalidade);

		sql = "insert into tbloghetmap "
				+ "("
				+ "idFuncionalidade"
				+ ",DataHoraLog"
				+ ",StatusExecucao"
				+ ",Descricao"
				+ ")"
				+ "values(?, NOW(), ?, ?)";

		try {
			PreparedStatement pst = conn.prepareStatement(sql);

			pst.setInt(1, id);
			pst.setString(2, status);
			pst.setString(3, descricao);

			pst.executeUpdate();

			retorno = "OK";

		}catch(SQLException e) {
			e.printStackTrace();
			retorno = aux.msgRegFalha(e.getMessage());
		}
		return retorno;
	}

	private int recuperarIDFuncionalidade(String funcionalidade) {
		String sql = "select idFuncionalidade from tbFuncionalidades where Funcionalidade = '"+funcionalidade+"'";

		String strRS = "";

		try {
			Statement st = conn.createStatement();
			ResultSet rs = st.executeQuery(sql);
			while(rs.next()){
				strRS = rs.getString("idFuncionalidade");
			}
		}catch(SQLException e) {
			e.printStackTrace();
		}
		return Integer.parseInt(strRS);
	}


	private void carregarJson(String codigo, String status) {
		bean.setNomeTabela(codigo);
		bean.setStatus(status);
	}


	public String getAll(String data) throws ParseException {
		JSONArray jsonArray = new JSONArray();
		String dataConvertida = aux.converterDataPadraoBanco(data);
		String sql = "SELECT * FROM bdheatmap.tbloghetmap where DATE_FORMAT(datahoralog, \"%Y-%m-%d\") = \""+dataConvertida+"\" ";

		try {
			Statement st = conn.createStatement();
			ResultSet rs = st.executeQuery(sql);

			while(rs.next()){
				JSONObject my_obj = new JSONObject();

				my_obj.put("idFuncionalidade",rs.getString("idFuncionalidade"));
				my_obj.put("idLog",rs.getString("idLog"));
				my_obj.put("DataHoraLog",rs.getString("DataHoraLog"));
				my_obj.put("StatusExecucao",rs.getString("StatusExecucao"));
				my_obj.put("Descricao",rs.getString("Descricao"));

				jsonArray.put(my_obj);
			}

			retorno = jsonArray.toString();
			
		} catch (SQLException | JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return retorno;
	}

	public String getAll() throws ParseException {
		JSONArray jsonArray = new JSONArray();
		String sql = "SELECT * FROM bdheatmap.tbloghetmap";

		try {
			Statement st = conn.createStatement();
			ResultSet rs = st.executeQuery(sql);

			while(rs.next()){
				JSONObject my_obj = new JSONObject();

				my_obj.put("idFuncionalidade",rs.getString("idFuncionalidade"));
				my_obj.put("idLog",rs.getString("idLog"));
				my_obj.put("DataHoraLog",rs.getString("DataHoraLog"));
				my_obj.put("StatusExecucao",rs.getString("StatusExecucao"));
				my_obj.put("Descricao",rs.getString("Descricao"));


				jsonArray.put(my_obj);
			}
			retorno = jsonArray.toString();
		} catch (SQLException | JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return retorno;
	}

}
