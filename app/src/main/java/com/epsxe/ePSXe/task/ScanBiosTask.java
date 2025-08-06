package com.epsxe.ePSXe.task;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.text.SpannableString;
import android.text.method.LinkMovementMethod;
import android.text.util.Linkify;
import android.util.Log;
import android.widget.TextView;

import com.epsxe.ePSXe.R;
import com.epsxe.ePSXe.ePSXeApplication;
import com.epsxe.ePSXe.util.DialogUtil;
import com.epsxe.ePSXe.util.FileUtil;
import java.io.File;
import org.apache.http.cookie.ClientCookie;

/* loaded from: classes.dex */
public class ScanBiosTask extends AsyncTask<String, String, Integer> {
    private Context context;
    ProgressDialog dialog;
    private int hlemode;
    private int optionmode;
    private String biosnamefound = "";
    String[][] bioslist = {new String[]{"SCPH-1000/DTL-H1000 (Version 1.0 J)", "239665b1a3dade1b5a52c06338011044"}, new String[]{"SCPH-1001/DTL-H1201/DTL-H3001 (Version 2.2 12/04/95 A)", "924e392ed05558ffdb115408c263dccf"}, new String[]{"DTL-H1001 (Version 2.0 05/07/95 A)", "dc2b9bf8da62ec93e868cfd29f0d067d"}, new String[]{"DTL-H1002/SCPH-1002 (Version 2.0 05/10/95 E)", "54847e693405ffeb0359c6287434cbef"}, new String[]{"SCPH-3000/DTL-H1000H (Version 1.1 01/22/95)", "849515939161e62f6b866f6853006780"}, new String[]{"SCPH-3500 (Version 2.1 07/17/95 J)", "cba733ceeff5aef5c32254f1d617fa62"}, new String[]{"DTL-H1100 (Version 2.2 03/06/96 D)", "ca5cfc321f916756e3f0effbfaeba13b"}, new String[]{"DTL-H1101 (Version 2.1 07/17/95 A)", "da27e8b6dab242d8f91a9b25d80c63b8"}, new String[]{"SCPH-1002/DTL-H1102 (Version 2.1 07/17/95 E)", "417b34706319da7cf001e76e40136c23"}, new String[]{"SCPH-5000/DTL-H1200 (Version 2.2 12/04/95 J)", "57a06303dfa9cf9351222dfcbb4a29d9"}, new String[]{"SCPH-1002/DTL-H1202/DTL-H3002 (Version 2.2 12/04/95 E)", "e2110b8a2b97a8e0b857a45d32f7e187"}, new String[]{"SCPH-5500 (Version 3.0 09/09/96 J)", "8dd7d5296a650fac7319bce665a6a53c"}, new String[]{"SCPH-5501/SCPH-7003 (Version 3.0 11/18/96 A)", "490f666e1afb15b7362b406ed1cea246"}, new String[]{"SCPH-5502/SCPH-5552 (Version 3.0 01/06/97 E)", "32736f17079d0b2b7024407c39bd3050"}, new String[]{"SCPH-7000/SCPH-9000 (Version 4.0 08/18/97 J)", "8e4c14f567745eff2f0408c8129f72a6"}, new String[]{"SCPH-7001/SCPH-7501/SCPH-7503/SCPH-9001 (Version 4.1 12/16/97 A)", "1e68c231d0896b7eadcad1d7d8e76129"}, new String[]{"SCPH-7002/SCPH-7502/SCPH-9002 (Version 4.1 12/16/97 E)", "b9d9a0286c33dc6b7237bb13cd46fdee"}, new String[]{"SCPH-100 (Version 4.3 03/11/00 J)", "8abc1b549a4a80954addc48ef02c4521"}, new String[]{"SCPH-102 (Version 4.4 03/24/00 E)", "b10f5e0e3d9eb60e5159690680b1e774"}, new String[]{"SCPH-101 (Version 4.5 05/25/00 A)", "6e3735ff4c7dc899ee98981385f6f3d0"}, new String[]{"SCPH-102 (Version 4.5 05/25/00 E)", "de93caec13d1a141a40a79f5c86168d6"}};

