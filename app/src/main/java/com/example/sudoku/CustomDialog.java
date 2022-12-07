package com.example.sudoku;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.Window;
import android.widget.Button;

public class CustomDialog {
    private Context context;
    Dialog dlg;

    public CustomDialog(Context context) {
        this.context = context;
    }

    public void callFunction() {
        dlg = new Dialog(context);
        dlg.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dlg.setContentView(R.layout.dialog_numpad);
        dlg.show();
        dlg.setCancelable(false);

        Button cancelB = (Button) dlg.findViewById(R.id.d_cancelB);
        cancelB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dlgDismiss();
            }
        });
    }

    public void dlgDismiss() {
        dlg.dismiss();
    }
}
