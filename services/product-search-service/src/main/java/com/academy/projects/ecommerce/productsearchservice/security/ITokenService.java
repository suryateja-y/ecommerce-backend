package com.academy.projects.ecommerce.productsearchservice.security;

import java.util.List;

public interface ITokenService {
    String validate(String token);
    List<String> getPermissions(String token);
}
