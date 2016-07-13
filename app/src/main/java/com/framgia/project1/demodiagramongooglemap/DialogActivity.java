package com.framgia.project1.demodiagramongooglemap;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.framgia.project1.demodiagramongooglemap.data.remote.DatabaseRemote;

public class DialogActivity extends AppCompatActivity {
    static int firstPoint, secondPoint;
    static int draw= 0;
    DatabaseRemote remote= new DatabaseRemote(this);
    EditText firstText, secondText;
    Button ok;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dialog);
        firstText=(EditText)findViewById(R.id.first_point);
        secondText=(EditText)findViewById(R.id.second_point);
        ok=(Button)findViewById(R.id.done);
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String first, second;
                first= firstText.getText().toString();
                second= secondText.getText().toString();
                if(first.length()>0&&second.length()>0){
                    firstPoint= Integer.parseInt(first);
                    secondPoint= Integer.parseInt(second);
                    draw= MapsActivity.statement;
                    finish();
                }
                else
                {
                    Toast.makeText(DialogActivity.this, "hay nhap day du cac o trong", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}