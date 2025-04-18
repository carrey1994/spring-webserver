package com.jameswu.demo.service;

import com.jameswu.demo.model.entity.ActiveToken;
import com.jameswu.demo.model.entity.Coupon;
import com.jameswu.demo.model.entity.GcProfileTreeNode;
import com.jameswu.demo.model.entity.GcUser;
import com.jameswu.demo.model.entity.UserProfile;
import com.jameswu.demo.model.enums.UserRole;
import com.jameswu.demo.model.enums.UserStatus;
import com.jameswu.demo.model.payload.LoginPayload;
import com.jameswu.demo.model.payload.RegisterPayload;
import com.jameswu.demo.model.resultmapping.GcProfileLevel;
import com.jameswu.demo.repository.CouponRepository;
import com.jameswu.demo.repository.EntityManagerHelper;
import com.jameswu.demo.repository.TokenRepository;
import com.jameswu.demo.repository.UserRepository;
import com.jameswu.demo.utils.GzTexts;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class UserManagementService {

	private final EntityManagerHelper entityManagerHelper;
	private final UserRepository userRepository;
	private final BCryptPasswordEncoder bCryptPasswordEncoder;
	private final TokenRepository tokenRepository;
	private final CouponRepository couponRepository;
	private final JwtService jwtService;
	private final AuthenticationManager authenticationManager;

	@Transactional
	public UserProfile register(RegisterPayload registerPayload) {
		userRepository.findByUsernameIgnoreCase(registerPayload.username()).ifPresent(gcUser -> {
			throw new IllegalArgumentException(GzTexts.USER_ALREADY_EXISTS);
		});
		if (registerPayload.isRecommenderExists()) {
			userRepository.findById(registerPayload.recommenderId()).ifPresentOrElse(action -> {}, () -> {
				throw new IllegalArgumentException(GzTexts.USER_NOT_FOUND);
			});
		}

		UserProfile userProfile = UserProfile.builder()
				.email(registerPayload.email())
				.address(registerPayload.address())
				.nickname(registerPayload.nickname())
				.enrollmentDate(Instant.now())
				.recommenderId(registerPayload.recommenderId())
				.build();

		GcUser newUser = GcUser.builder()
				.username(registerPayload.username())
				.password(bCryptPasswordEncoder.encode(registerPayload.password()))
				.userRole(UserRole.USER)
				.userStatus(UserStatus.INACTIVE)
				.profile(userProfile)
				.build();

		newUser = userRepository.save(newUser);
		tokenRepository.save(ActiveToken.userWithDefaultToken(newUser));
		return newUser.getProfile();
	}

	@Transactional
	public UserProfile activeUser(String token) {
		ActiveToken activeToken = tokenRepository
				.findByToken(token)
				.orElseThrow(() -> new IllegalArgumentException("Active token invalid"));
		if (!activeToken.validateToken()) {
			throw new IllegalArgumentException("Active token invalid");
		}
		GcUser user = activeToken.getUser();
		user.setUserStatus(UserStatus.ACTIVE);
		tokenRepository.delete(activeToken);
		Coupon newUserCouponGift =
				Coupon.percentageCoupon(BigDecimal.valueOf(0.9), "new user coupon gift", user, UUID.randomUUID());
		couponRepository.save(newUserCouponGift);
		return userRepository.save(activeToken.getUser()).getProfile();
	}

	public Map<String, String> login(LoginPayload payload) {
		Authentication authentication = authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(payload.username(), payload.password()));
		String jwt = jwtService.generateToken((GcUser) authentication.getPrincipal());
		return Map.of(GzTexts.ACCESS_TOKEN, jwt);
	}

	public Page<UserProfile> activeUsers(Pageable pageable) {
		/* Don't use user repo calling findAll to get user profiles, it makes n+1 queries. */
		return userRepository.findByUserStatus(UserStatus.ACTIVE, pageable).map(GcUser::getProfile);
	}

	public GcProfileTreeNode searchingDiagram(int userId) {
		return queryByUserIdAndLevel(userId, 4);
	}

	public GcProfileTreeNode searchingChildren(int userId) {
		return queryByUserIdAndLevel(userId, 2);
	}

	private GcProfileTreeNode queryByUserIdAndLevel(int userId, int level) {
		List<GcProfileLevel> gcProfileLevelList = entityManagerHelper.queryChildren(userId, level);
		if (gcProfileLevelList.size() == 1) {
			// no any child, return user only.
			return new GcProfileTreeNode(gcProfileLevelList.get(0).toUserProfile(), new ArrayList<>());
		}
		return mappingChildren(gcProfileLevelList, userId);
	}

	private GcProfileTreeNode mappingChildren(List<GcProfileLevel> gcProfileLevelList, int userId) {
		GcProfileLevel rootProfileLevel = gcProfileLevelList.stream()
				.filter(profileLevel -> profileLevel.userId() == userId)
				.toList()
				.get(0);
		Map<Integer, List<GcProfileLevel>> collect = gcProfileLevelList.stream()
				.filter(u -> u.toUserProfile().isRecommenderExists())
				.collect(Collectors.groupingBy(GcProfileLevel::recommenderId));
		List<GcProfileTreeNode> children = collect.get(userId).stream()
				.map(e -> new GcProfileTreeNode(e.toUserProfile(), new ArrayList<>()))
				.toList();
		GcProfileTreeNode rootNode = new GcProfileTreeNode(rootProfileLevel.toUserProfile(), children);
		createTreeDiagram(collect, rootNode);
		return rootNode;
	}

	private void createTreeDiagram(Map<Integer, List<GcProfileLevel>> collect, GcProfileTreeNode node) {
		if (collect.get(node.getUserProfile().getUserId()) == null) {
			return;
		}
		List<GcProfileTreeNode> children = collect.get(node.getUserProfile().getUserId()).stream()
				.map(e -> new GcProfileTreeNode(e.toUserProfile(), new ArrayList<>()))
				.toList();
		node.setChildrenProfiles(children);
		for (GcProfileTreeNode child : children) {
			createTreeDiagram(collect, child);
		}
	}
}
