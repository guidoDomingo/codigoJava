package Control;

import Base.BaseControl;
import java.awt.Color;
import java.util.ArrayList;
import javax.swing.table.DefaultTableModel;
import Base.GUI;
import Modelo.dao.ProcedenciaDAO;
import Modelo.dto.ProcedenciaDTO;
import Reportes.Informes;
import Vista.Procedencia;

public class ProcedenciaCTR implements BaseControl<ProcedenciaDTO> {

    private Procedencia gui;
    private ProcedenciaDAO dao;
    private ProcedenciaDTO dto;
    private DefaultTableModel m;

    public ProcedenciaCTR(Procedencia gui) {
        this.gui = gui;
        InitDatos();
    }

    @Override
    public void InitDatos() {
        //cargar la tabla 
        m = (DefaultTableModel) gui.tabla.getModel();
        m.setRowCount(0);
        dao = new ProcedenciaDAO();
        ArrayList<ProcedenciaDTO> datos = dao.recuperarRegistros();
        if (datos.size() > 0) {
            for (ProcedenciaDTO u : datos) {
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
    
     public void verInforme(){
        Informes info= new Informes();
        info.setArchivo("Procedencia.jasper");
        info.setTitulo("LISTADO DE PROCEDENCIAS DE EQUIPAMIENTOS");
        info.setSubtitulo("");
        info.setInforme(Informes.INFORME.PROCEDENCIA);
        info.procesar();
    }

    @Override
    public void CargarDatos(int id) {
        if (dao == null) {
            dao = new ProcedenciaDAO();
        }
        dto = dao.recuperarRegistro(new ProcedenciaDTO(id));
        if (dto != null) {
            gui.txIdProcedencia.setText(String.valueOf(dto.getId()));
            gui.txProcedencia.setText(dto.getDescrip());
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
        //gui.txIdProcedencia.setEnabled(b);
    }

    @Override
    public void Cancelar() {
        BotonesAgregar(true);
        dto = null;
        dao = null;
        
        gui.txIdProcedencia.setText("");
        
        gui.txProcedencia.setText("");
        gui.txComentario.setText("");
        //gui.txBuscar.setText("");
        InitDatos();
    }

    @Override
    public void Agregar() {
        dto = ValidarDatos(true);
        if (dto != null) {
            dao = new ProcedenciaDAO();
            if (dao.agregar(dto) == true) {
                GUI.msgInfo(gui.lbInfo, "Procedencia '" + dto.getDescrip() + "' registrado correctamente.");
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
            dao = new ProcedenciaDAO();
        }
        dto = ValidarDatos(false);
        if (dto != null) {
            if (dao.modificar(dto)) {
                GUI.msgInfo(gui.lbInfo, "Procedencia '" + dto.getDescrip() + "' actualizado correctamente.");
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
            dao = new ProcedenciaDAO();
        }
        if (dao.eliminar(dto)) {
            GUI.msgInfo(gui.lbInfo, "Procedencia '" + dto.getDescrip() + "' eliminado correctamente.");
            Cancelar();
        } else {
            GUI.msgAtencion(gui.lbInfo, "Error al eliminar datos.");
            if (GUI.esAdmin()) {
                GUI.adminMsg(dao.msj());
            }
        }
    }

    @Override
    public ProcedenciaDTO ValidarDatos(boolean agregar) {
        if (GUI.isEmpty(gui.txProcedencia)) {
            GUI.focusAll(gui.txProcedencia);
            GUI.msgAtencion(gui.lbInfo, "Campo [Procedencia] requerido");
            return null;
        }

        if (agregar == true) {
            dao = new ProcedenciaDAO();
            if (dao.existeDescrip(GUI.get(gui.txProcedencia)) == true) {
                GUI.focusAll(gui.txProcedencia);
                GUI.msgAtencion(gui.lbInfo, "Procedencia [" + GUI.get(gui.txProcedencia) + "] ya existe");
                return null;
            }
        }

        dto = new ProcedenciaDTO();
        dto.setId( (agregar == false ? Integer.valueOf(GUI.get(gui.txIdProcedencia)) : 0));
        dto.setDescrip(GUI.get(gui.txProcedencia));
        dto.setComentario(GUI.get(gui.txComentario));
        return dto;
    }

    @Override
    public void CerrarVentana() {
        gui.dispose();
    }

}
