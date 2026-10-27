package Mini_Project;

//IMPORT SECTION
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

// ============================================================ 
// MEDICINE CLASS 
// ============================================================
class Medicine {
    int id, stock;
    String name;
    double price;

    Medicine(int id, String name, double price, int stock) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.stock = stock;
    }
}

// ============================================================ 
// MAIN PHARMACY INVENTORY SYSTEM CLASS 
// ============================================================
public class PharmacyInventorySystem extends JFrame {
    Medicine[] medicines = new Medicine[100]; //CAN STORE UPTO 100 MEDICINES
    int count = 0;
    
    //INPUT FIELDS FOR MEDICINE DETAILS
    JTextField id = new JTextField();
    JTextField name = new JTextField();
    JTextField price = new JTextField();
    JTextField stock = new JTextField();
    
    // SEARCH AND BILLING INPUT FIELDS
    JTextField search = new JTextField(15);
    JTextField qty = new JTextField(5);

    // ------------------------------------------------------------ 
    // TABLE MODEL 
    // ------------------------------------------------------------
    DefaultTableModel model = new DefaultTableModel(
            new String[]{"ID", "Medicine Name", "Price", "Stock"}, 0) {

        @Override
        public boolean isCellEditable(int r, int c) {
            return false;
        }
    };

    JTable table = new JTable(model);
    
