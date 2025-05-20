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
     * Constructor que genera una baraja estándar de 52 cartas.
     */
    public Baraja() {
        cartas = new ArrayList<>();
        String[] figuras = {"Corazones", "Diamantes", "Tréboles", "Picas"};
        boolean bocaArriba = false;

        for (String figura : figuras) {
            String color = (figura.equals("Corazones") || figura.equals("Diamantes")) ? "Rojo" : "Negro";

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
     */
    public Carta sacarCarta() {
        if (!cartas.isEmpty()) {
            return cartas.remove(0);
        }
        return null;
    }

    /**
     * Devuelve el número de cartas restantes en la baraja.
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
        Baraja baraja = new Baraja();
        baraja.mostrarBaraja();

        System.out.println("Mezclando las cartas...");
        baraja.barajar();
        baraja.mostrarBaraja();

        System.out.println("Sacando una carta: " + baraja.sacarCarta());
        System.out.println("Cartas restantes: " + baraja.cartasRestantes());
    }
}
