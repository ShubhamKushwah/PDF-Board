package com.syberkeep.pdfboard;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;

public class Tab2 extends Fragment implements View.OnClickListener{

    private View rootView;
    private EditText editTextContent;
    private FloatingActionButton fabDone, fabPlus, fabMinus, fabDir;
    private int def_text_size = 20;
    private EditText font_size_text;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.tab2_layout, container, false);
        init();
        return rootView;
    }

    private void init(){
        fabDone = (FloatingActionButton) rootView.findViewById(R.id.fab_done);
        fabPlus = (FloatingActionButton) rootView.findViewById(R.id.fab_plus);
        fabMinus = (FloatingActionButton) rootView.findViewById(R.id.fab_minus);
        fabDir = (FloatingActionButton) rootView.findViewById(R.id.fab_dir);
        editTextContent = (EditText) rootView.findViewById(R.id.edit_pdf_text);
        font_size_text = (EditText) rootView.findViewById(R.id.font_size_text);

        fabDone.setOnClickListener(this);
        fabPlus.setOnClickListener(this);
        fabMinus.setOnClickListener(this);
        fabDir.setOnClickListener(this);

        font_size_text.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (Integer.valueOf(v.getText().toString()) < 12) {
                    font_size_text.setText(String.valueOf(12));
                    def_text_size = 12;
                    changeTextSize(def_text_size);
                }
                else if (Integer.valueOf(v.getText().toString()) > 50) {
                    font_size_text.setText(String.valueOf(50));
                    def_text_size = 50;
                    changeTextSize(def_text_size);
                }
                else {
                    def_text_size = Integer.valueOf(font_size_text.getText().toString());
                    changeTextSize(def_text_size);
                }
                return false;
            }
        });

        font_size_text.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (Integer.valueOf(font_size_text.getText().toString()) < 12) {
                    font_size_text.setText(String.valueOf(12));
                    def_text_size = 12;
                    changeTextSize(def_text_size);
                }
                else if (Integer.valueOf(font_size_text.getText().toString()) > 50) {
                    font_size_text.setText(String.valueOf(50));
                    def_text_size = 50;
                    changeTextSize(def_text_size);
                }
                else {
                    def_text_size = Integer.valueOf(font_size_text.getText().toString());
                    changeTextSize(def_text_size);
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.fab_done:
                createPDF();
                break;
            case R.id.fab_plus:
                if(def_text_size>=12 && def_text_size<50)
                    changeTextSize(++def_text_size);
                break;
            case R.id.fab_minus:
                if(def_text_size>12 && def_text_size<=50)
                    changeTextSize(--def_text_size);
                break;
            case R.id.fab_dir:
                createDirectory("Shubham");
                break;
        }
    }

    private void createDirectory(String folder_name) {
        SharedPreferences sharedPref;
        sharedPref = PreferenceManager.getDefaultSharedPreferences(getActivity());
        if (sharedPref.getBoolean ("folderDef", false)){
            sharedPref.edit().putString("folder", "/Android/data/com.syberkeep.pdfboard/").apply();
        }

        String folder = sharedPref.getString("folder", "/Android/data/com.syberkeep.pdfboard/");

        File directory = new File(Environment.getExternalStorageDirectory() + folder + "/" + folder_name + "/");
        if (!directory.exists()) {
            boolean is = directory.mkdirs();

            if(is)
                Toast.makeText(getActivity(), "Folder created!", Toast.LENGTH_SHORT).show();
            else
                Toast.makeText(getActivity(), "Unable to create folder!", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(getActivity(), "Folder already exists!", Toast.LENGTH_SHORT).show();
        }
    }

    private void changeTextSize(int newSize){
        editTextContent.setTextSize(TypedValue.COMPLEX_UNIT_SP, newSize);
        font_size_text.setText(String.valueOf(newSize));
    }

    private void createPDF() {
        Toast.makeText(getActivity(), "Creating PDF!", Toast.LENGTH_SHORT).show();
    }
}
