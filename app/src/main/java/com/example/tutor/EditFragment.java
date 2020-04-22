package com.example.tutor;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class EditFragment extends Fragment implements View.OnClickListener {
    String[] reqwest = {"Книги","Подработка","Репетиторы","Афишы","Города","Крестики Нолики","Другое"};
    String item;
    EditText txt;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.edit_frag, container, false);
        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
        txt=rootView.findViewById(R.id.txt);
        txt.setAlpha(0);


        Spinner spinner = (Spinner) rootView.findViewById(R.id.spinner);


        // Создаем адаптер ArrayAdapter с помощью массива строк и стандартной разметки элемета spinner
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, reqwest);
        // Определяем разметку для использования при выборе элемента
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Применяем адаптер к элементу spinner
        spinner.setAdapter(adapter);


        AdapterView.OnItemSelectedListener itemSelectedListener = new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                // Получаем выбранный объект
                item = (String)parent.getItemAtPosition(position);
                Toast.makeText(getActivity(), item, Toast.LENGTH_SHORT).show();
                if(item.equals("Другое")){
                    txt.setAlpha(1);
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        };
        spinner.setOnItemSelectedListener(itemSelectedListener);


        return rootView;

    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fabOk:
                if(item.equals("Другое")){

                }else{

                }

                break;

        }

    }


}
