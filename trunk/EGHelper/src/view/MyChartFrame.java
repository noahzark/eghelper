package view;

import java.awt.BorderLayout; 
import java.awt.event.WindowAdapter; 
import java.awt.event.WindowEvent; 
import javax.swing.JFrame; 

import control.EGMessenger;
import control.RealTimeChart;
 
public class MyChartFrame{
	private JFrame frame = null;
	private RealTimeChart rtcp = null;
 
	public MyChartFrame(EGMessenger egMessenger, int[] levels) {
		frame = new JFrame("排名信息变化趋势"); 
		rtcp = new RealTimeChart(
				egMessenger,
				levels,
				"Ranking - Time Data Graph",
				"Time(min)",
				"Ranking(pt)"
		);
		frame.getContentPane().add(rtcp,BorderLayout.CENTER); 
		frame.pack(); 
		frame.setVisible(true);
		Thread t = new Thread(rtcp);
		egMessenger.setMonitor(t);
		t.start(); 
		frame.addWindowListener(
			new WindowAdapter(){ 
				public void windowClosing(WindowEvent windowevent){ 
					windowevent.getWindow().dispose();
				} 
		 
			}
		); 
	}

	public void dispose() {
		rtcp.setStopShow(true);
		this.frame.dispose();
	}
}
