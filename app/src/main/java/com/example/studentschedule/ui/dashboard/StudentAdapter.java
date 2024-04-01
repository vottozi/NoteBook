package com.example.studentschedule.ui.dashboard;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.PopupMenu;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.navigation.NavController;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.example.studentschedule.R;
import com.example.studentschedule.databinding.ItemCardBinding;
import com.example.studentschedule.models.Student;
import com.example.studentschedule.room.AppDatabase;
import com.example.studentschedule.room.StudentDao;

import java.util.ArrayList;
import java.util.List;

public class StudentAdapter extends RecyclerView.Adapter<StudentAdapter.ViewHolder> {

    Context mBase;
    ItemCardBinding binding;
    List<Student> list = new ArrayList<>();
    StudentDao studentDao;
    Context context;
    NavController navController;
    Student newStudent;
    EditText my_message;

    public void setList(List<Student> list) { this.list = list; }

    @NonNull
    @Override
    public StudentAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        ItemCardBinding itemCardBinding = ItemCardBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);

        ViewHolder viewHolder = new ViewHolder(itemCardBinding);

        return viewHolder;
        // return new ViewHolder(binding.getRoot());
    }

    @Override
    public void onBindViewHolder(@NonNull StudentAdapter.ViewHolder holder, int position) {

        studentDao = Room.databaseBuilder(holder.binding.getRoot().getContext(), AppDatabase.class, "database")
                .allowMainThreadQueries().build().studentDao();
        Student student = list.get(position);

        holder.binding.nameSurnameCard.setText(student.getName_surname());
        holder.binding.number.setText(student.getTel_number());

        holder.binding.imageCard.setImageBitmap(BitmapFactory.decodeByteArray(student.getImage(), 0, student.getImage().length));

        newStudent = student;

        holder.binding.dropdownMenu.setOnClickListener(v1->{
            PopupMenu popup = new PopupMenu(holder.binding.getRoot().getContext(),
                    holder.binding.dropdownMenu);
            popup.getMenuInflater().inflate(R.menu.card_menu, popup.getMenu());

            popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {

                @SuppressLint("IntentReset")
                public boolean onMenuItemClick(MenuItem item) {

                    switch (item.getTitle().toString()){
                        case "call":
                            if (ContextCompat.checkSelfPermission(
                                    holder.binding.getRoot().getContext(), android.Manifest.permission.CALL_PHONE)
                                    != PackageManager.PERMISSION_GRANTED) {

                                ActivityCompat.requestPermissions((Activity) holder.binding.getRoot().getContext(),
                                        new String[]{android.Manifest.permission.CALL_PHONE}, 0);
                            } else {
                                String num_student = holder.binding.number.getText().toString().trim();

                                if (!TextUtils.isEmpty(num_student)) {
                                    Uri call = Uri.parse("tel:" + num_student);
                                    Intent intent = new Intent(Intent.ACTION_DIAL, call);
                                    holder.binding.getRoot().getContext().startActivity(intent);
                                } else {
                                    Toast.makeText(holder.binding.getRoot().getContext(), "Phone number is empty", Toast.LENGTH_SHORT).show();
                                }
                            }
                            break;

                        case "message":
                            holder.binding.myMessage.setVisibility(View.VISIBLE);

                            Intent emailIntent = new Intent(Intent.ACTION_SEND);
                            emailIntent.setData(Uri.parse("mailto:"));
                            emailIntent.setType("text/plain");
                            emailIntent.putExtra(Intent.EXTRA_SUBJECT, "My message");
                            emailIntent.putExtra(Intent.EXTRA_TEXT, holder.binding.myMessage.getText().toString());

                            try{
                                holder.itemView.getContext().startActivity(Intent.createChooser(emailIntent,"Send mail..."));
                            }catch (android.content.ActivityNotFoundException e){
                                Toast.makeText(holder.itemView.getContext(), "There is no email client installed.", Toast.LENGTH_SHORT).show();
                            }

                            break;
                        case "delete":
                            AlertDialog alertDialog = new AlertDialog
                                    .Builder(holder.binding.getRoot().getContext()).create();
                            alertDialog.setTitle("Are you sure?");

                            alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Yes, i want",
                                    new DialogInterface.OnClickListener() {
                                        @SuppressLint("NotifyDataSetChanged")
                                        public void onClick(DialogInterface dialog, int which) {
                                            studentDao.delete(student);
                                            list.remove(holder.getAdapterPosition());

                                            notifyDataSetChanged();
                                            dialog.dismiss();
                                        }
                                    });
                            alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "No, i do not", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                            alertDialog.show();
                            break;
                        default:
                            Toast.makeText(context, "ok", Toast.LENGTH_SHORT).show();
                    }
                    return true;
                }
            });
            popup.show();
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ItemCardBinding binding;
        public ViewHolder(@NonNull ItemCardBinding itemView) {
            super(itemView.getRoot());
            binding = itemView;
        }

    }
}
