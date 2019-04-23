package sincronizacion;

public class Banco_Sin_Sincronizar {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}

class Banco {
	// cuentas bancarias
	private final double[] cuentas;

	public Banco() {
		// se inicializan las 100 cuantas en cero (0.0), cada posicion del array es la
		// identificacion de la misma
		this.cuentas = new double[100];

		for (int i = 0; i < this.cuentas.length; i++) {
			// se le asigna $2000 iniciales
			this.cuentas[i] = 2000;
		}
	}

	// transferencias bancarias
	public void transferencia(int cuenta_origen, int cuenta_destino, double cantidad) {
		// evitar transferencias mayores a la posible
		if (this.cuentas[cuenta_origen] < cantidad) {
			// sale
			return;
		}

		System.out.println(Thread.currentThread());

		// quita de dinero de la cuenta de origen
		this.cuentas[cuenta_origen] -= cantidad;

		System.out.printf("$%10.2f de %d transferido a la cuenta %d", cantidad, cuenta_origen, cuenta_destino);

		// agregar el dinero transferido a la cuenta de destino
		this.cuentas[cuenta_destino] += cantidad;

		System.out.printf("Saldo total: $%10.2f \n", this.getSaldoTotal());
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