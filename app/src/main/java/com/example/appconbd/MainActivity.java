package com.example.appconbd;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {

    private DatabaseReference database;
    private EditText emailEditText, passwordEditText;
    private Button registerButton, loginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Configuración EdgeToEdge
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Inicializar referencias de UI
        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        registerButton = findViewById(R.id.registerButton);
        loginButton = findViewById(R.id.loginButton);

        // Referencia a Firebase Realtime Database (nodo "users")
        database = FirebaseDatabase.getInstance().getReference("users");

        // --- Abrir Activity Registro ---
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, Registro.class);
                startActivity(intent);
            }
        });


        // --- Login usuario ---
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String inputEmail = emailEditText.getText().toString().trim();
                String inputPassword = passwordEditText.getText().toString().trim();

                if(inputEmail.isEmpty() || inputPassword.isEmpty()){
                    Toast.makeText(MainActivity.this, "Completa todos los campos", Toast.LENGTH_SHORT).show();
                    return;
                }

                String userKey = inputEmail.replace(".", ",");

                database.child(userKey).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {
                        if(snapshot.exists()){
                            String password = snapshot.child("password").getValue(String.class);
                            if(inputPassword.equals(password)){
                                Toast.makeText(MainActivity.this, "Login exitoso", Toast.LENGTH_SHORT).show();

                                // → Abrir Menu Activity
                                Intent intent = new Intent(MainActivity.this, Menu.class);
                                startActivity(intent);

                                // Opcional: cerrar MainActivity para que el usuario no pueda volver con back
                                finish();
                            } else {
                                Toast.makeText(MainActivity.this, "Contraseña incorrecta", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(MainActivity.this, "Usuario no registrado", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError error) {
                        Toast.makeText(MainActivity.this, "Error al leer la base de datos", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

    }
}
