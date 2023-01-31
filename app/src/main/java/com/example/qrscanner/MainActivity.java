package com.example.qrscanner;

import androidx.activity.result.ActivityResultLauncher;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;

public class MainActivity extends AppCompatActivity {

    Button btnScan;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnScan = findViewById(R.id.btn_scan);
        btnScan.setOnClickListener(v->
        {
            scanCode();
        });
    }

    /**
     * Scan l'écran
     */
    private void scanCode() {
        ScanOptions options = new ScanOptions();
        options.setPrompt("scanning ...");
        options.setBeepEnabled(true);
        options.setOrientationLocked(true);
        options.setCaptureActivity(CaptureAct.class);
        barLauncher.launch(options);
    }

    /**
     * Récupère le résultat du scan et l'affiche dans une boite de dialogue
     */
    ActivityResultLauncher<ScanOptions> barLauncher = registerForActivityResult(new ScanContract(), result -> {
        if (result.getContents() != null) {
            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            builder.setTitle("Résultat");
            builder.setMessage(result.getContents());
            builder.setPositiveButton("Go", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int which) {
                    String url = result.getContents();

                    Uri uri = Uri.parse(url);
                    if (url.contains("geo")) {
                        uri = Uri.parse("http://maps.google.com/maps?q=loc:" + url.replace("geo:", ""));
                    }
                    Intent urlIntent = new Intent(Intent.ACTION_VIEW, uri);

                    // Lance sur le lien
                    startActivity(urlIntent);
                }
            });

            builder.setNegativeButton("Annuler", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int which) {
                    dialogInterface.dismiss();
                }
            });

            builder.show();
        }
    });
}