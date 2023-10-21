package in.codecubes.agromart;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;


import android.Manifest;
import android.app.Activity;
import android.content.ClipData;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
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
import java.io.IOException;
import java.util.ArrayList;
import java.util.UUID;

public class AddPostActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private static final int REQUEST_CAMERA_PERMISSION = 200;
    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final int REQUEST_IMAGE_PICK = 2;

    private FirebaseDatabase rootNode;
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private DatabaseReference reference;
    private StorageReference storageReference;

    private TextInputLayout varietyTIL, gradeTIL, packingTIL, quantityTIL, stateTIL, districtTIL, villageTIL;
    private Button addPostButton;
    private ImageView uploadImages, takeImages;
    private Uri imageUri;
    private String variety, grade, packing, state, district;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_add_post);

        rootNode = FirebaseDatabase.getInstance();
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        storageReference = FirebaseStorage.getInstance().getReference("Images");

        varietyTIL = findViewById(R.id.selectVariety);
        gradeTIL = findViewById(R.id.selectGrade);
        packingTIL = findViewById(R.id.packingType);
        quantityTIL = findViewById(R.id.setQuantity);
        stateTIL = findViewById(R.id.setState);
        districtTIL = findViewById(R.id.setDistrict);
        villageTIL = findViewById(R.id.set_village);
        addPostButton = findViewById(R.id.addPostButton);
        uploadImages = findViewById(R.id.uploadImages);
        takeImages = findViewById(R.id.takeImages);

        uploadImages.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery();
            }
        });

        takeImages.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkCameraPermission();
            }
        });

        addPostButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!validateVariety() | !validateGrade() | !validatePacking() | !validateQuantity()
                        | !validateState() | !validateDistrict() | !validateVillage()){
                    return;
                }
                String userId = mUser.getUid();

                // Get village and quantity values
                String quantity = quantityTIL.getEditText().getText().toString();
                String village = villageTIL.getEditText().getText().toString();

                if (imageUri != null) {
                    uploadImage(variety, grade, packing, quantity, state, district, village, userId);
                } else {
                    Toast.makeText(AddPostActivity.this, "Please select image first.", Toast.LENGTH_SHORT).show();
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
                "J&K",
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
//        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, REQUEST_IMAGE_PICK);

    }

    private void checkCameraPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, REQUEST_CAMERA_PERMISSION);
        } else {
            openCamera();
        }
    }

    private void openCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, REQUEST_IMAGE_CAPTURE);
    }

    private void uploadImage(
            String variety,
            String grade,
            String packing,
            String quantity,
            String state,
            String district,
            String village,
            String userId
    ) {
        StorageReference imageRef = storageReference.child(System.currentTimeMillis() + ".jpg");
        reference = rootNode.getReference("POSTS");
        imageRef.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                imageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        String image = uri.toString();
                        String postId = reference.push().getKey();
                        Post post = new Post(variety, grade, packing, quantity, state, district, village, userId, postId, image);

                        if (postId != null) {
                            reference.child(postId).setValue(post);
                            startActivity(new Intent(AddPostActivity.this, MainActivity.class));
                        }
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(AddPostActivity.this, "There is some error " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                Toast.makeText(AddPostActivity.this, "Getting Uploaded... " + (float) (100.0 * snapshot.getBytesTransferred() / snapshot.getTotalByteCount()) + " %", Toast.LENGTH_SHORT).show();
            }
        });
//        try {
//            Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
//            ByteArrayOutputStream baos = new ByteArrayOutputStream();
//            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
//            byte[] data = baos.toByteArray();
//
//            UploadTask uploadTask = imageRef.putBytes(data);
//            uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
//
//                @Override
//                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                    imageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
//                        @Override
//                        public void onSuccess(Uri uri) {
//                            String imageUrl = uri.toString();
//                            Toast.makeText(AddPostActivity.this, "image " + imageUrl, Toast.LENGTH_SHORT).show();
//                            reference.child(postId).child("imageUrl").setValue(imageUrl);
//                            startActivity(new Intent(AddPostActivity.this, MainActivity.class));
//                        }
//                    });
//                }
//            });
//        } catch (IOException e) {
//            Log.e("UploadImage", "IOException: " + e.getMessage());
//        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_IMAGE_CAPTURE) {
//                Bitmap imageBitmap = (Bitmap) data.getExtras().get("data");
//                uploadImages.setImageBitmap(imageBitmap);
//                imageUri = getImageUri(imageBitmap);
                Glide.with(AddPostActivity.this).load(data.getData()).into(uploadImages);
                imageUri = data.getData();
            } else if (requestCode == REQUEST_IMAGE_PICK && data != null) {
                Uri selectedImageUri = data.getData();
                imageUri = data.getData();
                Glide.with(AddPostActivity.this).load(data.getData()).into(uploadImages);
//                try {
//                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), selectedImageUri);
//                    uploadImages.setImageBitmap(bitmap);
//                } catch (IOException e) {
//                    Log.e("ImagePick", "IOException: " + e.getMessage());
//                }
            }
        }
    }

    private Uri getImageUri(Bitmap bitmap) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(getContentResolver(), bitmap, "Title", null);
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
            districtTIL.setError("select variety");
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