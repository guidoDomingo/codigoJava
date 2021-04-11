package Modelo.dto;

import Base.BaseDTO;
import java.sql.Statement;
import java.sql.Time;
import java.util.Date;

public class MonitoreoAlarmaDTO extends BaseDTO{
   
    private Integer      id;
    private MonitoreoDTO monitoreo;
    private ReservorioDTO reservorio;
    private AlarmaDTO    alarma;
    private Time         horario;
    private String hora;
    
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public MonitoreoDTO getMonitoreo() {
        return monitoreo;
    }

    public void setMonitoreo(MonitoreoDTO monitoreo) {
        this.monitoreo = monitoreo;
    }

    public AlarmaDTO getAlarma() {
        return alarma;
    }

    public void setAlarma(AlarmaDTO alarma) {
        this.alarma = alarma;
    }

    public void setHorario(Time horario) {
        this.horario = horario;
    }

    public Time getHorario() {
        return horario;
    }

    public void setReservorio(ReservorioDTO reservorio) {
        this.reservorio = reservorio;
    }

    public ReservorioDTO getReservorio() {
        return reservorio;
    }

    public String getHora() {
        return hora;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }
    
}
