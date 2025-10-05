package gwan.zheng.core.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import gwan.zheng.core.model.entity.InviteCode;
import gwan.zheng.core.model.entity.User;
import gwan.zheng.core.model.repository.InviteCodeRepository;
import gwan.zheng.core.model.repository.UserRepository;
import gwan.zheng.core.utils.RandomCodeGenerator;
import gwan.zheng.springbootcommondemo.dto.ResultDto;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;


/**
 * @Author: 郑国荣
 * @Date: 2025-10-04-13:42
 * @Description:
 */
@Service
@AllArgsConstructor
public class InviteCodeService {
    private final InviteCodeRepository inviteCodeRepository;
    private final StringRedisTemplate redisTemplate;
    private final ObjectMapper objectMapper; // Jackson
    private static final String INVITE_CODE_KEY = "invite:code:";
//    private static final String INVITE_OWNER_KEY = "invite_owner";
    // 生成邀请码，长度 length，天数 daysValid
    private static final int DEFAULT_CODE_LENGTH = 8;
    private static final int DEFAULT_DAYS_VALID = 7;
    @Autowired
    private UserRepository userRepository;


    public List<InviteCode> getAndRefreshOwnerValidInviteCodes(User owner) {
        List<InviteCode> ownerInviteCodes = owner.getInviteCodes();

        // 统计未使用且未过期的邀请码
        List<InviteCode> validInviteCodes = new ArrayList<>();
        int activeCount = 0; // 当前有效邀请码总数（未使用 + 已使用，但总数不超过 5）
        List<InviteCode> expiredUnused = new ArrayList<>();

        for (InviteCode code : ownerInviteCodes) {
            if (!code.isUsed() && code.getExpireAt().isAfter(LocalDateTime.now())) {
                validInviteCodes.add(code);
                if(!redisTemplate.hasKey(INVITE_CODE_KEY + code.getCode())) {
                    // 如果 Redis 中没有该邀请码，重新存入 Redis
                    String redisKey = INVITE_CODE_KEY + code.getCode();
                    code.setOwnerId(owner.getId());
                    redisTemplate.opsForValue().set(redisKey, toJson(code), DEFAULT_DAYS_VALID, TimeUnit.DAYS);
                }
            } else if (!code.isUsed()) {
                // 未使用但已过期
                expiredUnused.add(code);
            }
            activeCount++;
        }

        // 当前总数不超过 5，计算需要生成的新邀请码数量
        int toGenerate = 5 - activeCount + expiredUnused.size();
        for (int i = 0; i < toGenerate; i++) {
            String codeStr = RandomCodeGenerator.generateOneCode(DEFAULT_CODE_LENGTH);
            InviteCode inviteCode = new InviteCode();
            inviteCode.setCode(codeStr);
            inviteCode.setOwner(owner);
            inviteCode.setExpireAt(LocalDateTime.now().plusDays(DEFAULT_DAYS_VALID));
            InviteCode saved = inviteCodeRepository.save(inviteCode);

            // 存入 Redis，每个邀请码单独管理
            String redisKey = INVITE_CODE_KEY + codeStr;
            redisTemplate.opsForValue().set(redisKey, toJson(saved), DEFAULT_DAYS_VALID, TimeUnit.DAYS);

            validInviteCodes.add(saved);
        }

        // 返回未使用且未过期的邀请码
        return validInviteCodes;
    }


    public ResultDto<InviteCode> registerWithInvite(String code, User newUser) {
        ResultDto<InviteCode> result = new ResultDto<>();
        String redisKey = INVITE_CODE_KEY + code;

        // 优先从 Redis 获取
        InviteCode inviteCode = null;
        String json = redisTemplate.opsForValue().get(redisKey);
        if (json != null) {
            inviteCode = fromJson(json, InviteCode.class);
        } else {
            result.addError("找不到邀请码,可能无效或已过期");
            return result;
        }

        // 检查是否已使用或过期
        if (inviteCode.isUsed() || inviteCode.getExpireAt().isBefore(LocalDateTime.now())) {
            result.addError("邀请码无效或已过期");
            return result;
        }

        Long ownerId = inviteCode.getOwnerId();
        User owner = userRepository.findById(ownerId).orElse(null);
        if (owner == null) {
            result.addError("邀请码无效，找不到拥有者");
            return result;
        }
        inviteCode.setOwner(owner);
        // 标记为已用
        inviteCode.setUsed(true);
        inviteCode.setInvitee(newUser);
        inviteCode.setUsedAt(LocalDateTime.now());
//        inviteCodeRepository.save(inviteCode);

        // 删除 Redis 中该邀请码，防止二次使用
//        redisTemplate.delete(redisKey);

        result.setSuccess(true);
        result.setData(inviteCode);
        result.setMessage("邀请码有效，注册成功！");
        return result;
    }

    public List<InviteCode> getOwnerInviteCodes(User owner) {
        return inviteCodeRepository.findAllByOwner(owner);
    }

    // 序列化
    private String toJson(Object obj) {
        try {
            return objectMapper.writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException("序列化失败", e);
        }
    }

    // 反序列化
    private <T> T fromJson(String json, Class<T> clazz) {
        try {
            return objectMapper.readValue(json, clazz);
        } catch (Exception e) {
            throw new RuntimeException("反序列化失败", e);
        }
    }

}
