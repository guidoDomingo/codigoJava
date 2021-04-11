package Modelo.dto;

import Base.BaseDTO;
import java.sql.Date;


public class MonitoreoDTO extends BaseDTO{
    
    private Integer id;
    private Integer idInterno;
    private UsuarioDTO usuario;
    private CentralOperativoDTO centralOperativo;
    private Date fecha;

    public MonitoreoDTO() {
    }

    
    public MonitoreoDTO(Integer id) {
        this.id = id;
    }
    
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getIdInterno() {
        return idInterno;
    }

    public void setIdInterno(Integer idInterno) {
        this.idInterno = idInterno;
    }

    public UsuarioDTO getUsuario() {
        return usuario;
    }

    public void setUsuario(UsuarioDTO usuario) {
        this.usuario = usuario;
    }

    public CentralOperativoDTO getCentralOperativo() {
        return centralOperativo;
    }

    public void setCentralOperativo(CentralOperativoDTO centralOperativo) {
        this.centralOperativo = centralOperativo;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public Date getFecha() {
        return fecha;
    }

    
    
}
