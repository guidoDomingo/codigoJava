package Base;

import java.awt.KeyboardFocusManager;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ActionMap;
import javax.swing.BorderFactory;
import javax.swing.InputMap;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.KeyStroke;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;

public class BuscadorGUI<T> extends javax.swing.JDialog {

    private DefaultTableModel modelo;
    private String id = null;
    private String descrip = null;
    private final int FILTRO_COLS[] = {0, 1};
    private String cadenaGUI;
    private GUI.tipoVentana tipoVentana;
    
    public BuscadorGUI(java.awt.Frame parent, boolean modal,List<T> datos) {
        super(parent, modal);
        initComponents();
         formatoTabla();
        armarTabla(datos);
        capturarDatosTabla();
        Conf_focus();
        TeclaGlobales();
        GUI.parseTabEnter();
        tabla.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    if (id != null) {
                        e.consume();
                        dispose();
                    }
                }
            }
        });

        tabla.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
            }

            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    if (id != null) {
                        e.consume();
                        dispose();
                    }
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
            }
        });
    }
    
    public BuscadorGUI(List<T> datos) {
        initComponents();
       // URL url = getClass().getResource("/recursos/iconos/t48/gota48.png");
      
       // txBuscar.setDocument(new MaxLength(100, "UPPER", "ALFA"));
        formatoTabla();
        armarTabla(datos);
        capturarDatosTabla();
        Conf_focus();
        TeclaGlobales();
        GUI.parseTabEnter();
        tabla.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    if (id != null) {
                        e.consume();
                        dispose();
                    }
                }
            }
        });

        tabla.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
            }

            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    if (id != null) {
                        e.consume();
                        dispose();
                    }
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
            }
        });
    }

    private void TeclaGlobales() {
        JPanel contentPane = (JPanel) this.getContentPane();
        int estado = JComponent.WHEN_IN_FOCUSED_WINDOW;
        InputMap mapaEntrada = contentPane.getInputMap(estado);
        ActionMap mapaAccion = contentPane.getActionMap();

        mapaEntrada.put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), "Escape");
        mapaAccion.put("Escape", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                dispose();
            }
        });
    }

