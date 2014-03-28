package com.zjut.mark.test;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import com.itextpdf.text.Document;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;
import com.itextpdf.text.pdf.PdfWriter;

public class PDFMarkTest {

	public static void buildPDF(String imageFile, String PDFFile,
			String waterMarkName, int permission) {
		try {
			File file = File.createTempFile("tmpFile", ".pdf");
			if (createPDF(file)) {
				waterMark(file.getPath(), imageFile, PDFFile, waterMarkName,
						permission);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static boolean createPDF(File file) {
		Rectangle rect = new Rectangle(PageSize.A4);
		Document document = new Document(rect, 50.0F, 50.0F, 50.0F, 50.0F);
		try {
			PdfWriter.getInstance(document, new FileOutputStream(file));
			document.open();
			BaseFont bf = BaseFont.createFont("./resource/simsun.ttc,1",
					BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
			Font font = new Font(bf, 14.0F, 0);
			font.setStyle(37);
			font.setFamily(" 宋体");
			Paragraph p = new Paragraph("PDF 水印测试 \r  很多情况下，我们的需求并不是很复杂，往往就是整页有一个背景图片就够了，它往往又是一些", font);
			document.add(p);
			document.close();
			return true;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}

	public static void waterMark(String input, String imageFile, String output,
			String waterMarkName, int permission) {
		try {
			PdfReader reader = new PdfReader(input);
			PdfStamper stamper = new PdfStamper(reader, new FileOutputStream(
					output));
			BaseFont base = BaseFont.createFont("./resource/simsun.ttc,1",
					BaseFont.IDENTITY_H, true);
			int total = reader.getNumberOfPages() + 1;
			Image image = Image.getInstance(imageFile);
			image.setAbsolutePosition(40, 700);                   
			PdfContentByte under;
			int j = waterMarkName.length();
			char c = 0;
			int rise = 0;
			for (int i = 1; i < total; i++) {
				rise = 400;
				under = stamper.getUnderContent(i);
				under.beginText();
				under.setFontAndSize(base, 30);
				
				
				under.addImage(image);

//				if (j > 15) {
//					under.setTextMatrix(200, 120);
//					for (int k = 0; k < j; k++) {
//						under.setTextRise(rise);
//						c = waterMarkName.charAt(k);
//						under.showText(c + "");
//					}
//				} else {
//					under.setTextMatrix(240, 100);
//					for (int k = 0; k < j; k++) {
//						under.setTextRise(rise);
//						c = waterMarkName.charAt(k);
//						under.showText(c + "");
//						rise -= 18;
//					}
//
//				}
				
				under.endText();
				under.stroke();
			}

			stamper.close();

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public static void main(String[] args) {
		String imageFilePath = "./resource/zjut.jpg";
		String PDFFile = "./resource/myPDF.pdf";
		buildPDF(imageFilePath, PDFFile, "zjut", 16);
	}
}
