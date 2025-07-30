package br.com.rb.api.application.mapper;

import br.com.rb.api.domain.model.User;

public class UserMapper {
    public static User toEntity(String name, String email, String encodedPassword) {
        User user = new User();
        user.setName(name);
        user.setEmail(email);
        user.setPassword(encodedPassword);
        return user;
    }
}