package com.epsxe.ePSXe.dialog;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.epsxe.ePSXe.R;
import com.epsxe.ePSXe.ePSXe;
import com.epsxe.ePSXe.ePSXeHelp;
import com.epsxe.ePSXe.ePSXeReadPreferences;
import com.epsxe.ePSXe.ePSXeSupport;
import com.epsxe.ePSXe.ePSXeTerms;
import com.epsxe.ePSXe.util.DeviceUtil;
import com.epsxe.ePSXe.util.DialogUtil;
import com.epsxe.ePSXe.util.ReportUtil;

/* loaded from: classes.dex */
public final class AboutDialog {
    public static void showAboutDialog(Context mContext) {
        String version = DeviceUtil.getAppVersion(mContext);
        AlertDialog alertDialog = new AlertDialog.Builder(mContext).create();
        alertDialog.setTitle(R.string.main_aboutepsxe);
        alertDialog.setMessage("ePSXe (Enhanced PSX Emulator) v." + version + "\n[http://www.epsxe.com]\n\n____/ ePSXe Team \\____\ncalb, Galtor and _Demo_\n\nSpecial thanks! Roor, Expert, Doomed, Vood, Fennec, Jean-Ioup Gailly/Mark Adler, Zsknight, Zilmar, Jabo, Alex7/Burutter, 1964 team, Fpse team, Duddie, Tratax, i4get, Psychojak, Shunt, Willy, Shalma, Tikalat, David Muriel, Goi, Jose, and the 7zip team.\n\n_____| PSEmuPro Plugin & coders |_____\nPete, Lewpy, Kazzuya, JNS, Null2, Iori, Andy, Nickk, Barrett, Knack, linuzappz, Adrenaline,Nik, Segu, e}i{, Mathias Roslund, Edgbla, Shalma, Tikalat, Tapcio, Zenju, KrossX, Hyllian, Lottes ...\n\n___\\ ePSXe Testers Team /___\nGladiator, Pts, CDBuRnOuT, GreenImp, Wormie, squall-leonheart, sxamiga, emumaniac, crualfoxhound and Bnu.\n\n_| ePSXe Webmaster/Help file |__\nBobbi, _Demo_\n\n___\\ ePSXe Translations Team /___ \nNekokabu, Chow Chi Hoi, Ultra Taber, Thomas, Luca, Jesse,Duo Jeong, Marco Freire, Gabriel Franco, Insani Ramdhan, Eliterarking  \n\n___\\ ePSXe Android Art /__\n Robert Typek, Javier ~ JZX1673 \n");
        alertDialog.setButton(mContext.getString(R.string.dialog_action_ok), new DialogInterface.OnClickListener() { // from class: com.epsxe.ePSXe.dialog.AboutDialog.1
            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        alertDialog.setIcon(R.drawable.icon);
        alertDialog.show();
    }

    public static void showHelpDialog(final ePSXe epsxe, final ePSXeReadPreferences pref, final int x86, final int cores, final int bits) {
        String[] items = {epsxe.getString(R.string.menu_help_doc), epsxe.getString(R.string.menu_help_support), epsxe.getString(R.string.menu_help_report), epsxe.getString(R.string.menu_help_prefs), epsxe.getString(R.string.menu_help_privacy), epsxe.getString(R.string.menu_about)};
        ListView gListView = new ListView(epsxe);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(epsxe, android.R.layout.simple_list_item_1, items);
        gListView.setAdapter((ListAdapter) adapter);
        AlertDialog.Builder builder = new AlertDialog.Builder(epsxe);
        builder.setView(gListView);
        final AlertDialog helpAlert = builder.create();
        helpAlert.show();
        gListView.setOnItemClickListener(new AdapterView.OnItemClickListener() { // from class: com.epsxe.ePSXe.dialog.AboutDialog.2
            @Override // android.widget.AdapterView.OnItemClickListener
            public void onItemClick(AdapterView<?> listView, View itemView, int position, long itemId) {
                switch (position) {
                    case 0:
                        Intent myIntent = new Intent(epsxe, (Class<?>) ePSXeHelp.class);
                        epsxe.startActivity(myIntent);
                        epsxe.finish();
                        break;
                    case 1:
                        Intent myIntent2 = new Intent(epsxe, (Class<?>) ePSXeSupport.class);
                        epsxe.startActivity(myIntent2);
                        epsxe.finish();
                        break;
                    case 2:
                        ReportUtil.showReportShortPreferencesDialog(epsxe, pref, x86, cores, bits);
                        break;
                    case 3:
                        ReportUtil.showReportFullPreferencesDialog(epsxe, pref, x86, cores, bits);
                        break;
                    case 4:
                        Intent myIntent3 = new Intent(epsxe, (Class<?>) ePSXeTerms.class);
                        epsxe.startActivity(myIntent3);
                        epsxe.finish();
                        break;
                    case 5:
                        AboutDialog.showAboutDialog(epsxe);
                        break;
                }
                DialogUtil.closeDialog(helpAlert);
            }
        });
    }
}
