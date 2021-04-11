package Control;

import Base.BaseControl;
import java.awt.Color;
import java.util.ArrayList;
import javax.swing.table.DefaultTableModel;
import Base.GUI;
import Modelo.dao.TipoMantenimientoDAO;
import Modelo.dao.TipoMantenimientoDAO;
import Modelo.dto.TipoMantenimientoDTO;
import Modelo.dto.TipoMantenimientoDTO;
import Reportes.Informes;
import Vista.Procedencia;
import Vista.TipoMantenimiento;

public class TipoMantenimientoCTR implements BaseControl<TipoMantenimientoDTO> {

    private TipoMantenimiento gui;
    private TipoMantenimientoDAO dao;
    private TipoMantenimientoDTO dto;
    private DefaultTableModel m;

    public TipoMantenimientoCTR(TipoMantenimiento gui) {
        this.gui = gui;
        InitDatos();
    }
  public void verInforme(){
        Informes info= new Informes();
        info.setArchivo("TipoMantenimiento.jasper");
        info.setTitulo("LISTADO DE TIPO DE MANTENIMIENTO");
        info.setSubtitulo("");
        info.setInforme(Informes.INFORME.TIPOMANTENIMIENTO);
        info.procesar();
    }
    
    @Override
    public void InitDatos() {
        //cargar la tabla 
        m = (DefaultTableModel) gui.tabla.getModel();
        m.setRowCount(0);
        dao = new TipoMantenimientoDAO();
        ArrayList<TipoMantenimientoDTO> datos = dao.recuperarRegistros();
        if (datos.size() > 0) {
            for (TipoMantenimientoDTO u : datos) {
                m.addRow(new Object[]{
                    u.getId(),
                    u.getDescrip(),
                    u.getComentario()
                }
                );
            }
        } else {
            GUI.msgAtencion(gui.lbInfo, "No se pudo recuperar los datos");
        }
    }

    @Override
    public void CargarDatos(int id) {
        if (dao == null) {
            dao = new TipoMantenimientoDAO();
        }
        dto = dao.recuperarRegistro(new TipoMantenimientoDTO(id));
        if (dto != null) {
            gui.txIdTipoMant.setText(String.valueOf(dto.getId()));
            gui.txTipoMant.setText(dto.getDescrip());
            gui.txComentario.setText(dto.getComentario());
        } else {
            GUI.msgError(gui.lbInfo, "Error al cargar datos");
            if (GUI.esAdmin()) {
                GUI.adminMsg(dao.msj());
            }
        }
    }

    public void editar() {
        int f = gui.tabla.getSelectedRow();
        if (f >= 0) {
            BotonesAgregar(false);
            int id = Integer.valueOf(gui.tabla.getValueAt(f, 0).toString());
            CargarDatos(id);
        } else {
            GUI.msgAtencion(gui.lbInfo, "Seleccionar un registro de la lista.");
        }
    }

    @Override
    public void BotonesAgregar(boolean b) {
        gui.btAgregar.setVisible(b);
        gui.btGuardar.setVisible(!b);
        gui.btEditar.setVisible(b);

        if (GUI.esAdmin()) {
            gui.btEliminar.setVisible(!b);
        }

        gui.tabla.setEnabled(b);
        if (b) {
            gui.tabla.setBackground(Color.white);
        } else {
            gui.tabla.setBackground(new Color(240, 240, 240, 255));
        }

        //gui.txBuscar.setEnabled(b);
       // gui.txIdTipoMant.setEnabled(b);
    }

    @Override
    public void Cancelar() {
        BotonesAgregar(true);
        dto = null;
        dao = null;
        
        gui.txIdTipoMant.setText("");
        
        gui.txTipoMant.setText("");
        gui.txComentario.setText("");
        //gui.txBuscar.setText("");
        InitDatos();
    }

    @Override
    public void Agregar() {
        dto = ValidarDatos(true);
        if (dto != null) {
            dao = new TipoMantenimientoDAO();
            if (dao.agregar(dto) == true) {
                GUI.msgInfo(gui.lbInfo, "Tipo Mantenimiento '" + dto.getDescrip() + "' registrado correctamente.");
                Cancelar();
            } else {
                GUI.msgAtencion(gui.lbInfo, "Error al insertar datos.");
                if (GUI.esAdmin()) {
                    GUI.adminMsg(dao.msj());
                }
            }
        } else {

        }
    }

    @Override
    public void Modificar() {
        if (dao == null) {
            dao = new TipoMantenimientoDAO();
        }
        dto = ValidarDatos(false);
        if (dto != null) {
            if (dao.modificar(dto)) {
                GUI.msgInfo(gui.lbInfo, "Tipo Mantenimiento '" + dto.getDescrip() + "' actualizado correctamente.");
                Cancelar();
            } else {
                GUI.msgAtencion(gui.lbInfo, "Error al modificar datos.");
                if (GUI.esAdmin()) {
                    GUI.adminMsg(dao.msj());
                }
            }
        }

    }

    @Override
    public void Eliminar() {
        if (dao == null) {
            dao = new TipoMantenimientoDAO();
        }
        if (dao.eliminar(dto)) {
            GUI.msgInfo(gui.lbInfo, "Tipo Mantenimiento '" + dto.getDescrip() + "' eliminado correctamente.");
            Cancelar();
        } else {
            GUI.msgAtencion(gui.lbInfo, "Error al eliminar datos.");
            if (GUI.esAdmin()) {
                GUI.adminMsg(dao.msj());
            }
        }
    }

    @Override
    public TipoMantenimientoDTO ValidarDatos(boolean agregar) {
        if (GUI.isEmpty(gui.txTipoMant)) {
            GUI.focusAll(gui.txTipoMant);
            GUI.msgAtencion(gui.lbInfo, "Campo [Tipo Mantenimiento] requerido");
            return null;
        }

        if (agregar == true) {
            dao = new TipoMantenimientoDAO();
            if (dao.existeDescrip(GUI.get(gui.txTipoMant)) == true) {
                GUI.focusAll(gui.txTipoMant);
                GUI.msgAtencion(gui.lbInfo, "Tipo Mantenimiento [" + GUI.get(gui.txTipoMant) + "] ya existe");
                return null;
            }
        }

        dto = new TipoMantenimientoDTO();
        dto.setId( (agregar == false ? Integer.valueOf(GUI.get(gui.txIdTipoMant)) : 0));
        dto.setDescrip(GUI.get(gui.txTipoMant));
        dto.setComentario(GUI.get(gui.txComentario));
        return dto;
    }

    @Override
    public void CerrarVentana() {
        gui.dispose();
    }

}
