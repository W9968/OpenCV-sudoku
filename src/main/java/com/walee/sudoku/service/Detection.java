package com.walee.sudoku.service;

import org.opencv.core.*;
import org.opencv.core.Point;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class Detection {

    List<MatOfPoint> contours;
    Mat contoured = new Mat();
    Mat grid = new Mat();
    Mat cropped = new Mat();
    int[] coordination;;
    String directorImage = "src/main/resources/images/cells/";

    public Detection() {}

    public void toDetectContour(Mat dilated , Mat original){
        Mat hirerachy = new Mat();
        contours = new ArrayList<MatOfPoint>();
        Imgproc.findContours(dilated, contours, hirerachy, Imgproc.RETR_LIST, Imgproc.CHAIN_APPROX_SIMPLE);
        System.out.println("contours: "+contours.size());
        Mat imageContourResult = original.clone();
        Imgproc.drawContours(imageContourResult, contours, -1, new Scalar(0,255,0), 2);
        System.out.println(imageContourResult);
        Imgcodecs.imwrite("src/main/resources/images/sudoku05.jpg", imageContourResult);
        contoured = imageContourResult;
    }

    public void toDetectGrid(Mat dilated, Mat original ) {
        Mat grilled = new Mat(dilated.rows(), dilated.cols(), dilated.type());
        contours = new ArrayList<MatOfPoint>();
        Imgproc.findContours(dilated, contours, grilled, Imgproc.RETR_LIST, Imgproc.CHAIN_APPROX_SIMPLE);

        double maxArea = ((dilated.rows()*dilated.cols()))/ 2 ;
        double supArea = (dilated.rows() - 3)*(dilated.cols() - 3);
        int nbContourArea = 0;
        int maxAreaIdx = -1;
        Mat ImageGrilleResult = original.clone();

        System.out.println("max is: "+maxArea);
        System.out.println("min is: "+supArea);

        for (int index = contours.size()-1; index >=0 ; index--) {
            Mat contour = contours.get(index);
            double contourArea = Imgproc.contourArea(contour);
            if (contourArea > maxArea && contourArea < supArea) {
                maxAreaIdx = index;
                Imgproc.drawContours(ImageGrilleResult, contours, maxAreaIdx, new Scalar(0,255,0), 2);
                nbContourArea++;
            } else {
                contours.remove(index);
            }
        }
        System.out.println("contours number is: "+contours.size()+"\nAccepted -> "+nbContourArea);
        Imgcodecs.imwrite("src/main/resources/images/sudoku06.jpg",ImageGrilleResult);
        grid = ImageGrilleResult;
    }

    public void toPositionGrid( Mat imgConRes) {

        MatOfPoint2f contour = new MatOfPoint2f();
        MatOfPoint2f approx = new MatOfPoint2f();
        contours.get(0).convertTo(contour, CvType.CV_32FC2);
        Imgproc.approxPolyDP(contour, approx, 2, true);
        MatOfPoint cm = new MatOfPoint();
        approx.convertTo(cm, CvType.CV_32S);

        coordination = new int[8];
        cm.get(0, 0, coordination);
        Point HG = new Point(coordination[0], coordination[1]);
        Point HD = new Point(coordination[2], coordination[3]);
        Point BD = new Point(coordination[4], coordination[5]);
        Point BG = new Point(coordination[6], coordination[7]);
        System.out.println("High left point: "+HG+ "\nHigh right point: " + HD+ "\nbottom right point: " + BD+ "\nBottom left point: " + BG);

        Imgproc.circle(imgConRes, HG, 10, new Scalar(255,0,0), 3);
        Imgproc.circle(imgConRes, HD, 10, new Scalar(255,0,0), 3);
        Imgproc.circle(imgConRes, BD, 10, new Scalar(255,0,0), 3);
        Imgproc.circle(imgConRes, BG, 10, new Scalar(255,0,0), 3);
        System.out.println(imgConRes);
        Imgcodecs.imwrite("src/main/resources/images/sudoku07.jpg", imgConRes);
    }

    public void toPerspective(Mat orig) {
        int x= coordination[0];
        int y= coordination[1];
        int h= Math.abs(coordination[0]-coordination[4]);
        int w= Math.abs(coordination[1]-coordination[5]);
        Rect zone = new Rect(x, y, h, w);

        orig = orig.submat(zone);
        System.out.println(zone);
        Imgcodecs.imwrite("src/main/resources/images/sudoku08.jpg",orig);
        cropped = orig ;
    }

    public void toDetectCase( Mat imageCase ){
        int lang = imageCase.rows();
        int haut = imageCase.cols();
        int langCase = lang/9;
        int hautCase = haut/9;
        int y = langCase/2;
        int x = hautCase/2;
        Mat ImageCase = imageCase.clone();
        System.out.println("\nHeight and width: {"+lang+","+haut+"}");
        System.out.println("Distance of height and width: {"+langCase+","+hautCase+"}");
        for (int i=0; i<9; i++)
            for (int j=0; j<9; j++){
                Point beginpoint = new Point(0+(hautCase*i), 0+(langCase*j));
                Point endPoint   = new Point(langCase+(hautCase*i), langCase+(langCase*j));
                Imgproc.rectangle(imageCase, beginpoint, endPoint, new Scalar(255,0,0), 5);
            }
        System.out.println(imageCase);
        Imgcodecs.imwrite("src/main/resources/images/sudoku09.jpg", imageCase);
    }


//    public void toComposeImage() throws IOException {
//        int rows = 9; //You should decide the values for rows and cols variables
//        int cols = 9;
//        int chunks = rows * cols;
//        int chunkWidth = cropped.cols() / cols; // determines the chunk width and height
//        int chunkHeight = cropped.rows() / rows;
//        int count = 0;
//        Mat[] imgs= new Mat[chunks]; //Image array to hold image chunks
//        for (int i = 0; i < rows; i++) {
//            for (int j = 0; j < cols; j++) {
//                imgs[count] = new Mat(chunkWidth, chunkHeight, cropped.type());
//                Rect zone = new Rect(rows, cols, chunkWidth, chunkHeight);
//                Mat ne = new Mat();
//            }
//        }
//        System.out.println("Splitting done");
//        System.out.println("--------------");
//
//        writing mini com.walee.sudoku.images into image files
//        for (int i = 0; i < imgs.length; i++) {
//            System.out.println(imgs[i]);
//            imageCell.add(imgs[i]);
//            ImageIO.write(imgs[i], "jpg", new File("src/main/resources/images/cells/"+i + ".jpg"));
//        }
//        System.out.println("--------------");
//        System.out.println("decomposed com.walee.sudoku.images created");
//        System.out.println("list contain: "+imageCell.size());
//        System.out.println("-----------------------\n detection end");
//    }


//    public void toComposeImage() {
//        List<Mat> listOfPieces = new ArrayList<Mat>();
//        Mat mat = new Mat();
//        int x = cropped.cols()/9;
//        int y = cropped.rows()/9;
//
//        for(int i=0; i<4; i++){
//            for(int j=0; j<4; j++){
//                Rect roi = new Rect(new Point(i*x ,j*y), new Point((i+1)*x, (j+1)*y));
//                Mat submat = mat.submat(roi);
//                listOfPieces.add(submat);
//                System.out.println(listOfPieces.size());
//            }
//        }
//    }

    public Mat getGrid() {
        return grid;
    }
    public Mat getCropped() { return cropped; }
}