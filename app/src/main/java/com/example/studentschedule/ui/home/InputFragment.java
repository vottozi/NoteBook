package com.example.studentschedule.ui.home;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.room.Room;

import com.example.studentschedule.MainActivity;
import com.example.studentschedule.R;
import com.example.studentschedule.databinding.FragmentInputBinding;
import com.example.studentschedule.models.Student;
import com.example.studentschedule.room.AppDatabase;
import com.example.studentschedule.room.StudentDao;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class InputFragment extends Fragment {
    private FragmentInputBinding binding;
    private AppDatabase appDatabase;
    private StudentDao studentDao;
    private ActivityResultLauncher<String> content_l;
    private Bitmap bitmap_imageStudent;
    private boolean isImgSelected = false;
    NavController navController;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentInputBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        binding.btnLoad.setOnClickListener(v1 -> {
            InputFragment.this.content_l.launch("image/*");
        });
        content_l = registerForActivityResult(new ActivityResultContracts.GetContent(),
                new ActivityResultCallback<Uri>() {
                    @Override
                    public void onActivityResult(Uri result) {
                        try {
                            bitmap_imageStudent = MediaStore
                                    .Images
                                    .Media
                                    .getBitmap(getContext()
                                            .getContentResolver(), result);

                            binding.imageInput.setImageBitmap(bitmap_imageStudent);

                            isImgSelected = true;
                        } catch (IOException e) {
                            e.printStackTrace();
                            isImgSelected = false;
                        }
                    }
                });
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);
        binding.btnAdd.setOnClickListener(v2 -> {

            String nameSurnameStudent = binding.nameSurnameForm.getText().toString();
            String telStudent = binding.numberForm.getText().toString();

            if (nameSurnameStudent.isEmpty() || telStudent.isEmpty()) {
                Toast.makeText(requireActivity(), "Заполните поле контакты", Toast.LENGTH_LONG).show();

                isImgSelected = false;
            } else {
                if (isImgSelected) {
                    ByteArrayOutputStream baos_imageStudent = new ByteArrayOutputStream();
                    bitmap_imageStudent.compress(Bitmap.CompressFormat.PNG, 100, baos_imageStudent);

                    byte[] imageStudent = baos_imageStudent.toByteArray();

                    Student student = new Student(nameSurnameStudent, telStudent, imageStudent);

//                    this.appDatabase = Room.databaseBuilder(
//                            binding.getRoot().getContext(),
//                            AppDatabase.class, "database").fallbackToDestructiveMigration().allowMainThreadQueries().build();

                    try {

                        this.appDatabase = Room.databaseBuilder(binding.getRoot().getContext(), AppDatabase.class, "database")
                                .fallbackToDestructiveMigration()
                                .allowMainThreadQueries().build();

                    } catch (Exception e) {
                        Toast.makeText(requireActivity(), "photo", Toast.LENGTH_SHORT).show();
                    }

                    studentDao = appDatabase.studentDao();

                    studentDao.insert(student);

                    Intent intent = new Intent(getActivity(), MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);

                    navController = Navigation.findNavController(requireActivity(), R.id.nav_host);
                    navController.navigate(R.id.action_navigation_dashboard_to_navigation_home);
                } else {
                    Toast.makeText(requireActivity(), "Загрузите фото", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}