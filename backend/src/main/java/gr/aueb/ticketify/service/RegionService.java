package gr.aueb.ticketify.service;

import gr.aueb.ticketify.dto.RegionDTO;
import gr.aueb.ticketify.model.static_data.Region;
import gr.aueb.ticketify.repository.RegionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RegionService implements IRegionService {

    private final RegionRepository regionRepository;

    @Override
    @Transactional(readOnly = true)
    public List<RegionDTO> getAllRegions() {
        return regionRepository.findAll().stream()
                .map(region -> new RegionDTO(region.getId(), region.getName()))
                .collect(Collectors.toList());
    }
}
