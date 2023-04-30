package com.kreuterkeule.StudentSorter.model;

import jakarta.persistence.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.*;

@Entity
@Table(name = "users")
public class UserEntity implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String username;
    private String password;
    private String role; // ADMIN oder USER
    private String priorityMate;
    private boolean bilingual;
    private String WP;
    @ElementCollection
    private Map<Integer, String> fiveMatesPriorityName;

    public Map<Integer, String> getBlacklistedMates() {
        return blacklistedMates;
    }

    public void setBlacklistedMates(Map<Integer, String> blacklistedMates) {
        this.blacklistedMates = blacklistedMates;
    }

    @ElementCollection
    private Map<Integer, String> blacklistedMates;
    private boolean accExpired;
    private boolean locked;
    private boolean credExpired;
    private boolean enabled;

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getPriorityMate() {
        return priorityMate;
    }

    public void setPriorityMate(String priorityMate) {
        this.priorityMate = priorityMate;
    }

    public Map<Integer, String> getFiveMatesPriorityName() {
        return fiveMatesPriorityName;
    }

    public void setFiveMatesPriorityName(Map<Integer, String> fiveMatesPriorityName) {
        this.fiveMatesPriorityName = fiveMatesPriorityName;
    }

    public boolean isAccExpired() {
        return accExpired;
    }

    public void setAccExpired(boolean accExpired) {
        this.accExpired = accExpired;
    }

    public boolean isLocked() {
        return locked;
    }

    public void setLocked(boolean locked) {
        this.locked = locked;
    }

    public boolean isCredExpired() {
        return credExpired;
    }

    public void setCredExpired(boolean credExpired) {
        this.credExpired = credExpired;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public void addToBlacklist(String username) {
        this.blacklistedMates.put(this.blacklistedMates.size() + 1, username);
    }

    public UserEntity() {
    }

    public UserEntity(String username, String password, String role) {
        this.username = username;
        this.password = password;
        this.role = role;
        this.bilingual = false;
        WP = "MINT"; //TODO: standard Wert, vielleicht nicht noetig?
        this.priorityMate = "";
        this.fiveMatesPriorityName = new HashMap<>();
        this.blacklistedMates = new HashMap<>();
    }

    public long getId() {
        return id;
    }

    @Override
    public boolean isAccountNonExpired() {
        return !this.accExpired;
    }

    @Override
    public boolean isAccountNonLocked() {
        return !this.locked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return !this.credExpired;
    }

    @Override
    public boolean isEnabled() {
        return this.enabled;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Set<GrantedAuthority> authorities = new HashSet<GrantedAuthority>();
        authorities.add(new SimpleGrantedAuthority(this.role));
        return authorities;
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.username;
    }

    public String getWP() {
        return WP;
    }

    public void setWP(String WP) {
        this.WP = WP;
    }

    public boolean isBilingual() {
        return bilingual;
    }

    public void setBilingual(boolean bilingual) {
        this.bilingual = bilingual;
    }

    public void deleteFromBlacklist(Integer id) {
        blacklistedMates.remove(blacklistedMates.get(id - 1)); // starts at 0 as always
    }
}
