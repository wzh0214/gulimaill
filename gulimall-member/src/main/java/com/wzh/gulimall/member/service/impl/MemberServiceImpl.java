package com.wzh.gulimall.member.service.impl;

import com.wzh.gulimall.member.dao.MemberLevelDao;
import com.wzh.gulimall.member.entity.MemberLevelEntity;
import com.wzh.gulimall.member.exception.PhoneExistException;
import com.wzh.gulimall.member.exception.UsernameExistException;
import com.wzh.gulimall.member.vo.UserRegistVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wzh.common.utils.PageUtils;
import com.wzh.common.utils.Query;

import com.wzh.gulimall.member.dao.MemberDao;
import com.wzh.gulimall.member.entity.MemberEntity;
import com.wzh.gulimall.member.service.MemberService;


@Service("memberService")
public class MemberServiceImpl extends ServiceImpl<MemberDao, MemberEntity> implements MemberService {
    @Autowired
    private MemberLevelDao memberLevelDao;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<MemberEntity> page = this.page(
                new Query<MemberEntity>().getPage(params),
                new QueryWrapper<MemberEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public void regist(UserRegistVo vo) {
        MemberDao memberDao = this.baseMapper;
        MemberEntity entity = new MemberEntity();

        // 检查用户名和手机号是否唯一，为了让controller感知到，抛出自定义异常好被感知到是哪个
        checkUserNameUnique(vo.getUserName());
        checkPhoneUnique(vo.getPhone());

        entity.setUsername(vo.getUserName());
        entity.setMobile(vo.getPhone());

        // 设置默认等级
        MemberLevelEntity levelEntity = memberLevelDao.getDefaultLevel();
        entity.setLevelId(levelEntity.getId());

        // 密码加密存储，spring家的BCryptPasswordEncoder加密器
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String encode = passwordEncoder.encode(vo.getPassword());
        entity.setPassword(encode);

        // 其他默认信息


        // 保存
        memberDao.insert(entity);


    }

    @Override
    public void checkUserNameUnique(String userName) {
        MemberDao memberDao = this.baseMapper;
        Integer username = memberDao.selectCount(new QueryWrapper<MemberEntity>().eq("username", userName));
        if (username > 0) {
            throw new UsernameExistException();
        }
    }

    @Override
    public void checkPhoneUnique(String phone) {
        MemberDao memberDao = this.baseMapper;
        Integer mobile = memberDao.selectCount(new QueryWrapper<MemberEntity>().eq("mobile", phone));
        if (mobile > 0) {
            throw new PhoneExistException();
        }
    }


}