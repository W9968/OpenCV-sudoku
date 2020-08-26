package com.walee.sudoku.service;

import java.io.File;

import net.sourceforge.tess4j.ITesseract;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;


public class Reconnaissance {

    String result;

    public Reconnaissance() {}

    public void processImage() {
        //final File imageFile = new File("src/main/resources/images/cells/74.jpg");
        final File imageFile = new File("src/main/resources/images/sudoku08v2.jpg");
        System.out.println("Image found");
        final ITesseract instance = new Tesseract();
        instance.setTessVariable("tessedit_char_whitelist", "0123456789");
        instance.setDatapath("data/tessdata");
        instance.setLanguage("eng");
        try {
            result = instance.doOCR(imageFile);
            System.out.println(result);
        } catch (TesseractException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}

