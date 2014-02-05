package raphapy.examples.jee6.largetransactions.boundary;

import java.util.concurrent.Future;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import raphapy.examples.jee6.largetransactions.activator.MyServiceActivator;

/**
 * Este recurso simula la venta masiva de un item ficticio.
 * <p>
 * La idea es cambiar el estado del "subrecurso" venta-masiva para activar o
 * desactivar la ejecucion del proceso. Este estado puede ser consultado en todo
 * momento mediante GET item/venta-masiva/estado
 * 
 * @author Rafael E. Benegas - rafaelbenegas@gmail.com
 * 
 */
@Path("item")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@ApplicationScoped
public class MyResource {

	private Future<String> procesoDeVentaMasiva;

	@Inject
	MyServiceActivator serviceExecutor;

	/**
	 * Retorna una cadera que describe el estado actual del proceso de venta
	 * masiva del item.
	 * 
	 * @return
	 * @throws Exception
	 */
	@Path("venta-masiva/estado")
	@GET
	public Response consultarEstadoDeVentaMasiva() throws Exception {

		if (this.procesoDeVentaMasiva == null) {
			// el proceso asincrono jamas ha sido invocado
			return Response.status(400).entity("El proceso no se ha activado.")
					.build();
		}

		// revisamos si el proceso ha culminado o no
		if (!this.procesoDeVentaMasiva.isDone()) {
			return Response.ok("EN EJECUCION").build();
		} else {
			try {

				if (procesoDeVentaMasiva.isCancelled()) {
					return Response.ok(this.procesoDeVentaMasiva.get()).build();

				}

				return Response.ok(this.procesoDeVentaMasiva.get()).build();
			} catch (Exception e) {
				return Response.serverError().entity(e.getMessage()).build();
			}

		}
	}

	/**
	 * Cambia el estado del proceso de venta masiva del item.
	 * 
	 * @param estado
	 *            que puede ser E para ejecutarlo e I para interrumpirlo. Este
	 *            argumento va en el body de la petici&oacute;n en el atributo
	 *            {@code valor}
	 * @return
	 * @throws Exception
	 */
	@Path("venta-masiva/estado")
	@PUT
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public Response cambiarEstadoDelProcesoDeVentaMasiva(
			@FormParam("valor") String estado) throws Exception {

		if (estado == null || "".equals(estado)) {
			return Response.status(400).entity("Se requiere el estado. E | I")
					.build();
		}

		// si se solicita el estado E (Ejecutandose) para el proceso
		if ("E".equalsIgnoreCase(estado)) {

			// verificamos que el proceso no se haya activado o haya sido
			// cancelado previamente
			if (this.procesoDeVentaMasiva == null
					|| procesoDeVentaMasiva.isDone()
					|| procesoDeVentaMasiva.isCancelled()) {

				// iniciamos la ejecución en forma asíncrona
				procesoDeVentaMasiva = serviceExecutor.procesarVentaMasiva();

			}

			// si se solicita la interrupción del proceso
		} else if ("I".equalsIgnoreCase(estado)) {

			// verificamos que el proceso esté activo
			if (this.procesoDeVentaMasiva != null) {
				procesoDeVentaMasiva.cancel(true);
			}

		} else {
			return Response.status(406).entity("Estado no valido").build();
		}

		// retornamos al cliente
		return Response.ok().build();

	}
}
