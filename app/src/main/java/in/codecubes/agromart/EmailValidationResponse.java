package in.codecubes.agromart;

public class EmailValidationResponse {
    private String status;
    private String email;
    private String verdict;
    private String error;

    // Getters and setters
    public String getStatus() {
        return status;
    }

    public String getEmail() {
        return email;
    }

    public String getVerdict() {
        return verdict;
    }

    public String getError() {
        return error;
    }
}
