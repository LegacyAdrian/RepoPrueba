package bank.backbank.services;

import bank.backbank.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserSecurityService implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        bank.backbank.models.User user = userRepository.findByname(username);

        if (user == null) {
            throw new UsernameNotFoundException("User not found");
        }


        return User.builder()
                .username(user.getName())
                .password(user.getPassword())
                .authorities("ROLE_USER")
                .build();
    }
}
