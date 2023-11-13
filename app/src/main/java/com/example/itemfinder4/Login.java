package com.example.itemfinder4;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Login extends AppCompatActivity {

    private EditText txtLoginUser, txtLoginPass;
    private Button btnLogin;
    private DatabaseReference usersDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        txtLoginUser = findViewById(R.id.txtLoginUser);
        txtLoginPass = findViewById(R.id.txtLoginPass);
        btnLogin = findViewById(R.id.btnLogin);

        usersDatabase = FirebaseDatabase.getInstance().getReference().child("usuarios");

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String nombre = txtLoginUser.getText().toString();
                String clave = txtLoginPass.getText().toString();

                signInWithUsernameAndPassword(nombre, clave);
            }
        });
    }

    private void signInWithUsernameAndPassword(final String nombre, final String clave) {
        usersDatabase.orderByChild("nombre").equalTo(nombre).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                        Usuario usuario = userSnapshot.getValue(Usuario.class);
                        if (usuario != null && usuario.getClave().equals(clave)) {
                            Toast.makeText(Login.this, "Inicio de sesión exitoso.", Toast.LENGTH_SHORT).show();

                            // Guardar datos de usuario en SharedPreferences
                            guardarDatosUsuarioEnSharedPreferences(usuario);

                            Intent intent = new Intent(Login.this, VistaGeneral.class);
                            startActivity(intent);
                            finish();
                            return;
                        }
                    }
                }

                Toast.makeText(Login.this, "Error al iniciar sesión. Verifica tus credenciales.", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(Login.this, "Error al acceder a la base de datos.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void guardarDatosUsuarioEnSharedPreferences(Usuario usuario) {
        SharedPreferences preferences = getSharedPreferences("usuario", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("uid", usuario.getUid());
        editor.putString("nombreUsuario", usuario.getNombre());
        editor.putString("emailUsuario", usuario.getEmail());
        editor.apply();
    }
}
