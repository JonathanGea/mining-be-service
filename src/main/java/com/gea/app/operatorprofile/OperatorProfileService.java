package com.gea.app.operatorprofile;

import com.gea.app.operatorprofile.dto.OperatorProfileCreateRequestDTO;
import com.gea.app.operatorprofile.dto.OperatorProfileResponseDTO;
import com.gea.app.operatorprofile.entity.OperatorProfile;
import com.gea.app.unit.UnitRepository;
import com.gea.app.unit.entity.Unit;
import com.gea.app.user.UserRepository;
import com.gea.app.user.entity.User;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class OperatorProfileService {

    private final OperatorProfileRepository operatorProfileRepository;
    private final UserRepository userRepository;
    private final UnitRepository unitRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public OperatorProfileService(OperatorProfileRepository operatorProfileRepository, UserRepository userRepository, UnitRepository unitRepository, ModelMapper modelMapper) {
        this.operatorProfileRepository = operatorProfileRepository;
        this.userRepository = userRepository;
        this.unitRepository = unitRepository;
        this.modelMapper = modelMapper;
    }

    public List<OperatorProfileResponseDTO> getAllOperatorProfiles() {
        return operatorProfileRepository.findAll().stream()
                .map(profile -> modelMapper.map(profile, OperatorProfileResponseDTO.class))
                .collect(Collectors.toList());
    }

    public OperatorProfileResponseDTO getOperatorProfileById(UUID id) {
        return operatorProfileRepository.findById(id)
                .map(profile -> modelMapper.map(profile, OperatorProfileResponseDTO.class))
                .orElse(null);
    }

    @Transactional
    public OperatorProfileResponseDTO createOperatorProfile(OperatorProfileCreateRequestDTO createRequestDTO) {
        // 1. Cari entitas User
        User user = (User) userRepository.findById(createRequestDTO.getUserId())
                .orElseThrow(() -> new EntityNotFoundException("User with id " + createRequestDTO.getUserId() + " not found"));

        // 2. Cari entitas Unit
        Unit unit = unitRepository.findById(createRequestDTO.getUnitId())
                .orElseThrow(() -> new EntityNotFoundException("Unit with id " + createRequestDTO.getUnitId() + " not found"));

        // 3. Buat entitas OperatorProfile
        OperatorProfile profile = new OperatorProfile();
        profile.setUser(user);
        profile.setUnit(unit);

        // 4. Simpan ke database
        OperatorProfile savedProfile = operatorProfileRepository.save(profile);

        // 5. Map ke DTO untuk respons
        return modelMapper.map(savedProfile, OperatorProfileResponseDTO.class);
    }
}