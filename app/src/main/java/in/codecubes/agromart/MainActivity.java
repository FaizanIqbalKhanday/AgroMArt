package in.codecubes.agromart;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.utils.widget.ImageFilterView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.SearchView;

import com.bumptech.glide.Glide;
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

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private ImageView account,menuBar;
    private ImageFilterView userProfilePic;
    private FloatingActionButton addPostBtn;
    private FirebaseAuth mAuth;
   private FirebaseUser mUser;
   private String  uId;
    private ProgressBar progress_Bar;
    private NavigationView navigationView;
    private ActionBarDrawerToggle drawerToggle;
    private DrawerLayout drawerLayout;
    private ActionBar actionBar;
    private TextView user_name, user_email;

    private RecyclerView postRecyclerView;
    private ArrayList<Post> postList;
    private DatabaseReference reference2;
    private PostAdapter adapter;
    private ArrayList<Post> filteredList;
    private CardView delicious_apple, kullu_apple,golden_apple, mahraji_apple,treal_apple,american_apple,pear_apple;
    private String delicious="delicious", kullu="kullu",golden="golden",mahraji="mahraji",treal="treal",american="american",pear="pear";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FirebaseApp.initializeApp(this);
        mAuth = FirebaseAuth.getInstance();
        addPostBtn = findViewById(R.id.addPost);
        menuBar=findViewById(R.id.menu_bar);
        account = findViewById(R.id.goToProfile);
        progress_Bar = findViewById(R.id.progressBar);
        drawerLayout = findViewById(R.id.drawable_layout);
        navigationView = findViewById(R.id.nav_view);
        View headerView = navigationView.getHeaderView(0);
        user_name=headerView.findViewById(R.id.userName);
        user_email=headerView.findViewById(R.id.userEmail);
        userProfilePic = headerView.findViewById(R.id.user_Profile_Pic);
        navigationView.setNavigationItemSelectedListener(this);
        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close);

        drawerLayout.addDrawerListener(drawerToggle);
        drawerToggle.syncState();
        navigationView.bringToFront();
        menuBar.setOnClickListener(view -> drawerLayout.openDrawer(navigationView));


        postRecyclerView = findViewById(R.id.posts_recycler_view);
        postRecyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(this, 2);
        postRecyclerView.setLayoutManager(layoutManager);
        mUser=mAuth.getCurrentUser();
        uId =mUser.getUid();
        if (mUser!=null){

        }
        getUserDataInMenuBar();

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
        delicious_apple=findViewById(R.id.delicious);
        golden_apple=findViewById(R.id.golden);
        kullu_apple=findViewById(R.id.kullu);
        mahraji_apple=findViewById(R.id.mahraji);
        treal_apple=findViewById(R.id.treal);
        pear_apple=findViewById(R.id.pear);
        american_apple=findViewById(R.id.american);

        delicious_apple.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchByCategory(delicious);
            }
        });
        american_apple.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchByCategory(american);

            }
        });
        golden_apple.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchByCategory(golden);

            }
        });
        kullu_apple.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchByCategory(kullu);

            }
        });
        mahraji_apple.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchByCategory(mahraji);

            }
        });
        treal_apple.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchByCategory(treal);

            }
        });
        pear_apple.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchByCategory(pear);

            }
        });

        SearchView searchView = findViewById(R.id.searchView);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                performSearch(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                performSearch(newText);
                return true;
            }
        });

    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser mUser = mAuth.getCurrentUser();
        if (mUser != null) {
            return;
        } else {
            startActivity(new Intent(this, LoginActivity.class));
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
                if (dataSnapshot.exists()) {
                    postList = new ArrayList<>();
                    for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                        Post post = dataSnapshot1.getValue(Post.class);
                        postList.add(post);
                    }
                    adapter = new PostAdapter(MainActivity.this, postList);
                    postRecyclerView.setAdapter(adapter);
                    filteredList = new ArrayList<>(postList);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(MainActivity.this, "Failed to load posts", Toast.LENGTH_SHORT).show();

            }
        });
    }


    private void performSearch(String query) {
        ArrayList<Post> searchResults = new ArrayList<>();

        if (query.isEmpty()) {
            searchResults.addAll(postList);
        } else {
            String lowerCaseQuery = query.toLowerCase();
            for (Post post : postList) {
                if ((post.getVariety() != null && post.getVariety().toLowerCase().contains(lowerCaseQuery))
                        || (post.getDistrict() != null && post.getDistrict().toLowerCase().contains(lowerCaseQuery))) {
                    searchResults.add(post);
                }
            }
        }


        adapter.setFilteredList(searchResults);
    }
    private void searchByCategory(String category) {

        ArrayList<Post> searchResults = new ArrayList<>();

        if (category.isEmpty()) {
            searchResults.addAll(postList);
        } else {
            String lowerCaseQuery = category.toLowerCase();
            for (Post post : postList) {
                if ((post.getVariety() != null && post.getVariety().toLowerCase().contains(lowerCaseQuery))
                        || (post.getDistrict() != null && post.getDistrict().toLowerCase().contains(lowerCaseQuery))) {
                    searchResults.add(post);
                }
            }
        }

        adapter.setFilteredList(searchResults);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id=item.getItemId();
        switch (id){
            case R.id.item1:
                startActivity(new Intent(this, MainActivity.class));

                break;
            case R.id.item3:
                Toast.makeText(this, "terms is selected", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse("https://agromarttermsandcondations.blogspot.com/2025/01/agromart-terms-and-conditions-welcome.html"));
                startActivity(intent);
                break;
            case R.id.item5:
                Toast.makeText(this, "home is selected", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(this, AboutUsActivity.class));
                break;
            case R.id.item4:
                mAuth.signOut();
                startActivity(new Intent(this, LoginActivity.class));
                Toast.makeText(this, "you are signed out", Toast.LENGTH_SHORT).show();
                break;
        }
        return true;
    }
    public void getUserDataInMenuBar() {
        reference2 = FirebaseDatabase.getInstance().getReference("user_data");
        reference2.child(uId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                user_name.setText(snapshot.child("fullName").getValue(String.class));
                user_email.setText(snapshot.child("email").getValue(String.class));

                // Get profile image URL from database
                String profileImageUrl = snapshot.child("profileImageUrl").getValue(String.class);

                // Load image using Glide if URL is available
                if (profileImageUrl != null) {
                    Glide.with(MainActivity.this) // Use MainActivity context
                            .load(profileImageUrl)
                            .circleCrop() // Apply circular crop if needed
                            .into(userProfilePic); // Set image to userProfilePic ImageFilterView
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(MainActivity.this, "failed to load data", Toast.LENGTH_SHORT).show();
            }
        });
    }
}