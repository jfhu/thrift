/**
 * @author milki
 */
package com.doesntexist.milki;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.SystemColor;
import java.awt.event.ActionEvent;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.Date;
import java.util.StringTokenizer;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JToggleButton;
import javax.swing.SwingConstants;
import javax.swing.UIManager;

/**
 * <p>
 * Title: Swing����
 * </p>
 * <p>
 * Description: ��������
 * </p>.
 * 
 * @author duxu2004
 * @author milki
 */


public class JCalendar extends JPanel {
	// ��̬��ʾ������
	/** The year. */
	private int year = 0;
	
	/** The month. */
	private int month = 0;
	
	/** The day. */
	private int day = 0;
	// �����
	/** The Main. */
	private JPanel main = new JPanel();
	// �����
	/** The j panel day. */
	private JPanel jPanelDay = new JPanel();
	// �����
	/** The j panel month. */
	private JPanel jPanelMonth = new JPanel();
	// �������λ��
	/** The Year. */
	private JTextField jYear = new JTextField();
	// �µ�����λ��
	/** The Month. */
	private JTextField jMonth = new JTextField();
	// �����·�
	/** The Month down. */
	private JButton jMonthDown = new JButton();
	// �����·�
	/** The Month up. */
	private JButton jMonthUp = new JButton();

	/** The j panel button. */
	private JPanel jPanelButton = new JPanel();
	// �������
	/** The Year down. */
	private JButton jYearDown = new JButton();
	// �������
	/** The Year up. */
	private JButton jYearUp = new JButton();
	// ��ʾ���ڵ�λ��
	/** The Out. */
	private JLabel jOut = new JLabel();

	// �й�ʱ�����Ժ���Դ�������չ��������ʱ���Ĺ���
	/** The l. */
	private Locale l = Locale.CHINESE;
	// ������
	/** The cal. */
	private GregorianCalendar cal = new GregorianCalendar(l);
	// �������
	/** The week panel. */
	private JPanel weekPanel = new JPanel();
	// �찴ť��
	/** The days. */
	private JToggleButton[] days = new JToggleButton[42];
	// �����
	/** The Days. */
	private JPanel jDays = new JPanel();
	// ��ʾ
	/** The j label1. */
	private JLabel jLabel1 = new JLabel();
	
	/** The j label2. */
	private JLabel jLabel2 = new JLabel();
	
	/** The j label3. */
	private JLabel jLabel3 = new JLabel();
	
	/** The j label4. */
	private JLabel jLabel4 = new JLabel();
	
	/** The j label5. */
	private JLabel jLabel5 = new JLabel();
	
	/** The j label6. */
	private JLabel jLabel6 = new JLabel();
	
	/** The j label7. */
	private JLabel jLabel7 = new JLabel();
	// ��ǰѡ���������ť
	/** The cur. */
	//private JToggleButton cur = null;
	// �·��������飬����ȡ�õ����ж�����
	// 1 2 3 4 5 6 7 8 9 10 11 12
	/** The days for each month. */
	private int[] mm = { 31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31 };

	private ThriftGUI gui;

