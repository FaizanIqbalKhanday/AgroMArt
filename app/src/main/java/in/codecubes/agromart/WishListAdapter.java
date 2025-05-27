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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import in.codecubes.agromart.R;

public  class WishListAdapter extends RecyclerView.Adapter<WishListAdapter.PostViewHolder> {

    Context context;
    ArrayList<Post> postList;
    ArrayList<Post> filteredList;

    public WishListAdapter(Context context, ArrayList<Post> postList) {
        this.context = context;
        this.postList = postList;
        this.filteredList = new ArrayList<>(postList);
    }

    public WishListAdapter() {

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
        Glide.with(context).load(filteredList.get(position).getImages().get(0)).into(holder.thumbnail);
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
                if (filteredList == null || postList == null || filteredList.isEmpty() || postList.isEmpty()) {
                    Toast.makeText(context, "Invalid data", Toast.LENGTH_SHORT).show();
                    return false;
                }

                if (position < 0 || position >= filteredList.size() || position >= postList.size()) {
                    Toast.makeText(context, "Invalid position", Toast.LENGTH_SHORT).show();
                    return false;
                }

                FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
                if (currentUser == null) {
                    Toast.makeText(context, "User not logged in", Toast.LENGTH_SHORT).show();
                    return false;
                }

                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Remove from Wishlist")
                        .setMessage("Are you sure you want to remove this post from your wishlist?")
                        .setPositiveButton("Remove", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String currentUserID = currentUser.getUid();
                                Post post = filteredList.get(position);
                                if (post == null || post.getPostId() == null) {
                                    Toast.makeText(context, "Invalid post data", Toast.LENGTH_SHORT).show();
                                    return;
                                }

                                String postId = post.getPostId();

                                // Remove from wishlist only (not from POSTS)
                                DatabaseReference wishlistRef = FirebaseDatabase.getInstance().getReference()
                                        .child("wishlist").child(currentUserID).child(postId);

                                wishlistRef.removeValue().addOnCompleteListener(task -> {
                                    if (task.isSuccessful()) {
                                        filteredList.remove(position);
                                        notifyItemRemoved(position);
                                        notifyItemRangeChanged(position, getItemCount());

                                        Toast.makeText(context, "Removed from wishlist", Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(context, "Failed to remove from wishlist", Toast.LENGTH_SHORT).show();
                                    }
                                });
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