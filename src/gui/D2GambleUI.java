package gui;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.ButtonGroup;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.SwingUtilities;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.DocumentEvent.EventType;
import org.knowm.xchart.XChartPanel;
import org.knowm.xchart.XYChart;
import org.knowm.xchart.XYChartBuilder;
import core.DataBase;
import core.GambleCalc;
import core.Item;
import model.ItemComboBoxModel;
import net.miginfocom.swing.MigLayout;
import javax.swing.JButton;
import javax.swing.JTextField;
import javax.swing.KeyStroke;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import javax.swing.SwingConstants;

public class D2GambleUI extends JFrame {

	private static final long serialVersionUID = -6398321295889228857L;
	private static final Color BACKGROUND_COLOR = new Color(240, 240, 240);
	private static final DecimalFormat DECIMALS = new DecimalFormat("#.#####");
	private final JRadioButton rbUnique = new JRadioButton("Unique");
	private final JRadioButton rbSet = new JRadioButton("Set");
	private final JComboBox<Item> cbUnique = new JComboBox<Item>();
	private final JComboBox<Item> cbSet = new JComboBox<Item>();
	private final ItemComboBoxModel uniqueModel = new ItemComboBoxModel(DataBase.getInstance().getAllUniqueItems());
	private final ItemComboBoxModel setModel = new ItemComboBoxModel(DataBase.getInstance().getAllSetItems());
	private final JPanel cardsPanel = new JPanel(new CardLayout());
	private final JLabel lblOptimal = new JLabel("Optimal level: -");
	private final JPanel chartContainer = new JPanel(new BorderLayout());
	private final JButton btnCalculate = new JButton("Calculate");
	private final GambleCalc calc = new GambleCalc();
	private final JTextField searchField = new JTextField();
	private boolean isSwitching = false;

	public D2GambleUI() {
		super("D2 GambleUI");
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setSize(1000, 800);
		getContentPane().setLayout(new MigLayout("fill, insets 10", "[grow]", "[][grow][]"));

		ButtonGroup group = new ButtonGroup();
		group.add(rbUnique);
		group.add(rbSet);
		rbUnique.setSelected(true);

		cbUnique.setMaximumRowCount(20);
		cbSet.setMaximumRowCount(20);
		cbUnique.setModel(uniqueModel);
		cbSet.setModel(setModel);
		cbUnique.setSelectedIndex(0);
		cardsPanel.add(cbUnique, "UNIQUE");
		cardsPanel.add(cbSet, "SET");
		rbUnique.addActionListener(e -> switchCard("UNIQUE"));
		rbSet.addActionListener(e -> switchCard("SET"));
		searchField.setFont(new Font("Tahoma", Font.PLAIN, 14));
		searchField.setColumns(10);
		this.setupSearchBox();
		JPanel controlPanel = new JPanel(new MigLayout("", "[]5[]5[250px,grow,fill]5[grow,fill]5[]", "[]"));
		controlPanel.add(rbUnique, "cell 0 0");
		controlPanel.add(rbSet, "cell 1 0");
		getContentPane().add(controlPanel, "grow,wrap");
		controlPanel.add(cardsPanel, "cell 2 0,growy");
		controlPanel.add(searchField, "cell 3 0,grow");
		controlPanel.add(btnCalculate, "cell 4 0");
		btnCalculate.addActionListener(e -> updateChart());
		lblOptimal.setFont(new Font("Tahoma", Font.BOLD, 14));

		getContentPane().add(chartContainer, "grow, wrap");
		getContentPane().add(lblOptimal, "cell 0 3 1 3,alignx center");
		updateChart();
	}

