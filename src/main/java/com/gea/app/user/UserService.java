package com.gea.app.user;

import com.gea.app.unit.UnitRepository;
import com.gea.app.user.entity.User;
import com.gea.app.user.dto.UserResponse;
import com.gea.app.unit.dto.UnitResponseDTO;
import com.gea.app.unit.entity.Unit;
import com.gea.app.unitType.dto.UnitTypeResponseDTO;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UnitRepository unitRepository;
    private final ModelMapper modelMapper;

    @Transactional(readOnly = true)
    public List<UserResponse> getUsers() {
        return userRepository.findAllWithUnit()
                .stream()
                .map(u -> modelMapper.map(u, UserResponse.class))
                .toList();
    }

    @Transactional(readOnly = true)
    public UserResponse getMe(User principal) {
        var u = userRepository.findByIdWithUnit(principal.getId())
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
        return modelMapper.map(u, UserResponse.class);
    }

    @Transactional
    public UserResponse assignUnitToUser(UUID userId, UUID unitIdOrNull) {
        var user = userRepository.findByIdWithUnit(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        // Lepaskan unit (unitId = null)
        if (unitIdOrNull == null) {
            user.setUnit(null);
            userRepository.save(user);
            return modelMapper.map(user, UserResponse.class);
        }

        // Pastikan unit ada
        var unit = unitRepository.findById(unitIdOrNull)
                .orElseThrow(() -> new EntityNotFoundException("Unit not found"));

        // Jika unit sudah dipakai user lain, lepas dari user itu (jaga 1–1)
//        userRepository.findById(unitIdOrNull).ifPresent(existing -> {
//            if (!existing.getId().equals(userId)) {
//                existing.setUnit(null);
//                userRepository.save(existing);
//            }
//        });

        user.setUnit(unit);
        userRepository.save(user);

        return modelMapper.map(user, UserResponse.class);
    }
}