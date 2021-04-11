
package Control;

import Base.GUI;
import Modelo.dao.MonitoreoDAO;
import Modelo.dto.MonitoreoAlarmaDTO;
import Reportes.Informes;
import Vista.AlarmasEmitidas;
import java.sql.Date;
import java.util.List;
import javax.swing.table.DefaultTableModel;

public class AlarmasEmitidasCTR {
    private AlarmasEmitidas gui;
    private MonitoreoAlarmaDTO dto;
    private MonitoreoDAO dao;
    private List<MonitoreoAlarmaDTO> lista;
    String fechaHasta, fechaDesde;
    
    public AlarmasEmitidasCTR(AlarmasEmitidas gui) {
        this.gui = gui;
        cargarAlarmas(null,null);
    }
    
    
    public void cargarAlarmas(String fecha1, String fecha2){
        dao = new MonitoreoDAO();
        lista = dao.getAlarmasEmitidas(fecha1 , fecha2);
        DefaultTableModel tabla = (DefaultTableModel) gui.tabla.getModel();
        tabla.setRowCount(0);
        //"Id","Fecha", "Usuario","Reservorio","Alarma","Horario"};
        for (MonitoreoAlarmaDTO m : lista) {
            tabla.addRow(new Object[]{ m.getId(),
                GUI.formatoDiaMesAnho(m.getMonitoreo().getFecha()),
                m.getMonitoreo().getUsuario().getUsusario(),
                m.getMonitoreo().getUsuario().getNombre(),
                m.getReservorio().getDescrip(),
                m.getAlarma().getDescrip(),
                GUI.formatoSoloHora(m.getHorario())
                
            });
        }
    }
    
     public void cargarAlarmas(){
        dao = new MonitoreoDAO();
        fechaDesde =  gui.fechaDesde.getText().toString();
        fechaHasta = gui.fechaHasta.getText().toString();
        lista = dao.getAlarmasEmitidas(fechaDesde , fechaHasta);
        DefaultTableModel tabla = (DefaultTableModel) gui.tabla.getModel();
        tabla.setRowCount(0);
        //"Id","Fecha", "Usuario","Reservorio","Alarma","Horario"};
        for (MonitoreoAlarmaDTO m : lista) {
            tabla.addRow(new Object[]{ m.getId(),
                GUI.formatoDiaMesAnho(m.getMonitoreo().getFecha()),
                m.getMonitoreo().getUsuario().getUsusario(),
                m.getMonitoreo().getUsuario().getNombre(),
                m.getReservorio().getDescrip(),
                m.getAlarma().getDescrip(),
                GUI.formatoSoloHora(m.getHorario())
                
            });
        }
    }
     public void verInforme(){
        Informes info= new Informes();
        info.setArchivo("report4.jasper");
        info.setTitulo("LISTADO DE ALARMAS Emitidas");
        info.setSubtitulo("");
        info.setInforme(Informes.INFORME.ALARMA_EMITIDA);
        info.procesar();
    }
    
    
}
