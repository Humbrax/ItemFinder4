package com.example.itemfinder4;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class Perfil extends AppCompatActivity {

    private TextView usernameTextView, emailTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil2);

        // Configurar el Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        usernameTextView = findViewById(R.id.textViewNombre);
        emailTextView = findViewById(R.id.textViewEmail);

        // Intent para verificar si hay datos
        Intent intent = getIntent();
        if (intent != null) {
            // Obtener datos del intent
            String nombreUsuario = intent.getStringExtra("nombreUsuario");
            String emailUsuario = intent.getStringExtra("emailUsuario");

            // Verificar si los datos están en el intent
            if (nombreUsuario != null && emailUsuario != null) {
                // Mostrar datos en los TextView
                usernameTextView.setText(nombreUsuario);
                emailTextView.setText(emailUsuario);
            } else {
                // Si los datos no están en el intent, intentar obtenerlos de SharedPreferences
                SharedPreferences preferences = getSharedPreferences("usuario", MODE_PRIVATE);
                String nombreUsuarioPrefs = preferences.getString("nombreUsuario", null);
                String emailUsuarioPrefs = preferences.getString("emailUsuario", null);

                // Mostrar datos en los TextView si se encuentran en SharedPreferences
                if (nombreUsuarioPrefs != null && emailUsuarioPrefs != null) {
                    usernameTextView.setText(nombreUsuarioPrefs);
                    emailTextView.setText(emailUsuarioPrefs);
                }
            }
        }
    }

    // Inflar el menú en la barra de herramientas
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.bottom_ajustes_cuenta, menu);
        return true;
    }

    // Manejar clics en elementos del menú
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();

        if (itemId == R.id.modificar_cuenta) {
            // Obtener la UID del SharedPreferences o de donde la tengas almacenada
            SharedPreferences preferences = getSharedPreferences("usuario", MODE_PRIVATE);
            String uidUsuario = preferences.getString("uid", "");

            // Abrir la actividad ModificarCuenta y pasar la UID como extra en el Intent
            Intent intentModificarCuenta = new Intent(this, ModificarCuenta.class);
            intentModificarCuenta.putExtra("uidUsuario", uidUsuario);
            startActivity(intentModificarCuenta);
            return true;
        } else if (itemId == R.id.cerrar_sesion) {
            // Abrir la actividad MainActivity
            Intent intentMainActivity = new Intent(this, MainActivity.class);
            startActivity(intentMainActivity);
            // Puedes agregar finish() si deseas cerrar la actividad actual después de ir a MainActivity
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }


}
