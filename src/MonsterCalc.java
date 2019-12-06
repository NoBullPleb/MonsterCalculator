

import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;

import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListModel;
import javax.swing.ScrollPaneConstants;


@SuppressWarnings("serial")
public class MonsterCalc extends JFrame {
	private final static int numStats = 7;
	private static JPanel contentPane;
	@SuppressWarnings("unchecked")
	private static JList<Double>[] CRs = new JList[2];
	private static Double[] c = { 0d, 1 / 8d, 1 / 4d, 1 / 2d, 1d, 2d, 3d, 4d, 5d, 6d, 7d, 8d, 9d, 10d, 11d, 12d, 13d,
			14d, 15d, 16d, 17d, 18d, 19d, 20d, 21d, 22d, 23d, 24d, 25d, 26d, 27d, 28d, 29d, 30d };

	private static String[] labels = { "CR", "PB", "AC", "HP", "AB", "DR", "DC" };
	private static String[] tooltips = { "Challenge Rating", "Proficiency Bonus", "Armor Class", "Hit Points", "Attack Bonus", "Damage per Round", "Save DC" };
	
	
	JScrollPane[] sp = new JScrollPane[2];
	private static JTextField[] statsB = new JTextField[numStats];
	private static JTextField[] statsA = new JTextField[numStats];
	private static List<String> crChart = ResourceLoader.getTable("cr chart.txt");
	private static Double[][] chart = new Double[34][numStats];

	public static void main(String[] args) {
		MonsterCalc mc = new MonsterCalc();
		mc.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mc.setVisible(true);

	}

	static {
		for (int x = 0; x < 34; x++) {
			String[] stringStat = crChart.get(x).split(",");
			for (int i = 0; i < numStats; i++) {
				chart[x][i] = Double.parseDouble(stringStat[i]);
			}
		}
	}

	private void updateFields() {
		int in1 = CRs[0].getSelectedIndex(), in2 = CRs[1].getSelectedIndex();
		for (int i = 1; i < numStats; i++) {
			try {
				Integer stat = Integer.parseInt(statsB[i].getText());
				Double maxCurrent = chart[in1][i], maxFuture = chart[in2][i];
				stat = (int) Math.round((stat / maxCurrent) * maxFuture);
				statsA[i].setText(stat.toString());
			} catch (Exception e) {
			}
		}
	}

	public MonsterCalc() {
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 200, 260);
		contentPane = new JPanel();
		contentPane.setLayout(null);
		for (int i = 0; i < 2; i++) {
			CRs[i] = new JList<>();
			CRs[i].setListData(c);
			CRs[i].setSelectedIndex(0);
			CRs[i].setVisibleRowCount(1);
			CRs[i].setAutoscrolls(true);
			sp[i] = new JScrollPane(CRs[i]);
			sp[i].setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
			sp[i].setBounds(40 + (80 * i), 5, 60, 20);
			contentPane.add(sp[i]);
			CRs[i].addListSelectionListener((d) -> updateFields());
		}

		for (int i = 0; i < numStats; i++) {
			JLabel crLbL = new JLabel(labels[i]);
			crLbL.setToolTipText(tooltips[i]);
			crLbL.setBounds(10, 5 + (25 * i), 30, 20);
			contentPane.add(crLbL);
			if (i > 0) {
				statsB[i] = new JTextField();
				statsB[i].setBounds(40, 5 + (25 * i), 63, 20);
				statsB[i].addActionListener(e -> updateFields());
				contentPane.add(statsB[i]);
				statsA[i] = new JTextField();
				statsA[i].setEditable(false);
				statsA[i].setBounds(115, 5 + (25 * i), 63, 20);
				statsA[i].addActionListener(e -> updateFields());
				contentPane.add(statsA[i]);
			}
		}
		JButton guess = new JButton("Guess the CR");
		guess.setBounds(10, 185, 170, 15);
		guess.addActionListener(e -> guessCR());
		contentPane.add(guess);
		this.setTitle("Monster Calculator");
		setContentPane(contentPane);
	}

	private void guessCR() {
		Double cr = 0d;
		int devBy = numStats - 1;
		System.out.println();
		for (int i = 1; i < numStats; i++) {
			String t = statsB[i].getText();
			if (t==null || t.isBlank()) {
				devBy--;
				continue;
			}
			Integer stat = Integer.parseInt(t);
			System.out.print(labels[i] + "=" + stat);
			int x = 0;
			// find the minCR
			Double previousMax = 0d;
			boolean foundMax = false;
			Double thiscr = 0d;
			for (; x < chart.length; x++) {
				Double maxVale = chart[x][i];
				if (maxVale >= stat && (!foundMax || maxVale.equals(previousMax))) {
					thiscr = chart[x][0];
					previousMax = maxVale;
					foundMax = true;
				}
			}
			if (!foundMax)
				thiscr = 30.0;
			System.out.println(" CR: " + thiscr);
			cr += thiscr;
		}
		cr /= devBy;
		System.out.println("REAL CR: " + cr);
		ListModel<Double> list = CRs[0].getModel();
		for (int i = 0; i < list.getSize(); i++) {
			if ((cr < 1 && list.getElementAt(i) >= cr) || (Math.round(cr) == list.getElementAt(i))) {
				CRs[0].setSelectedIndex(i);
				int height = CRs[0].getPreferredScrollableViewportSize().height * i;
				sp[0].getVerticalScrollBar().setValue(height);
				CRs[0].repaint();
				break;
			}
		}
	}
}
