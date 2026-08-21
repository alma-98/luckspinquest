package com.luckspinquest.entity;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(
        name = "user_profiles",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "user_profiles_user_id_key",
                        columnNames = "user_id"
                )
        }
)
public class UserProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "profile_id")
    private Long profileId;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "user_id",
            nullable = false,
            unique = true,
            foreignKey = @ForeignKey(name = "fk_user_profiles_user")
    )
    private User user;

    @Column(name = "profile_display_name", length = 100)
    private String profileDisplayName;

    @Column(name = "profile_avatar_url", columnDefinition = "TEXT")
    private String profileAvatarUrl;

    @Column(name = "profile_gender", length = 20)
    private String profileGender;

    @Column(name = "profile_birth_date")
    private LocalDate profileBirthDate;

    @Column(name = "profile_country", length = 100)
    private String profileCountry;

    @Column(name = "profile_city", length = 100)
    private String profileCity;

    @Column(name = "profile_language", length = 10)
    private String profileLanguage;

    @Column(name = "profile_timezone", length = 50)
    private String profileTimezone;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    public Long getProfileId() {
        return profileId;
    }

    public void setProfileId(Long profileId) {
        this.profileId = profileId;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getProfileDisplayName() {
        return profileDisplayName;
    }

    public void setProfileDisplayName(String profileDisplayName) {
        this.profileDisplayName = profileDisplayName;
    }

    public String getProfileAvatarUrl() {
        return profileAvatarUrl;
    }

    public void setProfileAvatarUrl(String profileAvatarUrl) {
        this.profileAvatarUrl = profileAvatarUrl;
    }

    public String getProfileGender() {
        return profileGender;
    }

    public void setProfileGender(String profileGender) {
        this.profileGender = profileGender;
    }

    public LocalDate getProfileBirthDate() {
        return profileBirthDate;
    }

    public void setProfileBirthDate(LocalDate profileBirthDate) {
        this.profileBirthDate = profileBirthDate;
    }

    public String getProfileCountry() {
        return profileCountry;
    }

    public void setProfileCountry(String profileCountry) {
        this.profileCountry = profileCountry;
    }

    public String getProfileCity() {
        return profileCity;
    }

    public void setProfileCity(String profileCity) {
        this.profileCity = profileCity;
    }

    public String getProfileLanguage() {
        return profileLanguage;
    }

    public void setProfileLanguage(String profileLanguage) {
        this.profileLanguage = profileLanguage;
    }

    public String getProfileTimezone() {
        return profileTimezone;
    }

    public void setProfileTimezone(String profileTimezone) {
        this.profileTimezone = profileTimezone;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}
