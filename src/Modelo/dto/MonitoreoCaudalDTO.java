
package Modelo.dto;

import Base.BaseDTO;
import java.sql.Time;
import java.util.Date;


public class MonitoreoCaudalDTO extends BaseDTO{
    private Integer id;
    private MonitoreoDTO monitoreo;
    private ReservorioDTO reservorio;
    private Time horario;
    private Date hora;
    private Integer volumen;
    private Integer horaCorta;
    

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

    public ReservorioDTO getReservorio() {
        return reservorio;
    }

    public void setReservorio(ReservorioDTO reservorio) {
        this.reservorio = reservorio;
    }

    public Time getHorario() {
        return horario;
    }

    public void setHorario(Time horario) {
        this.horario = horario;
    }

    public Integer getVolumen() {
        return volumen;
    }

    public void setVolumen(Integer volumen) {
        this.volumen = volumen;
    }

    public void setHoraCorta(Integer horaCorta) {
        this.horaCorta = horaCorta;
    }

    public Integer getHoraCorta() {
        return horaCorta;
    }

    public Date getHora() {
        return hora;
    }

    public void setHora(Date hora) {
        this.hora = hora;
    }

    
    
    
}
