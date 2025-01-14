package in.codecubes.agromart;

public class UpdateProfile {
    private String fullName;
    private String PhoneNumber;
    private String state;
    private String district;
    private String village;

    public UpdateProfile() {
    }

    public UpdateProfile(String fullName, String phoneNumber, String state, String district, String village) {
        this.fullName = fullName;
        PhoneNumber = phoneNumber;
        this.state = state;
        this.district = district;
        this.village = village;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getPhoneNumber() {
        return PhoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        PhoneNumber = phoneNumber;
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
}
