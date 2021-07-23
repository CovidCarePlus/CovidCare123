package HOSPITAL;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.example.covidcare.R;

public class TermsAndCondition extends AppCompatActivity {
    CheckBox checkBox;
    Button terms;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_terms_and_condition);
        checkBox=findViewById(R.id.check);
        terms=findViewById(R.id.terms);
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    terms.setEnabled(true);
                }else {
                    terms.setEnabled(false);
                }

            }
        });

        terms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(),HospitalRegister.class);
                startActivity(intent);
                finish();

            }
        });

    }
}