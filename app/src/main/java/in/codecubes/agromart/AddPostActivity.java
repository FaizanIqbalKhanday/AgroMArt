package in.codecubes.agromart;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AddPostActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private FirebaseDatabase rootNode;
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private DatabaseReference reference;

    private TextInputLayout varietyTIL, gradeTIL, packingTIL, quantityTIL, stateTIL, districtTIL, villageTIL;
    private Button addPostButton;
    private String variety, grade, packing, state, district;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_post);

        rootNode = FirebaseDatabase.getInstance();
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();

        varietyTIL = findViewById(R.id.selectVariety);
        gradeTIL = findViewById(R.id.selectGrade);
        packingTIL = findViewById(R.id.packingType);
        quantityTIL = findViewById(R.id.setQuantity);
        stateTIL = findViewById(R.id.setState);
        districtTIL = findViewById(R.id.setDistrict);
        villageTIL = findViewById(R.id.set_village);
        addPostButton = findViewById(R.id.addPostButton);

        addPostButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userId = mUser.getUid();
                reference = rootNode.getReference("POSTS");

                // Get village and quantity values
                String quantity = quantityTIL.getEditText().getText().toString();
                String village = villageTIL.getEditText().getText().toString();

                String postId = reference.push().getKey();

                Post post = new Post(variety, grade, packing, quantity, state, district, village, userId, postId);

                if (postId != null) {
                    reference.child(postId).setValue(post);
                    startActivity(new Intent(AddPostActivity.this, MainActivity.class));
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
                        // Get the selected varietyTIL
                        variety  = item.getTitle().toString();

                        // Set the selected varietyTIL to the AutoCompleteTextView
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
                        // Get the selected varietyTIL
                        grade = item.getTitle().toString();

                        // Set the selected varietyTIL to the AutoCompleteTextView
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
                        // Get the selected varietyTIL
                        packing = item.getTitle().toString();

                        // Set the selected varietyTIL to the AutoCompleteTextView
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

//    private  boolean validateVariety(){
//        if(!varietyTIL.isSelected()){
//            varietyTIL.setError("select varietyTIL");
//            return false;
//        }
//        else {
//            varietyTIL.setError(null);
//            return true;
//        }
//    }
//
//    private boolean validateGrade(){
//        if(!gradeTIL.isSelected()){
//            gradeTIL.setError("select gradeTIL");
//            return false;
//        }
//        else {
//            gradeTIL.setError(null);
//            return true;
//        }
//    }
//
//    private boolean validatePackingType(){
//        if(!packingTIL.isSelected()){
//            packingTIL.setError("select packing type");
//            return false;
//        }
//        else {
//            packingTIL.setError(null);
//            return true;
//        }
//    }
//
//    private boolean validateQuantity(){
//        String val =quantityTIL.getEditText().getText().toString();
//        if(val.isEmpty()){
//           quantityTIL.setError("quantity is required");
//            return false;
//        }
//        else {
//            quantityTIL.setError(null);
//            return true;
//        }
//    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {}

    @Override
    public void onNothingSelected(AdapterView<?> parent) {}
}




