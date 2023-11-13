
package com.example.itemfinder4;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Registro extends AppCompatActivity {

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    EditText txtUser, txtEmail, txtPass;
    Button btnRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);

        txtUser = findViewById(R.id.txtRegisterUser);
        txtEmail = findViewById(R.id.txtRegisterEmail);
        txtPass = findViewById(R.id.txtRegisterPass);
        btnRegister = findViewById(R.id.btnRegister);

        inicializarFirebase();

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                agregarUsuario();
            }
        });
    }

    private void inicializarFirebase() {
        FirebaseApp.initializeApp(this);
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
    }

    private void agregarUsuario() {
        final String nombre = txtUser.getText().toString();
        final String email = txtEmail.getText().toString();
        final String clave = txtPass.getText().toString();

        // Verificar si el nombre ya existe en la base de datos
        databaseReference.child("usuarios").orderByChild("nombre").equalTo(nombre)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            // El nombre ya existe, mostrar un mensaje de error
                            Toast.makeText(Registro.this, "Nombre de usuario ya registrado", Toast.LENGTH_SHORT).show();
                        } else {
                            // El nombre no existe, agregar el nuevo usuario
                            agregarNuevoUsuario(nombre, email, clave);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        // Manejar el error, si es necesario
                        Toast.makeText(Registro.this, "Error al verificar el nombre de usuario", Toast.LENGTH_SHORT).show();
                    }
                });
    }
    private void agregarNuevoUsuario(String nombre, String email, String clave) {
        // Generar un ID único para el usuario
        String userId = databaseReference.child("usuarios").push().getKey();

        Usuario nuevoUsuario = new Usuario();
        nuevoUsuario.setUid(userId);
        nuevoUsuario.setNombre(nombre);
        nuevoUsuario.setEmail(email);
        nuevoUsuario.setClave(clave);

        // Guardar el nuevo usuario en la base de datos
        databaseReference.child("usuarios").child(userId).setValue(nuevoUsuario);

        Toast.makeText(Registro.this, "Usuario agregado correctamente", Toast.LENGTH_SHORT).show();
    }
}
