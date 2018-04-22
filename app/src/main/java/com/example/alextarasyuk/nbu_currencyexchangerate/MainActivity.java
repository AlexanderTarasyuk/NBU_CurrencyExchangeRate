package com.example.alextarasyuk.nbu_currencyexchangerate;

import android.app.DatePickerDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.example.alextarasyuk.nbu_currencyexchangerate.adapter.NbuAdapter;
import com.example.alextarasyuk.nbu_currencyexchangerate.model.CurrencyModel;
import com.example.alextarasyuk.nbu_currencyexchangerate.rest.NbuApiService;
import com.example.alextarasyuk.nbu_currencyexchangerate.rest.NbuService;
import com.example.alextarasyuk.nbu_currencyexchangerate.utils.Util;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;


public class MainActivity extends AppCompatActivity {


    private List<CurrencyModel> mCurrencyModels;
    private EditText mEditText;
    private RecyclerView mRecyclerView;
    private NbuAdapter mNbuAdapter;
    private Calendar dateInstance;
    private SimpleDateFormat mSimpleDateFormat;

    private CompositeDisposable mCompositeDisposable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initRecyclerView();
        initEditText();
        mCompositeDisposable = new CompositeDisposable();

        if (Util.isConnection(this)) {
            loadJSON(mEditText.getText().toString());
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.refresh_toolbar:
                Toast.makeText(MainActivity.this, "Обновление", Toast.LENGTH_SHORT).show();
                final String dateField = mEditText.getText().toString();
                loadJSON(dateField);
                return true;
        }

        return true;
    }

    private DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

            dateInstance.set(Calendar.YEAR, year);
            dateInstance.set(Calendar.MONTH, monthOfYear);
            dateInstance.set(Calendar.DAY_OF_MONTH, dayOfMonth);

            Date chooseDate = dateInstance.getTime();
            String chooseDateText = mSimpleDateFormat.format(chooseDate);
            mEditText.setText(chooseDateText);
        }
    };

    private void initEditText() {
        mEditText = (EditText) findViewById(R.id.date_edit);
        mEditText.setInputType(InputType.TYPE_NULL);
        mEditText.requestFocus();

        dateInstance = Calendar.getInstance();
        mSimpleDateFormat = new SimpleDateFormat("yyyy.MM.dd");
        Date currentDate = Calendar.getInstance().getTime();
        String currentDateText = mSimpleDateFormat.format(currentDate);
        mEditText.setText(currentDateText);

    }

    private void initRecyclerView() {
        mRecyclerView = (RecyclerView) findViewById(R.id.main_listview);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    private void loadJSON(String date) {

        String completedUri = completedBaseUrl(date);

        NbuApiService nbuApiService = NbuService.getApi();

        mCompositeDisposable.add(nbuApiService.getCurrency(completedUri)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(this::handleResponse, this::handleError));
    }

    private void handleResponse(List<CurrencyModel> androidList) {

        mCurrencyModels = new ArrayList<>(androidList);
        mNbuAdapter = new NbuAdapter(mCurrencyModels, MainActivity.this);
        mRecyclerView.setAdapter(mNbuAdapter);
    }

    private void handleError(Throwable error) {

        Toast.makeText(this, "Error " + error.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
    }


    private static String completedBaseUrl(String inputDate) {
        inputDate = clearDate(inputDate);
        String url = "https://bank.gov.ua/NBUStatService/v1/statdirectory/exchange?date="
                + inputDate + "&json";
        return url;
    }


    private static String clearDate(String tempDate) {
        tempDate = tempDate.replace(".", "");
        return tempDate;
    }

    public void setNewDate(View view) {
        new DatePickerDialog(MainActivity.this, dateSetListener,
                dateInstance.get(Calendar.YEAR),
                dateInstance.get(Calendar.MONTH),
                dateInstance.get(Calendar.DAY_OF_MONTH))
                .show();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mCompositeDisposable.dispose();
    }
}
