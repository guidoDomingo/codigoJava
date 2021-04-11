package Control;

import Base.BaseControl;
import java.awt.Color;
import java.util.ArrayList;
import javax.swing.table.DefaultTableModel;
import Base.GUI;
import Modelo.dao.ReservorioDAO;
import Modelo.dto.ReservorioDTO;
import Reportes.Informes;
import Vista.Reservorio;

public class ReservorioCTR implements BaseControl<ReservorioDTO> {

    
    private Reservorio gui;
    private ReservorioDAO dao;
    private ReservorioDTO dto;
    private DefaultTableModel m;

    public ReservorioCTR(Reservorio gui) {
        this.gui = gui;
        InitDatos();
    }

    @Override
    public void InitDatos() {
        //cargar la tabla 
        m = (DefaultTableModel) gui.tabla.getModel();
        m.setRowCount(0);
        dao = new ReservorioDAO();
        ArrayList<ReservorioDTO> datos = dao.recuperarRegistros();
        if (datos.size() > 0) {
            for (ReservorioDTO u : datos) {
                m.addRow(new Object[]{
                    u.getId(),
                    u.getDescrip(),
                    u.getCapacidad(),
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
            dao = new ReservorioDAO();
        }
        dto = dao.recuperarRegistro(new ReservorioDTO(id));
        if (dto != null) {
            gui.txIdReservorio.setText(String.valueOf(dto.getId()));
            gui.txReservorio.setText(dto.getDescrip());
            gui.txCapacidad.setText(dto.getCapacidad().toString());
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
        
        gui.txIdReservorio.setText("");
        
        gui.txReservorio.setText("");
        gui.txComentario.setText("");
        gui.txCapacidad.setText("");
        //gui.txBuscar.setText("");
        InitDatos();
    }

    @Override
    public void Agregar() {
        dto = ValidarDatos(true);
        if (dto != null) {
            dao = new ReservorioDAO();
            if (dao.agregar(dto) == true) {
                GUI.msgInfo(gui.lbInfo, "Reservorio '" + dto.getDescrip() + "' registrado correctamente.");
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
            dao = new ReservorioDAO();
        }
        dto = ValidarDatos(false);
        if (dto != null) {
            if (dao.modificar(dto)) {
                GUI.msgInfo(gui.lbInfo, "Reservorio '" + dto.getDescrip() + "' actualizado correctamente.");
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
            dao = new ReservorioDAO();
        }
        if (dao.eliminar(dto)) {
            GUI.msgInfo(gui.lbInfo, "Reservorio '" + dto.getDescrip() + "' eliminado correctamente.");
            Cancelar();
        } else {
            GUI.msgAtencion(gui.lbInfo, "Error al eliminar datos.");
            if (GUI.esAdmin()) {
                GUI.adminMsg(dao.msj());
            }
        }
    }

    @Override
    public ReservorioDTO ValidarDatos(boolean agregar) {
        if (GUI.isEmpty(gui.txReservorio)) {
            GUI.focusAll(gui.txReservorio);
            GUI.msgAtencion(gui.lbInfo, "Campo [Reservorio] requerido");
            return null;
        }
        
        if (GUI.isEmpty(gui.txCapacidad)) {
            GUI.focusAll(gui.txCapacidad);
            GUI.msgAtencion(gui.lbInfo, "Campo [Capacidad] requerido");
            return null;
        }

        if (agregar == true) {
            dao = new ReservorioDAO();
            if (dao.existeDescrip(GUI.get(gui.txReservorio)) == true) {
                GUI.focusAll(gui.txReservorio);
                GUI.msgAtencion(gui.lbInfo, "Reservorio [" + GUI.get(gui.txReservorio) + "] ya existe");
                return null;
            }
        }

        dto = new ReservorioDTO();
        dto.setId( (agregar == false ? Integer.valueOf(GUI.get(gui.txIdReservorio)) : 0));
        dto.setDescrip(GUI.get(gui.txReservorio));
        dto.setCapacidad(Integer.parseInt(gui.txCapacidad.getText().trim()));
        dto.setComentario(GUI.get(gui.txComentario));
        return dto;
    }

    public void verInforme(){
        Informes info= new Informes();
        info.setArchivo("Reservorios.jasper");
        info.setTitulo("LISTADO DE RESERVORIOS");
        info.setSubtitulo("");
        info.setInforme(Informes.INFORME.RESERVORIO);
        info.procesar();
    }
    
    @Override
    public void CerrarVentana() {
        gui.dispose();
    }

}
