package practice.mapper;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import practice.entity.Category;
import practice.entity.Product;
import practice.vo.category.CategoryVo;
import practice.vo.product.ProductVo;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2023-11-01T00:53:51+0800",
    comments = "version: 1.5.3.Final, compiler: javac, environment: Java 17.0.8 (Amazon.com Inc.)"
)
@Component
public class CategoryMapperImpl implements CategoryMapper {

    @Autowired
    private ProductMapper productMapper;

    @Override
    public List<CategoryVo> toVo(List<Category> entities) {
        if ( entities == null ) {
            return null;
        }

        List<CategoryVo> list = new ArrayList<CategoryVo>( entities.size() );
        for ( Category category : entities ) {
            list.add( toVo( category ) );
        }

        return list;
    }

    @Override
    public CategoryVo toVo(Category entity) {
        if ( entity == null ) {
            return null;
        }

        CategoryVo categoryVo = new CategoryVo();

        categoryVo.setProducts( productListToProductVoList( entity.getProducts() ) );
        categoryVo.setCategoryId( entity.getCategoryId() );
        categoryVo.setCategoryName( entity.getCategoryName() );
        categoryVo.setCategoryIcon( entity.getCategoryIcon() );
        categoryVo.setCategorySlogan( entity.getCategorySlogan() );
        categoryVo.setCategoryPic( entity.getCategoryPic() );
        categoryVo.setChildCategories( toVo( entity.getChildCategories() ) );

        return categoryVo;
    }

    protected List<ProductVo> productListToProductVoList(List<Product> list) {
        if ( list == null ) {
            return null;
        }

        List<ProductVo> list1 = new ArrayList<ProductVo>( list.size() );
        for ( Product product : list ) {
            list1.add( productMapper.toVoWithOutProductSku( product ) );
        }

        return list1;
    }
}
