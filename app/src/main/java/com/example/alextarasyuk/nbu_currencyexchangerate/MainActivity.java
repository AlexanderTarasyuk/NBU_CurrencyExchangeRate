package com.example.alextarasyuk.nbu_currencyexchangerate;

import android.app.DatePickerDialog;
import android.os.AsyncTask;
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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import okhttp3.HttpUrl;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {


    private ArrayList<CurrencyModel> mCurrencyModels = new ArrayList<>();
    private EditText mEditText;
    private RecyclerView mRecyclerView;
    private NbuAdapter mNbuAdapter;
    private Calendar dateInstance;
    private SimpleDateFormat mSimpleDateFormat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRecyclerView = (RecyclerView) findViewById(R.id.main_listview);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        mEditText = (EditText) findViewById(R.id.date_edit);
        mEditText.setInputType(InputType.TYPE_NULL);
        mEditText.requestFocus();

        dateInstance = Calendar.getInstance();
        mSimpleDateFormat = new SimpleDateFormat("yyyy.MM.dd");
        Date currentDate = Calendar.getInstance().getTime();
        String currentDateText = mSimpleDateFormat.format(currentDate);
        mEditText.setText(currentDateText);

        new AsyncWork().execute(currentDateText);


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
                new AsyncWork().execute(dateField);
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

            new AsyncWork().execute(chooseDateText);

        }
    };

    private void updateCurrencyList(String inputUrl) {

        HttpUrl url = HttpUrl.parse(inputUrl);

        NbuApiService nbuApiService = NbuService.getApi();
        nbuApiService.getCurrency(url.toString()).enqueue(new Callback<List<CurrencyModel>>() {

            @Override
            public void onResponse(Call<List<CurrencyModel>> call, Response<List<CurrencyModel>> response) {

                mCurrencyModels.clear();
                mCurrencyModels.addAll(response.body());
                NbuAdapter adapter = new NbuAdapter(mCurrencyModels, MainActivity.this);
                mRecyclerView.setAdapter(adapter);
                adapter.notifyDataSetChanged();

            }

            @Override
            public void onFailure(Call<List<CurrencyModel>> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Mistake", Toast.LENGTH_LONG).show();
            }

        });
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


    class AsyncWork extends AsyncTask<String, String, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(String... params) {
            updateCurrencyList(completedBaseUrl(params[0]));
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
        }
    }
}
