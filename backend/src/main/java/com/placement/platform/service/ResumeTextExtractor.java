package com.placement.platform.service;

import com.placement.platform.exception.ResumeExtractionException;
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

@Component
public class ResumeTextExtractor {

    public String extractText(String filePath) {
        File file = new File(filePath);
        if (!file.exists()) {
            throw new ResumeExtractionException("Resume file not found at: " + filePath);
        }

        String lowerPath = filePath.toLowerCase();
        if (lowerPath.endsWith(".pdf")) {
            return extractTextFromPdf(file);
        } else if (lowerPath.endsWith(".docx")) {
            return extractTextFromDocx(file);
        } else if (lowerPath.endsWith(".doc")) {
            throw new ResumeExtractionException("Parsing .doc is not supported. Please convert your resume to PDF or DOCX.");
        } else {
            throw new ResumeExtractionException("Unsupported file type: " + filePath);
        }
    }

    private String extractTextFromPdf(File file) {
        try (PDDocument document = Loader.loadPDF(file)) {
            PDFTextStripper stripper = new PDFTextStripper();
            String text = stripper.getText(document);
            if (text == null || text.trim().isEmpty()) {
                throw new ResumeExtractionException("Extracted PDF text is empty or document is scanned/image-only.");
            }
            return text;
        } catch (IOException e) {
            throw new ResumeExtractionException("Failed to read or parse PDF file: " + file.getName(), e);
        } catch (Exception e) {
            throw new ResumeExtractionException("Unexpected error during PDF text extraction: " + e.getMessage(), e);
        }
    }

    private String extractTextFromDocx(File file) {
        try (FileInputStream fis = new FileInputStream(file);
             XWPFDocument document = new XWPFDocument(fis);
             XWPFWordExtractor extractor = new XWPFWordExtractor(document)) {
            String text = extractor.getText();
            if (text == null || text.trim().isEmpty()) {
                throw new ResumeExtractionException("Extracted DOCX text is empty.");
            }
            return text;
        } catch (IOException e) {
            throw new ResumeExtractionException("Failed to read or parse DOCX file: " + file.getName(), e);
        } catch (Exception e) {
            throw new ResumeExtractionException("Unexpected error during DOCX text extraction: " + e.getMessage(), e);
        }
    }
}
