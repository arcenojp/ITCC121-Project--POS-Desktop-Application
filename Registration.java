import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Registration extends JFrame {

    private JPanel panel1;
    private JButton LoginButton;
    private JTextField usernameField;
    private JPasswordField passwordField;
    Registration(){
        setTitle("Login Form");
        setContentPane(panel1);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setSize(400,400);

        LoginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = usernameField.getText();
                String password = new String(passwordField.getPassword());
                if (username.equals("admin") && password.equals("password123")) {
                    JOptionPane.showMessageDialog(Registration.this,
                            "Login Successful!",
                            "Success",
                            JOptionPane.INFORMATION_MESSAGE);
                    new Inventory().setVisible(true);
                    dispose();

                } else {
                    JOptionPane.showMessageDialog(Registration.this,
                            "Invalid username or password",
                            "Error",JOptionPane.INFORMATION_MESSAGE);
                }
            }});
    }
        public static void main(String[] args) {
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    new Registration().setVisible(true);
                }
            });
        }
    }