    // ============================================================ 
    // CONSTRUCTOR 
    // ============================================================ 
    // This method runs automatically when the PharmacyInventorySystem 
    // object is created. It creates and arranges the GUI.
    public PharmacyInventorySystem() {

        setTitle("Pharmacy Inventory System");
        setSize(900, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        // BorderLayout divides the window into North, South, 
        // East, West and Center sections.
        JPanel main = new JPanel(new BorderLayout(10, 10));
        main.setBorder(
                BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // ============================================================ 
        // MEDICINE DETAILS SECTION 
        // ============================================================
        JPanel input = new JPanel(new GridLayout(3, 4, 5, 5));
        input.setBorder(
                BorderFactory.createTitledBorder("Medicine Details"));

        input.add(new JLabel("Medicine ID:"));
        input.add(id);

        input.add(new JLabel("Medicine Name:"));
        input.add(name);

        input.add(new JLabel("Price:"));
        input.add(price);

        input.add(new JLabel("Stock:"));
        input.add(stock);
        
        //BUTTONS
        JButton add = new JButton("Add Medicine");
        JButton update = new JButton("Update Medicine");
        JButton searchBtn = new JButton("Search Medicine");
        JButton bill = new JButton("Generate Bill");

        input.add(add);
        input.add(update);
        input.add(searchBtn);
        input.add(bill);

        // ============================================================
        // SEARCH / BILLING SECTION
        // ============================================================
        JPanel controls = new JPanel(
                new FlowLayout(FlowLayout.LEFT));

        controls.setBorder(
                BorderFactory.createTitledBorder(
                        "Search / Billing"));

        controls.add(new JLabel("Search:"));
        controls.add(search);

        JButton show = new JButton("Show All");
        controls.add(show);

        controls.add(new JLabel("Quantity:"));
        controls.add(qty);

        // Button actions
        add.addActionListener(e -> addMedicine());
        update.addActionListener(e -> updateMedicine());

        // Search uses LOWER search box
        searchBtn.addActionListener(e -> searchMedicine());

        // Bill uses LOWER search box + quantity
        bill.addActionListener(e -> generateBill());

        show.addActionListener(e -> refresh());

        main.add(input, BorderLayout.NORTH);

        JPanel center = new JPanel(
                new BorderLayout(5, 5));

        center.add(controls, BorderLayout.NORTH);
        center.add(new JScrollPane(table), BorderLayout.CENTER);

        main.add(center, BorderLayout.CENTER);

        add(main);
    }

    // ============================================================ 
    // ADD MEDICINE METHOD 
    // ============================================================
    void addMedicine() {
        try {
            int i = Integer.parseInt(id.getText().trim());
            String n = name.getText().trim();
            double p = Double.parseDouble(price.getText().trim());
            int s = Integer.parseInt(stock.getText().trim());

            if (n.isEmpty())
                throw new Exception(
                        "Medicine name cannot be empty.");

            if (p <= 0)
                throw new Exception(
                        "Price must be greater than 0.");

            if (s < 0)
                throw new Exception(
                        "Stock cannot be negative.");

            if (find(i) != null)
                throw new Exception(
                        "Medicine ID already exists.");

            if (count == medicines.length)
                throw new Exception(
                        "Storage is full.");

            medicines[count++] =
                    new Medicine(i, n, p, s);

            JOptionPane.showMessageDialog(
                    this,
                    "Medicine added successfully!");

            clear();
            refresh();

        } catch (NumberFormatException e) {
            error("Enter valid numeric values.");
        } catch (Exception e) {
            error(e.getMessage());
        }
    }
    // ============================================================ 
    // UPDATE STOCK + PRICE
    // ============================================================
    
    void updateMedicine() {
        try {
            Medicine m = find(
                    Integer.parseInt(id.getText().trim()));

            double p = Double.parseDouble(
                    price.getText().trim());

            int s = Integer.parseInt(
                    stock.getText().trim());

            if (m == null)
                throw new Exception(
                        "Medicine not found.");

            if (p <= 0)
                throw new Exception(
                        "Price must be greater than 0.");

            if (s < 0)
                throw new Exception(
                        "Stock cannot be negative.");

            m.price = p;
            m.stock = s;

            JOptionPane.showMessageDialog(
                    this,
                    "Medicine updated successfully!");

            refresh();

        } catch (NumberFormatException e) {
            error("Enter valid ID, price and stock.");
        } catch (Exception e) {
            error(e.getMessage());
        }
    }
    
    // ============================================================ 
    // SEARCH MEDICINE using LOWER Search box
    // ============================================================
    void searchMedicine() {

        String text =
                search.getText().trim().toLowerCase();

        if (text.isEmpty()) {
            error("Enter an ID or medicine name.");
            return;
        }

        model.setRowCount(0);
        boolean found = false;

        for (int i = 0; i < count; i++) {

            Medicine m = medicines[i];

            if (String.valueOf(m.id).equals(text)
                    || m.name.toLowerCase().contains(text)) {

                addRow(m);
                found = true;
            }
        }

        if (!found) {
            JOptionPane.showMessageDialog(
                    this,
                    "Medicine not found.");

            refresh();
        }
    }
// ============================================================ 
// GENERATE BILL
// ============================================================
    
    void generateBill() {
        try {

            String text =
                    search.getText().trim().toLowerCase();

            int q = Integer.parseInt(
                    qty.getText().trim());

            if (text.isEmpty())
                throw new Exception(
                        "Enter Medicine ID or name in Search.");

            if (q <= 0)
                throw new Exception(
                        "Quantity must be greater than 0.");

            Medicine m = findMedicine(text);

            if (m == null)
                throw new Exception(
                        "Medicine not found.");

            if (q > m.stock)
                throw new Exception(
                        "Insufficient stock. Available: "
                                + m.stock);

            double total = m.price * q;

            m.stock -= q;

            String bill = String.format(
                    "===============================%n"
                            + "   PHARMACY BILL%n"
                            + "===============================%n"
                            + "Medicine ID : %d%n"
                            + "Medicine    : %s%n"
                            + "Price       : ₹%.2f%n"
                            + "Quantity    : %d%n"
                            + "--------------------------------%n"
                            + "TOTAL       : ₹%.2f%n"
                            + "===============================%n"
                            + "   Thank You! Visit Again%n"
                            + "===============================",
                    m.id,
                    m.name,
                    m.price,
                    q,
                    total);

            JTextArea area =
                    new JTextArea(bill);

            area.setFont(
                    new Font(
                            "Monospaced",
                            Font.PLAIN,
                            14));

            area.setEditable(false);

            JOptionPane.showMessageDialog(
                    this,
                    area,
                    "Generated Bill",
                    JOptionPane.INFORMATION_MESSAGE);

            qty.setText("");
            refresh();

        } catch (NumberFormatException e) {
            error("Enter a valid quantity.");
        } catch (Exception e) {
            error(e.getMessage());
        }
    }

    // ============================================================ 
    // FIND MEDICINE BY ID 
    // ============================================================
    Medicine find(int id) {
        for (int i = 0; i < count; i++) {
            if (medicines[i].id == id)
                return medicines[i];
        }

        return null;
    }

    // Find by ID or exact name
    Medicine findMedicine(String text) {

        for (int i = 0; i < count; i++) {

            Medicine m = medicines[i];

            if (String.valueOf(m.id).equals(text)
                    || m.name.toLowerCase().equals(text)) {
                return m;
            }
        }
        return null;
    }

    // Refresh Table
    void refresh() {
        model.setRowCount(0);

        for (int i = 0; i < count; i++)
            addRow(medicines[i]);
    }

    // Add Table Row
    void addRow(Medicine m) {
        model.addRow(new Object[]{
                m.id,
                m.name,
                String.format("%.2f", m.price),
                m.stock
        });
    }

    // Clear Fields
    void clear() {
        id.setText("");
        name.setText("");
        price.setText("");
        stock.setText("");
        qty.setText("");
    }

    // Error Message
    void error(String msg) {
        JOptionPane.showMessageDialog(
                this,
                msg,
                "Error",
                JOptionPane.ERROR_MESSAGE);
    }
    
    // ============================================================ 
    // MAIN METHOD 
    // ============================================================
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() ->
                new PharmacyInventorySystem().setVisible(true));
    }
}
