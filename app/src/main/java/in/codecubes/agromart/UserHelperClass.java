package in.codecubes.agromart;

public class UserHelperClass {
    String fullName, email, phoneNumber;

    public UserHelperClass() {
    }

    public UserHelperClass(String fullName, String email, String phoneNumber) {
        this.fullName = fullName;
        this.email = email;
        this.phoneNumber = phoneNumber;
    }
    UserHelperClass(String fullName){
        this.fullName=fullName;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

}
