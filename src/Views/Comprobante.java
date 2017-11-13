/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Views;

import APISunat.BillService;
import APISunat.BillService_Service;
import Controllers.ConnectionDB;
import Controllers.HeaderHandlerResolver;
import Controllers.NumberToLetterConverter;
import Controllers.Zip;
import Models.Cliente;
import Models.ComprobantePago;
import Models.CorreoEmpresa;
import Models.Empresa;
import Models.Producto;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Event;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.RoundingMode;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.security.InvalidAlgorithmParameterException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableEntryException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.activation.DataHandler;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.DefaultCellEditor;
import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.KeyStroke;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.xml.crypto.MarshalException;
import javax.xml.crypto.dsig.CanonicalizationMethod;
import javax.xml.crypto.dsig.DigestMethod;
import javax.xml.crypto.dsig.Reference;
import javax.xml.crypto.dsig.SignatureMethod;
import javax.xml.crypto.dsig.SignedInfo;
import javax.xml.crypto.dsig.Transform;
import javax.xml.crypto.dsig.XMLSignature;
import javax.xml.crypto.dsig.XMLSignatureException;
import javax.xml.crypto.dsig.XMLSignatureFactory;
import javax.xml.crypto.dsig.dom.DOMSignContext;
import javax.xml.crypto.dsig.keyinfo.KeyInfo;
import javax.xml.crypto.dsig.keyinfo.KeyInfoFactory;
import javax.xml.crypto.dsig.keyinfo.X509Data;
import javax.xml.crypto.dsig.spec.C14NMethodParameterSpec;
import javax.xml.crypto.dsig.spec.TransformParameterSpec;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperPrintManager;
import net.sf.jasperreports.engine.JasperReport;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.dom.DOMSource;
import org.w3c.dom.Element;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import org.apache.axis2.databinding.utils.ConverterUtil;
import org.apache.commons.codec.binary.Base64;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;
import org.xml.sax.SAXException;



/**
 *
 * @author javierfuenteshuertas
 */
public class Comprobante extends JPanel {

    /**
     * Creates new form Factura
     */
    String datos[];
    String moneda="PEN";
    JTabbedPane tabbed;
    private final ConnectionDB conexion;
    private Connection conn;
    private final Empresa empresa;
    private CorreoEmpresa correoEmpresa;
    private final String tipoComprobante;
    private String tipoDocumento;
    private Cliente cliente;
    private final float COMPRA;
    private final float VENTA;
    private ComprobantePago comprobante;
    private String numComprobante;
    private final int idCuenta;
    private ArrayList<Producto> listProductos;
    private boolean guardo=true;
    DecimalFormat format;
    public static final float IGVSUNAT=0.18f;
    private  ComprobantePago comprobanteModificar;
    String barcode=new String();
    long lastTime=0;
    public Comprobante(JTabbedPane panel,Empresa empresa,String tipoComprobante,float compra,float venta,int idCuenta) {
        //instanciar atributos
        this.tabbed=panel;
        this.empresa=empresa;
        this.tipoComprobante=tipoComprobante;
        this.COMPRA=compra;
        this.VENTA=venta;
        this.idCuenta=idCuenta;
        //iniciar Objeto modelos
        comprobante=new ComprobantePago();
        cliente=new Cliente();
        listProductos=new ArrayList<Producto>();
        //Iniciar interfaz gráfica
        initComponents();
        eventosTabla();
        eventosTeclado();
        //inciar Base de datos
        conexion=new ConnectionDB();
        conn=conexion.connect(conn);
        //datos por defecto de factura
        datosCorreo();
        datosIniciales();
        //agregarProductoTabla();
        
    }

    public void setComprobanteModificar(ComprobantePago comprobanteModificar) {
        this.comprobanteModificar = comprobanteModificar;
        String serie=comprobanteModificar.getSerieComprobante().substring(0,3);
        String numero=comprobanteModificar.getSerieComprobante().substring(3);
        jtSerieGuia.setText(comprobanteModificar.getTipoComprobanteIdTipoComprobante()+serie);
        jtnumGuia.setText(numero);
    }
    
    public String getTipoComprobante() {
        return tipoComprobante;
    }
    
    public Cliente getCliente() {
        return cliente;
    }
    
    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public ArrayList<Producto> getListProductos() {
        return listProductos;
    }

    public void setListProductos(ArrayList<Producto> listProductos) {
        this.listProductos = listProductos;
        //Productos
        DefaultTableModel dtm=(DefaultTableModel) jtProductos.getModel();
        int tempRow=dtm.getRowCount();
        for(int i=0;i<tempRow;i++){
            dtm.removeRow(0);
        }
        
        Object[] obj=null;
        Producto tempProducto;
        jlCantProductos.setText(String.valueOf(listProductos.size()));
        for(int i=0;i<listProductos.size();i++){
            dtm.addRow(obj);
            tempProducto=listProductos.get(i);
            dtm.setValueAt(tempProducto.getIdProducto(), i, 0);
            dtm.setValueAt(tempProducto.getDescripcion(), i, 1);
            dtm.setValueAt(tempProducto.getCantidad(), i, 2);
            dtm.setValueAt(tempProducto.getUnidadMedida(), i, 3);
            dtm.setValueAt(tempProducto.getPrecioUnit(), i, 4);
            if(tempProducto.getImpuesto()>0)dtm.setValueAt(true, i, 5);
            else dtm.setValueAt(false, i, 5);
        }
        actualizarTablaProductos();
    }

    public ComprobantePago getComprobante() {
        return comprobante;
    }

    public void setComprobante(ComprobantePago comprobante) {
        this.comprobante = comprobante;
    }
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jlLogo = new javax.swing.JLabel();
        jPanel5 = new javax.swing.JPanel();
        jlRUCEmpresa = new javax.swing.JLabel();
        jlTipoComprobante = new javax.swing.JLabel();
        jlNumFactura = new javax.swing.JLabel();
        jlEstadoFactura = new javax.swing.JLabel();
        jlDireccion = new javax.swing.JLabel();
        jlNombreComercial = new javax.swing.JLabel();
        jlUbigeo = new javax.swing.JLabel();
        jlEmail = new javax.swing.JLabel();
        jlTelefono = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jPanel6 = new javax.swing.JPanel();
        jlTipoDocumento = new javax.swing.JLabel();
        jtRUC = new javax.swing.JTextField();
        jLabel12 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jtDireccion = new javax.swing.JTextField();
        jtNombreContrinuyente = new javax.swing.JTextField();
        jButton3 = new javax.swing.JButton();
        jbSeleccionarFactura = new javax.swing.JButton();
        jcbMotivo = new javax.swing.JComboBox<>();
        jPanel7 = new javax.swing.JPanel();
        jLabel14 = new javax.swing.JLabel();
        jdFechaEmision = new com.toedter.calendar.JDateChooser();
        jLabel15 = new javax.swing.JLabel();
        jcbMoneda = new javax.swing.JComboBox();
        jtSerieGuia = new javax.swing.JTextField();
        jtnumGuia = new javax.swing.JTextField();
        jrbDocReferencia = new javax.swing.JRadioButton();
        jLabel29 = new javax.swing.JLabel();
        jsDescuento = new javax.swing.JSpinner();
        jPanel3 = new javax.swing.JPanel();
        jPanel8 = new javax.swing.JPanel();
        jLabel19 = new javax.swing.JLabel();
        jlMonIGV = new javax.swing.JLabel();
        jlIGV = new javax.swing.JLabel();
        jPanel9 = new javax.swing.JPanel();
        jLabel20 = new javax.swing.JLabel();
        jlDescuento = new javax.swing.JLabel();
        jlMonTotal = new javax.swing.JLabel();
        jPanel11 = new javax.swing.JPanel();
        jLabel18 = new javax.swing.JLabel();
        jlMonSubTotal = new javax.swing.JLabel();
        jlSubTotal = new javax.swing.JLabel();
        jPanel12 = new javax.swing.JPanel();
        jlNumeroLetras = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jtaObservacion = new javax.swing.JTextArea();
        jPanel13 = new javax.swing.JPanel();
        jLabel23 = new javax.swing.JLabel();
        jlTotal = new javax.swing.JLabel();
        jlMonTotal2 = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jtProductos = new javax.swing.JTable();
        jbAgregarProdcuto = new javax.swing.JButton();
        jbEliminarProducto = new javax.swing.JButton();
        jLabel21 = new javax.swing.JLabel();
        jlCantProductos = new javax.swing.JLabel();
        jToolBar1 = new javax.swing.JToolBar();
        jButton8 = new javax.swing.JButton();
        jbGuardarComprobante = new javax.swing.JButton();
        jbEliminar = new javax.swing.JButton();
        jbValidarSunat = new javax.swing.JButton();
        jbEnviarEmail = new javax.swing.JButton();
        jbImprimir = new javax.swing.JButton();
        filler1 = new javax.swing.Box.Filler(new java.awt.Dimension(0, 0), new java.awt.Dimension(0, 0), new java.awt.Dimension(32767, 0));
        jLabel28 = new javax.swing.JLabel();
        jlEstado = new javax.swing.JLabel();
        jLabel27 = new javax.swing.JLabel();
        jlCondicion = new javax.swing.JLabel();

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanel1.setMaximumSize(new java.awt.Dimension(32767, 130));
        jPanel1.setPreferredSize(new java.awt.Dimension(900, 130));

        jlLogo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/1.jpg"))); // NOI18N
        jlLogo.setMaximumSize(new java.awt.Dimension(350, 150));
        jlLogo.setMinimumSize(new java.awt.Dimension(350, 150));
        jlLogo.setPreferredSize(new java.awt.Dimension(350, 150));
        jlLogo.setSize(new java.awt.Dimension(350, 150));

