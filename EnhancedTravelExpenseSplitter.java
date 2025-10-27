import java.util.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

// Class 1: Person - stores individual expense data
class Person {
    private String name;
    private double amount;
    private String category;

    public Person(String name, double amount, String category) {
        this.name = name;
        this.amount = amount;
        this.category = category;
    }

    public String getName() {
        return name;
    }

    public double getAmount() {
        return amount;
    }

    public String getCategory() {
        return category;
    }

    @Override
    public String toString() {
        return name + " - " + String.format("%.2f", amount) + " ฿ (" + category + ")";
    }
}

// Class 2: ExpenseCalculator - handles all calculations
class ExpenseCalculator {
    private ArrayList<Person> people;

    public ExpenseCalculator(ArrayList<Person> people) {
        this.people = people;
    }

    // Calculation Function 1: Calculate total expenses
    public double calculateTotal() {
        return people.stream().mapToDouble(Person::getAmount).sum();
    }

    // Calculation Function 2: Calculate average per person
    public double calculateAverage() {
        return people.isEmpty() ? 0 : calculateTotal() / people.size();
    }

    // Calculation Function 3: Calculate individual balances
    public String calculateSplit() {
        if (people.isEmpty())
            return "No data to calculate.";

        double avg = calculateAverage();
        StringBuilder result = new StringBuilder();
        result.append("💰 EXPENSE SPLIT CALCULATION 💰\n");
        result.append("Total: ").append(String.format("%.2f", calculateTotal())).append(" ฿\n");
        result.append("Average: ").append(String.format("%.2f", avg)).append(" ฿\n\n");

        for (Person p : people) {
            double balance = p.getAmount() - avg;
            if (balance > 0.01) {
                result.append("💚 ").append(p.getName()).append(" receives ")
                        .append(String.format("%.2f", balance)).append(" ฿\n");
            } else if (balance < -0.01) {
                result.append("💸 ").append(p.getName()).append(" pays ")
                        .append(String.format("%.2f", -balance)).append(" ฿\n");
            } else {
                result.append("✅ ").append(p.getName()).append(" is settled\n");
            }
        }
        return result.toString();
    }

    // Calculation Function 4: Calculate statistics
    public String calculateStatistics() {
        if (people.isEmpty())
            return "No data for statistics.";

        double total = calculateTotal();
        double avg = calculateAverage();
        Person maxSpender = people.stream().max(Comparator.comparing(Person::getAmount)).orElse(null);
        Person minSpender = people.stream().min(Comparator.comparing(Person::getAmount)).orElse(null);

        StringBuilder stats = new StringBuilder();
        stats.append("📊 STATISTICS 📊\n");
        stats.append("Participants: ").append(people.size()).append("\n");
        stats.append("Total: ").append(String.format("%.2f", total)).append(" ฿\n");
        stats.append("Average: ").append(String.format("%.2f", avg)).append(" ฿\n");
        if (maxSpender != null && minSpender != null) {
            stats.append("Highest: ").append(maxSpender.getName()).append(" (")
                    .append(String.format("%.2f", maxSpender.getAmount())).append(" ฿)\n");
            stats.append("Lowest: ").append(minSpender.getName()).append(" (")
                    .append(String.format("%.2f", minSpender.getAmount())).append(" ฿)\n");
        }
        return stats.toString();
    }
}

// Class 3: ReportGenerator - generates various reports
class ReportGenerator {
    private ArrayList<Person> people;

    public ReportGenerator(ArrayList<Person> people) {
        this.people = people;
    }

    public String generateDetailedReport() {
        if (people.isEmpty())
            return "No data for detailed report.";

        StringBuilder report = new StringBuilder();
        report.append("📋 DETAILED EXPENSE REPORT 📋\n\n");

        for (int i = 0; i < people.size(); i++) {
            report.append(String.format("%d. %s\n", i + 1, people.get(i).toString()));
        }

        return report.toString();
    }

    public String generateCategoryReport() {
        if (people.isEmpty())
            return "No data for category report.";

        Map<String, Double> categoryTotals = new HashMap<>();
        for (Person p : people) {
            categoryTotals.merge(p.getCategory(), p.getAmount(), Double::sum);
        }

        StringBuilder report = new StringBuilder();
        report.append("📈 CATEGORY BREAKDOWN 📈\n\n");

        for (Map.Entry<String, Double> entry : categoryTotals.entrySet()) {
            report.append(entry.getKey()).append(": ")
                    .append(String.format("%.2f", entry.getValue())).append(" ฿\n");
        }

        return report.toString();
    }
}

// Class 4: Main GUI Application with Menu Functions
public class EnhancedTravelExpenseSplitter extends JFrame implements ActionListener {
    private ArrayList<Person> people;
    private ExpenseCalculator calculator;
    private ReportGenerator reportGenerator;

    // GUI Components
    private JTextField nameField, amountField;
    private JComboBox<String> categoryBox;
    private JTextArea resultArea;
    private JList<String> personList;
    private DefaultListModel<String> listModel;

    // Menu Buttons (4+ Menu Functions)
    private JButton addBtn, removeBtn, calculateBtn, statsBtn, detailBtn, categoryBtn, clearBtn;

    public EnhancedTravelExpenseSplitter() {
        people = new ArrayList<>();
        calculator = new ExpenseCalculator(people);
        reportGenerator = new ReportGenerator(people);
        setupGUI();
    }

