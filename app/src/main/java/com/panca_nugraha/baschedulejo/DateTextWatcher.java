    package com.panca_nugraha.baschedulejo;

    import android.text.Editable;
    import android.text.TextWatcher;
    import android.widget.EditText;

    public class DateTextWatcher implements TextWatcher {
        private EditText editText;

        public DateTextWatcher(EditText editText) {
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

            // Tambahkan "/" setelah tanggal
            if (text.length() == 2 && !text.contains("/")) {
                text += "/";
            }

            // Tambahkan "/" setelah bulan
            if (text.length() == 5 && !text.endsWith("/")) {
                text = text.substring(0, 5) + "/" + text.substring(5);
            }

            editText.removeTextChangedListener(this); // Hapus listener sementara
            editText.setText(text);
            editText.setSelection(text.length()); // Posisikan kursor di akhir teks
            editText.addTextChangedListener(this); // Tambahkan listener kembali
        }

    }



