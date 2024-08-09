package in.codecubes.agromart;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ProfileUI extends AppCompatActivity {
    private TextView profileName ,profileEmail, userPhoneNumber ,state,district,village;
    private DatabaseReference reference, reference2;
    private FirebaseUser mUser;
    private CardView editProfile;
    private String userId;
    private String pId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_ui);
        Button back_btn=findViewById(R.id.back_to_home);

        editProfile= findViewById(R.id.edit_profile);
        profileName=(TextView) findViewById(R.id.profile_name);
        profileEmail=(TextView)findViewById(R.id.profile_emailID);
        userPhoneNumber=(TextView)findViewById(R.id.number);
        state= (TextView)findViewById(R.id.profile_state);
        district =(TextView)findViewById(R.id.profile_district);
        village =(TextView)findViewById(R.id.profile_village);




        editProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editProfile();
            }
        });


        mUser=FirebaseAuth.getInstance().getCurrentUser();
        userId =mUser.getUid();
       if (mUser!=null){

       }
        reference= FirebaseDatabase.getInstance().getReference("user_data");
            getUserData();







        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                homeScreen();
            }
        });
    }
    public void editProfile(){
        Intent intent=new Intent(this,EditProfileActivity.class);
        startActivity(intent);
    }
    public void homeScreen(){
        Intent intent=new Intent(this,MainActivity.class);
        startActivity(intent);
    }
    private void getUserData(){
        reference.child(userId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Toast.makeText(ProfileUI.this, userId, Toast.LENGTH_SHORT).show();
                profileName.setText(snapshot.child("fullName").getValue(String.class));
                profileEmail.setText(snapshot.child("email").getValue(String.class));
                userPhoneNumber.setText(snapshot.child("phoneNumber").getValue(String.class));

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
