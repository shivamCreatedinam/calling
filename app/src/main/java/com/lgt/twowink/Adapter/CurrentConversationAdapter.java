package com.lgt.twowink.Adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.lgt.twowink.Activities.ChatActivity;
import com.lgt.twowink.Extras.Commn;
import com.lgt.twowink.Extras.SessionManager;
import com.lgt.twowink.Model.ChatModel;
import com.lgt.twowink.Model.CurrentConversationModel;
import com.lgt.twowink.R;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.util.List;

public class CurrentConversationAdapter  extends RecyclerView.Adapter<CurrentConversationAdapter.ViewHolder> {

    private Context context;
    private List<CurrentConversationModel> list;
    private FirebaseDatabase database;
    private SessionManager sessionManager;

    public CurrentConversationAdapter(Context context, List<CurrentConversationModel> list) {
        this.context = context;
        this.list = list;
        database=FirebaseDatabase.getInstance();
        sessionManager=new SessionManager();
    }

    @NonNull
    @Override
    public CurrentConversationAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater;
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = layoutInflater.inflate(R.layout.current_conversation_layout, parent, false);
        return new CurrentConversationAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull CurrentConversationAdapter.ViewHolder holder, int position) {

        final CurrentConversationModel model=list.get(position);
        try {

            if (model.getFrom().equalsIgnoreCase(sessionManager.getUser(context).getUser_id())){
                holder.itemView.setVisibility(View.GONE);
            }else {
                holder.itemView.setVisibility(View.VISIBLE);
            }

            Log.e("dhasd",model.getMessage()+"");
            Glide.with(context).load(model.getUser_image()).diskCacheStrategy(DiskCacheStrategy.ALL).into(holder.iv_user_image);

            holder.tv_user_name.setText(model.getUser_name());
            holder.tv_last_msg.setText(model.getMessage());
            holder.tv_last_msg_time.setText(model.getTime());

                holder.iv_online_offline.setImageDrawable(context.getResources().getDrawable(R.drawable.online));

                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(context, ChatActivity.class);
                        intent.putExtra(Commn.USER_ID,model.getFrom());
                        intent.putExtra(Commn.user_name,model.getUser_name());
                        intent.putExtra(Commn.user_image,model.getUser_image());
                        Log.e("from_key",model.getFrom()+",");
                        context.startActivity(intent);
                    }
                });
        }catch (ArrayIndexOutOfBoundsException e){
            e.printStackTrace();
        }
    }


    @Override
    public int getItemCount() {

        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView iv_user_image,iv_online_offline;
        private TextView tv_user_name,tv_last_msg,tv_last_msg_time;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            iv_user_image=itemView.findViewById(R.id.iv_user_image);
            tv_user_name=itemView.findViewById(R.id.tv_user_name);
            tv_last_msg=itemView.findViewById(R.id.tv_last_msg);
            iv_online_offline=itemView.findViewById(R.id.iv_online_offline);
            tv_last_msg_time=itemView.findViewById(R.id.tv_last_msg_time);
        }
    }
}