    public ScanBiosTask(Context ctx, int hle, int mode) {
        this.hlemode = 2;
        this.optionmode = 0;
        this.context = ctx;
        this.hlemode = hle;
        this.optionmode = mode;
        this.dialog = new ProgressDialog(this.context);
        this.dialog.setTitle(R.string.bios_finding);
        this.dialog.show();
        this.dialog.setCancelable(false);
        Log.e("ScansdcardBiosTask", "start scan bios");
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void BiosDialog() {
        final Context mContext = this.context;
        TextView message = new TextView(mContext);
        SpannableString s = new SpannableString(mContext.getText(R.string.bios_dialog_message));
        message.setText(s);
        message.setMovementMethod(LinkMovementMethod.getInstance());
        message.setTextAppearance(this.context, android.R.style.TextAppearance_Medium);
        AlertDialog builder = new AlertDialog.Builder(mContext)
                .setTitle(R.string.bios_dialog_title)
                .setCancelable(true)
                .setIcon(android.R.drawable.ic_dialog_info)
                .setNegativeButton(R.string.bios_dialog_info, new DialogInterface.OnClickListener() { // from class: com.epsxe.ePSXe.task.ScanBiosTask.2
            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialog, int which) {
            }
        }).setPositiveButton(R.string.bios_dialog_skip, new DialogInterface.OnClickListener() { // from class: com.epsxe.ePSXe.task.ScanBiosTask.1
            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialog, int which) {
                SharedPreferences sharedPreferences = ePSXeApplication.getDefaultSharedPreferences(mContext);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("biosHlePref", "1");
                editor.commit();
            }
        }).setView(message).create();
        builder.show();
    }

    private void WelcomeDialog() {
        final Context mContext = this.context;
        TextView message = new TextView(mContext);
        SpannableString s = new SpannableString(mContext.getText(R.string.welcome_dialog_message));
        message.setText(s);
        message.setMovementMethod(LinkMovementMethod.getInstance());
        message.setTextAppearance(this.context, android.R.style.TextAppearance_Medium);
        AlertDialog builder = new AlertDialog.Builder(mContext).setTitle(R.string.welcome_dialog_title).setCancelable(true).setIcon(android.R.drawable.ic_dialog_info).setNegativeButton(R.string.welcome_dialog_info, new DialogInterface.OnClickListener() { // from class: com.epsxe.ePSXe.task.ScanBiosTask.4
            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialog, int which) {
                ScanBiosTask.this.BiosDialog();
            }
        }).setPositiveButton(R.string.welcome_dialog_skip, new DialogInterface.OnClickListener() { // from class: com.epsxe.ePSXe.task.ScanBiosTask.3
            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialog, int which) {
                SharedPreferences sharedPreferences = ePSXeApplication.getDefaultSharedPreferences(mContext);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("biosHlePref", "1");
                editor.commit();
            }
        }).setView(message).create();
        builder.show();
    }

    private void BiosFoundDialog(String BiosName) {
        Context mContext = this.context;
        TextView message = new TextView(mContext);
        SpannableString s = new SpannableString(mContext.getString(R.string.main_psxbios) + BiosName + "\n" + ((Object) mContext.getText(R.string.bios_found_dialog_message)));
        Linkify.addLinks(s, 1);
        message.setText(s);
        message.setMovementMethod(LinkMovementMethod.getInstance());
        AlertDialog builder = new AlertDialog.Builder(mContext).setTitle(R.string.bios_found_dialog_title).setCancelable(true).setIcon(android.R.drawable.ic_dialog_info).setPositiveButton(mContext.getString(R.string.dialog_action_ok), (DialogInterface.OnClickListener) null).setView(message).create();
        builder.show();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // android.os.AsyncTask
    public Integer doInBackground(String... params) {
        Log.e("ScansdcardBiosTask", "start scan bios 2");
        publishProgress("scanning folders...");
        findBios(params[0], 0);
        publishProgress("done...");
        return Integer.valueOf(findBios(params[0], 0));
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // android.os.AsyncTask
    public void onProgressUpdate(String... values) {
        this.dialog.setMessage(values[0]);
    }

    public void doProgress(String value) {
        publishProgress(value);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // android.os.AsyncTask
    public void onPostExecute(Integer result) {
        DialogUtil.closeDialog(this.dialog);
        if (result.intValue() == -1) {
            if (this.optionmode == 0) {
                WelcomeDialog();
                return;
            } else {
                BiosDialog();
                return;
            }
        }
        SharedPreferences sharedPreferences = ePSXeApplication.getDefaultSharedPreferences(this.context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("biosPref", this.biosnamefound);
        editor.putString("biosHlePref", "0");
        editor.commit();
        BiosFoundDialog(this.bioslist[result.intValue()][0]);
    }

    public int findBios(String path, int level) {
        if (level > 10) {
            return -1;
        }
        try {
            File f = new File(path);
            File[] fileList = f.listFiles();
            if (!f.getName().equals("DCIM") && !f.getName().equals("Camera") && !f.getName().equals("asec") && !f.getName().equals(ClientCookie.SECURE_ATTR) && !f.getName().equals("dev") && !f.getName().equals("obb") && !f.getName().equals("\\.lfs") && !f.getAbsolutePath().contains("/Android/data")) {
                publishProgress(f.getAbsolutePath());
                Log.e("findBios", "DirScan: [" + f.getAbsoluteFile() + "]" + f.getName());
                for (File file : fileList) {
                    try {
                        if (file.isDirectory()) {
                            int ret = findBios(file.getAbsolutePath(), level + 1);
                            if (ret != -1) {
                                return ret;
                            }
                        } else if (file.getName().toLowerCase().endsWith(".bin")) {
                            long msize = file.length();
                            if (msize == 524288) {
                                String md5sum = FileUtil.getMD5fromFile(file.getAbsolutePath());
                                Log.e("findBios", "Bios found: " + md5sum);
                                for (int n = 0; n < 21; n++) {
                                    if (this.bioslist[n][1].equals(md5sum)) {
                                        this.biosnamefound = file.getAbsolutePath();
                                        return n;
                                    }
                                }
                            } else {
                                continue;
                            }
                        } else {
                            continue;
                        }
                    } catch (Exception e) {
                        Log.e("findBios", "" + e.getMessage());
                        return -1;
                    }
                }
                return -1;
            }
            return -1;
        } catch (Exception e2) {
            Log.e("findBios", "" + e2.getMessage());
            return -1;
        }
    }
}
