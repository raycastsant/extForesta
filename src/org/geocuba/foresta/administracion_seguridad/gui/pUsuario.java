package org.geocuba.foresta.administracion_seguridad.gui;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JTextField;
import org.geocuba.foresta.herramientas.utiles.gui.Panel;
import org.geocuba.foresta.herramientas.utiles.gui.UIUtils;

import com.iver.andami.PluginServices;
import javax.swing.JPasswordField;
import javax.swing.JRadioButton;
import org.geocuba.foresta.herramientas.utiles.Funciones_Utiles;
import org.geocuba.foresta.herramientas.utiles.Global;

/**
 *
 * @author Raisel
 */
public class pUsuario extends Panel 
{
    public pUsuario(String title) {
        initComponents();
        _title  = title;
        _panel_id = "_"+title;
        UIUtils.AddPanel(this);
        jTFNombre.requestFocus();
       }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        bg1 = new javax.swing.ButtonGroup();
        jLabel1 = new javax.swing.JLabel();
        jLId = new javax.swing.JLabel();
        jBGuardar = new javax.swing.JButton();
        jBCancelar = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jTFApellidos = new javax.swing.JTextField();
        jPanel1 = new javax.swing.JPanel();
        jRBEspecialista = new javax.swing.JRadioButton();
        jRBAdministrador = new javax.swing.JRadioButton();
        jPFPass = new javax.swing.JPasswordField();
        jLPass = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jTFNombre = new javax.swing.JTextField();
        jTFUsuario = new javax.swing.JTextField();
        jCBXCambiarPass = new javax.swing.JCheckBox();
        jLConfirmPass = new javax.swing.JLabel();
        jPFConfirmPass = new javax.swing.JPasswordField();

