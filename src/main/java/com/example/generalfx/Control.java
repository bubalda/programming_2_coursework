package com.example.generalfx;

abstract class Control {
    protected SceneController sceneController;

    public void setSceneController(SceneController sceneController) {
        this.sceneController = sceneController;
    }
}
