/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Views;

import Controllers.ConnectionDB;
import Models.Producto;
import com.apple.eawt.Application;
import java.awt.Event;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author javierfuenteshuertas
 */
public class Productos extends javax.swing.JFrame {

    /**
     * Creates new form Productos
     */
    private JTable tabla;
    private ArrayList<Producto> listProductos;
    private ConnectionDB conexion;
    private Connection conn;
    private int idEmpresa;
    private int fila;
    private float COMPRA;
    private float VENTA;
    private String MONEDA;
    public Productos(JTable tabla ,int idEmpresa,int row, float compra,float venta,String moneda) {
        initComponents();
        conexion=new ConnectionDB();
        conn=conexion.connect(conn);
        this.tabla=tabla;
        this.idEmpresa=idEmpresa;
        this.fila=row;
        this.COMPRA=compra;
        this.VENTA=venta;
        this.MONEDA=moneda;
        listProductos=new ArrayList<Producto>();
        initializeEvents();
        buscarProductos("%");
        
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
    private void initializeEvents() {
         getRootPane().getInputMap(javax.swing.JComponent.WHEN_IN_FOCUSED_WINDOW).put(
            javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_ESCAPE, 0), "Cancel");
        
        getRootPane().getActionMap().put("Cancel", new javax.swing.AbstractAction(){
            @Override
            public void actionPerformed(java.awt.event.ActionEvent e)
            {
               setVisible(false);
            }
        });
        jtProductos.addMouseListener(new MouseAdapter() {
        public void mousePressed(MouseEvent me) {
            JTable table =(JTable) me.getSource();
            Point p = me.getPoint();
            int row = table.rowAtPoint(p);
            if (me.getClickCount() == 2) {
                tabla.setValueAt(jtProductos.getValueAt(row, 0), fila, 0);
                tabla.setValueAt(jtProductos.getValueAt(row, 1), fila, 1);
                tabla.setValueAt(jtProductos.getValueAt(row, 2), fila, 3);
                cambiarDivisa((float)jtProductos.getValueAt(row, 3), (String)jtProductos.getValueAt(row, 4), fila);
                setVisible(false);
            }
            
        }

        });
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
        jLabel1 = new javax.swing.JLabel();
        jlDescripcionProducto = new javax.swing.JTextField();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jtProductos = new javax.swing.JTable();

        jPanel1.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel1.setText("Buscar:");

        jlDescripcionProducto.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jlDescripcionProductoKeyPressed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jlDescripcionProducto)
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(jlDescripcionProducto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder("Resultados"));

        jtProductos.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Código", "Descripción", "Unid Medida", "Precio Venta", "Moneda"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane1.setViewportView(jtProductos);
        if (jtProductos.getColumnModel().getColumnCount() > 0) {
            jtProductos.getColumnModel().getColumn(1).setMinWidth(200);
            jtProductos.getColumnModel().getColumn(1).setPreferredWidth(200);
            jtProductos.getColumnModel().getColumn(1).setMaxWidth(200);
        }

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 464, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 237, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void jlDescripcionProductoKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jlDescripcionProductoKeyPressed
        if(evt.getKeyCode()==Event.ENTER){
            buscarProductos(jlDescripcionProducto.getText()+"%");
        }
    }//GEN-LAST:event_jlDescripcionProductoKeyPressed

    /**
     * @param args the command line arguments
     */
   
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextField jlDescripcionProducto;
    private javax.swing.JTable jtProductos;
    // End of variables declaration//GEN-END:variables

    private void buscarProductos(String nomProducto) {
        PreparedStatement stmt;
        try {
            stmt = conn.prepareStatement("select * from producto p inner join Empresa_has_producto ehp on p.idProducto=ehp.Producto_idProducto  where Empresa_idEmpresa=? and descripcion like ?");
            stmt.setInt(1, idEmpresa);
            stmt.setString(2,nomProducto);
            ResultSet rs=stmt.executeQuery();
            listProductos=new ArrayList<Producto>();
            while(rs.next()){
                Producto tempProducto=new Producto();
                tempProducto.setIdProducto(rs.getString("idProducto"));
                tempProducto.setDescripcion(rs.getString("descripcion"));
                tempProducto.setPrecioUnit(rs.getFloat("precioUnit"));
                tempProducto.setUnidadMedida(rs.getString("unidadMedida"));
                tempProducto.setMoneda(rs.getString("moneda"));
                listProductos.add(tempProducto);
            }
            actualizarTablaProducto();
        } catch (SQLException ex) {
            Logger.getLogger(Clientes.class.getName()).log(Level.SEVERE, null, ex);
        }      
    }

    private void actualizarTablaProducto() {
        DefaultTableModel dtm=(DefaultTableModel) jtProductos.getModel();
        Producto temp;
        Object[] object = null;
        limpiarTabla(dtm);
        for(int i=0;i<listProductos.size();i++){
            temp=listProductos.get(i);
            dtm.addRow(object);
            dtm.setValueAt(temp.getIdProducto(), i, 0);
            dtm.setValueAt(temp.getDescripcion(), i, 1);
            dtm.setValueAt(temp.getUnidadMedida(), i, 2); 
            dtm.setValueAt(temp.getPrecioUnit(), i, 3);
            dtm.setValueAt(temp.getMoneda(), i, 4);
        }
    }

    private void limpiarTabla(DefaultTableModel dtm) {
        for(int i=0;i<dtm.getRowCount();i++){
            dtm.removeRow(i);
        }
    }
    private void cambiarDivisa(float value , String monedaProd,int fila){
        if(MONEDA.equals(monedaProd)){
            tabla.setValueAt(value, fila, 4);
            return;
        }
        if(monedaProd.equals("USD")&& MONEDA.equals("PEN")){
            tabla.setValueAt(value*COMPRA, fila, 4);
            return;
        }
        else if(monedaProd.equals("PEN")&& MONEDA.equals("USD")){
            System.out.println(value +" "+VENTA);
            tabla.setValueAt(value*(1/VENTA), fila, 4);
            return;
        }
        
    }
    
}