	// �����ڹ��캯��
	/**
	 * Instantiates a new j calendar.
	 */
	public JCalendar(ThriftGUI gui) {
		this.gui = gui;
		try {
			jbInit();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Instantiates a new j calendar.
	 * ���������õĹ��캯��
	 * 
	 * @param year the year
	 * @param month the month
	 * @param day the day
	 */
	public JCalendar(final int year, final int month, final int day) {
		cal.set(year, month, day);
		try {
			jbInit();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Instantiates a new j calendar.
	 * ����������Ĺ��캯��
	 * 
	 * @param calendar the calendar
	 */
	public JCalendar(final GregorianCalendar calendar) {
		cal = calendar;
		try {
			jbInit();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Instantiates a new j calendar.
	 * ����������Ĺ��캯��
	 * 
	 * @param date the date
	 */
	public JCalendar(final Date date) {
		cal.setTime(date);
		try {
			jbInit();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * ��ʼ�����.
	 * 
	 * @throws Exception the exception
	 */
	private void jbInit() throws Exception {
		// ��ʼ���ꡢ�¡���
		iniCalender();

		this.setLayout(new BorderLayout());
		this.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
		this.setMaximumSize(new Dimension(200, 200));
		this.setMinimumSize(new Dimension(200, 200));
		this.setPreferredSize(new Dimension(200, 200));

		main.setLayout(new BorderLayout());
		main.setBackground(SystemColor.info);
		main.setBorder(null);

		jOut.setBackground(Color.lightGray);
		jOut.setHorizontalAlignment(SwingConstants.CENTER);
		jOut.setMaximumSize(new Dimension(100, 19));
		jOut.setMinimumSize(new Dimension(100, 19));
		jOut.setPreferredSize(new Dimension(100, 19));

		jLabel1.setForeground(Color.red);
		jLabel1.setHorizontalAlignment(SwingConstants.CENTER);
		jLabel1.setHorizontalTextPosition(SwingConstants.CENTER);
		jLabel1.setText(Messages.getString("JCalendar.SUN")); //$NON-NLS-1$
		jLabel2.setForeground(Color.blue);
		jLabel2.setHorizontalAlignment(SwingConstants.CENTER);
		jLabel2.setHorizontalTextPosition(SwingConstants.CENTER);
		jLabel2.setText(Messages.getString("JCalendar.SAT")); //$NON-NLS-1$
		jLabel3.setHorizontalAlignment(SwingConstants.CENTER);
		jLabel3.setHorizontalTextPosition(SwingConstants.CENTER);
		jLabel3.setText(Messages.getString("JCalendar.FRI")); //$NON-NLS-1$
		jLabel4.setHorizontalAlignment(SwingConstants.CENTER);
		jLabel4.setHorizontalTextPosition(SwingConstants.CENTER);
		jLabel4.setText(Messages.getString("JCalendar.THU")); //$NON-NLS-1$
		jLabel5.setHorizontalAlignment(SwingConstants.CENTER);
		jLabel5.setHorizontalTextPosition(SwingConstants.CENTER);
		jLabel5.setText(Messages.getString("JCalendar.WED")); //$NON-NLS-1$
		jLabel6.setBorder(null);
		jLabel6.setHorizontalAlignment(SwingConstants.CENTER);
		jLabel6.setHorizontalTextPosition(SwingConstants.CENTER);
		jLabel6.setText(Messages.getString("JCalendar.TUE")); //$NON-NLS-1$
		jLabel7.setBackground(Color.lightGray);
		jLabel7.setForeground(Color.black);
		jLabel7.setBorder(null);
		jLabel7.setHorizontalAlignment(SwingConstants.CENTER);
		jLabel7.setHorizontalTextPosition(SwingConstants.CENTER);
		jLabel7.setText(Messages.getString("JCalendar.MON")); //$NON-NLS-1$

		weekPanel.setBackground(UIManager
				.getColor("InternalFrame.activeTitleGradient")); //$NON-NLS-1$
		weekPanel.setBorder(BorderFactory.createEtchedBorder());
		weekPanel.setLayout(new GridLayout(1, 7));
		weekPanel.add(jLabel1, null);
		weekPanel.add(jLabel7, null);
		weekPanel.add(jLabel6, null);
		weekPanel.add(jLabel5, null);
		weekPanel.add(jLabel4, null);
		weekPanel.add(jLabel3, null);
		weekPanel.add(jLabel2, null);

		jMonthUp.setAlignmentX((float) 0.0);
		jMonthUp.setActionMap(null);

		jPanelMonth.setBackground(SystemColor.info);
		jPanelMonth.setLayout(new BorderLayout());
		jPanelMonth.setBorder(BorderFactory.createEtchedBorder());

		jMonth.setBorder(null);
		jMonth.setHorizontalAlignment(SwingConstants.CENTER);
		jMonth.addMouseListener(new java.awt.event.MouseAdapter() {
			public void mouseClicked(final MouseEvent e) {
				monthMouseClicked(e);
			}
		});
		jMonth.addKeyListener(new java.awt.event.KeyAdapter() {
			public void keyPressed(final KeyEvent e) {
				monthKeyPressed(e);
			}
		});

		jMonthDown.setBorder(null);
		jMonthDown.setText("<-"); //$NON-NLS-1$
		jMonthDown.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(final ActionEvent e) {
				monthDownActionPerformed(e);
			}
		});
		jMonthUp.setBorder(null);
		jMonthUp.setText("->"); //$NON-NLS-1$
		jMonthUp.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(final ActionEvent e) {
				monthUpActionPerformed(e);
			}
		});

		jPanelButton.setLayout(null);
		jPanelButton.setBorder(null);
		jPanelButton
				.addComponentListener(new java.awt.event.ComponentAdapter() {
					public void componentResized(
							final java.awt.event.ComponentEvent evt) {
						jPanelButtonComponentResized(evt);
					}
				});

		jYear.setBorder(BorderFactory.createEtchedBorder());
		jYear.setMaximumSize(new Dimension(80, 25));
		jYear.setMinimumSize(new Dimension(80, 25));
		jYear.setPreferredSize(new Dimension(80, 25));
		jYear.setHorizontalAlignment(SwingConstants.CENTER);
		jYear.addMouseListener(new java.awt.event.MouseAdapter() {
			public void mouseClicked(final MouseEvent e) {
				yearMouseClicked(e);
			}
		});
		jYear.addKeyListener(new java.awt.event.KeyAdapter() {
			public void keyPressed(final KeyEvent e) {
				yearKeyPressed(e);
			}
		});

		jYearDown.setBorder(null);
		jYearDown.setMaximumSize(new Dimension(16, 16));
		jYearDown.setMinimumSize(new Dimension(16, 16));
		jYearDown.setPreferredSize(new Dimension(16, 16));
		jYearDown.setSize(new Dimension(16, 16));
		jYearDown.setText("-"); //$NON-NLS-1$
		jYearDown.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(final ActionEvent e) {
				yearDownActionPerformed(e);
			}
		});
		jYearUp.setBorder(null);
		jYearUp.setMaximumSize(new Dimension(16, 16));
		jYearUp.setMinimumSize(new Dimension(16, 16));
		jYearUp.setPreferredSize(new Dimension(16, 16));
		jYearUp.setSize(new Dimension(16, 16));
		jYearUp.setText("+"); //$NON-NLS-1$
		jYearUp.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(final ActionEvent e) {
				yearUpActionPerformed(e);
			}
		});

		jPanelDay.setLayout(new BorderLayout());

		jDays.setLayout(new GridLayout(6, 7));
		jDays.setBackground(SystemColor.info);

		for (int i = 0; i < 42; i++) {
			days[i] = new JToggleButton();
			days[i].setBorder(null);
			days[i].setBackground(SystemColor.info);
			days[i].setHorizontalAlignment(SwingConstants.CENTER);
			days[i].setHorizontalTextPosition(SwingConstants.CENTER);
			// days[i].setSize(l,l);
			days[i].addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(final ActionEvent e) {
					day = Integer.parseInt(((JToggleButton) e.getSource())
							.getText());
//					showDate();
//					showDays();
					showMe();
				}
			});
			jDays.add(days[i]);
		}

		this.add(main, BorderLayout.NORTH);
		this.add(jPanelDay, BorderLayout.CENTER);
		this.add(jPanelMonth, BorderLayout.SOUTH);

		main.add(jYear, BorderLayout.CENTER);
		main.add(jOut, BorderLayout.WEST);
		main.add(jPanelButton, BorderLayout.EAST);

		jPanelButton.add(jYearUp);
		jPanelButton.add(jYearDown);

		jPanelDay.add(weekPanel, BorderLayout.NORTH);
		jPanelDay.add(jDays, BorderLayout.CENTER);

		jPanelMonth.add(jMonth, BorderLayout.CENTER);
		jPanelMonth.add(jMonthDown, BorderLayout.WEST);
		jPanelMonth.add(jMonthUp, BorderLayout.EAST);

		showMe();
	}

	/**
	 * J panel button component resized.
	 * �Զ����ػ���ѡ�����
	 * 
	 * @param evt the evt
	 */
	void jPanelButtonComponentResized(final java.awt.event.ComponentEvent evt) {
		jYearUp.setLocation(0, 0);
		jYearDown.setLocation(0, jYearUp.getHeight());
		jPanelButton.setSize(jYearUp.getWidth(), jYearUp.getHeight() * 2);
		jPanelButton.setPreferredSize(new Dimension(jYearUp.getWidth(), jYearUp
				.getHeight() * 2));
		jPanelButton.updateUI();
	}

	// �������
	/**
	 * Year up_action performed.
	 * 
	 * @param e the e
	 */
	void yearUpActionPerformed(final ActionEvent e) {
		year++;
		showMe();
	}

	// �������
	/**
	 * Year down_action performed.
	 * 
	 * @param e the e
	 */
	void yearDownActionPerformed(final ActionEvent e) {
		year--;
		showMe();
	}

	// �����·�
	/**
	 * Month down_action performed.
	 * 
	 * @param e the e
	 */
	void monthDownActionPerformed(final ActionEvent e) {
		month--;
		if (month < 0) {
			month = 11;
			year--;
			showYear();
		}
		showMe();
	}

	// �����·�
	/**
	 * Month up_action performed.
	 * 
	 * @param e the e
	 */
	void monthUpActionPerformed(final ActionEvent e) {
		month++;
		if (month == 12) {
			month = 0;
			year++;
			showYear();
		}
		showMe();
	}

	// ��ʼ��������
	/**
	 * Ini calender.
	 */
	void iniCalender() {
		year = cal.get(Calendar.YEAR);
		month = cal.get(Calendar.MONTH);
		day = cal.get(Calendar.DAY_OF_MONTH);
	}

	// ˢ���·�
	/**
	 * Show month.
	 */
	void showMonth() {
		jMonth.setText(Integer.toString(month + 1) + Messages.getString("JCalendar.Month")); //$NON-NLS-1$
	}

	// ˢ�����
	/**
	 * Show year.
	 */
	void showYear() {
		jYear.setText(Integer.toString(year) + Messages.getString("JCalendar.Year")); //$NON-NLS-1$
	}

	// ˢ������
	/**
	 * Show date.
	 */
	void showDate() {
		DecimalFormat df = new DecimalFormat("00"); //$NON-NLS-1$
		jOut.setText(Integer.toString(year) + "-" + df.format(month + 1) //$NON-NLS-1$
				+ "-" + df.format(day)); //$NON-NLS-1$
	}

	// �ػ�����ѡ�����
	/**
	 * Show days.
	 */
	void showDays() {
		cal.set(year, month, 1);
		int firstDayOfWeek = cal.get(Calendar.DAY_OF_WEEK);
		int n = mm[month];
		if (cal.isLeapYear(year) && month == 1) {
			n++;
		}
		if (day > n) {
			//day�����ֵΪn
			day = n;
		}
		int i = 0;
		for (; i < firstDayOfWeek - 1; i++) {
			days[i].setEnabled(false);
			days[i].setSelected(false);
			days[i].setText(""); //$NON-NLS-1$
		}
		int d = 1;
		for (; d <= n; d++) {
			days[i].setText(Integer.toString(d));
			days[i].setEnabled(true);
			if (d == day) {
				days[i].setSelected(true);
			} else {
				days[i].setSelected(false);
			}
			i++;
		}
		for (; i < 42; i++) {
			days[i].setEnabled(false);
			days[i].setSelected(false);
			days[i].setText(""); //$NON-NLS-1$
		}
	}

	// ����������ѡ����������ַ���
	/**
	 * Selection year.
	 */
	void selectionYear() {
		jYear.setSelectionStart(0);
		jYear.setSelectionEnd(jYear.getText().length());
	}

	// �����·����ѡ�������·��ַ���
	/**
	 * Selection month.
	 */
	void selectionMonth() {
		jMonth.setSelectionStart(0);
		jMonth.setSelectionEnd(jMonth.getText().length());
	}

	// �·������Ӧ��굥���¼�
	/**
	 * Month_mouse clicked.
	 * 
	 * @param e the e
	 */
	void monthMouseClicked(final MouseEvent e) {
		// SelectionMonth();
		inputMonth();
	}

	// ����������·�
	/**
	 * Input month.
	 */
	void inputMonth() {
		String s;
		if (jMonth.getText().endsWith(Messages.getString("JCalendar.Month"))) { //$NON-NLS-1$
			s = jMonth.getText().substring(0, jMonth.getText().lastIndexOf(Messages.getString("JCalendar.Month")));  //$NON-NLS-1$
		} else {
			s = jMonth.getText();
		}
		month = Integer.parseInt(s) - 1;
		/* Month should be valid */
		if (month > 11) {
			month = 11;
		}
		if (month < 0) {
			month = 0;
		}
		this.showMe();
	}

	// �·��������û��¼���Ӧ
	/**
	 * Month_key pressed.
	 * 
	 * @param e the e
	 */
	void monthKeyPressed(final KeyEvent e) {
		if (e.getKeyChar() == 10) {
			inputMonth();
		}
	}

	// ��������Ӧ��굥���¼�
	/**
	 * Year_mouse clicked.
	 * 
	 * @param e the e
	 */
	void yearMouseClicked(final MouseEvent e) {
		// SelectionYear();
		inputYear();
	}

	// ��ݼ����û��¼���Ӧ
	/**
	 * Year_key pressed.
	 * 
	 * @param e the e
	 */
	void yearKeyPressed(KeyEvent e) {
		// System.out.print(new Integer(e.getKeyChar()).byteValue());
		if (e.getKeyChar() == 10) {
			inputYear();
		}
	}

	// �������������ַ���
	/**
	 * Input year.
	 */
	void inputYear() {
		String s;
		if (jYear.getText().endsWith(Messages.getString("JCalendar.Year"))) { //$NON-NLS-1$
			s = jYear.getText().substring(0, jYear.getText().lastIndexOf(Messages.getString("JCalendar.Year"))); //$NON-NLS-1$
		} else {
			s = jYear.getText();
		}
		try {
			year = Integer.parseInt(s);
		} catch (Exception e) {
			Utilities.log("Invalid year string."); //$NON-NLS-1$
		}
		this.showMe();
	}

	/**
	 * Gets the date, yyyy-mm-dd.
	 * 
	 * @return the date
	 */
	public String getDate() {
		return jOut.getText();
	}

	/**
	 * Sets the date, yyyy-mm-dd.
	 * 
	 * @param date the new date
	 */
	public void setDate(final String date) {
		if (date != null) {
			StringTokenizer f = new StringTokenizer(date, "-"); //$NON-NLS-1$
			if (f.hasMoreTokens()) {
				year = Integer.parseInt(f.nextToken());
			}
			if (f.hasMoreTokens()) {
				month = Integer.parseInt(f.nextToken());
			}
			if (f.hasMoreTokens()) {
				day = Integer.parseInt(f.nextToken());
			}
			cal.set(year, month, day);
		}
		this.showMe();
	}

	// �����ڶ�����ʽ��������
	/**
	 * Sets the time.
	 * 
	 * @param date the new time
	 */
	public void setTime(final Date date) {
		cal.setTime(date);
		this.iniCalender();
		this.showMe();
	}

	// �������ڶ���
	/**
	 * Gets the time.
	 * 
	 * @return the time
	 */
	public Date getTime() {
		return cal.getTime();
	}

	// ���ص�ǰ����
	/**
	 * Gets the day.
	 * 
	 * @return the day
	 */
	public int getDay() {
		return day;
	}

	// ���õ�ǰ����
	/**
	 * Sets the day.
	 * 
	 * @param day the new day
	 */
	public void setDay(int day) {
		this.day = day;
		cal.set(this.year, this.month, this.day);
		this.showMe();
	}

	// ���õ�ǰ����
	/**
	 * Sets the year.
	 * 
	 * @param year the new year
	 */
	public void setYear(int year) {
		this.year = year;
		cal.set(this.year, this.month, this.day);
		this.showMe();
	}

	// ���ص�ǰ����
	/**
	 * Gets the year.
	 * 
	 * @return the year
	 */
	public int getYear() {
		return year;
	}

	// ���ص�ǰ����
	/**
	 * Gets the month.
	 * 
	 * @return the month
	 */
	public int getMonth() {
		return month;
	}
	
	public String getDisplay() {
		return jOut.getText();
	}

	// ���õ�ǰ����
	/**
	 * Sets the month.
	 * 
	 * @param month the new month
	 */
	public void setMonth(int month) {
		this.month = month;
		cal.set(this.year, this.month, this.day);
		this.showMe();
	}

	// ˢ��
	/**
	 * Show me.
	 */
	public void showMe() {
		this.showDays();
		this.showMonth();
		this.showYear();
		this.showDate();
		gui.updateTheChart();
	}
	
	public void setThriftGUI(ThriftGUI gui) {
		this.gui = gui;
	}
	
	// ������
	/**
	 * The main method.
	 * 
	 * @param args the arguments
	 */
	/*public static void main(final String[] args) {
		JFrame f = new JFrame();
		f.setContentPane(new JCalendar());
		f.pack();
		f.setVisible(true);
	}*/
}
