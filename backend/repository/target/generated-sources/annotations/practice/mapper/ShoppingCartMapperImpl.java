package practice.mapper;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;
import practice.entity.ProductSku;
import practice.entity.ShoppingCart;
import practice.vo.shoppingcart.ShoppingCartItemVo;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2023-11-01T00:53:51+0800",
    comments = "version: 1.5.3.Final, compiler: javac, environment: Java 17.0.8 (Amazon.com Inc.)"
)
@Component
public class ShoppingCartMapperImpl implements ShoppingCartMapper {

    @Override
    public ShoppingCartItemVo toShoppingCartItemVo(ShoppingCart entity) {
        if ( entity == null ) {
            return null;
        }

        ShoppingCartItemVo.ShoppingCartItemVoBuilder shoppingCartItemVo = ShoppingCartItemVo.builder();

        shoppingCartItemVo.sellPrice( entitySkuSellPrice( entity ) );
        shoppingCartItemVo.skuProps( entity.getSkuProps() );
        shoppingCartItemVo.cartNum( entity.getCartNum() );
        shoppingCartItemVo.cartId( entity.getCartId() );

        return shoppingCartItemVo.build();
    }

    @Override
    public List<ShoppingCartItemVo> toShoppingCartItemVo(List<ShoppingCart> entities) {
        if ( entities == null ) {
            return null;
        }

        List<ShoppingCartItemVo> list = new ArrayList<ShoppingCartItemVo>( entities.size() );
        for ( ShoppingCart shoppingCart : entities ) {
            list.add( toShoppingCartItemVo( shoppingCart ) );
        }

        return list;
    }

    private Integer entitySkuSellPrice(ShoppingCart shoppingCart) {
        if ( shoppingCart == null ) {
            return null;
        }
        ProductSku sku = shoppingCart.getSku();
        if ( sku == null ) {
            return null;
        }
        int sellPrice = sku.getSellPrice();
        return sellPrice;
    }
}
