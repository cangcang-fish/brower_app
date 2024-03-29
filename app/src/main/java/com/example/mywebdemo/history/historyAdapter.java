package com.example.mywebdemo.history;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mywebdemo.MainActivity;
import com.example.mywebdemo.R;

import java.util.ArrayList;
import java.util.List;

public class historyAdapter extends RecyclerView.Adapter<historyAdapter.ViewHolder> {
    private ArrayList<String> mList;
    private ArrayList<String> mtitle;

    public historyAdapter(ArrayList<String> list,ArrayList<String> title) {
        mList = list;
        mtitle=title;
    }
    @NonNull
    @Override
    public historyAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        historyAdapter.ViewHolder holder = new historyAdapter.ViewHolder(LayoutInflater.from(
//                parent.getContext()).inflate(R.layout.item, parent,
//                false));
        final View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item,parent,false);
        final ViewHolder holder = new ViewHolder(view);
         holder.btnDelete =(Button) view.findViewById(R.id.btn_delete);
         holder.btnDelete.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 int position = holder.getAdapterPosition();
                 mList.remove(position);
                 mtitle.remove(position);
                 Log.d("item3", mtitle.toString());
                 notifyItemRemoved(position);
                 notifyDataSetChanged();
             }
         });
        //编写点击事件
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //用v.getContext();
                int position = holder.getAdapterPosition();
                String currenturl=mList.get(position);
                Intent intent = new Intent(v.getContext(), MainActivity.class);
                Bundle bundle=new Bundle();
                bundle.putString("url",currenturl);
                bundle.putStringArrayList("title",mtitle);
                bundle.putStringArrayList("list",mList);
                intent.putExtras(bundle);
                v.getContext().startActivity(intent);
                historyActivity activity=(historyActivity)v.getContext();
                activity.finish();
            }
        });

        return holder;

    }

    @Override
    public void onBindViewHolder(@NonNull historyAdapter.ViewHolder holder, final int position) {
        holder.mtitleView.setText(mtitle.get(position));
//        holder.mView.setText(mList.get(position));

    }

    @Override
    public int getItemCount() {
        return mtitle.size() ;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView mView;
        public TextView mtitleView;
        public Button btnDelete;

        public ViewHolder(View itemView) {
            super(itemView);
//            mView = itemView.findViewById(R.id.text_url);
            mtitleView= itemView.findViewById(R.id.text_view);
        }
    }
}
