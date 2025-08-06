package com.epsxe.ePSXe.dialog;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.epsxe.ePSXe.R;
import com.epsxe.ePSXe.ePSXe;
import com.epsxe.ePSXe.jni.libepsxe;
import com.epsxe.ePSXe.task.MultiplayerClientTask;
import com.epsxe.ePSXe.util.DialogUtil;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/* loaded from: classes.dex */
public final class GetIPAddressDialog {
    private static final Pattern IP_ADDRESS = Pattern.compile("((25[0-5]|2[0-4][0-9]|[0-1][0-9]{2}|[1-9][0-9]|[1-9])\\.(25[0-5]|2[0-4][0-9]|[0-1][0-9]{2}|[1-9][0-9]|[1-9]|0)\\.(25[0-5]|2[0-4][0-9]|[0-1][0-9]{2}|[1-9][0-9]|[1-9]|0)\\.(25[0-5]|2[0-4][0-9]|[0-1][0-9]{2}|[1-9][0-9]|[0-9]))");

    public static void showGetIPAddressDialog(final ePSXe act, final libepsxe epsxe, final int serverMode, String ipSrv, final int cd, final String md) {
        AlertDialog.Builder alert = new AlertDialog.Builder(act);
        alert.setTitle(R.string.net_title);
        alert.setMessage(act.getString(R.string.net_msg));
        final EditText input = new EditText(act);
        alert.setView(input);
        if (ipSrv != null && !ipSrv.equals("")) {
            input.setText(ipSrv);
        }
        alert.setPositiveButton(act.getString(R.string.net_ok), new DialogInterface.OnClickListener() { // from class: com.epsxe.ePSXe.dialog.GetIPAddressDialog.1
            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialog, int whichButton) {
            }
        });
        alert.setNegativeButton(act.getString(R.string.net_cancel), new DialogInterface.OnClickListener() { // from class: com.epsxe.ePSXe.dialog.GetIPAddressDialog.2
            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialog, int whichButton) {
            }
        });
        final AlertDialog dialog = alert.create();
        dialog.show();
        dialog.getButton(-1).setOnClickListener(new View.OnClickListener() { // from class: com.epsxe.ePSXe.dialog.GetIPAddressDialog.3
            @Override // android.view.View.OnClickListener
            public void onClick(View v) {
                String ipServer = input.getText().toString();
                Matcher matcher = GetIPAddressDialog.IP_ADDRESS.matcher(ipServer);
                if (matcher.matches()) {
                    DialogUtil.closeDialog(dialog);
                    if (serverMode != 2) {
                        if (serverMode == 4) {
                            new MultiplayerClientTask(act, epsxe, serverMode, ipServer, cd, md).execute("");
                            return;
                        }
                        return;
                    }
                    act.runIso("___NETWORK___", 0, ipServer);
                    return;
                }
                Toast.makeText(act, "Wrong IP Address. Exit and try again", 0).show();
            }
        });
    }
}
