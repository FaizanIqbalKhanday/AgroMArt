package in.codecubes.agromart;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;

import com.google.android.material.textfield.TextInputLayout;

public class ForgetPassword extends AppCompatActivity {
    Button nextBtn;
    TextInputLayout email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);
        nextBtn= findViewById(R.id.nextButton);
        email =findViewById(R.id.forgotPasswordEmail);

        email.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                email.setError(null);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!validateEmail()){
                    return;
                }
                else {
                    openVerifyOtpActivity();
                }
            }
        });
    }
    public void openVerifyOtpActivity(){
        Intent intent= new Intent(this, VerifyOTP.class);
        startActivity(intent);
    }
    private Boolean validateEmail(){
        String val=email.getEditText().getText().toString();
        String emailPattern = "^([\\w\\-\\.]+)@((\\[([0-9]{1,3}\\.){3}[0-9]{1,3}\\])|(([\\w\\-]+\\.)+)([a-zA-Z]{2,4}))$";
        if (val.isEmpty()) {
            email.setError("this field is required");
            return false;
        }
        else if(!val.matches(emailPattern)){
            email.setError("invalid email");
            return false;
        }
        else
            email.setError(null);
        return true;
    }
}