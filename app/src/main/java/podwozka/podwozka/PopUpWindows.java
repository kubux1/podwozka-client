package podwozka.podwozka;

import android.content.DialogInterface;
import android.content.Context;
import android.os.Handler;
import android.support.v7.app.AlertDialog;

public class PopUpWindows {

    // pass value in miliseconds
    int alertWindowTimeout = 1500;

    protected void showAlert(Context context, final String title, final String message){
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
}
