package in.codecubes.agromart;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MenuBarActivity extends AppCompatActivity {
    private TextView user_name, user_email;
    private DatabaseReference reference;
    private FirebaseUser mUser;
    private String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_bar);

        user_name=findViewById(R.id.userName);
        user_email=findViewById(R.id.userEmail);

        mUser= FirebaseAuth.getInstance().getCurrentUser();
        userId=mUser.getUid();
        if(mUser!=null){

        }
        reference=FirebaseDatabase.getInstance().getReference("user_data");
        getUserData();
    }
    private void getUserData(){
        reference.child(userId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Toast.makeText(MenuBarActivity.this, userId, Toast.LENGTH_SHORT).show();
                user_name.setText(snapshot.child("fullName").getValue(String.class));
                user_email.setText(snapshot.child("email").getValue(String.class));

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}