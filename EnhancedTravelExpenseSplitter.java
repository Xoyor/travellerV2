import java.util.*; // นำเข้า library ของ Java utilities สำหรับ ArrayList, Collections เป็นต้น
import javax.swing.*;// library ที่ใช้ในการสร้าง GUI ด้วย Swing
import java.awt.*;// library ที่ใช้ในการสร้าง GUI ด้วย AWT
import java.awt.event.*;// library ที่ใช้จัดการ event ต่างๆ

// Class 1: Person - stores individual expense data (Encapsulation)
class Person { // เริ่มต้นคลาส Person
    private String name; // ตัวแปร private สำหรับเก็บชื่อ
    private double amount; // ตัวแปร private สำหรับเก็บจำนวนเงิน

    // ตรงนี้จะเป็น Constructor
    public Person(String name, double amount) { // รับพารามิเตอร์ชื่อและจำนวนเงิน
        // คำสั่ง this จะชี้ไปยัง attribute ของ class Person
        this.name = name; // กำหนดค่าชื่อ
        this.amount = amount; // กำหนดค่าจำนวนเงิน
    } // ปิด constructor

    public String getName() { // method getter สำหรับดึงชื่อ
        return name; // คืนค่าชื่อ
    } // ปิด method getName

    public double getAmount() { // method getter สำหรับดึงจำนวนเงิน
        return amount; // คืนค่าจำนวนเงิน
    } // ปิด method getAmount

    @Override // annotation สำหรับ override method
    public String toString() { // method แปลง object เป็น String
        return name + " - " + String.format("%.2f", amount) + " ฿"; // คืนค่าในรูปแบบ "ชื่อ - จำนวนเงิน ฿"
    } // ปิด method toString
} // ปิดคลาส Person

// Class 2: ExpenseCalculator - handles all calculations
class ExpenseCalculator { // เริ่มต้นคลาส ExpenseCalculator
    private ArrayList<Person> people;// รับข้อมูล ArrayList<Person> มาจาก Class หลัก คือ
                                     // EnhancedTravelExpenseSplitter

    // Constructor ที่รับค่า ArrayList<Person> people จาก class หลัก
    public ExpenseCalculator(ArrayList<Person> people) { // รับพารามิเตอร์ ArrayList ของ Person
        this.people = people; // กำหนดค่า ArrayList ที่รับมา
    } // ปิด constructor

    // Calculation Function 1: Calculate total expenses
    public double calculateTotal() { // method คำนวณยอดรวม
        double total = 0.0; // ตัวแปรเก็บยอดรวม เริ่มต้นที่ 0
        for (Person person : people) { // วนลูปผ่านแต่ละคนในรายการ
            total += person.getAmount(); // บวกจำนวนเงินของแต่ละคนเข้าไปในยอดรวม
        } // ปิดลูป for
        return total; // คืนค่ายอดรวม
    } // ปิด method calculateTotal

    // Calculation Function 2: Calculate average per person
    public double calculateAverage() { // method คำนวณค่าเฉลี่ย
        // มีการเช็คก่อนว่า people ว่างไหม/มีค่าอะไรไหมในตัวแปร people
        // return ค่าใช้จ่ายฉลี่ย ? คือการทำ if else แบบย่อ ถ้า false แล้วจะ เอาค่า
        // CalculateTotal() ที่เป็นเงินรวมที่ใช้และจำนวนคนมาหารกัน
        // CalculateTotal()(เงินรวมทั้งหมด) / people.size()(จำนวนคน)
        // ถ้าเป็น true จะ return 0
        return people.isEmpty() ? 0 : calculateTotal() / people.size(); // ถ้าไม่มีข้อมูลคืนค่า 0 ไม่งั้นคืนค่าเฉลี่ย
    } // ปิด method calculateAverage

