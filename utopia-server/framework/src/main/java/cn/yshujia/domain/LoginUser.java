package cn.yshujia.domain;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author yshujia
 * @create 2024/4/23
 * @description 登录用户
 */

@Data
@Component
@NoArgsConstructor
public class LoginUser implements UserDetails {
	
	private Long userId;
	
	private String username;
	
	private String password;
	
	private String token;
	
	private Long loginTime;
	
	private Long roleId;
	
	private List<String> permission;
	
	private Boolean admin;
	
	public LoginUser(Long userId, String username, String password, Long roleId, Boolean admin, List<String> permission) {
		this.userId = userId;
		this.username = username;
		this.password = password;
		this.roleId = roleId;
		this.admin = admin;
		this.permission = permission;
	}
	
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		Set<GrantedAuthority> set = new HashSet<>();
		if (this.permission == null) {
			return set;
		}
		for (String s : this.permission) {
			set.add(new SimpleGrantedAuthority(s));
		}
		return set;
	}
	
	public Set<String> getPermission() {
		return new HashSet<>(this.permission);
	}
	
	@Override
	public String getPassword() {
		return this.password;
	}
	
	@Override
	public String getUsername() {
		return this.username;
	}
	
	@Override
	public boolean isAccountNonExpired() {
		return true;
	}
	
	@Override
	public boolean isAccountNonLocked() {
		return true;
	}
	
	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}
	
	@Override
	public boolean isEnabled() {
		return true;
	}
	
}