package in.codecubes.agromart;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.UUID;

public class ProfileUI extends AppCompatActivity {

    private TextView profileName, profileEmail, userPhoneNumber, state, district, village;
    private DatabaseReference reference;
    private FirebaseUser mUser;
    private CardView editProfile, my_posts_card;
    private String userId;
    private ImageView profileImageView;
    private Button uploadImageButton;
    private Uri selectedImageUri;

    private static final int PICK_IMAGE_REQUEST = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_ui);

        Button back_btn = findViewById(R.id.back_to_home);
        editProfile = findViewById(R.id.edit_profile);
        my_posts_card = findViewById(R.id.my_posts_card_btn);
        profileName = findViewById(R.id.profile_name);
        profileEmail = findViewById(R.id.profile_emailID);
        userPhoneNumber = findViewById(R.id.number);
        state = findViewById(R.id.profile_state);
        district = findViewById(R.id.profile_district);
        village = findViewById(R.id.profile_village);
        profileImageView = findViewById(R.id.imageView9);
        uploadImageButton = findViewById(R.id.uploadImageButton); // Make sure you have this button in your layout

        editProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editProfile();
            }
        });

        my_posts_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent my_post_intent = new Intent(ProfileUI.this, MyPostsActivity.class);
                startActivity(my_post_intent);
            }
        });

        profileImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery();
            }
        });

        uploadImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadImageToFirebase();
            }
        });

        mUser = FirebaseAuth.getInstance().getCurrentUser();
        userId = mUser.getUid();

        reference = FirebaseDatabase.getInstance().getReference("user_data");
        getUserData();

        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                homeScreen();
            }
        });
    }

    public void editProfile() {
        Intent intent = new Intent(this, EditProfileActivity.class);
        startActivity(intent);
    }

    public void homeScreen() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    private void getUserData() {
        reference.child(userId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                profileName.setText(snapshot.child("fullName").getValue(String.class));
                profileEmail.setText(snapshot.child("email").getValue(String.class));
                userPhoneNumber.setText(snapshot.child("phoneNumber").getValue(String.class));
                state.setText(snapshot.child("state").getValue(String.class));
                district.setText(snapshot.child("district").getValue(String.class));
                village.setText(snapshot.child("village").getValue(String.class));

                // Load profile image URL from database
                String profileImageUrl = snapshot.child("profileImageUrl").getValue(String.class);

                // Load image using Glide if URL is available
                if (profileImageUrl != null) {
                    Glide.with(ProfileUI.this)
                            .load(profileImageUrl)
                            .circleCrop()
                            .into(profileImageView);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ProfileUI.this, "Failed to load data", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void openGallery() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            selectedImageUri = data.getData();

            // Load image using Glide with circular transformation
            Glide.with(this)
                    .load(selectedImageUri)
                    .circleCrop() // Apply circular crop
                    .into(profileImageView);
        }
    }

    private void uploadImageToFirebase() {
        if (selectedImageUri != null) {
            // Create and show ProgressDialog
            ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Uploading Image");
            progressDialog.setMessage("Please wait...");
            progressDialog.setCancelable(false); // Prevent dismissing by tapping outside
            progressDialog.show();

            StorageReference storageRef = FirebaseStorage.getInstance().getReference("profile_images/" + UUID.randomUUID().toString());

            storageRef.putFile(selectedImageUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            storageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    String imageUrl = uri.toString();
                                    saveImageUrlToDatabase(imageUrl);

                                    // Dismiss ProgressDialog after successful upload
                                    progressDialog.dismiss();
                                }
                            });
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(ProfileUI.this, "Failed to upload image", Toast.LENGTH_SHORT).show();

                            // Dismiss ProgressDialog on failure
                            progressDialog.dismiss();
                        }
                    });
        } else {
            Toast.makeText(ProfileUI.this, "Please select an image", Toast.LENGTH_SHORT).show();
        }
    }

    private void saveImageUrlToDatabase(String imageUrl) {
        reference.child(userId).child("profileImageUrl").setValue(imageUrl)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(ProfileUI.this, "Image uploaded successfully", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(ProfileUI.this, "Failed to save image URL", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}