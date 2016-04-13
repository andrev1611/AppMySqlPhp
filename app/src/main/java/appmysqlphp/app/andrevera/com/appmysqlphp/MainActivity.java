package appmysqlphp.app.andrevera.com.appmysqlphp;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;

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
                String nombre,telefono;
                nombre=etNombres.getText().toString();
                telefono=etTelefono.getText().toString();
                //en mi caso es localhost:8080 por eso aca le pongo 10.0.3.2:8080
                //10.0.3.2 es la ip para los emuladores de Genymotion
                new CargarDatos().execute("http://10.0.3.2:8080/AndroidMySql/registro.php?nombres="+nombre+"&tel="+telefono);
                break;
            case R.id.btnConsultar:
                String id;
                id=etId.getText().toString();

                new ConsultarDatos().execute("http://10.0.3.2:8080/AndroidMySql/consulta.php?id="+id);
                break;
        }
    }
    //METODO ASINCRONO 1
    private class CargarDatos extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {

            // params comes from the execute() call: params[0] is the url.
            try {
                return downloadUrl(urls[0]);
            } catch (IOException e) {
                return "Unable to retrieve web page. URL may be invalid.";
            }
        }
        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {
            //textView.setText(result);
            Toast.makeText(getApplicationContext(),"Se guardó los datos correctamente",Toast.LENGTH_SHORT).show();
        }
    }
    //METODO ASINCRONO 2
    private class ConsultarDatos extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {

            // params comes from the execute() call: params[0] is the url.
            try {
                return downloadUrl(urls[0]);
            } catch (IOException e) {
                return "Unable to retrieve web page. URL may be invalid.";
            }
        }
        // esto sucede cuando se terminna de ejecutar la url que mandamos
        @Override
        protected void onPostExecute(String result) {
            //Toast.makeText(getApplicationContext(),"Se consultó los datos correctamente",Toast.LENGTH_SHORT).show();
            JSONArray ja = null;
            try {
                ja = new JSONArray(result);//dado que en la consulta estamos trayendo t odo id nombre y telefono
                etNombres.setText(ja.getString(1));//aqui seria 1 que es el nombre
                etTelefono.setText(ja.getString(2));//aqui seria 2 que es el telefono 0 sería el id

            }catch (JSONException e){//esto ocurrirá cuando suceda un error o no se encuentre datos para traer osea el id no exista
                Toast.makeText(getApplicationContext(),"Ocurrio un error al recuperar los datos!",Toast.LENGTH_SHORT).show();
            }
        }
    }
    //el siguiente método está en http://developer.android.com/intl/es/training/basics/network-ops/connecting.html
    private String downloadUrl(String myurl) throws IOException {
        Log.i("URL",""+myurl);
        myurl = myurl.replace(" ","%20");
        //coge la url de longitud maxima de 500 y la convierte en un inputstream
        InputStream is = null;
        int len = 500;

        try {
            URL url = new URL(myurl);//creamos una url
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();//y aqui abrimos la url
            conn.setReadTimeout(10000 /* milliseconds */);//tiempo tope para lalectura
            conn.setConnectTimeout(15000 /* milliseconds */);//tiempo tope para la conección
            conn.setRequestMethod("GET");//el metodo para enviar los datos GET POST
            conn.setDoInput(true);//esto no se
            // empiza la conección
            conn.connect();
            int response = conn.getResponseCode();//aqui se guardara un valor dependiendo si se hizo o no la conección
            //200 es que hubo conección y 404 es que no la hubo
            Log.d("MensajeRespuesta", "The response is: " + response);//aquí lo mostramos
            is = conn.getInputStream();//lo que responda la URL lo guardamos en is que es inputStream

            // Convertimos el InputStream a string
            String contentAsString = readIt(is, len);//is y len que es longitud maxima
            return contentAsString;

            // Makes sure that the InputStream is closed after the app is
            // finished using it.
        } finally {
            if (is != null) {
                is.close();
            }
        }
    }
    //MÉTODO PARA CONVERTIR EL INPUTSREAM A STRING - TAMBIEN HAY PARA CONVERTIR EL IS A UN BITMAP OSEA IMAGEN
    public String readIt(InputStream stream, int len) throws IOException {//UnsupportedEncodingException esto puede o no ir, normal
        Reader reader = null;
        reader = new InputStreamReader(stream, "UTF-8");
        char[] buffer = new char[len];
        reader.read(buffer);
        return new String(buffer);
    }
}
