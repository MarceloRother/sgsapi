package com.example.sgsapi.service;

import com.example.sgsapi.api.dto.ComandaDTO;
import com.example.sgsapi.api.dto.ItemComandaDTO;
import com.example.sgsapi.model.entity.Comanda;
import com.example.sgsapi.model.entity.Item;
import com.example.sgsapi.model.entity.ItemComanda;
import com.example.sgsapi.model.entity.Lote;
import com.example.sgsapi.model.repository.ComandaRepository;
import com.example.sgsapi.model.repository.ItemComandaRepository;
import com.example.sgsapi.model.repository.ItemRepository;
import com.example.sgsapi.model.repository.LoteRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ComandaService {

    private final ComandaRepository comandaRepository;
    private final ItemComandaRepository itemComandaRepository;
    private final ItemRepository itemRepository;
    private final LoteRepository loteRepository;

    @Transactional
    public Long abrirComanda() {
        Comanda novaComanda = new Comanda();
        novaComanda.setDataHoraAbertura(String.valueOf(LocalDateTime.now()));
        novaComanda.setAberta(true);

        Comanda comandaSalva = comandaRepository.save(novaComanda);
        return novaComanda.getId();
    }

    public ComandaDTO visualizarConta(Long idComanda) {
        Comanda comanda = comandaRepository.findById(idComanda).orElseThrow(() -> new RuntimeException("Comanda não encontrada!"));

        ModelMapper modelMapper = new ModelMapper();
        return modelMapper.map(comanda, ComandaDTO.class);
    }

    public List<ComandaDTO> visualizarComandas(){
        List<Comanda> comanda = comandaRepository.findAll();

        ModelMapper modelMapper = new ModelMapper();
        return Collections.singletonList(modelMapper.map(comanda, ComandaDTO.class));
    }

    @Transactional
    public void fecharComanda(Long idComanda) {
        Comanda comanda = comandaRepository.findById(idComanda).orElseThrow(() -> new RuntimeException("Comanda não encontrada!"));

        comanda.setAberta(false);
        comanda.setDataHoraFechamento(String.valueOf(LocalDateTime.now()));

        comandaRepository.save(comanda);
    }

    @Transactional
    public void addItem(ItemComandaDTO dto){
        Comanda comanda = comandaRepository.findById(dto.getIdComanda()).orElseThrow(() -> new RuntimeException("Comanda não encontrada!"));
        // Bloqueia a venda se o cliente já pagou
        if (!comanda.isAberta()) {
            throw new RuntimeException("Não é possível adicionar itens. Esta comanda já está fechada!");
        }
        Lote lote = loteRepository.findById(dto.getIdLote()).orElseThrow(() -> new RuntimeException("Lote não encontrado no cardápio!"));
        Item item = itemRepository.findById(dto.getIdItem()).orElseThrow(() -> new RuntimeException("Item não encontrado no cardápio!"));


        ItemComanda novoPedido = new ItemComanda();
        novoPedido.setComanda(comanda);
        novoPedido.setLote(lote);
        novoPedido.setItem(item);
        novoPedido.setQuantidade(dto.getQuantidade());

        itemComandaRepository.save(novoPedido);
    }

    @Transactional
    public void alteraItem(ItemComandaDTO dto){
        Comanda comanda = comandaRepository.findById(dto.getIdComanda()).orElseThrow(() -> new RuntimeException("Comanda não encontrada!"));
        // Bloqueia a venda se o cliente já pagou
        if (!comanda.isAberta()) {
            throw new RuntimeException("Não é possível adicionar itens. Esta comanda já está fechada!");
        }
        Lote lote = loteRepository.findById(dto.getIdItem()).orElseThrow(() -> new RuntimeException("Lote não encontrado no cardápio!"));
        Item item = itemRepository.findById(dto.getIdItem()).orElseThrow(() -> new RuntimeException("Item não encontrado no cardápio!"));


        ItemComanda pedido = itemComandaRepository.findById(dto.getId()).orElseThrow(() -> new RuntimeException("ItemComanda não encontrado!"));
        pedido.setComanda(comanda);
        pedido.setLote(lote);
        pedido.setItem(item);
        pedido.setQuantidade(dto.getQuantidade());

        itemComandaRepository.save(pedido);
    }

    @Transactional
    public void deletaItem(ItemComandaDTO dto){
        Comanda comanda = comandaRepository.findById(dto.getIdComanda()).orElseThrow(() -> new RuntimeException("Comanda não encontrada!"));
        // Bloqueia a venda se o cliente já pagou
        if (!comanda.isAberta()) {
            throw new RuntimeException("Não é possível adicionar itens. Esta comanda já está fechada!");
        }
        Lote lote = loteRepository.findById(dto.getIdItem()).orElseThrow(() -> new RuntimeException("Lote não encontrado no cardápio!"));
        Item item = itemRepository.findById(dto.getIdItem()).orElseThrow(() -> new RuntimeException("Item não encontrado no cardápio!"));


        ItemComanda pedido = itemComandaRepository.findById(dto.getId()).orElseThrow(() -> new RuntimeException("ItemComanda não encontrado!"));

        itemComandaRepository.delete(pedido);
    }
}