    // Calculation Function 3: Calculate individual balances
    public String calculateSplit() { // method คำนวณการแบ่งค่าใช้จ่าย
        if (people.isEmpty()) // ตรวจสอบว่ามีข้อมูลหรือไม่
            return "No data to calculate."; // คืนค่าข้อความไม่มีข้อมูล

        double avg = calculateAverage(); // คำนวณค่าเฉลี่ย
        StringBuilder result = new StringBuilder(); // สร้าง StringBuilder สำหรับสร้างผลลัพธ์
        result.append("💰 EXPENSE SPLIT CALCULATION 💰\n"); // เพิ่มหัวข้อ
        result.append("Total: ").append(String.format("%.2f", calculateTotal())).append(" ฿\n"); // เพิ่มยอดรวม
        result.append("Average: ").append(String.format("%.2f", avg)).append(" ฿\n\n"); // เพิ่มค่าเฉลี่ย

        for (Person p : people) { // วนลูปผ่านแต่ละคน
            double balance = p.getAmount() - avg; // คำนวณส่วนต่างจากค่าเฉลี่ย
            if (balance > 0.01) { // ถ้าจ่ายมากกว่าค่าเฉลี่ย
                result.append("💚 ").append(p.getName()).append(" receives ")
                        .append(String.format("%.2f", balance)).append(" ฿\n"); // แสดงว่าได้รับเงินคืน
            } else if (balance < -0.01) { // ถ้าจ่ายน้อยกว่าค่าเฉลี่ย
                result.append("💸 ").append(p.getName()).append(" pays ")
                        .append(String.format("%.2f", -balance)).append(" ฿\n"); // แสดงว่าต้องจ่ายเพิ่ม
            } else { // ถ้าจ่ายพอดีค่าเฉลี่ย
                result.append("✅ ").append(p.getName()).append(" is settled\n"); // แสดงว่าจ่ายครบแล้ว
            } // ปิด else
        } // ปิดลูป for
        return result.toString(); // คืนค่าผลลัพธ์ในรูปแบบ String
    } // ปิด method calculateSplit

    // Calculation Function 4: Calculate statistics
    public String calculateStatistics() { // method คำนวณสถิติ
        if (people.isEmpty()) // ตรวจสอบว่ามีข้อมูลหรือไม่
            return "No data for statistics."; // คืนค่าข้อความไม่มีข้อมูล

        double total = calculateTotal(); // คำนวณยอดรวม
        double avg = calculateAverage(); // คำนวณค่าเฉลี่ย
        Person maxSpender = people.stream().max(Comparator.comparing(Person::getAmount)).orElse(null); // หาคนที่จ่ายมากที่สุด
        Person minSpender = people.stream().min(Comparator.comparing(Person::getAmount)).orElse(null); // หาคนที่จ่ายน้อยที่สุด

        StringBuilder stats = new StringBuilder(); // สร้าง StringBuilder สำหรับสร้างรายงานสถิติ
        stats.append("📊 STATISTICS 📊\n"); // เพิ่มหัวข้อสถิติ
        stats.append("Participants: ").append(people.size()).append("\n"); // เพิ่มจำนวนผู้เข้าร่วม
        stats.append("Total: ").append(String.format("%.2f", total)).append(" ฿\n"); // เพิ่มยอดรวม
        stats.append("Average: ").append(String.format("%.2f", avg)).append(" ฿\n"); // เพิ่มค่าเฉลี่ย
        if (maxSpender != null && minSpender != null) { // ตรวจสอบว่าหาค่าสูงสุดและต่ำสุดได้
            stats.append("Highest: ").append(maxSpender.getName()).append(" (")
                    .append(String.format("%.2f", maxSpender.getAmount())).append(" ฿)\n"); // เพิ่มข้อมูลคนจ่ายมากสุด
            stats.append("Lowest: ").append(minSpender.getName()).append(" (")
                    .append(String.format("%.2f", minSpender.getAmount())).append(" ฿)\n"); // เพิ่มข้อมูลคนจ่ายน้อยสุด
        } // ปิด if
        return stats.toString(); // คืนค่ารายงานสถิติในรูปแบบ String
    } // ปิด method calculateStatistics
} // ปิดคลาส ExpenseCalculator

// Class 3: ReportGenerator - generates various reports
class ReportGenerator { // เริ่มต้นคลาส ReportGenerator
    private ArrayList<Person> people; // ตัวแปรเก็บรายการคนทั้งหมด

    public ReportGenerator(ArrayList<Person> people) { // constructor รับรายการคน
        this.people = people; // กำหนดค่า ArrayList ที่รับมา
    } // ปิด constructor

