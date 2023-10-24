package com.ingsoftware.api.services;



import com.ingsoftware.api.DTO.*;
import com.ingsoftware.api.exceptions.JwtResponseException;
import com.ingsoftware.api.exceptions.RolDoesntExistException;
import com.ingsoftware.api.exceptions.UserException;
import com.ingsoftware.api.mappers.RolMapper;
import com.ingsoftware.api.mappers.UserMapper;
import com.ingsoftware.api.models.Rol;
import com.ingsoftware.api.models.User;
import com.ingsoftware.api.repository.IRolRepository;
import com.ingsoftware.api.repository.IUserRepository;
import com.ingsoftware.api.security.TokenUtils;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.sql.Timestamp;
import java.text.MessageFormat;
import java.util.*;


@Service
public class AuthenticationService {
    private IUserRepository _userRepository;
    private IRolRepository _rolRepository;
    private UserMapper _userMapper;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final TokenUtils _tokenUtils;

    public AuthenticationService(IUserRepository _userRepository, IRolRepository _rolRepository, UserMapper _userMapper,PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager, TokenUtils tokenUtils) {
        this._userRepository = _userRepository;
        this._rolRepository = _rolRepository;
        this._userMapper = _userMapper;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this._tokenUtils = tokenUtils;
    }




    @Value("${app.jwt.refresh_token_validity_in_minutes}")
    private long REFRESH_TOKEN_VALIDITY_SECOND;
    @Value("${app.jwt.access_token_validity_in_minutes}")
    public long ACCESS_TOKEN_VALIDITY_SECOND;



    public boolean DoSignUp(SignUpPayload payload) throws UserException, RolDoesntExistException {
        //CReo modelo del signup payload, se valida si existe en la bbdd el correo, en caso de que exista se lanza una excepcion
        //Sino, se settea el rol al usuario y se guarda en base de datos
        var user = new User();
        // var test = new SignUpMessages();
        user.setEmail(payload.getEmail());
        user.setFirstname(payload.getFirstName());
        user.setMiddlename(payload.getMiddleName());
        user.setLastname(payload.getLastName());
        user.setPhoneNumber(payload.getPhoneNumber());
        user.setActive(true);
        user.setLockout(payload.isLockout());
        user.setPassword(passwordEncoder.encode(payload.getPassword()));


        /* ToDo
         [ ] Add role validation
        */
        User userDB = null;
        try {
            userDB = _userRepository.findByEmail(user.getEmail()).orElseThrow();
        } catch (Exception e) {
        }
        if (userDB != null) throw new UserException("User already exist.");
        var rol = _rolRepository.findById(payload.getRol()).orElseThrow(()-> new RolDoesntExistException("Rol doesn't exist."));
        user.setRol(rol);

        _userRepository.save(user);
        return true;
    }

    public GenericResponse DoLogIn(LoginPayload payload) {
        User user = null;
        try {
            user = (User) authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(payload.getEmail(), payload.getPassword())).getPrincipal();
        } catch (BadCredentialsException badCredentialsException) {
            return GenericResponse.builder().data("Bad credentials").build();
        }

        var accessToken = _tokenUtils.createToken(user);
        var refreshToken = _tokenUtils.createRefreshToken();

        LoginResponseDTO response = LoginResponseDTO.builder()
                .authToken(accessToken)
                .refreshToken(refreshToken)
                .build();
        user.setRefreshToken(refreshToken);
        user.setRefreshTokenExpirationDate(new Timestamp(System.currentTimeMillis() + (REFRESH_TOKEN_VALIDITY_SECOND * 60 * 1000)));

        _userRepository.save(user);

        return GenericResponse.builder().data(response).error(null).build();
    }

    public boolean ValidateToken(String token, String refreshToken) throws JwtResponseException {
        try {
            return this._tokenUtils.validateJwtToken(token.replace("Bearer ", ""));
        } catch (JwtResponseException e) {
            throw new JwtResponseException(e.getMessage());
        }
    }


    public GenericResponse RefreshToken(HttpServletRequest request) throws JwtResponseException, IOException {
        String token = request.getHeader("Authorization").replace("Bearer ", "");
        String refreshToken = request.getHeader("RefreshToken");

        try {
            var isValid = _tokenUtils.validateJwtToken(token);
            LoginResponseDTO respones = LoginResponseDTO.builder().authToken(token).refreshToken(refreshToken).build();
            return GenericResponse.builder().data(respones).error(null).build();
        } catch (ExpiredJwtException e) {
            Claims claims = e.getClaims();
            var user = _userRepository.findByEmail(claims.get("email").toString()).orElseThrow();
            if (user.getRefreshToken().equals(refreshToken)) {
                if (user.getRefreshTokenExpirationDate().after(new java.util.Date(System.currentTimeMillis()))) {
                    String newToken = _tokenUtils.refreshToken(parteClaimsToMap(claims), claims.getSubject());
                    String newRefreshToken = _tokenUtils.createRefreshToken();
                    user.setRefreshToken(newRefreshToken);
                    user.setRefreshTokenExpirationDate(new Timestamp(System.currentTimeMillis() + (REFRESH_TOKEN_VALIDITY_SECOND * 60 * 1000)));
                    _userRepository.save(user);

                    LoginResponseDTO loginResponseDTO = LoginResponseDTO.builder()
                            .refreshToken(newRefreshToken)
                            .authToken(newToken)
                            .build();
                    return GenericResponse.builder().data(loginResponseDTO).error(null).build();
                }
                throw new JwtResponseException("Refresh token doesn't match");
            }
            throw new JwtResponseException("Refresh token expired");
        } catch (JwtResponseException e) {
            throw e;
        }
    }

