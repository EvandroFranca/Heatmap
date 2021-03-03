package api;

import java.text.ParseException;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.json.JSONException;

import dao.CrudHeatmap;

@Path("/")
public class Heatmap {

	public static void main(String[] args) throws JSONException, ParseException {
		Heatmap h = new Heatmap();
		CrudHeatmap crud = new CrudHeatmap();
	}

	@GET
	@Path("/RecuperarTabela3/{data}&{app}")
	@Produces("application/json; charset=UTF-8")
	public Response recuperarTabela3(@PathParam("data") String data,@PathParam("app") String app) throws JSONException, ParseException {
		//System.out.println("Com app");	
		CrudHeatmap cHeatmap = new CrudHeatmap();
		
		String retorno = cHeatmap.recuperarTabela(data, app);
		
		return Response.status(200).entity(retorno).build();
	}	
	
	@GET
	@Path("/RecuperarTabela/{data}")
	@Produces("application/json; charset=UTF-8")
	public Response recuperarTabela(@PathParam("data") String data) throws JSONException, ParseException {
		//System.out.println("Sem app");	
		CrudHeatmap cHeatmap = new CrudHeatmap();
		String app = "APP_OLD";
		
		String retorno = cHeatmap.recuperarTabela(data, app);
		
		return Response.status(200).entity(retorno).build();
	}
	
	@GET
	@Path("/SalvarTabela/{funcionalidade}&{status}&{descricao}")
	@Produces(MediaType.TEXT_PLAIN)
	public Response salvarTabela(@PathParam("funcionalidade") String id, @PathParam("status") String status, @PathParam("descricao") String descricao) throws JSONException {
		
		CrudHeatmap cHeatmap = new CrudHeatmap();
		
		String retorno = cHeatmap.salvarTabela(id, status, descricao);
		
		return Response.status(200).entity(retorno).build();
	}
	
	@GET
	@Path("/SalvarTabela/{funcionalidade}&{status}")
	@Produces(MediaType.TEXT_PLAIN)
	public Response salvarTabela(@PathParam("funcionalidade") String id, @PathParam("status") String status) throws JSONException {
		
		CrudHeatmap cHeatmap = new CrudHeatmap();
		
		String retorno = cHeatmap.salvarTabela(id, status, "");
		
		return Response.status(200).entity(retorno).build();
	}
	
	@GET
	@Path("/GetAll/")
	@Produces("application/json; charset=UTF-8")
	public Response getAll() throws JSONException, ParseException {
		
		CrudHeatmap cHeatmap = new CrudHeatmap();
		
		String retorno = cHeatmap.getAll();
		
		return Response.status(200).entity(retorno).build();
	}
	
	
//	@GET
//	@Path("/RecuperarTabela2/{data}")
//	@Produces("application/json; charset=UTF-8")
//	public Response getAllTabela(@PathParam("data") String data) throws JSONException, ParseException {
//		
//		CrudHeatmap cHeatmap = new CrudHeatmap();
//		
//		String retorno = cHeatmap.getAll(data);
//		
//		return Response.status(200).entity(retorno).build();
//	}
	
	@GET
	@Path("/ConsultaNovoApp/{data}")
	@Produces("application/json; charset=UTF-8")
	public Response consultaNovoApp(@PathParam("data") String data) throws JSONException, ParseException {
		//System.out.println("Novo app");	
		CrudHeatmap cHeatmap = new CrudHeatmap();
		String app = "APP_NEW";
		
		String retorno = cHeatmap.recuperarTabela(data, app);
		
		return Response.status(200).entity(retorno).build();
	}	
	
	@GET
	@Path("/ConsultaNovoApp/{data}&{app}")
	@Produces("application/json; charset=UTF-8")
	public Response consultaNovoApp(@PathParam("data") String data, @PathParam("app") String app) throws JSONException, ParseException {
		//System.out.println("sobrecarga");	
		CrudHeatmap cHeatmap = new CrudHeatmap();
		
		String retorno = cHeatmap.recuperarTabela(data, app);
		
		return Response.status(200).entity(retorno).build();
	}	
	
	@GET
	@Path("/ConsultaNovoAppAll/{data}")
	@Produces("application/json; charset=UTF-8")
	public Response consultaNovoAppAll(@PathParam("data") String data) throws JSONException, ParseException {
		//System.out.println("sobrecarga");	
		
		CrudHeatmap cHeatmap = new CrudHeatmap();
		String app = "ALL";
		String retorno = cHeatmap.recuperarTabela(data, app);
		
		return Response.status(200).entity(retorno).build();
	}	
}
