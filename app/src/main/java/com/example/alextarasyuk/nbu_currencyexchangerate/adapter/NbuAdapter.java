package com.example.alextarasyuk.nbu_currencyexchangerate.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.alextarasyuk.nbu_currencyexchangerate.R;
import com.example.alextarasyuk.nbu_currencyexchangerate.model.CurrencyModel;

import java.util.List;
import java.util.Locale;

public class NbuAdapter extends RecyclerView.Adapter<NbuAdapter.ViewHolder> {
    private List<CurrencyModel> mCurrencyModels;
    private Context mContext;


    public NbuAdapter(List<CurrencyModel> currencyModels, Context context) {
        mCurrencyModels = currencyModels;
        mContext = context;

    }

    @NonNull
    @Override
    public NbuAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View rowView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_mainview, parent, false);
        ViewHolder viewHolder = new ViewHolder(rowView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull NbuAdapter.ViewHolder holder, int position) {

        if (mCurrencyModels != null) {
            final CurrencyModel currencyModel = mCurrencyModels.get(position);
            holder.mTextViewNameOfCurrency.setText(currencyModel.getTxt().toString());
            holder.mTextViewValueOfCurrency.setText(String.format(currencyModel.getRate().toString(), Locale.getDefault()).substring(0, 7));

        }
    }

    @Override
    public int getItemCount() {
        if (mCurrencyModels != null) {
            return mCurrencyModels.size();
        } else {
            return 0;
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView mTextViewNameOfCurrency;
        private TextView mTextViewValueOfCurrency;

        public ViewHolder(View itemView) {
            super(itemView);
            mTextViewNameOfCurrency = (TextView) itemView.findViewById(R.id.currency_name);
            mTextViewValueOfCurrency = (TextView) itemView.findViewById(R.id.currency_value);
        }
    }
}
