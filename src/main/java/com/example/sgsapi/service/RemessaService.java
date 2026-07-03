package com.example.sgsapi.service;

import com.example.sgsapi.api.dto.RemessaDTO;
import com.example.sgsapi.model.entity.Fornecedor;
import com.example.sgsapi.model.entity.Remessa;
import com.example.sgsapi.model.repository.FornecedorRepository;
import com.example.sgsapi.model.repository.RemessaRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RemessaService {

    private final RemessaRepository remessaRepository;
    private final FornecedorRepository fornecedorRepository;

    @Transactional
    public void cadastraRemessa(RemessaDTO dto){
        Fornecedor fornecedor = fornecedorRepository.findById(dto.getFornecedor_id())
                .orElseThrow(() -> new RuntimeException("Fornecedor não encontrado!"));

        Remessa novaRemessa = new Remessa();
        novaRemessa.setFornecedor(fornecedor);
        novaRemessa.setDataChegada(dto.getDataChegada());
        novaRemessa.setNumeroNotaFiscal(dto.getNumeroNotaFiscal());
        novaRemessa.setCustoTotalFrete(dto.getCustoTotalFrete());

        remessaRepository.save(novaRemessa);
    }

    public List<RemessaDTO> consultarRemessa(){
        List<Remessa> remessas = remessaRepository.findAll();
        ModelMapper modelMapper = new ModelMapper();

        return remessas.stream()
                .map(remessa -> modelMapper.map(remessa, RemessaDTO.class))
                .collect(Collectors.toList());
    }

    public RemessaDTO consultarRemessaID(Long id){
        Remessa remessa = remessaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Remessa não encontrada!"));

        ModelMapper modelMapper = new ModelMapper();
        return modelMapper.map(remessa, RemessaDTO.class);
    }

    @Transactional
    public void alterarRemessa(RemessaDTO remessaNovaDto){
        Remessa remessaAtual = remessaRepository.findById(remessaNovaDto.getId()).orElseThrow(() -> new RuntimeException("Remessa não encontrada"));

        remessaAtual.setCustoTotalFrete(remessaNovaDto.getCustoTotalFrete());
        remessaAtual.setNumeroNotaFiscal(remessaNovaDto.getNumeroNotaFiscal());

        remessaRepository.save(remessaAtual);
    }

    @Transactional
    public void deletarRemessa(Long id){
        Remessa remessa = remessaRepository.findById(id).orElseThrow(() -> new RuntimeException("Remessa não encontrada"));

        remessaRepository.delete(remessa);
    }
}