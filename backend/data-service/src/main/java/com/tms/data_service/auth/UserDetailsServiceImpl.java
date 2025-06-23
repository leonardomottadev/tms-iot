package com.tms.data_service.auth;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.tms.data_service.repository.UserRepository;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    public UserDetailsServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        System.out.println("Buscando o e-mail: "+ email);
        try {
            userRepository.findByEmail(email);
        } catch (Exception ex){
             System.out.println("Exceção: " + ex.getMessage());
        }
        var user = userRepository.findByEmail(email);
        System.out.println("Fim da busca pelo e-mail: " + email);
        if (user.isPresent()) {
            System.out.println("Usuário ENCONTRADO para o email: " + email);
        } else {
            System.out.println("Usuário NÃO ENCONTRADO para o email: " + email);
        }

        return user
        .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }
}
