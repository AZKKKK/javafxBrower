package project.javafxbrowser;

import java.io.FileNotFoundException;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebHistory;
import javafx.scene.web.WebView;
import javafx.stage.Stage;

public class Browser extends Application {
    private WebView webView;
    private WebEngine webEngine;
    private HBox addressBar;
    private TextField url;
    private HBox statusBar;
    private Text domain;
    private Button home;
    private Button refresh;
    private WebHistory history;
    private Button forward;
    private Button backward;
    private Text clock;
    private Text date;
    private final String homePage = "https://ubc.ca";
    private final String barStyle = "-fx-background-color: Pink";
    private final String buttonStyle = "-fx-background-color: White";

    public static void main(String[] args) {
        Application.launch(args);
    }
    //-----------------------------------
    private void setupAddressBar() {
        addressBar = new HBox();
        addressBar.setAlignment(Pos.CENTER);
        addressBar.setStyle(barStyle);
        addressBar.setPrefHeight(50);


        url = new TextField();
        url.setPrefSize(1025, 30);

        this.setUpHomeButton();
        this.setUpRefreshButton();
        this.setUpBackwardButton();
        this.setUpForwardButton();

        addressBar.getChildren().addAll(backward,forward,home,refresh,url);
        addressBar.setOnKeyPressed(this::searchWeb);
    }

    public void searchWeb(KeyEvent event) {
        switch(event.getCode()) {
            case ENTER:
                String link = url.getText();
                webEngine.load("https://" + link);
                this.setLink();
                break;
        }
    }

    private void setUpHomeButton() {
        home = new Button();
        home.setStyle(buttonStyle);
        ImageView homeImage = new ImageView(Paths.get("src/pictures/home.png")
                .toUri().toString());


        homeImage.setFitHeight(23);
        homeImage.setFitWidth(23);

        home.setGraphic(homeImage);
        home.setOnAction(arg0 -> {
            webEngine.load(homePage);
            domain.setText(homePage);
        });

    }

    private void setUpRefreshButton() {
        refresh = new Button();
        refresh.setStyle(buttonStyle);

        ImageView refreshImage = new ImageView(Paths.get("src/pictures/refresh.png")
                .toUri().toString());
        refreshImage.setFitHeight(23);
        refreshImage.setFitWidth(23);

        refresh.setGraphic(refreshImage);
        refresh.setOnAction(e -> webEngine.reload());

    }

    private void setUpForwardButton() {
        forward = new Button();
        forward.setStyle(buttonStyle);

        ImageView forwardImage = new ImageView(Paths.get("src/pictures/forward.png")
                .toUri().toString());
        forwardImage.setFitHeight(23);
        forwardImage.setFitWidth(23);

        forward.setGraphic(forwardImage);
        forward.setOnAction(event -> this.goForward());

    }

    private void setUpBackwardButton() {
        backward = new Button();
        backward.setStyle(buttonStyle);

        ImageView backwardImage = new ImageView(Paths.get("src/pictures/backward.png")
                .toUri().toString());
        backwardImage.setFitHeight(23);
        backwardImage.setFitWidth(23);

        backward.setGraphic(backwardImage);
        backward.setOnAction(event -> this.goBackward());

    }

    private void goForward() {
        history = webEngine.getHistory();

        history.go(+1);
        this.setLink();
    }

    private void goBackward() {
        history = webEngine.getHistory();

        history.go(-1);
        this.setLink();
    }


    //-----------------------------------
    private void setupStatusBar() {
        statusBar = new HBox();
        statusBar.setAlignment(Pos.CENTER);
        statusBar.setPrefHeight(10);
        statusBar.setStyle(barStyle);
        domain = new Text(webEngine.getLocation());

        Text copyright = new Text("© 2022 -- Jiaming Zhang");
        Text separator1 = new Text("|");
        Text separator2 = new Text("|");
        Text separator3 = new Text("|");

        clock = new Text();
        date = new Text();
        Thread timerThread = new Thread(() -> {
            SimpleDateFormat TimeFormat = new SimpleDateFormat("HH:mm:ss");
            SimpleDateFormat DateFormat = new SimpleDateFormat("MMMMMMMMM-dd-yyyy");

            while (true) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                String time = TimeFormat.format(new Date());
                String today = DateFormat.format(new Date());

                Platform.runLater(() ->
                        this.setDateTime(time, today));
            }
        });
        timerThread.start();

        statusBar.getChildren().addAll(domain,separator1,copyright,separator2,date,separator3,clock);
    }
    private void setDateTime(String t, String d) {
        clock.setText(t);
        date.setText(d);
    }
    //-----------------------------------
    private void setupWebView() {
        webView= new WebView();
        webEngine = webView.getEngine();
        webEngine.load(homePage);
        webView.setOnMouseClicked(event -> this.setLink());
        webView.setOnMouseMoved(event -> this.setLink());

    }

    private void setLink() {
        domain.setText(this.getHost());
        url.setText(webEngine.getLocation());
    }

    private String getHost() {
        String link = webEngine.getLocation();

        if (link.contains("https://")) {
            link = link.replaceAll("https://", "");
        }

        if (link.contains("https://")) {
            link = link.replaceAll("http://", "");
        }

        int indexOfBackwardsSlash = link.indexOf("/");

        return link.substring(0, indexOfBackwardsSlash);

    }

    public void start(Stage stage) {
        BorderPane root = new BorderPane();
        //-----------------------------------

        this.setupAddressBar();
        this.setupWebView();
        this.setupStatusBar();
        //-----------------------------------

        root.setTop(addressBar);
        root.setBottom(statusBar);
        root.setCenter(webView);
        //-----------------------------------

        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setWidth(1200);
        stage.setHeight(1000);
        stage.setTitle("JavaFX Web Browser");
        stage.show();

    }
}