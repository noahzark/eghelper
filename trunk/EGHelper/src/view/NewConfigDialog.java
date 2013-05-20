package view;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Toolkit;

import javax.swing.JButton;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import control.EGMessenger;
import control.NewConfigListener;


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
public class NewConfigDialog extends javax.swing.JDialog {
	/**
	 * 
	 */
	private static final long serialVersionUID = -4931944315952342100L;
	private JPanel jPanelContaint;
	private JTextField jTextFieldApp;
	private JTextField jTextFieldDigest;
	private JTextField jTextFieldUID;
	private JLabel jLabelUID;
	private JTextField jTextFieldAID3;
	private JTextField jTextFieldAID2;
	private JLabel jLabelAID3;
	private JLabel jLabelAID2;
	private JTextField jTextFieldAID1;
	private JLabel jLabelAID1;
	private JLabel jLabelDigest;
	private JTextField jTextFieldSDK;
	private JLabel jLabelSDK;
	private JLabel jLabelApp;
	private JButton jButtonCancel;
	private JButton jButtonConfirm;
	private JPanel jPanelButtons;
	private NewConfigListener ncl;
	public StartMenuFrame frame;

	public JTextField getjTextFieldApp() {
		return jTextFieldApp;
	}

	public JTextField getjTextFieldSDK() {
		return jTextFieldSDK;
	}

	public JTextField getjTextFieldDigest() {
		return jTextFieldDigest;
	}

	public JTextField getjTextFieldUID() {
		return jTextFieldUID;
	}

	public JTextField getjTextFieldAID3() {
		return jTextFieldAID3;
	}

	public JTextField getjTextFieldAID2() {
		return jTextFieldAID2;
	}

	public JTextField getjTextFieldAID1() {
		return jTextFieldAID1;
	}

	public JButton getjButtonCancel() {
		return jButtonCancel;
	}

	public JButton getjButtonConfirm() {
		return jButtonConfirm;
	}

	{
		//Set Look & Feel
		try {
			javax.swing.UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public NewConfigDialog(StartMenuFrame frame, EGMessenger carrier) {
		super(frame, "新建配置文件");
		this.frame = frame;
		ncl = new NewConfigListener(this, carrier);
		initGUI();
	}
	
	private void initGUI() {
		try {
			{
			}
			BorderLayout thisLayout = new BorderLayout();
			getContentPane().setLayout(thisLayout);
			{
				jPanelContaint = new JPanel();
				FlowLayout jPanelContaintLayout = new FlowLayout();
				getContentPane().add(jPanelContaint, BorderLayout.CENTER);
				jPanelContaint.setLayout(jPanelContaintLayout);
				jPanelContaint.setPreferredSize(new java.awt.Dimension(344, 226));
				{
					jLabelApp = new JLabel();
					jPanelContaint.add(jLabelApp);
					jLabelApp.setText("APP: ");
				}
				{
					jTextFieldApp = new JTextField();
					jPanelContaint.add(jTextFieldApp);
					jTextFieldApp.setText("1.3");
					jTextFieldApp.setPreferredSize(new java.awt.Dimension(120, 21));
				}
				{
					jLabelSDK = new JLabel();
					jPanelContaint.add(jLabelSDK);
					jLabelSDK.setText("SDK: ");
				}
				{
					jTextFieldSDK = new JTextField();
					jPanelContaint.add(jTextFieldSDK);
					jTextFieldSDK.setText("5.1.0");
					jTextFieldSDK.setPreferredSize(new java.awt.Dimension(120, 21));
				}
				{
					jLabelDigest = new JLabel();
					jPanelContaint.add(jLabelDigest);
					jLabelDigest.setText("Digest: ");
				}
				{
					jTextFieldDigest = new JTextField();
					jPanelContaint.add(jTextFieldDigest);
					jTextFieldDigest.setText("7fc15376099483231440dfef76540a9678a99fa5dc977c35b55b9e46ee7444c4");
					jTextFieldDigest.setPreferredSize(new java.awt.Dimension(262, 21));
				}
				{
					jLabelAID1 = new JLabel();
					jPanelContaint.add(jLabelAID1);
					jLabelAID1.setText("APP-ID-1: ");
				}
				{
					jTextFieldAID1 = new JTextField();
					jPanelContaint.add(jTextFieldAID1);
					jTextFieldAID1.setText("YOUR ID1");
					jTextFieldAID1.setPreferredSize(new java.awt.Dimension(250, 21));
				}
				{
					jLabelAID2 = new JLabel();
					jPanelContaint.add(jLabelAID2);
					jLabelAID2.setText("APP-ID-2: ");
				}
				{
					jTextFieldAID2 = new JTextField();
					jPanelContaint.add(jTextFieldAID2);
					jTextFieldAID2.setText("YOUR ID2");
					jTextFieldAID2.setPreferredSize(new java.awt.Dimension(250, 21));
				}
				{
					jLabelAID3 = new JLabel();
					jPanelContaint.add(jLabelAID3);
					jLabelAID3.setText("APP-ID-3: ");
				}
				{
					jTextFieldAID3 = new JTextField();
					jPanelContaint.add(jTextFieldAID3);
					jTextFieldAID3.setText("YOUR ID3");
					jTextFieldAID3.setPreferredSize(new java.awt.Dimension(250, 21));
				}
				{
					jLabelUID = new JLabel();
					jPanelContaint.add(jLabelUID);
					jLabelUID.setText("UID: ");
				}
				{
					jTextFieldUID = new JTextField();
					jPanelContaint.add(jTextFieldUID);
					jTextFieldUID.setText("YOUR UID");
					jTextFieldUID.setPreferredSize(new java.awt.Dimension(280, 21));
				}
			}
			{
				jPanelButtons = new JPanel();
				getContentPane().add(jPanelButtons, BorderLayout.SOUTH);
				jPanelButtons.setPreferredSize(new java.awt.Dimension(355, 35));
				{
					jButtonConfirm = new JButton();
					jPanelButtons.add(jButtonConfirm);
					jButtonConfirm.setText("\u786e\u5b9a");
					jButtonConfirm.setPreferredSize(new java.awt.Dimension(80, 25));
					jButtonConfirm.addActionListener(ncl);
				}
				{
					jButtonCancel = new JButton();
					jPanelButtons.add(jButtonCancel);
					jButtonCancel.setText("\u53d6\u6d88");
					jButtonCancel.setPreferredSize(new java.awt.Dimension(80, 25));
					jButtonCancel.addActionListener(ncl);
				}
			}
			this.setSize(360, 234);
			Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize(); 
			setBounds((screenSize.width - this.getWidth()) / 2, (screenSize.height - this.getHeight()) / 2, this.getWidth(), this.getHeight());
			this.setResizable(false);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
