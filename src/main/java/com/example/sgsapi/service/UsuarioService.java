package com.example.sgsapi.service;

import com.example.sgsapi.api.dto.LoteDTO;
import com.example.sgsapi.api.dto.UsuarioDTO;
import com.example.sgsapi.model.entity.Lote;
import com.example.sgsapi.model.entity.Usuario;
import com.example.sgsapi.model.enums.Perfil;
import com.example.sgsapi.model.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder; // Import novo
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UsuarioService implements UserDetailsService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    public void cadastrarUsuario(UsuarioDTO dto) {
        Usuario novoUsuario = new Usuario();
        novoUsuario.setNome(dto.getNome());
        novoUsuario.setLogin(dto.getLogin());

        novoUsuario.setSenha(passwordEncoder.encode(dto.getSenha()));

        novoUsuario.setDataNascimento(dto.getDataNascimento());
        novoUsuario.setDataContratacao(dto.getDataContratacao());
        novoUsuario.setGrupo(dto.getGrupo());

        usuarioRepository.save(novoUsuario);
    }

    public void alterarUsuario(Long id, String nome, String login, String senha, Perfil grupo) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado!"));
        usuario.setNome(nome);
        usuario.setLogin(login);

        // Não esqueça de criptografar ao alterar a senha também!
        usuario.setSenha(passwordEncoder.encode(senha));
        usuario.setGrupo(grupo);

        usuarioRepository.save(usuario);
    }

    public void excluirUsuario(Long id) {
        usuarioRepository.deleteById(id);
    }

    public List<UsuarioDTO> listarUsuarios(){
        List<Usuario> usuarios = usuarioRepository.findAll();
        ModelMapper modelMapper = new ModelMapper();

        return usuarios.stream().map(usuario -> {
            UsuarioDTO dto = modelMapper.map(usuario, UsuarioDTO.class);
            dto.setSenha(null); // Nunca expor a senha (mesmo criptografada) na API
            return dto;
        }).collect(Collectors.toList());
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Usuario usuario = usuarioRepository.findByLogin(username).orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado: " + username));

        return User.builder()
                .username(usuario.getLogin()) // Trocado para getLogin()
                .password(usuario.getSenha())
                .roles(usuario.getGrupo().name())
                .build();
    }
}