package in.codecubes.agromart;
import static android.content.Intent.getIntent;

import android.content.Context;
import android.view.ViewGroup;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;

import java.util.Date;
import java.util.List;
import java.util.Locale;

public class CommitAdapter extends RecyclerView.Adapter<CommitAdapter.ReviewViewHolder> {

    private final List<Commits> commitsList;
    private Context context;


    public CommitAdapter(Context context,List<Commits> commitsList) {
        this.commitsList = commitsList;
        this.context=context;

    }


    @NonNull
    @Override
    public ReviewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate the item layout (item_review.xml)
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.commints, parent, false);
        return new ReviewViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull ReviewViewHolder holder, int position) {
        // Bind data to the ViewHolder
        Commits commit = commitsList.get(position);
        holder.userNameTextView.setText(commit.getUserName());
        holder.commentTextView.setText(commit.getComment());

        // Format and set the date
        if (commit.getTimestamp() > 0) {
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault());
            String formattedDate = dateFormat.format(new Date(commit.getTimestamp()));
            holder.dateTextView.setText(formattedDate);
        } else {
            holder.dateTextView.setText("Unknown Date");
        }

        // Long press to delete the commit
        holder.itemView.setOnLongClickListener(v -> {
            // Get the current user's ID from Firebase Authentication
            FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
            if (currentUser == null) {
                Toast.makeText(context, "User not logged in", Toast.LENGTH_SHORT).show();
                return false; // Prevent crash if no user is logged in
            }
            String currentUserID = currentUser.getUid();

            // Get the commits user ID from the commit object
            String commitUserID = commitsList.get(position).getUserId();

            // Check if the current user is the one who made the commit
            if (currentUserID.equals(commitUserID)) {
                // Show the confirmation dialog
                new AlertDialog.Builder(holder.itemView.getContext()) // Use itemView context
                        .setTitle("Delete Commit")
                        .setMessage("Are you sure you want to delete this commit?")
                        .setPositiveButton("Delete", (dialog, which) -> {
                            // Get the commit ID from the current commit object
                            String postId = getPostIdForComment(position);
                            String commitId = commitsList.get(position).getCommitId();

                            // Check for valid data before proceeding
                            if (postId == null || commitId == null) {
                                Toast.makeText(holder.itemView.getContext(), "Invalid commit data", Toast.LENGTH_SHORT).show();
                                return;
                            }

                            // Firebase reference to delete the commit
                            DatabaseReference commitRef = FirebaseDatabase.getInstance()
                                    .getReference()
                                    .child("POSTS")
                                    .child(postId)
                                    .child("comments")
                                    .child(commitId);

                            // Ensure Firebase data exists before removing
                            commitRef.get().addOnCompleteListener(task -> {
                                if (task.isSuccessful() && task.getResult().exists()) {
                                    // Remove the commit from Firebase
                                    commitRef.removeValue()
                                            .addOnSuccessListener(aVoid -> {
                                                // Successfully removed from database, update RecyclerView
                                                if (commitsList != null && commitsList.size() > position) {
                                                    commitsList.remove(position);
                                                    notifyItemRemoved(position);
                                                    notifyItemRangeChanged(position, getItemCount());
                                                }

                                                // Show confirmation toast
                                                Toast.makeText(holder.itemView.getContext(), "Commit deleted", Toast.LENGTH_SHORT).show();
                                            })
                                            .addOnFailureListener(e -> {
                                                // Handle failure
                                                Toast.makeText(holder.itemView.getContext(), "Failed to delete commit", Toast.LENGTH_SHORT).show();
                                            });
                                } else {
                                    Toast.makeText(holder.itemView.getContext(), "Commit not found in Firebase", Toast.LENGTH_SHORT).show();
                                }
                            });
                        })
                        .setNegativeButton("Cancel", null)
                        .show();
                return true; // Indicate that the long press was handled
            } else {
                // Show a toast if the user tries to delete someone else's commit
                Toast.makeText(holder.itemView.getContext(), "You can only delete your own commits", Toast.LENGTH_SHORT).show();
                return false; // Indicate that the long press was not handled
            }
        });


    }

    private String getPostIdForComment(int position) {
        Commits commit = commitsList.get(position);

        // Return the postId associated with this commit
        return commit.getPostId();
    }


    @Override
    public int getItemCount() {
        return commitsList.size();
    }

    public static class ReviewViewHolder extends RecyclerView.ViewHolder {
        TextView userNameTextView, dateTextView, commentTextView;

        public ReviewViewHolder(@NonNull View itemView) {
            super(itemView);
            // Initialize views from item_review.xml
            userNameTextView = itemView.findViewById(R.id.userNameTextView);
            dateTextView = itemView.findViewById(R.id.dateTextView);
            commentTextView = itemView.findViewById(R.id.commentTextView);
        }
    }
}

