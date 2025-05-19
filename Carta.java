/**
 * Representa una carta individual con valor, figura, color y estado (boca arriba/abajo).
 * @author (AYJB - LGLM)
 * @version (MAY 2025)
 */

package proyectofinal; 

public class Carta {
    private final int valor;
    private final String figura;
    private final String color;
    private boolean bocaArriba;

    public Carta(int valor, String figura, String color, boolean bocaArriba) {
        this.valor = valor;
        this.figura = figura;
        this.color = color;
        this.bocaArriba = bocaArriba;
    }

    public int getValor() { return valor; }
    public String getFigura() { return figura; }
    public String getColor() { return color; }
    public boolean isBocaArriba() { return bocaArriba; }

    public boolean esIgualA(Carta otraCarta) {
        return otraCarta != null && 
               valor == otraCarta.getValor() &&
               figura.equals(otraCarta.getFigura()) &&
               color.equals(otraCarta.getColor());
    }

    public boolean tieneElMismoValor(Carta otraCarta) {
        return otraCarta != null && valor == otraCarta.getValor();
    }

    public boolean tieneLaMismaFigura(Carta otraCarta) {
        return otraCarta != null && figura.equals(otraCarta.getFigura());
    }

    @Override
    public String toString() {
        String[] nombres = {"As", "2", "3", "4", "5", "6", "7", "8", "9", "10", "J", "Q", "K"};
        String valorStr = (valor >= 1 && valor <= 13) ? nombres[valor - 1] : "?";
        return valorStr + " de " + figura + " (" + color + ")" + (bocaArriba ? " ↑" : " ↓");
    }
}
