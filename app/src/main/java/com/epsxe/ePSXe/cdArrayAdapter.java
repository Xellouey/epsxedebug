package com.epsxe.ePSXe;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import java.util.List;

/* loaded from: classes.dex */
public class cdArrayAdapter extends ArrayAdapter<OptionCD> {

    /* renamed from: c */
    private Context f148c;

    /* renamed from: id */
    private int f149id;
    private List<OptionCD> items;

    public cdArrayAdapter(Context context, int textViewResourceId, List<OptionCD> objects) {
        super(context, textViewResourceId, objects);
        this.f148c = context;
        this.f149id = textViewResourceId;
        this.items = objects;
    }

    @Override // android.widget.ArrayAdapter, android.widget.Adapter
    public OptionCD getItem(int i) {
        return this.items.get(i);
    }

    public void updateData(List<OptionCD> objects) {
        this.items = objects;
    }

    @Override // android.widget.ArrayAdapter, android.widget.Adapter
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        if (v == null) {
            LayoutInflater vi = (LayoutInflater) this.f148c.getSystemService("layout_inflater");
            v = vi.inflate(this.f149id, (ViewGroup) null);
        }
        OptionCD o = this.items.get(position);
        if (o != null) {
            TextView t1 = (TextView) v.findViewById(R.id.TextView01);
            TextView t2 = (TextView) v.findViewById(R.id.TextView02);
            if (t1 != null) {
                String name = o.getName();
                int dot = name.lastIndexOf('.');
                if (dot > 0) {
                    name = name.substring(0, dot);
                }
                t1.setText(name);
            }
            if (t2 != null) {
                t2.setText(o.getData());
            }
        }
        return v;
    }
}
