package com.syberkeep.pdfboard;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.itextpdf.text.Document;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import com.syberkeep.pdfboard.helper.helper_main;
import com.syberkeep.pdfboard.helper.helper_pdf;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Tab1 extends Fragment implements FloatingActionButton.OnClickListener{

    View view;
    private FloatingActionButton fab, fab_1, fab_2, fab_3, fab_4, fab_5, fab_6;
    private EditText paragraph;
    private SharedPreferences sharedPref;
    private String folder;
    private String title;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.tab1_layout, container, false);
        init();
        return view;
    }

    public void init(){
        fab = (FloatingActionButton) view.findViewById(R.id.fab);
        fab_1 = (FloatingActionButton) view.findViewById(R.id.fab_1);
        fab_2 = (FloatingActionButton) view.findViewById(R.id.fab_2);
        fab_3 = (FloatingActionButton) view.findViewById(R.id.fab_3);
        fab_4 = (FloatingActionButton) view.findViewById(R.id.fab_4);
        fab_5 = (FloatingActionButton) view.findViewById(R.id.fab_5);
        fab_6 = (FloatingActionButton) view.findViewById(R.id.fab_6);
        fab.setOnClickListener(this);
        fab_1.setOnClickListener(this);
        fab_2.setOnClickListener(this);
        fab_3.setOnClickListener(this);
        fab_4.setOnClickListener(this);
        fab_5.setOnClickListener(this);
        fab_6.setOnClickListener(this);

        paragraph = (EditText) view.findViewById(R.id.paragraph);

        PreferenceManager.setDefaultValues(getActivity(), R.xml.user_settings, false);
        sharedPref = PreferenceManager.getDefaultSharedPreferences(getActivity());

        fab_1.setVisibility(View.INVISIBLE);
        fab_2.setVisibility(View.INVISIBLE);
        fab_3.setVisibility(View.INVISIBLE);
        fab_4.setVisibility(View.INVISIBLE);
        fab_5.setVisibility(View.INVISIBLE);
        fab_6.setVisibility(View.INVISIBLE);

        Intent intent = getActivity().getIntent();
        String action = intent.getAction();
        String type = intent.getType();

        if (Intent.ACTION_SEND.equals(action) && type != null) {
            if (type.startsWith("text/")) {
                handleSendText(intent); // Handle single image2 being sent
            }
        }
    }

    private void handleSendText(Intent intent) {
        String sharedText = intent.getStringExtra(Intent.EXTRA_TEXT);
        if (sharedText != null) {
            // Update UI to reflect text being shared
            paragraph.setText(sharedText);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.fab:
                createFabClicked();
                break;
            case R.id.fab_1:
                Toast.makeText(getActivity(), "F.A.B 1", Toast.LENGTH_SHORT).show();
                break;
            case R.id.fab_2:
                Toast.makeText(getActivity(), "F.A.B 2", Toast.LENGTH_SHORT).show();
                break;
            case R.id.fab_3:
                Toast.makeText(getActivity(), "F.A.B 3", Toast.LENGTH_SHORT).show();
                break;
            case R.id.fab_4:
                Toast.makeText(getActivity(), "F.A.B 4", Toast.LENGTH_SHORT).show();
                break;
            case R.id.fab_5:
                Toast.makeText(getActivity(), "F.A.B 5", Toast.LENGTH_SHORT).show();
                break;
            case R.id.fab_6:
                Toast.makeText(getActivity(), "F.A.B 6", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    public void createFabClicked(){
        String para = paragraph.getText().toString().trim();

        if(para.isEmpty()){
            Snackbar.make(paragraph, "Enter some text please!", Snackbar.LENGTH_LONG).show();
        } else {
            folder = sharedPref.getString("folder", "/Android/data/com.syberkeep.pdfboard/");

            android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(getActivity());
            View dialogView = View.inflate(getActivity(), R.layout.dialog_title, null);

            final EditText editTitle = (EditText) dialogView.findViewById(R.id.title);

            builder.setView(dialogView);
            builder.setTitle("Enter a title:");

            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {

                public void onClick(DialogInterface dialog, int whichButton) {

                    title = editTitle.getText().toString().trim() + ".pdf";
                    sharedPref.edit()
                            .putString("title", title)
                            .putString("pathPDF", Environment.getExternalStorageDirectory() +  folder + title)
                            .apply();
                    createPDF();

                    InputStream in;
                    OutputStream out;

                    try {

                        title = sharedPref.getString("title", null);
                        folder = sharedPref.getString("folder", "/Android/data/com.syberkeep.pdfboard/");
                        String path = sharedPref.getString("pathPDF", Environment.getExternalStorageDirectory() +
                                folder + title);

                        in = new FileInputStream(Environment.getExternalStorageDirectory() +  "/" + title);
                        out = new FileOutputStream(path);

                        byte[] buffer = new byte[1024];
                        int read;
                        while ((read = in.read(buffer)) != -1) {
                            out.write(buffer, 0, read);
                        }
                        in.close();

                        // write the output file
                        out.flush();
                        out.close();
                    } catch (Exception e) {
                        Log.e("Error 1", e.getMessage());
                    }

                    File pdfFile = new File(Environment.getExternalStorageDirectory() +  "/" + title);
                    if(pdfFile.exists()){
                        pdfFile.delete();
                    }
                    paragraph.setText("");
                    helper_pdf.pdf_textField(getActivity(), view);
                    helper_pdf.toolbar(getActivity());
                }
            });
            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

                public void onClick(DialogInterface dialog, int whichButton) {
                    dialog.cancel();
                }
            });
            builder.setNeutralButton("Date", null);

            final android.support.v7.app.AlertDialog dialog2 = builder.create();
            // Display the custom alert dialog on interface
            dialog2.setOnShowListener(new DialogInterface.OnShowListener() {

                @Override
                public void onShow(DialogInterface dialog) {

                    Button b = dialog2.getButton(AlertDialog.BUTTON_NEUTRAL);
                    b.setOnClickListener(new View.OnClickListener() {

                        @Override
                        public void onClick(View view) {
                            Date date = new Date();
                            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                            String dateNow = format.format(date);
                            editTitle.append(String.valueOf(dateNow));

                        }
                    });
                }
            });
            dialog2.show();

            new Handler().postDelayed(new Runnable() {
                public void run() {
                    helper_main.showKeyboard(getActivity(), editTitle);
                }
            }, 200);
        }
    }

    private void createPDF() {

        // Output file
        title = sharedPref.getString("title", null);
        folder = sharedPref.getString("folder", "/Android/data/de.baumann.pdf/");
        String outputPath = sharedPref.getString("pathPDF", Environment.getExternalStorageDirectory() +
                folder + title);

        // Run conversion
        final boolean result = convertToPdf(outputPath);

        // Notify the UI
        if (result) {
            Snackbar snackbar = Snackbar
                    .make(paragraph, "PDF successfully created!", Snackbar.LENGTH_LONG)
                    .setAction("Open?", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            File file = new File(helper_pdf.actualPath(getActivity()));
                            helper_main.openFile(getActivity(), file, "application/pdf", paragraph);
                        }
                    });
            snackbar.show();
        } else Snackbar.make(paragraph, "An error occurred!", Snackbar.LENGTH_LONG).show();
    }

    private boolean convertToPdf(String outputPdfPath) {
        try {

            String paragraph = this.paragraph.getText().toString().trim();

            // Create output file if needed
            File outputFile = new File(outputPdfPath);
            if (!outputFile.exists()) outputFile.createNewFile();

            Document document;
            if (sharedPref.getString ("rotateString", "portrait").equals("portrait")) {
                document = new Document(PageSize.A4);
            } else {
                document = new Document(PageSize.A4.rotate());
            }

            PdfWriter.getInstance(document, new FileOutputStream(outputFile));
            document.open();
            document.add (new Paragraph(paragraph));

            document.close();

            return true;
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return false;
    }

    @Override
    public void onResume() {
        super.onResume();    //To change body of overridden methods use File | Settings | File Templates.
        helper_pdf.pdf_textField(getActivity(), view);
        helper_pdf.toolbar(getActivity());
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser && isResumed()) {
            helper_pdf.pdf_textField(getActivity(), view);
            helper_pdf.toolbar(getActivity());
        }
    }

}