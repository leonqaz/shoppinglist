package org.projects.shoppinglist;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.ShareActionProvider;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;

import com.firebase.client.Firebase;
import com.flurry.android.FlurryAgent;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity  {

    Firebase ref = new Firebase("https://dazzling-torch-6020.firebaseio.com/shoppinglist");
    ListView listView;


    ShoppingBag bagAdapter = new ShoppingBag(this,ShoppingLine.class,android.R.layout.simple_list_item_checked,ref, android.R.id.text1);
    //ArrayList<ShoppingLine> bag = new ArrayList<ShoppingLine>();
    String[] items = {"", "1","2","3","4","5","6","7","8","9"};
    private ShareActionProvider shareActionProvider;



    private void dataSetChanged()
    {
        setSharingIntent();
        bagAdapter.notifyDataSetChanged();

    }

    public void onClickDelete(View v)
    {
        if(!bagAdapter.isEmpty()) {
            int selectIndex = listView.getCheckedItemPosition();

            if (selectIndex != AdapterView.INVALID_POSITION) {
                bagAdapter.deleteItem(selectIndex);
                FlurryAgent.logEvent("Delete");
                dataSetChanged();
                final View parent = findViewById(R.id.layout);
                Snackbar snackbar = Snackbar
                        .make(parent, "Line deleted", Snackbar.LENGTH_LONG)
                        .setAction("undo", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                bagAdapter.undoDeleteItem();
                                FlurryAgent.logEvent("Undo delete");
                                dataSetChanged();
                            }
                        });

                snackbar.show();
            }
        }

    }
    private void setSharingIntent(){
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT, bagAdapter.toString());
        shareActionProvider.setShareIntent(intent);
    }

    private void clearList()
    {
        WarningDialog warn = new WarningDialog(){
            @Override
            protected void positiveClick() {
                bagAdapter.clear();
                dataSetChanged();
                FlurryAgent.logEvent("Bag cleared");
            }

        };
        warn.show(getFragmentManager(), "MyFragment");

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        int selectIndex = -1;
        applyPreferences();

        if(savedInstanceState != null && savedInstanceState.containsKey("bag")) {
            selectIndex = savedInstanceState.getInt("selectIndex");
        }

        listView = (ListView) findViewById(R.id.list);

        listView.setAdapter(bagAdapter);

        listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

        if(selectIndex >= 0)
        listView.setItemChecked(selectIndex,true);

        Button addButton = (Button) findViewById(R.id.addButton);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText txtfield = (EditText) findViewById(R.id.newItemText);
                EditText countfield = (EditText) findViewById(R.id.newItemCount);
                if(txtfield.getText().toString().isEmpty() || countfield.toString().isEmpty())
                    return;
                ShoppingLine line = new ShoppingLine(new Product(txtfield.getText().toString()), Integer.parseInt(countfield.getText().toString()));
                bagAdapter.updateBag(line);
                        txtfield.setText("");
                countfield.setText("");

                Map<String, String> shoppingParams = new HashMap<String, String>();
                shoppingParams.put("productName", line.getProduct().getProductName());
                shoppingParams.put("count", ""+line.getCount());
                FlurryAgent.logEvent("Product added", shoppingParams);
                dataSetChanged();
            }

        });

        Spinner spinner = (Spinner) findViewById(R.id.countSpinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_dropdown_item, items);


        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view,
                                       int position, long id) {
                if (adapterView.getSelectedItem().toString() != "") {
                    EditText countfield = (EditText) findViewById(R.id.newItemCount);
                    countfield.setText(adapterView.getSelectedItem().toString());
                    adapterView.setSelection(0);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO you would normally do something here
                // for instace setting the selected item to "null"
                // or something.
            }
        });

        FlurryAgent.logEvent("Application started");



    }
    @Override
    public void onSaveInstanceState(Bundle b) {
        listView = (ListView) findViewById(R.id.list);

        int selectIndex = listView.getCheckedItemPosition();
        b.putInt("selectIndex", selectIndex);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        MenuItem menuItem = menu.findItem(R.id.action_share);

        shareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(menuItem);
        setSharingIntent();
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        switch (id) {
            case R.id.action_settings: {
                editPreferences();
                return true;
            }
            case R.id.action_clearList:
            {
                clearList();
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    private void editPreferences()
    {
        Intent intent = new Intent(this, PreferencesActivity.class);
        startActivityForResult(intent, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == 1)
        {
            applyPreferences();
            bagAdapter.notifyDataSetChanged();
        }

        super.onActivityResult(requestCode, resultCode, data);
    }
    private void applyPreferences()
    {
        SharedPreferences prefs = getSharedPreferences("my_prefs", MODE_PRIVATE);
        bagAdapter.setAppendMethod(prefs.getBoolean("appendMethod",false)? AppendMethod.update : AppendMethod.add);

    }



}
