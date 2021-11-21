package USER;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

public class Login extends AppCompatActivity {
    Button createacc,login;
    EditText mail,password;
    String user,email;
    TextView resetpassword;
    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login2);
        createacc=findViewById(R.id.reglog);
        login=findViewById(R.id.login1);
        mail=findViewById(R.id.email);
        resetpassword=findViewById(R.id.reset);
        password=findViewById(R.id.password);
        user=mail.getText().toString().trim();
        firebaseAuth=FirebaseAuth.getInstance();
        createacc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(),Registration.class);
                startActivity(intent);
                finish();
            }
        });
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String gmail=mail.getText().toString().trim();
                String password1=password.getText().toString().trim();
                if (gmail.isEmpty()){
                    mail.setError("Enter E-Mail id");
                    return;
                }
                if (password1.isEmpty()){
                    password.setError("Enter Password");
                    return;
                }

                firebaseAuth.signInWithEmailAndPassword(gmail,password1).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {

                        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                        String patient="patient";
                        if(user.isEmailVerified()){
                            if (user.getDisplayName().equals(patient)){
                                Intent intent1 = new Intent(getApplicationContext(),emptyactivity2.class);
                                intent1.putExtra("gmailid",gmail);
                                startActivity(intent1);
                                finish();

                            }else {
                                Toast.makeText(Login.this, "You belong to hospital end", Toast.LENGTH_SHORT).show();
                            }

                        }else{
                            Intent intent=new Intent(getApplicationContext(),Verification.class);
                            intent.putExtra("gmail",gmail);
                            intent.putExtra("password",password1);
                            startActivity(intent);
                            finish();
                            Toast.makeText(Login.this, "verification link has been sent", Toast.LENGTH_SHORT).show();
                        }





                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(Login.this, e.getMessage(), Toast.LENGTH_SHORT).show();

                    }
                });
            }
        });

        resetpassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               resetpassword();
            }
        });




    }

    private void resetpassword() {
        String mailid=mail.getText().toString().trim();
        if (mailid.isEmpty()){
            mail.setError("Enter E-Mail");
            return;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(mailid).matches()){
            mail.setError("Provide Valid E-Mail ID");
            return;
        }
        firebaseAuth.sendPasswordResetEmail(mailid).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    Toast.makeText(Login.this, "Password Reset Mail sent", Toast.LENGTH_SHORT).show();
                }

            }
        });

    }


}