        jPanel5.setBackground(new java.awt.Color(255, 255, 255));
        jPanel5.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true));
        jPanel5.setDebugGraphicsOptions(javax.swing.DebugGraphics.NONE_OPTION);
        jPanel5.setMaximumSize(new java.awt.Dimension(280, 150));
        jPanel5.setSize(new java.awt.Dimension(280, 150));

        jlRUCEmpresa.setFont(new java.awt.Font("Lucida Grande", 0, 18)); // NOI18N
        jlRUCEmpresa.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jlRUCEmpresa.setText("R.U.C. 20602138390");
        jlRUCEmpresa.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);

        jlTipoComprobante.setFont(new java.awt.Font("Lucida Grande", 0, 24)); // NOI18N
        jlTipoComprobante.setText("FACTURA ELETRÓNICA");

        jlNumFactura.setFont(new java.awt.Font("Lucida Grande", 0, 16)); // NOI18N
        jlNumFactura.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jlNumFactura.setText("<html><center>Nº F005-00000241</center></html>");

        jlEstadoFactura.setFont(new java.awt.Font("Lucida Grande", 0, 14)); // NOI18N
        jlEstadoFactura.setForeground(new java.awt.Color(255, 0, 0));
        jlEstadoFactura.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jlEstadoFactura.setText("BORRADOR");

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jlRUCEmpresa, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jlTipoComprobante, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jlNumFactura, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jlEstadoFactura, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jlRUCEmpresa)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jlTipoComprobante)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jlEstadoFactura, javax.swing.GroupLayout.PREFERRED_SIZE, 17, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jlNumFactura))
        );

        jlDireccion.setText("PJ. GERONIMO DE LA TORRE NRO. 220 INT. 1PIS URB. LAS QUINTANAS (PRIMER PISO)");

        jlNombreComercial.setFont(new java.awt.Font("Lucida Grande", 1, 14)); // NOI18N
        jlNombreComercial.setText("GR CONTADORES Y EMPRESAS S.A.C.");

        jlUbigeo.setText("Trujillo- Trujillo - La Libertad");

        jlEmail.setText("danixa.garcia@grcontadores.com.pe");

        jlTelefono.setText("+51 959792561");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jlLogo, javax.swing.GroupLayout.PREFERRED_SIZE, 350, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jlDireccion, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addComponent(jlNombreComercial, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jlUbigeo)
                            .addComponent(jlEmail)
                            .addComponent(jlTelefono))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jlLogo, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(6, 6, 6)
                        .addComponent(jlNombreComercial)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jlDireccion)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jlUbigeo)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jlEmail, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jlTelefono)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        jPanel2.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jlTipoDocumento.setLabelFor(jtRUC);
        jlTipoDocumento.setText("R.U.C.      :");

        jtRUC.setNextFocusableComponent(jtProductos);
        jtRUC.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jtRUCKeyTyped(evt);
            }
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jtRUCKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jtRUCKeyReleased(evt);
            }
        });

        jLabel12.setText("Señores   :");

        jLabel13.setText("Dirección:");

        jtDireccion.setEditable(false);
        jtDireccion.setBackground(new Color(254,191,36));
        jtDireccion.setFocusable(false);

        jtNombreContrinuyente.setEditable(false);
        jtNombreContrinuyente.setBackground(new Color(254,191,36));
        jtNombreContrinuyente.setFocusable(false);

        jButton3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/seller.png"))); // NOI18N
        jButton3.setFocusable(false);
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        jbSeleccionarFactura.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/timeline.png"))); // NOI18N
        jbSeleccionarFactura.setText("Seleccionar Comprobante");
        jbSeleccionarFactura.setVisible(false);
        jbSeleccionarFactura.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbSeleccionarFacturaActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel12)
                    .addComponent(jlTipoDocumento)
                    .addComponent(jLabel13))
                .addGap(7, 7, 7)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jtDireccion)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addComponent(jtRUC, javax.swing.GroupLayout.PREFERRED_SIZE, 172, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jbSeleccionarFactura)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jcbMotivo, 0, 1, Short.MAX_VALUE))
                    .addComponent(jtNombreContrinuyente))
                .addContainerGap())
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jlTipoDocumento)
                        .addComponent(jtRUC, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jButton3, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jbSeleccionarFactura)
                        .addComponent(jcbMotivo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel12)
                    .addComponent(jtNombreContrinuyente, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel13)
                    .addComponent(jtDireccion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 0, Short.MAX_VALUE))
        );

        jcbMotivo.setVisible(false);

        jLabel14.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel14.setText("Fecha Emisión");

        jdFechaEmision.setFocusable(false);
        jdFechaEmision.setMaxSelectableDate(new java.util.Date());
        jdFechaEmision.setMinSelectableDate(new java.util.Date(946706464000L));
        jdFechaEmision.setNextFocusableComponent(jtProductos);

        jLabel15.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel15.setText("Moneda:");

        jcbMoneda.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "PEN", "USD" }));
        jcbMoneda.setFocusable(false);
        jcbMoneda.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jcbMonedaActionPerformed(evt);
            }
        });

        jtSerieGuia.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        jtSerieGuia.setEnabled(false);

        jtnumGuia.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        jtnumGuia.setEnabled(false);

        jrbDocReferencia.setText("Guía de Remisión");
        jrbDocReferencia.setFocusable(false);
        jrbDocReferencia.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jrbDocReferenciaActionPerformed(evt);
            }
        });

        jLabel29.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel29.setText("Descuento");
        jLabel29.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);

        jsDescuento.setModel(new javax.swing.SpinnerNumberModel(0, 0, 100, 1));
        jsDescuento.setFocusTraversalKeysEnabled(false);
        jsDescuento.setFocusable(false);
        jsDescuento.setNextFocusableComponent(jtProductos);
        jsDescuento.setRequestFocusEnabled(false);
        jsDescuento.setValue(0);
        jsDescuento.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                jsDescuentoStateChanged(evt);
            }
        });

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jrbDocReferencia, javax.swing.GroupLayout.PREFERRED_SIZE, 142, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(15, 15, 15))
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addGap(35, 35, 35)
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel14, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(jPanel7Layout.createSequentialGroup()
                                .addComponent(jLabel15, javax.swing.GroupLayout.PREFERRED_SIZE, 101, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, Short.MAX_VALUE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)))
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addComponent(jtSerieGuia, javax.swing.GroupLayout.PREFERRED_SIZE, 76, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jtnumGuia))
                    .addComponent(jdFechaEmision, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addComponent(jcbMoneda, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel29, javax.swing.GroupLayout.DEFAULT_SIZE, 68, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jsDescuento, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel14))
                    .addComponent(jdFechaEmision, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jcbMoneda, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel29)
                        .addComponent(jsDescuento, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jLabel15, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jtSerieGuia, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jtnumGuia, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addComponent(jrbDocReferencia, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(2, 2, 2)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );

        jPanel3.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanel3.setPreferredSize(new java.awt.Dimension(482, 152));

        jPanel8.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jLabel19.setText("I.G.V. :");

        jlMonIGV.setText("S/.");

        jlIGV.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jlIGV.setText("0.00");

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel19, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jlMonIGV)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jlIGV, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jlMonIGV)
                        .addComponent(jlIGV))
                    .addComponent(jLabel19))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel9.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jLabel20.setText("Descuento:");

        jlDescuento.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jlDescuento.setText("0.00");

        jlMonTotal.setText("S/.");

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel20, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jlMonTotal)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jlDescuento, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jlMonTotal)
                        .addComponent(jlDescuento))
                    .addComponent(jLabel20))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel11.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jLabel18.setText("Sub Total");

        jlMonSubTotal.setText("S/.");

        jlSubTotal.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jlSubTotal.setText("0 .00");

        javax.swing.GroupLayout jPanel11Layout = new javax.swing.GroupLayout(jPanel11);
        jPanel11.setLayout(jPanel11Layout);
        jPanel11Layout.setHorizontalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel18)
                .addGap(18, 18, 18)
                .addComponent(jlMonSubTotal)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jlSubTotal, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel11Layout.setVerticalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel18)
                    .addComponent(jlMonSubTotal)
                    .addComponent(jlSubTotal))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel12.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jLabel17.setFont(new java.awt.Font("Lucida Grande", 1, 14)); // NOI18N
        jLabel17.setText("SON:");

        javax.swing.GroupLayout jPanel12Layout = new javax.swing.GroupLayout(jPanel12);
        jPanel12.setLayout(jPanel12Layout);
        jPanel12Layout.setHorizontalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel12Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel17)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jlNumeroLetras, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel12Layout.setVerticalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel12Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel12Layout.createSequentialGroup()
                        .addComponent(jLabel17)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(jlNumeroLetras, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        jLabel16.setText("Descripción");

        jtaObservacion.setColumns(20);
        jtaObservacion.setRows(5);
        jtaObservacion.setNextFocusableComponent(jtProductos);
        jtaObservacion.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jtaObservacionKeyTyped(evt);
            }
        });
        jScrollPane2.setViewportView(jtaObservacion);

        jPanel13.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jLabel23.setFont(new java.awt.Font("Lucida Grande", 1, 13)); // NOI18N
        jLabel23.setText("TOTAL:");

        jlTotal.setFont(new java.awt.Font("Lucida Grande", 1, 13)); // NOI18N
        jlTotal.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jlTotal.setText("0.00");

        jlMonTotal2.setFont(new java.awt.Font("Lucida Grande", 1, 13)); // NOI18N
        jlMonTotal2.setText("S/.");

        javax.swing.GroupLayout jPanel13Layout = new javax.swing.GroupLayout(jPanel13);
        jPanel13.setLayout(jPanel13Layout);
        jPanel13Layout.setHorizontalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel13Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel23, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jlMonTotal2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jlTotal, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel13Layout.setVerticalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel13Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jlMonTotal2)
                        .addComponent(jlTotal))
                    .addComponent(jLabel23))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jLabel16)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(jScrollPane2)
                    .addComponent(jPanel12, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel9, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel8, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel11, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel13, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jPanel12, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel11, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jLabel16)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 77, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel13, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(10, 10, 10))
        );

        jPanel4.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jtProductos.setFont(new java.awt.Font("AppleGothic", 0, 12)); // NOI18N
        jtProductos.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Cod", "Descripción", "Cantidad", "Unid Medida", "Precio Unit.", "IGV", "Impuesto", "Valor Venta"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.Object.class, java.lang.Float.class, java.lang.Object.class, java.lang.Float.class, java.lang.Boolean.class, java.lang.Float.class, java.lang.Object.class
            };
            boolean[] canEdit = new boolean [] {
                false, true, true, true, true, true, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jtProductos.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jtProductosKeyReleased(evt);
            }
        });
        jScrollPane1.setViewportView(jtProductos);
        if (jtProductos.getColumnModel().getColumnCount() > 0) {
            jtProductos.getColumnModel().getColumn(0).setMinWidth(120);
            jtProductos.getColumnModel().getColumn(0).setPreferredWidth(120);
            jtProductos.getColumnModel().getColumn(0).setMaxWidth(120);
            jtProductos.getColumnModel().getColumn(1).setPreferredWidth(270);
            jtProductos.getColumnModel().getColumn(2).setMinWidth(60);
            jtProductos.getColumnModel().getColumn(2).setPreferredWidth(60);
            jtProductos.getColumnModel().getColumn(2).setMaxWidth(60);
            jtProductos.getColumnModel().getColumn(3).setMinWidth(90);
            jtProductos.getColumnModel().getColumn(3).setPreferredWidth(90);
            jtProductos.getColumnModel().getColumn(3).setMaxWidth(90);
            jtProductos.getColumnModel().getColumn(4).setMinWidth(100);
            jtProductos.getColumnModel().getColumn(4).setPreferredWidth(100);
            jtProductos.getColumnModel().getColumn(4).setMaxWidth(100);
            jtProductos.getColumnModel().getColumn(5).setMinWidth(30);
            jtProductos.getColumnModel().getColumn(5).setPreferredWidth(30);
            jtProductos.getColumnModel().getColumn(5).setMaxWidth(30);
            jtProductos.getColumnModel().getColumn(6).setMinWidth(60);
            jtProductos.getColumnModel().getColumn(6).setPreferredWidth(60);
            jtProductos.getColumnModel().getColumn(6).setMaxWidth(60);
            jtProductos.getColumnModel().getColumn(7).setMinWidth(100);
            jtProductos.getColumnModel().getColumn(7).setPreferredWidth(100);
            jtProductos.getColumnModel().getColumn(7).setMaxWidth(100);
        }

        jbAgregarProdcuto.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/plus.png"))); // NOI18N
        jbAgregarProdcuto.setToolTipText("Agregar producto");
        jbAgregarProdcuto.setFocusable(false);
        jbAgregarProdcuto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbAgregarProdcutoActionPerformed(evt);
            }
        });

        jbEliminarProducto.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/remove.png"))); // NOI18N
        jbEliminarProducto.setToolTipText("Eliminar producto");
        jbEliminarProducto.setFocusable(false);
        jbEliminarProducto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbEliminarProductoActionPerformed(evt);
            }
        });

        jLabel21.setText("Cantidad de productos");

        jlCantProductos.setFont(new java.awt.Font("Lucida Grande", 0, 14)); // NOI18N
        jlCantProductos.setText("0");

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                        .addComponent(jLabel21)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jlCantProductos)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jbAgregarProdcuto)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jbEliminarProducto)))
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 126, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jbAgregarProdcuto)
                    .addComponent(jbEliminarProducto)
                    .addComponent(jLabel21)
                    .addComponent(jlCantProductos)))
        );

        jToolBar1.setRollover(true);

        jButton8.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/search.png"))); // NOI18N
        jButton8.setToolTipText("Buscar Factura");
        jButton8.setFocusable(false);
        jButton8.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton8.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButton8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton8ActionPerformed(evt);
            }
        });
        jToolBar1.add(jButton8);

        jbGuardarComprobante.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/save.png"))); // NOI18N
        jbGuardarComprobante.setToolTipText("Guardar");
        jbGuardarComprobante.setFocusable(false);
        jbGuardarComprobante.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jbGuardarComprobante.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jbGuardarComprobante.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbGuardarComprobanteActionPerformed(evt);
            }
        });
        jToolBar1.add(jbGuardarComprobante);

        jbEliminar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/trash.png"))); // NOI18N
        jbEliminar.setToolTipText("Eliminar");
        jbEliminar.setFocusable(false);
        jbEliminar.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jbEliminar.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jbEliminar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbEliminarActionPerformed(evt);
            }
        });
        jToolBar1.add(jbEliminar);

        jbValidarSunat.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/sunat_logo.png"))); // NOI18N
        jbValidarSunat.setToolTipText("Validar Sunat");
        jbValidarSunat.setFocusable(false);
        jbValidarSunat.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jbValidarSunat.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jbValidarSunat.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbValidarSunatActionPerformed(evt);
            }
        });
        jToolBar1.add(jbValidarSunat);

        jbEnviarEmail.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/email.png"))); // NOI18N
        jbEnviarEmail.setToolTipText("Enviar al email");
        jbEnviarEmail.setFocusable(false);
        jbEnviarEmail.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jbEnviarEmail.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jbEnviarEmail.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbEnviarEmailActionPerformed(evt);
            }
        });
        jToolBar1.add(jbEnviarEmail);

        jbImprimir.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/printer.png"))); // NOI18N
        jbImprimir.setToolTipText("Imprimir");
        jbImprimir.setFocusable(false);
        jbImprimir.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jbImprimir.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jbImprimir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbImprimirActionPerformed(evt);
            }
        });
        jToolBar1.add(jbImprimir);
        jToolBar1.add(filler1);

        jLabel28.setFont(new java.awt.Font("AppleGothic", 0, 14)); // NOI18N
        jLabel28.setText("Estado: ");
        jToolBar1.add(jLabel28);

        jlEstado.setFont(new java.awt.Font("AppleGothic", 1, 14)); // NOI18N
        jlEstado.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jlEstado.setText("Estado");
        jlEstado.setEnabled(false);
        jToolBar1.add(jlEstado);

        jLabel27.setFont(new java.awt.Font("AppleGothic", 0, 14)); // NOI18N
        jLabel27.setText("    Condición:");
        jToolBar1.add(jLabel27);

        jlCondicion.setFont(new java.awt.Font("AppleGothic", 1, 14)); // NOI18N
        jlCondicion.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jlCondicion.setText("Condición");
        jlCondicion.setEnabled(false);
        jToolBar1.add(jlCondicion);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jToolBar1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 921, Short.MAX_VALUE)
                            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, 921, Short.MAX_VALUE)
                            .addComponent(jPanel4, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jToolBar1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, 162, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void jtRUCKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jtRUCKeyTyped
        // TODO add your handling code here:
        char enter = evt.getKeyChar();
        if(!Character.isDigit(enter)){
            evt.consume();
            return;
        }
        if(tipoDocumento.equals("6") )
            if(jtRUC.getText().length()>10){
                evt.consume();
            }
        if(tipoDocumento.equals("1") )
            if(jtRUC.getText().length()>7){
                evt.consume();
            }
    }//GEN-LAST:event_jtRUCKeyTyped

    private void jrbDocReferenciaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jrbDocReferenciaActionPerformed
        if(jrbDocReferencia.isSelected()){
            jtSerieGuia.setEnabled(true);
            jtnumGuia.setEnabled(true);
        }
        else{
            jtSerieGuia.setEnabled(false);
            jtnumGuia.setEnabled(false); 
        }
    }//GEN-LAST:event_jrbDocReferenciaActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        Clientes viewCliente=new Clientes(empresa.getIdEmpresa(),tipoDocumento,this);
        viewCliente.setVisible(true);
    }//GEN-LAST:event_jButton3ActionPerformed
    
    public void asignarCliente(Cliente newCliente){
        cliente=newCliente;
        jtNombreContrinuyente.setText(cliente.getNombre());
        jtDireccion.setText(cliente.getDireccion());
        jtRUC.setText(cliente.getNumero());
        jtDireccion.setEditable(false);
        jtNombreContrinuyente.setEditable(false);
    }
    
    private void jtRUCKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jtRUCKeyPressed
       if(evt.getKeyCode()==Event.ENTER){
           setCursor(new Cursor(Cursor.WAIT_CURSOR));
           jtRUC.setCursor(new Cursor(Cursor.WAIT_CURSOR));
           consultarDatos();
           jtRUC.setCursor(new Cursor(Cursor.TEXT_CURSOR));
           setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
        }
    }//GEN-LAST:event_jtRUCKeyPressed

    private void jcbMonedaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jcbMonedaActionPerformed
        int ind=jcbMoneda.getSelectedIndex();
        if(ind==0){
            moneda="PEN";
            jlMonIGV.setText("S/.");
            jlMonSubTotal.setText("S/.");
            jlMonTotal.setText("S/.");
            jlMonTotal2.setText("S/.");
        }
        else if(ind==1){
            moneda="USD";
            jlMonIGV.setText("$");
            jlMonSubTotal.setText("$");
            jlMonTotal.setText("$");
            jlMonTotal2.setText("$");
        }
        comprobante.setMoneda(moneda);
        
    }//GEN-LAST:event_jcbMonedaActionPerformed

    private void jbAgregarProdcutoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbAgregarProdcutoActionPerformed
        agregarProductoTabla();
    }//GEN-LAST:event_jbAgregarProdcutoActionPerformed

    private void jbEliminarProductoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbEliminarProductoActionPerformed
        eliminarProducto();
    }//GEN-LAST:event_jbEliminarProductoActionPerformed

    private void jbEliminarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbEliminarActionPerformed
        int dialogResult = JOptionPane.showConfirmDialog (null, "¿Seguro que desea eliminar el comprobante?","Warning",JOptionPane.INFORMATION_MESSAGE);
        if(dialogResult == JOptionPane.YES_OPTION){
            if(jlEstadoFactura.getText().equals("VALIDADO")) JOptionPane.showMessageDialog(null, "No se puede eliminar un comprobante validado", "Error al eliminar", JOptionPane.ERROR_MESSAGE);
            else eliminarComprobante();
        }
    }//GEN-LAST:event_jbEliminarActionPerformed

    private void jtaObservacionKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jtaObservacionKeyTyped
        if(jtaObservacion.getText().length()>255){
            evt.consume();
        }
    }//GEN-LAST:event_jtaObservacionKeyTyped

    private void jbGuardarComprobanteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbGuardarComprobanteActionPerformed
        guardarComprobante();
    }//GEN-LAST:event_jbGuardarComprobanteActionPerformed

    private void jButton8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton8ActionPerformed
        BuscarComprobante bc=new BuscarComprobante(empresa.getIdEmpresa(), tipoComprobante,this,false);
        bc.setVisible(true);
    }//GEN-LAST:event_jButton8ActionPerformed

    private void jbImprimirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbImprimirActionPerformed
        setCursor(new Cursor(Cursor.WAIT_CURSOR));
        //guardo=false;
        //guardarComprobante();
        JasperReport jasperReport;
        JasperPrint jasperPrint;
        //URL  in=this.getClass().getClassLoader().getResource( "Reportes/Comprobante.jrxml" );
        String pathReport = System.getProperty("user.dir")+"/Reportes/Comprobante.jrxml"; 
        //URL urlLogo=this.getClass().getResource(empresa.getLogo());
        try {
           jasperReport=JasperCompileManager.compileReport(pathReport);
            //se procesa el archivo jasper
           Map parametros = new HashMap();
           parametros.put("TipoComprobante_idTipoComprobante", tipoComprobante);
           parametros.put("ComprobantePago_serieComprobante", comprobante.getSerieComprobante());
           parametros.put("Empresa_idEmpresa", empresa.getIdEmpresa());
           parametros.put("logoEmpresa", System.getProperty("user.dir")+empresa.getLogo());
           parametros.put("logoImagen",System.getProperty("user.dir")+"/Images/Plantilla-Planeta-Hojas.jpg");
           parametros.put("montoLetras",jlNumeroLetras.getText());
           parametros.put("marcadorAgua",System.getProperty("user.dir")+"/Images/marcador"+empresa.getIdEmpresa()+".jpg");
           jasperPrint = JasperFillManager.fillReport(jasperReport, parametros, conn );
           //impresion de reporte
           // TRUE: muestra la ventana de dialogo "preferencias de impresion"
           JasperPrintManager.printReport(jasperPrint, true); 
           setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
        } catch (JRException ex) {
            Logger.getLogger(Comprobante.class.getName()).log(Level.SEVERE, null, ex);
        }
           
    }//GEN-LAST:event_jbImprimirActionPerformed

    private void jbEnviarEmailActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbEnviarEmailActionPerformed
        setCursor(new Cursor(Cursor.WAIT_CURSOR));
        //guardo=false;
        //guardarComprobante();
        //if(!jlEstadoFactura.getText().equals("GUARDADO"))return;
        String path=guardarPDF();
        if(path==null){
            JOptionPane.showMessageDialog(null, "Ocurrió un problema al intentar guardar el archivo");
            return;
        }
        if(correoEmpresa.getEmail().length()==0){
            JOptionPane.showMessageDialog(this, "No se ha regitrado un email a esta empresa");
            return;
        }
        if(cliente.getEmail()==null){
            cliente.setEmail(JOptionPane.showInputDialog(null, "Ingrese email para envío de constancia al cliente"));
            actualizarEmail(cliente.getEmail());
        }
        ViewEmailSender ves=new ViewEmailSender(correoEmpresa, cliente.getEmail(), path);
        setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
        ves.setVisible(true);
    }//GEN-LAST:event_jbEnviarEmailActionPerformed

    private void jbValidarSunatActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbValidarSunatActionPerformed
        setCursor(new Cursor(Cursor.WAIT_CURSOR));
        if(jlEstadoFactura.getText().equals("VALIDADO")){
            JOptionPane.showMessageDialog(null, "El comprobante ya ha sido validado");
            return;
        }
        guardo=false;
        guardarComprobante();
        String fileRpt=generarXML();
        int rpt=-1;
        if(fileRpt!=null)rpt=leerRespuesta(fileRpt);
        if(rpt==0){
            jbEnviarEmail.setEnabled(true);
            jbImprimir.setEnabled(true);
            jbGuardarComprobante.setEnabled(false);
            jbEliminar.setEnabled(false);
            jbValidarSunat.setEnabled(false);
            validarBDComprobante();
            jlEstadoFactura.setForeground(new Color(30, 132, 73));
            jlEstadoFactura.setText("VALIDADO");
        }
        setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
    }//GEN-LAST:event_jbValidarSunatActionPerformed

    private void jsDescuentoStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_jsDescuentoStateChanged
        actualizarTablaProductos();
    }//GEN-LAST:event_jsDescuentoStateChanged

    private void jbSeleccionarFacturaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbSeleccionarFacturaActionPerformed
        if(jtRUC.getText().length()==0){
            JOptionPane.showMessageDialog(this,"Debe seleccionar primero un cliente");
            return;
        }
        String tempTipoCombronate;
        if(jtRUC.getText().length()==8)tempTipoCombronate="B";
        else tempTipoCombronate="F";
        BuscarComprobante bc=new BuscarComprobante(empresa.getIdEmpresa(), tempTipoCombronate, this,true);
        bc.setBusqueda(cliente.getNombre());
        bc.setVisible(true);
    }//GEN-LAST:event_jbSeleccionarFacturaActionPerformed
    
    private void jtProductosKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jtProductosKeyReleased
       detectarBarcoder(evt);
    }//GEN-LAST:event_jtProductosKeyReleased

    private void jtRUCKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jtRUCKeyReleased
        detectarBarcoder(evt);
    }//GEN-LAST:event_jtRUCKeyReleased
    private void detectarBarcoder(java.awt.event.KeyEvent evt){
         if(evt.getWhen()-lastTime<300 || lastTime==0){
            lastTime=evt.getWhen();
            if(evt.getKeyCode() == KeyEvent.VK_ENTER) {
                barcodeSearch();
                barcode=new String();
                lastTime=0;
            } else {
                if(Character.isDigit(evt.getKeyChar())){
                    barcode+= evt.getKeyChar();
                    evt.consume();
                }
            }
        }
        else{
            barcode=new String();
            lastTime=0;
        }
    }
    private int leerRespuesta(String nameFile){
        try {
            if(!new File("../Respuestas SUNAT").isDirectory())new File("../Respuestas SUNAT").mkdirs();
            Zip.UnZip(nameFile+".zip", System.getProperty("user.dir")+"/../Respuestas SUNAT");
            new File(nameFile+".zip").delete();
            //System.out.println(VerifySignature.validarCertificado("Responses/"+nameFile+".xml"));
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance ( );
            Document documento = null;
            DocumentBuilder builder = factory.newDocumentBuilder();
            documento = builder.parse( new File("../Respuestas SUNAT/"+nameFile+".xml") );
            Element codeResponse=(Element) documento.getElementsByTagName("cbc:ResponseCode").item(0);
            Element description=(Element) documento.getElementsByTagName("cbc:Description").item(0);
            JOptionPane.showMessageDialog(null, description.getTextContent());
            return Integer.parseInt(codeResponse.getTextContent());
        } catch (Exception ex) {
            Logger.getLogger(Comprobante.class.getName()).log(Level.SEVERE, null, ex);
        }
        return -1;
    }


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.Box.Filler filler1;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton8;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel29;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JPanel jPanel13;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JToolBar jToolBar1;
    private javax.swing.JButton jbAgregarProdcuto;
    private javax.swing.JButton jbEliminar;
    private javax.swing.JButton jbEliminarProducto;
    private javax.swing.JButton jbEnviarEmail;
    private javax.swing.JButton jbGuardarComprobante;
    private javax.swing.JButton jbImprimir;
    private javax.swing.JButton jbSeleccionarFactura;
    private javax.swing.JButton jbValidarSunat;
    private javax.swing.JComboBox jcbMoneda;
    public javax.swing.JComboBox<String> jcbMotivo;
    private com.toedter.calendar.JDateChooser jdFechaEmision;
    private javax.swing.JLabel jlCantProductos;
    private javax.swing.JLabel jlCondicion;
    private javax.swing.JLabel jlDescuento;
    private javax.swing.JLabel jlDireccion;
    private javax.swing.JLabel jlEmail;
    private javax.swing.JLabel jlEstado;
    private javax.swing.JLabel jlEstadoFactura;
    private javax.swing.JLabel jlIGV;
    private javax.swing.JLabel jlLogo;
    private javax.swing.JLabel jlMonIGV;
    private javax.swing.JLabel jlMonSubTotal;
    private javax.swing.JLabel jlMonTotal;
    private javax.swing.JLabel jlMonTotal2;
    private javax.swing.JLabel jlNombreComercial;
    private javax.swing.JLabel jlNumFactura;
    private javax.swing.JLabel jlNumeroLetras;
    private javax.swing.JLabel jlRUCEmpresa;
    private javax.swing.JLabel jlSubTotal;
    private javax.swing.JLabel jlTelefono;
    private javax.swing.JLabel jlTipoComprobante;
    public javax.swing.JLabel jlTipoDocumento;
    private javax.swing.JLabel jlTotal;
    private javax.swing.JLabel jlUbigeo;
    public javax.swing.JRadioButton jrbDocReferencia;
    public javax.swing.JSpinner jsDescuento;
    private javax.swing.JTextField jtDireccion;
    private javax.swing.JTextField jtNombreContrinuyente;
    private javax.swing.JTable jtProductos;
    private javax.swing.JTextField jtRUC;
    public javax.swing.JTextField jtSerieGuia;
    private javax.swing.JTextArea jtaObservacion;
    public javax.swing.JTextField jtnumGuia;
    // End of variables declaration//GEN-END:variables

    private void consultarDatos() {
        if(tipoDocumento.equals("6"))
            if(jtRUC.getText().length()<11){
                if(jtRUC.getText().length()!=8){
                    JOptionPane.showMessageDialog(null, "El campo RUC debe tener 11 dígitos");
                    return;
                }
            }
        if(tipoDocumento.equals("1"))
            if(jtRUC.getText().length()<8){
                JOptionPane.showMessageDialog(null, "El campo DNI debe tener 8 dígitos");
                return;
            }
        try {
            
            HttpURLConnection connection = (HttpURLConnection) new  URL("http://py-devs.com/api/"+getNombreDocumento(tipoDocumento)+"/"+jtRUC.getText()).openConnection();            
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Content-Type", 
                       "application/x-www-form-urlencoded");
            connection.setRequestProperty("Content-Language", "en-US"); 
            connection.setRequestProperty("User-Agent",
                    "Mozilla/5.0 (Windows NT 5.1) AppleWebKit/535.11 (KHTML, like Gecko) Chrome/17.0.963.56 Safari/535.11");
            connection.setUseCaches(false);
            connection.setDoInput(true);
            connection.setDoOutput(true);
            InputStream is = connection.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            String line = null;
            Boolean band=false;
            String data="";
            while ((line = br.readLine()) != null) {
                if(line.equals("</span>{")){
                    band=true;
                    continue;
                }
                if(band){
                    if(line.equals("}</pre>"))break;
                    String temp=line.replaceAll("&quot;","\"");
                    data+=temp;
                    //System.out.println(temp);
                }
            }
            is.close();
            datos=data.split(",");
            if(tipoDocumento.equals("6")){
                cliente.setNumero(jtRUC.getText());
                cliente.setDireccion(datos[7].substring(25,datos[7].length()-1));                   
                cliente.setNombre(datos[10].substring(15,datos[10].length()-1));
                this.asignarCliente(cliente);
                String estado=datos[5].substring(29,datos[5].length()-1);
                String condicion=datos[6].substring(32,datos[6].length()-1);
                jlEstado.setText(estado);
                jlCondicion.setText(" "+condicion);
                jlEstado.setEnabled(true);
                jlCondicion.setEnabled(true);
                if(estado.equals("ACTIVO"))jlEstado.setForeground(Color.blue);
                else jlEstado.setForeground(Color.red);
                if(condicion.equals("HABIDO"))jlCondicion.setForeground(Color.blue);
                else jlCondicion.setForeground(Color.red);
                    
            }
            else if(tipoDocumento.equals("1")){
                cliente.setNumero(jtRUC.getText());
                cliente.setNombre(datos[1].substring(16,datos[1].length()-1)+" "+datos[2].substring(20,datos[2].length()-1)+" "+datos[3].substring(20,datos[3].length()-1));
                this.asignarCliente(cliente);
            }
        } catch (MalformedURLException ex) {
            JOptionPane.showMessageDialog(null, "No existe Contribuyente");
        } catch (ProtocolException ex) {
            Logger.getLogger(Comprobante.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            //Logger.getLogger(Comprobante.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(null, "No existe conexión a internet ,los datos se ingresarán manualmente");
            reiniciarFactura();
            jtDireccion.setEditable(true);
            jtNombreContrinuyente.setEditable(true);
        }
    }

    private void reiniciarFactura() {
        jtNombreContrinuyente.setText("");
        jtDireccion.setText("");
        jlEstado.setEnabled(false);
        jlCondicion.setEnabled(false);
    }

   

    private void eventosTabla() {
        jtProductos.addMouseListener(new MouseAdapter() {
        @Override
        public void mousePressed(MouseEvent me) {
            JTable table =(JTable) me.getSource();
            Point p = me.getPoint();
            int row = table.rowAtPoint(p);
            int column = table.columnAtPoint(p);
            if (me.getClickCount() == 2 && column==0) {
                String moneda=(String) jcbMoneda.getItemAt(jcbMoneda.getSelectedIndex());
                Productos prod=new Productos(jtProductos,empresa.getIdEmpresa(),row,COMPRA,VENTA,moneda);
                prod.setVisible(true);
            }
        }
        @Override
        public void mouseClicked(MouseEvent me){
            JTable table =(JTable) me.getSource();
            Point p = me.getPoint();
            int row = table.rowAtPoint(p);
            int column = table.columnAtPoint(p);
            if(column==5){
                boolean val=(boolean) jtProductos.getValueAt(row,5);
                float temp=(float)jtProductos.getValueAt(row, 2)*(float)jtProductos.getValueAt(row,4)*IGVSUNAT;
                if(val)jtProductos.setValueAt(temp, row, 6);
                else jtProductos.setValueAt(0.00, row, 6);
                actualizarTablaProductos();
            }
        }
        });
        jtProductos.addKeyListener(new KeyListener() {
            @Override
            public void keyPressed(KeyEvent e) {
                JTable table =(JTable) e.getSource();
                int row = table.getSelectedRow();
                if(e.getKeyCode()==Event.ENTER){
                   actualizarTablaProductos();
                }
            }

            @Override
            public void keyTyped(KeyEvent e) {
                
            }

            @Override
            public void keyReleased(KeyEvent e) {
               
            }
        });
    }
    private void actualizarTablaProductos(){
        DefaultTableModel dtm= (DefaultTableModel) jtProductos.getModel();
        float valVenta;
        float cant;
        float precio;
        float igv;
        float totalVenta=0;
        float igvTotal=0;
        for(int i=0; i<dtm.getRowCount();i++){
            cant=    Float.parseFloat(String.valueOf(dtm.getValueAt(i, 2)));
            precio=  Float.parseFloat(String.valueOf(dtm.getValueAt(i, 4)));
            Boolean val=(Boolean) jtProductos.getValueAt(i,5);
            if(val)igv=cant*precio*IGVSUNAT;
            else igv=0;
            jtProductos.setValueAt(igv, i, 6);
            igvTotal+=igv;
            valVenta=cant*precio;
            totalVenta+=valVenta;
            dtm.setValueAt(valVenta, i, 7);
        }
        float descuento=(totalVenta*(int)jsDescuento.getValue())/100;
        jlSubTotal.setText(format.format(totalVenta));
        jlDescuento.setText(format.format(descuento));
        //int igv=(int) jsIGV.getValue();
        //float igvTotalLabel;
        //MODIFICAR PARA NOTAS DE CREDITO Y DEBITO
        //if(tipoComprobante.equals("F"))  
            igvTotal   =this.getSumIGV();
        //else igvTotal=(float) 0.0;
        jlIGV.setText(format.format(igvTotal));
        float total=totalVenta+igvTotal - descuento;
        jlTotal.setText(format.format(total));
        String numero=NumberToLetterConverter.convertNumberToLetter(total, jcbMoneda.getSelectedIndex());
        jlNumeroLetras.setText(numero);
    }
    public static double redondearDecimales(double valorInicial, int numeroDecimales) {
        double parteEntera, resultado;
        resultado = valorInicial;
        parteEntera = Math.floor(resultado);
        resultado=(resultado-parteEntera)*Math.pow(10, numeroDecimales);
        resultado=Math.round(resultado);
        resultado=(resultado/Math.pow(10, numeroDecimales))+parteEntera;
        return resultado;
    }
    private void guardarComprobante() {
        if(!verficarComprobante())return;
        verificarCliente();
        if(verificarProductos())return;
        agregarProductosComprobantes();
        jbValidarSunat.setEnabled(true);
        jbEliminar.setEnabled(true);

    }

    private Boolean verficarComprobante() {
        if(jtNombreContrinuyente.getText().length()==0){
            JOptionPane.showMessageDialog(this, "No asignado ningún cliente", "Cliente", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        if(jtProductos.getRowCount()==0){
            JOptionPane.showMessageDialog(this, "No tiene productos asignados", "Productos", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        if(!verificarTabla()){
            JOptionPane.showMessageDialog(this, "Uno de los productos no tiene todos los datos", "Productos", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        if(jrbDocReferencia.isSelected() && jtSerieGuia.getText().length()==0 && jtnumGuia.getText().length()==0){
            JOptionPane.showMessageDialog(this, "Complete los datos de la Guía de remisión", "Guía Remisión", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        if((tipoComprobante.equals("C") || tipoComprobante.equals("D")) && jtaObservacion.getText().length()==0 ){
            JOptionPane.showMessageDialog(this, "Debe completar la descripción del comprobante", "Descripción", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        return true;
    }
    //compruba errores sintácticos de la tabla como semánticos
    private boolean verificarTabla() {
        for(int i=0;i<jtProductos.getRowCount();i++){
            if(jtProductos.getValueAt(i, 1).equals("")){
                return false;
            }
            if(Float.parseFloat(String.valueOf(jtProductos.getValueAt(i, 2)))==0.0f){
                return false;
            }
            if(jtProductos.getValueAt(i, 3).equals("")){
                return false;
            }
            if((float)jtProductos.getValueAt(i, 4)==0.0f){
                return false;
            }
            if(String.valueOf(jtProductos.getValueAt(i, 5)).equals("0.00")){
                return false;
            }
        }
        return true;
    }

    private void verificarCliente() {
        PreparedStatement stmt;
        int num=0;
        try {
            //Verfica si el cliente está asignado a la empresa 
            stmt = conn.prepareStatement("SELECT count(*) as num from empresa_has_cliente where Cliente_numero=? and Empresa_idEmpresa=?");
            stmt.setString(1, jtRUC.getText());
            stmt.setInt(2, empresa.getIdEmpresa());
            ResultSet rs=stmt.executeQuery();
            if(rs.next())num=rs.getInt(1);
            //System.out.println(num);
            if(num==0){
                //comprueba si el cliente existe 
                stmt = conn.prepareStatement("SELECT count(*) as num from cliente where numero=?");
                stmt.setString(1, jtRUC.getText());
                rs=stmt.executeQuery();
                if(rs.next())num=rs.getInt(1);
                //si el cliente no existe se agrega uno nuevo 
                if(num==0)insertarCliente();
                //se asigna a la empresa
                asginarAEmpresa(empresa.getIdEmpresa(),jtRUC.getText());
            }
            
            
        } catch (SQLException ex) {
            Logger.getLogger(Comprobante.class.getName()).log(Level.SEVERE, null, ex);
        }
           
    }

    private void insertarCliente() {
        PreparedStatement stmt;
        try {
            stmt = conn.prepareStatement("INSERT  INTO cliente (numero,tipoDocumento,direccion,email,nombre) values(?,?,?,?,?)");
            stmt.setString(1, jtRUC.getText());
            stmt.setString(2, tipoDocumento);
            stmt.setString(3, jtDireccion.getText());
            String email=JOptionPane.showInputDialog(this, "Ingrese email para envío de constancia al cliente");
            stmt.setString(4, email);
            stmt.setString(5, jtNombreContrinuyente.getText());
            stmt.executeUpdate();
            //nuevo cliente
            cliente=new Cliente();
            cliente.setNombre( jtNombreContrinuyente.getText());
            cliente.setNumero(jtRUC.getText());
            cliente.setTipoDocumento(tipoDocumento);
            cliente.setDireccion(jtDireccion.getText());
            cliente.setEmail(email);
        } catch (SQLException ex) {
            Logger.getLogger(Comprobante.class.getName()).log(Level.SEVERE, null, ex);
        }
       
    }    
    private void actualizarEmail(String email){
         PreparedStatement stmt;
        try {
            stmt = conn.prepareStatement("update cliente set email=? where numero=? and tipoDocumento=?");
            stmt.setString(1, email);
            stmt.setString(2, jtRUC.getText());
            stmt.setString(3, tipoDocumento);
            stmt.executeUpdate();
            //actaulizar cliente
            cliente=new Cliente();
            cliente.setNombre( jtNombreContrinuyente.getText());
            cliente.setNumero(jtRUC.getText());
            cliente.setTipoDocumento(tipoDocumento);
            cliente.setDireccion(jtDireccion.getText());
            cliente.setEmail(email);
        } catch (SQLException ex) {
            Logger.getLogger(Comprobante.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    private void datosIniciales() {
        Date date =new Date();
        //datos de comprobante
        comprobante.setFechaEmision(date);
        comprobante.setMoneda(moneda);//por defecto siempre será PEN notación internacional
        jdFechaEmision.setDate(date);
        //datos de membrete de factura
        jlLogo.setIcon(new ImageIcon(getClass().getResource(empresa.getLogo())));
        jlNombreComercial.setText(empresa.getNombreComercial());
        jlDireccion.setText(empresa.getDireccionFiscal());
        jlUbigeo.setText(this.getUbigeo());
        jlEmail.setText(correoEmpresa.getEmail());
        jlTelefono.setText(empresa.getTelefono());
        jlRUCEmpresa.setText("RUC "+empresa.getRucEmpresa());
        switch (tipoComprobante){
            case "F": jlTipoComprobante.setText("FACTURA ELECTRÓNICA");tipoDocumento="6";break;
            case "B": jlTipoComprobante.setText("BOLETA ELECTRÓNICA");tipoDocumento="1";break;
            case "C": jlTipoComprobante.setText("NOTA DE CRÉDITO");
                      jtRUC.setEditable(false);
                      jtRUC.setBackground(new Color(254,191,36));
                      jbSeleccionarFactura.setVisible(true);
                      break;
            case "D": jlTipoComprobante.setText("NOTA DE DÉBITO");
                      jtRUC.setEditable(false);
                      jtRUC.setBackground(new Color(254,191,36));
                      jbSeleccionarFactura.setVisible(true);
                      break;
        }
        //genera el último numero de comprobante 
        generarNumComprobante();  
        jbEliminar.setEnabled(false);
        jbValidarSunat.setEnabled(false);
        jbEnviarEmail.setEnabled(false);
        jbImprimir.setEnabled(false);
        String patternThreeDecimalPoints = "0.00";
        DecimalFormatSymbols simbolos = new DecimalFormatSymbols();
        simbolos.setDecimalSeparator('.');
        simbolos.setPerMill(',');
        format = new DecimalFormat(patternThreeDecimalPoints,simbolos);
        format.setRoundingMode(RoundingMode.DOWN);
    }
    private String getUbigeo() {
        PreparedStatement stmt;
        String ubigeo="";
        try {
            stmt = conn.prepareStatement("SELECT nombreUbigeo(?) as ubigeo");
            stmt.setString(1,empresa.getUbigeo());
            ResultSet rs=stmt.executeQuery();
            if(rs.next())ubigeo=rs.getString("ubigeo");
        } catch (SQLException ex) {
            Logger.getLogger(Comprobante.class.getName()).log(Level.SEVERE, null, ex);
        }
        return ubigeo;
    }

    private void generarNumComprobante() {
       PreparedStatement stmt;
        numComprobante="00100000000";
        try {
            stmt = conn.prepareStatement("select seriecomprobante from ComprobantePago where Empresa_idEmpresa=? and TipoComprobante_idTipoComprobante=? order by seriecomprobante desc limit 1");
            stmt.setInt(1,empresa.getIdEmpresa());
            stmt.setString(2, tipoComprobante);
            ResultSet rs=stmt.executeQuery();
            if(rs.next())numComprobante=rs.getString("seriecomprobante");
        } catch (SQLException ex) {
            Logger.getLogger(Comprobante.class.getName()).log(Level.SEVERE, null, ex);
        }
        long tempNum=Long.parseLong(numComprobante);
        tempNum++;
        numComprobante=String.valueOf(tempNum);
        while(numComprobante.length()<11){
            numComprobante="0".concat(numComprobante);
        }
        comprobante.setSerieComprobante(numComprobante);
        jlNumFactura.setText(tipoComprobante+numComprobante.substring(0,3)+"-"+numComprobante.substring(3, 11));
        
    }

    private void asginarAEmpresa(int idEmpresa, String numero) {
        PreparedStatement stmt;
        try {
            stmt = conn.prepareStatement("INSERT into Empresa_has_Cliente  values(?,?)");
            stmt.setInt(1, idEmpresa);
            stmt.setString(2, numero);
            stmt.executeUpdate();
            
        } catch (SQLException ex) {
            Logger.getLogger(Comprobante.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void eventosTeclado() {
        //Agrega un  nuevo producto si se presiona la tecla '+'
        KeyStroke keyAdd=KeyStroke.getKeyStroke(com.sun.glass.events.KeyEvent.VK_PLUS, 0,true);
        Action performAdd = new AbstractAction("Add") { 
            public void actionPerformed(ActionEvent e) {     
                agregarProductoTabla();
            }
        };
        jbAgregarProdcuto.getActionMap().put("Add", performAdd);
        jbAgregarProdcuto.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(keyAdd, "Add");
        //Elimina un  producto si se presiona la tecla 'supr'
        KeyStroke keyDelete=KeyStroke.getKeyStroke(com.sun.glass.events.KeyEvent.VK_DELETE, 0,true);
        Action performDelete = new AbstractAction("Delete") { 
            public void actionPerformed(ActionEvent e) {     
                eliminarProducto();
            }
        };
        jbEliminarProducto.getActionMap().put("Delete", performDelete);
        jbEliminarProducto.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(keyDelete, "Delete");
    }

    private void agregarProductoTabla() {
        DefaultTableModel dtm= (DefaultTableModel) jtProductos.getModel();
        Object[] object = {"Automático","",0,"UND",0,true,0.0,0.0};
        dtm.addRow(object);
        TableColumn col = jtProductos.getColumnModel().getColumn(3);
        JComboBox jcbUM= new JComboBox(new String[] {"UND","KG","METROS","LITROS","METROS 2","METROS 3"});
        jcbUM.setSelectedIndex(0);
        col.setCellEditor(new DefaultCellEditor(jcbUM));
        jtProductos.setAutoCreateRowSorter(true);
        actualizarNumeroProductos();
         focusTable();
    }
    public void focusTable(){
        //Focus
        jtProductos.requestFocus();
        jtProductos.changeSelection(jtProductos.getRowCount()-1,0,false, false);
    }
    private String getInternationalizeUnit(int row){
        String internationalUnit=null;
        String[] unidades= {"UND","KG","METROS","LITROS","METROS 2","METROS 3"};
        String[] valInternationalUnit={"NIU","KGM","LTR","MTR","MTK","D90"};
        String temp=(String) jtProductos.getValueAt(row, 3);
        for(int i=0;i<unidades.length;i++){
            if(temp.equals(unidades[i]))return valInternationalUnit[i];
        }
        return internationalUnit;
    }
    private boolean verificarProductos() {
        DefaultTableModel dtm= (DefaultTableModel) jtProductos.getModel();
        for(int i=0;i<dtm.getRowCount();i++){
            if((float)dtm.getValueAt(i, 2)==0 || (float)dtm.getValueAt(i,4)==0){
                int tempRow=i+1;
                JOptionPane.showMessageDialog(null, "El producto "+tempRow+" tiene una cantidad igual a" + dtm.getValueAt(i, 2));
                return true;
            }
            if(String.valueOf(dtm.getValueAt(i,0)).equals("Automático"))agregarProductoBase(i);
            consultarProducto((String)dtm.getValueAt(i,0),i);
            
        }
        return false;
    }

    private void consultarProducto(String tempProducto,int row) {
        PreparedStatement stmt;
        int numProducto = 0;
        try {
            stmt = conn.prepareStatement("select count(*) from producto where idProducto=?");
            stmt.setString(1, tempProducto);
            ResultSet rs=stmt.executeQuery();
            if(rs.next())numProducto=rs.getInt(1);
            if(numProducto==0)agregarProductoBase(tempProducto,row);
            actualizarProductoEmpresa(tempProducto, row);
        } catch (SQLException ex) {
            Logger.getLogger(Comprobante.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    private String actualizarProductoEmpresa(String idProducto,int row){
        String numProducto=null;
        PreparedStatement stmt;
        try {
            //Eliminar el producto para luego modificarlo
            stmt = conn.prepareStatement("DELETE FROM EMPRESA_HAS_PRODUCTO WHERE  EMPRESA_IDEMPRESA=? AND PRODUCTO_IDPRODUCTO=?");
            stmt.setInt(1,empresa.getIdEmpresa());
            stmt.setString(2, idProducto);
            stmt.executeUpdate();
            //Inserta el producto
            stmt = conn.prepareStatement("Insert into Empresa_has_Producto  values(?,?,?,?,?) ");
            stmt.setInt(1, empresa.getIdEmpresa());
            stmt.setString(2, idProducto);
            stmt.setFloat(3, (float) jtProductos.getValueAt(row, 4));
            stmt.setString(4, (String) jtProductos.getValueAt(row,3));
            stmt.setString(5, String.valueOf(jcbMoneda.getSelectedItem()));
            stmt.executeUpdate();
            stmt = conn.prepareStatement("SELECT @@identity AS idProducto");
            ResultSet rs=stmt.executeQuery();
            if(rs.next()){
                jtProductos.setValueAt(idProducto,row,0);
                numProducto=rs.getString(1);
            }
            
        } catch (SQLException ex) {
            Logger.getLogger(Comprobante.class.getName()).log(Level.SEVERE, null, ex);
        }
        return numProducto;
    }
    private void agregarProductoBase(String idProducto,int row) {
        PreparedStatement stmt;
        try {
            stmt = conn.prepareStatement("Insert into producto values(?,?) ");
            stmt.setString(1, idProducto);
            stmt.setString(2, String.valueOf(jtProductos.getValueAt(row, 1)));
            stmt.executeUpdate();
            stmt = conn.prepareStatement("SELECT @@identity AS idProducto");
            ResultSet rs=stmt.executeQuery();
            if(rs.next())jtProductos.setValueAt(rs.getInt(1),row,0);
        } catch (SQLException ex) {
            Logger.getLogger(Comprobante.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    private void agregarProductoBase(int row) {
        PreparedStatement stmt;
        try {
            stmt = conn.prepareStatement("Insert into producto values(?,?) ");
            stmt.setString(1, "0");
            stmt.setString(2, String.valueOf(jtProductos.getValueAt(row, 1)));
            stmt.executeUpdate();
            stmt = conn.prepareStatement("SELECT idProducto FROM producto order by idProducto desc limit 1;");
            ResultSet rs=stmt.executeQuery();
            if(rs.next())jtProductos.setValueAt(rs.getString(1),row,0);
        } catch (SQLException ex) {
            Logger.getLogger(Comprobante.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    private void nuevoComprobante(){
        PreparedStatement stmt; 
        String mon=(String) jcbMoneda.getItemAt(jcbMoneda.getSelectedIndex());
        generarNumComprobante();
        try {
            stmt = conn.prepareStatement("insert into ComprobantePago (serieComprobante,saldoBruto,impuesto,total,moneda,fechaEmision,observacion,Empresa_idEmpresa,Cliente_numero,TipoComprobante_idTipoComprobante,Cuenta_idCuenta,descuentoGlobal,motivo) values(?,?,?,?,?,?,?,?,?,?,?,?,?)");
            stmt.setString(1,numComprobante);
            stmt.setFloat(2, Float.parseFloat(jlSubTotal.getText()));
            stmt.setFloat(3, Float.parseFloat(jlIGV.getText()));
            stmt.setFloat(4, Float.parseFloat(jlTotal.getText()));
            stmt.setString(5, mon);
            Date date = jdFechaEmision.getDate();
            stmt.setDate(6, new java.sql.Date(date.getTime()));
            stmt.setString(7, jtaObservacion.getText());
            stmt.setInt(8, empresa.getIdEmpresa());
            stmt.setString(9, cliente.getNumero());
            stmt.setString(10, tipoComprobante);
            stmt.setInt(11, idCuenta);
            stmt.setInt(12, (int) jsDescuento.getValue());
            stmt.setInt(13, jcbMotivo.getSelectedIndex()+1);
            stmt.executeUpdate();
            agregarGuiRemision(stmt);
        } catch (SQLException ex) {
            Logger.getLogger(Comprobante.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    private void agregarGuiRemision(PreparedStatement stmt) throws SQLException{
        stmt = conn.prepareStatement("update ComprobantePago set serieguia=?, numguia=? where seriecomprobante=? and Empresa_idEmpresa=? and TipoComprobante_idTipoComprobante=?");            
        if(jrbDocReferencia.isSelected()){
            stmt.setString(1,jtSerieGuia.getText());
            stmt.setString(2, jtnumGuia.getText());
        }
        else{
            stmt.setString(1,null);
            stmt.setString(2, null);
        }
        stmt.setString(3,comprobante.getSerieComprobante());
        stmt.setInt(4, empresa.getIdEmpresa());
        stmt.setString(5, tipoComprobante);
        stmt.executeUpdate();                
        
    }
    private void modificarComprobante(){
        PreparedStatement stmt; 
        String mon=(String) jcbMoneda.getItemAt(jcbMoneda.getSelectedIndex());
        try {
            stmt = conn.prepareStatement("update ComprobantePago set saldoBruto=?,impuesto=?,total=?,moneda=?,fechaEmision=?,observacion=?,Cliente_numero=?,descuentoGlobal=?, motivo=? where serieComprobante=? and Empresa_idEmpresa=? and TipoComprobante_idTipoComprobante=?");            
            stmt.setFloat(1, Float.parseFloat(jlSubTotal.getText()));
            stmt.setFloat(2, Float.parseFloat(jlIGV.getText()));
            stmt.setFloat(3, Float.parseFloat(jlTotal.getText()));
            stmt.setString(4, mon);
            Date date = jdFechaEmision.getDate();
            stmt.setDate(5, new java.sql.Date(date.getTime()));
            stmt.setString(6, jtaObservacion.getText()); 
            stmt.setString(7, cliente.getNumero());
            stmt.setInt(8, (int)jsDescuento.getValue());
            stmt.setString(9,comprobante.getSerieComprobante());
            stmt.setInt(10, empresa.getIdEmpresa());
            stmt.setString(11, tipoComprobante);
            stmt.setInt(12, jcbMotivo.getSelectedIndex()+1);
            stmt.executeUpdate();
            agregarGuiRemision(stmt);
        } catch (SQLException ex) {
            Logger.getLogger(Comprobante.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    private void agregarProductosComprobantes() {
        actualizarTablaProductos();
        if(jlEstadoFactura.getText().equals("BORRADOR"))nuevoComprobante();
        else if(jlEstadoFactura.getText().equals("GUARDADO"))modificarComprobante();
        else{
            JOptionPane.showMessageDialog(null, "No se puede modificar un comprobante validado");
            return;
        }
        nuevosProductos();
        if(guardo)JOptionPane.showMessageDialog(null, "Se guardó correctamente");
        guardo=true;
        jlEstadoFactura.setForeground(Color.BLUE);
        jlEstadoFactura.setText("GUARDADO");
        
    }
    private void nuevosProductos(){
        PreparedStatement stmt; 
        DefaultTableModel dtm=(DefaultTableModel) jtProductos.getModel(); 
        try {
            for(int i=0;i<dtm.getRowCount();i++){
                    stmt = conn.prepareStatement("delete from  Producto_has_ComprobantePago where Producto_idProducto=? and ComprobantePago_serieComprobante=? and ComprobantePago_Empresa_idEmpresa=? and ComprobantePago_TipoComprobante_idTipoComprobante=?"); 
                    stmt.setString(1, String.valueOf(dtm.getValueAt(i,0 )));
                    stmt.setString(2,comprobante.getSerieComprobante());
                    stmt.setInt(3, empresa.getIdEmpresa());
                    stmt.setString(4, tipoComprobante);
                    stmt.executeUpdate(); 
                    stmt = conn.prepareStatement("insert into Producto_has_ComprobantePago values(?,?,?,?,?,?)"); 
                    stmt.setString(1, String.valueOf(dtm.getValueAt(i,0 )));
                    stmt.setString(2,comprobante.getSerieComprobante());
                    stmt.setInt(3, empresa.getIdEmpresa());
                    stmt.setString(4, tipoComprobante);
                    stmt.setFloat(5, (float) dtm.getValueAt(i, 2));
                    stmt.setFloat(6, (float) dtm.getValueAt(i, 6));//No hay descuento
                    stmt.executeUpdate();    
            }
        } catch (SQLException ex) {
                Logger.getLogger(Comprobante.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public void cargarComprobante(){
        if(comprobante.getEstado().equals("GUARDADO"))jlEstadoFactura.setForeground(Color.blue);
        else if(comprobante.getEstado().equals("VALIDADO"))jlEstadoFactura.setForeground(new Color(30, 132, 73));
        else jlEstadoFactura.setForeground(Color.red);
        jlEstadoFactura.setText(comprobante.getEstado());
        String tempNumFactura=tipoComprobante+comprobante.getSerieComprobante().substring(0,3)+"-"+comprobante.getSerieComprobante().substring(3,11);
        jlNumFactura.setText(tempNumFactura);
        jdFechaEmision.setDate(comprobante.getFechaEmision());
        jcbMoneda.setSelectedItem(comprobante.getMoneda());
        jtaObservacion.setText(comprobante.getObservacion());
        jcbMotivo.setSelectedIndex(comprobante.getMotivo()-1);
        if(comprobante.getSerieGuia()!=null){
            jtSerieGuia.setText(comprobante.getSerieGuia());
            jtnumGuia.setText(comprobante.getNumGuia());
            jrbDocReferencia.setSelected(true);
            jtSerieGuia.setEnabled(true);
            jtnumGuia.setEnabled(true);
        }
        else {
            jrbDocReferencia.setSelected(false);
            jtSerieGuia.setEnabled(false);
            jtnumGuia.setEnabled(false);
        }
        if(tipoComprobante.equals("C") || tipoComprobante.equals("D")){
            jtSerieGuia.setEnabled(false);
            jtnumGuia.setEnabled(false);
        }
        //Cliente
        jtRUC.setText(cliente.getNumero());
        jtNombreContrinuyente.setText(cliente.getNombre());
        jtDireccion.setText(cliente.getDireccion());
        
        tabbed.setTitleAt(tabbed.getSelectedIndex(), cliente.getNombre().substring(0, 10));
        if(jlEstadoFactura.getText().equals("GUARDADO")){
            jbValidarSunat.setEnabled(true);
            jbEnviarEmail.setEnabled(false);
            jbImprimir.setEnabled(false);
            jbEliminar.setEnabled(true);
            jbGuardarComprobante.setEnabled(true);
        }
        else{
            jbEnviarEmail.setEnabled(true);
            jbImprimir.setEnabled(true);
            jbEliminar.setEnabled(false);
            jbValidarSunat.setEnabled(false);
            jbGuardarComprobante.setEnabled(false);
        }
        jsDescuento.setValue(comprobante.getDescuentoGlobal());
        float tempDescuento=comprobante.getDescuentoGlobal()*comprobante.getSaldoBruto()/100;
        jlDescuento.setText(String.valueOf(tempDescuento));
    }

    private void eliminarProducto() {
        int row=jtProductos.getSelectedRow();
        if(row>=0){
            DefaultTableModel dmt= (DefaultTableModel) jtProductos.getModel();
            dmt.removeRow(row);
            int temp=Integer.parseInt(jlCantProductos.getText());
            temp--;
            jlCantProductos.setText(String.valueOf(temp));
            actualizarTablaProductos();
        }
        else JOptionPane.showMessageDialog(null, "Debe seleccionar un producto");
    }

    private void actualizarNumeroProductos() {
        int temp=jtProductos.getRowCount();
        //temp++;
        jlCantProductos.setText(String.valueOf(temp));
    }
    private String getNombreDocumento(String doc){
        String nombre = null;
        if(doc.equals("1"))nombre="dni";
        else if(doc.equals("6"))nombre="ruc";
        return nombre;
    }

    private void eliminarComprobante() {
       PreparedStatement stmt;
        try {
            stmt = conn.prepareStatement("DELETE FROM ComprobantePago WHERE SERIECOMPROBANTE=? AND EMPRESA_IDEMPRESA=? AND TIPOCOMPROBANTE_IDTIPOCOMPROBANTE=?");
            stmt.setString(1,comprobante.getSerieComprobante());
            stmt.setInt(2, empresa.getIdEmpresa());
            stmt.setString(3, tipoComprobante);
            stmt.executeUpdate();
            tabbed.remove(tabbed.getSelectedIndex());
        } catch (SQLException ex) {
            Logger.getLogger(Comprobante.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private String guardarPDF() {
        String path=null;
        JasperReport jasperReport;
        JasperPrint jasperPrint;
        String pathReport = System.getProperty("user.dir")+"/Reportes/Comprobante.jrxml"; 
        try {
            jasperReport=JasperCompileManager.compileReport(pathReport);
            //se procesa el archivo jasper
           Map parametros = new HashMap();
           parametros.put("TipoComprobante_idTipoComprobante", tipoComprobante);
           parametros.put("ComprobantePago_serieComprobante", comprobante.getSerieComprobante());
           parametros.put("Empresa_idEmpresa", empresa.getIdEmpresa());
           parametros.put("logoEmpresa", System.getProperty("user.dir")+empresa.getLogo());
           parametros.put("logoImagen",System.getProperty("user.dir")+"/Images/Plantilla-Planeta-Hojas.jpg");
           parametros.put("montoLetras",jlNumeroLetras.getText());
           parametros.put("marcadorAgua",System.getProperty("user.dir")+"/Images/marcador"+empresa.getIdEmpresa()+".jpg");
           jasperPrint = JasperFillManager.fillReport(jasperReport, parametros, conn );
           File pdf = File.createTempFile(tipoComprobante+comprobante.getSerieComprobante(), ".pdf");
           JasperExportManager.exportReportToPdfStream(jasperPrint, new FileOutputStream(pdf));
           path=pdf.getAbsolutePath();
        } catch (JRException ex) {
            Logger.getLogger(Comprobante.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Comprobante.class.getName()).log(Level.SEVERE, null, ex);
        }
        return path;
    }

    private void datosCorreo() {
        PreparedStatement stmt;
        correoEmpresa= new CorreoEmpresa();
        try {
            stmt = conn.prepareStatement("select * from CorreoEmpresa where Empresa_idEmpresa=?");
            stmt.setInt(1, empresa.getIdEmpresa());
            ResultSet rs=stmt.executeQuery();
            while(rs.next()){
                correoEmpresa.setEmail(rs.getString("email"));
                correoEmpresa.setPasswordEmail(rs.getBytes("passwordEmail"));
                correoEmpresa.setHost(rs.getString("host"));
                correoEmpresa.setPort(rs.getString("port"));
                correoEmpresa.setStarttls(rs.getString("starttls"));
                correoEmpresa.setEmpresaIdEmpresa(rs.getInt("Empresa_idEmpresa"));
            }
        } catch (SQLException ex) {
            Logger.getLogger(Comprobante.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    private String numComprobante(String tipoComprobante){
        String numComprobante=null;
        PreparedStatement stmt;
        correoEmpresa= new CorreoEmpresa();
        try {
            stmt = conn.prepareStatement("select numTipoComprobante from TipoComprobante where idTipoComprobante=?");
            stmt.setString(1, tipoComprobante);
            ResultSet rs=stmt.executeQuery();
            if(rs.next())numComprobante=rs.getString("numTipoComprobante");
        } catch (SQLException ex) {
            Logger.getLogger(Comprobante.class.getName()).log(Level.SEVERE, null, ex);
        }
        return numComprobante;
    }
    private float getOperacionGravadas(){
        float ventasGravadas=0.0f;
        for(int i=0;i<jtProductos.getRowCount();i++){
            boolean temp=(boolean) jtProductos.getValueAt(i, 5);
            if(temp){
                float tempOper=(float)jtProductos.getValueAt(i, 2)*(float)jtProductos.getValueAt(i,4);
                ventasGravadas+=tempOper;
            }
        }
        return ventasGravadas;
    }
    private float getSumIGV(){
        float sumIGV=0.0f;
        for(int i=0;i<jtProductos.getRowCount();i++){
            boolean temp=(boolean) jtProductos.getValueAt(i, 5);
            if(temp){
                float tempOper=(float)jtProductos.getValueAt(i, 6);
                sumIGV+=tempOper;
            }
        }
        return sumIGV;
    }
    private float getOperacionExoneradas(){
        float ventasGravadas=0.0f;
        for(int i=0;i<jtProductos.getRowCount();i++){
            boolean temp=(boolean) jtProductos.getValueAt(i, 5);
            if(!temp){
                float tempOper=(float)jtProductos.getValueAt(i, 2)*(float)jtProductos.getValueAt(i,4);
                ventasGravadas+=tempOper;
            }
        }
        return ventasGravadas;
    }
    private String getXMLTipoDocumento(String tipoComp){
        String xmlRoot=null;
        if(tipoComp.equals("C"))xmlRoot="CreditNote";
        else if(tipoComp.equals("D"))xmlRoot="DebitNote";
        else xmlRoot="Invoice";
        return xmlRoot;
    }
    private String generarXML() {
        try {
            String root=getXMLTipoDocumento(tipoComprobante);
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setNamespaceAware(true);
            DocumentBuilder builder = factory.newDocumentBuilder();
            DOMImplementation implementation = builder.getDOMImplementation();
            
            Document document;
            if(tipoComprobante.equals("F") || tipoComprobante.equals("B"))document = implementation.createDocument("urn:oasis:names:specification:ubl:schema:xsd:Invoice-2", root, null);
            else if(tipoComprobante.equals("C"))document = implementation.createDocument("urn:oasis:names:specification:ubl:schema:xsd:CreditNote-2", root, null);
            else document = implementation.createDocument("urn:oasis:names:specification:ubl:schema:xsd:DebitNote-2", root, null);
            document.setXmlVersion("1.0");
            //<Invoice>
            Element raiz = document.getDocumentElement();
            //atributos de Invoice
            
            //raiz.setAttributeNS("http://www.w3.org/2000/xmlns/","xmlns","urn:oasis:names:specification:ubl:schema:xsd:Invoice-2");
            raiz.setAttributeNS("http://www.w3.org/2000/xmlns/","xmlns:cac","urn:oasis:names:specification:ubl:schema:xsd:CommonAggregateComponents-2");
            raiz.setAttributeNS("http://www.w3.org/2000/xmlns/","xmlns:cbc","urn:oasis:names:specification:ubl:schema:xsd:CommonBasicComponents-2");
            raiz.setAttributeNS("http://www.w3.org/2000/xmlns/","xmlns:ccts","urn:un:unece:uncefact:documentation:2");
            raiz.setAttributeNS("http://www.w3.org/2000/xmlns/","xmlns:ds","http://www.w3.org/2000/09/xmldsig#");
            raiz.setAttributeNS("http://www.w3.org/2000/xmlns/","xmlns:ext","urn:oasis:names:specification:ubl:schema:xsd:CommonExtensionComponents-2");
            raiz.setAttributeNS("http://www.w3.org/2000/xmlns/","xmlns:qdt","urn:oasis:names:specification:ubl:schema:xsd:QualifiedDatatypes-2");
            raiz.setAttributeNS("http://www.w3.org/2000/xmlns/","xmlns:sac","urn:sunat:names:specification:ubl:peru:schema:xsd:SunatAggregateComponents-1");
            raiz.setAttributeNS("http://www.w3.org/2000/xmlns/","xmlns:udt","urn:un:unece:uncefact:data:specification:UnqualifiedDataTypesSchemaModule:2");
            raiz.setAttributeNS("http://www.w3.org/2000/xmlns/","xmlns:xsi","http://www.w3.org/2001/XMLSchema-instance");
            
            //Total ventas sin gravar
            Element extUBLExtensions =document.createElement("ext:UBLExtensions");
            Element extUBLExtension =document.createElement("ext:UBLExtension");
            Element extExtensionContent =document.createElement("ext:ExtensionContent");
            Element additionalInformation=document.createElement("sac:AdditionalInformation");
            
            Element sacAdditionalMonetaryTotalOperGrav =document.createElement("sac:AdditionalMonetaryTotal");
            Element cbcIDOperGrav =document.createElement("cbc:ID");
            Text valuecbcIDOperGrav = document.createTextNode("1001");
            cbcIDOperGrav.appendChild(valuecbcIDOperGrav);
            Element cbcPayableAmountOperGrav =document.createElement("cbc:PayableAmount");
            cbcPayableAmountOperGrav.setAttribute("currencyID", (String) jcbMoneda.getSelectedItem());
            Text valuecbcPayableAmountOperGrav = document.createTextNode(String.valueOf(this.getOperacionGravadas()));
            cbcPayableAmountOperGrav.appendChild(valuecbcPayableAmountOperGrav);
            sacAdditionalMonetaryTotalOperGrav.appendChild(cbcIDOperGrav);
            sacAdditionalMonetaryTotalOperGrav.appendChild(cbcPayableAmountOperGrav);
            
            Element sacAdditionalMonetaryTotalOperExo =document.createElement("sac:AdditionalMonetaryTotal");
            Element cbcIDOperExo =document.createElement("cbc:ID");
            Text valuecbcIDOperExo = document.createTextNode("1003");
            cbcIDOperExo.appendChild(valuecbcIDOperExo);
            Element cbcPayableAmountOperExo =document.createElement("cbc:PayableAmount");
            cbcPayableAmountOperExo.setAttribute("currencyID", (String) jcbMoneda.getSelectedItem());
            Text valuecbcPayableAmountOperExo = document.createTextNode(String.valueOf(this.getOperacionExoneradas()));
            cbcPayableAmountOperExo.appendChild(valuecbcPayableAmountOperExo);
            sacAdditionalMonetaryTotalOperExo.appendChild(cbcIDOperExo);
            sacAdditionalMonetaryTotalOperExo.appendChild(cbcPayableAmountOperExo);
            
            additionalInformation.appendChild(sacAdditionalMonetaryTotalOperGrav);
            additionalInformation.appendChild(sacAdditionalMonetaryTotalOperExo);
            extExtensionContent.appendChild(additionalInformation);
            
            extUBLExtension.appendChild(extExtensionContent); 
            extUBLExtensions.appendChild(extUBLExtension);
            
/**************************************Firma del XML ****************************************************************************************/
            Element extUBLExtensionFirma =document.createElement("ext:UBLExtension");
            Element extExtensionContentFirma =document.createElement("ext:ExtensionContent");
            extUBLExtensionFirma.appendChild(extExtensionContentFirma);
            extUBLExtensions.appendChild(extUBLExtensionFirma);
            
/**************************************FIN DE LA FIRMA  ****************************************************************************************/
            raiz.appendChild(extUBLExtensions);
            //Versión UBL
            Element ublVersionID = document.createElement("cbc:UBLVersionID");
            Text valueUBLVersionID = document.createTextNode("2.0");
            ublVersionID.appendChild(valueUBLVersionID);
            raiz.appendChild(ublVersionID);
            
            //Versión Estructura del documento
            Element documentoVersion = document.createElement("cbc:CustomizationID");
            Text valueDocumentoVersion = document.createTextNode("1.0");
            documentoVersion.appendChild(valueDocumentoVersion);
            raiz.appendChild(documentoVersion);
            //Número de Comprobante
            Element IDITC = document.createElement("cbc:ID");
            Text valueIDITC;/* El número de comprobante lo delimita el tipo de comprobante al cual modifica */
            if(tipoComprobante.equals("F") || tipoComprobante.equals("B"))valueIDITC = document.createTextNode(jlNumFactura.getText());
            else valueIDITC = document.createTextNode(jtSerieGuia.getText().substring(0,1)+ jlNumFactura.getText().substring(1));
            IDITC.appendChild(valueIDITC);
            raiz.appendChild(IDITC);
            
            //Fecha Emision
            Date date = jdFechaEmision.getDate();
            Element fechaEmision = document.createElement("cbc:IssueDate");
            Text valueFechaEmision = document.createTextNode(new java.sql.Date(date.getTime()).toString());
            fechaEmision.appendChild(valueFechaEmision);
            raiz.appendChild(fechaEmision);
            
            //Tipo de Comprobante
            if(tipoComprobante.equals("F") || tipoComprobante.equals("B")){
                Element InvoiceTypeCode = document.createElement("cbc:InvoiceTypeCode");
                Text valueInvoiceTypeCode = document.createTextNode(numTipoComprobante(tipoComprobante));
                InvoiceTypeCode.appendChild(valueInvoiceTypeCode);
                raiz.appendChild(InvoiceTypeCode);
            }
            
            //Tipo de Moneda
            Element DocumentCurrencyCode = document.createElement("cbc:DocumentCurrencyCode");
            Text valueDocumentCurrencyCode = document.createTextNode(String.valueOf(jcbMoneda.getSelectedItem()));
            DocumentCurrencyCode.appendChild(valueDocumentCurrencyCode);
            raiz.appendChild(DocumentCurrencyCode);
            
            if(tipoComprobante.equals("C") || tipoComprobante.equals("D")){
                Element cacDiscrepancyResponse=document.createElement("cac:DiscrepancyResponse");
                Element cbcReferenceID=document.createElement("cbc:ReferenceID");
                Text valueCbcReferenceID=document.createTextNode(jtSerieGuia.getText()+"-"+jtnumGuia.getText());
                cbcReferenceID.appendChild(valueCbcReferenceID);
                Element cbcResponseCode=document.createElement("cbc:ResponseCode");
                int tempValueSelected=jcbMotivo.getSelectedIndex()+1;
                Text valueCbcResponseCode=document.createTextNode("0"+String.valueOf(tempValueSelected));
                cbcResponseCode.appendChild(valueCbcResponseCode);
                Element cbcDescription=document.createElement("cbc:Description");
                Text valuecbcDescription=document.createTextNode(jtaObservacion.getText());
                cbcDescription.appendChild(valuecbcDescription);
                cacDiscrepancyResponse.appendChild(cbcReferenceID);
                cacDiscrepancyResponse.appendChild(cbcResponseCode);
                cacDiscrepancyResponse.appendChild(cbcDescription);
                raiz.appendChild(cacDiscrepancyResponse);
                
                Element cacBillingReference=document.createElement("cac:BillingReference");
                Element cacInvoiceDocumentReference=document.createElement("cac:InvoiceDocumentReference");
                Element cbcID=document.createElement("cbc:ID");
                Text valuecacBillingReference=document.createTextNode(jtSerieGuia.getText()+"-"+jtnumGuia.getText());
                cbcID.appendChild(valuecacBillingReference);
                Element cbcDocumentTypeCode=document.createElement("cbc:DocumentTypeCode");
                Text valuecbcDocumentTypeCode=document.createTextNode(numTipoComprobante(jtSerieGuia.getText().substring(0, 1)));
                cbcDocumentTypeCode.appendChild(valuecbcDocumentTypeCode);
                cacInvoiceDocumentReference.appendChild(cbcID);
                cacInvoiceDocumentReference.appendChild(cbcDocumentTypeCode);
                cacBillingReference.appendChild(cacInvoiceDocumentReference);
                raiz.appendChild(cacBillingReference);
            }
            //Detalles de la firma
            Element cacSignature=document.createElement("cac:Signature");
            Element cbcIdSignature=document.createElement("cbc:ID");
            Text valueCbcIdSignature=document.createTextNode("IDTeslaCer");
            cbcIdSignature.appendChild(valueCbcIdSignature);
            Element cacSignatoryParty=document.createElement("cac:SignatoryParty");
            Element cacPartyIdentification=document.createElement("cac:PartyIdentification");
            Element cbcId=document.createElement("cbc:ID");
            Text valueCbcId=document.createTextNode(empresa.getRucEmpresa());
            cbcId.appendChild(valueCbcId);
            cacPartyIdentification.appendChild(cbcId);
            cacSignatoryParty.appendChild(cacPartyIdentification);
            Element cacPartyName=document.createElement("cac:PartyName");
            Element cbcName=document.createElement("cbc:Name");
            Text valueCbcName=document.createTextNode(empresa.getNombreComercial());
            cbcName.appendChild(valueCbcName);
            cacPartyName.appendChild(cbcName);
            cacSignatoryParty.appendChild(cacPartyName);
            Element cacDigitalSignatureAttachment=document.createElement("cac:DigitalSignatureAttachment");
            Element cacExternalReference=document.createElement("cac:ExternalReference");
            Element cbcURI=document.createElement("cbc:URI");
            Text valueCbcURI=document.createTextNode("#TeslaCer");
            cbcURI.appendChild(valueCbcURI);
            cacExternalReference.appendChild(cbcURI);
            cacDigitalSignatureAttachment.appendChild(cacExternalReference);
            cacSignature.appendChild(cbcIdSignature);
            cacSignature.appendChild(cacSignatoryParty);
            cacSignature.appendChild(cacDigitalSignatureAttachment);
            raiz.appendChild(cacSignature);
            
            //Datos de contribuyente
            Element AccountingSupplierParty = document.createElement("cac:AccountingSupplierParty");
            Element CustomerAssignedAccountID = document.createElement("cbc:CustomerAssignedAccountID");
            Text valueCustomerAssignedAccountID = document.createTextNode(empresa.getRucEmpresa());
            CustomerAssignedAccountID.appendChild(valueCustomerAssignedAccountID);
            AccountingSupplierParty.appendChild(CustomerAssignedAccountID);
            
            Element AdditionalAccountID = document.createElement("cbc:AdditionalAccountID");
            Text valueAdditionalAccountID = document.createTextNode("6");
            AdditionalAccountID.appendChild(valueAdditionalAccountID);
            AccountingSupplierParty.appendChild(AdditionalAccountID);
            
            Element Party = document.createElement("cac:Party");
            Element PostalAddress = document.createElement("cac:PostalAddress");
            Element ID = document.createElement("cbc:ID");
            Text valueID = document.createTextNode(empresa.getUbigeo());
            ID.appendChild(valueID);
            PostalAddress.appendChild(ID);
            Element StreetName = document.createElement("cbc:StreetName");
            Text valueStreetName = document.createTextNode(empresa.getDireccionFiscal());
            StreetName.appendChild(valueStreetName);
            PostalAddress.appendChild(StreetName);

            
            Element CityName = document.createElement("cbc:CityName");
            Text valueCityName = document.createTextNode(Empresa.getProvincia(jlUbigeo.getText()));
            CityName.appendChild(valueCityName);
            PostalAddress.appendChild(CityName);
            
            Element CountrySubentity = document.createElement("cbc:CountrySubentity");
            Text valueCountrySubentity = document.createTextNode(Empresa.getDepartamento(jlUbigeo.getText()));
            CountrySubentity.appendChild(valueCountrySubentity);
            PostalAddress.appendChild(CountrySubentity);
            
            Element District = document.createElement("cbc:District");
            Text valueDistrict = document.createTextNode(Empresa.getDistrito(jlUbigeo.getText()));
            District.appendChild(valueDistrict);
            PostalAddress.appendChild(District);
            
            Element Country = document.createElement("cac:Country");
            Element IdentificationCode = document.createElement("cbc:IdentificationCode");
            Text valueIdentificationCode = document.createTextNode("PE");
            IdentificationCode.appendChild(valueIdentificationCode);
            
            Country.appendChild(IdentificationCode);
            PostalAddress.appendChild(Country);
            Party.appendChild(PostalAddress);   
            
            Element PartyLegalEntity = document.createElement("cac:PartyLegalEntity");
            Element RegistrationName = document.createElement("cbc:RegistrationName");
            Text valueRegistrationName = document.createTextNode(empresa.getNombreComercial());
            RegistrationName.appendChild(valueRegistrationName);
            PartyLegalEntity.appendChild(RegistrationName);
            Party.appendChild(PartyLegalEntity);
            
            AccountingSupplierParty.appendChild(Party);
            raiz.appendChild(AccountingSupplierParty);
            
            //Datos del cliente
            Element AccountingCustomerParty = document.createElement("cac:AccountingCustomerParty");
            
            Element CustomerAssignedAccountIDACP = document.createElement("cbc:CustomerAssignedAccountID");
            Text valueCustomerAssignedAccountIDACP = document.createTextNode(cliente.getNumero());
            CustomerAssignedAccountIDACP.appendChild(valueCustomerAssignedAccountIDACP);
            AccountingCustomerParty.appendChild(CustomerAssignedAccountIDACP);
            
            Element AdditionalAccountIDACP = document.createElement("cbc:AdditionalAccountID");
            Text valueAdditionalAccountIDACP = document.createTextNode(String.valueOf(cliente.getTipoDocumento()));
            AdditionalAccountIDACP.appendChild(valueAdditionalAccountIDACP);
            AccountingCustomerParty.appendChild(AdditionalAccountIDACP);
            
            Element PartyACP = document.createElement("cac:Party");
            Element PartyLegalEntityACP = document.createElement("cac:PartyLegalEntity");
            Element RegistrationNameACP = document.createElement("cbc:RegistrationName");
            Text valueRegistrationNameACP = document.createTextNode(cliente.getNombre());
            RegistrationNameACP.appendChild(valueRegistrationNameACP);
            PartyLegalEntityACP.appendChild(RegistrationNameACP);
            
            PartyACP.appendChild(PartyLegalEntityACP);
            AccountingCustomerParty.appendChild(PartyACP);
            raiz.appendChild(AccountingCustomerParty);
            
            //Total de Impuestos
            Element TaxTotalGen = document.createElement("cac:TaxTotal");
            Element TaxAmountGen = document.createElement("cbc:TaxAmount");
            TaxAmountGen.setAttribute("currencyID", (String)jcbMoneda.getSelectedItem());
            Text valueTaxAmountGen = document.createTextNode(jlIGV.getText());
            TaxAmountGen.appendChild(valueTaxAmountGen);
            TaxTotalGen.appendChild(TaxAmountGen);
            
            Element TaxSubtotalGen = document.createElement("cac:TaxSubtotal");
            
            Element TaxAmountTStGen = document.createElement("cbc:TaxAmount");
            TaxAmountTStGen.setAttribute("currencyID", (String)jcbMoneda.getSelectedItem());
            Text valueTaxAmountTStGen = document.createTextNode(jlIGV.getText());
            TaxAmountTStGen.appendChild(valueTaxAmountTStGen);
            TaxSubtotalGen.appendChild(TaxAmountTStGen);
            
            Element TaxCategoryGen = document.createElement("cac:TaxCategory");
            
            Element TaxExemptionReasonCodeGen = document.createElement("cbc:TaxExemptionReasonCode");
            Text valueTaxExemptionReasonCodeGen = document.createTextNode("10");
            TaxExemptionReasonCodeGen.appendChild(valueTaxExemptionReasonCodeGen);
            TaxCategoryGen.appendChild(TaxExemptionReasonCodeGen);
            
            Element TaxSchemeGen = document.createElement("cac:TaxScheme");
            
            Element IDTSGen = document.createElement("cbc:ID");
            Text valueIDTSGen = document.createTextNode("1000");
            IDTSGen.appendChild(valueIDTSGen);
            TaxSchemeGen.appendChild(IDTSGen);
            
            Element NameGen = document.createElement("cbc:Name");
            Text valueNameGen = document.createTextNode("IGV");
            NameGen.appendChild(valueNameGen);
            TaxSchemeGen.appendChild(NameGen);
            
            Element TaxTypeCodeGen = document.createElement("cbc:TaxTypeCode");
            Text valueTaxTypeCodeGen = document.createTextNode("VAT");
            TaxTypeCodeGen.appendChild(valueTaxTypeCodeGen);
            TaxSchemeGen.appendChild(TaxTypeCodeGen);
            
            TaxCategoryGen.appendChild(TaxSchemeGen);
            TaxSubtotalGen.appendChild(TaxCategoryGen);
            TaxTotalGen.appendChild(TaxSubtotalGen);
            raiz.appendChild(TaxTotalGen);
            
            //Monto Total
            Element LegalMonetaryTotal;
            if(tipoComprobante.equals("D"))LegalMonetaryTotal = document.createElement("cac:RequestedMonetaryTotal");
            else LegalMonetaryTotal = document.createElement("cac:LegalMonetaryTotal");
            Element PayableAmount = document.createElement("cbc:PayableAmount");
            Text valuePayableAmount = document.createTextNode(jlTotal.getText());
            PayableAmount.setAttribute("currencyID", (String)jcbMoneda.getSelectedItem());
            PayableAmount.appendChild(valuePayableAmount);
            LegalMonetaryTotal.appendChild(PayableAmount);
            raiz.appendChild(LegalMonetaryTotal);
            
            //Productos
            for(int i=0;i<jtProductos.getRowCount();i++){
                Element InvoiceLine;
                if(tipoComprobante.equals("F") || tipoComprobante.equals("B"))InvoiceLine = document.createElement("cac:InvoiceLine");
                else if(tipoComprobante.equals("C")) InvoiceLine = document.createElement("cac:CreditNoteLine");
                else InvoiceLine = document.createElement("cac:DebitNoteLine");
                
                Element IDIL = document.createElement("cbc:ID");
                Text valueIDIL = document.createTextNode(String.valueOf(i+1));
                IDIL.appendChild(valueIDIL);
                InvoiceLine.appendChild(IDIL);
                
                Element InvoicedQuantity;
                if(tipoComprobante.equals("F") || tipoComprobante.equals("B"))InvoicedQuantity = document.createElement("cbc:InvoicedQuantity");
                else if(tipoComprobante.equals("C")) InvoicedQuantity = document.createElement("cbc:CreditedQuantity");
                else InvoicedQuantity = document.createElement("cbc:DebitedQuantity");
                  
                InvoicedQuantity.setAttribute("unitCode", this.getInternationalizeUnit(i));
                Text valueInvoicedQuantity = document.createTextNode(String.valueOf(jtProductos.getValueAt(i, 2)));
                InvoicedQuantity.appendChild(valueInvoicedQuantity);
                InvoiceLine.appendChild(InvoicedQuantity);
                
                Float tempValorItem=(Float) jtProductos.getValueAt(i, 2)*(Float)jtProductos.getValueAt(i, 4);
                Element LineExtensionAmount = document.createElement("cbc:LineExtensionAmount");
                Text valueLineExtensionAmount = document.createTextNode(String.valueOf(tempValorItem));
                LineExtensionAmount.setAttribute("currencyID", (String)jcbMoneda.getSelectedItem());
                LineExtensionAmount.appendChild(valueLineExtensionAmount);
                InvoiceLine.appendChild(LineExtensionAmount);
                
                Element PricingReference = document.createElement("cac:PricingReference");
                Element AlternativeConditionPrice = document.createElement("cac:AlternativeConditionPrice");
                Element PriceAmountPR = document.createElement("cbc:PriceAmount");
                PriceAmountPR.setAttribute("currencyID", (String)jcbMoneda.getSelectedItem());
                Text valuePriceAmountPR = document.createTextNode(String.valueOf(jtProductos.getValueAt(i, 4)));
                PriceAmountPR.appendChild(valuePriceAmountPR);
                AlternativeConditionPrice.appendChild(PriceAmountPR);
                Element PriceTypeCode = document.createElement("cbc:PriceTypeCode");
                Text valuePriceTypeCode = document.createTextNode("01");
                PriceTypeCode.appendChild(valuePriceTypeCode);
                AlternativeConditionPrice.appendChild(PriceTypeCode);
                PricingReference.appendChild(AlternativeConditionPrice);
                InvoiceLine.appendChild(PricingReference);
                
                Element TaxTotal = document.createElement("cac:TaxTotal");
                float impuestoXProducto=(float)jtProductos.getValueAt(i, 6);
                
                Element TaxAmount = document.createElement("cbc:TaxAmount");
                Text valueTaxAmount = document.createTextNode(format.format(impuestoXProducto));
                TaxAmount.setAttribute("currencyID", (String)jcbMoneda.getSelectedItem());
                TaxAmount.appendChild(valueTaxAmount);
                TaxTotal.appendChild(TaxAmount);
                Element TaxSubtotal = document.createElement("cac:TaxSubtotal");
                Element TaxAmountTSt = document.createElement("cbc:TaxAmount");
                Text valueTaxAmountTSt = document.createTextNode(format.format(impuestoXProducto));
                TaxAmountTSt.setAttribute("currencyID", (String)jcbMoneda.getSelectedItem());
                TaxAmountTSt.appendChild(valueTaxAmountTSt);
                TaxSubtotal.appendChild(TaxAmountTSt);
                Element TaxCategory = document.createElement("cac:TaxCategory");
            
                Element TaxExemptionReasonCode = document.createElement("cbc:TaxExemptionReasonCode");
                boolean temp=(boolean)jtProductos.getValueAt(i, 5);
                Text valueTaxExemptionReasonCode;
                if(temp) valueTaxExemptionReasonCode = document.createTextNode("10");
                else valueTaxExemptionReasonCode = document.createTextNode("20");
                TaxExemptionReasonCode.appendChild(valueTaxExemptionReasonCode);
                TaxCategory.appendChild(TaxExemptionReasonCode);
            
                Element TaxScheme = document.createElement("cac:TaxScheme");
            
                Element IDTS = document.createElement("cbc:ID");
                Text valueIDTS = document.createTextNode("1000");
                IDTS.appendChild(valueIDTS);
                TaxScheme.appendChild(IDTS);
            
                Element Name = document.createElement("cbc:Name");
                Text valueName = document.createTextNode("IGV");
                Name.appendChild(valueName);
                TaxScheme.appendChild(Name);
            
                Element TaxTypeCode = document.createElement("cbc:TaxTypeCode");
                Text valueTaxTypeCode = document.createTextNode("VAT");
                TaxTypeCode.appendChild(valueTaxTypeCode);
                TaxScheme.appendChild(TaxTypeCode);
            
                TaxCategory.appendChild(TaxScheme);
                TaxSubtotal.appendChild(TaxCategory);
                TaxTotal.appendChild(TaxSubtotal);
                InvoiceLine.appendChild(TaxTotal);
                
                Element Item = document.createElement("cac:Item");
                Element Description = document.createElement("cbc:Description");
                Text valueDescription = document.createTextNode(String.valueOf(jtProductos.getValueAt(i, 1)));
                Description.appendChild(valueDescription);
                Item.appendChild(Description);
                InvoiceLine.appendChild(Item);
                
                Element Price = document.createElement("cac:Price");
                Element PriceAmount = document.createElement("cbc:PriceAmount");
                PriceAmount.setAttribute("currencyID", (String)jcbMoneda.getSelectedItem());
                Float tempPrecioUnit=(Float) jtProductos.getValueAt(i, 4);
                Text valuePriceAmount = document.createTextNode(String.valueOf(tempPrecioUnit));
                PriceAmount.appendChild(valuePriceAmount);
                Price.appendChild(PriceAmount);
                InvoiceLine.appendChild(Price);
                raiz.appendChild(InvoiceLine);  
            }   
            //Generar el archivo XML
            Source source = new DOMSource(document);
            //Indicamos donde lo queremos almacenar
            
            String path = null;
            String nameComprobante=empresa.getRucEmpresa()+"-"+numTipoComprobante(tipoComprobante)+"-";
            if(tipoComprobante.equals("F") || tipoComprobante.equals("B"))nameComprobante+=jlNumFactura.getText();
            else nameComprobante+=jtSerieGuia.getText().substring(0,1)+jlNumFactura.getText().substring(1);
            try {
                path = new java.io.File(".").getCanonicalPath();
            } catch (IOException ex) {
                Logger.getLogger(Comprobante.class.getName()).log(Level.SEVERE, null, ex);
            }
            Result result = new StreamResult(new java.io.File(nameComprobante+".xml")); //nombre del archivo
            Transformer transformer = TransformerFactory.newInstance().newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty(OutputKeys.METHOD, "xml");
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "5");
            transformer.setOutputProperty(OutputKeys.ENCODING, "ISO-8859-1");
            transformer.transform(source, result);
            File xmlFile=new File(nameComprobante+".xml");
            firmarDocumento(xmlFile);
            
            Zip.Zippear(nameComprobante+".xml",nameComprobante+".zip");
            //DataSource fds = new FileDataSource(nameComprobante+".zip");
            File file = new File(nameComprobante+".zip");            
            byte[] bytes = loadFile(file);
            byte[] encoded = Base64.encodeBase64(bytes);
            String encodedString = new String(encoded,StandardCharsets.UTF_8);
                //System.out.println(encodedString);
                DataHandler dataHandler= ConverterUtil.convertToDataHandler(encodedString);
                FileOutputStream fos = new FileOutputStream("R-"+nameComprobante+".zip");
                fos.write(sendBill(nameComprobante+".zip", dataHandler));
                fos.close();
            //Guardar archivos en la carpeta respuestas
            if(!new File("../Archivos XML SUNAT").isDirectory())new File("../Archivos XML SUNAT").mkdirs();
            xmlFile.renameTo(new File("../Archivos XML SUNAT/"+nameComprobante+".xml"));
            file.renameTo(new File("../Archivos XML SUNAT/"+nameComprobante+".zip"));
            return  "R-"+nameComprobante;
            
        } catch (TransformerConfigurationException ex) {
            Logger.getLogger(Comprobante.class.getName()).log(Level.SEVERE, null, ex);
        } catch (TransformerException ex) {
            Logger.getLogger(Comprobante.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ParserConfigurationException ex) {
            Logger.getLogger(Comprobante.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(Comprobante.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InvalidAlgorithmParameterException ex) {
            Logger.getLogger(Comprobante.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Comprobante.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(this, ex.toString());
        } catch (CertificateException ex) {
            Logger.getLogger(Comprobante.class.getName()).log(Level.SEVERE, null, ex);
        }catch (KeyStoreException ex) {
                Logger.getLogger(Comprobante.class.getName()).log(Level.SEVERE, null, ex);
        } catch (UnrecoverableEntryException ex) {
            Logger.getLogger(Comprobante.class.getName()).log(Level.SEVERE, null, ex);
        } catch (MarshalException ex) {
            Logger.getLogger(Comprobante.class.getName()).log(Level.SEVERE, null, ex);
        } catch (XMLSignatureException ex) {
            Logger.getLogger(Comprobante.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(Comprobante.class.getName()).log(Level.SEVERE, null, ex);
            //JOptionPane.showMessageDialog(this, "El comprobante fue valdidado por la SUNAT", "SUNAT", JOptionPane.INFORMATION_MESSAGE, new javax.swing.ImageIcon(getClass().getResource("/Images/sunat_logo.png")));
            //validarBDComprobante();
            //jlEstadoFactura.setText("VALIDADO");
            
        }
        return null;
       }
    private String numTipoComprobante(String comprobante){
        if(comprobante.equals("F"))return "01";
        else if(comprobante.equals("B"))return "03";
        else if(comprobante.equals("C")) return "07";
        else return "08";
    }
    private static byte[] sendBill(String fileName, DataHandler contentFile) {
        
        BillService_Service service = new BillService_Service();
        HeaderHandlerResolver handlerResolver = new HeaderHandlerResolver();
        service.setHandlerResolver(handlerResolver);
        BillService port = service.getBillServicePort1();
        
        return port.sendBill(fileName, contentFile,null);
    }
   private static byte[] loadFile(File file) throws IOException {
	    InputStream is = new FileInputStream(file);

	    long length = file.length();
	    if (length > Integer.MAX_VALUE) {
	        // File is too large
	    }
	    byte[] bytes = new byte[(int)length];
	    
	    int offset = 0;
	    int numRead = 0;
	    while (offset < bytes.length
	           && (numRead=is.read(bytes, offset, bytes.length-offset)) >= 0) {
	        offset += numRead;
	    }

	    if (offset < bytes.length) {
	        throw new IOException("Could not completely read file "+file.getName());
	    }

	    is.close();
	    return bytes;
	}

    private void validarBDComprobante() {
        PreparedStatement stmt;
        try {
            stmt = conn.prepareStatement("Update comprobantePago set estado=\"VALIDADO\" WHERE serieComprobante=? and Empresa_idEmpresa=? and TipoComprobante_idTipoComprobante=? ");
            stmt.setString(1, comprobante.getSerieComprobante());
            stmt.setInt(2, empresa.getIdEmpresa());
            stmt.setString(3, tipoComprobante);
            stmt.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(Comprobante.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    private void firmarDocumento(File file) throws InvalidAlgorithmParameterException, NoSuchAlgorithmException, KeyStoreException, FileNotFoundException, UnrecoverableEntryException, IOException, CertificateException, CertificateException, ParserConfigurationException, SAXException, MarshalException, XMLSignatureException, TransformerException{
        XMLSignatureFactory fac=XMLSignatureFactory.getInstance("DOM");
            Reference ref=fac.newReference("",fac.newDigestMethod(DigestMethod.SHA1, null),
                    Collections.singletonList(fac.newTransform(Transform.ENVELOPED, (TransformParameterSpec)null)),
                    null,null);
            SignedInfo si=fac.newSignedInfo(fac.newCanonicalizationMethod(CanonicalizationMethod.INCLUSIVE,
                    (C14NMethodParameterSpec)null),
                    fac.newSignatureMethod(SignatureMethod.RSA_SHA1, null),
                    Collections.singletonList(ref));
            KeyStore ks;
            ks = KeyStore.getInstance("JKS");
            ks.load(new FileInputStream(System.getProperty("user.dir")+"/TeslaCer.jks"), "pepito".toCharArray());
            KeyStore.PrivateKeyEntry keyEntry=(KeyStore.PrivateKeyEntry)ks.getEntry("TeslaCer", new KeyStore.PasswordProtection("pepito".toCharArray()));
           
            X509Certificate cert =(X509Certificate) keyEntry.getCertificate();
            
            KeyInfoFactory kif=fac.getKeyInfoFactory();
            List x509Content=new ArrayList();
            x509Content.add(cert.getSubjectX500Principal().getName());
            x509Content.add(cert);
            X509Data xd= kif.newX509Data(x509Content);
            KeyInfo ki=kif.newKeyInfo(Collections.singletonList(xd));
            
            DocumentBuilderFactory dbf= DocumentBuilderFactory.newInstance();
            dbf.setNamespaceAware(true);
            Document doc=dbf.newDocumentBuilder().parse(file);
            NodeList list=doc.getElementsByTagName("ext:ExtensionContent");
            Element extensionContent=(Element) list.item(1);
            DOMSignContext dsc=new DOMSignContext(keyEntry.getPrivateKey(),extensionContent);
            XMLSignature signature=fac.newXMLSignature(si, ki,null,"TeslaCer",null);
            signature.sign(dsc);
            FileOutputStream	f			   = new FileOutputStream(file);
            TransformerFactory factory	   	   = TransformerFactory.newInstance();
            Transformer		   transformer	   = factory.newTransformer();
        
            //transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");        
            transformer.setOutputProperty(OutputKeys.ENCODING, "ISO-8859-1");
            transformer.transform(new DOMSource(doc), new StreamResult(f));
            f.close();
    }
    private void barcodeSearch() {
        agregarProductoTabla();
        int lastRow=jtProductos.getRowCount()-1;
        PreparedStatement stmt;
        try {
            stmt = conn.prepareStatement("select * from producto where idProducto=?");
            stmt.setString(1, barcode);
            ResultSet rs=stmt.executeQuery();
            if(rs.next()){
                jtProductos.setValueAt(rs.getString("descripcion"), lastRow, 1);
                stmt = conn.prepareStatement("select * from producto p left join `Empresa_has_Producto` ep on p.`idProducto`=ep.`Producto_idProducto` where p.idProducto=? and ep.`Empresa_idEmpresa`=?");
                stmt.setInt(2, empresa.getIdEmpresa());
                stmt.setString(1, barcode);
                if(rs.next()){
                    jtProductos.setValueAt(rs.getString("unidadMedida"), lastRow, 3);
                    jtProductos.setValueAt(rs.getFloat("precioUnit"), lastRow, 4);
                    actualizarTablaProductos();
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(Comprobante.class.getName()).log(Level.SEVERE, null, ex);
        }
        jtProductos.setValueAt(barcode, lastRow, 0); 
    }
}
