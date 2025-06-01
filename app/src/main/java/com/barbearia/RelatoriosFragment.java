package com.barbearia;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.fragment.app.Fragment;

public class RelatoriosFragment extends Fragment {
    public RelatoriosFragment() {
        // Construtor vazio obrigatório
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflar o layout do fragmento
        return inflater.inflate(R.layout.fragment_relatorios, container, false);
    }
}
