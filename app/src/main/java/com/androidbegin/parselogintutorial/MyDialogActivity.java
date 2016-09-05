package com.androidbegin.parselogintutorial;

/**
 * Created by Emi on 26.06.2015.
 */
public class MyDialogActivity {
    private static MyDialogActivity ourInstance = new MyDialogActivity();

    public static MyDialogActivity getInstance() {
        return ourInstance;
    }

    private MyDialogActivity() {
    }
}
