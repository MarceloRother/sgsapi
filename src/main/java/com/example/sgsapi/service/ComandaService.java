package com.example.sgsapi.service;

import com.example.sgsapi.api.dto.ComandaDTO;
import com.example.sgsapi.model.entity.Comanda;
import com.example.sgsapi.model.entity.ItemComanda;
import com.example.sgsapi.model.repository.ComandaRepository;
import com.example.sgsapi.model.repository.ItemComandaRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ComandaService {

    private final ComandaRepository comandaRepository;
    private final ItemComandaRepository itemComandaRepository;

    @Transactional
    public Long abrirComanda(String nomeCliente, int numeroMesa) {
        Comanda novaComanda = new Comanda();
        novaComanda.setNumeroMesa(numeroMesa);
        novaComanda.setDataHoraAbertura(LocalDateTime.now());
        novaComanda.setAberta(true);

        Comanda comandaSalva = comandaRepository.save(novaComanda);
        return novaComanda.getId();
    }

    public ComandaDTO visualizarConta(Long idComanda) {
        Comanda comanda = comandaRepository.findById(idComanda).orElseThrow(() -> new RuntimeException("Comanda não encontrada!"));

        ModelMapper modelMapper = new ModelMapper();
        return modelMapper.map(comanda, ComandaDTO.class);
    }

    @Transactional
    public void fecharComanda(Long idComanda) {
        Comanda comanda = comandaRepository.findById(idComanda).orElseThrow(() -> new RuntimeException("Comanda não encontrada!"));

        comanda.setAberta(false);
        comanda.setDataHoraFechamento(LocalDateTime.now());

        comandaRepository.save(comanda);
    }
}