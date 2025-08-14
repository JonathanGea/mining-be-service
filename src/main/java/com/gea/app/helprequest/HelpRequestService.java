package com.gea.app.helprequest;

import com.gea.app.helprequest.dto.HelpRequestCreateRequestDTO;
import com.gea.app.helprequest.dto.HelpRequestResponseDTO;
import com.gea.app.helprequest.entity.HelpRequest;
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
public class HelpRequestService {

    private final HelpRequestRepository helpRequestRepository;
    private final UserRepository userRepository;
    private final UnitRepository unitRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public HelpRequestService(HelpRequestRepository helpRequestRepository, UserRepository userRepository,
            UnitRepository unitRepository, ModelMapper modelMapper) {
        this.helpRequestRepository = helpRequestRepository;
        this.userRepository = userRepository;
        this.unitRepository = unitRepository;
        this.modelMapper = modelMapper;
    }

    public List<HelpRequestResponseDTO> getAllHelpRequests() {
        return helpRequestRepository.findAll().stream()
                .map(req -> modelMapper.map(req, HelpRequestResponseDTO.class))
                .collect(Collectors.toList());
    }

    public HelpRequestResponseDTO getHelpRequestById(UUID id) {
        return helpRequestRepository.findById(id)
                .map(req -> modelMapper.map(req, HelpRequestResponseDTO.class))
                .orElse(null);
    }

    @Transactional
    public HelpRequestResponseDTO createHelpRequest(HelpRequestCreateRequestDTO createRequestDTO) {
        System.out.println("[LOG] Mulai proses createHelpRequest untuk requesterId: "
                + createRequestDTO.getRequesterId() + ", unitId: " + createRequestDTO.getUnitId());

        // Validasi keberadaan user dan unit
        User requester = (User) userRepository.findById(createRequestDTO.getRequesterId())
                .orElseThrow(() -> {
                    System.out.println(
                            "[ERROR] User dengan id " + createRequestDTO.getRequesterId() + " tidak ditemukan");
                    return new EntityNotFoundException(
                            "User with id " + createRequestDTO.getRequesterId() + " not found");
                });
        System.out.println("[LOG] User ditemukan: " + requester.getId());

        Unit targetUnit = unitRepository.findById(createRequestDTO.getUnitId())
                .orElseThrow(() -> {
                    System.out.println("[ERROR] Unit dengan id " + createRequestDTO.getUnitId() + " tidak ditemukan");
                    return new EntityNotFoundException("Unit with id " + createRequestDTO.getUnitId() + " not found");
                });
        System.out.println("[LOG] Unit ditemukan: " + targetUnit.getId());

        // Buat entitas baru
        HelpRequest newHelpRequest = HelpRequest.builder()
                .requester(requester)
                .targetUnit(targetUnit)
                .details(createRequestDTO.getDetails())
                .status("OPEN") // Set status awal secara otomatis
                .build();
        System.out.println("[LOG] HelpRequest baru dibuat (belum disimpan)");

        HelpRequest savedRequest = helpRequestRepository.save(newHelpRequest);
        System.out.println("[LOG] HelpRequest berhasil disimpan dengan id: " + savedRequest.getId());

        HelpRequestResponseDTO responseDTO = modelMapper.map(savedRequest, HelpRequestResponseDTO.class);
        System.out.println("[LOG] Mapping ke HelpRequestResponseDTO selesai");

        return responseDTO;
    }

}