package spring.security.demo.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.GrantedAuthorityImpl;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import spring.security.demo.dao.UserDao;
import spring.security.demo.domain.User;

public class CustomUserDetailsService implements UserDetailsService {

	protected static Logger logger = Logger.getLogger("service");

	private UserDao userDAO = new UserDao();

	public UserDetails loadUserByUsername(String username)
			throws UsernameNotFoundException {
		UserDetails userDetails = null;

		System.out.println("sddfsdf");
		try {

			// 鎼滅储鏁版嵁搴撲互鍖归厤鐢ㄦ埛鐧诲綍鍚�.
			// 鎴戜滑鍙互閫氳繃dao浣跨敤JDBC鏉ヨ闂暟鎹簱
			User user = userDAO.getDatabase(username);

			// Populate the Spring User object with details from the dbUser
			// Here we just pass the username, password, and access level
			// getAuthorities() will translate the access level to the correct
			// role type

			userDetails = new org.springframework.security.core.userdetails.User(
					user.getUsername(), user.getPassword().toLowerCase(), true,
					true, true, true, getAuthorities(user.getAccess()));

		} catch (Exception e) {
			logger.error("Error in retrieving user");
			throw new UsernameNotFoundException("Error in retrieving user");
		}

		return userDetails;
	}

	/**
	 * 鑾峰緱璁块棶瑙掕壊鏉冮檺
	 * 
	 * @param access
	 * @return
	 */
	public Collection<GrantedAuthority> getAuthorities(Integer access) {

		List<GrantedAuthority> authList = new ArrayList<GrantedAuthority>(2);

		// 鎵�鏈夌殑鐢ㄦ埛榛樿鎷ユ湁ROLE_USER鏉冮檺
		logger.debug("Grant ROLE_USER to this user");
		authList.add(new GrantedAuthorityImpl("ROLE_USER"));

		// 濡傛灉鍙傛暟access涓�1.鍒欐嫢鏈塕OLE_ADMIN鏉冮檺
		if (access.compareTo(1) == 0) {
			logger.debug("Grant ROLE_ADMIN to this user");
			authList.add(new GrantedAuthorityImpl("ROLE_ADMIN"));
		}

		return authList;
	}

}
