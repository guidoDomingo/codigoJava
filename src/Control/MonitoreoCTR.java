
package Control;

import Base.GUI;
import Modelo.dao.MonitoreoDAO;
import Modelo.dto.AlarmaDTO;
import Modelo.dto.CentralOperativoDTO;
import Modelo.dto.MonitoreoAlarmaDTO;
import Modelo.dto.MonitoreoDTO;
import Modelo.dto.ReservorioDTO;
import Modelo.dto.UsuarioDTO;
import Reportes.Informes;
import Vista.MonitoreoCentral;
import java.sql.Time;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;


public class MonitoreoCTR {
    private MonitoreoCentral gui;
    private MonitoreoDAO dao;
    private MonitoreoDTO dto;
    public enum ALARMAS{ NIVEL_MINIMO,NIVEL_MAXIMO,PARADA_EMERGENCIA    }
    public enum EQUIPAMIENTO{ MOTOR_UNO,MOTOR_DOS}
    
    
    
//    public MonitoreoCTR() {
//        registrarMonitoreo();
//    }
    public MonitoreoCTR(int id) {
        
        registrarMonitoreo(id);
    }

    public MonitoreoCTR(MonitoreoCentral gui) {
        this.gui = gui;
        registrarMonitoreo();
    }
    
    private void registrarMonitoreo(){
        dao = new MonitoreoDAO();
        if (dao.getIdMonitoreo() == null ) {
            dto = new MonitoreoDTO();
            dto.setCentralOperativo(new CentralOperativoDTO(1));
            dto.setUsuario(new UsuarioDTO(1));
            dto.setFecha(GUI.getDateSQL());
            dao.registrarMonitoreo(dto);
        } 
    }
    
     private void registrarMonitoreo(int id){
        dao = new MonitoreoDAO();
        //if (dao.getIdMonitoreo() == null ) {
            dto = new MonitoreoDTO();
            dto.setCentralOperativo(new CentralOperativoDTO(1));
            dto.setUsuario(new UsuarioDTO(id));
            dto.setFecha(GUI.getDateSQL());
            dao.registrarMonitoreo(dto);
        //} 
    }
  
    
     public void verReporteCaudal(){
        Informes info= new Informes();
        info.setArchivo("Caudal.jasper");
        info.setTitulo("HISTORICO DE CAUDAL");
        info.setSubtitulo("");
        info.setInforme(Informes.INFORME.CAUDAL);
        info.procesar();
    }
    
    
    public void grabarAlarma(ALARMAS alarma ){
        Integer idTipoAlarma = 0;
        switch (alarma) {
            case NIVEL_MINIMO:
                idTipoAlarma= 1;
                break;
            
            case NIVEL_MAXIMO:
                idTipoAlarma = 2 ;
                break;
            case PARADA_EMERGENCIA:
                idTipoAlarma = 3;
                break;
        }
        //recuperar hora actual
        DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
        Date date = new Date();
        MonitoreoAlarmaDTO monitAlarmaDTO= new MonitoreoAlarmaDTO();
        monitAlarmaDTO.setAlarma(new AlarmaDTO(idTipoAlarma));
        monitAlarmaDTO.setHora(dateFormat.format(date));
        monitAlarmaDTO.setMonitoreo(new MonitoreoDTO(new MonitoreoDAO().getIdMonitoreo()));
        monitAlarmaDTO.setReservorio(new ReservorioDTO(1));
        dao = new MonitoreoDAO();
        
        dao.registrarMonitoreoAlarma(monitAlarmaDTO);
    
    }  
    
    
     public void grabarAlarma(ALARMAS alarma , int id ){
        Integer idTipoAlarma = 0;
        switch (alarma) {
            case NIVEL_MINIMO:
                idTipoAlarma= 1;
                break;
            
            case NIVEL_MAXIMO:
                idTipoAlarma = 2 ;
                break;
            case PARADA_EMERGENCIA:
                idTipoAlarma = 3;
                break;
        }
        //recuperar hora actual
        DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
        Date date = new Date();

        System.out.println("Hora actual: " + dateFormat.format(date));
        //
        MonitoreoAlarmaDTO monitAlarmaDTO= new MonitoreoAlarmaDTO();
        monitAlarmaDTO.setAlarma(new AlarmaDTO(idTipoAlarma));
       // monitAlarmaDTO.setHorario(GUI.getTimeServer());
       monitAlarmaDTO.setHora(dateFormat.format(date));
       monitAlarmaDTO.setMonitoreo(new MonitoreoDTO(new MonitoreoDAO().getIdMonitoreo()));
       monitAlarmaDTO.setReservorio(new ReservorioDTO(id));
        dao = new MonitoreoDAO();
        
        dao.registrarMonitoreoAlarma(monitAlarmaDTO);
    
    } 
    
}
