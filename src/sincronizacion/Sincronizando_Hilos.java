package sincronizacion;

public class Sincronizando_Hilos {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		Hilos_Varios hilo_1 = new Hilos_Varios();

		// el parametro es el hilo que tiene que terminar para que empiece la ejecucion
		Hilos_Varios_2 hilo_2 = new Hilos_Varios_2(hilo_1);

		hilo_2.start(); // no empieza hasta que hilo_1 haya terminado
		hilo_1.start();

		// el hilo_1 se ejecuta hasta su muerte
//		try {
//			hilo_1.join();
//		} catch (InterruptedException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//
//		Hilos_Varios hilo_2 = new Hilos_Varios();
//		hilo_2.start();
//
//		try {
//			hilo_2.join();
//		} catch (InterruptedException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}

		System.out.println("Terminadas las tareas");
	}

}

class Hilos_Varios extends Thread {

	public void run() {
		for (int i = 0; i < 15; i++) {
			// getName() devuelve el nombre del hilo actual
			System.out.println("Ejecutando hilo " + this.getName());

			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}

class Hilos_Varios_2 extends Thread {
	private Thread hilo;

	public Hilos_Varios_2(Thread hilo) {
		this.hilo = hilo;
	}

	public void run() {
		// comenzar despues del hilo pasado por parametro:
		try {
			this.hilo.join();
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		for (int i = 0; i < 15; i++) {
			// getName() devuelve el nombre del hilo actual
			System.out.println("Ejecutando hilo " + this.getName());

			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}