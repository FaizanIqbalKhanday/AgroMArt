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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.PostViewHolder> {
    Context context;
    ArrayList<Post> postList;
    ArrayList<Post> filteredList;

    public PostAdapter(Context context, ArrayList<Post> postList) {
        this.context = context;
        this.postList = postList;
        this.filteredList = new ArrayList<>(postList);
    }

    public void setFilteredList(ArrayList<Post> filteredList) {
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
        Glide.with(context).load(filteredList.get(position).getImage()).into(holder.image);
        holder.variety.setText(filteredList.get(position).getVariety());
        String address = filteredList.get(position).getVillage() + " " + filteredList.get(position).getDistrict();
        holder.address.setText(address);

        holder.postListLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, PostActivity.class);
                intent.putExtra("post_id", filteredList.get(position).getPostId());
                intent.putExtra("user_id", filteredList.get(position).getUserId());
                context.startActivity(intent);
            }
        });

        holder.postListLayout.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Delete Post")
                        .setMessage("Are you sure you want to delete this post?")
                        .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // Get the current user's ID
                                String currentUserID = FirebaseAuth.getInstance().getCurrentUser().getUid();

                                // Get the post's user ID
                                String postUserID = filteredList.get(position).getUserId();

                                if (currentUserID.equals(postUserID)) {
                                    // Remove the post from filteredList
                                    filteredList.remove(position);
                                    notifyItemRemoved(position);
                                    notifyItemRangeChanged(position, getItemCount());

                                    // Delete the post from the Firebase Realtime Database
                                    String postId = postList.get(position).getPostId();
                                    DatabaseReference postRef = FirebaseDatabase.getInstance().getReference().child("POSTS").child(postId);
                                    postRef.removeValue();

                                    Toast.makeText(context, "Post deleted", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(context, "You can only delete your own posts", Toast.LENGTH_SHORT).show();
                                }
                            }
                        })
                        .setNegativeButton("Cancel", null)
                        .show();
                return true;
            }
        });
    }

    @Override
    public int getItemCount() {
        return filteredList.size();
    }

    public class PostViewHolder extends RecyclerView.ViewHolder {

        ImageView image;
        TextView variety, address;
        LinearLayout postListLayout;

        public PostViewHolder(@NonNull View itemView) {
            super(itemView);

            image = itemView.findViewById(R.id.post_thumbnail_main);
            variety = itemView.findViewById(R.id.post_variety_main);
            address = itemView.findViewById(R.id.post_address_main);
            postListLayout = itemView.findViewById(R.id.post_list_layout);
        }
    }
}