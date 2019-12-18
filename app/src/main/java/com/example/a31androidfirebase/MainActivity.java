package com.example.a31androidfirebase;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity
{
    DatabaseReference mData;
    TextView textView1, textView2;
    ListView listView1;
    ArrayList<String> arrayList;
    ArrayAdapter arrayAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView1 = findViewById(R.id.textView1);
        textView2 = findViewById(R.id.textView2);
        listView1 = findViewById(R.id.listView1);
        arrayList = new ArrayList<>();



        arrayAdapter = new ArrayAdapter(MainActivity.this, android.R.layout.simple_list_item_1, arrayList);
        listView1.setAdapter(arrayAdapter);


        mData = FirebaseDatabase.getInstance().getReference();

        // trường hợp 1
        mData.child("HoTen").setValue("Nguyễn Văn Tỷ");

        // trường hợp 2
        final SinhVien sv = new SinhVien("Nguyễn Văn Tỷ", "TPHCM", 1997);
        mData.child("SinhVien").setValue(sv);

        // trường hợp 3
        Map<String, Integer> map = new HashMap<>();
        map.put("XeMay", 2);
        mData.child("PhuongTien").setValue(map);

        // trường hợp 4
//        SinhVien sv1 = new SinhVien("Nguyễn Văn Long", "Hà Nội", 1998);
//        mData.child("SinhVien1").push().setValue(sv1);

        // bắt sự kiện hoàn thành khi setValue
        mData.child("KhoaPhamTraining").setValue("Lập Trình Android", new DatabaseReference.CompletionListener()
        {
            @Override
            public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference)
            {
                if(databaseError == null)
                {
                    Toast.makeText(MainActivity.this, "Lưu Thành Công", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Toast.makeText(MainActivity.this, "Lưu Thất Bại", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // lấy dữ liệu từ firebase về cách 1 không con
        mData.child("KhoaPhamTraining").addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                textView2.setText(dataSnapshot.getValue().toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError)
            {

            }
        });
        // lấy dữ liệu về cách 2 có con
        mData.child("SinhVien1").addChildEventListener(new ChildEventListener()
        {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s)
            {
              //  textView1.setText(dataSnapshot.getValue().toString()); thay bằng để lấy all dữ liệu
            //    textView1.append(dataSnapshot.getValue().toString() + "\n");



//                SinhVien sv2 = dataSnapshot.getValue(SinhVien.class);
//                Toast.makeText(MainActivity.this, sv2.getHoTen(), Toast.LENGTH_SHORT).show();



                SinhVien sv2 = dataSnapshot.getValue(SinhVien.class);
                arrayList.add(sv2.getHoTen() + " - " + sv2.getDiaChi() + " - " + sv2.getNamSinh());
                arrayAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s)
            {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot)
            {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s)
            {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError)
            {

            }
        });
    }
}
