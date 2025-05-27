package in.codecubes.agromart;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class WishlistActivity extends AppCompatActivity {

    private static final String TAG = "WishlistActivity";
    private RecyclerView wishlistRecyclerView;
    private PostAdapter postAdapter;
    private ArrayList<Post> postList;
    private DatabaseReference wishlistRef, postsRef;
    private String userId;
    private WishListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wishlist);

        wishlistRecyclerView = findViewById(R.id.wishlist_recycler_view);
        wishlistRecyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(this, 2);
        wishlistRecyclerView.setLayoutManager(layoutManager);
        postList = new ArrayList<>();
        postAdapter = new PostAdapter(WishlistActivity.this, postList);
        wishlistRecyclerView.setAdapter(postAdapter);

        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();

        if (user == null) {
            Toast.makeText(this, "User not logged in!", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        userId = user.getUid();
        wishlistRef = FirebaseDatabase.getInstance().getReference("user_data").child(userId);
        postsRef = FirebaseDatabase.getInstance().getReference("POSTS");

        loadWishlist();
    }

    public void loadWishlist() {
        String uId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference wishlistRef = FirebaseDatabase.getInstance().getReference("wishlist").child(uId);

        wishlistRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                postList.clear(); // Clear to avoid duplicates

                if (snapshot.exists()) {
                    List<String> wishlistPostIds = new ArrayList<>();

                    for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                        String postId = postSnapshot.getKey();
                        if (postId != null) {
                            wishlistPostIds.add(postId);
                        }
                    }

                    if (wishlistPostIds.isEmpty()) {
                        Toast.makeText(WishlistActivity.this, "No items in wishlist.", Toast.LENGTH_SHORT).show();
                        adapter = new WishListAdapter(WishlistActivity.this, postList);
                        wishlistRecyclerView.setAdapter(adapter);
                    } else {
                        // Load all posts
                        DatabaseReference postsRef = FirebaseDatabase.getInstance().getReference("POSTS");
                        postsRef.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot postsSnapshot) {
                                for (DataSnapshot postSnapshot : postsSnapshot.getChildren()) {
                                    Post post = postSnapshot.getValue(Post.class);
                                    if (post != null && wishlistPostIds.contains(post.getPostId())) {
                                        postList.add(post);
                                    }
                                }

                                adapter = new WishListAdapter(WishlistActivity.this, postList);
                                wishlistRecyclerView.setAdapter(adapter);
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                                Toast.makeText(WishlistActivity.this, "Failed to load posts: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                } else {
                    Toast.makeText(WishlistActivity.this, "No items in wishlist.", Toast.LENGTH_SHORT).show();
                    adapter = new WishListAdapter(WishlistActivity.this, postList);
                    wishlistRecyclerView.setAdapter(adapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(WishlistActivity.this, "Failed to load wishlist: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }




}
