package jp.profield.chat.model;

import java.util.Date;
import java.util.Objects;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 * A UserManagement.
 * 
 * @author Ranjith
 * @sincer 26-Nov-2018
 * 
 * @version 1.0
 */
@Document(collection = "user")
public class UserManagement {


	@Id
	private String id;

	@Field("first_name")
	private String firstName;

	@Field("last_name")
	private String lastName;

	@Field("user_name")
	private String userName;

	@Field("email")
	private String email;

	@Field("gender")
	private String gender;

	@Field
	private Role role;

	@Field("p_word")
	private String password;

	@Field("profile_image-url")
	private String profileImageUrl;

	@Field("profile_image")
	private byte[] profileImage;

	@Field("profile_image_thumbnail")
	private byte[] profileImageThumbnail;

	@Field("resetpwd_token_generated_on")
	private Date resetpwdLinkGeneratedOn;

	@Field("tocken")
	private long token;
	
	@Field("login_on")
	private Date loginAt;

	@Field("logout_on")
	private Date logoutAt;

	@Field("is_online")
	private boolean isOnline =false;

	@Field("is_deleted")
	private boolean isDeleted = false;

	@Field("is_activated")
	private boolean activated = false;

	@Field("deleted_by")
	private String deletedBy;

	@Field("deleted_on")
	private Date deletedOn;

	@Field("created_by")
	private String createdBy;

	@Field("created_on")
	private Date createdOn;

	@Field("unique_key")
	private Long ukMessageKey;

	@DBRef
	private Attachment attachment;

	public UserManagement() {
		super();
	}

	
	
	public UserManagement(String id, String firstName, String lastName,
			String userName, String email, String gender, Role role,
			String password, String profileImageUrl, byte[] profileImage,
			byte[] profileImageThumbnail, Date resetpwdLinkGeneratedOn,
			long token, Date loginAt, Date logoutAt, boolean isOnline,
			boolean isDeleted, boolean activated, String deletedBy,
			Date deletedOn, String createdBy, Date createdOn,
			Long ukMessageKey, Attachment attachment) {
		super();
		this.id = id;
		this.firstName = firstName;
		this.lastName = lastName;
		this.userName = userName;
		this.email = email;
		this.gender = gender;
		this.role = role;
		this.password = password;
		this.profileImageUrl = profileImageUrl;
		this.profileImage = profileImage;
		this.profileImageThumbnail = profileImageThumbnail;
		this.resetpwdLinkGeneratedOn = resetpwdLinkGeneratedOn;
		this.token = token;
		this.loginAt = loginAt;
		this.logoutAt = logoutAt;
		this.isOnline = isOnline;
		this.isDeleted = isDeleted;
		this.activated = activated;
		this.deletedBy = deletedBy;
		this.deletedOn = deletedOn;
		this.createdBy = createdBy;
		this.createdOn = createdOn;
		this.ukMessageKey = ukMessageKey;
		this.attachment = attachment;
	}



	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getProfileImageUrl() {
		return profileImageUrl;
	}

	public void setProfileImageUrl(String profileImageUrl) {
		this.profileImageUrl = profileImageUrl;
	}

	public byte[] getProfileImage() {
		return profileImage;
	}

	public void setProfileImage(byte[] profileImage) {
		this.profileImage = profileImage;
	}

	public byte[] getProfileImageThumbnail() {
		return profileImageThumbnail;
	}

	public void setProfileImageThumbnail(byte[] profileImageThumbnail) {
		this.profileImageThumbnail = profileImageThumbnail;
	}

	public Date getResetpwdLinkGeneratedOn() {
		return resetpwdLinkGeneratedOn;
	}

	public void setResetpwdLinkGeneratedOn(Date resetpwdLinkGeneratedOn) {
		this.resetpwdLinkGeneratedOn = resetpwdLinkGeneratedOn;
	}

	public Date getLoginAt() {
		return loginAt;
	}

	public void setLoginAt(Date loginAt) {
		this.loginAt = loginAt;
	}

	public Date getLogoutAt() {
		return logoutAt;
	}

	public void setLogoutAt(Date logoutAt) {
		this.logoutAt = logoutAt;
	}

	public boolean getIsOnline() {
		return isOnline;
	}

	public void setIsOnline(boolean isOnline) {
		this.isOnline = isOnline;
	}

	public boolean getIsDeleted() {
		return isDeleted;
	}

	public void setIsDeleted(boolean isDeleted) {
		this.isDeleted = isDeleted;
	}

	public boolean isActivated() {
		return activated;
	}

	public void setActivated(boolean activated) {
		this.activated = activated;
	}

	public String getDeletedBy() {
		return deletedBy;
	}

	public void setDeletedBy(String deletedBy) {
		this.deletedBy = deletedBy;
	}

	public Date getDeletedOn() {
		return deletedOn;
	}

	public void setDeletedOn(Date deletedOn) {
		this.deletedOn = deletedOn;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public Date getCreatedOn() {
		return createdOn;
	}

	public void setCreatedOn(Date createdOn) {
		this.createdOn = createdOn;
	}

	public Long getUkMessageKey() {
		return ukMessageKey;
	}

	public void setUkMessageKey(Long ukMessageKey) {
		this.ukMessageKey = ukMessageKey;
	}

	public Attachment getAttachment() {
		return attachment;
	}

	public void setAttachment(Attachment attachment) {
		this.attachment = attachment;
	}

	
	
	public long getToken() {
		return token;
	}

	public void setToken(long token) {
		this.token = token;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		UserManagement userManagement = (UserManagement) o;
		if (userManagement.id == null || id == null) {
			return false;
		}
		if (Objects.equals(id, userManagement.id)) {
			return true;
		}
		return Objects.equals(id, userManagement.id);
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(id);
	}
	


}
