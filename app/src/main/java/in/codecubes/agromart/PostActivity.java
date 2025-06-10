package in.codecubes.agromart;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.library.foysaltech.smarteist.autoimageslider.SliderView;

import java.util.ArrayList;
import java.util.List;

public class PostActivity extends AppCompatActivity {

    private static final String TAG = "PostActivity";

    private Button  submit_commit;

    private Button callBtn, chatBtn;
    private TextView variety, grade, packing, quantity, address, userName, userPhoneNumber,postDescription;
    private String phoneNumber;

    private EditText commentEditText;
    private RecyclerView commentsRecyclerView;
    private CommitAdapter commentAdapter;
    private List<Commits> commentList;
    private DatabaseReference reference, reference2;
    private FirebaseAuth auth;
    private String uId;
    private ImageButton btnWishlist;


    private SliderAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);

        // Initialize Views
        callBtn = findViewById(R.id.call_btn);
        chatBtn = findViewById(R.id.chat_btn);

        commentEditText = findViewById(R.id.comment_input);
        commentsRecyclerView = findViewById(R.id.commints_recycler_view);
        submit_commit = findViewById(R.id.submit_comment);

// Initialize RecyclerView for comments
        commentList = new ArrayList<>();
        commentAdapter = new CommitAdapter(this,commentList);
        commentsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        commentsRecyclerView.setAdapter(commentAdapter);


        variety = findViewById(R.id.post_variety);
        grade = findViewById(R.id.post_grade);
        packing = findViewById(R.id.post_packing);
        quantity = findViewById(R.id.post_quantity);
        address = findViewById(R.id.post_user_address);
        userName = findViewById(R.id.post_user_name);
        userPhoneNumber = findViewById(R.id.post_user_phone);
        postDescription=findViewById(R.id.post_description);

        btnWishlist = findViewById(R.id.btn_wishlist);


        SliderView sliderView = findViewById(R.id.slider);

        // Initialize Firebase Reference
        auth = FirebaseAuth.getInstance();

        // Now you can safely access auth.getCurrentUser()
        FirebaseUser user = auth.getCurrentUser();
        if (user == null) {
            Toast.makeText(this, "User not logged in!", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        uId = user.getUid(); // Use the user object to get UID
        reference = FirebaseDatabase.getInstance().getReference();
        reference2 = FirebaseDatabase.getInstance().getReference("user_data");


        // Now you can safely access auth.getCurrentUser()


        uId = user.getUid(); // Use the user object to get UID
        reference = FirebaseDatabase.getInstance().getReference();
        reference2 = FirebaseDatabase.getInstance().getReference("user_data");

        // Get Intent Extras
        String postId = getIntent().getStringExtra("post_id");
        String userId = getIntent().getStringExtra("user_id");

        if (postId == null || userId == null) {
            Log.e(TAG, "Invalid post_id or user_id passed to activity");
            finish();
            return;
        }
        DatabaseReference wishlistRef = FirebaseDatabase.getInstance().getReference("wishlist").child(uId).child(postId);

        wishlistRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    // Post is in wishlist -> show red
                    btnWishlist.setColorFilter(ContextCompat.getColor(PostActivity.this, R.color.progress_end)); // red
                    btnWishlist.setTag("added");
                } else {
                    // Post not in wishlist -> show gray
                    btnWishlist.setColorFilter(ContextCompat.getColor(PostActivity.this, R.color.gray)); // gray
                    btnWishlist.setTag("removed");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("PostActivity", "Failed to read wishlist state", error.toException());
            }
        });

        // Fetch Post Data
        fetchPostData(postId, sliderView);


        fetchComments(postId);

        // Fetch User Data
        fetchUserData(userId);

        // Handle Call Button Click
        callBtn.setOnClickListener(v -> {
            if (phoneNumber != null) {
                Intent callIntent = new Intent(Intent.ACTION_DIAL);
                callIntent.setData(Uri.parse("tel:" + phoneNumber));
                startActivity(callIntent);
            } else {
                Log.e(TAG, "Phone number is null");
            }
        });

        // Handle Chat Button Click
        chatBtn.setOnClickListener(v -> {
            if (phoneNumber != null) {
                String url = "https://api.whatsapp.com/send?phone=" + phoneNumber;
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(url));
                startActivity(intent);
            } else {
                Log.e(TAG, "Phone number is null");
            }
        });

        submit_commit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submitComment(postId);

            }
        });




        // Track the current color state

        btnWishlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseReference wishlistRef = FirebaseDatabase.getInstance().getReference("wishlist");
                String uId = FirebaseAuth.getInstance().getCurrentUser().getUid();
                String postId = getIntent().getStringExtra("post_id");

                if (btnWishlist.getTag() != null && btnWishlist.getTag().equals("added")) {
                    wishlistRef.child(uId).child(postId).removeValue()
                            .addOnSuccessListener(aVoid -> {
                                btnWishlist.setColorFilter(ContextCompat.getColor(PostActivity.this, R.color.gray));
                                btnWishlist.setTag("removed");
                                Log.d("PostActivity", "Post removed from wishlist");
                            })
                            .addOnFailureListener(e -> Log.e("PostActivity", "Failed to remove post from wishlist", e));
                } else {
                    wishlistRef.child(uId).child(postId).setValue(true)
                            .addOnSuccessListener(aVoid -> {
                                btnWishlist.setColorFilter(ContextCompat.getColor(PostActivity.this, R.color.progress_end));
                                btnWishlist.setTag("added");
                                Log.d("PostActivity", "Post added to wishlist");
                            })
                            .addOnFailureListener(e -> Log.e("PostActivity", "Failed to add post to wishlist", e));
                }
            }
        });




    }

    private void fetchPostData(String postId, SliderView sliderView) {
        reference.child("POSTS").child(postId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                // Set Post Details
                variety.setText(snapshot.child("variety").getValue(String.class));
                grade.setText(snapshot.child("grade").getValue(String.class));
                packing.setText(snapshot.child("packingType").getValue(String.class));
                postDescription.setText(snapshot.child("description").getValue(String.class));
                quantity.setText(snapshot.child("quantity").getValue(String.class) + " Boxes");

                // Build Address
                String fullAddress = snapshot.child("village").getValue(String.class) + " "
                        + snapshot.child("district").getValue(String.class) + " "
                        + snapshot.child("state").getValue(String.class);
                address.setText(fullAddress);

                // Set Slider Data
                ArrayList<SliderData> sliderDataArrayList = new ArrayList<>();
                if (snapshot.hasChild("images")) {
                    for (DataSnapshot imageSnapshot : snapshot.child("images").getChildren()) {
                        String imageUrl = imageSnapshot.getValue(String.class);
                        if (imageUrl != null && !imageUrl.isEmpty()) {
                            sliderDataArrayList.add(new SliderData(imageUrl));
                        }
                    }
                }
                adapter = new SliderAdapter(PostActivity.this, sliderDataArrayList);
                sliderView.setSliderAdapter(adapter);

                // Configure SliderView
                sliderView.setAutoCycleDirection(SliderView.LAYOUT_DIRECTION_LTR);
                sliderView.setScrollTimeInSec(3);
                sliderView.setAutoCycle(true);
                sliderView.startAutoCycle();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e(TAG, "Failed to fetch post data: " + error.getMessage());
            }
        });
    }


    private void fetchUserData(String userId) {
        reference.child("user_data").child(userId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                // Set User Details
                userName.setText(snapshot.child("fullName").getValue(String.class));
                phoneNumber = snapshot.child("phoneNumber").getValue(String.class);
                userPhoneNumber.setText(phoneNumber);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e(TAG, "Failed to fetch user data: " + error.getMessage());
            }
        });
    }
    private void submitComment(String postId) {
        String commentText = commentEditText.getText().toString().trim();

        if (commentText.isEmpty()) {
            commentEditText.setError("Comment cannot be empty");
            return;
        }

        // Get current user's UID
        FirebaseAuth auth = FirebaseAuth.getInstance();
        if (auth.getCurrentUser() == null) {
            Log.e(TAG, "User is not authenticated.");
            return;
        }

        String currentUserId = auth.getCurrentUser().getUid();

        // Reference to the user's data in the database
        DatabaseReference userReference = FirebaseDatabase.getInstance()
                .getReference("user_data")
                .child(currentUserId)
                .child("fullName");

        // Fetch username
        userReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String userName = snapshot.getValue(String.class);

                if (userName != null) {
                    // Create Comment object
                    String commentId = reference.child("POSTS").child(postId).child("comments").push().getKey();
                    if (commentId != null) {
                        Commits commit = new Commits(userName, commentText, System.currentTimeMillis(), commentId, currentUserId,postId);

                        reference.child("POSTS").child(postId).child("comments").child(commentId)
                                .setValue(commit)
                                .addOnCompleteListener(task -> {
                                    if (task.isSuccessful()) {
                                        commentEditText.setText(""); // Clear input
                                        Log.d(TAG, "Comment added successfully");
                                    } else {
                                        Log.e(TAG, "Failed to add comment: " + task.getException().getMessage());
                                    }
                                });
                    }
                } else {
                    Log.e(TAG, "Username not found for current user.");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e(TAG, "Failed to fetch username: " + error.getMessage());
            }
        });
    }
    private void fetchComments(String postId) {
        reference.child("POSTS").child(postId).child("comments")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        commentList.clear();
                        for (DataSnapshot commentSnapshot : snapshot.getChildren()) {
                            Commits comment = commentSnapshot.getValue(Commits.class);
                            if (comment != null) {
                                commentList.add(comment);
                            }
                        }
                        commentAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Log.e(TAG, "Failed to fetch comments: " + error.getMessage());
                    }
                });
    }
}
