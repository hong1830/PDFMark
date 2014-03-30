package com.zjut.mark.test;

import java.io.File;
import java.io.FileOutputStream;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Font.FontFamily;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.GrayColor;
import com.itextpdf.text.pdf.PdfPageEventHelper;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;
import com.itextpdf.text.pdf.PdfWriter;

public class PDFCreater {

	private static final String OWNERPASSWORD = "123456";

	public static void main(String[] args) throws Exception {
		createPDF("./resource/03.pdf");

	}

	public static void createPDF(String pdfPath) throws Exception {
		
		File file = File.createTempFile("Temp", ".pdf");

		BaseFont bfCN = BaseFont.createFont("./resource/simsun.ttc,1",
				BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
		Font fontCN = new Font(bfCN, 30, Font.NORMAL, BaseColor.BLACK);
		// 第一步： Create a Document
		Document document = new Document(PageSize.A4);
		// 第二 步： Get a PdfWriter instance.
		PdfWriter writer = PdfWriter.getInstance(document,
				new FileOutputStream(file.getPath()));
		writer.setPageEvent(new Watermark("浙江工业大学", bfCN));
		// 第三步：Open the Document.
		document.open();

		// 添加Meta信息
		document.addAuthor("zjut");
		document.addCreator("zjut");

		// 第四步：添加内容

		// 添加 Paragraph
		Paragraph paragraph = new Paragraph("盲审意见", fontCN);
		paragraph.setAlignment(1);
		document.add(paragraph);

		document.add(Chunk.NEWLINE);

		// 添加 中文信息

		// 第五步：Close the Document.
		document.close();
		
		PdfReader reader = new PdfReader(file.getPath());
        PdfStamper stamper = new PdfStamper(reader, new FileOutputStream(pdfPath));
        // 设置密码
        
        stamper.setEncryption(null, OWNERPASSWORD.getBytes(), 2052, false);
        stamper.close();
	}

	public static class Watermark extends PdfPageEventHelper {
		// Font FONT = new Font(FontFamily.HELVETICA, 30, Font.BOLD, new
		// GrayColor(0.95f));

		private Font FONT;
		private String waterCont;// 水印内容

		public Watermark() {

		}

		public Watermark(String waterCont, BaseFont bs) {
			this.waterCont = waterCont;
			this.FONT = new Font(bs, 30, Font.BOLD, new GrayColor(0.55f));
		}

		@Override
		public void onEndPage(PdfWriter writer, Document document) {
			for (int i = 0; i < 6; i++) {
				for (int j = 0; j < 6; j++) {
					ColumnText.showTextAligned(writer.getDirectContentUnder(),
							Element.ALIGN_CENTER, new Phrase(
									this.waterCont == null ? "HELLO WORLD"
											: this.waterCont, FONT),
							(50.5f + i * 350), (40.0f + j * 150), writer
									.getPageNumber() % 2 == 1 ? 45 : -45);
				}
			}
		}
	}

}