    public String generateDetailedReport() { // method สร้างรายงานแบบละเอียด
        if (people.isEmpty()) // ตรวจสอบว่ามีข้อมูลหรือไม่
            return "No data for detailed report."; // คืนค่าข้อความไม่มีข้อมูล

        StringBuilder report = new StringBuilder(); // สร้าง StringBuilder สำหรับสร้างรายงาน
        report.append("📋 DETAILED EXPENSE REPORT 📋\n\n"); // เพิ่มหัวข้อรายงาน

        for (int i = 0; i < people.size(); i++) { // วนลูปผ่านแต่ละคนด้วย index
            report.append(String.format("%d. %s\n", i + 1, people.get(i).toString())); // เพิ่มข้อมูลของแต่ละคนพร้อมหมายเลข
        } // ปิดลูป for

        return report.toString(); // คืนค่ารายงานในรูปแบบ String
    } // ปิด method generateDetailedReport

    public String generateSummaryReport() { // method สร้างรายงานแบบสรุป
        if (people.isEmpty()) // ตรวจสอบว่ามีข้อมูลหรือไม่
            return "No data for summary report."; // คืนค่าข้อความไม่มีข้อมูล

        ExpenseCalculator calc = new ExpenseCalculator(people); // สร้าง instance ของ ExpenseCalculator
        StringBuilder report = new StringBuilder(); // สร้าง StringBuilder สำหรับสร้างรายงาน
        report.append("📊 EXPENSE SUMMARY 📊\n\n"); // เพิ่มหัวข้อรายงานสรุป

        report.append("Total Participants: ").append(people.size()).append("\n"); // เพิ่มจำนวนผู้เข้าร่วม
        report.append("Total Expenses: ").append(String.format("%.2f", calc.calculateTotal())).append(" ฿\n"); // เพิ่มยอดรวม
        report.append("Average per Person: ").append(String.format("%.2f", calc.calculateAverage())).append(" ฿\n\n"); // เพิ่มค่าเฉลี่ย

        report.append("All Expenses:\n"); // เพิ่มหัวข้อรายการค่าใช้จ่ายทั้งหมด
        for (Person p : people) { // วนลูปผ่านแต่ละคน
            report.append("• ").append(p.toString()).append("\n"); // เพิ่มข้อมูลของแต่ละคน
        } // ปิดลูป for

        return report.toString(); // คืนค่ารายงานในรูปแบบ String
    } // ปิด method generateSummaryReport
} // ปิดคลาส ReportGenerator

// Class 4: Main GUI Application with Menu Functions
public class EnhancedTravelExpenseSplitter extends JFrame implements ActionListener { // คลาสหลักสำหรับ GUI
    private ArrayList<Person> people; // เป็น Object ตัวหลัก ArrayList ที่เก็บข้อมูล Person และถูกส่งไปยัง
                                      // ExpenseCalculator และ ReportGenerator
    private ExpenseCalculator calculator; // สร้าง instance ของ ExpenseCalculator
    private ReportGenerator reportGenerator; // สร้าง instance ของ ReportGenerator

    // GUI Components
    private JTextField nameField, amountField; // ตัวรับ input ชื่อ และเงินที่จ่าย
    private JTextArea resultArea; // ส่วนแสดงผลลัพธ์ต่างๆ

    // 2 ตัวนี้เป็นส่วนที่ใช้แสดงรายชื่อคนที่เพิ่มเข้ามา ListModel เปรียบเสมือน
    // Container ที่เก็บข้อมูลสำหรับ JList ส่วน Jlist จะเป็นตัวแสดงผล
    private JList<String> personList; // รายการแสดงชื่อคนและจำนวนเงิน
    private DefaultListModel<String> listModel; // โมเดลสำหรับจัดการข้อมูลใน JList

    // Menu Buttons
    private JButton addBtn, removeBtn, calculateBtn, statsBtn, detailBtn, summaryBtn, clearBtn; // ปุ่มเมนูต่างๆ

    public EnhancedTravelExpenseSplitter() { // constructor ของคลาสหลัก
        people = new ArrayList<>(); // สร้าง ArrayList ใหม่สำหรับเก็บข้อมูลคน
        calculator = new ExpenseCalculator(people); // สร้าง instance ของ ExpenseCalculator
        reportGenerator = new ReportGenerator(people); // สร้าง instance ของ ReportGenerator
        setupGUI(); // เรียก method ตั้งค่า GUI
    } // ปิด constructor