/*    public GenericResponse changePasswordStepTwo(ChangePasswordDTO payload) {
        User user = _userRepository.findByEmail(payload.getEmail()).orElse(null);
        if (user == null) return GenericResponse.builder().data(false).error(null).build();

        if (user.getTokenChangePassword().equals(payload.getToken())) {
            return GenericResponse.builder().data(true).error(null).build();
        }
        return GenericResponse.builder().data(false).error(null).build();
    }*/

   /* public GenericResponse changePasswordStepOne(ChangePasswordDTO payload) {
        User user = _userRepository.findByEmail(payload.getEmail()).orElse(null);
        if (user == null) return GenericResponse.builder().data(false).error(null).build();
        String token = GenerateTokenPassword();

        TwillioRequestDTO twillioRequestDTO = TwillioRequestDTO.builder()
                .phoneNumber(user.getPhoneNumber())
                .body(wspMessage.replace("[[token]]", token))
                .build();
        _ambassadorClient.sendMessage(twillioRequestDTO);
        user.setTokenChangePassword(token);
        _userRepository.save(user);
        return GenericResponse.builder().data(true).error(null).build();
    }

    public GenericResponse changePasswordStepThree(ChangePasswordDTO payload) {
        User user = _userRepository.findByEmail(payload.getEmail()).orElse(null);
        if (user == null) return GenericResponse.builder().data(false).error(null).build();
        if (payload.getToken().equals(user.getTokenChangePassword())) {

            user.setPassword(passwordEncoder.encode(payload.getNewPassword()));
            _userRepository.save(user);
        }
        return GenericResponse.builder().data(true).error(null).build();
    }*/

    private String GenerateTokenPassword() {
        return UUID.randomUUID().toString().replace("-", "").substring(0, 6);
    }


    private Map<String, Object> parteClaimsToMap(Claims claims) {
        Map<String, Object> result = new HashMap<>(claims);
        result.remove("exp");
        result.remove("iat");
        return result;
    }

    public GenericResponse getUserInfo(int userId) throws UserException {
        User user = _userRepository.findById(userId).orElseThrow(() -> new UserException("User not found"));

        UserDTO userDTO = _userMapper.createDTO(user);

        return GenericResponse
                .builder()
                .data(userDTO)
                .error(null)
                .build();

    }
/*    public GenericResponse getUserInfoMassive(List<Integer> userId) throws UserException {
        List<User> users = _userRepository.findAllByIdIn(userId).orElseThrow(() -> new UserException("Users not found"));
        List<UserDTO> userDTOS = new ArrayList<>();

        for (User user : users) {
            UserDTO userDTO = _userMapper.createDTO(user);
            userDTOS.add(userDTO);
        }

        AssignUserCommunityDTO assignUserCommunityDTO = AssignUserCommunityDTO.builder()
                .users(userDTOS)
                .build();

        return GenericResponse
                .builder()
                .data(assignUserCommunityDTO)
                .error(null)
                .build();

    }*/

    public GenericResponse getAllUsers() {

        List<UserDTO> userDTOS = new ArrayList<>();
        List<User> users = this._userRepository.findAll();


        for (User user : users) {
            UserDTO userDTO = _userMapper.createDTO(user);
            userDTOS.add(userDTO);
        }

        return GenericResponse.builder().data(userDTOS).error(null).build();
    }

    public GenericResponse getAllRoles() {

        List<RolDTO> rolDTOS = new ArrayList<>();
        List<Rol> rols = this._rolRepository.findAll();

        for (Rol rol : rols) {
            RolDTO rolDTO = RolMapper.createDTO(rol);
            rolDTOS.add(rolDTO);
        }
        return GenericResponse.builder().data(rolDTOS).error(null).build();
    }

    public GenericResponse updateUser(UserDTO userDTO) {
        GenericResponse response;
        try {
            User user = _userRepository.findById(userDTO.getUserId()).orElse(null);
            if (user == null)
                throw new UserException(MessageFormat.format("User {0} with Email {1} doesn't exist.", userDTO.getFirstName(), userDTO.getEmail()));

            user.setFirstname(userDTO.getFirstName());
            user.setMiddlename(userDTO.getMiddleName());
            user.setLastname(userDTO.getLastName());
            user.setEmail(userDTO.getEmail());
            user.setPhoneNumber(userDTO.getPhoneNumber());
            user.setLockout(userDTO.isLockout());
            user.setActive(userDTO.isActive());
            user.setRol(Rol.builder().name(userDTO.getRol().getName()).id(userDTO.getRol().getId()).build());

            user = _userRepository.save(user);
            UserDTO userDTOResponse = _userMapper.createDTO(user);
            response = GenericResponse.builder()
                    .data(userDTOResponse)
                    .error(null)
                    .build();
        } catch (UserException e) {
            response = GenericResponse.builder()
                    .data(null)
                    .error(e.getMessage())
                    .build();
        }
        return response;
    }

    public GenericResponse getByEmail(String email) throws UserException {
        User user = _userRepository.findByEmail(email).orElseThrow(() ->new UserException("Email not found"));

        UserDTO userDTO = _userMapper.createDTO(user);
        return GenericResponse
                .builder()
                .data(userDTO)
                .error(null)
                .build();
    }

}
