
package aurelienribon.gdxsetupui.ui.panels;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import org.apache.commons.io.FileUtils;

import aurelienribon.gdxsetupui.ui.MainPanel;
import aurelienribon.ui.css.Style;
import aurelienribon.utils.HttpUtils;
import aurelienribon.utils.HttpUtils.DownloadListener;

/** @author Aurelien Ribon | http://www.aurelienribon.com/ */
public class DownloadPanel extends javax.swing.JPanel {
	
	private static final long serialVersionUID = -607526673670168603L;
	private Callback callback;
	private String out;

	public DownloadPanel (final MainPanel mainPanel) {
		
		initComponents();
		Style.registerCssClasses(headerPanel, ".header");
		Style.registerCssClasses(jLabel2, ".statusLabel");
		Style.registerCssClasses(jLabel1, ".bold");
	}

	public void setup(Callback callback, String in, String out) {
		this.callback = callback;
		this.out = "./" + out;

		nameLabel.setText(in);
		countLabel.setText("...waiting for response from the server...");
		try {
			new File(out).getCanonicalFile().getParentFile().mkdirs();
		} catch (IOException ex) {
			assert false;
		}
		OutputStream outputStream;
		try {
			outputStream = new BufferedOutputStream(new FileOutputStream(out + ".tmp"));
		} catch (IOException ex) {
			throw new RuntimeException(ex);
		}
		final HttpUtils.DownloadTask task = HttpUtils.downloadAsync(in, outputStream, "tag");
		task.addListener(fullCallback);
	}
	
	public static interface Callback {
		public void completed ();
	}

	private final DownloadListener fullCallback = new DownloadListener() {
		@Override
		public void onComplete () {
			System.gc();
			try {
				FileUtils.deleteQuietly(new File(out));
				FileUtils.moveFile(new File(out + ".tmp"), new File(out));
				//dispose();
				callback.completed();
			} catch (IOException ex) {
				//String msg = "Could not rename \"" + out + ".tmp" + "\" into \"" + out + "\"";
				System.out.println(ex.getMessage() + " " + new File(out + ".tmp"));
				//JOptionPane.showMessageDialog(SwingUtils.getJFrame(DownloadPanel.this), msg + ex.getMessage());
				//dispose();
			}
		}

		@Override
		public void onCancel () {
			FileUtils.deleteQuietly(new File(out + ".tmp"));
			System.out.println(out + ".tmp");
			//dispose();
		}

		@Override
		public void onError (IOException ex) {
			FileUtils.deleteQuietly(new File(out + ".tmp"));
			String msg = "Something went wrong during the download.\n" + ex.getClass().getSimpleName() + ": " + ex.getMessage();
			System.out.println(msg);
		}

		@Override
		public void onUpdate (int length, int totalLength) {
			if (totalLength > 0) {
				progressBar.setIndeterminate(false);
				progressBar.setValue((int)Math.round((double)length * 100 / totalLength));
				countLabel.setText((length / 1024) + " / " + (totalLength / 1024));
			} else {
				progressBar.setIndeterminate(true);
				countLabel.setText((length / 1024) + " / unknown");
			}
		}
	};

	// -------------------------------------------------------------------------
	// Generated stuff
	// -------------------------------------------------------------------------

	
	// <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
	private void initComponents () {

		jPanel1 = new javax.swing.JPanel();
		headerPanel = new javax.swing.JPanel();
		headerLabel = new javax.swing.JLabel();
		jLabel1 = new javax.swing.JLabel();
		progressBar = new javax.swing.JProgressBar();
		countLabel = new javax.swing.JLabel();
		jLabel2 = new javax.swing.JLabel();
		nameLabel = new javax.swing.JLabel();

		setLayout(new java.awt.BorderLayout());

        jPanel1.setOpaque(false);

		jLabel1.setText("Downloading: ");

		countLabel.setText("xxx");
		
		nameLabel.setText("xxx");

		jLabel2.setText("KiloBytes downloaded: ");
		
		javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
						.addComponent(jLabel1)
						.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
						.addComponent(nameLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 470, javax.swing.GroupLayout.PREFERRED_SIZE))
					.addGroup(jPanel1Layout.createSequentialGroup()
						.addComponent(jLabel2)
						.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
						.addComponent(countLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(progressBar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))          
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(nameLabel)
                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(progressBar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                	.addComponent(jLabel2)
                	.addComponent(countLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        add(jPanel1, java.awt.BorderLayout.CENTER);
		
		headerLabel.setText("<html> Download in progress...");
        headerLabel.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        
        javax.swing.GroupLayout headerPanelLayout = new javax.swing.GroupLayout(headerPanel);
        headerPanel.setLayout(headerPanelLayout);
        
        headerPanelLayout.setHorizontalGroup(
            headerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(headerLabel, javax.swing.GroupLayout.DEFAULT_SIZE, 249, Short.MAX_VALUE)
        );
        headerPanelLayout.setVerticalGroup(
            headerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            	.addComponent(headerLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        add(headerPanel, java.awt.BorderLayout.NORTH);
	}// </editor-fold>//GEN-END:initComponents
		// Variables declaration - do not modify//GEN-BEGIN:variables

	private javax.swing.JLabel countLabel;
	private javax.swing.JLabel jLabel1;
	private javax.swing.JLabel jLabel2;
	private javax.swing.JLabel nameLabel;
	private javax.swing.JLabel headerLabel;
    private javax.swing.JPanel headerPanel;
    private javax.swing.JPanel jPanel1;
	private javax.swing.JProgressBar progressBar;
	// End of variables declaration//GEN-END:variables

}
