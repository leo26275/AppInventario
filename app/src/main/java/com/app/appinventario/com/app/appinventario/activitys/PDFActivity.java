package com.app.appinventario.com.app.appinventario.activitys;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.app.appinventario.R;
import com.github.barteksc.pdfviewer.PDFView;

public class PDFActivity extends AppCompatActivity {

    PDFView pdfView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdf);

        pdfView = (PDFView) findViewById(R.id.pdfView);
        pdfView.fromAsset("Manual pdf.pdf").load();
    }
}
