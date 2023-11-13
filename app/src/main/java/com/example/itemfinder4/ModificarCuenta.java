package com.example.itemfinder4;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ModificarCuenta extends AppCompatActivity {

    private EditText txtModificarUser, txtModificarEmail, txtModificarPass, txtModificarPass2;
    private Button btnModificar;

    private FirebaseAuth mAuth;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modificar_cuenta);

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            databaseReference = FirebaseDatabase.getInstance().getReference().child("usuarios").child(user.getUid());
        }

        txtModificarUser = findViewById(R.id.txtModificarUser);
        txtModificarEmail = findViewById(R.id.txtModificarEmail);
        txtModificarPass = findViewById(R.id.txtModificarPass);
        txtModificarPass2 = findViewById(R.id.txtModificarPass2);
        btnModificar = findViewById(R.id.btnModificar);

        inicializarFirebase();

        btnModificar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                actualizarDatos();
            }
        });
    }

    private void inicializarFirebase() {
        FirebaseApp.initializeApp(this);
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
    }

    private void actualizarDatos() {
        String nuevoUsuario = txtModificarUser.getText().toString();
        String nuevoEmail = txtModificarEmail.getText().toString();
        String nuevaClave = txtModificarPass2.getText().toString();

        // Obtener el uid del usuario actual
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String uidUsuario = user != null ? user.getUid() : "";

        if (!uidUsuario.isEmpty()) {
            DatabaseReference usuarioRef = databaseReference.child("usuarios").child(uidUsuario);

            if (!nuevoUsuario.isEmpty()) {
                usuarioRef.child("nombre").setValue(nuevoUsuario)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (!task.isSuccessful()) {
                                    Toast.makeText(ModificarCuenta.this, "Error al actualizar el nombre", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
            if (!nuevoEmail.isEmpty()) {
                usuarioRef.child("email").setValue(nuevoEmail)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (!task.isSuccessful()) {
                                    Toast.makeText(ModificarCuenta.this, "Error al actualizar el email", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
            if (!nuevaClave.isEmpty()) {
                usuarioRef.child("clave").setValue(nuevaClave)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (!task.isSuccessful()) {
                                    Toast.makeText(ModificarCuenta.this, "Error al actualizar la clave", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }

            Toast.makeText(ModificarCuenta.this, "Datos actualizados correctamente", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(ModificarCuenta.this, "Error al obtener el UID del usuario actual", Toast.LENGTH_SHORT).show();
        }
    }
}
