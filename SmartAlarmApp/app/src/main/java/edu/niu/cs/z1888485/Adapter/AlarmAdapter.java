package edu.niu.cs.z1888485.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import edu.niu.cs.z1888485.Models.Alarm;
import edu.niu.cs.z1888485.R;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SwitchCompat;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

public class AlarmAdapter extends ListAdapter<Alarm, AlarmAdapter.AlarmHolder> {
    private OnItemClickListener listener;
    private Context mContext;
    public AlarmAdapter(Context context)
    {
        super(DIFF_CALLBACK);
        mContext=context;
    }

    private static final DiffUtil.ItemCallback<Alarm> DIFF_CALLBACK = new DiffUtil.ItemCallback<Alarm>() {
        @Override
        public boolean areItemsTheSame(Alarm oldItem, Alarm newItem) {
            return oldItem.getId() == newItem.getId();
        }
        @Override
        public boolean areContentsTheSame(Alarm oldItem, Alarm newItem) {
            return oldItem.getAlarmMessage().equals(newItem.getAlarmMessage()) &&
                    oldItem.getAlarmDate().equals(newItem.getAlarmDate()) &&
                    oldItem.getAlarmTime().equals(newItem.getAlarmTime()) ;
        }
    };
    @NonNull
    @Override
    public AlarmHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.listings_row, parent, false);
        return new AlarmHolder(itemView);
    }
    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull AlarmHolder holder, int position) {
        Alarm currentNote = getItem(position);
        holder.tvMessage.setText(currentNote.getAlarmMessage());
        holder.tvTime.setText(currentNote.getAlarmTime());
        holder.tvDate.setText(currentNote.getAlarmDate());

        if (currentNote.isAlarmActive())
            holder.offONSwitch.setChecked(true);
        else
            holder.offONSwitch.setChecked(false);


        holder.offONSwitch.setOnCheckedChangeListener((buttonView, isChecked) ->
        {
            if (isChecked)
            {
                if (listener != null && position != RecyclerView.NO_POSITION) {
                    listener.onSwitchClick(getItem(position) , position,true);
                }
            }

            else
            {
                if (listener != null && position != RecyclerView.NO_POSITION) {
                    listener.onSwitchClick(getItem(position) , position,false);
                }
            }

        });

    }
    public Alarm getAlarmAt(int position) {
        return getItem(position);
    }
    class AlarmHolder extends RecyclerView.ViewHolder {
        private TextView tvMessage, tvTime ,tvDate;
        private SwitchCompat offONSwitch;

        public AlarmHolder(View itemView) {
            super(itemView);
            tvMessage = itemView.findViewById(R.id.tv_description);
            tvTime = itemView.findViewById(R.id.time);
            tvDate = itemView.findViewById(R.id.date);
            offONSwitch = itemView.findViewById(R.id.btn_switch);

            itemView.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (listener != null && position != RecyclerView.NO_POSITION) {
                    listener.onItemClick(getItem(position) , position);
                }
            });
        }
    }
    public interface OnItemClickListener {
        void onItemClick(Alarm alarm , int pos);
        void onSwitchClick(Alarm alarm , int pos, boolean isChecked);
    }
    public void setOnItemClickListener(OnItemClickListener listener ) {
        this.listener = listener;
    }


}