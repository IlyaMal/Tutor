package com.example.tutor;

import android.content.ContentValues;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.preference.PreferenceManager;


import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class MesageFragment extends Fragment implements View.OnClickListener {
    public Elements title;
    String[] resp = {"Книги","Подработка","Репетиторы","Афишы","Города","Крестики Нолики"};
    // то в чем будем хранить данные пока не передадим адаптеру
    public ArrayList<String> messages = new ArrayList<String>();
    // Listview Adapter для вывода данных
    private ArrayAdapter<String> adapter;
    // List view
    private ListView lv;
    NewThread at;
    Button btnSend;
    EditText etEmail;
    DBHelper dbHelper;
    String email,task,url,titelstr;
    List<String> answ = new ArrayList<>();
    SharedPreferences sp;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.mesage_frag, container, false);
        lv = (ListView) rootView.findViewById(R.id.list_of_messages);
        // запрос к нашему отдельному поток на выборку данных
        // Добавляем данные для ListView
        adapter = new ArrayAdapter<String>(getActivity(), R.layout.list_item, R.id.product_name, messages);
        btnSend = (Button) rootView.findViewById(R.id.btnSend);
        btnSend.setOnClickListener( this);
        etEmail = (EditText) rootView.findViewById(R.id.etEmail);
        sp = PreferenceManager.getDefaultSharedPreferences(getActivity());
        messages.clear();
        dbHelper = new DBHelper(getActivity());
        SQLiteDatabase database = dbHelper.getWritableDatabase();
        Cursor cursor = database.query(DBHelper.TABLE_MES, null, null, null, null, null, null);
        int emailIndex = cursor.getColumnIndex(DBHelper.KEY_MAIL);

        if (cursor.moveToFirst()) {
            do {

                messages.add(cursor.getString(emailIndex));
            } while (cursor.moveToNext());
        }
        lv.setAdapter(adapter);
        return rootView;

    }

    @Override
    public void onClick(View v) {
        email = etEmail.getText().toString();
        if (email.equals("")) {
        }else {
            SQLiteDatabase database = dbHelper.getWritableDatabase();
            ContentValues contentValues = new ContentValues();
            messages.add(email);
            int i = messages.size();
            lv.smoothScrollToPosition(i);
            contentValues.put(DBHelper.KEY_MAIL, email);
            database.insert(DBHelper.TABLE_MES, null, contentValues);
            at = new NewThread();
            at.execute();
            for (int j = 0; j <= 8; j++) {
                DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
                DatabaseReference answerRef = rootRef.child("answers").child(resp[j]);
                ValueEventListener eventListener = new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot ds : dataSnapshot.getChildren()) {
                            String friend = ds.getKey();
                            answ.add(friend);
                        }
                    }

                    @Override
                public void onCancelled (DatabaseError databaseError){
                }
            } ;
            answerRef.addListenerForSingleValueEvent(eventListener);
                if (answ.contains(email)) {
                    task=resp[j];
                    break;
                }
            }

        }
        etEmail.setText(null);
        adapter.notifyDataSetInvalidated();
    }
    public class NewThread extends AsyncTask<String, Void, String> {

        // Метод выполняющий запрос в фоне, в версиях выше 4 андроида, запросы в главном потоке выполнять
        // нельзя, поэтому все что вам нужно выполнять - выносите в отдельный тред
        @Override
        protected String doInBackground(String... arg) {

            // класс который захватывает страницу
            Document doc;

            switch(task){
                case "Афиши":
                    String url = sp.getString("city", "");
                    try {
                        // определяем откуда будем воровать данные
                        doc = Jsoup.connect(url).get();
                        // задаем с какого места, я выбрал заголовке статей
                        title = doc.select(".title");

                        // чистим наш аррей лист для того что бы заполнить
                        // и в цикле захватываем все данные какие есть на странице
                        for (Element titles : title) {
                            // записываем в аррей лист
                            titelstr=titelstr+titles;
                        }
                        messages.add(titelstr);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    break;
                case "Книги":
                    url = sp.getString("book", "");
                    try {
                        doc = Jsoup.connect(url).get();
                        Elements data = doc.select("div.product-card.js_product.js__product_card");
                        int size = data.size();
                        for (int i = 0; i < size; i++) {
                            String title = data.select("div.product-card__title.js-analytic-product-title")
                                    .select("a")
                                    .eq(i)
                                    .text();
                            String auth = data.select("div.product-card__author")
                                    .select("a")
                                    .eq(i)
                                    .text();
                            titelstr=titelstr+title+"\n"+auth+"\n"+"\n\n";
                        }
                        messages.add(titelstr);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    break;
                case "Подработка":
                    url = "https://ru.jooble.org/%D1%80%D0%B0%D0%B1%D0%BE%D1%82%D0%B0-%D0%B4%D0%BB%D1%8F-%D1%88%D0%BA%D0%BE%D0%BB%D1%8C%D0%BD%D0%B8%D0%BA%D0%BE%D0%B2/%D0%A3%D1%84%D0%B0";
                    try {
                        doc = Jsoup.connect(url).get();
                        Elements data = doc.select("div.top-wr");
                        int size = data.size();
                        for (int i = 0; i < size; i++) {
                            String title = data.select("h2.position")
                                    .select("span")
                                    .eq(i)
                                    .text();
                            String auth = data.select("span.salary")
                                    .eq(i)
                                    .text();
                            titelstr=titelstr+title+"\n"+auth+"\n"+"\n\n";
                        }
                        messages.add(titelstr);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    break;
                case "Репетиторы":
                    url = sp.getString("lessons", "");
                    try {
                        doc = Jsoup.connect(url).get();
                        Elements data = doc.select("div.teacher-card");
                        int size = data.size();
                        for (int i = 0; i < size; i++) {
                            String name = data.select("div.teacher-card")
                                    .select("a")
                                    .eq(i)
                                    .text();
                            String price = data.select("div.price-and-button.online-promo-price-and-button")
                                    .select("h3.price online-promo-teacher-card-new-price")
                                    .eq(i)
                                    .text();
                            titelstr=titelstr+name+"\n"+price+"\n\n";
                        }
                        messages.add(titelstr);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    break;
                case "Города":

                    break;
                case "Крестики Нолики":

                    break;

            }
            // ничего не возвращаем потому что я так захотел)
            return null;
        }

        @Override
        protected void onPostExecute(String result) {

            adapter.notifyDataSetInvalidated();

        }
    }
}
