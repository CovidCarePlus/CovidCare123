package HOSPITAL;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.covidcare.R;

import USER.Registration;

public class REGandLOG extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_r_e_gand_l_o_g);

        Button Reg = findViewById(R.id.reg);
        Button Login = findViewById(R.id.login);

        Reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(getApplicationContext(), TermsAndCondition.class);
                startActivity(intent1);
                finish();
            }
        });

        Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent2 = new Intent(getApplicationContext(),HospitalLogin.class);
                startActivity(intent2);
                finish();
            }
        });
    }
}