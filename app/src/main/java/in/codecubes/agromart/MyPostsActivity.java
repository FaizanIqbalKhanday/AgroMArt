package in.codecubes.agromart;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

import java.util.ArrayList;

public class MyPostsActivity extends AppCompatActivity {
    private RecyclerView postRecyclerView;
    private ArrayList<Post> postList;
    private DatabaseReference reference;
    private PostAdapter adapter;
    private TextView variety,address;

    private FirebaseAuth mAuth;
    private FirebaseUser user;
    private String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_posts);

        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        userId = user.getUid();

        postRecyclerView=findViewById(R.id.posts_recycler_view);
        postRecyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(this, 2);
        postRecyclerView.setLayoutManager(layoutManager);

        loadMyPosts();

    }
    public void loadMyPosts(){
        reference = (DatabaseReference) FirebaseDatabase.getInstance().getReference()
                .child("POSTS").orderByChild("userid")
                .equalTo(userId);

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    postList = new ArrayList<>();
                    for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                        Post post = dataSnapshot1.getValue(Post.class);
                        postList.add(post);
                    }
                    adapter = new PostAdapter(MyPostsActivity.this, postList);
                    postRecyclerView.setAdapter(adapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(MyPostsActivity.this, "Failed to load posts", Toast.LENGTH_SHORT).show();
            }
        });
    }
}