/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GUI;

import BUS.bangChamCongBUS;
import BUS.chiTietBangChamCongBUS;
import DTO.bangChamCongDTO;
import DTO.chiTietBangChamCongDTO;
import GUI.model.DateValidator;
import GUI.model.DateValidatorUsingDateFormat;
import GUI.model.header;
import GUI.model.headerSuggest;
import GUI.model.navItem;
import GUI.model.pnHHMMSS;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import javax.swing.*;
import static javax.swing.BorderFactory.createLineBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import java.util.Calendar;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jdesktop.swingx.JXDatePicker;

/**
 *
 * @author MaiThy
 */
public class chiTietBangChamCongGUI extends JDialog {

    DateValidator validator = new DateValidatorUsingDateFormat("yyyy-MM-dd");
    DateValidator validatorHour = new DateValidatorUsingDateFormat("HH:mm:ss");
    private chiTietBangChamCongBUS ctbccBUS = new chiTietBangChamCongBUS();
    private bangChamCongBUS bccBUS = new bangChamCongBUS();
    private int DEFALUT_WIDTH;
    private JPanel header, pnDisplay, pnEast, pnOption, pnFind, pnTable;
    JLabel lbAdd, lbEdit, lbRemove, btnConfirm, btnBack, lbExit;
    private JTable tbChiTietBangChamCong;
    private DefaultTableModel model;
    private boolean EditOrAdd = true;
    JTextField[] txtChiTietBangChamCong;
    JLabel[] lbChiTietBangChamCong;
    Font font;
    TableRowSorter<TableModel> rowSorter;
    String mabangchamcong, manhanvien, thoigian;
    JXDatePicker picker;
    JXDatePicker[] HourPicker;

    public chiTietBangChamCongGUI(String mabangchamcong, String manhanvien, String thoigian) {
        this.mabangchamcong = mabangchamcong;
        this.manhanvien = manhanvien;
        this.thoigian = thoigian;
        init();
    }

    public void init() {

        font = new Font("Segoe UI", Font.BOLD, 18);
        setLayout(new BorderLayout(10, 10));
        setBackground(null);
        setBounds(new Rectangle(10, 10, 1300, 730));
        setLocationRelativeTo(null);
        pnDisplay();
        pnEast();
        pnOption();

        pnTable();
        tbChiTietBangChamCong.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int i = tbChiTietBangChamCong.getSelectedRow();
//                SET TEXT DATE
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
                String dateInString = tbChiTietBangChamCong.getModel().getValueAt(i, 3).toString();
                Date date = null;
                try {
                    date = formatter.parse(dateInString);
                } catch (ParseException ex) {
                    Logger.getLogger(NhanVienGUI.class.getName()).log(Level.SEVERE, null, ex);
                }
                picker.setDate(date);
//----------------------------------------------------------------------------      
                txtChiTietBangChamCong[1].setText(tbChiTietBangChamCong.getModel().getValueAt(i, 2).toString());
                txtChiTietBangChamCong[2].setText(tbChiTietBangChamCong.getModel().getValueAt(i, 4).toString());
                txtChiTietBangChamCong[3].setText(tbChiTietBangChamCong.getModel().getValueAt(i, 5).toString());
            }
        });

//      HEADER
        header = new JPanel(null);
        header.setBackground(new Color(27, 27, 30));
        header.setPreferredSize(new Dimension(1300, 40));

        headerSuggest headerSuggest = new headerSuggest("B???NG CH???M C??NG " + manhanvien + " " + thoigian, 1300, 40);
        header.add(headerSuggest);
