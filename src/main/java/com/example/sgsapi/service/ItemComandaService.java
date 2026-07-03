package com.example.sgsapi.service;

import com.example.sgsapi.api.dto.ItemComandaDTO;
import com.example.sgsapi.model.entity.Comanda;
import com.example.sgsapi.model.entity.Item;
import com.example.sgsapi.model.entity.ItemComanda;
import com.example.sgsapi.model.repository.ComandaRepository;
import com.example.sgsapi.model.repository.ItemComandaRepository;
import com.example.sgsapi.model.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ItemComandaService {

    private final ItemComandaRepository itemComandaRepository;
    private final ComandaRepository comandaRepository;
    private final ItemRepository itemRepository;
    private final LoteService loteService;

    @Transactional
    public void adicionarItem(ItemComandaDTO dto) {
        Comanda comanda = comandaRepository.findById(dto.getIdComanda()).orElseThrow(() -> new RuntimeException("Comanda não encontrada!"));

        Item itemCardapio = itemRepository.findById(dto.getIdItem()).orElseThrow(() -> new RuntimeException("Item não encontrado no cardápio!"));

        // Bloqueia a venda se o cliente já pagou
        if (!comanda.isAberta()) {
            throw new RuntimeException("Não é possível adicionar itens. Esta comanda já está fechada!");
        }

        // INTEGRAÇÃO DE ESTOQUE: Tira o item do freezer na hora!
        // (Nota: Precisaremos criar esse método no LoteService)
        loteService.deduzirEstoquePorItem(itemCardapio.getId(), dto.getQuantidade());

        ItemComanda novoPedido = new ItemComanda();
        novoPedido.setComanda(comanda);
        novoPedido.setItem(itemCardapio);
        novoPedido.setQuantidade(dto.getQuantidade());
        novoPedido.setPreco(itemCardapio.getPrecoVenda());

        itemComandaRepository.save(novoPedido);
    }

    @Transactional
    public void removerPedido(ItemComandaDTO dto) {
        Comanda comanda = comandaRepository.findById(dto.getIdComanda()).orElseThrow(() -> new RuntimeException("Comanda não encontrada!"));

        if (!comanda.isAberta()) {
            throw new RuntimeException("Não é possível remover itens de uma comanda fechada!");
        }

        // CORREÇÃO: Aqui sim usamos o getId() que representa o ID do ItemComanda
        ItemComanda pedidoCancelado = itemComandaRepository.findById(dto.getId()).orElseThrow(() -> new RuntimeException("Não existe ItemComanda com esse ID"));

        // DEVOLUÇÃO AO ESTOQUE (Se cancelou o pedido, o sorvete volta pro freezer)
        loteService.estornarEstoquePorItem(pedidoCancelado.getItem().getId(), pedidoCancelado.getQuantidade());

        itemComandaRepository.delete(pedidoCancelado);
    }

    @Transactional
    public void alterarPedido(ItemComandaDTO dto) {
        // Busca a comanda e verifica se está aberta (usando idComanda)
        Comanda comanda = comandaRepository.findById(dto.getIdComanda()).orElseThrow(() -> new RuntimeException("Comanda não encontrada!"));

        if (!comanda.isAberta()) {
            throw new RuntimeException("Não é possível alterar itens de uma comanda fechada!");
        }

        // Busca o pedido (ItemComanda) atual no banco (usando id)
        ItemComanda pedidoAtual = itemComandaRepository.findById(dto.getId()).orElseThrow(() -> new RuntimeException("Não existe ItemComanda com esse ID"));

        // Busca o novo item que o cliente deseja do cardápio
        Item novoItemCardapio = itemRepository.findById(dto.getIdItem()).orElseThrow(() -> new RuntimeException("Item não encontrado no cardápio!"));

        // Devolve os itens antigos para o "freezer"
        loteService.estornarEstoquePorItem(pedidoAtual.getItem().getId(), pedidoAtual.getQuantidade());

        // Retira do freezer a nova configuração do pedido
        loteService.deduzirEstoquePorItem(novoItemCardapio.getId(), dto.getQuantidade());

        pedidoAtual.setComanda(comanda);

        pedidoAtual.setItem(novoItemCardapio);
        pedidoAtual.setQuantidade(dto.getQuantidade());
        pedidoAtual.setPreco(novoItemCardapio.getPrecoVenda());

        itemComandaRepository.save(pedidoAtual);
    }
}