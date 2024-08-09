package in.codecubes.agromart;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;

import com.google.android.material.textfield.TextInputLayout;

public class SetNewPassword extends AppCompatActivity {
    Button setNewPasswordBtn;
    TextInputLayout password, confirmPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_set_new_password);
        setNewPasswordBtn= findViewById(R.id.okButton);
        password =findViewById(R.id.newPassword);
        confirmPassword =findViewById(R.id.confirmNewPassword);

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
        setNewPasswordBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!validatePassword() | !validateConfirmPassword()){
                    return;
                }
                else {
                    openSetNewPasswordActivity();
                }

            }
        });
    }
    public void openSetNewPasswordActivity(){
        Intent intent= new Intent(this, ForgetPasswordSuccessMessage.class);
        startActivity(intent);
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