package com.epsxe.ePSXe.dialog;

import android.app.AlertDialog;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.epsxe.ePSXe.IsoFileSelected;
import com.epsxe.ePSXe.OptionCD;
import com.epsxe.ePSXe.R;
import com.epsxe.ePSXe.cdArrayAdapter;
import com.epsxe.ePSXe.jni.libepsxe;
import com.epsxe.ePSXe.pbpFile;
import com.epsxe.ePSXe.util.DialogUtil;
import com.epsxe.ePSXe.util.PSXUtil;
import com.fasterxml.jackson.core.util.MinimalPrettyPrinter;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/* loaded from: classes.dex */
public final class ChangediscDialog {
    /* JADX INFO: Access modifiers changed from: private */
    public static List<OptionCD> fillCD(Context c, File f) {
        File[] dirs = f.listFiles();
        ArrayList arrayList = new ArrayList();
        List<OptionCD> dir = new ArrayList<>();
        try {
            for (File ff : dirs) {
                if (ff.isDirectory()) {
                    dir.add(new OptionCD(ff.getName(), "Folder", ff.getAbsolutePath(), 0));
                } else {
                    Log.e("folder", "File " + ff.getName());
                    if (PSXUtil.isIsoExtension(ff.getName())) {
                        long msize = ff.length() / 1048576;
                        if (msize > 2) {
                            if (ff.getName().toLowerCase().endsWith(".pbp")) {
                                pbpFile mfile = new pbpFile(ff.getAbsolutePath(), ff.getName());
                                int n = mfile.getNumFiles();
                                for (int i = 0; i < n; i++) {
                                    arrayList.add(new OptionCD(mfile.getFileName(i + 1), c.getString(R.string.main_filesize) + MinimalPrettyPrinter.DEFAULT_ROOT_VALUE_SEPARATOR + (msize / n) + " Mbytes", ff.getAbsolutePath(), i));
                                }
                            } else {
                                arrayList.add(new OptionCD(ff.getName(), c.getString(R.string.main_filesize) + MinimalPrettyPrinter.DEFAULT_ROOT_VALUE_SEPARATOR + msize + " Mbytes", ff.getAbsolutePath(), 0));
                            }
                        } else {
                            arrayList.add(new OptionCD(ff.getName(), c.getString(R.string.main_filesize) + MinimalPrettyPrinter.DEFAULT_ROOT_VALUE_SEPARATOR + ff.length() + " Bytes", ff.getAbsolutePath(), 0));
                        }
                    }
                }
            }
        } catch (Exception e) {
        }
        Collections.sort(dir);
        Collections.sort(arrayList);
        dir.addAll(arrayList);
        if (!f.getAbsolutePath().equalsIgnoreCase("/")) {
            dir.add(0, new OptionCD("..", "Parent Directory", f.getParent(), 0));
        }
        return dir;
    }