//  
        this.add(header, BorderLayout.NORTH);
        this.add(pnDisplay, BorderLayout.WEST);
        this.add(pnOption, BorderLayout.CENTER);
        this.add(pnEast, BorderLayout.EAST);
        this.add(pnTable, BorderLayout.SOUTH);
        setUndecorated(true);
        this.setVisible(true);
    }

    public JPanel pnDisplay() {
        pnDisplay = new JPanel();
        pnDisplay.setLayout(null);
        pnDisplay.setPreferredSize(new Dimension((1300 / 2) - 20, 300));
        pnDisplay.setBackground(Color.pink);
        String[] arrChiTietBangChamCong = {"Ng??y", "T??n nh??n vi??n", "Gi??? v??o", "Gi??? ra"};
        txtChiTietBangChamCong = new JTextField[arrChiTietBangChamCong.length];
        lbChiTietBangChamCong = new JLabel[arrChiTietBangChamCong.length];
        HourPicker = new JXDatePicker[arrChiTietBangChamCong.length];
        int xLb = 50, yLb = 40;
        int xTxt = 320, yTxt = 40;
        for (int i = 0; i < arrChiTietBangChamCong.length; i++) {
            lbChiTietBangChamCong[i] = new JLabel(arrChiTietBangChamCong[i]);
            lbChiTietBangChamCong[i].setBounds(xLb, yLb, 250, 30);
            lbChiTietBangChamCong[i].setHorizontalAlignment(JLabel.CENTER);
            lbChiTietBangChamCong[i].setFont(font);
            lbChiTietBangChamCong[i].setName("lb" + i);
            pnDisplay.add(lbChiTietBangChamCong[i]);
            yLb = yLb + 45;
            if (i == 0) {
                picker = new JXDatePicker();
                picker.setFormats(new SimpleDateFormat("yyyy-MM-dd"));
                picker.setBounds(xTxt, yTxt, 230, 30);
                pnDisplay.add(picker);
                yTxt = yTxt + 45;
            } else {
                txtChiTietBangChamCong[i] = new JTextField();
                txtChiTietBangChamCong[i].setName("txt" + i);
                txtChiTietBangChamCong[i].setBounds(xTxt, yTxt, 230, 30);
                pnDisplay.add(txtChiTietBangChamCong[i]);
                yTxt = yTxt + 45;
            }
        }
        txtChiTietBangChamCong[2].setText("HH:mm:ss");
        txtChiTietBangChamCong[3].setText("HH:mm:ss");
        return pnDisplay;
    }

    public JPanel pnEast() {
        pnEast = new JPanel();
        pnEast.setLayout(new FlowLayout(1, 0, 40));
        pnEast.setBackground(Color.pink);
        pnEast.setPreferredSize(new Dimension(320, 200));
        lbExit = new JLabel(new ImageIcon("./src/img/Close_150px.png"), JLabel.CENTER);
        lbExit.setPreferredSize(new Dimension(150, 150));
        lbExit.setCursor(new Cursor(Cursor.HAND_CURSOR));
        lbExit.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                dispose();
            }
        });
        pnEast.add(lbExit);
        return pnEast;
    }

    public JPanel pnOption() {
        pnOption = new JPanel();
        pnOption.setLayout(null);
        pnOption.setBackground(Color.PINK);
        pnOption.setPreferredSize(new Dimension(320, 200));
        lbEdit = new JLabel(new ImageIcon("./src/img/chamcong.png"), JLabel.CENTER);
        lbEdit.setCursor(new Cursor(Cursor.HAND_CURSOR));
        lbEdit.setBounds(60, 100, 200, 70);
        pnOption.add(lbEdit);
        btnConfirm = new JLabel(new ImageIcon("./src/img/done.png"));
        btnConfirm.setVisible(false);
        btnConfirm.setBounds(80, 40, 155, 100);
        btnConfirm.setCursor(new Cursor(Cursor.HAND_CURSOR));

        btnBack = new JLabel(new ImageIcon("./src/img/back.png"));
        btnBack.setVisible(false);
        btnBack.setBounds(80, 120, 155, 100);
        btnBack.setCursor(new Cursor(Cursor.HAND_CURSOR));

        pnOption.add(btnConfirm);
        pnOption.add(btnBack);

        // MouseClick btnEdit
        lbEdit.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {

                if (picker.getEditor().getText().equals("")) {
                    JOptionPane.showMessageDialog(null, "Vui l??ng ch???n chi ti???t b???ng ch???m c??ng c???n s???a!");
                    return;
                }
                picker.setEditable(false);
                txtChiTietBangChamCong[1].setEditable(false);
                lbEdit.setVisible(false);

                btnConfirm.setVisible(true);
                btnBack.setVisible(true);
                tbChiTietBangChamCong.setEnabled(false);
            }
        });
        //MouseClick btnBack
        btnBack.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                cleanView();
                txtChiTietBangChamCong[2].setText("HH:mm:ss");
                txtChiTietBangChamCong[3].setText("HH:mm:ss");

                lbEdit.setVisible(true);
                txtChiTietBangChamCong[1].setEditable(true);
                btnConfirm.setVisible(false);
                btnBack.setVisible(false);
