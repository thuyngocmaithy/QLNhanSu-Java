/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GUI;

import BUS.phuCapBUS;
import DTO.phuCapDTO;
import GUI.model.checkError;

import GUI.model.navItem;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.SQLException;
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
public class PhuCapGUI extends JPanel {
    checkError check = new checkError();
    private phuCapBUS pcBUS = new phuCapBUS();
    private int DEFALUT_WIDTH;
    private JPanel pnDisplay, pnEast, pnOption, pnFind, pnTable;
    JLabel lbAdd, lbEdit, lbRemove, btnConfirm, btnBack;
    private JTable tbPhuCap;
    private DefaultTableModel model;
    private boolean EditOrAdd = true;
    JTextField[] txtPhuCap;
    JLabel[] lbPhuCap;
String maphucap;
    TableRowSorter<TableModel> rowSorter;

    public PhuCapGUI(int width) {
        DEFALUT_WIDTH = width;
        init();
    }

    public void init() {
        setLayout(new BorderLayout());
        setBackground(null);
        setBounds(new Rectangle(10, 0, this.DEFALUT_WIDTH - 200, 1000));

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

                tbPhuCap.clearSelection();
                tbPhuCap.setEnabled(false);
            }
        });

        // MouseClick btnDelete
        lbRemove.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int i = JOptionPane.showConfirmDialog(null, "X??c nh???n x??a", "Alert", JOptionPane.YES_NO_OPTION);
                if (i == 0) {
                    pcBUS.deletePhuCap(maphucap);
                    cleanView();
                    tbPhuCap.clearSelection();
                    outModel(model, pcBUS.getList());
                }
            }
        });

        // MouseClick btnEdit
        lbEdit.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {

                if (txtPhuCap[0].getText().equals("")) {
                    JOptionPane.showMessageDialog(null, "Vui l??ng ch???n ph??? c???p c???n s???a!");
                    return;
                }

                EditOrAdd = false;
                lbAdd.setVisible(false);
                lbEdit.setVisible(false);
                lbRemove.setVisible(false);

                btnConfirm.setVisible(true);
                btnBack.setVisible(true);
//                btnFile.setVisible(true);

//                tbl.clearSelection();
                tbPhuCap.setEnabled(false);
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

                tbPhuCap.setEnabled(true);
            }
        });

        //MouseClick btnConfirm
        btnConfirm.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int i;
                if (EditOrAdd) //Th??m
                {
                    i = JOptionPane.showConfirmDialog(null, "X??c nh???n th??m ph??? c???p", "", JOptionPane.YES_NO_OPTION);
                    if (i == 0) {
                        //L???y d??? li???u t??? TextField
                        String maphucapAdd = pcBUS.maphucap();
                        String tenphucap = txtPhuCap[0].getText();
                        if (validate(txtPhuCap[1].getText()) == false) {
                            return;
                        }
                        float tienphucap = Float.parseFloat(txtPhuCap[1].getText());
                        

                        //Upload s???n ph???m l??n DAO v?? BUS
                        phuCapDTO pc = new phuCapDTO(maphucapAdd, tenphucap, tienphucap);
                        pcBUS.addPhuCap(pc);

                        outModel(model, pcBUS.getList());
                        cleanView();
                    }
                } else // Edit S???n ph???m
                {
                    i = JOptionPane.showConfirmDialog(null, "X??c nh???n s???a ph??? c???p", "", JOptionPane.YES_NO_OPTION);
                    if (i == 0) {
                        //L???y d??? li???u t??? TextField
                        String tenphucap = txtPhuCap[0].getText();
                        if (validate(txtPhuCap[1].getText()) == false) {
                            return;
                        }
                        float tienphucap = Float.parseFloat(txtPhuCap[1].getText());

                        //Upload s???n ph???m l??n DAO v?? BUS
                        phuCapDTO pc = new phuCapDTO(maphucap, tenphucap, tienphucap);
                        pcBUS.setPhuCap(pc);

                        outModel(model, pcBUS.getList());// Load l???i table
                        JOptionPane.showMessageDialog(null, "S???a th??nh c??ng", "Th??nh c??ng", JOptionPane.INFORMATION_MESSAGE);
                        tbPhuCap.setEnabled(true);
                    }
                }

            }
        });

        pnFind();
        pnEast = new JPanel();
        pnEast.setLayout(new BorderLayout(10, 10));
        pnEast.setPreferredSize(new Dimension(((this.DEFALUT_WIDTH - 200) / 2) - 10, 1000 / 3));
        pnEast.add(pnOption, BorderLayout.NORTH);
        pnEast.add(pnFind, BorderLayout.SOUTH);
        pnTable();
        tbPhuCap.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int i = tbPhuCap.getSelectedRow();
                maphucap = tbPhuCap.getModel().getValueAt(i, 0).toString();
                txtPhuCap[0].setText(tbPhuCap.getModel().getValueAt(i, 1).toString());
                txtPhuCap[1].setText(tbPhuCap.getModel().getValueAt(i, 2).toString());

            }
        });

