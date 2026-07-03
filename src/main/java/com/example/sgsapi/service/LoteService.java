package com.example.sgsapi.service;

import com.example.sgsapi.api.dto.LoteDTO;
import com.example.sgsapi.model.entity.Item;
import com.example.sgsapi.model.entity.Lote;
import com.example.sgsapi.model.entity.Remessa;
import com.example.sgsapi.model.enums.TipoMovimentacao;
import com.example.sgsapi.model.repository.ItemRepository;
import com.example.sgsapi.model.repository.LoteRepository;
import com.example.sgsapi.model.repository.RemessaRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LoteService {

    private final LoteRepository loteRepository;
    private final ItemRepository itemRepository;
    private final RemessaRepository remessaRepository;
    private final HistoricoService historicoService;

    @Transactional
    public Long cadastrarLote(LoteDTO dto) {

        Item itemReal = itemRepository.findById(dto.getIdItem()).orElseThrow(() -> new RuntimeException("Item não encontrado!"));

        Remessa remessaReal = remessaRepository.findById(dto.getIdRemessa()).orElseThrow(() -> new RuntimeException("Remessa não encontrada!"));

        Lote novoLote = new Lote();
        novoLote.setItem(itemReal);
        novoLote.setRemessa(remessaReal);
        novoLote.setNome(dto.getNome());
        novoLote.setDataValidade(dto.getDataValidade());
        novoLote.setPrecoFabrica(dto.getPrecoFabrica());
        novoLote.setQuantidade(dto.getQuantidade());

        Lote loteSalvo = loteRepository.save(novoLote);

        // Registrar movimentação no histórico
        historicoService.registrarMovimentacao(loteSalvo.getId(), dto.getQuantidade(), TipoMovimentacao.ENTRADA);

        return novoLote.getId();
    }

    public List<LoteDTO> listarTodosOsLotes() {
        List<Lote> lotes = loteRepository.findAll();
        ModelMapper modelMapper = new ModelMapper();

        return lotes.stream().map(lote -> modelMapper.map(lote, LoteDTO.class)).collect(Collectors.toList());
    }

    @Transactional
    public LoteDTO loteByBid(Long id){
        Lote lote = loteRepository.findById(id).orElseThrow(() -> new RuntimeException("Lote não encontrado!"));

        ModelMapper modelMapper = new ModelMapper();
        return modelMapper.map(lote, LoteDTO.class);
    }

    @Transactional
    public void darBaixaLote(Long idLote, float quantidade) {
        // Encontra lote pelo ID
        Lote lote = loteRepository.findById(idLote).orElseThrow(() -> new RuntimeException("Lote não encontrado!"));

        if (quantidade > lote.getQuantidade()) {
            throw new RuntimeException("Quantidade insuficiente no lote! Disponível: " + lote.getQuantidade());
        }

        // Da a baixa do lote
        lote.setQuantidade(lote.getQuantidade() - quantidade);

        // Registrar movimentação no histórico
        historicoService.registrarMovimentacao(idLote, quantidade, TipoMovimentacao.SAIDA);
    }

    public float calcularEstoqueItem(Long idItem){
        return loteRepository.calcularEstoqueTotalDoItem(idItem);
    }

    @Transactional
    public void deduzirEstoquePorItem(Long idItem, float quantidadeDesejada) {
        // Busca os lotes disponíveis do item, ordenados pela validade (FIFO)
        List<Lote> lotesDisponiveis = loteRepository.findByItemIdAndQuantidadeGreaterThanOrderByDataValidadeAsc(idItem, 0f);

        float quantidadeRestanteParaBaixar = quantidadeDesejada;

        // Passa lote por lote até conseguir abater toda a quantidade pedida
        for (Lote lote : lotesDisponiveis) {

            // Se já bateu a quantidade que precisava, sai do loop
            if (quantidadeRestanteParaBaixar <= 0) {
                break;
            }

            if (lote.getQuantidade() >= quantidadeRestanteParaBaixar) {
                // CENÁRIO A: Esse lote tem sorvete suficiente para fechar o pedido
                lote.setQuantidade(lote.getQuantidade() - quantidadeRestanteParaBaixar);

                // Registra a saída no histórico
                historicoService.registrarMovimentacao(lote.getId(), quantidadeRestanteParaBaixar, TipoMovimentacao.SAIDA);

                quantidadeRestanteParaBaixar = 0; // Pedido inteirado!

            } else {
                // CENÁRIO B: Esse lote tem pouco sorvete. Vamos esvaziar ele e continuar buscando no próximo lote.
                float quantidadeAbatidaDesteLote = lote.getQuantidade();
                quantidadeRestanteParaBaixar -= quantidadeAbatidaDesteLote;

                lote.setQuantidade(0f);

                // Registra a saída no histórico
                historicoService.registrarMovimentacao(lote.getId(), quantidadeAbatidaDesteLote, TipoMovimentacao.SAIDA);
            }

            // Salva o lote atualizado no banco
            loteRepository.save(lote);
        }

        // Se o loop acabou e a quantidade restante ainda for maior que zero... FURTOU O ESTOQUE!
        if (quantidadeRestanteParaBaixar > 0) {
            throw new RuntimeException("Estoque insuficiente! Faltam " + quantidadeRestanteParaBaixar + " unidades no freezer.");
        }
    }

    @Transactional
    public void estornarEstoquePorItem(Long idItem, float quantidadeDevolvida) {
        // Busca os lotes disponíveis. O "Desc" coloca a maior data de validade na posição 0!
        List<Lote> lotes = loteRepository.findByItemIdAndQuantidadeGreaterThanOrderByDataValidadeDesc(idItem, -1f);

        if (lotes.isEmpty()) {
            throw new RuntimeException("Não há lotes ativos para este item para fazer a devolução.");
        }

        Lote loteMaisNovo = lotes.get(0);

        loteMaisNovo.setQuantidade(loteMaisNovo.getQuantidade() + quantidadeDevolvida);

        historicoService.registrarMovimentacao(loteMaisNovo.getId(), quantidadeDevolvida, TipoMovimentacao.ENTRADA);

        loteRepository.save(loteMaisNovo);
    }
}