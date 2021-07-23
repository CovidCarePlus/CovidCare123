package HOSPITAL;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.covidcare.R;
import com.github.ybq.android.spinkit.sprite.Sprite;
import com.github.ybq.android.spinkit.style.Wave;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;
import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.Map;

import USER.NavigationActivity;

public class Hospitalupdateprofile extends AppCompatActivity {
    EditText username,usernumber,useraddress,usermail;
    String name,number,address,mail;
    ImageView profilephoto;
    Button update;
    StorageReference storage;
    DocumentReference document;
    ProgressBar progressBar;
    String gmailid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hospitalupdateprofile);
        username=findViewById(R.id.upname);
        usernumber=findViewById(R.id.upnumber);
        useraddress=findViewById(R.id.upaddress);
        usermail=findViewById(R.id.upmail);
        profilephoto=findViewById(R.id.upprofile);
        progressBar = (ProgressBar)findViewById(R.id.upprogressbar2);
        Sprite doubleBounce = new Wave();
        progressBar.setIndeterminateDrawable(doubleBounce);
        update=findViewById(R.id.upupdateprofile);

        gmailid=getIntent().getStringExtra("update");
        document= FirebaseFirestore.getInstance().collection("hospital").document(gmailid);
        usermail.setText(gmailid);
        progressBar.setVisibility(View.VISIBLE);

        storage=FirebaseStorage.getInstance().getReference().child(gmailid+"/hospitalprofile.jpg");
        storage.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).into(profilephoto, new Callback() {
                    @Override
                    public void onSuccess() {
                        progressBar.setVisibility(View.INVISIBLE);
                    }

                    @Override
                    public void onError(Exception e) {

                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull @NotNull Exception e) {
                progressBar.setVisibility(View.INVISIBLE);

            }
        });

        document=FirebaseFirestore.getInstance().collection("hospital").document(gmailid);
        document.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                String name=documentSnapshot.getString("name");
                String number=documentSnapshot.getString("phone");
                String address=documentSnapshot.getString("address");
                String mail=documentSnapshot.getString("email");
                username.setText(name);
                usernumber.setText(number);
                useraddress.setText(address);
                usermail.setText(mail);

            }
        });









        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                name=username.getText().toString();
                number=usernumber.getText().toString();
                address=useraddress.getText().toString();
                mail=usermail.getText().toString();

                if (name.isEmpty()){
                    username.setError("Enter your Name");
                    return;
                }
                if (number.isEmpty() || number.length()!=10){
                    usernumber.setError("Enter your Mobile Number");
                    return;
                }
                if (address.isEmpty()){
                    useraddress.setError("Enter Your Address");
                    return;
                }
                if (mail.isEmpty()){
                    usermail.setError("Enter your E-Mail ID");
                    return;
                }
                Map<String, Object> hospitalprofile = new HashMap<>();
                hospitalprofile.put("name",name);
                hospitalprofile.put("phone",number);
                hospitalprofile.put("address",address);
                hospitalprofile.put("email",mail);
                document= FirebaseFirestore.getInstance().collection("hospital").document(gmailid);
                document.set(hospitalprofile).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull @NotNull Task<Void> task) {
                        Toast.makeText(getApplicationContext(), "Success", Toast.LENGTH_SHORT).show();
                        Intent intent=new Intent(getApplicationContext(), NavigationActivity.class);
                        intent.putExtra("gmailid",gmailid);
                        startActivity(intent);
                        finish();


                    }
                });

            }

        });

        profilephoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gallery=new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(gallery,1000);
            }
        });



    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==1000){
            if (resultCode== Activity.RESULT_OK){
                Uri imageuri=data.getData();
                //profilephoto.setImageURI(imageuri);
                uploaddatatofirebase(imageuri);
                progressBar.setVisibility(View.VISIBLE);
            }
        }
    }

    private void uploaddatatofirebase(Uri imageuri) {
        final  StorageReference file=storage;
        file.putFile(imageuri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                file.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Picasso.get().load(uri).into(profilephoto, new Callback() {
                            @Override
                            public void onSuccess() {
                                progressBar.setVisibility(View.INVISIBLE);
                            }

                            @Override
                            public void onError(Exception e) {

                            }
                        });

                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull @NotNull Exception e) {
                Toast.makeText(Hospitalupdateprofile.this, "Failed", Toast.LENGTH_SHORT).show();
            }
        });
    }
}

