package com.doesntexist.milki;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import javax.swing.JFrame;
import javax.swing.JPanel;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.labels.StandardPieSectionLabelGenerator;
import org.jfree.chart.plot.PieLabelLinkStyle;
import org.jfree.chart.plot.PiePlot3D;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.ui.RectangleEdge;

import com.doesntexist.milki.abstractModel.Account;
import com.doesntexist.milki.abstractModel.Category;
import com.doesntexist.milki.abstractModel.Entry;

public class PieChart {
	private JPanel panel;
	private ArrayList<Entry> data;
	private ArrayList<Category> categoryList;
	private ArrayList<Account> accountList;
	private double startAngle = Math.random()*360;
	
	public PieChart() {
	}
	
	public PieChart(ArrayList<Entry> data, ArrayList<Category> categoryList, ArrayList<Account> accountList) {
		this.data = data;
		this.categoryList = categoryList;
		this.accountList = accountList;
	}
	
	private DefaultPieDataset getDataset(int comboIndex, Calendar cal) {
		DefaultPieDataset dataset = new DefaultPieDataset();
		Utilities.log("combo index:" + comboIndex);
		String[] section = null;
		double[] data = null;
//		TODO deal with the combo box
		/* section string first */
		if (comboIndex == 0 || comboIndex == 2 || comboIndex == 4 || comboIndex == 6) {
			section = new String[categoryList.size()];
			for (int i = 0; i < section.length; i++) {
				section[i] = categoryList.get(i).getId();
			}
		} else {
			section = new String[accountList.size()];
			for (int i = 0; i < section.length; i++) {
				section[i] = accountList.get(i).getId();
			}
		}
		/* Then Data */
		
		/* All - Category */
		/* All - Account */
		if (comboIndex == 0 || comboIndex == 1) {
			data = new double[section.length];
			for (Entry e : this.data) {
				if (e.isValid() == false) {
					continue;
				}
				if (comboIndex == 0) {
					for (int i = 0; i < section.length; i++) {
						if (e.getCategoryId().equals(section[i])) {
							data[i] += e.getAmount();
							break;
						}
					}
				} else {
					for (int i = 0; i < section.length; i++) {
						if (e.getAccountId().equals(section[i])) {
							data[i] += e.getAmount();
							break;
						}
					}
				}
			}
		} else
		/* Monthly - Category */
		/* Monthly - Account */
		if (comboIndex == 2 || comboIndex == 3) {
			data = new double[section.length];
			for (Entry e : this.data) {
				Calendar cal2 = Calendar.getInstance();
				cal2.setTime(e.getDate());
				if (e.isValid() == false 
						|| cal.get(Calendar.MONTH) != cal2.get(Calendar.MONTH)
						|| cal.get(Calendar.YEAR) != cal2.get(Calendar.YEAR)) {
					continue;
				}
				if (comboIndex == 2) {
					for (int i = 0; i < section.length; i++) {
						if (e.getCategoryId().equals(section[i])) {
							data[i] += e.getAmount();
							break;
						}
					}
				} else {
					for (int i = 0; i < section.length; i++) {
						if (e.getAccountId().equals(section[i])) {
							data[i] += e.getAmount();
							break;
						}
					}
				}
			}
		} else
		/* Weekly*/
		if (comboIndex == 4 || comboIndex == 5) {
			data = new double[section.length];
			for (Entry e : this.data) {
				Calendar cal2 = Calendar.getInstance();
				cal2.setTime(e.getDate());
				if (e.isValid() == false 
						|| cal.get(Calendar.WEEK_OF_YEAR) != cal2.get(Calendar.WEEK_OF_YEAR)
						|| cal.get(Calendar.YEAR) != cal2.get(Calendar.YEAR)) {
					continue;
				}
				if (comboIndex == 4) {
					for (int i = 0; i < section.length; i++) {
						if (e.getCategoryId().equals(section[i])) {
							data[i] += e.getAmount();
							break;
						}
					}
				} else {
					for (int i = 0; i < section.length; i++) {
						if (e.getAccountId().equals(section[i])) {
							data[i] += e.getAmount();
							break;
						}
					}
				}
			}
		} else
		/* Daily */
		if (comboIndex == 6 || comboIndex == 7) {
			data = new double[section.length];
			for (Entry e : this.data) {
				Calendar cal2 = Calendar.getInstance();
				cal2.setTime(e.getDate());
				if (e.isValid() == false 
						|| cal.get(Calendar.DAY_OF_YEAR) != cal2.get(Calendar.DAY_OF_YEAR)
						|| cal.get(Calendar.YEAR) != cal2.get(Calendar.YEAR)) {
					continue;
				}
				if (comboIndex == 6) {
					for (int i = 0; i < section.length; i++) {
						if (e.getCategoryId().equals(section[i])) {
							data[i] += e.getAmount();
							break;
						}
					}
				} else {
					for (int i = 0; i < section.length; i++) {
						if (e.getAccountId().equals(section[i])) {
							data[i] += e.getAmount();
							break;
						}
					}
				}
			}
		}
		
		/* Generating dataset. */
		double totalData = 0;
		if (data != null) {
			for (int i = 0; i < data.length; i++) {
				dataset.setValue(section[i], data[i]);
				totalData += data[i];
			}
		}
		ThriftGUI.getPieChartOptionPanelSum().setText("$" + new DecimalFormat("0.00").format(totalData));
		return dataset;
	}
	
