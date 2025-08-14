package com.gea.app.unit;

import com.gea.app.unit.dto.UnitCreateRequestDTO;
import com.gea.app.unit.dto.UnitResponseDTO;
import com.gea.app.unit.entity.Unit;
import com.gea.app.unitType.UnitTypeRepository;
import com.gea.app.unitType.entity.UnitType;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class UnitService {

    private final UnitRepository unitRepository;
    private final UnitTypeRepository unitTypeRepository; // Diperlukan untuk mencari UnitType
    private final ModelMapper modelMapper;

    @Autowired
    public UnitService(UnitRepository unitRepository, UnitTypeRepository unitTypeRepository, ModelMapper modelMapper) {
        this.unitRepository = unitRepository;
        this.unitTypeRepository = unitTypeRepository;
        this.modelMapper = modelMapper;
    }

    public List<UnitResponseDTO> getAllUnits() {
        return unitRepository.findAll().stream()
                .map(unit -> modelMapper.map(unit, UnitResponseDTO.class))
                .collect(Collectors.toList());
    }

    public UnitResponseDTO getUnitById(UUID id) {
        return unitRepository.findById(id)
                .map(unit -> modelMapper.map(unit, UnitResponseDTO.class))
                .orElse(null);
    }

    @Transactional
    public UnitResponseDTO createUnit(UnitCreateRequestDTO createRequestDTO) {
        // 1. Cari entitas UnitType berdasarkan ID yang diberikan
        UnitType unitType = unitTypeRepository.findById(createRequestDTO.getUnitTypeId())
                .orElseThrow(() -> new EntityNotFoundException("UnitType with id " + createRequestDTO.getUnitTypeId() + " not found"));

        // 2. Buat objek entitas Unit baru
        Unit unit = new Unit();
        unit.setName(createRequestDTO.getName());
        unit.setStatus(createRequestDTO.getStatus());
        unit.setUnitType(unitType); // Set relasi objeknya

        // 3. Simpan entitas Unit yang baru
        Unit savedUnit = unitRepository.save(unit);

        // 4. Map hasil yang sudah disimpan ke DTO untuk respons
        return modelMapper.map(savedUnit, UnitResponseDTO.class);
    }
}