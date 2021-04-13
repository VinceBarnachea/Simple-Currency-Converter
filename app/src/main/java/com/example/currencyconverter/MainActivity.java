package com.example.currencyconverter;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

   // private Spinner spinner1, spinner2;
    private EditText baseCurrency;
    private TextView resultCurrency, convertFrom, convertTo;
    private Button convert;
    private DatabaseReference Rootref;
    private ProgressBar load, submitLoad;
    private Dialog fromDialog, toDialog;
    private ListView listView;
    private String input, symbol1, symbol2;
    ArrayAdapter<String> arrayAdapter1, arrayAdapter2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Rootref = FirebaseDatabase.getInstance().getReference();


        //spinner1 = findViewById(R.id.spinner1);
        //spinner2 = findViewById(R.id.spinner2);
        convertFrom = findViewById(R.id.convert_from_dropdown_menu);
        convertTo = findViewById(R.id.convert_to_dropdown_menu);
        baseCurrency = findViewById(R.id.currency1);
        resultCurrency = findViewById(R.id.showResult);
       // editText = findViewById(R.id.edit_text);
//        currencySymbol1 = findViewById(R.idim.currencySymbol1);
//        currencySymbol2 = findViewById(R.id.currencySymbol2);
        convert = findViewById(R.id.convertButton);
        load = findViewById(R.id.load);
        submitLoad = findViewById(R.id.submit_load);


        //fetchCurrencyInFirebasetoSpinner();

        convertFrom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fromDialog = new Dialog(MainActivity.this);
                fromDialog.setContentView(R.layout.from_spinner);
                fromDialog.getWindow().setLayout(1050,1800);
                fromDialog.show();

                listView = fromDialog.findViewById(R.id.list_view);
                EditText editText = fromDialog.findViewById(R.id.edit_text);

                load.setVisibility(View.VISIBLE);
                Rootref.child("Country").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        final List<String> list = new ArrayList<String>();
                        String symbol;
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                            String name2 = dataSnapshot.child("code").getValue(String.class);
                            String name1 = dataSnapshot.child("name").getValue(String.class);
                            symbol = dataSnapshot.child("symbol_native").getValue(String.class);
                            String finalString = name1 + " | "+symbol+" | " + name2;

                            list.add(finalString);
                        }


                        arrayAdapter1 = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_spinner_item, list);
                        arrayAdapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        listView.setAdapter(arrayAdapter1);


//                        ArrayAdapter<String> arrayAdapter2 = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_spinner_item, list);
//                        arrayAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//                        listView.setAdapter(arrayAdapter2);


                        load.setVisibility(View.GONE);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

            editText.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    arrayAdapter1.getFilter().filter(charSequence);
                }

                @Override
                public void afterTextChanged(Editable editable) {

                }
            });

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    convertFrom.setText(arrayAdapter1.getItem(i));
                    fromDialog.dismiss();

                }
            });

            }
        });


        convertTo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fromDialog = new Dialog(MainActivity.this);
                fromDialog.setContentView(R.layout.from_spinner);
                fromDialog.getWindow().setLayout(650,800);

                fromDialog.show();

                listView = fromDialog.findViewById(R.id.list_view);
                EditText editText = fromDialog.findViewById(R.id.edit_text);

                load.setVisibility(View.VISIBLE);
                Rootref.child("Country").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        final List<String> list = new ArrayList<String>();
                        String symbol;
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                            String name2 = dataSnapshot.child("code").getValue(String.class);
                            String name1 = dataSnapshot.child("name").getValue(String.class);
                            symbol = dataSnapshot.child("symbol_native").getValue(String.class);
                            String finalString = name1 + " | "+symbol+" | " + name2;

                            list.add(finalString);
                        }


                        arrayAdapter2 = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_spinner_item, list);
                        arrayAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        listView.setAdapter(arrayAdapter2);


//                        ArrayAdapter<String> arrayAdapter2 = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_spinner_item, list);
//                        arrayAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//                        listView.setAdapter(arrayAdapter2);


                        load.setVisibility(View.GONE);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

                editText.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                        arrayAdapter2.getFilter().filter(charSequence);
                    }

                    @Override
                    public void afterTextChanged(Editable editable) {

                    }
                });

                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        convertTo.setText(arrayAdapter2.getItem(i));
                        fromDialog.dismiss();

                    }
                });

            }
        });


        convert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submitLoad.setVisibility(View.VISIBLE);
                input = baseCurrency.getText().toString();
                String s1 = convertFrom.getText().toString();
                String s2 = convertTo.getText().toString();
                String real1 = s1.substring(s1.length() - 3);
                String real2 = s2.substring(s2.length() - 3);


                if (TextUtils.isEmpty(input)) {
                    Toast.makeText(MainActivity.this, "Enter some values!", Toast.LENGTH_SHORT).show();
                } else {
                    FetchData(input, real1, real2);
                }
            }
        });

    }

    private void FetchData(String input, String real1, String real2) {
        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "https://free.currconv.com/api/v7/convert?q=" + real1 + "_" + real2 + "&compact=ultra&apiKey=cc2db394e749f4cdc852";

// Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.
//                        resultCurrency.setText("Response is: "+ response.substring(0,500));
                        float i = Float.parseFloat(input);

                        try {
                            JSONObject jsonObject = new JSONObject(response.toString());
                            float string = Float.parseFloat(jsonObject.getString(real1 + "_" + real2));
                            float res = string * i;
                            resultCurrency.setText(String.valueOf(res));
                            submitLoad.setVisibility(View.GONE);
                        } catch (JSONException e) {
                            e.printStackTrace();
                            submitLoad.setVisibility(View.GONE);
                        }


                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainActivity.this, "Error!", Toast.LENGTH_SHORT).show();
            }
        });

// Add the request to the RequestQueue.
        queue.add(stringRequest);
    }

//    private void fetchCurrencyInFirebasetoSpinner() {
//        load.setVisibility(View.VISIBLE);
//        Rootref.child("Country").addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                final List<String> list = new ArrayList<String>();
//                String symbol;
//                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
//                    String name2 = dataSnapshot.child("code").getValue(String.class);
//                    String name1 = dataSnapshot.child("name").getValue(String.class);
//                    symbol = dataSnapshot.child("symbol_native").getValue(String.class);
//                    String finalString = name1 + " | "+symbol+" | " + name2;
//
//                    list.add(finalString);
//                }
//
//
//                ArrayAdapter<String> arrayAdapter1 = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_spinner_item, list);
//                arrayAdapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//                spinner1.setAdapter(arrayAdapter1);
//
//
//                ArrayAdapter<String> arrayAdapter2 = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_spinner_item, list);
//                arrayAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//                spinner2.setAdapter(arrayAdapter2);
//
//
//                load.setVisibility(View.GONE);
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });
//    }


}