package practice.mapper;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import practice.entity.Product;
import practice.entity.ProductSku;
import practice.vo.product.ProductVo;
import practice.vo.productsku.ProductSkuVo;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2023-11-01T00:53:51+0800",
    comments = "version: 1.5.3.Final, compiler: javac, environment: Java 17.0.8 (Amazon.com Inc.)"
)
@Component
public class ProductMapperImpl implements ProductMapper {

    @Autowired
    private ProductSkuMapper productSkuMapper;
    @Autowired
    private ProductImgMapper productImgMapper;

    @Override
    public ProductVo toVo(Product entity) {
        if ( entity == null ) {
            return null;
        }

        ProductVo productVo = new ProductVo();

        productVo.setProductId( entity.getProductId() );
        productVo.setProductName( entity.getProductName() );
        productVo.setSoldNum( entity.getSoldNum() );
        productVo.setProductImages( productImgMapper.toVo( entity.getProductImages() ) );
        productVo.setProductSkus( productSkuListToProductSkuVoList( entity.getProductSkus() ) );

        return productVo;
    }

    @Override
    public ProductVo toVoWithOutProductSku(Product entity) {
        if ( entity == null ) {
            return null;
        }

        ProductVo productVo = new ProductVo();

        productVo.setProductId( entity.getProductId() );
        productVo.setProductName( entity.getProductName() );
        productVo.setSoldNum( entity.getSoldNum() );
        productVo.setProductImages( productImgMapper.toVo( entity.getProductImages() ) );

        return productVo;
    }

    @Override
    public ProductVo toVoWithOutProductSkuAndProductImages(Product entity) {
        if ( entity == null ) {
            return null;
        }

        ProductVo productVo = new ProductVo();

        productVo.setProductId( entity.getProductId() );
        productVo.setProductName( entity.getProductName() );
        productVo.setSoldNum( entity.getSoldNum() );

        return productVo;
    }

    @Override
    public List<ProductVo> toVos(List<Product> entities) {
        if ( entities == null ) {
            return null;
        }

        List<ProductVo> list = new ArrayList<ProductVo>( entities.size() );
        for ( Product product : entities ) {
            list.add( toVo( product ) );
        }

        return list;
    }

    @Override
    public List<ProductVo> toVosWithOutProductSku(List<Product> entities) {
        if ( entities == null ) {
            return null;
        }

        List<ProductVo> list = new ArrayList<ProductVo>( entities.size() );
        for ( Product product : entities ) {
            list.add( toVoWithOutProductSku( product ) );
        }

        return list;
    }

    @Override
    public List<ProductVo> toVosWithOutProductSkuAndProductImages(List<Product> entities) {
        if ( entities == null ) {
            return null;
        }

        List<ProductVo> list = new ArrayList<ProductVo>( entities.size() );
        for ( Product product : entities ) {
            list.add( toVoWithOutProductSkuAndProductImages( product ) );
        }

        return list;
    }

    protected List<ProductSkuVo> productSkuListToProductSkuVoList(List<ProductSku> list) {
        if ( list == null ) {
            return null;
        }

        List<ProductSkuVo> list1 = new ArrayList<ProductSkuVo>( list.size() );
        for ( ProductSku productSku : list ) {
            list1.add( productSkuMapper.toVo( productSku ) );
        }

        return list1;
    }
}
