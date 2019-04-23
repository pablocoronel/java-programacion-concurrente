package sincronizacion;

import java.util.concurrent.locks.*;

public class Banco_Sin_Sincronizar {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		// uso de la clase Banco
		Banco b = new Banco();

		// activa cada una de las 100 cuentas
		for (int i = 0; i < 100; i++) {
			// instanciacion de la clase que implementa la interface Runnable
			Ejecucion_Transferencias r = new Ejecucion_Transferencias(b, i, 2000);

			// instancia de la clase Thread que recibe un objeto Runnable
			Thread t = new Thread(r);

			// arranque del hilo
			t.start();
		}
	}

}

class Banco {
	// cuentas bancarias
	private final double[] cuentas;

	// instancia para bloquear y desbloquear codigo
	// el tipo es la interface, para que sea solamente los metodos de la interface
	private Lock cierre_banco = new ReentrantLock();

	// condicion para la espera y liberacion de un hilo
	private Condition saldo_suficiente;

	public Banco() {
		// se inicializan las 100 cuentas en cero (0.0), cada posicion del array es la
		// identificacion de la misma
		this.cuentas = new double[100];

		for (int i = 0; i < this.cuentas.length; i++) {
			// se le asigna $2000 iniciales
			this.cuentas[i] = 2000;
		}

		// asignacion de la condicion
		this.saldo_suficiente = cierre_banco.newCondition();
	}

	// transferencias bancarias
	// dentro de este método, los hilos al ejecutarse en simultaneo, se van
	// "pisando" los datos y ocaciona distintas cantidades de dinero total, ya que
	// no se ejecutan de forma ordenada, uno tras otro
	public void transferencia(int cuenta_origen, int cuenta_destino, double cantidad) throws InterruptedException {
		// bloquea el codigo que está adentro del try
		cierre_banco.lock();

		try {

			// evitar transferencias mayores a la posible
//			if (this.cuentas[cuenta_origen] < cantidad) {
//				System.out.println("--------------CANTIDAD INSUFICIENTE en cuenta: " + cuenta_origen
//						+ ".........SALDO: " + cuentas[cuenta_origen] + " ......... INTENTO TRANSFERIR: " + cantidad);
//
//				// sale del método, vuelve el flujo de ejecucion ala llamada del método
//				return;
//			} else {
//				System.out.println("-----------CANTIDAD OK---------- en cuenta: " + cuenta_origen);
//			}

			// evitar transferencias mayores a la posible
			// con while para consultar si ya tiene disponible saldo suficiente y de esa
			// forma estar en condiciones de despertar el hilo en espera
			while (this.cuentas[cuenta_origen] < cantidad) {
				this.saldo_suficiente.await();
				System.err.println(
						"HILO " + Thread.currentThread().getName() + " a la espera, en la cuenta " + cuenta_origen
								+ " con saldo actual: " + cuentas[cuenta_origen] + ", queriendo enviar: " + cantidad);
			}

			System.out.println(Thread.currentThread());

			// quita de dinero de la cuenta de origen
			this.cuentas[cuenta_origen] -= cantidad;

			System.out.printf("$%10.2f de %d transferido a la cuenta %d ", cantidad, cuenta_origen, cuenta_destino);

			// agregar el dinero transferido a la cuenta de destino
			this.cuentas[cuenta_destino] += cantidad;

			System.out.printf("Saldo total: $%10.2f \n", this.getSaldoTotal());

			// avisa a los hilos en espera que pueden volver a evaluar si despertar o seguir
			// esperando
			saldo_suficiente.signalAll();
		} finally {
			// desbloquea el codigo para el siguiente hilo, exista una excepcion o no, se
			// ejecuta igual
			cierre_banco.unlock();
		}
	}

	public double getSaldoTotal() {
		double suma_cuentas = 0;

		for (double cada_cuenta : this.cuentas) {
			suma_cuentas += cada_cuenta;
		}

		// devuelve la suma total de todas las cuentas
		return suma_cuentas;
	}
}

// clase que ejecuta las transferencias en cada hilo
// implementa la interface RUnnable
class Ejecucion_Transferencias implements Runnable {
	private Banco banco;
	private int origen;
	private double cantidad_maxima;

	public Ejecucion_Transferencias(Banco banco, int origen, double cantidad_maxima) {
		// TODO Auto-generated constructor stub
		this.banco = banco;
		this.origen = origen;
		this.cantidad_maxima = cantidad_maxima;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub

		// el try a esta altura para detener el while si salta la excepcion
		try {
			// bucle infinito
			while (true) {
				// numero aleatorio de cuenta destino
				int cuenta_destino = (int) (Math.random() * 100);

				double dinero_a_transferir = Math.random() * this.cantidad_maxima;

				// hacer la transferencia
				this.banco.transferencia(this.origen, cuenta_destino, dinero_a_transferir);

				// pausar la ejecucion del hilo
				Thread.sleep((int) Math.random() * 10);
			}
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}