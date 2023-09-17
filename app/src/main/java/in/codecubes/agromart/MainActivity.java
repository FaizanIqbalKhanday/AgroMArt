package in.codecubes.agromart;

import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    private CardView postCard;
    private ImageView account ,menu_button;
    private FloatingActionButton addPostBtn;
    private FirebaseAuth mAuth;
    private ProgressBar progress_Bar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth =FirebaseAuth.getInstance();

        postCard = (CardView) findViewById(R.id.postView1);
        addPostBtn = findViewById(R.id.addPost);
        account =findViewById(R.id.goToProfile);
        menu_button =findViewById(R.id.menubtn);
        progress_Bar=findViewById(R.id.progressBar);

        addPostBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openActivity(AddPostActivity.class);

            }
        });

        account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openActivity(ProfileUI.class);
            }
        });

        postCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openActivity(PostActivity.class);
            }
        });
        menu_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signOut().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            // Sign out successful
                            // You can navigate to the login screen or perform other actions here
                            startActivity(new Intent(MainActivity.this, LoginActivity.class));
                            finish();
                        } else {
                            // Sign out failed, handle the error
                            // You can show an error message or log the error for debugging
                            Toast.makeText(MainActivity.this, "Sign out failed", Toast.LENGTH_SHORT).show();
                        };
         }
});

            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser mUser=mAuth.getCurrentUser();
        if(mUser!=null){
            return;

        }
        else{
            startActivity(new Intent(this,LoginActivity.class));
            progress_Bar.setVisibility(View.INVISIBLE);
            finish();
        }
    }

    public void openActivity(final Class<? extends Activity> targetActivity) {
        Intent intent = new Intent(this, targetActivity);
        startActivity(intent);
    }
}