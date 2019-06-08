package com.example.easynotes.model;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class CustomUserDetails extends User implements UserDetails {
    public CustomUserDetails(final User user)
    {
        super(user);
    }

//    public Collection<? extends GrantedAuthority> getAuthorities() {
//        Role role = getUserrole();
////        Role[] roles = new Role[]{role};
//////        ArrayList<Role> rolesList = new ArrayList<>();
//////        rolesList.add(role);
////        Collection<? extends GrantedAuthority> collection = new ArrayList<>();
////        ((ArrayList<Role>) collection).add(0,role);
//
//            List<GrantedAuthority> authorities
//                    = new ArrayList<>();
//            //for (Role role : roles) {
//                authorities.add(new SimpleGrantedAuthority(role.getRole()));
////                role.getPrivileges().stream()
////                        .map(p -> new SimpleGrantedAuthority(p.getName()))
////                        .forEach(authorities::add);
//           // }
//
//        return authorities;
//    }

        @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {

//        return getRoles()
//                .stream()
//                .map(role -> new SimpleGrantedAuthority("ROLE_" + role.getRole()))
//                .collect(Collectors.toList());
        return getRoles()
                .stream()
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role.getRole()))
                .collect(Collectors.toList());

        //return getUserrole();

    }

    @Override
    public String getPassword() {
        return super.getPassword();
    }

    @Override
    public String getUsername() {
        return super.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
