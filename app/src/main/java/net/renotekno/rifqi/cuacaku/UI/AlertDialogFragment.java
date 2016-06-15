package net.renotekno.rifqi.cuacaku.UI;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.os.Bundle;

public class AlertDialogFragment extends DialogFragment{
    private String errorMessage;
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Context context = getActivity();
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder
                .setTitle("Oops Sorry")
                .setMessage(getErrorMessage())
                .setPositiveButton("Ok", null);
        AlertDialog alertDialog = builder.create();
        return alertDialog;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
