package com.github.ahmed_zein.ecommerce_backend.api.controller.user;

import com.github.ahmed_zein.ecommerce_backend.model.Address;
import com.github.ahmed_zein.ecommerce_backend.model.LocalUser;
import com.github.ahmed_zein.ecommerce_backend.model.dao.AddressDAO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController()
@RequestMapping("/users")
public class UserController {
    private final AddressDAO addressDAO;

    public UserController(AddressDAO addressDAO) {
        this.addressDAO = addressDAO;
    }

    @GetMapping("/{userId}/address")
    public ResponseEntity<List<Address>> getAddress(@PathVariable Long userId, @AuthenticationPrincipal LocalUser user) {
        if (userHasNoPermission(userId, user)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        var addressList = addressDAO.findByUser_Id(userId);
        return ResponseEntity.ok(addressList);
    }

    @PutMapping("/{userId}/address")
    public ResponseEntity<Address> putAddress(@AuthenticationPrincipal LocalUser user, @PathVariable Long userId, @RequestBody Address address) {
        if (userHasNoPermission(userId, user)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        address.setId(null);
        LocalUser refUser = new LocalUser();
        refUser.setId(userId);
        address.setUser(refUser);

        return ResponseEntity.ok(addressDAO.save(address));
    }

    @PatchMapping("/{userId}/address/{addressId}")
    public ResponseEntity<Address> patchAddress(@AuthenticationPrincipal LocalUser user,
                                                @PathVariable Long userId,
                                                @PathVariable Long addressId,
                                                @RequestBody Address address
    ) {
        if (userHasNoPermission(userId, user)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        if (!address.getId().equals(addressId)) {
            return ResponseEntity.badRequest().build();
        }
        Optional<Address> opOriginalAddress = addressDAO.findById(addressId);
        if (opOriginalAddress.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        var addressOwner = opOriginalAddress.get().getUser();
        if (!addressOwner.getId().equals(userId)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        address.setUser(addressOwner);
        Address updatedAddress = addressDAO.save(address);
        return ResponseEntity.ok(updatedAddress);
    }

    @DeleteMapping("/{userId}/address/{addressId}")
    public ResponseEntity<Address> deleteAddress(@AuthenticationPrincipal LocalUser user, @PathVariable Long userId, @PathVariable Long addressId) {
        if (userHasNoPermission(userId, user)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        Optional<Address> opAddress = addressDAO.findById(addressId);
        if (opAddress.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        var addressOwner = opAddress.get().getUser();
        if (!addressOwner.getId().equals(userId)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        addressDAO.delete(opAddress.get());
        return ResponseEntity.ok().build();
    }

    private boolean userHasNoPermission(Long userId, LocalUser user) {
        return !user.getId().equals(userId);
    }
}
