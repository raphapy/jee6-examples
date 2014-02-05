package raphapy.examples.jee6.largetransactions.aspect.impl;

import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;
import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;
import javax.transaction.Status;
import javax.transaction.UserTransaction;

import raphapy.examples.jee6.largetransactions.aspect.AutonomousTransaction;

/**
 * Interceptor asociado al interceptor binding {@code AutonomousTransaction}.
 * <p>
 * Se encarga de iniciar una transacción nueva, seteando un tiempo de vida para
 * la misma. Y también de confirmarla o abortarla dependiendo del resultado de
 * ejecución del método interceptado.
 * 
 * @see raphapy.examples.jee6.largetransactions.aspect.AutonomousTransaction
 * @author Rafael E. Benegas - rafaelbenegas@gmail.com
 * 
 */
@Interceptor
@AutonomousTransaction
public class AutonomousTransactionInterceptor {

	@Inject
	UserTransaction tx;

	@AroundInvoke
	public Object doInterception(InvocationContext context) throws Throwable {

		if (Status.STATUS_NO_TRANSACTION != tx.getStatus()) {
			throw new IllegalStateException(
					"No debe existir alguna transacción");
		}

		TransactionManagement txMngtAnnotation = context.getTarget().getClass()
				.getAnnotation(TransactionManagement.class);

		if (txMngtAnnotation == null
				|| !TransactionManagementType.BEAN.equals(txMngtAnnotation
						.value())) {
			throw new IllegalStateException(
					"TransactionManagement debe ser de tipo TransactionManagementType.BEAN");
		}

		Object result = null;
		try {

			final int seconds = context.getMethod()
					.getAnnotation(AutonomousTransaction.class).timeOut();
			tx.setTransactionTimeout(seconds);
			tx.begin();
			result = context.proceed();
			tx.commit();

		} catch (Throwable e) {

			// rollback solo si existe una transaccion
			if (Status.STATUS_NO_TRANSACTION != tx.getStatus()) {
				tx.rollback();
			}
			throw e;

		}

		return result;
	}
}
