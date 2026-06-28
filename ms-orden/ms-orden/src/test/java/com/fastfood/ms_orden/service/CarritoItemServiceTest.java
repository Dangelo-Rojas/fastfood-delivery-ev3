package com.fastfood.ms_orden.service;

import com.fastfood.ms_orden.DTO.CarritoItemDTO;
import com.fastfood.ms_orden.DTO.CatalogoResponseDTO;
import com.fastfood.ms_orden.client.RestauranteClient;
import com.fastfood.ms_orden.model.CarritoItem;
import com.fastfood.ms_orden.repository.CarritoItemRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Pruebas unitarias - CarritoItemService")
class CarritoItemServiceTest {

    @Mock
    private CarritoItemRepository carritoItemRepository;

    @Mock
    private RestauranteClient restauranteClient;

    @InjectMocks
    private CarritoItemService carritoItemService;

    private CarritoItem item;
    private CatalogoResponseDTO catalogoMock;

    @BeforeEach
    void setUp() {
        item = new CarritoItem(1, 2, 5990.0, 10, 100);
        catalogoMock = new CatalogoResponseDTO();
        catalogoMock.setIdCatalogo(100);
        catalogoMock.setNombreCatalogo("Hamburguesa Clasica");
        catalogoMock.setPrecio(5990.0);
        catalogoMock.setDisponible(true);
        catalogoMock.setIdRestaurante(1);
    }

