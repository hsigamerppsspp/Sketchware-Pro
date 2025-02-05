package mod.hilal.saif.activities.android_manifest;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.RippleDrawable;
import android.os.Build;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.sketchware.remod.Resources;

import java.util.ArrayList;
import java.util.HashMap;

import mod.SketchwareUtil;
import mod.hey.studios.util.Helper;
import mod.hilal.saif.android_manifest.ActComponentsDialog;
import mod.hilal.saif.lib.FileUtil;

import static mod.SketchwareUtil.getDip;

public class AndroidManifestInjectionDetails extends Activity {

    private static String ATTRIBUTES_FILE_PATH;
    private final ArrayList<HashMap<String, Object>> listMap = new ArrayList<>();
    private ListView listView;
    private String src_id;
    private String activityName;
    private String type;
    private String constant;

    private AlertDialog.Builder dia;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(Resources.layout.add_custom_attribute);

        if (getIntent().hasExtra("sc_id") && getIntent().hasExtra("file_name") && getIntent().hasExtra("type")) {
            src_id = getIntent().getStringExtra("sc_id");
            activityName = getIntent().getStringExtra("file_name").replaceAll(".java", "");
            type = getIntent().getStringExtra("type");
        }
        ATTRIBUTES_FILE_PATH = FileUtil.getExternalStorageDir().concat("/.sketchware/data/").concat(src_id).concat("/Injection/androidmanifest/attributes.json");
        setupConst();
        setToolbar();
        setupViews();
    }

    private void setupConst() {
        switch (type) {
            case "all":
                constant = "_apply_for_all_activities";
                break;

            case "application":
                constant = "_application_attrs";
                break;

            case "permission":
                constant = "_application_permissions";
                break;

            default:
                constant = activityName;
                break;
        }
    }

    private void setupViews() {
        FloatingActionButton fab = findViewById(Resources.id.add_attr_fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ///fab pressed
                showAddDial();
            }
        });
        listView = findViewById(Resources.id.add_attr_listview);
        refreshList();

    }

    private void refreshList() {
        listMap.clear();
        ArrayList<HashMap<String, Object>> data;
        if (FileUtil.isExistFile(ATTRIBUTES_FILE_PATH)) {
            data = new Gson().fromJson(FileUtil.readFile(ATTRIBUTES_FILE_PATH), Helper.TYPE_MAP_LIST);
            for (int i = 0; i < data.size(); i++) {
                String str = (String) data.get(i).get("name");
                if (str.equals(constant)) {
                    listMap.add(data.get(i));
                }
            }
            listView.setAdapter(new ListAdapter(listMap));
            ((BaseAdapter) listView.getAdapter()).notifyDataSetChanged();
        }
    }

    private void a(View view, int i2, int i3) {
        GradientDrawable gradientDrawable = new GradientDrawable();
        gradientDrawable.setShape(GradientDrawable.RECTANGLE);
        gradientDrawable.setCornerRadii(new float[]{(float) i2, (float) i2, (float) i2 / 2, (float) i2 / 2, (float) i2, (float) i2, (float) i2 / 2, (float) i2 / 2});
        gradientDrawable.setColor(Color.parseColor("#ffffff"));
        RippleDrawable rippleDrawable = new RippleDrawable(new ColorStateList(new int[][]{new int[0]}, new int[]{Color.parseColor("#20008DCD")}), gradientDrawable, null);
        if (Build.VERSION.SDK_INT >= 21) {
            view.setElevation((float) i3);
            view.setBackground(rippleDrawable);
            view.setClickable(true);
            view.setFocusable(true);
        }
    }

    private void showDial(int pos) {
        final AlertDialog create = new AlertDialog.Builder(this).create();
        View inflate = getLayoutInflater().inflate(Resources.layout.custom_dialog_attribute, null);
        create.setView(inflate);
        create.setCanceledOnTouchOutside(true);
        ///create.setCancelable(true);
        // ???
        create.getWindow().setBackgroundDrawableResource(17170445);
        final TextView textsave = inflate.findViewById(Resources.id.dialog_btn_save);
        final TextView textcancel = inflate.findViewById(Resources.id.dialog_btn_cancel);

        final EditText editText3 = inflate.findViewById(Resources.id.dialog_input_res);
        editText3.setVisibility(View.GONE);
        final EditText editText2 = inflate.findViewById(Resources.id.dialog_input_attr);
        editText2.setVisibility(View.GONE);
        final EditText editText = inflate.findViewById(Resources.id.dialog_input_value);
        final TextView textView = (TextView) ((ViewGroup) editText2.getParent()).getChildAt(0);
        textView.setText("Edit Value");
        editText.setText((String) listMap.get(pos).get("value"));
        editText.setHint("android:attr=\"value\"");
        final int position = pos;
        textsave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View _view) {
                listMap.get(position).put("value", editText.getText().toString());
                applyChange();
                create.dismiss();
                SketchwareUtil.toast("saved");
            }
        });

        textcancel.setOnClickListener(Helper.getDialogDismissListener(create));

        create.show();
    }

    private void showAddDial() {
        final AlertDialog create = new AlertDialog.Builder(this).create();
        View inflate = getLayoutInflater().inflate(Resources.layout.custom_dialog_attribute, null);
        create.setView(inflate);
        create.setCanceledOnTouchOutside(true);
        ///create.setCancelable(true);
        // ???
        create.getWindow().setBackgroundDrawableResource(17170445);
        final TextView textsave = inflate.findViewById(Resources.id.dialog_btn_save);
        final TextView textcancel = inflate.findViewById(Resources.id.dialog_btn_cancel);

        final EditText editText3 = inflate.findViewById(Resources.id.dialog_input_res);
        //editText3.setVisibility(View.GONE);
        if (type.equals("permission")) {
            editText3.setText("android");
        }
        final EditText editText2 = inflate.findViewById(Resources.id.dialog_input_attr);
        //editText2.setVisibility(View.GONE);
        if (type.equals("permission")) {
            editText2.setText("name");
        }
        final EditText editText = inflate.findViewById(Resources.id.dialog_input_value);
        if (type.equals("permission")) {
            editText3.setHint("permission");
        }
        final TextView textView = (TextView) ((ViewGroup) editText2.getParent()).getChildAt(0);
        if (type.equals("permission")) {
            textView.setText("Add new Permission");
        } else {
            textView.setText("Add new Attribute");
        }

        //editText.setText("");
        //editText.setHint("value");

        textsave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View _view) {
                String fstr = editText3.getText().toString().trim() + ":" + editText2.getText().toString().trim() + "=\"" + editText.getText().toString().trim() + "\"";
                HashMap<String, Object> map = new HashMap<>();
                map.put("name", constant);
                map.put("value", fstr);
                listMap.add(map);
                applyChange();
                create.dismiss();
                SketchwareUtil.toast("added");
            }
        });

        textcancel.setOnClickListener(Helper.getDialogDismissListener(create));

        create.show();
    }

    private void applyChange() {
        ArrayList<HashMap<String, Object>> data = new ArrayList<>();
        if (FileUtil.isExistFile(ATTRIBUTES_FILE_PATH)) {
            data = new Gson().fromJson(FileUtil.readFile(ATTRIBUTES_FILE_PATH), Helper.TYPE_MAP_LIST);
            ///int val = data.size()-1;
            for (int i = data.size() - 1; i > -1; i--) {
                String str = (String) data.get(i).get("name");
                if (str.equals(constant)) {
                    data.remove(i);
                }
                ///val--;
            }
            for (int i = 0; i < listMap.size(); i++) {
                data.add(listMap.get(i));
            }
        } else {
            for (int i = 0; i < listMap.size(); i++) {
                data.add(listMap.get(i));
            }
        }
        FileUtil.writeFile(ATTRIBUTES_FILE_PATH, new Gson().toJson(data));
        refreshList();
    }

    private TextView newText(String str, float size, int color, int width, int height, float weight) {
        TextView temp_card = new TextView(this);
        temp_card.setLayoutParams(new LinearLayout.LayoutParams(width, height, weight));
        temp_card.setPadding((int) getDip(4), (int) getDip(4), (int) getDip(4), (int) getDip(4));
        temp_card.setTextColor(color);
        temp_card.setText(str);
        temp_card.setTextSize(size);
        //temp_card.setTypeface(Typeface.DEFAULT_BOLD);
        //temp_card.setTypeface(Typeface.ITALIC);
        return temp_card;
    }

    private void setToolbar() {
        String str = "";
        switch (type) {
            case "all":
                str = "Attributes for all activities";
                break;

            case "application":
                str = "Application Attributes";
                break;

            case "permission":
                str = "Application Permissions";
                break;

            default:
                str = activityName;
                break;
        }
        ((TextView) findViewById(Resources.id.tx_toolbar_title)).setText(str);
        ViewGroup par = (ViewGroup) findViewById(Resources.id.tx_toolbar_title).getParent();
        ImageView _img = findViewById(Resources.id.ig_toolbar_back);
        _img.setOnClickListener(Helper.getBackPressedClickListener(this));
        //(String str , float size, int color, int width, int height, float weight){
        if (!str.equals("Attributes for all activities") && !str.equals("Application Attributes") && !str.equals("Application Permissions")) {
            // Feature description: allows to inject anything into the {@code activity} tag of the Activity
            // (yes, Command Blocks can do that too, but removing features is bad.)
            TextView actComponent = newText("Components ASD", 15, Color.parseColor("#ffffff"), -2, -2, 0);
            actComponent.setTypeface(Typeface.DEFAULT_BOLD);
            par.addView(actComponent);
            actComponent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ActComponentsDialog acd = new ActComponentsDialog(AndroidManifestInjectionDetails.this, src_id, activityName);
                    acd.show();
                }
            });
        }
    }

    private class ListAdapter extends BaseAdapter {

        private final ArrayList<HashMap<String, Object>> _data;

        public ListAdapter(ArrayList<HashMap<String, Object>> _arr) {
            _data = _arr;
        }

        @Override
        public int getCount() {
            return _data.size();
        }

        @Override
        public HashMap<String, Object> getItem(int position) {
            return _data.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = getLayoutInflater().inflate(Resources.layout.custom_view_attribute, null);
            }
            LinearLayout linearLayout = convertView.findViewById(Resources.id.cus_attr_layout);
            TextView textView = convertView.findViewById(Resources.id.cus_attr_text);
            final ImageView imageView = convertView.findViewById(Resources.id.cus_attr_btn);
            imageView.setVisibility(View.GONE);
            a(linearLayout, (int) getDip(4), (int) getDip(2));


            try {
                SpannableString spannableString = new SpannableString((String) _data.get(position).get("value"));
                spannableString.setSpan(new ForegroundColorSpan(0xff7a2e8c), 0, ((String) _data.get(position).get("value")).indexOf(":"), 33);
                spannableString.setSpan(new ForegroundColorSpan(0xff212121), ((String) _data.get(position).get("value")).indexOf(":"), ((String) _data.get(position).get("value")).indexOf("=") + 1, 33);
                spannableString.setSpan(new ForegroundColorSpan(0xff45a245), ((String) _data.get(position).get("value")).indexOf("\""), ((String) _data.get(position).get("value")).length(), 33);
                textView.setText(spannableString);
            } catch (Exception e) {
                textView.setText((String) _data.get(position).get("value"));
            }
            textView.setPadding((int) getDip(12), (int) getDip(12), (int) getDip(12), (int) getDip(12));
            textView.setTextSize(16);
            linearLayout.setVisibility(View.VISIBLE);

            linearLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ///activity pressed
                    showDial(position);
                }
            });

            linearLayout.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    ////activity long pressed

                    dia = new AlertDialog.Builder(AndroidManifestInjectionDetails.this);

                    dia.setTitle("Attribute");

                    dia.setMessage("do you want to delete this attribute?");

                    dia.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            listMap.remove(position);
                            applyChange();
                            SketchwareUtil.toast("deleted");
                        }
                    });

                    dia.setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
                    dia.show();


                    return true;
                }
            });

            ///end

            return convertView;
        }
    }
}





