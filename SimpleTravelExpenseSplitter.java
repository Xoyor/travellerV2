import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import javax.swing.*;

// Simple Person class
class Person {
    String name;
    double amount;

    Person(String name, double amount) {
        this.name = name;
        this.amount = amount;
    }
}

// Simple ExpenseCalculator class
class ExpenseCalculator {
    private ArrayList<Person> people;

    ExpenseCalculator(ArrayList<Person> people) {
        this.people = people;
    }

    double getTotal() {
        double sum = 0;
        for (Person p : people) {
            sum += p.amount;
        }
        return sum;
    }

    double getAverage() {
        return people.size() > 0 ? getTotal() / people.size() : 0;
    }

    String getReport() {
        if (people.size() == 0)
            return "Welcome! 👋\n\nAdd people and their expenses to start calculating the fair split!";

        double avg = getAverage();
        StringBuilder sb = new StringBuilder();

        sb.append("╔═══════════════════════════════════╗\n");
        sb.append("║        💰 EXPENSE REPORT 💰       ║\n");
        sb.append("╚═══════════════════════════════════╝\n\n");

        sb.append("📊 Summary:\n");
        sb.append("   Total Expenses: ").append(String.format("%.2f", getTotal())).append(" ฿\n");
        sb.append("   Average per Person: ").append(String.format("%.2f", avg)).append(" ฿\n");
        sb.append("   Number of People: ").append(people.size()).append("\n\n");

        sb.append("💸 Individual Balances:\n");
        sb.append("─────────────────────────────────────\n");

        for (Person p : people) {
            double balance = p.amount - avg;
            if (balance > 0.01) {
                sb.append("💚 ").append(p.name).append(" should receive ")
                        .append(String.format("%.2f", balance)).append(" ฿\n");
            } else if (balance < -0.01) {
                sb.append("💸 ").append(p.name).append(" should pay ")
                        .append(String.format("%.2f", -balance)).append(" ฿\n");
            } else {
                sb.append("✅ ").append(p.name).append(" is perfectly settled!\n");
            }
        }

        sb.append("\n🎉 Calculation complete! Have a great trip!");
        return sb.toString();
    }
}

// Main GUI class - simplified
public class SimpleTravelExpenseSplitter extends JFrame implements ActionListener {
    // Components
    private JTextField nameField, amountField;
    private JButton addBtn, calcBtn, clearBtn, removeBtn;
    private JTextArea resultArea;
    private JList<String> personList;
    private DefaultListModel<String> listModel;
    private ArrayList<Person> people;

    public SimpleTravelExpenseSplitter() {
        people = new ArrayList<>();
        setupGUI();
    }

