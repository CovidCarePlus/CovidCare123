package USER;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

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

public class NavigationActivity extends AppCompatActivity {

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
        setContentView(R.layout.activity_navigation);
        setUpToolbar();
        gmail=getIntent().getStringExtra("gmailid");
        hospitals=findViewById(R.id.im1);
        update=findViewById(R.id.im2);
        guidelines=findViewById(R.id.im3);
        history=findViewById(R.id.im4);

        navigationView = (NavigationView) findViewById(R.id.navigation_menu);
        firebaseAuth=FirebaseAuth.getInstance();
        hospitals.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(NavigationActivity.this, details.class);
                intent.putExtra("gmail",gmail);
                startActivity(intent);

            }
        });
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(NavigationActivity.this, UpdateProfile.class);
                intent.putExtra("gmail",gmail);
                startActivity(intent);

            }
        });
        history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(NavigationActivity.this, BookingHistory.class);
                intent.putExtra("gmail",gmail);
                startActivity(intent);

            }
        });
        guidelines.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(NavigationActivity.this, Guidelines.class);
                intent.putExtra("gmail",gmail);
                startActivity(intent);

            }
        });
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId())
                {
                    case  R.id.showprofile:

                        Intent intent = new Intent(NavigationActivity.this, Showuser.class);
                        intent.putExtra("gmail",gmail);
                        startActivity(intent);
                        break;

                    case R.id.searchhosp:
                        Intent intent1=new Intent(getApplicationContext(),details.class);
                        intent1.putExtra("gmail",gmail);
                        startActivity(intent1);
                        break;

                    case R.id.update:
                        Intent intent2=new Intent(getApplicationContext(),UpdateProfile.class);
                        intent2.putExtra("gmail",gmail);
                        startActivity(intent2);
                        break;

                    case R.id.booking:
                        Intent intent3=new Intent(getApplicationContext(),BookingHistory.class);
                        intent3.putExtra("gmail",gmail);
                        startActivity(intent3);
                        break;

                    case R.id.logout:
                        firebaseAuth.signOut();
                        Intent intent4=new Intent(getApplicationContext(),R_AND_L.class);
                        intent4.putExtra("gmail",gmail);
                        startActivity(intent4);
                        finish();
                        break;

                   


                    default:
                        Intent intent5=new Intent(getApplicationContext(),NavigationActivity.class);
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
        drawerLayout = findViewById(R.id.drawerLayout);
        Toolbar toolbar = findViewById(R.id.toolbar);
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
}
