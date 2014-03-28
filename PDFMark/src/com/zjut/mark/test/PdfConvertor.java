package com.zjut.mark.test;
import java.awt.Color;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.Header;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfGState;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;
import com.itextpdf.text.pdf.PdfWriter;


public class PdfConvertor {
    // txt原始文件的路径
    private static final String txtFilePath = "./resource/01.txt";
    // 生成的pdf文件路径
    private static final String pdfFilePath = "./resource/01.pdf";
    // 添加水印图片路径
    private static final String imageFilePath = "./resource/zjut.jpg";
    // 生成临时文件前缀
    private static final String prefix = "tempFile";
    // 所有者密码
    private static final String OWNERPASSWORD = "123456";

    
    public static void generatePDFWithTxt(String txtFile, String pdfFile,
            String userPassWord, String waterMarkName, int permission) {
        try {
            // 生成临时文件
            File file = File.createTempFile(prefix, ".pdf");
            // 创建pdf文件到临时文件
            if (createPDFFile(txtFile, file)) {
                // 增加水印和加密
                waterMark(file.getPath(), pdfFile, userPassWord, OWNERPASSWORD,
                        waterMarkName, permission);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    
    private static boolean createPDFFile(String txtFilePath, File file) {
        // 设置纸张
        Rectangle rect = new Rectangle(PageSize.A4);
        // 设置页码
        // step1
        Document doc = new Document(rect, 50, 50, 50, 50);
        try {
            FileReader fileRead = new FileReader(txtFilePath);
            BufferedReader read = new BufferedReader(fileRead);
            // 设置pdf文件生成路径 step2
            PdfWriter.getInstance(doc, new FileOutputStream(file));
            // 打开pdf文件 step3
            doc.open();
            // 实例化Paragraph 获取写入pdf文件的内容，调用支持中文的方法. step4
            while (read.ready()) {
                // 添加内容到pdf(这里将会按照txt文件的原始样式输出)
                doc.add(new Paragraph(read.readLine(), PdfConvertor
                        .setChineseFont()));
            }
            // 关闭pdf文件 step5
            doc.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    
    private static void waterMark(String inputFile, String outputFile,
            String userPassWord, String ownerPassWord, String waterMarkName,
            int permission) {
        try {
            PdfReader reader = new PdfReader(inputFile);
            PdfStamper stamper = new PdfStamper(reader, new FileOutputStream(
                    outputFile));
            // 设置密码
            //stamper.setEncryption(userPassWord.getBytes(), ownerPassWord.getBytes(), permission, false);
            BaseFont base = BaseFont.createFont("./resource/simsun.ttc,1",
					BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
            int total = reader.getNumberOfPages() + 1;
            Image image = Image.getInstance(imageFilePath);
            image.setAbsolutePosition(50, 400);//坐标
            image.setRotation(-20);//旋转 弧度
//            image.scaleAbsolute(200,100);//自定义大小
            image.scalePercent(250);//依照比例缩放
            PdfContentByte under;
            int j = waterMarkName.length();
            char c = 0;
            int rise = 0;
            for (int i = 1; i < total; i++) {
                rise = 500;
                under = stamper.getUnderContent(i);
                // 添加图片
//                under.addImage(image);2
                PdfGState gs = new PdfGState();
                gs.setFillOpacity(0.2f);// 设置透明度为0.2
                under.setGState(gs);
                under.beginText();
                under.setFontAndSize(base, 30);
                // 设置水印文字字体倾斜 开始
                if (j >= 15) {
                    under.setTextMatrix(200, 120);
                    for (int k = 0; k < j; k++) {
                        under.setTextRise(rise);
                        c = waterMarkName.charAt(k);
                        under.showText(c + "");
                        rise -= 20;
                    }
                } else {
                    under.setTextMatrix(180, 100);
                    for (int k = 0; k < j; k++) {
                        under.setTextRise(rise);
                        c = waterMarkName.charAt(k);
                        under.showText(c + "");
                        rise -= 18;
                    }
                }
                // 字体设置结束
                under.endText();
                // 画一个圆
                // under.ellipse(250, 450, 350, 550);
                // under.setLineWidth(1f);
                // under.stroke();
            }
            stamper.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    
    private static Font setChineseFont() {
        BaseFont base = null;
        Font fontChinese = null;
        try {
        	base = BaseFont.createFont("./resource/simsun.ttc,1",
					BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
            fontChinese = new Font(base, 12, Font.NORMAL);
        } catch (DocumentException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return fontChinese;
    }

    public static void main(String[] args) {
        generatePDFWithTxt(txtFilePath, pdfFilePath, "123", "浙江工业大学", 16);
    }
}

