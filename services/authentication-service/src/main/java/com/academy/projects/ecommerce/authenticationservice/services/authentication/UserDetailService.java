package com.academy.projects.ecommerce.authenticationservice.services.authentication;

import com.academy.projects.ecommerce.authenticationservice.exceptions.authentication.UserNotFoundException;
import com.academy.projects.ecommerce.authenticationservice.models.Permission;
import com.academy.projects.ecommerce.authenticationservice.models.Role;
import com.academy.projects.ecommerce.authenticationservice.models.User;
import com.academy.projects.ecommerce.authenticationservice.repositories.authentication.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserDetailService implements UserDetailsService {

    private final UserRepository userRepository;

    @Autowired
    public UserDetailService(final UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    @Override
    public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {
        User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException(userId));
        return new org.springframework.security.core.userdetails.User(userId, user.getPassword(), user.isEnabled(), user.isAccountNonExpired(), user.isCredentialsNonExpired(), user.isAccountNonLocked(), getAuthorities(user));
    }

    private List<GrantedAuthority> getAuthorities(User user) {
        Set<Role> roles = user.getRoles();
        List<String> privileges = getPrivileges(roles);
        return privileges.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toUnmodifiableList());
    }

    private List<String> getPrivileges(Set<Role> roles) {
        List<String> privileges = new ArrayList<>();
        for (Role role : roles) {
            privileges.add("ROLE_" + role.getRoleName());
            for(Permission permission : role.getPermissions()) {
                privileges.add(permission.getPermissionName());
            }
        }
        return privileges;
    }


}
