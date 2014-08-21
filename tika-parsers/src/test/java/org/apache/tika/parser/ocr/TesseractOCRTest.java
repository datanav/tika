package org.apache.tika.parser.ocr;

import org.apache.tika.TikaTest;
import org.apache.tika.exception.TikaException;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.Parser;
import org.apache.tika.parser.pdf.PDFParserConfig;
import org.apache.tika.sax.BodyContentHandler;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;

import static org.junit.Assert.assertTrue;


public class TesseractOCRTest  extends TikaTest {

    // Minimalistic method to verify if the reason of exception is missing tesseract
    public static boolean tessractMissing (String message) {
        return message.contains("tesseract") && message.contains("No such file or directory");
    }

    @Test
    public void testPDFOCR() throws Exception {
        Parser parser = new AutoDetectParser();
        BodyContentHandler handler = new BodyContentHandler();
        Metadata metadata = new Metadata();

        TesseractOCRConfig config = new TesseractOCRConfig();
        PDFParserConfig pdfConfig = new PDFParserConfig();
        pdfConfig.setExtractInlineImages(true);

        ParseContext parseContext = new ParseContext();
        parseContext.set(TesseractOCRConfig.class, config);
        parseContext.set(Parser.class, new TesseractOCRParser());
        parseContext.set(PDFParserConfig.class, pdfConfig);

        InputStream stream = TesseractOCRTest.class.getResourceAsStream(
                "/test-documents/testOCR.pdf");

        try {
            parser.parse(stream, handler, metadata, parseContext);
            System.out.println(handler.toString() + "ZZZZ");
            assertTrue(handler.toString().contains("Happy New Year 2003!"));
        } catch (IOException e) {
            String msg = e.getCause().getMessage();
            // Checking if test fails due to missing tesseract. Ignore if that is the case
            assertTrue(TesseractOCRTest.tessractMissing(e.getCause().getMessage()));
        } finally {
            stream.close();
        }

    }

    @Test
    public void testDOCXOCR() throws Exception {
        Parser parser = new AutoDetectParser();
        BodyContentHandler handler = new BodyContentHandler();
        Metadata metadata = new Metadata();

        TesseractOCRConfig config = new TesseractOCRConfig();

        ParseContext parseContext = new ParseContext();
        parseContext.set(TesseractOCRConfig.class, config);
        parseContext.set(Parser.class, new TesseractOCRParser());

        InputStream stream = TesseractOCRTest.class.getResourceAsStream(
                "/test-documents/testOCR.docx");

        try {
            parser.parse(stream, handler, metadata, parseContext);
            assertTrue(handler.toString().contains("Happy New Year 2003!"));
            assertTrue(handler.toString().contains("This is some text."));
            assertTrue(handler.toString().contains("Here is an embedded image:"));
        } catch (TikaException e) {
            String msg = e.getCause().getMessage();
            // Checking if test fails due to missing tesseract. Ignore if that is the case
            assertTrue(TesseractOCRTest.tessractMissing(e.getCause().getMessage()));
        } finally {
            stream.close();
        }

    }

    @Test
    public void testPPTXOCR() throws Exception {
        Parser parser = new AutoDetectParser();
        BodyContentHandler handler = new BodyContentHandler();
        Metadata metadata = new Metadata();

        TesseractOCRConfig config = new TesseractOCRConfig();

        ParseContext parseContext = new ParseContext();
        parseContext.set(TesseractOCRConfig.class, config);
        parseContext.set(Parser.class, new TesseractOCRParser());

        InputStream stream = TesseractOCRTest.class.getResourceAsStream(
                "/test-documents/testOCR.pptx");

        try {
            parser.parse(stream, handler, metadata, parseContext);
            assertTrue("Check for the image's text.", handler.toString().contains("Happy New Year 2003!"));
            assertTrue("Check for the standard text.", handler.toString().contains("This is some text"));
        } catch (TikaException e) {
            String msg = e.getCause().getMessage();
            // Checking if test fails due to missing tesseract. Ignore if that is the case
            assertTrue(TesseractOCRTest.tessractMissing(e.getCause().getMessage()));
        } finally {
            stream.close();
        }

    }
}
