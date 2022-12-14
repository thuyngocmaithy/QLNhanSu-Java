/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package GUI;

import BUS.heSoLuongBUS;
import DTO.heSoLuongDTO;
import GUI.model.checkError;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Rectangle;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Vector;
import javax.swing.BorderFactory;
import static javax.swing.BorderFactory.createLineBorder;
import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.RowFilter;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

/**
 *
 * @author trinh
 */

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
public class HeSoLuongGUI extends JPanel {

    checkError check = new checkError();
    private heSoLuongBUS hslBUS = new heSoLuongBUS();
    private int DEFALUT_WIDTH;
    private JPanel pnDisplay, pnEast, pnOption, pnFind, pnTable;
    JLabel lbAdd, lbEdit, lbRemove, btnConfirm, btnBack;
    private JTable tbHeSoLuong;
    private DefaultTableModel model;
    private boolean EditOrAdd = true;
    JTextField[] txtHeSoLuong;
    JLabel[] lbHeSoLuong;
    String mahesoluong;
    TableRowSorter<TableModel> rowSorter;

    public HeSoLuongGUI(int width) {
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

                tbHeSoLuong.clearSelection();
                tbHeSoLuong.setEnabled(false);
            }
        });

        // MouseClick btnDelete
        lbRemove.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int i = JOptionPane.showConfirmDialog(null, "X??c nh???n x??a", "Alert", JOptionPane.YES_NO_OPTION);
                if (i == 0) {
                    hslBUS.deleteHeSoLuong(mahesoluong);
                    cleanView();
                    tbHeSoLuong.clearSelection();
                    outModel(model, hslBUS.getList());
                }
            }
        });

        // MouseClick btnEdit
        lbEdit.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {

                if (txtHeSoLuong[0].getText().equals("")) {
                    JOptionPane.showMessageDialog(null, "Vui l??ng ch???n h??? s??? l????ng c???n s???a!");
                    return;
                }

                EditOrAdd = false;

                lbAdd.setVisible(false);
                lbEdit.setVisible(false);
                lbRemove.setVisible(false);

                btnConfirm.setVisible(true);
                btnBack.setVisible(true);

                tbHeSoLuong.setEnabled(false);
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

                tbHeSoLuong.setEnabled(true);
            }
        });

        //MouseClick btnConfirm
        btnConfirm.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int i;
                if (EditOrAdd) //Th??m
                {
                    i = JOptionPane.showConfirmDialog(null, "X??c nh???n th??m h??? s??? l????ng", "", JOptionPane.YES_NO_OPTION);
                    if (i == 0) {
                        //L???y d??? li???u t??? TextField
                        String mahesoluongAdd = hslBUS.mahesoluong();
                        if (validate(txtHeSoLuong[0].getText()) == false) {
                            return;
                        }
                       
                        float hesoluong = Float.parseFloat(txtHeSoLuong[0].getText());

                        if (hslBUS.checkHeSoLuong(hesoluong)) {
                            JOptionPane.showMessageDialog(null, "H??? s??? l????ng ???? t???n t???i!");
                            return;
                        }
                        //Upload s???n ph???m l??n DAO v?? BUS
                        heSoLuongDTO hsl = new heSoLuongDTO(mahesoluongAdd, hesoluong);
                        hslBUS.addHeSoLuong(hsl);

                        outModel(model, hslBUS.getList());// Load l???i table

//                        saveIMG();// L??u h??nh ???nh 
                        cleanView();
                    }
                } else // Edit S???n ph???m
                {
                    i = JOptionPane.showConfirmDialog(null, "X??c nh???n s???a h??? s??? l????ng", "", JOptionPane.YES_NO_OPTION);
                    if (i == 0) {
                        //L???y d??? li???u t??? TextField
                        if (validate(txtHeSoLuong[0].getText()) == false) {
                            return;
                        }
                        float hesoluong = Float.parseFloat(txtHeSoLuong[0].getText());
                        if (hslBUS.checkHeSoLuong(hesoluong)) {
                            JOptionPane.showMessageDialog(null, "H??? s??? l????ng ???? t???n t???i!");
                            return;
                        }
                        heSoLuongDTO hsl = new heSoLuongDTO(mahesoluong, hesoluong);
                        hslBUS.setHeSoLuong(hsl);

                        outModel(model, hslBUS.getList());
                        JOptionPane.showMessageDialog(null, "S???a th??nh c??ng", "Th??nh c??ng", JOptionPane.INFORMATION_MESSAGE);
                        tbHeSoLuong.setEnabled(true);
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
        tbHeSoLuong.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int i = tbHeSoLuong.getSelectedRow();
                mahesoluong = tbHeSoLuong.getModel().getValueAt(i, 0).toString();
                txtHeSoLuong[0].setText(tbHeSoLuong.getModel().getValueAt(i, 1).toString());
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
        String[] arrChucNang = {"H??? s??? l????ng"};
        txtHeSoLuong = new JTextField[arrChucNang.length];
        lbHeSoLuong = new JLabel[arrChucNang.length];
        int xLb = 30, yLb = 80;
        int xTxt = 230, yTxt = 80;
        for (int i = 0; i < arrChucNang.length; i++) {
            lbHeSoLuong[i] = new JLabel(arrChucNang[i]);
            lbHeSoLuong[i].setBounds(xLb, yLb, 180, 30);
            lbHeSoLuong[i].setHorizontalAlignment(JLabel.CENTER);
            lbHeSoLuong[i].setOpaque(false);
            lbHeSoLuong[i].setName("lb" + i);
            pnDisplay.add(lbHeSoLuong[i]);
            yLb = yLb + 50;
            txtHeSoLuong[i] = new JTextField();
            txtHeSoLuong[i].setName("txt" + i);
            txtHeSoLuong[i].setBounds(xTxt, yTxt, 220, 30);
            pnDisplay.add(txtHeSoLuong[i]);
            yTxt = yTxt + 50;
        }
        pnDisplay.setBorder(BorderFactory.createLoweredBevelBorder());
        return pnDisplay;
    }

    public JPanel pnOption() {
        pnOption = new JPanel();
        pnOption.setLayout(null);
        pnOption.setBackground(Color.PINK);
        pnOption.setPreferredSize(new Dimension(400, 200));
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
        cmbChoice.addItem("M?? h??? s??? l????ng");
        cmbChoice.addItem("H??? s??? l????ng");
//        cmbChoice.setPreferredSize(new Dimension(120, 30));
        cmbChoice.setBounds(new Rectangle(0, 0, 150, 40));

        //Ph???n TextField
        JTextField txtSearch = new JTextField();
        txtSearch.setBounds(new Rectangle(150, 0, 310, 40));
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
        header.add("M?? h??? s??? l????ng");
        header.add("H??? s??? l????ng");
        model = new DefaultTableModel(header, 2);
        tbHeSoLuong = new JTable(model);
        rowSorter = new TableRowSorter<TableModel>(model);
        tbHeSoLuong.setRowSorter(rowSorter);
        listHeSoLuong(); //?????c t??? database l??n table 

        /**
         * ******************************************************
         */
        /**
         * ************** T???O TABLE
         * ***********************************************************
         */
        // Ch???nh width c??c c???t 
        tbHeSoLuong.getColumnModel().getColumn(0).setPreferredWidth(40);
        tbHeSoLuong.getColumnModel().getColumn(1).setPreferredWidth(40);

        // Custom table
        tbHeSoLuong.setFocusable(false);
        tbHeSoLuong.setIntercellSpacing(new Dimension(0, 0));
        tbHeSoLuong.setRowHeight(30);
        tbHeSoLuong.getTableHeader().setOpaque(false);
        tbHeSoLuong.setFillsViewportHeight(true);
        tbHeSoLuong.getTableHeader().setBackground(new Color(232, 57, 99));
        tbHeSoLuong.getTableHeader().setForeground(Color.WHITE);
        tbHeSoLuong.setSelectionBackground(new Color(52, 152, 219));

        // Add table v??o ScrollPane
        JScrollPane scroll = new JScrollPane(tbHeSoLuong);
        scroll.setBounds(new Rectangle(0, 10, this.DEFALUT_WIDTH - 230, 470));
        scroll.setBackground(null);

        pnTable.add(scroll);
        return pnTable;
    }

    public void cleanView() //X??a tr???ng c??c TextField
    {
        txtHeSoLuong[0].setEditable(true);
        for (int i = 0; i < txtHeSoLuong.length; i++) {
            txtHeSoLuong[i].setText("");
        }

    }

    public void outModel(DefaultTableModel model, ArrayList<heSoLuongDTO> cn) // Xu???t ra Table t??? ArrayList
    {
        Vector data;
        model.setRowCount(0);
        for (heSoLuongDTO n : cn) {
            data = new Vector();
            data.add(n.getMahesoluong());
            data.add(n.getHesoluong());
            model.addRow(data);
        }
        tbHeSoLuong.setModel(model);
    }

    public void listHeSoLuong() // Ch??p ArrayList l??n table
    {
        if (hslBUS.getList() == null) {
            hslBUS.listHeSoLuong();
        }
        ArrayList<heSoLuongDTO> cn = hslBUS.getList();
//        model.setRowCount(0);
        outModel(model, cn);
    }

    public boolean validate(String hesoluong) {
        boolean validate = true;
        boolean validateFull = true;
        for (int i = 0; i < txtHeSoLuong.length; i++) {
            if (txtHeSoLuong[i].getText().equals("")) {
                validateFull = false;
            }
        }
        if (validateFull == false) {
            JOptionPane.showMessageDialog(null, "Vui l??ng nh???p ?????y ????? th??ng tin!");
            validate = false;
            return false;
        }
        if (check.check_Number(hesoluong)) {
            JOptionPane.showMessageDialog(null, "H??? s??? l????ng ph???i l?? s???!");
            validate = false;
            return false;
        }
        return validate;
    }

}
