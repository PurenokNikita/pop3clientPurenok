package view;

import controller.PopController;

import java.awt.Font;
import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Vector;
import javax.swing.JButton;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;
import model.Mail;

public class MainFrame extends JFrame {
    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JTextField addrs_text;
    private JTextField subject_text;
    private JTextArea textArea;
    private JScrollPane scrollPane_text;
    private JScrollPane scrollPane_table;
    private JTable table;
    private JButton btn_back;
    private JButton btn_save;
    private JButton btn_delete;
    private JEditorPane textArea_emailbox;
    private JButton btnSend;
    private String addr;
    private String pass;
    private String pop_addr;
    private int curMailIndex;
    private Vector<Mail> mails = new Vector();
    private PopController popController;
    private LoginDialog loginDialog;
    private ActionListener listener = new ActionListener() {
        public void actionPerformed(ActionEvent e) {
            String var2;
            switch((var2 = e.getActionCommand()).hashCode()) {
                case -1335458389:
                    if (var2.equals("delete")) {
                        MainFrame.this.doDelete();
                    }
                    break;

                case 3015911:
                    if (var2.equals("back")) {
                        MainFrame.this.setTableVisible();
                    }
                    break;
                case 3522941:
                    if (var2.equals("save")) {
                        MainFrame.this.doSave();
                    }
                    break;
//                case 108401386:
//                    if (var2.equals("reply")) {
//                        MainFrame.this.doReply();
//                    }
            }

        }
    };

    public MainFrame(String addr, String pass, String pop_addr, LoginDialog loginDialog) {
        this.setResizable(false);
        this.loginDialog = loginDialog;
        this.addr = addr;
        this.pass = pass;
        this.pop_addr = pop_addr;
        this.setTitle("MAIL CLIENT");
        Font font = new Font("Verdana", 0, 17);
        this.setType(Type.POPUP);
        this.setDefaultCloseOperation(3);
        this.setBounds(100, 100, 837, 585);
        this.contentPane = new JPanel();
        this.contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        this.setContentPane(this.contentPane);
        this.contentPane.setLayout((LayoutManager)null);
        JPanel panel_box = new JPanel();
        panel_box.setBounds(0,0,937, 585);
        this.contentPane.add(panel_box);
        this.subject_text = new JTextField();
        this.subject_text.setToolTipText("");
        this.subject_text.setFont(new Font("Verdana", 0, 14));
        this.subject_text.setColumns(10);
        this.subject_text.setBounds(137, 44, 412, 24);
        panel_box.setLayout((LayoutManager)null);
        this.textArea_emailbox = new JEditorPane();
        this.textArea_emailbox.setBounds(96, 0, 578, 500);
        this.scrollPane_text = new JScrollPane(this.textArea_emailbox);
        this.scrollPane_text.setBounds(0, 0, 777, 500);
        panel_box.add(this.scrollPane_text);
        this.btn_back = new JButton("Назад");
        this.btn_back.setActionCommand("back");
        this.btn_back.addActionListener(this.listener);
        this.btn_back.setBounds(525, 513, 93, 27);
        panel_box.add(this.btn_back);
        this.btn_save = new JButton("Сохранить");
        this.btn_save.setActionCommand("save");
        this.btn_save.addActionListener(this.listener);
        this.btn_save.setBounds(311, 513, 93, 27);
        panel_box.add(this.btn_save);
        this.btn_delete = new JButton("Удалить");
        this.btn_delete.setActionCommand("delete");
        this.btn_delete.addActionListener(this.listener);
        this.btn_delete.setBounds(418, 513, 93, 27);
        panel_box.add(this.btn_delete);
        this.scrollPane_table = new JScrollPane();
        this.scrollPane_table.setBounds(0, 0, 837, 550);
        panel_box.add(this.scrollPane_table);
        this.table = new JTable();
        this.scrollPane_table.setViewportView(this.table);
        this.setTableVisible();
        (new Thread(new Runnable() {
            public void run() {
                MainFrame.this.initPop();
            }
        })).start();
    }

    private void setEmailContentVisible() {
        this.btn_back.setVisible(true);
        this.btn_save.setVisible(true);
        this.btn_delete.setVisible(true);
//        this.btn_reply.setVisible(true);
        this.scrollPane_text.setVisible(true);
        this.scrollPane_table.setVisible(false);
    }

    private void setTableVisible() {
        this.btn_back.setVisible(false);
        this.btn_save.setVisible(false);
        this.btn_delete.setVisible(false);
        this.scrollPane_text.setVisible(false);
        this.scrollPane_table.setVisible(true);
    }

    private void initPop() {
        this.popController = new PopController(this.pop_addr, this.addr, this.pass, this);
        int count = this.popController.getMailCount();
        if (count == -1) {
            this.showMyDialog("Ошибка входа!");
            this.setVisible(false);
        } else {
            this.loginDialog.setVisible(false);
            this.setVisible(true);
        }

        this.mails = new Vector();

        for(int i =1; i < 2; ++i) {
            Mail mail = new Mail(this.popController.getItemString(String.valueOf(i)));
            this.mails.add(mail);
        }

        TableModel model = new TableModel() {
            public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
            }

            public void removeTableModelListener(TableModelListener l) {
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return false;
            }

            public Object getValueAt(int rowIndex, int columnIndex) {
                return columnIndex == 0 ? (MainFrame.this.mails.get(rowIndex)).getFrom() : (MainFrame.this.mails.get(rowIndex)).getSubject();

            }



            public int getRowCount() {
                return MainFrame.this.mails.size();
            }

            public String getColumnName(int columnIndex) {
                switch(columnIndex) {
                    case 0:
                        return "Отправитель";
                    case 1:
                        return "Заголовок";
                    default:
                        return null;
                }
            }

            public int getColumnCount() {
                return 2;
            }

            public Class<?> getColumnClass(int columnIndex) {
                return String.class;
            }

            public void addTableModelListener(TableModelListener l) {
            }
        };
        this.table.setModel(model);
        this.table.setRowHeight(35);
        this.table.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    int row = ((JTable)e.getSource()).rowAtPoint(e.getPoint());
                    MainFrame.this.curMailIndex = row;
                    MainFrame.this.textArea_emailbox.setText(((Mail)MainFrame.this.mails.get(row)).toString());
                    MainFrame.this.setEmailContentVisible();
                }

            }
        });
    }


    protected void doSave() {

        String fileName = "mail_from_" + ((Mail)this.mails.get(this.curMailIndex)).getFrom() + ".txt";
        File file = new File(fileName);

        try {
            file.createNewFile();
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            fileOutputStream.write(((Mail)this.mails.get(this.curMailIndex)).toString().getBytes());
            fileOutputStream.close();
        } catch (IOException var4) {
            var4.printStackTrace();
        }

        this.showMyDialog("Сохранено в файл:" + file.getAbsolutePath());
    }




    protected void doDelete() {
        if (this.popController.deleteItem(String.valueOf(this.curMailIndex))) {
            this.showMyDialog("Удалено！");
            this.setTableVisible();
            this.initPop();
        } else {
            this.showMyDialog("Удалить не удалось！");
        }

    }

    private void enableBtn() {
        this.btnSend.setEnabled(true);
        this.btnSend.setText("Отправить");
    }

    public void showMyDialog(String msg) {
        JOptionPane.showMessageDialog(this, msg);
    }
}
