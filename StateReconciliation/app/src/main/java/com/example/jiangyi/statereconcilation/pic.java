package com.example.jiangyi.statereconcilation;

/**
 * Created by jiangyi on 09/07/2017.
 */

import com.example.jiangyi.statereconcilation.PictureTagView.Status;

import android.app.Activity;
import android.app.usage.NetworkStats;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.util.SparseArrayCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.SparseArray;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;


public class pic extends AppCompatActivity implements View.OnClickListener{
    private Button send = null;
    public int cur_pic_number = 0;
    final String ANNOTATION = "Annotation";
    final String TIME = "CURRENT";
    final String DEL = "Delete";
    String anno = "";
    String cur_time = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pic);

        Intent cur_intent = getIntent();
        final String pic_num = cur_intent.getStringExtra("pic_num");
        cur_pic_number = Integer.parseInt(pic_num);

        PictureTagLayout image = (PictureTagLayout) findViewById(R.id.image);

        if (cur_pic_number == 1){image.setBackgroundResource(R.drawable.pic1);}
        else if (cur_pic_number == 2){image.setBackgroundResource(R.drawable.pic2);}
        else if (cur_pic_number == 3){image.setBackgroundResource(R.drawable.pic3);}
        else if (cur_pic_number == 4){image.setBackgroundResource(R.drawable.pic4);}
        else if (cur_pic_number == 5){image.setBackgroundResource(R.drawable.pic5);}
        else if (cur_pic_number == 6){image.setBackgroundResource(R.drawable.pic6);}
        else if (cur_pic_number == 7){image.setBackgroundResource(R.drawable.pic7);}
        else {image.setBackgroundResource(R.drawable.pic8);}
        image.setNum(cur_pic_number);
        image.load(); //read /sdcard/info.txt and init the subviews(annotations)
//        image.write();

        send = (Button) findViewById(R.id.send);
        send.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.send:
                Intent intent = new Intent(pic.this, send.class);
                PictureTagLayout image = (PictureTagLayout) findViewById(R.id.image);
                String content = image.message();
                image.write(content);
                intent.putExtra("CONTENT", content);
                intent.putExtra("CUR_NUM", String.valueOf(cur_pic_number));
                startActivityForResult(intent,0);
                break;
            default:
                break;
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent annotation) {
        if (resultCode == 0) { //Done
            anno = annotation.getStringExtra(ANNOTATION);
            cur_time = annotation.getStringExtra(TIME);
            if (!TextUtils.isEmpty(anno)) {
                PictureTagLayout image = (PictureTagLayout) findViewById(R.id.image);
                image.setStatus(Status.Normal,anno);
            }
            super.onActivityResult(requestCode,resultCode,annotation);
        }
        else if (resultCode == 1) { //Del
            String del = annotation.getStringExtra(DEL);
            PictureTagLayout image = (PictureTagLayout) findViewById(R.id.image);
            image.setStatus(Status.Del,del);
            super.onActivityResult(requestCode,resultCode,annotation);
        }
        else if (resultCode == 2) { //Send Done
            super.onActivityResult(requestCode,resultCode,annotation);
        }
    }

    public void init() {
        InputStream is = pic.this.getClass().getClassLoader().
                getResourceAsStream("assets/" + "info.json");
        InputStreamReader streamReader = new InputStreamReader(is);
        BufferedReader reader = new BufferedReader(streamReader);
        String line;
        StringBuilder stringBuilder = new StringBuilder();
        try {
            while ((line = reader.readLine()) != null) {
                stringBuilder.append(line);
            }
            reader.close();
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            JSONObject info = new JSONObject(stringBuilder.toString());
            JSONObject anno_array = info.getJSONObject("ANNOTATION");
            JSONArray cur_pic_anno_array = anno_array.getJSONArray(cur_pic_number+"");
            if (cur_pic_anno_array.length()==0);
            else {
                for (int i = 0;i < cur_pic_anno_array.length(); i++) {
                    JSONObject temp = cur_pic_anno_array.getJSONObject(i);
                    int x = temp.getInt("x");
                    int y = temp.getInt("y");
                    String a = temp.getString("anno");

                    PictureTagLayout image = (PictureTagLayout) findViewById(R.id.image);
                    View view = image.addItem(x,y);
                    ((PictureTagView)view).setAnnotation(a);

                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public int getCur_pic_number() {
        return cur_pic_number;
    }

}
