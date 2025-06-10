package in.codecubes.agromart;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;
import androidx.core.content.FileProvider;
import androidx.drawerlayout.widget.DrawerLayout;

import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class AddPostActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final int GALLERY_REQUEST_CODE = 123;
    private FirebaseDatabase rootNode;
    private FirebaseUser mUser;
    private ImageAdapter imageAdapter;
    private DatabaseReference reference;
    private StorageReference storageReference;
    private TextInputLayout varietyTIL, gradeTIL, packingTIL, quantityTIL, stateTIL, districtTIL, villageTIL,postDescription;
    private Button addPostButton;
    private LinearLayout takePhoto, selectFromGallery;
    private GridView uploadedImagesGrid;
    private String variety, grade, packing, state, district;
    private ActionBarDrawerToggle drawerToggle;
    private DrawerLayout drawerLayout;
    private ActionBar actionBar;
    private ProgressBar progressBar;
    private ArrayList<Uri> imageUris = new ArrayList<>();
    private String postId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_post);
        rootNode = FirebaseDatabase.getInstance();
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        storageReference = FirebaseStorage.getInstance().getReference("Images");

        varietyTIL = findViewById(R.id.selectVariety);
        postDescription=findViewById(R.id.set_description);
        gradeTIL = findViewById(R.id.selectGrade);
        packingTIL = findViewById(R.id.packingType);
        quantityTIL = findViewById(R.id.setQuantity);
        stateTIL = findViewById(R.id.setState);
        districtTIL = findViewById(R.id.setDistrict);
        villageTIL = findViewById(R.id.set_village);
        addPostButton = findViewById(R.id.addPostButton);
        selectFromGallery = findViewById(R.id.selectFromGallery);
        takePhoto = findViewById(R.id.takePhoto);
        uploadedImagesGrid = findViewById(R.id.uploadedImagesGrid);

        progressBar=findViewById(R.id.progressBar);
        drawerLayout = findViewById(R.id.drawable_layout);
        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close);

        drawerLayout.addDrawerListener(drawerToggle);
        drawerToggle.syncState();
        actionBar = getSupportActionBar();
        if (actionBar != null) actionBar.setDisplayHomeAsUpEnabled(true);
        imageAdapter = new ImageAdapter(this, imageUris);
        uploadedImagesGrid.setAdapter(imageAdapter);

        selectFromGallery.setOnClickListener(v -> openGallery());
        takePhoto.setOnClickListener(v -> openCamera());

        uploadedImagesGrid.setOnItemClickListener((AdapterView<?> parent, View view, int position, long id) -> {
            Toast.makeText(this, "Clicked image at position: " + position, Toast.LENGTH_SHORT).show();
        });

        addPostButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Validate the form fields
                if (!validateVariety() || !validateDescription() || !validateGrade() || !validatePacking() || !validateQuantity()
                        || !validateState() || !validateDistrict() || !validateVillage()) {
                    return;
                }

                // Show progress bar
                progressBar.setVisibility(View.VISIBLE);

                // Get userId, quantity, and village from the fields
                String userId = mUser.getUid();
                String quantity = quantityTIL.getEditText().getText().toString();
                String village = villageTIL.getEditText().getText().toString();
                String description =postDescription.getEditText().getText().toString();

                AddPost(variety, grade, packing, quantity,state,district, village,  description,userId);

                // Check if imageUris is empty
                if (!imageUris.isEmpty()) {
                    uploadImage(imageUris);
                } else {
                    // Show a message if no images are selected
                    progressBar.setVisibility(View.INVISIBLE);
                    Toast.makeText(AddPostActivity.this, "Please select images first.", Toast.LENGTH_SHORT).show();
                }
            }
        });


        varietyTIL.setEndIconOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create a PopupMenu
                PopupMenu popupMenu = new PopupMenu(AddPostActivity.this, v);

                // Inflate the menu resource file
                popupMenu.getMenuInflater().inflate(R.menu.select_variety, popupMenu.getMenu());

                // Set a listener to handle menu item clicks
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        // Get the selected variety
                        variety = item.getTitle().toString();

                        // Set the selected variety to the AutoCompleteTextView
                        AutoCompleteTextView autoCompleteTextView = varietyTIL.findViewById(R.id.selectVarietyAutoCompleteTextView);
                        autoCompleteTextView.setText(variety);

                        return true;
                    }
                });

                // Show the popup menu
                popupMenu.show();
            }
        });

        gradeTIL.setEndIconOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create a PopupMenu
                PopupMenu popupMenu = new PopupMenu(AddPostActivity.this, v);

                // Inflate the menu resource file
                popupMenu.getMenuInflater().inflate(R.menu.select_grade, popupMenu.getMenu());

                // Set a listener to handle menu item clicks
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        // Get the selected grade
                        grade = item.getTitle().toString();

                        // Set the selected grade to the AutoCompleteTextView
                        AutoCompleteTextView autoCompleteTextView = gradeTIL.findViewById(R.id.selectGradeAutoCompleteTextView);
                        autoCompleteTextView.setText(grade);

                        return true;
                    }
                });

                // Show the popup menu
                popupMenu.show();
            }
        });

        packingTIL.setEndIconOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create a PopupMenu
                PopupMenu popupMenu = new PopupMenu(AddPostActivity.this, v);

                // Inflate the menu resource file
                popupMenu.getMenuInflater().inflate(R.menu.select_packing_type, popupMenu.getMenu());

                // Set a listener to handle menu item clicks
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        // Get the selected packing type
                        packing = item.getTitle().toString();

                        // Set the selected packing type to the AutoCompleteTextView
                        AutoCompleteTextView autoCompleteTextView = packingTIL.findViewById(R.id.selectPackingAutoCompleteTextView);
                        autoCompleteTextView.setText(packing);

                        return true;
                    }
                });

                // Show the popup menu
                popupMenu.show();
            }

        });



        String[] states;
        String[][] districts;

        // Define the states and districts arrays
        states = new String[]{
                "Andaman and Nicobar Islands",
                "Andhra Pradesh",
                "Arunachal Pradesh",
                "Assam",
                "Bihar",
                "Chandigarh",
                "Chhattisgarh",
                "Dadra and Nagar Haveli",
                "Daman and Diu",
                "Delhi",
                "Goa",
                "Gujarat",
                "Haryana",
                "Himachal Pradesh",
                "Jammu & Kashmir",
                "Jharkhand",
                "Karnataka",
                "Kerala",
                "Ladakh",
                "Lakshadweep",
                "Madhya Pradesh",
                "Maharashtra",
                "Manipur",
                "Meghalaya",
                "Mizoram",
                "Nagaland",
                "Odisha",
                "Pondicherry",
                "Punjab",
                "Rajasthan",
                "Sikkim",
                "Tamil Nadu",
                "Telangana",
                "Tripura",
                "Uttarakhand",
                "Uttar Pradesh",
                "West Bengal"
        };

        districts = new String[][]{
                getResources().getStringArray(R.array.array_andaman_nicobar_districts),
                getResources().getStringArray(R.array.array_andhra_pradesh_districts),
                getResources().getStringArray(R.array.array_arunachal_pradesh_districts),
                getResources().getStringArray(R.array.array_assam_districts),
                getResources().getStringArray(R.array.array_bihar_districts),
                getResources().getStringArray(R.array.array_chandigarh_districts),
                getResources().getStringArray(R.array.array_chhattisgarh_districts),
                getResources().getStringArray(R.array.array_dadra_nagar_haveli_districts),
                getResources().getStringArray(R.array.array_daman_diu_districts),
                getResources().getStringArray(R.array.array_delhi_districts),
                getResources().getStringArray(R.array.array_goa_districts),
                getResources().getStringArray(R.array.array_gujarat_districts),
                getResources().getStringArray(R.array.array_haryana_districts),
                getResources().getStringArray(R.array.array_himachal_pradesh_districts),
                getResources().getStringArray(R.array.array_jammu_kashmir_districts),
                getResources().getStringArray(R.array.array_jharkhand_districts),
                getResources().getStringArray(R.array.array_karnataka_districts),
                getResources().getStringArray(R.array.array_kerala_districts),
                getResources().getStringArray(R.array.array_ladakh_districts),
                getResources().getStringArray(R.array.array_lakshadweep_districts),
                getResources().getStringArray(R.array.array_madhya_pradesh_districts),
                getResources().getStringArray(R.array.array_maharashtra_districts),
                getResources().getStringArray(R.array.array_manipur_districts),
                getResources().getStringArray(R.array.array_meghalaya_districts),
                getResources().getStringArray(R.array.array_mizoram_districts),
                getResources().getStringArray(R.array.array_nagaland_districts),
                getResources().getStringArray(R.array.array_odisha_districts),
                getResources().getStringArray(R.array.array_puducherry_districts),
                getResources().getStringArray(R.array.array_punjab_districts),
                getResources().getStringArray(R.array.array_rajasthan_districts),
                getResources().getStringArray(R.array.array_sikkim_districts),
                getResources().getStringArray(R.array.array_tamil_nadu_districts),
                getResources().getStringArray(R.array.array_telangana_districts),
                getResources().getStringArray(R.array.array_tripura_districts),
                getResources().getStringArray(R.array.array_uttarakhand_districts),
                getResources().getStringArray(R.array.array_uttar_pradesh_districts),
                getResources().getStringArray(R.array.array_west_bengal_districts)
        };

        AutoCompleteTextView stateAutoCompleteTextView, districtAutoCompleteTextView;
        stateAutoCompleteTextView = stateTIL.findViewById(R.id.stateAutoCompleteTextView);
        districtAutoCompleteTextView = districtTIL.findViewById(R.id.districtAutoCompleteTextView);

        // Create an ArrayAdapter for the states AutoCompleteTextView
        ArrayAdapter<String> stateAdapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, states);
        stateAutoCompleteTextView.setAdapter(stateAdapter);

        // Set an item selected listener for the states AutoCompleteTextView
        stateAutoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Handle the item selection here
                state = (String) parent.getItemAtPosition(position);
                ArrayAdapter<String> districtAdapter = new ArrayAdapter<>(AddPostActivity.this, android.R.layout.simple_dropdown_item_1line, districts[position]);
                districtAutoCompleteTextView.setAdapter(districtAdapter);

                districtAutoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        // Handle the item selection here
                        district = (String) parent.getItemAtPosition(position);
                        ArrayAdapter<String> districtAdapter = new ArrayAdapter<>(AddPostActivity.this, android.R.layout.simple_dropdown_item_1line, districts[position]);
                    }
                });
            }
        });
    }


    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        intent.setType("image/*");
        startActivityForResult(Intent.createChooser(intent, "Select Images"), GALLERY_REQUEST_CODE);
    }

    private void openCamera() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }





    private void uploadImage(List<Uri> imageFiles) {
        List<String> imageUrls = new ArrayList<>();

        for (Uri imageFile : imageFiles) {
            StorageReference imageRef = storageReference.child(System.currentTimeMillis() + ".jpg");
            imageRef.putFile(imageFile).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    imageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            String image = uri.toString();

                            imageUrls.add(uri.toString());

                            // Save URLs to the database once all images are uploaded
                            if (imageUrls.size() == imageFiles.size()) {
                                rootNode.getReference("POSTS").child(postId).child("images").setValue(imageUrls).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        Toast.makeText(AddPostActivity.this, "Images uploaded successfully...", Toast.LENGTH_SHORT).show();
                                        progressBar.setVisibility(View.INVISIBLE);
                                        Intent intent = new Intent(AddPostActivity.this, MainActivity.class);
                                        startActivity(intent);
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(AddPostActivity.this, "Image upload error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        }
                    });
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(AddPostActivity.this, "There is an error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                    progressBar.setVisibility(View.VISIBLE);
                    Toast.makeText(AddPostActivity.this, "Uploading... " +
                                    (float) (100.0 * snapshot.getBytesTransferred() / snapshot.getTotalByteCount()) + " %",
                            Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void AddPost(
            String variety,
            String grade,
            String packing,
            String quantity,
            String state,
            String district,
            String village,
            String description,
            String userId

    ) {
        reference = rootNode.getReference("POSTS");
        postId = reference.push().getKey();
        List<String> images = new ArrayList<>();

        Post post = new Post(
                images,System.currentTimeMillis(),variety,grade, packing, quantity, state, district, village, userId, description, postId
        );

        if (postId != null) {
            reference.child(postId).setValue(post);
            progressBar.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_IMAGE_CAPTURE) {
                // Handle camera photo
                Bundle extras = data.getExtras();
                Bitmap imageBitmap = (Bitmap) extras.get("data");
                if (imageBitmap != null) {
                    // Convert Bitmap to Uri and add to the list
                    Uri imageUri = getImageUri(this, imageBitmap);
                    imageUris.add(imageUri);
                    imageAdapter.notifyDataSetChanged();// Add the captured image

                }
            } else if (requestCode == GALLERY_REQUEST_CODE) {
                // Handle gallery selection
                if (data != null) {
                    // Check if multiple images are selected
                    if (data.getClipData() != null) {
                        ClipData clipData = data.getClipData();
                        imageUris.clear();//
                        // Clear previous selections

                        // Add all selected images
                        for (int i = 0; i < clipData.getItemCount(); i++) {
                            imageUris.add(clipData.getItemAt(i).getUri());
                        }
                        imageAdapter.notifyDataSetChanged();
                    }
                    // Single image selected
                    else if (data.getData() != null) {
                        imageUris.clear(); // Clear previous selections
                        imageUris.add(data.getData()); // Add single selected image
                    }

                    // Upload images to Firebase
                    imageAdapter.notifyDataSetChanged();
                }
            }
        }
    }
    private Uri getImageUri(Context context, Bitmap bitmap) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(context.getContentResolver(), bitmap, "Title", null);
        return Uri.parse(path);
    }
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    }
    private  boolean validateVariety(){
        if(variety==null) {
            varietyTIL.setError("select variety");
            return false;
        }
        else {
            varietyTIL.setError(null);
            return true;
        }

    }


    private  boolean validateGrade(){
        if(grade==null) {
            gradeTIL.setError("select grade");
            return false;
        }
        else {
            gradeTIL.setError(null);
            return true;
        }

    }
    private  boolean validatePacking(){
        if(packing==null) {
            packingTIL.setError("select packing type");
            return false;
        }
        else {
            packingTIL.setError(null);
            return true;
        }

    }
    private  boolean validateState(){
        if(state==null) {
            stateTIL.setError("select state");
            return false;
        }
        else {
            stateTIL.setError(null);
            return true;
        }

    }
    private  boolean validateDistrict(){
        if(district==null) {
            districtTIL.setError("select district");
            return false;
        }
        else {
            districtTIL.setError(null);
            return true;
        }

    }
    private  boolean validateVillage(){
        String village = villageTIL.getEditText().getText().toString();
        if(village.isEmpty()) {
            villageTIL.setError("village is required");
            return false;
        }
        else {
            villageTIL.setError(null);
            return true;
        }

    }
    private  boolean validateDescription(){
        String description = villageTIL.getEditText().getText().toString();
        if(description.isEmpty()) {
            postDescription.setError("village is required");
            return false;
        }
        else {
            postDescription.setError(null);
            return true;
        }

    }
    private  boolean validateQuantity(){
        String quantity = quantityTIL.getEditText().getText().toString();
        if(quantity.isEmpty()) {
            quantityTIL.setError("quantity is required");
            return false;
        }
        else {
            quantityTIL.setError(null);
            return true;
        }

    }
}