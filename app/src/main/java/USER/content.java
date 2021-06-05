package USER;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.renderscript.Sampler;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.covidcare.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;

public class content extends AppCompatActivity {
    TextView name,beds,oxygen;
    Button book;
    String pa;
    String hospital;
    Integer i;
    String city;
    String patient;
    DocumentReference ref;
    Button map;

    String names,age,phone,address,email;
    String hospita;
    String hospaddress;
    DatabaseReference harsh;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_content);
        name=findViewById(R.id.name);
        beds=findViewById(R.id.beds);
        oxygen=findViewById(R.id.oxygen);
        book=findViewById(R.id.book);
        city=getIntent().getStringExtra("city");
        hospita=getIntent().getStringExtra("hosp");
        map=findViewById(R.id.map);
        ref= FirebaseFirestore.getInstance().collection(city).document(hospita);
        ref.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if(documentSnapshot.exists()){
                    name.setText(documentSnapshot.getString("Address of My Hospital") );
                    hospaddress=documentSnapshot.getString("Address of My Hospital");
                    //+" NORMAL BEDS/n "+documentSnapshot.getString("Total no of Normal Beds")+"OXYGEN CYLINDER \n   "+documentSnapshot.getString("Total no of Oxygen Beds")
                    beds.setText(documentSnapshot.getString("Total no of Normal Beds"));
                    oxygen.setText(documentSnapshot.getString("Total no of Oxygen Beds"));
                    hospital=documentSnapshot.getString("Gmail of Hospital");
                    DocumentReference ref1=FirebaseFirestore.getInstance().collection("user").document(patient);

                    ref1.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            names=documentSnapshot.getString("name");
                            age=documentSnapshot.getString("age");
                            email=documentSnapshot.getString("email");
                            phone=documentSnapshot.getString("phone");
                            address=documentSnapshot.getString("address");

                        }
                    });
                }
            }
        });
        patient=getIntent().getStringExtra("gmail");
         pa=patient;
        String [] java =patient.split("(?=@)");
        String java1=java[0];
        java.util.Map<String, Object> data = new HashMap<>();
        data.put("hospital name",hospita);
        data.put("address of hospital",hospaddress);

        book.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                harsh=FirebaseDatabase.getInstance().getReference().child("BOOKING HISTORY").child(java1);
                String id = harsh.push().getKey();

                harsh.child(id).setValue(data);
                senEmail();
                Toast.makeText(content.this, "Confirmation E-Mail has been sent", Toast.LENGTH_SHORT).show();
                Intent intent=new Intent(getApplicationContext(),ConfirmationActivity.class);
                intent.putExtra("city",city);
                intent.putExtra("hospital",hospita);
                intent.putExtra("email",hospital);
                startActivity(intent);


            }
        });

        map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent3 =new Intent(getApplicationContext(), Map.class);
                intent3.putExtra("address1",hospaddress);
                startActivity(intent3);
            }
        });
    }




    private void senEmail() {

        String mEmail =hospital;
        String  mSubject = "Booking Notification";
        String mMessage = "A seat has been booked in your hospital\n"+"Name:"+names+"\nAge:"+age+"\nPhone Number:"+phone+"\nEmail:"+email+"\nAddress:"+address;


        JavaMailAPI javaMailAPI = new JavaMailAPI(this, mEmail, mSubject, mMessage);

        javaMailAPI.execute();

        String mEmail1 = pa;
        mSubject = "Booking request";
        mMessage = "A seat has been booked in"+hospita+"hospital\n"+"Address:"+hospaddress+"\n show this email in the hospital in order to claim your seats";

        JavaMailAPI javaMailAPI1 = new JavaMailAPI(this, mEmail1, mSubject, mMessage);

        javaMailAPI1.execute();
    }
}