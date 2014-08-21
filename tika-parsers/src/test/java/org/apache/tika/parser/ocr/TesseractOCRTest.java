package org.apache.tika.parser.ocr;

import org.apache.tika.TikaTest;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.Parser;
import org.apache.tika.parser.pdf.PDFParserConfig;
import org.apache.tika.sax.BodyContentHandler;
import org.junit.Test;

import java.io.InputStream;

import static org.junit.Assert.assertTrue;


public class TesseractOCRTest  extends TikaTest {

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
        } finally {
            stream.close();
        }
        assertTrue(handler.toString().contains("Happy New Year 2003!"));
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
        } finally {
            stream.close();
        }

        assertTrue(handler.toString().contains("Happy New Year 2003!"));
        assertTrue(handler.toString().contains("This is some text."));
        assertTrue(handler.toString().contains("Here is an embedded image:"));
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
        } finally {
            stream.close();
        }

        assertTrue("Check for the image's text.", handler.toString().contains("Happy New Year 2003!"));
        assertTrue("Check for the standard text.", handler.toString().contains("This is some text"));
    }
}
