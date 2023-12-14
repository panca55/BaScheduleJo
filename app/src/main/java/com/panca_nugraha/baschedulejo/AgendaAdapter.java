package com.panca_nugraha.baschedulejo;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class AgendaAdapter extends BaseAdapter {
    private ArrayList<Agenda> agendaList;

    public AgendaAdapter(ArrayList<Agenda> agendaList) {
        this.agendaList = agendaList;
    }

    @Override
    public int getCount() {
        return agendaList.size();
    }

    @Override
    public Object getItem(int position) {
        return agendaList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_agenda, parent, false);
        }

        TextView agendaTextView = convertView.findViewById(R.id.agendaTextView);
        TextView dateTextView = convertView.findViewById(R.id.textDate);
        TextView timeTextView = convertView.findViewById(R.id.textTime);
        TextView locationTextView = convertView.findViewById(R.id.textLocation);

        Agenda agenda = agendaList.get(position);

        agendaTextView.setText(agenda.getAgenda());
        dateTextView.setText("Date: " + agenda.getDate());
        timeTextView.setText("Time: " + agenda.getTime());
        locationTextView.setText("Location: " + agenda.getLocation());

        Button deleteButton = convertView.findViewById(R.id.deleteButton);
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hapusAgenda(position);
            }
        });

        return convertView;
    }

    // Metode untuk menghapus agenda dari adapter dan Firestore
    public void hapusAgenda(int position) {
        // Dapatkan ID dokumen agenda yang akan dihapus
        String documentId = agendaList.get(position).getId();

        // Periksa apakah documentId valid
        if (documentId == null) {
            Log.e("Firestore", "Error: documentId is null");
            return;
        }

        Log.d("Firestore", "Deleting document with ID: " + documentId);

        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        // Hapus dari Firestore
        firestore.collection("agenda").document(documentId)
                .delete()
                .addOnSuccessListener(aVoid -> {
                    // Hapus dari adapter
                    agendaList.remove(position);
                    notifyDataSetChanged();
                })
                .addOnFailureListener(e -> {
                    Log.e("Firestore", "Error deleting agenda data", e);
                });
    }

}
