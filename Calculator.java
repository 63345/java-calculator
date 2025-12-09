import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Calculator extends JFrame implements ActionListener {
    private JTextField display;
    private StringBuilder input;

    public Calculator() {
        input = new StringBuilder();
        setTitle("Java 简易计算器");
        setSize(300, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        setLocationRelativeTo(null);

        display = new JTextField();
        display.setEditable(false);
        display.setFont(new Font("Arial", Font.PLAIN, 24));
        display.setHorizontalAlignment(JTextField.RIGHT);
        display.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        add(display, BorderLayout.NORTH);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(4, 4, 5, 5));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        String[] buttons = {
            "7", "8", "9", "/",
            "4", "5", "6", "*",
            "1", "2", "3", "-",
            "0", "C", "=", "+"
        };

        for (String text : buttons) {
            JButton button = new JButton(text);
            button.setFont(new Font("Arial", Font.PLAIN, 20));
            button.addActionListener(this);
            button.setFocusPainted(false);
            button.setBorder(BorderFactory.createRaisedBevelBorder());
            if (text.matches("[+\\-*/=C]")) {
                button.setBackground(new Color(240, 240, 240));
            }
            panel.add(button);
        }

        add(panel, BorderLayout.CENTER);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand();

        if (command.equals("C")) {
            input.setLength(0);
            display.setText("");
        } else if (command.equals("=")) {
            try {
                String expr = input.toString().trim();
                if (expr.isEmpty()) {
                    display.setText("");
                    return;
                }
                if (expr.matches(".*[+\\-*/]$")) {
                    expr = expr.substring(0, expr.length() - 1);
                }
                double result = evaluate(expr);
                display.setText(result == (long) result ?
                    String.valueOf((long) result) : String.valueOf(result));
                input.setLength(0);
            } catch (Exception ex) {
                display.setText("Error");
                input.setLength(0);
            }
        } else {
            if (command.matches("[+\\-*/]") && input.length() > 0) {
                char lastChar = input.charAt(input.length() - 1);
                if (lastChar == '+' || lastChar == '-' || lastChar == '*' || lastChar == '/') {
                    input.setCharAt(input.length() - 1, command.charAt(0));
                } else {
                    input.append(command);
                }
            } else {
                input.append(command);
            }
            display.setText(input.toString());
        }
    }

    // evaluate方法必须在Calculator类内部！
    private double evaluate(String expr) throws Exception {
        try {
            return (double) new javax.script.ScriptEngineManager()
                    .getEngineByName("nashorn")
                    .eval(expr);
        } catch (Exception e) {
            double result = 0;
            char op = '+';
            StringBuilder num = new StringBuilder();
            for (int i = 0; i < expr.length(); i++) {
                char c = expr.charAt(i);
                if (Character.isDigit(c) || c == '.') {
                    num.append(c);
                } else {
                    double n = Double.parseDouble(num.toString());
                    result = calculate(result, n, op);
                    op = c;
                    num.setLength(0);
                }
            }
            double n = Double.parseDouble(num.toString());
            result = calculate(result, n, op);
            return result;
        }
    }

    // calculate辅助方法也必须在类内部！
    private double calculate(double a, double b, char op) {
        switch (op) {
            case '+': return a + b;
            case '-': return a - b;
            case '*': return a * b;
            case '/':
                if (b == 0) throw new ArithmeticException("除数不能为0");
                return a / b;
            default: return 0;
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Calculator calculator = new Calculator();
            calculator.setVisible(true);
        });
    }
}

