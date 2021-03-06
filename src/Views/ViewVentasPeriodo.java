/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Views;

import Controllers.ConnectionDB;
import Models.Empresa;
import Models.TipoComprobante;
import com.apple.eawt.Application;
import java.awt.Cursor;
import java.awt.Desktop;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.export.JRXlsExporter;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import net.sf.jasperreports.export.SimpleXlsReportConfiguration;

/**
 *
 * @author javierfuenteshuertas
 */
public class ViewVentasPeriodo extends javax.swing.JFrame {

    /**
     * Creates new form ViewVentasPeriodo
     */
    private ConnectionDB conexion;
    private Connection conn;
    private TipoComprobante[] tipocombrobante;
    private Empresa empresa;
    private int tipoDocumento;
    private float saldobruto,impuesto,total;
    public ViewVentasPeriodo(Empresa empresa) {
        this.empresa=empresa;
        saldobruto=0;
        impuesto=0;
        total=0;
        initComponents();
        conexion=new ConnectionDB();
        conn=conexion.connect(conn);
        initDatosIniciales();
        this.getRootPane().setDefaultButton(jButton3);
        String sSistemaOperativo = System.getProperty("os.name");
        if(sSistemaOperativo.equals("Mac OS X")){
            Application application = Application.getApplication();
            Image image = Toolkit.getDefaultToolkit().getImage(getClass().getResource("/Images/logoKipu.png"));
            application.setDockIconImage(image);
        }
        else{
            Image icon = Toolkit.getDefaultToolkit().getImage(getClass().getResource("/Images/logoKipu.png"));
            this.setIconImage(icon);
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

        buttonGroup1 = new javax.swing.ButtonGroup();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jcbMes = new com.toedter.calendar.JMonthChooser();
        jcbAnio = new com.toedter.calendar.JYearChooser();
        jcbTipoComprobante = new javax.swing.JComboBox<>();
        jLabel2 = new javax.swing.JLabel();
        jRadioButton1 = new javax.swing.JRadioButton();
        jrbExcel = new javax.swing.JRadioButton();
        jLabel5 = new javax.swing.JLabel();
        jButton3 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();

        jPanel1.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(153, 153, 153), 1, true));

        jLabel1.setText("Periodo            :");

        jLabel2.setText("Comprobante   :");

        buttonGroup1.add(jRadioButton1);
        jRadioButton1.setText("PDF");

        buttonGroup1.add(jrbExcel);
        jrbExcel.setSelected(true);
        jrbExcel.setText("Excel");

