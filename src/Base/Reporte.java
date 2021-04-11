package Base;

import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import javax.print.Doc;
import javax.print.DocFlavor;
import javax.print.DocPrintJob;
import javax.print.PrintException;
import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import javax.print.SimpleDoc;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.KeyStroke;
import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperPrintManager;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.JRXlsExporter;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import net.sf.jasperreports.export.SimpleXlsReportConfiguration;
import net.sf.jasperreports.view.JasperViewer;

public class Reporte {

    private static String msg;
    private static JasperViewer jv;

    public static enum TIPO {
        VER, IMPRIMIR, PDF, EXCEL
    };

    private static JasperPrint procesar(String reporte, Map param) {
        JasperPrint jp;
        try {
            InputStream st = Reporte.class.getResourceAsStream(reporte);
            jp = JasperFillManager.fillReport(st, param, new JREmptyDataSource());
            return jp;
        } catch (JRException ex) {
            msg = ex.getMessage();
            return null;
        }
    }
   

    private static JasperPrint procesar(String reporte, Map param, JRBeanCollectionDataSource datos) {
        JasperPrint jp;
        try {
            InputStream st = Reporte.class.getResourceAsStream(reporte);
            if (datos == null) {
                jp = JasperFillManager.fillReport(st, param, new JREmptyDataSource());
            } else {
                jp = JasperFillManager.fillReport(st, param, datos);
            }
      
            return jp;
        } catch (JRException ex) {
            msg = "ERROR " + ex.getMessage();
            System.out.println(ex.getMessage());
            return null;
        }
    }
    

    private static boolean existe_archivo(String reporte) {
        File f = new File(reporte);
        if (f.exists()) {
            if (f.isFile()) {
                msg += "Es archivo<br>";
                return true;
            } else {
                msg += "No es archivo<br>";
            }
        } else {
            msg += "No existe<br>";
        }
        return false;
    }

    public static boolean ver(String reporte, Map param) {
        try {
            jv = new JasperViewer(procesar(reporte, param), false);
            jv.setTitle(param.get("titulo").toString());
            jv.setIconImage(GUI.getImagen("t48/gota48"));

            JPanel contentPane = (JPanel) jv.getContentPane();
            int condition = JComponent.WHEN_IN_FOCUSED_WINDOW;
            InputMap inputMap = contentPane.getInputMap(condition);
            ActionMap actionMap = contentPane.getActionMap();

            inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), "Escape");
            actionMap.put("Escape", new AbstractAction() {
                @Override
                public void actionPerformed(ActionEvent arg0) {
                    jv.dispose();
                }
            });

