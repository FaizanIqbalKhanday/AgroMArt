package in.codecubes.agromart;

public class Commits {
    private String userName;
    private String comment;
    private long timestamp;
    private String commitId; // Field for commit ID
    private String userId;
    private String postId;// Field for user ID

    public Commits() {
        // Default constructor
    }

    public Commits(String userName, String comment, long timestamp, String commitId, String userId,String postId) {
        this.userName = userName;
        this.comment = comment;
        this.timestamp = timestamp;
        this.commitId = commitId;
        this.userId = userId;
        this.postId=postId;

    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String getCommitId() {
        return commitId; // Return the commit ID
    }

    public void setCommitId(String commitId) {
        this.commitId = commitId;
    }

    public String getUserId() {
        return userId; // Return the user ID
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }
}


