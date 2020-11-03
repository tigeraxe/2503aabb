package com.hanyuzhou.accountingapp;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.LinkedList;


@SuppressLint("ValidFragment")
public class MainFragment extends Fragment implements AdapterView.OnItemLongClickListener{

    private View rootView;
    private TextView textView;
    private ListView listView;
    private ListViewAdapter listViewAdapter;

    private int choice;
    private String account;

    private LinkedList<RecordBean> records = new LinkedList<>();

    private String date;

    @SuppressLint("ValidFragment")
    public MainFragment(String date, int choice, String account){
        this.date = date;
        this.choice = choice;
        this.account = account;
        if(choice == 1 && account == null){
            records = GlobalUtil.getInstance().databaseHelper.read_allRecords();
        }
        else if(choice == 1 && account != null){
            records = GlobalUtil.getInstance().databaseHelper.select_by_account(account);
        }
        else if(choice == 2){
            records = GlobalUtil.getInstance().databaseHelper.select_by_year(date, account);
        }
        else if(choice == 3){
            records = GlobalUtil.getInstance().databaseHelper.select_by_month(date, account);
        }
        else {
            records = GlobalUtil.getInstance().databaseHelper.readRecords(date,account);
        }

    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_main,container,false);
        initView();
        return rootView;
    }
    public void reload(){
        if(choice == 1 && account == null){
            records = GlobalUtil.getInstance().databaseHelper.read_allRecords();
        }
        else if(choice == 1&& account != null){
            records = GlobalUtil.getInstance().databaseHelper.select_by_account(account);
        }
        else if(choice == 2){
            records = GlobalUtil.getInstance().databaseHelper.select_by_year(date,account);
        }
        else if(choice == 3){
            records = GlobalUtil.getInstance().databaseHelper.select_by_month(date,account);
        }
        else {
            records = GlobalUtil.getInstance().databaseHelper.readRecords(date,account);
        }
        if (listViewAdapter==null){
            listViewAdapter = new ListViewAdapter(getActivity().getApplicationContext());
        }

        listViewAdapter.setData(records);
        listView.setAdapter(listViewAdapter);

        if (listViewAdapter.getCount()>0){
            rootView.findViewById(R.id.no_record_layout).setVisibility(View.INVISIBLE);
        }
    }

    private void initView(){
        textView = (TextView) rootView.findViewById(R.id.day_text);
        listView = (ListView) rootView.findViewById(R.id.listView);
        textView.setText(date);
        listViewAdapter = new ListViewAdapter(getContext());
        listViewAdapter.setData(records);
        listView.setAdapter(listViewAdapter);

        if (listViewAdapter.getCount()>0){
            rootView.findViewById(R.id.no_record_layout).setVisibility(View.INVISIBLE);
        }

        listView.setOnItemLongClickListener(this);
    }

    public ArrayList<String> getcategory(){
        ArrayList<String> str = new ArrayList<>();
        for(RecordBean record :records){
            str.add(record.getCategory());
        }
        return str;
    }
    public ArrayList<String> getcategory2(){
        ArrayList<String> str = new ArrayList<>();
        for(RecordBean record :records){
            str.add(record.getCategory2());
        }
        return str;
    }
    public ArrayList<String> getmember(){
        ArrayList<String> str = new ArrayList<>();
        for(RecordBean record :records){
                str.add(record.getMember());
        }
        return str;
    }
    public ArrayList<String> getaccount(){
        ArrayList<String> str = new ArrayList<>();
        for(RecordBean record :records){
            str.add(record.getAccount());
        }
        return str;
    }
    public BigDecimal getTotalCost(){
        BigDecimal totalCost = new BigDecimal(0);
        for (RecordBean record: records){
            if (record.getType()==1){
                totalCost = totalCost.add(record.getAmount());
            }
        }
        return totalCost;
    }
    public BigDecimal getAccountCost(String account){
        BigDecimal totalCost = new BigDecimal(0);
        for (RecordBean record: records){
            if (record.getType()==1 && record.getAccount().equals(account)){
                totalCost = totalCost.add(record.getAmount());
            }
        }
        return totalCost;
    }
    public BigDecimal getAccountEarn(String account){
        BigDecimal totalEarn = new BigDecimal(0);
        for (RecordBean record: records){
            if (record.getType()==2 && record.getAccount().equals(account)){
                totalEarn = totalEarn.add(record.getAmount());
            }
        }
        return totalEarn;
    }
    public BigDecimal getTotalEarn(){
        BigDecimal totalEarn = new BigDecimal(0);
        for (RecordBean record: records){
            if (record.getType()==2){
                totalEarn = totalEarn.add(record.getAmount());
            }
        }
        return totalEarn;
    }
    public float getPie(String category){
        BigDecimal amount = new BigDecimal(0);
        for(RecordBean record: records){
            if((record.getCategory().equals(category) || record.getCategory2().equals(category) || record.getAccount().equals(category) || record.getMember().equals(category))&& record.getType() == 1){
                amount = amount.add(record.getAmount());
            }
        }
        return Float.valueOf(String.valueOf(amount));
    }
    public float getPie_earn(String category){
        BigDecimal amount = new BigDecimal(0);
        for(RecordBean record: records){
            if((record.getCategory().equals(category) || record.getCategory2().equals(category) || record.getAccount().equals(category) || record.getMember().equals(category)) && record.getType() == 2){
                amount = amount.add(record.getAmount());
            }
        }
        return Float.valueOf(String.valueOf(amount));
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        showDialog(position);
        return false;
    }

    private void showDialog(int index){
        final String[] options={"删除","编辑"};
        final RecordBean selectedRecord = records.get(index);
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.create();
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (which==0){
                    String uuid = selectedRecord.getUuid();
                    GlobalUtil.getInstance().databaseHelper.removeRecord(uuid);
                    reload();
                    GlobalUtil.getInstance().mainActivity.updateHeader();
                }else if (which==1){
                    Intent intent = new Intent(getActivity(),AddRecordActivity.class);
                    Bundle extra = new Bundle();
                    extra.putSerializable("record",selectedRecord);
                    intent.putExtras(extra);
                    startActivityForResult(intent,1);
                }
            }
        });
        builder.setNegativeButton("取消",null);
        builder.create().show();
    }
}
