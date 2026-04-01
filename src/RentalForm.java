import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

public class RentalForm extends JFrame {


    private JTextField passengersField;
    private JTextField daysField;
    private JTextField mileageField;
    private JPanel resultPanel;
    private List<Car> cars;
    private static final double GAS_PRICE_PER_GALLON = 2.25;

    public RentalForm() {

        String[] roles = {"USER", "ADMIN"};
String selectedRole = (String) JOptionPane.showInputDialog(
        this,
        "Select user role:",
        "Login",
        JOptionPane.QUESTION_MESSAGE,
        null,
        roles,
        roles[0]
);

if (selectedRole == null) {
    System.exit(0);
}

    currentUserRole = UserRole.valueOf(selectedRole);
        
        try {
            cars = JsonCarLoader.loadCars("cars.json");
        } catch (IOException e) {
            cars = new ArrayList<>();
            JOptionPane.showMessageDialog(
                    this,
                    "Error loading cars.json:\n" + e.getMessage(),
                    "File Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }

        setTitle("Rental Finder");
        setSize(900, 720);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(new Color(240, 242, 245));
        mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20));

        JPanel topWrapper = new JPanel(new GridBagLayout());
        topWrapper.setOpaque(false);

        JPanel cardPanel = new JPanel();
        cardPanel.setLayout(new BoxLayout(cardPanel, BoxLayout.Y_AXIS));
        cardPanel.setBackground(Color.WHITE);
        cardPanel.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(220, 220, 220), 1, true),
                new EmptyBorder(22, 22, 22, 22)
        ));
        cardPanel.setPreferredSize(new Dimension(820, 470));

        JLabel titleLabel = new JLabel("Find Your Perfect Rental");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 28));
        titleLabel.setForeground(new Color(20, 20, 20));
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel subTitleLabel = new JLabel("Enter your trip details to find the most cost-effective car rental option");
        subTitleLabel.setFont(new Font("Arial", Font.PLAIN, 18));
        subTitleLabel.setForeground(new Color(110, 110, 110));
        subTitleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        passengersField = createStyledField("e.g., 4");
        daysField = createStyledField("e.g., 7");
        mileageField = createStyledField("e.g., 500");

        cardPanel.add(titleLabel);
        cardPanel.add(Box.createVerticalStrut(10));
        cardPanel.add(subTitleLabel);
        cardPanel.add(Box.createVerticalStrut(25));

        cardPanel.add(createFieldSection("Number of Passengers", passengersField));
        cardPanel.add(Box.createVerticalStrut(18));

        cardPanel.add(createFieldSection("Number of Rental Days", daysField));
        cardPanel.add(Box.createVerticalStrut(18));

        cardPanel.add(createFieldSection("Approximate Mileage for Trip (miles)", mileageField));
        cardPanel.add(Box.createVerticalStrut(24));

        JPanel buttonPanel = new JPanel(new BorderLayout(12, 0));
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        buttonPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 45));

        JButton findButton = new JButton("Find Best Options");
        findButton.setPreferredSize(new Dimension(650, 45));
        findButton.setBackground(new Color(3, 5, 35));
        findButton.setForeground(Color.WHITE);
        findButton.setFocusPainted(false);
        findButton.setFont(new Font("Arial", Font.BOLD, 16));
        findButton.setBorder(BorderFactory.createEmptyBorder());

        JButton resetButton = new JButton("Reset");
        resetButton.setPreferredSize(new Dimension(90, 45));
        resetButton.setBackground(Color.WHITE);
        resetButton.setForeground(Color.BLACK);
        resetButton.setFocusPainted(false);
        resetButton.setFont(new Font("Arial", Font.PLAIN, 15));
        resetButton.setBorder(new LineBorder(new Color(210, 210, 210), 1, true));

        findButton.addActionListener(e -> findBestCar());

        resetButton.addActionListener(e -> {
            resetField(passengersField, "e.g., 4");
            resetField(daysField, "e.g., 7");
            resetField(mileageField, "e.g., 500");
            showEmptyResult();
        });

        buttonPanel.add(findButton, BorderLayout.CENTER);
        buttonPanel.add(resetButton, BorderLayout.EAST);

        cardPanel.add(buttonPanel);

        topWrapper.add(cardPanel);

        resultPanel = new JPanel();
        resultPanel.setLayout(new BoxLayout(resultPanel, BoxLayout.Y_AXIS));
        resultPanel.setOpaque(false);
        resultPanel.setBorder(new EmptyBorder(20, 0, 0, 0));

        showEmptyResult();

        JPanel content = new JPanel();
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setOpaque(false);
        content.add(topWrapper);
        content.add(resultPanel);

        JScrollPane scrollPane = new JScrollPane(content);
        scrollPane.setBorder(null);
        scrollPane.getViewport().setBackground(new Color(240, 242, 245));
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        mainPanel.add(scrollPane, BorderLayout.CENTER);
        add(mainPanel);
    }

    private enum UserRole {
    ADMIN,
    USER
}

