package view;
import java.awt.BorderLayout;
import java.awt.TextArea;
import javax.swing.JButton;
import javax.swing.JLabel;

import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JTextArea;
import javax.swing.WindowConstants;

import control.EGMessenger;
import control.InfoListener;


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
public class InfoFrame extends javax.swing.JFrame {

	private static final long serialVersionUID = 6701513318805553827L;
	private TextArea TextArea;
	private JPanel jPanelButtons;
	private JButton jButtonClear;
	private JProgressBar jProgressBarBP;
	private JLabel jLabelBP;
	private JProgressBar jProgressBarST;
	private JLabel jLabelST;
	private TextArea TextAreaList;
	private TextArea jTextAreaRankInfo;
	private JPanel ListPanel;
	private JProgressBar jProgressBarEXP;
	private JLabel jLabelEXP;
	private JButton jButtonManual;
	private JButton jButtonClose;
	private JPanel jPanelBars;
	private InfoListener il;

	{
		//Set Look & Feel
		try {
			javax.swing.UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public InfoFrame(String string, EGMessenger carrier) {
		super(string);
		il = new InfoListener(this,carrier);
		initGUI();
		carrier.setInst(this);
	}
	
	private void initGUI() {
		try {
			BorderLayout thisLayout = new BorderLayout();
			this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
			getContentPane().setLayout(thisLayout);
			{
				TextArea = new TextArea("", 1, 1, 1);
				getContentPane().add(getTextArea(), BorderLayout.CENTER);
				TextArea.setEditable(false);
			}
			{
				jPanelButtons = new JPanel();
				getContentPane().add(jPanelButtons, BorderLayout.SOUTH);
				jPanelButtons.setPreferredSize(new java.awt.Dimension(384, 34));
				{
					jButtonManual = new JButton();
					jPanelButtons.add(jButtonManual);
					jButtonManual.setText("\u624b\u52a8");
					jButtonManual.setPreferredSize(new java.awt.Dimension(80, 25));
					jButtonManual.addActionListener(il);
					jButtonManual.setEnabled(false);
				}
				{
					jButtonClear = new JButton();
					jPanelButtons.add(jButtonClear);
					jButtonClear.setText("\u6e05\u7a7a");
					jButtonClear.setPreferredSize(new java.awt.Dimension(80, 25));
					jButtonClear.addActionListener(il);
				}
				{
					jButtonClose = new JButton();
					jPanelButtons.add(jButtonClose);
					jButtonClose.setText("\u9000\u51fa");
					jButtonClose.setPreferredSize(new java.awt.Dimension(80, 25));
					jButtonClose.addActionListener(il);
				}
			}
			{
				jPanelBars = new JPanel();
				getContentPane().add(jPanelBars, BorderLayout.NORTH);
				jPanelBars.setPreferredSize(new java.awt.Dimension(624, 28));
				{
					jLabelST = new JLabel();
					jPanelBars.add(jLabelST);
					jLabelST.setText("ST:");
				}
				{
					jProgressBarST = new JProgressBar();
					jPanelBars.add(jProgressBarST);
					jProgressBarST.setPreferredSize(new java.awt.Dimension(120, 18));
					jProgressBarST.setIndeterminate(true);
				}
				{
					jLabelBP = new JLabel();
					jPanelBars.add(jLabelBP);
					jLabelBP.setText("BP:");
				}
				{
					jProgressBarBP = new JProgressBar();
					jPanelBars.add(getJProgressBarBP());
					jProgressBarBP.setPreferredSize(new java.awt.Dimension(120, 18));
					jProgressBarBP.setIndeterminate(true);
				}
				{
					jLabelEXP = new JLabel();
					jPanelBars.add(jLabelEXP);
					jLabelEXP.setText("EP:");
				}
				{
					jProgressBarEXP = new JProgressBar();
					jPanelBars.add(getJProgressBarEXP());
					jProgressBarEXP.setPreferredSize(new java.awt.Dimension(120, 18));
					jProgressBarEXP.setIndeterminate(true);
				}
			}
			{
				ListPanel = new JPanel();
				BorderLayout jPanel1Layout = new BorderLayout();
				getContentPane().add(ListPanel, BorderLayout.EAST);
				ListPanel.setLayout(jPanel1Layout);
				ListPanel.setPreferredSize(new java.awt.Dimension(200, 309));
				{
					TextAreaList = new TextArea();
					ListPanel.add(TextAreaList);
					TextAreaList.setText("ListsHere");
					TextAreaList.setPreferredSize(new java.awt.Dimension(200, 100));
					TextAreaList.setEditable(false);
				}
				{
					jTextAreaRankInfo = new TextArea();
					ListPanel.add(jTextAreaRankInfo, BorderLayout.SOUTH);
					jTextAreaRankInfo.setText("RankInfoHere");
					jTextAreaRankInfo.setPreferredSize(new java.awt.Dimension(200, 115));
					jTextAreaRankInfo.setEditable(false);
				}
			}
			pack();
			this.setSize(640, 400);
			this.setResizable(false);
		} catch (Exception e) {
		    //add your error handling code here
			e.printStackTrace();
		}
	}
	
	public TextArea getTextArea() {
		return TextArea;
	}

	public JButton getjButtonClear() {
		return jButtonClear;
	}

	public JButton getjButtonClose() {
		return jButtonClose;
	}

	public JButton getjButtonManual() {
		return jButtonManual;
	}
	
	public TextArea getTextAreaList() {
		return TextAreaList;
	}
	
	public JProgressBar getJProgressBarST() {
		return jProgressBarST;
	}
	
	public JProgressBar getJProgressBarBP() {
		return jProgressBarBP;
	}
	
	public JProgressBar getJProgressBarEXP() {
		return jProgressBarEXP;
	}

	public TextArea getjTextAreaRankInfo() {
		return jTextAreaRankInfo;
	}
	

}
