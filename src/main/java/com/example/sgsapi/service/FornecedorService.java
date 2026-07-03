package com.example.sgsapi.service;

import com.example.sgsapi.api.dto.FornecedorDTO;
import com.example.sgsapi.model.entity.Fornecedor;
import com.example.sgsapi.model.repository.FornecedorRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FornecedorService {

    private final FornecedorRepository fornecedorRepository;

    // CADASTRAR
    @Transactional
    public Long cadastrarFornecedor(FornecedorDTO dto) {
        ModelMapper modelMapper = new ModelMapper();

        // Converte o DTO direto para a Entidade e salva
        Fornecedor novoFornecedor = modelMapper.map(dto, Fornecedor.class);
        fornecedorRepository.save(novoFornecedor);

        return novoFornecedor.getId();
    }

    // LISTAR TODOS
    public List<FornecedorDTO> consultarFornecedores() {
        List<Fornecedor> fornecedores = fornecedorRepository.findAll();
        ModelMapper modelMapper = new ModelMapper();

        return fornecedores.stream().map(fornecedor -> modelMapper.map(fornecedor, FornecedorDTO.class)).collect(Collectors.toList());
    }

    // BUSCAR POR ID
    public FornecedorDTO consultarFornecedorPorId(Long id) {
        Fornecedor fornecedor = fornecedorRepository.findById(id).orElseThrow(() -> new RuntimeException("Fornecedor não encontrado!"));

        ModelMapper modelMapper = new ModelMapper();
        return modelMapper.map(fornecedor, FornecedorDTO.class);
    }

    // ALTERAR
    @Transactional
    public void alterarFornecedor(Long id, FornecedorDTO dto) {
        // Garante que o fornecedor existe antes de alterar
        Fornecedor fornecedorAtual = fornecedorRepository.findById(id).orElseThrow(() -> new RuntimeException("Fornecedor não encontrado!"));

        // Atualiza os dados
        fornecedorAtual.setRazaoSocial(dto.getRazaoSocial());
        fornecedorAtual.setCnpj(dto.getCnpj());
        fornecedorAtual.setTelefoneContato(dto.getTelefoneContato());
        fornecedorAtual.setEmail(dto.getEmail());

        // Salva a alteração no banco
        fornecedorRepository.save(fornecedorAtual);
    }

    // EXCLUIR/INATIVAR (Soft Delete)
    @Transactional
    public void inativarFornecedor(Long id) {
        Fornecedor fornecedor = fornecedorRepository.findById(id).orElseThrow(() -> new RuntimeException("Fornecedor não encontrado!"));

        fornecedor.setAtivo(false);
        fornecedorRepository.save(fornecedor);
    }
}