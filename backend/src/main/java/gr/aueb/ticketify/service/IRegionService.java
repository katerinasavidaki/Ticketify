package gr.aueb.ticketify.service;

import gr.aueb.ticketify.dto.RegionDTO;
import gr.aueb.ticketify.model.static_data.Region;

import java.util.List;

public interface IRegionService {
    List<RegionDTO> getAllRegions();
}
