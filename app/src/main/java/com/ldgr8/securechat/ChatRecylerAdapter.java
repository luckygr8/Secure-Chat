package com.ldgr8.securechat;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Random;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ChatRecylerAdapter extends RecyclerView.Adapter<ChatRecylerAdapter.ViewHolder> {

    private Context context;
    private LinkedList<Object> message_list;
    private HashMap<String,Integer> color_list;
    private int message_count;
    private RecyclerView thisrecycler;

    public ChatRecylerAdapter(Context context,RecyclerView recyclerView)
    {
        this.context=context;
        message_list = new LinkedList<>();
        color_list = new HashMap<>();
        message_list.clear();
        message_count=0;
        thisrecycler=recyclerView;

    }

    public Integer getRandomColor()
    {
        Random rand = new Random();
        int r = rand.nextInt(150);
        int g = rand.nextInt(150);
        int b = rand.nextInt(150);

        return Color.rgb(r,g,b);
    }

    public void AddIntoMessageList(Object new_message)
    {
        message_list.addLast(new_message);
        notifyItemChanged(message_list.size()-1);
        message_count++;

        thisrecycler.getLayoutManager().scrollToPosition(message_list.size()-1);

    }

    public void AddnewUser(String username)
    {
        color_list.put(username,getRandomColor());
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater li = LayoutInflater.from(parent.getContext());
        View v = li.inflate(R.layout.chatitem, parent, false);
        return new ViewHolder(v);

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Object current_message = message_list.get(position);
        if(current_message instanceof Sender)
        {
            Sender sender = (Sender) current_message;
            holder.sender_name.setTextColor(color_list.get(sender.getSender_name()));
            holder.sender_name.setText(sender.getSender_name()+" ");
            holder.sender_message.setText(sender.getSender_message()+" ");
            holder.sender_time.setText(sender.getSender_time()+" ");

            setYouInvisible(holder);
            setSenderVisible(holder);

        }
        else
        {
            You you = (You) current_message;

            holder.your_message.setText(you.getYour_message()+" ");
            holder.your_time.setText(you.getYour_time()+" ");

            setSenderInvisible(holder);
            setYouVisible(holder);

        }


    }

    public void setSenderVisible(ViewHolder holder)
    {
        holder.sender_name.setVisibility(View.VISIBLE);
        holder.sender_message.setVisibility(View.VISIBLE);
        holder.sender_time.setVisibility(View.VISIBLE);
        holder.sender_layout.setVisibility(View.VISIBLE);
    }

    public void setSenderInvisible(ViewHolder holder)
    {
        holder.sender_name.setVisibility(View.INVISIBLE);
        holder.sender_message.setVisibility(View.INVISIBLE);
        holder.sender_time.setVisibility(View.INVISIBLE);
        holder.sender_layout.setVisibility(View.INVISIBLE);
    }

    public void setYouVisible(ViewHolder holder)
    {
        holder.your_time.setVisibility(View.VISIBLE);
        holder.your_message.setVisibility(View.VISIBLE);
        holder.your_layout.setVisibility(View.VISIBLE);
    }

    public void setYouInvisible(ViewHolder holder)
    {
        holder.your_time.setVisibility(View.INVISIBLE);
        holder.your_message.setVisibility(View.INVISIBLE);
        holder.your_layout.setVisibility(View.INVISIBLE);
    }

    @Override
    public int getItemCount() {
        return message_list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        //sender stuff
        TextView sender_name;
        TextView sender_message;
        TextView sender_time;

        LinearLayout sender_layout;
        //

        //your stuff
        TextView your_message;
        TextView your_time;

        LinearLayout your_layout;
        //

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            sender_name = itemView.findViewById(R.id.sender_name);
            sender_message = itemView.findViewById(R.id.sender_message);
            sender_time= itemView.findViewById(R.id.sender_time);
            your_message = itemView.findViewById(R.id.your_message);
            your_time = itemView.findViewById(R.id.your_time);
            sender_layout=itemView.findViewById(R.id.sender_layout);
            your_layout=itemView.findViewById(R.id.your_layout);
        }
    }
}
