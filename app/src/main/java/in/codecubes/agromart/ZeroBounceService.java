package in.codecubes.agromart;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ZeroBounceService {

    // Define the validateEmail endpoint
    @GET("v2/validate")
    Call<EmailValidationResponse> validateEmail(
            @Query("api_key") String apiKey,  // ZeroBounce API Key
            @Query("email") String email      // The email to validate
    );
}