    @Test
    @DisplayName("obtenerTodos: retorna lista de DTOs cuando existen items")
    void obtenerTodos_conDatos_retornaListaDTOs() {
        CarritoItem i2 = new CarritoItem(2, 1, 3490.0, 10, 101);
        when(carritoItemRepository.findAll()).thenReturn(Arrays.asList(item, i2));
        List<CarritoItemDTO> resultado = carritoItemService.obtenerTodos();
        assertThat(resultado).hasSize(2);
        assertThat(resultado.get(0).getCantidad()).isEqualTo(2);
        verify(carritoItemRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("obtenerTodos: retorna lista vacia cuando no hay items")
    void obtenerTodos_sinDatos_retornaListaVacia() {
        when(carritoItemRepository.findAll()).thenReturn(Collections.emptyList());
        List<CarritoItemDTO> resultado = carritoItemService.obtenerTodos();
        assertThat(resultado).isEmpty();
        verify(carritoItemRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("obtenerPorCarrito: retorna lista de items del carrito indicado")
    void obtenerPorCarrito_existente_retornaListaDTOs() {
        CarritoItem i2 = new CarritoItem(2, 1, 3490.0, 10, 101);
        when(carritoItemRepository.findByIdCarrito(10)).thenReturn(Arrays.asList(item, i2));
        List<CarritoItemDTO> resultado = carritoItemService.obtenerPorCarrito(10);
        assertThat(resultado).hasSize(2);
        verify(carritoItemRepository, times(1)).findByIdCarrito(10);
    }

    @Test
    @DisplayName("buscarPorId: retorna DTO cuando el item existe")
    void buscarPorId_existente_retornaDTO() {
        when(carritoItemRepository.findById(1)).thenReturn(Optional.of(item));
        CarritoItemDTO resultado = carritoItemService.buscarPorId(1);
        assertThat(resultado).isNotNull();
        assertThat(resultado.getIdCarritoItem()).isEqualTo(1);
        verify(carritoItemRepository, times(1)).findById(1);
    }

    @Test
    @DisplayName("buscarPorId: lanza RuntimeException cuando el item no existe")
    void buscarPorId_noExistente_lanzaExcepcion() {
        when(carritoItemRepository.findById(99)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> carritoItemService.buscarPorId(99))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Item con ID 99 no encontrado");
        verify(carritoItemRepository, times(1)).findById(99);
    }

    @Test
    @DisplayName("guardar: valida catalogo en ms-restaurante y persiste el item")
    void guardar_catalogoExistente_persisteItem() {
        CarritoItem nuevo = new CarritoItem(null, 3, 7990.0, 11, 100);
        CarritoItem guardado = new CarritoItem(3, 3, 7990.0, 11, 100);
        when(restauranteClient.obtenerCatalogoPorId(100)).thenReturn(catalogoMock);
        when(carritoItemRepository.save(nuevo)).thenReturn(guardado);
        CarritoItemDTO resultado = carritoItemService.guardar(nuevo);
        assertThat(resultado).isNotNull();
        assertThat(resultado.getIdCarritoItem()).isEqualTo(3);
        verify(restauranteClient, times(1)).obtenerCatalogoPorId(100);
        verify(carritoItemRepository, times(1)).save(nuevo);
    }

    @Test
    @DisplayName("guardar: usa el precio del catalogo si el item no trae precio")
    void guardar_sinPrecio_usaPrecioDelCatalogo() {
        CarritoItem sinPrecio = new CarritoItem(null, 1, null, 11, 100);
        when(restauranteClient.obtenerCatalogoPorId(100)).thenReturn(catalogoMock);
        when(carritoItemRepository.save(any(CarritoItem.class))).thenAnswer(invocation -> invocation.getArgument(0));
        CarritoItemDTO resultado = carritoItemService.guardar(sinPrecio);
        assertThat(resultado.getPrecioUnitario()).isEqualTo(5990.0);
        verify(restauranteClient, times(1)).obtenerCatalogoPorId(100);
    }

    @Test
    @DisplayName("guardar: lanza RuntimeException cuando ms-restaurante no encuentra el catalogo")
    void guardar_catalogoNoExiste_lanzaExcepcion() {
        CarritoItem nuevo = new CarritoItem(null, 1, 5990.0, 11, 999);
        when(restauranteClient.obtenerCatalogoPorId(999))
                .thenThrow(new RuntimeException("Catalogo con ID 999 no encontrado en ms-restaurante"));
        assertThatThrownBy(() -> carritoItemService.guardar(nuevo))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Catalogo con ID 999 no encontrado");
        verify(restauranteClient, times(1)).obtenerCatalogoPorId(999);
        verify(carritoItemRepository, never()).save(any(CarritoItem.class));
    }

    @Test
    @DisplayName("actualizar: modifica los campos cuando el item existe")
    void actualizar_existente_modificaCampos() {
        CarritoItem datos = new CarritoItem(null, 5, 9990.0, 20, 200);
        when(carritoItemRepository.findById(1)).thenReturn(Optional.of(item));
        when(carritoItemRepository.save(any(CarritoItem.class))).thenReturn(item);
        CarritoItemDTO resultado = carritoItemService.actualizar(1, datos);
        assertThat(resultado).isNotNull();
        assertThat(resultado.getCantidad()).isEqualTo(5);
        verify(carritoItemRepository, times(1)).findById(1);
        verify(carritoItemRepository, times(1)).save(any(CarritoItem.class));
    }

    @Test
    @DisplayName("actualizar: lanza RuntimeException cuando el item no existe")
    void actualizar_noExistente_lanzaExcepcion() {
        CarritoItem datos = new CarritoItem(null, 5, 9990.0, 20, 200);
        when(carritoItemRepository.findById(99)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> carritoItemService.actualizar(99, datos))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Item con ID 99 no encontrado");
        verify(carritoItemRepository, times(1)).findById(99);
        verify(carritoItemRepository, never()).save(any(CarritoItem.class));
    }

    @Test
    @DisplayName("eliminar: retorna mensaje de confirmacion cuando el item existe")
    void eliminar_existente_retornaMensajeExitoso() {
        when(carritoItemRepository.findById(1)).thenReturn(Optional.of(item));
        doNothing().when(carritoItemRepository).delete(item);
        String resultado = carritoItemService.eliminar(1);
        assertThat(resultado).isEqualTo("Item con ID 1 eliminado exitosamente.");
        verify(carritoItemRepository, times(1)).findById(1);
        verify(carritoItemRepository, times(1)).delete(item);
    }

    @Test
    @DisplayName("eliminar: lanza RuntimeException cuando el item no existe")
    void eliminar_noExistente_lanzaExcepcion() {
        when(carritoItemRepository.findById(99)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> carritoItemService.eliminar(99))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Item con ID 99 no encontrado");
        verify(carritoItemRepository, times(1)).findById(99);
        verify(carritoItemRepository, never()).delete(any(CarritoItem.class));
    }
}