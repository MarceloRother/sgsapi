package com.example.sgsapi.service;

import com.example.sgsapi.api.dto.HistoricoDTO;
import com.example.sgsapi.model.entity.Historico;
import com.example.sgsapi.model.enums.TipoMovimentacao;
import com.example.sgsapi.model.repository.HistoricoRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class HistoricoService {

    private final HistoricoRepository historicoRepository;

    @Transactional
    public void registrarMovimentacao(Long loteId, float quantidade, TipoMovimentacao tipo) {
        Historico registro = new Historico();
        registro.setData(LocalDateTime.now());
        registro.setIdLote(loteId);
        registro.setQuantidade(quantidade);
        registro.setTipoOperacao(tipo);

        historicoRepository.save(registro);

        System.out.println("Auditoria: Movimentação de " + tipo + " registrada com sucesso!");
    }

    public List<HistoricoDTO> buscarTodoHistorico() {
        List<Historico> historicos = historicoRepository.findAll();
        ModelMapper modelMapper = new ModelMapper();
        return historicos.stream().map(h -> modelMapper.map(h, HistoricoDTO.class)).collect(Collectors.toList());
    }

    public List<HistoricoDTO> buscarHistoricoPorLote(Long loteId) {
        List<Historico> historicos = historicoRepository.findByIdLote(loteId);

        ModelMapper modelMapper = new ModelMapper();
        return historicos.stream().map(h -> modelMapper.map(h, HistoricoDTO.class)).collect(Collectors.toList());
    }

    public List<HistoricoDTO> buscarMovimentacoesDeHoje() {
        LocalDateTime comecoDoDia = LocalDateTime.now().withHour(0).withMinute(0).withSecond(0);
        LocalDateTime fimDoDia = LocalDateTime.now().withHour(23).withMinute(59).withSecond(59);

        List<Historico> historicos = historicoRepository.findByDataBetween(comecoDoDia, fimDoDia);
        ModelMapper modelMapper = new ModelMapper();
        return historicos.stream().map(h -> modelMapper.map(h, HistoricoDTO.class)).collect(Collectors.toList());
    }
}