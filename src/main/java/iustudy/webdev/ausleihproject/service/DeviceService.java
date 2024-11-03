package iustudy.webdev.ausleihproject.service;

import iustudy.webdev.ausleihproject.data.DeviceRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DeviceService {

    final DeviceRepository deviceRepository;
    public DeviceService(DeviceRepository bookingRepository) {
        this.deviceRepository = bookingRepository;
    }

}
