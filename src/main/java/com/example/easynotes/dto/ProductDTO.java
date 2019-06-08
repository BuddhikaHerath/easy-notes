package com.example.easynotes.dto;

import com.example.easynotes.dto.OrderProductDTO;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Lob;
import java.util.List;

public class ProductDTO {
    @Setter @Getter private long id;

    @Setter @Getter private String description;

    @Setter @Getter private String title;

    @Setter @Getter private int quantity;

    @Setter @Getter private double price;

    @Lob
    @Setter @Getter private byte[] image;

    @Setter @Getter private String imagePath;

    @Setter @Getter private String company;

    @Setter @Getter private List<OrderProductDTO> orderProductDTOList;
}
