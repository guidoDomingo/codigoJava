
package Base;

import java.util.HashMap;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;


public abstract class InformeBase <T> {
    String msg = "";
    private final String dirArchivo = "/Reportes/";
    String archivo, titulo, subtitulo;
    int codInforme;
    
    public abstract HashMap<String, Object> getParams();
    public abstract JRBeanCollectionDataSource getDatos();

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
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

    public int getCodInforme() {
        return codInforme;
    }

    public void setCodInforme(int codInforme) {
        this.codInforme = codInforme;
    }
    
    
}
