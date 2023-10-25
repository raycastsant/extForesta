package org.geocuba.foresta.gestion_datos.gui;

import javax.swing.JButton;
import javax.swing.JTextField;
import org.geocuba.foresta.herramientas.utiles.gui.Panel;
import org.geocuba.foresta.herramientas.utiles.gui.UIUtils;
import com.iver.andami.PluginServices;
import org.geocuba.foresta.herramientas.utiles.Funciones_Utiles;
import org.geocuba.foresta.herramientas.utiles.Global;

/**
 *
 * @author Raisel
 */
public class pEspecie extends Panel 
{
    public pEspecie(String title) {
        initComponents();
        _title  = title;
//        _panel_id = "_"+title;
//        UIUtils.AddPanel(this);
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jBGuardar = new javax.swing.JButton();
        jBCancelar = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jTFNComun = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jTFNCientifico = new javax.swing.JTextField();
        jTFSiglas = new javax.swing.JTextField();

        setFocusCycleRoot(true);

        jBGuardar.setIcon(new javax.swing.ImageIcon("D:"+Global.fileSeparator+"PROYECTOS"+Global.fileSeparator+"Tesis"+Global.fileSeparator+"gvSIG_1.11_FORESTA_WORKSPACE"+Global.fileSeparator+"extForesta"+Global.fileSeparator+"images"+Global.fileSeparator+"guardar.png")); // NOI18N
        jBGuardar.setIcon(new javax.swing.ImageIcon(PluginServices.getPluginServices("org.geocuba.foresta").getPluginDirectory()+""+Global.fileSeparator+"images"+Global.fileSeparator+"guardar.png"));
        jBGuardar.setText("Guardar");
        jBGuardar.setEnabled(false);

        jBCancelar.setIcon(new javax.swing.ImageIcon("D:"+Global.fileSeparator+"PROYECTOS"+Global.fileSeparator+"Tesis"+Global.fileSeparator+"gvSIG_1.11_FORESTA_WORKSPACE"+Global.fileSeparator+"extForesta"+Global.fileSeparator+"images"+Global.fileSeparator+"cancelar.png")); // NOI18N
        jBCancelar.setIcon(new javax.swing.ImageIcon(PluginServices.getPluginServices("org.geocuba.foresta").getPluginDirectory()+""+Global.fileSeparator+"images"+Global.fileSeparator+"cancelar.png"));
        jBCancelar.setText("Cancelar");

        jPanel1.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel3.setFont(new java.awt.Font("Tahoma", 1, 11));
        jLabel3.setText("Nombre cient�fico");

        jTFNComun.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 102)));
        jTFNComun.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jTFNComunKeyTyped(evt);
            }
        });

        jLabel4.setFont(new java.awt.Font("Tahoma", 1, 11));
        jLabel4.setText("Nombre com�n");

        jLabel2.setFont(new java.awt.Font("Tahoma", 1, 11));
        jLabel2.setText("Siglas");

        jTFNCientifico.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 102)));
        jTFNCientifico.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jTFNCientificoKeyTyped(evt);
            }
        });

        jTFSiglas.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 102)));
        jTFSiglas.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jTFSiglasKeyTyped(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTFNCientifico))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createSequentialGroup()
                        .addGap(21, 21, 21)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel4)
                            .addComponent(jLabel2))
                        .addGap(6, 6, 6)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jTFSiglas, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(138, 138, 138))
                            .addComponent(jTFNComun, javax.swing.GroupLayout.DEFAULT_SIZE, 206, Short.MAX_VALUE))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(jTFSiglas, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(8, 8, 8)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(jTFNComun, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(jTFNCientifico, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(19, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(138, Short.MAX_VALUE)
                .addComponent(jBGuardar, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jBCancelar)
                .addGap(29, 29, 29))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jBCancelar, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jBGuardar))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void jTFSiglasKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTFSiglasKeyTyped
     char caracter = evt.getKeyChar();
     if(!Funciones_Utiles.formatoInputTextField(jTFSiglas.getText(), caracter, 'S', 4))
      evt.consume();
    }//GEN-LAST:event_jTFSiglasKeyTyped

    private void jTFNComunKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTFNComunKeyTyped
     char caracter = evt.getKeyChar();
     if(!Funciones_Utiles.formatoInputTextField(jTFNComun.getText(), caracter, 'S', 30))
      evt.consume();
    }//GEN-LAST:event_jTFNComunKeyTyped

    private void jTFNCientificoKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTFNCientificoKeyTyped
     char caracter = evt.getKeyChar();
     if(!Funciones_Utiles.formatoInputTextField(jTFNCientifico.getText(), caracter, 'S', 30))
      evt.consume();
    }//GEN-LAST:event_jTFNCientificoKeyTyped

public JButton getButtonGuardar(){
 return jBGuardar;
}

public JButton getButtonCancelar(){
 return jBCancelar;
}

public JTextField getTFSiglas(){
 return jTFSiglas;
}

public JTextField getTFNComun(){
 return jTFNComun;
}

public JTextField getTFNCientifico(){
 return jTFNCientifico;
}

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jBCancelar;
    private javax.swing.JButton jBGuardar;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JTextField jTFNCientifico;
    private javax.swing.JTextField jTFNComun;
    private javax.swing.JTextField jTFSiglas;
    // End of variables declaration//GEN-END:variables

}