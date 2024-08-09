package in.codecubes.agromart;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {
    private Button loginBtn, forgetPasswordBtn ,newUserBtn;
    private TextInputLayout loginEmail ,password;
    private FirebaseAuth mAuth;
    private ProgressBar progress_Bar;
    private DatabaseReference reference;
    private FirebaseDatabase rootNode;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth=FirebaseAuth.getInstance();

        if (mAuth.getCurrentUser() != null) {
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
        }

        loginEmail =findViewById(R.id.loginEmail);
        password=findViewById(R.id.loginPassword);
        loginBtn= findViewById(R.id.loginButton);
        forgetPasswordBtn= findViewById(R.id.forgotPassword);
        progress_Bar=findViewById(R.id.progressBar);
        newUserBtn= findViewById(R.id.newUser);
        rootNode=FirebaseDatabase.getInstance();

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
                   progress_Bar.setVisibility(View.VISIBLE);
                   isUser();
               }

            }
        });
        forgetPasswordBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openForgetPasswordActivity();
                finish();

            }
        });
        newUserBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openSignUpActivity();

            }
        });


    }
    public void openSignUpActivity(){
        Intent intent= new Intent(this, RegisterActivity.class);
        startActivity(intent);


    }
    public void openForgetPasswordActivity(){
        Intent intent= new Intent(this, ForgetPassword.class);
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

    private void isUser(){
        String userEnteredEmail = loginEmail.getEditText().getText().toString().trim();
        String userEnteredPassword = password.getEditText().getText().toString().trim();
        mAuth.signInWithEmailAndPassword(userEnteredEmail, userEnteredPassword)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            // Sign in success, update UI with the signed-in user's information
                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                            startActivity(intent);
                            finish();
                        } else {
                            // If sign in fails, display a message to the user.
                            progress_Bar.setVisibility(View.INVISIBLE);
                            Toast.makeText(getApplicationContext(), "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }


}