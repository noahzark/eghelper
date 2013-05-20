package view;
import java.awt.FlowLayout;
import javax.swing.JButton;

import javax.swing.WindowConstants;

import control.EGMessenger;
import control.StartMenuListener;


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
public class StartMenuFrame extends javax.swing.JFrame {

	private static final long serialVersionUID = 166548626181499675L;
	private JButton jButtonLoadCfg;
	private JButton jButtonNewCfg;
	private JButton jButtonSetProxy;
	private JButton jButtonStart;
	private JButton jButtonConfig;
	private StartMenuListener sml;

	public JButton getjButtonLoadCfg() {
		return jButtonLoadCfg;
	}

	public JButton getjButtonNewCfg() {
		return jButtonNewCfg;
	}

	public JButton getjButtonSetProxy() {
		return jButtonSetProxy;
	}

	public JButton getjButtonStart() {
		return jButtonStart;
	}

	public JButton getjButtonConfig() {
		return jButtonConfig;
	}

	{
		//Set Look & Feel
		try {
			javax.swing.UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public StartMenuFrame(String name, EGMessenger carrier) {
		super(name);
		this.sml = new StartMenuListener(this,carrier);
		initGUI();
		sml.checkAllButtons();
	}
	
	private void initGUI() {
		try {
			FlowLayout thisLayout = new FlowLayout();
			getContentPane().setLayout(thisLayout);
			setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
			{
				jButtonLoadCfg = new JButton();
				getContentPane().add(jButtonLoadCfg);
				jButtonLoadCfg.setText("\u52a0\u8f7d\u5e94\u7528\u914d\u7f6e");
				jButtonLoadCfg.setPreferredSize(new java.awt.Dimension(150, 30));
				jButtonLoadCfg.addActionListener(sml);
			}
			{
				jButtonNewCfg = new JButton();
				getContentPane().add(jButtonNewCfg);
				jButtonNewCfg.setText("\u65b0\u5efa\u5e94\u7528\u914d\u7f6e");
				jButtonNewCfg.setPreferredSize(new java.awt.Dimension(150, 30));
				jButtonNewCfg.addActionListener(sml);
			}
			{
				jButtonSetProxy = new JButton();
				getContentPane().add(jButtonSetProxy);
				jButtonSetProxy.setText("\u8bbe\u7f6e\u4ee3\u7406\u670d\u52a1\u5668");
				jButtonSetProxy.setPreferredSize(new java.awt.Dimension(150, 30));
				jButtonSetProxy.addActionListener(sml);
			}
			{
				jButtonConfig = new JButton();
				getContentPane().add(jButtonConfig);
				jButtonConfig.setText("\u7b56\u7565\u89c4\u5219\u8bbe\u7f6e");
				jButtonConfig.setPreferredSize(new java.awt.Dimension(150, 30));
				jButtonConfig.addActionListener(sml);
			}
			{
				jButtonStart = new JButton();
				getContentPane().add(jButtonStart);
				jButtonStart.setText("\u542f\u52a8");
				jButtonStart.setPreferredSize(new java.awt.Dimension(150, 30));
				jButtonStart.addActionListener(sml);
			}
			pack();
			this.setSize(250, 220);
			this.setResizable(false);
		} catch (Exception e) {
		    //add your error handling code here
			e.printStackTrace();
		}
	}

}
