package raphapy.examples.jee6.largetransactions.aspect;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import javax.enterprise.util.Nonbinding;
import javax.interceptor.InterceptorBinding;

/**
 * Este interceptor binding se debe utilizar cuando se requiere iniciar una
 * transacción autónoma al invocar el método marcado con esta anotación.
 * 
 * Se requiere que, el EJB que contenga al método esté marcado con
 * {@code TransactionManagement(TransactionManagementType.BEAN)} y que no exista
 * una transacción activa. En caso contrario se recibirá una excepción en tiempo
 * ed ejecución.
 * 
 * La transacción se confirmará cuando el método marcado haya finalizado sin
 * propagar excepción alguna. En caso contrario la transacción es abortada.
 * 
 * @author Rafael E. Benegas - rafaelbenegas@gmail.com
 * @see raphapy.commons.jee6.aspect.impl.AutonomousTransactionInterceptor
 */
@Inherited
@InterceptorBinding
@Target({ METHOD, TYPE })
@Retention(RUNTIME)
public @interface AutonomousTransaction {
	/**
	 * Este atributo especifica la cantidad de segundos que durará la
	 * transacción a iniciarse. Su valor por defecto es {@code 0}, lo cual
	 * indica que se utilizará la configuración por defecto del contenedor de
	 * EJB
	 */
	@Nonbinding
	int timeOut() default 0;
}
