import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Introduction extends JFrame {
    private JList list1;
    private JButton proceedButton;
    private JPanel IntroPanel;

    Introduction(){
        setTitle("Introduction");
        setContentPane(IntroPanel);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setSize(500,300);
        proceedButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new Registration().setVisible(true);
                dispose();
            }
        });
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new Introduction().setVisible(true);
            }
        });
    }
}
