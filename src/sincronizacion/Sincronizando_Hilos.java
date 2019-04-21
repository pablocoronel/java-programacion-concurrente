package sincronizacion;

public class Sincronizando_Hilos {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		Hilos_Varios hilo_1 = new Hilos_Varios();
		hilo_1.start();

		// el hilo_1 se ejecuta hasta su muerte
		try {
			hilo_1.join();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		Hilos_Varios hilo_2 = new Hilos_Varios();
		hilo_2.start();

		try {
			hilo_2.join();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

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