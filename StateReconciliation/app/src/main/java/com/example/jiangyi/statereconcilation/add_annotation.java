package com.example.jiangyi.statereconcilation;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by jiangyi on 09/07/2017.
 */

public class add_annotation extends AppCompatActivity implements View.OnClickListener{
    private Button done, del = null;
    private EditText anno1;
    private int seq, cur_seq;
    private TextView anno_prev, edited_time;
    final String ANNOTATION = "Annotation";
    final String DEL = "Delete";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_annotation);

        Intent intent = getIntent();
        String cur_anno = intent.getStringExtra("CUR_ANNO");
        String time = intent.getStringExtra("TIME");

        anno1 = (EditText) findViewById(R.id.anno1);
        done = (Button) findViewById(R.id.done);
        del = (Button) findViewById(R.id.del);
        anno_prev = (TextView) findViewById(R.id.anno_prev);
        edited_time = (TextView) findViewById(R.id.edited_time);

        anno_prev.setText(cur_anno);
        anno1.setText(cur_anno);
        edited_time.setText(time);

        done.setOnClickListener(this);
        del.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.done:
                Intent intent = new Intent(add_annotation.this, pic.class);
//                final String cur_time = getTime();
                String anno_added = gettext();
                if (!TextUtils.isEmpty(anno_added)) {
//                    intent.putExtra(TIME, cur_time);
                    intent.putExtra(ANNOTATION, anno_added);
                    setResult(0, intent);
                    finish();
                } else {
                    Toast toast = Toast.makeText(getApplicationContext(), "Empty Annotation", Toast.LENGTH_SHORT);
                    toast.show();
                }
                break;
            case R.id.del:
                Intent intent1 = new Intent(add_annotation.this, pic.class);
                intent1.putExtra(DEL, "delete");
                setResult(1, intent1);
                finish();
                break;
            default:
                break;
        }
    }

    public String gettext() {
        return anno1.getText().toString();
    }
}
