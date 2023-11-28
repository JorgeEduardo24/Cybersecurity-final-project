package ui;

import java.awt.Color;
import java.awt.Font;
import java.awt.Image;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.border.LineBorder;
import model.CipherManager;

public class MainUI {
    private static final String LOGO_PATH = "src/main/java/images/logo.png";
    private static final String FG_LOGO_PATH = "src/main/java/images/fg.png";

    private JButton startButton;
    private JTextField filePathTextField;
    private JPasswordField passwordTextField;
    private JButton browseButton;
    private JLabel labelFile;
    private JLabel labelPassword;
    private JLabel imagen;
    private JLabel slogan;
    private ButtonGroup groupOptions;
    private JRadioButton optionDecript;
    private JRadioButton optionEncript;
    private Image logoFG;
    private JFileChooser fileChooser;

    public MainUI() {
        initComponents();
        createUI();
        addActionListeners();   
    }

    private void initComponents() {
        startButton = new JButton("Comenzar");
        browseButton = new JButton("Buscar");
        labelFile = new JLabel("Archivo: ");
        labelPassword = new JLabel("Contraseña: ");
        filePathTextField = new JTextField();
        passwordTextField = new JPasswordField();
        imagen = new JLabel(new ImageIcon(LOGO_PATH));
        groupOptions = new ButtonGroup();
        slogan = new JLabel("\"Archivo seguro, mente tranquila: Encripta el presente, protege tu futuro.\"");
        logoFG = new ImageIcon(FG_LOGO_PATH).getImage();

        // CORNER BUTTONS
        startButton.setBorder(new LineBorder(Color.WHITE, 2));
        browseButton.setBorder(new LineBorder(Color.WHITE, 2));
        filePathTextField.setBorder(new LineBorder(Color.WHITE, 2));
        passwordTextField.setBorder(new LineBorder(Color.WHITE, 2));

        // Labels
        labelFile.setOpaque(true);
        labelFile.setFont(new Font("rockwell", Font.PLAIN, 12));
        labelFile.setForeground(Color.WHITE);
        labelFile.setBackground(Color.DARK_GRAY);
        labelFile.setBounds(150, 150, 70, 20);
        labelPassword.setOpaque(true);
        labelPassword.setFont(new Font("rockwell", Font.PLAIN, 12));
        labelPassword.setForeground(Color.WHITE);
        labelPassword.setBackground(Color.DARK_GRAY);
        labelPassword.setBounds(150, 175, 80, 20);
        slogan.setOpaque(true);
        slogan.setFont(new Font("rockwell", Font.PLAIN, 12));
        slogan.setForeground(Color.WHITE);
        slogan.setBackground(Color.DARK_GRAY);
        slogan.setBounds(180, 93, 450, 20);

        // TextFields
        filePathTextField.setBounds(235, 150, 300, 20);
        filePathTextField.setFont(new Font("rockwell", Font.PLAIN, 12));
        filePathTextField.setBackground(Color.DARK_GRAY);
        filePathTextField.setForeground(Color.WHITE);
        filePathTextField.setEditable(false);// Deshabilitar la edición
        passwordTextField.setBounds(235, 175, 300, 20);
        passwordTextField.setFont(new Font("rockwell", Font.PLAIN, 12));
        passwordTextField.setBackground(Color.DARK_GRAY);
        passwordTextField.setForeground(Color.WHITE);

        // Buttons
        browseButton.setBackground(Color.DARK_GRAY);
        browseButton.setBounds(540, 150, 80, 20);
        browseButton.setForeground(Color.WHITE);
        browseButton.setFont(new Font("rockwell", Font.PLAIN, 12));
        startButton.setBackground(Color.DARK_GRAY);
        startButton.setBounds(540, 175, 80, 20);
        startButton.setForeground(Color.WHITE);
        startButton.setFont(new Font("rockwell", Font.PLAIN, 12));

        optionEncript = new JRadioButton("Cifrar ", true);
        optionEncript.setBounds(150, 200, 100, 30);
        optionEncript.setBackground(Color.DARK_GRAY);
        optionEncript.setForeground(Color.WHITE);

        optionDecript = new JRadioButton("Descifrar ", false);
        optionDecript.setBounds(150, 230, 100, 30);
        optionDecript.setBackground(Color.DARK_GRAY);
        optionDecript.setForeground(Color.WHITE);

        // ADD TO GROUP OF BUTTONS
        groupOptions.add(optionEncript);
        groupOptions.add(optionDecript);

        // Images
        imagen.setBounds(125, 20, 525, 71);
    }

    private void createUI() {
        JFrame frame = new JFrame("File Guard");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 330);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.setBackground(Color.DARK_GRAY);
        frame.setIconImage(logoFG);

        JPanel panel = new JPanel();
        panel.setLayout(null);
        panel.setSize(800, 330);
        panel.setBackground(Color.DARK_GRAY);

        // ADD TO PANEL
        panel.add(imagen);
        panel.add(labelFile);
        panel.add(labelPassword);
        panel.add(filePathTextField);
        panel.add(passwordTextField);
        panel.add(startButton);
        panel.add(browseButton);
        panel.add(optionDecript);
        panel.add(optionEncript);
        panel.add(slogan);

        // ADD CONTENT OF PANEL TO FRAME
        frame.getContentPane().add(panel);
        frame.setVisible(true);
    }

    private void addActionListeners() {
        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleStartButtonAction();
            }
        });

        browseButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleBrowseButtonAction();
            }
        });
    }

    private void handleStartButtonAction() {
        // Lógica del botón "Comenzar"
        if (filePathTextField.getText().isEmpty() || passwordTextField.getText().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Por favor, rellena todos los campos.", "Error", JOptionPane.ERROR_MESSAGE);
        } else {
            String filePath = filePathTextField.getText();
            String password = passwordTextField.getText();
            CipherManager cipherManager = new CipherManager();

            try {
                if (optionEncript.isSelected()) {
                    cipherManager.encryptFile(filePath, password);
                    JOptionPane.showMessageDialog(null, "Cifrado completado exitosamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    cipherManager.decryptFile(filePath, password);
                    JOptionPane.showMessageDialog(null, "Descifrado completado exitosamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
            }
        }
    }

    private void handleBrowseButtonAction() {
        // Lógica del botón "Buscar"
        fileChooser = new JFileChooser();
        int result = fileChooser.showOpenDialog(null);

        if (result == JFileChooser.APPROVE_OPTION) {
            filePathTextField.setText(fileChooser.getSelectedFile().getAbsolutePath());
        } else {
            JOptionPane.showMessageDialog(null, "No se ha seleccionado ningun archivo", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new MainUI();
            }
        });
    }
}
