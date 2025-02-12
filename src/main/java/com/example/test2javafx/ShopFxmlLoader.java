package com.example.test2javafx;

import javafx.scene.layout.Pane;
import javafx.fxml.FXMLLoader;

import java.io.File;
import java.net.URL;

public class ShopFxmlLoader {
    private Pane view;

    public Pane getPage(String fileName){
        try{
            System.out.println("Working Directory = " + System.getProperty("user.dir"));

            File file = new File("src/main/resources/com/example/test2javafx/" + fileName + ".fxml");
            URL fileUrl = file.toURI().toURL();
            System.out.println("File URL: " + fileUrl);

            if(fileUrl == null){
                throw new java.io.FileNotFoundException("FXML file can't be found");
            }
            FXMLLoader loader = new FXMLLoader(fileUrl);
            return loader.load();
        } catch (Exception e){
            System.out.println("No page " + fileName);
            e.printStackTrace();
            return null;
        }
    }
}