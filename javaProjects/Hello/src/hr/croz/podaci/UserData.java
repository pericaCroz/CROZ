package hr.croz.podaci;

import java.util.List;

public class UserData {
	
	private String username;
	private String password;
	private List<String> roles;
	
	public UserData(String username, String password,
			List<String> roles) {
		super();
		this.username = username;
		this.password = password;
		this.roles = roles;
	}

	public String getUsername() {
		return username;
	}

	public String getPassword() {
		return password;
	}

	public List<String> getRoles() {
		return roles;
	}

}
