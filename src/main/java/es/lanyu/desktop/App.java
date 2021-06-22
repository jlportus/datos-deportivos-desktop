package es.lanyu.desktop;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import com.esotericsoftware.tablelayout.swing.Table;

import es.lanyu.commons.servicios.entidad.ServicioEntidad;
import es.lanyu.commons.servicios.entidad.ServicioEntidadImpl;
import es.lanyu.comun.evento.Partido;
import es.lanyu.participante.Participante;
import es.lanyu.ui.swing.SimpleJTable;

public class App {
  
  public static void main(String[] args) {
    int ancho = 800, alto = 400;
    JFrame frame = new JFrame("Mi Frame");
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    
    Table tabla = new Table();
    frame.getContentPane().add(tabla);
    
    List<Participante> participantes = new ParticipanteDAO().getParticipantes();
    
    List<Partido> partidos = new PartidoDAO().getPartidos();
    ServicioEntidad servicioEntidad = new ServicioEntidadImpl();
    participantes.forEach(p -> servicioEntidad.getGestorNombrables().addNombrable(Participante.class, p));
    partidos.forEach(p -> p.setServicioEntidad(servicioEntidad));
    partidos.forEach(System.out::println);
    
    JPanel panelFormulario = new JPanel();
    panelFormulario.add(new JLabel("Seleccione un partido con doble click"));
    tabla.addCell(panelFormulario).expandX();
    tabla.row();
    
    SimpleJTable<Partido> tablaPartidos = new SimpleJTable<Partido>(partidos,
        new String[] { "Fecha", "Equipos" },
        p -> p.getFecha(),
        Partido::getEquipos);
    
    tablaPartidos.addMouseListener(new MouseAdapter() {
      public void mouseClicked(MouseEvent e) {
        if (e.getClickCount() > 1) {
          Partido partido = tablaPartidos.getSeleccionado();
          System.out.println(partido);
          panelFormulario.removeAll();
          panelFormulario.add(new PartidoForm(partido));
          panelFormulario.revalidate();
        }
      };
    });
    tablaPartidos.setAnchosPreferidos(200, 600);
    JScrollPane scrollPane = new JScrollPane(tablaPartidos);
    tabla.addCell(scrollPane).fillX();
    
    tabla.debug();
    frame.setSize(ancho, alto);
//    frame.pack();
    frame.setLocationRelativeTo(null);
//    frame.setLocation(2000, 200);
    frame.setVisible(true);
  }
}
