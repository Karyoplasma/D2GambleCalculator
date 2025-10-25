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
import core.Item;
import net.miginfocom.swing.MigLayout;

public class D2GambleCalculatorGUI extends JFrame {

	private static final long serialVersionUID = 4974391899083728435L;
	private final JRadioButton rbUnique = new JRadioButton("Unique");
	private final JRadioButton rbSet = new JRadioButton("Set");

	private final JPanel cardsPanel = new JPanel(new CardLayout());
	private final JComboBox<Item> cbUnique = new JComboBox<>();
	private final JComboBox<Item> cbSet = new JComboBox<>();

	private final JLabel lblResult = new JLabel("Select an item to see probability.");

	public D2GambleCalculatorGUI(List<Item> uniqueItems, List<Item> setItems) {
		super("D2(R) Gamble Probability Calculator");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(500, 200);

		ButtonGroup group = new ButtonGroup();
		group.add(rbUnique);
		group.add(rbSet);
		rbUnique.setSelected(true);

		JPanel radioPanel = new JPanel();
		radioPanel.add(rbUnique);
		radioPanel.add(rbSet);

		uniqueItems.forEach(cbUnique::addItem);
		setItems.forEach(cbSet::addItem);

		cardsPanel.add(cbUnique, "UNIQUE");
		cardsPanel.add(cbSet, "SET");

		rbUnique.addActionListener(e -> switchCard("UNIQUE"));
		rbSet.addActionListener(e -> switchCard("SET"));

		cbUnique.addActionListener(e -> updateResult(cbUnique));
		cbSet.addActionListener(e -> updateResult(cbSet));

		setLayout(new BorderLayout());
		add(radioPanel, BorderLayout.NORTH);
		add(cardsPanel, BorderLayout.CENTER);
		add(lblResult, BorderLayout.SOUTH);

		switchCard("UNIQUE");
	}

	private void switchCard(String cardName) {
		CardLayout cl = (CardLayout) (cardsPanel.getLayout());
		cl.show(cardsPanel, cardName);
		lblResult.setText("Select an item to see probability.");
	}

	private void updateResult(JComboBox<Item> comboBox) {
		Item selected = (Item) comboBox.getSelectedItem();
		if (selected != null) {
			double chance = calculateGambleChance(selected);
			lblResult.setText(String.format("%s chance: 1 in %d (%.5f%%)", selected.toString(),
					Math.round(1.0 / chance), chance * 100));
		}
	}

	// TODO implement
	private double calculateGambleChance(Item item) {
		return 0.25;
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