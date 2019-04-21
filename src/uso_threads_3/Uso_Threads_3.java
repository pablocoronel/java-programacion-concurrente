package uso_threads_3;

import java.awt.geom.*;

import javax.swing.*;

import java.util.*;
import java.awt.*;
import java.awt.event.*;

public class Uso_Threads_3 {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		JFrame marco = new MarcoRebote();

		marco.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		marco.setVisible(true);

	}

}

/**
 * 1er paso: CLASE QUE IMPLEMENTA LA INTERFACE RUNNABLE
 * 
 * 2do paso: En el metodo run() se coloca el codigo de la tarea que queremos que
 * se realice de forma simultanea con otras cosas
 * 
 */
class Pelota_Hilos implements Runnable {
	private Pelota pelota;
	private Component componente;

	public Pelota_Hilos(Pelota una_pelota, Component un_componente) {
		// TODO Auto-generated constructor stub
		this.pelota = una_pelota;
		this.componente = un_componente;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub

		System.out.println("Estado del hilo al comenzar: " + Thread.currentThread().isInterrupted());

		// for (int i = 1; i <= 3000; i++) {

		// para que se ejecute siempre, mientras no se interrumpa el hilo
		while (!Thread.interrupted()) {

			// tambien se ejecuta mientras no esté interrumpido, pero se consulta sobre el
			// hilo actual
//			while(! Thread.currentThread().isInterrupted()) {
//				
//			}
			this.pelota.mueve_pelota(this.componente.getBounds());

			this.componente.paint(this.componente.getGraphics());

			try {
				Thread.sleep(4);
			} catch (InterruptedException e) {
				// cuando sale la excepcion por el sleep(), se interrumpe el hilo
				Thread.currentThread().interrupt();
			}

		}

		System.out.println("Estado del hilo al terminar: " + Thread.currentThread().isInterrupted());
	}

}

//Movimiento de la pelota-----------------------------------------------------------------------------------------

class Pelota {

	// Mueve la pelota invirtiendo posición si choca con límites

	public void mueve_pelota(Rectangle2D limites) {

		x += dx;

		y += dy;

		if (x < limites.getMinX()) {

			x = limites.getMinX();

			dx = -dx;
		}

		if (x + TAMX >= limites.getMaxX()) {

			x = limites.getMaxX() - TAMX;

			dx = -dx;
		}

		if (y < limites.getMinY()) {

			y = limites.getMinY();

			dy = -dy;
		}

		if (y + TAMY >= limites.getMaxY()) {

			y = limites.getMaxY() - TAMY;

			dy = -dy;

		}

	}

	// Forma de la pelota en su posición inicial

	public Ellipse2D getShape() {

		return new Ellipse2D.Double(x, y, TAMX, TAMY);

	}

	private static final int TAMX = 15;

	private static final int TAMY = 15;

	private double x = 0;

	private double y = 0;

	private double dx = 1;

	private double dy = 1;

}

// Lámina que dibuja las pelotas----------------------------------------------------------------------

class LaminaPelota extends JPanel {

	// Añadimos pelota a la lámina

	public void add(Pelota b) {

		pelotas.add(b);
	}

	public void paintComponent(Graphics g) {

		super.paintComponent(g);

		Graphics2D g2 = (Graphics2D) g;

		for (Pelota b : pelotas) {

			g2.fill(b.getShape());
		}

	}

	private ArrayList<Pelota> pelotas = new ArrayList<Pelota>();
}

//Marco con lámina y botones------------------------------------------------------------------------------

class MarcoRebote extends JFrame {
	private LaminaPelota lamina;

	// se crean 3 hilos independientes
	private Thread t1, t2, t3;

	// botones independientes para cada hilo
	JButton arranca_1, arranca_2, arranca_3, detener_1, detener_2, detener_3;

	public MarcoRebote() {

		setBounds(600, 300, 600, 350);

		setTitle("Rebotes");

		lamina = new LaminaPelota();

		add(lamina, BorderLayout.CENTER);

		JPanel laminaBotones = new JPanel();

		// instancia de cada boton
		arranca_1 = new JButton("Hilo 1");
		arranca_1.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				comienza_el_juego(e);
			}
		});

		laminaBotones.add(arranca_1);

		// boton de hilo 2
		arranca_2 = new JButton("Hilo 2");
		arranca_2.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				comienza_el_juego(e);
			}
		});

		laminaBotones.add(arranca_2);

		// boton de hilo 3
		arranca_3 = new JButton("Hilo 3");
		arranca_3.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				comienza_el_juego(e);
			}
		});

		laminaBotones.add(arranca_3);

		/********************************************************/
		// botones de detener
		// instancia de cada boton
		detener_1 = new JButton("detener 1");
		detener_1.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				detener(e);
			}
		});

		laminaBotones.add(detener_1);

		// boton de hilo 2
		detener_2 = new JButton("detener 2");
		detener_2.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				detener(e);
			}
		});

		laminaBotones.add(detener_2);

		// boton de hilo 3
		detener_3 = new JButton("detener 3");
		detener_3.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				detener(e);
			}
		});

		laminaBotones.add(detener_3);

		/******************************/

		add(laminaBotones, BorderLayout.SOUTH);
	}

	// Ponemos botones

	// Añade pelota y la bota 1000 veces

	public void comienza_el_juego(ActionEvent e) {

		Pelota pelota = new Pelota();

		lamina.add(pelota);

		/**
		 * 3er paso: Se instancia la clase creada y se almacena en una variable de tipo
		 * Runnable
		 */

		Runnable r = new Pelota_Hilos(pelota, lamina);

		/**
		 * 4to paso: crear instancia de la clase Thread pasando como parametro al
		 * constructor de Thread el objeto Runnable del paso 3
		 */

		if (e.getSource().equals(arranca_1)) {
			t1 = new Thread(r);

			/**
			 * 5to y ultimo paso: Poner en marcha el hilo de ejecucion con el metodo start()
			 * de la clase Thread
			 */
			t1.start();
		} else if (e.getSource().equals(arranca_2)) {
			t2 = new Thread(r);

			/**
			 * 5to y ultimo paso: Poner en marcha el hilo de ejecucion con el metodo start()
			 * de la clase Thread
			 */
			t2.start();
		} else if (e.getSource().equals(arranca_3)) {
			t3 = new Thread(r);

			/**
			 * 5to y ultimo paso: Poner en marcha el hilo de ejecucion con el metodo start()
			 * de la clase Thread
			 */
			t3.start();
		}

	}

	// boton nuevo, para detener
	public void detener(ActionEvent e) {

		if (e.getSource().equals(detener_1)) {
			t1.interrupt();
		} else if (e.getSource().equals(detener_2)) {
			t2.interrupt();
		} else if (e.getSource().equals(detener_3)) {
			t3.interrupt();
		}
	}

}
