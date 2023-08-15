package in.codecubes.agromart;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;

import com.google.android.material.textfield.TextInputLayout;

public class LoginActivity extends AppCompatActivity {
    Button loginBtn, forgetPasswordBtn ,newUserBtn;
    TextInputLayout loginEmail ,password;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        loginEmail =findViewById(R.id.loginEmail);
        password=findViewById(R.id.loginPassword);
        loginBtn= findViewById(R.id.loginButton);
        forgetPasswordBtn= findViewById(R.id.forgotPassword);
        newUserBtn= findViewById(R.id.newUser);

        loginEmail.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // Do nothing
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                loginEmail.setError(null); // Hide the error message
            }

            @Override
            public void afterTextChanged(Editable s) {
                // Do nothing
            }
        });

        password.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // Do nothing
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                password.setError(null); // Hide the error message
            }

            @Override
            public void afterTextChanged(Editable s) {
                // Do nothing
            }
        });

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               if(!validateEmail() | !validatePassword()){
                   return;
               }
               else {
                   openMainActivity();
               }

            }
        });
        forgetPasswordBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openForgetPasswordActivity();

            }
        });
        newUserBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openSignUpActivity();

            }
        });


    }
    public void openMainActivity(){
        Intent intent= new Intent(this, MainActivity.class);
        startActivity(intent);


    }
    public void openForgetPasswordActivity(){
        Intent intent= new Intent(this, ForgetPassword.class);
        startActivity(intent);
    }
    public void openSignUpActivity(){
        Intent intent= new Intent(this, RegisterActivity.class);
        startActivity(intent);
    }


    private Boolean validateEmail(){
        String val=loginEmail.getEditText().getText().toString();
        String emailPattern = "^([\\w\\-\\.]+)@((\\[([0-9]{1,3}\\.){3}[0-9]{1,3}\\])|(([\\w\\-]+\\.)+)([a-zA-Z]{2,4}))$";
        if (val.isEmpty()) {
            loginEmail.setError("this field is required");
            return false;
        }
        else if(!val.matches(emailPattern)){
           loginEmail.setError("invalid email");
            return false;
        }
        else
            loginEmail.setError(null);
        return true;
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


}