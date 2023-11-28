package com.awake.orm.entity;

import com.awake.orm.anno.EntityCache;
import com.awake.orm.anno.Id;
import com.awake.orm.anno.Persister;
import com.awake.orm.model.IEntity;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

/**
 * @version : 1.0
 * @ClassName: MapEntity
 * @Description: TODO
 * @Auther: awake
 * @Date: 2023/11/28 11:42
 **/

@EntityCache(persister = @Persister("time30s"))
@Data
public class MapEntity implements IEntity<Long> {
    @Id
    private long id;

    private Map<String, ItemBag> bagMap = new HashMap<>();

    private Map<String, Map<String, String>> baseMap = new HashMap<>();

    @Override
    public Long id() {
        return id;
    }
}
