package com.doesntexist.milki;

import javax.swing.JFrame;
import javax.swing.JPanel;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PiePlot3D;
import org.jfree.data.general.DefaultPieDataset;

public class PieChartPanel {
	
	private DefaultPieDataset getDataset() {
		String[] section = new String[] { "Jan","Feb","Mar","Apr","May","Jun", "Jul","Aug","Sep","Oct","Nov","Dec" };
		double[] data = new double[section.length];   
		 for (int i = 0; i < data.length; i++) {
		     data[i] = 10 + (Math.random() * 10);
		}

		 DefaultPieDataset dataset = new DefaultPieDataset();   
		 for (int i = 0; i < data.length; i++) {
		        dataset.setValue(section[i], data[i]);   
		 }
		 return dataset;   
	}
	
	private static JFreeChart createChart(DefaultPieDataset dataset) {
		JFreeChart chart = ChartFactory.createPieChart3D("Demo", dataset, true, true, false);
		return chart;   
	}   
 
	public  JPanel createDemoPanel() {   
		JFreeChart chart = createChart(getDataset());
		PiePlot3D plot = (PiePlot3D) chart.getPlot();
		plot.setForegroundAlpha(0.5F);
		return new ChartPanel(chart);
	}
	
	public static void main(String[] args) {   
		JFrame demo = new JFrame();
		demo.setContentPane(new PieChartPanel().createDemoPanel());
		demo.pack();
		demo.setVisible(true);   
	}   
}   