        setFocusCycleRoot(true);

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 11));
        jLabel1.setText("Id:");

        jLId.setText("Gid:");

        jBGuardar.setIcon(new javax.swing.ImageIcon("D:\\PROYECTOS\\Tesis\\gvSIG_1.11_FORESTA_WORKSPACE\\extForesta\\images\\guardar.png")); // NOI18N
        jBGuardar.setIcon(new javax.swing.ImageIcon(PluginServices.getPluginServices("org.geocuba.foresta").getPluginDirectory()+Global.fileSeparator+"images"+Global.fileSeparator+"guardar.png"));
        jBGuardar.setText("Guardar");
        jBGuardar.setEnabled(false);

        jBCancelar.setIcon(new javax.swing.ImageIcon("D:\\PROYECTOS\\Tesis\\gvSIG_1.11_FORESTA_WORKSPACE\\extForesta\\images\\cancelar.png")); // NOI18N
        jBCancelar.setIcon(new javax.swing.ImageIcon(PluginServices.getPluginServices("org.geocuba.foresta").getPluginDirectory()+Global.fileSeparator+"images"+Global.fileSeparator+"cancelar.png"));
        jBCancelar.setText("Cancelar");

        jPanel2.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel2.setFont(new java.awt.Font("Tahoma", 1, 11));
        jLabel2.setText("Usuario");

        jLabel5.setFont(new java.awt.Font("Tahoma", 1, 11));
        jLabel5.setText("Apellidos");

        jTFApellidos.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 102)));
        jTFApellidos.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTFApellidosActionPerformed(evt);
            }
        });
        jTFApellidos.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jTFApellidosKeyTyped(evt);
            }
        });

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("Nivel"));

        bg1.add(jRBEspecialista);
        bg1.add(jRBAdministrador);
        jRBEspecialista.setSelected(true);
        jRBEspecialista.setText("Especielista");

        jRBAdministrador.setText("Administrador");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jRBEspecialista)
                    .addComponent(jRBAdministrador))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jRBEspecialista)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jRBAdministrador)
                .addContainerGap(6, Short.MAX_VALUE))
        );

        jPFPass.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 102)));
        jPFPass.setEnabled(false);
        jPFPass.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jPFPassKeyTyped(evt);
            }
        });

        jLPass.setFont(new java.awt.Font("Tahoma", 1, 11));
        jLPass.setText("Contraseņa");
        jLPass.setEnabled(false);

        jLabel4.setFont(new java.awt.Font("Tahoma", 1, 11));
        jLabel4.setText("Nombre");

        jTFNombre.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 102)));
        jTFNombre.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTFNombreActionPerformed(evt);
            }
        });
        jTFNombre.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jTFNombreKeyTyped(evt);
            }
        });

        jTFUsuario.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 102)));
        jTFUsuario.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jTFUsuarioKeyTyped(evt);
            }
        });

        jCBXCambiarPass.setText("Cambiar contraseņa");

        jLConfirmPass.setFont(new java.awt.Font("Tahoma", 1, 11));
        jLConfirmPass.setText("Confirmar Contraseņa");
        jLConfirmPass.setEnabled(false);

        jPFConfirmPass.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 102)));
        jPFConfirmPass.setEnabled(false);
        jPFConfirmPass.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jPFConfirmPassKeyTyped(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel2Layout.createSequentialGroup()
                        .addGap(66, 66, 66)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel2Layout.createSequentialGroup()
                                .addComponent(jLConfirmPass)
                                .addGap(10, 10, 10)
                                .addComponent(jPFConfirmPass, javax.swing.GroupLayout.PREFERRED_SIZE, 201, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel2Layout.createSequentialGroup()
                                .addGap(59, 59, 59)
                                .addComponent(jLPass)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jPFPass, javax.swing.GroupLayout.DEFAULT_SIZE, 201, Short.MAX_VALUE)
                                    .addComponent(jCBXCambiarPass)))))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel2Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(13, 13, 13)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel2)
                            .addComponent(jLabel5)
                            .addComponent(jLabel4))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jTFApellidos, javax.swing.GroupLayout.DEFAULT_SIZE, 202, Short.MAX_VALUE)
                            .addComponent(jTFUsuario, javax.swing.GroupLayout.DEFAULT_SIZE, 202, Short.MAX_VALUE)
                            .addComponent(jTFNombre, javax.swing.GroupLayout.DEFAULT_SIZE, 202, Short.MAX_VALUE))))
                .addGap(18, 18, 18))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jPanel1, javax.swing.GroupLayout.Alignment.LEADING, 0, 82, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel2Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jTFNombre, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel4))
                        .addGap(12, 12, 12)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jTFApellidos, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel5))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jTFUsuario, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel2))))
                .addGap(7, 7, 7)
                .addComponent(jCBXCambiarPass)
                .addGap(5, 5, 5)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jPFPass, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLPass))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jPFConfirmPass, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLConfirmPass))
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLId, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(201, 201, 201)
                        .addComponent(jBGuardar, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jBCancelar))
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(22, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(jLId))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jBCancelar, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jBGuardar))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void jTFNombreActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTFNombreActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTFNombreActionPerformed

    private void jTFApellidosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTFApellidosActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTFApellidosActionPerformed

    private void jTFNombreKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTFNombreKeyTyped
     char caracter = evt.getKeyChar();
     if(caracter == '_')
      evt.consume();
     else
     if(!Funciones_Utiles.formatoInputTextField(jTFNombre.getText(), caracter, 'S', 50))
      evt.consume();
    }//GEN-LAST:event_jTFNombreKeyTyped

    private void jTFApellidosKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTFApellidosKeyTyped
     char caracter = evt.getKeyChar();
     if(!Funciones_Utiles.formatoInputTextField(jTFApellidos.getText(), caracter, 'S', 99))
      evt.consume();
    }//GEN-LAST:event_jTFApellidosKeyTyped

    private void jTFUsuarioKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTFUsuarioKeyTyped
     char caracter = evt.getKeyChar();
     if(!Funciones_Utiles.formatoInputTextField(jTFUsuario.getText(), caracter, 'N', 20))
      evt.consume();
    }//GEN-LAST:event_jTFUsuarioKeyTyped

    private void jPFPassKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jPFPassKeyTyped
     char caracter = evt.getKeyChar();
     if(!Funciones_Utiles.formatoInputTextField(jPFPass.getText(), caracter, 'N', 22))
      evt.consume();
    }//GEN-LAST:event_jPFPassKeyTyped

    private void jPFConfirmPassKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jPFConfirmPassKeyTyped
     char caracter = evt.getKeyChar();
     if(!Funciones_Utiles.formatoInputTextField(jPFConfirmPass.getText(), caracter, 'N', 22))
      evt.consume();
    }//GEN-LAST:event_jPFConfirmPassKeyTyped

public JButton getButtonGuardar(){
 return jBGuardar;
}

public JButton getButtonCancelar(){
 return jBCancelar;
}

public JTextField getTFUsuario(){
 return jTFUsuario;
}

public JTextField getTFNombre(){
 return jTFNombre;
}

public JTextField getTFApellidos(){
 return jTFApellidos;
}

public JPasswordField getFieldPassword(){
 return jPFPass;
}

    public JCheckBox getjCBXCambiarPass() {
        return jCBXCambiarPass;
    }

    public JLabel getjLConfirmPass() {
        return jLConfirmPass;
    }

    public JLabel getjLPass() {
        return jLPass;
    }

    public JPasswordField getjPFConfirmPass() {
        return jPFConfirmPass;
    }

public JRadioButton getRBEspecialista(){
 return jRBEspecialista;
}

public JRadioButton getRBAdministrador(){
 return jRBAdministrador;
}

public JLabel getLabelId(){
 return jLId;
}

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup bg1;
    private javax.swing.JButton jBCancelar;
    private javax.swing.JButton jBGuardar;
    private javax.swing.JCheckBox jCBXCambiarPass;
    private javax.swing.JLabel jLConfirmPass;
    private javax.swing.JLabel jLId;
    private javax.swing.JLabel jLPass;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JPasswordField jPFConfirmPass;
    private javax.swing.JPasswordField jPFPass;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JRadioButton jRBAdministrador;
    private javax.swing.JRadioButton jRBEspecialista;
    private javax.swing.JTextField jTFApellidos;
    private javax.swing.JTextField jTFNombre;
    private javax.swing.JTextField jTFUsuario;
    // End of variables declaration//GEN-END:variables

}
