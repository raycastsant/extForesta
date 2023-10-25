/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * dialogoUsuarios.java
 *
 * Created on 04-mar-2011, 22:15:42
 */

package org.geocuba.foresta.administracion_seguridad.gui;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import com.iver.andami.PluginServices;
import org.geocuba.foresta.herramientas.utiles.Funciones_Utiles;
import org.geocuba.foresta.herramientas.utiles.Global;
import org.geocuba.foresta.herramientas.utiles.gui.UIUtils;

/**
 *
 * @author Raisel
 */
public class CambiarPassDlg extends javax.swing.JDialog {

    /** Creates new form dialogoUsuarios */
    public CambiarPassDlg(java.awt.Frame parent, String title) {
        super(parent, true);
        initComponents();
        setTitle(title);
        setResizable(false);
        jPFPassword.requestFocusInWindow();
        UIUtils.inhabilitaPegar(this);
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jBAceptar = new javax.swing.JButton();
        jBCancelar = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        jPFPassword = new javax.swing.JPasswordField();
        jLabel2 = new javax.swing.JLabel();
        jTFUsuario = new javax.swing.JTextField();
        jPFPasswordConfirm = new javax.swing.JPasswordField();
        jLabel1 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jBAceptar.setIcon(new javax.swing.ImageIcon("D:"+Global.fileSeparator+"PROYECTOSTesis"+Global.fileSeparator+"gvSIG_1.11_FORESTA_WORKSPACE"+Global.fileSeparator+"extForesta"+Global.fileSeparator+"images"+Global.fileSeparator+"aceptar.png")); // NOI18N
        jBAceptar.setIcon(new javax.swing.ImageIcon(PluginServices.getPluginServices("org.geocuba.foresta").getPluginDirectory()+""+Global.fileSeparator+"images"+Global.fileSeparator+"aceptar.png"));
        jBAceptar.setText("Aceptar");
        jBAceptar.setEnabled(false);

        jBCancelar.setIcon(new javax.swing.ImageIcon("D:"+Global.fileSeparator+"PROYECTOS"+Global.fileSeparator+"Tesis"+Global.fileSeparator+"gvSIG_1.11_FORESTA_WORKSPACE"+Global.fileSeparator+"extForesta"+Global.fileSeparator+"images"+Global.fileSeparator+"cancelar.png")); // NOI18N
        jBCancelar.setIcon(new javax.swing.ImageIcon(PluginServices.getPluginServices("org.geocuba.foresta").getPluginDirectory()+""+Global.fileSeparator+"images"+Global.fileSeparator+"cancelar.png"));
        jBCancelar.setText("Cancelar");

        jPanel1.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jPFPassword.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 102)));
        jPFPassword.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jPFPasswordKeyTyped(evt);
            }
        });

        jLabel2.setText("Contrase�a nueva");

        jTFUsuario.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 102)));

        jPFPasswordConfirm.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 102)));
        jPFPasswordConfirm.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jPFPasswordConfirmKeyTyped(evt);
            }
        });

        jLabel1.setText("Usuario");

        jLabel4.setText("Confirmar contrase�a");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel2)
                    .addComponent(jLabel4)
                    .addComponent(jLabel1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPFPasswordConfirm)
                    .addComponent(jTFUsuario, javax.swing.GroupLayout.PREFERRED_SIZE, 126, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPFPassword, javax.swing.GroupLayout.PREFERRED_SIZE, 169, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTFUsuario, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jPFPassword, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(jPFPasswordConfirm, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(102, Short.MAX_VALUE)
                .addComponent(jBAceptar)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jBCancelar)
                .addGap(27, 27, 27))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jBCancelar)
                    .addComponent(jBAceptar))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jPFPasswordKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jPFPasswordKeyTyped
     char caracter = evt.getKeyChar();
     if(!Funciones_Utiles.formatoInputTextField(jPFPassword.getText(), caracter, 'N', 22))
      evt.consume();
    }//GEN-LAST:event_jPFPasswordKeyTyped

    private void jPFPasswordConfirmKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jPFPasswordConfirmKeyTyped
     char caracter = evt.getKeyChar();
     if(!Funciones_Utiles.formatoInputTextField(jPFPasswordConfirm.getText(), caracter, 'N', 22))
      evt.consume();
    }//GEN-LAST:event_jPFPasswordConfirmKeyTyped

public JTextField getFieldUsuario(){
 return jTFUsuario;
}

public JPasswordField getFieldPassword(){
 return jPFPassword;
}

public JPasswordField getFieldPasswordConfirm(){
 return jPFPasswordConfirm;
}

public JButton getButtonAceptar(){
 return jBAceptar;
}

public JButton getButtonCancelar(){
 return jBCancelar;
}

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jBAceptar;
    private javax.swing.JButton jBCancelar;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JPasswordField jPFPassword;
    private javax.swing.JPasswordField jPFPasswordConfirm;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JTextField jTFUsuario;
    // End of variables declaration//GEN-END:variables

}