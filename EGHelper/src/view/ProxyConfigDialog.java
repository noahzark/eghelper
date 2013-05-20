package view;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Toolkit;

import javax.swing.JButton;
import javax.swing.JCheckBox;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import control.EGMessenger;
import control.ProxyConfigListener;

/**
* This code was edited or generated using CloudGarden's Jigloo
* SWT/Swing GUI Builder, which is free for non-commercial
* use. If Jigloo is being used commercially (ie, by a corporation,
* company or business for any purpose whatever) then you
* should purchase a license for each developer using Jigloo.
* Please visit www.cloudgarden.com for details.
* Use of Jigloo implies acceptance of these licensing terms.
* A COMMERCIAL LICENSE HAS NOT BEEN PURCHASED FOR
* THIS MACHINE, SO JIGLOO OR THIS CODE CANNOT BE USED
* LEGALLY FOR ANY CORPORATE OR COMMERCIAL PURPOSE.
*/
public class ProxyConfigDialog extends javax.swing.JDialog {
	/**
	 * 
	 */
	private static final long serialVersionUID = 8657498419994562112L;
	private JCheckBox jCheckBoxUseProxy;
	private JLabel jLabelPort;
	private JTextField jTextFieldPort;
	private JTextField jTextFieldAdress;
	private JLabel jLabelServer;
	private JPanel jPanel;
	private JButton jButtonConfirm;
	private ProxyConfigListener pcl;

	{
		//Set Look & Feel
		try {
			javax.swing.UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public ProxyConfigDialog(StartMenuFrame frame, EGMessenger carrier) {
		super(frame);
		this.pcl = new ProxyConfigListener(this, frame, carrier);
		initGUI();
	}
	
	private void initGUI() {
		try {
			{
			}
			BorderLayout thisLayout = new BorderLayout();
			getContentPane().setLayout(thisLayout);
			{
				jCheckBoxUseProxy = new JCheckBox();
				getContentPane().add(jCheckBoxUseProxy, BorderLayout.NORTH);
				jCheckBoxUseProxy.setText("\u4f7f\u7528\u4ee3\u7406\u670d\u52a1\u5668");
				jCheckBoxUseProxy.addItemListener(pcl);
			}
			{
				jButtonConfirm = new JButton();
				getContentPane().add(jButtonConfirm, BorderLayout.SOUTH);
				jButtonConfirm.setText("\u786e\u5b9a");
				jButtonConfirm.setPreferredSize(new java.awt.Dimension(384, 27));
				jButtonConfirm.addActionListener(pcl);
			}
			{
				jPanel = new JPanel();
				FlowLayout jPanelLayout = new FlowLayout();
				getContentPane().add(jPanel, BorderLayout.CENTER);
				jPanel.setLayout(jPanelLayout);
				jPanel.setPreferredSize(new java.awt.Dimension(315, 211));
				{
					jLabelServer = new JLabel();
					jPanel.add(jLabelServer);
					jLabelServer.setText("\u670d\u52a1\u5668\u5730\u5740\uff1a");
				}
				{
					jTextFieldAdress = new JTextField();
					jPanel.add(getJTextFieldAdress());
					jTextFieldAdress.setText("127.0.0.1");
					jTextFieldAdress.setPreferredSize(new java.awt.Dimension(150, 21));
					jTextFieldAdress.setEnabled(false);
				}
				{
					jLabelPort = new JLabel();
					jPanel.add(jLabelPort);
					jLabelPort.setText("\u670d\u52a1\u5668\u7aef\u53e3\uff1a");
				}
				{
					jTextFieldPort = new JTextField();
					jPanel.add(jTextFieldPort);
					jTextFieldPort.setText("8765");
					jTextFieldPort.setPreferredSize(new java.awt.Dimension(150, 21));
					jTextFieldPort.setEnabled(false);
				}
			}
			this.setSize(268, 146);
			Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize(); 
			setBounds((screenSize.width - this.getWidth()) / 2, (screenSize.height - this.getHeight()) / 2, this.getWidth(), this.getHeight());
			this.setResizable(false);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public JTextField getJTextFieldAdress() {
		return jTextFieldAdress;
	}

	public JCheckBox getjCheckBoxUseProxy() {
		return jCheckBoxUseProxy;
	}

	public JTextField getjTextFieldPort() {
		return jTextFieldPort;
	}

	public JButton getjButtonConfirm() {
		return jButtonConfirm;
	}

}
