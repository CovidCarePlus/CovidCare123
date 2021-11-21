package USER;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.covidcare.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.time.LocalDate;

public class Verification extends AppCompatActivity {
      TextView verify,confirmation,sent;
      Button vbtn;
      String mailgid,pass,name,parts11;
      RelativeLayout relativeLayout;
      DatabaseReference date;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verification);
        verify = findViewById(R.id.verify);
        confirmation=findViewById(R.id.confirmation);
        relativeLayout=findViewById(R.id.relative);
        sent=findViewById(R.id.sent);
        vbtn = findViewById(R.id.verifybtn);
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        mailgid =getIntent().getStringExtra("gmail");
        pass  = getIntent().getStringExtra("password");
        name=getIntent().getStringExtra("name");
        String[] parts = mailgid.split("(?=@)");
        parts11 = parts[0];
        relativeLayout.setVisibility(View.INVISIBLE);
        sent.setVisibility(View.INVISIBLE);
        verify.setText(mailgid);
        if (!mailgid.isEmpty()){
            Toast.makeText(this, "Verify Your mail to enter", Toast.LENGTH_SHORT).show();
            vbtn.setVisibility(View.VISIBLE);
            vbtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    firebaseAuth.signInWithEmailAndPassword(mailgid,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                                if(user.isEmailVerified()){
                                    date= FirebaseDatabase.getInstance().getReference().child("Update").child(parts11).child("Date");
                                    LocalDate localDate=LocalDate.now();
                                    String threeday=localDate.toString();
                                    date.setValue(threeday);
                                    Intent intent1 = new Intent(getApplicationContext(),emptyactivity.class);
                                    intent1.putExtra("gmailid",mailgid);
                                    intent1.putExtra("name",name);
                                    startActivity(intent1);
                                    finish();
                                }else{
                                    user.sendEmailVerification();
                                    vbtn.setText("CONTINUE");
                                    confirmation.setText("After Successful Verification,click Continue button");
                                    relativeLayout.setVisibility(View.VISIBLE);
                                    sent.setVisibility(View.VISIBLE);
                                }
                        }
                        }
                    });
                }
            });
        }else{
            vbtn.setVisibility(View.INVISIBLE);

        }

    }
}