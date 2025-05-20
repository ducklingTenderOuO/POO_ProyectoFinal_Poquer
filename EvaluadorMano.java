import java.util.*;

public class EvaluadorMano {

    public static Jugada evaluarMejorMano(List<Carta> mano, List<Carta> mesa) {
        List<Carta> cartas = new ArrayList<>(mano);
        cartas.addAll(mesa);
        cartas.sort((a, b) -> Integer.compare(b.getValor(), a.getValor())); // Orden descendente

        if (esEscaleraReal(cartas)) {
            return new Jugada(TipoJugada.ESCALERA_REAL, obtenerColor(cartas, 5));
        } else if (esEscaleraColor(cartas)) {
            return new Jugada(TipoJugada.ESCALERA_COLOR, obtenerEscaleraColor(cartas));
        } else if (esPoker(cartas)) {
            return new Jugada(TipoJugada.POKER, obtenerPorValor(cartas, 4));
        } else if (esFullHouse(cartas)) {
            return new Jugada(TipoJugada.FULL_HOUSE, obtenerFullHouse(cartas));
        } else if (esColor(cartas)) {
            return new Jugada(TipoJugada.COLOR, obtenerColor(cartas, 5));
        } else if (esEscalera(cartas)) {
            return new Jugada(TipoJugada.ESCALERA, obtenerEscalera(cartas));
        } else if (esTrio(cartas)) {
            return new Jugada(TipoJugada.TRIO, obtenerPorValor(cartas, 3));
        } else if (esDoblePareja(cartas)) {
            return new Jugada(TipoJugada.DOBLE_PAREJA, obtenerDoblePareja(cartas));
        } else if (esPareja(cartas)) {
            return new Jugada(TipoJugada.PAREJA, obtenerPorValor(cartas, 2));
        } else {
            return new Jugada(TipoJugada.CARTA_ALTA, cartas.subList(0, 5));
        }
    }



    private static boolean esColor(List<Carta> cartas) {
        return obtenerColor(cartas, 5) != null;
    }

    private static boolean esTrio(List<Carta> cartas) {
        return contarPorValor(cartas, 3) >= 1;
    }

    private static boolean esPareja(List<Carta> cartas) {
        return contarPorValor(cartas, 2) >= 1;
    }

    private static boolean esDoblePareja(List<Carta> cartas) {
        return contarPorValor(cartas, 2) >= 2;
    }

    private static boolean esPoker(List<Carta> cartas) {
        return contarPorValor(cartas, 4) >= 1;
    }

    private static boolean esFullHouse(List<Carta> cartas) {
        return contarPorValor(cartas, 3) >= 1 && contarPorValor(cartas, 2) >= 1;
    }

    private static boolean esEscalera(List<Carta> cartas) {
        return obtenerEscalera(cartas) != null;
    }

    private static boolean esEscaleraColor(List<Carta> cartas) {
        return obtenerEscaleraColor(cartas) != null;
    }

    private static boolean esEscaleraReal(List<Carta> cartas) {
        List<Carta> escColor = obtenerEscaleraColor(cartas);
        if (escColor == null) return false;
        return escColor.get(0).getValor() == 13 &&
                escColor.get(1).getValor() == 12 &&
                escColor.get(2).getValor() == 11 &&
                escColor.get(3).getValor() == 10 &&
                escColor.get(4).getValor() == 1;
    }

    //Métodos para obtener combinaciones

    private static int contarPorValor(List<Carta> cartas, int cantidad) {
        Map<Integer, Integer> conteo = new HashMap<>();
        for (Carta c : cartas) {
            conteo.put(c.getValor(), conteo.getOrDefault(c.getValor(), 0) + 1);
        }
        int total = 0;
        for (int val : conteo.values()) {
            if (val >= cantidad) total++;
        }
        return total;
    }

    private static List<Carta> obtenerPorValor(List<Carta> cartas, int cantidad) {
        Map<Integer, List<Carta>> mapa = new HashMap<>();
        for (Carta c : cartas) {
            mapa.computeIfAbsent(c.getValor(), k -> new ArrayList<>()).add(c);
        }
        for (List<Carta> grupo : mapa.values()) {
            if (grupo.size() == cantidad) return grupo;
        }
        return null;
    }

    private static List<Carta> obtenerFullHouse(List<Carta> cartas) {
        List<Carta> trio = obtenerPorValor(cartas, 3);
        List<Carta> resto = new ArrayList<>(cartas);
        if (trio != null) resto.removeAll(trio);
        List<Carta> pareja = obtenerPorValor(resto, 2);
        if (trio != null && pareja != null) {
            List<Carta> full = new ArrayList<>();
            full.addAll(trio);
            full.addAll(pareja);
            return full;
        }
        return null;
    }

    private static List<Carta> obtenerColor(List<Carta> cartas, int cantidad) {
        Map<String, List<Carta>> figuras = new HashMap<>();
        for (Carta c : cartas) {
            figuras.computeIfAbsent(c.getFigura(), k -> new ArrayList<>()).add(c);
        }
        for (List<Carta> lista : figuras.values()) {
            if (lista.size() >= cantidad) {
                lista.sort((a, b) -> Integer.compare(b.getValor(), a.getValor()));
                return lista.subList(0, cantidad);
            }
        }
        return null;
    }

    private static List<Carta> obtenerEscalera(List<Carta> cartas) {
        Set<Integer> valores = new HashSet<>();
        for (Carta c : cartas) valores.add(c.getValor());
        List<Integer> lista = new ArrayList<>(valores);
        lista.sort(Collections.reverseOrder());

        // Soporte para As como 1 o 14
        if (valores.contains(13)) lista.add(0, 14);

        for (int i = 0; i <= lista.size() - 5; i++) {
            boolean escalera = true;
            for (int j = 0; j < 4; j++) {
                if (lista.get(i + j) - 1 != lista.get(i + j + 1)) {
                    escalera = false;
                    break;
                }
            }
            if (escalera) {
                List<Carta> resultado = new ArrayList<>();
                for (int k = 0; k < 5; k++) {
                    final int val = lista.get(i + k) == 14 ? 1 : lista.get(i + k);
                    for (Carta c : cartas) {
                        if (c.getValor() == val && !resultado.contains(c)) {
                            resultado.add(c);
                            break;
                        }
                    }
                }
                return resultado;
            }
        }
        return null;
    }

    private static List<Carta> obtenerEscaleraColor(List<Carta> cartas) {
        Map<String, List<Carta>> porFigura = new HashMap<>();
        for (Carta c : cartas) {
            porFigura.computeIfAbsent(c.getFigura(), k -> new ArrayList<>()).add(c);
        }
        for (List<Carta> grupo : porFigura.values()) {
            if (grupo.size() >= 5) {
                List<Carta> esc = obtenerEscalera(grupo);
                if (esc != null) return esc;
            }
        }
        return null;
    }

    private static List<Carta> obtenerDoblePareja(List<Carta> cartas) {
        Map<Integer, List<Carta>> mapa = new HashMap<>();
        for (Carta c : cartas) {
            mapa.computeIfAbsent(c.getValor(), k -> new ArrayList<>()).add(c);
        }
        List<List<Carta>> parejas = new ArrayList<>();
        for (List<Carta> grupo : mapa.values()) {
            if (grupo.size() >= 2) parejas.add(grupo.subList(0, 2));
        }
        if (parejas.size() >= 2) {
            List<Carta> doblePareja = new ArrayList<>();
            doblePareja.addAll(parejas.get(0));
            doblePareja.addAll(parejas.get(1));
            return doblePareja;
        }
        return null;
    }
}
