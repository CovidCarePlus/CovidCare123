package USER;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.animation.ValueAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.renderscript.Sampler;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.covidcare.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Period;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

public class content extends AppCompatActivity {
    TextView name,beds,oxygen,time,date,success;
    Button book,book1;
    String pa;
    String hospital;
    Integer i;
    String city;
    String patient;
    DocumentReference ref;
    int number=0;
    CardView map;
    String a;
    TextView three;
    DatabaseReference databaseReference;
    String afterday;
    String names,age,phone,address,email,hosptime,hospdate;
    String hospita;
    String hospaddress;
    DatabaseReference harsh;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_content);
        name=findViewById(R.id.name);
        beds=findViewById(R.id.bed);
        oxygen=findViewById(R.id.oxygen);
        time=findViewById(R.id.todaytime);
        date=findViewById(R.id.todaydate);
        success=findViewById(R.id.stark);
        book=findViewById(R.id.book);
        book1 =  findViewById(R.id.oxybook);
        city=getIntent().getStringExtra("city");
        hospita=getIntent().getStringExtra("hosp");
        LocalDate localDate=LocalDate.now();
        String current=localDate.toString();
        Date currentTime = Calendar.getInstance().getTime();
        Calendar c = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("hh.mm.ss aa");
        String output = dateFormat.format(currentTime);
        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
        String formattedDate = df.format(c.getTime());


        success.setText(hospita);


        map=findViewById(R.id.roll);
        ref= FirebaseFirestore.getInstance().collection(city).document(hospita);
        ref.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if(documentSnapshot.exists()){
                    name.setText(documentSnapshot.getString("Address of My Hospital") );
                    hospaddress=documentSnapshot.getString("Address of My Hospital");
                    hospdate=documentSnapshot.getString("date");
                    hosptime=documentSnapshot.getString("time");
                    time.setText(hosptime);
                    date.setText(hospdate);
                    //+" NORMAL BEDS/n "+documentSnapshot.getString("Total no of Normal Beds")+"OXYGEN CYLINDER \n   "+documentSnapshot.getString("Total no of Oxygen Beds")
                    String normalbed=documentSnapshot.getString("Total no of Normal Beds");
                    int score=Integer.parseInt(normalbed);
                    if (score==0){
                        book.setEnabled(false);
                    }

                    String oxygenbed=documentSnapshot.getString("Total no of Oxygen Beds");
                    int oxyscore=Integer.parseInt(oxygenbed);
                    if (oxyscore==0){
                        book1.setEnabled(false);
                    }
                    ValueAnimator valueAnimator=ValueAnimator.ofInt(0,score);
                    ValueAnimator valueAnimator1=ValueAnimator.ofInt(0,oxyscore);
                    valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                        @Override
                        public void onAnimationUpdate(ValueAnimator animation) {
                            beds.setText(valueAnimator.getAnimatedValue().toString());
                        }
                    });
                    valueAnimator.start();
                    valueAnimator1.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                        @Override
                        public void onAnimationUpdate(ValueAnimator animation) {
                            oxygen.setText(valueAnimator1.getAnimatedValue().toString());
                        }
                    });
                    valueAnimator1.start();
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
        databaseReference=FirebaseDatabase.getInstance().getReference().child("Update").child(java1);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
               afterday=snapshot.child("Date").getValue(String.class);

               LocalDate afterthree=LocalDate.parse(afterday);
               Period period = Period.between(localDate,afterthree);
               number=period.getDays();
               if (number<=0 ){
                   book.setEnabled(true);
                   book1.setEnabled(true);
               }else {
                   book.setEnabled(false);
                   book1.setEnabled(false);
                   Toast.makeText(content.this, "Booking will be enabled after 3days from booking", Toast.LENGTH_LONG).show();
               }

            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });

        java.util.Map<String, Object> data = new HashMap<>();

        book.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                harsh=FirebaseDatabase.getInstance().getReference().child("BOOKING HISTORY").child(java1);
                String id = harsh.push().getKey();
                data.put("hospitalname",hospita);
                data.put("address",hospaddress);
                data.put("time",output);
                data.put("date",formattedDate);
                data.put("type","NORMAL BED");
                harsh.child(id).setValue(data);
                senEmail();
                Toast.makeText(content.this, "Confirmation E-Mail has been sent", Toast.LENGTH_SHORT).show();
                Intent intent=new Intent(getApplicationContext(),ConfirmationActivity.class);
                intent.putExtra("city",city);
                intent.putExtra("hospital",hospita);
                intent.putExtra("email",hospital);
                intent.putExtra("time",output);
                intent.putExtra("date",formattedDate);
                intent.putExtra("address",hospaddress);
                intent.putExtra("username",names);
                intent.putExtra("userage",age);
                intent.putExtra("phone",phone);
                intent.putExtra("usermail",java1);
                startActivity(intent);
                finish();


            }
        });
        book1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                harsh=FirebaseDatabase.getInstance().getReference().child("BOOKING HISTORY").child(java1);
                String id = harsh.push().getKey();
                data.put("hospitalname",hospita);
                data.put("address",hospaddress);
                data.put("time",output);
                data.put("date",formattedDate);
                data.put("type","OXYGEN BED");
                harsh.child(id).setValue(data);
                senEmail();
                Toast.makeText(content.this, "Confirmation E-Mail has been sent", Toast.LENGTH_SHORT).show();
                Intent intent=new Intent(getApplicationContext(),confirmation2.class);
                intent.putExtra("city",city);
                intent.putExtra("hospital",hospita);
                intent.putExtra("email",hospital);
                intent.putExtra("time",output);
                intent.putExtra("date",formattedDate);
                intent.putExtra("address",hospaddress);
                intent.putExtra("age",age);
                intent.putExtra("name",names);
                intent.putExtra("oxygen",java1);

                startActivity(intent);
                finish();



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
        String mMessage = "A seat has been booked in your hospital \n"+"Name:"+names+"\nAge:"+age+"\nPhone Number:"+phone+"\nEmail:"+email+"\nAddress:"+address;


        JavaMailAPI javaMailAPI = new JavaMailAPI(this, mEmail, mSubject, mMessage);

        javaMailAPI.execute();

        String mEmail1 = pa;
        mSubject = "Booking request";
        mMessage = "A seat has been booked in "+hospita.toUpperCase()+" HOSPITAL\n"+"Address:"+hospaddress+"\n Show this email in the hospital in order to claim your seats";

        JavaMailAPI javaMailAPI1 = new JavaMailAPI(this, mEmail1, mSubject, mMessage);

        javaMailAPI1.execute();
    }
}