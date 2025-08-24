package com.gea.app.helpRequest;

import com.gea.app.helpRequest.dto.HelpRequestCreateRequestDTO;
import com.gea.app.helpRequest.dto.HelpRequestResponseDTO;
import com.gea.app.helpRequest.entity.HelpRequest;
import com.gea.app.unit.UnitRepository;
import com.gea.app.unitType.UnitTypeRepository;
import com.gea.app.unitType.entity.UnitType;
import com.gea.app.user.UserRepository;
import com.gea.app.user.entity.User;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.messaging.simp.SimpMessagingTemplate;
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
    private final UnitTypeRepository unitTypeRepository;
    private final ModelMapper modelMapper;
    private final SimpMessagingTemplate messagingTemplate;

    @Autowired
    public HelpRequestService(HelpRequestRepository helpRequestRepository, UserRepository userRepository,
                              UnitTypeRepository unitTypeRepository, ModelMapper modelMapper,
                              SimpMessagingTemplate messagingTemplate) {
        this.helpRequestRepository = helpRequestRepository;
        this.userRepository = userRepository;
        this.unitTypeRepository = unitTypeRepository;
        this.modelMapper = modelMapper;
        this.messagingTemplate = messagingTemplate;
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

        User requester = (User) userRepository.findById(createRequestDTO.getRequesterId())
                .orElseThrow(() -> {
                    System.out.println(
                            "[ERROR] User dengan id " + createRequestDTO.getRequesterId() + " tidak ditemukan");
                    return new EntityNotFoundException(
                            "User with id " + createRequestDTO.getRequesterId() + " not found");
                });
        System.out.println("[LOG] User ditemukan: " + requester.getId());

        UnitType TargetUnitType = unitTypeRepository.findById(createRequestDTO.getTargetUnitTypeId())
                .orElseThrow(() -> {
                    System.out.println("[ERROR] Unit dengan id " + createRequestDTO.getTargetUnitTypeId() + " tidak ditemukan");
                    return new EntityNotFoundException("Unit with id " + createRequestDTO.getTargetUnitTypeId() + " not found");
                });
        System.out.println("[LOG] Unit ditemukan: " + TargetUnitType.getId());

        // Buat entitas baru
        HelpRequest newHelpRequest = HelpRequest.builder()
                .requester(requester)
                .targetUnitType(TargetUnitType)
                .details(createRequestDTO.getDetails())
                .status("OPEN") // Set status awal secara otomatis
                .build();
        System.out.println("[LOG] HelpRequest baru dibuat (belum disimpan)");

        HelpRequest savedRequest = helpRequestRepository.save(newHelpRequest);
        System.out.println("[LOG] HelpRequest berhasil disimpan dengan id: " + savedRequest.getId());

        HelpRequestResponseDTO responseDTO = modelMapper.map(savedRequest, HelpRequestResponseDTO.class);
        System.out.println("[LOG] Mapping ke HelpRequestResponseDTO selesai");

        try {
            var unitTypeId = savedRequest.getTargetUnitType().getId();
            String destination = "/topic/help-requests/" + unitTypeId;
            System.out.println("[WS] broadcasting to " + destination);
            // kirim payload DTO ke subscriber sesuai unit type
            messagingTemplate.convertAndSend(destination, responseDTO);
        } catch (Exception e) {
            System.out.println("[WS][WARN] gagal kirim notifikasi: " + e.getMessage());
        }

        return responseDTO;
    }

}