private UserRole currentUserRole = UserRole.USER;

private boolean hasAdminAccess() {
    return currentUserRole == UserRole.ADMIN;
}


    private boolean areInputsFilled(String passengersText, String daysText, String mileageText) {
    return !passengersText.isEmpty() && !daysText.isEmpty() && !mileageText.isEmpty();
    }

    private boolean areInputValuesValid(int passengers, int rentalDays, double mileage) {
    return passengers > 0 && passengers <= 7
            && rentalDays > 0
            && mileage >= 0;
    }


private List<Car> getSuitableCars(List<Car> cars, int passengers) {
    List<Car> suitableCars = new ArrayList<>();

    for (Car car : cars) {
        if (car.getMaxPassengers() >= passengers) {
            suitableCars.add(car);
        }
    }

    return suitableCars;
}


private List<Car> getCheapestCars(List<Car> cars, int rentalDays, double mileage) {
    List<Car> cheapestCars = new ArrayList<>();
    double minCost = Double.MAX_VALUE;

    for (Car car : cars) {
        double totalCost = car.calculateTotalCost(rentalDays, mileage, GAS_PRICE_PER_GALLON);

        if (totalCost < minCost) {
            minCost = totalCost;
            cheapestCars.clear();
            cheapestCars.add(car);
        } else if (totalCost == minCost) {
            cheapestCars.add(car);
        }
    }

    return cheapestCars;
}

private List<Car> getBestComfortCars(List<Car> cars) {
    List<Car> bestCars = new ArrayList<>();
    String bestComfort = "";

    for (Car car : cars) {
        String comfort = car.getComfort();

        if (isBetterComfort(comfort, bestComfort)) {
            bestComfort = comfort;
            bestCars.clear();
            bestCars.add(car);
        } else if (comfort.equals(bestComfort)) {
            bestCars.add(car);
        }
    }

    return bestCars;
}

private boolean isBetterComfort(String c1, String c2) {
    return getComfortRank(c1) > getComfortRank(c2);
}

