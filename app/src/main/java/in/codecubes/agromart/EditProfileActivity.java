package in.codecubes.agromart;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Objects;

public class EditProfileActivity extends AppCompatActivity {
    private TextInputLayout changeName, changeNumber,stateTIL, districtTIL, villageTIL;
    private TextInputEditText setName,setNumber;
    private String state, district;
    private FirebaseUser mUser;
    private String phoneNumber;
    private String userId;
    private ProgressBar progressBar;
    private AppCompatButton saveChangesButton;

    private DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        changeName=findViewById(R.id.change_name);
        changeNumber=findViewById(R.id.change_phoneNumber);
        setName=findViewById(R.id.newName);
        setNumber=findViewById(R.id.newNumber);
        stateTIL=findViewById(R.id.setState);
        districtTIL=findViewById(R.id.setDistrict);
        villageTIL=findViewById(R.id.set_village);
        saveChangesButton=findViewById(R.id.updateButton);
        progressBar=findViewById(R.id.progressBar);

        mUser= FirebaseAuth.getInstance().getCurrentUser();
        userId =mUser.getUid();
        if (mUser!=null){

        }
        reference= FirebaseDatabase.getInstance().getReference("user_data");
        getUserData();


        saveChangesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!validateName() | !validatePhoneNumber() |!validateState() |!validateDistrict() | !validateVillage()){
                    return;
                }
                progressBar.setVisibility(View.VISIBLE);
                updateUserData();
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
                "Jammu and Kashmir",
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
                ArrayAdapter<String> districtAdapter = new ArrayAdapter<>(EditProfileActivity.this, android.R.layout.simple_dropdown_item_1line, districts[position]);
                districtAutoCompleteTextView.setAdapter(districtAdapter);

                districtAutoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        // Handle the item selection here
                        district = (String) parent.getItemAtPosition(position);
                        ArrayAdapter<String> districtAdapter = new ArrayAdapter<>(EditProfileActivity.this, android.R.layout.simple_dropdown_item_1line, districts[position]);
                    }
                });
            }
        });
    }
    private Boolean validateName() {
        String val = changeName.getEditText().getText().toString();
        String validateName ="^[A-Za-z\\s]{1,}[\\.]{0,1}[A-Za-z\\s]{0,}$";
        if (val.isEmpty()) {
            changeName.setError("this field is required");
            changeName.setErrorEnabled(true);
            return false;
        }
        else if(!val.matches(validateName)){
            changeName.setError("this field should contain only alphabet");
            return false;
        }
        else {
            changeName.setError(null);
            return true;
        }
    }
    private boolean validatePhoneNumber(){
        String val =changeNumber.getEditText().getText().toString();
        String phoneNumberPattern ="^\\s*(?:\\+?(\\d{1,3}))?[-. (]*(\\d{3})[-. )]*(\\d{3})[-. ]*(\\d{4})(?: *x(\\d+))?\\s*$";
        if (val.isEmpty()) {
            changeNumber.setError("this field is required");
            return false;}
        else if(!val.matches(phoneNumberPattern)){
            changeNumber.setError("invalid phone number");
            return false;
        }
        else{
            changeNumber.setError(null);
            return true;
        }
    }
    private void getUserData(){
        reference.child(userId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                setName.setText(snapshot.child("fullName").getValue(String.class));
                setNumber.setText(snapshot.child("phoneNumber").getValue(String.class));


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private void updateUserData() {
        String updatedName = setName.getText().toString().trim();
        String updatedNumber = setNumber.getText().toString().trim();
        String updatedVillage = villageTIL.getEditText().getText().toString().trim();

        // Ensure state and district are selected
        if (state == null || district == null) {
            Toast.makeText(this, "Please select state and district.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Create a map to hold the updated data
        HashMap<String, Object> updates = new HashMap<>();
        updates.put("fullName", updatedName);
        updates.put("phoneNumber", updatedNumber);
        updates.put("state", state);
        updates.put("district", district);
        updates.put("village", updatedVillage);

        // Push data to Firebase under the user's node
        reference.child(userId).updateChildren(updates).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                progressBar.setVisibility(View.INVISIBLE);
                Toast.makeText(EditProfileActivity.this, "Profile updated successfully.", Toast.LENGTH_SHORT).show();
                Intent intent=new Intent(this,ProfileUI.class);
                startActivity(intent);
            } else {
                Toast.makeText(EditProfileActivity.this, "Failed to update profile.", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(e -> {
            Toast.makeText(EditProfileActivity.this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            Log.e("UpdateError", Objects.requireNonNull(e.getMessage()));
        });
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
        String village = Objects.requireNonNull(villageTIL.getEditText()).getText().toString();
        if(village.isEmpty()) {
            villageTIL.setError("village is required");
            return false;
        }
        else {
            villageTIL.setError(null);
            return true;
        }

    }
}