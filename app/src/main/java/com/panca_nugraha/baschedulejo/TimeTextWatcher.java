package com.panca_nugraha.baschedulejo;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

public class TimeTextWatcher implements TextWatcher {
    private EditText editText;

    public TimeTextWatcher(EditText editText) {
        this.editText = editText;
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {
        // Tidak perlu diimplementasikan
    }

    @Override
    public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
        // Tidak perlu diimplementasikan
    }

    @Override
    public void afterTextChanged(Editable editable) {
        String text = editable.toString();

        if (text.length() == 2 && !text.contains(":")) {
            text += ":";
        }

        editText.removeTextChangedListener(this); // Hapus listener sementara
        editText.setText(text);
        editText.setSelection(text.length()); // Posisikan kursor di akhir teks
        editText.addTextChangedListener(this); // Tambahkan listener kembali
    }

}

