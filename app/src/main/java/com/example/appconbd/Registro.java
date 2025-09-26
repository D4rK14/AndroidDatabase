package com.example.appconbd;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Registro extends AppCompatActivity {

    private DatabaseReference database;
    private EditText emailEditText, passwordEditText;
    private Button registerButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);

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

        // Referencia a Firebase Realtime Database (nodo "users")
        database = FirebaseDatabase.getInstance().getReference("users");

        // --- Registrar usuario ---
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = emailEditText.getText().toString().trim();
                String password = passwordEditText.getText().toString().trim();

                if(email.isEmpty() || password.isEmpty()){
                    Toast.makeText(Registro.this, "Completa todos los campos", Toast.LENGTH_SHORT).show();
                    return;
                }

                String userKey = email.replace(".", ",");
                User user = new User(email, password);

                database.child(userKey).setValue(user).addOnCompleteListener(task -> {
                    if(task.isSuccessful()){
                        Toast.makeText(Registro.this, "Usuario registrado", Toast.LENGTH_SHORT).show();
                        finish(); // cerrar la Activity de registro y volver al login
                    } else {
                        Toast.makeText(Registro.this, "Error al registrar", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }
}
