package in.codecubes.agromart;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.smarteist.autoimageslider.SliderView;

import java.util.ArrayList;

public class PostActivity extends AppCompatActivity {

    private Button callBtn, chatBtn;
    private TextView full_name, variety, grade, packing, quantity, address, address2, userName, userPhoneNumber;
    private String phoneNumber;
    private DatabaseReference reference;
    private SliderAdapter adapter;

    private String url1 = "https://images.unsplash.com/photo-1682685795557-976f03aca7b2?ixlib=rb-4.0.3&ixid=M3wxMjA3fDF8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D&auto=format&fit=crop&w=871&q=80";
    private String url2 = "https://images.unsplash.com/photo-1682687220198-88e9bdea9931?ixlib=rb-4.0.3&ixid=M3wxMjA3fDF8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D&auto=format&fit=crop&w=870&q=80";
    private String url3 = "https://images.unsplash.com/photo-1682687220742-aba13b6e50ba?ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D&auto=format&fit=crop&w=870&q=80";

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);

        callBtn = (Button) findViewById(R.id.call_btn);
        chatBtn = (Button) findViewById(R.id.chat_btn);

        variety = (TextView) findViewById(R.id.post_variety);
        grade = (TextView) findViewById(R.id.post_grade);
        packing = (TextView) findViewById(R.id.post_packing);
        quantity = (TextView) findViewById(R.id.post_quantity);
        address = (TextView) findViewById(R.id.post_user_address);
        address2 = (TextView) findViewById(R.id.post_address_2);
        userName = (TextView) findViewById(R.id.post_user_name);
        full_name = (TextView) findViewById(R.id.full_name);
        userPhoneNumber = (TextView) findViewById(R.id.post_user_phone);

        // we are creating array list for storing our image urls.
        ArrayList<SliderData> sliderDataArrayList = new ArrayList<>();

        // initializing the slider view.
        SliderView sliderView = findViewById(R.id.slider);

        reference = FirebaseDatabase.getInstance().getReference();
        reference.child("POSTS").child(getIntent().getStringExtra("post_id")).addValueEventListener(new ValueEventListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                variety.setText(snapshot.child("variety").getValue(String.class));
                grade.setText(snapshot.child("grade").getValue(String.class));
                packing.setText(snapshot.child("packingType").getValue(String.class));
                quantity.setText(snapshot.child("quantity").getValue(String.class) + " Boxes");
                String addr = snapshot.child("village").getValue(String.class)
                                + " "
                                + snapshot.child("district").getValue(String.class)
                                + " "
                                + snapshot.child("state").getValue(String.class);
                address.setText(addr);
                address2.setText(addr);
                sliderDataArrayList.add(new SliderData(snapshot.child("image").getValue(String.class)));
                // passing this array list inside our adapter class.
                adapter = new SliderAdapter(PostActivity.this, sliderDataArrayList);
                // below method is used to
                // setadapter to sliderview.
                sliderView.setSliderAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        reference.child("user_data").child(getIntent().getStringExtra("user_id")).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                userName.setText(snapshot.child("fullName").getValue(String.class));
                full_name.setText(snapshot.child("fullName").getValue(String.class));
                String phone = snapshot.child("phoneNumber").getValue(String.class);
                phoneNumber = phone;
                userPhoneNumber.setText(phone);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

//        // adding the urls inside array list
//        sliderDataArrayList.add(new SliderData(url1));
//        sliderDataArrayList.add(new SliderData(url2));
//        sliderDataArrayList.add(new SliderData(url3));

        // below method is used to set auto cycle direction in left to
        // right direction you can change according to requirement.
        sliderView.setAutoCycleDirection(SliderView.LAYOUT_DIRECTION_LTR);

        // below method is use to set
        // scroll time in seconds.
        sliderView.setScrollTimeInSec(3);

        // to set it scrollable automatically
        // we use below method.
        sliderView.setAutoCycle(true);

        // to start autocycle below method is used.
        sliderView.startAutoCycle();

        callBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent callIntent = new Intent(Intent.ACTION_DIAL);
                callIntent.setData(Uri.parse("tel:" + phoneNumber));
                startActivity(callIntent);
            }
        });

        chatBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = "https://api.whatsapp.com/send?phone=" + phoneNumber;
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(url));
                startActivity(intent);
            }
        });
    }

}