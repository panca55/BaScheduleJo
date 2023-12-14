package com.panca_nugraha.baschedulejo;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import org.checkerframework.checker.nullness.qual.NonNull;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class RegisterActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private EditText emailEditText, passwordEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();
        emailEditText = findViewById(R.id.registerEmail);
        passwordEditText = findViewById(R.id.registerPassword);
        Button registerButton = findViewById(R.id.registerButton);
        Button loginButton = findViewById(R.id.loginButton);

        registerButton.setOnClickListener(view -> {
            // Logika untuk melakukan registrasi
            register();
        });

        loginButton.setOnClickListener(view -> {
            // Ketika tombol login diklik, buka LoginActivity
            Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
            startActivity(intent);
        });
    }

    public void register() {
        String email = emailEditText.getText().toString();
        String password = passwordEditText.getText().toString();

        // Validasi minimal email dan password (ini tetap tidak aman)
        if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
            Toast.makeText(this, "Email dan password harus diisi", Toast.LENGTH_SHORT).show();
            return;
        }

        // Registrasi pengguna menggunakan Firebase Authentication
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            FirebaseUser user = mAuth.getCurrentUser();
                            saveUserDataToFirestore(email,password);
                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(RegisterActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            saveUserDataToFirestore(null,null);
                        }
                    }
                });
    }

    private void saveUserDataToFirestore(String email, String pasword) {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            // User telah terautentikasi, dapatkan UID
            String userId = currentUser.getUid();

            // Simpan informasi pengguna ke dalam Firestore
            Map<String, Object> userData = new HashMap<>();
            userData.put("email", email);

            FirebaseFirestore.getInstance().collection("users")
                    .document(userId)
                    .set(userData)
                    .addOnSuccessListener(aVoid -> {
                        // Registrasi dan penyimpanan di Firestore berhasil
                        Toast.makeText(RegisterActivity.this, "Registrasi berhasil", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                        startActivity(intent);
                    })
                    .addOnFailureListener(e -> {
                        // Penyimpanan di Firestore gagal, tampilkan pesan kesalahan
                        Toast.makeText(RegisterActivity.this, "Gagal menyimpan data pengguna", Toast.LENGTH_SHORT).show();
                    });
        } else {
            // User belum terautentikasi, handle sesuai kebutuhan aplikasi Anda
            Toast.makeText(RegisterActivity.this, "User belum terautentikasi", Toast.LENGTH_SHORT).show();
        }
    }

}
