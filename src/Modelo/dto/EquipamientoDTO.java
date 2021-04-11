
package Modelo.dto;

import Base.BaseDTO;
import java.sql.Date;

public class EquipamientoDTO extends BaseDTO{
    private String nroSerie;
    private String modelo;
    private Date   fechaIngreso;
    private MarcaDTO marca;
    private ProcedenciaDTO procedencia;

    public EquipamientoDTO() {
    }
    
    public EquipamientoDTO(Integer id) {
        super(id);
    }
    
    public EquipamientoDTO(Integer id, String descrip) {
        super(id,descrip);
    }
    
     public EquipamientoDTO(String descrip) {
        super(descrip);
    }

    public String getNroSerie() {
        return nroSerie;
    }

    public void setNroSerie(String nroSerie) {
        this.nroSerie = nroSerie;
    }

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public Date getFechaIngreso() {
        return fechaIngreso;
    }

    public void setFechaIngreso(Date fechaIngreso) {
        this.fechaIngreso = fechaIngreso;
    }

    public MarcaDTO getMarca() {
        return marca;
    }

    public void setMarca(MarcaDTO marca) {
        this.marca = marca;
    }

    public ProcedenciaDTO getProcedencia() {
        return procedencia;
    }

    public void setProcedencia(ProcedenciaDTO procedencia) {
        this.procedencia = procedencia;
    }
    
}
