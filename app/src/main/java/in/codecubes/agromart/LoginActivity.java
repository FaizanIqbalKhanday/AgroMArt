package in.codecubes.agromart;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Button loginBtn= findViewById(R.id.loginButton);
        Button forgetPasswordBtn= findViewById(R.id.forgotPassword);
        Button newUserBtn= findViewById(R.id.newUser);
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               openMainActivity();

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


}