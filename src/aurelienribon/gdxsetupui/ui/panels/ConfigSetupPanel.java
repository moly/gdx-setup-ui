package aurelienribon.gdxsetupui.ui.panels;

import aurelienribon.gdxsetupui.ui.Ctx;
import aurelienribon.gdxsetupui.ui.MainPanel;
import aurelienribon.ui.css.Style;
import java.awt.Cursor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.IOException;
import java.util.regex.Pattern;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

/**
 * @author Aurelien Ribon | http://www.aurelienribon.com/
 */
public class ConfigSetupPanel extends javax.swing.JPanel {
	private boolean clicToShowSettings = true;

    public ConfigSetupPanel(final MainPanel mainPanel) {
        initComponents();

		nameField.setText(Ctx.cfgSetup.projectName);
		packageField.setText(Ctx.cfgSetup.packageName);
		mainClassField.setText(Ctx.cfgSetup.mainClassName);
		stageWidthField.setText(Ctx.cfgSetup.stageWidth);
		stageHeightField.setText(Ctx.cfgSetup.stageHeight);
		gameWidthField.setText(Ctx.cfgSetup.gameWidth);
		gameHeightField.setText(Ctx.cfgSetup.gameHeight);
		zoomField.setText(Ctx.cfgSetup.zoom);

		try {
			File destDir = new File(Ctx.cfgSetup.destinationPath);
			destinationField.setText(destDir.getCanonicalPath());
		} catch (IOException ex) {
			assert false;
		}

		nameField.addMouseListener(selectOnFocusMouseListener);
		nameField.addKeyListener(updateOnTypeKeyListener);
		nameField.addKeyListener(projectNameKeyListener);
		packageField.addMouseListener(selectOnFocusMouseListener);
		packageField.addKeyListener(updateOnTypeKeyListener);
		packageField.addKeyListener(packageNameKeyListener);
		mainClassField.addMouseListener(selectOnFocusMouseListener);
		mainClassField.addKeyListener(updateOnTypeKeyListener);
		mainClassField.addKeyListener(mainClassNameKeyListener);
		stageWidthField.addMouseListener(selectOnFocusMouseListener);
		stageWidthField.addKeyListener(updateOnTypeKeyListener);
		gameWidthField.addMouseListener(selectOnFocusMouseListener);
		gameWidthField.addKeyListener(updateOnTypeKeyListener);
		gameHeightField.addMouseListener(selectOnFocusMouseListener);
		gameHeightField.addKeyListener(updateOnTypeKeyListener);
		zoomField.addMouseListener(selectOnFocusMouseListener);
		zoomField.addKeyListener(updateOnTypeKeyListener);

		browseBtn.addActionListener(new ActionListener() {@Override public void actionPerformed(ActionEvent e) {browse();}});
		genDesktopPrjChk.addActionListener(new ActionListener() {@Override public void actionPerformed(ActionEvent e) {update();}});
		genAndroidPrjChk.addActionListener(new ActionListener() {@Override public void actionPerformed(ActionEvent e) {update();}});
		//genHtmlPrjChk.addActionListener(new ActionListener() {@Override public void actionPerformed(ActionEvent e) {update();}});

		advancedSettingsLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		advancedSettingsLabel.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				if (clicToShowSettings && mainPanel.showAdvancedSettings()) {
					clicToShowSettings = false;
					advancedSettingsLabel.setText("< Hide advanced settings");
				} else if (mainPanel.hideAdvancedSettings()) {
					clicToShowSettings = true;
					advancedSettingsLabel.setText("Show advanced settings >");
				}
			}
		});

		update();

		Style.registerCssClasses(headerPanel, ".header");
		Style.registerCssClasses(numberLabel, ".headerNumber");
		Style.registerCssClasses(advancedSettingsLabel, ".linkLabel");
		Style.registerCssClasses(browseBtn, ".center");

		nameField.requestFocusInWindow();
		nameField.selectAll();
    }

	private void browse() {
		String path = Ctx.cfgSetup.destinationPath;
		JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(this);

		JFileChooser chooser = new JFileChooser(new File(path));
		chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		chooser.setDialogTitle("Select the destination folder");

		if (chooser.showOpenDialog(frame) == JFileChooser.APPROVE_OPTION) {
			destinationField.setText(chooser.getSelectedFile().getPath());
			update();
		}
	}

	private void update() {
		Ctx.cfgSetup.projectName = nameField.getText();
		Ctx.cfgSetup.packageName = packageField.getText();
		Ctx.cfgSetup.mainClassName = mainClassField.getText();
		Ctx.cfgSetup.destinationPath = destinationField.getText();
		Ctx.cfgSetup.isDesktopIncluded = genDesktopPrjChk.isSelected();
		Ctx.cfgSetup.isAndroidIncluded = genAndroidPrjChk.isSelected();
		Ctx.cfgSetup.stageWidth = stageWidthField.getText();
		Ctx.cfgSetup.stageHeight = stageHeightField.getText();
		Ctx.cfgSetup.gameWidth = gameWidthField.getText();
		Ctx.cfgSetup.gameHeight = gameHeightField.getText();
		Ctx.cfgSetup.zoom = zoomField.getText();
		//Ctx.cfgSetup.isHtmlIncluded = genHtmlPrjChk.isSelected();
		Ctx.fireCfgSetupChanged();
	}

	private final KeyListener updateOnTypeKeyListener = new KeyAdapter() {
		@Override
		public void keyReleased(KeyEvent e) {
			update();
		}
	};

	private final MouseListener selectOnFocusMouseListener = new MouseAdapter() {
		@Override
		public void mousePressed(MouseEvent e) {
			JTextField field = (JTextField) e.getSource();
			if (!field.isFocusOwner()) field.selectAll();
		}
	};

	private final KeyListener projectNameKeyListener = new KeyAdapter() {
		private String backup;

		@Override
		public void keyPressed(KeyEvent e) {
			JTextField field = (JTextField) e.getSource();
			backup = field.getText();
		}

		@Override
		public void keyReleased(KeyEvent e) {
			JTextField field = (JTextField) e.getSource();
			if (!Pattern.compile("[a-zA-Z0-9_-]*").matcher(field.getText()).matches()) {
				String msg = "Only alphanumeric, '-' and '_' characters are allowed for project name.";
				JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(ConfigSetupPanel.this);
				JOptionPane.showMessageDialog(frame, msg);
				field.setText(backup);
				update();
			}
		}
	};

	private final KeyListener packageNameKeyListener = new KeyAdapter() {
		private String backup;

		@Override
		public void keyPressed(KeyEvent e) {
			JTextField field = (JTextField) e.getSource();
			backup = field.getText();
		}

		@Override
		public void keyReleased(KeyEvent e) {
			JTextField field = (JTextField) e.getSource();
			if (!Pattern.compile("[a-zA-Z0-9_\\.]*").matcher(field.getText()).matches()) {
				String msg = "Only alphanumeric, '_' and '.' characters are allowed for package name.";
				JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(ConfigSetupPanel.this);
				JOptionPane.showMessageDialog(frame, msg);
				field.setText(backup);
				update();
			}
		}
	};

	private final KeyListener mainClassNameKeyListener = new KeyAdapter() {
		private String backup;

		@Override
		public void keyPressed(KeyEvent e) {
			JTextField field = (JTextField) e.getSource();
			backup = field.getText();
		}

		@Override
		public void keyReleased(KeyEvent e) {
			JTextField field = (JTextField) e.getSource();
			if (!Pattern.compile("[a-zA-Z0-9_]*").matcher(field.getText()).matches()) {
				String msg = "Only alphanumeric and '_' characters are allowed for class name.\n";
				JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(ConfigSetupPanel.this);

				JOptionPane.showMessageDialog(frame, msg);
				field.setText(backup);
				update();
			}
		}
	};

	// -------------------------------------------------------------------------
	// Generated stuff
	// -------------------------------------------------------------------------

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        headerPanel = new javax.swing.JPanel();
        headerLabel = new javax.swing.JLabel();
        numberLabel = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        destinationField = new javax.swing.JTextField();
        nameField = new javax.swing.JTextField();
        packageLabel = new javax.swing.JLabel();
        packageField = new javax.swing.JTextField();
        nameLabel = new javax.swing.JLabel();
        destinationLabel = new javax.swing.JLabel();
        mainClassLabel = new javax.swing.JLabel();
        mainClassField = new javax.swing.JTextField();
        stageWidthLabel = new javax.swing.JLabel();
        stageWidthField = new javax.swing.JTextField();
        stageHeightLabel = new javax.swing.JLabel();
        stageHeightField = new javax.swing.JTextField();
        gameWidthLabel = new javax.swing.JLabel();
        gameWidthField = new javax.swing.JTextField();
        gameHeightLabel = new javax.swing.JLabel();
        gameHeightField = new javax.swing.JTextField();
        zoomLabel = new javax.swing.JLabel();
        zoomField = new javax.swing.JTextField();
        advancedSettingsLabel = new javax.swing.JLabel();
        browseBtn = new aurelienribon.ui.components.Button();
        genCorePrjChk = new aurelienribon.ui.CompactCheckBox();
        genAndroidPrjChk = new aurelienribon.ui.CompactCheckBox();
        genDesktopPrjChk = new aurelienribon.ui.CompactCheckBox();
        genHtmlPrjChk = new aurelienribon.ui.CompactCheckBox();

        setLayout(new java.awt.BorderLayout());

        headerLabel.setText("<html> Main parameters defining your project. See the overview panel to know if it suits your needs.");
        headerLabel.setVerticalAlignment(javax.swing.SwingConstants.TOP);

        numberLabel.setText("1");

        javax.swing.GroupLayout headerPanelLayout = new javax.swing.GroupLayout(headerPanel);
        headerPanel.setLayout(headerPanelLayout);
        headerPanelLayout.setHorizontalGroup(
            headerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, headerPanelLayout.createSequentialGroup()
                .addComponent(numberLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(headerLabel, javax.swing.GroupLayout.DEFAULT_SIZE, 321, Short.MAX_VALUE))
        );
        headerPanelLayout.setVerticalGroup(
            headerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(headerLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
            .addComponent(numberLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        add(headerPanel, java.awt.BorderLayout.NORTH);

        jPanel1.setOpaque(false);

        destinationField.setEditable(false);

        packageLabel.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        packageLabel.setText("Package");

        nameLabel.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        nameLabel.setText("Name");

        destinationLabel.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        destinationLabel.setText("Destination");

        mainClassLabel.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        mainClassLabel.setText("Game class");
        
        stageWidthLabel.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        stageWidthLabel.setText("StageWidth");
        
        stageHeightLabel.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        stageHeightLabel.setText("StageHeight");

        gameWidthLabel.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
		gameWidthLabel.setText("GameWidth");
		
		gameHeightLabel.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
		gameHeightLabel.setText("GameHeight");
		
		zoomLabel.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
		zoomLabel.setText("Zoom");
        
        advancedSettingsLabel.setText("Show advanced settings >");

        browseBtn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/res/gfx/ic_folder.png"))); // NOI18N

        genCorePrjChk.setEnabled(false);
        genCorePrjChk.setSelected(true);
        genCorePrjChk.setText("Generate core project (required)");

        genAndroidPrjChk.setEnabled(false);
        genAndroidPrjChk.setSelected(true);
        genAndroidPrjChk.setText("Generate android project (required)");

        genDesktopPrjChk.setSelected(Ctx.cfgSetup.isDesktopIncluded);
        genDesktopPrjChk.setText("Generate desktop project");
        
        genHtmlPrjChk.setSelected(Ctx.cfgSetup.isHtmlIncluded);
        genHtmlPrjChk.setText("Generate html project");
        genHtmlPrjChk.setVisible(false);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(nameLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 54, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(nameField, javax.swing.GroupLayout.DEFAULT_SIZE, 255, Short.MAX_VALUE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(packageLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 54, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(packageField))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(destinationLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(mainClassLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(destinationField, javax.swing.GroupLayout.DEFAULT_SIZE, 200, Short.MAX_VALUE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(browseBtn, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(mainClassField)))
                    .addGroup(jPanel1Layout.createSequentialGroup()
						.addComponent(stageWidthLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
						.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
						.addComponent(stageWidthField, javax.swing.GroupLayout.DEFAULT_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
						.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
						.addComponent(stageHeightLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
						.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
						.addComponent(stageHeightField, javax.swing.GroupLayout.DEFAULT_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
					.addGroup(jPanel1Layout.createSequentialGroup()
						.addComponent(gameWidthLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
						.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
						.addComponent(gameWidthField, javax.swing.GroupLayout.DEFAULT_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
						.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
						.addComponent(gameHeightLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
						.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
						.addComponent(gameHeightField, javax.swing.GroupLayout.DEFAULT_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
						.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
						.addComponent(zoomLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
						.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
						.addComponent(zoomField, javax.swing.GroupLayout.DEFAULT_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))	
					.addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(advancedSettingsLabel))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(genCorePrjChk, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(genAndroidPrjChk, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(genDesktopPrjChk, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(genHtmlPrjChk, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(nameLabel)
                    .addComponent(nameField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(packageLabel)
                    .addComponent(packageField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(mainClassLabel)
                    .addComponent(mainClassField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(destinationLabel)
                    .addComponent(destinationField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(browseBtn, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
				.addGroup(
					jPanel1Layout
						.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
						.addComponent(stageWidthLabel)
						.addComponent(stageWidthField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE,
							javax.swing.GroupLayout.PREFERRED_SIZE)
						.addComponent(stageHeightLabel)
						.addComponent(stageHeightField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE,
							javax.swing.GroupLayout.PREFERRED_SIZE))
				.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
				.addGroup(
					jPanel1Layout
						.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
						.addComponent(gameWidthLabel)
						.addComponent(gameWidthField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE,
							javax.swing.GroupLayout.PREFERRED_SIZE)
						.addComponent(gameHeightLabel)
						.addComponent(gameHeightField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE,
							javax.swing.GroupLayout.PREFERRED_SIZE)
						.addComponent(zoomLabel)
						.addComponent(zoomField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE,
							javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(genCorePrjChk, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(genAndroidPrjChk, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(genDesktopPrjChk, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(genHtmlPrjChk, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(advancedSettingsLabel)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel1Layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {browseBtn, destinationField, nameLabel, packageLabel, destinationLabel, mainClassLabel, mainClassField, nameField, packageField,
        	stageWidthLabel, stageWidthField, stageHeightLabel, stageHeightField, gameWidthLabel, gameWidthField, gameHeightLabel, gameHeightField, zoomLabel, zoomField});

        add(jPanel1, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel advancedSettingsLabel;
    private aurelienribon.ui.components.Button browseBtn;
    private javax.swing.JTextField destinationField;
    private aurelienribon.ui.CompactCheckBox genAndroidPrjChk;
    private aurelienribon.ui.CompactCheckBox genCorePrjChk;
    private aurelienribon.ui.CompactCheckBox genDesktopPrjChk;
    private aurelienribon.ui.CompactCheckBox genHtmlPrjChk;
    private javax.swing.JPanel headerPanel;
    private javax.swing.JLabel nameLabel;
    private javax.swing.JLabel packageLabel;
    private javax.swing.JLabel destinationLabel;
    private javax.swing.JLabel headerLabel;
    private javax.swing.JLabel mainClassLabel;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JTextField mainClassField;
    private javax.swing.JTextField nameField;
    private javax.swing.JLabel numberLabel;
    private javax.swing.JTextField packageField;
    private javax.swing.JLabel stageWidthLabel;
    private javax.swing.JTextField stageWidthField;
    private javax.swing.JLabel stageHeightLabel;
    private javax.swing.JTextField stageHeightField;
    private javax.swing.JLabel gameWidthLabel;
    private javax.swing.JTextField gameWidthField;
    private javax.swing.JLabel gameHeightLabel;
    private javax.swing.JTextField gameHeightField;
    private javax.swing.JLabel zoomLabel;
    private javax.swing.JTextField zoomField;
    // End of variables declaration//GEN-END:variables

}
