package com.example.appdentart;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.appdentart.Clases.ChatMensajes;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class ChatActivity extends AppCompatActivity {


    EditText e1;
    TextView t1,t2;
    private String user_name,room_name;
    DatabaseReference reference;

    String temp_key;
    RecyclerView recycler;
    Button btnsubir;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);


        recycler = (RecyclerView) findViewById(R.id.recylermensaje);
        recycler.setLayoutManager(new LinearLayoutManager(this));
        e1= (EditText)findViewById(R.id.editText2);

        String room="222";
        reference = FirebaseDatabase.getInstance().getReference("chatrooms").child(room).child("chats");
    }

    public void send(View v)
    {
        String key;
        Map<String,Object> map = new HashMap<String,Object>();
        key = reference.push().getKey();
        reference.updateChildren(map);

        DatabaseReference child_ref = reference.child(key);
        Map<String,Object> map2 = new HashMap<>();
        map2.put("user","melisa");
        map2.put("message", e1.getText().toString());//sendDate:
        map2.put("sendDate","Wed Oct 07 2020 18:49:33 GMT-0500 (hora estándar de Perú)");
        map2.put("type","newmsg");
        child_ref.updateChildren(map2).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(Exception e) {
                Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
        e1.setText("");

    }

    @Override
    public void onStart() {
        super.onStart();

        FirebaseRecyclerOptions<ChatMensajes> recyclerOptions = new FirebaseRecyclerOptions.Builder<ChatMensajes>()
                .setQuery(reference, ChatMensajes.class)
                .build();

        FirebaseRecyclerAdapter<ChatMensajes,ChatsVH> adapter2 = new FirebaseRecyclerAdapter<ChatMensajes, ChatsVH>(recyclerOptions) {
            @Override
            protected void onBindViewHolder(@NonNull final ChatsVH holder, int position, @NonNull ChatMensajes model) {

                final String userID = getRef(position).getKey();

                reference.child(userID).addValueEventListener(new ValueEventListener() {

                    @Override
                    public void onDataChange(@NonNull final DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()){

                            final String mensaje = dataSnapshot.child("message").getValue().toString();
                            final String name = dataSnapshot.child("user").getValue().toString();
                            holder.user_presence.setText(name);
                            holder.txtmensaje.setText(mensaje);

                            holder.itemView.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                }
                            });

                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }

            @NonNull
            @Override
            public ChatsVH onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
                View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_chat, viewGroup, false);
                return new ChatsVH(view);
            }
        };

        recycler.setAdapter(adapter2);
        adapter2.startListening();

    }
    public static class ChatsVH extends RecyclerView.ViewHolder{
        TextView txtmensaje, user_presence,tipo;

        public ChatsVH(View itemView) {
            super(itemView);
            user_presence= itemView.findViewById(R.id.nname);
            txtmensaje = itemView.findViewById(R.id.txtmensaje);

        }
    }
}