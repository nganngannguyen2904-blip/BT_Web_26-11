package com.accounthub.entity;

import jakarta.persistence.*;

/**
 * Entity Category cho chức năng Quản lý danh mục sản phẩm.
 */
@Entity
@Table(name = "categories")
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false, length = 200)
    private String name;

    @Column(name = "images", length = 500)
    private String images;

    @Column(name = "status")
    private Integer status = 1; // 1: Active, 0: Inactive

    public Category() {
    }

    public Category(String name, String images, Integer status) {
        this.name = name;
        this.images = images;
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImages() {
        return images;
    }

    public void setImages(String images) {
        this.images = images;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}
