package com.example.dead;searchBox.textProperty().addListener((observable, oldValue, newValue) -> {
    searchGridPane.getChildren().clear();
    searchGridPane.setVgap(15);
    searchGridPane.setPadding(new Insets(15));
    searchGridPane.setAlignment(Pos.CENTER);

    List<User> users = UserController.getInstance().search(searchBox.getText());
    int row = 0;

    for (User user : users) {

        HBox userRow = new HBox(20);
        userRow.setOnMouseClicked(event -> {
            ProfileScene.user = user;
            FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("profile-view.fxml"));
            Scene scene;
            try {
                scene = new Scene(fxmlLoader.load(), bounds.getWidth(), bounds.getHeight());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            HelloApplication.stage.setScene(scene);
        });

        ImageView imageView = new ImageView();
        imageView.setImage(chooseImage(user.getId()).getImage());
        imageView.setFitWidth(50);
        imageView.setFitHeight(50);
        imageView.setPreserveRatio(true);
        userRow.setAlignment(Pos.CENTER_LEFT);
        userRow.getStyleClass().add("row-style");
        Text userName = new Text(user.getName());
        userName.setStyle("-fx-font-weight: bold; -fx-font-size: 22px;");
        userRow.getChildren().addAll(imageView, userName);
        userRow.setPadding(new Insets(10));
        userRow.setBackground(new Background(new BackgroundFill(
                Color.web("rgba(115, 104, 199, 0.48)"), new CornerRadii(15), Insets.EMPTY)));
        userRow.setBorder(new Border(new BorderStroke(
                Color.web("rgba(80, 72, 150, 0.8)"), BorderStrokeStyle.SOLID,
                new CornerRadii(15), new BorderWidths(2))));

        searchGridPane.add(userRow, 0, row++);
    }
});
