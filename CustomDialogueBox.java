package com.example.textntalk;
// DialogHelper.java
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

public class CustomDialogueBox {

    public static void showExitConfirmationDialog(Context context, String title, String positiveButtonText, String negativeButtonText, DialogInterface.OnClickListener positiveClickListener, DialogInterface.OnClickListener negativeClickListener) {
        new AlertDialog.Builder(context)
                .setTitle(title)
                .setPositiveButton(positiveButtonText, positiveClickListener)
                .setNegativeButton(negativeButtonText, negativeClickListener)
                .setCancelable(false)
                .show();
    }
}