	private void setupSearchBox() {
		searchField.getDocument().addDocumentListener(new DocumentListener() {
			private void filter(DocumentEvent e) {
				if (isSwitching) {
					return;
				}
				final String text = searchField.getText().toLowerCase();
				SwingUtilities.invokeLater(() -> {
					JComboBox<Item> box = rbUnique.isSelected() ? cbUnique : cbSet;
					ItemComboBoxModel model = (ItemComboBoxModel) box.getModel();
					List<Item> allItems = null;
					if (e.getType() == EventType.INSERT && e.getOffset() == text.length() - 1) {
						allItems = model.getView();
					} else {
						allItems = model.getOriginal();
					}
					List<Item> newView = new ArrayList<Item>();
					for (Item item : allItems) {
						if (item.toString().toLowerCase().contains(text)) {
							newView.add(item);
						}
					}

					box.setMaximumRowCount(Math.min(20, newView.size()));
					model.setView(newView);
					if (!newView.isEmpty()) {
						box.showPopup();
					} else {
						box.hidePopup();
					}
				});
			}

			@Override
			public void insertUpdate(DocumentEvent e) {
				filter(e);
			}

			@Override
			public void removeUpdate(DocumentEvent e) {
				filter(e);
			}

			@Override
			public void changedUpdate(DocumentEvent e) {
				filter(e);
			}
		});
		searchField.getInputMap(JComponent.WHEN_FOCUSED).put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0),
				"enterPressed");
		searchField.getInputMap(JComponent.WHEN_FOCUSED).put(KeyStroke.getKeyStroke(KeyEvent.VK_UP, 0), "upPressed");
		searchField.getInputMap(JComponent.WHEN_FOCUSED).put(KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, 0),
				"downPressed");

		searchField.getActionMap().put("enterPressed", new AbstractAction() {
			private static final long serialVersionUID = -5314483504870624892L;

			@Override
			public void actionPerformed(ActionEvent e) {
				btnCalculate.doClick();
			}
		});
		searchField.getActionMap().put("upPressed", new AbstractAction() {

			private static final long serialVersionUID = -8997726027369310222L;

			@Override
			public void actionPerformed(ActionEvent e) {
				JComboBox<Item> box = (rbUnique.isSelected()) ? cbUnique : cbSet;
				((ItemComboBoxModel) box.getModel()).selectPreviousItem();
			}
		});
		searchField.getActionMap().put("downPressed", new AbstractAction() {
			private static final long serialVersionUID = 5445050628155017232L;

			@Override
			public void actionPerformed(ActionEvent e) {
				JComboBox<Item> box = (rbUnique.isSelected()) ? cbUnique : cbSet;
				((ItemComboBoxModel) box.getModel()).selectNextItem();
			}
		});
	}

	private void switchCard(String cardName) {
		isSwitching = true;
		if (rbUnique.isSelected()) {
			cbUnique.setMaximumRowCount(20);
			uniqueModel.resetView();
		} else {
			cbSet.setMaximumRowCount(20);
			setModel.resetView();
		}
		searchField.setText("");

		CardLayout cl = (CardLayout) (cardsPanel.getLayout());
		cl.show(cardsPanel, cardName);
		searchField.requestFocus();
		isSwitching = false;
	}

	private void updateChart() {
		JComboBox<Item> activeBox = (rbUnique.isSelected()) ? cbUnique : cbSet;
		activeBox.hidePopup();
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
			return String.format("Optimal level to gamble %s is clvl%.0f with a chance of %s%% (1 in %d)",
					item.toString(), summary[0], DECIMALS.format(bestChance * 100), Math.round(reverse));
		}
		return String.format("Optimal level range to gamble %s is clvl%.0f to clvl%.0f with a chance of %s%% (1 in %d)",
				item.toString(), summary[0], summary[1], DECIMALS.format(bestChance * 100), Math.round(reverse));
	}

	private String getCursorFormatString(Double y) {
		if (y <= 0.0) {
			return "Impossible";
		}
		double reverse = 1.0 / y;
		return DECIMALS.format(y * 100) + "% (1 in " + Math.round(reverse) + ")";
	}

	public static void main(String[] args) {
		SwingUtilities.invokeLater(() -> {
			D2GambleUI gui = new D2GambleUI();
			gui.setVisible(true);
		});
	}
}