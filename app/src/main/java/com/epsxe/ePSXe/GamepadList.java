package com.epsxe.ePSXe;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import androidx.core.view.InputDeviceCompat;
import android.util.Log;
import android.view.InputDevice;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.bda.controller.Controller;
import com.epsxe.ePSXe.util.DialogUtil;
import com.fasterxml.jackson.core.util.MinimalPrettyPrinter;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/* loaded from: classes.dex */
public class GamepadList extends ListActivity {
    private ListView gListView;
    private GamepadArrayAdapter gamepadadapter;
    private AlertDialog lAlert;
    private ePSXeReadPreferences mePSXeReadPreferences;
    private Controller mController = null;
    protected int player = 0;
    BroadcastReceiver mReceiver = null;

    @Override // android.app.Activity
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(4, 4);
        this.player = getIntent().getIntExtra("com.epsxe.ePSXe.player", 0);
        Log.e("epsxe", "parameter player" + this.player);
        drawGamepadList(this.player);
    }

    public void drawGamepadList(int player) {
        Log.e("drawGamepadList: ", " " + player);
        fillGamepadList(player);
        setContentView(R.layout.list_view);
    }

    private void alertdialog_automapgamepadext(final SharedPreferences.Editor e, final String n, final String v, final int pl, Context mCont, final GamepadDetection g) {
        this.gListView = new ListView(mCont);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(mCont, android.R.layout.simple_list_item_1, g.getGamepadListExt());
        this.gListView.setAdapter((ListAdapter) adapter);
        this.gListView.setOnItemClickListener(new AdapterView.OnItemClickListener() { // from class: com.epsxe.ePSXe.GamepadList.1
            @Override // android.widget.AdapterView.OnItemClickListener
            public void onItemClick(AdapterView<?> listView, View itemView, int position, long itemId) {
                if (position == 0) {
                    DialogUtil.closeDialog(GamepadList.this.lAlert);
                } else {
                    DialogUtil.closeDialog(GamepadList.this.lAlert);
                    g.saveGamepadPref(e, n, v, pl, 0, position);
                }
                e.commit();
                Intent myIntent = new Intent(GamepadList.this, (Class<?>) ePSXePreferences.class);
                myIntent.putExtra("com.epsxe.ePSXe.screen", "inputpreferences.controller" + (GamepadList.this.player + 1));
                GamepadList.this.startActivity(myIntent);
                GamepadList.this.finish();
            }
        });
        AlertDialog.Builder builder = new AlertDialog.Builder(mCont);
        builder.setTitle(R.string.main_gamepadtypetitle);
        builder.setView(this.gListView);
        builder.setCancelable(false);
        this.lAlert = builder.create();
        this.lAlert.show();
    }

    @Override // android.app.ListActivity
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        int p = this.player + 1;
        OptionGamepad o = this.gamepadadapter.getItem(position);
        String name = o.getName();
        String value = o.getValue();
        Log.e("GamepadList", "name: " + name);
        Log.e("GamepadList", "value: " + value);
        Log.e("GamepadList", "which: " + position);
        SharedPreferences sharedPreferences = ePSXeApplication.getDefaultSharedPreferences(v.getContext());
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("analog" + p + "padidPref", value);
        editor.putString("analog" + p + "paddescPref", name);
        editor.putString("analog" + p + "padvpIdPref", o.getVendorId() + ":" + o.getProductId());
        GamepadDetection gpd = new GamepadDetection(null, null);
        int pad = gpd.MatchGamepad(name, o.getVendorId(), o.getProductId());
        Log.e("ZZZ", "--> " + name + " vid " + o.getVendorId() + " pid " + o.getProductId() + " pad " + pad);
        if (pad == 0) {
            alertdialog_automapgamepadext(editor, name, value, p, v.getContext(), gpd);
            return;
        }
        if (pad > 0) {
            gpd.saveGamepadPref(editor, name, value, p, pad, -1);
        }
        editor.commit();
        Intent myIntent = new Intent(this, (Class<?>) ePSXePreferences.class);
        myIntent.putExtra("com.epsxe.ePSXe.screen", "inputpreferences.controller" + (this.player + 1));
        startActivity(myIntent);
        finish();
    }

    private void fillGamepadList(int player) {
        String descriptor;
        int p = player + 1;
        BluetoothAdapter bluetoothAdapter = null;
        setTitle(getString(R.string.gamepad_configplayer1) + p + getString(R.string.gamepad_configplayer2));
        List<OptionGamepad> fls = new ArrayList<>();
        if (this.mePSXeReadPreferences == null) {
            this.mePSXeReadPreferences = new ePSXeReadPreferences(this);
        }
        int listfilter = this.mePSXeReadPreferences.getInputlistfilter();
        if (player < 2) {
            fls.add(new OptionGamepad(getString(R.string.gamepad_virtual), "virtual", -1, -1));
        }
        fls.add(new OptionGamepad("None", "none", -1, -1));
        if (player == 0 && (Build.DEVICE.toLowerCase().contains("zeus") || Build.DEVICE.toLowerCase().contains("r800") || Build.DEVICE.toLowerCase().contains("z1i") || Build.DEVICE.toLowerCase().contains("so-01d") || Build.DEVICE.toLowerCase().contains("s0-01d"))) {
            fls.add(new OptionGamepad("xperiaplay", "###xperiaplay###1###", -1, -1));
        }
        int osVersion = Integer.parseInt(Build.VERSION.SDK);
        if (osVersion >= 12) {
            int productId = -1;
            int vendorId = -1;
            int[] deviceIds = InputDevice.getDeviceIds();
            for (int i = 0; i < deviceIds.length; i++) {
                InputDevice device = InputDevice.getDevice(deviceIds[i]);
                if (device != null) {
                    int sources = device.getSources();
                    if (osVersion >= 19) {
                        vendorId = device.getVendorId();
                        productId = device.getProductId();
                    }
                    if (listfilter == 0 || (16777232 & sources) == 16777232 || (sources & InputDeviceCompat.SOURCE_GAMEPAD) == 1025 || ((sources & 257) == 257 && device.getName().startsWith("GS "))) {
                        String s = device.toString();
                        int in = s.indexOf("Descriptor:");
                        if (in != -1) {
                            descriptor = s.substring(in + 12, s.indexOf(10, in + 12));
                            Log.i("Gamepad", "[" + descriptor + "]");
                        } else {
                            descriptor = "###" + device.getName() + "###" + deviceIds[i] + "###";
                        }
                        fls.add(new OptionGamepad(device.getName(), descriptor, vendorId, productId));
                    }
                }
            }
        }
        if (this.mePSXeReadPreferences.getInputBluez() == 1 && osVersion < 18 && (bluetoothAdapter = BluetoothAdapter.getDefaultAdapter()) != null) {
            Set<BluetoothDevice> pairedDevices = bluetoothAdapter.getBondedDevices();
            Log.e("GamepadList", "paired: " + pairedDevices.size());
            if (pairedDevices.size() > 0) {
                for (BluetoothDevice device2 : pairedDevices) {
                    String devName = device2.getName();
                    String devAddress = device2.getAddress();
                    if (devName == null) {
                        devName = "bluez";
                    }
                    if (device2 != null && device2.getBluetoothClass() != null && devAddress != null) {
                        Log.e("GamepadList", "name: " + devName + " address " + devAddress + "class " + device2.getBluetoothClass().getMajorDeviceClass());
                        if (device2.getBluetoothClass().getMajorDeviceClass() == 1280) {
                            fls.add(new OptionGamepad(devName, "###bluez###" + devAddress + "###", -1, -1));
                        }
                    }
                }
            }
        }
        this.mController = Controller.getInstance(this);
        if (this.mController != null) {
            Log.e("GamepadList", "Starting moga native driver...");
            try {
                GamepadMoga.init(this.mController, this);
                int connection = this.mController.getState(1);
                this.mController.getState(4);
                Log.e("GamepadList", "starting moga state " + connection);
                switch (this.mController.getState(4)) {
                    case 0:
                        fls.add(new OptionGamepad("Moga", "###moganative###ID###", -1, -1));
                        break;
                    case 1:
                        fls.add(new OptionGamepad("Moga Pro", "###moganative###ID###", -1, -1));
                        break;
                }
            } catch (Exception e) {
                Log.e("GamepadList", "Moga driver failed to init..." + e);
            }
        }
        this.gamepadadapter = new GamepadArrayAdapter(this, R.layout.file_viewos, fls);
        setListAdapter(this.gamepadadapter);
        if (bluetoothAdapter != null && bluetoothAdapter.isEnabled()) {
            Toast.makeText(this, R.string.gamepad_scan, 1).show();
            bluetoothAdapter.startDiscovery();
            this.mReceiver = new BroadcastReceiver() { // from class: com.epsxe.ePSXe.GamepadList.2
                @Override // android.content.BroadcastReceiver
                public void onReceive(Context context, Intent intent) {
                    String action = intent.getAction();
                    if ("android.bluetooth.device.action.FOUND".equals(action)) {
                        BluetoothDevice device3 = (BluetoothDevice) intent.getParcelableExtra("android.bluetooth.device.extra.DEVICE");
                        String name = device3.getName();
                        String addr = device3.getAddress();
                        if (name == null) {
                            name = "bluez";
                        }
                        Log.e("onreceive", "--> " + name + MinimalPrettyPrinter.DEFAULT_ROOT_VALUE_SEPARATOR + addr);
                        GamepadList.this.gamepadadapter.add(GamepadList.this.new OptionGamepad(name, "###bluez###" + addr + "###", -1, -1));
                    }
                }
            };
            IntentFilter filter = new IntentFilter("android.bluetooth.device.action.FOUND");
            registerReceiver(this.mReceiver, filter);
        }
    }

    private class OptionGamepad implements Comparable<OptionGamepad> {
        private String name;
        private int productId;
        private String value;
        private int vendorId;

        public OptionGamepad(String n, String v, int e, int p) {
            this.name = n;
            this.value = v;
            this.vendorId = e;
            this.productId = p;
        }

        public String getName() {
            return this.name;
        }

        public String getValue() {
            return this.value;
        }

        public int getProductId() {
            return this.productId;
        }

        public int getVendorId() {
            return this.vendorId;
        }

        @Override // java.lang.Comparable
        public int compareTo(OptionGamepad o) {
            if (this.name != null) {
                return this.name.toLowerCase().compareTo(o.getName().toLowerCase());
            }
            throw new IllegalArgumentException();
        }
    }

    public class GamepadArrayAdapter extends ArrayAdapter<OptionGamepad> {

        /* renamed from: c */
        private Context f129c;

        /* renamed from: id */
        private int f130id;
        private List<OptionGamepad> items;

        public GamepadArrayAdapter(Context context, int textViewResourceId, List<OptionGamepad> objects) {
            super(context, textViewResourceId, objects);
            this.f129c = context;
            this.f130id = textViewResourceId;
            this.items = objects;
        }

        @Override // android.widget.ArrayAdapter, android.widget.Adapter
        public OptionGamepad getItem(int i) {
            return this.items.get(i);
        }

        @Override // android.widget.ArrayAdapter, android.widget.Adapter
        public View getView(int position, View convertView, ViewGroup parent) {
            View v = convertView;
            if (v == null) {
                LayoutInflater vi = (LayoutInflater) this.f129c.getSystemService("layout_inflater");
                v = vi.inflate(this.f130id, (ViewGroup) null);
            }
            OptionGamepad o = this.items.get(position);
            if (o != null) {
                TextView t1 = (TextView) v.findViewById(R.id.TextView01);
                TextView t2 = (TextView) v.findViewById(R.id.TextView02);
                if (t1 != null) {
                    int e = o.getVendorId();
                    int p = o.getProductId();
                    if (e == -1 && p == -1) {
                        t1.setText(o.getName());
                    } else {
                        t1.setText("(" + e + ":" + p + ")" + o.getName());
                    }
                }
                if (t2 != null) {
                    if (o.getValue().equals("###moganative###ID###")) {
                        t2.setText("Moga Native Driver");
                    } else {
                        t2.setText(o.getValue());
                    }
                }
            }
            return v;
        }
    }

    @Override // android.app.Activity, android.view.KeyEvent.Callback
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == 4 && event.getRepeatCount() == 0) {
            Intent myIntent = new Intent(this, (Class<?>) ePSXePreferences.class);
            myIntent.putExtra("com.epsxe.ePSXe.screen", "inputpreferences.controller" + (this.player + 1));
            startActivity(myIntent);
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override // android.app.ListActivity, android.app.Activity
    protected void onDestroy() {
        if (this.mController != null) {
            this.mController.exit();
        }
        super.onDestroy();
        if (this.mReceiver != null) {
            unregisterReceiver(this.mReceiver);
        }
    }
}
