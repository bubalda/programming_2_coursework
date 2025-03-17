//        logo.toFront();
//        logo2.toFront();
//
//        TranslateTransition moveDown = new TranslateTransition(Duration.seconds(0.5), logo);
//        moveDown.setToY(loginBox.getTranslateY());
//
//        ScaleTransition scaleUp = new ScaleTransition(Duration.seconds(0.5), logo);
//        scaleUp.setToX(5);
//        scaleUp.setToY(5);
//
//        TranslateTransition moveLeft = new TranslateTransition(Duration.seconds(0.5), logo);
//        moveLeft.setToX(logo2.getX());
//
//        ScaleTransition scaleDown = new ScaleTransition(Duration.seconds(0.5), logo);
//        scaleUp.setToX(1);
//        scaleUp.setToY(1);
//
//        SequentialTransition st = new SequentialTransition(moveDown, scaleUp);
//
//        SequentialTransition st2 = new SequentialTransition(moveLeft, scaleDown);
//
//        st.play();
//        st.setOnFinished(_ -> {
//            st2.play();
//        });