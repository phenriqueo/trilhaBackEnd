package com.trilhaback.pedro.domain.dimension;

import java.util.List;

public interface DimensionContentRepository{

    void insert (DimensionContent dimensionContent, Dimension dimension);
    DimensionContent update (DimensionContent dimensionContent, Dimension dimension);
    DimensionContent findById (Long dimensionId, String dimensionContentId);
    List<DimensionContent> findAll (Long dimensionId);
    void deleteById (Long dimensionId, String dimensionContentId);



}
