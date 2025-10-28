package ru.kuznetsov.shop.gate.controller.business;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.kuznetsov.shop.represent.contract.business.StoreContract;
import ru.kuznetsov.shop.represent.dto.StockDto;
import ru.kuznetsov.shop.represent.dto.StoreDto;

import java.util.Collection;
import java.util.List;

@RestController
@RequestMapping("/store")
public class StoreController extends AbstractController<StoreDto, StoreContract> {
    protected StoreController(StoreContract contractService) {
        super(contractService);
    }
//
//    @Override
//    public ResponseEntity<List<StoreDto>> getAll() {
//        return super.getAll();
//    }

    @GetMapping()
    public ResponseEntity<List<StoreDto>> getAllStores(
            @RequestParam(required = false) Long id,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) Long addressId
    ) {
        return ResponseEntity.ok(contractService.getAll(id, name, addressId));
    }

    @GetMapping("/{id}/stock")
    public ResponseEntity<Collection<StockDto>> getAllByStoreId(@PathVariable long id) {
        return ResponseEntity.ok(contractService.getAllStockByStoreId(id));
    }
}
