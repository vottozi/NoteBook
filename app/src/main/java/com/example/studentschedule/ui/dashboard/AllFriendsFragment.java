package com.example.studentschedule.ui.dashboard;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.activity.result.ActivityResultCaller;
import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.example.studentschedule.databinding.FragmentAllFriendsBinding;
import com.example.studentschedule.room.AppDatabase;
import com.example.studentschedule.room.StudentDao;

public class AllFriendsFragment extends Fragment {

    private FragmentAllFriendsBinding binding;
    private AppDatabase appDatabase;
    private StudentDao studentDao;
    private StudentAdapter studentAdapter;
    RecyclerView rv_main_catalog;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {


        binding = FragmentAllFriendsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        rv_main_catalog = binding.rvMain;
        studentAdapter = new StudentAdapter();
        rv_main_catalog.setAdapter(studentAdapter);

        appDatabase = Room.databaseBuilder(binding.getRoot().getContext(),
                        AppDatabase.class, "database")
                .fallbackToDestructiveMigration()
                .allowMainThreadQueries()
                .build();
        studentDao = appDatabase.studentDao();
        studentAdapter.setList(studentDao.getAll());

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}