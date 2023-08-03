package in.codecubes.agromart;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class SetNewPassword extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_new_password);
        Button setNewPasswordBtn= findViewById(R.id.okButton);
        setNewPasswordBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openSetNewPasswordActivity();

            }
        });
    }
    public void openSetNewPasswordActivity(){
        Intent intent= new Intent(this, ForgetPasswordSuccessMessage.class);
        startActivity(intent);
    }
}