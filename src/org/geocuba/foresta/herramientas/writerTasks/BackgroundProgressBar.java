package org.geocuba.foresta.herramientas.writerTasks;

public class BackgroundProgressBar extends javax.swing.JDialog implements com.iver.utiles.swing.threads.IProgressMonitorIF{

	private static final long serialVersionUID = 1L;
	boolean canceled = false;
	private String title = "Processing...";
	
	
	public BackgroundProgressBar() {
        super();
        initComponents();  
        this.setLocationRelativeTo(null);
        initialize();
    }
	
	public BackgroundProgressBar(java.awt.Frame parent, String title) {
        super(parent, false);
        initComponents();        
        this.title = title;
        initialize();
    }
	
	private void initialize() {
		this.setSize(245, 122);
		this.setTitle(title);		
	}

    
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jProgressBar1 = new javax.swing.JProgressBar();
        jButton1 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setResizable(false);
        
        jLabel1.setText("Cargando..."); 
        jLabel1.setFont(new java.awt.Font("Dialog",java.awt.Font.BOLD, 12));
        
		//setCursor(new java.awt.Cursor(java.awt.Cursor.WAIT_CURSOR));

        jButton1.setText("Cancelar");
        jButton1.setMaximumSize(new java.awt.Dimension(75, 21));
        jButton1.setMinimumSize(new java.awt.Dimension(75, 21));
        jButton1.setPreferredSize(new java.awt.Dimension(73, 21));
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(layout.createSequentialGroup()
                        .addContainerGap()
                        .add(jLabel1))
                    .add(layout.createSequentialGroup()
                        .addContainerGap()
                        .add(jProgressBar1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 201, Short.MAX_VALUE))
                    .add(layout.createSequentialGroup()
                        .add(73, 73, 73)
                        .add(jButton1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 75, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(jLabel1)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                .add(jProgressBar1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jButton1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 21, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>                        

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {
        cancel();
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JProgressBar jProgressBar1;
    // End of variables declaration//GEN-END:variables

    public void setInitialStep(int step) {
    	jProgressBar1.setMinimum(step);
	}

	public void setLastStep(int step) {
		jProgressBar1.setMaximum(step);
	}

	public void setCurrentStep(int step) {
		jProgressBar1.setValue(step);
	}

	public int getInitialStep() {
		return jProgressBar1.getMinimum();
	}

	public int getLastStep() {
		return jProgressBar1.getMaximum();
	}

	public int getCurrentStep() {
		return jProgressBar1.getValue();
	}

	public void setIndeterminated(boolean indeterminated) {
		jProgressBar1.setIndeterminate(indeterminated);
		setBarStringDrawed(!indeterminated);			
	}

	public boolean isIndeterminated() {
		return jProgressBar1.isIndeterminate();
	}

	public void setBarStringDrawed(boolean stringDrawed) {
		jProgressBar1.setStringPainted(stringDrawed);
	}
	
	public void setBarString(String barString) {
		jProgressBar1.setString(barString);
	}

	public void setMainTitleLabel(String text) {
		jLabel1.setText(text);
	}

	public void setNote(String note) {
		if(!isIndeterminated())
			setBarString(note);
	}

	public void cancel() {
		canceled = true;
	}

	public boolean isCanceled() {
		return canceled == true;
	}

	public void close() {
		this.dispose();
	}

	public void open() {
		this.setVisible(true);
	}

}
