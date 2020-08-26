package com.walee.sudoku.service;

import org.opencv.core.*;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

public class Normalisation {

    String input = "src/main/resources/images/sudoku01.jpg";
    Mat source = Imgcodecs.imread(input);
    Mat destination = new Mat();
    Mat black_white = new Mat();
    Mat new_erode  = new Mat(black_white.rows(),black_white.cols(), black_white.type());
    Mat new_dilate  = new Mat(black_white.rows(),black_white.cols(), black_white.type());

    public Normalisation() {}

    public void toGray(){
        Imgproc.cvtColor(source, destination, Imgproc.COLOR_RGB2GRAY);
        Imgcodecs.imwrite("src/main/resources/images/sudoku02.jpg", destination);
        System.out.println("turned gray \n"+destination);
    }

    public void toBnW(){
        Imgproc.threshold(destination,black_white,0,255,Imgproc.THRESH_BINARY+Imgproc.THRESH_OTSU);
        Imgcodecs.imwrite("src/main/resources/images/sudoku03.jpg", black_white);
        System.out.println("turned black and white \n"+black_white);
    }

    public void toErode(){
        final Size KernelSize = new Size(3,3);
        final Point anchor = new Point(-1,-1);
        final int integration = 1;
        Mat kernel = Imgproc.getStructuringElement(Imgproc.MORPH_ELLIPSE,KernelSize);
        Imgproc.erode(black_white,new_erode,kernel,anchor,integration);
    }

    public void toDilate(){
        final Size KernelSize = new Size(5,5);
        final Point anchor = new Point(-1,-1);
        final int integration = 1;
        Mat kernel = Imgproc.getStructuringElement(Imgproc.MORPH_ELLIPSE,KernelSize);
        System.out.println(kernel.dump());
        Imgproc.erode(black_white,new_dilate,kernel,anchor,integration);
        Imgcodecs.imwrite("src/main/resources/images/sudoku04.jpg", new_dilate);
        System.out.println("standardization ended");

    }


    public Mat getSource() { return source; }
    public Mat getBlack_white() { return black_white; }
    public Mat getNew_dilate() {
        Mat dilated = new Mat();
        dilated = new_dilate;
        return dilated;
    }
}
