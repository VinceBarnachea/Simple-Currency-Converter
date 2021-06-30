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
import org.w3c.dom.Text;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

//   // private Spinner spinner1, spinner2;
    private EditText baseCurrency;
    private TextView resultCurrency, convertFrom, convertTo, fromCurrencySym, toCurrencySym;
    private Button convert;
    private DatabaseReference rootref;
    private ProgressBar load, submitLoad;
    private Dialog fromDialog, toDialog;
    private ListView listView;
    private String input, txtFrom, txtTo;
    ArrayAdapter<String> arrayAdapter1, arrayAdapter2;

    private Dialog showDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);






        rootref = FirebaseDatabase.getInstance().getReference();


        convertFrom = findViewById(R.id.convert_from_dropdown_menu);
        convertTo = findViewById(R.id.convert_to_dropdown_menu);
        baseCurrency = findViewById(R.id.currency1);
        resultCurrency = findViewById(R.id.showResult);

        //Mga Bagong lagay
        fromCurrencySym = findViewById(R.id.fromCurrencySym);
        toCurrencySym = findViewById(R.id.toCurrencySym);


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
                rootref.child("Country").addValueEventListener(new ValueEventListener() {
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
                        //Mga Bagong lagay
                        String countryCode = arrayAdapter1.getItem(i);
                        fromCurrencySym.setText(countryCode.substring(countryCode.length()-3));
                        fromDialog.dismiss();
                    }
                });

            }
        });


        convertTo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toDialog = new Dialog(MainActivity.this);
                toDialog.setContentView(R.layout.from_spinner);
                toDialog.getWindow().setLayout(1050,1800);

                toDialog.show();

                listView = toDialog.findViewById(R.id.list_view);
                EditText editText = toDialog.findViewById(R.id.edit_text);

                load.setVisibility(View.VISIBLE);
                rootref.child("Country").addValueEventListener(new ValueEventListener() {
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
                        String countryCode = arrayAdapter2.getItem(i);
                        //Mga Bagong lagay
                        toCurrencySym.setText(countryCode.substring(countryCode.length()-3));
                        toDialog.dismiss();

                    }
                });

            }
        });


        convert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submitLoad.setVisibility(View.VISIBLE);
                input = baseCurrency.getText().toString();
                txtFrom = convertFrom.getText().toString();
                txtTo = convertTo.getText().toString();
                String s1 = convertFrom.getText().toString();
                String s2 = convertTo.getText().toString();



                //Mga Bago
               if(convertFrom.getText().toString().isEmpty() || convertTo.getText().toString().isEmpty()){
                    Toast.makeText(MainActivity.this, "Please Select a Country!", Toast.LENGTH_SHORT).show();
                }
                else if(TextUtils.isEmpty(input)) {
                    Toast.makeText(MainActivity.this, "Please enter the amount!", Toast.LENGTH_SHORT).show();
                }
                else {
                    String real1 = s1.substring(s1.length() - 3);
                    String real2 = s2.substring(s2.length() - 3);
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
                        //Mga
                        //Ginawa kong double para maround off sa two decimal places
                        double i = Double.parseDouble(input);

                        try {
                            JSONObject jsonObject = new JSONObject(response.toString());
                            //Eto mga naging double
                            double string = Float.parseFloat(jsonObject.getString(real1 + "_" + real2));
                            double res = string * i;
                            double roundOff = (double) Math.round(res * 100) / 100;
                            NumberFormat resultFormat = NumberFormat.getNumberInstance();
                            resultFormat.setGroupingUsed(true);
                            resultCurrency.setText(String.valueOf(resultFormat.format(roundOff)));
                            //resultCurrency.setText(String.valueOf(String.format("%.2f",res)));
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

    }



