package aurelienribon.gdxsetupui.ui.panels;

import aurelienribon.gdxsetupui.ProjectSetupConfiguration;
import aurelienribon.gdxsetupui.ui.Ctx;
import aurelienribon.gdxsetupui.ui.MainPanel;
import aurelienribon.ui.css.Style;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * @author Aurelien Ribon | http://www.aurelienribon.com/
 */
public class GoPanel extends javax.swing.JPanel {
	
	private static final long serialVersionUID = -1161307002297650916L;
	public GoPanel(final MainPanel mainPanel) {
        initComponents();
		Style.registerCssClasses(headerPanel, ".header");
		Style.registerCssClasses(numberLabel, ".headerNumber");
		Style.registerCssClasses(errorLabel, ".statusLabel");
		Style.registerCssClasses(goBtn, ".bold");
		
		Ctx.listeners.add(new Ctx.Listener() {
			@Override public void modeChanged() {update();}
			@Override public void cfgSetupChanged() {update();}
			@Override public void cfgUpdateChanged() {update();}
		});

		goBtn.addActionListener(new ActionListener() {
			@Override 
			public void actionPerformed(ActionEvent e) {
				mainPanel.showProcessSetupPanel();
			}
		});		
    }
    
	private void update() {
		errorLabel.firePropertyChange("valid", true, false);
		errorLabel.firePropertyChange("error", true, false);

		if (isProjectCreationValid(Ctx.cfgSetup)) {
			goBtn.setEnabled(true);
			errorLabel.setText("<html>Your configuration is valid.");
			errorLabel.firePropertyChange("valid", false, true);
		} else {
			goBtn.setEnabled(false);
			errorLabel.setText("<html>" + getCreationErrorMessage(Ctx.cfgSetup));
			errorLabel.firePropertyChange("error", false, true);
		}

		numberLabel.setText("3");
		goBtn.setText("Open the generation screen");
	}

	private boolean isProjectCreationValid(ProjectSetupConfiguration cfg) {
		if (cfg.projectName.trim().equals("")) return false;
		if (cfg.packageName.trim().equals("")) return false;
		if (cfg.packageName.endsWith(".")) return false;
		if (cfg.mainClassName.trim().equals("")) return false;
		if (cfg.stageWidth.trim().equals("")) return false;
		if (cfg.stageHeight.trim().equals("")) return false;
		if (cfg.gameWidth.trim().equals("")) return false;
		if (cfg.gameHeight.trim().equals("")) return false;
		if (cfg.zoom.trim().equals("")) return false;

		if (cfg.libraries.isEmpty())
			return false;

		return true;
	}

	private String getCreationErrorMessage(ProjectSetupConfiguration cfg) {
		if (cfg.projectName.trim().equals("")) return "Project name is not set.";
		if (cfg.packageName.trim().equals("")) return "Package name is not set.";
		if (cfg.packageName.endsWith(".")) return "Package name ends with a dot.";
		if (cfg.mainClassName.trim().equals("")) return "Main class name is not set.";
		if (cfg.stageWidth.trim().equals("")) return "StageWidth is not set.";
		if (cfg.stageHeight.trim().equals("")) return "StageHeight is not set.";
		if (cfg.gameWidth.trim().equals("")) return "GameWidth is not set.";
		if (cfg.gameHeight.trim().equals("")) return "GameHeight is not set.";
		if (cfg.zoom.trim().equals("")) return "Zoom is not set.";

		if (cfg.libraries.isEmpty())
			return "Loading config file...";

		return "No error found";
	}

	// -------------------------------------------------------------------------
	// Generated stuff
	// -------------------------------------------------------------------------

    
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        errorLabel = new javax.swing.JLabel();
        goBtn = new aurelienribon.ui.components.Button();
        headerPanel = new javax.swing.JPanel();
        headerLabel = new javax.swing.JLabel();
        numberLabel = new javax.swing.JLabel();

        setLayout(new java.awt.BorderLayout());

        jPanel1.setOpaque(false);

        errorLabel.setText("...");
        errorLabel.setVerticalAlignment(javax.swing.SwingConstants.TOP);

        goBtn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/res/gfx/ic_run.png"))); // NOI18N
        goBtn.setText("Open the generation screen");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(errorLabel, javax.swing.GroupLayout.DEFAULT_SIZE, 241, Short.MAX_VALUE)
                    .addComponent(goBtn, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(goBtn, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(errorLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        add(jPanel1, java.awt.BorderLayout.CENTER);

        headerLabel.setText("<html> Ready to go?");
        headerLabel.setVerticalAlignment(javax.swing.SwingConstants.TOP);

        numberLabel.setText("3");

        javax.swing.GroupLayout headerPanelLayout = new javax.swing.GroupLayout(headerPanel);
        headerPanel.setLayout(headerPanelLayout);
        headerPanelLayout.setHorizontalGroup(
            headerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, headerPanelLayout.createSequentialGroup()
                .addComponent(numberLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(headerLabel, javax.swing.GroupLayout.DEFAULT_SIZE, 249, Short.MAX_VALUE))
        );
        headerPanelLayout.setVerticalGroup(
            headerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(headerLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
            .addComponent(numberLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        add(headerPanel, java.awt.BorderLayout.NORTH);
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel errorLabel;
    private aurelienribon.ui.components.Button goBtn;
    private javax.swing.JLabel headerLabel;
    private javax.swing.JPanel headerPanel;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JLabel numberLabel;
    // End of variables declaration//GEN-END:variables

}
