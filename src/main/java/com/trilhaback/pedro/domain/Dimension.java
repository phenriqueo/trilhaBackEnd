package com.trilhaback.pedro.domain;


import lombok.*;

import java.sql.Array;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor

public class Dimension {

    private Long id;
    private String name;
    private DataType datatype;
    //rivate Long sonId;
    //private List<Dimension> parentDimensions = new ArrayList<>();
    //private List<DimensionContent> content = new ArrayList<>();
    public static Builder builder() {
        return new Builder();
    }
    public Dimension(String name, DataType dataType){
        this.name = name;
        this.datatype = dataType;
    }

    @Override
    public String toString() {
        return this.id + " " + this.name;
    }

    public static class Builder {
            private Long id;
            private String name;
            private DataType datatype;

        public Builder id(Long id) {
            this.id = id;
            return this;
        }

        public Builder name(String name){
            this.name = name;
            return this;
        }

        public Builder dataType(DataType dataType) {
            this.datatype = dataType;
            return this;
        }

        public Dimension build() {
            Dimension dimension = new Dimension(name, datatype);
            dimension.id = this.id;
            return dimension;
        }
    }
}
