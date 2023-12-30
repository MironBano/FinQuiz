package com.bano.finquiz;

import androidx.appcompat.app.AppCompatActivity;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.dynamiclinks.DynamicLink;
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;

public class DetalisActivity extends AppCompatActivity {
    View goBackView;
    Button goBackButton;

    TextView dynamicLinkTextView;
    String dynamicLinkUrl = "https://github.com/MironBano";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalis);

        goBackView = findViewById(R.id.goBackView);

        goBackButton = goBackView.findViewById(R.id.goBackButton);
        goBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        dynamicLinkTextView = findViewById(R.id.linkText);
        dynamicLinkTextView.setText("https://github.com/MironBano");

        DynamicLink dynamicLink = FirebaseDynamicLinks.getInstance().createDynamicLink()
                .setLink(Uri.parse(dynamicLinkUrl))
                .setDomainUriPrefix("https://github.com/MironBano")
                .buildDynamicLink();

        dynamicLinkTextView.setOnClickListener(view -> {
            Uri dynamicLinkUri = dynamicLink.getUri();
            Intent intent = new Intent(Intent.ACTION_VIEW, dynamicLinkUri);
            startActivity(intent);
        });
    }
}