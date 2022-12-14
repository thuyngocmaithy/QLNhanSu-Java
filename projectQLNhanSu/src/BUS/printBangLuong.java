/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package BUS;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfWriter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/**
 *
 * @author Shadow
 */
public class printBangLuong {

    private String file = "./report/test.pdf";
    private BaseFont bf;
    String mabangluong, mabangchamcong, thoigian, manhanvien, tennhanvien;
    float luongcanban, hesoluong, tienthuong, tienphucap, sogiotre, sogiolamthem;
    double tongluong;
    String thoigiantmp;
    String luongcanbanGET, thuongGET, phucapGET, tongluongGET;

    public printBangLuong(String mabangluong, String mabangchamcong, String thoigian, String manhanvien, String tennhanvien, float luongcanban, float tienthuong, float hesoluong, float tienphucap, float sogiotre, float sogiolamthem, double tongluong) {
        this.mabangluong = mabangluong;
        this.mabangchamcong = mabangchamcong;
        this.thoigian = thoigian;
        this.manhanvien = manhanvien;
        this.tennhanvien = tennhanvien;
        this.luongcanban = luongcanban;
        this.hesoluong = hesoluong;
        this.tienthuong = tienthuong;
        this.tienphucap = tienphucap;
        this.tongluong = tongluong;
        this.sogiotre = sogiotre;
        this.sogiolamthem = sogiolamthem;
        thoigiantmp = thoigian.replace(" ", "");
        thoigiantmp = thoigian.replace("/", "-");
        file = "./report/" + mabangluong + "-" + manhanvien + "-" + thoigiantmp + ".pdf";
    }

    public void print() {
        String uderline = "*";
        try {
            //T???o Font
            bf = BaseFont.createFont("C:/Windows/Fonts/arial.ttf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED);

            // T???o t??i li???u
            Document bangluong = new Document(PageSize.A4, 15, 15, 10, 10);

            String line = "";
            for (int i = 0; i < bangluong.getPageSize().getWidth() / 5; i++) {
                line += uderline;
            }
            //T???o ?????i t?????ng writter
            PdfWriter.getInstance(bangluong, new FileOutputStream(file));

            //M??? document
            bangluong.open();

            Paragraph header = new Paragraph("B???NG L????NG", new Font(bf, 35));
            header.setAlignment(1);
            bangluong.add(header);

            Paragraph info = new Paragraph(""
                    + "B???ng l????ng : "
                    + mabangluong
                    + "                                                               Th???i gian: "
                    + thoigian
                    + "\nNh??n vi??n: "
                    + manhanvien, new Font(bf, 15));

            bangluong.add(info);

            Paragraph l = new Paragraph(line);
            l.setAlignment(1);
            bangluong.add(l);
//            CONVERT TI???N
            Locale localeVN = new Locale("vi", "VN");
            NumberFormat currencyVN = NumberFormat.getCurrencyInstance(localeVN);
            luongcanbanGET = currencyVN.format((new Float(luongcanban)).longValue());
            thuongGET = currencyVN.format((new Float(tienthuong)).longValue());
            phucapGET = currencyVN.format((new Float(tienphucap)).longValue());
            phucapGET = currencyVN.format((new Float(tienphucap)).longValue());
            tongluongGET = currencyVN.format((new Double(tongluong)).longValue());
            Paragraph main = new Paragraph(""
                    + "H??? t??n nh??n vi??n : "
                    + tennhanvien
                    + "\nL????ng c??n b???n: "
                    + luongcanbanGET
                    + "\nH??? s??? l????ng: "
                    + hesoluong
                    + "\nTi???n th?????ng: "
                    + thuongGET
                    + "\nTi???n ph??? c???p: "
                    + phucapGET
                    + "\nS??? gi??? ??i tr???: "
                    + sogiotre
                    + "\nS??? gi??? l??m th??m: "
                    + sogiolamthem, new Font(bf, 15));
            main.setAlignment(Element.ALIGN_LEFT);
            bangluong.add(main);
            bangluong.add(l);

            Paragraph luongchinhthuc = new Paragraph("L????ng ch??nh th???c : " + tongluongGET, new Font(bf, 20));
            luongchinhthuc.setAlignment(Element.ALIGN_RIGHT);
            bangluong.add(luongchinhthuc);

            bangluong.close();

            JOptionPane.showMessageDialog(null, "In ho??n t???t");
            System.out.println("Done");
        } catch (DocumentException | FileNotFoundException ex) {
            Logger.getLogger(printBangLuong.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(printBangLuong.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public PdfPCell createCell(String s) {
        PdfPCell cell = new PdfPCell(new Phrase(s, new Font(bf, 13)));
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setPaddingBottom(10);

        return cell;
    }

    public PdfPCell createCell(String s, Font font) {
        PdfPCell cell = new PdfPCell(new Phrase(s, font));
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setPaddingBottom(10);
        return cell;
    }
}
