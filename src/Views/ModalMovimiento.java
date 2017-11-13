/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Views;


import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import javax.swing.JLabel;

/**
 *
 * @author javierfuenteshuertas
 */
public class ModalMovimiento extends Thread {
    
    private JFrame frame;
    public Boolean  band;
    public ModalMovimiento(){
        frame=new JFrame();
        jlImagen = new javax.swing.JLabel();
        jlMensaje = new javax.swing.JLabel();
        
        jlImagen.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/Contador.png"))); // NOI18N
        
        frame.setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        frame.setUndecorated(true);
        frame.setOpacity(0.8F);

        

        jlMensaje.setFont(new java.awt.Font("AppleGothic", 0, 18)); // NOI18N
        jlMensaje.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jlMensaje.setText("Consultando");
        
        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(frame.getContentPane());
        frame.getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jlImagen)
                .addGap(0, 0, Short.MAX_VALUE))
            .addComponent(jlMensaje, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jlImagen)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jlMensaje))
        );

        frame.pack();
        frame.setLocationRelativeTo(null);
        band=true;
    }
    public Boolean getBand() {
        return band;
    }

    public void setBand(Boolean band) {
        this.band = band;
    }
    
    @Override
    public void run() {
        Thread.currentThread().setPriority(Thread.MAX_PRIORITY); 
        frame.setVisible(true);
        while(band){
            try {
                Thread.sleep(500);
            } catch (InterruptedException ex) {
                Logger.getLogger(ModalMovimiento.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        frame.setVisible(false);
    }
    private javax.swing.JLabel jlImagen;
    private javax.swing.JLabel jlMensaje;
}
