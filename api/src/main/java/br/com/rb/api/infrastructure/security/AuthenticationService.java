package br.com.rb.api.infrastructure.security;

import br.com.rb.api.domain.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class AuthenticationService implements UserDetailsService {

    private static final Logger logger = LoggerFactory.getLogger(AuthenticationService.class);
    private final UserRepository userRepository;

    public AuthenticationService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        logger.debug("Tentando encontrar usuário com o email: {}", username);
        UserDetails user = userRepository.findByEmail(username);

        if (user == null) {
            logger.error("Usuário não encontrado: {}", username);
            throw new UsernameNotFoundException("Usuário não encontrado: " + username);
        }

        logger.debug("Usuário {} encontrado. Senha no DB: {}", username, user.getPassword());
        logger.debug("Autoridades: {}", user.getAuthorities());

        return user;
    }
}