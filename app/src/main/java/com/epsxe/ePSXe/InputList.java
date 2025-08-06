package com.epsxe.ePSXe;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import androidx.core.internal.view.SupportMenu;
import android.util.Log;
import android.util.SparseArray;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import com.fasterxml.jackson.core.util.MinimalPrettyPrinter;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

/* loaded from: classes.dex */
public class InputList extends ListActivity {
    public static final String[] ButtonDisplayNames;
    public static final String[] ButtonExtraDisplayNames;
    public static final int KEYCODE_0 = 7;
    public static final int KEYCODE_1 = 8;
    public static final int KEYCODE_2 = 9;
    public static final int KEYCODE_3 = 10;
    public static final int KEYCODE_3D_MODE = 206;
    public static final int KEYCODE_4 = 11;
    public static final int KEYCODE_5 = 12;
    public static final int KEYCODE_6 = 13;
    public static final int KEYCODE_7 = 14;
    public static final int KEYCODE_8 = 15;
    public static final int KEYCODE_9 = 16;
    public static final int KEYCODE_A = 29;
    public static final int KEYCODE_ALT_LEFT = 57;
    public static final int KEYCODE_ALT_RIGHT = 58;
    public static final int KEYCODE_APOSTROPHE = 75;
    public static final int KEYCODE_APP_SWITCH = 187;
    public static final int KEYCODE_AT = 77;
    public static final int KEYCODE_AVR_INPUT = 182;
    public static final int KEYCODE_AVR_POWER = 181;
    public static final int KEYCODE_AXIS_BRAKE = 10023;
    public static final int KEYCODE_AXIS_GAS = 10022;
    public static final int KEYCODE_AXIS_HAT_X = 10015;
    public static final int KEYCODE_AXIS_HAT_Y = 10016;
    public static final int KEYCODE_AXIS_HSCROLL = 10010;
    public static final int KEYCODE_AXIS_LTRIGGER = 10017;
    public static final int KEYCODE_AXIS_ORIENTATION = 10008;
    public static final int KEYCODE_AXIS_PRESSURE = 10002;
    public static final int KEYCODE_AXIS_RTRIGGER = 10018;
    public static final int KEYCODE_AXIS_RUDDER = 10020;
    public static final int KEYCODE_AXIS_RX = 10012;
    public static final int KEYCODE_AXIS_RY = 10013;
    public static final int KEYCODE_AXIS_RZ = 10014;
    public static final int KEYCODE_AXIS_SIZE = 10003;
    public static final int KEYCODE_AXIS_THROTTLE = 10019;
    public static final int KEYCODE_AXIS_TOOL_MAJOR = 10006;
    public static final int KEYCODE_AXIS_TOOL_MINOR = 10007;
    public static final int KEYCODE_AXIS_TOUCH_MAJOR = 10004;
    public static final int KEYCODE_AXIS_TOUCH_MINOR = 10005;
    public static final int KEYCODE_AXIS_VSCROLL = 10009;
    public static final int KEYCODE_AXIS_WHEEL = 10021;
    public static final int KEYCODE_AXIS_X = 10000;
    public static final int KEYCODE_AXIS_Y = 10001;
    public static final int KEYCODE_AXIS_Z = 10011;
    public static final int KEYCODE_B = 30;
    public static final int KEYCODE_BACK = 4;
    public static final int KEYCODE_BACKSLASH = 73;
    public static final int KEYCODE_BOOKMARK = 174;
    public static final int KEYCODE_BREAK = 121;
    public static final int KEYCODE_BUTTON_1 = 188;
    public static final int KEYCODE_BUTTON_10 = 197;
    public static final int KEYCODE_BUTTON_11 = 198;
    public static final int KEYCODE_BUTTON_12 = 199;
    public static final int KEYCODE_BUTTON_13 = 200;
    public static final int KEYCODE_BUTTON_14 = 201;
    public static final int KEYCODE_BUTTON_15 = 202;
    public static final int KEYCODE_BUTTON_16 = 203;
    public static final int KEYCODE_BUTTON_2 = 189;
    public static final int KEYCODE_BUTTON_3 = 190;
    public static final int KEYCODE_BUTTON_4 = 191;
    public static final int KEYCODE_BUTTON_5 = 192;
    public static final int KEYCODE_BUTTON_6 = 193;
    public static final int KEYCODE_BUTTON_7 = 194;
    public static final int KEYCODE_BUTTON_8 = 195;
    public static final int KEYCODE_BUTTON_9 = 196;
    public static final int KEYCODE_BUTTON_A = 96;
    public static final int KEYCODE_BUTTON_B = 97;
    public static final int KEYCODE_BUTTON_C = 98;
    public static final int KEYCODE_BUTTON_L1 = 102;
    public static final int KEYCODE_BUTTON_L2 = 104;
    public static final int KEYCODE_BUTTON_MODE = 110;
    public static final int KEYCODE_BUTTON_R1 = 103;
    public static final int KEYCODE_BUTTON_R2 = 105;
    public static final int KEYCODE_BUTTON_SELECT = 109;
    public static final int KEYCODE_BUTTON_START = 108;
    public static final int KEYCODE_BUTTON_THUMBL = 106;
    public static final int KEYCODE_BUTTON_THUMBR = 107;
    public static final int KEYCODE_BUTTON_X = 99;
    public static final int KEYCODE_BUTTON_Y = 100;
    public static final int KEYCODE_BUTTON_Z = 101;
    public static final int KEYCODE_C = 31;
    public static final int KEYCODE_CALL = 5;
    public static final int KEYCODE_CAMERA = 27;
    public static final int KEYCODE_CAPS_LOCK = 115;
    public static final int KEYCODE_CAPTIONS = 175;
    public static final int KEYCODE_CHANNEL_DOWN = 167;
    public static final int KEYCODE_CHANNEL_UP = 166;
    public static final int KEYCODE_CLEAR = 28;
    public static final int KEYCODE_COMMA = 55;
    public static final int KEYCODE_CTRL_LEFT = 113;
    public static final int KEYCODE_CTRL_RIGHT = 114;
    public static final int KEYCODE_D = 32;
    public static final int KEYCODE_DEL = 67;
    public static final int KEYCODE_DPADX_DOWN = 707;
    public static final int KEYCODE_DPADX_LEFT = 704;
    public static final int KEYCODE_DPADX_RIGHT = 705;
    public static final int KEYCODE_DPADX_UP = 706;
    public static final int KEYCODE_DPAD_CENTER = 23;
    public static final int KEYCODE_DPAD_DOWN = 20;
    public static final int KEYCODE_DPAD_LEFT = 21;
    public static final int KEYCODE_DPAD_RIGHT = 22;
    public static final int KEYCODE_DPAD_UP = 19;
    public static final int KEYCODE_DVR = 173;
    public static final int KEYCODE_E = 33;
    public static final int KEYCODE_ENDCALL = 6;
    public static final int KEYCODE_ENTER = 66;
    public static final int KEYCODE_ENVELOPE = 65;
    public static final int KEYCODE_EQUALS = 70;
    public static final int KEYCODE_ESCAPE = 111;
    public static final int KEYCODE_EXPLORER = 64;
    public static final int KEYCODE_F = 34;
    public static final int KEYCODE_F1 = 131;
    public static final int KEYCODE_F10 = 140;
    public static final int KEYCODE_F11 = 141;
    public static final int KEYCODE_F12 = 142;
    public static final int KEYCODE_F2 = 132;
    public static final int KEYCODE_F3 = 133;
    public static final int KEYCODE_F4 = 134;
    public static final int KEYCODE_F5 = 135;
    public static final int KEYCODE_F6 = 136;
    public static final int KEYCODE_F7 = 137;
    public static final int KEYCODE_F8 = 138;
    public static final int KEYCODE_F9 = 139;
    public static final int KEYCODE_FOCUS = 80;
    public static final int KEYCODE_FORWARD = 125;
    public static final int KEYCODE_FORWARD_DEL = 112;
    public static final int KEYCODE_FUNCTION = 119;
    public static final int KEYCODE_G = 35;
    public static final int KEYCODE_GRAVE = 68;
    public static final int KEYCODE_GUIDE = 172;
    public static final int KEYCODE_H = 36;
    public static final int KEYCODE_HEADSETHOOK = 79;
    public static final int KEYCODE_HOME = 3;
    public static final int KEYCODE_I = 37;
    public static final int KEYCODE_INFO = 165;
    public static final int KEYCODE_INSERT = 124;
    public static final int KEYCODE_J = 38;
    public static final int KEYCODE_K = 39;
    public static final int KEYCODE_L = 40;
    public static final int KEYCODE_LANGUAGE_SWITCH = 204;
    public static final int KEYCODE_LEFT_BRACKET = 71;
    public static final int KEYCODE_M = 41;
    public static final int KEYCODE_MANNER_MODE = 205;
    public static final int KEYCODE_MEDIA_CLOSE = 128;
    public static final int KEYCODE_MEDIA_EJECT = 129;
    public static final int KEYCODE_MEDIA_FAST_FORWARD = 90;
    public static final int KEYCODE_MEDIA_NEXT = 87;
    public static final int KEYCODE_MEDIA_PAUSE = 127;
    public static final int KEYCODE_MEDIA_PLAY = 126;
    public static final int KEYCODE_MEDIA_PLAY_PAUSE = 85;
    public static final int KEYCODE_MEDIA_PREVIOUS = 88;
    public static final int KEYCODE_MEDIA_RECORD = 130;
    public static final int KEYCODE_MEDIA_REWIND = 89;
    public static final int KEYCODE_MEDIA_STOP = 86;
    public static final int KEYCODE_MENU = 82;
    public static final int KEYCODE_META_LEFT = 117;
    public static final int KEYCODE_META_RIGHT = 118;
    public static final int KEYCODE_MINUS = 69;
    public static final int KEYCODE_MOVE_END = 123;
    public static final int KEYCODE_MOVE_HOME = 122;
    public static final int KEYCODE_MUTE = 91;
    public static final int KEYCODE_N = 42;
    public static final int KEYCODE_NOTIFICATION = 83;
    public static final int KEYCODE_NUM = 78;
    public static final int KEYCODE_NUMPAD_0 = 144;
    public static final int KEYCODE_NUMPAD_1 = 145;
    public static final int KEYCODE_NUMPAD_2 = 146;
    public static final int KEYCODE_NUMPAD_3 = 147;
    public static final int KEYCODE_NUMPAD_4 = 148;
    public static final int KEYCODE_NUMPAD_5 = 149;
    public static final int KEYCODE_NUMPAD_6 = 150;
    public static final int KEYCODE_NUMPAD_7 = 151;
    public static final int KEYCODE_NUMPAD_8 = 152;
    public static final int KEYCODE_NUMPAD_9 = 153;
    public static final int KEYCODE_NUMPAD_ADD = 157;
    public static final int KEYCODE_NUMPAD_COMMA = 159;
    public static final int KEYCODE_NUMPAD_DIVIDE = 154;
    public static final int KEYCODE_NUMPAD_DOT = 158;
    public static final int KEYCODE_NUMPAD_ENTER = 160;
    public static final int KEYCODE_NUMPAD_EQUALS = 161;
    public static final int KEYCODE_NUMPAD_LEFT_PAREN = 162;
    public static final int KEYCODE_NUMPAD_MULTIPLY = 155;
    public static final int KEYCODE_NUMPAD_RIGHT_PAREN = 163;
    public static final int KEYCODE_NUMPAD_SUBTRACT = 156;
    public static final int KEYCODE_NUM_LOCK = 143;
    public static final int KEYCODE_O = 43;
    public static final int KEYCODE_P = 44;
    public static final int KEYCODE_PAGE_DOWN = 93;
    public static final int KEYCODE_PAGE_UP = 92;
    public static final int KEYCODE_PERIOD = 56;
    public static final int KEYCODE_PICTSYMBOLS = 94;
    public static final int KEYCODE_PLUS = 81;
    public static final int KEYCODE_POUND = 18;
    public static final int KEYCODE_POWER = 26;
    public static final int KEYCODE_PROG_BLUE = 186;
    public static final int KEYCODE_PROG_GREEN = 184;
    public static final int KEYCODE_PROG_RED = 183;
    public static final int KEYCODE_PROG_YELLOW = 185;
    public static final int KEYCODE_Q = 45;
    public static final int KEYCODE_R = 46;
    public static final int KEYCODE_RIGHT_BRACKET = 72;
    public static final int KEYCODE_S = 47;
    public static final int KEYCODE_SCROLL_LOCK = 116;
    public static final int KEYCODE_SEARCH = 84;
    public static final int KEYCODE_SEMICOLON = 74;
    public static final int KEYCODE_SETTINGS = 176;
    public static final int KEYCODE_SHIFT_LEFT = 59;
    public static final int KEYCODE_SHIFT_RIGHT = 60;
    public static final int KEYCODE_SLASH = 76;
    public static final int KEYCODE_SOFT_LEFT = 1;
    public static final int KEYCODE_SOFT_RIGHT = 2;
    public static final int KEYCODE_SPACE = 62;
    public static final int KEYCODE_STAR = 17;
    public static final int KEYCODE_STB_INPUT = 180;
    public static final int KEYCODE_STB_POWER = 179;
    public static final int KEYCODE_SWITCH_CHARSET = 95;
    public static final int KEYCODE_SYM = 63;
    private static final SparseArray<String> KEYCODE_SYMBOLIC_NAMES = new SparseArray<>();
    public static final int KEYCODE_SYSRQ = 120;
    public static final int KEYCODE_T = 48;
    public static final int KEYCODE_TAB = 61;
    public static final int KEYCODE_TV = 170;
    public static final int KEYCODE_TV_INPUT = 178;
    public static final int KEYCODE_TV_POWER = 177;
    public static final int KEYCODE_U = 49;
    public static final int KEYCODE_UNKNOWN = 0;
    public static final int KEYCODE_V = 50;
    public static final int KEYCODE_VOLUME_DOWN = 25;
    public static final int KEYCODE_VOLUME_MUTE = 164;
    public static final int KEYCODE_VOLUME_UP = 24;
    public static final int KEYCODE_W = 51;
    public static final int KEYCODE_WINDOW = 171;
    public static final int KEYCODE_X = 52;
    public static final int KEYCODE_Y = 53;
    public static final int KEYCODE_Z = 54;
    public static final int KEYCODE_ZOOM_IN = 168;
    public static final int KEYCODE_ZOOM_OUT = 169;
    private static final int LAST_KEYCODE = 20;
    private InputArrayAdapter inputadapter;
    private ePSXeReadPreferences mePSXeReadPreferences;
    private int[][] keycodes = (int[][]) Array.newInstance((Class<?>) Integer.TYPE, 4, 21);
    private int[] keyspecialcodes = new int[16];
    protected int player = 0;

