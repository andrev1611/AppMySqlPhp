package appmysqlphp.app.andrevera.com.appmysqlphp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    EditText etId,etNombres,etTelefono;
    Button btnGuardar,btnConsultar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etId = (EditText)findViewById(R.id.etId);
        etNombres = (EditText)findViewById(R.id.etNombres);
        etTelefono = (EditText)findViewById(R.id.etTelefono);

        btnGuardar = (Button)findViewById(R.id.btnGuardar);
        btnConsultar = (Button)findViewById(R.id.btnConsultar);

        btnGuardar.setOnClickListener(this);
        btnConsultar.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnGuardar:

                break;
            case R.id.btnConsultar:

                break;
        }
    }
}
