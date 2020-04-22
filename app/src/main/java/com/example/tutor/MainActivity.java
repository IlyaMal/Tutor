package com.example.tutor;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.OvershootInterpolator;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.w3c.dom.Document;

import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    // благодоря этому классу мы будет разбирать данные на куски
    FloatingActionButton fabMain, fabOne, fabTwo, fabThree;
    Float translationY = 100f;
    OvershootInterpolator interpolator = new OvershootInterpolator();
    Boolean isMenuOpen = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // определение данных

        initFabMenu();
        if (savedInstanceState==null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    new MesageFragment()).commit();
        }
    }

    private void initFabMenu() {
        fabMain = findViewById(R.id.fabMain);
        fabOne = findViewById(R.id.fabOne);
        fabTwo = findViewById(R.id.fabTwo);
        fabThree = findViewById(R.id.fabThree);

        fabOne.setAlpha(0f);
        fabTwo.setAlpha(0f);
        fabThree.setAlpha(0f);

        fabOne.setTranslationY(translationY);
        fabTwo.setTranslationY(translationY);
        fabThree.setTranslationY(translationY);
        fabOne.setTranslationX(0f);
        fabTwo.setTranslationX(90f);
        fabThree.setTranslationX(45f);

        fabMain.setOnClickListener(this);
        fabOne.setOnClickListener(this);
        fabTwo.setOnClickListener(this);
        fabThree.setOnClickListener(this);
    }
    private void openMenu() {
        isMenuOpen = !isMenuOpen;

        fabMain.animate().setInterpolator(interpolator).rotation(90f).setDuration(300).start();

        fabOne.animate().translationY(0f).translationX(0f).alpha(1f).setInterpolator(interpolator).setDuration(300).start();
        fabTwo.animate().translationY(0f).translationX(0f).alpha(1f).setInterpolator(interpolator).setDuration(300).start();
        fabThree.animate().translationY(0f).translationX(0f).alpha(1f).setInterpolator(interpolator).setDuration(300).start();
    }
    private void closeMenu() {
        isMenuOpen = !isMenuOpen;

        fabMain.animate().setInterpolator(interpolator).rotation(0f).setDuration(300).start();

        fabOne.animate().translationY(translationY).translationX(0f).alpha(0f).setInterpolator(interpolator).setDuration(300).start();
        fabTwo.animate().translationY(translationY).translationX(90f).alpha(0f).setInterpolator(interpolator).setDuration(300).start();
        fabThree.animate().translationY(translationY).translationX(45f).alpha(0f).setInterpolator(interpolator).setDuration(300).start();

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fabMain:
                if (isMenuOpen) {
                    closeMenu();
                } else {
                    openMenu();
                }
                break;
            case R.id.fabOne:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new EditFragment()).commit();
                closeMenu();
                break;
            case R.id.fabTwo:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new MesageFragment()).commit();
                closeMenu();
                break;
            case R.id.fabThree:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new ProfileFragment()).commit();
                closeMenu();
                break;
        }

    }

}
