/**
 * Write a description of class Baraja here.
 *
 * @author (AYJB - LGLM)
 * @version (MAY 2025)
 */

package proyectofinal;
import java.util.ArrayList;
import java.util.Collections;

/**
 * Clase que representa una baraja de cartas.
 */
public class Baraja {
    private ArrayList<Carta> cartas;

    /**
     * Clase interna que representa una carta individual.
     */
    private class Carta {
        private int valor;
        private String figura;
        private String color;
        private boolean bocaArriba;

        // Constructor
        public Carta(int valor, String figura, String color, boolean bocaArriba) {
            this.valor = valor;
            this.figura = figura;
            this.color = color;
            this.bocaArriba = false;
        }

        // Método para obtener la representación textual de la carta
        @Override
        public String toString() {
            String[] valores = {"As", "2", "3", "4", "5", "6", "7", "8", "9", "10", "J", "Q", "K"};
            return valores[valor - 1] + " de " + figura + " (" + color + ")";
        }
    }

    /**
     * Constructor que genera una baraja estándar de 52 cartas.
     */
    public Baraja() {
        cartas = new ArrayList<>();
        String[] figuras = {"Corazones", "Diamantes", "Tréboles", "Picas"};
        String color;
        boolean bocaArriba = false;

        for (String figura : figuras) {
            color = (figura.equals("Corazones") || figura.equals("Diamantes")) ? "Rojo" : "Negro";

            for (int valor = 1; valor <= 13; valor++) {
                cartas.add(new Carta(valor, figura, color, bocaArriba));
            }
        }
    }

    /**
     * Mezcla las cartas de la baraja.
     */
    public void barajar() {
        Collections.shuffle(cartas);
    }

    /**
     * Extrae una carta del tope de la baraja.
     * @return 
     */
    public Carta sacarCarta() {
        if (!cartas.isEmpty()) {
            return cartas.remove(0);
        } else {
            return null; // Baraja vacía
        }
    }

    /**
     * Devuelve el número de cartas restantes en la baraja.
     * @return 
     */
    public int cartasRestantes() {
        return cartas.size();
    }

    /**
     * Muestra todas las cartas en la baraja.
     */
    public void mostrarBaraja() {
        for (Carta c : cartas) {
            System.out.println(c);
        }
    }

    public static void main(String[] args) {
        // Aquí empieza el código de Main, donde se ejecuta el programa
        Baraja baraja = new Baraja();
        baraja.mostrarBaraja(); // Muestra todas las cartas

        System.out.println("Mezclando las cartas...");
        baraja.barajar();
        baraja.mostrarBaraja(); // Muestra las cartas después de barajar

        System.out.println("Sacando una carta: " + baraja.sacarCarta());
        System.out.println("Cartas restantes: " + baraja.cartasRestantes());
    }
}