	private DefaultPieDataset generateRandomDataset() {
		String[] section = new String[] { "图书","食物","水果","交通","其它"}; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$
		double[] data = new double[section.length];   
		for (int i = 0; i < data.length; i++) {
			data[i] = 10 + Double.parseDouble(new DecimalFormat("0.0").format((Math.random() * 100))); //$NON-NLS-1$
		}

		 DefaultPieDataset dataset = new DefaultPieDataset();   
		 for (int i = 0; i < data.length; i++) {
		        dataset.setValue(section[i], data[i]);   
		 }
		 return dataset;   
	}
	
	private static JFreeChart createChart(DefaultPieDataset dataset) {
		JFreeChart chart = ChartFactory.createPieChart3D(
				null, dataset, true, false, false);
		return chart;   
	}   

	public  JPanel getPieChartPanel(int comboIndex, String dateStr) {
		Utilities.log("Generating pie chart..."); //$NON-NLS-1$
		Date date = new Date();
		try {
			date = new SimpleDateFormat("yyyy-MM-dd").parse(dateStr); //$NON-NLS-1$
		} catch (ParseException e) {
			e.printStackTrace();
			Utilities.log("Error parsing date from JCalendar."); //$NON-NLS-1$
		}
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		JFreeChart chart = createChart(getDataset(comboIndex, cal));
		chart.setBackgroundPaint(null);
		chart.getLegend().setPosition(RectangleEdge.RIGHT);
		
		PiePlot3D plot = (PiePlot3D) chart.getPlot();
		plot.setForegroundAlpha(0.5F);
		plot.setLabelGenerator(new StandardPieSectionLabelGenerator(
				"{0}\n${1}\n{2}", NumberFormat.getNumberInstance(), //$NON-NLS-1$
				new DecimalFormat("0.0%"))); //$NON-NLS-1$
		plot.setLabelLinkStyle(PieLabelLinkStyle.QUAD_CURVE);
		plot.setNoDataMessage(Messages.getString("PieChart.noData")); //$NON-NLS-1$
		plot.setBackgroundPaint(null);
		plot.setStartAngle(startAngle);
		
		panel = new ChartPanel(chart);
		return panel;
	}

	/*public static void main(final String[] args) {   
		JFrame demo = new JFrame();
		demo.setContentPane(new PieChart().getPieChartPanel());
		demo.pack();
		demo.setVisible(true);   
	}   
	*/
}
