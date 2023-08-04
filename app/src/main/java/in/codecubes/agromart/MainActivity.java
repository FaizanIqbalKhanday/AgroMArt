package in.codecubes.agromart;

import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FloatingActionButton addPostBtn= findViewById(R.id.addPost);
        addPostBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openAddPostActivity();

            }
        });

        ImageView account =findViewById(R.id.goToProfile);
        account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openProfileui();
            }
        });

    }
    public void openAddPostActivity(){
        Intent intent= new Intent(this, AddPostActivity.class);
        startActivity(intent);
    }
    public void openProfileui(){
        Intent intent=new Intent(this, ProfileUI.class);
        startActivity(intent);
    }
}