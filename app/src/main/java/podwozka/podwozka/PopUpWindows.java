package podwozka.podwozka;

import android.content.DialogInterface;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v7.app.AlertDialog;

public class PopUpWindows {

    // pass value in miliseconds
    private int alertWindowTimeout = 1500;
    private String response;

    private void setResponse(String response){ this.response = response; }

    public String getResponse(){ return this.response; }

    public void showAlertWindow(Context context, final String title, final String message){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);

        if (title != null){
            alertDialogBuilder.setTitle(title);
        }

        if (message != null){
            alertDialogBuilder.setMessage(message);
        }

        alertDialogBuilder
                .setCancelable(false);

        // create alert dialog
        final AlertDialog alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.show();

        final Handler handler  = new Handler();
        final Runnable runnable = new Runnable() {
            @Override
            public void run() {
                if (alertDialog.isShowing()) {
                    alertDialog.dismiss();
                }
            }
        };

        alertDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                handler.removeCallbacks(runnable);
            }
        });

        // dialog timeout
        handler.postDelayed(runnable, alertWindowTimeout);
    }

    public void showYesNoWindow(Context context, final String title, final String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        if(title != null){
            builder.setTitle(title);
        }
        builder.setMessage(message);

        builder.setPositiveButton("Tak", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {
                // Do nothing but close the dialog
                setResponse("Tak");
                dialog.dismiss();
            }
        });

        builder.setNegativeButton("Nie", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

                setResponse("Nie");
                dialog.dismiss();
            }
        });

        AlertDialog alert = builder.create();
        alert.show();
    }
}