    /*********************** Method สร้าง GUI **************************/
    private void setupGUI() { // method สำหรับตั้งค่า GUI
        setTitle("💰 Enhanced Travel Expense Splitter"); // ตั้งชื่อหน้าต่าง
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // ตั้งค่าให้ปิดโปรแกรมเมื่อปิดหน้าต่าง
        setLayout(new BorderLayout()); // ตั้งค่า layout เป็น BorderLayout

        // Input Panel (User Input Section)
        JPanel inputPanel = new JPanel(new FlowLayout()); // สร้าง panel สำหรับรับข้อมูล
        inputPanel.setBackground(new Color(70, 130, 180)); // ตั้งสีพื้นหลัง

        inputPanel.add(new JLabel("👤 Name:") { // สร้าง label สำหรับชื่อ
            { // anonymous inner class
                setForeground(Color.WHITE); // ตั้งสีตัวอักษรเป็นสีขาว
            } // ปิด anonymous inner class
        }); // ปิดการเพิ่ม label
        nameField = new JTextField(10); // สร้าง text field สำหรับรับชื่อ
        inputPanel.add(nameField); // เพิ่ม text field เข้าไปใน panel

        inputPanel.add(new JLabel("💵 Amount:") { // สร้าง label สำหรับจำนวนเงิน
            { // anonymous inner class
                setForeground(Color.WHITE); // ตั้งสีตัวอักษรเป็นสีขาว
            } // ปิด anonymous inner class
        }); // ปิดการเพิ่ม label
        amountField = new JTextField(8); // สร้าง text field สำหรับรับจำนวนเงิน
        inputPanel.add(amountField); // เพิ่ม text field เข้าไปใน panel

        add(inputPanel, BorderLayout.NORTH); // เพิ่ม input panel ที่ด้านบน

        // Center Panel - Split View
        JPanel centerPanel = new JPanel(new GridLayout(1, 2, 10, 10)); // สร้าง panel กลางแบบแบ่ง 2 คอลัมน์
        centerPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // ตั้งค่า border

        // Left: Person List
        JPanel leftPanel = new JPanel(new BorderLayout()); // สร้าง panel ซ้ายสำหรับรายชื่อ
        leftPanel.setBorder(BorderFactory.createTitledBorder("👥 People List")); // ตั้งชื่อ border
        listModel = new DefaultListModel<>(); // สร้าง list model
        personList = new JList<>(listModel); // สร้าง JList พร้อม model
        leftPanel.add(new JScrollPane(personList), BorderLayout.CENTER); // เพิ่ม JList พร้อม scroll pane
        centerPanel.add(leftPanel); // เพิ่ม panel ซ้ายเข้าไปใน center panel

        // Right: Results
        JPanel rightPanel = new JPanel(new BorderLayout()); // สร้าง panel ขวาสำหรับแสดงผลลัพธ์
        rightPanel.setBorder(BorderFactory.createTitledBorder("📊 Results")); // ตั้งชื่อ border
        resultArea = new JTextArea(); // สร้าง text area สำหรับแสดงผลลัพธ์
        resultArea.setEditable(false); // ตั้งค่าให้แก้ไขไม่ได้
        resultArea.setFont(new Font("Monospaced", Font.PLAIN, 12)); // ตั้งค่าฟอนต์
        resultArea.setText("Welcome! Add people and expenses to start calculating."); // ตั้งข้อความเริ่มต้น
        rightPanel.add(new JScrollPane(resultArea), BorderLayout.CENTER); // เพิ่ม text area พร้อม scroll pane
        centerPanel.add(rightPanel); // เพิ่ม panel ขวาเข้าไปใน center panel

        add(centerPanel, BorderLayout.CENTER); // เพิ่ม center panel ที่ตรงกลาง

        // Menu Panel (4+ Menu Functions)
        JPanel menuPanel = new JPanel(new GridLayout(2, 4, 5, 5)); // สร้าง panel เมนูแบบ 2x4 grid
        menuPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // ตั้งค่า border

        // Row 1: Core Functions
        addBtn = createButton("➕ Add Person", new Color(34, 139, 34)); // สร้างปุ่มเพิ่มคน
        removeBtn = createButton("🗑️ Remove", new Color(220, 20, 60)); // สร้างปุ่มลบ
        calculateBtn = createButton("📊 Calculate Split", new Color(30, 144, 255)); // Menu Function 1 //
                                                                                    // สร้างปุ่มคำนวณการแบ่ง
        statsBtn = createButton("📈 Statistics", new Color(255, 140, 0)); // Menu Function 2 // สร้างปุ่มสถิติ

        // Row 2: Report Functions
        detailBtn = createButton("📋 Detail Report", new Color(138, 43, 226)); // Menu Function 3 //
                                                                               // สร้างปุ่มรายงานละเอียด
        summaryBtn = createButton("📊 Summary Report", new Color(50, 205, 50)); // Menu Function 4 //
                                                                                // สร้างปุ่มรายงานสรุป
        clearBtn = createButton("🔄 Clear All", new Color(178, 34, 34)); // สร้างปุ่มล้างข้อมูลทั้งหมด
        JButton exitBtn = createButton("🚪 Exit", new Color(128, 128, 128)); // สร้างปุ่มออกจากโปรแกรม
        exitBtn.addActionListener(e -> System.exit(0)); // เพิ่ม action listener สำหรับออกจากโปรแกรม

        menuPanel.add(addBtn); // เพิ่มปุ่มเพิ่มคนเข้าไปใน menu panel
        menuPanel.add(removeBtn); // เพิ่มปุ่มลบเข้าไปใน menu panel
        menuPanel.add(calculateBtn); // เพิ่มปุ่มคำนวณเข้าไปใน menu panel
        menuPanel.add(statsBtn); // เพิ่มปุ่มสถิติเข้าไปใน menu panel
        menuPanel.add(detailBtn); // เพิ่มปุ่มรายงานละเอียดเข้าไปใน menu panel
        menuPanel.add(summaryBtn); // เพิ่มปุ่มรายงานสรุปเข้าไปใน menu panel
        menuPanel.add(clearBtn); // เพิ่มปุ่มล้างข้อมูลเข้าไปใน menu panel
        menuPanel.add(exitBtn); // เพิ่มปุ่มออกจากโปรแกรมเข้าไปใน menu panel

        add(menuPanel, BorderLayout.SOUTH); // เพิ่ม menu panel ที่ด้านล่าง

        setSize(800, 600); // ตั้งขนาดหน้าต่าง
        setLocationRelativeTo(null); // ตั้งตำแหน่งให้อยู่กลางจอ
        setVisible(true); // แสดงหน้าต่าง
    } // ปิด method setupGUI

