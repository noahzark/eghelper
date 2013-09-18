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
	private JTextField jTextFieldUID;
	private JLabel jLabelUID;
	private JTextField jTextFieldAID3;
	private JLabel jLabelMaxLine;
	private JLabel jLabelAdvanceInfo;
	private JLabel jLabelBasicInfo;
	private JTextField jTextFieldSTUp;
	private JLabel jLabelST2;
	private JTextField jTextFieldSTDown;
	private JLabel jLabelST1;
	private JTextField jTextFieldMaxLine;
	private JCheckBox jCheckBoxShowRank;
	private JTextField jTextFieldAID2;
	private JLabel jLabelAID3;
	private JLabel jLabelAID2;
	private JTextField jTextFieldAID1;
	private JLabel jLabelAID1;
	private JButton jButtonCancel;
	private JButton jButtonConfirm;
	private JPanel jPanelButtons;
	private NewConfigListener ncl;
	public StartMenuFrame frame;

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
					jLabelBasicInfo = new JLabel();
					jPanelContaint.add(jLabelBasicInfo);
					jLabelBasicInfo.setText("--------------------Basic Infomation--------------------");
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
				{
					jLabelAdvanceInfo = new JLabel();
					jPanelContaint.add(jLabelAdvanceInfo);
					jLabelAdvanceInfo.setText("-------------------Advance Infomation-------------------");
				}
				{
					jLabelST1 = new JLabel();
					jPanelContaint.add(jLabelST1);
					jLabelST1.setText("Keep stamia points in the range: ");
				}
				{
					jTextFieldSTDown = new JTextField();
					jPanelContaint.add(jTextFieldSTDown);
					jTextFieldSTDown.setText("20");
					jTextFieldSTDown.setPreferredSize(new java.awt.Dimension(30, 21));
				}
				{
					jLabelST2 = new JLabel();
					jPanelContaint.add(jLabelST2);
					jLabelST2.setText("to Max ST-");
				}
				{
					jTextFieldSTUp = new JTextField();
					jPanelContaint.add(jTextFieldSTUp);
					jTextFieldSTUp.setText("30");
					jTextFieldSTUp.setPreferredSize(new java.awt.Dimension(30, 21));
				}
				{
					jLabelMaxLine = new JLabel();
					jPanelContaint.add(jLabelMaxLine);
					jLabelMaxLine.setText("MaxLine of histroy: ");
				}
				{
					jTextFieldMaxLine = new JTextField();
					jPanelContaint.add(getJTextFieldMaxLine());
					jTextFieldMaxLine.setText("1000");
					jTextFieldMaxLine.setPreferredSize(new java.awt.Dimension(70, 21));
				}
				{
					jCheckBoxShowRank = new JCheckBox();
					jPanelContaint.add(getJCheckBoxShowRank());
					jCheckBoxShowRank.setText("ShowRank");
					jCheckBoxShowRank.setSelected(true);
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
			this.setSize(360, 260);
			Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize(); 
			setBounds((screenSize.width - this.getWidth()) / 2, (screenSize.height - this.getHeight()) / 2, this.getWidth(), this.getHeight());
			this.setResizable(false);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public JCheckBox getJCheckBoxShowRank() {
		return jCheckBoxShowRank;
	}
	
	public JTextField getJTextFieldMaxLine() {
		return jTextFieldMaxLine;
	}
	
	public JTextField getJTextFieldSTDown() {
		return jTextFieldSTDown;
	}
	
	public JTextField getJTextFieldSTUp() {
		return jTextFieldSTUp;
	}

}
