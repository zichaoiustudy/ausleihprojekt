package iustudy.webdev.ausleihproject.service;

import iustudy.webdev.ausleihproject.data.Device;
import iustudy.webdev.ausleihproject.data.DeviceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DeviceService {

    final DeviceRepository deviceRepository;

    @Autowired
    public DeviceService(DeviceRepository bookingRepository) {
        this.deviceRepository = bookingRepository;
    }

    // find & search
    public List<Device> findAllDevices() { return deviceRepository.findAll(); }

    public Device findDevice(long id) { return deviceRepository.findById(id).orElse(null); }

    public List<Device> searchDevices(String searchTerm) {
        if (searchTerm == null || searchTerm.isEmpty() || searchTerm.isBlank())
            return findAllDevices();
        else return deviceRepository.search(searchTerm);
    }

    // save
    public void saveDevice(Device device) {
        if (device == null) {
            System.err.println("Device is null!");
            return;
        }
        deviceRepository.save(device);
    }

    // delete
    public void deleteDevice(long id) { deviceRepository.deleteById(id); }
}
