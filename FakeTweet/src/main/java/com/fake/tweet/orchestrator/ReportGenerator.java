package com.fake.tweet.orchestrator;

import java.io.FileOutputStream;

import com.fake.tweet.pojo.EvaluatorResult;
import com.fake.tweet.utils.AppConfig;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;

/**
 * Created by saranyakrishnan on 11/5/17.
 */
public class ReportGenerator {
    public void generateReport(EvaluatorResult j48EvaluationResult, EvaluatorResult svmEvaluationResult)
            throws Exception {
        Document document = new Document();
        PdfWriter
                .getInstance(document,
                        new FileOutputStream(AppConfig.getConfig("TEST_DATASET") + "FakeTweetJ48vsSVMReport.pdf"))
                .setInitialLeading(16);
        document.open();
        Font titleFont = FontFactory.getFont(FontFactory.TIMES_ROMAN, 18, Font.BOLD);
        Font headingFont = FontFactory.getFont(FontFactory.TIMES_ROMAN, 16, Font.BOLD);
        Font subHeadingFont = FontFactory.getFont(FontFactory.TIMES_ROMAN, 14, Font.BOLD);
        Font bodyFont = FontFactory.getFont(FontFactory.TIMES_ROMAN, 12, BaseColor.BLACK);

        Paragraph title = new Paragraph(new Chunk("Report", titleFont));
        title.setAlignment(Element.ALIGN_CENTER);
        document.add(title);
        document.add(Chunk.NEWLINE);

        document.add(new Chunk("J48", headingFont));
        document.add(Chunk.NEWLINE);

        document.add(new Chunk("Statistics", subHeadingFont));
        document.add(new Paragraph(new Chunk(j48EvaluationResult.getEvaluation().toSummaryString(), bodyFont)));
        document.add(new Chunk("Detailed Class Statistics", subHeadingFont));
        document.add(new Paragraph(new Chunk(j48EvaluationResult.getEvaluation().toClassDetailsString(), bodyFont)));
        document.add(new Chunk("Confusion Matrix", subHeadingFont));
        document.add(new Paragraph(new Chunk(j48EvaluationResult.getEvaluation().toMatrixString(), bodyFont)));

        document.newPage();

        document.add(new Chunk("SVM", headingFont));
        document.add(Chunk.NEWLINE);
        document.add(new Chunk("Statistics", subHeadingFont));
        document.add(new Paragraph(new Chunk(svmEvaluationResult.getEvaluation().toSummaryString(), bodyFont)));
        document.add(new Chunk("Detailed Class Statistics", subHeadingFont));
        document.add(new Paragraph(new Chunk(svmEvaluationResult.getEvaluation().toClassDetailsString(), bodyFont)));
        document.add(new Chunk("Confusion Matrix", subHeadingFont));
        document.add(new Paragraph(new Chunk(svmEvaluationResult.getEvaluation().toMatrixString(), bodyFont)));

        document.close();
    }
}
