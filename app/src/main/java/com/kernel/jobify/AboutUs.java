package com.kernel.jobify;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class AboutUs extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);

        String abc = "Jobify is a platform where we provide news about job openings available for All Fields \n\n" +
                "We are here to change the old system of consultancy services that are there to fool you and make you pay just for the jobs that you deserve \n\n" +
                "Jobify is news platform where you will get news of openings available and you can apply directly on the companies website or portal \n\n" +
                "At jobify we don't collect user data we will connect users directly to the recruiter of the companies that are providing jobs \n\n" +
                "Thanks For Downloading The App We Will Make Sure You Get The Job You Deserve.";

        TextView textView = findViewById(R.id.about_us);
        textView.setText(abc);

        Toolbar toolbar = findViewById(R.id.ustoolbar);
        toolbar.setNavigationIcon(R.drawable.ic_action_back);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
