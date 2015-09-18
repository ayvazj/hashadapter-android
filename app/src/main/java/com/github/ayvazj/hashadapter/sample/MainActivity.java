package com.github.ayvazj.hashadapter.sample;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Spinner;
import android.widget.TextView;

import com.github.ayvazj.hashadapter.LinkedHashMapAdapter;

import java.util.LinkedHashMap;
import java.util.Map;


public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private LinkedHashMapAdapter<String, String> adapter;
    private LinkedHashMap<String, String> mapData;
    private Spinner spinner;

    TextView selectedKey;
    TextView selectedValue;
    private AutoCompleteTextView autocomplete;
    private LinkedHashMapAdapter<String, String> acadapter;
    private int mAcFlags;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        mapData = new LinkedHashMap<String, String>();

        mapData.put("shamu", "Nexus 6");
        mapData.put("fugu", "Nexus Player");
        mapData.put("volantisg", "Nexus 9 (LTE)");
        mapData.put("volantis", "Nexus 9 (Wi-Fi)");
        mapData.put("hammerhead", "Nexus 5 (GSM/LTE)");
        mapData.put("razor", "Nexus 7 [2013] (Wi-Fi)");
        mapData.put("razorg", "Nexus 7 [2013] (Mobile)");
        mapData.put("mantaray", "Nexus 10");
        mapData.put("occam", "Nexus 4");
        mapData.put("nakasi", "Nexus 7 (Wi-Fi)");
        mapData.put("nakasig", "Nexus 7 (Mobile)");
        mapData.put("tungsten", "Nexus Q");

        adapter = new LinkedHashMapAdapter<String, String>(this, android.R.layout.simple_spinner_item, mapData);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner = (Spinner) findViewById(R.id.spinner);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);

        selectedKey = (TextView) findViewById(R.id.key);
        selectedValue = (TextView) findViewById(R.id.value);

        mAcFlags = LinkedHashMapAdapter.FLAG_FILTER_ON_KEY;
        acadapter = new LinkedHashMapAdapter<String, String>(this, android.R.layout.simple_list_item_1, mapData);
        autocomplete = (AutoCompleteTextView) findViewById(R.id.autocomplete);
        autocomplete.setAdapter(acadapter);

        ((CheckBox) findViewById(R.id.checkbox_keyFilter)).setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    mAcFlags = mAcFlags | LinkedHashMapAdapter.FLAG_FILTER_ON_KEY;
                } else {
                    mAcFlags = mAcFlags & ~LinkedHashMapAdapter.FLAG_FILTER_ON_KEY;
                }
                acadapter.setFlags(mAcFlags);
            }
        });
        ((CheckBox) findViewById(R.id.checkbox_keyFilter)).setChecked(true);

        ((CheckBox) findViewById(R.id.checkbox_valueFilter)).setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    mAcFlags = mAcFlags | LinkedHashMapAdapter.FLAG_FILTER_ON_VALUE;
                } else {
                    mAcFlags = mAcFlags & ~LinkedHashMapAdapter.FLAG_FILTER_ON_VALUE;
                }
                acadapter.setFlags(mAcFlags);
            }
        });

        ((CheckBox) findViewById(R.id.checkbox_valueResult)).setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    mAcFlags = mAcFlags | LinkedHashMapAdapter.FLAG_FILTER_RESULT_USE_VALUE;
                } else {
                    mAcFlags = mAcFlags & ~LinkedHashMapAdapter.FLAG_FILTER_RESULT_USE_VALUE;
                }
                acadapter.setFlags(mAcFlags);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        spinner.setSelection(position);
        Map.Entry<String, String> item = (Map.Entry<String, String>) spinner.getSelectedItem();
        selectedKey.setText(item.getKey());
        selectedValue.setText(item.getValue());
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
