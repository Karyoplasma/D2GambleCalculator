package gui;

import java.awt.CardLayout;
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
import enums.Rarity;
import net.miginfocom.swing.MigLayout;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.JButton;

public class D2GambleUI extends JFrame {

	private static final long serialVersionUID = -6398321295889228857L;
	private final JRadioButton rbUnique = new JRadioButton("Unique");
	private final JRadioButton rbSet = new JRadioButton("Set");
	private final JComboBox<Item> cbUnique = new JComboBox<>();
	private final JComboBox<Item> cbSet = new JComboBox<>();
	private final JPanel cardsPanel = new JPanel(new CardLayout());
	private final JLabel lblOptimal = new JLabel("Optimal level: -");
	private final JPanel chartContainer = new JPanel(new java.awt.BorderLayout());
	private final JButton btnCalculate = new JButton("Calculate");
	private final GambleCalc calc = new GambleCalc();

	public D2GambleUI(List<Item> uniqueItems, List<Item> setItems) {
		super("D2 GambleUI");
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setSize(700, 500);
		getContentPane().setLayout(new MigLayout("fill, insets 10", "[grow]", "[][grow][]")); // 3 rows: controls,
																								// chart, info

		ButtonGroup group = new ButtonGroup();
		group.add(rbUnique);
		group.add(rbSet);
		rbUnique.setSelected(true);

		// cards
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
		add(chartPanel, "flowx,cell 0 1 1 3");
		chartContainer.revalidate();
		chartContainer.repaint();
	}

	private XChartPanel<XYChart> createChartPanel(Item item) {
		double[] xData = new double[99];
		double[] yData = new double[99];
		int optimalLevel = 1;
		double bestChance = -1;

		for (int lvl = 1; lvl <= 99; lvl++) {
			double chance = calc.calculateChanceToGamble(item.getName(), lvl, item.getRarity());

			if (chance > bestChance) {
				bestChance = chance;
				optimalLevel = lvl;
			}

			xData[lvl - 1] = lvl;
			yData[lvl - 1] = chance;
		}

		lblOptimal.setText("Optimal gamble level: " + optimalLevel);

		XYChart chart = new XYChartBuilder().width(1000).height(800).title(item.toString() + " Gamble Chance")
				.xAxisTitle("Char Level")
				// .yAxisTitle("Chance")
				.build();
		chart.addSeries(item.toString(), xData, yData);
		chart.getStyler().setToolTipsAlwaysVisible(true);
		chart.getStyler().setToolTipFont(new java.awt.Font("Dialog", java.awt.Font.PLAIN, 12));
		return new XChartPanel<XYChart>(chart);
	}

	@Deprecated
	private double calculateChanceToGamble(String name, int charLvl, Rarity rarity) {
		return Math.random() * 0.25; // just for testing
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