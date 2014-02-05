package raphapy.examples.jee6.largetransactions.activator;

import static javax.ejb.LockType.WRITE;

import java.util.concurrent.Future;

import javax.ejb.AsyncResult;
import javax.ejb.Asynchronous;
import javax.ejb.Lock;
import javax.ejb.Singleton;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;

import raphapy.examples.jee6.largetransactions.control.MyService;

/**
 * Este singleton se utiliza para gestionar la ejecucion de tareas pesadas de
 * forma segura.
 * 
 * @author Rafael E. Benegas - rafaelbenegas@gmail.com
 * 
 */
@Singleton
public class MyServiceActivator {

	@Inject
	MyService service;

	/**
	 * Este metodo se ejecuta en forma asincrona y encapsula la llamada a una
	 * tarea pesada.
	 * <p>
	 * Este metodo esta bloqueado para escritura, lo que significa que solo se
	 * puede ejecutar una vez mientras no termine.
	 * <p>
	 * Notese que su resultado esta envuelto en la interfaz {@code Future} para
	 * poder consultar el resultado de su ejecucion por mas que el metodo sea
	 * asincrono y que tiene
	 * TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED) para que no
	 * inicie una transacción sino hasta la llamada del trabajo pesado.
	 * 
	 * @see java.util.concurrent.Future
	 * @see javax.ejb.AsyncResult
	 * @see javax.ejb.Asynchronous
	 * @return
	 * @throws Exception
	 */
	@Lock(WRITE)
	@Asynchronous
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Future<String> procesarVentaMasiva() throws Exception {

		try {
			// esta rutina debería tardar
			service.hardWork();
		} catch (Throwable e) {
			return new AsyncResult<String>("ABORTADO");
		}

		return new AsyncResult<String>("CULMINADO");
	}
}
