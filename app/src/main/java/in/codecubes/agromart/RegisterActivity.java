package in.codecubes.agromart;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.datepicker.MaterialTextInputPicker;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class RegisterActivity extends AppCompatActivity {
    Button signUpBtn;
    TextInputLayout fullName,email, phoneNumber, password, confirmPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        signUpBtn = findViewById(R.id.signUpButton);
        fullName = findViewById(R.id.fullName);
        email = findViewById(R.id.email);
        phoneNumber = findViewById(R.id.phoneNumber);
        password = findViewById(R.id.password);
        confirmPassword = findViewById(R.id.confirmPassword);

        fullName.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                fullName.setError(null);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

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

        phoneNumber.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                phoneNumber.setError(null);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        password.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                password.setError(null);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        confirmPassword.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                confirmPassword.setError(null);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!validateName() | !validateEmail() | !validatePhoneNumber() | !validateConfirmPassword() | !validatePassword())
                {
                    return;
                }

            }
        });
    }
    private Boolean validateName() {
        String val = fullName.getEditText().getText().toString();
        String validateName ="^[A-Za-z\\s]{1,}[\\.]{0,1}[A-Za-z\\s]{0,}$";
        if (val.isEmpty()) {
            fullName.setError("this field is required");
            fullName.setErrorEnabled(true);
            return false;
        }
        else if(!val.matches(validateName)){
            fullName.setError("this field should contain only alphabet");
            return false;
        }
        else {
            fullName.setError(null);
            return true;
        }
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
    private boolean validatePhoneNumber(){
        String val =phoneNumber.getEditText().getText().toString();
        String phoneNumberPattern ="^\\s*(?:\\+?(\\d{1,3}))?[-. (]*(\\d{3})[-. )]*(\\d{3})[-. ]*(\\d{4})(?: *x(\\d+))?\\s*$";
        if (val.isEmpty()) {
           phoneNumber.setError("this field is required");
            return false;}
       else if(!val.matches(phoneNumberPattern)){
            phoneNumber.setError("invalid phone number");
            return false;
        }
        else{
            phoneNumber.setError(null);
            return true;
        }
    }
    private boolean validatePassword(){
        String val =password.getEditText().getText().toString();
        if(val.isEmpty()){
            password.setError("this field is required");
            return false;
        }
        else{
            password.setError(null);
            return true;
        }
    }
    private boolean validateConfirmPassword(){
        String val=password.getEditText().getText().toString();
        String val2=confirmPassword.getEditText().getText().toString();
        if(!val2.equals(val)){
            confirmPassword.setError("password does not match");
            return false;
        }
        else{
            confirmPassword.setError(null);
            return true;
        }

    }

}