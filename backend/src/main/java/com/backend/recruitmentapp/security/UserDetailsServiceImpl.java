package com.backend.recruitmentapp.security;

import com.backend.recruitmentapp.model.Person;
import com.backend.recruitmentapp.repository.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UserDetailsServiceImpl implements UserDetailsService
    {
    @Autowired
    PersonRepository userRepository;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException
        {
        List<Person> personList = userRepository.findPersonByUsername(username);
        if (personList.size() == 0)
            {
            new UsernameNotFoundException("User Not Found with username: " + username);
            }
        if (personList.size() > 1)
            {
            new UsernameNotFoundException("So much users: " + username);
            }
        Person user = userRepository.findPersonByUsername(username).get(0);
        return UserDetailsImpl.build(user);
        }

    }