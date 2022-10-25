package com.wzh.gulimall.member.dao;

import com.wzh.gulimall.member.entity.MemberEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 会员
 * 
 * @author wzh
 * @email wzh@gmail.com
 * @date 2022-10-25 20:56:20
 */
@Mapper
public interface MemberDao extends BaseMapper<MemberEntity> {
	
}
