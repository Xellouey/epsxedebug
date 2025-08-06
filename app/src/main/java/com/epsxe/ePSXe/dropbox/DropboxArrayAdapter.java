package com.epsxe.ePSXe.dropbox;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.epsxe.ePSXe.R;

import java.util.List;

/* loaded from: classes.dex */
public class DropboxArrayAdapter extends ArrayAdapter<OptionDropbox> {

    /* renamed from: c */
    private Context f150c;

    /* renamed from: id */
    private int f151id;
    private List<OptionDropbox> items;

    public DropboxArrayAdapter(Context context, int textViewResourceId, List<OptionDropbox> objects) {
        super(context, textViewResourceId, objects);
        this.f150c = context;
        this.f151id = textViewResourceId;
        this.items = objects;
    }

    @Override // android.widget.ArrayAdapter, android.widget.Adapter
    public OptionDropbox getItem(int i) {
        return this.items.get(i);
    }

    @Override // android.widget.ArrayAdapter, android.widget.Adapter
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        if (v == null) {
            LayoutInflater vi = (LayoutInflater) this.f150c.getSystemService("layout_inflater");
            v = vi.inflate(this.f151id, (ViewGroup) null);
        }
        OptionDropbox o = this.items.get(position);
        if (o != null) {
            TextView t1 = (TextView) v.findViewById(R.id.TextView01);
            TextView t2 = (TextView) v.findViewById(R.id.TextView02);
            TextView t3 = (TextView) v.findViewById(R.id.TextView03);
            if (t1 != null) {
                t1.setText(o.getName());
            }
            if (t2 != null) {
                t2.setText("Local: " + o.getLocal());
            }
            if (t3 != null) {
                t3.setText("Remote: " + o.getRemote());
            }
        }
        return v;
    }
}
