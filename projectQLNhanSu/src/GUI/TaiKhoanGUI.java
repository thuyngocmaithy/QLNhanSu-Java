/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GUI;

import BUS.taiKhoanBUS;
import DTO.taiKhoanDTO;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.*;
import javax.swing.*;
import static javax.swing.BorderFactory.createLineBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

/**
 *
 * @author MaiThy
 */
public class TaiKhoanGUI extends JPanel {

    private taiKhoanBUS tkBUS = new taiKhoanBUS();
    private int DEFALUT_WIDTH;
    private JPanel pnDisplay, pnOption, pnFind, pnTable;
    JLabel lbAdd, lbEdit, lbRemove, btnConfirm, btnBack, lbKhoa, lbMoKhoa;
    private JTable tbTaiKhoan;
    private DefaultTableModel model;
    private boolean EditOrAdd = true;
    JTextField[] txtTaiKhoan;
    JLabel[] lbTaiKhoan;
//  true: add || false: edit
    TableRowSorter<TableModel> rowSorter;
    JLabel lbMaQuyen, lbSuggestMaQuyen;
    JTextField txtMaQuyen;

    public TaiKhoanGUI(int width) {
        DEFALUT_WIDTH = width;
        init();
    }

    public void init() {
        setLayout(new BorderLayout(10, 10));
        setBackground(null);
        setBounds(new Rectangle(10, 10, this.DEFALUT_WIDTH - 200, 680));

        pnDisplay();
        pnOption();
        // MouseClick btnADD
        lbAdd.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                EditOrAdd = true;

                cleanView();

                lbAdd.setVisible(false);
                lbEdit.setVisible(false);
                lbRemove.setVisible(false);

                btnConfirm.setVisible(true);
                btnBack.setVisible(true);

                tbTaiKhoan.clearSelection();
                tbTaiKhoan.setEnabled(false);
            }
        });

        // MouseClick btnDelete
        lbRemove.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int i = JOptionPane.showConfirmDialog(null, "X??c nh???n x??a", "Alert", JOptionPane.YES_NO_OPTION);
                if (i == 0) {
                    tkBUS.deleteTaiKhoan(txtTaiKhoan[0].getText());
                    cleanView();
                    tbTaiKhoan.clearSelection();
                    outModel(model, tkBUS.getList());
                }
            }
        });

        // MouseClick btnEdit
        lbEdit.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {

                if (txtTaiKhoan[0].getText().equals("")) {
                    JOptionPane.showMessageDialog(null, "Vui l??ng ch???n t??i kho???n c???n s???a!");
                    return;
                }

                EditOrAdd = false;

                txtTaiKhoan[0].setEditable(false);

                lbAdd.setVisible(false);
                lbEdit.setVisible(false);
                lbRemove.setVisible(false);

                btnConfirm.setVisible(true);
                btnBack.setVisible(true);

                tbTaiKhoan.setEnabled(false);
            }
        });
        //MouseClick btnBack
        btnBack.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                cleanView();

                lbAdd.setVisible(true);
                lbEdit.setVisible(true);
                lbRemove.setVisible(true);

                btnConfirm.setVisible(false);
                btnBack.setVisible(false);
