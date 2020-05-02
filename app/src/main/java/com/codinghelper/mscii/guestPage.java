package com.codinghelper.mscii;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class guestPage extends AppCompatActivity {
    private Button website;
    private Button cutoff;
    private Button scholar;
    private Button fee;
    private Button placement;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guest_page);
        this.website=findViewById(R.id.website);
        this.cutoff=findViewById(R.id.cutoff);
        this.scholar=findViewById(R.id.scholar);
        this.fee=findViewById(R.id.fee);
        this.placement=findViewById(R.id.placementDrive);
        website.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openSite("http://www.marwaricollegeranchi.ac.in/");
            }
        });
        cutoff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               openCutoff("http://www.marwaricollegeranchi.com/dwn_admnotices.php");
            }
        });
        scholar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openScholar("https://ekalyan.cgg.gov.in/");

            }
        });
        fee.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openFee("http://www.marwaricollegeranchi.com/fee_structure.php");
            }
        });
        placement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openPlacement("http://marwaricollegeranchi.ac.in/Placement.aspx");
            }
        });
    }
    public void  openSite(String url) {
        Uri uri = Uri.parse(url);
        Intent launchWeb = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(launchWeb);
    }
    public void  openCutoff(String url) {
        Uri uri = Uri.parse(url);
        Intent launchWeb = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(launchWeb);
    }
    public void  openScholar(String url) {
        Uri uri = Uri.parse(url);
        Intent launchWeb = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(launchWeb);
    }
    public void  openFee(String url) {
        Uri uri = Uri.parse(url);
        Intent launchWeb = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(launchWeb);
    }
    public void  openPlacement(String url) {
        Uri uri = Uri.parse(url);
        Intent launchWeb = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(launchWeb);
    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }
}
