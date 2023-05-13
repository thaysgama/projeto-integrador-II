package com.br.digitalmoneyhouse.user.service;

import com.br.digitalmoneyhouse.core.exceptions.NotFoundException;
import com.br.digitalmoneyhouse.core.exceptions.NotUniqueException;
import com.br.digitalmoneyhouse.core.mapper.UserMapper;
import com.br.digitalmoneyhouse.core.role.model.RoleEntity;
import com.br.digitalmoneyhouse.core.role.service.RoleService;
import com.br.digitalmoneyhouse.core.security.UserPrincipal;
import com.br.digitalmoneyhouse.user.dto.*;
import com.br.digitalmoneyhouse.user.model.UserEntity;
import com.br.digitalmoneyhouse.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Optional;
import java.util.Random;
import java.util.Set;
import java.util.UUID;

/**
 * The type User service.
 */
@Log4j2
@Service
@RequiredArgsConstructor
//@FeignClient(name="user-service")
public class UserServiceImpl implements UserDetailsService, UserService {

    private final UserRepository userRepository;
    private final RoleService roleService;
    private final PasswordEncoder encoder;

    private final UserMapper userMapper;

    @Value("${email.from}")
    private String emailFrom;

    @Autowired
    private JavaMailSender emailSender;

    /**
     * If the user is found, return a UserPrincipal object, otherwise throw a UsernameNotFoundException
     *
     * @param username The username of the user to load.
     * @return UserPrincipal
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<UserEntity> user = userRepository.findByUsername(username);
        if (user.isEmpty()) {
            log.error("User not found [" + username + "].");
            throw new UsernameNotFoundException("User [" + username + "] not found.");
        } else {
            log.info("Loading user [" + username + "].");
            return new UserPrincipal(user.get());
        }

    }

    /**
     * > This function checks if a user exists in the database by their username
     *
     * @param username The username of the user to check for.
     * @return A boolean value.
     */
    @Override
    public Boolean existsByUsername(String username){
        return userRepository.existsByUsername(username);
    }

    /**
     * > It saves a user to the database
     *
     * @param userDTO The user object that we want to save.
     * @return UserDTO
     */
    @Override
    public UserResponse save(UserDTO userDTO, String roleName) {
        RoleEntity role = roleService.findByName(roleName);
        Boolean existsByUsername = userRepository.existsByUsername(userDTO.getUsername());
        if(existsByUsername){
            log.error("Email already taken.");
            throw new NotUniqueException("Email already taken.");
        }
        userDTO.setPassword(encoder.encode(userDTO.getPassword()));
        UserEntity user = new UserEntity(userDTO, Set.of(role));
        log.info("Saving user [" + user.getUsername() + "].");
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(this.emailFrom);
        message.setTo(userDTO.getUsername());
        message.setSubject("Confirme seu email!");
        UUID randomUUID = UUID.randomUUID();
        String randomDigit =  randomUUID.toString().replaceAll("_", "");
        user.setCodeEmailVerify(randomDigit);
        message.setText("Para confirmar seu email, clique nesse link logo abaixo" +
                "\n Link: http://localhost:8080/api/v1/user/confirmEmail/" + randomDigit);
        emailSender.send(message);
        userRepository.save(user);
        return new UserResponse(user);
    }

    @Override
    public Boolean confirmEmail(String code) {
        Optional<UserEntity> user = userRepository.findByCodeEmailVerify(code);
        if (user.isEmpty() || user.get().getActiveEmail()) {
            return false;
        }
        user.get().setCodeEmailVerify(null);
        user.get().setActiveEmail(true);
        userRepository.save(user.get());
        return true;
    }

    @Override
    public void recoverUser(RecoverDTO username) {
        Optional<UserEntity> opUser = userRepository.findByUsername(username.getUsername());
        opUser.get().setDateExpireCodeRecover(new Timestamp(System.currentTimeMillis()).getTime());
        Random rnd = new Random();
        Integer digit = 100000 + rnd.nextInt(900000);
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(this.emailFrom);
            message.setTo(opUser.get().getUsername());
            message.setSubject("Recuperação da conta");
            message.setText("Código: " + digit + "\n Link para recuperação: http://localhost:8080/api/v1/user/recoveruser/" + opUser.get().getId());
            opUser.get().setCodeRecoverPassword(digit.toString());
            emailSender.send(message);
            this.userRepository.save(opUser.get());
        } catch(Error err) {
            throw err;
        }
    }

    @Override
    public void codeRecoverUser(CodeRecoverDTO code, Long id) {
        Optional<UserEntity> opUser = userRepository.findById(id);
        Long actualTime = new Timestamp(System.currentTimeMillis()).getTime();
        System.out.println(actualTime - opUser.get().getDateExpireCodeRecover());
        if (actualTime - opUser.get().getDateExpireCodeRecover() <= 360000) {
            if (opUser.get().getCodeRecoverPassword().equals(code.getCode())) {
                opUser.get().setPassword(code.getPassword());
                this.userRepository.save(opUser.get());
            } else {
                System.out.println("Código incorreto");
            }
        } else {
            System.out.println("Tempo excedido");
        }
    }

    @Override
    public UserDetailResponse describeUser(Long id) {
        UserEntity user = userRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("User not found [" + id + "].");
                    throw new UsernameNotFoundException("User [" + id + "] not found.");
                });
        return userMapper.fromUserEntitytoUserDetailResponse(user);
    }

    @Override
    public UserDetailResponse updateUser(Long id, UserDTOUpdate userDTO) {
        UserEntity userEntity = userRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("User not found [" + id + "].");
                    throw new UsernameNotFoundException("User [" + id + "] not found.");
                });
        userEntity.setFirstName(userDTO.getFirstName());
        userEntity.setSurname(userDTO.getSurname());
        userEntity.setPhoneNumber(userDTO.getPhoneNumber());
        userEntity.setUsername(userDTO.getUsername());
        userEntity.setPassword(encoder.encode(userDTO.getPassword()));
        return userMapper.fromUserEntitytoUserDetailResponse(userRepository.save(userEntity));
    }

    @Override
    public UserEntity findById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("User not found [" + id + "].");
                    throw new NotFoundException("User [" + id + "] not found.");
                });
    }
}