        jLabel5.setText("Tipo de archivo:");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jcbMes, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jcbAnio, javax.swing.GroupLayout.PREFERRED_SIZE, 53, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createSequentialGroup()
                                    .addGap(6, 6, 6)
                                    .addComponent(jrbExcel)
                                    .addGap(18, 18, 18)
                                    .addComponent(jRadioButton1))
                                .addComponent(jcbTipoComprobante, javax.swing.GroupLayout.PREFERRED_SIZE, 194, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel5)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(jcbAnio, javax.swing.GroupLayout.DEFAULT_SIZE, 28, Short.MAX_VALUE)
                    .addComponent(jcbMes, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jcbTipoComprobante, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jrbExcel)
                    .addComponent(jRadioButton1))
                .addContainerGap(12, Short.MAX_VALUE))
        );

        jButton3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/check.png"))); // NOI18N
        jButton3.setText("Aceptar");
        jButton3.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton3.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        jButton4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/error.png"))); // NOI18N
        jButton4.setText("Cancelar");
        jButton4.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton4.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 91, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 91, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 54, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 58, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(13, Short.MAX_VALUE))
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        try {
            setCursor(new Cursor(Cursor.WAIT_CURSOR));
            generarArchivo();
            setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            this.setVisible(false);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(ViewVentasPeriodo.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        this.setVisible(false);
    }//GEN-LAST:event_jButton4ActionPerformed

   

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JRadioButton jRadioButton1;
    private com.toedter.calendar.JYearChooser jcbAnio;
    private com.toedter.calendar.JMonthChooser jcbMes;
    private javax.swing.JComboBox<String> jcbTipoComprobante;
    private javax.swing.JRadioButton jrbExcel;
    // End of variables declaration//GEN-END:variables
    private void initDatosIniciales() {
         Date date = new Date();
         jcbMes.setMonth(date.getMonth());
         int year = Calendar.getInstance().get(Calendar.YEAR);
         jcbAnio.setYear(year);
         tipocombrobante=new TipoComprobante[4];
         PreparedStatement stmt;
        try {
            stmt = conn.prepareStatement("select * from tipocomprobante order by numTipoComprobante asc");
            ResultSet rs=stmt.executeQuery();
            int i=0;
            while(rs.next()){
                tipocombrobante[i]=new TipoComprobante();
                tipocombrobante[i].setIdTipoComprobante(rs.getString("idTipoComprobante"));
                tipocombrobante[i].setAbreviatura(rs.getString("abreviatura"));
                tipocombrobante[i].setNombreComprobante(rs.getString("nombreComprobante"));
                tipocombrobante[i].setNumTipoComprobante(rs.getString("numTipoComprobante"));
                jcbTipoComprobante.addItem(tipocombrobante[i].getNombreComprobante());
                i++;
            }
        } catch (SQLException ex) {
            Logger.getLogger(ViewReporteCliente.class.getName()).log(Level.SEVERE, null, ex);
        }
        tipoDocumento=6;
        jcbTipoComprobante.addItemListener(new ItemListener() {
             @Override
             public void itemStateChanged(ItemEvent e) {
                 int ind=jcbTipoComprobante.getSelectedIndex();
                 if(ind==0)tipoDocumento=6;
                 else if(ind==1)tipoDocumento=1;
                 else if(ind==2)tipoDocumento=6;
                 else if(ind==3)tipoDocumento=6;
             }
         });
    }
    private String generarArchivo() throws FileNotFoundException {
        String path=null;
        JasperReport jasperReport;
        JasperPrint jasperPrint;
        String pathReport = System.getProperty("user.dir")+"/Reportes/ReportesdeVentaPeriodo.jrxml"; 
        try {
            jasperReport=JasperCompileManager.compileReport(pathReport);
            //se procesa el archivo jasper
           Map parametros = new HashMap();
           parametros.put("tipoComprobante", tipocombrobante[jcbTipoComprobante.getSelectedIndex()].getIdTipoComprobante());
           parametros.put("idEmpresa", empresa.getIdEmpresa());
           parametros.put("logoEmpresa", System.getProperty("user.dir")+empresa.getLogo());
           parametros.put("mes", jcbMes.getMonth()+1);
           parametros.put("anio", jcbAnio.getYear());
           this.consultarTotales();
           parametros.put("totalBruto", saldobruto);
           parametros.put("totalImpuesto", impuesto);
           parametros.put("totalGeneral", total);
           parametros.put("nombreMes",this.nombreMes(jcbMes.getMonth()));
           parametros.put("logoImagen",System.getProperty("user.dir")+"/Images/Plantilla-Planeta-Hojas.jpg");
           jasperPrint = JasperFillManager.fillReport(jasperReport, parametros, conn );
           Date date=new Date();
           SimpleDateFormat dt1 = new SimpleDateFormat("yyyyymmddhhmmss");
           String nameFile="ReportePeriodo"+dt1.format(date);
           if(jrbExcel.isSelected()){
               nameFile+=".xls";
               JRXlsExporter xlsExporter = new JRXlsExporter();
                xlsExporter.setExporterInput(new SimpleExporterInput(jasperPrint));
                xlsExporter.setExporterOutput(new SimpleOutputStreamExporterOutput(nameFile));
                SimpleXlsReportConfiguration xlsReportConfiguration = new SimpleXlsReportConfiguration();
                xlsReportConfiguration.setOnePagePerSheet(false);
                xlsReportConfiguration.setRemoveEmptySpaceBetweenRows(true);
                xlsReportConfiguration.setDetectCellType(false);
                xlsReportConfiguration.setWhitePageBackground(false);
                xlsExporter.setConfiguration(xlsReportConfiguration);
                xlsExporter.exportReport();
                File fileXls= new File(nameFile);
                Desktop.getDesktop().open(fileXls);
           }
           else {
               nameFile+=".pdf"; 
               File pdf =new  File(nameFile);
               JasperExportManager.exportReportToPdfStream(jasperPrint, new FileOutputStream(pdf));
               Desktop.getDesktop().open(pdf);
           }
           
           //path=pdf.getAbsolutePath();
        } catch (JRException ex) {
            Logger.getLogger(Comprobante.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) { 
            Logger.getLogger(ViewReporteCliente.class.getName()).log(Level.SEVERE, null, ex);
        }
        return path;
    }
    private void consultarTotales(){
        PreparedStatement stmt;
        try {
            stmt = conn.prepareStatement("select sum(impuesto) as sumaImpuesto,sum(saldoBruto)as saldobruto,sum(total) as total from comprobantepago where Empresa_idEmpresa=? and TipoComprobante_idTipoComprobante=?  and MONTH(fechaEmision)=? and YEAR(fechaEmision)=?");
            stmt.setInt(1, empresa.getIdEmpresa());
            stmt.setString(2, tipocombrobante[jcbTipoComprobante.getSelectedIndex()].getIdTipoComprobante());
            stmt.setInt(3, jcbMes.getMonth()+1);
            stmt.setInt(4,jcbAnio.getYear());
            ResultSet rs=stmt.executeQuery();
            if(rs.next()){
                saldobruto=rs.getFloat("saldobruto");
                impuesto=rs.getFloat("sumaImpuesto");
                total=rs.getFloat("total");
            }
        } catch (SQLException ex) {
            Logger.getLogger(ViewReporteCliente.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    public String nombreMes(int numMes){
        String[] nombresMeses={"Enero","Febrero","Marzo","Abril","Mayo","Junio","Julio","Agosto","Setiembre","Octubre","Noviembre","Diciembre"};
        return nombresMeses[numMes];
    }
}
