package Reportes;

import Base.Reporte;
import Modelo.dao.AlarmaDAO;
import Modelo.dao.CentralOperativoDAO;
import Modelo.dao.EquipamientoDAO;
import Modelo.dao.MantenimientoEquipamientoDAO;
import Modelo.dao.MarcaDAO;
import Modelo.dao.MonitoreoDAO;
import Modelo.dao.ProcedenciaDAO;
import Modelo.dao.ReservorioDAO;
import Modelo.dao.TipoMantenimientoDAO;
import java.util.HashMap;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;


public class Informes {
   
    public enum INFORME{MARCA,PROCEDENCIA,TIPOMANTENIMIENTO,EQUIPAMIENTOS, MANTENIMIENTO_EQUIPAMIENTO,ALARMAS,CENTRAL_OPERATIVO,RESERVORIO,ALARMAS_ACTIVADAS,CAUDAL,ALARMA_EMITIDA};
    private INFORME informe;
    private  String dirRaizArchivo = "/Reportes/";
    private  String archivo, titulo, subtitulo;
    private Integer idEquipamiento;
    
    public HashMap<String, Object> getParams() {
        HashMap<String, Object> param = new HashMap<>();
        param.put("titulo", getTitulo());
        param.put("subtitulo", getSubtitulo());
        param.put("empresa", "ESSAP S.A.");
        return param;
    }

    public void setInforme(INFORME informe) {
        this.informe = informe;
    }

    public JRBeanCollectionDataSource getDatos() {
        switch(informe){
            case MARCA: 
                return new JRBeanCollectionDataSource(new MarcaDAO().recuperarRegistros());
            case PROCEDENCIA: 
                return new JRBeanCollectionDataSource(new ProcedenciaDAO().recuperarRegistros());
            case TIPOMANTENIMIENTO: 
                return new JRBeanCollectionDataSource(new TipoMantenimientoDAO().recuperarRegistros());
            case EQUIPAMIENTOS: 
                return new JRBeanCollectionDataSource(new EquipamientoDAO().recuperarRegistros());
            case MANTENIMIENTO_EQUIPAMIENTO: 
                return new JRBeanCollectionDataSource(new MantenimientoEquipamientoDAO().recuperarMantenimientosEquipamiento(getIdEquipamiento()));
            case ALARMAS: 
                return new JRBeanCollectionDataSource(new AlarmaDAO().recuperarRegistros());
            case CENTRAL_OPERATIVO: 
                return new JRBeanCollectionDataSource(new CentralOperativoDAO().recuperarRegistros());
            case RESERVORIO: 
                return new JRBeanCollectionDataSource(new ReservorioDAO().recuperarRegistros());
            case CAUDAL: 
                return new JRBeanCollectionDataSource(new MonitoreoDAO().getReporteCaudal());
            case ALARMA_EMITIDA: 
                return new JRBeanCollectionDataSource(new MonitoreoDAO().getAlarmaEmitida());    
        }
        return null;
    }
    
    
    public void procesar(){
        dirRaizArchivo += getArchivo();
        Reporte.ver(dirRaizArchivo,getParams(), getDatos());
    }

    public String getArchivo() {
        return archivo;
    }

    public void setArchivo(String archivo) {
        this.archivo = archivo;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getSubtitulo() {
        return subtitulo;
    }

    public void setSubtitulo(String subtitulo) {
        this.subtitulo = subtitulo;
    }

    public void setIdEquipamiento(Integer idEquipamiento) {
        this.idEquipamiento = idEquipamiento;
    }
    
    private Integer getIdEquipamiento() {
        return idEquipamiento;
    }
}
