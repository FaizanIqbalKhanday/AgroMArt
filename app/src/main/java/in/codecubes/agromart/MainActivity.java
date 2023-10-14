package in.codecubes.agromart;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;
import android.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.menu.MenuBuilder;
import androidx.cardview.widget.CardView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private ImageView account;
    private FloatingActionButton addPostBtn;
    private FirebaseAuth mAuth;
    private Toolbar toolbar;
    private ProgressBar progress_Bar;
    private NavigationView navigationView;
    private ActionBarDrawerToggle drawerToggle;
    private DrawerLayout drawerLayout;
    private ActionBar actionBar;

    private RecyclerView postRecyclerView;
    private ArrayList<Post> postList;
    private PostAdapter adapter;

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (drawerToggle.onOptionsItemSelected(item)) return true;
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FirebaseApp.initializeApp(this);
        mAuth = FirebaseAuth.getInstance();

        addPostBtn = findViewById(R.id.addPost);
        account = findViewById(R.id.goToProfile);
        progress_Bar = findViewById(R.id.progressBar);
        drawerLayout = findViewById(R.id.drawable_layout);
        navigationView = findViewById(R.id.nav_view);
        drawerToggle = new ActionBarDrawerToggle(this,drawerLayout,R.string.open,R.string.close);

        drawerLayout.addDrawerListener(drawerToggle);
        drawerToggle.syncState();
        actionBar = getSupportActionBar();

        if (actionBar != null) actionBar.setDisplayHomeAsUpEnabled(true);

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.item1:{
                        Toast.makeText(MainActivity.this,"home selected",Toast.LENGTH_SHORT).show();
                        break;
                    }
                    case R.id.item2:{
                        Toast.makeText(MainActivity.this,"profile selected",Toast.LENGTH_SHORT).show();
                        break;
                    }
                    case R.id.item3:{
                        Toast.makeText(MainActivity.this,"my post selected",Toast.LENGTH_SHORT).show();
                        break;
                    }
                    case R.id.item4:{
                        Toast.makeText(MainActivity.this,"logout selected",Toast.LENGTH_SHORT).show();
                        break;
                    }
                    case R.id.item5:{
                        Toast.makeText(MainActivity.this,"about us selected",Toast.LENGTH_SHORT).show();
                        break;
                    }
                }
                return false;

            }
        });

        postRecyclerView = (RecyclerView) findViewById(R.id.posts_recycler_view);
        postRecyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(this, 2);
        postRecyclerView.setLayoutManager(layoutManager);

        loadData();

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
    }

    @Override
    public void onBackPressed() {
        if(drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        }
        else{
            super.onBackPressed();
        }
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

    public void loadData() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("POSTS");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    postList = new ArrayList<>();
                    for(DataSnapshot dataSnapshot1:dataSnapshot.getChildren()){
                        Post post = dataSnapshot1.getValue(Post.class);
                        postList.add(post);
                    }
                    adapter = new PostAdapter(MainActivity.this, postList);
                    postRecyclerView.setAdapter(adapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}