    public static void showChangediscDialog(final Context cont, final libepsxe e, String path, final IsoFileSelected mIsoName) {
        File currentCDDir = new File(path);
        final List<OptionCD> cdlist = fillCD(cont, currentCDDir);

        String currentName = mIsoName != null ? mIsoName.getmIsoName() : null;
        String ext = null;
        String prefix = null;
        if (currentName != null) {
            File currentFile = new File(currentName);
            String fileName = currentFile.getName();
            int dot = fileName.lastIndexOf('.');
            if (dot != -1) {
                ext = fileName.substring(dot).toLowerCase();
            }
            if (fileName.length() >= 2) {
                prefix = fileName.substring(0, 2).toLowerCase();
            }
        }

        List<OptionCD> filteredList = new ArrayList<>();
        for (OptionCD o : cdlist) {
            String name = o.getName().toLowerCase();
            if ((prefix == null || name.startsWith(prefix)) && (ext == null || name.endsWith(ext))
                && (currentName == null || !o.getPath().equals(currentName))){
                filteredList.add(o);
            }
        }

        final cdArrayAdapter cdadapter = new cdArrayAdapter(cont, R.layout.file_view, filteredList);
        ListView mListView = new ListView(cont);
        mListView.setAdapter((ListAdapter) cdadapter);
        AlertDialog.Builder builder = new AlertDialog.Builder(cont);
        builder.setView(mListView);
        final AlertDialog mAlert = builder.create();
        mAlert.show();

        final String finalPrefix = prefix;
        final String finalExt = ext;

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> listView, View itemView, int position, long itemId) {
                OptionCD o = cdadapter.getItem(position);
                if (o.getData().equalsIgnoreCase("folder") || o.getData().equalsIgnoreCase("parent directory")) {
                    File currentDir = new File(o.getPath());
                    cdlist.clear();
                    cdlist.addAll(ChangediscDialog.fillCD(cont, currentDir));
                    // Повторно фильтруем после перехода в папку
                    List<OptionCD> newFilteredList = new ArrayList<>();
                    for (OptionCD opt : cdlist) {
                        String n = opt.getName().toLowerCase();
                        if ((finalPrefix == null || n.startsWith(finalPrefix)) && (finalExt == null || n.endsWith(finalExt))) {
                            newFilteredList.add(opt);
                        }
                    }
                    cdadapter.clear();
                    cdadapter.addAll(newFilteredList);
                    cdadapter.notifyDataSetChanged();
                    return;
                }
                if (!o.getPath().equalsIgnoreCase("folder")) {
                    Toast.makeText(cont, String.valueOf(getDiscNumber(o.getName())), Toast.LENGTH_SHORT).show();
                    e.changedisc(o.getPath(), o.getSlot());
                    mIsoName.setmIsoName(o.getPath());
                    mIsoName.setmIsoSlot(o.getSlot());
                    DialogUtil.closeDialog(mAlert);

                }
            }
        });
    }
    public static void autoChangeToNextDiscIfTwo(Context context, libepsxe e, String currentPath, IsoFileSelected mIsoName) {
        File currentFile = new File(mIsoName.getmIsoName());
        String fileName = currentFile.getName();
        String ext = ".cue";
        String prefix = fileName.substring(0, 2).toLowerCase();
        File dir = currentFile.getParentFile();
        List<File> discs = new ArrayList<>();
        if (dir != null) {
            File[] files = dir.listFiles();
            if (files != null) {
                for (File f : files) {
                    String fname = f.getName().toLowerCase();
                    if (fname.startsWith(prefix) && fname.endsWith(ext)) {
                        discs.add(f);
                    }
                }
            }
        }
        if (discs.size() != 2) {
            // Toast.makeText(context, "Больше двух cue-дисков не найдено", Toast.LENGTH_SHORT).show();
            return;
        }
        int currentIndex = -1;
        for (int i = 0; i < discs.size(); i++) {
            if (discs.get(i).getName().equals(fileName)) {
                currentIndex = i;
                break;
            }
        }
        if (currentIndex == -1) {
            // Toast.makeText(context, "Не удалось найти текущий cue-диск", Toast.LENGTH_SHORT).show();
            return;
        }
        int nextIndex = (currentIndex == 0) ? 1 : 0;
        File nextDisc = discs.get(nextIndex);
        e.changedisc(nextDisc.getAbsolutePath(), 0);
        mIsoName.setmIsoName(nextDisc.getAbsolutePath());
        mIsoName.setmIsoSlot(0);
    }
    private static int getDiscNumber(String fileName) {
        java.util.regex.Pattern pattern = java.util.regex.Pattern.compile("(disc|disk|cd)[ _-]?(\\d+)", java.util.regex.Pattern.CASE_INSENSITIVE);
        java.util.regex.Matcher matcher = pattern.matcher(fileName);
        if (matcher.find()) {
            try {
                return Integer.parseInt(matcher.group(2));
            } catch (Exception ignore) {}
        }
        return -1;
    }
}
