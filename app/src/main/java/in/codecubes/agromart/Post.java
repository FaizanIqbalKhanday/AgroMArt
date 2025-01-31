package in.codecubes.agromart;

import java.util.List;

public class Post {
    private String title;
    private List<String> images;
    private String variety;
    private String grade;
    private String packingType;
    private String quantity;
    private String state;
    private String district;
    private String village;
    private String userId;
    private String description;
    private String postId;

    public Post(){}

    public Post(

            List<String> images,
            String variety,
            String grade,
            String packingType,
            String quantity,
            String state,
            String district,
            String village,
            String userId,
            String description,
            String postId) {
        this.images = images;
        this.variety = variety;
        this.grade = grade;
        this.packingType = packingType;
        this.quantity = quantity;
        this.state = state;
        this.district = district;
        this.village = village;
        this.userId = userId;
        this.description = description;
        this.postId = postId;
    }

    public List<String> getImages() {
        return images;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }

    public String getVariety() {
        return variety;
    }

    public void setVariety(String variety) {
        this.variety = variety;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public String getPackingType() {
        return packingType;
    }

    public void setPackingType(String packingType) {
        this.packingType = packingType;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getVillage() {
        return village;
    }

    public void setVillage(String village) {
        this.village = village;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }
}