    /*********************** Method สร้าง GUI **************************/

    /*********************** Method สร้าง Button **************************/
    private JButton createButton(String text, Color color) { // method สำหรับสร้างปุ่ม
        JButton button = new JButton(text); // สร้างปุ่มใหม่พร้อมข้อความ
        button.setBackground(color); // ตั้งสีพื้นหลัง
        button.setForeground(Color.WHITE); // ตั้งสีตัวอักษรเป็นสีขาว
        button.setFocusPainted(false); // ปิดการแสดง focus border
        button.addActionListener(this); // เพิ่ม action listener
        return button; // คืนค่าปุ่มที่สร้าง
    } // ปิด method createButton

    /*********************** Method สร้าง Button **************************/

    // Action Handler for Menu Functions
    public void actionPerformed(ActionEvent e) { // method จัดการ event เมื่อคลิกปุ่ม
        if (e.getSource() == addBtn) { // ถ้าคลิกปุ่มเพิ่มคน
            addPerson(); // เรียก method addPerson
        } else if (e.getSource() == removeBtn) { // ถ้าคลิกปุ่มลบ
            removePerson(); // เรียก method removePerson
        } else if (e.getSource() == calculateBtn) { // ถ้าคลิกปุ่มคำนวณการแบ่ง
            // Menu Function 1: Calculate Split (Calculation Function)
            resultArea.setText(calculator.calculateSplit()); // แสดงผลการคำนวณการแบ่งค่าใช้จ่าย
        } else if (e.getSource() == statsBtn) { // ถ้าคลิกปุ่มสถิติ
            // Menu Function 2: Statistics (Calculation Function)
            resultArea.setText(calculator.calculateStatistics()); // แสดงผลสถิติ
        } else if (e.getSource() == detailBtn) { // ถ้าคลิกปุ่มรายงานละเอียด
            // Menu Function 3: Detail Report
            resultArea.setText(reportGenerator.generateDetailedReport()); // แสดงรายงานละเอียด
        } else if (e.getSource() == summaryBtn) { // ถ้าคลิกปุ่มรายงานสรุป
            // Menu Function 4: Summary Report
            resultArea.setText(reportGenerator.generateSummaryReport()); // แสดงรายงานสรุป
        } else if (e.getSource() == clearBtn) { // ถ้าคลิกปุ่มล้างข้อมูล
            clearAll(); // เรียก method clearAll
        } // ปิด if-else
    } // ปิด method actionPerformed

