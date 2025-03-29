package com.bubalda.shoppingplatformfx;

import javafx.fxml.Initializable;

public abstract class ControllerRoot implements Initializable {
    protected final SceneManager sceneManager = SceneManager.getInstance();

    public static String getResourcePath(String relPath) {
        // https://stackoverflow.com/questions/2593154/get-a-resource-using-getresource
        // strange path finding
        String comPath = "/com/bubalda/shoppingplatformfx/";
        return String.valueOf(SceneManager.class.getResource(comPath + relPath));
    };
}
