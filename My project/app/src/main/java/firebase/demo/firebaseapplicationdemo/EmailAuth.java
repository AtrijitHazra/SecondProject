package firebase.demo.firebaseapplicationdemo;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthEmailException;
import com.google.firebase.auth.FirebaseAuthException;

public class EmailAuth extends AppCompatActivity {

    String TAG = "EmailAuth";

    EditText user_name;
    EditText user_password;
    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email_auth);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        firebaseAuth = FirebaseAuth.getInstance();
        user_name = findViewById(R.id.user_name);
        user_password = findViewById(R.id.user_password);

    }

    public void signUp(View v) {
        String email = user_name.getText().toString().trim();
        String password = user_password.getText().toString().trim();

        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        //checking if success
                        if (task.isSuccessful()) {
                            finish();
                            Toast.makeText(EmailAuth.this, "Registration success", Toast.LENGTH_LONG).show();
                        } else {
                            FirebaseAuthException e = (FirebaseAuthException) task.getException();
                            Log.d(TAG, "onComplete: error "+e);
                            Toast.makeText(EmailAuth.this, ""+e, Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    public void logIn(View v) {
        String email = user_name.getText().toString().trim();
        String password = user_password.getText().toString().trim();

        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            //start the profile activity
                            finish();
                            Toast.makeText(EmailAuth.this, "login success", Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    public void forgotpassword(View v) {
        String email = user_name.getText().toString().trim();
        FirebaseAuth.getInstance().sendPasswordResetEmail(email)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(EmailAuth.this, "Email sent.", Toast.LENGTH_LONG).show();
                        }
                        else
                        {
                            FirebaseAuthEmailException e = (FirebaseAuthEmailException) task.getException();
                            Toast.makeText(getApplicationContext(),""+e,Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}
