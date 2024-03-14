package com.ru.studybuddy.user;

import com.ru.studybuddy.course.Course;
import com.ru.studybuddy.department.Department;
import com.ru.studybuddy.group.Group;
import com.ru.studybuddy.speciality.Speciality;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "users")
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private String email;
    private String phone;
    private String password;

    private String name;
    private String imageUrl;

    @ManyToOne
    private Department department;

    @ManyToOne
    private Speciality speciality;

    @ManyToOne
    private Group group;

    @ManyToMany(mappedBy = "students")
    private List<Course> studiedCourses;

    @ManyToMany(mappedBy = "teachers")
    private List<Course> taughtCourses;

    @Enumerated(EnumType.STRING)
    private UserRole role;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role.name()));
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
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
