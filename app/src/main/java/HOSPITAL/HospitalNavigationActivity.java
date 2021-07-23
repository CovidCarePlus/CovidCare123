package HOSPITAL;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.covidcare.R;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

import USER.BookingHistory;
import USER.Guidelines;
import USER.R_AND_L;
import USER.Showuser;
import USER.UpdateProfile;
import USER.details;

public class HospitalNavigationActivity extends AppCompatActivity {

    String gmail,name;
    TextView textview5;
    public DrawerLayout drawerLayout;
    public ActionBarDrawerToggle actionBarDrawerToggle;
    NavigationView navigationView;
    FirebaseAuth firebaseAuth;
    ImageView hospitals,update,guidelines,history;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hospital_navigation);
        setUpToolbar();
        gmail=getIntent().getStringExtra("mailid");
        hospitals=findViewById(R.id.im11);
        update=findViewById(R.id.im21);
        guidelines=findViewById(R.id.im31);
        history=findViewById(R.id.information);
        navigationView = (NavigationView) findViewById(R.id.navigation_menu1);
        firebaseAuth=FirebaseAuth.getInstance();
        hospitals.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), HospitalShowProfile.class);
                intent.putExtra("gmail",gmail);
                startActivity(intent);

            }
        });
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Hospitalupdateprofile.class);
                intent.putExtra("update",gmail);
                startActivity(intent);

            }
        });
        history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Showdet.class);
                intent.putExtra("mailid",gmail);
                startActivity(intent);

            }
        });
        guidelines.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Guidelines.class);
                intent.putExtra("gmail",gmail);
                startActivity(intent);

            }
        });
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId())
                {
                    case  R.id.hospitalshowprofile:

                        Intent intent = new Intent(getApplicationContext(), HospitalShowProfile.class);
                        intent.putExtra("gmail",gmail);
                        startActivity(intent);
                        break;

                    case R.id.hospitalupdate:
                        Intent intent1=new Intent(getApplicationContext(),Hospitalupdateprofile.class);
                        intent1.putExtra("update",gmail);
                        startActivity(intent1);
                        break;

                    case R.id.modifydata:
                        Intent intent2=new Intent(getApplicationContext(),Showdet.class);
                        intent2.putExtra("mailid",gmail);
                        startActivity(intent2);
                        break;

                    case R.id.hospitalcovidguideline:
                        Intent intent3=new Intent(getApplicationContext(),Guidelines.class);
                        intent3.putExtra("gmail",gmail);
                        startActivity(intent3);
                        break;

                    case R.id.hospitallogout:
                        firebaseAuth.signOut();
                        Intent intent4=new Intent(getApplicationContext(), REGandLOG.class);
                        intent4.putExtra("gmail",gmail);
                        startActivity(intent4);
                        finish();
                        break;




                    default:
                        Intent intent5=new Intent(getApplicationContext(), USER.NavigationActivity.class);
                        startActivity(intent5);
                        break;






//Paste your privacy policy link

//                    case  R.id.nav_Policy:{
//
//                        Intent browserIntent  = new Intent(Intent.ACTION_VIEW , Uri.parse(""));
//                        startActivity(browserIntent);
//
//                    }
                    //       break;


                }
                return false;
            }
        });

    }
    public void setUpToolbar() {
        drawerLayout = findViewById(R.id.drawerLayout11);
        Toolbar toolbar = findViewById(R.id.toolbar11);
        setSupportActionBar(toolbar);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout,R.string.nav_open , R.string.nav_close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (actionBarDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);

    }
    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setMessage("Are you sure you want to exit?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Intent intent = new Intent(Intent.ACTION_MAIN);
                        intent.addCategory(Intent.CATEGORY_HOME);
                        startActivity(intent);
                    }
                })
                .setNegativeButton("No", null)
                .show();
    }
}