package com.epsxe.ePSXe;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.fasterxml.jackson.core.util.MinimalPrettyPrinter;
import java.util.List;

/* loaded from: classes.dex */
public class sstateArrayAdapter extends ArrayAdapter<OptionSstate> {

    /* renamed from: c */
    private Context f187c;

    /* renamed from: id */
    private int f188id;
    private List<OptionSstate> items;

    public sstateArrayAdapter(Context context, int textViewResourceId, List<OptionSstate> objects) {
        super(context, textViewResourceId, objects);
        this.f187c = context;
        this.f188id = textViewResourceId;
        this.items = objects;
    }

    @Override // android.widget.ArrayAdapter, android.widget.Adapter
    public OptionSstate getItem(int i) {
        return this.items.get(i);
    }

    @Override // android.widget.ArrayAdapter, android.widget.Adapter
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        if (v == null) {
            LayoutInflater vi = (LayoutInflater) this.f187c.getSystemService("layout_inflater");
            v = vi.inflate(this.f188id, (ViewGroup) null);
        }
        OptionSstate o = this.items.get(position);
        if (o != null) {
            TextView t1 = (TextView) v.findViewById(R.id.TextView01);
            TextView t2 = (TextView) v.findViewById(R.id.TextView02);
            TextView t3 = (TextView) v.findViewById(R.id.TextView03);
            TextView t4 = (TextView) v.findViewById(R.id.TextView04);
            ImageView v1 = (ImageView) v.findViewById(R.id.image);
            if (t1 != null) {
                t1.setText(o.getName());
            }
            if (t2 != null) {
                int slot = Integer.parseInt(o.getSlot());
                if (slot == -1) {
                    t2.setText("");
                } else if (slot == 5) {
                    t2.setText(this.f187c.getString(R.string.main_autosave));
                } else {
                    t2.setText(this.f187c.getString(R.string.main_slotnumber) + MinimalPrettyPrinter.DEFAULT_ROOT_VALUE_SEPARATOR + slot);
                }
            }
            if (t3 != null) {
                t3.setText(o.getData());
            }
            if (t4 != null) {
                t4.setText(o.getDate());
            }
            if (v1 != null && o.getBitmap() != null) {
                v1.setImageBitmap(o.getBitmap());
            }
        }
        return v;
    }
}
