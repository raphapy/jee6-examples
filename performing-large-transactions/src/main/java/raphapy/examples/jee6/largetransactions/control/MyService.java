package raphapy.examples.jee6.largetransactions.control;

import javax.ejb.Stateless;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;

import raphapy.examples.jee6.largetransactions.aspect.AutonomousTransaction;

/**
 * Servicio que contiene el método que requiere una transacción de larga
 * duración. Utilizamos AutonomousTransaction para poder setear la duración
 * máxima de la transacción y anotamos el session bean con
 * TransactionManagement(TransactionManagementType.BEAN) para que se pueda
 * aplicar la configuración.
 * 
 * @author Rafael E. Benegas - rafaelbenegas@gmail.com
 * @see javax.ejb.TransactionManagement
 * @see javax.ejb.TransactionManagementType
 */
@Stateless
@TransactionManagement(TransactionManagementType.BEAN)
public class MyService {

	/**
	 * Este método, se supone, realiza una tarea costosa y requiere una
	 * transacción de larga duración. Por ello va anotado con
	 * AutonomousTransaction.
	 * <p>
	 * Para este caso particular le seteamos 30 minutos(unos 1800 segundos) de
	 * tiempo de espera a la transacción.
	 * 
	 * @throws Exception
	 * 
	 * @see raphapy.commons.jee6.aspect.AutonomousTransaction
	 */
	@AutonomousTransaction(timeOut = 1800)
	public void hardWork() throws Exception {

		//TODO
		//Agrega aqui tu tarea duradera y pesada
	}
}
