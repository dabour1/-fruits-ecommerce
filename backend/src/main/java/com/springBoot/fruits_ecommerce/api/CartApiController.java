package com.springBoot.fruits_ecommerce.api;

import com.springBoot.fruits_ecommerce.models.AddToCartRequest;
import com.springBoot.fruits_ecommerce.models.Cart;
import com.springBoot.fruits_ecommerce.models.CartDetailsResponse;
import com.springBoot.fruits_ecommerce.models.ErrorResponse;
import com.springBoot.fruits_ecommerce.services.CartService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.context.request.NativeWebRequest;

import javax.validation.constraints.*;
import javax.validation.Valid;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import javax.annotation.Generated;

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2024-10-26T04:53:21.057987600+03:00[Asia/Riyadh]", comments = "Generator version: 7.8.0")
@Controller
@RequestMapping("${openapi.cart.base-path:}")
public class CartApiController implements CartApi {
    @Autowired
    private CartService cartService;
    private final NativeWebRequest request;

    @Autowired
    public CartApiController(NativeWebRequest request) {
        this.request = request;
    }

    @Override
    public Optional<NativeWebRequest> getRequest() {
        return Optional.ofNullable(request);
    }

    @Override
    @PreAuthorize("hasRole('ROLE_CLIENT')")
    public ResponseEntity<CartDetailsResponse> getCartDetails(@RequestParam Long userId) {

        CartDetailsResponse cartDetails = cartService.getCartDetails(userId);
        return ResponseEntity.ok(cartDetails);
    }

    @Override
    @PreAuthorize("hasRole('ROLE_CLIENT')")
    // @PreAuthorize("@cartService.isCartOwner(authentication,
    // #addToCartRequest.userId)")
    public ResponseEntity<Cart> addProductToCart(
            @RequestBody AddToCartRequest addToCartRequest) {

        Cart cart = cartService.addProductToCart(
                addToCartRequest.getUserId(),
                addToCartRequest.getProductId(),
                addToCartRequest.getQuantity());

        return ResponseEntity.ok(cart);
    }

}
