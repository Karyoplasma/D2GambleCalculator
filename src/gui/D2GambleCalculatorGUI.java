package gui;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.util.List;
import javax.swing.ButtonGroup;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.SwingUtilities;
import core.DataBase;
import core.GambleCalc;
import core.Item;
import enums.Rarity;
import net.miginfocom.swing.MigLayout;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;

public class D2GambleCalculatorGUI extends JFrame {

	private static final long serialVersionUID = 4974391899083728435L;
	private final JRadioButton rbUnique = new JRadioButton("Unique");
	private final JRadioButton rbSet = new JRadioButton("Set");
	private final GambleCalc calc = new GambleCalc();
	private final JPanel cardsPanel = new JPanel(new CardLayout());
	private final JComboBox<Item> cbUnique = new JComboBox<>();
	private final JComboBox<Item> cbSet = new JComboBox<>();

	private final JLabel lblResult = new JLabel("Select an item to see probability.");
	private final JSpinner spinnerCharLevel = new JSpinner();

	public D2GambleCalculatorGUI(List<Item> uniqueItems, List<Item> setItems) {
		super("D2(R) Gamble Probability Calculator");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(500, 200);

		ButtonGroup group = new ButtonGroup();
		group.add(rbUnique);
		group.add(rbSet);
		rbUnique.setSelected(true);

		JPanel radioPanel = new JPanel();
		spinnerCharLevel.setModel(new SpinnerNumberModel(1, 1, 99, 1));
		spinnerCharLevel.addChangeListener(e -> updateLevel(spinnerCharLevel));
		radioPanel.add(spinnerCharLevel);
		radioPanel.add(rbUnique);
		radioPanel.add(rbSet);

		uniqueItems.forEach(cbUnique::addItem);
		setItems.forEach(cbSet::addItem);

		cardsPanel.add(cbUnique, "UNIQUE");
		cardsPanel.add(cbSet, "SET");

		rbUnique.addActionListener(e -> switchCard("UNIQUE"));
		rbSet.addActionListener(e -> switchCard("SET"));

		cbUnique.addActionListener(e -> updateItem(cbUnique));
		cbSet.addActionListener(e -> updateItem(cbSet));

		getContentPane().setLayout(new BorderLayout());
		getContentPane().add(radioPanel, BorderLayout.NORTH);
		getContentPane().add(cardsPanel, BorderLayout.CENTER);
		getContentPane().add(lblResult, BorderLayout.SOUTH);

		switchCard("UNIQUE");
	}

	private void updateLevel(JSpinner spinnerCharLevel) {
		JComboBox<Item> activeBox = (rbUnique.isSelected()) ? cbUnique : cbSet;
		Item selected = (Item) activeBox.getSelectedItem();
		if (selected != null) {
			double chance = calculateGambleChance(selected, (int) spinnerCharLevel.getValue());
			if (chance == 0.0) {
				lblResult.setText("Cannot be gambled at this level.");
			} else {
				lblResult.setText(String.format("%s chance: 1 in %d (%.5f%%)", selected.toString(),
						Math.round(1.0 / chance), chance * 100));
			}	
		}
	}

	private void switchCard(String cardName) {
		CardLayout cl = (CardLayout) (cardsPanel.getLayout());
		cl.show(cardsPanel, cardName);
		lblResult.setText("Select an item to see probability.");
	}

	private void updateItem(JComboBox<Item> comboBox) {
		Item selected = (Item) comboBox.getSelectedItem();
		if (selected != null) {
			double chance = calculateGambleChance(selected, (int) spinnerCharLevel.getValue());
			if (chance == 0.0) {
				lblResult.setText("Cannot be gambled at this level.");
			} else {
				lblResult.setText(String.format("%s chance: 1 in %d (%.5f%%)", selected.toString(),
						Math.round(1.0 / chance), chance * 100));
			}
		}
	}

	// TODO implement
	private double calculateGambleChance(Item item, int charLvl) {
		return calc.calculateChanceToGamble(item.getName(), charLvl, ((rbUnique.isSelected()) ? Rarity.UNIQUE : Rarity.SET));
	}

	public static void main(String[] args) {
		List<Item> uniqueItems = DataBase.getInstance().getAllUniqueItems();
		List<Item> setItems = DataBase.getInstance().getAllSetItems();

		SwingUtilities.invokeLater(() -> {
			D2GambleCalculatorGUI gui = new D2GambleCalculatorGUI(uniqueItems, setItems);
			gui.setVisible(true);
		});
	}
}