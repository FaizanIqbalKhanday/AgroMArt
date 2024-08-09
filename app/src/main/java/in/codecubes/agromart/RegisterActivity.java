package in.codecubes.agromart;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends AppCompatActivity {
    private Button signUpBtn;
    private TextInputLayout fullName,email, phoneNumber, password, confirmPassword;
    private DatabaseReference reference;
    private FirebaseAuth mAuth;
    private ImageView menuBar;
    private ProgressBar progress_Bar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth=FirebaseAuth.getInstance();

        if (mAuth.getCurrentUser() != null) {
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
        }

        signUpBtn = findViewById(R.id.signUpButton);
        fullName = findViewById(R.id.fullName);
        email = findViewById(R.id.email);
        phoneNumber = findViewById(R.id.phoneNumber);
        password = findViewById(R.id.password);
        progress_Bar=findViewById(R.id.progressBar);
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
                progress_Bar.setVisibility(View.VISIBLE);

                String userName = fullName.getEditText().getText().toString();
                String userEmail = email.getEditText().getText().toString();
                String userPhoneNumber = phoneNumber.getEditText().getText().toString();
                String userPassword = password.getEditText().getText().toString();
                mAuth.createUserWithEmailAndPassword(userEmail,userPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            FirebaseUser user = mAuth.getCurrentUser();
                            UserHelperClass helperClass = new UserHelperClass(userName,userEmail,userPhoneNumber,userPassword);
                            reference = FirebaseDatabase.getInstance().getReference("user_data");
                            reference.child(user.getUid()).setValue(helperClass);
                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                            startActivity(intent);
                            finish();
                        } else {
                            progress_Bar.setVisibility(View.INVISIBLE);
                            Toast.makeText(RegisterActivity.this,"registration failed",Toast.LENGTH_LONG).show();
                        }

                    }
                });

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
        else if(val.length()<=5){
            password.setError("password is too small");
            return false;
        }
        else if (val.length()>=16){
            password.setError("password is too long");
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