package com.kernel.jobify;


import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;


import java.util.ArrayList;

public class ListAdapterPref extends RecyclerView.Adapter<ListAdapterPref.listholder> {

    Context context;
    public static ArrayList<String> list;
    String[] abcd = {"IT","ECS","MECH","CIVIL","GOVT","INTERN"};

    public ListAdapterPref(@NonNull Context context)
    {
        list = new ArrayList<>();
        this.context = context;
    }

    @NonNull
    @Override
    public listholder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View view = inflater.inflate(R.layout.listitem,viewGroup,false);
        view.setMinimumHeight((viewGroup.getHeight()/6));

        return new listholder(view,context);
    }

    @Override
    public void onBindViewHolder(@NonNull listholder listholder, int i) {
        listholder.checkBox.setChecked(false);
        listholder.textView.setText(abcd[i]);
    }

    @Override
    public int getItemCount() {
        return abcd.length;
    }

    public static ArrayList<String> msg()
    {
        return list;
    }

    public class listholder extends RecyclerView.ViewHolder implements View.OnClickListener
    {
        CheckBox checkBox;
        TextView textView;
        Context cttx;

        public listholder(View itemView , Context ctx) {
            super(itemView);
            itemView.setOnClickListener(this);
            checkBox = itemView.findViewById(R.id.check);
            textView = itemView.findViewById(R.id.listcat);

            cttx = ctx;

        }

        @Override
        public void onClick(View v) {
            int pos = getAdapterPosition();

            if (checkBox.isChecked())
            {
                checkBox.setChecked(false);
                if (list.size()!=0)
                {
                    list.remove(abcd[pos]);
                }
            }
            else
            {
                checkBox.setChecked(true);
                list.add(abcd[pos]);
            }
        }
    }
}
