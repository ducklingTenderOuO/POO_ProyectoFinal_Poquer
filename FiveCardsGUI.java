package proyectofinal;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.List;
import java.net.URL;

public class FiveCardsGUI extends JFrame {

    private List<Jugador> jugadores;
    private Bote bote;
    private Baraja baraja;

    private Jugador jugadorActual;
    private int indiceJugadorActual;

    private JTextArea infoArea = new JTextArea(10, 30);
    private JPanel cartasPanel = new JPanel();
    private List<JToggleButton> botonesCartas = new ArrayList<>();

    private JButton btnPasar = new JButton("Pasar");
    private JButton btnApostar = new JButton("Apostar");
    private JButton btnIgualar = new JButton("Igualar");
    private JButton btnSubir = new JButton("Subir");
    private JButton btnRetirarse = new JButton("Retirarse");
    private JButton btnConfirmarDescartar = new JButton("Confirmar Descarte");

    private JTextField txtApuesta = new JTextField(5);

    private enum EstadoRonda {APUESTA, DESCARTE, ENFRENTAMIENTO, FIN, ESPERANDO}
    private EstadoRonda estado;

    private int apuestaMinima = 10;
    private int apuestaActual = 0;
    private int jugadoresQueIgualaron = 0;
    private int jugadoresActivos;

    public FiveCardsGUI(List<Jugador> jugadores) {
        this.jugadores = jugadores;
        this.baraja = new Baraja();
        this.bote = new Bote();
        this.estado = EstadoRonda.ESPERANDO;

        initGUI();
    }

    private void initGUI() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setVisible(true);

        infoArea.setEditable(false);
        JScrollPane scroll = new JScrollPane(infoArea);
        add(scroll, BorderLayout.EAST);

        cartasPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 15, 10));  // centrado con margen
        add(cartasPanel, BorderLayout.CENTER);

        JPanel panelAcciones = new JPanel();
        panelAcciones.add(new JLabel("Apuesta:"));
        panelAcciones.add(txtApuesta);
        panelAcciones.add(btnPasar);
        panelAcciones.add(btnApostar);
        panelAcciones.add(btnIgualar);
        panelAcciones.add(btnSubir);
        panelAcciones.add(btnRetirarse);
        panelAcciones.add(btnConfirmarDescartar);

        add(panelAcciones, BorderLayout.SOUTH);

        btnPasar.addActionListener(e -> accionPasar());
        btnApostar.addActionListener(e -> accionApostar());
        btnIgualar.addActionListener(e -> accionIgualar());
        btnSubir.addActionListener(e -> accionSubir());
        btnRetirarse.addActionListener(e -> accionRetirarse());
        btnConfirmarDescartar.addActionListener(e -> accionConfirmarDescartar());
    }
    
    public void iniciarJuego() {
        setVisible(true);
        empezarRonda();
    }

    private void empezarRonda() {
        // Repartir cartas a todos
        for (Jugador j : jugadores) {
            j.reiniciarEstado();  // limpia mano y activa jugador
        }

        baraja.barajar();
        for (Jugador j : jugadores) {
            for (int i = 0; i < 5; i++) {
                Carta c = baraja.sacarCarta();
                if (c != null) {
                    j.recibirCarta(c);
                }
            }
        }

        bote.limpiar();
        apuestaActual = 0;
        apuestaMinima = 10;
        jugadoresActivos = jugadores.size();
        jugadoresQueIgualaron = 0;

        estado = EstadoRonda.APUESTA;
        indiceJugadorActual = 0;
        jugadorActual = jugadores.get(indiceJugadorActual);

        infoArea.setText("");
        infoArea.append("¡Nueva ronda! Comienza la apuesta.\n");
        actualizarGUI();
    }

    private void actualizarGUI() {
        cartasPanel.removeAll();
        botonesCartas.clear();

        infoArea.append("\nTurno: " + jugadorActual.getNombre() + " | Fichas: " + jugadorActual.getFichas() + " | Bote: " + bote.getCantidad() + "\n");
        
        // Mostrar cartas con toggles solo en fase de descarte
        List<Carta> mano = jugadorActual.getMano();
        cartasPanel.removeAll();  // Limpia el panel antes de agregar nuevos botones
        botonesCartas.clear();

        for (Carta c : mano) {
            java.net.URL imagenURL = getClass().getResource(c.getRutaImagen()); // <-- declaración aquí mismo
            if (imagenURL == null) {
                System.err.println("No se encontró la imagen: " + c.getRutaImagen());
                continue;
            }

            ImageIcon iconoOriginal = new ImageIcon(imagenURL);
            Image imagenEscalada = iconoOriginal.getImage().getScaledInstance(200, 250, Image.SCALE_SMOOTH);
            ImageIcon iconoEscalado = new ImageIcon(imagenEscalada);

            JToggleButton tbtn = new JToggleButton(iconoEscalado);
            tbtn.setPreferredSize(new Dimension(200, 250));
            tbtn.setEnabled(estado == EstadoRonda.DESCARTE);
            botonesCartas.add(tbtn);
            cartasPanel.add(tbtn);
        }

        cartasPanel.revalidate();
        cartasPanel.repaint();

                // Controlar botones según estado
                switch (estado) {
                    case APUESTA:
                        btnConfirmarDescartar.setEnabled(false);
                        btnPasar.setEnabled(apuestaActual == 0);
                        btnApostar.setEnabled(apuestaActual == 0);
                        btnIgualar.setEnabled(apuestaActual > 0);
                        btnSubir.setEnabled(apuestaActual > 0);
                        btnRetirarse.setEnabled(apuestaActual > 0);
                        txtApuesta.setEnabled(true);
                        break;

                    case DESCARTE:
                        btnConfirmarDescartar.setEnabled(true);
                        btnPasar.setEnabled(false);
                        btnApostar.setEnabled(false);
                        btnIgualar.setEnabled(false);
                        btnSubir.setEnabled(false);
                        btnRetirarse.setEnabled(false);
                        txtApuesta.setEnabled(false);
                        break;

                    case ENFRENTAMIENTO:
                    case FIN:
                        btnConfirmarDescartar.setEnabled(false);
                        btnPasar.setEnabled(false);
                        btnApostar.setEnabled(false);
                        btnIgualar.setEnabled(false);
                        btnSubir.setEnabled(false);
                        btnRetirarse.setEnabled(false);
                        txtApuesta.setEnabled(false);
                        break;
                }

                cartasPanel.revalidate();
                cartasPanel.repaint();
            }

    // --- ACCIONES ---

    private void siguienteTurnoApuesta() {
        indiceJugadorActual = (indiceJugadorActual + 1) % jugadores.size();
        jugadorActual = jugadores.get(indiceJugadorActual);

        // Saltar jugadores retirados
        while (!jugadorActual.estaActivo()) {
            indiceJugadorActual = (indiceJugadorActual + 1) % jugadores.size();
            jugadorActual = jugadores.get(indiceJugadorActual);
        }

        // Verificar si la ronda de apuestas terminó
        long activos = jugadores.stream().filter(Jugador::estaActivo).count();
        if (activos == 1) {
            infoArea.append("Solo queda un jugador activo, ronda termina.\n");
            estado = EstadoRonda.FIN;
            determinarGanador();
            actualizarGUI();
            return;
        }

        if (jugadoresQueIgualaron >= activos && apuestaActual > 0) {
            // Todos igualaron, pasar a descarte
            infoArea.append("Todos han igualado. Pasando a ronda de descarte.\n");
            estado = EstadoRonda.DESCARTE;
            indiceJugadorActual = 0;
            jugadorActual = jugadores.get(indiceJugadorActual);
            actualizarGUI();
            return;
        }

        infoArea.append("Turno de: " + jugadorActual.getNombre() + "\n");
        actualizarGUI();
    }

    private void accionPasar() {
        if (apuestaActual > 0) {
            infoArea.append("No puedes pasar, hay apuesta activa.\n");
            return;
        }
        infoArea.append(jugadorActual.getNombre() + " pasa.\n");
        jugadoresQueIgualaron++;
        siguienteTurnoApuesta();
    }

    private void accionApostar() {
        if (apuestaActual > 0) {
            infoArea.append("Ya hay apuesta activa, usa igualar o subir.\n");
            return;
        }
        try {
            int cant = Integer.parseInt(txtApuesta.getText());
            if (cant < apuestaMinima) {
                infoArea.append("La apuesta mínima es " + apuestaMinima + ".\n");
                return;
            }
            if (!jugadorActual.apostar(cant)) {
                infoArea.append("No tienes fichas suficientes para apostar esa cantidad.\n");
                return;
            }
            apuestaActual = cant;
            bote.agregarApuesta(cant);
            jugadoresQueIgualaron = 1; // Primer jugador que igualó apuesta
            infoArea.append(jugadorActual.getNombre() + " apuesta " + cant + ".\n");
            siguienteTurnoApuesta();
        } catch (NumberFormatException e) {
            infoArea.append("Cantidad inválida.\n");
        }
    }

    private void accionIgualar() {
        if (apuestaActual == 0) {
            infoArea.append("No hay apuesta que igualar.\n");
            return;
        }
        if (!jugadorActual.apostar(apuestaActual)) {
            infoArea.append("No tienes fichas suficientes para igualar.\n");
            return;
        }
        bote.agregarApuesta(apuestaActual);
        jugadoresQueIgualaron++;
        infoArea.append(jugadorActual.getNombre() + " iguala " + apuestaActual + ".\n");
        siguienteTurnoApuesta();
    }

    private void accionSubir() {
        if (apuestaActual == 0) {
            infoArea.append("Primero debes apostar.\n");
            return;
        }
        try {
            int cant = Integer.parseInt(txtApuesta.getText());
            if (cant <= apuestaActual) {
                infoArea.append("La subida debe ser mayor que " + apuestaActual + ".\n");
                return;
            }
            if (!jugadorActual.apostar(cant)) {
                infoArea.append("No tienes fichas suficientes para subir.\n");
                return;
            }
            apuestaActual = cant;
            bote.agregarApuesta(cant);
            jugadoresQueIgualaron = 1; // reinicia el conteo de igualar
            infoArea.append(jugadorActual.getNombre() + " sube la apuesta a " + cant + ".\n");
            siguienteTurnoApuesta();
        } catch (NumberFormatException e) {
            infoArea.append("Cantidad inválida.\n");
        }
    }

    private void accionRetirarse() {
        jugadorActual.retirarse();
        jugadoresActivos--;
        infoArea.append(jugadorActual.getNombre() + " se retiró.\n");

        if (jugadoresActivos == 1) {
            estado = EstadoRonda.FIN;
            determinarGanador();
            actualizarGUI();
            return;
        }
        siguienteTurnoApuesta();
    }

    private void accionConfirmarDescartar() {
        // Crear lista índices cartas para descartar
        List<Integer> cartasParaCambiar = new ArrayList<>();
        for (int i = 0; i < botonesCartas.size(); i++) {
            if (botonesCartas.get(i).isSelected()) {
                cartasParaCambiar.add(i);
            }
        }

        jugadorActual.descartarCartas(cartasParaCambiar);

        // Repartir nuevas cartas para reemplazo
        for (int i = 0; i < cartasParaCambiar.size(); i++) {
            Carta nuevaCarta = baraja.sacarCarta();
            if (nuevaCarta != null) {
                jugadorActual.recibirCarta(nuevaCarta);
            }
        }

        infoArea.append(jugadorActual.getNombre() + " descartó " + cartasParaCambiar.size() + " cartas.\n");

        siguienteTurnoDescarte();
    }

    private void siguienteTurnoDescarte() {
        indiceJugadorActual = (indiceJugadorActual + 1) % jugadores.size();
        jugadorActual = jugadores.get(indiceJugadorActual);

        // Saltar jugadores retirados
        while (!jugadorActual.estaActivo()) {
            indiceJugadorActual = (indiceJugadorActual + 1) % jugadores.size();
            jugadorActual = jugadores.get(indiceJugadorActual);
        }
        
        if (indiceJugadorActual == 0) {
            infoArea.append("Ronda de descarte terminada.\n");
            estado = EstadoRonda.ENFRENTAMIENTO;
            determinarGanador();
            actualizarGUI();
        } else {
            infoArea.append("Turno de descarte: " + jugadorActual.getNombre() + "\n");
            actualizarGUI();
        }
    }

    private void determinarGanador() {
        infoArea.append("\n--- Resultado ---\n");

        Jugada mejorJugada = null;
        Jugador ganador = null;

        for (Jugador j : jugadores) {
            if (!j.estaActivo()) continue;

            Jugada jugada = EvaluadorMano.evaluarMejorMano(j.getMano(), Collections.emptyList());
            j.setJugada(jugada); // Guarda la jugada en el jugador

            infoArea.append(j.getNombre() + " tiene: " + jugada.getTipoJugada() + " (" + jugada.getCartas() + ")\n");

            if (mejorJugada == null || jugada.compareTo(mejorJugada) > 0) {
                mejorJugada = jugada;
                ganador = j;
            }
        }

        if (ganador != null) {
            infoArea.append("Ganador: " + ganador.getNombre() + " gana el bote de " + bote.getCantidad() + " fichas!\n");
            ganador.ajustarFichas(bote.getCantidad());
        } else {
            infoArea.append("No hay ganador claro.\n");
        }

        estado = EstadoRonda.FIN;

        int opcion = JOptionPane.showConfirmDialog(this, "¿Quieres jugar otra ronda?", "Fin de ronda", JOptionPane.YES_NO_OPTION);
        if (opcion == JOptionPane.YES_OPTION) {
            empezarRonda();
        } else {
            System.exit(0);
        }
    }
}