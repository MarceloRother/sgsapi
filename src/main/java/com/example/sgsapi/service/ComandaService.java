package com.example.sgsapi.service;

import com.example.sgsapi.api.dto.ComandaDTO;
import com.example.sgsapi.api.dto.ItemComandaDTO;
import com.example.sgsapi.model.entity.Comanda;
import com.example.sgsapi.model.entity.Item;
import com.example.sgsapi.model.entity.ItemComanda;
import com.example.sgsapi.model.entity.Lote;
import com.example.sgsapi.model.enums.TipoMovimentacao;
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
    private final HistoricoService historicoService;

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

        historicoService.registrarMovimentacao(lote.getId(), dto.getQuantidade(), TipoMovimentacao.SAIDA);

        itemComandaRepository.save(novoPedido);

    }

    @Transactional
    public void alteraItem(ItemComandaDTO dto){
        Comanda comanda = comandaRepository.findById(dto.getIdComanda())
                .orElseThrow(() -> new RuntimeException("Comanda não encontrada!"));

        if (!comanda.isAberta()) {
            throw new RuntimeException("Não é possível alterar itens. Esta comanda já está fechada!");
        }

        ItemComanda pedidoAntigo = itemComandaRepository.findById(dto.getId())
                .orElseThrow(() -> new RuntimeException("Item da comanda não encontrado!"));

        Lote loteAntigo = pedidoAntigo.getLote();
        float quantidadeAntiga = pedidoAntigo.getQuantidade();

        loteAntigo.setQuantidade(loteAntigo.getQuantidade() + quantidadeAntiga);
        loteRepository.save(loteAntigo);

        historicoService.registrarMovimentacao(loteAntigo.getId(), quantidadeAntiga, TipoMovimentacao.ENTRADA);

        Lote loteNovo = loteRepository.findById(dto.getIdLote()) // Corrigido para getIdLote()
                .orElseThrow(() -> new RuntimeException("Novo lote não encontrado!"));

        if (loteNovo.getQuantidade() < dto.getQuantidade()) {
            throw new RuntimeException("Estoque insuficiente no novo lote! Disponível: " + loteNovo.getQuantidade());
        }

        loteNovo.setQuantidade(loteNovo.getQuantidade() - dto.getQuantidade());
        loteRepository.save(loteNovo);

        historicoService.registrarMovimentacao(loteNovo.getId(), dto.getQuantidade(), TipoMovimentacao.SAIDA);

        Item itemNovo = loteNovo.getItem();

        pedidoAntigo.setComanda(comanda);
        pedidoAntigo.setLote(loteNovo);
        pedidoAntigo.setItem(itemNovo);
        pedidoAntigo.setQuantidade(dto.getQuantidade());

        itemComandaRepository.save(pedidoAntigo);
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

        historicoService.registrarMovimentacao(lote.getId(), dto.getQuantidade(), TipoMovimentacao.ENTRADA);
    }
}