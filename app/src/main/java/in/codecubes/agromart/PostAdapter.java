package in.codecubes.agromart;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.PostViewHolder> {

    Context context;
    ArrayList<Post> postList;
    ArrayList<Post> filteredList;

    public PostAdapter(Context context, ArrayList<Post> postList) {
        this.context = context;

        // Sort postList by timestamp in descending order (newest first)
        postList.sort((o1, o2) -> Long.compare(o2.getTimestamp(), o1.getTimestamp()));

        this.postList = postList;
        this.filteredList = new ArrayList<>(postList);
    }

    public void setFilteredList(ArrayList<Post> filteredList) {
        // Sort filteredList by timestamp in descending order
        filteredList.sort((o1, o2) -> Long.compare(o2.getTimestamp(), o1.getTimestamp()));
        this.filteredList = filteredList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.post, parent, false);
        return new PostViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PostViewHolder holder, @SuppressLint("RecyclerView") int position) {
        if (filteredList == null || position < 0 || position >= filteredList.size()) {
            return;
        }

        Post post = filteredList.get(position);

        // Load image safely
        if (post.getImages() != null && !post.getImages().isEmpty() && post.getImages().get(0) != null) {
            Glide.with(context).load(post.getImages().get(0)).into(holder.thumbnail);
        } else {
            // Load placeholder or leave empty
            holder.thumbnail.setImageResource(R.drawable.logo); // use your default image
        }

        // Set variety
        if (post.getVariety() != null) {
            holder.variety.setText(post.getVariety());
        } else {
            holder.variety.setText("Unknown Variety");
        }

        // Set address safely
        String village = post.getVillage() != null ? post.getVillage() : "";
        String district = post.getDistrict() != null ? post.getDistrict() : "";
        holder.address.setText(village + " " + district);

        holder.postListLayout.setOnClickListener(v -> {
            Intent intent = new Intent(context, PostActivity.class);
            intent.putExtra("post_id", post.getPostId());
            intent.putExtra("user_id", post.getUserId());
            context.startActivity(intent);
        });

        holder.postListLayout.setOnLongClickListener(v -> {
            FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
            if (currentUser == null) {
                Toast.makeText(context, "User not logged in", Toast.LENGTH_SHORT).show();
                return false;
            }

            new AlertDialog.Builder(context)
                    .setTitle("Delete Post")
                    .setMessage("Are you sure you want to delete this post?")
                    .setPositiveButton("Delete", (dialog, which) -> {
                        if (post.getUserId() == null) {
                            Toast.makeText(context, "Invalid post data", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        if (currentUser.getUid().equals(post.getUserId())) {
                            filteredList.remove(position);
                            notifyItemRemoved(position);
                            notifyItemRangeChanged(position, getItemCount());

                            FirebaseDatabase.getInstance().getReference()
                                    .child("POSTS")
                                    .child(post.getPostId())
                                    .removeValue();

                            Toast.makeText(context, "Post deleted", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(context, "You can only delete your own posts", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .setNegativeButton("Cancel", null)
                    .show();

            return true;
        });
    }


    @Override
    public int getItemCount() {
        return filteredList.size();
    }

    public class PostViewHolder extends RecyclerView.ViewHolder {

        ImageView thumbnail;
        TextView variety, address;
        LinearLayout postListLayout;

        public PostViewHolder(@NonNull View itemView) {
            super(itemView);
            thumbnail = itemView.findViewById(R.id.post_thumbnail_main);
            variety = itemView.findViewById(R.id.post_variety_main);
            address = itemView.findViewById(R.id.post_address_main);
            postListLayout = itemView.findViewById(R.id.post_list_layout);
        }
    }
}