private void findBestCar() {

    if (cars == null || cars.isEmpty()) {
        JOptionPane.showMessageDialog(
                this,
                "Car data is not available. Please check the data file.",
                "System Error",
                JOptionPane.ERROR_MESSAGE
        );
        return;
    }

    try {
        String passengersText = getRealText(passengersField, "e.g., 4");
        String daysText = getRealText(daysField, "e.g., 7");
        String mileageText = getRealText(mileageField, "e.g., 500");

         int passengers = Integer.parseInt(passengersText);
        int rentalDays = Integer.parseInt(daysText);
        double mileage = Double.parseDouble(mileageText);

        if (!areInputsFilled(passengersText, daysText, mileageText)) {
            JOptionPane.showMessageDialog(this, "Please fill in all fields.");
            return;
        }

        if (!areInputValuesValid(passengers, rentalDays, mileage)) {
            JOptionPane.showMessageDialog(this, "Please enter valid positive values.");
            return;
        }

        List<Car> suitableCars = getSuitableCars(cars, passengers);

        if (suitableCars.isEmpty()) {
            showNoCarMessage(passengers);
            return;
        }

        List<Car> cheapestCars = getCheapestCars(suitableCars, rentalDays, mileage);

        List<Car> bestCars = getBestComfortCars(cheapestCars);

        showResults(bestCars, rentalDays, mileage);

    } catch (NumberFormatException e) {
    JOptionPane.showMessageDialog(this,
            "Passengers, rental days, and mileage must be numeric values.");
} catch (IllegalArgumentException | IllegalStateException e) {
    JOptionPane.showMessageDialog(this,
            e.getMessage(),
            "Validation Error",
            JOptionPane.WARNING_MESSAGE);
}
}


    private void showEmptyResult() {
        resultPanel.removeAll();

        JPanel wrap = new JPanel(new GridBagLayout());
        wrap.setOpaque(false);

        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(220, 220, 220), 1, true),
                new EmptyBorder(18, 18, 18, 18)
        ));
        card.setPreferredSize(new Dimension(820, 70));

        JLabel label = new JLabel("No results yet. Fill in the form and click Find Best Options.");
        label.setFont(new Font("Arial", Font.PLAIN, 15));
        label.setForeground(new Color(120, 120, 120));
        label.setHorizontalAlignment(SwingConstants.CENTER);

        card.add(label, BorderLayout.CENTER);
        wrap.add(card);

        resultPanel.add(wrap);
        refreshResults();
    }

    private void showNoCarMessage(int passengers) {
        resultPanel.removeAll();

        JPanel wrap = new JPanel(new GridBagLayout());
        wrap.setOpaque(false);

        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(new Color(255, 248, 248));
        card.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(230, 180, 180), 1, true),
                new EmptyBorder(18, 18, 18, 18)
        ));
        card.setPreferredSize(new Dimension(820, 70));

        JLabel label = new JLabel("No available cars can fit " + passengers + " passengers.");
        label.setFont(new Font("Arial", Font.BOLD, 15));
        label.setForeground(new Color(180, 50, 50));
        label.setHorizontalAlignment(SwingConstants.CENTER);

        card.add(label, BorderLayout.CENTER);
        wrap.add(card);

        resultPanel.add(wrap);
        refreshResults();
    }

    private void showResults(List<Car> bestCars, int rentalDays, double mileage) {
        resultPanel.removeAll();

        JPanel headerWrap = new JPanel(new GridBagLayout());
        headerWrap.setOpaque(false);

        JPanel header = new JPanel();
        header.setLayout(new BoxLayout(header, BoxLayout.Y_AXIS));
        header.setOpaque(false);
        header.setPreferredSize(new Dimension(820, 60));

        JLabel title = new JLabel("Best Options Found");
        title.setFont(new Font("Arial", Font.BOLD, 26));
        title.setForeground(new Color(15, 23, 42));
        title.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel subtitle = new JLabel("We found the perfect car for your trip");
        subtitle.setFont(new Font("Arial", Font.PLAIN, 16));
        subtitle.setForeground(new Color(110, 110, 110));
        subtitle.setAlignmentX(Component.LEFT_ALIGNMENT);

        header.add(title);
        header.add(Box.createVerticalStrut(4));
        header.add(subtitle);

        headerWrap.add(header);
        resultPanel.add(headerWrap);
        resultPanel.add(Box.createVerticalStrut(15));

        for (Car car : bestCars) {
            JPanel cardWrap = new JPanel(new GridBagLayout());
            cardWrap.setOpaque(false);
            cardWrap.add(createResultCard(car, rentalDays, mileage));

            resultPanel.add(cardWrap);
            resultPanel.add(Box.createVerticalStrut(15));
        }

        refreshResults();
    }

    private JPanel createResultCard(Car car, int rentalDays, double mileage) {
        double rentalCost = car.calculateRentalCost(rentalDays);
        double gasCost = car.calculateGasCost(mileage, GAS_PRICE_PER_GALLON);
        double totalCost = car.calculateTotalCost(rentalDays, mileage, GAS_PRICE_PER_GALLON);

        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(220, 220, 220), 1, true),
                new EmptyBorder(22, 22, 22, 22)
        ));
        card.setPreferredSize(new Dimension(820, 390));

        JLabel carName = new JLabel(car.getMake() + " " + car.getModel());
        carName.setFont(new Font("Arial", Font.BOLD, 28));
        carName.setForeground(new Color(15, 23, 42));
        carName.setAlignmentX(Component.LEFT_ALIGNMENT);

        JPanel totalBox = new JPanel(new BorderLayout());
        totalBox.setBackground(new Color(249, 250, 252));
        totalBox.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(220, 220, 220), 2, true),
                new EmptyBorder(16, 16, 16, 16)
        ));
        totalBox.setAlignmentX(Component.LEFT_ALIGNMENT);
        totalBox.setMaximumSize(new Dimension(770, 75));
        totalBox.setPreferredSize(new Dimension(770, 75));

        JLabel totalText = new JLabel("Total Trip Cost");
        totalText.setFont(new Font("Arial", Font.BOLD, 20));
        totalText.setForeground(Color.BLACK);

      JLabel totalValue = new JLabel(String.format(java.util.Locale.US, "$ %.2f", totalCost));
        totalValue.setFont(new Font("Arial", Font.BOLD, 32));
        totalValue.setForeground(new Color(10, 20, 55));

        totalBox.add(totalText, BorderLayout.WEST);
        totalBox.add(totalValue, BorderLayout.EAST);

        card.add(carName);
