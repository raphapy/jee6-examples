jee6-examples
=============

Ejemplos de soluciones utilizando Java EE6

* **performing-large-transactions:**
<br>Solución para invocar asíncronamente procesos pesados que requieran transacciones largas.
<br>**Problema:** Timeout manejado por el contenedor.(Ej: “ARJUNA012117: TransactionReaper::check timeout en Jboss AS 7)
<br>**Patrones de diseño aplicados:** Variante de "Service Activator Pattern"(Utilizando Singleton en vez de un Message Listener), futures and promises y half-sync/half-async
