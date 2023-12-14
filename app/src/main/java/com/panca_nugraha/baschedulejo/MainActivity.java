package com.panca_nugraha.baschedulejo;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    private FirebaseFirestore firestore;
    private EditText agendaInput, dateInput, timeInput, locationInput;
    private ListView agendaListView;
    private ProgressBar loadingProgressBar;
    private AgendaAdapter agendaAdapter;
    private ArrayList<Agenda> agendaList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        firestore = FirebaseFirestore.getInstance();
        agendaInput = findViewById(R.id.agendaInput);
        dateInput = findViewById(R.id.dateInput);
        timeInput = findViewById(R.id.timeInput);
        locationInput = findViewById(R.id.locationInput);
        agendaListView = findViewById(R.id.agendaListView);
        loadingProgressBar = findViewById(R.id.loadingProgressBar);

        Button addButton = findViewById(R.id.addButton);
        addButton.setOnClickListener(view -> tambahAgenda());

        EditText dateInput = findViewById(R.id.dateInput);
        dateInput.addTextChangedListener(new DateTextWatcher(dateInput));

        EditText timeInput = findViewById(R.id.timeInput);
        timeInput.addTextChangedListener(new TimeTextWatcher(timeInput));

        // Inisialisasi ArrayList dan Adapter
        agendaList = new ArrayList<>();
        agendaAdapter = new AgendaAdapter(agendaList);

        // Set Adapter pada ListView
        agendaListView.setAdapter(agendaAdapter);

        // Periksa apakah ada data tambahan dalam intent
        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("username")) {
            // Ambil data tambahan
            String username = intent.getStringExtra("username");
            // Lakukan sesuatu dengan data pengguna, misalnya tampilkan pesan selamat datang
            Toast.makeText(this, "Selamat datang, " + username + "!", Toast.LENGTH_SHORT).show();
        }

        // Panggil fungsi untuk menampilkan data dari Firestore
        tampilkanData();
    }

    private void tambahAgenda() {
        String agendaText = agendaInput.getText().toString();
        String dateText = dateInput.getText().toString();
        String timeText = timeInput.getText().toString();
        String locationText = locationInput.getText().toString();

        // Periksa apakah form input lengkap
        if (agendaText.isEmpty() || dateText.isEmpty() || timeText.isEmpty() || locationText.isEmpty()) {
            Toast.makeText(MainActivity.this, "Semua input harus diisi", Toast.LENGTH_SHORT).show();
            return;
        }

        Map<String, Object> agendaData = new HashMap<>();
        agendaData.put("agenda", agendaText);

        // Tambahkan data tanggal, waktu, dan lokasi hanya jika tidak kosong
        agendaData.put("date", dateText);
        agendaData.put("time", timeText);
        agendaData.put("location", locationText);

        // Tambahkan data ke Firestore
        firestore.collection("agenda").add(agendaData)
                .addOnSuccessListener(documentReference -> {
                    // Dapatkan ID dokumen yang dihasilkan
                    String documentId = documentReference.getId();

                    // Atur ID dokumen pada objek Agenda
                    Agenda newAgenda = new Agenda(agendaText, dateText, timeText, locationText);
                    newAgenda.setId(documentId);

                    // Tambahkan objek Agenda yang telah diatur ID dokumennya ke ArrayList
                    agendaList.add(0, newAgenda); // Menambah di posisi teratas
                    agendaAdapter.notifyDataSetChanged();
                    agendaInput.setText("");
                    dateInput.setText("");
                    timeInput.setText("");
                    locationInput.setText("");

                    Toast.makeText(MainActivity.this, "Agenda ditambahkan", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    Log.e("Firebase", "Error adding agenda data", e);
                });

    }

    public void onDeleteButtonClick(View view) {
        // Dapatkan posisi item yang diklik
        int position = (int) view.getTag();

        // Panggil metode hapusAgenda dari adapter
        agendaAdapter.hapusAgenda(position);
    }

    // Fungsi untuk menampilkan data dari Firestore
    private void tampilkanData() {
        // Menampilkan ProgressBar saat memulai tugas
        loadingProgressBar.setVisibility(View.VISIBLE);

        firestore.collection("agenda").get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    // Menyembunyikan ProgressBar setelah tugas selesai
                    loadingProgressBar.setVisibility(View.GONE);

                    // Bersihkan data sebelum menambahkan data yang baru
                    agendaList.clear();

                    // Ambil data dan tambahkan ke ArrayList
                    for (com.google.firebase.firestore.QueryDocumentSnapshot document : queryDocumentSnapshots) {
                        try {
                            Agenda agenda = document.toObject(Agenda.class);
                            agenda.setId(document.getId()); // Atur ID dari dokumen Firestore
                            agendaList.add(agenda);
                        } catch (Exception e) {
                            Log.e("Firestore", "Error converting agenda data", e);
                        }
                    }

                    // Refresh Adapter setelah data berubah
                    agendaAdapter.notifyDataSetChanged();

                    // Scroll ke posisi teratas setelah menambahkan item baru
                    if (!agendaList.isEmpty()) {
                        agendaListView.smoothScrollToPosition(0);
                    }
                })
                .addOnFailureListener(e -> {
                    loadingProgressBar.setVisibility(View.GONE);
                    Log.e("Firestore", "Error getting agenda data", e);
                });
    }


}
