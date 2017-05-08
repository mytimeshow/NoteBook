package com.example.administrator.notebook;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import org.litepal.crud.DataSupport;

import java.util.List;

/**
 * Created by Administrator on 2017/5/2 0002.
 */

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder>{
    private List<Note> noteList;
    private OnclickLisenner lisenner;
    private Context context=new AddActivity();

    public MyAdapter(List<Note> noteList,OnclickLisenner lisenner){
        this.noteList=noteList;
        this.lisenner=lisenner;
    }
    public void changeDate(List<Note> noteList){
        this.noteList=noteList;

        notifyDataSetChanged();
    }

    static public class ViewHolder extends RecyclerView.ViewHolder{
        private View note;
        private ImageButton addImg;
        private TextView TITLE;
        private TextView TYPE;
        private TextView title;
        private TextView type;
private TextView content;
        private TextView date;



        public ViewHolder(View itemView) {
            super(itemView);
            note =itemView;
            addImg= (ImageButton) itemView.findViewById(R.id.addImg);
          TITLE= (TextView) itemView.findViewById(R.id.tv_TITLE);
            TYPE= (TextView) itemView.findViewById(R.id.tv_TYPE);
            title= (TextView) itemView.findViewById(R.id.tv_Title);
            type= (TextView) itemView.findViewById(R.id.tv_Type);
content= (TextView) itemView.findViewById(R.id.note_item_content);
            date= (TextView) itemView.findViewById(R.id.tv_time);


        }

    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.note_item,parent,false);
        final ViewHolder holder=new ViewHolder(view);
        holder.note.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                int position=holder.getAdapterPosition();
                int[] a={0};
                int[] b={position};
                if( lisenner.dialog(a,b)==1){

                }



//                Toast.makeText(v.getContext(),aa.getTitle(),Toast.LENGTH_LONG).show();
                return true;
            }
        });
        holder.note.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position=holder.getAdapterPosition();
                Note aa=noteList.get(position);
                Intent intent=new Intent();
                intent.putExtra("bitmap",aa.getBitmap());
                intent.putExtra("title",aa.getTitle());
                intent.putExtra("type",aa.getType());
                intent.putExtra("time",aa.getDate());
                intent.putExtra("content",aa.getContent());
//                Toast.makeText(v.getContext(),aa.getTitle(),Toast.LENGTH_LONG).show();
              lisenner.tiaozhuang(intent);


            }
        });
        return holder;
    }


    @Override
    public void onBindViewHolder(MyAdapter.ViewHolder holder, int position) {
        Note note=noteList.get(position);
        holder.addImg.setImageBitmap(getBitmap(note));
        holder.date.setText(String.valueOf(note.getDate()));
        holder.title.setText(note.getTitle());
        holder.type.setText(note.getType());
        holder.content.setText(note.getContent());




    }

    @Override
    public int getItemCount() {
        return noteList.size();
    }


    public Bitmap getBitmap(Note a){
        Bitmap bitmap= BitmapFactory.decodeFile(a.getBitmap());
        return  bitmap;
    }
    public void deleteitem(int position){

        Note aa=noteList.get(position);
        noteList.remove(position);
        DataSupport.deleteAll(Note.class,"title = ? and content = ? and type = ? and bitmap = ?",
                aa.getTitle(),aa.getContent(),aa.getType(),aa.getBitmap()
        );
        notifyDataSetChanged();
    }

}
