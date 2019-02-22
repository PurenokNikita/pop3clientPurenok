package view;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

public class LoginDialog extends JDialog {
    private final JPanel contentPanel = new JPanel();
    private JTextField address_text;
    private JTextField pop_addr_text;
    private JPasswordField pass_text;
    ActionListener btnActionListener = new ActionListener() {
        public void actionPerformed(ActionEvent e) {
            String var2;
            switch((var2 = e.getActionCommand()).hashCode()) {
                case 2524:
                    if (var2.equals("OK")) {
                        String addr = LoginDialog.this.address_text.getText();
                        String pass = String.valueOf(LoginDialog.this.pass_text.getPassword());
                        String pop_addr = LoginDialog.this.pop_addr_text.getText();
                        new MainFrame(addr, pass, pop_addr, LoginDialog.this);
                    }
                default:
            }
        }
    };

    public static void main(String[] args) {
        try {
            LoginDialog dialog = new LoginDialog();
            dialog.setDefaultCloseOperation(2);
            dialog.setVisible(true);
        } catch (Exception var2) {
            var2.printStackTrace();
        }

    }

    public LoginDialog() {
        this.setLocationRelativeTo(null);
        this.setTitle("Login");
        this.setBounds(100, 100, 450, 226);
        this.getContentPane().setLayout(new BorderLayout());
        this.contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
        this.getContentPane().add(this.contentPanel, "Center");
        this.contentPanel.setLayout((LayoutManager)null);
        JLabel lblNewLabel = new JLabel("address：");
        lblNewLabel.setBounds(58, 16, 86, 18);
        this.contentPanel.add(lblNewLabel);
        JLabel label = new JLabel("password：");
        label.setBounds(58, 47, 86, 18);
        this.contentPanel.add(label);
        JLabel lblPop = new JLabel("POP3：");
        lblPop.setBounds(58, 78, 86, 18);
        this.contentPanel.add(lblPop);
        this.address_text = new JTextField();
        this.address_text.setText("TestBoxPure@mail.ru");
        this.address_text.setBounds(147, 13, 201, 24);
        this.contentPanel.add(this.address_text);
        this.address_text.setColumns(10);
        this.pop_addr_text = new JTextField();
        this.pop_addr_text.setText("pop.mail.ru");
        this.pop_addr_text.setColumns(10);
        this.pop_addr_text.setBounds(147, 75, 201, 24);
        this.contentPanel.add(this.pop_addr_text);
        this.pass_text = new JPasswordField();
        this.pass_text.setText("purenoknikita13");
        this.pass_text.setBounds(147, 44, 201, 24);
        this.contentPanel.add(this.pass_text);
        JButton okButton = new JButton("Login");
        this.contentPanel.add(okButton);
        okButton.setActionCommand("OK");
        okButton.addActionListener(this.btnActionListener);
        okButton.setBounds(170,120,100,30);
        this.getRootPane().setDefaultButton(okButton);
    }
}
