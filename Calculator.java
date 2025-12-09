import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

// 简单Java计算器（Swing图形界面，可直接运行）
public class Calculator extends JFrame implements ActionListener {
    private JTextField display;
    private StringBuilder input;

    public Calculator() {
        // 初始化变量
        input = new StringBuilder();

        // 设置窗口属性
        setTitle("Java 简易计算器");
        setSize(300, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // 创建显示框
        display = new JTextField();
        display.setEditable(false);
        display.setFont(new Font("Arial", Font.PLAIN, 24));
        add(display, BorderLayout.NORTH);

        // 创建按钮面板
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(4, 4));

        // 按钮文本
        String[] buttons = {
            "7", "8", "9", "/",
            "4", "5", "6", "*",
            "1", "2", "3", "-",
            "0", "C", "=", "+"
        };

        // 添加按钮到面板
        for (String text : buttons) {
            JButton button = new JButton(text);
            button.setFont(new Font("Arial", Font.PLAIN, 20));
            button.addActionListener(this);
            panel.add(button);
        }

        add(panel, BorderLayout.CENTER);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand();

        if (command.charAt(0) == 'C') {
            // 清空输入
            input.setLength(0);
            display.setText("");
        } else if (command.charAt(0) == '=') {
            // 计算结果
            try {
                double result = evaluate(input.toString());
                display.setText(String.valueOf(result));
                input.setLength(0);
            } catch (Exception ex) {
                display.setText("Error");
            }
        } else {
            // 追加输入
            input.append(command);
            display.setText(input.toString());
        }
    }

    // 简单表达式计算（支持加减乘除）
    private double evaluate(String expr) throws Exception {
        return (double) new javax.script.ScriptEngineManager()
                .getEngineByName("JavaScript")
                .eval(expr);
    }

    public static void main(String[] args) {
        // 启动计算器（Swing需在EDT线程运行）
        SwingUtilities.invokeLater(() -> {
            Calculator calculator = new Calculator();
            calculator.setVisible(true);
        });
    }
}