    static {
        populateKeycodeSymbolicNames();
        ButtonDisplayNames = new String[]{"Up", "Right", "Down", "Left", "Select", "Start", "Triangle", "Circle", "X", "Square", "L1", "L2", "R1", "R2", "LeftUp", "UpRight", "RightDown", "DownLeft", "L3", "R3", "Mode"};
        ButtonExtraDisplayNames = new String[]{"LoadState1", "LoadState2", "LoadState3", "LoadState4", "LoadState5", "SaveState1", "SaveState2", "SaveState3", "SaveState4", "SaveState5", "ToggleFramelimit", "Menu", "FastForward - Hold pressed", "Turbo - Hold pressed + button", "VolumeUp", "VolumeDown"};
    }

    private static void populateKeycodeSymbolicNames() {
        SparseArray<String> names = KEYCODE_SYMBOLIC_NAMES;
        names.append(0, "KEYCODE_UNKNOWN");
        names.append(1, "KEYCODE_SOFT_LEFT");
        names.append(2, "KEYCODE_SOFT_RIGHT");
        names.append(3, "KEYCODE_HOME");
        names.append(4, "KEYCODE_BACK");
        names.append(5, "KEYCODE_CALL");
        names.append(6, "KEYCODE_ENDCALL");
        names.append(7, "KEYCODE_0");
        names.append(8, "KEYCODE_1");
        names.append(9, "KEYCODE_2");
        names.append(10, "KEYCODE_3");
        names.append(11, "KEYCODE_4");
        names.append(12, "KEYCODE_5");
        names.append(13, "KEYCODE_6");
        names.append(14, "KEYCODE_7");
        names.append(15, "KEYCODE_8");
        names.append(16, "KEYCODE_9");
        names.append(17, "KEYCODE_STAR");
        names.append(18, "KEYCODE_POUND");
        names.append(19, "KEYCODE_DPAD_UP");
        names.append(20, "KEYCODE_DPAD_DOWN");
        names.append(21, "KEYCODE_DPAD_LEFT");
        names.append(22, "KEYCODE_DPAD_RIGHT");
        names.append(23, "KEYCODE_DPAD_CENTER");
        names.append(24, "KEYCODE_VOLUME_UP");
        names.append(25, "KEYCODE_VOLUME_DOWN");
        names.append(26, "KEYCODE_POWER");
        names.append(27, "KEYCODE_CAMERA");
        names.append(28, "KEYCODE_CLEAR");
        names.append(29, "KEYCODE_A");
        names.append(30, "KEYCODE_B");
        names.append(31, "KEYCODE_C");
        names.append(32, "KEYCODE_D");
        names.append(33, "KEYCODE_E");
        names.append(34, "KEYCODE_F");
        names.append(35, "KEYCODE_G");
        names.append(36, "KEYCODE_H");
        names.append(37, "KEYCODE_I");
        names.append(38, "KEYCODE_J");
        names.append(39, "KEYCODE_K");
        names.append(40, "KEYCODE_L");
        names.append(41, "KEYCODE_M");
        names.append(42, "KEYCODE_N");
        names.append(43, "KEYCODE_O");
        names.append(44, "KEYCODE_P");
        names.append(45, "KEYCODE_Q");
        names.append(46, "KEYCODE_R");
        names.append(47, "KEYCODE_S");
        names.append(48, "KEYCODE_T");
        names.append(49, "KEYCODE_U");
        names.append(50, "KEYCODE_V");
        names.append(51, "KEYCODE_W");
        names.append(52, "KEYCODE_X");
        names.append(53, "KEYCODE_Y");
        names.append(54, "KEYCODE_Z");
        names.append(55, "KEYCODE_COMMA");
        names.append(56, "KEYCODE_PERIOD");
        names.append(57, "KEYCODE_ALT_LEFT");
        names.append(58, "KEYCODE_ALT_RIGHT");
        names.append(59, "KEYCODE_SHIFT_LEFT");
        names.append(60, "KEYCODE_SHIFT_RIGHT");
        names.append(61, "KEYCODE_TAB");
        names.append(62, "KEYCODE_SPACE");
        names.append(63, "KEYCODE_SYM");
        names.append(64, "KEYCODE_EXPLORER");
        names.append(65, "KEYCODE_ENVELOPE");
        names.append(66, "KEYCODE_ENTER");
        names.append(67, "KEYCODE_DEL");
        names.append(68, "KEYCODE_GRAVE");
        names.append(69, "KEYCODE_MINUS");
        names.append(70, "KEYCODE_EQUALS");
        names.append(71, "KEYCODE_LEFT_BRACKET");
        names.append(72, "KEYCODE_RIGHT_BRACKET");
        names.append(73, "KEYCODE_BACKSLASH");
        names.append(74, "KEYCODE_SEMICOLON");
        names.append(75, "KEYCODE_APOSTROPHE");
        names.append(76, "KEYCODE_SLASH");
        names.append(77, "KEYCODE_AT");
        names.append(78, "KEYCODE_NUM");
        names.append(79, "KEYCODE_HEADSETHOOK");
        names.append(80, "KEYCODE_FOCUS");
        names.append(81, "KEYCODE_PLUS");
        names.append(82, "KEYCODE_MENU");
        names.append(83, "KEYCODE_NOTIFICATION");
        names.append(84, "KEYCODE_SEARCH");
        names.append(85, "KEYCODE_MEDIA_PLAY_PAUSE");
        names.append(86, "KEYCODE_MEDIA_STOP");
        names.append(87, "KEYCODE_MEDIA_NEXT");
        names.append(88, "KEYCODE_MEDIA_PREVIOUS");
        names.append(89, "KEYCODE_MEDIA_REWIND");
        names.append(90, "KEYCODE_MEDIA_FAST_FORWARD");
        names.append(91, "KEYCODE_MUTE");
        names.append(92, "KEYCODE_PAGE_UP");
        names.append(93, "KEYCODE_PAGE_DOWN");
        names.append(94, "KEYCODE_PICTSYMBOLS");
        names.append(95, "KEYCODE_SWITCH_CHARSET");
        names.append(96, "KEYCODE_BUTTON_A");
        names.append(97, "KEYCODE_BUTTON_B");
        names.append(98, "KEYCODE_BUTTON_C");
        names.append(99, "KEYCODE_BUTTON_X");
        names.append(100, "KEYCODE_BUTTON_Y");
        names.append(101, "KEYCODE_BUTTON_Z");
        names.append(102, "KEYCODE_BUTTON_L1");
        names.append(103, "KEYCODE_BUTTON_R1");
        names.append(104, "KEYCODE_BUTTON_L2");
        names.append(105, "KEYCODE_BUTTON_R2");
        names.append(106, "KEYCODE_BUTTON_THUMBL");
        names.append(107, "KEYCODE_BUTTON_THUMBR");
        names.append(108, "KEYCODE_BUTTON_START");
        names.append(109, "KEYCODE_BUTTON_SELECT");
        names.append(KEYCODE_BUTTON_MODE, "KEYCODE_BUTTON_MODE");
        names.append(KEYCODE_ESCAPE, "KEYCODE_ESCAPE");
        names.append(KEYCODE_FORWARD_DEL, "KEYCODE_FORWARD_DEL");
        names.append(KEYCODE_CTRL_LEFT, "KEYCODE_CTRL_LEFT");
        names.append(KEYCODE_CTRL_RIGHT, "KEYCODE_CTRL_RIGHT");
        names.append(KEYCODE_CAPS_LOCK, "KEYCODE_CAPS_LOCK");
        names.append(KEYCODE_SCROLL_LOCK, "KEYCODE_SCROLL_LOCK");
        names.append(KEYCODE_META_LEFT, "KEYCODE_META_LEFT");
        names.append(KEYCODE_META_RIGHT, "KEYCODE_META_RIGHT");
        names.append(KEYCODE_FUNCTION, "KEYCODE_FUNCTION");
        names.append(KEYCODE_SYSRQ, "KEYCODE_SYSRQ");
        names.append(KEYCODE_BREAK, "KEYCODE_BREAK");
        names.append(KEYCODE_MOVE_HOME, "KEYCODE_MOVE_HOME");
        names.append(KEYCODE_MOVE_END, "KEYCODE_MOVE_END");
        names.append(124, "KEYCODE_INSERT");
        names.append(KEYCODE_FORWARD, "KEYCODE_FORWARD");
        names.append(126, "KEYCODE_MEDIA_PLAY");
        names.append(127, "KEYCODE_MEDIA_PAUSE");
        names.append(128, "KEYCODE_MEDIA_CLOSE");
        names.append(KEYCODE_MEDIA_EJECT, "KEYCODE_MEDIA_EJECT");
        names.append(130, "KEYCODE_MEDIA_RECORD");
        names.append(KEYCODE_F1, "KEYCODE_F1");
        names.append(KEYCODE_F2, "KEYCODE_F2");
        names.append(KEYCODE_F3, "KEYCODE_F3");
        names.append(KEYCODE_F4, "KEYCODE_F4");
        names.append(KEYCODE_F5, "KEYCODE_F5");
        names.append(136, "KEYCODE_F6");
        names.append(KEYCODE_F7, "KEYCODE_F7");
        names.append(KEYCODE_F8, "KEYCODE_F8");
        names.append(KEYCODE_F9, "KEYCODE_F9");
        names.append(KEYCODE_F10, "KEYCODE_F10");
        names.append(KEYCODE_F11, "KEYCODE_F11");
        names.append(KEYCODE_F12, "KEYCODE_F12");
        names.append(KEYCODE_NUM_LOCK, "KEYCODE_NUM_LOCK");
        names.append(KEYCODE_NUMPAD_0, "KEYCODE_NUMPAD_0");
        names.append(KEYCODE_NUMPAD_1, "KEYCODE_NUMPAD_1");
        names.append(KEYCODE_NUMPAD_2, "KEYCODE_NUMPAD_2");
        names.append(KEYCODE_NUMPAD_3, "KEYCODE_NUMPAD_3");
        names.append(KEYCODE_NUMPAD_4, "KEYCODE_NUMPAD_4");
        names.append(KEYCODE_NUMPAD_5, "KEYCODE_NUMPAD_5");
        names.append(KEYCODE_NUMPAD_6, "KEYCODE_NUMPAD_6");
        names.append(KEYCODE_NUMPAD_7, "KEYCODE_NUMPAD_7");
        names.append(KEYCODE_NUMPAD_8, "KEYCODE_NUMPAD_8");
        names.append(KEYCODE_NUMPAD_9, "KEYCODE_NUMPAD_9");
        names.append(KEYCODE_NUMPAD_DIVIDE, "KEYCODE_NUMPAD_DIVIDE");
        names.append(KEYCODE_NUMPAD_MULTIPLY, "KEYCODE_NUMPAD_MULTIPLY");
        names.append(KEYCODE_NUMPAD_SUBTRACT, "KEYCODE_NUMPAD_SUBTRACT");
        names.append(KEYCODE_NUMPAD_ADD, "KEYCODE_NUMPAD_ADD");
        names.append(KEYCODE_NUMPAD_DOT, "KEYCODE_NUMPAD_DOT");
        names.append(KEYCODE_NUMPAD_COMMA, "KEYCODE_NUMPAD_COMMA");
        names.append(KEYCODE_NUMPAD_ENTER, "KEYCODE_NUMPAD_ENTER");
        names.append(KEYCODE_NUMPAD_EQUALS, "KEYCODE_NUMPAD_EQUALS");
        names.append(KEYCODE_NUMPAD_LEFT_PAREN, "KEYCODE_NUMPAD_LEFT_PAREN");
        names.append(KEYCODE_NUMPAD_RIGHT_PAREN, "KEYCODE_NUMPAD_RIGHT_PAREN");
        names.append(KEYCODE_VOLUME_MUTE, "KEYCODE_VOLUME_MUTE");
        names.append(KEYCODE_INFO, "KEYCODE_INFO");
        names.append(KEYCODE_CHANNEL_UP, "KEYCODE_CHANNEL_UP");
        names.append(KEYCODE_CHANNEL_DOWN, "KEYCODE_CHANNEL_DOWN");
        names.append(KEYCODE_ZOOM_IN, "KEYCODE_ZOOM_IN");
        names.append(KEYCODE_ZOOM_OUT, "KEYCODE_ZOOM_OUT");
        names.append(KEYCODE_TV, "KEYCODE_TV");
        names.append(KEYCODE_WINDOW, "KEYCODE_WINDOW");
        names.append(KEYCODE_GUIDE, "KEYCODE_GUIDE");
        names.append(KEYCODE_DVR, "KEYCODE_DVR");
        names.append(KEYCODE_BOOKMARK, "KEYCODE_BOOKMARK");
        names.append(KEYCODE_CAPTIONS, "KEYCODE_CAPTIONS");
        names.append(KEYCODE_SETTINGS, "KEYCODE_SETTINGS");
        names.append(KEYCODE_TV_POWER, "KEYCODE_TV_POWER");
        names.append(KEYCODE_TV_INPUT, "KEYCODE_TV_INPUT");
        names.append(180, "KEYCODE_STB_INPUT");
        names.append(KEYCODE_STB_POWER, "KEYCODE_STB_POWER");
        names.append(KEYCODE_AVR_POWER, "KEYCODE_AVR_POWER");
        names.append(KEYCODE_AVR_INPUT, "KEYCODE_AVR_INPUT");
        names.append(KEYCODE_PROG_RED, "KEYCODE_PROG_RED");
        names.append(KEYCODE_PROG_GREEN, "KEYCODE_PROG_GREEN");
        names.append(KEYCODE_PROG_YELLOW, "KEYCODE_PROG_YELLOW");
        names.append(KEYCODE_PROG_BLUE, "KEYCODE_PROG_BLUE");
        names.append(KEYCODE_APP_SWITCH, "KEYCODE_APP_SWITCH");
        names.append(KEYCODE_BUTTON_1, "KEYCODE_BUTTON_1");
        names.append(KEYCODE_BUTTON_2, "KEYCODE_BUTTON_2");
        names.append(KEYCODE_BUTTON_3, "KEYCODE_BUTTON_3");
        names.append(KEYCODE_BUTTON_4, "KEYCODE_BUTTON_4");
        names.append(KEYCODE_BUTTON_5, "KEYCODE_BUTTON_5");
        names.append(KEYCODE_BUTTON_6, "KEYCODE_BUTTON_6");
        names.append(KEYCODE_BUTTON_7, "KEYCODE_BUTTON_7");
        names.append(KEYCODE_BUTTON_8, "KEYCODE_BUTTON_8");
        names.append(KEYCODE_BUTTON_9, "KEYCODE_BUTTON_9");
        names.append(KEYCODE_BUTTON_10, "KEYCODE_BUTTON_10");
        names.append(KEYCODE_BUTTON_11, "KEYCODE_BUTTON_11");
        names.append(KEYCODE_BUTTON_12, "KEYCODE_BUTTON_12");
        names.append(200, "KEYCODE_BUTTON_13");
        names.append(201, "KEYCODE_BUTTON_14");
        names.append(202, "KEYCODE_BUTTON_15");
        names.append(203, "KEYCODE_BUTTON_16");
        names.append(KEYCODE_DPADX_LEFT, "KEYCODE_DPAD_LEFT");
        names.append(KEYCODE_DPADX_RIGHT, "KEYCODE_DPAD_RIGHT");
        names.append(KEYCODE_DPADX_UP, "KEYCODE_DPAD_UP");
        names.append(KEYCODE_DPADX_DOWN, "KEYCODE_DPAD_DOWN");
        names.append(10000, "KEYCODE_AXIS_X");
        names.append(10001, "KEYCODE_AXIS_Y");
        names.append(KEYCODE_AXIS_PRESSURE, "KEYCODE_AXIS_PRESSURE");
        names.append(KEYCODE_AXIS_SIZE, "KEYCODE_AXIS_SIZE");
        names.append(KEYCODE_AXIS_TOUCH_MAJOR, "KEYCODE_AXIS_TOUCH_MAJOR");
        names.append(KEYCODE_AXIS_TOUCH_MINOR, "KEYCODE_AXIS_TOUCH_MINOR");
        names.append(KEYCODE_AXIS_TOOL_MAJOR, "KEYCODE_AXIS_TOOL_MAJOR");
        names.append(KEYCODE_AXIS_TOOL_MINOR, "KEYCODE_AXIS_TOOL_MINOR");
        names.append(KEYCODE_AXIS_ORIENTATION, "KEYCODE_AXIS_ORIENTATION");
        names.append(KEYCODE_AXIS_VSCROLL, "KEYCODE_AXIS_VSCROLL");
        names.append(KEYCODE_AXIS_HSCROLL, "KEYCODE_AXIS_HSCROLL");
        names.append(KEYCODE_AXIS_Z, "KEYCODE_AXIS_Z");
        names.append(KEYCODE_AXIS_RX, "KEYCODE_AXIS_RX");
        names.append(KEYCODE_AXIS_RY, "KEYCODE_AXIS_RY");
        names.append(KEYCODE_AXIS_RZ, "KEYCODE_AXIS_RZ");
        names.append(KEYCODE_AXIS_HAT_X, "KEYCODE_AXIS_HAT_X");
        names.append(KEYCODE_AXIS_HAT_Y, "KEYCODE_AXIS_HAT_Y");
        names.append(KEYCODE_AXIS_LTRIGGER, "KEYCODE_AXIS_LTRIGGER");
        names.append(KEYCODE_AXIS_RTRIGGER, "KEYCODE_AXIS_RTRIGGER");
        names.append(KEYCODE_AXIS_THROTTLE, "KEYCODE_AXIS_THROTTLE");
        names.append(KEYCODE_AXIS_RUDDER, "KEYCODE_AXIS_RUDDER");
        names.append(KEYCODE_AXIS_WHEEL, "KEYCODE_AXIS_WHEEL");
        names.append(KEYCODE_AXIS_GAS, "KEYCODE_AXIS_GAS");
        names.append(KEYCODE_AXIS_BRAKE, "KEYCODE_AXIS_BRAKE");
    }

