package com.example.electronic_queue_monolit.service.impl;

import com.example.electronic_queue_monolit.domain.dto.PlaceDto;
import com.example.electronic_queue_monolit.domain.dto.RoleDto;
import com.example.electronic_queue_monolit.domain.dto.UserDto;
import com.example.electronic_queue_monolit.domain.model.Place;
import com.example.electronic_queue_monolit.domain.model.Role;
import com.example.electronic_queue_monolit.domain.model.User;
import com.example.electronic_queue_monolit.repository.RoleRepository;
import com.example.electronic_queue_monolit.repository.UserRepository;
import com.example.electronic_queue_monolit.service.UserService;
import com.example.electronic_queue_monolit.service.base.BaseServiceImpl;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl extends BaseServiceImpl<User, UserDto, UserRepository> implements UserService {
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;

    public UserServiceImpl(UserRepository repo, PasswordEncoder passwordEncoder, RoleRepository roleRepository) {
        super(repo);
        this.passwordEncoder = passwordEncoder;
        this.roleRepository = roleRepository;
    }

    @Override
    public User toEntity(UserDto dto) {
        User user = new User();
        user.setUsername(dto.getUsername());
        user.setName(dto.getName());
        user.setSurname(dto.getSurname());
        user.setPatronymic(dto.getPatronymic());
        user.setPassword(dto.getPassword());
        user.setBlocked(dto.getBlocked());
        user.setPlace(createEntityIfNotNull(
                dto.getPlace() != null ? dto.getPlace().getId() : null, id -> {
                    Place place = new Place();
                    place.setId(id);
                    return place;
                }
        ));
        user.setRole(createEntityIfNotNull(
                dto.getRole() != null ? dto.getRole().getId() : null, id -> {
                    Role role = new Role();
                    role.setId(id);
                    return role;
                }
        ));

        return user;
    }

    private <T> T createEntityIfNotNull(Long id, java.util.function.Function<Long, T> constructor) {
        return id != null ? constructor.apply(id) : null;
    }

    @Override
    public UserDto toDTO(User entity) {
        UserDto dto = new UserDto();
        dto.setId(entity.getId());
        dto.setUsername(entity.getUsername());
        dto.setName(entity.getName());
        dto.setSurname(entity.getSurname());
        dto.setPatronymic(entity.getPatronymic());
        dto.setPassword(entity.getPassword());
        dto.setBlocked(entity.getBlocked());
        
        if (entity.getPlace() != null) {
            PlaceDto placeDto = new PlaceDto();
            placeDto.setId(entity.getPlace().getId());
            placeDto.setName(entity.getPlace().getName());
            dto.setPlace(placeDto);
        }
        
        if (entity.getRole() != null) {
            RoleDto roleDto = new RoleDto();
            roleDto.setId(entity.getRole().getId());
            roleDto.setName(entity.getRole().getName());
            dto.setRole(roleDto);
        }
        
        return dto;
    }

    @Override
    public UserDto update(Long id, UserDto dto) {
        User existingUser = repo.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));

        existingUser.setUsername(dto.getUsername());
        existingUser.setName(dto.getName());
        existingUser.setSurname(dto.getSurname());
        existingUser.setPatronymic(dto.getPatronymic());
        existingUser.setBlocked(dto.getBlocked());

        if (dto.getPassword() != null && !dto.getPassword().isEmpty()) {
            existingUser.setPassword(dto.getPassword());
        }

        existingUser.setPlace(createEntityIfNotNull(
                dto.getPlace() != null ? dto.getPlace().getId() : null, placeId -> {
                    Place place = new Place();
                    place.setId(placeId);
                    return place;
                }
        ));

        existingUser.setRole(createEntityIfNotNull(
                dto.getRole() != null ? dto.getRole().getId() : null, roleId -> {
                    Role role = new Role();
                    role.setId(roleId);
                    return role;
                }
        ));

        return toDTO(repo.save(existingUser));
    }

}
