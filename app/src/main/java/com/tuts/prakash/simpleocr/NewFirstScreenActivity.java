package com.tuts.prakash.simpleocr;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

/**
 * @author Rashmi Kataria
 * 3/16/2020
 */
public class NewFirstScreenActivity extends AppCompatActivity implements View.OnClickListener {

    private static final int requestPermissionID = 101;
    TextView Content;
    TextView Result;
    TextView DNI;
    Button btnLaunch;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_new);

        btnLaunch = findViewById(R.id.btnLaunchScanner);
        Result = findViewById(R.id.textResult);
        Content = findViewById(R.id.textEntrega);
        DNI = findViewById(R.id.textDNI);

        btnLaunch.setOnClickListener(this);

/*        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();

        assert bundle != null;
        String ent = bundle.getString("Entrega");
        String dni = bundle.getString("DNI");

        Result.setText(ent); */


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == requestPermissionID) {
            String message = data.getStringExtra("Entrega");
            String messageDni = data.getStringExtra("DNI");
            if (message!=null && message.contains("Entrega")) {
                btnLaunch.setText(R.string.scan_dni);
                Result.setVisibility(View.VISIBLE);
                Content.setVisibility(View.VISIBLE);
                Content.setText(message);
            }
            else if (messageDni!=null && messageDni.contains("DNI")) {
                btnLaunch.setVisibility(View.GONE);
                DNI.setVisibility(View.VISIBLE);
                DNI.setText(messageDni);
            } else {
                btnLaunch.setVisibility(View.VISIBLE);
                Result.setVisibility(View.GONE);
                Content.setVisibility(View.GONE);
                DNI.setVisibility(View.GONE);
            }

        }

    }


    @Override
    public void onClick(View view) {
        Intent intent = (new Intent(this, NewOpenScanActivity.class));
        startActivityForResult(intent, requestPermissionID);
    }
}