    public static String keyCodeToString(int keyCode) {
        String symbolicName = KEYCODE_SYMBOLIC_NAMES.get(keyCode);
        return symbolicName != null ? symbolicName : Integer.toString(keyCode);
    }

    @Override // android.app.Activity
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(4, 4);
        this.player = getIntent().getIntExtra("com.epsxe.ePSXe.player", 0);
        Log.e("epsxe", "parameter player" + this.player);
        drawButtonList(this.player);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void unmapButton(String buttonName, Context mcontext) {
        SharedPreferences sharedPreferences = ePSXeApplication.getDefaultSharedPreferences(mcontext);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(buttonName, -1);
        editor.commit();
    }

    public void drawButtonList(final int player) {
        Log.e("drawButtonList: ", " " + player);
        if (player < 10) {
            fillButtonList(player);
        } else {
            fillButtonExtraList(player);
        }
        setContentView(R.layout.list_view);
        getListView().setLongClickable(true);
        getListView().setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() { // from class: com.epsxe.ePSXe.InputList.1
            @Override // android.widget.AdapterView.OnItemLongClickListener
            public boolean onItemLongClick(AdapterView<?> parent, View v, int position, long id) {
                int p = player + 1;
                OptionInput o = InputList.this.inputadapter.getItem(position);
                Log.e("InputList", "name: " + o.getName());
                Log.e("InputList", "value: " + o.getValue());
                Log.e("InputList", "which: " + position);
                if (player < 10) {
                    InputList.this.unmapButton("P" + p + o.getName(), InputList.this);
                } else {
                    InputList.this.unmapButton(o.getName(), InputList.this);
                }
                o.setValue(InputList.this.getString(R.string.inputlist_unmapped));
                InputList.this.inputadapter.notifyDataSetChanged();
                return true;
            }
        });
    }

    @Override // android.app.ListActivity
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        int p = this.player + 1;
        OptionInput o = this.inputadapter.getItem(position);
        Log.e("InputList", "name: " + o.getName());
        Log.e("InputList", "value: " + o.getValue());
        Log.e("InputList", "which: " + position);
        Intent myIntent = new Intent(this, (Class<?>) InputMapping.class);
        if (this.player < 10) {
            myIntent.putExtra("com.epsxe.ePSXe.button", "P" + p + o.getName());
        } else {
            myIntent.putExtra("com.epsxe.ePSXe.button", o.getName());
        }
        startActivityForResult(myIntent, 1);
    }

    @Override // android.app.Activity
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == -1) {
            if (this.player >= 10) {
                fillButtonExtraList(this.player);
            } else {
                fillButtonList(this.player);
            }
        }
    }

    private void fillButtonList(int player) {
        int p = player + 1;
        setTitle(getString(R.string.inputlist_configplayer1) + MinimalPrettyPrinter.DEFAULT_ROOT_VALUE_SEPARATOR + p + MinimalPrettyPrinter.DEFAULT_ROOT_VALUE_SEPARATOR + getString(R.string.inputlist_configplayer2));
        List<OptionInput> fls = new ArrayList<>();
        if (this.mePSXeReadPreferences == null) {
            this.mePSXeReadPreferences = new ePSXeReadPreferences(this);
        }
        for (int i = 0; i < ButtonDisplayNames.length; i++) {
            this.keycodes[player][i] = this.mePSXeReadPreferences.getButtonKeycode(player, ButtonDisplayNames[i]);
            Log.e("epsxe", "keycode[" + player + "][" + i + "] = " + this.keycodes[player][i]);
            if (this.keycodes[player][i] < 0) {
                fls.add(new OptionInput(ButtonDisplayNames[i], getString(R.string.inputlist_unmapped)));
            } else {
                String name = keyCodeToString(this.keycodes[player][i] & SupportMenu.USER_MASK);
                if (name.startsWith("KEYCODE_")) {
                    name = name.substring(8);
                }
                fls.add(new OptionInput(ButtonDisplayNames[i], name.replace('_', ' ')));
            }
        }
        this.inputadapter = new InputArrayAdapter(this, R.layout.file_viewos, fls);
        setListAdapter(this.inputadapter);
    }

    private void fillButtonExtraList(int player) {
        setTitle(getString(R.string.inputextradesc));
        List<OptionInput> fls = new ArrayList<>();
        if (this.mePSXeReadPreferences == null) {
            this.mePSXeReadPreferences = new ePSXeReadPreferences(this);
        }
        for (int i = 0; i < ButtonExtraDisplayNames.length; i++) {
            this.keyspecialcodes[i] = this.mePSXeReadPreferences.getButtonKeycodeextra(i);
            Log.e("epsxe", "keyspecialcode[" + i + "] = " + this.keyspecialcodes[i]);
            if (this.keyspecialcodes[i] < 0) {
                fls.add(new OptionInput(ButtonExtraDisplayNames[i], getString(R.string.inputlist_unmapped)));
            } else {
                String name = keyCodeToString(this.keyspecialcodes[i] & SupportMenu.USER_MASK);
                if (name.startsWith("KEYCODE_")) {
                    name = name.substring(8);
                }
                fls.add(new OptionInput(ButtonExtraDisplayNames[i], name.replace('_', ' ')));
            }
        }
        this.inputadapter = new InputArrayAdapter(this, R.layout.file_viewos, fls);
        setListAdapter(this.inputadapter);
    }

    private class OptionInput implements Comparable<OptionInput> {
        private String name;
        private String value;

        public OptionInput(String n, String v) {
            this.name = n;
            this.value = v;
        }

        public String getName() {
            return this.name;
        }

        public String getValue() {
            return this.value;
        }

        public void setValue(String v) {
            this.value = v;
        }

        @Override // java.lang.Comparable
        public int compareTo(OptionInput o) {
            if (this.name != null) {
                return this.name.toLowerCase().compareTo(o.getName().toLowerCase());
            }
            throw new IllegalArgumentException();
        }
    }

    public class InputArrayAdapter extends ArrayAdapter<OptionInput> {

        /* renamed from: c */
        private Context f132c;

        /* renamed from: id */
        private int f133id;
        private List<OptionInput> items;

        public InputArrayAdapter(Context context, int textViewResourceId, List<OptionInput> objects) {
            super(context, textViewResourceId, objects);
            this.f132c = context;
            this.f133id = textViewResourceId;
            this.items = objects;
        }

        @Override // android.widget.ArrayAdapter, android.widget.Adapter
        public OptionInput getItem(int i) {
            return this.items.get(i);
        }

        @Override // android.widget.ArrayAdapter, android.widget.Adapter
        public View getView(int position, View convertView, ViewGroup parent) {
            View v = convertView;
            if (v == null) {
                LayoutInflater vi = (LayoutInflater) this.f132c.getSystemService("layout_inflater");
                v = vi.inflate(this.f133id, (ViewGroup) null);
            }
            OptionInput o = this.items.get(position);
            if (o != null) {
                TextView t1 = (TextView) v.findViewById(R.id.TextView01);
                TextView t2 = (TextView) v.findViewById(R.id.TextView02);
                if (t1 != null) {
                    t1.setText(o.getName());
                }
                if (t2 != null) {
                    t2.setText(o.getValue());
                }
            }
            return v;
        }
    }

    @Override // android.app.Activity, android.view.KeyEvent.Callback
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == 4 && event.getRepeatCount() == 0) {
            Intent myIntent = new Intent(this, (Class<?>) ePSXePreferences.class);
            if (this.player < 10) {
                myIntent.putExtra("com.epsxe.ePSXe.screen", "inputpreferences.controller" + (this.player + 1));
            } else {
                myIntent.putExtra("com.epsxe.ePSXe.screen", "inputpreferences.inputExtra");
            }
            startActivity(myIntent);
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }
}