//                btnFile.setVisible(false);

                tbTaiKhoan.setEnabled(true);
            }
        });

        //MouseClick btnConfirm
        btnConfirm.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int i;
                if (EditOrAdd) //Th??m
                {
                    i = JOptionPane.showConfirmDialog(null, "X??c nh???n th??m t??i kho???n", "", JOptionPane.YES_NO_OPTION);
                    if (i == 0) {
                        //L???y d??? li???u t??? TextField
                        String tentaikhoan = txtTaiKhoan[0].getText();
                        String matkhau = txtTaiKhoan[1].getText();
                        String trangthai = txtTaiKhoan[2].getText();
                        String maquyen = txtMaQuyen.getText();
                        if (validate(tentaikhoan) == false) {
                            return;
                        }
                        if (tkBUS.checkTenTaiKhoan(tentaikhoan)) {
                            JOptionPane.showMessageDialog(null, "M?? t??i kho???n ???? t???n t???i!");
                            return;
                        }
                        //Upload s???n ph???m l??n DAO v?? BUS
                        taiKhoanDTO tk = new taiKhoanDTO(tentaikhoan, matkhau, trangthai, maquyen);
                        tkBUS.addTaiKhoan(tk);

                        outModel(model, tkBUS.getList());// Load l???i table

                        cleanView();
                    }
                } else // Edit S???n ph???m
                {
                    i = JOptionPane.showConfirmDialog(null, "X??c nh???n s???a t??i kho???n", "", JOptionPane.YES_NO_OPTION);
                    if (i == 0) {
                        //L???y d??? li???u t??? TextField
                        String tentaikhoan = txtTaiKhoan[0].getText();
                        String matkhau = txtTaiKhoan[1].getText();
                        String trangthai = txtTaiKhoan[2].getText();
                        String maquyen = txtMaQuyen.getText();
                        if (validate(tentaikhoan) == false) {
                            return;
                        }
                        //Upload s???n ph???m l??n DAO v?? BUS
                        taiKhoanDTO tk = new taiKhoanDTO(tentaikhoan, matkhau, trangthai, maquyen);
                        tkBUS.setTaiKhoan(tk);

                        outModel(model, tkBUS.getList());// Load l???i table

//                        saveIMG();// L??u h??nh ???nh 
                        JOptionPane.showMessageDialog(null, "S???a th??nh c??ng", "Th??nh c??ng", JOptionPane.INFORMATION_MESSAGE);
                        tbTaiKhoan.setEnabled(true);
                    }
                }

            }
        });

        pnTable();
        tbTaiKhoan.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int i = tbTaiKhoan.getSelectedRow();
                txtTaiKhoan[0].setText(tbTaiKhoan.getModel().getValueAt(i, 0).toString());
                txtTaiKhoan[1].setText(tbTaiKhoan.getModel().getValueAt(i, 1).toString());
                txtMaQuyen.setText(tbTaiKhoan.getModel().getValueAt(i, 3).toString());
                tkBUS.listTaiKhoan();
                ArrayList<taiKhoanDTO> taikhoanDTO = tkBUS.getList();
                System.out.println(taikhoanDTO.get(0).getTrangthai());
                for (int j = 0; j < taikhoanDTO.size(); j++) {
                    if (taikhoanDTO.get(i).getTrangthai().equals("0")) {
                        lbKhoa.setVisible(true);
                        lbMoKhoa.setVisible(false);
                    } else if (taikhoanDTO.get(i).getTrangthai().equals("1")) {
                        lbKhoa.setVisible(false);
                        lbMoKhoa.setVisible(true);
                    } else {
                        lbKhoa.setVisible(false);
                        lbMoKhoa.setVisible(false);
                    }
                }
            }
        });

//TEST
//
        this.add(pnDisplay, BorderLayout.WEST);
        this.add(pnOption, BorderLayout.CENTER);
        this.add(pnTable, BorderLayout.SOUTH);
        this.setVisible(true);
    }

    public JPanel pnDisplay() {
        pnDisplay = new JPanel();
        pnDisplay.setLayout(null);
        pnDisplay.setPreferredSize(new Dimension(((this.DEFALUT_WIDTH - 200) / 2) - 20, 240));
        pnDisplay.setBackground(Color.pink);
        String[] arrTaiKhoan = {"T??n t??i kho???n", "M???t kh???u"};
        txtTaiKhoan = new JTextField[arrTaiKhoan.length];
        lbTaiKhoan = new JLabel[arrTaiKhoan.length];
        int xLb = 50, yLb = 50;
        int xTxt = 260, yTxt = 50;
        for (int i = 0; i < arrTaiKhoan.length; i++) {
            lbTaiKhoan[i] = new JLabel(arrTaiKhoan[i]);
            lbTaiKhoan[i].setBounds(xLb, yLb, 180, 30);
            lbTaiKhoan[i].setHorizontalAlignment(JLabel.CENTER);
            lbTaiKhoan[i].setName("lb" + i);
            pnDisplay.add(lbTaiKhoan[i]);
            yLb = yLb + 40;
            txtTaiKhoan[i] = new JTextField();
            txtTaiKhoan[i].setName("txt" + i);
            txtTaiKhoan[i].setBounds(xTxt, yTxt, 220, 30);
            pnDisplay.add(txtTaiKhoan[i]);
            yTxt = yTxt + 40;
        };
        //        LB TH??M M?? QUY???N
        lbMaQuyen = new JLabel("M?? quy???n");
        lbMaQuyen.setBounds(50, 130, 180, 30);
        lbMaQuyen.setHorizontalAlignment(JLabel.CENTER);
        pnDisplay.add(lbMaQuyen);
//        TXT M?? QUY???N
        txtMaQuyen = new JTextField();
        txtMaQuyen.setName("txtMaQuyen");
        txtMaQuyen.setBounds(260, 130, 190, 30);
        pnDisplay.add(txtMaQuyen);
////        LB CH???N M?? QUY???N
        lbSuggestMaQuyen = new JLabel("...", JLabel.CENTER);
        lbSuggestMaQuyen.setBounds(450, 130, 30, 30);
        lbSuggestMaQuyen.setHorizontalAlignment(JLabel.CENTER);
        lbSuggestMaQuyen.setOpaque(true);
        lbSuggestMaQuyen.setCursor(new Cursor(Cursor.HAND_CURSOR));
        pnDisplay.add(lbSuggestMaQuyen);
        lbSuggestMaQuyen.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                SuggestQuyen sq = new SuggestQuyen();
                String s = sq.getTextFieldContent();
                if (!s.equals("")) {
                    txtMaQuyen.setText(s);
                }
            }
        });

