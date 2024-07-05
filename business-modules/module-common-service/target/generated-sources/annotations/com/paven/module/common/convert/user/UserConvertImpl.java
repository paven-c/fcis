package com.paven.module.common.convert.user;

import com.paven.module.common.api.user.dto.UserRespDTO;
import com.paven.module.common.api.user.dto.UserSaveReqDTO;
import com.paven.module.common.controller.user.vo.user.UserSaveReqVO;
import com.paven.module.common.repository.pojo.user.User;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-07-05T16:40:03+0800",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 17.0.6 (Amazon.com Inc.)"
)
public class UserConvertImpl implements UserConvert {

    @Override
    public UserSaveReqVO convert(UserSaveReqDTO createReqDTO) {
        if ( createReqDTO == null ) {
            return null;
        }

        UserSaveReqVO userSaveReqVO = new UserSaveReqVO();

        userSaveReqVO.setId( createReqDTO.getId() );
        userSaveReqVO.setUsername( createReqDTO.getUsername() );
        userSaveReqVO.setNickname( createReqDTO.getNickname() );
        userSaveReqVO.setRemark( createReqDTO.getRemark() );
        userSaveReqVO.setDeptId( createReqDTO.getDeptId() );
        userSaveReqVO.setEmail( createReqDTO.getEmail() );
        userSaveReqVO.setMobile( createReqDTO.getMobile() );
        userSaveReqVO.setGender( createReqDTO.getGender() );
        userSaveReqVO.setAvatar( createReqDTO.getAvatar() );
        userSaveReqVO.setStatus( createReqDTO.getStatus() );
        userSaveReqVO.setPassword( createReqDTO.getPassword() );

        return userSaveReqVO;
    }

    @Override
    public List<UserRespDTO> convertDtoList(List<User> users) {
        if ( users == null ) {
            return null;
        }

        List<UserRespDTO> list = new ArrayList<UserRespDTO>( users.size() );
        for ( User user : users ) {
            list.add( userToUserRespDTO( user ) );
        }

        return list;
    }

    protected UserRespDTO userToUserRespDTO(User user) {
        if ( user == null ) {
            return null;
        }

        UserRespDTO userRespDTO = new UserRespDTO();

        userRespDTO.setId( user.getId() );
        userRespDTO.setUsername( user.getUsername() );
        userRespDTO.setNickname( user.getNickname() );
        userRespDTO.setDeptId( user.getDeptId() );
        userRespDTO.setMobile( user.getMobile() );
        userRespDTO.setGender( user.getGender() );
        userRespDTO.setAvatar( user.getAvatar() );
        userRespDTO.setStatus( user.getStatus() );
        userRespDTO.setLastLoginIp( user.getLastLoginIp() );
        userRespDTO.setCreateTime( user.getCreateTime() );

        return userRespDTO;
    }
}
