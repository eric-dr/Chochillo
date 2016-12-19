package com.example.pr_idi.mydatabaseexample;


import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import static android.widget.Toast.LENGTH_SHORT;

public class MainActivity extends AppCompatActivity {
    private BookData bookData;
    private SearchView mSearchView;
    private boolean buscar;
    private EditText buscaTitol;
    static List<Book> values;
    static RecyclerView myRecyclerView;
    static MyListAdapter myListAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        buscar = false;
        buscaTitol = (EditText)findViewById(R.id.busca_text);
        //Toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(null);

        bookData = new BookData(this);
        bookData.open();
        values = bookData.getAllBooks();

        myRecyclerView = (RecyclerView) findViewById(R.id.recyclervw_main);
        myListAdapter = new MyListAdapter(this, values);
        myRecyclerView.setHasFixedSize(true);
        myRecyclerView.setLayoutManager(new LinearLayoutManager(
                this,
                LinearLayoutManager.VERTICAL,
                false));
        myRecyclerView.addItemDecoration(new SimpleDividerItemDecoration(this));
        myRecyclerView.setAdapter(myListAdapter);

        swipeAction();
        ordena(values);
    }

    //Menu i opcions toolbar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.action_settings:
                Toast.makeText(this, "Settings", LENGTH_SHORT).show();
                return true;
            case R.id.action_add:
                Intent myIntent = new Intent(MainActivity.this, AltaLlibreActivity.class);
                MainActivity.this.startActivity(myIntent);
                return true;
            case R.id.action_search:
                buscar = !buscar;
                LinearLayout ll_search = (LinearLayout)findViewById(R.id.busca);
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                if (buscar) {
                    ll_search.setVisibility(View.VISIBLE);
                    ll_search.requestFocus();
                    imm.showSoftInput(buscaTitol, InputMethodManager.SHOW_IMPLICIT);
                    addTextListener();
                }
                else {
                    ll_search.setVisibility(View.GONE);
                    imm.hideSoftInputFromWindow(buscaTitol.getWindowToken(), 0);
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void addTextListener() {

    }

    @Override
    protected void onResume() {
        bookData.open();
        super.onResume();
    }

    @Override
    protected void onPause() {
        bookData.close();
        super.onPause();
    }

    private void swipeAction() {

        ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            Drawable background;
            boolean initiated;
            private void init() {
                background = new ColorDrawable(Color.RED);
                initiated = true;
            }
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public int getSwipeDirs(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder)
            {
                return super.getSwipeDirs(recyclerView, viewHolder);
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int swipeDir) {
                int swipedPosition = viewHolder.getAdapterPosition();
                bookData.deleteBook(values.get(swipedPosition));
                values.remove(viewHolder.getAdapterPosition());
                myListAdapter.notifyItemRemoved(viewHolder.getAdapterPosition());
            }

            @Override
            public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
                View itemView = viewHolder.itemView;
                if (viewHolder.getAdapterPosition() == -1) {
                    return;
                }
                if (!initiated) {
                    init();
                }
                background.setBounds(itemView.getRight() + (int) dX, itemView.getTop(), itemView.getRight(), itemView.getBottom());
                background.draw(c);
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            }

        };
        ItemTouchHelper mItemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
        mItemTouchHelper.attachToRecyclerView(myRecyclerView);
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