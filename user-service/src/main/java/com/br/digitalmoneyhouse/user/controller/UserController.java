package com.br.digitalmoneyhouse.user.controller;

import com.br.digitalmoneyhouse.core.security.JwtTokenUtil;
import com.br.digitalmoneyhouse.core.security.UserPrincipal;
import com.br.digitalmoneyhouse.token.model.RefreshTokenEntity;
import com.br.digitalmoneyhouse.token.service.RefreshTokenServiceImpl;
import com.br.digitalmoneyhouse.user.dto.*;
import com.br.digitalmoneyhouse.user.model.UserEntity;
import com.br.digitalmoneyhouse.user.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

import static com.br.digitalmoneyhouse.core.utils.ConstantsUtils.ADMIN_ROLE;
import static com.br.digitalmoneyhouse.core.utils.ConstantsUtils.CLIENT_ROLE;

/**
 * The type User controller.
 */
@RestController
@RequestMapping("/api/v1/user")
@Log4j2
public class UserController {

    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private UserService userService;
    @Autowired
    private JwtTokenUtil jwtTokenUtil;
    @Autowired
    private RefreshTokenServiceImpl refreshTokenService;

    /**
     * Login response entity.
     *
     * @param request the request
     * @param userDTO the user dto
     * @return the response entity
     */
    @PostMapping("/login")
    public ResponseEntity<?> login(HttpServletRequest request, @RequestBody UserDTO userDTO) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            userDTO.getUsername(),  userDTO.getPassword())
            );
            UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
            Map<String, String> tokens = jwtTokenUtil.genarateTokens(request, userPrincipal.getUser(), null);
            return ResponseEntity.ok().body(tokens);
        } catch (Exception ex) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.FORBIDDEN);
        }
    }

    /**
     * Save.
     *
     * @param userDTO the user dto
     */
    @PostMapping
    public ResponseEntity<UserResponse> saveUser(@RequestBody @Valid UserDTO userDTO){
        return new ResponseEntity<>(userService.save(userDTO, CLIENT_ROLE), HttpStatus.CREATED);
    }

    /**
     * Save.
     *
     * @param userDTO the user dto
     */
    @PostMapping("/admin")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserResponse> saveAdmin(@RequestBody @Valid UserDTO userDTO){
        return new ResponseEntity<>(userService.save(userDTO, ADMIN_ROLE), HttpStatus.CREATED);
    }

    @PostMapping("/recoveruser")
    public ResponseEntity<String> recoverUser(@RequestBody RecoverDTO recover) {
        try {
            userService.recoverUser(recover);
            return ResponseEntity.status(HttpStatus.OK).body("Email foi enviado corretamente");
        } catch(Error err) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Falha ao enviar o email");
        }
    }

    @PostMapping("/recoveruser/{id}")
    public ResponseEntity<String> codeRecoverUser(@RequestBody CodeRecoverDTO code, @PathVariable String id) {
        if (!code.getPassword().equals(code.getConfirmPassword())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Senhas não estão iguais.");
        }
        try {
            Long l = Long.parseLong(id);
            userService.codeRecoverUser(code, l);
            return ResponseEntity.status(HttpStatus.OK).body("Senha alterada com sucesso");
        } catch(Error err) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Falha ao mudar a senha");
        }
    }

    @GetMapping("/confirmEmail/{code}")
    public ResponseEntity<String> confirmEmail(@PathVariable String code) {
        Boolean response = userService.confirmEmail(code);
        if (response == true) {
            return ResponseEntity.status(200).body("Confirmado!");
        }
        return ResponseEntity.status(404).body("Código inválido!");

    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDetailResponse> describeUser(@PathVariable Long id) {
        return ResponseEntity.ok(userService.describeUser(id));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<UserDetailResponse> updateUser(@PathVariable Long id, @RequestBody UserDTOUpdate userDTO) {
        return ResponseEntity.accepted().body(userService.updateUser(id, userDTO));
    }

    @GetMapping("/refresh/token")
    public ResponseEntity<?> renewAccessToken(HttpServletRequest request) {
        String header = request.getHeader("Authorization");
        if (!ObjectUtils.isEmpty(header) || header.startsWith("Bearer")) {
            String refreshToken = header.split(" ")[1].trim();

            try {
                RefreshTokenEntity dataToken = refreshTokenService.getRefreshToken(refreshToken);
                UserEntity user = userService.findById(dataToken.getUserId());
                Map<String, String> tokens = jwtTokenUtil.genarateTokens(request, user, refreshToken);

                return ResponseEntity.ok().body(tokens);

            } catch (Exception ex) {
                return new ResponseEntity<>(ex.getMessage(), HttpStatus.FORBIDDEN);
            }
        }
        return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }

    @DeleteMapping("/logout")
    public ResponseEntity<?> logout(HttpServletRequest request) {
        String header = request.getHeader("Authorization");
        if (!ObjectUtils.isEmpty(header) || header.startsWith("Bearer")) {
            String refreshToken = header.split(" ")[1].trim();

            try {
                refreshTokenService.getRefreshToken(refreshToken);
                refreshTokenService.deleteToken(refreshToken);

                return ResponseEntity.noContent().build();

            } catch (Exception ex) {
                return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
            }
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @GetMapping("/ping")
    public String ping(){
        return "pong";
    }

}