//    private void armarTabla(ArrayList<T> datos) {
//        modelo = (DefaultTableModel) tabla.getModel();
//        if (datos.size() > 0) {
//            ArrayList<BaseDTO> dat = (ArrayList<BaseDTO>) datos;
//            for (BaseDTO d : dat) {
//                modelo.addRow(new Object[]{d.getDescrip(), d.getId()});
//            }
//        }
//    }
    
    private void armarTabla(List<T> datos) {
        modelo = (DefaultTableModel) tabla.getModel();
        if (datos.size() > 0) {
            ArrayList<BaseDTO> dat = (ArrayList<BaseDTO>)  datos;
            for (BaseDTO d : dat) {
                modelo.addRow(new Object[]{d.getDescrip(), d.getId()});
            }
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jButton1 = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        lbNombre3 = new javax.swing.JLabel();
        txBuscar = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        tabla = new javax.swing.JTable();

        jButton1.setText("jButton1");

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Buscador");

        jPanel3.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanel3.setFont(new java.awt.Font("Dialog", 0, 14)); // NOI18N

        lbNombre3.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        lbNombre3.setText("Buscar:");

        txBuscar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txBuscarActionPerformed(evt);
            }
        });
        txBuscar.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txBuscarKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txBuscarKeyTyped(evt);
            }
        });

        tabla.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        tabla.setRowHeight(20);
        tabla.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tablaMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tabla);

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                        .addComponent(lbNombre3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txBuscar))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 536, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lbNombre3)
                    .addComponent(txBuscar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 403, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(16, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(18, 18, 18))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void txBuscarKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txBuscarKeyTyped

    }//GEN-LAST:event_txBuscarKeyTyped

    private void txBuscarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txBuscarActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txBuscarActionPerformed

    private void txBuscarKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txBuscarKeyReleased
        GUI.actualizarFiltro(tabla, txBuscar.getText(), FILTRO_COLS);
    }//GEN-LAST:event_txBuscarKeyReleased

    private void tablaMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tablaMouseClicked
        if (evt.getClickCount() == 2) {
            capturarDatosTabla();
        }
    }//GEN-LAST:event_tablaMouseClicked

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    protected javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lbNombre3;
    protected javax.swing.JTable tabla;
    private javax.swing.JTextField txBuscar;
    // End of variables declaration//GEN-END:variables

    private void formatoTabla() {
        String[] cabeceras = {"Decripción", "Id"};

        DefaultTableModel m = new DefaultTableModel(cabeceras, 0) {
            @Override
            public Class<?> getColumnClass(int column) {
                if (getRowCount() > 0) {
                    Object value = getValueAt(0, column);
                    if (value != null) {
                        return getValueAt(0, column).getClass();
                    }
                }

                return super.getColumnClass(column);
            }

            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tabla.setModel(m);
        tabla.setCellSelectionEnabled(false);
        tabla.setRowSelectionAllowed(true);
        tabla.setFillsViewportHeight(true);

        tabla.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

        tabla.getColumnModel().getColumn(0).setMaxWidth(365); // Descripci�n
        tabla.getColumnModel().getColumn(0).setPreferredWidth(365);

        tabla.getColumnModel().getColumn(1).setMaxWidth(50); // Unidad Medida
        tabla.getColumnModel().getColumn(1).setPreferredWidth(50);

        GUI.alinerColumnaTabla(tabla, 1, GUI.ALINEACION.IZQUIERDA);
        GUI.alinerColumnaTabla(tabla, 1, GUI.ALINEACION.CENTRO);
        jScrollPane1.setBorder(BorderFactory.createEmptyBorder());
        //tabla.setIntercellSpacing(new Dimension(15, 0));

    }

    private void capturarDatosTabla() {
        tabla.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                int filaSelecciona = tabla.getSelectedRow();
                if (filaSelecciona >= 0) {
                    descrip = tabla.getValueAt(filaSelecciona, 0).toString().trim();
                    id = tabla.getValueAt(filaSelecciona, 1).toString().trim();
                }
            }
        });
        tabla.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        GUI.setFiltroTabla(tabla, txBuscar.getText(), FILTRO_COLS);
    }

    private void Conf_focus() {
        Set forwardKeys = new HashSet();
        forwardKeys.add(KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, 0));
        this.setFocusTraversalKeys(KeyboardFocusManager.FORWARD_TRAVERSAL_KEYS, forwardKeys);
        InputMap im = tabla.getInputMap(JTable.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
        KeyStroke tab = KeyStroke.getKeyStroke(KeyEvent.VK_TAB, 0);
        KeyStroke enter = KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0);
        im.put(enter, im.get(tab));
        KeyStroke right = KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, 0);
        im.put(right, "none");
        KeyStroke f5 = KeyStroke.getKeyStroke(KeyEvent.VK_F5, 0);
        im.put(f5, "none");
        final Action oldTabAction = tabla.getActionMap().get(im.get(tab));
        Action tabAction = new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                oldTabAction.actionPerformed(e);
                JTable table = (JTable) e.getSource();
                int row = table.getSelectedRow();
                int originalRow = row;
                int column = table.getSelectedColumn();
                int originalColumn = column;
                while (!table.isCellEditable(row, column)) {
                    oldTabAction.actionPerformed(e);
                    row = table.getSelectedRow();
                    column = table.getSelectedColumn();
                    if (row == originalRow && column == originalColumn) {
                        break;
                    }
                }
            }
        };
        tabla.getActionMap().put(im.get(tab), tabAction);
    }

    public String getID() {
        return id;
    }

    public String getDescrip() {
        return descrip;
    }

//    private void llamarVentana() {
//        if (cadenaGUI != null) {
//            try {
//                JInternalFrame gui;
//                Class myClass = Class.forName(cadenaGUI);
//                Constructor constructor = myClass.getConstructor();
//                Object instanceOfMyClass = constructor.newInstance();
//                gui = (JInternalFrame) instanceOfMyClass;
//                GUI.FormatoVentanas(gui, tipoVentana);
//                MDI.add(gui);
//                gui.show();
//                this.dispose();
//            } catch (ClassNotFoundException ex) {
//                Logger.getLogger(BuscadorGUI.class.getName()).log(Level.SEVERE, null, ex);
//            } catch (NoSuchMethodException ex) {
//                Logger.getLogger(BuscadorGUI.class.getName()).log(Level.SEVERE, null, ex);
//            } catch (SecurityException ex) {
//                Logger.getLogger(BuscadorGUI.class.getName()).log(Level.SEVERE, null, ex);
//            } catch (InstantiationException ex) {
//                Logger.getLogger(BuscadorGUI.class.getName()).log(Level.SEVERE, null, ex);
//            } catch (IllegalAccessException ex) {
//                Logger.getLogger(BuscadorGUI.class.getName()).log(Level.SEVERE, null, ex);
//            } catch (IllegalArgumentException ex) {
//                Logger.getLogger(BuscadorGUI.class.getName()).log(Level.SEVERE, null, ex);
//            } catch (InvocationTargetException ex) {
//                Logger.getLogger(BuscadorGUI.class.getName()).log(Level.SEVERE, null, ex);
//            }
//        }
//    }

}
