package com.tuts.prakash.simpleocr;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

/**
 * @author Rashmi Kataria
 * 3/13/2020
 */
public class NewScreenActivity extends AppCompatActivity {

    TextView txt;
    TextView txt2;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_screen);
        txt=findViewById(R.id.text1);
        txt2=findViewById(R.id.text2);



        Intent intent=getIntent();

        Bundle bundle= intent.getExtras();

        assert bundle != null;
        String ent= bundle.getString("Entrega");
        String dni= bundle.getString("DNI");

        txt.setText(ent);
        txt2.setText(dni);

        Log.e("tag",ent);
        Log.e("tag",dni);


    }
}
