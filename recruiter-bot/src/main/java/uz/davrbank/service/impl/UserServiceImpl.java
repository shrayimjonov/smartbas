package uz.davrbank.service.impl;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uz.davrbank.config.security.JwtUtils;
import uz.davrbank.dto.request.AuthRequest;
import uz.davrbank.dto.request.ChangePasswordDto;
import uz.davrbank.dto.request.RefreshRequest;
import uz.davrbank.dto.request.UserRequestDto;
import uz.davrbank.dto.response.DefaultSuccessResponse;
import uz.davrbank.dto.response.ResultData;
import uz.davrbank.dto.response.TokenResponse;
import uz.davrbank.dto.response.UserResponse;
import uz.davrbank.entity.Department;
import uz.davrbank.entity.FileUpload;
import uz.davrbank.entity.User;
import uz.davrbank.enums.ResponseStatus;
import uz.davrbank.exception.NotFoundException;
import uz.davrbank.mapper.UserMapper;
import uz.davrbank.repository.UserRepository;
import uz.davrbank.service.DepartmentService;
import uz.davrbank.service.FileUploadService;
import uz.davrbank.service.UserService;
import java.time.LocalDateTime;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final FileUploadService fileUploadService;
    private final DepartmentService departmentService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;
    private final UserDetailsService userDetailsService;
    private final UserMapper userMapper;

    @Override
    @Transactional
    public ResultData<UserResponse> createUser(@Valid UserRequestDto userRequestDto) {
        FileUpload fileUpload = fileUploadService.findById(userRequestDto.getFileId());
        Department department = departmentService.findDepartmentById(userRequestDto.getDepartmentId());
        String language = userRequestDto.getLanguage();
        if (!language.equals("ru") && !language.equals("uz")) {
            return new ResultData<>(false, ResponseStatus.FAILED, "Invalid language selected.");
        }
        User existingUser = userRepository.findTopByPhoneAndDepartmentOrderByCreatedAtDesc(userRequestDto.getPhone(), department);
        if (existingUser != null && existingUser.getCreatedAt().isAfter(LocalDateTime.now().minusMonths(6))) {
            return new ResultData<>(false, ResponseStatus.FAILED, "You can only submit a test to the same department once every 6 months.");
        }
        userRequestDto.setDepartmentId(department.getId());
        userRequestDto.setFileId(fileUpload.getId());
        User user = userMapper.toEntity(userRequestDto);
        user.setLanguage(language);
        User savedUser = userRepository.save(user);
        return new ResultData<>(userMapper.toDto(savedUser));
    }

    @Override
    public ResultData<TokenResponse> login(AuthRequest dto) {
        var authToken = new UsernamePasswordAuthenticationToken(dto.username(), dto.password());
        authenticationManager.authenticate(authToken);
        UserDetails ud = userDetailsService.loadUserByUsername(dto.username());
        var roles = ud.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());
        String accessToken = jwtUtils.generateToken(dto.username(), roles);
        String refreshToken = jwtUtils.generateRefreshToken(dto.username());
        return new ResultData<>(new TokenResponse(accessToken, refreshToken));
    }

    @Override
    public ResultData<TokenResponse> refreshToken(RefreshRequest dto) {
        String username = jwtUtils.getUsernameFromToken(dto.refreshToken(), true);
        UserDetails user = userDetailsService.loadUserByUsername(username);
        var roles = user.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList());
        String newAccess = jwtUtils.generateToken(username, roles);
        String newRefresh = jwtUtils.generateRefreshToken(username);
        return new ResultData<>(new TokenResponse(newAccess, newRefresh));
    }

    @Override
    @Transactional
    public ResultData<DefaultSuccessResponse> changePassword(ChangePasswordDto dto) {
        if (!dto.getNewPassword().equals(dto.getConfirmNewPassword())) {
            return new ResultData<>(false, ResponseStatus.FAILED, "New password and confirmation do not match");
        }
        if (dto.getNewPassword().equals(dto.getOldPassword())) {
            return new ResultData<>(false, ResponseStatus.FAILED, "New password must be different from old password");
        }
        User user = userRepository.findByUsername(dto.getUsername())
                .orElseThrow(() -> new NotFoundException("User not found"));
        if (!passwordEncoder.matches(dto.getOldPassword(), user.getPassword())) {
            return new ResultData<>(false, ResponseStatus.FAILED, "Old password is incorrect");
        }
        user.setPassword(passwordEncoder.encode(dto.getNewPassword()));
        return new ResultData<>(new DefaultSuccessResponse("Password changed successfully", LocalDateTime.now()));
    }
}
