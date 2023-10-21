package in.codecubes.agromart;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class VerifyOTP extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_verify_otp);
        Button verifyCodeBtn= findViewById(R.id.verifyCodeButton);
        verifyCodeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openVerifyActivity();

            }
        });
    }
    public void openVerifyActivity(){
        Intent intent= new Intent(this, SetNewPassword.class);
        startActivity(intent);
    }
}