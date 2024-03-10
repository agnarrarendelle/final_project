package practice.mapper;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;
import practice.entity.UserAddr;
import practice.vo.useraddr.UserAddrVo;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2023-11-01T00:53:51+0800",
    comments = "version: 1.5.3.Final, compiler: javac, environment: Java 17.0.8 (Amazon.com Inc.)"
)
@Component
public class UserAddrMapperImpl implements UserAddrMapper {

    @Override
    public UserAddrVo toUserAddrVo(UserAddr entity) {
        if ( entity == null ) {
            return null;
        }

        UserAddrVo userAddrVo = new UserAddrVo();

        userAddrVo.setProvince( entity.getProvince() );
        userAddrVo.setCity( entity.getCity() );
        userAddrVo.setArea( entity.getArea() );
        userAddrVo.setAddr( entity.getAddr() );
        userAddrVo.setReceiverMobile( entity.getReceiverMobile() );
        userAddrVo.setReceiverName( entity.getReceiverName() );
        userAddrVo.setCommonAddr( String.valueOf( entity.getCommonAddr() ) );

        return userAddrVo;
    }

    @Override
    public List<UserAddrVo> toUserAddrVo(List<UserAddr> entities) {
        if ( entities == null ) {
            return null;
        }

        List<UserAddrVo> list = new ArrayList<UserAddrVo>( entities.size() );
        for ( UserAddr userAddr : entities ) {
            list.add( toUserAddrVo( userAddr ) );
        }

        return list;
    }
}
