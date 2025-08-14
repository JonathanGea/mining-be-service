package com.gea.app.unitType;

import com.gea.app.unitType.dto.UnitTypeCreateRequestDTO;
import com.gea.app.unitType.dto.UnitTypeResponseDTO;
import com.gea.app.unitType.entity.UnitType;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class UnitTypeService {

    private final UnitTypeRepository unitTypeRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public UnitTypeService(UnitTypeRepository unitTypeRepository, ModelMapper modelMapper) {
        this.unitTypeRepository = unitTypeRepository;
        this.modelMapper = modelMapper;
    }

    /**
     * Mengambil semua UnitType dari database.
     * @return List dari UnitTypeResponseDTO.
     */
    public List<UnitTypeResponseDTO> getAllUnitTypes() {
        List<UnitType> unitTypes = unitTypeRepository.findAll();
        // Map setiap entitas UnitType ke UnitTypeResponseDTO
        return unitTypes.stream()
                .map(unitType -> modelMapper.map(unitType, UnitTypeResponseDTO.class))
                .collect(Collectors.toList());
    }

    /**
     * Mengambil satu UnitType berdasarkan ID.
     * @param id UUID dari UnitType.
     * @return UnitTypeResponseDTO jika ditemukan, jika tidak maka null.
     */
    public UnitTypeResponseDTO getUnitTypeById(UUID id) {
        return unitTypeRepository.findById(id)
                .map(unitType -> modelMapper.map(unitType, UnitTypeResponseDTO.class))
                .orElse(null); // Sesuai pola di contoh, kembalikan null jika tidak ditemukan
    }

    /**
     * Membuat UnitType baru.
     * @param createRequestDTO DTO yang berisi data untuk UnitType baru.
     * @return UnitTypeResponseDTO dari data yang baru saja disimpan.
     */
    @Transactional
    public UnitTypeResponseDTO createUnitType(UnitTypeCreateRequestDTO createRequestDTO) {
        // Map DTO ke Entitas
        UnitType unitType = modelMapper.map(createRequestDTO, UnitType.class);

        // Simpan entitas ke database
        UnitType savedUnitType = unitTypeRepository.save(unitType);

        // Map entitas yang sudah tersimpan (dengan ID) kembali ke DTO untuk respons
        return modelMapper.map(savedUnitType, UnitTypeResponseDTO.class);
    }
}