            jv.setExtendedState(Frame.MAXIMIZED_BOTH);
            jv.setVisible(true);
            msg = "Reporte Generado";
            return true;
        } catch (Exception ex) {
            msg += ex.getMessage();
            return false;
        }
    }
    
    
    public static boolean ver(String reporte, Map param, JRBeanCollectionDataSource datos) {
        try {
            jv = new JasperViewer(procesar(reporte, param, datos), false);
           // System.out.println(reporte + param + datos);
            jv.setTitle(param.get("titulo").toString());
            //jv.setIconImage(GUI.getImagen("t48/gota48"));

            JPanel contentPane = (JPanel) jv.getContentPane();
            int condition = JComponent.WHEN_IN_FOCUSED_WINDOW;
            InputMap inputMap = contentPane.getInputMap(condition);
            ActionMap actionMap = contentPane.getActionMap();

            inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), "Escape");
            actionMap.put("Escape", new AbstractAction() {
                @Override
                public void actionPerformed(ActionEvent arg0) {
                    jv.dispose();
                }
            });

            jv.setExtendedState(Frame.MAXIMIZED_BOTH);
            jv.setVisible(true);
            
            msg = "Reporte Generado";
            return true;
        } catch (Exception ex) {
            msg += ex.getMessage();
            System.out.println("Este el sms del metodo Ver del Reporte "+ex.getMessage() );
            return false;
        }
    }

    public static boolean imprimir(String reporte, Map param, JRBeanCollectionDataSource datos) {
        try {
            JasperPrintManager.printReport(procesar(reporte, param, datos), false);
            msg = "Reporte Generado";
            return true;
        } catch (JRException ex) {
            msg = ex.getMessage();
            return false;
        }
    }
    public static boolean imprimirConConfiguracion(String reporte, Map param, JRBeanCollectionDataSource datos) {
        try {
            boolean imprimir;
            imprimir =JasperPrintManager.printReport(procesar(reporte, param, datos), true);
            if (imprimir) {
                msg = "Reporte Generado";
                return true;
            } else {
                msg = "Impresi�n Cancelada";
                return false;
            }
            
        } catch (JRException ex) {
            msg = ex.getMessage();
            return false;
        }
    }

    public static boolean generarPDF(String reporte, Map param, JRBeanCollectionDataSource datos, String pathPdf) {
        try {
            //if(validar_archivo(pathPdf)){
            JasperExportManager.exportReportToPdfFile(procesar(reporte, param, datos), pathPdf);
            msg = "PDF Generado";
            //}
            return true;
        } catch (JRException ex) {
            msg = "generarPDF: " + ex.getMessage();
            return false;
        }
    }

    private static boolean validar_archivo(String path) {
        File a = new File(path);
        if (a.exists()) {
            int op = JOptionPane.showConfirmDialog(null,
                    "El archivo del informe a exportar ya existe en la ubicaci�n seleccionada.\n�Desea reemplazarlo?",
                    "Exportar archivo", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null);
            return op == JOptionPane.YES_OPTION;
        } else {
            return true;
        }
    }

    public static boolean generarExcel(String reporte, Map param, JRBeanCollectionDataSource datos, String pathArchivo) {
        try {
            if (validar_archivo(pathArchivo)) {
                JRXlsExporter e = new JRXlsExporter();
                e.setExporterInput(new SimpleExporterInput(procesar(reporte, param, datos)));
                e.setExporterOutput(new SimpleOutputStreamExporterOutput(new File(pathArchivo)));
                e.setConfiguration(config_Excel());

                e.exportReport();
                msg = "Excel Generado";
                return true;
            } else {
                return false;
            }
        } catch (JRException ex) {
            msg = "generarExcel: " + ex.getMessage();
            return false;
        }
    }

    private static SimpleXlsReportConfiguration config_Excel() {
        SimpleXlsReportConfiguration config = new SimpleXlsReportConfiguration();
        config.setOnePagePerSheet(false);
        config.setDetectCellType(true);
        config.setCollapseRowSpan(true);
        config.setWhitePageBackground(false);
        config.setShowGridLines(true);
        return config;
    }

    public static String getMsg() {
        return (msg == null ? "msg null" : msg);
    }

    public static boolean enviarImpresionDesdeArchivo(String file) {
        try {
            try (FileInputStream inputStream = new FileInputStream(file)) {
                if (inputStream == null) {
                    return false;
                }                
                DocFlavor docFormat = DocFlavor.INPUT_STREAM.AUTOSENSE;
                Doc document = new SimpleDoc(inputStream, docFormat, null);
                PrintRequestAttributeSet attributeSet = new HashPrintRequestAttributeSet();
                PrintService defaultPrintService = PrintServiceLookup.lookupDefaultPrintService();
                if (defaultPrintService != null) {
                    DocPrintJob printJob = defaultPrintService.createPrintJob();
                    try {
                        printJob.print(document, attributeSet);
                    } catch (PrintException e) {
                        e.printStackTrace();
                    }
                } else {
                    System.err.println("No existen impresoras instaladas");
                    return false;
                }
            }
            return true;
        } catch (FileNotFoundException e) {
        } catch (IOException ex) {
            //Logger.getLogger(Reporte.class.getName()).log(Level.SEVERE, null, ex);
            msg = ex.getMessage();
            return false;
        }
        return false;

    }
}
