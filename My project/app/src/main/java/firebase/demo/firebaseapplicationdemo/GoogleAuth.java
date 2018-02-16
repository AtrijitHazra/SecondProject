package firebase.demo.firebaseapplicationdemo;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import firebase.demo.firebaseapplicationdemo.firebase.SendMessage;

public class GoogleAuth extends AppCompatActivity {

    String TAG = "GoogleAuth";

    TextView id, name;
    ImageView image;
    private static final int RC_SIGN_IN = 9001;

    private FirebaseAuth mAuth;
    private GoogleSignInClient mGoogleSignInClient;
    DatabaseReference myRef;

    String fToken;
    ArrayList<UserDetails> allUserDetails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_google_auth);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        id = findViewById(R.id.id_profile);
        name = findViewById(R.id.name);
        image = findViewById(R.id.image);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        // [START initialize_auth]
        mAuth = FirebaseAuth.getInstance();
        // [END initialize_auth]
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);

    }

    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            id.setText(currentUser.getUid());
            name.setText(currentUser.getDisplayName());
            Picasso.with(this).load(currentUser.getPhotoUrl()).into(image);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.d(TAG, "Google sign in failed", e);
            }
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getId());

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            id.setText(user.getUid());
                            name.setText(user.getDisplayName());
                            mGoogleSignInClient.signOut().addOnCompleteListener(GoogleAuth.this, new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    Log.d(TAG, "onComplete: sign out");
                                }
                            });
                            storeIntoFirebaseDb(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                        }
                    }
                });
    }

    public void storeIntoFirebaseDb(FirebaseUser user) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        myRef = database.getReference("user_details");
        fToken = FirebaseInstanceId.getInstance().getToken();
        UserDetails userDetails = new UserDetails();
        userDetails.setUserToken(fToken);
        userDetails.setUserName(user.getDisplayName());
        userDetails.setUserEmail(user.getEmail());
        userDetails.setUserPh(user.getPhoneNumber());
        userDetails.setUserImage(user.getPhotoUrl().toString());

        Log.d(TAG, " storeIntoFirebaseDb: " + fToken);
        myRef.child(fToken).setValue(userDetails);
    }

    public void getData(View v) {
        myRef.child("jhfdkjsf8983943rhjkhdjkhdjksa").child("userImage").setValue("https://lh6.googleusercontent.com/-pf1WQc93YyI/AAAAAAAAAAI/AAAAAAAABX4/5dwNMp09WjQ/s96-c/photo.jpg");
        myRef.child("jhfdkjsf8983943rhjkhdjkhdjksadsdsadsadsd").child("userImage").setValue("https://lh6.googleusercontent.com/-pf1WQc93YyI/AAAAAAAAAAI/AAAAAAAABX4/5dwNMp09WjQ/s96-c/photo.jpg");
        myRef.child("jhfdkjsf8983943rhjkhdjkhdjksadsdsasdsdsddsadsd").child("userImage").setValue("https://lh6.googleusercontent.com/-pf1WQc93YyI/AAAAAAAAAAI/AAAAAAAABX4/5dwNMp09WjQ/s96-c/photo.jpg");
        myRef.child("jhfdkjsf8983943rhjkhdjkhdjksadsdsasdsdsdsdsdsdaerewredsddsadsd").child("userImage").setValue("https://lh6.googleusercontent.com/-pf1WQc93YyI/AAAAAAAAAAI/AAAAAAAABX4/5dwNMp09WjQ/s96-c/photo.jpg");

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                allUserDetails = new ArrayList<>();
                for (DataSnapshot ymdSnapshot : dataSnapshot.getChildren()) {
                    Log.d("ymdSnapshot", ymdSnapshot.getKey().toString());
                    UserDetails userDetails1 = ymdSnapshot.getValue(UserDetails.class);
                    System.out.println(userDetails1.toString());
                    allUserDetails.add(userDetails1);
                }
                displayuserData();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });
    }

    public void displayuserData() {
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        UserDetailsAdapter adapter = new UserDetailsAdapter(GoogleAuth.this, allUserDetails);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(adapter);
        final ArrayList<UserDetails> toSend = new ArrayList<>();

        recyclerView.addOnItemTouchListener(new UserDetailsAdapter.RecyclerTouchListener(this, recyclerView, new UserDetailsAdapter.ClickListener() {
            @Override
            public void onClick(int position) {
                toSend.add(allUserDetails.get(position));
                Intent in = new Intent(GoogleAuth.this, SendMessage.class);
                in.putParcelableArrayListExtra("tokens", toSend);
                startActivity(in);
            }

            @Override
            public void onLongClick(int position) {

            }
        }));

       /* FirebaseMessaging fm = FirebaseMessaging.getInstance();
        fm.send(new RemoteMessage.Builder(SENDER_ID + "@gcm.googleapis.com")
                .setMessageId(Integer.toString(msgId.incrementAndGet()))
                .addData("my_message", "Hello World")
                .addData("my_action", "SAY_HELLO")
                .build());*/

    }
}
