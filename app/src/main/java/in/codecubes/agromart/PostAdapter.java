package in.codecubes.agromart;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

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

            image = (ImageView) itemView.findViewById(R.id.post_thumbnail_main);
            variety = (TextView) itemView.findViewById(R.id.post_variety_main);
            address = (TextView) itemView.findViewById(R.id.post_address_main);
            postListLayout = (LinearLayout) itemView.findViewById(R.id.post_list_layout);
        }
    }
}
