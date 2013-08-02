package control;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.time.Millisecond;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;

public class RealTimeChart extends ChartPanel implements Runnable{
	private EGMessenger carrier;
	
	private static final long serialVersionUID = 3324082823827833305L;
	private static TimeSeries timeSeries[];
	
	private int[] levels;
	
	private boolean stopShow = false;
	
	public RealTimeChart(
			EGMessenger egMessenger, 
			int[] dataTitles,
			String title,
			String xaxisName,
			String yaxisName){
		super(createChart(dataTitles, title, xaxisName, yaxisName));
		carrier = egMessenger;
		levels = dataTitles;
	}

	@SuppressWarnings("deprecation")
	private static JFreeChart createChart(
			int[] dataTitles,
			String title,
			String xaxisName,
			String yaxisName
	){
		TimeSeriesCollection timeseriescollection = new TimeSeriesCollection();
		timeSeries = new TimeSeries[dataTitles.length];
		
		for (int i=0; i<timeSeries.length;i++){
			timeSeries[i] = new TimeSeries("No."+dataTitles[i], Millisecond.class);
			timeseriescollection.addSeries(timeSeries[i]);
		}
		
		JFreeChart jfreechart = ChartFactory.createTimeSeriesChart(
				title,
				xaxisName,
				yaxisName,
				timeseriescollection,
				true,
				true,
				false
		);
		XYPlot xyplot = jfreechart.getXYPlot();
		ValueAxis valueaxis = xyplot.getDomainAxis();
		valueaxis.setAutoRange(true);
		long t = 7200;
		valueaxis.setFixedAutoRange(t*1000D);

		valueaxis = xyplot.getRangeAxis();
		//valueaxis.setRange(0.0D,200D);

		return jfreechart;
	}

	public void run(){
		while(true){
			if (this.isStopShow())
				break;
			try{
				Thread.sleep(15000);
				for (int i=0; i<timeSeries.length;i++){
					timeSeries[i].add(new Millisecond(), carrier.getRankInfo(levels[i]));
				}
			} catch (InterruptedException e) {
			}
		} 
	}

	public boolean isStopShow() {
		return stopShow;
	}

	public void setStopShow(boolean stopShow) {
		this.stopShow = stopShow;
	}
} 