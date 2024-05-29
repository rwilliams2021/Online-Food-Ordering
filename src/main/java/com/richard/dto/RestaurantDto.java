package com.richard.dto;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Data;

import java.util.List;

@Data
@Embeddable
public class RestaurantDto {
    
    private Long id;
    private String title;
    @Column(length = 1000)
    private List<String> images;
    private String description;
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RestaurantDto that = (RestaurantDto) o;
        return id.equals(that.id);
    }
    
    public int hashCode() {
        return id.hashCode();
    }
}
