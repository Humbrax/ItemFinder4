
package com.example.itemfinder4;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class VistaGeneral extends AppCompatActivity {

    private BottomNavigationView navView;
    private String nombreUsuario;
    private String emailUsuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vista_general);

        navView = findViewById(R.id.nav_view);

        SharedPreferences preferences = getSharedPreferences("usuario", MODE_PRIVATE);
        nombreUsuario = preferences.getString("nombreUsuario", "");
        emailUsuario = preferences.getString("emailUsuario", "");

        // Verificar si hay un usuario autenticado
        String uidUsuario = preferences.getString("uid", "");

        if (uidUsuario.isEmpty()) {
            // No hay usuario autenticado, redirigir a la actividad de inicio de sesión
            Intent intent = new Intent(VistaGeneral.this, Login.class);
            startActivity(intent);
            finish();
        } else {
            // Hay un usuario autenticado, configurar el listener de BottomNavigationView
            configurarBottomNavigationView();
        }
    }

    private void configurarBottomNavigationView() {
        navView.setOnNavigationItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.navigation_home) {
                // Lógica para el fragmento Home
                return true;
            } else if (itemId == R.id.navigation_cuenta) {
                // Lógica para el fragmento Cuenta
                return true;
            } else if (itemId == R.id.navigation_buscar) {
                if (!nombreUsuario.isEmpty() && !emailUsuario.isEmpty()) {
                    Intent intent = new Intent(VistaGeneral.this, Perfil.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(VistaGeneral.this, "Inicia sesión primero", Toast.LENGTH_SHORT).show();
                }
                return true;
            }
            return false;
        });

    }
}
