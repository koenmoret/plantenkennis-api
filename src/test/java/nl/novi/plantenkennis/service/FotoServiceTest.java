package nl.novi.plantenkennis.service;

import nl.novi.plantenkennis.entity.Foto;
import nl.novi.plantenkennis.entity.PlantSoort;
import nl.novi.plantenkennis.exception.ResourceNotFoundException;
import nl.novi.plantenkennis.repository.FotoRepository;
import nl.novi.plantenkennis.repository.PlantSoortRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FotoServiceTest {

    @Mock FotoRepository fotoRepository;
    @Mock PlantSoortRepository plantSoortRepository;
    @Mock FotoStorageService storageService;

    @InjectMocks FotoService service;

    @DisplayName("getByPlantSoort() retourneert alle foto's voor een plantsoort")
    @Test
    void getByPlantSoort_returnsList() {
        Foto f1 = Foto.builder().id(1L).url("/uploads/a.jpg").build();
        Foto f2 = Foto.builder().id(2L).url("/uploads/b.jpg").build();

        when(fotoRepository.findByPlantSoortId(10L)).thenReturn(List.of(f1, f2));

        List<Foto> result = service.getByPlantSoort(10L);

        assertEquals(2, result.size());
        verify(fotoRepository).findByPlantSoortId(10L);
        verifyNoMoreInteractions(fotoRepository, plantSoortRepository, storageService);
    }

    @DisplayName("uploadToPlantSoort() slaat foto + metadata op en gebruikt storageService")
    @Test
    void uploadToPlantSoort_savesFoto_andStoresFile() {
        Long plantId = 10L;
        PlantSoort plant = PlantSoort.builder()
                .id(plantId)
                .nederlandseNaam("Lavendel")
                .wetenschappelijkeNaam("Lavandula angustifolia")
                .build();

        when(plantSoortRepository.findById(plantId)).thenReturn(Optional.of(plant));
        when(fotoRepository.findFirstByPlantSoortIdAndHoofdfotoTrue(plantId)).thenReturn(Optional.empty());

        MockMultipartFile file = new MockMultipartFile(
                "file", "test.jpg", "image/jpeg", new byte[]{1, 2, 3}
        );

        var stored = new FotoStorageService.StoredFile(
                "plantsoorten/10/abc.jpg",
                "test.jpg",
                "image/jpeg",
                3L,
                "/uploads/plantsoorten/10/abc.jpg"
        );

        when(storageService.store(eq(plantId), any())).thenReturn(stored);

        when(fotoRepository.save(any(Foto.class))).thenAnswer(inv -> {
            Foto arg = inv.getArgument(0);
            arg.setId(99L);
            return arg;
        });

        Foto result = service.uploadToPlantSoort(
                plantId, file, "alt", true, "Koen", "CC-BY", "seed"
        );

        assertNotNull(result.getId());
        assertEquals("/uploads/plantsoorten/10/abc.jpg", result.getUrl());
        assertEquals("plantsoorten/10/abc.jpg", result.getStoragePath());
        assertEquals("test.jpg", result.getOriginalFilename());
        assertEquals("image/jpeg", result.getContentType());
        assertEquals(3L, result.getFileSize());
        assertTrue(result.isHoofdfoto());
        assertEquals(plantId, result.getPlantSoort().getId());

        verify(plantSoortRepository).findById(plantId);
        verify(storageService).store(eq(plantId), any());
        verify(fotoRepository).findFirstByPlantSoortIdAndHoofdfotoTrue(plantId);
        verify(fotoRepository).save(any(Foto.class));
        verifyNoMoreInteractions(fotoRepository, plantSoortRepository, storageService);
    }

    @DisplayName("uploadToPlantSoort() zet bestaande hoofdfoto uit als nieuwe hoofdfoto geupload wordt")
    @Test
    void uploadToPlantSoort_unsetsExistingHoofdfoto() {
        Long plantId = 10L;
        PlantSoort plant = PlantSoort.builder()
                .id(plantId)
                .nederlandseNaam("Lavendel")
                .wetenschappelijkeNaam("Lavandula angustifolia")
                .build();

        when(plantSoortRepository.findById(plantId)).thenReturn(Optional.of(plant));

        Foto bestaande = Foto.builder()
                .id(1L)
                .hoofdfoto(true)
                .plantSoort(plant)
                .build();

        when(fotoRepository.findFirstByPlantSoortIdAndHoofdfotoTrue(plantId))
                .thenReturn(Optional.of(bestaande));

        MockMultipartFile file = new MockMultipartFile(
                "file", "test.jpg", "image/jpeg", new byte[]{1}
        );

        when(storageService.store(eq(plantId), any())).thenReturn(
                new FotoStorageService.StoredFile(
                        "plantsoorten/10/new.jpg",
                        "test.jpg",
                        "image/jpeg",
                        1L,
                        "/uploads/plantsoorten/10/new.jpg"
                )
        );

        when(fotoRepository.save(any(Foto.class))).thenAnswer(inv -> inv.getArgument(0));

        service.uploadToPlantSoort(plantId, file, null, true, null, null, null);

        assertFalse(bestaande.isHoofdfoto());

        // 1x save bestaande (hoofdfoto=false) + 1x save nieuwe
        verify(fotoRepository, times(2)).save(any(Foto.class));
        verify(fotoRepository).findFirstByPlantSoortIdAndHoofdfotoTrue(plantId);
        verify(storageService).store(eq(plantId), any());
        verify(plantSoortRepository).findById(plantId);
        verifyNoMoreInteractions(fotoRepository, plantSoortRepository, storageService);
    }

    @DisplayName("uploadToPlantSoort() gooit ResourceNotFoundException als plantsoort niet bestaat")
    @Test
    void uploadToPlantSoort_throws_whenPlantMissing() {
        when(plantSoortRepository.findById(404L)).thenReturn(Optional.empty());

        MockMultipartFile file = new MockMultipartFile(
                "file", "test.jpg", "image/jpeg", new byte[]{1}
        );

        assertThrows(ResourceNotFoundException.class, () ->
                service.uploadToPlantSoort(404L, file, null, false, null, null, null)
        );

        verify(plantSoortRepository).findById(404L);
        verifyNoInteractions(fotoRepository, storageService);
    }

    @DisplayName("deleteFoto() verwijdert bestand + db-record als foto bestaat")
    @Test
    void deleteFoto_deletesFileAndDb() {
        Long plantId = 10L;
        Long fotoId = 5L;

        Foto foto = Foto.builder()
                .id(fotoId)
                .storagePath("plantsoorten/10/beuk-1.jpg")
                .build();

        when(fotoRepository.findByIdAndPlantSoortId(fotoId, plantId))
                .thenReturn(Optional.of(foto));

        service.deleteFoto(plantId, fotoId);

        verify(fotoRepository).findByIdAndPlantSoortId(fotoId, plantId);
        verify(storageService).delete("plantsoorten/10/beuk-1.jpg");
        verify(fotoRepository).delete(foto);
        verifyNoMoreInteractions(fotoRepository, plantSoortRepository, storageService);
    }

    @DisplayName("deleteFoto() gooit ResourceNotFoundException als foto niet bestaat")
    @Test
    void deleteFoto_throws_whenFotoMissing() {
        when(fotoRepository.findByIdAndPlantSoortId(5L, 10L))
                .thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> service.deleteFoto(10L, 5L));

        verify(fotoRepository).findByIdAndPlantSoortId(5L, 10L);
        verifyNoMoreInteractions(fotoRepository);
        verifyNoInteractions(storageService, plantSoortRepository);
    }
}
