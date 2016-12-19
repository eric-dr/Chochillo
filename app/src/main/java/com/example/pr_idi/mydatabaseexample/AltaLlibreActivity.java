package com.example.pr_idi.mydatabaseexample;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;


public class AltaLlibreActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private final static String[] catgs = {"Acció i aventures", "Assaig", "Biogràfica", "Ciència ficció", "Còmic i novel·la gràfica", "Fantasia",
            "Ficció literària", "Humor", "Infantil", "Literatura contemporània", "Misteri", "Narrativa", "Novel·la", "Novel·la negra", "Poesia i teatre", "Romàntica i eròtica", "Terror","Altra"};
    private final static String[] valrs = {"Molt bo", "Bo", "Regular", "Dolent", "Molt dolent"};
    private BookData bookData;
    Book book;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alta_llibre);

        //Toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(null);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        //Elements
        final EditText atitol = (EditText) findViewById(R.id.atitol);
        final EditText aautor = (EditText) findViewById(R.id.aautor);
        final EditText aany = (EditText) findViewById(R.id.aany);
        final EditText aeditorial = (EditText) findViewById(R.id.aeditorial);
        final Spinner acategoria = (Spinner) findViewById(R.id.acategoria);
        final Spinner avaloracio = (Spinner) findViewById(R.id.avaloracio);
        final Button aafegir = (Button) findViewById(R.id.aafegir);

        ArrayAdapter categories = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_dropdown_item, catgs);
        acategoria.setAdapter(categories);
        acategoria.setSelection(0);
        ArrayAdapter valoracions = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_dropdown_item, valrs);
        avaloracio.setAdapter(valoracions);
        avaloracio.setSelection(0);

        bookData = new BookData(this);
        bookData.open();
        aafegir.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                book = new Book();
                book.setTitle(atitol.getText().toString());
                book.setAuthor(aautor.getText().toString());
                book.setYear(aany.getText().toString());
                book.setPublisher(aeditorial.getText().toString());
                book.setCategory(acategoria.getSelectedItem().toString());
                book.setPersonal_evaluation(avaloracio.getSelectedItem().toString());
                // After I get the book data, I add it to the adapter
                Book nou_llibre = bookData.createBook(book.getTitle(), book.getAuthor(), Integer.parseInt(book.getYear()), book.getPublisher(), book.getCategory(), book.getPersonal_evaluation());
                MainActivity.values.add(nou_llibre);
                ordena(MainActivity.values);
                MainActivity.myListAdapter.notifyDataSetChanged();
                finish();
            }
        });
    }

    //Menu i opcions toolbar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_add, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    private void ordena(List<Book> values) {
        Collections.sort(values, new Comparator<Book>() {
            @Override
            public int compare(Book lhs, Book rhs) {
                return lhs.getCategory().compareTo(rhs.getCategory());
            }
        });
    }
}
