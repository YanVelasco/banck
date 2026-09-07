package com.bank.gatewayserver.config.auth;

import org.springframework.core.convert.converter.Converter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.Collection;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

public class KeycloakRoleConverter implements Converter<Jwt, Collection<GrantedAuthority>> {
    @Override
    public Collection<GrantedAuthority> convert(Jwt jwt) {
        return Optional.ofNullable(jwt.getClaim("realm_access"))
                .filter(Map.class::isInstance)
                .map(m -> (Map<?, ?>) m)
                .map(m -> m.get("roles"))
                .filter(Collection.class::isInstance)
                .map(r -> (Collection<?>) r)
                .stream()
                .flatMap(Collection::stream)
                .filter(Objects::nonNull)
                .map(Object::toString)
                .map(roleName -> "ROLE_" + roleName)
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }
}