//                btnFile.setVisible(false);

                tbChiTietBangChamCong.setEnabled(true);
            }
        });

        //MouseClick btnConfirm
        btnConfirm.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int i;

                i = JOptionPane.showConfirmDialog(null, "X??c nh???n s???a chi ti???t b???ng ch???m c??ng", "", JOptionPane.YES_NO_OPTION);
                if (i == 0) {
                    //L???y d??? li???u t??? TextField
                    String ngay = picker.getEditor().getText();
                    String giovao = txtChiTietBangChamCong[2].getText();
                    String giora = txtChiTietBangChamCong[3].getText();
                    if (validateBangChamCong(ngay, giovao, giora) == false) {
                        return;
                    }
                    //Upload s???n ph???m l??n DAO v?? BUS
                    chiTietBangChamCongDTO ctbcc = new chiTietBangChamCongDTO(mabangchamcong, ngay, giovao, giora);
                    ctbccBUS.setChiTietBangChamCong(ctbcc);
                    SetBangChamCong();
                    outModel(model, ctbccBUS.getList(mabangchamcong));// Load l???i table

//                        saveIMG();// L??u h??nh ???nh 
                    JOptionPane.showMessageDialog(null, "S???a th??nh c??ng", "Th??nh c??ng", JOptionPane.INFORMATION_MESSAGE);
                    tbChiTietBangChamCong.setEnabled(true);
                }

            }
        });
        return pnOption;
    }

    public void SetBangChamCong() {
        float sogiotre = bccBUS.soGioTre(manhanvien, mabangchamcong, null);
        float sogiolamthem = bccBUS.soGioLamThem(manhanvien, mabangchamcong, null);
        bangChamCongDTO bcc = new bangChamCongDTO(mabangchamcong, manhanvien, thoigian, sogiotre, sogiolamthem);
        bccBUS.listBangChamCong(manhanvien);
        bccBUS.setBangChamCong(bcc);
    }

    public JPanel pnFind() {
        /**
         * ********************* SORT TABLE ****************************
         */
        pnFind = new JPanel(new FlowLayout());
        pnFind.setPreferredSize(new Dimension(200, 100));
//        pnFind.setBounds(new Rectangle(50, 120, 300, 30));
        pnFind.setBorder(createLineBorder(Color.BLACK)); //Ch???nh vi???n 
        //PH???N CH???N SEARCH
        JComboBox cmbChoice = new JComboBox();
        cmbChoice.setEditable(true);
        cmbChoice.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        cmbChoice.addItem("M?? b???ng ch???m c??ng");
        cmbChoice.addItem("M?? nh??n vi??n");
        cmbChoice.addItem("T??n nh??n vi??n");
        cmbChoice.setPreferredSize(new Dimension(120, 30));
//        cmbChoice.setBounds(new Rectangle(70,0,120,30));

        //Ph???n TextField
        JTextField txtSearch = new JTextField();
        txtSearch.setPreferredSize(new Dimension(275, 30));
//        txtSearch.setBounds(new Rectangle(195, 0, 275, 30));
        txtSearch.setBorder(null);
        txtSearch.setOpaque(false);
        txtSearch.setFont(new Font("Segoe UI", Font.PLAIN, 15));

        // Custem Icon search
        JLabel searchIcon = new JLabel(new ImageIcon("./src/img/search_24px.png"));
        searchIcon.setPreferredSize(new Dimension(50, 30));
        searchIcon.setBounds(new Rectangle(345, -10, 50, 50));
        searchIcon.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Add t???t c??? v??o search box
        pnFind.add(cmbChoice);
        pnFind.add(txtSearch);
        pnFind.add(searchIcon);
        txtSearch.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                searchIcon.setIcon(new ImageIcon("./src/img/search_24px.png")); //?????i m??u icon
                pnFind.setBorder(createLineBorder(new Color(52, 152, 219))); // ?????i m??u vi???n 
            }

            public void focusLost(FocusEvent e) //Tr??? v??? nh?? c??
            {
                searchIcon.setIcon(new ImageIcon("./src/img/search_24px.png"));
                pnFind.setBorder(createLineBorder(Color.BLACK));
            }
        });
        txtSearch.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                String text = txtSearch.getText();
                int choice = cmbChoice.getSelectedIndex();

                if (text.trim().length() == 0) {
                    rowSorter.setRowFilter(null);
                } else {
                    rowSorter.setRowFilter(RowFilter.regexFilter("(?i)" + text + "", choice));
                }
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                String text = txtSearch.getText();
                int choice = cmbChoice.getSelectedIndex();

                if (text.trim().length() == 0) {
                    rowSorter.setRowFilter(null);
                } else {
                    rowSorter.setRowFilter(RowFilter.regexFilter("(?i)" + text + "", choice));
                }
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }

        });
        return pnFind;

    }

    public JPanel pnTable() {
        pnTable = new JPanel();
        pnTable.setLayout(null);
        pnTable.setPreferredSize(new Dimension(1300, 410));
        /**
         * ************ T???O MODEL V?? HEADER ********************
         */
        Vector header = new Vector();
        header.add("M?? b???ng ch???m c??ng");
        header.add("M?? nh??n vi??n");
        header.add("T??n nh??n vi??n");
        header.add("Ng??y");
        header.add("Gi??? v??o");
        header.add("Gi??? ra");
        model = new DefaultTableModel(header, 4);
        tbChiTietBangChamCong = new JTable(model);
        rowSorter = new TableRowSorter<TableModel>(model);
        tbChiTietBangChamCong.setRowSorter(rowSorter);
        listChiTietBangChamCong(mabangchamcong); //?????c t??? database l??n table 

        /**
         * ******************************************************
         */
        /**
         * ************** T???O TABLE
         * ***********************************************************
         */
        // Custom table
        tbChiTietBangChamCong.setFocusable(false);
        tbChiTietBangChamCong.setIntercellSpacing(new Dimension(0, 0));
        tbChiTietBangChamCong.setRowHeight(30);
        tbChiTietBangChamCong.getTableHeader().setOpaque(false);
        tbChiTietBangChamCong.setFillsViewportHeight(true);
        tbChiTietBangChamCong.getTableHeader().setBackground(new Color(232, 57, 99));
        tbChiTietBangChamCong.getTableHeader().setForeground(Color.WHITE);
        tbChiTietBangChamCong.setSelectionBackground(new Color(52, 152, 219));

        // Add table v??o ScrollPane
        JScrollPane scroll = new JScrollPane(tbChiTietBangChamCong);
        scroll.setBounds(new Rectangle(0, 0, 1300, 410));
        scroll.setBackground(null);

        pnTable.add(scroll);
        return pnTable;
    }

    public void cleanView() //X??a tr???ng c??c TextField
    {
        for (int i = 0; i < txtChiTietBangChamCong.length; i++) {
            if (i != 0) {
                txtChiTietBangChamCong[i].setText("");
            } else {
                picker.setDate(null);
            }

        }

    }

    public void outModel(DefaultTableModel model, ArrayList<chiTietBangChamCongDTO> ctbcc) // Xu???t ra Table t??? ArrayList
    {
        Vector data;
        model.setRowCount(0);
        for (chiTietBangChamCongDTO n : ctbcc) {
            data = new Vector();
            data.add(n.getMabangchamcong());
            data.add(n.getManhanvien());
            data.add(n.getTennhanvien());
            data.add(n.getNgay());
            data.add(n.getGiovao());
            data.add(n.getGiora());
            model.addRow(data);
        }
        tbChiTietBangChamCong.setModel(model);
    }

    public void listChiTietBangChamCong(String mabangchamcong) // Ch??p ArrayList l??n table
    {
        if (ctbccBUS.getList(mabangchamcong) == null) {
            ctbccBUS.listChiTietBangChamCong(mabangchamcong);
        }
        ArrayList<chiTietBangChamCongDTO> ctbcc = ctbccBUS.getList(mabangchamcong);
        outModel(model, ctbcc);
    }

    public boolean validateBangChamCong(String ngay, String giovao, String giora) {
        boolean validate = true;
        boolean validateFull = true;
        for (int i = 0; i < txtChiTietBangChamCong.length; i++) {
            if (i != 0) {
                if (txtChiTietBangChamCong[i].getText().equals("")) {
                    validateFull = false;
                }
            } else {
                if (picker.getDate() == null) {
                    validateFull = false;
                }
            }

        }
        if (validateFull == false) {
            JOptionPane.showMessageDialog(null, "Vui l??ng nh???p ?????y ????? th??ng tin!");
            validate = false;
            return false;
        }
        if (validatorHour.isValid(giovao) == false) {
            JOptionPane.showMessageDialog(null, "Vui l??ng nh???p ????ng ?????nh d???ng gi??? HH:mm:ss");
            txtChiTietBangChamCong[2].requestFocus();
            validate = false;
            return false;
        }
        if (validatorHour.isValid(giora) == false) {
            JOptionPane.showMessageDialog(null, "Vui l??ng nh???p ????ng ?????nh d???ng gi??? HH:mm:ss");
            txtChiTietBangChamCong[3].requestFocus();
            validate = false;
            return false;
        }
        return validate;
    }
}
