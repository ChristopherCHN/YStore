package com.cy.store.entity;

import java.util.Objects;

/**
 * 省市区的数据实体类
 */
public class District extends BaseEntity{
    private Integer id; // 唯一标识
    private String parent; // 父级区域代号
    private String code; // 本身区划代码
    private String name;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getParent() {
        return parent;
    }

    public void setParent(String parent) {
        this.parent = parent;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        District district = (District) o;
        return Objects.equals(id, district.id) && Objects.equals(parent, district.parent) && Objects.equals(code, district.code) && Objects.equals(name, district.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), id, parent, code, name);
    }

    @Override
    public String toString() {
        return "District{" +
                "id=" + id +
                ", parent='" + parent + '\'' +
                ", code='" + code + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
