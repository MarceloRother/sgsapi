package com.example.sgsapi.service;

import com.example.sgsapi.api.dto.ItemDTO;
import com.example.sgsapi.api.dto.TabelaNutricionalDTO;
import com.example.sgsapi.model.entity.Item;
import com.example.sgsapi.model.entity.TabelaNutricional;
import com.example.sgsapi.model.repository.ItemRepository;
import org.modelmapper.ModelMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ItemService {

    private final ItemRepository itemRepository;

    public List<ItemDTO> getItens() {
        List<Item> itens = itemRepository.findAll();
        ModelMapper modelMapper = new ModelMapper();

        // Converte todos os Itens da lista para ItemDTO de uma vez só
        return itens.stream().map(item -> modelMapper.map(item, ItemDTO.class)).collect(Collectors.toList());
    }

    public List<ItemDTO> getItensAtivos() {
        List<Item> itens = itemRepository.findByAtivoTrue();
        ModelMapper modelMapper = new ModelMapper();

        return itens.stream().map(item -> modelMapper.map(item, ItemDTO.class)).collect(Collectors.toList());
    }

    public ItemDTO getItemById(Long id){
        Item item = itemRepository.findById(id).orElseThrow(() -> new RuntimeException("Item não encontrado"));

        ModelMapper modelMapper = new ModelMapper();
        return modelMapper.map(item, ItemDTO.class);
    }

    public ItemDTO getItemByName(String nome) {
        Item item = itemRepository.findByNome(nome).orElseThrow(() -> new RuntimeException("Item não encontrado!"));

        ModelMapper modelMapper = new ModelMapper();
        return modelMapper.map(item, ItemDTO.class);
    }

    @Transactional
    public void cadastrarItem(ItemDTO dto) {
        // Instancia e mapeia a Tabela Nutricional
        TabelaNutricional nutri = new TabelaNutricional();
        nutri.setCalorias(dto.getTabelaNutricional().getCalorias());
        nutri.setGorduras(dto.getTabelaNutricional().getGorduras());
        nutri.setAcucares(dto.getTabelaNutricional().getAcucares());
        nutri.setContemGluten(dto.getTabelaNutricional().isContemGluten());
        nutri.setContemLactose(dto.getTabelaNutricional().isContemLactose());
        nutri.setContemLeite(dto.getTabelaNutricional().isContemLeite());

        // Instancia e mapeia o Item principal
        Item novoItem = new Item();
        novoItem.setNome(dto.getNome());
        novoItem.setTipo(dto.getTipo());
        novoItem.setTabelaNutricional(nutri); // Associa o relacionamento @OneToOne

        nutri.setItem(novoItem);
        novoItem.setTabelaNutricional(nutri);

        itemRepository.save(novoItem);
    }

    @Transactional
    public void alterarItem(Long id, ItemDTO dto) {
        // Busca o item existente no banco
        Item itemExistente = itemRepository.findById(id).orElseThrow(() -> new RuntimeException("Item não encontrado!"));

        // Atualiza os dados básicos
        itemExistente.setNome(dto.getNome());
        itemExistente.setPrecoVenda(dto.getPrecoVenda());

        // Atualiza a tabela nutricional
        TabelaNutricional nutri = itemExistente.getTabelaNutricional();
        nutri.setCalorias(dto.getTabelaNutricional().getCalorias());
        nutri.setGorduras(dto.getTabelaNutricional().getGorduras());
        nutri.setAcucares(dto.getTabelaNutricional().getAcucares());
        nutri.setContemGluten(dto.getTabelaNutricional().isContemGluten());
        nutri.setContemLactose(dto.getTabelaNutricional().isContemLactose());
        nutri.setContemLeite(dto.getTabelaNutricional().isContemLeite());

        itemRepository.save(itemExistente);
    }

    @Transactional
    public void inativarItem(Long id) {
        Item item = itemRepository.findById(id).orElseThrow(() -> new RuntimeException("Item não encontrado!"));

        item.setAtivo(false);
        itemRepository.save(item);
    }

    @Transactional
    public void deletarItem(Long id) {
        Item item = itemRepository.findById(id).orElseThrow(() -> new RuntimeException("Item não encontrado!"));

        itemRepository.delete(item);
    }

}