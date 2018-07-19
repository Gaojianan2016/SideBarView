package com.gjn.sidebarview;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.gjn.sidebarviewlibrary.SideBarView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView textView = findViewById(R.id.textView);

        SideBarView sideBarView = findViewById(R.id.sbv);
        sideBarView.setShowTextView(textView);
    }
}