//        LB KH??A T??I KHO???N
        lbKhoa = new JLabel("KH??A", JLabel.CENTER);
        lbKhoa.setBounds(100, 170, 100, 30);
        lbKhoa.setOpaque(true);
        lbKhoa.setCursor(new Cursor(Cursor.HAND_CURSOR));
        lbKhoa.setVisible(false);
        pnDisplay.add(lbKhoa);
        lbKhoa.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (!txtTaiKhoan[0].getText().equals(null)) {
                    tkBUS.KhoaTaiKhoan(txtTaiKhoan[0].getText());
                    lbKhoa.setVisible(false);
                    lbMoKhoa.setVisible(true);
                }
            }
        });

//        LB M??? KH??A T??I KHO???N
        lbMoKhoa = new JLabel("M??? KH??A", JLabel.CENTER);
        lbMoKhoa.setBounds(100, 170, 130, 30);
        lbMoKhoa.setOpaque(true);
        lbMoKhoa.setCursor(new Cursor(Cursor.HAND_CURSOR));
        lbMoKhoa.setVisible(false);
        pnDisplay.add(lbMoKhoa);
        lbMoKhoa.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (!txtTaiKhoan[0].getText().equals(null)) {
                    tkBUS.MoKhoaTaiKhoan(txtTaiKhoan[0].getText());
                    lbKhoa.setVisible(true);
                    lbMoKhoa.setVisible(false);
                }
            }
        });

        return pnDisplay;
    }

    public JPanel pnOption() {
        pnOption = new JPanel();
        pnOption.setLayout(null);
        pnOption.setBackground(Color.PINK);
        pnOption.setPreferredSize(new Dimension(530, 220));
        lbAdd = new JLabel(new ImageIcon("./src/img/add.png"), JLabel.CENTER);
        lbEdit = new JLabel(new ImageIcon("./src/img/edit.png"), JLabel.CENTER);
        lbRemove = new JLabel(new ImageIcon("./src/img/remove.png"), JLabel.CENTER);

        lbAdd.setBounds(155, 10, 200, 50);
        lbEdit.setBounds(155, 65, 200, 50);
        lbRemove.setBounds(155, 120, 200, 50);
        pnOption.add(lbAdd);
        pnOption.add(lbEdit);
        pnOption.add(lbRemove);
        lbAdd.setCursor(new Cursor((Cursor.HAND_CURSOR)));
        lbEdit.setCursor(new Cursor((Cursor.HAND_CURSOR)));
        lbRemove.setCursor(new Cursor((Cursor.HAND_CURSOR)));

        btnConfirm = new JLabel(new ImageIcon("./src/img/done.png"));
        btnConfirm.setVisible(false);
        btnConfirm.setBounds(80, 50, 155, 100);
        btnConfirm.setCursor(new Cursor(Cursor.HAND_CURSOR));

        btnBack = new JLabel(new ImageIcon("./src/img/back.png"));
        btnBack.setVisible(false);
        btnBack.setBounds(280, 50, 155, 100);
        btnBack.setCursor(new Cursor(Cursor.HAND_CURSOR));

        pnOption.add(btnConfirm);
        pnOption.add(btnBack);
        pnFind();
        pnOption.add(pnFind);
        return pnOption;
    }

    public JPanel pnFind() {
        /**
         * ********************* SORT TABLE ****************************
         */
        pnFind = new JPanel();
        pnFind.setLayout(null);
        pnFind.setBounds(0, 190, 530, 40);
        pnFind.setBorder(createLineBorder(Color.BLACK)); //Ch???nh vi???n 
        //PH???N CH???N SEARCH
        JComboBox cmbChoice = new JComboBox();
        cmbChoice.setEditable(true);
        cmbChoice.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        cmbChoice.addItem("M?? t??i kho???n");
        cmbChoice.addItem("T??n t??i kho???n");
        cmbChoice.addItem("M???t kh???u");
        cmbChoice.addItem("M?? quy???n");
        cmbChoice.setBounds(0, 0, 140, 40);

        //Ph???n TextField
        JTextField txtSearch = new JTextField();
        txtSearch.setBounds(120, 0, 360, 40);
//        txtSearch.setPreferredSize(new Dimension(230, 30));
        txtSearch.setBorder(null);
        txtSearch.setOpaque(false);
        txtSearch.setFont(new Font("Segoe UI", Font.PLAIN, 15));

        // Custem Icon search
        JLabel searchIcon = new JLabel(new ImageIcon("./src/img/search_24px.png"));
        searchIcon.setBounds(new Rectangle(480, 0, 50, 40));
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
        pnTable.setPreferredSize(new Dimension(1090, 440));
        /**
         * ************ T???O MODEL V?? HEADER ********************
         */
        Vector header = new Vector();
        header.add("T??n t??i kho???n");
        header.add("M???t kh???u");
        header.add("Tr???ng th??i");
        header.add("M?? quy???n");
        model = new DefaultTableModel(header, 4);
        tbTaiKhoan = new JTable(model);
        rowSorter = new TableRowSorter<TableModel>(model);
        tbTaiKhoan.setRowSorter(rowSorter);
        listTaiKhoan(); //?????c t??? database l??n table 

        tbTaiKhoan.setFocusable(false);
        tbTaiKhoan.setIntercellSpacing(new Dimension(0, 0));
        tbTaiKhoan.setRowHeight(30);
        tbTaiKhoan.getTableHeader().setOpaque(false);
        tbTaiKhoan.setFillsViewportHeight(true);
        tbTaiKhoan.getTableHeader().setBackground(new Color(232, 57, 99));
        tbTaiKhoan.getTableHeader().setForeground(Color.WHITE);
        tbTaiKhoan.setSelectionBackground(new Color(52, 152, 219));

        // Add table v??o ScrollPane
        JScrollPane scroll = new JScrollPane(tbTaiKhoan);
        scroll.setBounds(new Rectangle(0, 0, this.DEFALUT_WIDTH - 230, 440));
        scroll.setBackground(null);

        pnTable.add(scroll);
        return pnTable;
    }

    public void cleanView() //X??a tr???ng c??c TextField
    {
        txtTaiKhoan[0].setEditable(true);
        for (int i = 0; i < txtTaiKhoan.length; i++) {
            txtTaiKhoan[i].setText("");
        }

    }

    public void outModel(DefaultTableModel model, ArrayList<taiKhoanDTO> tk) // Xu???t ra Table t??? ArrayList
    {
        Vector data;
        model.setRowCount(0);
        for (taiKhoanDTO n : tk) {
            data = new Vector();
            data.add(n.getTentaikhoan());
            data.add(n.getMatkhau());
            if (n.getTrangthai().equals("0")) {
                data.add("Ho???t ?????ng");
            } else {
                data.add("Kh??ng ho???t ?????ng");
            }
            data.add(n.getMaquyen());
            model.addRow(data);
        }
        tbTaiKhoan.setModel(model);
    }

    public void listTaiKhoan() // Ch??p ArrayList l??n table
    {
        if (tkBUS.getList() == null) {
            tkBUS.listTaiKhoan();
        }
        ArrayList<taiKhoanDTO> tk = tkBUS.getList();
        outModel(model, tk);
    }

    public boolean validate(String tentaikhoan) {
        boolean validate = true;
        boolean validateFull = true;
        for (int i = 0; i < txtTaiKhoan.length; i++) {
            if (txtTaiKhoan[i].getText().equals("")) {
                validateFull = false;
            }
        }
        if (txtMaQuyen.equals("")) {
            validateFull = false;
        }
        if (validateFull == false) {
            JOptionPane.showMessageDialog(null, "Vui l??ng nh???p ?????y ????? th??ng tin!");
            validate = false;
            return false;
        }
        return validate;
    }
}
