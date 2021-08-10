package com.example.payphone;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    EditText txtHamburgisa, txtPizza, txtTacos, txtSanduche;
    EditText numero, cedula,nombre;
    Button btnCobrar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        nombre=findViewById(R.id.txtNombre);
        txtHamburgisa=findViewById(R.id.txtHambuerguesa);
        txtPizza=findViewById(R.id.txtPizza);
        txtTacos=findViewById(R.id.txtTacos);
        txtSanduche=findViewById(R.id.txtSanduche);
        numero=findViewById(R.id.txtCelular);
        cedula=findViewById(R.id.txtCedula);
        btnCobrar=findViewById(R.id.button);


        btnCobrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(suma>=100)
                {
                    EjecutarCobroPedido();
                }
                else
                {
                    Toast.makeText(MainActivity.this,"Por favor ingrese una valor valido",Toast.LENGTH_LONG).show();
                }
            }
        });
    }
    int suma=0, masImpuesto=0;

    public void suma(View view)
    {
        suma=Integer.parseInt(txtHamburgisa.getText().toString())+Integer.parseInt(txtPizza.getText().toString())+Integer.parseInt(txtTacos.getText().toString())+Integer.parseInt(txtSanduche.getText().toString());
        TextView textView=findViewById(R.id.textView12);
        textView.setText(""+suma);
        masImpuesto=suma+10;

        TextView textView1=findViewById(R.id.textView14);
        textView1.setText(""+masImpuesto);

    }
    public int aleatorio()
    {
        int numero = (int)(Math.random()*(75000-25000+1)+25000);
        return numero;
    }
    public void EjecutarCobroPedido(){

        String postUrl = "https://pay.payphonetodoesposible.com/api/Sale";
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("phoneNumber", numero.getText().toString());
            jsonObject.put("countryCode", "593");
            jsonObject.put("clientUserId", cedula.getText().toString());
            jsonObject.put("reference", nombre.getText().toString());
            jsonObject.put("responseUrl", "http://paystoreCZ.com/confirm.php");
            jsonObject.put("amount", masImpuesto);
            jsonObject.put("amountWithTax", suma);
            jsonObject.put("amountWithoutTax", "0");
            jsonObject.put("tax", "10");
            jsonObject.put("clientTransactionId", aleatorio());

        } catch (JSONException e) {
            e.printStackTrace();
        }
        System.out.println(jsonObject);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, postUrl, jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                System.out.println(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println(error.getMessage());
                error.printStackTrace();
            }
        })
        {
        @Override
        public Map<String, String> getHeaders() throws AuthFailureError {
            Map<String, String> headers = new HashMap<>();
            headers.put("Content-Type","application/json");
            headers.put("Accept","application/json");
            headers.put("Authorization", "Bearer 1sWaeXjpA1_p8rFmnOn8NtJeMOHLFd7wKb6bQ8ON-VxUXJe9UnZ5DRa7FnqMIQkc_p1b6A8ZOQWTWfv1s5GqqyWzvLzhFUR1fC0Rp761quZ8IXVPjlWDNLdtxQQOQbPu01V3WteObLCeC_BQk8ZqaGMGFUNAwu1k6pPvfHdcU2Um6K0Lbjsa6UXb1pY5GzSxDA-sBwjfQLEVfVPGNIUzAtBUR3EpbhyfX4HlwCBF1OVt6siL56I1UFUDcFmbo4OUz0SPfe1GSBPAo2W87WNYAyHZ_3aRA5LFodcnTN4I2zoe__IT-7zrE8tQ6aGGJwVRmcZmI8OwsR2y5kCwrL649cGMKGk" );
            return headers;
        }
    };

        requestQueue.add(jsonObjectRequest);

    }
}