card.add(Box.createVerticalStrut(18));
card.add(createInfoRow("Maximum Passengers", String.valueOf(car.getMaxPassengers())));
card.add(Box.createVerticalStrut(8));

if (hasAdminAccess()) {
    card.add(createInfoRow("Type", car.getType()));
    card.add(Box.createVerticalStrut(8));
    card.add(createInfoRow("Category", car.getCategory()));
    card.add(Box.createVerticalStrut(8));
    card.add(createInfoRow("Comfort Level", car.getComfort()));
    card.add(Box.createVerticalStrut(8));
    card.add(createInfoRow("Fuel Efficiency (MPG)", String.format(java.util.Locale.US, "%.0f", car.getMpg())));
    card.add(Box.createVerticalStrut(8));
    card.add(createInfoRow("Rental Cost", formatMoney(rentalCost)));
    card.add(Box.createVerticalStrut(8));
    card.add(createInfoRow("Gas Cost", formatMoney(gasCost)));
    card.add(Box.createVerticalStrut(20));
}

card.add(totalBox);

        return card;
    }

    private JPanel createInfoRow(String leftText, String rightText) {
        JPanel row = new JPanel(new BorderLayout());
        row.setOpaque(false);
        row.setMaximumSize(new Dimension(770, 25));
        row.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel left = new JLabel(leftText);
        left.setFont(new Font("Arial", Font.PLAIN, 17));
        left.setForeground(new Color(110, 110, 110));

        JLabel right = new JLabel(rightText);
        right.setFont(new Font("Arial", Font.BOLD, 17));
        right.setForeground(Color.BLACK);

        row.add(left, BorderLayout.WEST);
        row.add(right, BorderLayout.EAST);

        return row;
    }

   private String formatMoney(double value) {
    return String.format(java.util.Locale.US, "$ %.2f", value);
}

    private int getComfortRank(String comfort) {
    switch (comfort) {
        case "Poor": return 1;
        case "Medium": return 2;
        case "Good": return 3;
        default: return 0;
    }
}

    private JPanel createFieldSection(String labelText, JTextField field) {
        JPanel section = new JPanel();
        section.setLayout(new BoxLayout(section, BoxLayout.Y_AXIS));
        section.setBackground(Color.WHITE);
        section.setAlignmentX(Component.LEFT_ALIGNMENT);
        section.setMaximumSize(new Dimension(Integer.MAX_VALUE, 75));

        JLabel label = new JLabel(labelText);
        label.setFont(new Font("Arial", Font.BOLD, 18));
        label.setForeground(Color.BLACK);
        label.setAlignmentX(Component.LEFT_ALIGNMENT);

        field.setAlignmentX(Component.LEFT_ALIGNMENT);
        field.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));

        section.add(label);
        section.add(Box.createVerticalStrut(8));
        section.add(field);

        return section;
    }

    private JTextField createStyledField(String placeholder) {
        JTextField textField = new JTextField(placeholder);
        textField.setFont(new Font("Arial", Font.PLAIN, 15));
        textField.setForeground(new Color(130, 130, 130));
        textField.setBackground(new Color(245, 245, 247));
        textField.setBorder(new EmptyBorder(10, 12, 10, 12));
        textField.setPreferredSize(new Dimension(760, 40));

        textField.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                if (textField.getText().equals(placeholder)) {
                    textField.setText("");
                    textField.setForeground(Color.BLACK);
                }
            }

            public void focusLost(java.awt.event.FocusEvent evt) {
                if (textField.getText().trim().isEmpty()) {
                    textField.setText(placeholder);
                    textField.setForeground(new Color(130, 130, 130));
                }
            }
        });

        return textField;
    }

    private String getRealText(JTextField field, String placeholder) {
        String value = field.getText().trim();
        if (value.equals(placeholder)) {
            return "";
        }
        return value;
    }

    private void resetField(JTextField field, String placeholder) {
        field.setText(placeholder);
        field.setForeground(new Color(130, 130, 130));
    }

    private void refreshResults() {
        resultPanel.revalidate();
        resultPanel.repaint();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new RentalForm().setVisible(true);
        });
    }
}