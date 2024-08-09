package in.codecubes.agromart;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class EditProfileActivity extends AppCompatActivity {
    private TextInputLayout changeName, changeNumber,stateTIL, districtTIL, villageTIL;
    private TextInputEditText setName,setNumber;
    private String state, district;
    private String phoneNumber;

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

        reference = FirebaseDatabase.getInstance().getReference();
        String userId = getIntent().getStringExtra("user_id");
        if(userId!=null){
            reference.child("user_data").child(getIntent().getStringExtra("user_id")).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        String fullName = snapshot.child("fullName").getValue(String.class);
                        if (fullName != null) {
                            setName.setText(fullName);
                        }

                        String phoneNumber = snapshot.child("phoneNumber").getValue(String.class);
                        if (phoneNumber != null) {
                            setNumber.setText(phoneNumber);
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
        else{

        }



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
}