    private void setupGUI() {
        setTitle("💰 Travel Expense Splitter");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        getContentPane().setBackground(new Color(245, 245, 250));

        // Top panel - input
        JPanel topPanel = new JPanel(new FlowLayout());
        topPanel.setBackground(new Color(70, 130, 180));
        topPanel.setBorder(BorderFactory.createEmptyBorder(15, 10, 15, 10));

        JLabel nameLabel = new JLabel("👤 Name:");
        nameLabel.setForeground(Color.WHITE);
        nameLabel.setFont(new Font("Arial", Font.BOLD, 14));
        topPanel.add(nameLabel);

        nameField = new JTextField(12);
        nameField.setFont(new Font("Arial", Font.PLAIN, 14));
        nameField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
                BorderFactory.createEmptyBorder(5, 8, 5, 8)));
        topPanel.add(nameField);

        JLabel amountLabel = new JLabel("💵 Amount:");
        amountLabel.setForeground(Color.WHITE);
        amountLabel.setFont(new Font("Arial", Font.BOLD, 14));
        topPanel.add(amountLabel);

        amountField = new JTextField(10);
        amountField.setFont(new Font("Arial", Font.PLAIN, 14));
        amountField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
                BorderFactory.createEmptyBorder(5, 8, 5, 8)));
        topPanel.add(amountField);

        addBtn = createStyledButton("➕ Add", new Color(34, 139, 34));
        addBtn.addActionListener(this);
        topPanel.add(addBtn);

        removeBtn = createStyledButton("🗑️ Remove", new Color(220, 20, 60));
        removeBtn.addActionListener(this);
        topPanel.add(removeBtn);

        add(topPanel, BorderLayout.NORTH);

        // Center panel - split view
        JPanel centerPanel = new JPanel(new GridLayout(1, 2, 10, 0));
        centerPanel.setBackground(new Color(245, 245, 250));
        centerPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Left side - people list
        JPanel leftPanel = new JPanel(new BorderLayout());
        leftPanel.setBackground(Color.WHITE);
        leftPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder(BorderFactory.createLineBorder(new Color(70, 130, 180), 2),
                        "👥 People List", 0, 0, new Font("Arial", Font.BOLD, 16), new Color(70, 130, 180)),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)));

        listModel = new DefaultListModel<>();
        personList = new JList<>(listModel);
        personList.setFont(new Font("Arial", Font.PLAIN, 13));
        personList.setSelectionBackground(new Color(173, 216, 230));
        personList.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        JScrollPane listScroll = new JScrollPane(personList);
        listScroll.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200), 1));
        leftPanel.add(listScroll, BorderLayout.CENTER);
        centerPanel.add(leftPanel);

        // Right side - results
        JPanel rightPanel = new JPanel(new BorderLayout());
        rightPanel.setBackground(Color.WHITE);
        rightPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder(BorderFactory.createLineBorder(new Color(70, 130, 180), 2),
                        "📊 Results", 0, 0, new Font("Arial", Font.BOLD, 16), new Color(70, 130, 180)),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)));

        resultArea = new JTextArea();
        resultArea.setEditable(false);
        resultArea.setFont(new Font("Consolas", Font.PLAIN, 13));
        resultArea.setBackground(new Color(248, 248, 255));
        resultArea.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        resultArea.setText("Welcome! 👋\n\nAdd people and their expenses to start calculating the fair split!");

        JScrollPane resultScroll = new JScrollPane(resultArea);
        resultScroll.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200), 1));
        rightPanel.add(resultScroll, BorderLayout.CENTER);
        centerPanel.add(rightPanel);

        add(centerPanel, BorderLayout.CENTER);

        // Bottom panel - buttons
        JPanel bottomPanel = new JPanel(new FlowLayout());
        bottomPanel.setBackground(new Color(245, 245, 250));
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 15, 10));

        calcBtn = createStyledButton("📊 Calculate Split", new Color(30, 144, 255));
        calcBtn.addActionListener(this);
        bottomPanel.add(calcBtn);

        clearBtn = createStyledButton("🔄 Clear All", new Color(255, 140, 0));
        clearBtn.addActionListener(this);
        bottomPanel.add(clearBtn);

        add(bottomPanel, BorderLayout.SOUTH);

        // Window settings
        setSize(700, 500);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private JButton createStyledButton(String text, Color bgColor) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 13));
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setPreferredSize(new Dimension(140, 35));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return button;
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == addBtn) {
            addPerson();
        } else if (e.getSource() == removeBtn) {
            removePerson();
        } else if (e.getSource() == calcBtn) {
            calculateSplit();
        } else if (e.getSource() == clearBtn) {
            clearAll();
        }
    }

    private void addPerson() {
        try {
            String name = nameField.getText().trim();
            String amountText = amountField.getText().trim();

            if (name.isEmpty() || amountText.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter both name and amount!");
                return;
            }

            double amount = Double.parseDouble(amountText);
            if (amount < 0) {
                JOptionPane.showMessageDialog(this, "Amount cannot be negative!");
                return;
            }

            people.add(new Person(name, amount));
            updateList();
            nameField.setText("");
            amountField.setText("");
            nameField.requestFocus();

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Please enter a valid number!");
        }
    }

    private void removePerson() {
        int selectedIndex = personList.getSelectedIndex();
        if (selectedIndex != -1) {
            people.remove(selectedIndex);
            updateList();
        } else {
            JOptionPane.showMessageDialog(this, "Please select a person to remove!");
        }
    }

    private void calculateSplit() {
        if (people.size() == 0) {
            JOptionPane.showMessageDialog(this, "Please add at least one person first!");
            return;
        }

        ExpenseCalculator calc = new ExpenseCalculator(people);
        resultArea.setText(calc.getReport());
    }

    private void clearAll() {
        int confirm = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to clear all data?",
                "Confirm Clear",
                JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            people.clear();
            updateList();
            resultArea.setText("Welcome! 👋\n\nAdd people and their expenses to start calculating the fair split!");
        }
    }

    private void updateList() {
        listModel.clear();
        for (Person p : people) {
            listModel.addElement("👤 " + p.name + " - " + String.format("%.2f", p.amount) + " ฿");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new SimpleTravelExpenseSplitter());
    }
}