package Control;

import Base.BaseControl;
import java.awt.Color;
import java.util.ArrayList;
import javax.swing.table.DefaultTableModel;
import Base.GUI;
import Modelo.dao.CentralOperativoDAO;
import Modelo.dto.CentralOperativoDTO;
import Reportes.Informes;
import Vista.CentralOperativo;

public class CentralOperativoCTR implements BaseControl<CentralOperativoDTO> {

        
    private CentralOperativo gui;
    private CentralOperativoDAO dao;
    private CentralOperativoDTO dto;
    private DefaultTableModel m;

    public CentralOperativoCTR(CentralOperativo gui) {
        this.gui = gui;
        InitDatos();
    }

    @Override
    public void InitDatos() {
        //cargar la tabla 
        m = (DefaultTableModel) gui.tabla.getModel();
        m.setRowCount(0);
        dao = new CentralOperativoDAO();
        ArrayList<CentralOperativoDTO> datos = dao.recuperarRegistros();
        if (datos.size() > 0) {
            for (CentralOperativoDTO u : datos) {
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
            dao = new CentralOperativoDAO();
        }
        dto = dao.recuperarRegistro(new CentralOperativoDTO(id));
        if (dto != null) {
            gui.txIdCentralOperativo.setText(String.valueOf(dto.getId()));
            gui.txCentralOperativo.setText(dto.getDescrip());
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
        //gui.txIdMarca.setEnabled(b);
    }

    @Override
    public void Cancelar() {
        BotonesAgregar(true);
        dto = null;
        dao = null;
        
        gui.txIdCentralOperativo.setText("");
        
        gui.txCentralOperativo.setText("");
        gui.txComentario.setText("");
        //gui.txBuscar.setText("");
        InitDatos();
    }

    @Override
    public void Agregar() {
        dto = ValidarDatos(true);
        if (dto != null) {
            dao = new CentralOperativoDAO();
            if (dao.agregar(dto) == true) {
                GUI.msgInfo(gui.lbInfo, "Central Operativo '" + dto.getDescrip() + "' registrado correctamente.");
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
            dao = new CentralOperativoDAO();
        }
        dto = ValidarDatos(false);
        if (dto != null) {
            if (dao.modificar(dto)) {
                GUI.msgInfo(gui.lbInfo, "Central Operativo '" + dto.getDescrip() + "' actualizado correctamente.");
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
            dao = new CentralOperativoDAO();
        }
        if (dao.eliminar(dto)) {
            GUI.msgInfo(gui.lbInfo, "Central Operativo '" + dto.getDescrip() + "' eliminado correctamente.");
            Cancelar();
        } else {
            GUI.msgAtencion(gui.lbInfo, "Error al eliminar datos.");
            if (GUI.esAdmin()) {
                GUI.adminMsg(dao.msj());
            }
        }
    }

    @Override
    public CentralOperativoDTO ValidarDatos(boolean agregar) {
        if (GUI.isEmpty(gui.txCentralOperativo)) {
            GUI.focusAll(gui.txCentralOperativo);
            GUI.msgAtencion(gui.lbInfo, "Campo [Central Operativo] requerido");
            return null;
        }

        if (agregar == true) {
            dao = new CentralOperativoDAO();
            if (dao.existeDescrip(GUI.get(gui.txCentralOperativo)) == true) {
                GUI.focusAll(gui.txCentralOperativo);
                GUI.msgAtencion(gui.lbInfo, "Central Operativo [" + GUI.get(gui.txCentralOperativo) + "] ya existe");
                return null;
            }
        }

        dto = new CentralOperativoDTO();
        dto.setId( (agregar == false ? Integer.valueOf(GUI.get(gui.txIdCentralOperativo)) : 0));
        dto.setDescrip(GUI.get(gui.txCentralOperativo));
        dto.setComentario(GUI.get(gui.txComentario));
        return dto;
    }

    public void verInforme(){
        Informes info= new Informes();
        info.setArchivo("CentralOperativo.jasper");
        info.setTitulo("LISTADO DE CENTRALES OPERATIVAS");
        info.setSubtitulo("");
        info.setInforme(Informes.INFORME.CENTRAL_OPERATIVO);
        info.procesar();
    }
    
    @Override
    public void CerrarVentana() {
        gui.dispose();
    }

}