    private void setupGUI() {
        setTitle("💰 Enhanced Travel Expense Splitter");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Input Panel (User Input Section)
        JPanel inputPanel = new JPanel(new FlowLayout());
        inputPanel.setBackground(new Color(70, 130, 180));

        inputPanel.add(new JLabel("👤 Name:") {
            {
                setForeground(Color.WHITE);
            }
        });
        nameField = new JTextField(10);
        inputPanel.add(nameField);

        inputPanel.add(new JLabel("💵 Amount:") {
            {
                setForeground(Color.WHITE);
            }
        });
        amountField = new JTextField(8);
        inputPanel.add(amountField);

        inputPanel.add(new JLabel("📁 Category:") {
            {
                setForeground(Color.WHITE);
            }
        });
        categoryBox = new JComboBox<>(new String[] { "Food", "Transport", "Accommodation", "Other" });
        inputPanel.add(categoryBox);

        add(inputPanel, BorderLayout.NORTH);

        // Center Panel - Split View
        JPanel centerPanel = new JPanel(new GridLayout(1, 2, 10, 10));
        centerPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Left: Person List
        JPanel leftPanel = new JPanel(new BorderLayout());
        leftPanel.setBorder(BorderFactory.createTitledBorder("👥 People List"));
        listModel = new DefaultListModel<>();
        personList = new JList<>(listModel);
        leftPanel.add(new JScrollPane(personList), BorderLayout.CENTER);
        centerPanel.add(leftPanel);

        // Right: Results
        JPanel rightPanel = new JPanel(new BorderLayout());
        rightPanel.setBorder(BorderFactory.createTitledBorder("📊 Results"));
        resultArea = new JTextArea();
        resultArea.setEditable(false);
        resultArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        resultArea.setText("Welcome! Add people and expenses to start calculating.");
        rightPanel.add(new JScrollPane(resultArea), BorderLayout.CENTER);
        centerPanel.add(rightPanel);

        add(centerPanel, BorderLayout.CENTER);

        // Menu Panel (4+ Menu Functions)
        JPanel menuPanel = new JPanel(new GridLayout(2, 4, 5, 5));
        menuPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Row 1: Core Functions
        addBtn = createButton("➕ Add Person", new Color(34, 139, 34));
        removeBtn = createButton("🗑️ Remove", new Color(220, 20, 60));
        calculateBtn = createButton("📊 Calculate Split", new Color(30, 144, 255)); // Menu Function 1
        statsBtn = createButton("📈 Statistics", new Color(255, 140, 0)); // Menu Function 2

        // Row 2: Report Functions
        detailBtn = createButton("📋 Detail Report", new Color(138, 43, 226)); // Menu Function 3
        categoryBtn = createButton("📁 Category Report", new Color(50, 205, 50)); // Menu Function 4
        clearBtn = createButton("🔄 Clear All", new Color(178, 34, 34));
        JButton exitBtn = createButton("🚪 Exit", new Color(128, 128, 128));
        exitBtn.addActionListener(e -> System.exit(0));

        menuPanel.add(addBtn);
        menuPanel.add(removeBtn);
        menuPanel.add(calculateBtn);
        menuPanel.add(statsBtn);
        menuPanel.add(detailBtn);
        menuPanel.add(categoryBtn);
        menuPanel.add(clearBtn);
        menuPanel.add(exitBtn);

        add(menuPanel, BorderLayout.SOUTH);

        setSize(800, 600);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private JButton createButton(String text, Color color) {
        JButton button = new JButton(text);
        button.setBackground(color);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.addActionListener(this);
        return button;
    }

    // Action Handler for Menu Functions
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == addBtn) {
            addPerson();
        } else if (e.getSource() == removeBtn) {
            removePerson();
        } else if (e.getSource() == calculateBtn) {
            // Menu Function 1: Calculate Split (Calculation Function)
            resultArea.setText(calculator.calculateSplit());
        } else if (e.getSource() == statsBtn) {
            // Menu Function 2: Statistics (Calculation Function)
            resultArea.setText(calculator.calculateStatistics());
        } else if (e.getSource() == detailBtn) {
            // Menu Function 3: Detail Report
            resultArea.setText(reportGenerator.generateDetailedReport());
        } else if (e.getSource() == categoryBtn) {
            // Menu Function 4: Category Report
            resultArea.setText(reportGenerator.generateCategoryReport());
        } else if (e.getSource() == clearBtn) {
            clearAll();
        }
    }

    // User Input Function: Add Person
    private void addPerson() {
        try {
            String name = nameField.getText().trim();
            String amountText = amountField.getText().trim();
            String category = (String) categoryBox.getSelectedItem();

            if (name.isEmpty() || amountText.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter both name and amount!");
                return;
            }

            double amount = Double.parseDouble(amountText);
            if (amount < 0) {
                JOptionPane.showMessageDialog(this, "Amount cannot be negative!");
                return;
            }

            Person person = new Person(name, amount, category);
            people.add(person);
            updateDisplay();

            // Clear input fields
            nameField.setText("");
            amountField.setText("");
            nameField.requestFocus();

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Please enter a valid number for amount!");
        }
    }

    private void removePerson() {
        int selectedIndex = personList.getSelectedIndex();
        if (selectedIndex != -1) {
            people.remove(selectedIndex);
            updateDisplay();
        } else {
            JOptionPane.showMessageDialog(this, "Please select a person to remove!");
        }
    }

    private void clearAll() {
        int confirm = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to clear all data?",
                "Confirm Clear", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            people.clear();
            updateDisplay();
            resultArea.setText("All data cleared. Add people and expenses to start calculating.");
        }
    }

    private void updateDisplay() {
        listModel.clear();
        for (Person person : people) {
            listModel.addElement(person.toString());
        }

        // Update calculator and report generator references
        calculator = new ExpenseCalculator(people);
        reportGenerator = new ReportGenerator(people);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new EnhancedTravelExpenseSplitter());
    }
}