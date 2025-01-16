package in.codecubes.agromart;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;

public class ForgetPassword extends AppCompatActivity {
    Button nextBtn;
    TextInputLayout email;
    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);
        nextBtn = findViewById(R.id.nextButton);
        email = findViewById(R.id.forgotPasswordEmail);
        firebaseAuth = FirebaseAuth.getInstance();

        email.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                email.setError(null);
            }

            @Override
            public void afterTextChanged(Editable editable) {}
        });

        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!validateEmail()) {
                    return;
                } else {
                    sendResetPasswordEmail();
                }
            }
        });
    }

    private Boolean validateEmail() {
        String val = email.getEditText().getText().toString();
        String emailPattern = "^([\\w\\-\\.]+)@((\\[([0-9]{1,3}\\.){3}[0-9]{1,3}\\])|(([\\w\\-]+\\.)+)([a-zA-Z]{2,4}))$";
        if (val.isEmpty()) {
            email.setError("This field is required");
            return false;
        } else if (!val.matches(emailPattern)) {
            email.setError("Invalid email");
            return false;
        } else {
            email.setError(null);
        }
        return true;
    }

    private void sendResetPasswordEmail() {
        String emailAddress = email.getEditText().getText().toString();

        firebaseAuth.sendPasswordResetEmail(emailAddress).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Toast.makeText(ForgetPassword.this, "Password reset email sent.", Toast.LENGTH_SHORT).show();
                Intent intent= new Intent(this, LoginActivity.class);
                startActivity(intent);
            } else {
                Toast.makeText(ForgetPassword.this, "Error: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
