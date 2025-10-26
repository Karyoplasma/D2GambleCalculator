package gui;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.text.DecimalFormat;
import java.util.List;
import javax.swing.ButtonGroup;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.SwingUtilities;
import org.knowm.xchart.XChartPanel;
import org.knowm.xchart.XYChart;
import org.knowm.xchart.XYChartBuilder;
import core.DataBase;
import core.GambleCalc;
import core.Item;
import net.miginfocom.swing.MigLayout;
import javax.swing.JButton;

public class D2GambleUI extends JFrame {

	private static final long serialVersionUID = -6398321295889228857L;
	private static final Color BACKGROUND_COLOR = new Color(240, 240, 240);
	private static final DecimalFormat DECIMALS = new DecimalFormat("#.#####");
	private final JRadioButton rbUnique = new JRadioButton("Unique");
	private final JRadioButton rbSet = new JRadioButton("Set");
	private final JComboBox<Item> cbUnique = new JComboBox<>();
	private final JComboBox<Item> cbSet = new JComboBox<>();
	private final JPanel cardsPanel = new JPanel(new CardLayout());
	private final JLabel lblOptimal = new JLabel("Optimal level: -");
	private final JPanel chartContainer = new JPanel(new BorderLayout());
	private final JButton btnCalculate = new JButton("Calculate");
	private final GambleCalc calc = new GambleCalc();

	public D2GambleUI(List<Item> uniqueItems, List<Item> setItems) {
		super("D2 GambleUI");
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setSize(1000, 800);
		getContentPane().setLayout(new MigLayout("fill, insets 10", "[grow]", "[][grow][]"));

		ButtonGroup group = new ButtonGroup();
		group.add(rbUnique);
		group.add(rbSet);
		rbUnique.setSelected(true);

		uniqueItems.forEach(cbUnique::addItem);
		setItems.forEach(cbSet::addItem);
		cardsPanel.add(cbUnique, "UNIQUE");
		cardsPanel.add(cbSet, "SET");
		rbUnique.addActionListener(e -> switchCard("UNIQUE"));
		rbSet.addActionListener(e -> switchCard("SET"));

		JPanel controlPanel = new JPanel(new MigLayout("", "[]5[]5[grow,fill]5[]", "[]"));
		controlPanel.add(rbUnique, "cell 0 0");
		controlPanel.add(rbSet, "cell 1 0");
		getContentPane().add(controlPanel, "grow,wrap");
		controlPanel.add(cardsPanel, "cell 2 0,growy");
		controlPanel.add(btnCalculate, "cell 3 0");
		btnCalculate.addActionListener(e -> updateChart());

		getContentPane().add(chartContainer, "grow, wrap");

		getContentPane().add(lblOptimal, "cell 0 3 1 3");
		updateChart();
	}

	private void switchCard(String cardName) {
		CardLayout cl = (CardLayout) (cardsPanel.getLayout());
		cl.show(cardsPanel, cardName);
	}

	private void updateChart() {
		JComboBox<Item> activeBox = (rbUnique.isSelected()) ? cbUnique : cbSet;
		Item selected = (Item) activeBox.getSelectedItem();
		if (selected == null)
			return;

		XChartPanel<XYChart> chartPanel = createChartPanel(selected);
		chartContainer.removeAll();
		chartContainer.add(chartPanel, java.awt.BorderLayout.CENTER);
		chartContainer.revalidate();
		chartContainer.repaint();
	}

	private XChartPanel<XYChart> createChartPanel(Item item) {
		double[][] summary = calc.packChancesAndOptimalLevel(item);
		XYChart chart = new XYChartBuilder().width(1000).height(800).title(item.toString() + " Gamble Chance").build();

		chart.addSeries("Chance", summary[0], summary[1]);
		chart.getStyler().setLegendVisible(false);
		chart.getStyler().setYAxisTicksVisible(false);
		chart.getStyler().setCursorEnabled(true);
		chart.getStyler().setCustomCursorXDataFormattingFunction(x -> String.format("Level %.0f", x));
		chart.getStyler().setCustomCursorYDataFormattingFunction(y -> getCursorFormatString(y));
		chart.getStyler().setChartBackgroundColor(BACKGROUND_COLOR);

		lblOptimal.setText(this.getOptimalLabelText(item, summary[2]));

		return new XChartPanel<XYChart>(chart);
	}

	private String getOptimalLabelText(Item item, double[] summary) {
		double bestChance = summary[2];
		double reverse = 1.0 / bestChance;
		if (summary[0] == summary[1]) {
			return String.format("Optimal level to gamble %s is clvl%.0f with a chance of %s%% (1 in %d)", item.toString(),
					summary[0], DECIMALS.format(bestChance * 100), Math.round(reverse));
		}
		return String.format("Optimal level range to gamble %s is clvls%.0f-%.0f with a chance of %s%% (1 in %d)", item.toString(),
				summary[0], summary[1], DECIMALS.format(bestChance * 100), Math.round(reverse));
	}

	private String getCursorFormatString(Double y) {
		if (y <= 0.0) {
			return "Impossible";
		}
		double reverse = 1.0 / y;
		return DECIMALS.format(y * 100) + "% (1 in " + Math.round(reverse) + ")";
	}

	public static void main(String[] args) {
		List<Item> uniqueItems = DataBase.getInstance().getAllUniqueItems();
		List<Item> setItems = DataBase.getInstance().getAllSetItems();

		SwingUtilities.invokeLater(() -> {
			D2GambleUI gui = new D2GambleUI(uniqueItems, setItems);
			gui.setVisible(true);
		});
	}
}