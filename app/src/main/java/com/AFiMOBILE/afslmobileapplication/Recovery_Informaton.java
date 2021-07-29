package com.AFiMOBILE.afslmobileapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class Recovery_Informaton extends AppCompatActivity {
    private Button BtnBktMovement , BtnvisitData , BtnDashboard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recovery_information);

        BtnBktMovement = findViewById(R.id.btnbktmovement);
        BtnvisitData   = findViewById(R.id.btnvisitinformastion);
        BtnDashboard   = findViewById(R.id.btncollectiondash);

        BtnBktMovement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent_bkt_mov = new Intent("android.intent.action.Recovery_Buket_Movement");
                startActivity(intent_bkt_mov);
            }
        });

        BtnvisitData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent_bkt_mov = new Intent("android.Recover_Visit_Informastion");
                startActivity(intent_bkt_mov);
            }
        });



    }


}