    // User Input Function: Add Person
    private void addPerson() { // method สำหรับเพิ่มคน
        try { // เริ่ม try block
            String name = nameField.getText().trim(); // ดึงชื่อจาก text field และตัดช่องว่าง
            String amountText = amountField.getText().trim(); // ดึงจำนวนเงินจาก text field และตัดช่องว่าง

            if (name.isEmpty() || amountText.isEmpty()) { // ตรวจสอบว่าข้อมูลครบหรือไม่
                JOptionPane.showMessageDialog(this, "Please enter both name and amount!"); // แสดงข้อความเตือน
                return; // ออกจาก method
            } // ปิด if

            double amount = Double.parseDouble(amountText); // แปลงข้อความเป็นตัวเลข
            if (amount < 0) { // ตรวจสอบว่าจำนวนเงินติดลบหรือไม่
                JOptionPane.showMessageDialog(this, "Amount cannot be negative!"); // แสดงข้อความเตือน
                return; // ออกจาก method
            } // ปิด if

            Person person = new Person(name, amount); // สร้าง object Person ใหม่
            people.add(person); // เพิ่มคนเข้าไปใน ArrayList
            updateDisplay(); // อัปเดตการแสดงผล

            // Clear input fields
            nameField.setText(""); // ล้างข้อความในช่องชื่อ
            amountField.setText(""); // ล้างข้อความในช่องจำนวนเงิน
            nameField.requestFocus(); // ให้โฟกัสกลับไปที่ช่องชื่อ

        } catch (NumberFormatException ex) { // จับ exception เมื่อแปลงตัวเลขไม่ได้
            JOptionPane.showMessageDialog(this, "Please enter a valid number for amount!"); // แสดงข้อความเตือน
        } // ปิด catch
    } // ปิด method addPerson

    private void removePerson() { // method สำหรับลบคน
        int selectedIndex = personList.getSelectedIndex(); // ดึง index ของรายการที่เลือก
        if (selectedIndex != -1) { // ถ้ามีการเลือกรายการ
            people.remove(selectedIndex); // ลบคนออกจาก ArrayList
            updateDisplay(); // อัปเดตการแสดงผล
        } else { // ถ้าไม่ได้เลือกรายการ
            JOptionPane.showMessageDialog(this, "Please select a person to remove!"); // แสดงข้อความเตือน
        } // ปิด else
    } // ปิด method removePerson

    private void clearAll() { // method สำหรับล้างข้อมูลทั้งหมด
        int confirm = JOptionPane.showConfirmDialog(this, // แสดง dialog ยืนยัน
                "Are you sure you want to clear all data?", // ข้อความถาม
                "Confirm Clear", JOptionPane.YES_NO_OPTION); // ตัวเลือก Yes/No
        if (confirm == JOptionPane.YES_OPTION) { // ถ้าเลือก Yes
            people.clear(); // ล้างข้อมูลใน ArrayList
            updateDisplay(); // อัปเดตการแสดงผล
            resultArea.setText("All data cleared. Add people and expenses to start calculating."); // แสดงข้อความล้างข้อมูลเสร็จ
        } // ปิด if
    } // ปิด method clearAll

    private void updateDisplay() { // method สำหรับอัปเดตการแสดงผล
        listModel.clear(); // clear() ก่อนเพิ่มข้อมูลใหม่ ข้อมูลจะได้ไม่ซ้ำกัน
        for (Person person : people) { // เพิ่มข้อมูลใหม่ลงใน listModel ด้วยการวนลูป
            listModel.addElement(person.toString()); // เพิ่มข้อมูลใหม่ลงใน listModel ผ่าน method addElement()
        } // ปิดลูป for

        // Update calculator and report generator references
        calculator = new ExpenseCalculator(people); // สร้าง instance ใหม่ของ ExpenseCalculator
        reportGenerator = new ReportGenerator(people); // สร้าง instance ใหม่ของ ReportGenerator
    } // ปิด method updateDisplay

    public static void main(String[] args) { // method main สำหรับเริ่มโปรแกรม
        SwingUtilities.invokeLater(() -> new EnhancedTravelExpenseSplitter()); // สร้าง instance ของโปรแกรมใน Event
                                                                               // Dispatch Thread
    } // ปิด method main
} // ปิดคลาส EnhancedTravelExpenseSplitter