//TEST
//
        this.add(pnDisplay, BorderLayout.WEST);
        this.add(pnEast, BorderLayout.EAST);
        this.add(pnTable, BorderLayout.SOUTH);
//        this.setUndecorated(true);
        this.setVisible(true);
    }

    public JPanel pnDisplay() {
        pnDisplay = new JPanel();
        pnDisplay.setLayout(null);
        pnDisplay.setPreferredSize(new Dimension(((this.DEFALUT_WIDTH - 200) / 2) - 20, 1000 / 3));
        pnDisplay.setBackground(Color.pink);
        String[] arrPhuCap = {"T??n ph??? c???p", "Ti???n ph??? c???p"};
        txtPhuCap = new JTextField[arrPhuCap.length];
        lbPhuCap = new JLabel[arrPhuCap.length];
        int xLb = 30, yLb = 70;
        int xTxt = 230, yTxt = 70;
        for (int i = 0; i < arrPhuCap.length; i++) {
            lbPhuCap[i] = new JLabel(arrPhuCap[i]);
            lbPhuCap[i].setBounds(xLb, yLb, 180, 30);
            lbPhuCap[i].setHorizontalAlignment(JLabel.CENTER);
            lbPhuCap[i].setOpaque(false);
            lbPhuCap[i].setName("lb" + i);
            pnDisplay.add(lbPhuCap[i]);
            yLb = yLb + 50;
            txtPhuCap[i] = new JTextField();
            txtPhuCap[i].setName("txt" + i);
            txtPhuCap[i].setBounds(xTxt, yTxt, 220, 30);
            pnDisplay.add(txtPhuCap[i]);
            yTxt = yTxt + 50;
        }
        pnDisplay.setBorder(BorderFactory.createLoweredBevelBorder());
        return pnDisplay;
    }

    public JPanel pnOption() {
        pnOption = new JPanel();
        pnOption.setLayout(null);
        pnOption.setBackground(Color.PINK);
        pnOption.setPreferredSize(new Dimension(0, 200));
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

        return pnOption;
    }

    public JPanel pnFind() {
        /**
         * ********************* SORT TABLE ****************************
         */
        pnFind = new JPanel(null);
        pnFind.setPreferredSize(new Dimension(500, 40));
//        pnFind.setBounds(new Rectangle(50, 120, 300, 30));
        pnFind.setBorder(createLineBorder(Color.BLACK)); //Ch???nh vi???n 
        //PH???N CH???N SEARCH
        JComboBox cmbChoice = new JComboBox();

        cmbChoice.setEditable(true);
        cmbChoice.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        cmbChoice.addItem("M?? ph??? c???p");
        cmbChoice.addItem("T??n ph??? c???p");
//        cmbChoice.setPreferredSize(new Dimension(120, 30));
        cmbChoice.setBounds(new Rectangle(0, 0, 120, 40));

        //Ph???n TextField
        JTextField txtSearch = new JTextField();
        txtSearch.setBounds(new Rectangle(120, 0, 340, 40));
        txtSearch.setBorder(null);
        txtSearch.setOpaque(false);
        txtSearch.setFont(new Font("Segoe UI", Font.PLAIN, 15));

        // Custem Icon search
        JLabel searchIcon = new JLabel(new ImageIcon("./src/img/search_24px.png"));
        searchIcon.setBounds(new Rectangle(460, 0, 40, 40));
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
        pnTable.setPreferredSize(new Dimension(1090, 750));
        /**
         * ************ T???O MODEL V?? HEADER ********************
         */
        Vector header = new Vector();
        header.add("M?? ph??? c???p");
        header.add("T??n ph??? c???p");
        header.add("Ti???n ph??? c???p");
        model = new DefaultTableModel(header, 3);
        tbPhuCap = new JTable(model);
        rowSorter = new TableRowSorter<TableModel>(model);
        tbPhuCap.setRowSorter(rowSorter);
        listPhuCap(); //?????c t??? database l??n table 

        /**
         * ******************************************************
         */
        /**
         * ************** T???O TABLE
         * ***********************************************************
         */
        // Ch???nh width c??c c???t 
        tbPhuCap.getColumnModel().getColumn(0).setPreferredWidth(40);
        tbPhuCap.getColumnModel().getColumn(1).setPreferredWidth(40);
        tbPhuCap.getColumnModel().getColumn(2).setPreferredWidth(40);

        // Custom table
        tbPhuCap.setFocusable(false);
        tbPhuCap.setIntercellSpacing(new Dimension(0, 0));
        tbPhuCap.setRowHeight(30);
        tbPhuCap.getTableHeader().setOpaque(false);
        tbPhuCap.setFillsViewportHeight(true);
        tbPhuCap.getTableHeader().setBackground(new Color(232, 57, 99));
        tbPhuCap.getTableHeader().setForeground(Color.WHITE);
        tbPhuCap.setSelectionBackground(new Color(52, 152, 219));

        // Add table v??o ScrollPane
        JScrollPane scroll = new JScrollPane(tbPhuCap);
        scroll.setBounds(new Rectangle(0, 10, this.DEFALUT_WIDTH - 230, 470));
        scroll.setBackground(null);

        pnTable.add(scroll);
        return pnTable;
    }

    public void cleanView() //X??a tr???ng c??c TextField
    {
        txtPhuCap[0].setEditable(true);
        for (int i = 0; i < txtPhuCap.length; i++) {
            txtPhuCap[i].setText("");
        }

    }

    public void outModel(DefaultTableModel model, ArrayList<phuCapDTO> pc) // Xu???t ra Table t??? ArrayList
    {
        Vector data;
        model.setRowCount(0);
        for (phuCapDTO n : pc) {
            data = new Vector();
            data.add(n.getMaphucap());
            data.add(n.getTenphucap());
            data.add(n.getTienphucap());
            model.addRow(data);
        }
        tbPhuCap.setModel(model);
    }

    public void listPhuCap() // Ch??p ArrayList l??n table
    {
        if (pcBUS.getList() == null) {
            pcBUS.listPhuCap();
        }
        ArrayList<phuCapDTO> cn = pcBUS.getList();
//        model.setRowCount(0);
        outModel(model, cn);
    }

    public boolean validate(String tienphucap) {
        boolean validate = true;
        boolean validateFull = true;
        for (int i = 0; i < txtPhuCap.length; i++) {
            if (txtPhuCap[i].getText().equals("")) {
                validateFull = false;
            }
        }
        if (validateFull == false) {
            JOptionPane.showMessageDialog(null, "Vui l??ng nh???p ?????y ????? th??ng tin!");
            validate = false;
            return false;
        }
        if (check.check_Number(tienphucap)) {
            JOptionPane.showMessageDialog(null, "Ti???n ph??? c???p ph???i l?? s???!");
            validate = false;
            return false;
        }
        return validate;
    }
}
