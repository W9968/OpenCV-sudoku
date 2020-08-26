package com.walee.sudoku;

import com.walee.sudoku.service.Detection;
import com.walee.sudoku.service.Normalisation;
import com.walee.sudoku.service.Reconnaissance;
import com.walee.sudoku.service.Solution;
import javafx.application.Application;
import javafx.stage.Stage;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;

import static com.walee.sudoku.service.Solution.Grid;

public class Main extends Application {

    static{ System.loadLibrary(Core.NATIVE_LIBRARY_NAME); }

    Reconnaissance rec = new Reconnaissance();
    Normalisation nm = new Normalisation();
    Detection det = new Detection();

    String input = "src/main/resources/images/sudoku01.jpg";
    Mat source = Imgcodecs.imread(input);
    Mat dilated, imgGrid, imgPres;
    @Override
    public void start(Stage primaryStage) throws Exception{
        nm.toGray();
        nm.toBnW();
        nm.toDilate();
        dilated = nm.getNew_dilate();
        det.toDetectContour(dilated, source);
        det.toDetectGrid(dilated,source);
        imgGrid = det.getGrid();
        det.toPositionGrid(imgGrid);
        det.toPerspective(source);
        imgPres = det.getCropped();
        System.out.println(imgPres);
        det.toDetectCase(imgPres);
        //det.toComposeImage();
        //det.DecomposerImage();
        rec.processImage();

        System.out.println("-----------------------------------");
        Solution sudoku = new Solution(Grid);
        sudoku.display();
        System.out.println("-----------------------------------");
        if (sudoku.solve()) {
            sudoku.display();
            System.out.println("-----------------------------------");
            System.out.println("Sudoku solved with BackTracking");
        } else
            System.out.println("Unsolvable");

    }


    public static void main(String[] args) {
        launch(args);
    }
}
