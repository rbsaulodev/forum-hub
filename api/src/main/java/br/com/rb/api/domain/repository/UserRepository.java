package br.com.rb.api.domain.repository;

import br.com.rb.api.domain.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;

public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByEmail(String email);
    boolean existsByRoles_Id(Long roleId);
    UserDetails findByEmail(String email);
    Page<User> findByRoles_Name(String roleName, Pageable pageable);
}
