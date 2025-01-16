package in.codecubes.agromart;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.TextView;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_posts);

        postRecyclerView=findViewById(R.id.posts_recycler_view);
        postRecyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(this, 2);
        postRecyclerView.setLayoutManager(layoutManager);

    }
    public void loadData(){
        reference= FirebaseDatabase.getInstance().getReference().child("POSTS");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}